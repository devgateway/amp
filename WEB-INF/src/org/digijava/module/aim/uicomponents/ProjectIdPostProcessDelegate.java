package org.digijava.module.aim.uicomponents;

import java.lang.reflect.Field;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm;
import org.digijava.module.aim.util.DbUtil;

public class ProjectIdPostProcessDelegate implements IPostProcessDelegate {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

		Field target = eaForm.getTargetForm().getClass().getDeclaredField(eaForm.getTargetCollection());
		target.setAccessible(true);

		OrgProjectId prevOrgs[] = (OrgProjectId[]) target.get(eaForm.getTargetForm());

		if (eaForm.getSelOrganisations() == null || eaForm.getSelOrganisations().length == 0)
			return mapping.findForward("forward");

		int index = 0;
		OrgProjectId[] currOrgs;
		if (prevOrgs != null) {
			currOrgs = new OrgProjectId[prevOrgs.length + eaForm.getSelOrganisations().length];
			for (int i = 0; i < prevOrgs.length; i++) {
				currOrgs[i] = prevOrgs[i];
			}
			index = prevOrgs.length;
		} else {
			currOrgs = new OrgProjectId[eaForm.getSelOrganisations().length];
		}

		for (int i = 0; i < eaForm.getSelOrganisations().length; i++) {
			boolean flag = false;
			if (prevOrgs != null) {
				for (int j = 0; j < prevOrgs.length; j++) {
					if (prevOrgs[j] != null) {
						if (prevOrgs[j].getOrganisation().getAmpOrgId().equals(eaForm.getSelOrganisations()[i])) {
							flag = true;
							break;
						}
					}
				}
			}

			if (!flag) {
				AmpOrganisation org = DbUtil.getOrganisation(eaForm.getSelOrganisations()[i]);
				if (org != null) {
					OrgProjectId opId = new OrgProjectId();
					opId.setId(new Date().getTime());
					opId.setProjectId(null);
					opId.setOrganisation(org);

					currOrgs[index++] = opId;
				}
			}

			target.set(eaForm.getTargetForm(), currOrgs);
			eaForm.setAfterSelect(true);

		}
		return mapping.findForward("forward");

	}

}
