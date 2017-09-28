package org.digijava.module.contentrepository.action;

import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
/**
 * Used to check where given type of documents exist
 * @author Dare
 *
 */
public class GetResourcesInfo extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
         DocumentManagerForm myForm     = (DocumentManagerForm) form;
         Boolean exists=true;
         HttpSession    httpSession     = request.getSession();
         TeamMember teamMember      = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        
         Session jcrWriteSession        = DocumentManagerUtil.getWriteSession(request);
         String tabType= request.getParameter("type");
         if(tabType.equals("private")){
             exists = DocumentManagerUtil.privateDocumentsExist(jcrWriteSession, teamMember);
         }else if (tabType.equals("team")){
             exists = DocumentManagerUtil.teamDocumentsExist(jcrWriteSession, teamMember);
         }
            
        XMLDocument msgInfo = new XMLDocument();
        XML root = new XML("resource-info");
        root.addAttribute("docsExist", exists);
        root.addAttribute("tabType", tabType);

        msgInfo.addElement(root);
        msgInfo.output(response.getOutputStream());
        response.getOutputStream().close();
        return null;
    }
}
