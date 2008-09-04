package org.digijava.module.aim.uicomponents;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.FundingOrganization;
import org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm;
import org.digijava.module.aim.util.DbUtil;

public class ToFundingOrganizationDelegate implements IPostProcessDelegate {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

		if (eaForm.getMultiSelect()) {
			Field target = eaForm.getTargetForm().getClass().getDeclaredField(eaForm.getTargetCollection());
			target.setAccessible(true);
			Collection<FundingOrganization> col = (Collection<FundingOrganization>) target.get(eaForm.getTargetForm());
			if (col == null)
				col = new ArrayList<FundingOrganization>();

			Long[] selected = eaForm.getSelOrganisations();
			for (int i = 0; i < selected.length; i++) {
				AmpOrganisation org = DbUtil.getOrganisation(selected[i]);
				FundingOrganization fOrg = new FundingOrganization();
				fOrg.setAmpOrgId(org.getAmpOrgId());
				fOrg.setOrgName(org.getName());
				if (!col.contains(fOrg)) {
					col.add(fOrg);
				}
			}
			target.set(eaForm.getTargetForm(), col);

		} else {

			Field target = eaForm.getTargetForm().getClass().getDeclaredField(eaForm.getTargetProperty());
			target.setAccessible(true);
			Collection<FundingOrganization> col = (Collection<FundingOrganization>) target.get(eaForm.getTargetForm());

			Long prevId = Long.parseLong(eaForm.getAditionalParameters().get("id"));
			Long id = Long.parseLong(request.getParameter("id"));
			AmpOrganisation orgSelected = DbUtil.getOrganisation(id);

			for (FundingOrganization fundingOrganization : col) {
				if (fundingOrganization.getAmpOrgId().compareTo(prevId)==0) {
					fundingOrganization.setAmpOrgId(orgSelected.getAmpOrgId());
					fundingOrganization.setOrgName(orgSelected.getName());
					break;
				}
			}
			target.set(eaForm.getTargetForm(), col);

		}
		eaForm.setAfterSelect(true);
		return mapping.findForward("forward");
	}
}
