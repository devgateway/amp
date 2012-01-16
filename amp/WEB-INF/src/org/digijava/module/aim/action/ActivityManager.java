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
import org.digijava.kernel.exception.DgException;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.ActivityForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;

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
		
		//AMP-5518
		if ((action != null) && (action.equals("search")) && (actForm.getKeyword() != null) && (actForm.getLastKeyword() != null) && (!actForm.getKeyword().equals(actForm.getLastKeyword())) && ("".equals(actForm.getKeyword().replaceAll(" ", "")))){
			action="reset";
		}

		if (action == null) {
			reset(actForm, request);
		} else if (action.equals("delete")) {
			deleteActivity(actForm, request);
		} else if (action.equals("sort")) {
			sortActivities(actForm, request);
		} else if (action.equals("search")) {
			actForm.setLastKeyword(actForm.getKeyword());
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
		actForm.setAllActivityList(ActivityUtil.getAllActivitiesAdmin(null));
		actForm.setKeyword(null);
		actForm.setLastKeyword(null);
		actForm.setSortByColumn(null);
		actForm.setPage(0);
		actForm.setTempNumResults(10);
	}

	private void doPagination(ActivityForm actForm, HttpServletRequest request) {
		List<AmpActivityFake> allActivities = actForm.getAllActivityList();
		List<AmpActivityFake> pageList = actForm.getActivityList();
		int pageSize = actForm.getTempNumResults();
		if (pageList == null) {
			pageList = new ArrayList<AmpActivityFake>();
			actForm.setActivityList(pageList);
		}

		pageList.clear();
		int i = 0;


		int idx = 0;

		if(pageSize != -1 && actForm.getPage() * actForm.getPageSize() < allActivities.size()){
			idx =  actForm.getPage() * actForm.getPageSize();
		}else{
			idx = 0;
			actForm.setPage(0);
		}
		
		actForm.setPageSize(pageSize);

		Double totalPages = 0.0;
		if(pageSize != -1){
			for (Iterator<AmpActivityFake> iterator = allActivities.listIterator(idx);
					iterator.hasNext() && i < pageSize; i++) {
				pageList.add(iterator.next());
			}
        	totalPages=Math.ceil(1.0*allActivities.size() / actForm.getPageSize());
		}
		else{
			for (Iterator<AmpActivityFake> iterator = allActivities.listIterator(idx);
				iterator.hasNext(); i++) {
				pageList.add(iterator.next());
	       }
			totalPages=1.0;       	
        }

		actForm.setTotalPages(totalPages.intValue());
	}

	private void searchActivities(ActivityForm actForm, HttpServletRequest request) {
		List<AmpActivityFake> activities = ActivityUtil.getAllActivitiesAdmin(actForm.getKeyword());
		actForm.setAllActivityList(activities);
		sortActivities(actForm,request);
	}

	private void sortActivities(ActivityForm actForm, HttpServletRequest request) {
		List<AmpActivityFake> activities = actForm.getAllActivityList();

		int sortBy = 0;
		if("activityName".equals(actForm.getSortByColumn())){
			sortBy = 1;
		}else if("activityId".equals(actForm.getSortByColumn())){
			sortBy = 2;
		}else if("activityTeamName".equals(actForm.getSortByColumn())){
			sortBy = 3;
		}

		switch (sortBy) {
		case 1:
			Collections.sort(activities, new Comparator<AmpActivityFake>(){
				public int compare(AmpActivityFake a1, AmpActivityFake a2) {
					String s1	= a1.getName();
					String s2	= a2.getName();
					if ( s1 == null )
						s1	= "";
					if ( s2 == null )
						s2	= "";
					
					return s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
					//return a1.getName().compareTo(a2.getName());
				}
			});
			break;
		case 2:
			Collections.sort(activities, new Comparator<AmpActivityFake>(){
				public int compare(AmpActivityFake a1, AmpActivityFake a2) 
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
		case 3:
			Collections.sort(activities, new Comparator<AmpActivityFake>(){
				public int compare(AmpActivityFake a1, AmpActivityFake a2) {
                                String s1 = "";
                                String s2 = "";
                                if (a1.getTeam() != null) {
                                    s1 = a1.getTeam().getName();

                                }
                                if (a2.getTeam() != null) {
                                    s2 = a2.getTeam().getName();

                                }


                                if (s1 == null) {
                                    s1 = "";
                                }
                                if (s2 == null) {
                                    s2 = "";
                                }
                                return s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
                            //return a1.getName().compareTo(a2.getName());
                            }
			});
			break;
		default:
			Collections.sort(activities, new Comparator<AmpActivityFake>(){
				public int compare(AmpActivityFake a1, AmpActivityFake a2) {
					String s1	= a1.getName();
					String s2	= a2.getName();
					if ( s1 == null )
						s1	= "";
					if ( s2 == null )
						s2	= "";
					
					return s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
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
	 * @throws DgException 
	 */
	private void deleteActivity(ActivityForm actForm, HttpServletRequest request) throws DgException {
		HttpSession session = request.getSession();
		String tIds=request.getParameter("tIds");
		String advancedAdminDelete = request.getParameter("advancedAdminDelete");
		List<Long> topicsIds=getActsIds(tIds.trim());
		for (Long ampActId : topicsIds) {
			AmpActivityVersion activity = ActivityUtil.loadActivity(ampActId);
			AuditLoggerUtil.logObject(session, request, activity, "delete");
			//ActivityUtil.deleteActivity(ampActId);
			if(advancedAdminDelete!=null && "true".compareTo(advancedAdminDelete)==0 )
				ActivityUtil.deleteAmpActivityWithVersions(ampActId);
			else
				ActivityUtil.archiveAmpActivityWithVersions(ampActId);
		}
		actForm.setAllActivityList(ActivityUtil.getAllActivitiesAdmin(null));		
	}
	private List<Long> getActsIds(String ids){
		List<Long> actsIds=new ArrayList<Long>();
		while(ids.indexOf(",")!= -1){
			Long id= new Long(ids.substring(0,ids.indexOf(",")).trim());
			actsIds.add(id);
			ids=ids.substring(ids.indexOf(",")+1);
		}
		actsIds.add(new Long(ids.trim()));
		return actsIds;
	}
	
}

