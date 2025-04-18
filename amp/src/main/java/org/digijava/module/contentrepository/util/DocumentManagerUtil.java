package org.digijava.module.contentrepository.util;

import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.wicket.util.lang.Bytes;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeManager;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeValidationResponse;
import org.digijava.kernel.ampapi.endpoints.filetype.FileTypeValidationStatus;
import org.digijava.kernel.content.ContentRepositoryManager;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.ResourceManagerSettingsUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.action.DocumentManager;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.dbentity.*;
import org.digijava.module.contentrepository.exception.CrException;
import org.digijava.module.contentrepository.exception.NoNodeInVersionNodeException;
import org.digijava.module.contentrepository.exception.NoVersionsFoundException;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.*;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.jcr.*;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;


public class DocumentManagerUtil {
    
    private static Logger logger = Logger.getLogger(DocumentManagerUtil.class);
    
    /**
     * to be called after every HttpServletRequest processed
     * @param request
     */
    public static void logoutJcrSessions(HttpServletRequest request) {
        ContentRepositoryManager.closeSessions(request);
    }
    
    public static Session getReadSession(HttpServletRequest request) {
        return ContentRepositoryManager.getReadSession(request);
    }

    public static Session getWriteSession(HttpServletRequest request) {
        return ContentRepositoryManager.getWriteSession(request);
    }
    
    public static NodeWrapper getReadNodeWrapper(String uuid, HttpServletRequest request) {
        Node n = getReadNode(uuid, request);
        if (n != null) {
            return new NodeWrapper(n);
        }
        
        return null;
    }

    public static boolean isVersionable(Node node){
        try{
            return node.getVersionHistory().getNodes().getSize() > 0;
        }
        catch(Exception e){return false;}       
    }
    
    public static Node getReadNode(String uuid, HttpServletRequest request) {
        Session session = ContentRepositoryManager.getReadSession(request);
        try {
            return session.getNodeByIdentifier(uuid);
        } catch (Exception e) {
            return null;
        }
    }

