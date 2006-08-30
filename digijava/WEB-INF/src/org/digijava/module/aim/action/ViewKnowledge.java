package org.digijava.module.aim.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.module.aim.form.KnowledgeForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.request.Site;
import java.util.ArrayList;


public class ViewKnowledge extends TilesAction {
	private static Logger logger = Logger.getLogger(ViewKnowledge.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session
				.getAttribute("currentMember");

		KnowledgeForm formBean = (KnowledgeForm) form;
		if (teamMember == null) {
			formBean.setValidLogin(false);
		} else {
			formBean.setValidLogin(true);
			String actId = request.getParameter("ampActivityId");

			Long id = null;
			if (actId != null) {
				try {
					long aId = Long.parseLong(actId);
					id = new Long(aId);
					formBean.setId(id);
				} catch (NumberFormatException nfe) {
					logger.error("Exception from ViewKnowledgeAction: " +
							"Trying to parse " + actId + " to Long");
				}
			}

            formBean.setManagedDocuments(null);
			if (id != null) {
				formBean.setDocuments(DbUtil.getKnowledgeDocuments(id));
                if (DocumentUtil.isDMEnabled()) {
                    AmpActivity activity = ActivityUtil.getAmpActivity(id);
                    Site currentSite = RequestUtils.getSite(request);
                    formBean.setManagedDocuments(DocumentUtil.
                                                 getDocumentsForActivity(
                        currentSite, activity));
                }
			}
		}

		return null;
	}
}
