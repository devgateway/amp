/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.Version;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;


/**
 * @author Alex Gartner
 *
 */
public class GetVersionsForDocumentManager extends Action {
	
	HttpServletRequest myRequest;
	DocumentManagerForm myForm;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {
		
		myForm							= (DocumentManagerForm) form;
		myRequest						= request;
		
		ArrayList<DocumentData> docs	= new ArrayList<DocumentData>();
		myForm.setOtherDocuments(docs);
		
		String nodeUUID		= request.getParameter("uuid"); 
		Collection versions	= DocumentManagerUtil.getVersions(nodeUUID, request, false);
		if (versions != null) {
			int counter	= 0;
			Iterator iter	= versions.iterator();
			while ( iter.hasNext() ) {
				Version v			= (Version)iter.next();
				NodeIterator nIter	= v.getNodes();
				while (nIter.hasNext()) {
					DocumentData docData	= new DocumentData();
					Node n					= nIter.nextNode();
					//String testUUID			= n.getUUID();
					//System.out.println(testUUID);
					
					if ( this.generateDocumentData(n, counter+1, docData) ) {
						docs.add(0, docData );
						counter++;
					}
				}
				
			}
		}
		return mapping.findForward("forward");
	}
	
	private boolean generateDocumentData (Node n, float verNum, DocumentData docData) 
					throws UnsupportedRepositoryOperationException, RepositoryException {
		
		NodeWrapper nodeWrapper		= new NodeWrapper(n);
		
		if ( nodeWrapper.getName() == null && nodeWrapper.getWebLink() == null)
			return false;

		
		docData.setName( nodeWrapper.getName() );
		docData.setWebLink( nodeWrapper.getWebLink() );
		
		docData.setTitle		( nodeWrapper.getTitle() );
		docData.setDescription	( nodeWrapper.getDescription() );
		docData.setNotes		( nodeWrapper.getNotes() );
		docData.setContentType	( nodeWrapper.getContentType() );
		docData.setCmDocTypeId	( nodeWrapper.getCmDocTypeId() );
		docData.setCalendar		( nodeWrapper.getDate() );
		docData.setFileSize		( nodeWrapper.getFileSizeInMegabytes() );
		docData.setVersionNumber( nodeWrapper.getVersionNumber() );
		docData.setUuid			( nodeWrapper.getUuid() );
		if ( docData.getVersionNumber() == 0 )
			docData.setVersionNumber(verNum);
		
		HashMap<String,CrDocumentNodeAttributes> uuidMapVer		= CrDocumentNodeAttributes.getPublicDocumentsMap(true);
		String nodeUUID											= n.getUUID();
		if ( uuidMapVer.containsKey(nodeUUID) ) {
			docData.setIsPublic(true);
		}
		docData.process(myRequest);
		docData.computeIconPath( false );
		return true;
	}
}

