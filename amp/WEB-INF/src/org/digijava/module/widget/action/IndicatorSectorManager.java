package org.digijava.module.widget.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.widget.form.IndicatorSectorRegionForm;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.widget.util.WidgetUtil;

/**
 *
 * @author medea
 */
public class IndicatorSectorManager extends DispatchAction {
    
    public final static int RECORDS_PER_PAGE=15;
    public final static long ALL_REGIONS_SELECTED=-2l;
    public final static long NATIONAL_SELECTED=-3l;

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
        List<IndicatorSector> indSectorList=null;
        
        //filter indicator Sectors
        String view=request.getParameter("view");
        if(view!=null && view.equalsIgnoreCase("all")){
        	indSecForm.setKeyWord(null);
        	indSectorList=DbUtil.getIndicatorSectorsForCurrentPage(selectedPage,RECORDS_PER_PAGE,null);
        }else{
        	indSectorList=DbUtil.getIndicatorSectorsForCurrentPage(selectedPage,RECORDS_PER_PAGE,indSecForm.getKeyWord());
        }
        
        
        Collections.sort(indSectorList, new IndicatorUtil.HelperIndicatorSectorNameComparator());
        indSecForm.setIndSectList(indSectorList);
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
        ActionMessages errors = new ActionMessages();
        IndicatorSector indSec;
        List<AmpCategoryValueLocations> regions = new ArrayList<AmpCategoryValueLocations>();
        AmpSector selectedSector = indSecForm.getSector();
        Long indId = indSecForm.getSelIndicator();
        AmpIndicator selectedIndicator = IndicatorUtil.getIndicator(indId);
        AmpLocation locNational = null;


        // true if "all" is selected in region dropdown
        boolean allRegionsSelected = indSecForm.getSelRegionId().equals(ALL_REGIONS_SELECTED);
        //true if "national" is selected in region dropdown
        boolean nationalSelected = (allRegionsSelected ? false : indSecForm.getSelRegionId().equals(NATIONAL_SELECTED));

        if (allRegionsSelected) {
            regions.addAll(indSecForm.getRegions());
        } else {
            if (!nationalSelected) {
                regions.add(DynLocationManagerUtil.getLocation(indSecForm.getSelRegionId(),false));
            } else {
                //National means no region, zone, district are selected
                AmpCategoryValueLocations defCountry = DynLocationManagerUtil.getDefaultCountry();
                locNational = LocationUtil.getAmpLocationByCVLocation(defCountry.getId());
                if (locNational == null) {
                    locNational = new AmpLocation();
                    locNational.setCountry(FeaturesUtil.getDefaultCountryIso());
                    locNational.setLocation(defCountry);
                    LocationUtil.saveLocation(locNational);
                }
                
            }
        }


