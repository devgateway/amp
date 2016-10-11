package org.digijava.module.aim.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.aim.form.DynLocationManagerForm.Option;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil.ErrorCode;
import org.digijava.module.aim.util.DynLocationManagerUtil.ErrorWrapper;


public class ImportLocationIndicatorValuesXLS extends Action {
	private static Logger logger = Logger
			.getLogger(ImportLocationIndicatorValuesXLS.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		DynLocationManagerForm  ioForm = (DynLocationManagerForm)form;
	
		if (request.getParameter("import") != null){
			FormFile uploadedFile = ioForm.getFileUploaded();
			if (logger.isDebugEnabled()) {
				logger.debug("Importing Location indicator values in filename: "+uploadedFile.getFileName());
			}
			byte[] fileData = uploadedFile.getFileData();
			InputStream inputStream = new ByteArrayInputStream(fileData);
			Option option=(ioForm.getOption()==1)?Option.OVERWRITE:Option.NEW;
			ActionMessages errors = new ActionMessages();
			ErrorWrapper errorWrapper = DynLocationManagerUtil.importIndicatorTableExcelFile(inputStream, option);
			ErrorCode errorCode=errorWrapper.getErrorCode();
			
			switch (errorCode) {
			case INCORRECT_CONTENT:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.indImportWrongContent"));
				break;
			case INEXISTANT_ADM_LEVEL:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.indImportInexistantAdmLevel"));
				break;
			case NUMBER_NOT_MATCH:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.indImportNumberMisMatch"));
				break;
			case NAME_NOT_MATCH:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.indImportNameMisMatch"));
				break;
			case LOCATION_NOT_FOUND:
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("feedback.aim.locationNotFound",errorWrapper.getMoreinfo()));
				break;
			case CORRECT_CONTENT:
				return mapping.findForward("success");
			}
			saveErrors(request, errors);
		}

		return mapping.findForward("forward");
	}
}