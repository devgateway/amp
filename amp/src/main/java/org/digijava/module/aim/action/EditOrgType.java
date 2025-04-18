package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.form.AddOrgTypeForm;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class EditOrgType extends DispatchAction {
    private static Logger logger = Logger.getLogger(EditOrgType.class);

    private boolean sessionChk(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return true;
        }else {
            String str = (String)session.getAttribute("ampAdmin");
            if ("no".equals(str)) {
                return true;
            }
        }
        return false;
    }
          
    private void removeSessAttribute (HttpServletRequest request) {
      if (null != request.getSession().getAttribute("ampOrgType")){
          request.getSession().setAttribute("ampOrgType", null);
      }                 
    }
          
    public ActionForward create(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws java.lang.Exception {
         if (sessionChk(request)){
             return mapping.findForward("index");
         }

        AddOrgTypeForm editForm = (AddOrgTypeForm) form;
        editForm.setDeleteFlag("no");
        if (null == editForm.getOrgType()){
            return mapping.findForward("forward");
        }else{
            int typesAmountWithGivenName=DbUtil.getOrgTypesAmount(editForm.getOrgType(),editForm.getAmpOrgTypeId());
            int typesAmountWithGivenCode=DbUtil.getOrgTypesByCode(editForm.getOrgTypeCode(),editForm.getAmpOrgTypeId());            
            if(typesAmountWithGivenName>0){
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.organizationTypeManager.saveOrgTypeError",TranslatorWorker.translateText("Please choose other name as it is currently in use by some other organization type!")));
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }else if(typesAmountWithGivenCode>0){
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.organizationTypeManager.saveOrgTypeCodeError",TranslatorWorker.translateText("Please choose other Type code as it is currently in use by some other organization type!")));
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }else{
                AmpOrgType otype = new AmpOrgType();
                otype.setOrgType(editForm.getOrgType());
                otype.setOrgTypeCode(editForm.getOrgTypeCode());
                otype.setClassification(editForm.getClassification());
                DbUtil.add(otype);
                removeSessAttribute(request);
                editForm.setReset(Boolean.TRUE);
                ARUtil.clearOrgGroupTypeDimensions();
                return mapping.findForward("added");
            }
        }       
        
    }
          
          
    public ActionForward edit(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws java.lang.Exception {
            if (sessionChk(request)){
                return mapping.findForward("index");
            }                   
                     
            AddOrgTypeForm editForm = (AddOrgTypeForm) form;
            int typesAmountWithGivenName=DbUtil.getOrgTypesAmount(editForm.getOrgType(),editForm.getAmpOrgTypeId());
            if(typesAmountWithGivenName > 0){
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.organizationTypeManager.saveOrgTypeError",TranslatorWorker.translateText("Please choose other name as it is currently in use by some other organization type!")));
                saveErrors(request, errors);
                return mapping.findForward("forward");
            }else{
                try {
                    editForm.setAmpOrgTypeId(new Long(request.getParameter("ampOrgTypeId")));
                }catch (NumberFormatException nex) {
                    logger.debug("Invalid org type-id in request scope");
                    return mapping.findForward("added");
                }
                AmpOrgType otype = DbUtil.getAmpOrgType(editForm.getAmpOrgTypeId());
                if (null == otype) {
                    logger.debug("No Object exists corresponding to ampOrgTypeId in request scope!");
                    return mapping.findForward("added");
                }
                if (null == editForm.getOrgType()) {
                    editForm.setOrgType(otype.getOrgType());
                    editForm.setOrgTypeCode(otype.getOrgTypeCode());
                    editForm.setClassification(otype.getClassification());
                    return mapping.findForward("forward");
                }
                otype.setOrgType(editForm.getOrgType());
                otype.setOrgTypeCode(editForm.getOrgTypeCode());
                otype.setClassification(editForm.getClassification());
                DbUtil.update(otype);
                removeSessAttribute(request);
                editForm.setReset(Boolean.TRUE); 
                ARUtil.clearOrgGroupTypeDimensions();
                return mapping.findForward("added");
            }
            
    }
          
    public ActionForward delete(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws java.lang.Exception {
        if (sessionChk(request)){
            return mapping.findForward("index");
        }            
        
        AddOrgTypeForm editForm = (AddOrgTypeForm) form;
        try {
            editForm.setAmpOrgTypeId(new Long(request.getParameter("ampOrgTypeId"))); 
        }catch (NumberFormatException nex) {
            logger.debug("Invalid org type-id in request scope");
            return mapping.findForward("added");
        }
        if (DbUtil.chkOrgTypeReferneces(editForm.getAmpOrgTypeId())) {
            editForm.setDeleteFlag("orgReferences");
            return mapping.findForward("forward");
        }else {
            AmpOrgType otype = DbUtil.getAmpOrgType(editForm.getAmpOrgTypeId());
            if (null != otype) {
                DbUtil.deleteUserExt(null, otype, null);
                DbUtil.delete(otype);
                ARUtil.clearOrgGroupTypeDimensions();
                removeSessAttribute(request);
            }else
             logger.debug("No Object exists corresponding to ampOrgTypeId in request scope!");
             editForm.setReset(Boolean.TRUE);
             return mapping.findForward("added");
        }
    }
}
