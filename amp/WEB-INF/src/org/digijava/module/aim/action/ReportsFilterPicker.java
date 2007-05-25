/**
 * 
 */
package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.form.ReportsFilterPickerForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author mihai
 *
 */
public class ReportsFilterPicker extends MultiAction {
	 
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ReportsFilterPickerForm filterForm=(ReportsFilterPickerForm) form;
		
		HttpSession httpSession = request.getSession();
		TeamMember teamMember = (TeamMember) httpSession
				.getAttribute("currentMember");

		Long ampTeamId = null;
		
		if(teamMember!=null) ampTeamId=teamMember.getTeamId();
		
		
		//create filter dropdowns		
		Collection currency = CurrencyUtil.getAmpCurrency();
		Collection allFisCalenders = DbUtil.getAllFisCalenders();
		ArrayList ampSectors = SectorUtil.getAmpSectors();
		ArrayList ampStatus = DbUtil.getAmpStatus();
		ArrayList donors;
		if(ampTeamId!=null) donors=DbUtil.getAmpDonors(ampTeamId); else donors=new ArrayList();
		Collection allIndicatorRisks = MEIndicatorsUtil.getAllIndicatorRisks();
		
		filterForm.setCurrencies(currency);
		filterForm.setCalendars(allFisCalenders);
		filterForm.setDonors(donors);
		filterForm.setRisks(allIndicatorRisks);
		filterForm.setSectors(ampSectors);
		filterForm.setStatuses(ampStatus);
		filterForm.setFromYears(new ArrayList());
		filterForm.setToYears(new ArrayList());
		
		
		for (int i = (1990 - Constants.FROM_YEAR_RANGE); i <= (1990 + Constants.TO_YEAR_RANGE); i++) {
			filterForm.getFromYears().add(new BeanWrapperImpl(new Long(i)));
			filterForm.getToYears().add(new BeanWrapperImpl(new Long(i)));		
		}
		
		return modeSelect(mapping,form,request,response);
	}

	public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getParameter("apply")!=null) return modeApply(mapping,form,request,response); 
		return mapping.findForward("forward");
	}
	
	/**
	 * generate a session based AmpARFilter object based on the form selections
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modeApply(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ReportsFilterPickerForm filterForm=(ReportsFilterPickerForm) form;
		AmpARFilter arf=new AmpARFilter();
		
		arf.setStatuses(Util.getSelectedObjects(filterForm.getStatuses(),filterForm.getSelectedStatuses()));
		arf.setSectors(Util.getSelectedObjects(filterForm.getSectors(),filterForm.getSelectedSectors()));
		arf.setCalendarType(new Integer(Util.getSelectedObject(filterForm.getCalendars(),filterForm.getCalendar()).toString()));
		
		
		return mapping.findForward("forward");
	}
	
	
}