    public static Node getWriteNode(String uuid, HttpServletRequest request) {
        Session session = getWriteSession(request);
        try {
            return session.getNodeByIdentifier(uuid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public static String getApplicationPath() {
        PathHelper ph   = new DocumentManagerUtil().new PathHelper();
        return ph.getApplicationPath();
    }

    public static void setFilePermission(String path, String permission)
    {
        Path filePath = Paths.get(path);

        // Define the desired permissions
        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(permission);

        try {
            // Set the permissions to the file
            Files.setPosixFilePermissions(filePath, permissions);
            System.out.println("File permissions set successfully.");
        } catch (Exception e) {
            System.out.println("Error setting file permissions: " + e.getMessage());
        }
    }
    
    public static String calendarToString(Calendar cal,boolean yearofPublication) {
        String retVal=null;
//      String [] monthNames    = {"", "January", "February", "March", "April", "May", "June",
//                                  "July", "August", "September", "October", "November", "December"};
        
        int year        = cal.get(Calendar.YEAR);
        int month       = cal.get(Calendar.MONTH) + 1;
        int day         = cal.get(Calendar.DAY_OF_MONTH);
        
//      int hour        = cal.get(Calendar.HOUR_OF_DAY);
//      int minute      = cal.get(Calendar.MINUTE);
//      int second      = cal.get(Calendar.SECOND);
        
        if(yearofPublication){
            retVal= new Long(year).toString() ;
        }else{
            retVal=month + "/" + day + "/" + year ;
        }
        return retVal ;
    }
    
    public static Node getNodeOfLastVersion(String currentUUID, HttpServletRequest request) throws CrException, RepositoryException {
        List<Version> versions  = getVersions(currentUUID, request, true);
        
        if (versions == null || versions.size() == 0) 
                throw new NoVersionsFoundException("No versions were found for node with UUID: " + currentUUID);
        
        Version lastVersion = versions.get( versions.size()-1 );
        
        NodeIterator niter  = lastVersion.getNodes();
        
        if ( !niter.hasNext() ) {
            throw new NoNodeInVersionNodeException("The last version node of node with UUID " + currentUUID + " doesn't contain any nodes");
        }
        
        return niter.nextNode();        
    }
    public static Node getLastVersionNotWaitingApproval(String currentUUID, HttpServletRequest request) throws CrException, RepositoryException {
        List<Version> versions  = getVersions(currentUUID, request, true);
        if (versions == null || versions.size() == 0) 
            throw new NoVersionsFoundException("No versions were found for node with UUID: " + currentUUID);
        
        for ( int i=versions.size()-1; i>=0; i-- ) {
            Version v           = versions.get(i);
            NodeIterator nIter  = v.getNodes();
            if (nIter.hasNext()) {
                Node n = nIter.nextNode();
                if (isGivenVersionPendingApproval(n.getIdentifier()) == null) {
                    return n;
                }
            }
        }
        return null;
        
        
        
    }
    
    public static int getNextVersionNumber(String uuid, HttpServletRequest request) {
        List versions   = getVersions(uuid, request, false);
        return versions.size() + 1;
    }

    public static List<Version> getVersions(String uuid, HttpServletRequest request, boolean needWriteSession) {
        if (uuid != null) {
            Node node;
            ArrayList<Version> versions     = new ArrayList<Version>();
            if (needWriteSession)
                node                = DocumentManagerUtil.getWriteNode(uuid, request);
            else
                node                = DocumentManagerUtil.getReadNode(uuid, request);
            VersionHistory history;
            try {
                history                         = node.getVersionHistory();
                //Version baseVersion               = history.getBaseVersion();
                //String uuidBaseVersion            = baseVersion.getUUID();
                VersionIterator iterator        = history.getAllVersions();
                iterator.skip(1);
                
                while(iterator.hasNext()) {
                    versions.add( iterator.nextVersion() );
                }
                return versions;
            } 
            catch (Exception e) {
                // TODO Auto-generated catch block
                logger.warn( e.getMessage() );
                logger.warn( "Above warning could be normal in case we're just testing to see if this node is a version or not." );
                return null;
            }
        }
        return null;
    }
    
    public static Boolean deleteDocumentWithRightsChecking (String uuid, HttpServletRequest request) throws Exception {
        Boolean hasDeleteRights     = DocumentManagerRights.hasDeleteRights(uuid, request);
        if (hasDeleteRights == null)
                return null;
        if (hasDeleteRights != null && hasDeleteRights.booleanValue() ) {
            return new Boolean (deleteDocument(uuid, request));
        }
        else
            return new Boolean(false);
    }
    
    /**
     * deleted a node from JR repo
     * @param session - the session OR null. If null, the current TLS request's session will be used
     * @param uuid
     * @return
     */
    public static boolean deleteNode(Session session, String uuid) {
        if (uuid == null) {
            return false;
        }
        
        if (session == null) {
            session = getWriteSession(TLSUtils.getRequest());
        }
        
        boolean successfully = false;
        String name = "";
        try {
            Node node = session.getNodeByIdentifier(uuid);
            name = new NodeWrapper(node).tryGetName();
            node.remove();
            session.save();
            successfully = true;
            
            return true;
        } catch (Exception e) {
            logger.error(uuid, e);
        }
        finally{
            logger.info(String.format("DELETED JackRabbit node with uuid = %s, name = %s, success: %b", uuid, name, successfully));
        }
        
        return false;
    }
    
    private static boolean deleteDocument(String uuid, HttpServletRequest request) {
        return deleteNode(getWriteSession(request), uuid);
    }
    
    public static Property getPropertyFromNode(Node n, String propertyName) {
        try {
            return n.getProperty(propertyName);
        } catch (PathNotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return null;
    }
    
    public static TeamInformationBeanDM getTeamInformationBeanDM (HttpSession session) {
        TeamInformationBeanDM teamInfo  = new TeamInformationBeanDM();
        teamInfo.setMeTeamMember( (TeamMember)session.getAttribute(Constants.CURRENT_MEMBER) );
        
        TeamMember me = teamInfo.getMeTeamMember();
        
        if (me != null) {
            teamInfo.setIsTeamLeader(me.getTeamHead());
            teamInfo.setMyTeamMembers(TeamMemberUtil.getAllTeamMembers(me.getTeamId()));
        }
        
        return teamInfo;
    }
    
    public static Collection<DocumentData> createDocumentDataCollectionForActivityPreview(HttpServletRequest request) {
        Collection<String> uuids = SelectDocumentDM.getSelectedDocsSet(request, 
                ActivityDocumentsConstants.RELATED_DOCUMENTS, false);
        ArrayList<DocumentData> ret = new ArrayList<DocumentData>();
        if (uuids == null) {
            return Collections.emptyList();
        }
        try {
            ArrayList<Node> documents = new ArrayList<Node>();
            Iterator<String> iter = uuids.iterator();
            while (iter.hasNext()) {
                String uuid = iter.next();
                Node documentNode = DocumentManagerUtil.getReadNode(uuid, request);
                if (documentNode == null) {
                    try {
                      throw new Exception("Document with uuid '" + uuid + "' not found !");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        RepairDbUtil.repairDocumentNoLongerInContentRepository(uuid, CrDocumentNodeAttributes.class.getName() );
                        RepairDbUtil.repairDocumentNoLongerInContentRepository(uuid, AmpActivityDocument.class.getName() );
                    }
                }
                else
                    documents.add(documentNode);
            }
            Iterator iterator           = documents.iterator();
            while ( iterator.hasNext() ) {
                Node documentNode   = (Node)iterator.next();
                String documentNodeBaseVersionUUID = documentNode.getIdentifier();
                NodeWrapper nodeWrapper = new NodeWrapper(documentNode);
                String fileName =  nodeWrapper.getName();
                if ( fileName == null && nodeWrapper.getWebLink() == null ){
                    continue;
                }
                
                DocumentData documentData = DocumentData.buildFromNodeWrapper(nodeWrapper, documentNodeBaseVersionUUID, 
                        nodeWrapper.getUuid());               
                ret.add(documentData);
            }   
            return ret;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
    }
    
    
    public static List<DocumentData> createDocumentDataCollectionFromSession(HttpServletRequest request) {
        List<String> uuids = new ArrayList<>();
        uuids.addAll(SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, true));
        if (!uuids.isEmpty()) {
            try {
                DocumentManager dm = new DocumentManager();
                List<DocumentData> ret = dm.getDocuments(uuids, request, null, false, true);
                ret.addAll(DocumentManagerUtil.retrieveTemporaryDocDataList(request));
                return ret;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        
        return null;
    }
    
    public static boolean checkFileSize(FormFile formFile, ActionMessages errors) {
        long maxFileSizeInBytes     = Long.MAX_VALUE;
        long maxFileSizeInMBytes    = Long.MAX_VALUE;
        String maxFileSizeGS        = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE); // File size in MB
        if (maxFileSizeGS != null) {
                maxFileSizeInMBytes     = Integer.parseInt( maxFileSizeGS );
                maxFileSizeInBytes      = 1024 * 1024 * maxFileSizeInMBytes; 
        }
        
        if (formFile.getFileSize() > maxFileSizeInBytes) {
            errors.add("title", new ActionMessage("error.contentrepository.addFile.fileTooLarge", maxFileSizeInMBytes));
            
            return false;
        }
        
        if (formFile.getFileSize() < 1) {
            errors.add("title", new ActionMessage("error.contentrepository.addFile.badPath"));
            
            return false;
        }
        
        return true;
    }
    
    public static boolean checkFileContentType(FormFile formFile, ActionMessages errors) {
        boolean contentValid = true;
        
        if (ResourceManagerSettingsUtil.isLimitFileToUpload()) {
            try {
                FileTypeManager mimeTypeManager = FileTypeManager.getInstance();
                InputStream is = new BufferedInputStream(formFile.getInputStream());
                FileTypeValidationResponse validationResponse = mimeTypeManager.validateFileType(is, formFile.getFileName());
                if (validationResponse.getStatus() != FileTypeValidationStatus.ALLOWED) {
                    if (validationResponse.getStatus() == FileTypeValidationStatus.NOT_ALLOWED) {
                        errors.add("title",
                                new ActionMessage("error.contentrepository.addFile.contentNotAllowed", 
                                        validationResponse.getDescription(), validationResponse.getContentName()));
                    } else if (validationResponse.getStatus() == FileTypeValidationStatus.CONTENT_EXTENSION_MISMATCH) {
                        errors.add("title", 
                                new ActionMessage("error.contentrepository.addFile.contentExtensionMismatch", 
                                        formFile.getFileName(), 
                                        validationResponse.getDescription() + " (" + validationResponse.getContentName() + ")"));
                    } else {
                        errors.add("title", new ActionMessage("error.contentrepository.addFile.internalError"));
                    }
                    contentValid = false;
                }
            } catch (IOException e) {
                logger.error("Error during the validation of the file", e);
                errors.add("title", new ActionMessage("error.contentrepository.addFile.internalError"));
                contentValid = false;
            }
        }
        
        return contentValid;
    }
    
    public static boolean validateFile(FormFile formFile, ActionMessages errors) {
        boolean hasValidSize = checkFileSize(formFile, errors);
        boolean hasValidContentType = checkFileContentType(formFile, errors);
        
        return hasValidSize && hasValidContentType;
    }
    
    public static boolean checkStringAsNodeTitle (String string) {
        for (int i=0; i<string.length(); i++) {
            char ch                 = string.charAt(i);
            Character charObj       = new Character(ch);
            
            if ( Character.isLetterOrDigit(ch) || charObj.equals('_') || Character.isSpaceChar(ch) )
                return true;
        }
        return false;
    }
    
    /**
     * 
     * @param uuid 
     * @param className - the class of the db objects that need to be deleted
     * @return number of objects deleted
     */
    public static int deleteObjectsReferringDocument(String uuid, String className) {
        org.hibernate.Session session = null;
        int number  = 0;
        try{
                session             = PersistenceManager.getSession();
                String queryString  = "select obj from " + className + " obj " +
                        "where obj.uuid=:uuid";
                if ( CrDocumentNodeAttributes.class.getName().equals(className) ) {
                    queryString += " OR publicVersionUUID=:publicVersionUUID";
                }
                Query query         = session.createQuery(queryString);
                if ( CrDocumentNodeAttributes.class.getName().equals(className) ) {
                    query.setParameter("publicVersionUUID", uuid,StringType.INSTANCE);
                }
                query.setParameter("uuid", uuid,StringType.INSTANCE);
                
                Collection<? extends ObjectReferringDocument> objsUsingDoc  = query.list();  
                
                if ( objsUsingDoc != null && objsUsingDoc.size() > 0) {
                    number                                          = objsUsingDoc.size();
                    for (ObjectReferringDocument obj : objsUsingDoc) {
                        obj.remove(session);
                    } 
                }
                session.flush();
                
        }
    
        
        catch (Exception ex) {
            logger.error("Exception : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return number;
    }
    
    public static String processUrl (String urlString, DocumentManagerForm formBean) {
        try {
            URL url = new URL( urlString );
            return url.toString();
        } catch (MalformedURLException e) {
            if ( !urlString.startsWith("http://") )
                return processUrl("http://"+urlString, formBean);
            
            if(formBean!=null){
                formBean.addError("error.contentrepository.addFile.malformedWebLink", "Error adding new document. Web link is malformed.");
            }           

            e.printStackTrace();
            return null;
        }
    }
    
    public static double bytesToMega (long bytes) {
        double size = ((double)bytes) / (1024*1024);
        int temp    = (int)(size * 1000);
        size        = ( (double)temp ) / 1000;
        
        return size;
    }
    
    public static Node getTeamNode(Session jcrWriteSession, Long teamId) {
        return DocumentManagerUtil.getNodeByPath(jcrWriteSession, "team/" + teamId);
    }
    
    public static Node getOrCreateTeamNode(Session jcrWriteSession, Long teamId) {
        Node teamNode = getTeamNode(jcrWriteSession, teamId);
        
        if (teamNode == null) {
            return createTeamNode(jcrWriteSession, teamId);
        }
    
        return teamNode;
    }
    
    public static Node createTeamNode(Session jcrWriteSession, Long teamId) {
        return DocumentManagerUtil.createNodeUsingPath(jcrWriteSession, "team/" + teamId);
    }
    
    public static Node getUserPrivateNode(Session jcrSession, TeamMember teamMember) {
        String userName = teamMember.getEmail();
        String teamId = "" + teamMember.getTeamId();
        return DocumentManagerUtil.getNodeByPath(jcrSession, "private/" + teamId + "/" + userName);
    }
    
    public static Node getOrCreateUserPrivateNode(Session jcrWriteSession, TeamMember teamMember) {
        Node userHomeNode = getUserPrivateNode(jcrWriteSession, teamMember);
        
        if (userHomeNode == null) {
            return createUserPrivateNode(jcrWriteSession, teamMember);
        }
        
        return userHomeNode;
    }
    
    public static Node createUserPrivateNode(Session jcrSession, TeamMember teamMember) {
        String userEmail = teamMember.getEmail();
        String path =  "private/" + teamMember.getTeamId() + "/" + userEmail;
        
        return DocumentManagerUtil.createNodeUsingPath(jcrSession, path);
    }
    
    public static String getWebLinkByUuid(String uuid, HttpServletRequest request) {
        if ( uuid==null || request==null )
            return null;
        NodeWrapper nw      = getReadNodeWrapper(uuid, request);
        String ret          = nw.getWebLink();
        return ret;
    }
    
    /**
     * 
     * @param jcrWriteSession
     * @param path
     */
    public static Node getNodeByPath(Session jcrWriteSession, String path) {
        try {
            Node folderPathNode = jcrWriteSession.getRootNode();
            String[] elements = path.split("/");

            for (int i = 0; i < elements.length; i++) {
                if (folderPathNode.hasNode(elements[i])) {
                    folderPathNode = folderPathNode.getNode(elements[i]);
                } else {
                    return null;
                }
            }
            
            return folderPathNode;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public static Node createNodeUsingPath(Session session, String path) {
        boolean toSave = false;
        try {
            Node folderPathNode = session.getRootNode();
            String[] elements = path.split("/");

            for (int i = 0; i < elements.length; i++) {
                if (folderPathNode.hasNode(elements[i])) {
                    folderPathNode = folderPathNode.getNode(elements[i]);
                } else {
                    folderPathNode.addNode(elements[i]);
                    folderPathNode = folderPathNode.getNode(elements[i]);
                    toSave = true;
                }
            }
            
            if (toSave) {
                session.save();
            }
            
            return folderPathNode;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public class PathHelper {
        private String applicationPath;
        
        public PathHelper() {
            
            
            URL rootUrl         = this.getClass().getResource("/");
            try {
                String path;
                try {
                    path           = rootUrl.toURI().getPath();
                }
                catch (Exception E) {
                    ////System.out.println("PathHelper:::0)Trying to recover from URI error ! ");
                    path                    = rootUrl.getPath();
                }
                //////System.out.println("PathHelper:::1)The path is: " + path);
                if (path.contains( "classes" )) {
                    path    = path + "../";
                }
                //////System.out.println("PathHelper:::2)The path is: " + path);
                if (path.contains( "WEB-INF" )) {
                    path    = path + "../";
                }
                //////System.out.println("PathHelper:::3)The path is: " + path);
                File applicationPathFile    = new File (path);
                applicationPath             = applicationPathFile.getCanonicalPath();
                ////System.out.println("PathHelper:::The application path is: " + applicationPath);
                logger.info("The application path is: " + applicationPath);
            }
            catch (Exception E) {
                logger.error(E.getMessage());
                E.printStackTrace();
                return;
            }
        }
        
        public String getApplicationPath() {
            return applicationPath;
        }       
    }
        
    
    /**
     * get node uuids which were shared or requested to be shared for the given team
     * @param teamId
     * @param state
     * @return
     */
    public static List<String> getSharedNodeUUIDs(TeamMember team, Integer state) {
        Long teamId = team.getTeamId();
        org.hibernate.Session session=null;
        Query qry=null;
        String queryString=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            if(! state.equals(CrConstants.SHARED_AMONG_WORKSPACES)){
                queryString="select r.nodeUUID from " + CrSharedDoc.class.getName() + " r where r.team="+teamId+" and r.state="+state;
            }else{
                queryString="select r.sharedNodeVersionUUID from " + CrSharedDoc.class.getName() + " r where r.team="+teamId+" and r.state="+state;
            }
            
            qry=session.createQuery(queryString);
            return qry.list();
        } catch (Exception e) {
            logger.error("Couldn't Load Resourcess: " + e.toString());
        }
        
        return Collections.emptyList();
    }
    
    public static CrSharedDoc getCrSharedDoc(String uuid,Long teamId, Integer state){
        CrSharedDoc retVal=null;
        org.hibernate.Session session=null;
        String queryString=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            if(state.equals(CrConstants.PENDING_STATUS) || state.equals(CrConstants.SHARED_AMONG_WORKSPACES)){
                queryString="select r from " + CrSharedDoc.class.getName() + " r where r.nodeUUID='"+uuid+"' ";
            }else if(state.equals(CrConstants.SHARED_IN_WORKSPACE)){
                queryString="select r from " + CrSharedDoc.class.getName() + " r where r.sharedPrivateNodeUUID='"+uuid+"' ";
            }
            queryString+=" and r.team="+teamId+" and r.state="+state;
            qry=session.createQuery(queryString);
            retVal=(CrSharedDoc)qry.uniqueResult();
        } catch (Exception e) {
            logger.error("Couldn't Load Resourcess: " + e.toString());
        }
        return retVal;
    }
    
    /**
     * searches if given node was shared on any level and if found returns them
     * uuid can ne base node uuid or shared version uuid
     */
    public static List<CrSharedDoc> getSharedDocsForGivenNodeUUID(String uuid){
        List<CrSharedDoc> retVal=null;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select r from " + CrSharedDoc.class.getName() + " r where r.nodeUUID='"+uuid+"' or (r.sharedPrivateNodeUUID='"+uuid+"' and r.state!="+CrConstants.PENDING_STATUS+" )"
            +" or r.sharedNodeVersionUUID='"+uuid+"'";
            qry=session.createQuery(queryString);
            retVal=qry.list();
        } catch (Exception e) {
            logger.error("Couldn't Load Resourcess: " + e.toString());
        }
        return retVal;
    }
    
    /**
     * 
     * @param nodeUUID
     * @return version number of the node, which was requested to be share, if such exists.
     */
    public static List<String> isPrivateResourceShared(String nodeUUID){
        List<String> retVal=null;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select r.sharedNodeVersionUUID from " + CrSharedDoc.class.getName() + " r where ";
            queryString+=   " (r.sharedPrivateNodeUUID='"+nodeUUID+"'  and r.state="+CrConstants.SHARED_IN_WORKSPACE+")";
            
            qry=session.createQuery(queryString);
            retVal=qry.list();
        } catch (Exception e) {
            logger.error("Couldn't Load Resources: " + e.toString());
        } 
        return retVal;
    }
    
    /**
     * checks whether team node's some version is shared with given workspace,if found returns it's shared version
     */
    public static String isTeamResourceSharedWithGivenWorkspace(String nodeUUID,Long workspaceId){
        CrSharedDoc sharedRecord=loadTeamResourceSharedWithWorkspace(nodeUUID, workspaceId);
        String retVal=null;
        if(sharedRecord!=null){
            retVal=sharedRecord.getSharedNodeVersionUUID();
        }       
        return retVal;
    }
    
    /**
     * checks whether team node's any version is shared with given workspace and if found, returns it
     */
    public static CrSharedDoc loadTeamResourceSharedWithWorkspace(String nodeUUID,Long workspaceId){
        CrSharedDoc retVal=null;
        org.hibernate.Session session=null;
        Query qry=null;
        String queryString=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            queryString="select r from " + CrSharedDoc.class.getName() + " r where r.state="+CrConstants.SHARED_AMONG_WORKSPACES;
            if(workspaceId!=null){
                queryString+=" and r.sharedNodeVersionUUID='"+nodeUUID+"' and r.team="+workspaceId;
            }else{
                queryString+=" and r.nodeUUID='"+nodeUUID+"'";
            }
            qry=session.createQuery(queryString);
            qry.setMaxResults(1); //if some version of the node is globally shared, this version is shared for all workspaces and not different for each workspace
            retVal=(CrSharedDoc)qry.uniqueResult();
        } catch (Exception e) {
            logger.error("Couldn't Load Resources: " + e.toString());
        } 
        return retVal;
    }
    
    public static boolean isGivenVersionShared(String versionUUID){
        boolean retVal=false;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select count(r.sharedNodeVersionUUID) from " + CrSharedDoc.class.getName() + 
            " r where r.sharedNodeVersionUUID='"+versionUUID+"' and r.state!="+CrConstants.PENDING_STATUS;
            qry=session.createQuery(queryString);
            Long longAmount = (Long) qry.uniqueResult();
            int amount=longAmount.intValue();
            if(amount>0){
                retVal=true;
            }
        } catch (Exception e) {
            logger.error("Couldn't Load Resources: " + e.toString());
        }
        return retVal;
    }
    
    /**
     * checks whether resource(given version) was requested to be shared and is still not approved by TL
     * @param nodeUUID
     * @return
     */
    public static boolean isResourcePendingtoBeShared (String nodeUUID){
        boolean retVal=false;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select count(r) from " + CrSharedDoc.class.getName() +" r where r.nodeUUID='"+nodeUUID+"' and r.state="+CrConstants.PENDING_STATUS;
            qry=session.createQuery(queryString);
            Long longAmount = (Long) qry.uniqueResult();
            int amount=longAmount.intValue();
            if(amount>0){
                retVal=true;
            }
        } catch (Exception e) {
            logger.error("Couldn't Load Resources: " + e.toString());
        }
        return retVal;
    }
    
    public static boolean isResourceVersionPendingtoBeShared (String nodeUUID, String versionUUID){
        boolean retVal=false;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select count(r) from " + CrSharedDoc.class.getName() +" r " +
                    "where r.nodeUUID=:nodeUUID and r.state=:state and r.sharedNodeVersionUUID=:versionUUID" ;
            qry=session.createQuery(queryString);
            qry.setParameter("nodeUUID", nodeUUID, StringType.INSTANCE);
            qry.setParameter("versionUUID", versionUUID, StringType.INSTANCE);
            qry.setParameter("state", CrConstants.PENDING_STATUS, IntegerType.INSTANCE);
            Long longAmount = (Long) qry.uniqueResult();
            int amount=longAmount.intValue();
            if(amount>0){
                retVal=true;
            }
        } catch (Exception e) {
            logger.error("Couldn't Load Resources: " + e.toString());
        }
        return retVal;
    }
    

    public static NodeLastApprovedVersion getlastApprovedVersionOfTeamNode(String nodeUUID){
        NodeLastApprovedVersion retVal=null;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select r from "+NodeLastApprovedVersion.class.getName() +" r where r.nodeUUID='"+nodeUUID+"'";          
            qry=session.createQuery(queryString);           
            retVal=(NodeLastApprovedVersion)qry.uniqueResult();
        } catch (Exception e) {
            logger.error("Couldn't Load Last Approved Version : " + e.toString());
        } 
        return retVal;
    }
    
    public static Map<String, NodeLastApprovedVersion> getLastApprovedVersionsByUUIDMap() {
        Map<String, NodeLastApprovedVersion> versionsMap = new HashMap<>();
        for (NodeLastApprovedVersion version : getLastApprovedVersions()) {
            versionsMap.put(version.getNodeUUID(), version);
        }
        
        return versionsMap;
    }
    
    public static List<NodeLastApprovedVersion> getLastApprovedVersions() {
        org.hibernate.Session session = PersistenceManager.getRequestDBSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<NodeLastApprovedVersion> criteriaQuery = criteriaBuilder.createQuery(NodeLastApprovedVersion.class);
        Root<NodeLastApprovedVersion> root = criteriaQuery.from(NodeLastApprovedVersion.class);
        criteriaQuery.select(root);
        Query<NodeLastApprovedVersion> query = session.createQuery(criteriaQuery);


        return query.list();

    }
    
    public static List<TeamNodePendingVersion> getPendingVersionsForResource(String nodeUUID){
        List<TeamNodePendingVersion> retVal=null;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select r from "+TeamNodePendingVersion.class.getName() +" r where r.nodeUUID='"+nodeUUID+"'";           
            qry=session.createQuery(queryString);           
            retVal=qry.list();
        } catch (Exception e) {
            logger.error("Couldn't Load Last Approved Version : " + e.toString());
        } 
        return retVal;
    }
    
    /**
     * check whether node's versions is pending approval and if true, return this record
     * @param versionID
     * @return
     */
    public static TeamNodePendingVersion isGivenVersionPendingApproval(String versionID){
        TeamNodePendingVersion retVal=null;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select r from "+TeamNodePendingVersion.class.getName() +" r where r.versionID='"+versionID+"'";         
            qry=session.createQuery(queryString);           
            retVal=(TeamNodePendingVersion)qry.uniqueResult();
        } catch (Exception e) {
            logger.error("Couldn't Load Last Approved Version : " + e.toString());
        } 
        return retVal;
    }
    
