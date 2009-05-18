package org.digijava.module.message.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.CalendarEvent;
import org.digijava.module.message.dbentity.UserMessage;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.util.AmpMessageUtil;

public class MsgSettingsAction extends DispatchAction {
	
	public ActionForward getSettings (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String str = (String) session.getAttribute("ampAdmin");

		if (str == null || str.equals("no")) {
			  SiteDomain currentDomain = RequestUtils.getSiteDomain(request);

			  String url = SiteUtils.getSiteURL(currentDomain, request
									.getScheme(), request.getServerPort(), request
									.getContextPath());
			  url += "/aim/index.do";
			  response.sendRedirect(url);
			  return null;
		}

		AmpMessageForm msgForm=(AmpMessageForm)form;
		msgForm=clearForm(msgForm); //clear form 
		AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
		if(settings!=null){
			msgForm.setMsgRefreshTimeCurr(settings.getMsgRefreshTime());
			msgForm.setMsgStoragePerMsgTypeCurr(settings.getMsgStoragePerMsgType());
			msgForm.setDaysForAdvanceAlertsWarningsCurr(settings.getDaysForAdvanceAlertsWarnings());			
			msgForm.setEmailMsgsCurrent(settings.getEmailMsgs());
		}		
		return mapping.findForward("settingsPage");
	}
	
	public ActionForward saveSettings (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AmpMessageForm msgForm=(AmpMessageForm)form;
		boolean runMessagesFilter=false;
		AmpMessageSettings setting;
		if(AmpMessageUtil.getMessageSettings()!=null){
			setting=AmpMessageUtil.getMessageSettings();
		}else {
			setting=new AmpMessageSettings();
		}		
			
		String settingType=request.getParameter("settingType");
		if(settingType!=null){
			
			if(settingType.equals("refreshTime")){
				setting.setMsgRefreshTime(new Long(msgForm.getMsgRefreshTimeNew())); 
			}else if(settingType.equals("storage")){
				setting.setMsgStoragePerMsgType(new Long(msgForm.getMsgStoragePerMsgTypeNew()));
				runMessagesFilter=true;				
			}else if(settingType.equals("warning")){
				setting.setDaysForAdvanceAlertsWarnings(new Long(msgForm.getDaysForAdvanceAlertsWarningsNew()));
			}else if(settingType.equals("emailAlerts")){
				setting.setEmailMsgs(msgForm.getEmailMsgsNew());
			}else if(settingType.equals("saveAll")){//<------user clicked save all button
				if(msgForm.getMsgRefreshTimeNew()!=null && !msgForm.getMsgRefreshTimeNew().equals("")){
					setting.setMsgRefreshTime(new Long(msgForm.getMsgRefreshTimeNew())); 
				}
				if(msgForm.getMsgStoragePerMsgTypeNew()!=null && !msgForm.getMsgStoragePerMsgTypeNew().equals("")){
					setting.setMsgStoragePerMsgType(new Long(msgForm.getMsgStoragePerMsgTypeNew()));
					runMessagesFilter=true;					
				}
				if(msgForm.getDaysForAdvanceAlertsWarningsNew()!=null && !msgForm.getDaysForAdvanceAlertsWarningsNew().equals("")){
					setting.setDaysForAdvanceAlertsWarnings(new Long(msgForm.getDaysForAdvanceAlertsWarningsNew()));
				}
				if(!msgForm.getEmailMsgsNew().equals(new Long(-1))){
					setting.setEmailMsgs(msgForm.getEmailMsgsNew());
				}
			}
		}
		
		boolean successfullySaved=AmpMessageUtil.saveOrUpdateSettings(setting);
		if(successfullySaved && runMessagesFilter){
			filterMessages(setting.getMsgStoragePerMsgType().intValue());
		}		
		return getSettings(mapping, msgForm, request, response);
	}
	
