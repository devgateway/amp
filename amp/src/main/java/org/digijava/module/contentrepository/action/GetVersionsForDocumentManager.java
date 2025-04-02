/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.version.Version;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.form.DocumentManagerForm;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.contentrepository.util.DocumentsNodesAttributeManager;


/**
 * @author Alex Gartner
 *
 */
public class GetVersionsForDocumentManager extends Action {
    
//  HttpServletRequest myRequest;
    DocumentManagerForm myForm;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {
        
        myForm                          = (DocumentManagerForm) form;
//      myRequest                       = request;
        
        ArrayList<DocumentData> docs    = new ArrayList<DocumentData>();
        myForm.setOtherDocuments(docs);
        
        String nodeUUID     = request.getParameter("uuid"); 
        Collection versions = DocumentManagerUtil.getVersions(nodeUUID, request, false);
        if (versions != null) {
            int counter = 0;
            Iterator iter   = versions.iterator();
            while ( iter.hasNext() ) {
                Version v           = (Version)iter.next();
                NodeIterator nIter  = v.getNodes();
                while (nIter.hasNext()) {
                    DocumentData docData    = new DocumentData();
                    Node n                  = nIter.nextNode();
                    boolean thisVersionNeedsApproval=false;
                    //if this node is not approved,then only TL or it's creator should be allowed to see the version
                    if (DocumentManagerUtil.isGivenVersionPendingApproval(n.getIdentifier()) != null) {
                        thisVersionNeedsApproval = true;
                    }
                    if(thisVersionNeedsApproval){
                        //thisVersionNeedsApproval=true;
                        TeamMember tm=getCurrentTeamMember(request);
                        if(!DocumentManagerRights.hasRightToSeePendingVersion(request, n)){
                            continue;
                        }
                        docData.setHasApproveVersionRights(DocumentManagerRights.hasApproveVersionRights(request));
                        docData.setBaseNodeUUID(nodeUUID);
                    }
                    if ( this.generateDocumentData(n, counter+1, docData,thisVersionNeedsApproval) ) {
                        docs.add(0, docData );
                        counter++;
                    }
                }
                
            }
        }
        DocumentManagerUtil.logoutJcrSessions(request);
        return mapping.findForward("forward");
    }
    
    private boolean generateDocumentData (Node n, float verNum, DocumentData docData,boolean versionNeedsApproval) 
                    throws UnsupportedRepositoryOperationException, RepositoryException {
        
        NodeWrapper nodeWrapper     = new NodeWrapper(n);
        
        if ( nodeWrapper.getName() == null && nodeWrapper.getWebLink() == null)
            return false;

        
        docData.setName( nodeWrapper.getName() );
        docData.setWebLink( nodeWrapper.getWebLink() );
        
        docData.setTitle        ( nodeWrapper.getTitle() );
        docData.setDescription  ( nodeWrapper.getDescription() );
        docData.setNotes        ( nodeWrapper.getNotes() );
        docData.setContentType  ( nodeWrapper.getContentType() );
        docData.setCmDocTypeId  ( nodeWrapper.getCmDocTypeId() );
        docData.setCalendar     ( nodeWrapper.getDate() );
        docData.setYearofPublication(nodeWrapper.getYearOfPublication());
        docData.setFileSize     ( nodeWrapper.getFileSizeInMegabytes() );
        docData.setVersionNumber( nodeWrapper.getVersionNumber() );
        docData.setUuid         ( nodeWrapper.getUuid() );
        docData.setLabels( nodeWrapper.getLabels() );
        docData.setIndex(nodeWrapper.getIndex());
        docData.setCategory(nodeWrapper.getCategory());
        
        if ( docData.getVersionNumber() == 0 )
            docData.setVersionNumber(verNum);
        
        Map<String, CrDocumentNodeAttributes> uuidMapVer = DocumentsNodesAttributeManager.getInstance()
                .getPublicDocumentsMap(true);
        
        String nodeUUID = n.getIdentifier();
        if ( uuidMapVer.containsKey(nodeUUID) ) {
            docData.setIsPublic(true);
        }
        //if this version is shared or not
        boolean isCurrentVersionShared = DocumentManagerUtil.isGivenVersionShared(n.getIdentifier());
        docData.setIsShared(isCurrentVersionShared);
        
        docData.setCurrentVersionNeedsApproval(versionNeedsApproval);
        
        docData.process();
        docData.computeIconPath( false );
        return true;
    }
    
    private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
        HttpSession httpSession     = request.getSession(); 
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        return teamMember;
    }
}

