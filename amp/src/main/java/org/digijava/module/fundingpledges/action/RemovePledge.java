package org.digijava.module.fundingpledges.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.util.AuditLoggerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemovePledge extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
            
            if (request.getParameter("pledgeId") != null && Long.valueOf(request.getParameter("pledgeId")) > 0){
                FundingPledges fp = PledgesEntityHelper.getPledgesById(Long.valueOf(request.getParameter("pledgeId")));
                PledgesEntityHelper.removePledge(fp);
                AuditLoggerUtil.logObject(request, fp, "delete", null);
            }
            return mapping.findForward("forward"); 
    }
}
