/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.jcr.Node;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.dbentity.CrDocumentsToOrganisations;
import org.digijava.module.contentrepository.form.DocToOrgForm;
import org.digijava.module.contentrepository.util.DocumentOrganizationManager;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class DocToOrgAction extends MultiAction {
    private static Logger logger    = Logger.getLogger(DocToOrgAction.class);
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        DocToOrgForm docToOrgForm   = (DocToOrgForm) form;
        docToOrgForm.setMessages(null);
        if ( docToOrgForm.getAddedOrgs() == null )
            docToOrgForm.setAddedOrgs(new TreeSet<AmpOrganisation>() );
        
        String uuid                 = request.getParameter("orgsforuuid");
        
        if ( uuid!=null ) {
            docToOrgForm.setUuidForOrgsShown(uuid);
        }
        
        return modeSelect(mapping, form, request, response);
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward modeSelect(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DocToOrgForm docToOrgForm   = (DocToOrgForm) form;
        if ( docToOrgForm.getAddedOrgs().size() > 0 && docToOrgForm.getUuidForOrgsShown() != null ) {
            modeAddNewOrgs(mapping, docToOrgForm, request, response);
        }
        if ( docToOrgForm.getRemovingUuid() != null && docToOrgForm.getRemovingOrgId() != null ) {
            modeDelete(mapping, docToOrgForm, request, response);
        }
        if (docToOrgForm.getUuidForOrgsShown() != null) {
            return modeShow(mapping, docToOrgForm, request, response);
        }
        logger.error("modeSelect function shouldn't return null");
        return null;
    }
    
    public void modeAddNewOrgs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        DocToOrgForm docToOrgForm = (DocToOrgForm) form;
        List<AmpOrganisation> existingOrgs = DocumentOrganizationManager.getInstance()
                .getOrganizationsByUUID(docToOrgForm.getUuidForOrgsShown());
        boolean orgAdded =false;
        for (AmpOrganisation org : docToOrgForm.getAddedOrgs()) {
            if (!existingOrgs.contains(org)) {
                CrDocumentsToOrganisations docToOrgObj = new CrDocumentsToOrganisations(
                        docToOrgForm.getUuidForOrgsShown(), org);
                DocumentOrganizationManager.getInstance().saveObject(docToOrgObj);
                orgAdded = true;
            }
        }
        
        docToOrgForm.getAddedOrgs().clear();
        if (orgAdded) {
            if (docToOrgForm.getMessages() == null) {
                docToOrgForm.setMessages(new ArrayList<String>());
            }
            docToOrgForm.getMessages().add(TranslatorWorker.translateText("Organisation(s) added to the document."));
        }
    }
    
    public void modeDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DocToOrgForm docToOrgForm = (DocToOrgForm) form;

        if (isLoggeedIn(request)) {
            Node n = DocumentManagerUtil.getReadNode(docToOrgForm.getRemovingUuid(), request);
            if (n != null && DocumentManagerRights.hasAddParticipatingOrgRights(n, request)) {
                DocumentOrganizationManager.getInstance().deleteDocumentOrganization(docToOrgForm.getRemovingUuid(),
                        docToOrgForm.getRemovingOrgId());
                if (docToOrgForm.getMessages() == null) {
                    docToOrgForm.setMessages(new ArrayList<String>());
                }
                docToOrgForm.getMessages()
                        .add(TranslatorWorker.translateText("Organisation(s) removed from the Document."));
            }
            DocumentManagerUtil.logoutJcrSessions(request);
        }

        docToOrgForm.setRemovingUuid(null);
        docToOrgForm.setRemovingOrgId(null);
    }
    
    public ActionForward modeShow(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String uuid                 = request.getParameter("orgsforuuid");
        DocToOrgForm docToOrgForm   = (DocToOrgForm) form;
        Node n                      = DocumentManagerUtil.getReadNode(uuid, request);
        
        docToOrgForm.setHasAddParticipatingOrgRights(false);
        if(isLoggeedIn(request)){
            if (n != null) {
                docToOrgForm.setHasAddParticipatingOrgRights( DocumentManagerRights.hasAddParticipatingOrgRights(n, request) );
            }
        }
        
        VersionHistory vh   = null;
        try {
            if (n.getParent() instanceof Version ) {
                vh              = ( (Version)(n.getParent()) ).getContainingHistory() ;
                uuid            = vh.getVersionableUUID() ;
            }
        }
        catch (Exception e) {
            logger.info("Error finding root node of version");
        }
        docToOrgForm.setOrgs(new TreeSet<AmpOrganisation>() );
        if (uuid != null) {
            List<CrDocumentsToOrganisations> docsToOrgsList = DocumentOrganizationManager.getInstance()
                    .getDocToOrgObjsByUuid(uuid);
            if ( docsToOrgsList != null ) {
                for ( CrDocumentsToOrganisations dto: docsToOrgsList ) {
                    docToOrgForm.getOrgs().add( dto.getAmpOrganisation() );
                }
            }
        }
        
        DocumentManagerUtil.logoutJcrSessions(request);
        return mapping.findForward("list");
    }

    
    private boolean isLoggeedIn(HttpServletRequest request) {
        if ( getCurrentTeamMember(request) != null) 
            return true;
        return false;
    }
    
    private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
        HttpSession httpSession     = request.getSession();
        TeamMember teamMember       = (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
        return teamMember;
    }
}
