/*
 * AddAmpActivity.java
 */

package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.util.PermissionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * a shadow of the former page; remains active for some reason
 *
 * @author Priyajith
 */
public class AddAmpActivity extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

      HttpSession session = request.getSession();
      request.setAttribute(GatePermConst.ACTION_MODE, GatePermConst.Actions.EDIT);
      session.removeAttribute("returnSearch");
      TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
      PermissionUtil.putInScope(session, GatePermConst.ScopeKeys.CURRENT_MEMBER, teamMember);
      
      Long idForOriginalActivity = null; // AMP-9633
      EditActivityForm eaForm = (EditActivityForm) form;
      if(request.getSession().getAttribute("idForOriginalActivity")!=null)
      {
          idForOriginalActivity = (Long) request.getSession().getAttribute("idForOriginalActivity") ;
          request.getSession().removeAttribute("idForOriginalActivity");
          if(idForOriginalActivity.equals(new Long(-1))){ //create case
              eaForm.setActivityId(null);
          }else{ //edit case
              eaForm.setActivityId(idForOriginalActivity);
          }
      }
      request.setAttribute("actId", eaForm.getActivityId());
      
      return mapping.findForward("preview");
  }  
}
