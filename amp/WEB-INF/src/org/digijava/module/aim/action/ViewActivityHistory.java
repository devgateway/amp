package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.form.ViewActivityHistoryForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.hibernate.Query;
import org.hibernate.Session;

public class ViewActivityHistory extends Action {

	private static Logger logger = Logger.getLogger(EditActivity.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ViewActivityHistoryForm hForm = (ViewActivityHistoryForm) form;
		// Load current activity, get group and retrieve past versions.
		Session session = PersistenceManager.getRequestDBSession();
		AmpActivityVersion currentActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, hForm.getActivityId());

		// AMP-7706: Filter last 5 versions.
		Query qry = session.createQuery("SELECT act FROM " + AmpActivityVersion.class.getName() + " act WHERE act.ampActivityGroup.ampActivityGroupId = ? ORDER BY act.ampActivityId DESC").setMaxResults(ActivityVersionUtil.numberOfVersions());
		qry.setParameter(0, currentActivity.getAmpActivityGroup().getAmpActivityGroupId());
		hForm.setActivities(new ArrayList<AmpActivityVersion>(qry.list()));
			
		TeamMember currentMember = (TeamMember)request.getSession().getAttribute("currentMember");
		AmpTeamMember ampCurrentMember = TeamMemberUtil.getAmpTeamMember(currentMember.getMemberId());
		
		boolean ispartofamanagetmentworkspace = ampCurrentMember.getAmpTeam().getAccessType().equalsIgnoreCase(Constants.ACCESS_TYPE_MNGMT);
		boolean iscurrentworkspacemanager = ampCurrentMember.getAmpMemberRole().getTeamHead();
		
		//If the current user is part of the management workspace or is not the workspace manager of a workspace that's not management then hide.
		hForm.setEnableadvanceoptions(!ispartofamanagetmentworkspace & iscurrentworkspacemanager);
		
		return mapping.findForward("forward");
	}
}