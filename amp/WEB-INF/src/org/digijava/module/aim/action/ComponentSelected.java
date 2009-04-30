/*
 * ComponentSelected.java
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
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
import org.digijava.module.aim.helper.DecimalToText;

public class ComponentSelected extends Action {
	
	private static Logger logger = Logger.getLogger(ComponentSelected.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) {
		
		EditActivityForm eaForm = (EditActivityForm) form;
		
		if (eaForm.getComponents().getComponentTitle() == null || eaForm.getComponents().getComponentTitle().trim().length() == 0) {
			return mapping.findForward("forward");
		}

		Components comp = new Components();
		if (eaForm.getComponents().getComponentAmount() == null || eaForm.getComponents().getComponentAmount().trim().length() == 0) {
			comp.setAmount("0.00");
		} else {
			String amt = eaForm.getComponents().getComponentAmount().replaceAll(",","");
			double amount = Double.parseDouble(amt);
			comp.setAmount(DecimalToText.ConvertDecimalToText(amount));
		}
		comp.setCurrencyCode(eaForm.getComponents().getCurrencyCode());
		comp.setDescription(eaForm.getComponents().getComponentDesc());
		comp.setReportingDate(eaForm.getComponents().getComponentRepDate());
		comp.setTitle(eaForm.getComponents().getComponentTitle());
		comp.setCurrencyCode(eaForm.getComponents().getCurrencyCode());
		
		if (eaForm.getComponents().getComponentId() != null &&
				eaForm.getComponents().getComponentId().intValue() != 0) {
			if (eaForm.getComponents().getSelectedComponents() != null) {
				Iterator itr = eaForm.getComponents().getSelectedComponents().iterator();
				while (itr.hasNext()) {
					Components temp = (Components) itr.next();
					if (temp.getComponentId().equals(eaForm.getComponents().getComponentId())) {
						comp.setPhyProgress(temp.getPhyProgress());
						break;
					}
				}
			}
			comp.setComponentId(eaForm.getComponents().getComponentId());
			eaForm.getComponents().getSelectedComponents().remove(new Components(eaForm.getComponents().getComponentId()));
		} else {
			comp.setComponentId(new Long(System.currentTimeMillis()));
		}
		if (eaForm.getComponents().getSelectedComponents() == null) {
			eaForm.getComponents().setSelectedComponents(new ArrayList());
		} 
		eaForm.getComponents().getSelectedComponents().add(comp);
		eaForm.getComponents().setComponentId(null);
		return mapping.findForward("forward");
	}
}
