package org.digijava.module.contentrepository.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.contentrepository.dbentity.CrSharedDoc;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class UnshareDocument extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
        DocumentManagerForm myForm = (DocumentManagerForm) form;
        String nodeUUID=request.getParameter("uuid"); //holds base node uuid or sharedversion id, depending on the tab      
        if(nodeUUID!=null){
            Node nodeThatIsUnshared=DocumentManagerUtil.getReadNode(nodeUUID, request);
            List<CrSharedDoc> sharedDocs=new ArrayList<CrSharedDoc>();
            /**
             * if tm from owner workspace clicked unshare, all records that should be removed, otherwise record only for given team
             */
            TeamMember currentMember=getCurrentTeamMember(request);
            Long nodeCreatorTeamId=nodeThatIsUnshared.getProperty(CrConstants.PROPERTY_CREATOR_TEAM).getLong();
            if(currentMember.getTeamId().longValue()==nodeCreatorTeamId){
                sharedDocs=DocumentManagerUtil.getSharedDocsForGivenNodeUUID(nodeUUID);             
            }else{
                CrSharedDoc retVal=DocumentManagerUtil.loadTeamResourceSharedWithWorkspace(nodeUUID, currentMember.getTeamId());
                if(retVal!=null){
                    sharedDocs.add(retVal);
                }
            }           
            if(sharedDocs!=null && sharedDocs.size()>0){
                for (CrSharedDoc sharedDoc : sharedDocs) {
                    /**
                     * unshare for now happens only from team resources or shared resources tabs, not from private space.
                     * in case it will be allowed from private space, then team node should be removed too and not only sharedDoc entries 
                     */
                    DbUtil.delete(sharedDoc);
                }
            }
        }
        DocumentManagerUtil.logoutJcrSessions(request);
        request.getSession().setAttribute("resourcesTab", myForm.getType());
        return null;
    }
    
    private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
        HttpSession httpSession     = request.getSession();
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        return teamMember;
    }
}
