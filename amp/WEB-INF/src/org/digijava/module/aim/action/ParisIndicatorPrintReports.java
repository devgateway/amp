
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ParisIndicatorReportForm;
public class ParisIndicatorPrintReports extends Action 
{
    private static Logger logger = Logger.getLogger(ParisIndicatorPrintReports.class);
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception 
    {
         
        ParisIndicatorReportForm formBean = (ParisIndicatorReportForm) form;
        logger.info(" this is the codenumber.... "+formBean.getIndicatorId()+ " this is the num "+formBean.getIndicatorCode());
        
        logger.info(" in the paris print reports!!!!!");
        if(formBean.getIndicatorCode().equals("6")||formBean.getIndicatorCode().equals("7"))
        {
            logger.info(" here");
            return mapping.findForward("pi2");
        }
        else
        {
            logger.info(" in else");
        return mapping.findForward("pi");
        }
    }
}
