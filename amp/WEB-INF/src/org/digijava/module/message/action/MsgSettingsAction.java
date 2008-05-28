package org.digijava.module.message.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.util.AmpMessageUtil;

public class MsgSettingsAction extends DispatchAction {
	
	public ActionForward getSettings (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AmpMessageForm msgForm=(AmpMessageForm)form;
		msgForm=clearForm(msgForm); //clear form 
		AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
		if(settings!=null){
			msgForm.setMsgRefreshTimeCurr(settings.getMsgRefreshTime());
			msgForm.setMsgStoragePerMsgTypeCurr(settings.getMsgStoragePerMsgType());
			msgForm.setDaysForAdvanceAlertsWarningsCurr(settings.getDaysForAdvanceAlertsWarnings());
			msgForm.setMaxValidityOfMsgCurr(settings.getMaxValidityOfMsg());
			msgForm.setEmailMsgsCurrent(settings.getEmailMsgs());
		}		
		return mapping.findForward("settingsPage");
	}
	
	public ActionForward saveSettings (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		AmpMessageForm msgForm=(AmpMessageForm)form;
		
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
			}else if(settingType.equals("warning")){
				setting.setDaysForAdvanceAlertsWarnings(new Long(msgForm.getDaysForAdvanceAlertsWarningsNew()));
			}else if(settingType.equals("maxValidity")){
				setting.setMaxValidityOfMsg(new Long(msgForm.getMaxValidityOfMsgNew()));
			}else if(settingType.equals("emailAlerts")){
				setting.setEmailMsgs(msgForm.getEmailMsgsNew());
			}else if(settingType.equals("saveAll")){//<------user clicked save all button
				if(msgForm.getMsgRefreshTimeNew()!=null && !msgForm.getMsgRefreshTimeNew().equals("")){
					setting.setMsgRefreshTime(new Long(msgForm.getMsgRefreshTimeNew())); 
				}
				if(msgForm.getMsgStoragePerMsgTypeNew()!=null && !msgForm.getMsgStoragePerMsgTypeNew().equals("")){
					setting.setMsgStoragePerMsgType(new Long(msgForm.getMsgStoragePerMsgTypeNew()));
				}
				if(msgForm.getDaysForAdvanceAlertsWarningsNew()!=null && !msgForm.getDaysForAdvanceAlertsWarningsNew().equals("")){
					setting.setDaysForAdvanceAlertsWarnings(new Long(msgForm.getDaysForAdvanceAlertsWarningsNew()));
				}
				if(msgForm.getMaxValidityOfMsgNew()!=null && !msgForm.getMaxValidityOfMsgNew().equals("")){
					setting.setMaxValidityOfMsg(new Long(msgForm.getMaxValidityOfMsgNew()));
				}
				if(!msgForm.getEmailMsgsNew().equals(new Long(-1))){
					setting.setEmailMsgs(msgForm.getEmailMsgsNew());
				}
			}
		}
		
		AmpMessageUtil.saveOrUpdateSettings(setting);
		return getSettings(mapping, msgForm, request, response);
	}
	
	private AmpMessageForm clearForm(AmpMessageForm form){
		form.setMsgRefreshTimeCurr(null);
		form.setMsgStoragePerMsgTypeCurr(null);
		form.setDaysForAdvanceAlertsWarningsCurr(null);
		form.setMaxValidityOfMsgCurr(null);
		form.setEmailMsgsCurrent(new Long(-1));
		
		form.setMsgRefreshTimeNew(null);
		form.setMsgStoragePerMsgTypeNew(null);
		form.setDaysForAdvanceAlertsWarningsNew(null);
		form.setMaxValidityOfMsgNew(null);
		form.setEmailMsgsNew(new Long(-1));
		
		return form;
	}
}
