/**
 * DocumentSelected.java
 * @author Priyajith
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.admin.util.DbUtil;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.cms.dbentity.CMSContentItem;

public class DocumentSelected extends Action {

	private static Logger logger = Logger.getLogger(DocumentSelected.class);
	
	private static final String LINK_START = "http://";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		HttpSession session = request.getSession();
		TeamMember tm = (TeamMember) session.getAttribute("currentMember");

		if (eaForm.getDocFileOrLink().equals("file")) {
			if (eaForm.getDocTitle() == null || eaForm.getDocTitle().trim().length() == 0 ||
								 eaForm.getDocFile() == null) {
				return mapping.findForward("forward");
			}
		} else {
			if (eaForm.getDocTitle() == null || eaForm.getDocTitle().trim().length() == 0 ||
								 eaForm.getDocWebResource() == null || eaForm.getDocWebResource().trim().length() == 0) {
				return mapping.findForward("forward");
			}				  
		}
		
		CMSContentItem cmsItem = new CMSContentItem();
		
		cmsItem.setTitle(eaForm.getDocTitle());
		if (eaForm.getDocDescription() == null ||
				eaForm.getDocDescription().trim().length() == 0) {
			cmsItem.setDescription(" ");
		} else {
			cmsItem.setDescription(eaForm.getDocDescription());	
		}
		
		if (eaForm.getDocFileOrLink().equals("file")) {
			cmsItem.setIsFile(true);
			FormFile formFile = eaForm.getDocFile();
			if (formFile != null) {
				cmsItem.setFileName(formFile.getFileName());
		        cmsItem.setContentType(formFile.getContentType());
				if (formFile.getFileSize() != 0) {
			        cmsItem.setFile(formFile.getFileData());
				} else {
					byte[] temp = {0};
			        cmsItem.setFile(temp);
			    }
			}
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
			byte[] temp = {0};
			cmsItem.setFile(temp);
		}
		
		cmsItem.setId(System.currentTimeMillis());
		RelatedLinks rl = new RelatedLinks();
		rl.setShowInHomePage(eaForm.isShowInHomePage());
		rl.setRelLink(cmsItem);
		rl.setMember(org.digijava.module.aim.util.DbUtil.getAmpTeamMember(tm.getMemberId()));
		
		
		if (cmsItem.getIsFile()) {
			if (eaForm.getDocumentList() == null) {
				eaForm.setDocumentList(new ArrayList());
			}
			eaForm.getDocumentList().add(rl);
		} else {
			if (eaForm.getLinksList() == null) {
				eaForm.setLinksList(new ArrayList());
			}
			eaForm.getLinksList().add(rl);
		}
		return mapping.findForward("forward");
	}
}
