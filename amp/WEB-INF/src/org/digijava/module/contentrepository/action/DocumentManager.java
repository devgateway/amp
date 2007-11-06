/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
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
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class DocumentManager extends Action {
	private static Logger logger	= Logger.getLogger(DocumentManager.class);
	DocumentManagerForm myForm		= null;
	HttpServletRequest myRequest	= null;
	ActionErrors errors				= null;

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		errors		= new ActionErrors();
		
		myForm		= (DocumentManagerForm) form;
		
		myRequest	= request;
		
		myRequest.setAttribute("ServletContext", this.getServlet().getServletContext() );
		
		if (  myForm.getAjaxDocumentList() ) {
			ajaxDocumentList();
			return mapping.findForward("ajaxDocumentList");
		}
		
		if ( !isLoggeedIn(myRequest) ) {
			return mapping.findForward("publicView");
		}

		initializeRepository(request);
		
		this.saveErrors(request, errors);
		
		return mapping.findForward("forward");
	}
	
	private boolean ajaxDocumentList() {
		Session jcrWriteSession			= 	DocumentManagerUtil.getWriteSession(myRequest);
		if ( !isLoggeedIn(myRequest) || myRequest.getParameter(CrConstants.GET_PUBLIC_DOCUMENTS) != null ) {
			HashMap<String, CrDocumentNodeAttributes> uuidMap		= CrDocumentNodeAttributes.getPublicDocumentsMap(true);
			try {
				myForm.setOtherDocuments( this.getDocuments( uuidMap.keySet() ) );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (myForm.getDocListInSession() != null) {
			HashSet<String> UUIDs		= SelectDocumentDM.getSelectedDocsSet(myRequest, myForm.getDocListInSession(), false);
			if (UUIDs != null)
				try {
					myForm.setOtherDocuments( this.getDocuments(UUIDs) );
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
				Node otherHomeNode				= this.getUserPrivateNode(jcrWriteSession , otherTeamMember );
				myForm.setOtherDocuments( this.getDocuments(otherHomeNode) );
			}
		}
		if ( myForm.getOtherUsername() == null && myForm.getOtherTeamId() != null ) {
			TeamMember otherTeamLeader			= TeamMemberUtil.getTMTeamHead( myForm.getOtherTeamId() );
			Node otherHomeNode					= this.getTeamNode(jcrWriteSession, otherTeamLeader);
			myForm.setOtherDocuments( this.getDocuments(otherHomeNode) );
		}
		return false;
	}
	
	private boolean initializeRepository(HttpServletRequest request) {
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
				Node userHomeNode			= this.getUserPrivateNode(jcrWriteSession, teamMember);
				if (myForm.getFileData() != null) {
					if (   this.addFileNode( jcrWriteSession, userHomeNode, myForm.getFileData(), false, false, null )   ) {
						//jcrWriteSession.save();
					}
				}
			}
			if ( myForm.getType() != null && myForm.getType().equals("team") && teamMember.getTeamHead() ) {
				Node teamHomeNode			= this.getTeamNode(jcrWriteSession, teamMember);
				if (myForm.getFileData() != null) {
					if (   this.addFileNode( jcrWriteSession, teamHomeNode, myForm.getFileData(), false, false, null )   ) {
						jcrWriteSession.save();
					}
				}
			}
			if ( myForm.getType() != null && myForm.getType().equals("version") && myForm.getUuid() != null ) {
				Node vNode		= DocumentManagerUtil.getWriteNode(myForm.getUuid(), request);
				if (myForm.getFileData() != null) {
					if (   this.addFileNode( jcrWriteSession, vNode, myForm.getFileData(), true, false, null )   ) {
						//jcrWriteSession.save();
					}
				}
			}
			
			myForm.setMyPersonalDocuments(  this.getPrivateDocuments(teamMember, jcrWriteSession.getRootNode())  );
			myForm.setMyTeamDocuments( this.getTeamDocuments(teamMember, jcrWriteSession.getRootNode()) );
		}catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private Node getTeamNode(Session jcrWriteSession, TeamMember teamMember){
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
	}
	
	private Node getUserPrivateNode(Session jcrWriteSession, TeamMember teamMember){
		Node rootNode		= null;
		Node privateNode	= null;
		Node teamNode		= null;
		Node userNode		= null;
		
		String userName		= teamMember.getEmail();
		String teamId		= "" + teamMember.getTeamId();
		
		return 
				DocumentManagerUtil.getNodeByPath(jcrWriteSession, teamMember, "private/"+teamId+"/"+userName);
		
		/*try {
			rootNode	= jcrWriteSession.getRootNode();
			privateNode	= rootNode.getNode("private");
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("Private node not created. Trying to create now.");
			try {
				privateNode	= rootNode.addNode("private");
			}
			catch(Exception E) {
				logger.error("Cannot create private node");
				e.printStackTrace();
				return null;
			}
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		try {
			teamNode	= privateNode.getNode(teamId);
		} catch (PathNotFoundException e) {
			logger.info("Team node not created. Trying to create now.");
			try{
				teamNode	= privateNode.addNode(teamId);
			}
			catch (Exception E) {
				logger.error("Cannot create team node");
				e.printStackTrace();
				return null;
			}
			
		} catch (RepositoryException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			userNode	= teamNode.getNode(userName);
			return userNode;
		} catch (PathNotFoundException e) {
			logger.info("User node not created. Trying to create now.");
			try{
				userNode	= teamNode.addNode(userName);
				jcrWriteSession.save();
				return userNode;
			}
			catch (Exception E) {
				logger.error("Cannot create user node");
				e.printStackTrace();
				return null;
			}
			
		} catch (RepositoryException e) {
			e.printStackTrace();
			return null;
		}*/
	}
	
	private boolean addFileNode(Session jcrWriteSession, Node parentNode, FormFile formFile, boolean isANewVersion, boolean isLink, String uuid) {
		if (formFile == null) {
			logger.error("No file was transmitted to the server");
			return false;
		}
		int maxFileSizeInBytes		= Integer.MAX_VALUE;
		int maxFileSizeInMBytes	= Integer.MAX_VALUE;
		String maxFileSizeGS		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.CR_MAX_FILE_SIZE); // File size in MB
		if (maxFileSizeGS != null) {
				maxFileSizeInMBytes		= Integer.parseInt( maxFileSizeGS );
				maxFileSizeInBytes		= 1024 * 1024 * maxFileSizeInMBytes; 
		}
		int test	= formFile.getFileSize();
		if ( formFile.getFileSize() > maxFileSizeInBytes) {
			errors.add("title", 
					new ActionError("error.contentrepository.addFile.fileTooLarge", maxFileSizeInMBytes + "")
					);
			return false;
		}
			
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
				newNode.setProperty("ampdoc:link", uuid);
			else
				newNode.setProperty("ampdoc:data", formFile.getInputStream());
			newNode.setProperty("ampdoc:name", formFile.getFileName());
			newNode.setProperty("ampdoc:title", myForm.getDocTitle());
			newNode.setProperty("ampdoc:description", myForm.getDocDescription());
			newNode.setProperty("ampdoc:contentType", formFile.getContentType());
			newNode.setProperty("ampdoc:addingDate", Calendar.getInstance());
			newNode.setProperty("ampdoc:creator", teamMember.getEmail() );
			
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
	}
	private Collection getPrivateDocuments(TeamMember teamMember, Node rootNode) {
		Node userNode;
		try {
			//userNode = rootNode.getNode("private/" + teamMember.getTeamId() +  "/" + teamMember.getEmail());
			userNode	= this.getUserPrivateNode(rootNode.getSession(), teamMember);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(userNode);
	}
	private Collection getTeamDocuments(TeamMember teamMember, Node rootNode) {
		Node teamNode;
		try {
			//teamNode = rootNode.getNode("team/" + teamMember.getTeamId() );
			teamNode	= this.getTeamNode(rootNode.getSession(), teamMember);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return getDocuments(teamNode);
	}
	
	private Collection getDocuments(Node node) {
		try {
			NodeIterator nodeIterator	= node.getNodes();
			return getDocuments(nodeIterator);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
						
				
	}
	
	public Collection getDocuments(Collection<String> UUIDs) throws Exception {
		ArrayList<Node> documents		= new ArrayList<Node>();
		Iterator<String> iter			= UUIDs.iterator();
		while (iter.hasNext()) {
			String uuid			= iter.next();
			Node documentNode	= DocumentManagerUtil.getReadNode(uuid, myRequest);
			if (documentNode == null)
				throw new Exception("Document with uuid '" + uuid + "' not found !");
			
			documents.add(documentNode);
		}
		Iterator iterator			= documents.iterator();
		return 
				getDocuments(iterator);
	}
	
	private Collection getDocuments(Iterator nodeIterator) {
		ArrayList documents										= new ArrayList();
		HashMap<String,CrDocumentNodeAttributes> uuidMapOrg		= CrDocumentNodeAttributes.getPublicDocumentsMap(false);
		HashMap<String,CrDocumentNodeAttributes> uuidMapVer		= CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		try{
			while ( nodeIterator.hasNext() ) {
				Node documentNode		= (Node)nodeIterator.next();
				Property name			= null;
				Property title			= null;
				Property description	= null;
				Property calendar		= null;
				Property contentType	= null;
				
				Boolean hasViewRights			= false;
				Boolean hasVersioningRights		= false;
				Boolean hasDeleteRights			= false;
				Boolean hasMakePublicRights		= false;
				Boolean hasDeleteRightsOnPublicVersion			= false;
				
				String uuid						= documentNode.getUUID();
				boolean isPublicVersion		= uuidMapVer.containsKey(uuid);
				
				if ( isPublicVersion )
						hasViewRights			= true;
				else
						hasViewRights			= DocumentManagerRights.hasViewRights(documentNode, myRequest);
				
				if ( hasViewRights == null || !hasViewRights.booleanValue() ) {
					continue;
				}
				
				try{
					name		= documentNode.getProperty("ampdoc:name");
				}
				catch(PathNotFoundException E) {
					continue;
				}
				try{
					title		= documentNode.getProperty("ampdoc:title");
				}
				catch(PathNotFoundException E) {
					;
				}
				try{
					description	= documentNode.getProperty("ampdoc:description");
				}
				catch(PathNotFoundException E) {
					;
				}
				try{
					calendar	= documentNode.getProperty("ampdoc:addingDate");
				}
				catch(PathNotFoundException E) {
					;
				}
				try{
					contentType	= documentNode.getProperty("ampdoc:contentType");
				}
				catch(PathNotFoundException E) {
					;
				}
				
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
					if (calendar != null) {
						Calendar cal 	=  calendar.getDate() ;
						documentData.setCalendar(DocumentManagerUtil.calendarToString(cal));
					}
					if (contentType != null) {
						documentData.setContentType( contentType.getString() );
					}
					
	//				Boolean hasViewRights			= DocumentManagerRights.hasViewRights(documentNode, myRequest); 
	//				if ( hasViewRights != null ) {
	//					documentData.setHasViewRights( hasViewRights.booleanValue() );
	//				}
					
					if ( !isPublicVersion ) {
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
						
						if ( uuidMapOrg.containsKey(uuid) )
								documentData.setIsPublic(true);
						else
								documentData.setIsPublic(false);
					
						
					}
					else {
						documentData.showVersionHistory			= false; 
					}
					documents.add(documentData);
				}
				
			}
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
	
	public class DocumentData {
		String name;
		String uuid;
		String title;
		String description;
		String calendar;
		String contentType;
		
		
		boolean hasDeleteRights			= false;
		boolean hasViewRights				= false;
		boolean hasVersioningRights		= false;
		boolean hasMakePublicRights		= false;
		boolean hasDeleteRightsOnPublicVersion	= false;
		
		boolean isPublic					= false;
		
		boolean showVersionHistory		= true;
		
		public boolean getHasDeleteRights() {
			return hasDeleteRights;
		}
		public void setHasDeleteRights(boolean hasDeleteRights) {
			this.hasDeleteRights = hasDeleteRights;
		}
		public boolean getHasVersioningRights() {
			return hasVersioningRights;
		}
		public void setHasVersioningRights(boolean hasVersioningRights) {
			this.hasVersioningRights = hasVersioningRights;
		}
		public boolean getHasViewRights() {
			return hasViewRights;
		}
		public void setHasViewRights(boolean hasViewRights) {
			this.hasViewRights = hasViewRights;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public String getCalendar() {
			return calendar;
		}
		public void setCalendar(String calendar) {
			this.calendar = calendar;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		public boolean getHasMakePublicRights() {
			return hasMakePublicRights;
		}
		public void setHasMakePublicRights(boolean hasMakePublicRights) {
			this.hasMakePublicRights = hasMakePublicRights;
		}
		public boolean getIsPublic() {
			return isPublic;
		}
		public void setIsPublic(boolean isPublic) {
			this.isPublic = isPublic;
		}
		public boolean isHasDeleteRightsOnPublicVersion() {
			return hasDeleteRightsOnPublicVersion;
		}
		public void setHasDeleteRightsOnPublicVersion(boolean hasDeleteRightsOnPublicVersion) {
			this.hasDeleteRightsOnPublicVersion = hasDeleteRightsOnPublicVersion;
		}
		public boolean isShowVersionHistory() {
			return showVersionHistory;
		}
		public void setShowVersionHistory(boolean showVersionHistory) {
			this.showVersionHistory = showVersionHistory;
		}
		
		
		
		
	}
}
