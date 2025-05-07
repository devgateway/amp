package org.digijava.module.aim.helper;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrgRecipient;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.uicomponents.IPostProcessDelegate;
import org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RecipientPostProcessDelegate implements IPostProcessDelegate {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        selectOrganizationComponentForm selOrgForm = (selectOrganizationComponentForm) form;

        Field target = selOrgForm.getTargetForm().getClass().getDeclaredField(selOrgForm.getTargetCollection());
        target.setAccessible(true);

        List<AmpOrgRecipient> recipients = (List<AmpOrgRecipient>) target.get(selOrgForm.getTargetForm());

        if (selOrgForm.getAllSelectedOrgsIds() == null || selOrgForm.getAllSelectedOrgsIds().size() == 0) {
            return mapping.findForward("forward");
        }


        List<Long> selectedOrgs = selOrgForm.getAllSelectedOrgsIds();
        if (recipients == null) {
            recipients = new ArrayList<AmpOrgRecipient>();
            for (Long selectedOrgId : selectedOrgs) {
                AmpOrgRecipient recipient = new AmpOrgRecipient();
                AmpOrganisation org = DbUtil.getOrganisation(selectedOrgId);
                recipient.setDescription(null);
                recipient.setOrganization(org);
                recipients.add(recipient);
            }
        } else {
            for (Long selectedOrgId : selectedOrgs) {
                boolean exists = false;

                for (AmpOrgRecipient orgRecipient : recipients) {
                    if (orgRecipient.getOrganization().getAmpOrgId().equals(selectedOrgId)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    AmpOrgRecipient recipient = new AmpOrgRecipient();
                    AmpOrganisation org = DbUtil.getOrganisation(selectedOrgId);
                    recipient.setDescription(null);
                    recipient.setOrganization(org);
                    recipients.add(recipient);
                }

            }

        }
        target.set(selOrgForm.getTargetForm(), recipients);
        selOrgForm.setAfterSelect(true);


        return mapping.findForward("forward");

    }
}
