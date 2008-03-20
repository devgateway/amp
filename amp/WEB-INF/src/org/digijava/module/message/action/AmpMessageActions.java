package org.digijava.module.message.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.util.AmpMessageUtil;
import org.digijava.module.um.util.AmpUserUtil;

public class AmpMessageActions extends DispatchAction {
private static Logger logger = Logger.getLogger(AmpMessageActions.class);
    
    public ActionForward fillTypesAndLevels (ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {        		
    		AmpMessageForm messageForm=(AmpMessageForm)form;    		
    		if(request.getParameter("editingMessage").equals("false")){
    			setDefaultValues(messageForm);
    		}else {
    			Long id=new Long(request.getParameter("messageId"));
    	    	AmpMessage message=AmpMessageUtil.getMessage(id);
    	    	fillFormFields(message,messageForm);    	
    		}
		 return loadReceiversList(mapping,form,request,response);	
	}
    
   /**
    * user clicked cancel on view Messages page
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */ 
    public ActionForward cancelMessage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {    	
    	AmpMessageForm alertsForm=(AmpMessageForm)form;
    	setDefaultValues(alertsForm);
    	return mapping.findForward("viewMyDesktop");
    }
    
    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return All Messages that belong to this user as a Team Member or as a User(If one selected USER or ALL when creating message)
     * @throws Exception
     */
    public ActionForward viewAllMessages(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current team member 
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
    	//get from helper teamMember class AmpTeamMember, to see this User's Id 
    	AmpTeamMember ampTeamMember=TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId());
    	Long userId=ampTeamMember.getUser().getId();
		AmpMessageForm messageForm=(AmpMessageForm)form;		
		messageForm.setAllMessages(AmpMessageUtil.getAllMessagesForCurrentUser(teamMember.getTeamId(),userId,teamMember.getMemberId()));
    	return mapping.findForward("showAll");
    }
    
