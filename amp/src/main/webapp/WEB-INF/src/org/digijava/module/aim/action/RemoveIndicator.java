package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.form.ViewIndicatorsForm;
import org.digijava.module.aim.util.IndicatorUtil;

public class RemoveIndicator extends Action {
     public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {      
         ViewIndicatorsForm allIndForm = (ViewIndicatorsForm) form;
         String id = request.getParameter("indicatorId");
         if (id != null){ 
             if (IndicatorUtil.getAllConnectionsOfIndicator(IndicatorUtil.getIndicator(new Long(id))) == null || IndicatorUtil.getAllConnectionsOfIndicator(IndicatorUtil.getIndicator(new Long(id))).size() == 0){
                 IndicatorUtil.deleteIndicator(new Long(id));
             } else {
                 ActionMessages errors=new ActionMessages();
                 errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.unableToRemoveIndicator", TranslatorWorker.translateText("Indicator is been used by activity or theme")));
                 saveErrors(request, errors);
                 request.getSession().setAttribute("removeIndicatorErrors", errors);
             }
         }
         return mapping.findForward("viewAll");
     }
}
