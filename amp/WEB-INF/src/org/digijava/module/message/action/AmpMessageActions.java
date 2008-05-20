package org.digijava.module.message.action;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.helper.MessageHelper;
import org.digijava.module.message.util.AmpMessageUtil;

public class AmpMessageActions extends DispatchAction {
	
	public static final String ROOT_TAG = "Messaging";
    
    public ActionForward fillTypesAndLevels (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {        		
    		AmpMessageForm messageForm=(AmpMessageForm)form;    		
    		if(request.getParameter("editingMessage").equals("false")){
    			setDefaultValues(messageForm);
    		}else {
    			Long id=new Long(request.getParameter("msgStateId"));    			
    	    	AmpMessageState state=AmpMessageUtil.getMessageState(id);
    	    	fillFormFields(state.getMessage(),messageForm,id);    	
    		}
		 return loadReceiversList(mapping,form,request,response);	
	}
    
   /**
    * user clicked cancel on view Messages page   
    */ 
    public ActionForward cancelMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {    	
    	AmpMessageForm alertsForm=(AmpMessageForm)form;
    	setDefaultValues(alertsForm);
    	return mapping.findForward("viewMyDesktop");
    }
    
    /**    
     * @return All Messages that belong to this Team Member. 
     * @throws Exception
     */
    public ActionForward viewAllMessages(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	AmpMessageForm messageForm=(AmpMessageForm)form;
    	
    	HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current team member 
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);   	
    	int tabIndex=0;
    	if(request.getParameter("tabIndex")!=null){
    		tabIndex=Integer.parseInt(request.getParameter("tabIndex"));
    	}else {
    		tabIndex=messageForm.getTabIndex();
    	}    	    	
    	
    	//List<AmpMessageState> allMessages=AmpMessageUtil.loadAllMsgStatesForTm(teamMember.getMemberId(),false); //inbox messages
    	List<AmpMessageState> allMessages=AmpMessageUtil.loadAllMessagesStates(teamMember.getMemberId()); //all messages
    	
    	if(tabIndex==1){ //<-----messages    		
    		if(allMessages!=null && allMessages.size()>0){
    			messageForm.setMessagesForTm(separateMessagesByType(allMessages,"m"));
    		}
    	}else if(tabIndex==2){// <--alerts
    		if(allMessages!=null && allMessages.size()>0){
    			messageForm.setMessagesForTm(separateMessagesByType(allMessages,"a"));
			}
    	}else if(tabIndex==3){// <---approvals
    		if(allMessages!=null && allMessages.size()>0){
    			messageForm.setMessagesForTm(separateMessagesByType(allMessages,"p"));
    		}    		
    	}else if(tabIndex==4){// <--calendar events
    		if(allMessages!=null && allMessages.size()>0){
    			messageForm.setMessagesForTm(separateMessagesByType(allMessages,"c"));
    		}   
    	}
    	messageForm.setTabIndex(tabIndex);
    	
    	String childTab=null;
    	if(request.getParameter("childTab")!=null){
    		childTab=request.getParameter("childTab");
    	}else {
    		childTab=messageForm.getChildTab();
    	}
    	List<AmpMessageState> msgStates=messageForm.getMessagesForTm();
    	
    	if(childTab==null || childTab.equalsIgnoreCase("inbox")){//<---received messages,alerts e.t.c.
    		messageForm.setMessagesForTm(filterMsgStates(msgStates,teamMember.getMemberId(),childTab)) ;
    	}else if(childTab.equalsIgnoreCase("sent")){ //<---sent 
    		messageForm.setMessagesForTm(filterMsgStates(msgStates,teamMember.getMemberId(),childTab)) ;
    	}else if(childTab.equalsIgnoreCase("draft")){ //<---draft
    		messageForm.setMessagesForTm(filterMsgStates(msgStates,teamMember.getMemberId(),childTab)) ;    		
    	}
    	
    	if(childTab==null){
    		childTab="inbox";
    	}
    	messageForm.setChildTab(childTab);
    	//sorting messages from newest one to oldest
    	Collections.reverse(messageForm.getMessagesForTm());
    	
    	//pagination
    	List<AmpMessageState> messages=messageForm.getMessagesForTm();    
    	int page=1;
    	if(request.getParameter("page")!=null){
    		page=Integer.parseInt(request.getParameter("page"));
    	}
    	int howManyPages=0;
    	if(messages!=null){
    		if(messages.size()%MessageConstants.MESSAGES_PER_PAGE==0){ //<--10 messages will be per page. This should come from settings
    			howManyPages=messages.size()/MessageConstants.MESSAGES_PER_PAGE;
    		}else {
    			howManyPages=(messages.size()/MessageConstants.MESSAGES_PER_PAGE)+1;
    		}   		
    		
    		
    		String[] allPages=new String[howManyPages==0?1:howManyPages];
    		
    		for (int i=1;i<=howManyPages;i++) {
				allPages[i-1]=Integer.toString(i);
			}
    		
    		messageForm.setAllPages(allPages);
    		
    		if(messageForm.getPage()==null){
    			if(request.getParameter("page")!=null){
    				messageForm.setPage(request.getParameter("page"));
    			}else{
    				messageForm.setPage("1");
    			}    			
    		}
    		
    		int fromIndex=MessageConstants.MESSAGES_PER_PAGE*(page-1);
    		int toIndex=0; 
    		if(messages.size()<fromIndex+MessageConstants.MESSAGES_PER_PAGE){//2 imitom, rom tito gverdze minda 2 cali message
    			toIndex=messages.size();
    		}else {
    			toIndex=fromIndex+MessageConstants.MESSAGES_PER_PAGE;
    		}    		
    		messageForm.setPagedMessagesForTm(messages.subList(fromIndex, toIndex));
    		messageForm.setLastPage(Integer.toString(howManyPages));
    		messageForm.setPagesToShow(MessageConstants.PAGES_TO_SHOW);
    	}
    	
    	return mapping.findForward("showAllMessages");
    }
    
