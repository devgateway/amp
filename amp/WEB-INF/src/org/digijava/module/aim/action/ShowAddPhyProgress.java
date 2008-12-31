/*******************************************************************************
 * @author Priyajith
 * @version 0.1 Created on 19/01/2005
 */

package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.PhysicalProgress;

public class ShowAddPhyProgress extends Action {

	private static Logger logger = Logger.getLogger(ShowAddPhyProgress.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		EditActivityForm eaForm = (EditActivityForm) form;

		if (request.getParameter("phyProgReset") != null
				&& request.getParameter("phyProgReset").equals("false")) {
			eaForm.getPhisycalProgress().setPhyProgReset(false);
		} else {
			eaForm.getPhisycalProgress().setPhyProgReset(true);
			eaForm.reset(mapping, request);
		}

		Long compId = null;
		if (request.getParameter("comp") != null) {
			try {
				compId = new Long(Long.parseLong(request.getParameter("comp")));
				logger.info("setting compId to " + compId);
			} catch (Exception e) {
				logger.error("Exception :" + e);
				return mapping.findForward("forward");
			}
		} else {
			return mapping.findForward("forward");
		}
		logger.info("request.getParameter(id) :" + request.getParameter("id"));
		logger.info("request.getParameter(comp) :" + request.getParameter("comp"));
		
		Components comp = null;
		boolean flag = false;
		if (eaForm.getComponents().getSelectedComponents() != null) {
			Iterator itr = eaForm.getComponents().getSelectedComponents().iterator();
			while (itr.hasNext()) {
				comp = (Components) itr.next();
				if (comp.getComponentId().equals(compId)) {
					flag = true;
					break;
				}
			}
		}
		
		if (!flag) return mapping.findForward("forward");
		
		if (request.getParameter("id") != null) {
			try {
				long id = Long.parseLong(request.getParameter("id"));
				Long pId = new Long(id);
				Collection selPhyProg = comp.getPhyProgress();
				if (selPhyProg != null && selPhyProg.size() > 0) {
					Iterator itr = selPhyProg.iterator();
					while (itr.hasNext()) {
						PhysicalProgress phyProg = (PhysicalProgress) itr.next();
						if (phyProg.getPid().equals(pId)) {
							eaForm.getPhisycalProgress().setPhyProgTitle(phyProg.getTitle());
							eaForm.getPhisycalProgress().setPhyProgDesc(phyProg.getDescription());
							eaForm.getPhisycalProgress().setPhyProgRepDate(phyProg.getReportingDate());
							logger.info("phyProg.getPid() :"+ phyProg.getPid());
							eaForm.getPhisycalProgress().setPhyProgId(phyProg.getPid());
							logger.info("setting form bean value for compId to " + compId);
							eaForm.getComponents().setComponentId(compId);
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("Exception :" + e);
			}
		}

		logger.info("setting form bean value for compId to " + compId);
		eaForm.getComponents().setComponentId(compId);
		return mapping.findForward("forward");
	}
}
