package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.harvest.DBUtil;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ReportsForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.AdvancedReportUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.aim.helper.ApplicationSettings;
import java.util.List;

public class ShowTeamReports extends Action {

	private static Logger logger = Logger.getLogger(ShowTeamReports.class);
	private final static int FIRST_PAGE = 1;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		List dbReturnSet = null;
		HttpSession session = request.getSession();
		int reportsPerPage = 0;
		int startReport = 0;
		boolean appSettingSet = false;

		ReportsForm rf = (ReportsForm) form;
		rf.setCurrentMemberId(null);
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if (rf.getCurrentPage() == 0) {
			rf.setCurrentPage(FIRST_PAGE);
		}

		if (tm == null) {
			Collection reports = ARUtil.getAllPublicReports();
			rf.setReports(reports);
			rf.setTotalPages(FIRST_PAGE);
		} else {
			rf.setCurrentMemberId(tm.getMemberId());
			ApplicationSettings appSettings = tm.getAppSettings();
			if (appSettings == null || appSettings.getDefReportsPerPage() == 0) {
				rf.setTotalPages(FIRST_PAGE);
			} else {
				reportsPerPage = appSettings.getDefReportsPerPage();
				startReport = reportsPerPage * (rf.getCurrentPage() - 1);
				appSettingSet = true;
			}
/**
 * New Code, for AMP-2191
 */
			Set<AmpReports> reps = new TreeSet<AmpReports>(
					new AdvancedReportUtil.AmpReportIdComparator());

//			Collection results = null;
//			if (appSettingSet) {
//				results = TeamUtil.getAllTeamReports(tm.getTeamId(),startReport, reportsPerPage,true,tm.getMemberId());
//				//teamMemberResults = TeamMemberUtil.getAllTeamMembersReports(tm.getTeamId(), startReport, reportsPerPage);
//				Double totalPages = Math.ceil(1.0* TeamUtil.getAllTeamReportsCount(tm.getTeamId(),true,tm.getMemberId()) / appSettings.getDefReportsPerPage());
//				rf.setTotalPages(totalPages.intValue());
//			}else{
//				results = TeamUtil.getAllTeamReports(tm.getTeamId(),null, null,true,tm.getTeamId());				
//				//teamMemberResults = TeamMemberUtil.getAllTeamMembersReports(tm.getTeamId(),null,null);
//				 
//			}
//			/*
//			if(teamLeadResults!=null){
//				reps.addAll(teamLeadResults);
//			}
//			if (teamMemberResults!=null){
//				reps.addAll(teamMemberResults);
//			}
//			*/
//			if (results!=null){
//				reps.addAll(results);
//			}		
			
			ArrayList teamResults = null;
			//Collection teamMemberResults = null;
			AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
			AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
			if (appSettingSet) {
				teamResults = (ArrayList)TeamUtil.getAllTeamReports(tm.getTeamId(),startReport, reportsPerPage,true,tm.getMemberId());
				Double totalPages = Math.ceil(1.0* TeamUtil.getAllTeamReportsCount(tm.getTeamId(),true,tm.getMemberId()) / appSettings.getDefReportsPerPage());
				rf.setTotalPages(totalPages.intValue());
				
			}else{
				teamResults = (ArrayList)TeamUtil.getAllTeamReports(tm.getTeamId(),null, null,true,tm.getMemberId());
				}
			boolean found = false;
			if (defaultTeamReport != null){
				Iterator iter = teamResults.iterator();
				while (iter.hasNext()) {
					AmpReports el = (AmpReports) iter.next();                                 
					if (el.compareTo(defaultTeamReport) == 0){
						found = true;
						break;
					}
				}
			}
			if (!found){
				teamResults.add(defaultTeamReport);
			}
			if(teamResults!=null){
				reps.addAll(teamResults);
			}
			/*
			if (teamMemberResults!=null){
				reps.addAll(teamMemberResults);
			}
			*/
			List<AmpReports> sortedReports=new ArrayList<AmpReports>();
			//do not add this in ArrayList constructor.
			sortedReports.addAll(reps);
			//AmpReports are comparable by name, so this will sort by name.
			Collections.sort(sortedReports);
			rf.setReports(sortedReports);
			
/***********
 * Old Code. 
 */			
//			if (tm.getTeamHead() == true) {
//				if (appSettingSet) {
//					dbReturnSet = TeamUtil.getAllTeamReports(tm.getTeamId(),startReport, reportsPerPage);
//
//					Double totalPages = Math.ceil(1.0
//							* TeamUtil.getAllTeamReportsCount(tm.getTeamId())
//							/ appSettings.getDefReportsPerPage());
//					rf.setTotalPages(totalPages.intValue());
//
//				} else {
//					dbReturnSet = new ArrayList(TeamUtil.getAllTeamReports(tm.getTeamId()));
//				}
//			} else {
//				if (appSettingSet) {
//					dbReturnSet = TeamMemberUtil.getAllMemberReports(tm
//							.getMemberId(), startReport, reportsPerPage);
//					if (dbReturnSet == null)
//						dbReturnSet = new ArrayList();
//					/*
//					 * AmpReports defaultReport =
//					 * (AmpReports)session.getAttribute(Constants.DEFAULT_TEAM_REPORT);
//					 * if (defaultReport != null) { dbReturnSet.add(
//					 * session.getAttribute(Constants.DEFAULT_TEAM_REPORT) ); }
//					 * else logger.info("There is no default team report set!");
//					 */
//					Double totalPages = Math.ceil(0.1
//							* TeamMemberUtil.getAllMemberReportsCount(tm.getMemberId())
//							/ appSettings.getDefReportsPerPage());
//					rf.setTotalPages(totalPages.intValue());
//				} else {
//					dbReturnSet = TeamMemberUtil.getAllMemberReports(tm.getMemberId());
//				}
//			}
//			rf.setReports(dbReturnSet);

		}                                             

		return mapping.findForward("forward");
	}

}