    /**
     * check whether the first version of this resource was shared from private space(Used for Team Level Share and Not Across Wokrspaces)
     * if found, return the record
     */
    public static CrSharedDoc isTeamResourceSharedFromPrivateSpace(String nodeUUID){
        CrSharedDoc retVal=null;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select r from " + CrSharedDoc.class.getName() + " r where r.nodeUUID='"+nodeUUID+"'  and r.state="+CrConstants.SHARED_IN_WORKSPACE;         
            qry=session.createQuery(queryString);
            retVal=(CrSharedDoc)qry.uniqueResult();
        } catch (Exception e) {
            logger.error("Couldn't Load Resources: " + e.toString());
        } 
        return retVal;
    }
    
    /**
     * get's it's unapproved and last approved versions if found any
     * @param nodeUUID
     * @return
     */
    public static List<TeamNodeState> getTeamNodeState(String nodeUUID){
        List<TeamNodeState> retVal=null;
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="select r from "+TeamNodeState.class.getName() +" r where r.nodeUUID='"+nodeUUID+"'";            
            qry=session.createQuery(queryString);           
            retVal=qry.list();
        } catch (Exception e) {
            logger.error("Couldn't Load Team Node States : " + e.toString());
        } 
        return retVal;
    }
    
    public static void deleteNodeStates(String nodeUUID){
        org.hibernate.Session session=null;
        Query query;
        if(nodeUUID!=null){
            try {
                session=PersistenceManager.getRequestDBSession();
                String qhl="delete from " + TeamNodeState.class.getName()+" t  where t.nodeUUID='"+nodeUUID+"'";
                query=session.createQuery(qhl);
                query.executeUpdate();
            } catch (Exception e) {
                logger.error("Delete Failed: " +e.toString());
            }       
        }       
    }
    
    public static void deleteAllShareRecordsrelatedToResource(String nodeUUID){
        org.hibernate.Session session=null;
        Query query;
        if(nodeUUID!=null){
            try {
                session=PersistenceManager.getRequestDBSession();
                String qhl="delete from " + CrSharedDoc.class.getName() + " r where r.nodeUUID='"+nodeUUID+"' or (r.sharedPrivateNodeUUID='"+nodeUUID+"' and r.state!="+CrConstants.PENDING_STATUS+" )";
                query=session.createQuery(qhl);
                query.executeUpdate();
            } catch (Exception e) {
                logger.error("Delete Failed: " +e.toString());
            }       
        }
    }
    
    public static void deleteTeamNodePendingVersion(String nodeUUID, String versionUUID){
        org.hibernate.Session session=null;
        Query qry=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            String queryString="delete from "+TeamNodePendingVersion.class.getName() +" r where r.nodeUUID='"+nodeUUID+"' and r.versionID='"+versionUUID+"'";           
            qry=session.createQuery(queryString);
            qry.executeUpdate();
        } catch (Exception e) {
            logger.error("Delete Failed:  " + e.toString());
        }
    }
    
    public synchronized static void shutdownRepository( ServletContext sContext ) {
        logger.info("Shutting down jackrabbit repository");
        JackrabbitRepository repository         = (JackrabbitRepository)sContext.getAttribute( CrConstants.JACKRABBIT_REPOSITORY );
        if ( repository == null ) {
            logger.warn("No repository found! Only normal if AMP was not used at all !");
        } else {
            repository.shutdown();
            logger.info("Jackrabbit repository shutdown succesfully !");
        }
    }
    
    public static boolean privateDocumentsExist(Session session, TeamMember teamMember) {
        String userName = teamMember.getEmail();
        String teamId = String.valueOf(teamMember.getTeamId());
        Node node = DocumentManagerUtil.getNodeByPath(session, "private/" + teamId + "/" + userName);
        
        if (node != null) {
            try {
                return node.hasNodes();
            } catch (RepositoryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }
    
    public static boolean teamDocumentsExist(Session session, TeamMember teamMember) {
        Node node = DocumentManagerUtil.getNodeByPath(session, "team/" + teamMember.getTeamId());
        if (node != null) {
            try {
                return node.hasNodes();
            } catch (RepositoryException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        return false;
    }
    
    
    public static boolean sharedDocumentsExist(TeamMember teamMember){
        boolean retVal=false;
        org.hibernate.Session session=null;
        Query qry=null;
        String queryString=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            queryString="select count(r.nodeUUID) from " + CrSharedDoc.class.getName() + " r where r.team="+teamMember.getTeamId()+" and r.state="+CrConstants.SHARED_AMONG_WORKSPACES;
            qry=session.createQuery(queryString);
            Long longValue = (Long) qry.uniqueResult();
            int value=longValue.intValue();
            retVal= value > 0;
        } catch (Exception e) {
            logger.error("Couldn't Load Resourcess: " + e.toString());
        }
        return retVal;
    }
    
    public static boolean publicDocumentsExist(TeamMember teamMember){
        boolean retVal=false;       
        org.hibernate.Session session=null;
        Query qry=null;
        String queryString=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            queryString="select count(r) from " + CrDocumentNodeAttributes.class.getName() + " r";
            qry=session.createQuery(queryString);
            Long longValue = (Long) qry.uniqueResult();
            int value=longValue.intValue();
            retVal= value > 0;
        } catch (Exception e) {
            logger.error("Couldn't Load Resourcess: " + e.toString());
        }
        return retVal;
    }

    public static void setMaxFileSizeAttribute(HttpServletRequest request) {
        String maxFileSizeGS = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE);
        request.setAttribute("uploadFailedTooBigMsg", "The file size limit is {size} MB. This file exceeds the limit.");
        request.setAttribute("maxFileSizeGS", maxFileSizeGS);
        request.setAttribute("uploadMaxFileSize",
                Long.toString(Bytes.megabytes(Long.parseLong(maxFileSizeGS)).bytes()));
    }
    
    public static ArrayList<DocumentData> retrieveTemporaryDocDataList(HttpServletRequest request) {
        HashMap<String, Object> map = SelectDocumentDM.getContentRepositoryHashMap(request);
        ArrayList<DocumentData> list = (ArrayList<DocumentData>) map
                .get(ActivityDocumentsConstants.TEMPORARY_DOCUMENTS);
        if (list == null || (list != null && list.size() == 0)) {
            list = new ArrayList<DocumentData>();
            map.put(ActivityDocumentsConstants.TEMPORARY_DOCUMENTS, list);
        }
        
        return list;
    }
}
