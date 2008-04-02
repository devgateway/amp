package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.form.ActivityForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;

public class ActivityManager extends Action {
	private static Logger logger = Logger.getLogger(ActivityManager.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		HttpSession session = request.getSession();
		String action = request.getParameter("action");

		if (session.getAttribute("ampAdmin") == null)
			return mapping.findForward("index");
		else {
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no"))
				return mapping.findForward("index");
		}

		ActivityForm actForm = (ActivityForm) form;

		if (action == null) {
			reset(actForm, request);
		} else if (action.equals("delete")) {
			deleteActivity(actForm, request);
		} else if (action.equals("sort")) {
			sortActivities(actForm, request);
		} else if (action.equals("search")) {
			searchActivities(actForm, request);
		} else if (action.equals("reset")) {
			reset(actForm, request);
		}
		
		int page = 0;
		if (request.getParameter("page") == null) {
			page = 0;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}
		actForm.setCurrentPage(new Integer (page));
		actForm.setPagesToShow(10);
		
		doPagination(actForm, request);

		return mapping.findForward("forward");
	}

	private void reset(ActivityForm actForm, HttpServletRequest request) {
		actForm.setAllActivityList(ActivityUtil.getAllActivitiesList());
		actForm.setKeyword(null);
		actForm.setSortByColumn(null);
		actForm.setPage(0);
	}

	private void doPagination(ActivityForm actForm, HttpServletRequest request) {
		List<AmpActivity> allActivities = actForm.getAllActivityList();
		List<AmpActivity> pageList = actForm.getActivityList();
		int pageSize = actForm.getTempNumResults();
		if (pageList == null) {
			pageList = new ArrayList<AmpActivity>();
			actForm.setActivityList(pageList);
		}

		pageList.clear();
		int i = 0;


		int idx = 0;

		if(actForm.getPage() * actForm.getPageSize() < allActivities.size()){
			idx =  actForm.getPage() * actForm.getPageSize();
		}else{
			idx = 0;
			actForm.setPage(0);
		}

		Double totalPages = 0.0;
		if(pageSize != -1){
			for (Iterator<AmpActivity> iterator = allActivities.listIterator(idx);
					iterator.hasNext() && i < pageSize; i++) {
				pageList.add(iterator.next());
			}
        	totalPages=Math.ceil(1.0*allActivities.size() / actForm.getPageSize());
		}
		else{
			for (Iterator<AmpActivity> iterator = allActivities.listIterator(idx);
				iterator.hasNext(); i++) {
				pageList.add(iterator.next());
	       }
			totalPages=1.0;       	
        }

		actForm.setTotalPages(totalPages.intValue());
	}

	private void searchActivities(ActivityForm actForm, HttpServletRequest request) {
		List<AmpActivity> activities = ActivityUtil.getAllActivitiesByName(actForm.getKeyword());
		actForm.setAllActivityList(activities);
		sortActivities(actForm,request);
	}

	private void sortActivities(ActivityForm actForm, HttpServletRequest request) {
		List<AmpActivity> activities = actForm.getAllActivityList();

		int sortBy = 0;
		if("activityName".equals(actForm.getSortByColumn())){
			sortBy = 1;
		}else if("activityId".equals(actForm.getSortByColumn())){
			sortBy = 2;
		}

		switch (sortBy) {
		case 1:
			Collections.sort(activities, new Comparator<AmpActivity>(){
				public int compare(AmpActivity a1, AmpActivity a2) {
					String s1	= a1.getName();
					String s2	= a2.getName();
					if ( s1 == null )
						s1	= "";
					if ( s2 == null )
						s2	= "";
					
					return s1.trim().compareTo(s2.trim());
					//return a1.getName().compareTo(a2.getName());
				}
			});
			break;
		case 2:
			Collections.sort(activities, new Comparator<AmpActivity>(){
				public int compare(AmpActivity a1, AmpActivity a2) 
				{
					//return a1.getAmpActivityId().compareTo(a2.getAmpActivityId());
					String c1="";
					String c2="";
					if(a1.getAmpId()!=null) c1=a1.getAmpId();
					if(a2.getAmpId()!=null) c2=a2.getAmpId();
					
					return c1.compareTo(c2);
				}
			});
			break;
		default:
			Collections.sort(activities, new Comparator<AmpActivity>(){
				public int compare(AmpActivity a1, AmpActivity a2) {
					return a1.getName().compareTo(a2.getName());
				}
			});
			break;
		}
	}

	/**
	 * @param actForm
	 * @param actForm
	 * @param request
	 * @param session
	 */
	private void deleteActivity(ActivityForm actForm, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Long ampActId = new Long(Long.parseLong(request.getParameter("id")));
		AmpActivity activity = ActivityUtil.getAmpActivity(ampActId);
		AuditLoggerUtil.logObject(session, request, activity, "delete");
		ActivityUtil.deleteActivityAmpComments(DbUtil
				.getActivityAmpComments(ampActId));
		ActivityUtil.deleteActivityPhysicalComponentReport(DbUtil
				.getActivityPhysicalComponentReport(ampActId));
		ActivityUtil.deleteActivityAmpReportCache(DbUtil
				.getActivityReportCache(ampActId));
		ActivityUtil.deleteActivityReportLocation(DbUtil
				.getActivityReportLocation(ampActId));
		ActivityUtil.deleteActivityReportPhyPerformance(DbUtil
				.getActivityRepPhyPerformance(ampActId));
		ActivityUtil.deleteActivityReportSector(DbUtil
				.getActivityReportSector(ampActId));
		ActivityUtil.deleteActivityIndicatorVal(DbUtil
				.getActivityMEIndValue(ampActId));
		ActivityUtil.deleteActivity(ampActId);
		actForm.setAllActivityList(ActivityUtil.getAllActivitiesList());
	}
}

