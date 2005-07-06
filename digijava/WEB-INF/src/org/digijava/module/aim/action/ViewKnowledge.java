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


public class ViewKnowledge extends TilesAction {
	private static Logger logger = Logger.getLogger(ViewKnowledge.class);

	public ActionForward execute(ComponentContext context,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
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
				} catch (NumberFormatException nfe) {
					logger.error("Exception from ViewKnowledgeAction: " +
							"Trying to parse " + actId + " to Long");
				}
			}
			
			if (id != null) {
				formBean.setDocuments(DbUtil.getKnowledgeDocuments(id));
			}
		}

		return null;
	}
}