	private void filterMessages(int limit) throws Exception{
		List<AmpMessageState> messagesToBeHidden=new ArrayList<AmpMessageState> ();
		List<AmpMessageState> messagesToBeShown=new ArrayList<AmpMessageState> ();
		List<Long> membersIds=new ArrayList<Long>();
		//get members whos inboxes(all of them) are full
		Class[] allTypesOfMessages=new Class [] {UserMessage.class, AmpAlert.class,Approval.class,CalendarEvent.class};
		for (Class<AmpMessage> clazz : allTypesOfMessages) {
			//INBOX
			membersIds=AmpMessageUtil.getOverflowedMembersIdsForInbox(limit, clazz);
			if(membersIds!=null && ! membersIds.isEmpty() && membersIds.size()>0){
				for (Long id : membersIds) {
					//load hidden messages list for inbox
					messagesToBeHidden.addAll(AmpMessageUtil.getHiddenInboxMsgs(clazz, id,limit)); // returned list won't be empty,because we have list of users which have extra messages in inbox
					//load visible messages list for inbox
					messagesToBeShown.addAll(AmpMessageUtil.getVisibleInboxMsgs(clazz, id,limit));
				}
			}else{//<-- if storage was changed to larger number, then overflowed members may be empty.also some overflowed members may require to become not overflowed. that means hidden messages should be changed to visible
				//anu tu iseti memberebi ar arseboben, romlebsac axal limitze meti mesiji aqvt,mahin unda vnaxot arsebobs tu ara bazashi hidden mesijebi da tu arsebobs, unda gadavaketot visible-ad,radgan storage-i gaizarda.
				messagesToBeShown.addAll(AmpMessageUtil.getAllInboxHiddenMessages(clazz));
			}
			
			//only UserMessage and AmpAlert have sent/draft tabs
			if(clazz.equals(UserMessage.class)||clazz.equals(AmpAlert.class)){
				//SENT
				membersIds=AmpMessageUtil.getOverflowedMembersIdsForSentOrDraft(limit, clazz, false);
				if(membersIds!=null && membersIds.size()>0){
					for (Long id : membersIds) {
						//load hidden messages list for inbox
						messagesToBeHidden.addAll(AmpMessageUtil.getHiddenSentOrDraftMsgs(clazz, id,false,limit)); // returned list won't be empty,because we have list of users which have extra messages in inbox
						//load visible messages list for inbox
						messagesToBeShown.addAll(AmpMessageUtil.getVisibleSentOrDraftMsgs(clazz, id, false,limit));
					}
				}else {
					messagesToBeShown.addAll(AmpMessageUtil.getAllSentOrDrartHiddenMessages(clazz,false));
				}
				
				//DRAFT
				membersIds=AmpMessageUtil.getOverflowedMembersIdsForSentOrDraft(limit, clazz, true);
				if(membersIds!=null && membersIds.size()>0){
					for (Long id : membersIds) {
						//load hidden messages list for inbox
						messagesToBeHidden.addAll(AmpMessageUtil.getHiddenSentOrDraftMsgs(clazz, id,true,limit));
						//load visible messages list for inbox
						messagesToBeShown.addAll(AmpMessageUtil.getVisibleSentOrDraftMsgs(clazz, id, true,limit));
					}
				}else {
					messagesToBeShown.addAll(AmpMessageUtil.getAllSentOrDrartHiddenMessages(clazz,true));
				}
			}					
		}
		
		//now update hidden messages
		if(!messagesToBeHidden.isEmpty()){
			for (AmpMessageState state : messagesToBeHidden) {
				state.setMessageHidden(true);
				AmpMessageUtil.saveOrUpdateMessageState(state);			
			}
		}
		
		//now update visible messages
		if(!messagesToBeShown.isEmpty()){
			for (AmpMessageState state : messagesToBeShown) {
				state.setMessageHidden(false);
				AmpMessageUtil.saveOrUpdateMessageState(state);			
			}
		}		
		 
	}
	
	private AmpMessageForm clearForm(AmpMessageForm form){
		form.setMsgRefreshTimeCurr(null);
		form.setMsgStoragePerMsgTypeCurr(null);
		form.setDaysForAdvanceAlertsWarningsCurr(null);		
		form.setEmailMsgsCurrent(new Long(-1));
		
		form.setMsgRefreshTimeNew(null);
		form.setMsgStoragePerMsgTypeNew(null);
		form.setDaysForAdvanceAlertsWarningsNew(null);		
		form.setEmailMsgsNew(new Long(-1));
		
		return form;
	}
}
