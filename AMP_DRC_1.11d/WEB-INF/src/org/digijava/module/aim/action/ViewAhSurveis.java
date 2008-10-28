package org.digijava.module.aim.action;

import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.form.ViewAhSurveisForm;
import org.digijava.module.aim.util.DbUtil;

public class ViewAhSurveis extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

        ViewAhSurveisForm svform=(ViewAhSurveisForm)form;
        svform.setSurveis(DbUtil.getAllAhSurveyIndicators());
        return mapping.findForward("forward");
    }
}
