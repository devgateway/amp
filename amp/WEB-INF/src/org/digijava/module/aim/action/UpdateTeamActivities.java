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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.ApprovalStatus;
import org.digijava.module.aim.form.TeamActivitiesForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.triggers.ActivitySaveTrigger;
import org.digijava.module.message.triggers.NotApprovedActivityTrigger;

public class UpdateTeamActivities extends Action {

    private static Logger logger = Logger.getLogger(UpdateTeamActivities.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {

        logger.debug("In update team activities");
        
        boolean permitted = false;
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") != null) {
            String key = (String) session.getAttribute("ampAdmin");
            if (key.equalsIgnoreCase("yes")) {
                permitted = true;
            } else {
                if (session.getAttribute("teamLeadFlag") != null) {
                    key = (String) session.getAttribute("teamLeadFlag");
                    if (key.equalsIgnoreCase("true")) {
                        permitted = true;   
                    }
                }
            }
        }
        if (!permitted) {
            return mapping.findForward("index");
        }

        TeamActivitiesForm taForm = (TeamActivitiesForm) form;

        String archiveCmd = taForm.getRemoveActivity();
        if ( archiveCmd != null ) {
            ArrayList<Long> selectedActivities  = new ArrayList<Long>();
            if ( taForm.getSelActivities() != null ) {
                for (int i=0; i<taForm.getSelActivities().length; i++){
                    selectedActivities.add(taForm.getSelActivities()[i]);
                }
            }                
                    
            if ( GetTeamActivities.ARCHIVE_COMMAND.equals(archiveCmd) ) {
                ActivityUtil.changeActivityArchiveStatus(selectedActivities, true);
                request.setAttribute(GetTeamActivities.ARCHIVED_PARAMETER, GetTeamActivities.UNARCHIVED_SUB_TAB);
                return mapping.findForward("forward");
            }
            if ( GetTeamActivities.UNARCHIVE_COMMAND.equals(archiveCmd) ) {
                ActivityUtil.changeActivityArchiveStatus(selectedActivities, false);
                request.setAttribute(GetTeamActivities.ARCHIVED_PARAMETER, GetTeamActivities.ARCHIVED_SUB_TAB);
                return mapping.findForward("forward");
            }
            
        }

        int numRecords = 0;
        int page = 0;
        String reset = request.getParameter("reset");

        Long id = null;
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");

        if (tm != null && tm.getAppSettings() != null) {
            id = tm.getTeamId();
            numRecords = tm.getAppSettings().getDefRecsPerPage();
             
            if (reset!=null && reset.equalsIgnoreCase("true")){
                 taForm.setTempNumResults(numRecords == 0 ? -1 : numRecords);
                 taForm.setKeyword(null);
                 taForm.setPage(1);
            }

            if (taForm.getTempNumResults() != -1){
                numRecords = taForm.getTempNumResults();
            }
        }

        if (taForm.getSelActivities() != null && taForm.getRemoveActivity().equals("remove")) {
            /* remove all selected activities */
            
            if (taForm.getSelActivities() != null) {
                TeamUtil.removeActivitiesFromTeam(taForm.getSelActivities(),taForm.getTeamId());
            }
                
            Long selActivities[] = taForm.getSelActivities();
            for (Long selActivityId:selActivities) {
                AmpActivityVersion activity=ActivityUtil.loadAmpActivity(selActivityId);
                String detail="unassigned from team";
                List<String> details=new ArrayList<String>();
                details.add(detail);
                AuditLoggerUtil.logActivityUpdate(request, activity, details);
            }
            taForm.setSelActivities(null);

            if (session.getAttribute("unassignedActivityList") != null) {
                session.removeAttribute("unassignedActivityList");
            }
            
            if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
                logger.info("removing ampProjects from session..");
                session.removeAttribute(Constants.AMP_PROJECTS);
            }
//          these attributes are never written to -> so we can safely delete the deletions          
//          if (session.getAttribute("teamActivityList") != null) {
//              session.removeAttribute("teamActivityList");
//          }
            taForm.setRemoveActivity(null);
            taForm.setSelActivities(null);
            
            //ReportContextData.getFromRequest().setGeneratedReport(null);
            //ReportContextData.getFromRequest().setReportMeta(null);
            return mapping.findForward("forward");
        } else if (taForm.getSelActivities() != null && taForm.getRemoveActivity().equals("assign")) {
            /* add the selected activities to the team list */
            logger.info("in assign activity");
            Long selActivities[] = taForm.getSelActivities();
            Long memberId = taForm.getMemberId();

            if (selActivities != null) {
                String detail="assigned to team";
                List<String> details=new ArrayList<String>();
                details.add(detail);
                for (int i = 0; i < selActivities.length; i++) {
                    if (selActivities[i] != null) {
                        Long actId = selActivities[i];
                        AmpActivityVersion activity = ActivityUtil.loadActivity(actId);
                        
                        AmpTeam ampTeam = TeamUtil.getAmpTeam(taForm.getTeamId());
                        activity.setTeam(ampTeam);
                        AmpTeamMember atm=TeamMemberUtil.getAmpTeamMember(memberId);
//                      AmpTeamMemberRoles ampRole = atm.getAmpMemberRole();
//                      AmpTeamMemberRoles headRole = TeamMemberUtil.getAmpTeamHeadRole();
                        //AMP-3937 - the activities assigned to an user, if that user is team lead then the 
                        //activities are approved!
                        if(atm.getAmpMemberRole().isApprover()){
                        //if (headRole!=null && ampRole.getAmpTeamMemRoleId().equals(headRole.getAmpTeamMemRoleId())) {
                            activity.setApprovalStatus(ApprovalStatus.approved);
                        }

                        logger.info("updating " + activity.getName());
                        DbUtil.update(activity);
                        new ActivitySaveTrigger(activity);
                        if (!activity.getApprovalStatus().equals(ApprovalStatus.approved)) {
                            new NotApprovedActivityTrigger(activity);
                        }
                        
                        AuditLoggerUtil.logActivityUpdate(request, activity, details);
                        //UpdateDB.updateReportCache(actId);
                    }
                }
            } else {
                taForm.setAssignActivity(null);
                taForm.setSelActivities(null);
                return mapping.findForward("forward");              
            }
            taForm.setSelActivities(null);
            if (session.getAttribute("unassignedActivityList") != null) {
                session.removeAttribute("unassignedActivityList");
            }
            if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
                session.removeAttribute(Constants.AMP_PROJECTS);
            }
//          these attributes are never written to -> so we can safely delete the deletions          
//          if (session.getAttribute("teamActivityList") != null) {
//              session.removeAttribute("teamActivityList");
//          }
            taForm.setAssignActivity(null);
            return mapping.findForward("forward");
        } else {
            /* show all unassigned activities */

            if ((reset!=null && reset.equalsIgnoreCase("true")) || request.getParameter("page") == null 
                    || Integer.parseInt(request.getParameter("page")) == 0) {
                page = 1;
            } else {
                page = Integer.parseInt(request.getParameter("page"));
            }

            AmpTeam ampTeam = TeamUtil.getAmpTeam(id);

            Collection<AmpActivity> col = null;
            if (session.getAttribute("unassignedActivityList") == null 
                    || StringUtils.isNotBlank(taForm.getKeyword())
                    || (reset !=null && reset.equalsIgnoreCase("true"))) {
                
                 List<AmpActivity> temp = new ArrayList<AmpActivity>();
                 for(AmpActivity act : TeamUtil.getAllTeamAmpActivities(null, true, taForm.getKeyword())) {
                     if (((org.dgfoundation.amp.onepager.util.ActivityUtil.ACTIVITY_TYPE_PROJECT.equals(act.getActivityType()) 
                                || act.getActivityType() == null)   && !ampTeam.isSSCWorkspace())  || 
                                (org.dgfoundation.amp.onepager.util.ActivityUtil.ACTIVITY_TYPE_SSC.equals(act.getActivityType())  
                                        &&  ampTeam.isSSCWorkspace())) {
                         temp.add(act);
                     }
                 }
                Collections.sort(temp);
                col = (Collection<AmpActivity>) temp;
                session.setAttribute("unassignedActivityList", col);
            }
            
            List<AmpActivity> actList = (List<AmpActivity>) session.getAttribute("unassignedActivityList");
            
            Comparator<AmpActivity> acronymComp = new Comparator<AmpActivity>() {
                public int compare(AmpActivity o1, AmpActivity o2) {
                    return o1.getDonors().trim().toLowerCase().compareTo(o2.getDonors().trim().toLowerCase());
                }
            };
            
            Comparator<AmpActivity> racronymComp = new Comparator<AmpActivity>() {
                public int compare(AmpActivity o1, AmpActivity o2) {
                    return -(o1.getDonors().trim().toLowerCase().compareTo(o2.getDonors().trim().toLowerCase()));
                }
            };
            
            String sort = (taForm.getSort() == null) ? null : taForm.getSort().trim();
            String sortOrder = (taForm.getSortOrder() == null) ? null : taForm.getSortOrder().trim();
            
            if (StringUtils.isEmpty(sort) || StringUtils.isEmpty(sortOrder)) {
                Collections.sort(actList);
                taForm.setSort("activity");
                taForm.setSortOrder("asc");
            } else {
                if ("activity".equals(sort)) {
                    if ("asc".equals(sortOrder))
                        Collections.sort(actList);
                    else
                        Collections.sort(actList,Collections.reverseOrder());
                } else if ("donor".equals(sort)) {
                    if ("asc".equals(sortOrder))
                        Collections.sort(actList, acronymComp);
                    else
                        Collections.sort(actList, racronymComp);
                }
            }           
            
            int stIndex = ((page - 1) * numRecords) + 1;
            if (stIndex < 0)
                stIndex = 1;
            int edIndex = page * numRecords;
            if (edIndex > actList.size()) {
                edIndex = actList.size();
            }

            Vector<AmpActivity> vect = new Vector<AmpActivity>();
            vect.addAll(actList);

            col = new ArrayList<AmpActivity>();
            for (int i = (stIndex - 1); i < edIndex; i++) {
                col.add(vect.get(i));
            }

            int numPages = actList.size() / numRecords;
            numPages += (actList.size() % numRecords != 0) ? 1 : 0;

            Collection pages = null;

            if (numPages > 1) {
                pages = new ArrayList();
                for (int i = 0; i < numPages; i++) {
                    Integer pageNum = new Integer(i + 1);
                    pages.add(pageNum);
                }
            }
            taForm.setPages(pages);
            taForm.setActivities(col);
            taForm.setTeamId(id);
            taForm.setTeamName(ampTeam.getName());
            taForm.setCurrentPage(new Integer(page));
            taForm.setSelActivities(null);
            taForm.setMembers(TeamMemberUtil.getAllTeamMembers(id));
            if(ampTeam.getTeamLead()!=null) taForm.setMemberId(ampTeam.getTeamLead().getAmpTeamMemId()); else taForm.setMemberId(null);
            session.setAttribute("pageno", new Integer(page));
            return mapping.findForward("showAddActivity");
            //return mapping.findForward("forward");
        }
    }

    public boolean canDelete(Long actId) {
        logger.debug("In can delete");
        Iterator itr = TeamMemberUtil.getAllMembersUsingActivity(actId).iterator();
        if (itr.hasNext()) {
            logger.debug("return false");
            return false;
        } else {
            logger.debug("return true");
            return true;
        }

    }
}
