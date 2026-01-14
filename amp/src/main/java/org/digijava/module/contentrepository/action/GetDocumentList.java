/**
 * 
 */
package org.digijava.module.contentrepository.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alex Gartner
 *
 */
public class GetDocumentList extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        return mapping.findForward("forward");
    }
    
}
