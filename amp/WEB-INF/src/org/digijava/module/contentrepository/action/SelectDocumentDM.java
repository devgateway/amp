package org.digijava.module.contentrepository.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.form.SelectDocumentForm;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.helper.TeamInformationBeanDM;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * 
 * This action is used whenever there is a need to select 1 or more documents from
 * the Content Repository. It is also used to remove a document from a list of 
 * already added documents.
 * 
 * @author Alex Gartner
 *
 */
public class SelectDocumentDM extends Action {
    public static String CONTENT_REPOSITORY_HASH_MAP    = "Content Repository Hash Map";
    
    public static HashMap<String, Object> getContentRepositoryHashMap(HttpServletRequest request) {
        HashMap<String, Object> contentRepositoryHashMap    = (HashMap<String, Object>)request.getSession().getAttribute(CONTENT_REPOSITORY_HASH_MAP);
        
        if (contentRepositoryHashMap == null) {
            contentRepositoryHashMap    = new HashMap<String, Object>();
            request.getSession().setAttribute(CONTENT_REPOSITORY_HASH_MAP, contentRepositoryHashMap);
        }
        
        return contentRepositoryHashMap;
    }
    
    /**
     * 
     * @param request the HttpRequest
     * @param documentsType RELATED_DOCUMENTS / ORGANISATION_DOCUMENTS from ActivityDocumentsConstants
     * @param createIfNull if true and the HashSet containing the selcted documents is null than the HashSet will be created
     * @return HashSet<String> containing the UUIDs 
     */
    public static HashSet<String> getSelectedDocsSet (HttpServletRequest request, String documentsType, boolean createIfNull) {
        HashMap<String,Object> map          = getContentRepositoryHashMap(request);
        HashSet<String> selectedDocsSet     = (HashSet<String>)map.get( documentsType );
        if ( createIfNull && selectedDocsSet == null) {
            selectedDocsSet = new HashSet<String>();
            map.put(documentsType, selectedDocsSet);
        }
        
        return selectedDocsSet;
    }
    
    public static void clearContentRepositoryHashMap(HttpServletRequest request) {
        HashMap<String,Object> map  = getContentRepositoryHashMap(request);
        map.clear();
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception 
    {
    
        DocumentManagerUtil.setMaxFileSizeAttribute(request);
        SelectDocumentForm selectDocumentForm   = (SelectDocumentForm) form;
        
        if (request.getParameter("selectedDocs") != null) {
            HashMap<String, Object> crSessionMap        = getContentRepositoryHashMap(request);
            HashSet<String> selectedDocsSet             = getSelectedDocsSet(request, selectDocumentForm.getDocumentsType(), true);
            
            
            if ( selectDocumentForm.getAction().equals("set") ) {
                for (int i=0; i<selectDocumentForm.getSelectedDocs().length; i++) {
                    selectedDocsSet.add( selectDocumentForm.getSelectedDocs()[i] );
                }
            }
            
            if ( selectDocumentForm.getAction().equals("remove") ){
                for (int i=0; i<selectDocumentForm.getSelectedDocs().length; i++) {
                    Set<String> typeSets            = crSessionMap.keySet();
                    Iterator<String> typeSetsIter   = typeSets.iterator();
                    while ( typeSetsIter.hasNext() ) { // this has at the moment 3 cycles
                        Object value        = crSessionMap.get( typeSetsIter.next() );
                        if (value instanceof HashSet) {
                            HashSet<String> docsSet = (HashSet<String>) value;
                            docsSet.remove( selectDocumentForm.getSelectedDocs()[i] );
                        }
                        else
                            if (value instanceof Collection) {
                                Collection<DocumentData> tempDocs   = (Collection<DocumentData>) value;
                                Iterator<DocumentData> tempIter     = tempDocs.iterator();
                                while (tempIter.hasNext()) {
                                    if ( selectDocumentForm.getSelectedDocs()[i].equals(tempIter.next().getUuid()) )
                                        tempIter.remove();
                                }
                            }
                    } 
                }
            }
            refreshTemporaryUuids(request);
            
            if(request.getParameter("reloadOrgDocs")!=null){    //get organization documents from db or not         
                request.getSession().setAttribute("reloadOrgDocsFromDb", new Boolean(false));
            }
            
            return null; 
        }
        
        this.resetForm(selectDocumentForm);
        
        TeamInformationBeanDM teamInfo          = DocumentManagerUtil.getTeamInformationBeanDM( request.getSession() );
        
        selectDocumentForm.setTeamInformationBeanDM(teamInfo);      
        DocumentManagerUtil.logoutJcrSessions(request);
        return mapping.findForward("forwardDM");
    }
    
    private void resetForm(SelectDocumentForm myForm) {
        myForm.setSelectedDocs(null);
        myForm.setTeamInformationBeanDM(null);
        myForm.setHasAddRights(false);
    }
    
    private void refreshTemporaryUuids(HttpServletRequest request) {
        ArrayList<DocumentData> list = DocumentManagerUtil.retrieveTemporaryDocDataList(request);
        Iterator<DocumentData> iter = list.iterator();
        int i = 0;
        while (iter.hasNext()) {
            iter.next().setUuid(CrConstants.TEMPORARY_UUID + (i++));
        }
    }
}
