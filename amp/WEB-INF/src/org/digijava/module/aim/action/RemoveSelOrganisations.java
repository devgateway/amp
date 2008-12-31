package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.OrgProjectId;

public class RemoveSelOrganisations extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		Long selOrgs[] = eaForm.getIdentification().getSelOrgs();
		if (selOrgs == null) {
			eaForm.setStep("1");
			return mapping.findForward("forward");
		}
		
		OrgProjectId prevOrgs[] = eaForm.getIdentification().getSelectedOrganizations();
		OrgProjectId currOrgs[] = null;

		if (prevOrgs != null && ((prevOrgs.length - selOrgs.length) != 0)) {
			currOrgs = new OrgProjectId[prevOrgs.length - selOrgs.length];
			int index = 0;
			for (int i = 0;i < prevOrgs.length;i ++) {
				boolean flag = false;
				for (int j = 0;j < selOrgs.length;j ++) {
					if (prevOrgs[i] != null){
					if (prevOrgs[i].getId().equals(selOrgs[j])) {
						flag = true;
						break;
					}
					//flag = true;
					//break;
				 }
				}
				if (!flag) {
					currOrgs[index++] = prevOrgs[i];
				}
			}			
		}
		
		eaForm.getIdentification().setSelectedOrganizations(currOrgs);
		eaForm.getIdentification().setSelOrgs(null);
		eaForm.setStep("1");
		return mapping.findForward("forward");
	}
}
