package org.digijava.module.gis.action;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.gis.form.GisDashboardForm;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;

/**
 * GIS Dashboard renderer action.
 * @author Irakli Kobiashvili
 *
 */
public class ShowGisDashboard extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        GisDashboardForm gisForm = (GisDashboardForm) form;

        Collection sectors = DbUtil.getPrimaryToplevelSectors();

        /*
                List secData = DbUtil.getUsedSectors();

                filterUsedSecData(secData);

                List usedSectors = new ArrayList();
                Iterator it = secData.iterator();
                while (it.hasNext()) {
                    Object[] obj = (Object[])it.next();
                    SectorRefCount src = new SectorRefCount((AmpSector) obj[0], ((Integer)obj[1]).intValue());
                    usedSectors.add(src);
                }

         */
        gisForm.setSectorCollection(sectors);
        gisForm.setAvailYears(DbUtil.getAvailIndicatorYears());
        //dropdown(on toolbar) things
		Calendar cal=Calendar.getInstance();
		Integer year=new Integer(cal.get(java.util.Calendar.YEAR));  //get current year
		gisForm.setSelectedFromYear(new Integer(year-1).toString());
		gisForm.setSelectedToYear(year.toString());
		//fill from years' drop-down
		gisForm.setYearsFrom(ChartWidgetUtil.getYears(true));
		//fill to years' drop-down
		gisForm.setYearsTo(ChartWidgetUtil.getYears(false));



//                Collection ggg = FeaturesUtil.getAMPFieldsVisibility();
                AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) request.getSession().getAttribute("ampTreeVisibility");

                AmpFieldsVisibility locPercentageVisible = FeaturesUtil.getFieldVisibility("Regional Percentage");
//                String abcd = locPercentageVisible.getVisible();
                AmpFieldsVisibility locPercentageMandatory = FeaturesUtil.getFieldVisibility("Validate Mandatory Regional Percentage");



        return mapping.findForward("forward");
    }

    private void filterUsedSecData(List secData) {
        Iterator it = secData.iterator();

    }

}
