package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion ;
import org.digijava.module.aim.form.KnowledgeForm;
import org.digijava.module.aim.helper.ActivityDocumentsUtil;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.gateperm.core.GatePermConst;


public class ViewKnowledge extends TilesAction {
	private static Logger logger = Logger.getLogger(ViewKnowledge.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		TeamMember teamMember = (TeamMember) session
				.getAttribute("currentMember");
		request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.VIEW);
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

				/* Content Repository */
				 AmpActivityVersion  act	 				= ActivityUtil.loadActivity(id);
				 SelectDocumentDM.clearContentRepositoryHashMap(request);
                 if (act.getActivityDocuments() != null) {
                 	ActivityDocumentsUtil.injectActivityDocuments(request, act.getActivityDocuments() );
                 }
				/* END - Content Repository */

                if (DocumentUtil.isDMEnabled()) {
                    AmpActivityVersion  activity = ActivityUtil.loadActivity(id);
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
