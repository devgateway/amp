package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.ampapi.endpoints.gis.services.MapTilesService;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DocumentUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.dbentity.CrSharedDoc;
import org.digijava.module.contentrepository.dbentity.NodeLastApprovedVersion;
import org.digijava.module.contentrepository.dbentity.TeamNodePendingVersion;
import org.digijava.module.contentrepository.dbentity.filter.DocumentFilter;
import org.digijava.module.contentrepository.exception.CrException;
import org.digijava.module.contentrepository.exception.NoVersionsFoundException;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.contentrepository.util.DocumentsNodesAttributeManager;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

public class DocumentManager extends Action {
    
    private static Logger logger = Logger.getLogger(DocumentManager.class);
    
    private static final String TYPE_PRIVATE = "private";
    private static final String TYPE_TEAM = "team";
    private static final String TYPE_SHARED = "shared";
    private static final String TYPE_PUBLIC = "public";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) {

        ActionMessages errors = new ActionMessages();
        DocumentManagerForm docForm = (DocumentManagerForm) form;
        
        DocumentManagerUtil.setMaxFileSizeAttribute(request);

        if (docForm.getAjaxDocumentList()) {
            populateDocumentManagerForm(request, docForm);
            return mapping.findForward("ajaxDocumentList");
        } else if (!isLoggeedIn(request)) {
            return mapping.findForward("publicView");
        }

        showContentRepository(request, docForm, errors);
        
        return mapping.findForward("forward");
    }

    /**
     * Populate document form with requested documents
     * 
     * @param request
     * @param docForm
     */
    private void populateDocumentManagerForm(HttpServletRequest request, DocumentManagerForm docForm) {
        
        if (request.getHeader("referer") != null && request.getHeader("referer").contains("documentManager.do")) {
            request.setAttribute("checkBoxToHide", true);
        }
        
        boolean showActionsButtons = docForm.getShowActions() != null && !docForm.getShowActions() ? false : true;
        List<DocumentData> documents = new ArrayList<>();
        
        if (!isLoggeedIn(request)) {
            documents = getPublicDocuments(request, showActionsButtons);
        } else if (docForm.getDocListInSession() != null) {
            documents = getSessionDocuments(request, showActionsButtons, docForm.getDocListInSession());
        } else {
            DocumentFilter documentFormFilter = new DocumentFilter(docForm);
            documents = getFilteredDocuments(request, documentFormFilter, showActionsButtons);
            docForm.setType(getDocumentFormType(docForm));
            request.setAttribute("dynamicList", request.getParameter("dynamicList"));
            request.setAttribute("tabType", docForm.getType());
        }
        
        docForm.setOtherDocuments(documents);
    }
    
    /**
     * Get session documents from document form session
     * 
     * @param request
     * @param showActionsButtons
     * @param docListInSession
     * @return documents
     */
    private List<DocumentData> getSessionDocuments(HttpServletRequest request, boolean showActionsButtons, 
            String docListInSession) {
        
        List<DocumentData> documents = new ArrayList<>();
        List<String> uuids = new ArrayList<>(SelectDocumentDM.getSelectedDocsSet(request, docListInSession, true));
        
        documents = getDocuments(uuids, request, null, false, showActionsButtons);
        documents.addAll(DocumentManagerUtil.retrieveTemporaryDocDataList(request));
        
        return documents;
    }

    /**
     * Get documents based on document filter
     * 
     * @param request
     * @param filter
     * @param showActionsButtons
     * @return documents
     */
    private List<DocumentData> getFilteredDocuments(HttpServletRequest request, DocumentFilter filter, 
            boolean showActionsButtons) {
        
        List<DocumentData> documents = new ArrayList<>();
        
        switch (filter.getSource()) {
            case DocumentFilter.SOURCE_PRIVATE_DOCUMENTS:
                documents = getPrivateDocuments(request, filter.getBaseUsername(), filter.getBaseTeamId(), 
                        showActionsButtons);
                break;
            case DocumentFilter.SOURCE_TEAM_DOCUMENTS:
                documents = getTeamDocuments(request, filter.getBaseTeamId(), showActionsButtons);
                break;
            case DocumentFilter.SOURCE_SHARED_DOCUMENTS:
                documents = getSharedDocuments(request, getCurrentTeamMember(request), showActionsButtons);
                break;
            case DocumentFilter.SOURCE_PUBLIC_DOCUMENTS:
                request.getSession().setAttribute(DocumentFilter.SESSION_LAST_APPLIED_PUBLIC_FILTER, filter);
                documents = getPublicDocuments(request, showActionsButtons);
                break;
            default:
                break;
        }

        return filter.applyFilter(documents);
    }

    /**
     * Get team document list based on team_id
     * This method is used to populate the table from team tab in Resource Manager
     * 
     * @param request
     * @param teamId
     * @param showActionsButtons
     * @return documents
     */
    private List<DocumentData> getTeamDocuments(HttpServletRequest request, Long teamId, boolean showActionsButtons) {
        List<DocumentData> documents = new ArrayList<>();
        Session jcrWriteSession = DocumentManagerUtil.getWriteSession(request);
        
        Node otherHomeNode = DocumentManagerUtil.getTeamNode(jcrWriteSession, teamId);
        if (otherHomeNode != null) {
            documents = getDocuments(otherHomeNode, request, CrConstants.TEAM_DOCS_TAB, false, showActionsButtons);
        }
        
        //resources pending approval
        TeamMember currentTM = getCurrentTeamMember(request);
        if (currentTM.getTeamHead()) { // should see all docs that are pending approval for this team
            List<String> uuids = DocumentManagerUtil.getSharedNodeUUIDs(currentTM, CrConstants.PENDING_STATUS);
            List<DocumentData> pendingResources = getDocuments(uuids, request, CrConstants.TEAM_DOCS_TAB, true, true);
            documents.addAll(pendingResources);
        }
        
        return documents;
    }

    /**
     * Get private document list based on teammember (email + team_id)
     * This method is used to populate the table from private tab in Resource Manager
     * 
     * @param request
     * @param email
     * @param teamId
     * @param showActionsButtons
     * @return documents
     */
    private List<DocumentData> getPrivateDocuments(HttpServletRequest request, String email, Long teamId, 
            boolean showActionsButtons) {
        
        Session jcrWriteSession = DocumentManagerUtil.getWriteSession(request);
        AmpTeamMember teamMember = TeamMemberUtil.getAmpTeamMemberByEmailAndTeam(email, teamId);
        
        if (teamMember != null) {
            TeamMember userTeamMember = TeamMemberUtil.getTeamMember(teamMember.getAmpTeamMemId());
            Node otherHomeNode = DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, userTeamMember);
            if (otherHomeNode != null) {
                return getDocuments(otherHomeNode, request, CrConstants.PRIVATE_DOCS_TAB, false, showActionsButtons);
            }
        }
        
        return Collections.emptyList();
    }
    
    /**
     * Get shared documents among workspaces
     * This method is used to populate the table from shared tab in Resource Manager
     * 
     * @param request
     * @param teamMember
     * @param showActionButtons
     * @return documents
     */
    private List<DocumentData> getSharedDocuments(HttpServletRequest request, TeamMember teamMember, 
            boolean showActionButtons) {
        
        List<String> allSharedDocsIds = DocumentManagerUtil.getSharedNodeUUIDs(teamMember, 
                CrConstants.SHARED_AMONG_WORKSPACES);
        
        return getDocuments(allSharedDocsIds, request, CrConstants.SHARED_DOCS_TAB, false, showActionButtons);
    }
    
    /**
     * Get public documents
     * This method is used to populate the table from public tab in Resource Manager
     * 
     * @param request
     * @param showActionsButtons
     * @return documents
     */
    private List<DocumentData> getPublicDocuments(HttpServletRequest request, boolean showActionsButtons) {
        DocumentsNodesAttributeManager docNodesAttributeManager = DocumentsNodesAttributeManager.getInstance();
        Map<String, CrDocumentNodeAttributes> uuidMap = docNodesAttributeManager.getPublicDocumentsMap(true);
        List<String> uuids = new ArrayList<>(uuidMap.keySet());
        
        return getDocuments(uuids, request, CrConstants.PUBLIC_DOCS_TAB, false, showActionsButtons);
    }

    
    private boolean showContentRepository(HttpServletRequest request, DocumentManagerForm myForm, ActionMessages errors) {
        try {
            HttpSession httpSession     = request.getSession();
            TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
            
            myForm.setTeamMember(teamMember);
            myForm.setTeamLeader( teamMember.getTeamHead() );
            myForm.setTeamMembers(TeamMemberUtil.getAllTeamMembersMail(teamMember.getTeamId()));
            Session jcrWriteSession     = DocumentManagerUtil.getWriteSession(request);
            
            //check if tabs have data
            myForm.setSharedDocsTabVisible(DocumentManagerUtil.sharedDocumentsExist(teamMember));
            myForm.setPublicDocsTabVisible(DocumentManagerUtil.publicDocumentsExist(teamMember));
            
            // AMP-8791: "resourcesTab" is set only when the document is
            // shared/unshared.
            // Then the attribute is deleted from session. 
            // If there is no resourceTab and no type set then select the first tab.
            if (httpSession.getAttribute("resourcesTab") == null || httpSession.getAttribute("resourcesTab").toString().equals("")) {
                if (myForm.getType() == null || myForm.getType().equals("")) {
                    if(FeaturesUtil.isVisibleFeature("My Resources")){
                        myForm.setType(TYPE_PRIVATE);
                    }else if (FeaturesUtil.isVisibleFeature("Team Resources")){
                        myForm.setType(TYPE_TEAM);
                    }else if (FeaturesUtil.isVisibleFeature("Shared Resources") && myForm.getSharedDocsTabVisible()){
                        myForm.setType(TYPE_SHARED);
                    }else if (FeaturesUtil.isVisibleFeature("Public Resources") && myForm.getPublicDocsTabVisible()){
                        myForm.setType(TYPE_PUBLIC);
                    }
                    
                }
            } else {
                myForm.setType(httpSession.getAttribute("resourcesTab").toString());
                if (myForm.getType().equals(TYPE_SHARED) && (!myForm.getSharedDocsTabVisible()))
                {
                    myForm.setType(TYPE_PRIVATE);
                }
                httpSession.removeAttribute("resourcesTab");
            }
                        
            if (myForm.getType() != null && myForm.getType().equals(TYPE_PRIVATE)) {
                if (myForm.getFileData() != null || myForm.getWebLink() != null) {
                    Node userHomeNode = DocumentManagerUtil.getOrCreateUserPrivateNode(jcrWriteSession, teamMember);
                    
                    NodeWrapper nodeWrapper = new NodeWrapper(myForm, request, userHomeNode, false, errors);
                    if (nodeWrapper != null && !nodeWrapper.isErrorAppeared()) {
                        nodeWrapper.saveNode(jcrWriteSession);
                    }
                }
            }
            if (myForm.getType() != null && myForm.getType().equals(TYPE_TEAM)
                    && DocumentManagerRights.hasAddResourceToTeamResourcesRights(request)) {
                
                if (myForm.getFileData() != null || myForm.getWebLink() != null) {
                    Node teamHomeNode = DocumentManagerUtil.getOrCreateTeamNode(jcrWriteSession,
                            teamMember.getTeamId());
                    NodeWrapper nodeWrapper = new NodeWrapper(myForm, request, teamHomeNode, false, errors);
                    if (nodeWrapper != null && !nodeWrapper.isErrorAppeared()) {
                        nodeWrapper.saveNode(jcrWriteSession);
                    }
                    //update team's last approved version id- If new team document is created,it's uuid is last approved
                    createVersionApprovalStatus(request, true, nodeWrapper);
                }
            }
            if (myForm.getType() != null && myForm.getType().equals("version") && myForm.getUuid() != null) {
                if (myForm.getFileData() != null || myForm.getWebLink() != null) {
                    Node vNode = DocumentManagerUtil.getWriteNode(myForm.getUuid(), request);

                    /**
                     * approval is not needed for version,if current member is TL, or he is creator of this node(base node,not version)
                     * or if tm's are allowed to add versions
                     */
                    Boolean hasVersioningRightsWithoutApprovalNeeded = DocumentManagerRights.hasVersioningRights(vNode, request);
                    NodeWrapper nodeWrapper = new NodeWrapper(myForm, request, vNode , true, errors);
                    vNode.setProperty(CrConstants.PROPERTY_ADDING_DATE, Calendar.getInstance());
                    if (nodeWrapper != null && !nodeWrapper.isErrorAppeared()) {
                        nodeWrapper.saveNode(jcrWriteSession);
                        if (nodeWrapper.isTeamDocument()) {
                            myForm.setType(TYPE_TEAM);
                            createVersionApprovalStatus(request,hasVersioningRightsWithoutApprovalNeeded,nodeWrapper);                          
                        }
                    }                   
                }
            }
            myForm.setYearOfPublication(null);
            
        } catch (Exception e) {
            logger.error("Error during the save of the document", e);
            return false;
        }
        
        myForm.setYears(getDocumentManagerFormYears());
        request.setAttribute("shareWithoutApprovalNeeded", isShareWithoutApprovalNeeded(request));
        
        saveErrors(request, errors);
        
        return true;
    }

    private void createVersionApprovalStatus(HttpServletRequest request,Boolean hasVersioningRightsWithoutApprovalNeeded,NodeWrapper nodeWrapper)
            throws UnsupportedRepositoryOperationException, RepositoryException, CrException, Exception {
        if (hasVersioningRightsWithoutApprovalNeeded) {
            //update team's last approved version id- If new team document is created,it's uuid is last approved
            String lastApprovedNodeVersionUUID = DocumentManagerUtil
                    .getNodeOfLastVersion(nodeWrapper.getUuid(), request).getIdentifier();
            NodeLastApprovedVersion lastAppVersion = DocumentManagerUtil
                    .getlastApprovedVersionOfTeamNode(nodeWrapper.getUuid());
            if (lastAppVersion != null) {
                lastAppVersion.setVersionID(lastApprovedNodeVersionUUID);
            } else {
                lastAppVersion = new NodeLastApprovedVersion(nodeWrapper.getUuid(), lastApprovedNodeVersionUUID);
            }       
            DbUtil.saveOrUpdateObject(lastAppVersion);
        } else {
            //version is unapproved
            String lastVersionOfTheNode = DocumentManagerUtil.getNodeOfLastVersion(nodeWrapper.getUuid(), request)
                    .getIdentifier();
            TeamNodePendingVersion pendingVersion = new TeamNodePendingVersion(nodeWrapper.getUuid(),
                    lastVersionOfTheNode);
            DbUtil.saveOrUpdateObject(pendingVersion);
        }
    }

    private List<DocumentData> getDocuments(Node node, HttpServletRequest request, String tabName, boolean isPending,
            boolean showActionButtons) {
        
        try {
            NodeIterator nodeIterator = node.getNodes();
            List<Node> nodes = new ArrayList<Node>();
            
            while (nodeIterator.hasNext()) {
                nodes.add(nodeIterator.nextNode());
            }
            
            return getDocumentsFromNodes(nodes, request, tabName, isPending, showActionButtons);
        } catch (RepositoryException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    private List<DocumentData> getDocumentsFromNodes(List<Node> nodes, HttpServletRequest request, String tabName,
            boolean isPending, boolean showActionButtons) {
        ArrayList<DocumentData> documents = new ArrayList<DocumentData>();
        DocumentsNodesAttributeManager docAttrManager = DocumentsNodesAttributeManager.getInstance();
        Map<String, CrDocumentNodeAttributes> uuidMapVer = docAttrManager.getPublicDocumentsMap(true);
        Map<String, NodeLastApprovedVersion> nlpvMap = DocumentManagerUtil.getLastApprovedVersionsByUUIDMap();
        
        try {
            for (Node documentNode : nodes) {
                // in case document node last version should be hidden and another should be shown
                Node baseNode = documentNode; 
                String docBaseUUID = documentNode.getIdentifier();
                
                /* pledge documents aren't workspace-bound and can only be added or removed from the pledge itself
                gpi survey documents shouldn't be visible as well */
                if (isDocumentPartOfPledgeOrGPI(docBaseUUID)) {
                    continue;
                }
                
                /**
                 * If this version of node is pending to be approved, then it should be visible only to the creator of the node or the TL
                 * other users should see last approved version of this node, so get version number from sharedDoc entry and load that version
                 */
                NodeLastApprovedVersion nlpv = nlpvMap.get(docBaseUUID);
                if (tabName != null && !tabName.equals(CrConstants.PUBLIC_DOCS_TAB)
                        && (DocumentManagerUtil.isGivenVersionPendingApproval(
                                DocumentManagerUtil.getNodeOfLastVersion(docBaseUUID, request).getIdentifier()) != null
                                || (nlpv != null && !nlpv.getNodeUUID().equals(docBaseUUID)))) {

                    String sharedVersionId = null;
                    if (nlpv != null) {
                        sharedVersionId = nlpv.getVersionID();
                    } else {
                        Node verNode = DocumentManagerUtil.getLastVersionNotWaitingApproval(docBaseUUID, request);
                        if (verNode != null) {
                            sharedVersionId = verNode.getIdentifier();
                        } else {
                            logger.error("There should be at least one version of a document not waiting approval.");
                            continue;
                        }
                    }
                    documentNode = DocumentManagerUtil.getReadNode(sharedVersionId, request);
                }
                
                if (isPending) { //getting documents that need approval to become team docs
                    CrSharedDoc crSharedDoc = DocumentManagerUtil.getCrSharedDoc(docBaseUUID, 
                            getCurrentTeamMember(request).getTeamId(), CrConstants.PENDING_STATUS);
                    String sharedVersionId = crSharedDoc.getSharedNodeVersionUUID();
                    Node docNodeLastVersion = DocumentManagerUtil.getNodeOfLastVersion(documentNode.getIdentifier(),
                            request);
                    /**
                     * If private document wasn't yet approved to become team doc and meanwhile TM added new version to his private doc,
                     * the version which he marked as shared should be visible and not the last version of the document.
                     */
                    if (!docNodeLastVersion.getIdentifier().equals(sharedVersionId)) {
                        documentNode = DocumentManagerUtil.getReadNode(sharedVersionId, request);
                    }
                }
                
                NodeWrapper nw = new NodeWrapper(documentNode);
                if (shouldSkipNode(nw, tabName, baseNode, uuidMapVer, request)) {
                    continue;
                }
                
                DocumentData documentData = DocumentData.buildFromNodeWrapper(nw, docBaseUUID, nw.getUuid());
                if (!CrConstants.PUBLIC_DOCS_TAB.equals(tabName) && showActionButtons) {
                    /**
                     * resources that are pending approval to become team resources, 
                     * shouldn't have possibility to view versions, add new versions e.t.c. 
                    */
                    setDocumentDataRights(documentData, baseNode, documentNode, isPending, request, tabName);
                    
                    //share rights ! this will be different according to settings
                    if (StringUtils.equalsIgnoreCase(tabName, CrConstants.PRIVATE_DOCS_TAB)
                            || (StringUtils.equalsIgnoreCase(tabName, CrConstants.TEAM_DOCS_TAB) && isPending)) {
                        documentData.setShareWith(CrConstants.SHAREABLE_WITH_TEAM);
                    } else {
                        documentData.setShareWith(CrConstants.SHAREABLE_WITH_OTHER_TEAMS);
                    }
                    
                    //if documentNode has pending status in sharedDocs,then it should be true
                    boolean needsApproval = false;
                    if (StringUtils.equalsIgnoreCase(tabName, CrConstants.TEAM_DOCS_TAB)) {
                        needsApproval = DocumentManagerUtil.isResourcePendingtoBeShared(docBaseUUID);
                    } else if (StringUtils.equalsIgnoreCase(tabName, CrConstants.PRIVATE_DOCS_TAB)) {
                        needsApproval = DocumentManagerUtil.isResourceVersionPendingtoBeShared(docBaseUUID,
                                nw.getLastVersionUUID(request));
                    }
                    documentData.setNeedsApproval(needsApproval);   //should show "share" or "approve" link
                    
                    List<String> sharedNodeVersionId = new ArrayList<String>();
                    if (StringUtils.equalsIgnoreCase(tabName, CrConstants.PRIVATE_DOCS_TAB)) {
                        sharedNodeVersionId = DocumentManagerUtil.isPrivateResourceShared(docBaseUUID);
                    } else if (StringUtils.equalsIgnoreCase(tabName, CrConstants.TEAM_DOCS_TAB) && !isPending) {
                        String retVal = DocumentManagerUtil.isTeamResourceSharedWithGivenWorkspace(docBaseUUID, null);
                        if (retVal != null) {
                            sharedNodeVersionId.add(retVal);
                        }
                    }
                    /**
                     * In case of team doc, instead of lastVersion we need just firstly given documentData-s uuid
                     *  if it's some version of the node and not original last version node !
                     */
                    String lastVerUUID = null;
                    if (!docBaseUUID.equals(documentNode.getIdentifier())) {
                        lastVerUUID = documentNode.getIdentifier();
                    } else {
                        try{
                            lastVerUUID = DocumentManagerUtil
                                    .getNodeOfLastVersion(documentNode.getIdentifier(), request).getIdentifier();
                        } catch (NoVersionsFoundException e) {
                            logger.warn(e.getMessage());
                        }
                    }
                    
                    if (sharedNodeVersionId != null && sharedNodeVersionId.size() > 0) {
                        documentData.setIsShared(true);
                        documentData.setLastVersionIsShared(sharedNodeVersionId.contains(lastVerUUID));
                    }
                    
                    //whether this document's any version is public and if is, then which one is public
                    if (docAttrManager.getPublicDocumentsMap(false).containsKey(docBaseUUID)) {
                        documentData.setIsPublic(true);
                        documentData.setLastVersionIsPublic(uuidMapVer.containsKey(lastVerUUID));
                    } else {
                        documentData.setIsPublic(false);
                    }
                    
                    // whether this document has any version, that needs to be approved to become team doc version
                    List<TeamNodePendingVersion> pendingVersionsForBaseNode = null;
                    if (tabName != null && tabName.equals(CrConstants.TEAM_DOCS_TAB)) {
                        pendingVersionsForBaseNode = DocumentManagerUtil.getPendingVersionsForResource(docBaseUUID);
                    }
                    if (pendingVersionsForBaseNode != null && pendingVersionsForBaseNode.size() > 0) {
                        documentData.setHasAnyVersionPendingApproval(true);
                    } else {
                        documentData.setHasAnyVersionPendingApproval(false);
                    }
                } else {
                    // This is not the actual document node. It is the node of the public version. 
                    // That's why one shouldn't have the above rights.
                    documentData.setShowVersionHistory(false); 
                }
                documents.add(documentData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }

    private boolean isDocumentPartOfPledgeOrGPI(String docBaseUUID) {
        Collection<String> pledgeDocumentsUuids = FundingPledges.getPledgeDocumentUuids();
        List<String> gpiSupportiveDocuments = DocumentUtil.getAllSupportiveDocumentsUUID();
        
        return gpiSupportiveDocuments.contains(docBaseUUID) || pledgeDocumentsUuids.contains(docBaseUUID);
    }

    private boolean shouldSkipNode(NodeWrapper nodeWrapper, String tabName, Node baseNode, 
            Map<String, CrDocumentNodeAttributes> uuidMapVer, HttpServletRequest request) {
        
        boolean shouldSkip = false;

        // This document is public and exactly this version is the public one
        boolean isPublicVersion = uuidMapVer.containsKey(nodeWrapper.getUuid());
        
        if (!(isPublicVersion || DocumentManagerRights.hasViewRights(baseNode, request))) {
            shouldSkip = true;
        }
        
        if (nodeWrapper.getName() == null && nodeWrapper.getWebLink() == null) {
            shouldSkip = true;
        }
        
        // AMP-27996 Map Tiles file shouldn't be visible in public documents table
        if (CrConstants.PUBLIC_DOCS_TAB.equals(tabName) && MapTilesService.FILE_NAME.equals(nodeWrapper.getName())) {
            shouldSkip = true;
        }
        
        return shouldSkip;
    }

    /**
     * @param documentData
     * @param baseNode
     * @param documentNode
     * @param isPending
     * @param request
     * @param tabName 
     */
    private void setDocumentDataRights(DocumentData documentData, Node baseNode, Node documentNode, boolean isPending,
            HttpServletRequest request, String tabName) {

        Boolean hasShowVersionsRights = false;
        Boolean hasMakePublicRights = false;
        Boolean hasVersioningRights = false;
        Boolean hasDeleteRights = false;
        Boolean hasDeleteRightsOnPublicVersion = false;
        Boolean hasApproveVersionRights = false;
        Boolean hasAddParticipatingOrgRights = false;

        hasShowVersionsRights = DocumentManagerRights.hasShowVersionsRights(baseNode, request);
        if (hasShowVersionsRights != null) {
            documentData.setHasShowVersionsRights(hasShowVersionsRights && !isPending);
        }

        hasVersioningRights = DocumentManagerRights.hasVersioningRights(baseNode, request);
        if (hasVersioningRights != null) {
            documentData.setHasVersioningRights(hasVersioningRights.booleanValue() && !isPending);
        }
        
        hasDeleteRights = DocumentManagerRights.hasDeleteRights(baseNode, request);
        if (hasDeleteRights != null) {
            documentData.setHasDeleteRights(hasDeleteRights.booleanValue() && !isPending);
        }
        
        hasMakePublicRights = DocumentManagerRights.hasMakePublicRights(request);
        if (hasMakePublicRights != null) {
            documentData.setHasMakePublicRights(hasMakePublicRights.booleanValue() && !isPending);
        }

        hasAddParticipatingOrgRights = DocumentManagerRights.hasAddParticipatingOrgRights(documentNode, request);
        if (hasAddParticipatingOrgRights != null) {
            documentData.setHasAddParticipatingOrgRights(hasAddParticipatingOrgRights);
        }

        hasDeleteRightsOnPublicVersion = DocumentManagerRights.hasDeleteRightsOnPublicVersion(baseNode, request);
        if (hasDeleteRightsOnPublicVersion != null) {
            documentData.setHasDeleteRightsOnPublicVersion(hasDeleteRightsOnPublicVersion.booleanValue() && !isPending);
        }

        hasApproveVersionRights = DocumentManagerRights.hasApproveVersionRights(request);
        if (hasApproveVersionRights != null) {
            documentData.setHasApproveVersionRights(hasApproveVersionRights);
        }
        
        documentData.setHasShareRights(DocumentManagerRights.hasShareRights(documentNode, request, tabName));
        documentData.setHasUnshareRights(DocumentManagerRights.hasUnshareRights(documentNode, request, tabName));
    }
    
    public List<DocumentData> getDocuments(List<String> uuidList, HttpServletRequest myRequest, String tabName,
            boolean isPending, boolean showActionsButton) {
        
        List<Node> documents = new ArrayList<Node>();
        for (String uuid : uuidList) {
            Node documentNode = DocumentManagerUtil.getReadNode(uuid, myRequest);
            /**
             * If documentNode is null it means that there is no node with the specified uuid in the repository
             * but the application still has some information about that node.
             * It means that there is a problem in the logic of the application so we need to throw an 
             * exception.
             */
            if (documentNode == null) {
                logger.error("JACKRABBIT: Document with uuid '" + uuid + "' not found !");
                RepairDbUtil.repairDocumentNoLongerInContentRepository(uuid, CrDocumentNodeAttributes.class.getName());
                RepairDbUtil.repairDocumentNoLongerInContentRepository(uuid, AmpActivityDocument.class.getName());
            } else {
                documents.add(documentNode);
            }
        }

        if (StringUtils.equals(tabName, CrConstants.SHARED_DOCS_TAB)) {
            return getSharedDocuments(documents, myRequest, showActionsButton);
        } 
        
        return getDocumentsFromNodes(documents, myRequest, tabName, isPending, showActionsButton);
    }
    
    //for shared Docs tab
    private List<DocumentData> getSharedDocuments(List<Node> nodes, HttpServletRequest request, 
            boolean showActionsButton) {
        ArrayList<DocumentData> documents = new ArrayList<DocumentData>();
        try {
            for (Node documentNode : nodes) {
                NodeWrapper nodeWrapper = new NodeWrapper(documentNode);
                
                if (nodeWrapper.getWebLink() != null) {
                    continue;
                }
                
                //fill node with data
                String fileName =  nodeWrapper.getName();
                if ( fileName == null && nodeWrapper.getWebLink() == null ){
                    continue;
                }
                
                DocumentData documentData = DocumentData.buildFromNodeWrapper(nodeWrapper);
                
                if(showActionsButton){
                    documentData.setHasUnshareRights(DocumentManagerRights.hasUnshareRights(documentNode, request, 
                            CrConstants.SHARED_DOCS_TAB));
                }               
                documentData.setIsShared(true);
                
                documentData.setShowVersionHistory(false);              
                documents.add(documentData);    
                
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        return documents;
    }
    
    private boolean isLoggeedIn(HttpServletRequest request) {
        return getCurrentTeamMember(request) != null;
    }
    
    private TeamMember getCurrentTeamMember(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        TeamMember teamMember = (TeamMember) httpSession.getAttribute(Constants.CURRENT_MEMBER);
        return teamMember;
    }
    
    /**
     * @return years based on Global Settings
     */
    private List<Long> getDocumentManagerFormYears() {
        List<Long> years = new ArrayList<Long>();
        Long yearFrom = FeaturesUtil.getGlobalSettingValueLong(Constants.GlobalSettings.YEAR_RANGE_START);
        Long countYear = FeaturesUtil.getGlobalSettingValueLong(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE);
        
        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
            years.add(new Long(i));
        }
        
        return years;
    }
    
    /**
     * @param request
     * @return
     */
    private boolean isShareWithoutApprovalNeeded(HttpServletRequest request) {
        TeamMember teamMember = getCurrentTeamMember(request);
        AmpApplicationSettings sett = DbUtil.getTeamAppSettings(teamMember.getTeamId());
        boolean shareWithoutApprovalNeeded = ((sett != null && sett.getAllowAddTeamRes() != null
                && sett.getAllowAddTeamRes().intValue() >= CrConstants.TEAM_RESOURCES_ADD_ALLOWED_WORKSP_MEMBER)
                || teamMember.getTeamHead());
        
        return shareWithoutApprovalNeeded;
    }
    
    private String getDocumentFormType(DocumentManagerForm docForm) {
        if (docForm.getOtherUsername() != null && docForm.getOtherTeamId() != null) {
            return TYPE_PRIVATE;
        } else if (docForm.getOtherUsername() == null && docForm.getOtherTeamId() != null) {
            return TYPE_TEAM;
        } else if (docForm.getShowSharedDocs() != null) {
            return TYPE_SHARED;
        }
        return TYPE_PUBLIC;
    }
}
