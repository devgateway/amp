package org.digijava.module.contentrepository.action;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.contentrepository.dbentity.CrSharedDoc;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.message.triggers.RejectResourceSharetrigger;

public class RejectResource extends DispatchAction {
    
     public ActionForward rejectShare(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
         DocumentManagerForm myForm = (DocumentManagerForm) form;
         HttpSession    httpSession     = request.getSession();
         TeamMember teamMember      = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
         String nodeBaseUUID=request.getParameter("uuid");
         CrSharedDoc sharedDoc=DocumentManagerUtil.getCrSharedDoc(nodeBaseUUID, teamMember.getTeamId(), CrConstants.PENDING_STATUS);
         String sharedPrivateResourceVersionUUID=sharedDoc.getSharedNodeVersionUUID();
         DbUtil.delete(sharedDoc);
         //create approval
         Node node=DocumentManagerUtil.getReadNode(sharedPrivateResourceVersionUUID, request);
         new RejectResourceSharetrigger(node);
         DocumentManagerUtil.logoutJcrSessions(request);
         request.getSession().setAttribute("resourcesTab", request.getParameter("type"));
         return null;
    }
}
