
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditSurveyForm;
public class ParisIndicatorPrintSurvey extends Action 
{
    private static Logger logger = Logger.getLogger(ParisIndicatorPrintSurvey.class);
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception 
    {
         
        EditSurveyForm formBean = (EditSurveyForm) form;
        logger.info(" in the paris print survey!!!!!");
        return mapping.findForward("forward");
    }
}
