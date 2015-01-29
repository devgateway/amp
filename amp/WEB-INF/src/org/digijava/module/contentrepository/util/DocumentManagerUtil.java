package org.digijava.module.contentrepository.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.jcr.InvalidItemStateException;
import javax.jcr.NamespaceException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.jackrabbit.api.JackrabbitRepository;
import org.apache.jackrabbit.core.NodeImpl;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.action.DocumentManager;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.action.SetAttributes;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.dbentity.CrSharedDoc;
import org.digijava.module.contentrepository.dbentity.NodeLastApprovedVersion;
import org.digijava.module.contentrepository.dbentity.TeamNodePendingVersion;
import org.digijava.module.contentrepository.dbentity.TeamNodeState;
import org.digijava.module.contentrepository.exception.CrException;
import org.digijava.module.contentrepository.exception.JCRSessionException;
import org.digijava.module.contentrepository.exception.NoNodeInVersionNodeException;
import org.digijava.module.contentrepository.exception.NoVersionsFoundException;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.digijava.module.contentrepository.helper.TeamInformationBeanDM;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.hibernate.Query;


public class DocumentManagerUtil {
	private static final String IS_ALREADY_LOCKED_BY_THE_CURRENT_PROCESS = "is already locked by the current process";
	
	/* Name for jcr sessions stored in http request */	
	private static final String JCR_READ_SESSION						= "jcrReadSession";
	private static final String JCR_WRITE_SESSION						= "jcrWriteSession";

	private static Logger logger	= Logger.getLogger(DocumentManagerUtil.class);
	
	private static Object repoLock = new Object();
	
	/**
	 * returns null if failed to open repo
	 * @param context
	 * @return
	 */
	public static Repository getJCRRepository (ServletContext context) 
	{		
		if (context == null) {
			logger.error("The request doesn't contain a ServletContext", new RuntimeException());
			return null;
		}
		synchronized (repoLock) {
			Repository repository = (Repository) context.getAttribute(CrConstants.JACKRABBIT_REPOSITORY);
			if (repository == null) {
				try{
					String appPath				= DocumentManagerUtil.getApplicationPath();
					String repPath				= appPath + "/jackrabbit";
					repository 					= new TransientRepository(repPath + "/repository.xml", repPath);
					context.setAttribute(CrConstants.JACKRABBIT_REPOSITORY, repository);
				} catch (Exception e) {
					logger.error("error opening JackRabbit repository", e);
					return null; 
				}
			}
			return repository;
		}
	}
	
	public static void shutdownJCRRepository(ServletContext context)
	{		
		if (context == null)
		{
			logger.error("The request doesn't contain a ServletContext");
			return;
		}
		synchronized (repoLock)
		{
			Repository repository = (Repository)context.getAttribute(CrConstants.JACKRABBIT_REPOSITORY);
			if (repository != null)
			{
				context.removeAttribute(CrConstants.JACKRABBIT_REPOSITORY);
				((TransientRepository)repository).shutdown();
			}
		}
	}

