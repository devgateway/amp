/*
 * RemoveChildWorkspace.java @Author Priyajith C Created: 07-Apr-2005
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.UpdateWorkspaceForm;

public class RemoveChildWorkspace extends Action {
    
    private static Logger logger = Logger.getLogger(RemoveChildWorkspace.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
        
        String dest = request.getParameter("dest");
        logger.debug("In Remove child workspace");
        String id = request.getParameter("tId");
        
        if(request.getParameter("childorgs")!=null){
            Long orgId=new Long(id);
            Iterator i=uwForm.getOrganizations().iterator();
            while (i.hasNext()) {
                AmpOrganisation org = (AmpOrganisation) i.next();
                if(org.getAmpOrgId().equals(orgId)) i.remove();
            }
        
        } else
        
        if (id != null && id.trim().length() > 0) {
            long temp = Long.parseLong(id);
            Long teamId = new Long(temp);
            if (uwForm.getChildWorkspaces() != null) {
                if(uwForm.getDeletedChildWorkspaces() == null)
                    uwForm.setDeletedChildWorkspaces(new ArrayList<Long>());
                Iterator<AmpTeam> i= uwForm.getChildWorkspaces().iterator();
                while (i.hasNext()) {
                    AmpTeam team = (AmpTeam) i.next();
                    
                    if(team.getAmpTeamId().equals(teamId)){
                        i.remove();
                        uwForm.getDeletedChildWorkspaces().add(teamId);
                    } 
                }
            }
        }
        
        return mapping.findForward(dest);
    }
}
