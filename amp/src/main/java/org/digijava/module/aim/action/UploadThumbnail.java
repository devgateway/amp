package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpHomeThumbnail;
import org.digijava.module.aim.form.WelcomePageForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadThumbnail extends Action {
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        WelcomePageForm wpForm = (WelcomePageForm) form;
        
        if (request.getParameter("action")!=null && request.getParameter("action").equals("deleteThumbnail")) {
            FeaturesUtil.deleteThumbnail(wpForm.getPlaceholder());
            return mapping.findForward("forward");
        }
        
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
                    homeThumbnail.setThumbnailLabel(wpForm.getThumbnailLabel());
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
                    homeThumbnail.setThumbnailLabel(wpForm.getThumbnailLabel());
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
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorMsg));
                saveErrors(request, errors);                        
            }               
        }       
        
        return mapping.findForward("forward");
    }
    
}
