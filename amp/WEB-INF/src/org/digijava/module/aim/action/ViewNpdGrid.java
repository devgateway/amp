package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.form.NpdForm;
import org.digijava.module.aim.helper.ActivityItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.IndicatorGridRow;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.NpdUtil;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * Displays NPD data grid.
 * @author Irakli Kobiashvili
 * @see IndicatorGridRow
 *
 */
public class ViewNpdGrid extends Action {
	
	private static Logger logger = Logger.getLogger(ViewNpdGrid.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest reqest, HttpServletResponse sponse)	throws Exception {
		NpdForm npdForm = (NpdForm) form;
		if (npdForm.getProgramId() != null) {
			//load theme
			AmpTheme mainProg = ProgramUtil.getThemeObject(npdForm.getProgramId());
			//Retrieve theme indicators, and if second param true then all sub indicators.
			Set<IndicatorTheme> indicators = IndicatorUtil.getIndicators(mainProg, npdForm.getRecursive());
			//if there are indicators.
			if (indicators != null && indicators.size() > 0) {
				//convert set to list
				List<IndicatorTheme> indicatorsList = new ArrayList<IndicatorTheme>(indicators);
				//sort by indicator name.
				Collections.sort(indicatorsList,new IndicatorUtil.IndThemeIndciatorNameComparator());
				List<IndicatorGridRow> result = new ArrayList<IndicatorGridRow>(indicatorsList.size());
				//generate row objects from each connection for specified years.
				for (IndicatorTheme connection : indicatorsList) {
					IndicatorGridRow row = new IndicatorGridRow(connection,npdForm.getSelYears());
					result.add(row);
				}
				npdForm.setIndicators(result);
			}
			//activities
			HttpSession session = reqest.getSession();
			TeamMember tm = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
			String statusId=npdForm.getStatusId();
			String donorIds=npdForm.getDonorIds();
			Date fromYear = NpdUtil.yearToDate(npdForm.getStartYear(), false);
			Date toYear = NpdUtil.yearToDate(npdForm.getEndYear(), true);
			logger.debug("retriving activities");
			Collection<ActivityItem> activities = NpdUtil.getActivities(npdForm.getProgramId(),statusId, donorIds, fromYear,toYear, null, tm, null,null);
						
			
			AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
	        AmpCurrency curr=ampAppSettings.getCurrency();
	        String currCode="USD";
	        if(curr!=null){
	        	currCode=ampAppSettings.getCurrency().toString();
	        }
	        
	        BigDecimal actualSum = new BigDecimal(0);
	        BigDecimal actualDisbSum = new BigDecimal(0);
	        BigDecimal plannedCommitments=new BigDecimal(0);
			if (activities != null && activities.size() > 0) {
				List<ActivityItem> acts=new ArrayList<ActivityItem>(activities.size());
				for(ActivityItem activity : activities) {
	                //create helper bean from activity and program percent
					ActivityItem item = new ActivityItem(activity.getAct(),currCode,activity.getPercent(), reqest);
					//get already calculated amounts from helper
					ActivityUtil.ActivityAmounts amounts = item.getAmounts();
					//calculate totals
					actualSum =actualSum.add( amounts.getActualAmount());
					actualDisbSum=actualDisbSum.add( amounts.getActualDisbAmoount());
					plannedCommitments=plannedCommitments.add(amounts.getPlannedAmount());
					acts.add(item);
				}
				npdForm.setActivities(acts);
				npdForm.setActualSum(actualSum.doubleValue()!=0? FormatHelper.formatNumber(actualSum):"0");
				npdForm.setActualDisbSum((actualDisbSum.doubleValue()!=0)? FormatHelper.formatNumber(actualDisbSum):"0");
				npdForm.setPlannedCommSum((plannedCommitments.doubleValue()!=0)? FormatHelper.formatNumber(plannedCommitments):"0");
			}			
		}
		return mapping.findForward("forward");
	}


}
