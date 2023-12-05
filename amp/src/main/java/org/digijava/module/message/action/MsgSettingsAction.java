package org.digijava.module.message.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.message.dbentity.*;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.util.AmpMessageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class MsgSettingsAction extends DispatchAction {
    
    public ActionForward getSettings (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
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
                    //update hidden messages to visible if necessary
                    AmpMessageUtil.updateHiddenInboxMsgsToVisible(clazz, id, limit);
                    //hide visible messages if necessary
                    AmpMessageUtil.updateVisibleInboxMsgsToHidden(clazz, id, limit);
                }
            }else{//<-- if storage was changed to larger number, then overflowed members may be empty.also some overflowed members may require to become not overflowed. that means hidden messages should be changed to visible                
                AmpMessageUtil.updateAllHiddenInboxMessagesToVisible(clazz);
            }
            
            //only UserMessage and AmpAlert have sent/draft tabs
            if(clazz.equals(UserMessage.class)||clazz.equals(AmpAlert.class)){
                //SENT
                membersIds=AmpMessageUtil.getOverflowedMembersIdsForSentOrDraft(limit, clazz, false);
                if(membersIds!=null && membersIds.size()>0){
                    for (Long id : membersIds) {
                        //update hidden messages to visible if necessary
                        AmpMessageUtil.updateHiddenSentOrDraftMsgsToVisible(clazz, id, false, limit);
                        //hide visible messages if necessary
                        AmpMessageUtil.updateVisibleSentOrDraftMsgsToHidden(clazz, id, false, limit);
                    }
                }else {
                    AmpMessageUtil.updateAllSentOrDrartHiddenMsgsToVisible(clazz, false);
                }
                
                //DRAFT
                membersIds=AmpMessageUtil.getOverflowedMembersIdsForSentOrDraft(limit, clazz, true);
                if(membersIds!=null && membersIds.size()>0){
                    for (Long id : membersIds) {
                        //update hidden messages to visible if necessary
                        AmpMessageUtil.updateHiddenSentOrDraftMsgsToVisible(clazz, id, true, limit);
                        //hide visible messages if necessary
                        AmpMessageUtil.updateVisibleSentOrDraftMsgsToHidden(clazz, id, true, limit);

                    }
                }else {
                    AmpMessageUtil.updateAllSentOrDrartHiddenMsgsToVisible(clazz, true);
                }
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
