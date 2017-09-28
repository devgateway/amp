package org.digijava.module.fundingpledges.action;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.ViewPledgesForm;

public class ShowPledgesList extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {

        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (tm == null || tm.getPledger() == null || !tm.getPledger()) {
            handleNonPledgeUser(request);
            return mapping.findForward("forward");
        }
        request.setAttribute("pledgeUser", true);

            
        ViewPledgesForm plForm = (ViewPledgesForm) form;
        
        List<FundingPledges> pledges = PledgesEntityHelper.getPledges();
        Collections.sort(pledges);
        
        for (FundingPledges pledge : pledges) {
            pledge.setYearsList(new TreeSet<String>());
            for (FundingPledgesDetails fpd : pledge.getFundingPledgesDetails()) {
                pledge.getYearsList().add(fpd.getDatesDescription());
            }
        }
        plForm.setAllFundingPledges(pledges);
        return mapping.findForward("forward");
    }

    private void handleNonPledgeUser(HttpServletRequest request) {
        ActionMessages errors = new ActionMessages();
        request.setAttribute("pledgeUser", false);
        ActionMessage message = new ActionMessage("error.aim.pledges.notPledgeUser");
        errors.add(ActionMessages.GLOBAL_MESSAGE, message);
        saveErrors(request, errors);
    }

}
