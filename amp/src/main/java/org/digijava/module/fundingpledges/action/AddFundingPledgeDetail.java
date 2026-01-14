package org.digijava.module.fundingpledges.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.fundingpledges.form.PledgeForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddFundingPledgeDetail extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        //HttpSession session = request.getSession();
        PledgeForm plForm = (PledgeForm) form;
        throw new RuntimeException("you called me!!!!");
    }
}
