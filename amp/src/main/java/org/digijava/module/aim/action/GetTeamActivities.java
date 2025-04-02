package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamUtil;

public class GetTeamActivities extends Action {

    private static Logger logger = Logger.getLogger(GetTeamActivities.class);
    public static final String ARCHIVED_PARAMETER   = "selectedSubTab";
    public static final String ARCHIVED_SUB_TAB     = "4";
    public static final String UNARCHIVED_SUB_TAB   = "3";
    public static final String ARCHIVE_COMMAND      = "archive";
    public static final String UNARCHIVE_COMMAND    = "unarchive";

    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        TeamActivitiesForm taForm = (TeamActivitiesForm) form;
        Long id = null;

        try {
            boolean permitted = false;
            boolean adminView = false;
            HttpSession session = request.getSession();
            TeamMember tm = (TeamMember) session.getAttribute("currentMember");
            if(session.getAttribute("ampAdmin") != null) {
                String key = (String) session.getAttribute("ampAdmin");
                if(key.equalsIgnoreCase("yes")) {
                    permitted = true;
                    adminView = true;
                } else {
                    if(session.getAttribute("teamLeadFlag") != null) {
                        key = (String) session.getAttribute("teamLeadFlag");
                        if(key.equalsIgnoreCase("true")) {
                            permitted = true;
                        }
                    }
                }
            }
            if(!permitted) {
                return mapping.findForward("index");
            }

            int numRecords = Constants.NUM_RECORDS;
            int page = 0;
            
            RepairDbUtil.repairDb();

            if(request.getParameter("id") != null){
                id = new Long(request.getParameter("id"));
            }else{
                id=taForm.getTeamId();
            }
           
            if(adminView && request.getParameter("reset")==null){
                numRecords= taForm.getTempNumResults();
            }else{
                if (id!=null){
                    AmpApplicationSettings appSettings = DbUtil.getTeamAppSettings(id);
                    if(appSettings != null) {
                        numRecords = appSettings.getDefaultRecordsPerPage().intValue();
                    }
                } else if(request.getAttribute("teamId") != null) {
                    id = (Long) request.getAttribute("teamId");
                    AmpApplicationSettings appSettings = DbUtil.getTeamAppSettings(id);
                    if(appSettings != null) {
                        numRecords = appSettings.getDefaultRecordsPerPage().intValue();
                    }
                } else if(tm != null) {
                    id = tm.getTeamId();
                    if(tm.getAppSettings() != null){
                        numRecords = tm.getAppSettings().getDefRecsPerPage();
                    }
                        
                    //String appSettingsForPages = request.getParameter("appSettingsForPages");
                    String reset = request.getParameter("reset");
                    
                    if(reset!=null && reset.equalsIgnoreCase("true")){
                        //taForm.setTempNumResults(-1);
                        if (numRecords!=0) {
                            taForm.setTempNumResults(numRecords);
                        }
                    }
                    numRecords = taForm.getTempNumResults();
                }
            }
            
            if(request.getParameter("reset")!=null){
                taForm.setKeyword(null);
            }
            //taForm.setTeamId(id);

            if(id != null) {
                if(request.getParameter("page") == null) {
                    page = 1;
                } else {
                    page = Integer.parseInt(request.getParameter("page"));
                }

                page = (page < 0) ? 1 : page;
               // taForm.setPage(page);

                AmpTeam ampTeam = TeamUtil.getAmpTeam(id);
                taForm.setTeamId(id);
                taForm.setTeamName(ampTeam.getName());

                if(taForm.getAllActivities() == null) {
                    Collection<AmpActivityVersion> col = null;
                    if(Constants.ACCESS_TYPE_MNGMT.equalsIgnoreCase(ampTeam.getAccessType())) {
                        col = TeamUtil.getManagementTeamActivities(id, taForm.getKeyword());
                        taForm.setDonorFlag(true);
                    } else {
                        col = TeamUtil.getAllTeamActivities(id, true, taForm.getKeyword());
                        taForm.setDonorFlag(false);
                    }
                   
                    logger.info("Loaded " + col.size() + " activities for the team " + ampTeam.getName());
                    taForm.setAllActivities(col);
                }

                Comparator<AmpActivityVersion> acronymComp = new Comparator<AmpActivityVersion>() {
                    public int compare(AmpActivityVersion o1, AmpActivityVersion o2) {
                        return o1.getDonors().trim().toLowerCase().compareTo(o2.getDonors().trim().toLowerCase());
                    }
                };
                Comparator<AmpActivityVersion> racronymComp = new  Comparator<AmpActivityVersion>() {
                    public int compare(AmpActivityVersion o1, AmpActivityVersion o2) {
                        return -(o1.getDonors().trim().toLowerCase().compareTo(o2.getDonors().trim().toLowerCase()));
                    }
                };

                List<AmpActivityVersion> temp = (List<AmpActivityVersion>) taForm.getAllActivities();
                String sort = (taForm.getSort() == null) ? null : taForm.getSort().trim();
                String sortOrder = (taForm.getSortOrder() == null) ? null : taForm.getSortOrder().trim();

                if(sort == null || "".equals(sort) || sortOrder == null || "".equals(sortOrder)) {
                    Collections.sort(temp);
                    taForm.setSort("activity");
                    taForm.setSortOrder("asc");
                } else {
                    if("activity".equals(sort)) {
                        if("asc".equals(sortOrder))
                            Collections.sort(temp);
                        else
                            Collections.sort(temp, Collections.reverseOrder());
                    } else if("donor".equals(sort)) {
                        
                        if("asc".equals(sortOrder))
                            Collections.sort(temp, acronymComp);
                        else
                            Collections.sort(temp, racronymComp);

                        boolean order = "asc".equals(sortOrder)?true:false;
                        temp = ActivityUtil.getSortedActivitiesByDonors(temp, order);
                    }
                }
                taForm.setAllActivities(temp);
                decideArchiveMode(mapping, taForm, request, response);

                int totActivities = taForm.getAllActivities().size();
                if(numRecords == -1 || numRecords > totActivities){
                    numRecords = totActivities;
                } else if(totActivities / numRecords > 50) {
                    numRecords = totActivities / 50;
                }
                    
                int numPages = numRecords == 0 ? 1 : 
                    (totActivities / numRecords) + ((totActivities % numRecords != 0) ? 1 : 0);
                
                if (page > numPages){
                    page = numPages;
                }
                
                int stIndex = ((page - 1) * numRecords) + 1;
                int edIndex = 1;
                if(page != 0) edIndex = page * numRecords;
                else edIndex = numRecords;
                          
                edIndex = (edIndex > totActivities) ? totActivities : edIndex;
                if (stIndex<1) stIndex=1;
                Vector vect = new Vector();
                vect.addAll(taForm.getAllActivities());

                taForm.setActivities(new ArrayList());
                for(int i = (stIndex - 1); i < edIndex; i++) {
                    taForm.getActivities().add(vect.get(i));
                }

               
                Collection pages = null;
                if(numPages > 1) {
                    pages = new ArrayList();
                    for(int i = 0; i < numPages; i++) {
                        Integer pageNum = new Integer(i + 1);
                        pages.add(pageNum);
                    }
                }
                taForm.setCurrentPage(new Integer(page));
                taForm.setPages(pages);
                session.setAttribute("pageno", new Integer(page));
                taForm.setSelActivities(new Long[0]);
                return mapping.findForward("forward");
            } else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }

