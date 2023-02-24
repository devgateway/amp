package org.digijava.module.aim.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class ImportIntoDynLocationManagerXSL extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        
        DynLocationManagerForm  ioForm = (DynLocationManagerForm)form;
        if (request.getParameter("import") != null) {
            FormFile uploadedFile = ioForm.getFileUploaded();
            byte[] fileData = uploadedFile.getFileData();
            InputStream inputStream = new ByteArrayInputStream(fileData);
            Option option = (ioForm.getOption()==1) ? Option.OVERWRITE : Option.NEW;
            
            ActionMessages errors = new ActionMessages();
            ErrorCode errorCode=DynLocationManagerUtil.importExcelFile(inputStream, option);
            switch (errorCode) {
                case INCORRECT_CONTENT:
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.regionImportWrongContent")); 
                    break;
                case NUMBER_NOT_MATCH:
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.regionImportImpsNumberMisMatch")); 
                    break;
                case NAME_NOT_MATCH: 
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.regionImportImpsNameMisMatch")); 
                    break;
                case CORRECT_CONTENT: 
                    return mapping.findForward("success");
            }
            
            saveErrors(request, errors);
        }

        return mapping.findForward("forward");
    }
}
