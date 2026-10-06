package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.um.util.DbUtil;


/**
 *
 * @author medea
 */
public class ConfirmUserRegistration extends Action{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws

    java.lang.Exception {

        String id=request.getParameter("id");
                boolean successfully=DbUtil.registerUser(id);
                if(successfully){
        return mapping.findForward("forward");
                }
                else{
                    return mapping.findForward("index");
                }
    }
}
