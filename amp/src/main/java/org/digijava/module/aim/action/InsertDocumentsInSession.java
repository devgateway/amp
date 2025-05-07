package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrganisationDocument;
import org.digijava.module.contentrepository.action.SelectDocumentDM;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class InsertDocumentsInSession extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        Long objId              = Long.parseLong( request.getParameter("objId") );
        String sessionName      = request.getParameter("sessionName");
        
        if ( objId!=null && sessionName!=null ) {
            if ( AmpOrganisationDocument.SESSION_NAME.equals(sessionName) ) {
                List<AmpOrganisationDocument>ampOrgDocs = AmpOrganisationDocument.findDocumentsByOrganisation(objId);
                Set<String> UUIDs                       = SelectDocumentDM.getSelectedDocsSet(request, sessionName, true);
                UUIDs.clear();
                Iterator<AmpOrganisationDocument> iter  = ampOrgDocs.iterator();
                while ( iter.hasNext() ) {
                    UUIDs.add( iter.next().getUuid() );
                }
                
            }
        }
        
        return mapping.findForward("forward");
    }
    
}
