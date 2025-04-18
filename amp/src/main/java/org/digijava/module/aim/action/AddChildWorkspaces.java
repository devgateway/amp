/*
 * AddChildWorkspaces.java @Author Priyajith C Created: 07-Apr-2005
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.UpdateWorkspaceForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

public class AddChildWorkspaces extends Action {
    
    private static Logger logger = Logger.getLogger(AddChildWorkspaces.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        UpdateWorkspaceForm uwForm = (UpdateWorkspaceForm) form;
        uwForm.setReset(false);
        String dest = request.getParameter("dest");
        uwForm.setActionType(null);
    if(request.getParameter("childorgs")!=null){
        if (uwForm.getSelChildOrgs() != null && uwForm.getSelChildOrgs().length > 0) {
            logger.info("Selchildworkspaces.length :" + uwForm.getSelChildOrgs().length);
            ArrayList allOrgs = (ArrayList) DbUtil.getAll(AmpOrganisation.class);
            if (uwForm.getOrganizations() == null) {
                logger.debug("childWorkspace is null");
                uwForm.setOrganizations(new ArrayList());
            } else {
                logger.debug("Child workspaces size = " + uwForm.getOrganizations().size());
            }
            Long [] auxIdOrgs=uwForm.getSelChildOrgs();
            
            ArrayList tmpOrg=new ArrayList();
            ArrayList tmpOrgAux=new ArrayList();
            ArrayList tmpOrgUnsel=new ArrayList();
            ArrayList auxIdOrgCol=new ArrayList();
            for(int i=0; i<auxIdOrgs.length;i++)
                auxIdOrgCol.add(auxIdOrgs[i]);
            for (Iterator it = allOrgs.iterator(); it.hasNext();) {
                AmpOrganisation orgAux = (AmpOrganisation) it.next();
                Long idOrgAux=orgAux.getAmpOrgId();
                if(auxIdOrgCol.contains(idOrgAux)==true)
                    {
                        tmpOrg.add(orgAux);
                    }
                else ;
            }
            TreeSet orgs=new TreeSet();
            orgs.addAll(tmpOrg);
            orgs.addAll(uwForm.getOrganizations());
            uwForm.setOrganizations(new ArrayList());
            if(orgs!=null)
                uwForm.getOrganizations().addAll(orgs);
        }
    }
    else
    {
        uwForm.setActionType(null);
        if (uwForm.getSelChildWorkspaces() != null && uwForm.getSelChildWorkspaces().length > 0) {
            logger.info("Selchildworkspaces.length :" + uwForm.getSelChildWorkspaces().length);
            Collection teams = TeamUtil.getAllTeams(uwForm.getSelChildWorkspaces());
            if (uwForm.getChildWorkspaces() == null) {
                logger.debug("childWorkspace is null");
                uwForm.setChildWorkspaces(new ArrayList());
            } else {
                logger.debug("Child workspaces size = " + uwForm.getChildWorkspaces().size());
            }
            
            ArrayList temp = new ArrayList(teams);
            for (int i= 0;i < temp.size();i ++) {
                Iterator itr1 = uwForm.getChildWorkspaces().iterator();
                boolean flag = false;
                AmpTeam childTeam = (AmpTeam) temp.get(i);          
                while (itr1.hasNext()) {
                    AmpTeam team = (AmpTeam) itr1.next();
                    if (team.getAmpTeamId().equals(childTeam.getAmpTeamId())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag && childTeam != null) {
                    uwForm.getChildWorkspaces().add(childTeam);
                }
            }       
        }
    }
        
        return mapping.findForward(dest);
    }
}
