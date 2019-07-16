package org.digijava.module.contentrepository.util;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.Workspace;
import javax.jcr.version.Version;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.NodeWrapper;

public class DocumentManagerRights {
    
    public static Boolean hasDeleteRights(String uuid, HttpServletRequest request) {
        Node node = DocumentManagerUtil.getWriteNode(uuid, request);
        return hasDeleteRights(node, request);
    }
    
    public static Boolean hasDeleteRights(Node node, HttpServletRequest request) {
        boolean result                      = true;
        Boolean manuallySetNoDeleteFlag = isManuallySetNoDeleteFlag(request);
        if (manuallySetNoDeleteFlag != null) {
            result = result && manuallySetNoDeleteFlag;
        }
        return result && 
        ( isOwnerOrTeamLeader(node, request) || (isCreator(node, request)&&isCreatorTeam(node, request)) );
    }
    
    public static Boolean hasViewRights(Node node, HttpServletRequest request) {
        Boolean manuallySetViewAllRights = isManuallySetViewAllFlag(request);
        if (manuallySetViewAllRights != null && manuallySetViewAllRights)
            return true;
        
        Boolean isOwnerOrTeamLeader = isOwnerOrTeamLeader(node, request);
        if (isOwnerOrTeamLeader != null && isOwnerOrTeamLeader.booleanValue())
            return true;

        Boolean isTeamDocument = isTeamDocument(node, request);
        if (isTeamDocument != null && isTeamDocument.booleanValue())
            return true;
        
        return false;
    }
    
    public static Boolean hasShowVersionsRights (Node node, HttpServletRequest request) {
        boolean result                      = true;
        Boolean manuallySetNoVersioningFlag = isManuallySetNoShowVersionsFlag(request);
        if (manuallySetNoVersioningFlag != null) {
            result = result && manuallySetNoVersioningFlag;
        }
        return result && 
        ( isOwnerOrTeamLeader(node, request) || (isCreator(node, request)&&isCreatorTeam(node, request)) || 
                (isAllowedVersioningTeamResourcesForMembers(request)&&isCreatorTeam(node, request)&&isTeamDocument(node, request)) 
        );
    }
    
    /**
     * add new version button is visible for TM or not...
     */
    public static Boolean hasViewAddNewVersioninsRights (Node node, HttpServletRequest request) {
        boolean result                      = true;
        Boolean manuallySetNoVersioningFlag = isManuallySetNoVersioningFlag(request);
        if (manuallySetNoVersioningFlag != null) {
            result = result && manuallySetNoVersioningFlag;
        }
        return result;
    }
    
    public static Boolean hasAddParticipatingOrgRights(Node node, HttpServletRequest request) {
        Boolean isVersion   = isNodeAVersion(request, node);
        return true && (isOwnerOrTeamLeader(node, request) || isCreator(node, request)) && (isVersion!=null && !isVersion) ;
    }
    
    public static Boolean hasVersioningRights (Node node, HttpServletRequest request) {
        boolean result                      = true;
        Boolean manuallySetNoVersioningFlag = isManuallySetNoVersioningFlag(request);
        if (manuallySetNoVersioningFlag != null) {
            result = result && manuallySetNoVersioningFlag;
        }
        //if it' team leader
        //or
        //creator of the document and members are allowed to add versions
        //or
        //team member but members are allowed to add versions and also share among workspaces
        return result && (isTeamLeader(request) || (isCreator(node, request)&&isCreatorTeam(node, request)&& isAllowedVersioningTeamResourcesForMembers(request))
                || (isAllowedVersioningTeamResourcesForMembers(request) && isAllowedShareAndUnshareResAcrossWorkspacesForMembers(request) &&isTeamDocument(node, request)));
        
        }
    
        
    public static Boolean hasMakePublicRights (HttpServletRequest request) {
        Boolean manuallySetNoMakePublicFlag = isManuallySetNoMakePublicFlag(request);
        if ( manuallySetNoMakePublicFlag == null ){
            manuallySetNoMakePublicFlag=true;
        }
        return manuallySetNoMakePublicFlag && (isTeamLeader(request) || isAllowedToPublishResources(request)) ;
//      if(isTeamLeader(request)){
//              return true;
//      }
//      return isAllowedToPublishResources(request);
    }
    
    private static boolean isAllowedToPublishResources(HttpServletRequest request) {
        boolean retVal=false;
        HttpSession httpSession     = request.getSession(); 
        TeamMember tm               = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        
        if (tm == null) {
            return false;
        }
        
        AmpApplicationSettings sett = DbUtil.getTeamAppSettings(tm.getTeamId());
        if ( sett.getAllowPublishingResources() != null && (sett.getAllowPublishingResources()>= CrConstants.PUBLISHING_RESOURCES_ALLOWED_SPECIFIC_USERS
                && tm.getPublishDocuments()!=null && tm.getPublishDocuments()) ){
            retVal=true;
        }       
        return retVal;
    }
    
