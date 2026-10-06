package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import org.digijava.module.aim.form.QuartzJobClassManagerForm;
import org.digijava.module.aim.util.QuartzJobClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QuartzJobClassManager extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

        QuartzJobClassManagerForm jcForm=(QuartzJobClassManagerForm)form;

        if("addJc".equals(jcForm.getAction())){
            jcForm.reset(mapping, request);
            return mapping.findForward("addJobClass");
        }else if("saveJc".equals(jcForm.getAction())){
            AmpQuartzJobClass jc=null;

            jc=QuartzJobClassUtils.getJobClassesByName(jcForm.getName());
            
            if(!classNameIsValid(mapping, jcForm)){
                return mapping.findForward("addJobClass");
            }
            if(jc==null){
                jc=QuartzJobClassUtils.getJobClassesByClassfullName(jcForm.getClassFullname());
                if(jc==null){
                    jc=new AmpQuartzJobClass();
                    jc.setName(jcForm.getName().trim());
                    jc.setClassFullname(jcForm.getClassFullname().trim());
                    QuartzJobClassUtils.addJobClasses(jc);
                }
            }

        }else if("updateJc".equals(jcForm.getAction())){
            AmpQuartzJobClass jc=QuartzJobClassUtils.getJobClassesById(jcForm.getId());
            jc.setName(jcForm.getName().trim());
            jc.setClassFullname(jcForm.getClassFullname().trim());
            if(!classNameIsValid(mapping, jcForm)){
                return mapping.findForward("editJobClass");
            }
            QuartzJobClassUtils.updateJobClasses(jc);

        }else if("editJc".equals(jcForm.getAction())){
            AmpQuartzJobClass jc=QuartzJobClassUtils.getJobClassesById(jcForm.getId());
            if(jc!=null){
                jcForm.setName(jc.getName());
                jcForm.setClassFullname(jc.getClassFullname());
                return mapping.findForward("editJobClass");
            }
        }else if("deleteJc".equals(jcForm.getAction())){
            QuartzJobClassUtils.deleteJobClasses(jcForm.getId());
        }

        jcForm.reset(mapping, request);
        jcForm.setClsCol(QuartzJobClassUtils.getAllJobClasses());

        return mapping.findForward("forward");
    }

    private boolean classNameIsValid(ActionMapping mapping, QuartzJobClassManagerForm jcForm) {
        boolean isValid = true;
        try {
            Class cls = Class.forName(jcForm.getClassFullname(), false, this.getClass().getClassLoader());
            jcForm.setErrorCode(null);
        } catch (java.lang.ClassNotFoundException e) {
            jcForm.setErrorCode("1");
            isValid=false;
        }
        return isValid;
    }

    public QuartzJobClassManager() {
    }
}
