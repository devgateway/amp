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
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.form.FlagUploaderForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class UploadFlag extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		FlagUploaderForm fuForm = (FlagUploaderForm) form;
		
		String errorMsg = null;
		FormFile flagFile = fuForm.getFlagFile();
		
		try {
			if (flagFile != null && flagFile.getFileSize() > 0) {
				if (fuForm.getCountryId().longValue() > 0) {
					AmpSiteFlag siteFlag = null;
					siteFlag = FeaturesUtil.getAmpSiteFlag(fuForm.getCountryId());
					if (siteFlag == null) {
						siteFlag = new AmpSiteFlag();
						byte[] image = flagFile.getFileData();
						siteFlag.setFlag(image);
						siteFlag.setContentType(flagFile.getContentType());
						siteFlag.setCountryId(fuForm.getCountryId());
						siteFlag.setDefaultFlag(false);
						DbUtil.add(siteFlag);						
					} else {
						byte[] image = flagFile.getFileData();
						siteFlag.setFlag(image);
						siteFlag.setContentType(flagFile.getContentType());
						DbUtil.update(siteFlag);												
					}
				} else {
					errorMsg = "error.aim.uploadFlag.noCountrySelected";	
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