    /**
     * fills Form to view selected Message
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward viewSelectedMessage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
    	Long id=new Long(request.getParameter("messageId"));
    	AmpMessage message=AmpMessageUtil.getMessage(id);
    	//If user clicked on a message, then it has to be marked as  read
    	message.setRead(true);
    	AmpMessageUtil.saveOrUpdateMessage(message);
    	fillFormFields(message,messagesForm);    	
    	return mapping.findForward("viewMessage");
    }
    /**@author Dare Roinishvili
     * Removes selected Message
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward removeSelectedMessage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
    	AmpMessageUtil.removeMessage(messagesForm.getMessageId()); 
    	return mapping.findForward("viewMyDesktop");
    }    
 
    
    /**
     * loads List of Receivers according to the receiverType(all,Users,Team,TM)
     * @author Dare Roinishvili
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward loadReceiversList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
    	LabelValueBean lvbean=null;
    	messagesForm.setReceivers(null);
    	//Team
    	if(messagesForm.getReceiverType()!=null && messagesForm.getReceiverType().equalsIgnoreCase("TEAM")){
    		List<AmpTeam> teams=(List<AmpTeam>)TeamUtil.getAllTeams();
    		if(teams!=null && teams.size()>0){
    			Iterator it=teams.iterator();
    			while (it.hasNext()) {
    				AmpTeam team=(AmpTeam)it.next();
    				lvbean=new LabelValueBean(team.getName(),team.getAmpTeamId().toString());
    				if(messagesForm.getReceivers()==null){
    					messagesForm.setReceivers(new ArrayList<LabelValueBean> ());
    				}
    				messagesForm.getReceivers().add(lvbean);
    			}
    		}
    	}else if (messagesForm.getReceiverType()!=null && messagesForm.getReceiverType().equalsIgnoreCase("USER")){ //Users
    		List<User> users=(List<User>)AmpUserUtil.getAllUsers(false); //get all not banned users
    		if(users!=null && users.size()>0){
    			Iterator it=users.iterator();
    			while (it.hasNext()) {
    				User user=(User)it.next();
    				lvbean=new LabelValueBean(user.getName(),user.getId().toString());
    				if(messagesForm.getReceivers()==null){
    					messagesForm.setReceivers(new ArrayList<LabelValueBean> ());
    				}
    				messagesForm.getReceivers().add(lvbean);
    			}
    		}    	
    	}else if (messagesForm.getReceiverType()!=null && messagesForm.getReceiverType().equalsIgnoreCase("TM")){ //Users
    		List<TeamMember> teamMembers=(List)TeamMemberUtil.getAllTeamMembers(messagesForm.getSelectedTeamId()); //get all Team Members
    		if(teamMembers!=null && teamMembers.size()>0){
    			Iterator it=teamMembers.iterator();
    			while (it.hasNext()) {
    				TeamMember tm=(TeamMember)it.next();
    				lvbean=new LabelValueBean(tm.getMemberName(),tm.getMemberId().toString());
    				if(messagesForm.getReceivers()==null){
    					messagesForm.setReceivers(new ArrayList<LabelValueBean> ());
    				}
    				messagesForm.getReceivers().add(lvbean);
    			}
    		}    	
    	}
    	
    	return mapping.findForward("addOrEditAmpMessage");	
    }
    
    
    /**
     * If receiver is teamMember, Than we should load Teams to define which team's team member is the receiver.
     * So if one user is the member of 2 or more teams, he/she should see message only if she loads for the correct team(which is the team selected while creating a message)
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward loadTeamsForTeamMembers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	
	    	AmpMessageForm messagesForm=(AmpMessageForm)form;
	    	LabelValueBean lvbean=null;
	    	messagesForm.setTeamsForTeamMembers(null);	
	    	List<AmpTeam> teams=(List<AmpTeam>)TeamUtil.getAllTeams();
			if(teams!=null && teams.size()>0){
				Iterator it=teams.iterator();
				while (it.hasNext()) {
					AmpTeam team=(AmpTeam)it.next();
					lvbean=new LabelValueBean(team.getName(),team.getAmpTeamId().toString());
					if(messagesForm.getTeamsForTeamMembers()==null){
						messagesForm.setTeamsForTeamMembers(new ArrayList<LabelValueBean> ());
					}
					messagesForm.getTeamsForTeamMembers().add(lvbean);
				}
			}
	    	return mapping.findForward("addOrEditAmpMessage");
    }
    
    
    
    /**  
     * @author Dare Roinishvili
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward addMessage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current member who has logged in from the session
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);    	
    	AmpMessageForm messageForm=(AmpMessageForm)form;
    	AmpMessage message=null; //If User creates a message, than it's normal AmpMessage(and not system message, Calendar event or something else)
    	if(messageForm.getMessageId()==null) {
    		message=new AmpMessage();
    	}else {
    		message=AmpMessageUtil.getMessage(messageForm.getMessageId());
    	}    	
    	message.setName(messageForm.getMessageName());
    	message.setDescription(messageForm.getDescription());
    	message.setMessageType(messageForm.getMessageType());  
    	message.setPriorityLevel(messageForm.getPriorityLevel());
    	message.setSenderType(MessageConstants.SENDER_TYPE_USER); //This is User. (imitom,rom am actionshi shemodixar marto homepageze tu add messages daawvebi. da tu add message-it sheiqmna raime message,misi avtori yofila konkretuli user, romelic adds daawva)
    	message.setSenderId(teamMember.getMemberId());
    	message.setReceiverType(messageForm.getReceiverType());
    	message.setReceiverId(messageForm.getReceiverId());
    	message.setSelectedTeamId(messageForm.getSelectedTeamId());
    	if(!messageForm.isEditingMessage()){ //So we are adding message, not editing already existing one
    		Calendar cal=Calendar.getInstance();
    		message.setCreationDate(cal.getTime());
    		message.setRead(false); //when creating a message it's unread
    	}    	
    	//saving message
    	AmpMessageUtil.saveOrUpdateMessage(message);
    	//cleaning form values
    	setDefaultValues(messageForm);
		return mapping.findForward("viewMyDesktop");	
	}     
   
 private void setDefaultValues(AmpMessageForm form) {
	 form.setEditingMessage(false);
	 form.setMessageId(null);
	 form.setMessageName(null);
	 form.setDescription(null);
	 form.setPriorityLevel(new Long(0));
	 form.setMessageType(new Long(0));
	 form.setSenderType(null);
	 form.setSenderId(null);
	 form.setAllMessages(new ArrayList<AmpMessage>());
	 form.setReceivers(null);
	 form.setReceiverType(null);
	 form.setReceiverId(null);	 
	 form.setSelectedTeamId(null);
	 form.setClassName(null);
 }
 
 private void fillFormFields (AmpMessage message,AmpMessageForm form){	 
	 if(message!=null){
		 form.setMessageId(message.getId());
		 form.setMessageName(message.getName());
		 form.setDescription(message.getDescription());
		 form.setMessageType(message.getMessageType());
		 form.setPriorityLevel(message.getPriorityLevel());
		 form.setSenderType(MessageConstants.SENDER_TYPE_USER); //So this message is created by User
		 form.setSenderId(message.getSenderId()); //aq sheizleba formas davumato senderi da id-s mixedvit amovigo useri, romelmac gaogzavna alerti.
		 form.setSelectedTeamId(message.getSelectedTeamId());
		 form.setReceiverType(message.getReceiverType());
		 form.setReceiverId(message.getReceiverId());		 
		 form.setCreationDate(DateConversion.ConvertDateToString(message.getCreationDate()));
		 
		 form.setClassName(message.getClassName());
 	}	 
 }
}
