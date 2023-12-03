package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.form.FlagUploaderForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadFlag extends Action {
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        FlagUploaderForm fuForm = (FlagUploaderForm) form;
        
        ActionMessage errorMsg = null;
        FormFile flagFile = fuForm.getFlagFile();
        
        try {
            if(flagFile == null || flagFile.getFileSize() <= 0){
                            errorMsg = new ActionMessage("error.aim.uploadFlag.noFlagSelected");
                            ActionErrors errors = new ActionErrors();
                            errors.add(ActionMessages.GLOBAL_MESSAGE, errorMsg);
                            //set the errors in session due to it is going to be redirected to GetAllFlags.java
                            request.getSession().setAttribute("uploadFlagErrors", errors);
            }else{
                String contentType=flagFile.getContentType();
                if(!contentType.startsWith("image/")) {
                    errorMsg = new ActionMessage("error.aim.uploadFlag.wrongFormat");
                }else if(flagFile.getFileData().length>65000){
                    errorMsg = new ActionMessage("error.aim.uploadFlag.wrongSize");
                }
                if(errorMsg!=null){
                    ActionErrors errors = new ActionErrors();
                    errors.add(ActionMessages.GLOBAL_MESSAGE, errorMsg);
                    //set the errors in session due to it is going to be redirected to GetAllFlags.java
                    request.getSession().setAttribute("uploadFlagErrors", errors);
                    return mapping.findForward("forward");
                }else{
                    byte[] image = flagFile.getFileData();
                    if(fuForm.getCountryId().longValue() > 0){
                        AmpSiteFlag siteFlag = null;
                        siteFlag = FeaturesUtil.getAmpSiteFlag(fuForm.getCountryId());
                        if(siteFlag==null){
                            siteFlag = new AmpSiteFlag();
                            siteFlag.setFlag(image);
                            siteFlag.setContentType(flagFile.getContentType());
                            siteFlag.setCountryId(fuForm.getCountryId());
                            siteFlag.setDefaultFlag(false);
                            DbUtil.add(siteFlag);
                        }else{
                            siteFlag.setFlag(image);
                            siteFlag.setContentType(flagFile.getContentType());
                            DbUtil.update(siteFlag);
                        }
                        
                        siteFlag.setContentType(flagFile.getContentType());
                        siteFlag.setDefaultFlag(false);
                        DbUtil.add(siteFlag);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
             
            return mapping.findForward("forward");
    }
    
}