    public static Boolean hasDeleteRightsOnPublicVersion(Node node, HttpServletRequest request) {
        return true && isOwnerOrTeamLeader(node, request);
    }
    public static Boolean hasAddResourceToTeamResourcesRights(HttpServletRequest request) {
        return isTeamLeader(request) || isAllowedAddTeamResourcesForMembers(request);
    }
    
    public static boolean hasApproveVersionRights(HttpServletRequest request){
        return isTeamLeader(request);
    }
    
    public static boolean hasRightToSeePendingVersion(HttpServletRequest request, Node version){
        return isTeamLeader(request)||isVersionCreator(request, version);
    }
    
    public static Boolean hasShareRights(Node node,HttpServletRequest request, String tabName){
        Boolean manuallySetNoShareFlag =isManuallySetNoShareFlag(request);
        if (manuallySetNoShareFlag != null) {
            return manuallySetNoShareFlag;
        }
        
        if(tabName==null){
            return false;
        }else if(tabName.equals(CrConstants.PRIVATE_DOCS_TAB)){
            return true;
        }else if(tabName.equals(CrConstants.SHARED_DOCS_TAB)){
            return false;
        }else   if(tabName.equals(CrConstants.TEAM_DOCS_TAB)){
            return hasShareAmongWorkspacesRights(request);
        }else return false;     
    }
    
    public static Boolean hasUnshareRights(Node node,HttpServletRequest request, String tabName){
        Boolean manuallySetNoUnShareFlag =isManuallySetNoShareFlag(request);
        if (manuallySetNoUnShareFlag != null) {
            return manuallySetNoUnShareFlag;
        }
        if(tabName==null){
            return false;
        }else if(tabName.equals(CrConstants.PRIVATE_DOCS_TAB)){
            return false;
        }else if(tabName.equals(CrConstants.SHARED_DOCS_TAB)){
            return isTeamLeader(request);
        }else   if(tabName.equals(CrConstants.TEAM_DOCS_TAB)){
            return hasUnshareAmongWorkspacesRights(request);
        }else{
            return false;       
        }
    }
    
    
    /**
     * is team member allowed to share team resources across workspaces 
     */
    private static Boolean hasShareAmongWorkspacesRights(HttpServletRequest request) {
        return isTeamLeader(request) || isAllowedShareAndUnshareResAcrossWorkspacesForMembers(request);
    }
    
    /**
     * is team member allowed to unshare globally shared team resources
     */
    private static Boolean hasUnshareAmongWorkspacesRights(HttpServletRequest request) {
        return isTeamLeader(request) || isAllowedShareAndUnshareResAcrossWorkspacesForMembers(request);
    }
    
    private static Boolean isTeamLeader( HttpServletRequest request ) {
        HttpSession httpSession     = request.getSession(); 
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        
        return teamMember != null && teamMember.getTeamHead();
    }
    
    private static Boolean isVersionCreator(HttpServletRequest request, Node version){
        HttpSession httpSession     = request.getSession(); 
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        if(teamMember == null) {
            return false;
        } else {
            try {
                return version.getProperty(CrConstants.PROPERTY_VERSION_CREATOR).getString().equals(teamMember.getEmail());
            } catch (Exception e) {
                return null;
            }
        }
    }
    
