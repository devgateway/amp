package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.apache.struts.upload.FormFile;

public class SetCMSFormValues extends Action {

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

					 CMSContentItemForm contItemForm = (CMSContentItemForm) form;

					 FormFile formFile = contItemForm.getFormFile();
					 if (formFile != null) {
								if (formFile.getFileSize() != 0) {
										  contItemForm.setFile(formFile.getFileData());
										  contItemForm.setFileName(formFile.getFileName());
										  contItemForm.setContentType(formFile.getContentType());
								}
								else {
										  if (contItemForm.getFile() == null) {
													 contItemForm.setFile(null);
													 contItemForm.setFileName(null);
													 contItemForm.setContentType(null);
										  }
								}
					 }

					 ActionErrors errors = contItemForm.validate(mapping, request);
					 if (errors != null && errors.size() != 0) {
								saveErrors(request, errors);
								contItemForm.setPreview(false);
					 }

					 contItemForm.setNoReset(true);
					 return mapping.findForward("forward");
		  }
}
