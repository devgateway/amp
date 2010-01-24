package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpHomeThumbnail;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.form.FlagUploaderForm;
import org.digijava.module.aim.form.WelcomePageForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class UploadThumbnail extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		WelcomePageForm wpForm = (WelcomePageForm) form;
		
		String errorMsg = null;
		FormFile thumbnail = wpForm.getThumbnail();
		
		try {
			if (thumbnail != null && thumbnail.getFileSize() > 0) {
				AmpHomeThumbnail homeThumbnail = null;
				homeThumbnail = FeaturesUtil.getAmpHomeThumbnail(wpForm.getPlaceholder());
				if (homeThumbnail == null) {
					homeThumbnail = new AmpHomeThumbnail();
					byte[] image = thumbnail.getFileData();
					homeThumbnail.setThumbnail(image);
					homeThumbnail.setThumbnailContentType(thumbnail.getContentType());
					homeThumbnail.setPlaceholder(wpForm.getPlaceholder());
					if (wpForm.getOptionalFile()!=null && wpForm.getOptionalFile().getFileSize()>0){
						homeThumbnail.setOptionalFile(wpForm.getOptionalFile().getFileData());
						homeThumbnail.setOptionalFileName(wpForm.getOptionalFile().getFileName());
						homeThumbnail.setOptionalFileContentType(wpForm.getOptionalFile().getContentType());
					}
					DbUtil.add(homeThumbnail);						
				} else {
					byte[] image = thumbnail.getFileData();
					homeThumbnail.setThumbnail(image);
					homeThumbnail.setThumbnailContentType(thumbnail.getContentType());
					homeThumbnail.setPlaceholder(wpForm.getPlaceholder());
					if (wpForm.getOptionalFile()!=null && wpForm.getOptionalFile().getFileSize()>0){
						homeThumbnail.setOptionalFile(wpForm.getOptionalFile().getFileData());
						homeThumbnail.setOptionalFileName(wpForm.getOptionalFile().getFileName());
						homeThumbnail.setOptionalFileContentType(wpForm.getOptionalFile().getContentType());
					} else {
						homeThumbnail.setOptionalFile(null);
						homeThumbnail.setOptionalFileName(null);
						homeThumbnail.setOptionalFileContentType(null);
					}
					DbUtil.update(homeThumbnail);												
				}
			} else {
				errorMsg = "error.aim.uploadFlag.noFlagSelected";				
			}
		} catch (Exception e) {
			errorMsg = "error.aim.serverError";
			e.printStackTrace(System.out);				
		} finally {
			if (errorMsg != null) {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorMsg));
				saveErrors(request, errors);						
			}				
		}		
		
		return mapping.findForward("forward");
	}
	
}
