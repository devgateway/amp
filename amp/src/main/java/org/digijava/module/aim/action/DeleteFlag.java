package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSiteFlag;
import org.digijava.module.aim.form.FlagUploaderForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;

public class DeleteFlag extends Action {

    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        FlagUploaderForm fuForm = (FlagUploaderForm) form;
        String strCId=request.getParameter("CountryId");
        Long cId=null;
        if(strCId==null){
            return mapping.findForward("forward");
        }else{
            cId=Long.valueOf(strCId);
        }

        String errorMsg = null;

        try {
            if (cId.longValue() > 0) {
                AmpSiteFlag siteFlag = null;
                siteFlag = FeaturesUtil.getAmpSiteFlag(cId);
                if (siteFlag != null) {
//                    siteFlag = new AmpSiteFlag();
//                    byte[] image = FeaturesUtil.getFlag(fuForm.getCountryId());
//
//                    siteFlag.setFlag(image);
//
//                    siteFlag.setCountryId(cId);
//                    siteFlag.setDefaultFlag(false);
                    DbUtil.delete(siteFlag);
                }
            } else {
                errorMsg = "error.aim.uploadFlag.noCountrySelected";
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
