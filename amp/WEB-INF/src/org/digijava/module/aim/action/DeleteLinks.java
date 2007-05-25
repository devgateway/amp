/*
 * DeleteLinks.java
 * Created : 13-July-2005 
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.MyDesktopForm;
import org.digijava.module.aim.helper.Documents;
import org.digijava.module.aim.util.TeamMemberUtil;


public class DeleteLinks extends Action {
    private static Logger logger = Logger.getLogger(DeleteLinks.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        MyDesktopForm mdForm = (MyDesktopForm) form;
        TeamMemberUtil.removeMemberLinks(mdForm.getTeamMemberId(),mdForm.getSelLinks());
        Long temp[] = mdForm.getSelLinks();
        for (int i = 0;i < temp.length;i ++) {
            mdForm.getDocuments().remove(new Documents(temp[i]));    
        }
        
        return mapping.findForward("forward");
    }
}