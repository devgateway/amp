package org.digijava.module.message.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageReceiver;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.util.AmpMessageUtil;

public class TemplateAlertActions extends DispatchAction {
    
    public ActionForward viewTemplates(ActionMapping mapping,ActionForm form, HttpServletRequest request,   HttpServletResponse response) throws Exception {
         AmpMessageForm messageForm=(AmpMessageForm)form;
         messageForm.setTemplates((List<TemplateAlert>)AmpMessageUtil.getAllMessages(TemplateAlert.class));
         return mapping.findForward("templatesManager"); 
     }
    
     public ActionForward addOrEditTemplate(ActionMapping mapping,ActionForm form, HttpServletRequest request,  HttpServletResponse response) throws Exception {
         AmpMessageForm messagesForm=(AmpMessageForm)form;
         if(request.getParameter("event")!=null && request.getParameter("event").equalsIgnoreCase("add")){
             setDefaultValues(messagesForm);
         }
         
         if(messagesForm.getTeamsMap()!=null){
             messagesForm.setTeamsMap(null); 
         }
         messagesForm.setTeamsMap(loadRecepients());
         
         List<LabelValueBean> availableTriggers= new ArrayList<LabelValueBean>();      
         for (int i=0;i<MessageConstants.availableTriggers.length;i++) {
            LabelValueBean lvb=new LabelValueBean(MessageConstants.triggerName[i],MessageConstants.availableTriggers[i].getName());
            availableTriggers.add(lvb);
        }
         messagesForm.setAvailableTriggersList(availableTriggers);
         
         if(request.getParameter("event")!=null && request.getParameter("event").equalsIgnoreCase("edit")){
             TemplateAlert tempAlert=(TemplateAlert)AmpMessageUtil.getMessage(messagesForm.getTemplateId());
             messagesForm.setTemplateId(tempAlert.getId());
             messagesForm.setMessageName(tempAlert.getName());
             messagesForm.setDescription(tempAlert.getDescription());
             messagesForm.setSelectedTrigger(tempAlert.getRelatedTriggerName());
             //receivers             
             messagesForm.setReceivers(getMessageRecipients(tempAlert));
         }
         
         return mapping.findForward("addOrEditPage"); 
     }
     
     public ActionForward saveTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
             HttpServletResponse response) throws Exception {
        
            AmpMessageForm msgForm = (AmpMessageForm) form;        
            TemplateAlert newTemplate = new TemplateAlert();
            String[] messageReceivers = msgForm.getReceiversIds();
            
            if (msgForm.getTemplateId() != null) {
                // remove all States that were associated to this message
                List<AmpMessageState> msgStates = AmpMessageUtil.loadMessageStates(msgForm.getTemplateId());
                for (AmpMessageState state : msgStates) {
                    AmpMessageUtil.removeMessageState(state);
                }
                //remove message
                AmpMessageUtil.removeMessage(msgForm.getTemplateId());
            }
            
            newTemplate.setName(msgForm.getMessageName());
            newTemplate.setDescription(msgForm.getDescription());
            Calendar cal=Calendar.getInstance();
            newTemplate.setCreationDate(cal.getTime());     
            newTemplate.setRelatedTriggerName(msgForm.getSelectedTrigger());
                            
            if (messageReceivers != null && messageReceivers.length > 0) {
                for (String receiver : messageReceivers) {
                    if (receiver.startsWith("m")) { // <--this means that receiver is a team member
                        Long memId = new Long(receiver.substring(2));
                        AmpTeamMember msgReceiver = TeamMemberUtil.getAmpTeamMember(memId);
                        newTemplate.addMessageReceiver(msgReceiver);
                    }
                }
            }
            
            //saving template
            AmpMessageUtil.saveOrUpdateMessage(newTemplate);        
            //cleaning form values
            setDefaultValues(msgForm);

            return viewTemplates(mapping,msgForm,request,response); 
        
     }
     
     /**
        * user clicked cancel on add Template page   
        */ 
        public ActionForward cancel(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {      
            AmpMessageForm msgForm=(AmpMessageForm)form;
            setDefaultValues(msgForm);
            return viewTemplates(mapping,msgForm,request,response);
        }
     
     public ActionForward deleteTemplate(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
         AmpMessageForm msgForm=(AmpMessageForm)form;
         AmpMessage templateAlert=null;
         List<AmpMessageState> states=new ArrayList<AmpMessageState>();
         if(msgForm.getTemplateId()!=null){
             templateAlert=AmpMessageUtil.getMessage(msgForm.getTemplateId());
             states=AmpMessageUtil.loadMessageStates(templateAlert.getId());
             for (AmpMessageState state : states) {
                 AmpMessageUtil.removeMessageState(state);
            }
             AmpMessageUtil.removeMessage(templateAlert.getId());
         }
         return viewTemplates(mapping,msgForm,request,response);
     }
     
     private Map<String, Team> loadRecepients(){
         Map<String, Team> teamMap=new HashMap<String, Team>();
         List<AmpTeam> teams=(List<AmpTeam>)TeamUtil.getAllTeams();
            if(teams!=null && teams.size()>0){
                for (AmpTeam ampTeam : teams) {
                    if(!teamMap.containsKey("t"+ampTeam.getAmpTeamId())){
                        Team team=new Team();
                        team.setId(ampTeam.getAmpTeamId());
                        team.setName(ampTeam.getName());
                        //getting members of that team
                        List<TeamMember> teamMembers=(List<TeamMember>)TeamMemberUtil.getAllTeamMembers(team.getId());
                        team.setMembers(teamMembers);
                        //putting team to Map
                        teamMap.put("t"+team.getId(), team); //if teamId=2 then the key will be t2. t=team
                    }
                }
            }       
        return teamMap;
     }
     
     /**
      * used to get message recipients, which will be shown on edit Message Page 
      */
    private static List<LabelValueBean> getMessageRecipients(TemplateAlert template) throws Exception {
        Set<AmpMessageReceiver> msgReceivers = template.getMessageReceivers();
        List<LabelValueBean> members = null;
        if (msgReceivers != null && msgReceivers.size() > 0) {
            members = new ArrayList<LabelValueBean>();
            Collection<AmpTeam> teamList = new ArrayList<AmpTeam>();
            Collection<AmpTeamMember> memberList = new ArrayList<AmpTeamMember>();
            
            for (AmpMessageReceiver receiver : msgReceivers) {
                AmpTeamMember teamMember = receiver.getReceiver();
                AmpTeam team = teamMember.getAmpTeam();
                if (!teamList.contains(team)) {
                    teamList.add(team);
                }
                memberList.add(teamMember);
            }

            for (AmpTeam team : teamList) {
                members.add(new LabelValueBean("---" + team.getName() + "---", "t:" + team.getAmpTeamId().toString()));
                for (AmpTeamMember member : memberList) {
                    if (team.getAmpTeamId().longValue() == member.getAmpTeam().getAmpTeamId().longValue()) {
                        LabelValueBean tm = new LabelValueBean(
                                member.getUser().getFirstNames() + " " + member.getUser().getLastName(),
                                "m:" + member.getAmpTeamMemId().toString());
                        
                        members.add(tm);
                    }
                }
            }
        }
        
        return members;
    }
     
     private void setDefaultValues(AmpMessageForm form){
         form.setEditingMessage(false);
         form.setTemplateId(null);
         form.setMessageName(null);
         form.setDescription(null);      
         form.setTeamsMap(null);
         form.setReceiversIds(null);     
         form.setClassName(null);
         form.setMsgStateId(null);
         form.setReceivers(null);
         form.setSelectedTrigger(null);
     }
}
