/*
 * GetUnAssignedWorkspaces.java @Author Priyajith C Created: 07-Apr-2005
 * Modified by Akashs 22-Feb-2006
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

/**
 * The action class will retrieve all workspaces which is not associated with
 * any parent team. It also filters the workspaces based on the 'Workspace Type' 
 * & 'Team Category'
 */
public class GetUnAssignedWorkspaces extends Action {
    
    private static Logger logger = Logger.getLogger(GetUnAssignedWorkspaces.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        if (null != form) {
            
            UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
            
            String dest = request.getParameter("dest");
            String workspaceType = request.getParameter("wType");
            //String teamCategory  = request.getParameter("tCategory");
            
            //workaround :(
//          if (team!=null && team.toLowerCase().equals("mofed")){
//              team="GOVERNMENT";
//          }
            uwForm.setActionType(null);
            uwForm.setSelChildOrgs(null);
            if(request.getParameter("childorgs")!=null){
/* Search feature start */
                Collection col = null;
                col = new ArrayList();
                
                uwForm.setOrgTypes(DbUtil.getAllOrgTypes());
                if (uwForm.getAmpOrgTypeId() != null && !uwForm.getAmpOrgTypeId().equals(new Long(-1))) {
                    if (uwForm.getKeyword().trim().length() != 0) {
                        // serach for organisations based on the keyword and the
                        // organisation type
                        col = DbUtil.searchForOrganisation(uwForm.getKeyword().trim(),
                                uwForm.getAmpOrgTypeId());
                    } else {
                        // search for organisations based on organisation type only
                        col = DbUtil.searchForOrganisationByType(uwForm.getAmpOrgTypeId());
                    }
                } else if (uwForm.getKeyword() != null && uwForm.getKeyword().trim().length() != 0) {
                    // search based on the given keyword only.
                    col = DbUtil.searchForOrganisation(uwForm.getKeyword().trim());
                } else {
                    // get all organisations since keyword field is blank and org type field has 'ALL'.
                    col = DbUtil.getAmpOrganisations();
                }
                uwForm.setAllOrganizations(col);
/* Search feature end */
                uwForm.setActionType("addOrgs");
            }
            else{
                uwForm.setActionType(null);
//              Long typeId = null;
//              if (teamCategory != null) {
//                  typeId  = new Long(teamCategory);
//              }
            
            if ((workspaceType == null || workspaceType.trim().length() == 0) ) {
                uwForm.setChildWorkspaceType(null);
                uwForm.setChildTeamTypeId(null);
            }
            
            Collection col = TeamUtil.getUnassignedWorkspaces(workspaceType);
            logger.debug("Unassigned workspaces retreived, size = " + col.size());
            
            uwForm.setAvailChildWorkspaces(col);
            }
            uwForm.setReset(false);
            //uwForm.setActionEvent(dest);
            uwForm.setDest(dest);

            return mapping.findForward("forward");
        }
        
        return null;
    }
}
