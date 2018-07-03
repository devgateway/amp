package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.servlet.ServletContext;
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
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
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
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.contentrepository.util.DocumentsNodesAttributeManager;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;

public class DocumentManager extends Action {
    
    private static Logger logger        = Logger.getLogger(DocumentManager.class);
    private boolean showOnlyLinks       = false;
    private boolean showOnlyDocs        = false;

    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception{

        ActionMessages errors                   = new ActionMessages();
        DocumentManagerForm myForm      = (DocumentManagerForm) form;

        request.setAttribute("ServletContext", this.getServlet().getServletContext() );
        if (  myForm.getAjaxDocumentList() ) {
            ajaxDocumentList(request, myForm);
            return mapping.findForward("ajaxDocumentList");
        }

        DocumentManagerUtil.setMaxFileSizeAttribute(request);

        if (!isLoggeedIn(request)) {
            return mapping.findForward("publicView");
        }
        
        //set years
        myForm.setYears(new ArrayList<Long>());
        Long yearFrom = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.YEAR_RANGE_START));
        Long countYear = Long.parseLong(FeaturesUtil.getGlobalSettingValue(Constants.GlobalSettings.NUMBER_OF_YEARS_IN_RANGE));
        for (long i = yearFrom; i <= (yearFrom + countYear); i++) {
            myForm.getYears().add(new Long(i));
        }
        
        showContentRepository(request, myForm, errors);
        
        this.saveErrors(request, errors);
        HttpSession httpSession     = request.getSession();
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        AmpApplicationSettings sett = DbUtil.getTeamAppSettings(teamMember.getTeamId());
        boolean shareWithoutApprovalNeeded=((sett!=null && sett.getAllowAddTeamRes()!=null && sett.getAllowAddTeamRes().intValue()>=CrConstants.TEAM_RESOURCES_ADD_ALLOWED_WORKSP_MEMBER) || teamMember.getTeamHead());
        request.setAttribute("shareWithoutApprovalNeeded", shareWithoutApprovalNeeded);
        return mapping.findForward("forward");
    }

    private boolean ajaxDocumentList(HttpServletRequest myRequest, DocumentManagerForm myForm) {
        // UGLY HACK. This needs to be re-written
        if (myRequest.getHeader("referer") != null && myRequest.getHeader("referer").contains("documentManager.do")) {
            myRequest.setAttribute("checkBoxToHide", true);
        }
        boolean showActionsButtons = true;
        if (myForm.getShowActions() != null && !myForm.getShowActions()) {
            showActionsButtons = false;
        }
        
        Session jcrWriteSession = DocumentManagerUtil.getWriteSession(myRequest);
        
        if (!isLoggeedIn(myRequest) || myRequest.getParameter(CrConstants.GET_PUBLIC_DOCUMENTS) != null) {
            Map<String, CrDocumentNodeAttributes> uuidMap = DocumentsNodesAttributeManager.getInstance()
                    .getPublicDocumentsMap(true);
            
            try {
                List<String> uuidList = uuidMap.keySet().stream().collect(Collectors.toList());
                List<DocumentData> otherDocuments = this.getDocuments(uuidList, myRequest, CrConstants.PUBLIC_DOCS_TAB,
                        false, showActionsButtons);
                myForm.setOtherDocuments(otherDocuments);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            if (myForm.getDocListInSession() != null) {
                List<String> uuids = new ArrayList<>();
                uuids.addAll(SelectDocumentDM.getSelectedDocsSet(myRequest, myForm.getDocListInSession(), true));
                Collection<DocumentData> tempCol = TemporaryDocumentData.retrieveTemporaryDocDataList(myRequest);
                if (!uuids.isEmpty()) {
                    List<DocumentData> documents = this.getDocuments(uuids, myRequest, null, false, showActionsButtons);
                    myForm.setOtherDocuments(documents);
                }

                try {
                    if (tempCol != null) {
                        if (myForm.getOtherDocuments() == null) {
                            myForm.setOtherDocuments(tempCol);
                        } else {
                            myForm.getOtherDocuments().addAll(tempCol);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

                return false;
            }
        
        //for selectDocumentDM
        
        myRequest.setAttribute("dynamicList", myRequest.getParameter("dynamicList") );
        String source   = null;
        if ( myForm.getOtherUsername() != null && myForm.getOtherTeamId() != null ) {
            source      = DocumentFilter.SOURCE_PRIVATE_DOCUMENTS;
            myForm.setType("private"); //TODO-CONSTANTIN COPY SOURCE
        }else if ( myForm.getOtherUsername() == null && myForm.getOtherTeamId() != null ) {
            source      = DocumentFilter.SOURCE_TEAM_DOCUMENTS;
            myForm.setType("team");
        }else if(myForm.getShowSharedDocs()!=null){
            source      = DocumentFilter.SOURCE_SHARED_DOCUMENTS;
            myForm.setType("shared");
        }else {
            source          = DocumentFilter.SOURCE_PUBLIC_DOCUMENTS;
        }
        
        myRequest.setAttribute("tabType", myForm.getType());        
        
        List<String> filterLablesUUID   = null;
        if ( myForm.getFilterLabelsUUID() != null ){
            filterLablesUUID        = Arrays.asList(myForm.getFilterLabelsUUID() );
        }
        
        List<Long> filterDocTypes       = null;
        if ( myForm.getFilterDocTypeIds() != null ){
            filterDocTypes          = Arrays.asList(myForm.getFilterDocTypeIds() );
            filterDocTypes          = new ArrayList<Long>(filterDocTypes );
            filterDocTypes.remove(new Long(0));
            filterDocTypes.remove(new Long(-1));
        }
        
        List<String> filterFileTypes    = null;
        if ( myForm.getFilterFileTypes() != null ){
            filterFileTypes         = Arrays.asList(myForm.getFilterFileTypes() );
            filterFileTypes         = new ArrayList<String>(filterFileTypes);
            filterFileTypes.remove("-1");
        }
        
        List<String> filterOwners           = null;
        if ( myForm.getFilterOwners() != null ) {
            filterOwners            = Arrays.asList(myForm.getFilterOwners() );
            filterOwners            = new ArrayList<String>( filterOwners );
            filterOwners.remove("-1");
        }
        
        
        List<Long> filterTeamIds            = null; 
        if ( myForm.getFilterTeamIds() != null ) { 
            filterTeamIds           = Arrays.asList(myForm.getFilterTeamIds() );
            filterTeamIds           = new ArrayList<Long> ( filterTeamIds );
            filterTeamIds.remove(new Long(0));
            filterTeamIds.remove(new Long(-1));
        }
        
        List<String> filterkeywords = null;
        if ( myForm.getFilterKeywords() != null ){
            filterkeywords      = Arrays.asList(myForm.getFilterKeywords() );
        }
        
        // organisationID to filter by: null, zero or negative numbers mean "no filtering"
        Long orgId = null;
        if (myForm.getFilterOrganisations() != null)
            orgId = Long.parseLong(myForm.getFilterOrganisations());
        
        String filterFromDate = myForm.getFilterFromDate();
        String filterToDate = myForm.getFilterToDate();
        
        DocumentFilter documentFilter   = new DocumentFilter(source, filterLablesUUID, filterDocTypes, 
                        filterFileTypes, filterTeamIds, filterOwners,filterkeywords, myForm.getOtherUsername(), myForm.getOtherTeamId(), orgId, filterFromDate, filterToDate);
        

        if ( DocumentFilter.SOURCE_PRIVATE_DOCUMENTS.equals(documentFilter.getSource()) ) {
            TeamMember  otherTeamMember     = null;
            Collection otherTeamMembers     = TeamMemberUtil.getTMTeamMembers( documentFilter.getBaseUsername() );
            
            Iterator iterator               = otherTeamMembers.iterator();
            //this search is terribly inefficient
            while ( iterator.hasNext() ) {
                TeamMember someTeamMember   = (TeamMember) iterator.next(); 
                if ( someTeamMember.getTeamId().longValue() == documentFilter.getBaseTeamId().longValue() ) {
                    otherTeamMember     = someTeamMember;
                    break;
                }
            }
            
            if (otherTeamMember != null) {
                List<DocumentData> allPrivateDocs = new ArrayList<>();
                Node otherHomeNode = DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, otherTeamMember);
                if (otherHomeNode != null) {
                    allPrivateDocs = this.getDocuments(otherHomeNode, myRequest, CrConstants.PRIVATE_DOCS_TAB, false, 
                            showActionsButtons);
                }
                myForm.setOtherDocuments(documentFilter.applyFilter(allPrivateDocs));
            }
        }
        else if ( DocumentFilter.SOURCE_TEAM_DOCUMENTS.equals(documentFilter.getSource()) ) {
            Node otherHomeNode                  = DocumentManagerUtil.getTeamNode(jcrWriteSession, myForm.getOtherTeamId());
            
            Collection<DocumentData> allTeamsDocs   = this.getDocuments(otherHomeNode, myRequest,CrConstants.TEAM_DOCS_TAB,false,showActionsButtons);       
            
            //resources pending approval
            TeamMember currentTM = getCurrentTeamMember(myRequest);
            List<DocumentData> pendingResources = null;
            if (currentTM.getTeamHead()) { // should see all docs that are pending approval for this team
                List<String> uuids = DocumentManagerUtil.getSharedNodeUUIDs(currentTM, CrConstants.PENDING_STATUS);
                if (uuids != null && uuids.size() > 0) {
                    pendingResources = getDocuments(uuids, myRequest, CrConstants.TEAM_DOCS_TAB, true, true);
                }
            }
                    
            if(allTeamsDocs!=null){
                if(pendingResources!=null){
                    allTeamsDocs.addAll(pendingResources);
                }
            }
            
            myForm.setOtherDocuments( documentFilter.applyFilter(allTeamsDocs) );
        }
        //shared documents
        else if (DocumentFilter.SOURCE_SHARED_DOCUMENTS.equals(documentFilter.getSource())) {
            Collection<DocumentData> allSharedDocs = this.getSharedDocuments(getCurrentTeamMember(myRequest), myRequest,
                    showActionsButtons);
            myForm.setOtherDocuments(documentFilter.applyFilter(allSharedDocs));
        } else if (DocumentFilter.SOURCE_PUBLIC_DOCUMENTS.equals(documentFilter.getSource())) {
            myRequest.getSession().setAttribute(DocumentFilter.SESSION_LAST_APPLIED_PUBLIC_FILTER, documentFilter);
            Map<String, CrDocumentNodeAttributes> uuidMap = DocumentsNodesAttributeManager.getInstance()
                    .getPublicDocumentsMap(true);
            try {
                List<String> uuids = new ArrayList<>();
                uuids.addAll(uuidMap.keySet());
                List<DocumentData> otherDocuments = this.getDocuments(uuids, myRequest, CrConstants.PUBLIC_DOCS_TAB, 
                        false, showActionsButtons);
                myForm.setOtherDocuments(documentFilter.applyFilter(otherDocuments));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } 
        }
        return false;
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
            // Then the attribute is deleted from session. If there is no
            // resourceTab and no type set then select the first tab.
            if (httpSession.getAttribute("resourcesTab") == null || httpSession.getAttribute("resourcesTab").toString().equals("")) {
                if (myForm.getType() == null || myForm.getType().equals("")) {
                    ServletContext ampContext = null;                   
                    ampContext = getServlet().getServletContext();                  
                    if(FeaturesUtil.isVisibleFeature("My Resources")){
                        myForm.setType("private");
                    }else if (FeaturesUtil.isVisibleFeature("Team Resources")){
                        myForm.setType("team");
                    }else if (FeaturesUtil.isVisibleFeature("Shared Resources") && myForm.getSharedDocsTabVisible()){
                        myForm.setType("shared");
                    }else if (FeaturesUtil.isVisibleFeature("Public Resources") && myForm.getPublicDocsTabVisible()){
                        myForm.setType("public");
                    }
                    
                }
            } else {
                myForm.setType(httpSession.getAttribute("resourcesTab").toString());
                if (myForm.getType().equals("shared") && (!myForm.getSharedDocsTabVisible()))
                {
                    myForm.setType("private");
                }
                httpSession.removeAttribute("resourcesTab");
            }
                        
            if (teamMember == null) {
                throw new Exception("No TeamMember found in HttpSession !");
            }           
            if (myForm.getType() != null && myForm.getType().equals("private") ) {
                if (myForm.getFileData() != null || myForm.getWebLink() != null) {
                    Node userHomeNode = DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, teamMember);
                    if (userHomeNode == null) {
                        userHomeNode = DocumentManagerUtil.createUserPrivateNode(jcrWriteSession, teamMember);
                    }
                    NodeWrapper nodeWrapper = new NodeWrapper(myForm, request, userHomeNode, false, errors);
                    if (nodeWrapper != null && !nodeWrapper.isErrorAppeared()) {
                        nodeWrapper.saveNode(jcrWriteSession);
                    }
                }
            }
            if (myForm.getType() != null && myForm.getType().equals("team") && DocumentManagerRights.hasAddResourceToTeamResourcesRights(request) ) {
                if (myForm.getFileData() != null || myForm.getWebLink() != null) {
                    Node teamHomeNode = DocumentManagerUtil.getTeamNode(jcrWriteSession, teamMember.getTeamId());
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
                            myForm.setType("team");
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
        
        return true;
    }

    private void createVersionApprovalStatus(HttpServletRequest request,Boolean hasVersioningRightsWithoutApprovalNeeded,NodeWrapper nodeWrapper)
            throws UnsupportedRepositoryOperationException, RepositoryException, CrException, Exception {
        if(hasVersioningRightsWithoutApprovalNeeded){
            //update team's last approved version id- If new team document is created,it's uuid is last approved
            String lastApprovedNodeVersionUUID=DocumentManagerUtil.getNodeOfLastVersion(nodeWrapper.getUuid(), request).getUUID();
            NodeLastApprovedVersion lastAppVersion=DocumentManagerUtil.getlastApprovedVersionOfTeamNode(nodeWrapper.getUuid());
            if(lastAppVersion!=null){
                lastAppVersion.setVersionID(lastApprovedNodeVersionUUID);
            }else{
                lastAppVersion=new NodeLastApprovedVersion(nodeWrapper.getUuid(), lastApprovedNodeVersionUUID);
            }                   
            DbUtil.saveOrUpdateObject(lastAppVersion);
        }else{
            //version is unapproved
            String lastVersionOfTheNode=DocumentManagerUtil.getNodeOfLastVersion(nodeWrapper.getUuid(), request).getUUID();
            TeamNodePendingVersion pendingVersion=new TeamNodePendingVersion(nodeWrapper.getUuid(),lastVersionOfTheNode);
            DbUtil.saveOrUpdateObject(pendingVersion);
        }
    }

    private List<DocumentData> getSharedDocuments(TeamMember teamMember, HttpServletRequest request,
            boolean showActionButtons) {
        List<DocumentData> sharedDocs = null;
        // get all nodes that are shared to this team
        List<String> allSharedDocsIds = DocumentManagerUtil.getSharedNodeUUIDs(teamMember,
                CrConstants.SHARED_AMONG_WORKSPACES);
        if (allSharedDocsIds != null) {
            sharedDocs = getDocuments(allSharedDocsIds, request, CrConstants.SHARED_DOCS_TAB, false, showActionButtons);
        }
        
        return sharedDocs;
    }
    
    
    private List<DocumentData> getDocuments(Node node, HttpServletRequest request, String tabName, boolean isPending,
            boolean showActionButtons) {
        
        if (node != null) {
            try {
                NodeIterator nodeIterator = node.getNodes();
                List<Node> nodes = new ArrayList<Node>();
                
                while (nodeIterator.hasNext()) {
                    nodes.add(nodeIterator.nextNode());
                }
                
                return getDocumentsFromNodes(nodes, request, tabName, isPending, showActionButtons);
            } catch (RepositoryException e) {
                logger.error(e.getMessage(), e);
            }
        }
        
        return null;
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
                Node lastVersionNode = DocumentManagerUtil.getNodeOfLastVersion(docBaseUUID, request);
                if (tabName != null && !tabName.equals(CrConstants.PUBLIC_DOCS_TAB)
                        && (DocumentManagerUtil.isGivenVersionPendingApproval(lastVersionNode.getIdentifier()) != null
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
                    Node docNodeLastVersion = DocumentManagerUtil.getNodeOfLastVersion(documentNode.getUUID(), request);
                    /**
                     * If private document wasn't yet approved to become team doc and meanwhile TM added new version to his private doc,
                     * the version which he marked as shared should be visible and not the last version of the document.
                     */
                    if (!docNodeLastVersion.getUUID().equals(sharedVersionId)) {
                        documentNode = DocumentManagerUtil.getReadNode(sharedVersionId, request);
                    }
                }
                
                NodeWrapper nw = new NodeWrapper(documentNode);
                if (shouldSkipNode(nw, baseNode, uuidMapVer, request)) {
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
                    boolean needsApproval=false;
                    if (StringUtils.equalsIgnoreCase(tabName, CrConstants.TEAM_DOCS_TAB)) {
                        needsApproval = DocumentManagerUtil.isResourcePendingtoBeShared(docBaseUUID);
                    } else if (StringUtils.equalsIgnoreCase(tabName, CrConstants.PRIVATE_DOCS_TAB)) {
                        needsApproval = DocumentManagerUtil.isResourceVersionPendingtoBeShared(docBaseUUID,
                                nw.getLastVersionUUID(request));
                    }
                    documentData.setNeedsApproval(needsApproval);   //should show "share" or "approve" link
                    
                    List<String> sharedNodeVersionId=new ArrayList<String>();
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
                }
                // This is not the actual document node. It is the node of the public version. 
                // That's why one shouldn't have the above rights.
                else {
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

    private boolean shouldSkipNode(NodeWrapper nodeWrapper, Node baseNode, 
            Map<String, CrDocumentNodeAttributes> uuidMapVer, HttpServletRequest request) {
        
        boolean shouldSkip = false;
        if (nodeWrapper.getWebLink() != null && showOnlyDocs 
                || (nodeWrapper.getWebLink() == null && showOnlyLinks)) {
            shouldSkip = true;
        }
        // This document is public and exactly this version is the public one
        boolean isPublicVersion = uuidMapVer.containsKey(nodeWrapper.getUuid());
        
        if (!(isPublicVersion || DocumentManagerRights.hasViewRights(baseNode, request))) {
            shouldSkip = true;
        }
        
        if (nodeWrapper.getName() == null && nodeWrapper.getWebLink() == null) {
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

        if (tabName != null && tabName.equals(CrConstants.SHARED_DOCS_TAB)) {
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
                
                if (nodeWrapper.getWebLink() != null && (showOnlyDocs || showOnlyLinks)) {
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
            logger.error(e.getMessage(), e);
        }
        
        return documents;
    }
    
    private boolean isLoggeedIn(HttpServletRequest request) {
        if ( getCurrentTeamMember(request) != null) 
            return true;
        return false;
    }
    
    private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
        HttpSession httpSession     = request.getSession();
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        return teamMember;
    }
}
