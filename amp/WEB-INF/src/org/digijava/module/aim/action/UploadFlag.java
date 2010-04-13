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
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.form.FlagUploaderForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class UploadFlag extends Action {
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		FlagUploaderForm fuForm = (FlagUploaderForm) form;
		
		ActionError errorMsg = null;
		FormFile flagFile = fuForm.getFlagFile();

				
		Long siteId = RequestUtils.getSite(request).getId();
		String locale = RequestUtils.getNavigationLanguage(request).getCode();	
		
		try {
			if (flagFile != null && flagFile.getFileSize() > 0) {
				byte[] image = flagFile.getFileData();
				int newfilelength=image.length;
				if(newfilelength>65000){
					 errorMsg = new ActionError("error.aim.uploadFlag.wrongSize", TranslatorWorker.translateText("The file's size should be lesser than 63k", locale, siteId));
				}
				else if (fuForm.getCountryId().longValue() > 0) {
					AmpSiteFlag siteFlag = null;
					siteFlag = FeaturesUtil.getAmpSiteFlag(fuForm.getCountryId());
					if (siteFlag == null) {
						siteFlag = new AmpSiteFlag();
						siteFlag.setFlag(image);
						siteFlag.setContentType(flagFile.getContentType());
						siteFlag.setCountryId(fuForm.getCountryId());
						siteFlag.setDefaultFlag(false);
						DbUtil.add(siteFlag);						
					} else {
						siteFlag.setFlag(image);
						siteFlag.setContentType(flagFile.getContentType());
						DbUtil.update(siteFlag);												
					}
				} else {
					 errorMsg = new ActionError("error.aim.uploadFlag.noCountrySelected", TranslatorWorker.translateText("No country selected", locale, siteId));
				}
			} else {
				 errorMsg = new ActionError("error.aim.uploadFlag.noFlagSelected", TranslatorWorker.translateText("No flag selected to upload", locale, siteId));
			}
		} catch (Exception e) {
		    errorMsg = new ActionError("error.aim.serverError", TranslatorWorker.translateText("Server have encountered an error", locale, siteId));
			e.printStackTrace(System.out);				
		} finally {
			if (errorMsg != null) {
				ActionErrors errors = new ActionErrors();
				errors.add(ActionErrors.GLOBAL_ERROR, errorMsg);
				//set the errors in session due to it is going to be redirected to GetAllFlags.java
				request.getSession().setAttribute("uploadFlagErrors", errors);
			}				
		}		
		
		return mapping.findForward("forward");
	}
	
}