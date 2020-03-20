package org.digijava.module.fundingpledges.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class AddPledge extends Action {
    
//  public final static ItemGateKeeper pledgesGateKeeper = new ItemGateKeeper();
    //public final static String PLEDGE_TIMESTAMP_EDITED_BY_CURRENT_SESSION_ATTR = "pledgeEditedByCurrentUser"; // session[this] = the last time this user has edited a pledge (heartbeat)
//  public final static String PLEDGE_ID_EDITED_BY_CURRENT_SESSION_ATTR = "pledgeEditedByCurrentUser"; // session[this] = the last time this user has edited a pledge (heartbeat)
//  public final static String PLEDGE_NEW_PLEDGE = "newPledge";
    public final static String PLEDGE_TIMESTAMP_EDITED_BY_CURRENT_SESSION_ATTR = "pledgeEditorTimestamp"; // session[this] = the last time this user has edited a pledge
    //public final static String PLEDGE_ID_EDITED_BY_CURRENT_SESSION_ATTR = "pledgeEditorId"; // session[this] = the id of the pledge currently edited by user
    public final static Long PLEDGE_EDITOR_EXCLUSIVITY_TIMEOUT = 2000l; // nr. of miliseconds 
    
    public boolean equalIds(Long a, Long b){
        if (a == null)
            return b == null;
        return a.equals(b);
    }
    
    public static void markPledgeEditorOpened(HttpSession session){
        session.setAttribute(PLEDGE_TIMESTAMP_EDITED_BY_CURRENT_SESSION_ATTR, System.currentTimeMillis());
    }
    
    public static void markPledgeEditorClosed(HttpSession session){
        session.removeAttribute(PLEDGE_TIMESTAMP_EDITED_BY_CURRENT_SESSION_ATTR);
    }


    public String editRightCheck(PledgeForm plForm, HttpServletRequest request, HttpServletResponse response,
                                 FundingPledges fp) {
        TeamMember currentMember = TeamMemberUtil.getLoggedInTeamMember();
        
        // check that logged in
        if (currentMember == null){
            return TranslatorWorker.translateText("Only logged-in members can edit pledges");
        }

        if (currentMember.getPledger() == null || !currentMember.getPledger() || !((currentMember.getPledgeSuperUser()
                || (fp != null && UserUtils.hasVerfifiedOrgGroup(currentMember.getUserId(), fp
                .getOrganizationGroup().getAmpOrgGrpId())) || (fp == null && !UserUtils.getVerifiedOrgs(currentMember
                .getUserId()).isEmpty())) || !FeaturesUtil
                        .isVisibleFeature("Pledges",
                                        "Limit Pledge Edition"))) {
            return TranslatorWorker.translateText("You are not allowed to edit pledges");
        }
        
        Long timeStamp = (Long) request.getSession().getAttribute(PLEDGE_TIMESTAMP_EDITED_BY_CURRENT_SESSION_ATTR);
        if (timeStamp != null){
            long elapsedMillies = System.currentTimeMillis() - timeStamp;
            //Long editedId = (Long) request.getSession().getAttribute(PLEDGE_ID_EDITED_BY_CURRENT_SESSION_ATTR);
            if (elapsedMillies < PLEDGE_EDITOR_EXCLUSIVITY_TIMEOUT){
                return TranslatorWorker.translateText("You cannot open two Pledge Editors at the same time. Please close the other one");
            }
        }
        
        markPledgeEditorOpened(request.getSession());
        return null;
    };
    
    void doHeartBeat(PledgeForm plForm, String param){
        TeamMember currentMember = TeamMemberUtil.getLoggedInTeamMember();
        if (currentMember == null)
            return;
        markPledgeEditorOpened(TLSUtils.getRequest().getSession());
    };
    
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
            
            //request.setAttribute("bootstrap_insert", true); // for the big layout to know to adapt the page for modern-web-standards insets
            PledgeForm plForm = (PledgeForm) form;
            
            if (request.getParameter("heartBeat") != null)
            {
                doHeartBeat(plForm, request.getParameter("heartBeat"));
                return null;
            }

        if (request.getParameter("pledgeId") != null) {
            plForm.setPledgeId(Long.parseLong(request.getParameter("pledgeId")));
        } else {
            plForm.reset();
        }
        //we need the pledge loaded to see if the user can edit it
        FundingPledges fp = null;

        if (plForm.getPledgeId() != null) {
            fp = PledgesEntityHelper.getPledgesById(plForm.getPledgeId());
        }

            String editRightsMsg = editRightCheck(plForm, request, response, fp);
            if (editRightsMsg != null) {
                request.getSession().setAttribute("PNOTIFY_ERROR_MESSAGE", editRightsMsg);
                request.getSession().setAttribute("PNOTIFY_ERROR_TITLE", TranslatorWorker.translateText("Error"));
                if (plForm.getPledgeId() == null) {
                    response.sendRedirect("/viewPledgesList.do");
                } else {
                    response.sendRedirect("/viewPledge.do?id=" + plForm.getPledgeId());
                }
                return null;
            }
            
            String yearToSpecify = TranslatorWorker.translateText("unspecified");
            
            if (plForm.getYear() == null) {     
               plForm.setYear(yearToSpecify);
            };
            
            if (request.getParameter("reset") != null && request.getParameter("reset").equalsIgnoreCase("true")) {
                plForm.reset();
                request.getSession().removeAttribute("reset");
            } else if ((plForm.getPledgeId() != null) && (plForm.getPledgeId() > 0)){
                plForm.reset();
                plForm.importPledgeData(fp);
                request.getSession().removeAttribute("pledgeId");
            }
            request.getSession().setAttribute("pledgeForm", plForm);
            
            ActionMessages errors = (ActionMessages)request.getSession().getAttribute("duplicatedTitleError");
            if(errors!=null){
                saveErrors(request, errors);
                request.getSession().removeAttribute("duplicatedTitleError");
            }
            
            return mapping.findForward("forward");
            
    }
   
}

