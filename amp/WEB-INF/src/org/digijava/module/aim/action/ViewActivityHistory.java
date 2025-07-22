package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ViewActivityHistoryForm;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.LinkedHashMap;

public class ViewActivityHistory extends DispatchAction {

    private static Logger logger = Logger.getLogger(EditActivity.class);

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        return showHistory(mapping, form, request, response);
    }

    public ActionForward showHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ViewActivityHistoryForm hForm = (ViewActivityHistoryForm) form;
        // Load current activity, get group and retrieve past versions.
        Long activityId =  hForm.getActivityId();

        hForm.setActivities(ActivityUtil.getActivityHistories(activityId));

        AmpActivityVersion previousActivity = ActivityUtil.getPreviousVersion(activityId);
        hForm.setEnableSummaryChange(previousActivity != null);

        TeamMember currentMember = (TeamMember)request.getSession().getAttribute("currentMember");

        //it also can be accessed anonymously
        if (currentMember!=null) {
            AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(currentMember.getMemberId());
            
            boolean ispartofamanagetmentworkspace = ampCurrentMember.getAmpTeam().getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT);
            boolean iscurrentworkspacemanager = ampCurrentMember.getAmpMemberRole().getTeamHead();
            
            //If the current user is part of the management workspace or is not the workspace manager of a workspace that's not management then hide.
            hForm.setEnableadvanceoptions(!ispartofamanagetmentworkspace & iscurrentworkspacemanager);
        }
        
        return mapping.findForward("forward");
    }

    public ActionForward changesSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        Long activityId = Long.parseLong(request.getParameter("activityId"));
        AmpActivityVersion previousActivity = ActivityUtil.getPreviousVersion(activityId);

        LinkedHashMap<Long, Long> activitiesIds = new LinkedHashMap<>();
        activitiesIds.put(activityId, activityId);
        activitiesIds.put(previousActivity.getAmpActivityId(), activityId);

        LinkedHashMap<Long, Collection<SummaryChange>> activityList = SummaryChangesService
                .processActivity(activitiesIds);

        Session session = PersistenceManager.getRequestDBSession();
        AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityId);
        for (Long id : activityList.keySet()) {

            Collection<SummaryChange> changesList = activityList.get(id);
            SummaryChangeHtmlRenderer renderer = new SummaryChangeHtmlRenderer(activity, changesList, RequestUtils
                    .getNavigationLanguage(request).getCode());

            request.setAttribute("changesTable", renderer.renderWithLegend());
        }

        return mapping.findForward("summaryChanges");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        return mapping.findForward("reload");
    }
}
