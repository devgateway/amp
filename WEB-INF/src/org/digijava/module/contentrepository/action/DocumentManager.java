/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityDocument;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.RepairDbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.TemporaryDocumentData;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class DocumentManager extends Action {
	private static Logger logger		= Logger.getLogger(DocumentManager.class);
	//public HttpServletRequest myRequest	= null;
	// DocumentManagerForm myForm			= null;
	private boolean showOnlyLinks		= false;
	private boolean showOnlyDocs		= false;

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		//errors		= new ActionErrors();
		ActionErrors errors					= new ActionErrors();
		DocumentManagerForm myForm		= (DocumentManagerForm) form;
		
		// myRequest	= request;
		
		request.setAttribute("ServletContext", this.getServlet().getServletContext() );
		
		if ( request.getParameter(CrConstants.REQUEST_GET_SHOW_DOCS) != null )
			showOnlyDocs 	= true;
		else
			showOnlyDocs	= false;
		if ( request.getParameter(CrConstants.REQUEST_GET_SHOW_LINKS) != null )
			showOnlyLinks 	= true;
		else
			showOnlyLinks	= false;
		
		if (  myForm.getAjaxDocumentList() ) {
			ajaxDocumentList(request, myForm);
			return mapping.findForward("ajaxDocumentList");
		}
		
		if ( !isLoggeedIn(request) ) {
			return mapping.findForward("publicView");
		}

		showContentRepository(request, myForm, errors);
		
		this.saveErrors(request, errors);
		
		return mapping.findForward("forward");
	}
	
	private boolean ajaxDocumentList(HttpServletRequest myRequest, DocumentManagerForm myForm) {
		Session jcrWriteSession			= 	DocumentManagerUtil.getWriteSession(myRequest);
		if ( !isLoggeedIn(myRequest) || myRequest.getParameter(CrConstants.GET_PUBLIC_DOCUMENTS) != null ) {
			HashMap<String, CrDocumentNodeAttributes> uuidMap		= CrDocumentNodeAttributes.getPublicDocumentsMap(true);
			try {
				Collection<DocumentData> otherDocuments = this.getDocuments( uuidMap.keySet(), myRequest );
				myForm.setOtherDocuments( otherDocuments );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (myForm.getDocListInSession() != null) {
			HashSet<String> UUIDs				= SelectDocumentDM.getSelectedDocsSet(myRequest, myForm.getDocListInSession(), true);
			Collection<DocumentData> tempCol	= TemporaryDocumentData.retrieveTemporaryDocDataList(myRequest);
			if (UUIDs != null)
				try {
					Collection<DocumentData> documents = this.getDocuments(UUIDs, myRequest);
					myForm.setOtherDocuments(documents);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				try{	
					if ( tempCol != null ) {
						if ( myForm.getOtherDocuments() == null ) {
							myForm.setOtherDocuments( tempCol );
						}
						else
							myForm.getOtherDocuments().addAll(tempCol);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		if ( myForm.getOtherUsername() != null && myForm.getOtherTeamId() != null ) {
			TeamMember	otherTeamMember		= null;
			Collection otherTeamMembers		= TeamMemberUtil.getTMTeamMembers( myForm.getOtherUsername() );
			
			Iterator iterator				= otherTeamMembers.iterator();
			
			while ( iterator.hasNext() ) {
				TeamMember someTeamMember	= (TeamMember) iterator.next(); 
				if ( someTeamMember.getTeamId().longValue() == myForm.getOtherTeamId().longValue() ) {
					otherTeamMember		= someTeamMember;
					break;
				}
			}
			
			if (otherTeamMember != null) {
				Node otherHomeNode				= DocumentManagerUtil.getUserPrivateNode(jcrWriteSession , otherTeamMember );
				myForm.setOtherDocuments( this.getDocuments(otherHomeNode, myRequest) );
			}
		}
		if ( myForm.getOtherUsername() == null && myForm.getOtherTeamId() != null ) {
			TeamMember otherTeamLeader			= TeamMemberUtil.getTMTeamHead( myForm.getOtherTeamId() );
			Node otherHomeNode					= DocumentManagerUtil.getTeamNode(jcrWriteSession, otherTeamLeader);
			myForm.setOtherDocuments( this.getDocuments(otherHomeNode, myRequest) );
		}
		return false;
	}
	
	private boolean showContentRepository(HttpServletRequest request, DocumentManagerForm myForm, ActionErrors errors) {
		try {
			
			HttpSession	httpSession		= request.getSession();
			TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
			
			myForm.setTeamMember(teamMember);
			myForm.setTeamLeader( teamMember.getTeamHead() );
			myForm.setTeamMembers( TeamMemberUtil.getAllTeamMembers(teamMember.getTeamId()) );
			
			
			if (teamMember == null) {
				throw new Exception("No TeamMember found in HttpSession !");
			}
			Session jcrWriteSession		= DocumentManagerUtil.getWriteSession(request);
			
			
			if ( myForm.getType() != null && myForm.getType().equals("private") ) {
				if (myForm.getFileData() != null || myForm.getWebLink() != null) {
					Node userHomeNode			= DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, teamMember);
					NodeWrapper nodeWrapper		= new NodeWrapper(myForm, request, userHomeNode, false, errors);
					if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() )
							nodeWrapper.saveNode(jcrWriteSession);
				}
			}
			if ( myForm.getType() != null && myForm.getType().equals("team") && teamMember.getTeamHead() ) {
				if (myForm.getFileData() != null || myForm.getWebLink() != null) {
					Node teamHomeNode			= DocumentManagerUtil.getTeamNode(jcrWriteSession, teamMember);
					NodeWrapper nodeWrapper		= new NodeWrapper(myForm, request, teamHomeNode , false, errors);
					if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ) {
						nodeWrapper.saveNode(jcrWriteSession);
					}
				}
			}
			if ( myForm.getType() != null && myForm.getType().equals("version") && myForm.getUuid() != null ) {
				if (myForm.getFileData() != null || myForm.getWebLink() != null) {
					Node vNode		= DocumentManagerUtil.getWriteNode(myForm.getUuid(), request);
					NodeWrapper nodeWrapper		= new NodeWrapper(myForm, request, vNode , true, errors);
					if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ) {
						nodeWrapper.saveNode(jcrWriteSession);
					}
				}
			}
			
			myForm.setMyPersonalDocuments(  this.getPrivateDocuments(teamMember, jcrWriteSession.getRootNode(), request)  );
			myForm.setMyTeamDocuments( this.getTeamDocuments(teamMember, jcrWriteSession.getRootNode(), request) );
		}catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/*private Node getTeamNode(Session jcrWriteSession, TeamMember teamMember){
		Node rootNode		= null;
		Node teamRootNode	= null;
		Node teamNode		= null;
		
		String teamId		= "" + teamMember.getTeamId();
		
		
		try {
			rootNode		= jcrWriteSession.getRootNode();
			teamRootNode	= rootNode.getNode("team");
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("Team root node not created. Trying to create now.");
			try {
				teamRootNode	= rootNode.addNode("team");
			}
			catch(Exception E) {
				logger.error("Cannot create team root node");
				e.printStackTrace();
				return null;
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		try {
			teamNode	= teamRootNode.getNode(teamId);
			return teamNode;
		} catch (PathNotFoundException e) {
			logger.info("Team home node not created. Trying to create now.");
			try{
				teamNode	= teamRootNode.addNode(teamId);
				return teamNode;
			}
			catch (Exception E) {
				logger.error("Cannot create team home node");
				e.printStackTrace();
				return null;
			}
			
		} catch (RepositoryException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	/**
	 * 
	 * @param jcrWriteSession
	 * @param parentNode
	 * @param formFile
	 * @param isANewVersion
	 * @param isLink true if this will be a link to another document from Jackrabbit
	 * @param uuid if isLink==true then the new document will be a link which points to the document with this uuid
	 * @return
	 */
	/*private boolean addFileNode(Session jcrWriteSession, Node parentNode, FormFile formFile, boolean isANewVersion, boolean isLink, String uuid) {
		if (formFile == null) {
			logger.error("No file was transmitted to the server");
			return false;
		}
		if ( !DocumentManagerUtil.checkFileSize(formFile, errors) ) {
			return false;
		}
		
		int uploadedFileSize	= formFile.getFileSize(); // This is in bytes	
		
		try {
			TeamMember teamMember		= (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
			Node newNode 	= null;
			if (isANewVersion){
				newNode		= parentNode;
				newNode.checkout();
			}
			else{
				newNode	= parentNode.addNode(formFile.getFileName());
				newNode.addMixin("mix:versionable");
			}
			if (isLink)
				newNode.setProperty(CrConstants.PROPERTY_LINK, uuid);
			else
				newNode.setProperty(CrConstants.PROPERTY_DATA, formFile.getInputStream());
			
			if (isANewVersion){
				int vernum	= DocumentManagerUtil.getNextVersionNumber( newNode.getUUID(), myRequest);
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
			}
			else{
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
			}
			
			newNode.setProperty( CrConstants.PROPERTY_NAME, formFile.getFileName());
			newNode.setProperty( CrConstants.PROPERTY_TITLE, myForm.getDocTitle());
			newNode.setProperty( CrConstants.PROPERTY_DESCRIPTION, myForm.getDocDescription());
			newNode.setProperty( CrConstants.PROPERTY_NOTES, myForm.getDocNotes());
			newNode.setProperty( CrConstants.PROPERTY_CONTENT_TYPE, formFile.getContentType());
			newNode.setProperty( CrConstants.PROPERTY_ADDING_DATE, Calendar.getInstance());
			newNode.setProperty( CrConstants.PROPERTY_CREATOR, teamMember.getEmail() );
			newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize);
			
			jcrWriteSession.save();
			newNode.checkin();

		} catch(RepositoryException e) {
			ActionError	error	= new ActionError("error.contentrepository.addFile.badPath");
			errors.add("title", error);
			e.printStackTrace();
			return false;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}*/
	private Collection getPrivateDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node userNode;
		try {
			//userNode = rootNode.getNode("private/" + teamMember.getTeamId() +  "/" + teamMember.getEmail());
			userNode	= DocumentManagerUtil.getUserPrivateNode(rootNode.getSession(), teamMember);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(userNode, request);
	}
	private Collection getTeamDocuments(TeamMember teamMember, Node rootNode, HttpServletRequest request) {
		Node teamNode;
		try {
			//teamNode = rootNode.getNode("team/" + teamMember.getTeamId() );
			teamNode	= DocumentManagerUtil.getTeamNode(rootNode.getSession(), teamMember);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(teamNode, request);
	}
	
	private Collection getDocuments(Node node, HttpServletRequest request) {
		try {
			NodeIterator nodeIterator	= node.getNodes();
			return getDocuments(nodeIterator, request);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
						
				
	}
	
	public Collection<DocumentData> getDocuments(Collection<String> UUIDs, HttpServletRequest myRequest) {
		ArrayList<Node> documents		= new ArrayList<Node>();
		Iterator<String> iter			= UUIDs.iterator();
		while (iter.hasNext()) {
			String uuid			= iter.next();
			Node documentNode	= DocumentManagerUtil.getReadNode(uuid, myRequest);
			
			/**
			 * If documentNode is null it means that there is no node with the specified uuid in the repository
			 * but the application still has some information about that node.
			 * It means that there is a problem in the logic of the application so we need to throw an 
			 * exception.
			 */
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
		return 
				getDocuments(iterator, myRequest);
	}
	
	private Collection<DocumentData> getDocuments(Iterator nodeIterator, HttpServletRequest myRequest) {
		ArrayList<DocumentData> documents										= new ArrayList<DocumentData>();
		HashMap<String,CrDocumentNodeAttributes> uuidMapOrg		= CrDocumentNodeAttributes.getPublicDocumentsMap(false);
		HashMap<String,CrDocumentNodeAttributes> uuidMapVer		= CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		try{
			while ( nodeIterator.hasNext() ) {
				Node documentNode		= (Node)nodeIterator.next();
				NodeWrapper nodeWrapper	= new NodeWrapper(documentNode);
				
				if ( nodeWrapper.getWebLink()!=null && showOnlyDocs )
					continue;
				if ( nodeWrapper.getWebLink()==null && showOnlyLinks )
					continue;
					
		
				Boolean hasViewRights			= false;
				Boolean hasShowVersionsRights	= false;
				Boolean hasVersioningRights		= false;
				Boolean hasDeleteRights			= false;
				Boolean hasMakePublicRights		= false;
				Boolean hasDeleteRightsOnPublicVersion			= false;
				
				String uuid						= documentNode.getUUID();
				boolean isPublicVersion		= uuidMapVer.containsKey(uuid);
				
				if ( isPublicVersion ) { // This document is public and exactly this version is the public one
						hasViewRights			= true;
				}
				else
						hasViewRights			= DocumentManagerRights.hasViewRights(documentNode, myRequest);
				
				if ( hasViewRights == null || !hasViewRights.booleanValue() ) {
					continue;
				}
				
				String fileName		=  nodeWrapper.getName();
				if ( fileName == null && nodeWrapper.getWebLink() == null )
						continue;
				
				DocumentData documentData		= new DocumentData();
				documentData.setName( fileName );
				documentData.setUuid( nodeWrapper.getUuid() );
				documentData.setTitle( nodeWrapper.getTitle() );
				documentData.setDescription( nodeWrapper.getDescription() );
				documentData.setNotes( nodeWrapper.getNotes() );
				documentData.setFileSize( nodeWrapper.getFileSizeInMegabytes() );
				documentData.setCalendar( nodeWrapper.getDate() );
				documentData.setVersionNumber( nodeWrapper.getVersionNumber() );
				documentData.setContentType( nodeWrapper.getContentType() );
				documentData.setWebLink( nodeWrapper.getWebLink() );
				documentData.setCmDocTypeId( nodeWrapper.getCmDocTypeId() );
				
				/*name		= DocumentManagerUtil.getPropertyFromNode(documentNode, CrConstants.PROPERTY_NAME);
				if ( name == null )
						continue;
				
				title			= DocumentManagerUtil.getPropertyFromNode(documentNode, CrConstants.PROPERTY_TITLE);
				description		= DocumentManagerUtil.getPropertyFromNode(documentNode, CrConstants.PROPERTY_DESCRIPTION);
				notes			= DocumentManagerUtil.getPropertyFromNode(documentNode, CrConstants.PROPERTY_NOTES);
				calendar		= DocumentManagerUtil.getPropertyFromNode(documentNode, CrConstants.PROPERTY_ADDING_DATE);
				contentType		= DocumentManagerUtil.getPropertyFromNode(documentNode, CrConstants.PROPERTY_CONTENT_TYPE);
				versionNumber	= DocumentManagerUtil.getPropertyFromNode(documentNode, CrConstants.PROPERTY_VERSION_NUMBER);
				fileSize		= DocumentManagerUtil.getPropertyFromNode(documentNode, CrConstants.PROPERTY_FILE_SIZE);
				
				
				if (name != null) {
					DocumentData documentData	= new DocumentData();
					documentData.setUuid(uuid);
					documentData.setName(name.getString());
					
					if (title != null) {
						documentData.setTitle( title.getString() );
					}
					if (description != null) {
						documentData.setDescription( description.getString() );
					}
					if (notes != null) {
						documentData.setNotes( notes.getString() ) ;
					}
					if (calendar != null) {
						Calendar cal 	=  calendar.getDate() ;
						documentData.setCalendar(DocumentManagerUtil.calendarToString(cal));
					}
					if (contentType != null) {
						documentData.setContentType( contentType.getString() );
					}
					if (versionNumber != null) {
						documentData.setVersionNumber( (float)versionNumber.getDouble() );
					}
					else {
						int verNum	= DocumentManagerUtil.getVersions(uuid, myRequest, false).size();
						documentData.setVersionNumber( verNum );
					}
					if (fileSize != null) {
						double size		= DocumentManagerUtil.bytesToMega( fileSize.getLong() );
						documentData.setFileSize( size );
					}
					else {
						documentData.setFileSize( 0 );
					}*/
					documentData.process(myRequest);
					documentData.computeIconPath(true);
					
	//				Boolean hasViewRights			= DocumentManagerRights.hasViewRights(documentNode, myRequest); 
	//				if ( hasViewRights != null ) {
	//					documentData.setHasViewRights( hasViewRights.booleanValue() );
	//				}
					
					if ( !isPublicVersion ) {
						hasShowVersionsRights	= DocumentManagerRights.hasShowVersionsRights(documentNode, myRequest);
						if ( hasShowVersionsRights != null )
							documentData.setHasShowVersionsRights(hasShowVersionsRights);
						
						hasVersioningRights		= DocumentManagerRights.hasVersioningRights(documentNode, myRequest);
						if ( hasVersioningRights != null ) {
							documentData.setHasVersioningRights( hasVersioningRights.booleanValue() );
						}
						hasDeleteRights			= DocumentManagerRights.hasDeleteRights(documentNode, myRequest);
						if ( hasDeleteRights != null ) {
							documentData.setHasDeleteRights( hasDeleteRights.booleanValue() );
						}
						hasMakePublicRights		= DocumentManagerRights.hasMakePublicRights(documentNode, myRequest);
						if ( hasMakePublicRights != null ) {
							documentData.setHasMakePublicRights( hasMakePublicRights.booleanValue() );
						}
						
						hasDeleteRightsOnPublicVersion			= DocumentManagerRights.hasDeleteRightsOnPublicVersion(documentNode, myRequest);
						if ( hasDeleteRightsOnPublicVersion != null ) {
							documentData.setHasDeleteRightsOnPublicVersion( hasDeleteRightsOnPublicVersion.booleanValue() );
						}
						
						if ( uuidMapOrg.containsKey(uuid) ) {
								documentData.setIsPublic(true);
								
								//Verify if the last (current) version is the public one.
								Node lastVersion	= DocumentManagerUtil.getNodeOfLastVersion(uuid, myRequest);
								String lastVerUUID	= lastVersion.getUUID();
								if ( uuidMapVer.containsKey(lastVerUUID) ) {
									documentData.setLastVersionIsPublic( true );
								}
								
						}
						else
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
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return documents;
	}
	
	private boolean isLoggeedIn(HttpServletRequest request) {
		if ( getCurrentTeamMember(request) != null) 
			return true;
		return false;
	}
	
	private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
		HttpSession httpSession		= request.getSession();
		TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
		return teamMember;
	}
		
	
}
