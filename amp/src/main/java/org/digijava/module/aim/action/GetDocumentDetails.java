/***
 * GetDocumentDetails.java
 * @author Priyajith 
 */
  
package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.form.RelatedLinksForm;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/***
 * Fetch the Document and activity details provided the document Id and activity Id 
 */

public class GetDocumentDetails
extends Action {
    
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        
        if (session.getAttribute("currentMember") == null) // User not logged in
            return mapping.findForward("index");
        
        if (request.getParameter("uuid") == null || 
                request.getParameter("actId") == null) // invalid url parameters specified 
            return mapping.findForward("index");
        
        Long aId = null;
        int pageId = 0;
        String uuid = request.getParameter("uuid");
        try {
            String actId = request.getParameter("actId");
            aId = new Long(Long.parseLong(actId));
            pageId = Integer.parseInt(request.getParameter("pageId"));
        } catch (NumberFormatException ex) {
            return mapping.findForward("index");
        }
        
        RelatedLinksForm rlForm = (RelatedLinksForm) form;
        
        if (uuid != null) {
            Documents document = new Documents();
            NodeWrapper nodeWrapper = new NodeWrapper(org.digijava.module.contentrepository.util.DocumentManagerUtil.getReadNode(uuid, request));
            
            AmpActivityVersion activity = ActivityUtil.loadAmpActivity(aId);
            document.setActivityId(activity.getAmpActivityId());
            document.setActivityName(activity.getName());
            document.setTitle(nodeWrapper.getTitle());
            document.setDocDescription(nodeWrapper.getDescription());
            document.setDate(nodeWrapper.getDate());
            document.setUuid(nodeWrapper.getUuid());
            document.setIsFile((nodeWrapper.getWebLink() == null)?true:false);
            document.setFileName(nodeWrapper.getName());
            document.setUrl(nodeWrapper.getWebLink());
            rlForm.setDocument(document);
            DocumentManagerUtil.logoutJcrSessions(request);
        }

        rlForm.setPageId(pageId);     
        if (pageId == 0) {
            return mapping.findForward("forward1");
        } else {
            return mapping.findForward("forward2"); 
        }
    }
}
