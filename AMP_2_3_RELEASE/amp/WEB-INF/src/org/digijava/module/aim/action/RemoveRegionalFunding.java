package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.RegionalFunding;

public class RemoveRegionalFunding extends Action {
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		Long selFund[] = eaForm.getFunding().getSelRegFundings();
		for (int i = 0;i < selFund.length;i ++) {
			RegionalFunding rf = new RegionalFunding();
			rf.setRegionId(selFund[i]);
			eaForm.getFunding().getRegionalFundings().remove(rf);
		}
		eaForm.getFunding().setSelRegFundings(null);
		return mapping.findForward("forward");
	}
}

