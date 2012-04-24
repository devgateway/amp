/***
 * UpdateDocument.java 
 */

package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.CMSContentItem;
import org.digijava.module.aim.form.RelatedLinksForm;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.DbUtil;

public class UpdateDocument
extends Action {
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		if (session.getAttribute("currentMember") == null)
			return mapping.findForward("index");
		
		RelatedLinksForm rlForm = (RelatedLinksForm) form;
		
		if (!rlForm.isValuesSet()) {
			CMSContentItem cmsItem = DbUtil.getCMSContentItem(rlForm.getDocId());
			AmpActivityVersion activity = ActivityUtil.getProjectChannelOverview(rlForm.getActivityId());
			rlForm.setActivityName(activity.getName());
			rlForm.setTitle(cmsItem.getTitle());
			rlForm.setDocDescription(cmsItem.getDescription());
			rlForm.setFile(cmsItem.getIsFile());
			if (!cmsItem.getIsFile()) {
				rlForm.setUrl(cmsItem.getUrl());
			} else {
				rlForm.setDocFile(null);
				rlForm.setFileName(cmsItem.getFileName());
			}
			rlForm.setValuesSet(true);
			if (rlForm.getPageId() == 0)
				return mapping.findForward("showEditDocument1");
			else 
				return mapping.findForward("showEditDocument2");
		} else {
			CMSContentItem cmsItem = DbUtil.getCMSContentItem(rlForm.getDocId());
			
			cmsItem.setTitle(rlForm.getTitle());
			
			if (rlForm.getDocDescription() == null ||
					rlForm.getDocDescription().trim().length() == 0) {
				cmsItem.setDescription(new String(" "));
			} else {
				cmsItem.setDescription(rlForm.getDocDescription());
			}
			
			if (rlForm.isFile()) {
				cmsItem.setIsFile(true);
				FormFile formFile = rlForm.getDocFile();
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
			} else {
				cmsItem.setIsFile(false);
				cmsItem.setUrl(rlForm.getUrl());				
			}
			
			if (cmsItem.getFile() == null) {
				byte file[] = new byte[1];
				file[0] = 0;
				cmsItem.setFile(file);
			}
			rlForm.setValuesSet(false);
			DbUtil.update(cmsItem);
			rlForm.setReset(true);
			rlForm.setAllDocuments(null);
			if (rlForm.getPageId() == 0) 
				return mapping.findForward("updated1");
			else
				return mapping.findForward("updated2");
		}
	}
}

