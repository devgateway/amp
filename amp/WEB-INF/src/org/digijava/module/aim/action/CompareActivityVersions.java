package org.digijava.module.aim.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.CompareActivityVersionsForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.LuceneUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class CompareActivityVersions extends DispatchAction {

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        return compare(mapping, form, request, response);
    }

    public ActionForward compare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
        Session session = PersistenceManager.getRequestDBSession();

        setAdvancemode(vForm, request);

        if (request.getParameter("action") != null && request.getParameter("action").equals("setVersion")
                && request.getParameter("activityCurrentVersion") != null) {

            Long activityId = Long.parseLong(request.getParameter("activityCurrentVersion"));
            AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityId);
            AmpActivityGroup group = activity.getAmpActivityGroup();

            AmpActivityVersion prevVer = group.getAmpActivityLastVersion();

            //Why send the activity to the top of the list? It's confusing for users
            //thus we don't set the modifiedDate
            //activity.setModifiedDate(Calendar.getInstance().getTime()); 
            group.setAmpActivityLastVersion(activity);
            session.update(group);
            //session.update(activity);

            Site site = RequestUtils.getSite(request);
            Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
            java.util.Locale locale = new java.util.Locale(navigationLanguage.getCode());
            LuceneUtil.addUpdateActivity(request.getSession().getServletContext().getRealPath("/"), true, site, locale, activity, prevVer);

            return new ActionForward(mapping.findForward("reload").getPath() + "?ampActivityId=" + activityId, true);
        }

        vForm.setOutputCollection(new ArrayList<CompareOutput>());
        vForm.setOutputCollectionGrouped(ActivityVersionUtil.compareActivities(vForm.getActivityOneId(), vForm.getActivityTwoId()));

        return mapping.findForward("forward");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        return mapping.findForward("reload");
    }


    private void setAdvancemode(CompareActivityVersionsForm vForm, HttpServletRequest request) {
        boolean ispartofamanagetmentworkspace = false;
        boolean iscurrentworkspacemanager = false;

        TeamMember currentMember = (TeamMember) request.getSession().getAttribute("currentMember");
        AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(currentMember.getMemberId());

        if (ampCurrentMember.getAmpMemberRole().getTeamHead())
            iscurrentworkspacemanager = true;
        if (ampCurrentMember.getAmpTeam().getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT))
            ispartofamanagetmentworkspace = true;

        //If the current user is part of the management workspace or is not the workspace manager of a workspace that's not management then hide.
        vForm.setAdvancemode(!ispartofamanagetmentworkspace & iscurrentworkspacemanager);
    }

}
