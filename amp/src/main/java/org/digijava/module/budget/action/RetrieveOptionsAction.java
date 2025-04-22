package org.digijava.module.budget.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class RetrieveOptionsAction extends Action{
    private static Logger logger = Logger.getLogger(RetrieveOptionsAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws java.lang.Exception
      {
        Logger logger = Logger.getLogger(getClass());
        logger.info( "==========================================================");
        logger.info("Starting in RetrieveOptionsAction");

        String optionstype = request.getParameter("optionstype");
        String outputsrt = null;
       
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String select = TranslatorWorker.translateText("Select");
        
        if (optionstype.equalsIgnoreCase("orgselect")){
             String budgetsectorid = request.getParameter("id");
            if (budgetsectorid!=null && !budgetsectorid.equalsIgnoreCase("")){
                ArrayList<AmpOrganisation> orgs = (ArrayList<AmpOrganisation>) BudgetDbUtil.getOrganizationsBySector(new Long(budgetsectorid));
                for (Iterator iterator = orgs.iterator(); iterator.hasNext();) {
                    AmpOrganisation ampOrganisation = (AmpOrganisation) iterator.next();
                    if (outputsrt==null){
                        outputsrt = "1||"+ "0|"+ select + "||" + ampOrganisation.getAmpOrgId()+ "|" + ampOrganisation.getName();
                    }else{
                        outputsrt = outputsrt + "||" + ampOrganisation.getAmpOrgId()+ "|" + ampOrganisation.getName();
                    }
                }
                out.print(outputsrt);
                out.flush();
            }
        }else if(optionstype.equalsIgnoreCase("depselect")){
            String orgid = request.getParameter("id");
            if (orgid!=null && !orgid.equalsIgnoreCase("")){
                ArrayList<AmpDepartments> departments = (ArrayList<AmpDepartments>) BudgetDbUtil.getDepartmentsbyOrg(new Long(orgid));
                for (Iterator iterator = departments.iterator(); iterator.hasNext();) {
                    AmpDepartments ampdepartments = (AmpDepartments) iterator.next();
                    if (outputsrt==null){
                        outputsrt = "2||" + "0|" + select + "||" + ampdepartments.getId()  + "|" + ampdepartments.getName();
                    }else{
                        outputsrt = outputsrt + "||" + ampdepartments.getId()  + "|" + ampdepartments.getName();
                    }
                }
                out.print(outputsrt);
                out.flush();
            }
        }
        return null;
  }
    
    
}
