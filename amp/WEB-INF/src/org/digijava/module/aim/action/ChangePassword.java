package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.security.PasswordPolicyValidator;
import org.digijava.module.aim.form.ChangePasswordForm;
import org.digijava.module.um.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangePassword extends Action {

    private static Logger logger = Logger.getLogger(ChangePassword.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

        ChangePasswordForm cpForm = (ChangePasswordForm) form;
        ActionMessages errors = new ActionMessages();
        if (!PasswordPolicyValidator.isValid(cpForm.getNewPassword(), cpForm.getUserId())) {

            errors.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("error.strong.validation"));
            saveErrors(request, errors);
            request.setAttribute(PasswordPolicyValidator.SHOW_PASSWORD_POLICY_RULES, true);
            return mapping.getInputForward();
        }

        if (cpForm.getUserId() != null && cpForm.getOldPassword() != null && cpForm.getNewPassword() != null) {
            try {
                if (!DbUtil.isRegisteredEmail(cpForm.getUserId())) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.invalidUserId"));
                    saveErrors(request, errors);
                    return mapping.getInputForward();
                }

                if (!DbUtil.isCorrectPassword(cpForm.getUserId(), cpForm.getOldPassword())) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.invalidPassword"));
                    saveErrors(request, errors);
                    return mapping.getInputForward();
                }

                DbUtil.updatePassword(cpForm.getUserId(), cpForm.getOldPassword(), cpForm.getNewPassword());


            } catch (Exception e) {
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.cannotChangePassword"));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }

            return mapping.findForward("success");
        } else {
            return mapping.findForward("changePassword");
        }
    }
}
