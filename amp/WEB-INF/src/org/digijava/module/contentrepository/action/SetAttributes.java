/**
 * 
 */
package org.digijava.module.contentrepository.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.form.SetAttributesForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentsNodesAttributeManager;

/**
 * @author Alex Gartner
 *
 */
public class SetAttributes extends Action {
    private SetAttributesForm myForm = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {

        myForm = (SetAttributesForm) form;

        if (myForm.getAction() != null && myForm.getAction().equals(CrConstants.MAKE_PUBLIC)) {
            makePublic(request, myForm.getUuid());
        }
        
        if (myForm.getAction() != null && myForm.getAction().equals(CrConstants.UNPUBLISH)) {
            unpublish(myForm.getUuid());
        }

        request.getSession().setAttribute("resourcesTab", myForm.getType());

        return null;
    }

    private void makePublic(HttpServletRequest request, String uuid) {
        DocumentsNodesAttributeManager.getInstance().makeDocumentPublic(request, uuid);
    }

    public void unpublish(String uuid) {
        DocumentsNodesAttributeManager.getInstance().deleteDocumentNodesAttributes(uuid);
    }
}
