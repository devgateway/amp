/**
 * DocumentSelected.java
 * @author Priyajith
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.cms.dbentity.CMSContentItem;

public class DocumentSelected extends Action {

	private static final String LINK_START = "http://";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		if (eaForm.getDocFileOrLink().equals("file")) {
			if (eaForm.getDocTitle() == null || eaForm.getDocTitle().trim().length() == 0 ||
								 eaForm.getDocFile() == null) {
				eaForm.setStep("5");
				return mapping.findForward("forward");
			}
		} else {
			if (eaForm.getDocTitle() == null || eaForm.getDocTitle().trim().length() == 0 ||
								 eaForm.getDocWebResource() == null || eaForm.getDocWebResource().trim().length() == 0) {
				eaForm.setStep("5");
				return mapping.findForward("forward");
			}				  
		}
		
		CMSContentItem cmsItem = new CMSContentItem();
		
		Collection prevDocs = null;
		Collection newDocs = new ArrayList();
		
		cmsItem.setTitle(eaForm.getDocTitle());
		cmsItem.setDescription(eaForm.getDocDescription());
		
		if (eaForm.getDocFileOrLink().equals("file")) {
			cmsItem.setIsFile(true);
			FormFile formFile = eaForm.getDocFile();
			if (formFile != null) {
				if (formFile.getFileSize() != 0) {
					cmsItem.setFileName(formFile.getFileName());
			        cmsItem.setFile(formFile.getFileData());
			        cmsItem.setContentType(formFile.getContentType());
				} else {
			        cmsItem.setFile(null);
			        cmsItem.setFileName(null);
			        cmsItem.setContentType(null);
			    }
			}
			prevDocs = eaForm.getDocumentList();
		} else {
			cmsItem.setIsFile(false);
			String url = eaForm.getDocWebResource();
			if (url.length() > LINK_START.length()) {
				String temp = url.substring(0,LINK_START.length());
				if (!temp.equalsIgnoreCase(LINK_START)) {
					url = LINK_START + url;
				}
			} else {
				url = LINK_START + url; 
			}
			
			cmsItem.setUrl(url);
			prevDocs = eaForm.getLinksList();
		}
		
		// add the cms item to the collection.

		long id = 1;
		if (prevDocs != null) { // there was some documents/links selected earlier
			newDocs.addAll(prevDocs);
			id = prevDocs.size() + 1;
		}
		
		cmsItem.setId(id);
		newDocs.add(cmsItem);
		 

		if (cmsItem.getIsFile()) {
			eaForm.setDocumentList(newDocs);
		} else {
			eaForm.setLinksList(newDocs);
		}
		
		eaForm.setStep("5");

		return mapping.findForward("forward");
	}
}