        return null;
    }
 private static void decideArchiveMode(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {
        
        TeamActivitiesForm tForm    = (TeamActivitiesForm) form;
        String showArchived         = request.getParameter("showArchivedActivities");
        String archiveComand        = tForm.getRemoveActivity();
        Boolean showArchivedActivities  = null;
        tForm.setRemoveActivity(null);
        
        if ( archiveComand != null ) {
            if ( ARCHIVE_COMMAND.equals(archiveComand) )
                showArchivedActivities  = false;
            if ( UNARCHIVE_COMMAND.equals(archiveComand) )
                showArchivedActivities  = true;
        }
        if ( showArchivedActivities == null && showArchived != null ) 
            showArchivedActivities  = Boolean.parseBoolean(showArchived);
        
        if ( showArchivedActivities != null ) {
            if ( !showArchivedActivities ) {
                request.setAttribute(ARCHIVED_PARAMETER, UNARCHIVED_SUB_TAB);
            }
            else
                request.setAttribute(ARCHIVED_PARAMETER, ARCHIVED_SUB_TAB);
            GetTeamActivities.filterArchivedActivities(tForm.getAllActivities(), showArchivedActivities);
        }
    }
    
    private static void filterArchivedActivities ( Collection<AmpActivityVersion> activities, boolean archived) {
        if (activities != null) {
            Iterator<AmpActivityVersion> iter   = activities.iterator();
            while ( iter.hasNext() ) {
                AmpActivityVersion activity =  iter.next();
                Boolean actArchived     = activity.getArchived();
                if (actArchived == null)
                    actArchived = false;
                if ( actArchived != archived ) {
                    iter.remove();
                }
            }
        }
    }
}
