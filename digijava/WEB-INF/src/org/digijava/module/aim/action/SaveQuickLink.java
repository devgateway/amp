/*
 * SaveQuickLink.java
 * Created: 13-July-2005 
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;

public class SaveQuickLink extends Action {
    private static Logger logger = Logger.getLogger(SaveQuickLink.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        String tempId = request.getParameter("id");
        String linkName = request.getParameter("nm"); 
        String link = request.getParameter("ln");
        String action = request.getParameter("factn");
        Long id = null;
        
        if (tempId != null && linkName != null && link != null) {
            CMSContentItem cmsItem = new CMSContentItem();
            
			cmsItem.setDescription(" ");
			byte file[] = new byte[1];
			file[0] = 0;
			cmsItem.setFile(file);            
            cmsItem.setIsFile(false);
            cmsItem.setTitle(linkName);
            cmsItem.setUrl(link);            
            if (action != null && action.equals("add")) {
                id = new Long(Long.parseLong(tempId));
                TeamUtil.addLinkToMember(id,cmsItem);
            } else if (action != null && action.equals("edit")) {
                cmsItem.setId(Long.parseLong(tempId));
                DbUtil.update(cmsItem);
            }
        }
        return mapping.findForward("forward");
    }
}
 