    /**
     * fills Form to view selected Message    
     */
    public ActionForward viewSelectedMessage(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
    	Long stateId=new Long(request.getParameter("msgStateId"));
    	AmpMessageState msgState=AmpMessageUtil.getMessageState(stateId);    
    	msgState.setRead(true); 
    	//changing message state to read
    	AmpMessageUtil.saveOrUpdateMessageState(msgState);
    	fillFormFields(msgState.getMessage(),messagesForm,stateId);    	
    	return mapping.findForward("viewMessage");
    }
    
    public ActionForward makeMsgRead(ActionMapping mapping,	ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	AmpMessageState state=null;
    	Long msgStateId=null;
    	if(request.getParameter("msgStateId")!=null){
    		msgStateId=new Long(request.getParameter("msgStateId"));
    		state=AmpMessageUtil.getMessageState(msgStateId);
    		if(state.getSenderId()==null){
    			state.setRead(true);
    		}    		
    		AmpMessageUtil.saveOrUpdateMessageState(state);
    		
    		//creating xml that will be returned   		
    		
    		response.setContentType("text/xml");
    		OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
    		PrintWriter out = new PrintWriter(outputStream, true);
    		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    		xml += "<" + ROOT_TAG +">";
    		xml+="<"+"message id=\""+msgStateId+"\" ";
    		xml+="read=\""+state.getRead()+"\" ";
    		xml+="/>";
    		xml+="</"+ROOT_TAG+">";
    		out.println(xml);
			out.close();
			// return xml			
			outputStream.close();
    	}    	
    	return null;
    }
    
    
    /**
     * used when user clicks on forward link 
     */
    public ActionForward forwardMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
    	if(request.getParameter("fwd")!=null && request.getParameter("fwd").equals("fillForm")){
    		Long stateId=new Long(request.getParameter("msgStateId"));
        	AmpMessageState oldMsgsState=AmpMessageUtil.getMessageState(stateId);
        	AmpMessage msg=oldMsgsState.getMessage();
        	MessageHelper msgHelper=createHelperMsgFromAmpMessage(msg,stateId);
        	messagesForm.setForwardedMsg(msgHelper);
    	}
    	return loadReceiversList(mapping,messagesForm,request,response);	
    }
    
    /**
     * Removes selected Message
    */
    public ActionForward removeSelectedMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {    	
    	AmpMessageForm messagesForm=(AmpMessageForm)form;
    	AmpMessageUtil.removeMessageState(messagesForm.getMsgStateId()); 
    	return viewAllMessages(mapping,messagesForm,request,response);
    }    
 
    
    /**
     * loads List of Receivers according to the receiverType(all,Users,Team,TM)
     * @author Dare Roinishvili
    */
    public ActionForward loadReceiversList(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	AmpMessageForm messagesForm=(AmpMessageForm)form;    	
    	messagesForm.setTeamsMap(null);    	
    	
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
    	messagesForm.setTeamsMap(teamMap);
    	
    	return mapping.findForward("addOrEditAmpMessage");	
    }
    
 
    /**     
     * add Message
     */
    public ActionForward addMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,	HttpServletResponse response) throws Exception {
    	
    	HttpSession session = request.getSession();
    	TeamMember teamMember = new TeamMember();        	  
    	 // Get the current member who has logged in from the session
    	teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);    	
    	AmpMessageForm messageForm=(AmpMessageForm)form;    	
    	AmpMessage message=null; 
    	String[] messageReceivers=messageForm.getReceiversIds();
    	
    	if(messageForm.getMessageId()==null) {
    		if(messageForm.getSetAsAlert()==2){
    			message=new AmpMessage();
    		}else {
    			message=new AmpAlert();    			
    		}    		    		
    	}else {
    		if(messageForm.getSetAsAlert()==2){
    			message=new AmpMessage();
    		}else {    			
    			message=new AmpAlert();     			
    		}
    		//remove all States that were associated to this message
			List<AmpMessageState> statesAssociatedWithMsg=AmpMessageUtil.loadMessageStates(messageForm.getMessageId());
			for (AmpMessageState state : statesAssociatedWithMsg) {
				AmpMessageUtil.removeMessageState(state.getId());
			}
			//remove message
			AmpMessageUtil.removeMessage(messageForm.getMessageId());
    	}    	
    	message.setName(messageForm.getMessageName());
    	message.setDescription(messageForm.getDescription());
    	message.setMessageType(messageForm.getMessageType());  
    	message.setPriorityLevel(messageForm.getPriorityLevel());
    	//This is User. (because this action happens only when user adds a message from the homepage.)
    	message.setSenderType(MessageConstants.SENDER_TYPE_USER); 
    	message.setSenderId(teamMember.getMemberId());
    	/**
    	 * this will be filled only when we are forwarding a message
    	 */
    	if(messageForm.getForwardedMsg()!=null){
    		message.setForwardedMessageId(messageForm.getForwardedMsg().getMsgId());
    	}    	
    	//should we send a message or not
    	if(request.getParameter("toDo")!=null){
    		if(request.getParameter("toDo").equals("draft")){
    			message.setDraft(true);
    		}else {
    			message.setDraft(false);
    		}
    	}
    	
    	//We are adding a message,not editing already existing one
    	//if(!messageForm.isEditingMessage()){ 
    		Calendar cal=Calendar.getInstance();
    		message.setCreationDate(cal.getTime());   	
    	//}        	 	
    	//saving message
    	AmpMessageUtil.saveOrUpdateMessage(message);   
    	
    	//now create one state, with senderId=teamMemberId. This AmpMessageState will be used further to see sent messages
   		AmpMessageState state=new AmpMessageState();
       	state.setMessage(message);
       	state.setSender(teamMember.getMemberName());
       	state.setSenderId(teamMember.getMemberId());
       	AmpMessageUtil.saveOrUpdateMessageState(state);        	
    	    	
    	
    	
    	List<AmpMessageState> statesList=AmpMessageUtil.loadMessageStates(messageForm.getMessageId());  
    	List<Long> statesMemberIds=new ArrayList<Long>();
    	if(statesList==null){
    		statesList=new ArrayList<AmpMessageState>();
    	}
    	
    	if(statesList!=null && statesList.size()>0){
			//getting members Ids from states list			
			for (AmpMessageState mId : statesList) {
				statesMemberIds.add(mId.getMemberId());
			}    			    			
    	}	
		if(messageReceivers!=null && messageReceivers.length>0){
			for (String receiver : messageReceivers) {	
				if(receiver.startsWith("t")){//<--this means that receiver is team
					List<TeamMember> teamMembers=(List<TeamMember>)TeamMemberUtil.getAllTeamMembers(new Long(receiver.substring(2)));
					if(teamMembers!=null && teamMembers.size()>0){
						for (TeamMember tm : teamMembers) {
							if(! statesMemberIds.contains(tm.getMemberId())){
								createMessageState(message,tm.getMemberId(),teamMember.getMemberName());
							}
						}
					}
				}else {//<--receiver is team member
					if(! statesMemberIds.contains(new Long(receiver.substring(2)))){
						Long memId=new Long(receiver.substring(2));
						createMessageState(message,memId,teamMember.getMemberName());
					}
				}
			}
		}
    	//cleaning form values
    	setDefaultValues(messageForm);
		return mapping.findForward("viewMyDesktop");	
	}   
    
    
    private void createMessageState(AmpMessage message,Long memberId,String senderName) throws Exception{
    	AmpMessageState newMessageState=new AmpMessageState();
		newMessageState.setMessage(message);
		newMessageState.setSender(senderName);
		newMessageState.setMemberId(memberId);	
		newMessageState.setRead(false);
		//saving current state in db
		AmpMessageUtil.saveOrUpdateMessageState(newMessageState);
    }
    
    /**
     * clear form    
     */
	 private void setDefaultValues(AmpMessageForm form) {
		 form.setEditingMessage(false);
		 form.setMessageId(null);
		 form.setMessageName(null);
		 form.setDescription(null);
		 form.setPriorityLevel(new Long(0));
		 form.setMessageType(new Long(0));
		 form.setSenderType(null);
		 form.setSenderId(null);		
		 form.setTeamsMap(null);
		 form.setReceiversIds(null);	 
		 form.setClassName(null);
		 form.setMsgStateId(null);
		 form.setReceivers(null);
		 form.setSender(null);
		 form.setTabIndex(0);
		 form.setMsgType(0);
		 form.setAlertType(0);
		 form.setCalendarEventType(0);
		 form.setApprovalType(0);
		 form.setChildTab(null);
		 form.setSetAsAlert(0);
		 form.setForwardedMsg(null);
		 form.setPage(null);
		 form.setAllPages(null);
		 form.setPagedMessagesForTm(null);
		 form.setLastPage(null);
	 }
	 
	 private void fillFormFields (AmpMessage message,AmpMessageForm form,Long stateId) throws Exception{	 
		 if(message!=null){
			 form.setMessageId(message.getId());
			 form.setMessageName(message.getName());
			 form.setDescription(message.getDescription());
			 form.setMessageType(message.getMessageType());
			 form.setPriorityLevel(message.getPriorityLevel());
			 form.setSenderType(MessageConstants.SENDER_TYPE_USER); //this message is created by User
			 form.setSenderId(message.getSenderId()); 	 
			 form.setCreationDate(DateConversion.ConvertDateToString(message.getCreationDate()));		 
			 form.setClassName(message.getClassName());
			 form.setMsgStateId(stateId);
			 form.setReceivers(getMessageRecipients(message.getId()));
			 form.setSender(AmpMessageUtil.getMessageState(stateId).getSender());
			 //is alert or not
			 if(message.getClassName().equals("a")){
				 form.setSetAsAlert(2);
			 }else {
				 form.setSetAsAlert(1);
			 }
			 //getting forwarded message,if exists
			 AmpMessage msg=AmpMessageUtil.getMessage(message.getForwardedMessageId());
			 if(msg!=null){				 		        	
				 form.setForwardedMsg(createHelperMsgFromAmpMessage(msg,stateId));
			 }else{
				 form.setForwardedMsg(null);
			 }
			 
		 }
	 }
	 
	 /**
	  *create helper Message class from AmpMessage entity	  
	  */
	 private MessageHelper createHelperMsgFromAmpMessage(AmpMessage msg,Long stateId) throws Exception{
		 MessageHelper msgHelper=new MessageHelper(msg.getId(),msg.getName(),msg.getDescription());
     	 msgHelper.setFrom(AmpMessageUtil.getMessageState(stateId).getSender());
     	 if(msgHelper.getReceivers()==null){
     		msgHelper.setReceivers(new ArrayList<String>());
     		List<LabelValueBean> receivers=getMessageRecipients(msg.getId());
     		for (LabelValueBean lvb : receivers) {
 				msgHelper.getReceivers().add(lvb.getLabel());
 			}
     		msgHelper.setCreationDate(DateConversion.ConvertDateToString(msg.getCreationDate()));
     	 }
     	 return msgHelper;
	 }
	 
	 
	 /**
	  * used to get message recipients, which will be shown on edit Message Page 
	  */
	 private static List<LabelValueBean> getMessageRecipients(Long messageId) throws Exception{
		 	List<AmpMessageState> msgStates=AmpMessageUtil.loadMessageStates(messageId);
			List<LabelValueBean> members=null;
			if(msgStates!=null && msgStates.size()>0){
				members=new ArrayList<LabelValueBean>();
				for (AmpMessageState state :msgStates) {
					if(state.getMemberId()!=null){
						AmpTeamMember teamMember=TeamMemberUtil.getAmpTeamMember(state.getMemberId());
						LabelValueBean tm=new LabelValueBean(teamMember.getUser().getName(),"m:"+state.getMemberId());				
						members.add(tm);
					}					
				}
			}
			return members;
	 }
	 
	 /**
	  * used to filter all kinds of messages by specified type
	  */
	 private List<AmpMessageState> separateMessagesByType(List<AmpMessageState> messages,String msgType) throws Exception {
	   	List<AmpMessageState> retValue=new ArrayList<AmpMessageState>();
	   	for (AmpMessageState state : messages) {
			if(state.getMessage().getClassName().equals(msgType)){
				retValue.add(state);
			}
		}
	    return retValue;
	 }

	 
	 /**
	  *loads inbox or sent or draft messages according to filterBy parameter 
	  */
	 private List<AmpMessageState> filterMsgStates(List<AmpMessageState> messages,Long tmId,String filterBy) throws Exception {
	  	List<AmpMessageState> retValue=new ArrayList<AmpMessageState>();
	  	if(filterBy==null ||filterBy.equalsIgnoreCase("inbox")){
	  		for (AmpMessageState state : messages) {
				if(!state.getMessage().getDraft() && state.getSenderId()==null){
					retValue.add(state);
				}
			}
	  	}else if(filterBy.equalsIgnoreCase("sent")){
	  		for (AmpMessageState state : messages) {
				if(state.getSenderId()!=null && state.getSenderId().equals(tmId) && !state.getMessage().getDraft()){
					retValue.add(state);
				}
			}
	  	}else if(filterBy.equalsIgnoreCase("draft") ){
	  		for (AmpMessageState state : messages) {
				if(state.getMessage().getDraft() && state.getSenderId()!=null){
					retValue.add(state);
				}
			}
	  	}	   	
	    return retValue;
	 } 
}
