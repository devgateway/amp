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
		
		if (eaForm.getComponentTitle() == null || eaForm.getComponentTitle().trim().length() == 0) {
			return mapping.findForward("forward");
		}

		Components comp = new Components();
		if (eaForm.getComponentAmount() == null || eaForm.getComponentAmount().trim().length() == 0) {
			comp.setAmount("0.00");
		} else {
			String amt = eaForm.getComponentAmount().replaceAll(",","");
			double amount = Double.parseDouble(amt);
			comp.setAmount(DecimalToText.ConvertDecimalToText(amount));
		}
		comp.setCurrencyCode(eaForm.getCurrencyCode());
		comp.setDescription(eaForm.getComponentDesc());
		comp.setReportingDate(eaForm.getComponentRepDate());
		comp.setTitle(eaForm.getComponentTitle());
		comp.setCurrencyCode(eaForm.getCurrencyCode());
		
		if (eaForm.getComponentId() != null &&
				eaForm.getComponentId().intValue() != 0) {
			if (eaForm.getSelectedComponents() != null) {
				Iterator itr = eaForm.getSelectedComponents().iterator();
				while (itr.hasNext()) {
					Components temp = (Components) itr.next();
					if (temp.getComponentId().equals(eaForm.getComponentId())) {
						comp.setPhyProgress(temp.getPhyProgress());
						break;
					}
				}
			}
			comp.setComponentId(eaForm.getComponentId());
			eaForm.getSelectedComponents().remove(new Components(eaForm.getComponentId()));
		} else {
			comp.setComponentId(new Long(System.currentTimeMillis()));
		}
		if (eaForm.getSelectedComponents() == null) {
			eaForm.setSelectedComponents(new ArrayList());
		} 
		eaForm.getSelectedComponents().add(comp);
		eaForm.setComponentId(null);
		return mapping.findForward("forward");
	}
}