    public static String getUUIDByPublicVersionUUID (String publicVersionUUID) {
        String retVal = null;
        org.hibernate.Session session = null;
        try {
            session	= PersistenceManager.getSession();
            StringBuilder queryStr = new StringBuilder("select obj.uuid from ").
                    append(CrDocumentNodeAttributes.class.getName()).
                    append(" obj where obj.publicVersionUUID=:publicVersionUUID");
            Query query = session.createQuery(queryStr.toString());
            query.setString("publicVersionUUID", publicVersionUUID);
            retVal = (String) query.uniqueResult();
            //session.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
	
    /**
     * to be called before every HttpServletRequest processed
     * @param request
     */
    public static void initJCRSessions(HttpServletRequest request)
    {
    	//nop
    }

    /**
     * to be called after every HttpServletRequest processed
     * @param request
     */
    public static void closeJCRSessions(HttpServletRequest request)
    {
    	logoutJcrSessions(request);
    }
	
	/**
	 * returns null if logging in failed for some reason
	 * @param context
	 * @return
	 */
	public static Session getSession(ServletContext context, SimpleCredentials creden)
	{
		synchronized (repoLock) {			
			try
			{
				Repository rep = getJCRRepository(context);
				if (rep == null)
					return null;
				if (creden == null)
					return rep.login();
				else
					return rep.login(creden);
			}
			catch (Exception re) 
			{
				if ( re.getMessage().contains(IS_ALREADY_LOCKED_BY_THE_CURRENT_PROCESS) ) 
				{
					logger.error("error trying to login to JCR, trying to recover by shutting down the repo", re);
					shutdownJCRRepository(context);
				}
				return null;
			}
		}
	}
	
	/**
	 * closes a JCR session, guaranteed exception- and fat- free
	 * @param sess
	 */
	public static void closeSession(Session sess)
	{
		if (sess == null)
			return;		
		try{sess.logout();}
		catch(Exception e){logger.error("paranoid exception caught while logging out of a JR session", e);}; // just being paranoid
	}
	
	/**
	 * if given Session is null or alive, returns it without touching it. Else, closes it and returns null
	 * @param sess
	 * @return
	 */
	public static Session annulateSessionIfNotAlive(Session sess)
	{
		if (sess == null)
			return null;
		
		if (sess != null && (!sess.isLive()))
		{
			closeSession(sess);
			return null;
		}
		return sess;
	}
	
	public static Session getReadSession(HttpServletRequest request) {
		synchronized (repoLock)
		{		
			Session jcrSession		= annulateSessionIfNotAlive((Session) request.getAttribute(JCR_READ_SESSION));
			
			if (jcrSession == null)
				jcrSession = getSession(request.getSession().getServletContext(), null);
			
			if (jcrSession == null)
			{
				logger.warn("trying to open a JCR read session for the second time...");
				jcrSession = getSession(request.getSession().getServletContext(), null);
			}
			
			if (jcrSession == null)
				throw new RuntimeException("could not open a JCR ReadSession, giving up");
			
			request.setAttribute(JCR_READ_SESSION, jcrSession);
			
			try {
				jcrSession.getRootNode().refresh(false);
			} 
			catch (Exception e) {
				logger.error("could not refresh() readSession", e);
			}
			
			return jcrSession;
		}
	}
	
	public static void logoutJcrSessions(HttpServletRequest httpRequest) {
		synchronized (repoLock) {
			closeSession((Session) httpRequest.getAttribute(JCR_WRITE_SESSION));
			httpRequest.removeAttribute(JCR_WRITE_SESSION);
			
			closeSession((Session) httpRequest.getAttribute(JCR_READ_SESSION));
			httpRequest.removeAttribute(JCR_READ_SESSION);
		}
	}
	
	public static Session getWriteSession(HttpServletRequest request)
	{
		synchronized (repoLock)
		{
			Session jcrSession = annulateSessionIfNotAlive((Session) request.getAttribute(JCR_WRITE_SESSION));				
			
			boolean newlyCreatedSession = (jcrSession == null);
			
			if (jcrSession == null)
			{
				HttpSession session = request.getSession();

				if (session == null)
				{
					throw new RuntimeException("no session found! why?");
				}
				
				TeamMember teamMember = (TeamMember) session.getAttribute(Constants.CURRENT_MEMBER);
				String userName;
				if (teamMember != null && teamMember.getEmail() != null) {
				   userName = teamMember.getEmail();
				} else {
				   userName = "admin@amp.org";
				}
				
				SimpleCredentials creden	= new SimpleCredentials(userName, userName.toCharArray());

				if (jcrSession == null)
					jcrSession = getSession(request.getSession().getServletContext(), creden);
				
				if (jcrSession == null)
				{
					logger.warn("trying to open a JCR write session for the second time...");
					jcrSession = getSession(request.getSession().getServletContext(), creden);
				}
				if (jcrSession == null)
					throw new JCRSessionException("could not open a JCR WriteSession");
			}
	
			
			if (newlyCreatedSession)
			{
				try {jcrSession.save();}
				catch(Exception e){logger.error("error saving JCR WriteSession", e);}
				registerNamespace(jcrSession, "ampdoc", "http://amp-demo.code.ro/ampdoc");
				registerNamespace(jcrSession, "amplabel", "http://amp-demo.code.ro/label");
			}
			
			request.setAttribute(JCR_WRITE_SESSION, jcrSession);
			try {
				Node rootNode = jcrSession.getRootNode();
				if (rootNode == null)
					throw new RuntimeException("jcr root node is null, how can this be?");				
				rootNode.refresh(false);
			}			
			catch (Exception e) {
				logger.error("could not refresh() writeSession", e);
			}
			
			return jcrSession;
		}
	}
	
	public static NodeWrapper getReadNodeWrapper(String uuid, HttpServletRequest request) {
		Node n = getReadNode(uuid, request);
		if (n != null) 
		{
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
	
	/**
	* DEBUG CODE
	*/
	public static Set<DocumentData> collectRepository(Node node, Set<DocumentData> bld) throws RepositoryException{
		DocumentData dd = DocumentData.buildFromNode(node);
		if ((dd != null) && (dd.getName() != null)){
			bld.add(dd);
			
			if (isVersionable(node)){
				VersionHistory vh = node.getVersionHistory();
				NodeIterator niter	= vh.getNodes();
				while (niter.hasNext()){
					Node n = niter.nextNode();
					DocumentData docData = DocumentData.buildFromNodeVersion(n, dd);
					docData.setName(dd.getName() + " VERSION with uuid = " + docData.getUuid());
					bld.add(docData);
				}
			}
		}
		
		NodeIterator children = node.getNodes();
		while(children.hasNext()){
			collectRepository(children.nextNode(), bld);
		}
		return bld;
	}
	/**
	 * debug-only function: return blabla
	 * @param session
	 * @param bld
	 * @return
	 */
	public static Set<DocumentData> collectRepository(Session session, Set<DocumentData> bld){
		try{
			collectRepository(session.getRootNode() , bld);
		}
		catch(Exception e){
			e.printStackTrace(); // this is a debug-only function, so it is acceptable
		}
		return bld;
	}
	
	public static Node getReadNode(String uuid, HttpServletRequest request) {
		Session session	= getReadSession(request);
		try {
			//session.getRootNode().refresh(false);
			//session.refresh(false);
			return session.getNodeByUUID(uuid);
		} catch (Exception e) {
//			e.printStackTrace();  DEBUG CODE - DO NOT DELETE THE COMMENTED CODE BELOW
//			Set<DocumentData> allDocuments = collectRepository(session, new TreeSet<DocumentData>(DocumentData.COMPARATOR_BY_NAME));
//			for(DocumentData doc:allDocuments)
//				logger.error("\t" + doc.toString());
			return null;
		}
	}

	public static Node getWriteNode (String uuid, HttpServletRequest request) {
		Session session	= getWriteSession(request);
		try {
			//session.getRootNode().refresh(false);
			//session.refresh(false);
			return session.getNodeByUUID(uuid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	private static void registerNamespace(Session session, String namespace, String uri) {
		Workspace workspace					= session.getWorkspace();
		NamespaceRegistry namespaceRegistry	= null;
		try {
			namespaceRegistry	= workspace.getNamespaceRegistry();
			namespaceRegistry.getURI(namespace);
		} catch(NamespaceException e) {
			logger.info("Namespace " + namespace + "not found. Creating it now.");
			try {
				namespaceRegistry.registerNamespace(namespace, uri);
			} catch (RepositoryException e1) {
				// TODO Auto-generated catch block
				logger.error("Couldn't create namespace");
				e1.printStackTrace();
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public static String getApplicationPath() {
		PathHelper ph	= new DocumentManagerUtil().new PathHelper();
		return ph.getApplicationPath();
	}
	
	public static String calendarToString(Calendar cal,boolean yearofPublication) {
		String retVal=null;
//		String [] monthNames	= {"", "January", "February", "March", "April", "May", "June",
//									"July", "August", "September", "October", "November", "December"};
		
		int year		= cal.get(Calendar.YEAR);
		int month		= cal.get(Calendar.MONTH) + 1;
		int day			= cal.get(Calendar.DAY_OF_MONTH);
		
//		int hour		= cal.get(Calendar.HOUR_OF_DAY);
//		int minute		= cal.get(Calendar.MINUTE);
//		int second		= cal.get(Calendar.SECOND);
		
		if(yearofPublication){
			retVal= new Long(year).toString() ;
		}else{
			retVal=month + "/" + day + "/" + year ;
		}
		return retVal ;
	}
	
	public static Node getNodeOfLastVersion(String currentUUID, HttpServletRequest request) throws CrException, RepositoryException {
		List<Version> versions	= getVersions(currentUUID, request, true);
		
		if (versions == null || versions.size() == 0) 
				throw new NoVersionsFoundException("No versions were found for node with UUID: " + currentUUID);
		
		Version lastVersion	= versions.get( versions.size()-1 );
		
		NodeIterator niter	= lastVersion.getNodes();
		
		if ( !niter.hasNext() ) {
			throw new NoNodeInVersionNodeException("The last version node of node with UUID " + currentUUID + " doesn't contain any nodes");
		}
		
		return niter.nextNode();		
	}
	public static Node getLastVersionNotWaitingApproval(String currentUUID, HttpServletRequest request) throws CrException, RepositoryException {
		List<Version> versions	= getVersions(currentUUID, request, true);
		if (versions == null || versions.size() == 0) 
			throw new NoVersionsFoundException("No versions were found for node with UUID: " + currentUUID);
		
		for ( int i=versions.size()-1; i>=0; i-- ) {
			Version v			= versions.get(i);
			NodeIterator nIter	= v.getNodes();
			if ( nIter.hasNext() ) {
				Node n				= nIter.nextNode();
				if ( isGivenVersionPendingApproval(n.getUUID()) != null ) 
					continue;
				else
					return n;
			}
		}
		return null;
		
		
		
	}
	
	public static int getNextVersionNumber(String uuid, HttpServletRequest request) {
		List versions	= getVersions(uuid, request, false);
		return versions.size() + 1;
	}

	public static List<Version> getVersions(String uuid, HttpServletRequest request, boolean needWriteSession) {
		if (uuid != null) {
			Node node;
			ArrayList<Version> versions		= new ArrayList<Version>();
			if (needWriteSession)
				node				= DocumentManagerUtil.getWriteNode(uuid, request);
			else
				node				= DocumentManagerUtil.getReadNode(uuid, request);
			VersionHistory history;
			try {
				history 						= node.getVersionHistory();
				//Version baseVersion				= history.getBaseVersion();
				//String uuidBaseVersion			= baseVersion.getUUID();
				VersionIterator iterator		= history.getAllVersions();
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
		Boolean hasDeleteRights		= DocumentManagerRights.hasDeleteRights(uuid, request);
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
	public static boolean deleteNode(Session session, String uuid){
		if (uuid == null)
			return false;
		if (session == null)
			session = getWriteSession(TLSUtils.getRequest());
		boolean successfully = false;
		String name = "";
		try {
			Node node = session.getNodeByUUID(uuid);
			name = new NodeWrapper(node).tryGetName();
			Node parent = node.getParent();
			node.remove();
			parent.save();			
			successfully = true;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			logger.error(String.format("DELETED JackRabbit node with uuid = %s, name = %s, success: %b", uuid, name, successfully));
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
		TeamInformationBeanDM teamInfo	= new TeamInformationBeanDM();
		teamInfo.setMeTeamMember( (TeamMember)session.getAttribute(Constants.CURRENT_MEMBER) );
		
		TeamMember me	= teamInfo.getMeTeamMember();
		
		if (me != null) {
			teamInfo.setIsTeamLeader( me.getTeamHead() );
			teamInfo.setMyTeamMembers( TeamMemberUtil.getAllTeamMembers(me.getTeamId()) );
		}
		
		return teamInfo;
	}
	
	public static Collection<DocumentData> createDocumentDataCollectionForActivityPreview(HttpServletRequest request) {
		Collection<String> UUIDs = SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false);
		ArrayList<DocumentData> ret = new ArrayList<DocumentData>();
		if ( UUIDs == null )
			return null;
		try {
			ArrayList<Node> documents = new ArrayList<Node>();
			Iterator<String> iter = UUIDs.iterator();
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
			Iterator iterator			= documents.iterator();
			while ( iterator.hasNext() ) {
				Node documentNode	= (Node)iterator.next();
				//Node baseNode=documentNode; 
				String documentNodeBaseVersionUUID=documentNode.getUUID();
				//NodeLastApprovedVersion nlpv	= DocumentManagerUtil.getlastApprovedVersionOfTeamNode(documentNodeBaseVersionUUID);
				NodeWrapper nodeWrapper	= new NodeWrapper(documentNode);
				String fileName	=  nodeWrapper.getName();
				if ( fileName == null && nodeWrapper.getWebLink() == null ){
					continue;
				}
				
				DocumentData documentData = DocumentData.buildFromNodeWrapper(nodeWrapper, fileName, documentNodeBaseVersionUUID, nodeWrapper.getUuid());				
				ret.add(documentData);
			}	
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public static Collection<DocumentData> createDocumentDataCollectionFromSession(HttpServletRequest request) {
		Collection<String> UUIDs = SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false);
		if ( UUIDs == null ) {
			return null;
		}	
		try {
			DocumentManager dm = new DocumentManager();
			Collection<DocumentData> ret = dm.getDocuments(UUIDs, request, null, false, true);
			ret.addAll(TemporaryDocumentData.retrieveTemporaryDocDataList(request));
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
//	public static boolean checkFileSize(FormFile formFile, ActionMessages errors) {
//		int maxFileSizeInBytes		= Integer.MAX_VALUE;
//		int maxFileSizeInMBytes		= Integer.MAX_VALUE;
//		String maxFileSizeGS		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE); // File size in MB
//		if (maxFileSizeGS != null) {
//				maxFileSizeInMBytes		= Integer.parseInt( maxFileSizeGS );
//				maxFileSizeInBytes		= 1024 * 1024 * maxFileSizeInMBytes; 
//		}
//		if ( formFile.getFileSize() > maxFileSizeInBytes) {
//			errors.add("title", 
//					new ActionMessage("error.contentrepository.addFile.fileTooLarge", maxFileSizeInMBytes + "")
//					);
//			return false;
//			}
//		if (formFile.getFileSize()<1){
//			ActionMessage	error	= new ActionMessage("error.contentrepository.addFile.badPath");
//			errors.add("title", error);
//			return false;
//		}
//		return true;
//	}

	
	public static boolean checkFileSize(FormFile formFile, ActionMessages errors) {
		long maxFileSizeInBytes		= Long.MAX_VALUE;
		long maxFileSizeInMBytes	= Long.MAX_VALUE;
		String maxFileSizeGS		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE); // File size in MB
		if (maxFileSizeGS != null) {
				maxFileSizeInMBytes		= Integer.parseInt( maxFileSizeGS );
				maxFileSizeInBytes		= 1024 * 1024 * maxFileSizeInMBytes; 
		}
		if ( formFile.getFileSize() > maxFileSizeInBytes) {
			errors.add("title",
					new ActionMessage("error.contentrepository.addFile.fileTooLarge", maxFileSizeInMBytes)
					);
			
			return false;
			}
		if (formFile.getFileSize()<1){
			errors.add("title", 
					new ActionMessage("error.contentrepository.addFile.badPath")
					);
			
			return false;
		}
		return true;
	}
	
	public static boolean checkStringAsNodeTitle (String string) {
		for (int i=0; i<string.length(); i++) {
			char ch					= string.charAt(i);
			Character charObj		= new Character(ch);
			
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
		int number	= 0;
		try{
				session				= PersistenceManager.getSession();
				String queryString	= "select obj from " + className + " obj " +
						"where obj.uuid=:uuid";
				if ( CrDocumentNodeAttributes.class.getName().equals(className) ) {
					queryString += " OR publicVersionUUID=:publicVersionUUID";
				}
				Query query			= session.createQuery(queryString);
				if ( CrDocumentNodeAttributes.class.getName().equals(className) ) {
					query.setString("publicVersionUUID", uuid);
				}
				query.setString("uuid", uuid);
				
				Collection<? extends ObjectReferringDocument> objsUsingDoc	= query.list();  
				
				if ( objsUsingDoc != null && objsUsingDoc.size() > 0) {
					number											= objsUsingDoc.size();
					Iterator<? extends ObjectReferringDocument> iter	= objsUsingDoc.iterator(); 
					while( iter.hasNext() ) {
						ObjectReferringDocument obj		= iter.next();
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
			URL url	= new URL( urlString );
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
		double size	= ((double)bytes) / (1024*1024);
		int temp	= (int)(size * 1000);
		size		= ( (double)temp ) / 1000;
		
		return size;
	}
	
	public static Node getTeamNode(Session jcrWriteSession, Long teamId){
		//String teamId		= "" + team.getAmpTeamId();		
		return	DocumentManagerUtil.getNodeByPath(jcrWriteSession, null, "team/"+teamId);
	}
	
	@Deprecated
	//please use getTeamNode(Session jcrWriteSession, Long teamId) instead
	public static Node getTeamNode(Session jcrWriteSession, TeamMember teamMember){
		String teamId		= "" + teamMember.getTeamId();
		
		return	DocumentManagerUtil.getNodeByPath(jcrWriteSession, teamMember, "team/"+teamId);
	}
	public static Node getUserPrivateNode(Session jcrWriteSession, TeamMember teamMember){
		String userName		= teamMember.getEmail();
		String teamId		= "" + teamMember.getTeamId();
			
				
		return	DocumentManagerUtil.getNodeByPath(jcrWriteSession, teamMember, "private/"+teamId+"/"+userName);
	}
	
	public static Node getTeamPendingNode(Session jcrWriteSession, TeamMember teamMember){
		String teamId		= "" + teamMember.getTeamId();
		return	DocumentManagerUtil.getNodeByPath(jcrWriteSession, teamMember, "pending/"+teamId);
		
	}
	
	
	public static String getWebLinkByUuid(String uuid, HttpServletRequest request) {
		if ( uuid==null || request==null )
			return null;
		NodeWrapper nw		= getReadNodeWrapper(uuid, request);
		String ret			= nw.getWebLink();
		return ret;
	}
	
	/**
	 * 
	 * @param jcrWriteSession
	 * @param teamMember
	 * @param path
	 */
	public static Node getNodeByPath(Session jcrWriteSession, TeamMember teamMember, String path) {
		Node folderNode	= null;
		
		try {
			Node tempNode;
			
			folderNode	= jcrWriteSession.getRootNode();
		
			String [] elements	= path.split("/");
			
			for (int i=0; i<elements.length; i++) {
				
					try{
						tempNode	= folderNode.getNode( elements[i] );
					}
					catch (PathNotFoundException e) {
						logger.info("Node '" + elements[i] + "' not created from path '" + path + "'. Trying to create now.");
						try {
							tempNode	= folderNode.addNode( elements[i] );
						}
						catch(Exception E) {
							logger.error("Cannot create '" + elements[i] + "' node from path '" + path + "'.");
							e.printStackTrace();
							return null;
						}
					}
					catch (RepositoryException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
					folderNode	= tempNode;
				}
				
				return folderNode;
		
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public class PathHelper {
		private String applicationPath;
		
		public PathHelper() {
			
			
			URL rootUrl			= this.getClass().getResource("/");
			try {
				String path;
				try {
					path					= rootUrl.toURI().getPath();
				}
				catch (Exception E) {
					////System.out.println("PathHelper:::0)Trying to recover from URI error ! ");
					path					= rootUrl.getPath();
				}
				//////System.out.println("PathHelper:::1)The path is: " + path);
				if (path.contains( "classes" )) {
					path	= path + "../";
				}
				//////System.out.println("PathHelper:::2)The path is: " + path);
				if (path.contains( "WEB-INF" )) {
					path	= path + "../";
				}
				//////System.out.println("PathHelper:::3)The path is: " + path);
				File applicationPathFile	= new File (path);
				applicationPath				= applicationPathFile.getCanonicalPath();
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
	public static List<String> getSharedNodeUUIDs(Long teamId,Integer state){
		List<String> retVal=null;
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
			retVal=qry.list();
		} catch (Exception e) {
			logger.error("Couldn't Load Resourcess: " + e.toString());
		}
		return retVal;
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
			queryString+=	" (r.sharedPrivateNodeUUID='"+nodeUUID+"'  and r.state="+CrConstants.SHARED_IN_WORKSPACE+")";
			
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
			int amount=(Integer)qry.uniqueResult();
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
			int amount=(Integer)qry.uniqueResult();
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
			qry.setString("nodeUUID", nodeUUID);
			qry.setString("versionUUID", versionUUID);
			qry.setInteger("state", CrConstants.PENDING_STATUS);
			int amount=(Integer)qry.uniqueResult();
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
		JackrabbitRepository repository			= (JackrabbitRepository)sContext.getAttribute( CrConstants.JACKRABBIT_REPOSITORY );
		if ( repository == null ) {
			logger.warn("No repository found! Only normal if AMP was not used at all !");
		} else
		repository.shutdown();
		logger.info("Jackrabbit repository shutdown succesfully !");
	}
	
	public static boolean privateDocumentsExist(Session jcrWriteSession, TeamMember teamMember){
		boolean retVal=false;
		String userName		= teamMember.getEmail();
		String teamId		= "" + teamMember.getTeamId();
		Node node = DocumentManagerUtil.getNodeByPath(jcrWriteSession, teamMember, "private/"+teamId+"/"+userName);
		try {
			NodeIterator iter = node.getNodes();
			if(iter.hasNext()){
				retVal=true;
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}
	
	
	public static boolean teamDocumentsExist(Session jcrWriteSession, TeamMember teamMember){
		boolean retVal=false;
		String teamId		= "" + teamMember.getTeamId();
		Node node = DocumentManagerUtil.getNodeByPath(jcrWriteSession, null, "team/"+teamId);
		try {
			NodeIterator iter = node.getNodes();
			if(iter.hasNext()){
				retVal=true;
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
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
			retVal=(Integer)qry.uniqueResult()>0?true:false;
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
			retVal=(Integer)qry.uniqueResult()>0?true:false;
		} catch (Exception e) {
			logger.error("Couldn't Load Resourcess: " + e.toString());
		}
		return retVal;
	}
	
}