/*
 * SaveQuickLink.java
 * Created: 13-July-2005 
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveQuickLink extends Action {
    private static Logger logger = Logger.getLogger(SaveQuickLink.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        throw new RuntimeException("not implemented!");
//      HttpSession session = request.getSession();
//        String tempId = request.getParameter("id");
//        String linkName = request.getParameter("nm"); 
//        String link = request.getParameter("ln");
//        String action = request.getParameter("factn");
//        Long id = null;
//        
//        if (tempId != null && linkName != null && link != null) {
//            CMSContentItem cmsItem = new CMSContentItem();
//            
//          cmsItem.setDescription(" ");
//          byte file[] = new byte[1];
//          file[0] = 0;
//          cmsItem.setFile(file);            
//            cmsItem.setIsFile(false);
//            cmsItem.setTitle(linkName);
//            cmsItem.setUrl(link);            
//            if (action != null && action.equals("add")) {
//                id = new Long(Long.parseLong(tempId));
//                TeamMemberUtil.addLinkToMember(id,cmsItem);
//            } else if (action != null && action.equals("edit")) {
//                cmsItem.setId(Long.parseLong(tempId));
//                DbUtil.update(cmsItem);
//            }
//          Documents document = new Documents();
//          document.setDocId(new Long(cmsItem.getId()));
//          document.setTitle(cmsItem.getTitle());
//          document.setIsFile(cmsItem.getIsFile());
//          document.setFileName(cmsItem.getFileName());
//          document.setUrl(cmsItem.getUrl());
//          document.setDocDescription(cmsItem.getDescription());  
//          document.setDate(cmsItem.getDate());
//          Collection col = (Collection) session.getAttribute(
//                  Constants.MY_LINKS);
//          if (col == null) {
//              col = new ArrayList();
//          }
//          col.add(document);
//          session.setAttribute(Constants.MY_LINKS,col);
//        }
//        return mapping.findForward("forward");
    }
}
 
