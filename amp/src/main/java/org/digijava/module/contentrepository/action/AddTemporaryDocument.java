/**
 * 
 */
package org.digijava.module.contentrepository.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;

/**
 * @author Alex Gartner
 *
 */
public class AddTemporaryDocument extends Action {
    private DocumentManagerForm myForm;
    private ActionMapping myMapping;
    private HttpServletRequest myRequest;
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        myForm          = (DocumentManagerForm) form;
        myRequest       = request;
        myMapping       = mapping;
        
        if ( myForm.getDocTitle() != null ) {
            return addTemporaryDocument(); 
        }
        else
            return showAddForm();
        
    }
    
    private ActionForward addTemporaryDocument () {
        ActionMessages errors               = new ActionMessages();
        TemporaryDocumentData tempDoc   = new TemporaryDocumentData(myForm, myRequest, errors);
        
        if ( !tempDoc.isErrorsFound() ) {
            tempDoc.addToSession( myRequest );
            myForm.setPageCloseFlag( true );
        }
        saveErrors(myRequest, errors);
        
        return myMapping.findForward("forward");
    } 
    
    private ActionForward showAddForm () {
        if ("true".equalsIgnoreCase( myRequest.getParameter("webResource") ) )
            myForm.setWebResource(true);
        else
            myForm.setWebResource(false);
        
        return myMapping.findForward("forward");
    }
}