    private static Boolean isNodeAVersion(HttpServletRequest request, Node node){
        try {
            List<Version> vList = DocumentManagerUtil.getVersions(node.getIdentifier(), request, false);
            return (vList==null)?true:false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static Boolean isOwnerOrTeamLeader(Node node, HttpServletRequest request) {
        HttpSession httpSession     = request.getSession(); 
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        
        if (teamMember == null) {
            return false;
        }
        
        String username             = teamMember.getEmail();
        String teamId               = teamMember.getTeamId() + "";
        
        String userPath             = "private"+"/"+teamId+"/"+username;
        
        try {
            Workspace workspace     = node.getSession().getWorkspace();
            String path             = node.getCorrespondingNodePath( workspace.getName() );
            /**
             * If owner of node
             */
            if ( path.contains(userPath) ) {
                return new Boolean(true);
            }
            
            /**
             * If team leader of the team
             */
            if ( teamMember.getTeamHead() && path.contains("/" + teamId + "/") ) {
                return new Boolean(true);
            }
            return new Boolean(false);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static Boolean isTeamDocument(Node node, HttpServletRequest request) {
        HttpSession httpSession     = request.getSession(); 
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        if ( teamMember == null )
            return new Boolean(false);
            
        String teamId               = teamMember.getTeamId() + "";
        
        String myTeamPath           = "team/"+teamId;
        
        try {
            Workspace workspace         = node.getSession().getWorkspace();
            String path                 = node.getCorrespondingNodePath( workspace.getName() );
            if ( path.contains(myTeamPath) ) {
                return new Boolean(true);
            }
            else 
                return new Boolean(false);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
    }
    
    private static Boolean isLeaderOfManagementWorkspace( HttpServletRequest request ) {
        HttpSession httpSession     = request.getSession(); 
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        AmpTeam ampTeam = TeamUtil.getAmpTeam(teamMember.getTeamId());
        if("Management".equals(ampTeam.getAccessType()) && teamMember.getTeamHead()) return true;
        return false;
    }
    
    private static Boolean isManuallySetNoShowVersionsFlag(HttpServletRequest request) {
        if ( request.getParameter("showVersionsRights") != null ) {
            Boolean result  = Boolean.parseBoolean( request.getParameter("showVersionsRights") );
            return result;
        }
        return null;
    }
    
    private static Boolean isManuallySetNoVersioningFlag(HttpServletRequest request) {
        if ( request.getParameter("versioningRights") != null ) {
            Boolean result  = Boolean.parseBoolean( request.getParameter("versioningRights") );
            return result;
        }
        return null;
    }
    
    private static Boolean isManuallySetNoDeleteFlag(HttpServletRequest request) {
        if ( request.getParameter("deleteRights") != null ) {
            Boolean result  = Boolean.parseBoolean( request.getParameter("deleteRights") );
            return result;
        }
        return null;
    }
    
    private static Boolean isManuallySetNoMakePublicFlag(HttpServletRequest request) {
        if ( request.getParameter("makePublicRights") != null ) {
            Boolean result  = Boolean.parseBoolean( request.getParameter("makePublicRights") );
            return result;
        }
        return null;
    }
    private static Boolean isManuallySetViewAllFlag(HttpServletRequest request) {
        if ( request.getParameter("viewAllRights") != null ) {
            Boolean result  = Boolean.parseBoolean( request.getParameter("viewAllRights") );
            return result;
        }
        return null;
    }
    
    private static Boolean isManuallySetNoShareFlag(HttpServletRequest request) {
        if ( request.getParameter("shareRights") != null ) {
            Boolean result  = Boolean.parseBoolean( request.getParameter("shareRights") );
            return result;
        }
        return null;
    }
    
    private static Boolean isManuallySetNoUnshareFlag(HttpServletRequest request) {
        if ( request.getParameter("unshareRights") != null ) {
            Boolean result  = Boolean.parseBoolean( request.getParameter("unshareRights") );
            return result;
        }
        return null;
    }
    
    private static Boolean isAllowedAddTeamResourcesForMembers (HttpServletRequest request) {
        HttpSession httpSession     = request.getSession(); 
        TeamMember tm               = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        AmpApplicationSettings sett = DbUtil.getTeamAppSettings(tm.getTeamId());

        return sett.getAllowAddTeamRes() != null && 
                sett.getAllowAddTeamRes() >= CrConstants.TEAM_RESOURCES_ADD_ALLOWED_WORKSP_MEMBER ;
    }
    
    private static Boolean isAllowedVersioningTeamResourcesForMembers (HttpServletRequest request) {
        HttpSession httpSession     = request.getSession(); 
        TeamMember tm               = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        
        if (tm == null) {
            return false;
        }
        
        Integer allowAddTeamRes = tm.getAllowAddTeamRes();

        return allowAddTeamRes != null && 
                allowAddTeamRes >= CrConstants.TEAM_RESOURCES_VERSIONING_ALLOWED_WORKSP_MEMBER ;        
    }
    
    private static Boolean isAllowedShareAndUnshareResAcrossWorkspacesForMembers (HttpServletRequest request) {
        HttpSession httpSession     = request.getSession(); 
        TeamMember tm               = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        AmpApplicationSettings sett = DbUtil.getTeamAppSettings(tm.getTeamId());
        
        return sett.getAllowShareTeamRes() != null && 
                sett.getAllowShareTeamRes() >= CrConstants.TEAM_RESOURCES_ADD_ALLOWED_WORKSP_MEMBER ;
    }
    
    private static Boolean isCreator(Node node, HttpServletRequest request) {
        NodeWrapper nw = new NodeWrapper(node);
        
        HttpSession httpSession     = request.getSession(); 
        TeamMember tm               = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        
        return tm != null && tm.getEmail() != null && tm.getEmail().equals(nw.getCreator());
    }
    
    private static Boolean isCreatorTeam(Node node, HttpServletRequest request) {
        NodeWrapper nw = new NodeWrapper(node);
        
        HttpSession httpSession     = request.getSession(); 
        TeamMember tm               = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        
        return tm != null && tm.getTeamId().equals(nw.getCreatorTeam());
    }

}
