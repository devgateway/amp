package org.digijava.module.contentrepository.helper;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFormatException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class NodeWrapper {
	private Node node;
	private boolean errorAppeared	= false;
	
	public NodeWrapper(Node node) {
		this.node	= node;
	}
	
	public NodeWrapper(DocumentManagerForm myForm, HttpServletRequest myRequest, Node parentNode,  
			boolean isANewVersion, ActionErrors errors) {
		
		FormFile formFile		= myForm.getFileData();
		
		boolean isAUrl			= false;
		if ( myForm.getWebLink() != null && myForm.getWebLink().length() > 0 )
			isAUrl				= true;
		
		try {
			TeamMember teamMember		= (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
			Node newNode 	= null;
			if (isANewVersion){
				newNode		= parentNode;
				newNode.checkout();
			}
			else{
				newNode	= parentNode.addNode( myForm.getDocTitle() );
				newNode.addMixin("mix:versionable");
			}
			
			
			if (isANewVersion){
				int vernum	= DocumentManagerUtil.getNextVersionNumber( newNode.getUUID(), myRequest);
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
			}
			else{
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
			}
			
			String contentType			= null;
			if ( isAUrl ){
				String link				= DocumentManagerUtil.processUrl(myForm.getWebLink(), errors);
				if (link != null) {
					newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, link );
					contentType				= CrConstants.URL_CONTENT_TYPE;
				}
				else
					errorAppeared	= true;
			}
			else{
				if ( !DocumentManagerUtil.checkFileSize(formFile, errors) ) {
					errorAppeared	= true;
				}
				else {
					newNode.setProperty(CrConstants.PROPERTY_DATA, formFile.getInputStream());
					
					contentType				= formFile.getContentType();
					int uploadedFileSize	= formFile.getFileSize(); // This is in bytes
					
					newNode.setProperty( CrConstants.PROPERTY_NAME, formFile.getFileName() );
					newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize );
				}
			}
			
			if ( !errorAppeared ) {			
				populateNode(newNode, myForm.getDocTitle(), myForm.getDocDescription(), myForm.getDocNotes(), 
					contentType, myForm.getDocType() , teamMember.getEmail() );
			}
			
			this.node		= newNode;

		} catch(RepositoryException e) {
			ActionError	error	= new ActionError("error.contentrepository.addFile:badPath");
			errors.add("title", error);
			e.printStackTrace();
			errorAppeared	= true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}
		
	}
	
	public NodeWrapper(TemporaryDocumentData tempDoc, HttpServletRequest myRequest, Node parentNode,  
			boolean isANewVersion, ActionErrors errors) {
		
		FormFile formFile		= tempDoc.getFormFile(); 
		
		boolean isAUrl			= false;
		if ( tempDoc.getWebLink()!=null && tempDoc.getWebLink().length()>0 ){
//			if (tempDoc.getWebLink().indexOf("http://") >= 0){
//				tempDoc.setWebLink(tempDoc.getWebLink().replaceFirst("http://", ""));
//			}
			if (tempDoc.getName().indexOf("http://") >= 0){
				tempDoc.setName(tempDoc.getName().replaceFirst("http://", ""));
			}
			if (tempDoc.getTitle().indexOf("http://") >= 0){
				tempDoc.setTitle(tempDoc.getTitle().replaceFirst("http://", ""));
			}
			isAUrl				= true;
		}
		
		try {
			TeamMember teamMember		= (TeamMember)myRequest.getSession().getAttribute(Constants.CURRENT_MEMBER);
			Node newNode 	= null;
			if (isANewVersion){
				newNode		= parentNode;
				newNode.checkout();
			}
			else{
				newNode	= parentNode.addNode(tempDoc.getTitle());
				newNode.addMixin("mix:versionable");
			}
			
			if (isANewVersion){
				int vernum	= DocumentManagerUtil.getNextVersionNumber( newNode.getUUID(), myRequest);
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)vernum);
			}
			else{
				newNode.setProperty(CrConstants.PROPERTY_VERSION_NUMBER, (double)1.0);
			}
			
			String contentType			= null;
			if ( isAUrl ){
				contentType				= CrConstants.URL_CONTENT_TYPE;
				newNode.setProperty ( CrConstants.PROPERTY_WEB_LINK, tempDoc.getWebLink() );
			}
			else{
				if ( !DocumentManagerUtil.checkFileSize(formFile, errors) ) {
					errorAppeared	= true;
				}
				else {
					newNode.setProperty(CrConstants.PROPERTY_DATA, formFile.getInputStream());
					contentType				= formFile.getContentType();
					int uploadedFileSize	= formFile.getFileSize(); // This is in bytes
					
					newNode.setProperty( CrConstants.PROPERTY_NAME, formFile.getFileName() );
					newNode.setProperty( CrConstants.PROPERTY_FILE_SIZE, uploadedFileSize );
				}
			}
			
			if ( !errorAppeared ) {
				populateNode(newNode, tempDoc.getTitle(), tempDoc.getDescription(), tempDoc.getNotes(), 
					contentType, tempDoc.getCmDocTypeId(), teamMember.getEmail() );
			}
			
			this.node		= newNode;

		} catch(RepositoryException e) {
			ActionError	error	= new ActionError("error.contentrepository.addFile.badPath");
			errors.add("title", error);
			e.printStackTrace();
			errorAppeared	= true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}
		
	}
	
	private void populateNode(Node newNode, String doTitle, String docDescr, String docNotes, String contentType, Long cmDocType, String user) {
		try{
			newNode.setProperty( CrConstants.PROPERTY_TITLE, doTitle );
			newNode.setProperty( CrConstants.PROPERTY_DESCRIPTION, docDescr );
			newNode.setProperty( CrConstants.PROPERTY_NOTES, docNotes );
			newNode.setProperty( CrConstants.PROPERTY_CONTENT_TYPE, contentType );
			newNode.setProperty( CrConstants.PROPERTY_CM_DOCUMENT_TYPE, cmDocType );
			newNode.setProperty( CrConstants.PROPERTY_ADDING_DATE, Calendar.getInstance());
			newNode.setProperty( CrConstants.PROPERTY_CREATOR, user );
		}
		catch (Exception e) {
			e.printStackTrace();
			errorAppeared	= true;
		}
	}

	public boolean isErrorAppeared() {
		return errorAppeared;
	}

	public void setErrorAppeared(boolean errorAppeared) {
		this.errorAppeared = errorAppeared;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	public boolean saveNode( Session jcrWriteSession ) {
		try {
			jcrWriteSession.save();
			node.checkin();
			return true;
		}
		catch (Exception E) {
			E.printStackTrace();
			return false;
		}

	}
	
	public String getUuid () {
		try {
			return node.getUUID();
		} catch (UnsupportedRepositoryOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getTitle() {
		Property title		=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_TITLE);
		if ( title != null ) {
			try {
				return title.getString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public String getDescription() {
		Property description	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_DESCRIPTION);
		if ( description != null ) {
			try {
				return description.getString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public String getNotes() {
		Property notes	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_NOTES);
		if ( notes != null ) {
			try {
				return notes.getString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	public String getDate() {
		Property calProperty	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_ADDING_DATE);
		if ( calProperty != null ) {
			try {
				Calendar cal 	= calProperty.getDate();
				return DocumentManagerUtil.calendarToString(cal);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public double getFileSizeInMegabytes() {
		Property fileSize	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_FILE_SIZE);
		if ( fileSize != null ) {
			try {
				double size		= DocumentManagerUtil.bytesToMega( fileSize.getLong() );
				return size;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return 0;
	}
	
	public String getContentType() {
		Property contentType	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CONTENT_TYPE);
		if ( contentType != null ) {
			try {
				return contentType.getString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public float getVersionNumber() {
		Property versionNumber		=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_VERSION_NUMBER);
		if ( versionNumber != null ) {
			try {
				return (float)versionNumber.getDouble();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return 0;
	}
	
	public String getName() {
		Property name	=  DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_NAME);
		if ( name != null ) {
			try {
				return name.getString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return null;
	}
	
	public String getWebLink() {
		Property webLinkProp		= DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_WEB_LINK);
		if ( webLinkProp != null ) {
			try{
				return webLinkProp.getString(); 
			}
			catch ( Exception E ) {
				E.printStackTrace();
			}
		}
		return null;
	}
	
	public Long getCmDocTypeId() {
		Property docType			= DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_CM_DOCUMENT_TYPE);
		if ( docType != null ) {
			try{
				return docType.getLong(); 
			}
			catch ( Exception E ) {
				E.printStackTrace();
			}
		}
		return null;
	}
	
}
