package org.digijava.module.aim.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;

public class ChangePasswordForm extends ValidatorForm {

          private String userId = null;
          private String oldPassword = null;
          private String newPassword = null;
          private String conformNewPassword = null;

          private boolean userIdEnabled = true;

          public boolean getUserIdEnabled() {
              return userIdEnabled;
          }

          public void setUserIdEnabled(boolean userIdEnabled) {
              this.userIdEnabled = userIdEnabled;
          }
          public String getUserId() {
                     return this.userId;
          }

          public void setUserId(String userId) {
                     this.userId = userId;
          }

          public String getOldPassword() {
                     return this.oldPassword;
          }

          public void setOldPassword(String oldPassword) {
                     this.oldPassword = oldPassword;
          }

          public String getNewPassword() {
                     return this.newPassword;
          }

          public void setNewPassword(String newPassword) {
                     this.newPassword = newPassword;
          }       

          public String getConfirmNewPassword() {
                     return this.conformNewPassword;
          }

          public void setConfirmNewPassword(String conformNewPassword) {
                     this.conformNewPassword = conformNewPassword;
          }       


         public ActionErrors validate( ActionMapping mapping,HttpServletRequest request) {
                    
                    return super.validate(mapping, request);                    
                    
                    /*
                    ActionErrors errors =  super.validate(mapping, request);

                    if ( (this.getNewPassword() != null) &&
                                         this.getNewPassword().trim().length() != 0) {
                              if (! (this.getNewPassword().equals(this.getConfirmNewPassword()))) {
                                         ActionMessage error = new ActionMessage(
                                                              "error.registration.NoPasswordMatch");
                                         errors.add(null, error);
                              }
                    }

                    return errors.isEmpty() ? null : errors;                    

                    */

         }
}
