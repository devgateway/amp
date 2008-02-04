

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;

/**
 *
 * @author medea
 */
public class DeleteIPAContract extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {
    
            EditActivityForm eaf = (EditActivityForm) form;
            Integer indexId =eaf.getContrcatToRemove();
            eaf.getContracts().remove(indexId - 1);
            return mapping.findForward("forward");
    }

}
