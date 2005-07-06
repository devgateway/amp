/*
 * ShowAddComponent.java
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
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.Components;


public class ShowAddComponent extends Action {
	
	private static Logger logger = Logger.getLogger(ShowAddComponent.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		if (request.getParameter("componentReset") != null
				&& request.getParameter("componentReset").equals("false")) {
			eaForm.setComponentReset(false);
		} else {
			eaForm.setComponentReset(true);
			eaForm.reset(mapping, request);
		}

		if (request.getParameter("id") != null) {
			try {
				long id = Long.parseLong(request.getParameter("id"));
				Long cId = new Long(id);
				Collection selComps = eaForm.getSelectedComponents();
				if (selComps != null && selComps.size() > 0) {
					Iterator itr = selComps.iterator();
					while (itr.hasNext()) {
						Components comp = (Components) itr.next();
						if (comp.getComponentId().equals(cId)) {
							eaForm.setComponentTitle(comp.getTitle());
							eaForm.setComponentAmount(comp.getAmount());
							eaForm.setComponentDesc(comp.getDescription());
							eaForm.setComponentRepDate(comp.getReportingDate());
							eaForm.setComponentId(comp.getComponentId());
							eaForm.setCurrencyCode(comp.getCurrencyCode());
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("Exception :" + e);
			}
		}
		eaForm.setCurrencies(DbUtil.getAmpCurrency());
		return mapping.findForward("forward");
	}
}