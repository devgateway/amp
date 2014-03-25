package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.forms.ItemGateKeeper;
import org.dgfoundation.amp.forms.LockVerificationResult;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.digijava.module.fundingpledges.dbentity.PledgesEntityHelper;
import org.digijava.module.fundingpledges.form.PledgeForm;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AddPledge extends Action {
	
	public final static ItemGateKeeper pledgesGateKeeper = new ItemGateKeeper();
	//public final static String PLEDGE_TIMESTAMP_EDITED_BY_CURRENT_SESSION_ATTR = "pledgeEditedByCurrentUser"; // session[this] = the last time this user has edited a pledge (heartbeat)
	public final static String PLEDGE_ID_EDITED_BY_CURRENT_SESSION_ATTR = "pledgeEditedByCurrentUser"; // session[this] = the last time this user has edited a pledge (heartbeat)
	public final static String PLEDGE_NEW_PLEDGE = "newPledge";
	
	public String editRightCheck(PledgeForm plForm, HttpServletRequest request, HttpServletResponse response){
		TeamMember currentMember = TeamMemberUtil.getLoggedInTeamMember();
		
		// check that logged in
		if (currentMember == null){
			return TranslatorWorker.translateText("Only logged-in members can edit pledges");
		};
		
		// check that nobody else is editing same pledge
		String pledgeIdStr = plForm.getPledgeId() == null ? PLEDGE_NEW_PLEDGE + currentMember.getMemberId() : plForm.getPledgeId().toString();
		LockVerificationResult lvr = pledgesGateKeeper.canCurrentUserEdit(pledgeIdStr);	
		if (!lvr.isActionAllowed()){
			AmpTeamMember owner = TeamMemberUtil.getAmpTeamMember(lvr.currentOwner);
			return TranslatorWorker.translateText("An another user is currently editing this pledge") + ": " + owner.getUser().getName() + "(" + owner.getAmpTeam().getName() + ")";
		}
		
//		// check that current user is not editing a different pledge
//		Long lastTouch = (Long) request.getSession().getAttribute(PLEDGE_TIMESTAMP_EDITED_BY_CURRENT_SESSION_ATTR);
//		if (lastTouch == null)
//			return null; //ok
//		if (lastTouch + pledgesGateKeeper.LOCK_TIMEOUT < System.currentTimeMillis()){
//			// whatever lock was in there, it expired -> allow
//			return null;
//		}
//		// gone till here -> the current user is editing a pledge, let's see whether it is the present one
		String id = (String) request.getSession().getAttribute(PLEDGE_ID_EDITED_BY_CURRENT_SESSION_ATTR);
		if (id == null){
			// first-comer
			return null;			
		}
		if (!id.equals(pledgeIdStr))
			return TranslatorWorker.translateText("You can only edit a single pledge at the same moment of time");
		return null;
	};
	
	void doHeartBeat(PledgeForm plForm, String param){
		TeamMember currentMember = TeamMemberUtil.getLoggedInTeamMember();
		if (currentMember == null)
			return;
		
		String pledgeIdStr = plForm.getPledgeId() == null ? PLEDGE_NEW_PLEDGE + currentMember.getMemberId() : plForm.getPledgeId().toString();
		pledgesGateKeeper.refreshLock(pledgeIdStr, currentMember.getMemberId());
		TLSUtils.getRequest().getSession().setAttribute(PLEDGE_ID_EDITED_BY_CURRENT_SESSION_ATTR, pledgeIdStr);
	};
	
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws java.lang.Exception {
        	
    		request.setAttribute("bootstrap_insert", true); // for the big layout to know to adapt the page for modern-web-standards insets
    		PledgeForm plForm = (PledgeForm) form;
    		
    		if (request.getParameter("heartBeat") != null)
    		{
    			doHeartBeat(plForm, request.getParameter("heartBeat"));
    			return null;
    		}
    		
    		if (request.getParameter("pledgeId") != null){
    			plForm.setPledgeId(Long.parseLong(request.getParameter("pledgeId")));
    		}
    			
//    		String editRightsMsg = editRightCheck(plForm, request, response);
//    		if (editRightsMsg != null)
//    		{
//    			request.setAttribute("PNOTIFY_ERROR_MESSAGE", editRightsMsg);
//    			return mapping.findForward("forward");
//    		}
    		
    		String yearToSpecify = TranslatorWorker.translateText("unspecified");
            
            if (plForm.getYear() == null) {     
               plForm.setYear(yearToSpecify);
            };
            
 	        if (request.getParameter("reset") != null && request.getParameter("reset").equalsIgnoreCase("true")) {
	        	plForm.reset();
	        	request.getSession().removeAttribute("reset");
			} else if ((plForm.getPledgeId() != null) && (plForm.getPledgeId() > 0)){
				FundingPledges fp = PledgesEntityHelper.getPledgesById(plForm.getPledgeId());
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

