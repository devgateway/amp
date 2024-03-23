package org.digijava.module.contentrepository.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentsToOrganisations;
import org.digijava.module.contentrepository.dbentity.CrSharedDoc;
import org.digijava.module.contentrepository.dbentity.NodeLastApprovedVersion;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.contentrepository.util.DocumentOrganizationManager;
import org.digijava.module.message.triggers.ApprovedResourceShareTrigger;
import org.digijava.module.message.triggers.PendingResourceShareTrigger;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
/**
 * @author dare
 */
public class ShareDocument extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception  {
        DocumentManagerForm myForm = (DocumentManagerForm) form;
        HttpSession httpSession     = request.getSession();
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        String shareWith=request.getParameter("shareWith");
        if(shareWith!=null){
            String nodeBaseUUID=request.getParameter("uuid"); //node's uuid which was requested to be shared
            Node node=DocumentManagerUtil.getReadNode(nodeBaseUUID, request);
            NodeWrapper tmpNodeWrapper = new NodeWrapper(node);
            AmpApplicationSettings sett = DbUtil.getTeamAppSettings(teamMember.getTeamId());
            
            if(shareWith.equals(CrConstants.SHAREABLE_WITH_TEAM)){//user is sharing resource from his private space !
                CrSharedDoc sharedDoc = null;
                
                AmpTeam team=TeamUtil.getAmpTeam(teamMember.getTeamId());
                boolean shareWithoutApprovalNeeded=((sett!=null && sett.getAllowAddTeamRes()!=null && sett.getAllowAddTeamRes().intValue()>=CrConstants.TEAM_RESOURCES_ADD_ALLOWED_WORKSP_MEMBER) || teamMember.getTeamHead());
                
                
                if(shareWithoutApprovalNeeded){
                    String sharedPrivateNodeVersionUUID=null;
                    sharedDoc = DocumentManagerUtil.getCrSharedDoc(node.getIdentifier(), teamMember.getTeamId(),
                            CrConstants.PENDING_STATUS);                 
                    if(sharedDoc!=null){
                        sharedPrivateNodeVersionUUID=sharedDoc.getSharedNodeVersionUUID();
                        DbUtil.delete(sharedDoc);
                    }
                    
                    Session jcrWriteSession     = DocumentManagerUtil.getWriteSession(request);
                    Node teamHomeNode = DocumentManagerUtil.getOrCreateTeamNode(jcrWriteSession,
                            teamMember.getTeamId());
                    
                    /**
                     * if tm shared document,which was yet unapproved by TL and in the meantime he(TM) added new version to this private document,
                     *  the version which he shared should become team doc(after TL clicks approve) and not the last version
                     */
                    if(sharedPrivateNodeVersionUUID!=null){
                        node=DocumentManagerUtil.getReadNode(sharedPrivateNodeVersionUUID, request);
                    }
                    NodeWrapper nodeWrapper     = new NodeWrapper(request, teamHomeNode , node,false); //create copy of private node to store as team one
                    
                    if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ) {

                        nodeWrapper.saveNode(jcrWriteSession);
                        List<Label> existingLabels  = tmpNodeWrapper.getLabels();
                        if (existingLabels != null && !existingLabels.isEmpty()) {
                            for (Label label : existingLabels) {
                                Node labelNode = DocumentManagerUtil.getWriteNode(label.getUuid(), request);
                                nodeWrapper.addLabel(labelNode);
                            }
                        }

                        //if TL approved TM's sharing resource, then new approval should be created
                        if(sharedPrivateNodeVersionUUID!=null){// before becoming team doc,this resource was pending approval
                            new ApprovedResourceShareTrigger(DocumentManagerUtil.getReadNode(nodeBaseUUID, request));
                        }
                        
                        sharedDoc=new CrSharedDoc(nodeWrapper.getUuid(), team, CrConstants.SHARED_IN_WORKSPACE);
                        sharedDoc.setSharedPrivateNodeUUID(nodeBaseUUID);
                        
                        if (sharedPrivateNodeVersionUUID != null) {
                            sharedDoc.setSharedNodeVersionUUID((sharedPrivateNodeVersionUUID));
                        } else {
                            Node lastVersionNode = DocumentManagerUtil.getNodeOfLastVersion(node.getIdentifier(),
                                    request);
                            sharedDoc.setSharedNodeVersionUUID(lastVersionNode.getIdentifier());
                        }
                        
                        DbUtil.saveOrUpdateObject(sharedDoc);
                        String lastApprovedNodeVersionUUID = DocumentManagerUtil
                                .getNodeOfLastVersion(nodeWrapper.getUuid(), request).getIdentifier();
                        //delete previous approved versionId
                        NodeLastApprovedVersion lastAppVersion=DocumentManagerUtil.getlastApprovedVersionOfTeamNode(nodeWrapper.getUuid());
                        if(lastAppVersion!=null){
                            DbUtil.delete(lastAppVersion);
                        }
                        //store last approved version of the team node in db
                        lastAppVersion=new NodeLastApprovedVersion(nodeWrapper.getUuid(), lastApprovedNodeVersionUUID);
                        DbUtil.saveOrUpdateObject(lastAppVersion);
                    }   
                } else {
                    sharedDoc = DocumentManagerUtil.getCrSharedDoc(node.getIdentifier(), teamMember.getTeamId(),
                            CrConstants.PENDING_STATUS);
                    if(sharedDoc!=null){ //if there was other version of this resource,which was not approved as team doc,then that previous version is replaced with this one
                        DbUtil.delete(sharedDoc);
                    }
                    
                    sharedDoc = new CrSharedDoc(node.getIdentifier(), team, CrConstants.PENDING_STATUS);
                    Node lastVersionNode = DocumentManagerUtil.getNodeOfLastVersion(node.getIdentifier(), request);
                    sharedDoc.setSharedNodeVersionUUID(lastVersionNode.getIdentifier());
                    DbUtil.saveOrUpdateObject(sharedDoc);
                    //create new Approval
                    new PendingResourceShareTrigger(lastVersionNode);
                }
                
                // copy all organizations from the original Node
                copyOrganizations(nodeBaseUUID, sharedDoc.getNodeUUID());
                
            }else if(shareWith.equals(CrConstants.SHAREABLE_WITH_OTHER_TEAMS)){
                CrSharedDoc sharedDoc = null;
                /**
                 * if other version was already shared, then just need to update version of the shared node among workspaces, 
                 * otherwise should create new one.
                 */
                Collection<AmpTeam> teams= TeamUtil.getAllTeams();
                if(teams!=null && teams.size()>0){
                    for (AmpTeam ampTeam : teams) {
                        sharedDoc = DocumentManagerUtil.getCrSharedDoc(node.getIdentifier(), ampTeam.getAmpTeamId(),
                                CrConstants.SHARED_AMONG_WORKSPACES);
                        if (sharedDoc == null) {
                            sharedDoc = new CrSharedDoc(node.getIdentifier(), ampTeam,
                                    CrConstants.SHARED_AMONG_WORKSPACES);
                        }
//                      Node lastVersionNode = DocumentManagerUtil.getNodeOfLastVersion(node.getUUID(), request);
//                      sharedDoc.setSharedNodeVersionUUID(lastVersionNode.getUUID());
                        /**
                         * get current node's last approved version that will be shared
                         */
                        NodeLastApprovedVersion lastAppVersionOfTeamNode = DocumentManagerUtil
                                .getlastApprovedVersionOfTeamNode(node.getIdentifier());
                        
                        if (lastAppVersionOfTeamNode != null) {
                            sharedDoc.setSharedNodeVersionUUID(lastAppVersionOfTeamNode.getVersionID());
                        } else {
                            sharedDoc.setSharedNodeVersionUUID(DocumentManagerUtil
                                    .getNodeOfLastVersion(node.getIdentifier(), request).getIdentifier());
                        }

                        DbUtil.saveOrUpdateObject(sharedDoc);
                        
                        // copy all organizations from the original Node
                        copyOrganizations(node.getIdentifier(), sharedDoc.getSharedNodeVersionUUID());
                    }
                }
            }
            
        }
        
        
        DocumentManagerUtil.logoutJcrSessions(request);
        request.getSession().setAttribute("resourcesTab", myForm.getType());
        return null;
    }
    
    /**
     * Since the documents are cloned when shared, we need to clone related list of organizations as well
     * @param originalNodeUUID
     * @param destinationNodeUUID
     */
    private void copyOrganizations(String originalNodeUUID, String destinationNodeUUID) {
        // copy all organizations from the original Node
        List<AmpOrganisation> existingOrgs = DocumentOrganizationManager.getInstance()
                .getOrganizationsByUUID(originalNodeUUID);
        if (existingOrgs != null) {
            for (AmpOrganisation organizationLinkToClone : existingOrgs) {
                CrDocumentsToOrganisations docToOrgObj = new CrDocumentsToOrganisations(destinationNodeUUID, organizationLinkToClone);
                DocumentOrganizationManager.getInstance().saveObject(docToOrgObj);
            }
        }       
    }
}
