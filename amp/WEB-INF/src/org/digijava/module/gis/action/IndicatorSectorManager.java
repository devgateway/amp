package org.digijava.module.gis.action;

import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.gis.form.IndicatorSectorRegionForm;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.LocationUtil;
import java.util.ArrayList;
import java.util.List;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 *
 * @author medea
 */
public class IndicatorSectorManager extends DispatchAction {
    
    public final static int RECORDS_PER_PAGE=15;

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return cancel(mapping, form, request, response);
    }

    /*
     * forwards to the page where user can view all sectorIndicators 
     */
    public ActionForward viewAll(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        int pages;
        int allRecords=DbUtil.getAllIndicatorSectorsSize();
        if(allRecords==0){
            pages=1;
        }
        else{
             if(allRecords%RECORDS_PER_PAGE==0){
                 pages=allRecords/RECORDS_PER_PAGE;
             }
             else{
                 pages=allRecords/RECORDS_PER_PAGE+1;
             }
        }
        if(indSecForm.getSelectedPage()==0){
            indSecForm.setSelectedPage(1);
        }
        /* We need this wrapper array to navigate 
           to the previous page if all records were deleted on current page
         */
        Integer[] selectedPage={indSecForm.getSelectedPage()};
        indSecForm.setIndSectList(DbUtil.getIndicatorSectorsForCurrentPage(selectedPage,RECORDS_PER_PAGE));
        indSecForm.setSelectedPage( selectedPage[0]);
        indSecForm.setPages(pages);
        return mapping.findForward("forward");

    }
    
    /*
     * saves modified SectorIndicator;
     */
    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        IndicatorSector indSec;
        if (indSecForm.getIndSectId() == null||indSecForm.getIndSectId() ==0) {
            indSec = new IndicatorSector();
        } else {
            indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        }
        Long indId = indSecForm.getSelIndicator();
        // same thing as in activity saving process, if no location exists we will just create new object
        AmpLocation ampLoc = LocationUtil.getAmpLocation(FeaturesUtil.getDefaultCountryIso(), indSecForm.getSelRegionId(), null, null);
        if (ampLoc == null) {
            ampLoc = new AmpLocation();
            ampLoc.setCountry(FeaturesUtil.getDefaultCountryIso());
            ampLoc.setDgCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(FeaturesUtil.getDefaultCountryIso()));
            ampLoc.setAmpRegion(LocationUtil.getAmpRegion(indSecForm.getSelRegionId()));
            LocationUtil.saveLocation(ampLoc);
        }
        indSec.setSector(indSecForm.getSector());
        indSec.setIndicator(IndicatorUtil.getIndicator(indId));
        indSec.setLocation(ampLoc);
        IndicatorUtil.saveIndicatorConnection(indSec);
        return viewAll(mapping, form, request, response);

    }

    /*
     * selects sector
     */
    public ActionForward selectSector(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        HttpSession session = request.getSession();
        Object sectorObject = session.getAttribute(
                "sectorSelected");
        /* Don't be surprised , yes, you see ActivitySector class here :)
        Currently sector selector component is written in 
        Such way that it uses ActivitySector class*/

        ActivitySector selectedSector;
        Long sectorId;
        if (sectorObject instanceof ActivitySector) {
            selectedSector = (ActivitySector) sectorObject;
        } else {
            selectedSector = (ActivitySector) ((ArrayList) sectorObject).get(0);// If they select more then one sector we will use first one
        }
        if (selectedSector.getSubsectorLevel2Id() != -1) {
            sectorId = selectedSector.getSubsectorLevel2Id();
        } else {
            if (selectedSector.getSubsectorLevel1Id() != -1) {
                sectorId = selectedSector.getSubsectorLevel1Id();
            } else {
                sectorId = selectedSector.getSectorId();
            }
        }
        indSecForm.setSector(SectorUtil.getAmpSector(sectorId));
        session.removeAttribute("sectorSelected");
        return mapping.findForward("create");

    }
    /*
     * goes to page where you can create IndicatorSector object
     */

    public ActionForward create(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        indSecForm.setIndicators(IndicatorUtil.getAllIndicators());
        indSecForm.setRegions(new ArrayList(LocationUtil.getAllRegionsUnderCountry(FeaturesUtil.getDefaultCountryIso())));
        indSecForm.setSelIndicator(-1l);
        indSecForm.setSelRegionId(-1l);
        indSecForm.setSector(null);
        indSecForm.setIndSectId(null);
        return mapping.findForward("create");

    }
    /*
     * goes to page where you can edit IndicatorSector object
     */

    public ActionForward edit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        indSecForm.setIndicators(IndicatorUtil.getAllIndicators());
        indSecForm.setRegions(new ArrayList(LocationUtil.getAllRegionsUnderCountry(FeaturesUtil.getDefaultCountryIso())));
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        if (indSec.getIndicator() != null) {
            indSecForm.setSelIndicator(indSec.getIndicator().getIndicatorId());
        } else {
            indSecForm.setSelIndicator(null);
        }
        if (indSec.getLocation() != null && indSec.getLocation().getAmpRegion() != null) {
            indSecForm.setSelRegionId(indSec.getLocation().getAmpRegion().getAmpRegionId());
        } else {
            indSecForm.setSelRegionId(null);
        }
        indSecForm.setSector(indSec.getSector());
        return mapping.findForward("create");

    }
    /*
     * deletes IndicatorSector objectt
     */

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        IndicatorUtil.removeConnection(indSec);
        return viewAll(mapping, form, request, response);

    }

    /* loads page where you can add/edit values of the selected IndicatorSector*/
    public ActionForward addEditValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        List<AmpIndicatorValue> values = new ArrayList(indSec.getValues());
        indSecForm.setValues(values);
        if (indSec.getIndicator() != null) {
            indSecForm.setSelIndicator(indSec.getIndicator().getIndicatorId());
            indSecForm.setIndicatorName(indSec.getIndicator().getName());
        } else {
            indSecForm.setSelIndicator(null);
            indSecForm.setIndicatorName("");
        }
        if (indSec.getLocation() != null && indSec.getLocation().getAmpRegion() != null) {
            indSecForm.setSelRegionId(indSec.getLocation().getAmpRegion().getAmpRegionId());
            indSecForm.setRegionName(indSec.getLocation().getAmpRegion().getName());
        } else {
            indSecForm.setSelRegionId(null);
            indSecForm.setRegionName("");
        }
        indSecForm.setSector(indSec.getSector());
        indSecForm.setSectorName(indSec.getSector().getName());

        return mapping.findForward("addValue");

    }
    /* addes value to collection, 
     * note: changes are not saved until saveValue is not called 
     */

    public ActionForward addValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        if (indSecForm.getValues() == null) {
            indSecForm.setValues(new ArrayList());
        }
        AmpIndicatorValue newValue = new AmpIndicatorValue();
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        newValue.setIndicatorConnection(indSec);
        indSecForm.getValues().add(newValue);
        return mapping.findForward("addValue");

    }
    /* removes selected value from collection, 
     *note: changes are not saved until saveValue is not called
     */

    public ActionForward removeValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        indSecForm.getValues().remove(indSecForm.getDeleteValIndex());
        indSecForm.setDeleteValIndex(-1);
        return mapping.findForward("addValue");

    }

    /*
     * saves modified  values of the selected SectorIndicator;
     */
    public ActionForward saveValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        List<AmpIndicatorValue> newlyAddedValues = new ArrayList();
        List<AmpIndicatorValue> remainsValues = new ArrayList();
        for (AmpIndicatorValue val : indSecForm.getValues()) {
            if (val.getIndValId() != null) {
                AmpIndicatorValue oldValue = IndicatorUtil.getAmpIndicatorValue(val.getIndValId());
                oldValue.setValue(val.getValue());
                oldValue.setValueDate(val.getValueDate());
                oldValue.setValueType(val.getValueType());
                remainsValues.add(oldValue);
            } else {
                newlyAddedValues.add(val);
            }

        }

        indSec.getValues().retainAll(remainsValues);
        indSec.getValues().addAll(newlyAddedValues);
        IndicatorUtil.saveIndicatorConnection(indSec);
        return viewAll(mapping, form, request, response);

    }
    /*
     * cancel action, reset values;
     */

    public ActionForward cancel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IndicatorSectorRegionForm indSecForm = (IndicatorSectorRegionForm) form;
        indSecForm.setSelIndicator(-1l);
        indSecForm.setSelRegionId(-1l);
        indSecForm.setSector(null);
        indSecForm.setIndSectId(null);
        indSecForm.setValues(null);
        indSecForm.setDeleteValIndex(-1);
        indSecForm.setSectorName(null);
        indSecForm.setRegionName(null);
        indSecForm.setIndicatorName(null);
        indSecForm.setIndSectId(null);
        indSecForm.setSelectedPage(0);
        return viewAll(mapping, form, request, response);

    }
}
