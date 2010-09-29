package org.digijava.module.gis.action;

import java.util.ArrayList;
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
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.gis.form.GisDashboardForm;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.widget.util.ChartWidgetUtil;

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
        String baseCurr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if (baseCurr == null) {
            baseCurr = "USD";
        }
        //currently we are using base currency but in the future we may use value, selected from currency breakdown.
        gisForm.setSelectedCurrency(baseCurr);

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

		Collection<LabelValueBean> allDonors = new ArrayList <LabelValueBean>() ; 
		
		List allDbDonors = DbUtil.getFundingDonors();
		
		for (Object donorItem : allDbDonors) {
			Object[] donorNameId = (Object[]) donorItem;
			LabelValueBean donorComboItem = new LabelValueBean((String)donorNameId[0], ((Long)donorNameId[1]).toString());
			allDonors.add(donorComboItem);
		}
		
		gisForm.setAllDonorOrgs(allDonors);
			
		if (request.getParameter("public")!=null){
			request.getSession().setAttribute("publicuser", true);
		}
        return mapping.findForward("forward");
    }

    private void filterUsedSecData(List secData) {
        Iterator it = secData.iterator();

    }

}
