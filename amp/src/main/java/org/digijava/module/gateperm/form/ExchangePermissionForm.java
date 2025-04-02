/**
 * 
 */
package org.digijava.module.gateperm.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * @author mihai
 *
 */
public class ExchangePermissionForm extends ActionForm {
    private Long[] permissions;
    private FormFile fileUploaded;

    @Override
    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        // TODO Auto-generated method stub
        super.reset(arg0, arg1);
        fileUploaded=null;
        permissions=null;

    }

    public Long[] getPermissions() {
        return permissions;
    }

    public void setPermissions(Long[] permissions) {
        this.permissions = permissions;
    }

    public FormFile getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(FormFile fileUploaded) {
        this.fileUploaded = fileUploaded;
    }


}
