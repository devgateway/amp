package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.aim.form.ChangePasswordForm;
import javax.servlet.http.*;

public class ChangePassword extends Action {

		  private static Logger logger = Logger.getLogger(ChangePassword.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {


					 ChangePasswordForm cpForm = (ChangePasswordForm) form;
					 
					 ActionErrors errors = new ActionErrors();

					 logger.debug("In change password");

					 if (cpForm.getUserId() != null && cpForm.getOldPassword() != null && cpForm.getNewPassword() != null) {
								try {
										  if (DbUtil.isRegisteredEmail(cpForm.getUserId()) != true) {
													 errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.aim.invalidUserId"));
													 saveErrors(request,errors);
													 return mapping.getInputForward();
										  }

										  if (DbUtil.isCorrectPassword(cpForm.getUserId(),cpForm.getOldPassword()) != true) {
													 errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("error.aim.invalidPassword"));
													 saveErrors(request,errors);
													 return mapping.getInputForward();
										  }
										  
										  DbUtil.updatePassword(cpForm.getUserId(),
																cpForm.getOldPassword(),
																cpForm.getNewPassword());
								} catch (Exception e) {
										  errors.add(ActionErrors.GLOBAL_ERROR,
																new ActionError("error.aim.cannotChangePassword"));
										  saveErrors(request,errors);
										  return mapping.getInputForward();
								}
								return mapping.findForward("success");
					 } else {
								return mapping.findForward("changePassword");
					 }
		  }
}
