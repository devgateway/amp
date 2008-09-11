package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.harvest.DBUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ActivityForm;
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

		String forwardName	= "forward";
		
		List dbReturnSet = null;
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		
		boolean appSettingSet = false;

		ReportsForm rf = (ReportsForm) form;
		rf.setShowTabs(null);
		if ( request.getParameter("tabs") != null ) {
			if (  "true".equals( request.getParameter("tabs") )  ) {
				rf.setShowTabs(true);
				forwardName	= "forwardTabs";
			}
			if (  "false".equals( request.getParameter("tabs") )  )
				rf.setShowTabs(false);
		}
		
		rf.setCurrentMemberId(null);
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");
		if(action==null){
			getAllReports(appSettingSet, rf, tm);
		}
		                                             
		int page = 0;
		if (request.getParameter("page") == null) {
			page = 0;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}
		rf.setCurrentPage(new Integer (page));
		rf.setPagesToShow(10);
		
		doPagination(rf, request);

		return mapping.findForward( forwardName );
	}
	private void doPagination(ReportsForm rf, HttpServletRequest request) {
		Collection allReports = rf.getReports();
		Collection pageList = rf.getReportsList();
		int pageSize = rf.getTempNumResults();
		if (pageList == null) {
			pageList = new ArrayList<AmpReports>();
			rf.setReportsList(pageList);
		}

		pageList.clear();
		int i = 0;


		int idx = 0;

		if(pageSize != -1 && rf.getPage() * rf.getPageSize() < allReports.size()){
			idx =  rf.getPage() * rf.getPageSize();
		}else{
			idx = 0;
			rf.setPage(0);
		}

		Double totalPages = 0.0;
		if(pageSize != -1){
			Iterator iterator = allReports.iterator();
			while(i<idx){
				iterator.next();
				i++;
			}
			for (i=0; iterator.hasNext() && i < pageSize; i++) {
				pageList.add(iterator.next());
			}
        	totalPages=Math.ceil(1.0*allReports.size() / rf.getPageSize());
		}
		else{
			Iterator iterator = allReports.iterator();
			for (i=0;iterator.hasNext(); i++) {
				pageList.add(iterator.next());
	       }
			totalPages=1.0;       	
        }

		rf.setTotalPages(totalPages.intValue());
	}

	private void getAllReports(boolean appSettingSet, ReportsForm rf, TeamMember tm) {
		if (rf.getCurrentPage() == 0) {
			rf.setCurrentPage(FIRST_PAGE);
		}

		if (tm == null) {
			Collection reports = ARUtil.getAllPublicReports(false);
			rf.setReports(reports);
			rf.setTotalPages(FIRST_PAGE);
		} else {
			rf.setCurrentMemberId(tm.getMemberId());
			ApplicationSettings appSettings = tm.getAppSettings();
			if (appSettings == null || appSettings.getDefReportsPerPage() == 0) {
				rf.setTotalPages(FIRST_PAGE);
			} else {
				appSettingSet = true;
			}

			Set<AmpReports> reps = new TreeSet<AmpReports>(
					new AdvancedReportUtil.AmpReportIdComparator());
			
			ArrayList teamResults = null;
			//Collection teamMemberResults = null;
			AmpApplicationSettings ampAppSettings = DbUtil.getTeamAppSettings(tm.getTeamId());
			AmpReports defaultTeamReport = ampAppSettings.getDefaultTeamReport();
			if (appSettingSet) {
				teamResults = (ArrayList)TeamUtil.getAllTeamReports(tm.getTeamId(), rf.getShowTabs(), 0, 0,true,tm.getMemberId());
				Double totalPages = Math.ceil(1.0* TeamUtil.getAllTeamReportsCount(tm.getTeamId(), rf.getShowTabs(), true,tm.getMemberId()) / appSettings.getDefReportsPerPage());
				rf.setTotalPages(totalPages.intValue());
				
			}else{
				teamResults = (ArrayList)TeamUtil.getAllTeamReports(tm.getTeamId(), rf.getShowTabs(), null, null,true,tm.getMemberId());
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
			if (!found && defaultTeamReport!=null && rf.getShowTabs()){
				teamResults.add(defaultTeamReport);
			}
			if(teamResults!=null){
				reps.addAll(teamResults);
			}

			List<AmpReports> sortedReports=new ArrayList<AmpReports>();
			//do not add this in ArrayList constructor.
			sortedReports.addAll(reps);
			//AmpReports are comparable by name, so this will sort by name.
			Collections.sort(sortedReports);
			rf.setReports(sortedReports);
			rf.setPage(0);
		}
	}

}
