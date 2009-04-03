package org.digijava.module.contentrepository.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import org.hibernate.Query;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.ActivityDocumentsConstants;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.action.DocumentManager;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.action.SetAttributes;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.exception.CrException;
import org.digijava.module.contentrepository.exception.NoNodeInVersionNodeException;
import org.digijava.module.contentrepository.exception.NoVersionsFoundException;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.ObjectReferringDocument;
import org.digijava.module.contentrepository.helper.TeamInformationBeanDM;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;


public class DocumentManagerUtil {
	private static Logger logger	= Logger.getLogger(DocumentManagerUtil.class);
	public static Repository getJCRRepository (HttpServletRequest request) {
		
//		ServletContext context			= (ServletContext)request.getAttribute("ServletContext");
		ServletContext context			= request.getSession().getServletContext();
		if (context == null) {
			logger.error("The request doesn't contain a ServletContext");
			return null;
		}
		Repository repository			= (Repository)context.getAttribute( "JackrabbitRepository" );
		if (repository == null) {
			try{
				String appPath				= DocumentManagerUtil.getApplicationPath();
				String repPath				= appPath + "/jackrabbit";
				repository 					= new TransientRepository(repPath + "/repository.xml", repPath);
				context.setAttribute("JackrabbitRepository", repository);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null; 
			}
		}
		return repository;
	}
	public static Session getReadSession(HttpServletRequest request) {
		HttpSession httpSession	= request.getSession();
		
		Session jcrSession		= (Session)httpSession.getAttribute(CrConstants.JCR_READ_SESSION);
		
		if ( jcrSession!=null && !jcrSession.isLive() ) {
			jcrSession.logout();
			jcrSession			= null;
		}
		
		if (jcrSession == null) {
			try {
				jcrSession	= getJCRRepository(request).login();
				httpSession.setAttribute(CrConstants.JCR_READ_SESSION, jcrSession);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		try {
			jcrSession.getRootNode().refresh(false);
		} catch (InvalidItemStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jcrSession;
	}
	public static Session getWriteSession(HttpServletRequest request) {
		HttpSession httpSession	= request.getSession();
		
		Session jcrSession		= (Session)httpSession.getAttribute(CrConstants.JCR_WRITE_SESSION);
		
		if ( jcrSession!=null && !jcrSession.isLive() ) {
			jcrSession.logout();
			jcrSession			= null;
		}
		
		if (jcrSession == null) {
			try {
				TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
				if (teamMember == null) {
					throw new Exception("No TeamMember found in HttpSession !");
				}
				   /* From add organization the user is always admin and this user has not email*/
				String userName;
				if (teamMember.getEmail() != null) {
				   userName = teamMember.getEmail();
				} else {
				   userName = "admin@amp.org";
				}
				
				SimpleCredentials creden	= new SimpleCredentials(userName, userName.toCharArray());
				
				Repository rep				= getJCRRepository(request); 
				
				jcrSession					= rep.login( creden );
				
				jcrSession.save();
				
				httpSession.setAttribute(CrConstants.JCR_WRITE_SESSION, jcrSession);
				
				registerNamespace(jcrSession, "ampdoc", "http://amp-demo.code.ro/ampdoc");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		try {
			jcrSession.getRootNode().refresh(false);
		} catch (InvalidItemStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jcrSession;
	}
	
	public static NodeWrapper getReadNodeWrapper(String uuid, HttpServletRequest request) {
		Node n 	= getReadNode(uuid, request);
		if ( n!=null ) {
			return new NodeWrapper (n);
		}
		return null;
	}
	
	public static Node getReadNode (String uuid, HttpServletRequest request) {
		Session session	= getReadSession(request);
		try {
			//session.getRootNode().refresh(false);
			//session.refresh(false);
			return session.getNodeByUUID(uuid);
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public static String calendarToString(Calendar cal) {
		String [] monthNames	= {"", "January", "February", "March", "April", "May", "June",
									"July", "August", "September", "October", "November", "December"};
		
		int year		= cal.get(Calendar.YEAR);
		int month		= cal.get(Calendar.MONTH) + 1;
		int day			= cal.get(Calendar.DAY_OF_MONTH);
		
		int hour		= cal.get(Calendar.HOUR_OF_DAY);
		int minute		= cal.get(Calendar.MINUTE);
		int second		= cal.get(Calendar.SECOND);
		return month + "/" + day + "/" + year ;
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
	
	public static int getNextVersionNumber(String uuid, HttpServletRequest request) {
		List versions	= getVersions(uuid, request, false);
		return versions.size() + 1;
	}
	
	public static List getVersions(String uuid, HttpServletRequest request, boolean needWriteSession) {
		if (uuid != null) {
			Node node;
			ArrayList versions		= new ArrayList();
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
				e.printStackTrace();
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
	private static boolean deleteDocument (String uuid, HttpServletRequest request) {
		if (uuid != null) {
			Node node		= DocumentManagerUtil.getWriteNode(uuid, request);
			try {
				Node parent		= node.getParent();
				node.remove();
				parent.save();
				
				SetAttributes.unpublish(uuid);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
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
	
	public static Collection<DocumentData> createDocumentDataCollectionFromSession(HttpServletRequest request) {
		Collection<String> UUIDs		= SelectDocumentDM.getSelectedDocsSet(request, ActivityDocumentsConstants.RELATED_DOCUMENTS, false);
		if ( UUIDs == null )
			return null;
		try {
			DocumentManager dm				= new DocumentManager();
			Collection<DocumentData> ret	= dm.getDocuments(UUIDs, request);
			ret.addAll(
					TemporaryDocumentData.retrieveTemporaryDocDataList(request)
				);
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
//	public static boolean checkFileSize(FormFile formFile, ActionErrors errors) {
//		int maxFileSizeInBytes		= Integer.MAX_VALUE;
//		int maxFileSizeInMBytes		= Integer.MAX_VALUE;
//		String maxFileSizeGS		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE); // File size in MB
//		if (maxFileSizeGS != null) {
//				maxFileSizeInMBytes		= Integer.parseInt( maxFileSizeGS );
//				maxFileSizeInBytes		= 1024 * 1024 * maxFileSizeInMBytes; 
//		}
//		if ( formFile.getFileSize() > maxFileSizeInBytes) {
//			errors.add("title", 
//					new ActionError("error.contentrepository.addFile.fileTooLarge", maxFileSizeInMBytes + "")
//					);
//			return false;
//			}
//		if (formFile.getFileSize()<1){
//			ActionError	error	= new ActionError("error.contentrepository.addFile.badPath");
//			errors.add("title", error);
//			return false;
//		}
//		return true;
//	}

	
	public static boolean checkFileSize(FormFile formFile, ActionErrors errors) {
		int maxFileSizeInBytes		= Integer.MAX_VALUE;
		int maxFileSizeInMBytes		= Integer.MAX_VALUE;
		String maxFileSizeGS		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE); // File size in MB
		if (maxFileSizeGS != null) {
				maxFileSizeInMBytes		= Integer.parseInt( maxFileSizeGS );
				maxFileSizeInBytes		= 1024 * 1024 * maxFileSizeInMBytes; 
		}
		if ( formFile.getFileSize() > maxFileSizeInBytes) {
			errors.add("title",
					new ActionError("error.contentrepository.addFile.fileTooLarge", maxFileSizeInMBytes)
					);
			
			return false;
			}
		if (formFile.getFileSize()<1){
			errors.add("title", 
					new ActionError("error.contentrepository.addFile.badPath")
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
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
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
			formBean.addError("error.contentrepository.addFile.malformedWebLink", "Error adding new document. Web link is malformed.");

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
	public static Node getTeamNode(Session jcrWriteSession, TeamMember teamMember){
		String teamId		= "" + teamMember.getTeamId();
		
		return
				DocumentManagerUtil.getNodeByPath(jcrWriteSession, teamMember, "team/"+teamId);
	}
	public static Node getUserPrivateNode(Session jcrWriteSession, TeamMember teamMember){
		String userName		= teamMember.getEmail();
		String teamId		= "" + teamMember.getTeamId();
		
		return 
				DocumentManagerUtil.getNodeByPath(jcrWriteSession, teamMember, "private/"+teamId+"/"+userName);
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
					//System.out.println("PathHelper:::0)Trying to recover from URI error ! ");
					path					= rootUrl.getPath();
				}
				////System.out.println("PathHelper:::1)The path is: " + path);
				if (path.contains( "classes" )) {
					path	= path + "../";
				}
				////System.out.println("PathHelper:::2)The path is: " + path);
				if (path.contains( "WEB-INF" )) {
					path	= path + "../";
				}
				////System.out.println("PathHelper:::3)The path is: " + path);
				File applicationPathFile	= new File (path);
				applicationPath				= applicationPathFile.getCanonicalPath();
				//System.out.println("PathHelper:::The application path is: " + applicationPath);
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
	
	
	
	public  static ArrayList<DocumentData> getDocuments(Iterator nodeIterator, HttpServletRequest request, Boolean showOnlyDocs, Boolean showOnlyLinks) {
		ArrayList<DocumentData> documents = new ArrayList<DocumentData>();
		HashMap<String, CrDocumentNodeAttributes> uuidMapOrg = CrDocumentNodeAttributes.getPublicDocumentsMap(false);
		HashMap<String, CrDocumentNodeAttributes> uuidMapVer = CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		try {
			while (nodeIterator.hasNext()) {
				Node documentNode = (Node) nodeIterator.next();
				NodeWrapper nodeWrapper = new NodeWrapper(documentNode);

				if (nodeWrapper.getWebLink() != null && showOnlyDocs)
					continue;
				if (nodeWrapper.getWebLink() == null && showOnlyLinks)
					continue;

				Boolean hasViewRights = false;
				Boolean hasShowVersionsRights = false;
				Boolean hasVersioningRights = false;
				Boolean hasDeleteRights = false;
				Boolean hasMakePublicRights = false;
				Boolean hasDeleteRightsOnPublicVersion = false;

				String uuid = documentNode.getUUID();
				boolean isPublicVersion = uuidMapVer.containsKey(uuid);

				if (isPublicVersion) {
					hasViewRights = true;
				} else
					hasViewRights = DocumentManagerRights.hasViewRights(documentNode, request);

				if (hasViewRights == null || !hasViewRights.booleanValue()) {
					continue;
				}

				String fileName = nodeWrapper.getName();
				if (fileName == null && nodeWrapper.getWebLink() == null)
					continue;

				DocumentData documentData = new DocumentData();
				documentData.setName(fileName);
				documentData.setUuid(nodeWrapper.getUuid());
				documentData.setTitle(nodeWrapper.getTitle());
				documentData.setDescription(nodeWrapper.getDescription());
				documentData.setNotes(nodeWrapper.getNotes());
				documentData.setFileSize(nodeWrapper.getFileSizeInMegabytes());
				documentData.setCalendar(nodeWrapper.getDate());
				documentData.setDate(nodeWrapper.getCalendarDate());
				documentData.setVersionNumber(nodeWrapper.getVersionNumber());
				documentData.setContentType(nodeWrapper.getContentType());
				documentData.setWebLink(nodeWrapper.getWebLink());
				documentData.setCmDocTypeId(nodeWrapper.getCmDocTypeId());

				documentData.process(request);
				documentData.computeIconPath(true);

				if (!isPublicVersion) {
					hasShowVersionsRights = DocumentManagerRights.hasShowVersionsRights(documentNode, request);
					if (hasShowVersionsRights != null)
						documentData.setHasShowVersionsRights(hasShowVersionsRights);

					hasVersioningRights = DocumentManagerRights.hasVersioningRights(documentNode, request);
					if (hasVersioningRights != null) {
						documentData.setHasVersioningRights(hasVersioningRights.booleanValue());
					}
					hasDeleteRights = DocumentManagerRights.hasDeleteRights(documentNode, request);
					if (hasDeleteRights != null) {
						documentData.setHasDeleteRights(hasDeleteRights.booleanValue());
					}
					hasMakePublicRights = DocumentManagerRights.hasMakePublicRights(documentNode, request);
					if (hasMakePublicRights != null) {
						documentData.setHasMakePublicRights(hasMakePublicRights.booleanValue());
					}

					hasDeleteRightsOnPublicVersion = DocumentManagerRights.hasDeleteRightsOnPublicVersion(documentNode, request);
					if (hasDeleteRightsOnPublicVersion != null) {
						documentData.setHasDeleteRightsOnPublicVersion(hasDeleteRightsOnPublicVersion.booleanValue());
					}

					if (uuidMapOrg.containsKey(uuid)) {
						documentData.setIsPublic(true);

						//Verify if the last (current) version is the public one.
						Node lastVersion = DocumentManagerUtil.getNodeOfLastVersion(uuid, request);
						String lastVerUUID = lastVersion.getUUID();
						if (uuidMapVer.containsKey(lastVerUUID)) {
							documentData.setLastVersionIsPublic(true);
						}

					} else
						documentData.setIsPublic(false);

				}
				// This is not the actual document node. It is the node of the public version. That's why one shouldn't have 
				// the above rights.
				else {
					documentData.setShowVersionHistory(false);
				}
				documents.add(documentData);
			}

			/*}*/
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return documents;
	}
	
}
