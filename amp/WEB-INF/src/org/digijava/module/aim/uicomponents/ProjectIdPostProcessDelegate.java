package org.digijava.module.aim.uicomponents;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Date;

public class ProjectIdPostProcessDelegate implements IPostProcessDelegate {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

        Field target = eaForm.getTargetForm().getClass().getDeclaredField(eaForm.getTargetCollection());
        target.setAccessible(true);

        OrgProjectId prevOrgs[] = (OrgProjectId[]) target.get(eaForm.getTargetForm());

        if (eaForm.getAllSelectedOrgsIds() == null || eaForm.getAllSelectedOrgsIds().size() == 0)
            return mapping.findForward("forward");

        int index = 0;
        OrgProjectId[] currOrgs;
        if (prevOrgs != null) {
            currOrgs = new OrgProjectId[prevOrgs.length + eaForm.getAllSelectedOrgsIds().size()];
            for (int i = 0; i < prevOrgs.length; i++) {
                currOrgs[i] = prevOrgs[i];
            }
            index = prevOrgs.length;
        } else {
            currOrgs = new OrgProjectId[eaForm.getAllSelectedOrgsIds().size()];
        }

        for (int i = 0; i < eaForm.getAllSelectedOrgsIds().size(); i++) {
            boolean flag = false;
            if (prevOrgs != null) {
                for (int j = 0; j < prevOrgs.length; j++) {
                    if (prevOrgs[j] != null) {
                        if (prevOrgs[j].getOrganisation().getAmpOrgId().equals(eaForm.getAllSelectedOrgsIds().get(i))) {
                            flag = true;
                            break;
                        }
                    }
                }
            }

            if (!flag) {
                AmpOrganisation org = DbUtil.getOrganisation(eaForm.getAllSelectedOrgsIds().get(i));
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
