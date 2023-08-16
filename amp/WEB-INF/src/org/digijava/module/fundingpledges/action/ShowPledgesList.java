package org.digijava.module.fundingpledges.action;

import org.apache.struts.action.*;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.fundingpledges.PledgeFormUtils;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.ViewPledgesForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class ShowPledgesList extends Action {
    private final static Logger logger = LoggerFactory.getLogger(ShowPledgesList.class);

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
        logger.info("All pledges: "+pledges);

        Collections.sort(pledges);

        
        for (FundingPledges pledge : pledges) {
            pledge.setYearsList(new TreeSet<>());
            for (FundingPledgesDetails fpd : pledge.getFundingPledgesDetails()) {
                pledge.getYearsList().add(fpd.getDatesDescription());
            }
        }

        plForm.setAllFundingPledges(pledges);

        PledgeFormUtils.pumpFlashAttribute(request, "PNOTIFY_ERROR_MESSAGE");
        PledgeFormUtils.pumpFlashAttribute(request, "PNOTIFY_ERROR_TITLE");
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
