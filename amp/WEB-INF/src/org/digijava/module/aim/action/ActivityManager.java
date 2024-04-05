package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeService;
import org.digijava.module.admin.helper.AmpActivityFake;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.ActivityForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


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
        actForm.setFrozenActivityIds(DataFreezeService.getFronzeActivities());

        if (action == null || action.equals("reset")) {
            reset(actForm, request);
        } else if (action.equals("delete")) {
            deleteActivity(actForm, request);
        } else if (action.equals("search")) {            
            actForm.setLastKeyword(actForm.getKeyword());
        } else if (action.equals("unfreeze")) {
            unfreezeActivities(actForm, request);
        }
            
        sortActivities(actForm, request);
        int page = 0;
        if (request.getParameter("page") == null) {
            page = 0;
        } else {
            page = Integer.parseInt(request.getParameter("page"));
        }
        actForm.setCurrentPage(new Integer (page));
        actForm.setPagesToShow(-1);
        
        doPagination(actForm, request);

        return mapping.findForward("forward");
    }

    private void reset(ActivityForm actForm, HttpServletRequest request) {
        actForm.setAllActivityList(ActivityUtil.getAllActivitiesAdmin(null, null, ActivityForm.DataFreezeFilter.ALL));
        actForm.setKeyword("");
        actForm.setLastKeyword("");
        actForm.setSortByColumn(null);
        actForm.setPage(0);
        actForm.setTempNumResults(-1);
        actForm.setDataFreezeFilter(ActivityForm.DataFreezeFilter.ALL.toString());
    }

    private void doPagination(ActivityForm actForm, HttpServletRequest request) {
        List<AmpActivityFake> allActivities = actForm.getAllActivityList();
        //sort...
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

    private void sortActivities(ActivityForm actForm, HttpServletRequest request) {
        List<AmpActivityFake> activities = null;
        
        if ((actForm.getKeyword() != null && actForm.getKeyword().trim().length()>0) || ActivityForm.DataFreezeFilter.FROZEN.equals(actForm.getDataFreezeFilterEnum()) || ActivityForm.DataFreezeFilter.UNFROZEN.equals(actForm.getDataFreezeFilterEnum())){
            activities = ActivityUtil.getAllActivitiesAdmin(actForm.getKeyword().trim(), actForm.getFrozenActivityIds(), actForm.getDataFreezeFilterEnum());
        } else {
            activities = actForm.getAllActivityList();
        }

        String sort = (actForm.getSort() == null) ? null : actForm.getSort().trim();
        String sortOrder = (actForm.getSortOrder() == null) ? null : actForm.getSortOrder().trim();
        int sortBy = 0;
        if("activityName".equals(sort)&&"asc".equalsIgnoreCase(sortOrder)){
            sortBy = 1;
        }else if("activityName".equals(sort)&&"desc".equalsIgnoreCase(sortOrder)){
            sortBy = 2;
        }else if("activityId".equals(sort)&&"asc".equalsIgnoreCase(sortOrder)){
            sortBy = 3;
        }else if("activityId".equals(sort)&&"desc".equalsIgnoreCase(sortOrder)){
            sortBy = 4;
        }else if("activityTeamName".equals(sort)&&"asc".equalsIgnoreCase(sortOrder)){
            sortBy = 5;
        }else if("activityTeamName".equals(sort)&&"desc".equalsIgnoreCase(sortOrder)){
            sortBy = 6;
        }

        switch (sortBy) {
        case 1:
            Collections.sort(activities, new Comparator<AmpActivityFake>(){
                public int compare(AmpActivityFake a1, AmpActivityFake a2) {
                    String s1   = a1.getName();
                    String s2   = a2.getName();
                    if ( s1 == null )
                        s1  = "";
                    if ( s2 == null )
                        s2  = "";
                    
                    return s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
                    //return a1.getName().compareTo(a2.getName());
                }
            });
            break;
        case 2:
            Collections.sort(activities, new Comparator<AmpActivityFake>(){
                public int compare(AmpActivityFake a1, AmpActivityFake a2) {
                    String s1   = a1.getName();
                    String s2   = a2.getName();
                    if ( s1 == null )
                        s1  = "";
                    if ( s2 == null )
                        s2  = "";
                    
                    return -s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
                    //return a1.getName().compareTo(a2.getName());
                }
            });
            break;  
        case 3:
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
        case 4:
            Collections.sort(activities, new Comparator<AmpActivityFake>(){
                public int compare(AmpActivityFake a1, AmpActivityFake a2) 
                {
                    //return a1.getAmpActivityId().compareTo(a2.getAmpActivityId());
                    String c1="";
                    String c2="";
                    if(a1.getAmpId()!=null) c1=a1.getAmpId();
                    if(a2.getAmpId()!=null) c2=a2.getAmpId();
                    
                    return -c1.compareTo(c2);
                }
            });
            break;
        case 5:
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
        case 6:
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
                                return -s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
                            //return a1.getName().compareTo(a2.getName());
                            }
            });
            break;  
        default:
            Collections.sort(activities, new Comparator<AmpActivityFake>(){
                public int compare(AmpActivityFake a1, AmpActivityFake a2) {
                    String s1   = a1.getName();
                    String s2   = a2.getName();
                    if ( s1 == null )
                        s1  = "";
                    if ( s2 == null )
                        s2  = "";
                    
                    return s1.toUpperCase().trim().compareTo(s2.toUpperCase().trim());
                }
            });
            break;
        }
        
        
            
      for(AmpActivityFake activity : activities) {
          activity.setFrozen(actForm.getFrozenActivityIds().contains(activity.getAmpActivityId()));     
      }
      
      actForm.setAllActivityList(activities);
    }

    private void deleteActivity(ActivityForm actForm, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String tIds = request.getParameter("tIds");
        List<Long> ids = getActsIds(tIds.trim());
        for (Long id : ids) {
            AmpActivityVersion activity = ActivityUtil.loadAmpActivity(id);
            AuditLoggerUtil.logObject(session, request, activity, "delete");
            ActivityUtil.deleteAmpActivityWithVersions(id);
        }
        actForm.setAllActivityList(ActivityUtil.getAllActivitiesAdmin(null, null, ActivityForm.DataFreezeFilter.ALL));      
    }

    private List<Long> getActsIds(String ids){
        List<Long> actsIds=new ArrayList<Long>();
        while(ids.contains(",")){
            Long id= new Long(ids.substring(0,ids.indexOf(",")).trim());
            actsIds.add(id);
            ids=ids.substring(ids.indexOf(",")+1);
        }
        actsIds.add(new Long(ids.trim()));
        return actsIds;
    }
    
    
    private void unfreezeActivities(ActivityForm actForm, HttpServletRequest request) {
        String activityIdsParam = request.getParameter("activityIds");
        Set<Long> activityIds = new LinkedHashSet<>(getActsIds(activityIdsParam.trim()));
        DataFreezeService.unfreezeActivities(activityIds);
        actForm.setFrozenActivityIds(DataFreezeService.getFronzeActivities());
    }
    
}

