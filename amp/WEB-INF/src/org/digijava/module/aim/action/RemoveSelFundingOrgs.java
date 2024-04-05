package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveSelFundingOrgs 
extends Action {
    
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        throw new RuntimeException("not implemented");
//      EditActivityForm eaForm = (EditActivityForm) form;
//      
//      Long selFund[] = eaForm.getFunding().getSelFundingOrgs();
//      Collection prevSelFund = eaForm.getFunding().getFundingOrganizations();
//      Collection newFund = new ArrayList();
//
//      Iterator itr = prevSelFund.iterator();
//
//      while (itr.hasNext()) {
//          boolean flag = false;
//          FundingOrganization fo = (FundingOrganization) itr.next();
//          for (int i = 0; i < selFund.length; i++) {
//              if (fo.getAmpOrgId().equals(selFund[i])) {
//                  flag = true;
//                  break;
//              }
//          }
//          if (!flag) {
//              newFund.add(fo);
//          }
//
//      }
//
//      eaForm.getFunding().setFundingOrganizations(newFund);
//      eaForm.setStep("3");
//      eaForm.getFunding().setSelFundingOrgs(null);
//      return mapping.findForward("forward");
    
    }
}

