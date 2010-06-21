/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.categorymanager.action.CategoryManager;
import org.digijava.module.contentrepository.dbentity.CrDocumentsToOrganisations;
import org.digijava.module.contentrepository.form.DocToOrgForm;
import org.digijava.module.contentrepository.util.DocToOrgDAO;
import org.digijava.module.contentrepository.util.DocumentManagerRights;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class DocToOrgAction extends MultiAction {
	private static Logger logger	= Logger.getLogger(DocToOrgAction.class);
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DocToOrgForm docToOrgForm	= (DocToOrgForm) form;
		if ( docToOrgForm.getAddedOrgs() == null )
			docToOrgForm.setAddedOrgs(new ArrayList<AmpOrganisation>() );
		String uuid					= request.getParameter("orgsforuuid");
		
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
		DocToOrgForm docToOrgForm	= (DocToOrgForm) form;
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
		
		DocToOrgForm docToOrgForm			= (DocToOrgForm) form;
		List<AmpOrganisation> existingOrgs	= DocToOrgDAO.getOrgsObjByUuid( docToOrgForm.getUuidForOrgsShown() );
		
		for (AmpOrganisation org: docToOrgForm.getAddedOrgs() ) {
			if ( !existingOrgs.contains(org) ) {
				CrDocumentsToOrganisations docToOrgObj	= new CrDocumentsToOrganisations(docToOrgForm.getUuidForOrgsShown(), org);
				DocToOrgDAO.saveObject(docToOrgObj);
			}
		}
		
		docToOrgForm.getAddedOrgs().clear();
		
	}
	
	public void modeDelete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DocToOrgForm docToOrgForm			= (DocToOrgForm) form;
		
		DocToOrgDAO.deleteDocToOrgObjs(docToOrgForm.getRemovingUuid(), docToOrgForm.getRemovingOrgId() );
		
		docToOrgForm.setRemovingUuid(null);
		docToOrgForm.setRemovingOrgId(null);
	}
	
	public ActionForward modeShow(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String uuid					= request.getParameter("orgsforuuid");
		DocToOrgForm docToOrgForm	= (DocToOrgForm) form;
		
		docToOrgForm.setHasAddParticipatingOrgRights(false);
		Node n		= DocumentManagerUtil.getReadNode(uuid, request);
		if (n != null) {
			docToOrgForm.setHasAddParticipatingOrgRights( DocumentManagerRights.hasAddParticipatingOrgRights(n, request) );
		}
		
		docToOrgForm.setOrgs(new ArrayList<AmpOrganisation>() );
		if (uuid != null) {
			List<CrDocumentsToOrganisations> docsToOrgsList	= DocToOrgDAO.getDocToOrgObjsByUuid(uuid);
			if ( docsToOrgsList != null ) {
				for ( CrDocumentsToOrganisations dto: docsToOrgsList ) {
					docToOrgForm.getOrgs().add( dto.getAmpOrganisation() );
				}
			}
		}
		
		return mapping.findForward("list");
	}

	
	
}