        if (indSecForm.getIndSectId() != null && indSecForm.getIndSectId() > 0) {
            // we are editing IndicatorSector

            indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
            indSec.setSector(selectedSector);
            indSec.setIndicator(selectedIndicator);

            if (allRegionsSelected) {

                /* if all is selected in the region dropdown 
                 * then we should create new sector indicator objects 
                 * for all regions except region which initially is assigned to IndicatorSector
                 * that is why we are removing it from list of regions
                 */

                regions.remove(indSec.getLocation().getLocation());
                IndicatorUtil.saveIndicatorConnection(indSec);
            } else if (nationalSelected) {
                indSec.setLocation(locNational);
                IndicatorUtil.saveIndicatorConnection(indSec);
                return viewAll(mapping, form, request, response);
            } else {
                AmpLocation ampLoc = getAmpLocation(regions.get(0));
                indSec.setLocation(ampLoc);

                if (!WidgetUtil.indicarorSectorExist(indSecForm.getSector().getAmpSectorId(), ampLoc.getAmpLocationId(), indId, indSecForm.getIndSectId())) {
                    IndicatorUtil.saveIndicatorConnection(indSec);
                    return viewAll(mapping, form, request, response);
                } else {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.widget.indicatorSector.indicatorSectorExists"));
                    saveErrors(request, errors);
                    return mapping.findForward("create");
                }

            }

        }
        if (locNational != null) {
            IndicatorSector ind = new IndicatorSector();
            ind.setLocation(locNational);
            ind.setSector(selectedSector);
            ind.setIndicator(selectedIndicator);
            IndicatorUtil.saveIndicatorConnection(ind);
        } else {
            for (AmpCategoryValueLocations  region : regions) {

                IndicatorSector ind = new IndicatorSector();
                AmpLocation ampLoc = getAmpLocation(region);
                ind.setLocation(ampLoc);
                ind.setSector(selectedSector);
                ind.setIndicator(selectedIndicator);

                if (!WidgetUtil.indicarorSectorExist(indSecForm.getSector().getAmpSectorId(), ampLoc.getAmpLocationId(), indId, null)) {
                    IndicatorUtil.saveIndicatorConnection(ind);
                } else {

                    if (allRegionsSelected) {
                        if (errors.size() == 0) {
                            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.widget.indicatorSector.indicatorSectorSkipped"));
                            saveErrors(request, errors);
                        }
                    } else {
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.widget.indicatorSector.indicatorSectorExists"));
                        saveErrors(request, errors);
                        return mapping.findForward("create");
                    }
                }

            }
        }
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
        //sort indicators by Name
        List<AmpIndicator> allIndicators=IndicatorUtil.getAllIndicators();
        if(allIndicators!=null && allIndicators.size()>0){
        	Collections.sort(allIndicators, new IndicatorUtil.IndicatorNameComparator());
        }
        indSecForm.setIndicators(allIndicators);
        Set<AmpCategoryValueLocations>  regions=
                			DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry() ;
        indSecForm.setRegions(new ArrayList(regions));
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
        Set<AmpCategoryValueLocations>  regions=
                			DynLocationManagerUtil.getLocationsOfTypeRegionOfDefCountry() ;
        indSecForm.setRegions(new ArrayList(regions));
        IndicatorSector indSec = IndicatorUtil.getConnectionToSector(indSecForm.getIndSectId());
        if (indSec.getIndicator() != null) {
            indSecForm.setSelIndicator(indSec.getIndicator().getIndicatorId());
        } else {
            indSecForm.setSelIndicator(null);
        }
        if (indSec.getLocation() == null) {
            indSecForm.setSelRegionId(null);
        } else {
          AmpCategoryValueLocations loc = indSec.getLocation().getLocation();
          String parentCategoryName=loc.getParentCategoryValue().getValue();
          if (CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY.getValueKey().equalsIgnoreCase(parentCategoryName) ){
				//National is selected
                    indSecForm.setSelRegionId(NATIONAL_SELECTED);
			}
          else{
               indSecForm.setSelRegionId(indSec.getLocation().getLocation().getId());
          }
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
        boolean widgetsExists = ChartWidgetUtil.isWidgetForIndicator(indSec);
        if (widgetsExists){
        	ActionMessages aes = new ActionMessages();
        	ActionMessage ae = new ActionMessage("error.aim.deleteIndicatorSector.widgetReferencesIt");
        	aes.add("Cannot delete indicator - widget is referenceing it",ae);
        	saveErrors(request, aes);
        }else{
            IndicatorUtil.removeConnection(indSec);
        }
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
        if (indSec.getLocation() != null && indSec.getLocation().getLocation() != null) {
            indSecForm.setSelRegionId(indSec.getLocation().getLocation().getId());
            indSecForm.setRegionName(indSec.getLocation().getLocation().getName());
        } else {
            indSecForm.setSelRegionId(null);
            indSecForm.setRegionName("");
        }
        indSecForm.setSector(indSec.getSector());
        if (indSec.getSector()!=null){
            indSecForm.setSectorName(indSec.getSector().getName());
        }else{
            indSecForm.setSectorName("");
        }

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
        indSecForm.setKeyWord(null);
        return viewAll(mapping, form, request, response);

    }

    private AmpLocation getAmpLocation(AmpCategoryValueLocations region) throws Exception {
        AmpLocation ampLoc = LocationUtil.getAmpLocationByCVLocation(region.getId());
        if (ampLoc == null) {
            ampLoc = new AmpLocation();
            ampLoc.setCountry(FeaturesUtil.getDefaultCountryIso());
            ampLoc.setRegionLocation(region);
            ampLoc.setLocation(region);
            LocationUtil.saveLocation(ampLoc);
        }
        return ampLoc;
    }
}
