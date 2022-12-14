package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.form.ChangePasswordForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.jfree.util.Log;

public class ShowChangePassword extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        
                java.lang.Exception {
        ChangePasswordForm cpForm = (ChangePasswordForm) form;

        TeamMember tm = TeamUtil.getCurrentMember();

        if (tm != null && tm.getEmail() != null) {
            cpForm.setUserId(tm.getEmail());
            cpForm.setUserIdEnabled(false);
        }else {
            cpForm.setUserIdEnabled(true);
        }

        return mapping.findForward("forward");
    }
}

