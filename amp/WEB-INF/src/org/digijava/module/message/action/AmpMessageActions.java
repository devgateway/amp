package org.digijava.module.message.action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.config.DigiConfig;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.mail.DgEmailManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.message.dbentity.AmpAlert;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageReceiver;
import org.digijava.module.message.dbentity.AmpMessageSettings;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.Approval;
import org.digijava.module.message.dbentity.CalendarEvent;
import org.digijava.module.message.dbentity.UserMessage;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.helper.MessageHelper;
import org.digijava.module.message.helper.ReciverName;
import org.digijava.module.message.util.AmpMessageUtil;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.util.DbUtil;

import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;

public class AmpMessageActions extends DispatchAction {

    public static final String ROOT_TAG = "Messaging";
    public static final String MESSAGES_TAG = "MessagesList";
    public static final String PAGINATION_TAG = "Pagination";
    public static final String INFORMATION_TAG = "Information";


    public ActionForward fillTypesAndLevels (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        AmpMessageForm messageForm=(AmpMessageForm)form;
        HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        if(request.getParameter("editingMessage").equals("false")){
            //load activities
            messageForm.setRelatedActivities(ActivityUtil.loadActivitiesNamesAndIds(teamMember));
            String[] conts=AmpMessageUtil.buildExternalReceiversFromContacts();
            messageForm.setContacts(conts);
            setDefaultValues(messageForm);
        }else {
            Long id=new Long(request.getParameter("msgStateId"));
            AmpMessageState state=AmpMessageUtil.getMessageState(id);
            fillFormFields(state.getMessage(),messageForm,id,true);
        }
     return loadReceiversList(mapping,form,request,response);
    }

    public ActionForward searchRelatedAcrivities (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        AmpMessageForm messageForm=(AmpMessageForm)form;
        HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        String srchStr = request.getParameter("srchStr");

        String[] srcResArray = ActivityUtil.searchActivitiesNamesAndIds(teamMember, srchStr);
        /*
        XMLDocument relatedActivityAutocompData = new XMLDocument();
        XML root = new XML("activities");
        relatedActivityAutocompData.addElement(root);
          */

        StringBuffer retVal = new StringBuffer();
        for (int srcResIdx = 0; srcResIdx < srcResArray.length; srcResIdx ++){
            /*
            XML actNode = new XML ("activity");
            actNode.addElement(srcResArray[srcResIdx]);
            root.addElement(actNode);
            */
            retVal.append(srcResArray[srcResIdx]);
            if (srcResIdx < srcResArray.length - 1) {
                retVal.append("\n");
            }
        }


        response.setCharacterEncoding("UTF-8");
        ServletOutputStream sos = response.getOutputStream();
        sos.write(retVal.toString().getBytes("UTF-8"));
        sos.close();

        return null;
    }
    
    private void writeListToOutput(String srchStr, List<String> srcResArray, HttpServletResponse response) throws IOException
    {
        StringBuffer retVal = new StringBuffer();
        String newValue = "Add New(-1)";
        if(newValue.toLowerCase().startsWith(srchStr.toLowerCase())){
            retVal.append(newValue);
            retVal.append("\n");
        }
        //retVal.append("[");
        for(int i=0; i<srcResArray.size();i++){
            /*if(i>0){
                retVal.append(",");
            }*/
            retVal.append(srcResArray.get(i));
            retVal.append("\n");
            
        }
        //retVal.append("]");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream sos = response.getOutputStream();
        sos.write(retVal.toString().getBytes("UTF-8"));
        sos.close();        
    }
        

    public ActionForward searchExternalContacts (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        AmpMessageForm messageForm=(AmpMessageForm)form;
        HttpSession session = request.getSession();
        String srchStr = request.getParameter("srchStr");

        String[] srcContactArray = AmpMessageUtil.searchExternalReceiversFromContacts(srchStr); 

        /*
        XMLDocument contactsAutocompData = new XMLDocument();
        XML root = new XML("contacts");
        contactsAutocompData.addElement(root);
          */
        StringBuffer retVal = new StringBuffer();
        for (int contactIdx = 0; contactIdx < srcContactArray.length; contactIdx ++){
            /*
            XML contactNode = new XML ("contact");
            contactNode.addElement(srcContactArray[contactIdx].replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
            root.addElement(contactNode);
            */
            retVal.append(srcContactArray[contactIdx]);
            if (contactIdx < srcContactArray.length - 1) {
                retVal.append("\n");
            }
        }
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF8"), true);
        
        out.print(retVal.toString());
        out.close();
        return null;
    }

   /**
    * user clicked cancel on view Messages page
    */
    public ActionForward cancelMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        //AmpMessageForm alertsForm=(AmpMessageForm)form;
        //setDefaultValues(alertsForm);
        
        //clear session if it contains sdm doc
        if(request.getSession().getAttribute("document")!=null){
            request.getSession().removeAttribute("document");
        }
        return mapping.findForward("showAllMessages");
    }

    public ActionForward gotoMessagesPage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

         // Get the current team member
         TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);

        AmpMessageForm messageForm=(AmpMessageForm)form;
        messageForm.setSortBy(null);
        AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
        if(settings!=null && settings.getMsgRefreshTime()!=null && settings.getMsgRefreshTime().longValue()>0){
            messageForm.setMsgRefreshTimeCurr(settings.getMsgRefreshTime());
            messageForm.setMsgStoragePerMsgTypeCurr(settings.getMsgStoragePerMsgType());
                int maxStorage = 0;
                if (settings.getMsgStoragePerMsgType() != null) {
                    maxStorage = settings.getMsgStoragePerMsgType().intValue();
                }
            messageForm.setDaysForAdvanceAlertsWarningsCurr(settings.getDaysForAdvanceAlertsWarnings());
            messageForm.setEmailMsgsCurrent(settings.getEmailMsgs());
//                 int tabIndex = 0;
//                if (request.getParameter("tabIndex") != null) {
//                    tabIndex = Integer.parseInt(request.getParameter("tabIndex"));
//                } else {
//                    tabIndex = messageForm.getTabIndex();
//                }
//                messageForm.setTabIndex(tabIndex);
//
//                String childTab = null;
//                if (request.getParameter("childTab") != null) {
//                    childTab = request.getParameter("childTab");
//                } else {
//                    childTab = messageForm.getChildTab();
//                }
//                if (childTab == null) {
//                    childTab = "inbox";
//                }
//            int count = 0;
//            int hiddenCount = 0;
//            messageForm.setChildTab(childTab);
//            if (tabIndex == 1) { //<-----messages
//                if (childTab == null || childTab.equalsIgnoreCase("inbox")) {
//                    //how many messages are in db. used for pagination
//                    count = AmpMessageUtil.getInboxMessagesCount(UserMessage.class, teamMember.getMemberId(), false,false,maxStorage);
//                    hiddenCount=AmpMessageUtil.getInboxMessagesCount(UserMessage.class, teamMember.getMemberId(), false,true,maxStorage);
//                } else if (childTab.equalsIgnoreCase("sent")) {
//                    //how many messages are in db. used for pagination
//                    count = AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class, teamMember.getMemberId(), false,false);
//                     hiddenCount = AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class, teamMember.getMemberId(), false,true);
//
//                } else if (childTab.equalsIgnoreCase("draft")) {
//                    //how many messages are in db. used for pagination
//                    count = AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class, teamMember.getMemberId(), true,false);
//                    hiddenCount= AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class, teamMember.getMemberId(), true,true);
//
//                }
//            } else if (tabIndex == 2) {// <--alerts
//                if (childTab == null || childTab.equalsIgnoreCase("inbox")) {
//                    //how many messages are in db. used for pagination
//                    count = AmpMessageUtil.getInboxMessagesCount(AmpAlert.class, teamMember.getMemberId(), false,false,maxStorage);
//                     hiddenCount = AmpMessageUtil.getInboxMessagesCount(AmpAlert.class, teamMember.getMemberId(), false,true,maxStorage);
//
//                } else if (childTab.equalsIgnoreCase("sent")) {
//                    //how many messages are in db. used for pagination
//                    count = AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class, teamMember.getMemberId(), false,false);
//                     hiddenCount = AmpMessageUtil.getInboxMessagesCount(AmpAlert.class, teamMember.getMemberId(), false,true,maxStorage);
//
//                } else if (childTab.equalsIgnoreCase("draft")) {
//                    //how many messages are in db. used for pagination
//                    count = AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class, teamMember.getMemberId(), true,false);
//                    hiddenCount= AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class, teamMember.getMemberId(), true,true);
//
//                } else {
//                    messageForm.setMsgRefreshTimeCurr(new Long(-1));
//                }
//
//            }else if(tabIndex == 3){ //<--approvals
//              count = AmpMessageUtil.getInboxMessagesCount(Approval.class, teamMember.getMemberId(), false,false,maxStorage);
//                hiddenCount = AmpMessageUtil.getInboxMessagesCount(Approval.class, teamMember.getMemberId(), false,true,maxStorage);
//            }else if(tabIndex == 4){//<--calendar events
//              count = AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class, teamMember.getMemberId(), false,false,maxStorage);
//                hiddenCount = AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class, teamMember.getMemberId(), false,true,maxStorage);
//            }
//            messageForm.setAllmsg(count);
//            messageForm.setHiddenMsgCount(hiddenCount);
        }

        String forwardPath = null;

        forwardPath = (request.getParameter("listOnly") != null && request.getParameter("listOnly").equals("true")) ? "showAllMessagesClean" : "showAllMessages";


        return mapping.findForward(forwardPath);
    }

    public ActionForward getMsgParams(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        response.setContentType("text/xml");
        AmpMessageForm messageForm = (AmpMessageForm) form;
        HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
        int maxStorage = 0;
        if (settings!=null && settings.getMsgStoragePerMsgType() != null) {
            maxStorage = settings.getMsgStoragePerMsgType().intValue();
        }

        int tabIndex = messageForm.getTabIndex();
        String childTab = messageForm.getChildTab();


        int count=0;
        int hiddenCount =0;
        if(tabIndex==1){
            if(childTab==null || childTab.equalsIgnoreCase("inbox")){
                count=AmpMessageUtil.getInboxMessagesCount(UserMessage.class,teamMember.getMemberId(),false,false,maxStorage);
                hiddenCount=AmpMessageUtil.getInboxMessagesCount(UserMessage.class, teamMember.getMemberId(), false,true,maxStorage);
            }else if(childTab.equalsIgnoreCase("sent")){
                count=AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class,teamMember.getMemberId(),false,false);
                 hiddenCount = AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class, teamMember.getMemberId(), false,true);
            }else if(childTab.equalsIgnoreCase("draft")){
                count=AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class,teamMember.getMemberId(),true,false);
                hiddenCount= AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class, teamMember.getMemberId(), true,true);
            }
        }else if(tabIndex==2){
            if(childTab==null || childTab.equalsIgnoreCase("inbox")){
                count=AmpMessageUtil.getInboxMessagesCount(AmpAlert.class,teamMember.getMemberId(),false,false,maxStorage);
                hiddenCount = AmpMessageUtil.getInboxMessagesCount(AmpAlert.class, teamMember.getMemberId(), false,true,maxStorage);
            }else if(childTab.equalsIgnoreCase("sent")){
                count=AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class,teamMember.getMemberId(),false,false);
                hiddenCount= AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class, teamMember.getMemberId(), false,true);
            }else if(childTab.equalsIgnoreCase("draft")){
                count=AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class,teamMember.getMemberId(),true,false);
                hiddenCount= AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class, teamMember.getMemberId(), true,true);
            }
        }else if(tabIndex==3){
            count=AmpMessageUtil.getInboxMessagesCount(Approval.class,teamMember.getMemberId(),false,false,maxStorage);
            hiddenCount = AmpMessageUtil.getInboxMessagesCount(Approval.class, teamMember.getMemberId(), false,true,maxStorage);
        }else if(tabIndex==4){
            count=AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class,teamMember.getMemberId(),false,false,maxStorage);
            hiddenCount = AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class, teamMember.getMemberId(), false,true,maxStorage);
        }

        XMLDocument msgInfo = new XMLDocument();
        XML root = new XML("message-info");
        root.addAttribute("total", count);
        root.addAttribute("totalHidden", hiddenCount);

        msgInfo.addElement(root);
        msgInfo.output(response.getOutputStream());
        response.getOutputStream().close();

        return null;
    }

    /**
     * @return All Messages that belong to this Team Member.
     * @throws Exception
     */
    public ActionForward viewAllMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {

        AmpMessageForm messageForm=(AmpMessageForm)form;

        HttpSession session = request.getSession();
        TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);

        int tabIndex=0;
        if(request.getParameter("tabIndex")!=null){
            tabIndex=Integer.parseInt(request.getParameter("tabIndex"));
        }else {
            tabIndex=messageForm.getTabIndex();
        }

        String childTab=null;
        if(request.getParameter("childTab")!=null){
            childTab=request.getParameter("childTab");
        }else {
            childTab=messageForm.getChildTab();
        }

        List<AmpMessageState> allMessages=null; //all messages
        AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
        int maxStorage = 0;
        if (settings!=null && settings.getMsgStoragePerMsgType() != null) {
            maxStorage = settings.getMsgStoragePerMsgType().intValue();
        }

        Integer[] page={1};
        if(request.getParameter("page")!=null){
            page[0] =Integer.parseInt(request.getParameter("page"));
        }

        int howManyPages=0;
        int count=0;
        int hiddenCount = 0;
        
        if(tabIndex==1){ //<-----messages
            if(childTab==null || childTab.equalsIgnoreCase("inbox")){
                //how many messages are in db. used for pagination
                count=AmpMessageUtil.getInboxMessagesCount(UserMessage.class,teamMember.getMemberId(),false,false,maxStorage);               
                allMessages =AmpMessageUtil.loadAllInboxMessagesStates(UserMessage.class,teamMember.getMemberId(),-1,page,maxStorage,messageForm.getSortBy());
            }else if(childTab.equalsIgnoreCase("sent")){
                //how many messages are in db. used for pagination
                count=AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class,teamMember.getMemberId(),false,false);
                allMessages=AmpMessageUtil.loadAllSentOrDraftMessagesStates(UserMessage.class,teamMember.getMemberId(),-1, false,page,messageForm.getSortBy());
            }else if(childTab.equalsIgnoreCase("draft")){
                //how many messages are in db. used for pagination
                count=AmpMessageUtil.getSentOrDraftMessagesCount(UserMessage.class,teamMember.getMemberId(),true,false);
                allMessages=AmpMessageUtil.loadAllSentOrDraftMessagesStates(UserMessage.class,teamMember.getMemberId(),-1, true,page,messageForm.getSortBy());
            }
        }else if(tabIndex==2){// <--alerts
            if(childTab==null || childTab.equalsIgnoreCase("inbox")){
                //how many messages are in db. used for pagination
                count=AmpMessageUtil.getInboxMessagesCount(AmpAlert.class,teamMember.getMemberId(),false,false,maxStorage);             
                allMessages =AmpMessageUtil.loadAllInboxMessagesStates(AmpAlert.class,teamMember.getMemberId(),-1,page,maxStorage,messageForm.getSortBy());
            }else if(childTab.equalsIgnoreCase("sent")){
                //how many messages are in db. used for pagination
                count=AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class,teamMember.getMemberId(),false,false);
                allMessages=AmpMessageUtil.loadAllSentOrDraftMessagesStates(AmpAlert.class,teamMember.getMemberId(),-1, false,page,messageForm.getSortBy());
            }else if(childTab.equalsIgnoreCase("draft")){
                //how many messages are in db. used for pagination
                count=AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class,teamMember.getMemberId(),true,false);
                allMessages=AmpMessageUtil.loadAllSentOrDraftMessagesStates(AmpAlert.class,teamMember.getMemberId(),-1, true,page,messageForm.getSortBy());
            }
        }else if(tabIndex==3){// <---approvals
            //how many messages are in db. used for pagination
            count=AmpMessageUtil.getInboxMessagesCount(Approval.class,teamMember.getMemberId(),false,false,maxStorage);
            allMessages =AmpMessageUtil.loadAllInboxMessagesStates(Approval.class,teamMember.getMemberId(),-1,page,maxStorage,messageForm.getSortBy());
        }else if(tabIndex==4){// <--calendar events
            //how many messages are in db. used for pagination
            count=AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class,teamMember.getMemberId(),false,false,maxStorage);
            allMessages =AmpMessageUtil.loadAllInboxMessagesStates(CalendarEvent.class,teamMember.getMemberId(),-1,page,maxStorage,messageForm.getSortBy());
        }
        if(allMessages!=null){
         if(allMessages!=null){
        Collections.reverse(allMessages);
         }
        
        }
        messageForm.setMessagesForTm(allMessages);

        messageForm.setAllmsg(count);

        messageForm.setTabIndex(tabIndex);
        
        messageForm.setHiddenMsgCount(hiddenCount);

        if(childTab==null){
            childTab="inbox";
        }
        messageForm.setChildTab(childTab);

        //pagination
        List<AmpMessageState> messages=messageForm.getMessagesForTm();
        List<AmpMessageState> msgs=messageForm.getMessagesForTm();
        /**
         * if max.Storage per message type is less than messages in db,then we should show only max.Storage amount messages.
         *and pages quantity will depend on max.Storage. In other case,we will show all messages and pages amount will depend on messages amount
         */
        if(settings!=null && settings.getMsgStoragePerMsgType()!=null && maxStorage<=count){
            count=maxStorage;
            //if(count<messages.size()){
                if(page[0]==1){
                    if(count<messages.size()){
                        messageForm.setMessagesForTm(messages.subList(0,count));
                    }hiddenCount= AmpMessageUtil.getSentOrDraftMessagesCount(AmpAlert.class, teamMember.getMemberId(), false,true);
                }else if(page[0]>1){
                    int toIndex=count-(page[0]-1)*MessageConstants.MESSAGES_PER_PAGE;
                    if(toIndex<messages.size()){
                        messageForm.setMessagesForTm(null);
                        msgs=new ArrayList<AmpMessageState>();
                        for (int i=0;i<toIndex;i++) {
                            msgs.add(messages.get(i));
                        }
                        messageForm.setMessagesForTm(msgs);
                    }
                }
            //}
        }
        if(count%MessageConstants.MESSAGES_PER_PAGE==0){
            howManyPages=count/MessageConstants.MESSAGES_PER_PAGE;
        }else {
            howManyPages=(count/MessageConstants.MESSAGES_PER_PAGE)+1;
        }

            // we always have at least 1 page
            howManyPages=howManyPages==0?1:howManyPages;

            String[] allPages=new String[howManyPages==0?1:howManyPages];

            for (int i=1;i<=howManyPages;i++) {
                allPages[i-1]=Integer.toString(i);
            }

            messageForm.setAllPages(allPages);
            messageForm.setPage(String.valueOf(page[0]));
            /*if(messageForm.getPage()==null){
                if(request.getParameter("page")!=null){
                    messageForm.setPage(request.getParameter("page"));
                }else{
                    messageForm.setPage("1");
                }
            }*/

            messageForm.setLastPage(Integer.toString(howManyPages));
            messageForm.setPagesToShow(MessageConstants.PAGES_TO_SHOW);

        messageForm.setPagedMessagesForTm(msgs);

        response.setContentType("text/xml");
           OutputStreamWriter outputStream = new OutputStreamWriter(response.
                                            getOutputStream(), "UTF-8");
        PrintWriter out = new PrintWriter(outputStream, true);
        String xml = messages2XML(messageForm.getPagedMessagesForTm(),messageForm);

        out.println(xml);
        out.close();
        // return xml
        outputStream.close();
        if(messageForm.isDeleteActionWasCalled()){
            messageForm.setDeleteActionWasCalled(false);
        }
        return null;
    }

    /**
     * fills Form to view selected Message
     */
    public ActionForward viewSelectedMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {

           AmpMessageForm messagesForm = (AmpMessageForm) form;
        AmpMessage message = null;
        boolean isMessageStateId=true;
        Long id=null;
        messagesForm.setReceivesrsNameMail(null);
        if (request.getParameter("msgStateId") != null) {
            id = new Long(request.getParameter("msgStateId"));
            AmpMessageState msgState = AmpMessageUtil.getMessageState(id);
            msgState.setRead(true);
            AmpMessageUtil.saveOrUpdateMessageState(msgState);
            message = msgState.getMessage();
        } else {
            // view forwarded message
            id = new Long(request.getParameter("msgId"));
            message = AmpMessageUtil.getMessage(id);
            isMessageStateId=false;
        }
        fillFormFields(message, messagesForm, id,isMessageStateId);

        
        for (AmpMessageReceiver receiver : message.getMessageReceivers()) {
            ReciverName recName = new ReciverName();
            recName.setUserNeme(receiver.getReceiver().getUser().getName());
            recName.setTeamName(receiver.getReceiver().getAmpTeam().getName());
            messagesForm.getReceivesrsNameMail().add(recName);
        }

        
        //external people
        if (message.getExternalReceivers() != null && message.getExternalReceivers().length() > 0) {
            String externalPeople = message.getExternalReceivers();
            String[] externalReceivers = externalPeople.split(",");
            for (int j = 0; j < externalReceivers.length; j++) {
                
                String receiverName = externalReceivers[j];
                if (j != externalReceivers.length - 1) {
                    receiverName += ",";
                }
                
                ReciverName recName = new ReciverName();
                recName.setUserNeme(receiverName);
                recName.setTeamName("");
                messagesForm.getReceivesrsNameMail().add(recName);
            }
        }

        return mapping.findForward("viewMessage");
    }

    public ActionForward makeMsgRead(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        AmpMessageState state=null;
        //Long msgStateId=null;
        if(request.getParameter("msgStateId")!=null){
            String[] messageIds=request.getParameter("msgStateId").split(",");
            //creating xml that will be returned
            response.setContentType("text/xml");
            OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
            PrintWriter out = new PrintWriter(outputStream, true);
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            xml += "<" + ROOT_TAG +">";
            
            for (String msgStateId : messageIds) {
                state=AmpMessageUtil.getMessageState(new Long(msgStateId));
                if(state.getSenderId()==null){
                    state.setRead(true);
                }
                AmpMessageUtil.saveOrUpdateMessageState(state);
                
                xml+="<"+"message id=\""+msgStateId+"\" ";
                xml+="read=\""+state.getRead()+"\" ";
                    xml+="msgId=\""+state.getMessage().getId()+"\" ";
                xml+="/>";
            }           
            xml+="</"+ROOT_TAG+">";
            out.println(xml);
            out.close();
            // return xml
            outputStream.close();
        }
        
        return null;
    }

    /*
     * used to check for new messages from desktop
     */
    public ActionForward checkForNewMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        AmpMessageForm messagesForm=(AmpMessageForm)form;

        messagesForm.setInboxFull(false);

        HttpSession session = request.getSession();
         // Get the current member who has logged in from the session
         TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);

        //separate message types: used on myMessages page on desktop
        int alertType=0;
        int msgType=0;
        int approvalType=0;
        int calEventType=0;
        AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();
        int maxStorage = 0;
        if (settings!=null && settings.getMsgStoragePerMsgType() != null) {
            maxStorage = settings.getMsgStoragePerMsgType().intValue();
        }
        
        if (teamMember != null) {
            msgType = AmpMessageUtil.getInboxMessagesCount(UserMessage.class, teamMember.getMemberId(),true,false,maxStorage);
            alertType = AmpMessageUtil.getInboxMessagesCount(AmpAlert.class, teamMember.getMemberId(),true,false,  maxStorage);
            approvalType = AmpMessageUtil.getInboxMessagesCount(Approval.class, teamMember.getMemberId(),true,false,  maxStorage);
            calEventType = AmpMessageUtil.getInboxMessagesCount(CalendarEvent.class, teamMember.getMemberId(),true,false,  maxStorage);

            //checking if Any of the inbox is full
            Class[] allTypesOfMessages=new Class [] {UserMessage.class, AmpAlert.class,Approval.class,CalendarEvent.class};
            for (Class<AmpMessage> cls : allTypesOfMessages) {
                if(AmpMessageUtil.isInboxFull(cls, teamMember.getMemberId())){
                    messagesForm.setInboxFull(true);
                    break;
                }
            }
        }

        //creating xml that will be returned
        response.setContentType("text/xml");
        OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
        PrintWriter out = new PrintWriter(outputStream, true);
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<" + ROOT_TAG +">";
        xml+="<"+"amount messages=\""+msgType+"\" ";
        xml+="alerts=\""+alertType+"\" ";
        xml+="approvals=\""+approvalType+"\" ";
        xml+="calEvents=\""+calEventType+"\" ";
        xml+="inboxFull=\""+messagesForm.isInboxFull()+"\" ";
        xml+="/>";
        xml+="</"+ROOT_TAG+">";
        out.println(xml);
        out.close();
        // return xml
        outputStream.close();
        return null;
    }

   /**
     * User clicked on reply/forward message
     */
    public ActionForward replyOrForwardMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        AmpMessageForm messagesForm=(AmpMessageForm)form;
        setDefaultValues(messagesForm);
        if( (request.getParameter("reply")!=null && request.getParameter("reply").equals("fillForm")) || ( request.getParameter("fwd")!=null && request.getParameter("fwd").equals("fillForm") ) ){
            Long stateId=new Long(request.getParameter("msgStateId"));
            AmpMessageState oldMsgsState=AmpMessageUtil.getMessageState(stateId);
            AmpMessage msg=oldMsgsState.getMessage();

                // for Bread-crumb Generation. we need to which tab we must return..
                if (msg instanceof AmpAlert) {
                    messagesForm.setTabIndex(2);
                } else {
                    if (msg instanceof Approval) {
                        messagesForm.setTabIndex(3);
                    } else {
                        if (msg instanceof CalendarEvent) {
                            messagesForm.setTabIndex(4);
                        } else {
                            messagesForm.setTabIndex(1);
                        }
                    }
                }
           //RE or FWD
           MessageHelper msgHelper=createHelperMsgFromAmpMessage(msg,stateId);

           if(request.getParameter("reply")!=null && request.getParameter("reply").equals("fillForm")){
               messagesForm.setMessageName("RE: "+ msg.getName());
               messagesForm.setRepliedMsg(msgHelper);
               //receiver- The suer who's message we reply(in case of User Message)
               if(msg.getSenderType().equals(MessageConstants.SENDER_TYPE_USER)){
                   Long messageCreatorId=msg.getSenderId();
                   AmpTeamMember creator=TeamMemberUtil.getAmpTeamMember(messageCreatorId);
                   if(creator!=null){
                       List<LabelValueBean> receivers=new ArrayList<LabelValueBean>();
                       AmpTeam creatorTeam=creator.getAmpTeam();
                       LabelValueBean teamLabel=new LabelValueBean("---"+creatorTeam.getName()+"---","t:"+creatorTeam.getAmpTeamId().toString());
                       receivers.add(teamLabel);
                       LabelValueBean tm=new LabelValueBean(creator.getUser().getFirstNames() + " " + creator.getUser().getLastName(),"m:" + creator.getAmpTeamMemId().toString());
                       receivers.add(tm);
                       messagesForm.setReceivers(receivers);
                       String[] recId = new String[]{"m:" + creator.getAmpTeamMemId().toString()};
                       messagesForm.setReceiversIds(recId);
                   }
               }               
           }else if(request.getParameter("fwd")!=null && request.getParameter("fwd").equals("fillForm")){
               messagesForm.setMessageName("FWD: "+ msg.getName());
               messagesForm.setDescription(msg.getDescription());
               messagesForm.setForwardedMsg(msgHelper);
           }
           //related activity possibilities
            HttpSession session = request.getSession();
            TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
            messagesForm.setRelatedActivities(ActivityUtil.loadActivitiesNamesAndIds(teamMember));
        }
        return loadReceiversList(mapping,messagesForm,request,response);
    }

    /**
     * Removes selected Message
    */
    public ActionForward removeSelectedMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        AmpMessageForm messagesForm=(AmpMessageForm)form;
        messagesForm.setDeleteActionWasCalled(true);
        String[] stateIds = messagesForm.getRemoveStateIds().split(",");
        for (String id : stateIds) {
            HttpSession session = request.getSession();
            TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
            boolean addAtTop = getNextMessage(teamMember.getMemberId(), messagesForm.getTabIndex(), messagesForm.getChildTab());
            messagesForm.setAddAtTop(addAtTop);
            //remove message from db
            AmpMessageUtil.removeMessageState(Long.parseLong(id));

        }
        //reduce hidden messages amount
        int hiddenMessages=messagesForm.getHiddenMsgCount();
        if (hiddenMessages > 0) {
            if(hiddenMessages> stateIds.length){
                hiddenMessages -= stateIds.length;
            }
            else{
                 hiddenMessages=0;
            }
            
        }
        messagesForm.setHiddenMsgCount(hiddenMessages);
        //getting message which will become visible instead of deleted one
        messagesForm.setRemoveStateIds(null);
        return viewAllMessages(mapping, messagesForm, request, response);
    }

    /**
     * Used to define whether User has hidden messages in her inbox/sent/draft tabs of specified Message Type
     * If he/she does, then after deleting one message, the first hidden one should become visible.this mean should be delivered to user.
     */
    private boolean getNextMessage(Long tmId,int tabIndex,String childTab) throws Exception{
        boolean addAtTop=false;
        boolean isFull=false;
        Class  clazz=null;
        AmpMessageState state=null;
        if(tabIndex==1){ //<-----messages
            clazz=UserMessage.class;
        }else if(tabIndex==2){// <--alerts
            clazz=AmpAlert.class;
        }else if(tabIndex==3){// <---approvals.
            clazz=Approval.class;
        }else if(tabIndex==4){// <--calendar events
            clazz=CalendarEvent.class;
        }

        if(childTab!=null && (childTab.equalsIgnoreCase("inbox"))){
            isFull=AmpMessageUtil.isInboxFull(clazz, tmId);
            state=AmpMessageUtil.getFirstHiddenInboxMessage(clazz, tmId);
        }else if(childTab!=null && (childTab.equalsIgnoreCase("sent"))){
            isFull=AmpMessageUtil.isSentOrDraftFull(clazz, tmId, false);
            state=AmpMessageUtil.getFirstHiddenSentOrDraftMessage(clazz, tmId, false);
        }else if(childTab!=null && (childTab.equalsIgnoreCase("draft"))){
            isFull=AmpMessageUtil.isSentOrDraftFull(clazz, tmId, true);
            state=AmpMessageUtil.getFirstHiddenSentOrDraftMessage(clazz, tmId, true);
        }
        if(isFull){
            addAtTop=true;
            state.setMessageHidden(false);
            AmpMessageUtil.saveOrUpdateMessageState(state);
        }
        return addAtTop;
    }


    /**
     * loads List of Receivers according to the receiverType(all,Users,Team,TM)
     * @author Dare Roinishvili
    */
    public ActionForward loadReceiversList(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {

        AmpMessageForm messagesForm=(AmpMessageForm)form;
        messagesForm.setTeamsMap(null);
        
        Collection<String> selAtts = new ArrayList<String> ();
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
    
    public ActionForward attachFilesToMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        AmpMessageForm messageForm=(AmpMessageForm)form;
        //receivers shouldn't be lost
        fillMessageFormReceivers(messageForm);      
        
        
        int currentAttachmentSize=messageForm.getFileUploaded().getFileData().length;
        int attachedFilesSize=messageForm.getAttachmentsSize();
        //check
        ActionErrors errors=null;
        if(currentAttachmentSize+attachedFilesSize > MessageConstants.ATTACHMENTS_MAX_SIZE){            
            errors=new ActionErrors();
            errors.add("invalidSize", new ActionMessage("error.message.invalidAttachmentsSize", TranslatorWorker.translateText("You can attach max 10 Mb files")));
            
            if (errors.size() > 0){
                //we have all the errors for this step saved and we must throw the amp error
                saveErrors(request, errors);                
            }
        }else{ //attachments size is OK
            
            Sdm messageHolder=null;
            if(messageForm.getSdmDocument()!=null){
                messageHolder=messageForm.getSdmDocument();
            }else{
                messageHolder=new Sdm();
            }
            SdmItem sdmItem = new SdmItem();
            sdmItem.setContentType(messageForm.getFileUploaded().getContentType());
            sdmItem.setRealType(SdmItem.TYPE_FILE);
            sdmItem.setContent(messageForm.getFileUploaded().getFileData());
            sdmItem.setContentText(messageForm.getFileUploaded().getFileName());
            sdmItem.setContentTitle(messageForm.getFileUploaded().getFileName());
            
            if (messageHolder.getItems() != null) {
                //sdmItem.setParagraphOrder(new Long(DbUtil.getLastParagraph(messageHolder,sdmItem.getParagraphOrder()) + 1));
                sdmItem.setParagraphOrder(new Long(messageHolder.getItems().size()));
                messageHolder.getItems().add(sdmItem);
            }
            else {
                HashSet items = new HashSet();
                sdmItem.setParagraphOrder(new Long(0));
                items.add(sdmItem);
                messageHolder.setItems(items);
            }
            
            messageForm.setSdmDocument(messageHolder);
            //increment attachments size        
            messageForm.setAttachmentsSize(attachedFilesSize+currentAttachmentSize);
            request.getSession().setAttribute("document", messageHolder);
        }
        
        return mapping.findForward("addOrEditAmpMessage");
    }
    
    
    
    public ActionForward removeAttachment(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        AmpMessageForm messageForm=(AmpMessageForm)form;
        //receivers shouldn't be lost
        fillMessageFormReceivers(messageForm);
        
        Sdm attachmentsHolder=messageForm.getSdmDocument(); //this won't be null, because if we are removing any attachment, this automatically means that we have Sdm
        Long attachmentOrder=null; //order number or sdmItem in Sdm
        if(request.getParameter("attachmentOrder")!=null){
            attachmentOrder=new Long(request.getParameter("attachmentOrder"));
        }
        //messageHolder should contain at least one attachment(the one we want to remove)
        if(attachmentOrder!=null){
            for (SdmItem attachment : attachmentsHolder.getItems()) {
                if(attachment.getParagraphOrder().equals(attachmentOrder)){                 
                    attachmentsHolder.getItems().remove(attachment);
                    //decrement attached files size
                    int currentAttachmentSize=attachment.getContent().length;
                    int attachedFilesSize=messageForm.getAttachmentsSize();
                    messageForm.setAttachmentsSize(attachedFilesSize-currentAttachmentSize);
                    break;
                }
            }
        }
        if(attachmentsHolder.getItems().size()==0){ //if all attachments were removed,we don't need to create any Sdm
            messageForm.setSdmDocument(null);
        }else{
            messageForm.setSdmDocument(attachmentsHolder);
        }       
        return mapping.findForward("addOrEditAmpMessage");
    }
    
    /**
     * Fills form receivers field with submitted values from messageForm.getReceiversIds().
     * used not to loose selected receivers when form is submitted to attach/remove attachment from the message
     * @param messageForm
     */

    private void fillMessageFormReceivers(AmpMessageForm messageForm) {
        String[] messageReceivers=messageForm.getReceiversIds();
        List<LabelValueBean> allReceivers=null;
        if(messageReceivers!=null && messageReceivers.length>0){
            allReceivers=new ArrayList<LabelValueBean>();
            
            Set<AmpTeam> selectedTeams=new HashSet<AmpTeam>();
            //this map will hold teamId as key and all the selected members for given team as value
            Map<Long,Set<AmpTeamMember>> membersForTheTeam=new HashMap<Long, Set<AmpTeamMember>>();
            Long id=null;
            for(int i=0;i<messageReceivers.length;i++){
                if(messageReceivers[i].startsWith("t:")){//team
                    id=new Long(messageReceivers[i].substring(messageReceivers[i].indexOf("t:")+2));
                    AmpTeam team=TeamUtil.getAmpTeam(id);
                    selectedTeams.add(team);
                }else if(messageReceivers[i].startsWith("m:")){//member
                    id=new Long(messageReceivers[i].substring(messageReceivers[i].indexOf("m:")+2));
                    AmpTeamMember teamMember=TeamMemberUtil.getAmpTeamMember(id);
                    Set<AmpTeamMember> members=membersForTheTeam.get(teamMember.getAmpTeam().getAmpTeamId());
                    if(members==null){
                        members=new HashSet<AmpTeamMember>();                       
                    }
                    members.add(teamMember);
                    membersForTheTeam.put(teamMember.getAmpTeam().getAmpTeamId(), members);
                                        
                }else if(messageReceivers[i].startsWith("c:")){//contacts
                    LabelValueBean contact=new LabelValueBean(messageReceivers[i].substring(messageReceivers[i].indexOf("c:")+2),messageReceivers[i]);
                    allReceivers.add(contact);
                }
            }
            
            for (AmpTeam team : selectedTeams) {
                LabelValueBean teamLabel=new LabelValueBean("---"+team.getName()+"---","t:"+team.getAmpTeamId().toString());
                allReceivers.add(teamLabel);
                Set<AmpTeamMember> membersOfThisTeam=membersForTheTeam.get(team.getAmpTeamId());
                for (AmpTeamMember member : membersOfThisTeam) {
                    LabelValueBean tm=new LabelValueBean(member.getUser().getFirstNames() + " " + member.getUser().getLastName(),"m:" + member.getAmpTeamMemId().toString());
                    allReceivers.add(tm);
                }
            }
            
            messageForm.setReceivers(allReceivers);
        }
    }


    /**
     * add Message
     */
    public ActionForward addMessage(ActionMapping mapping,ActionForm form, HttpServletRequest request,  HttpServletResponse response) throws Exception {
        ActionErrors errors = null;
        HttpSession session = request.getSession();
         // Get the current member who has logged in from the session
         TeamMember teamMember = (TeamMember) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_MEMBER);
        //getting settings for message
        AmpMessageSettings settings=AmpMessageUtil.getMessageSettings();

        AmpMessageForm messageForm=(AmpMessageForm)form;
        AmpMessage message=null;
        String[] messageReceivers=messageForm.getReceiversIds();

        if(messageForm.getMessageId()==null) {
            if(messageForm.getSetAsAlert()==0){
                message=new UserMessage();
            }else {
                message=new AmpAlert();
            }
        }else {
            if(messageForm.getSetAsAlert()==0){
                message=new UserMessage();
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
        if(message instanceof AmpAlert){
             messageForm.setTabIndex(2); // to navigate to the Alert Tab
        }
        else{
             messageForm.setTabIndex(1);// to navigate to the Message Tab
        }
        message.setName(messageForm.getMessageName());
        message.setDescription(messageForm.getDescription());
        message.setMessageType(messageForm.getMessageType());
        message.setPriorityLevel(messageForm.getPriorityLevel());
        //This is User. (because this action happens only when user adds a message from the homepage.)
        message.setSenderType(MessageConstants.SENDER_TYPE_USER);
        message.setSenderId(teamMember.getMemberId());
        User user=TeamMemberUtil.getAmpTeamMember(teamMember.getMemberId()).getUser();
        String senderName = user.getFirstNames() + " " + user.getLastName() 
                            + "<" + user.getEmailUsedForNotification() + ">;" + teamMember.getTeamName();
        message.setSenderName(senderName);
        /**
         * this will be filled only when we are forwarding a message
         */
        if(messageForm.getForwardedMsg()!=null){
            message.setForwardedMessage(AmpMessageUtil.getMessage(messageForm.getForwardedMsg().getMsgId()));
        }
        /**
         * This will be filled only when we are replying a message
         */
        if(messageForm.getRepliedMsg()!=null){
            message.setRepliedMessage(AmpMessageUtil.getMessage(messageForm.getRepliedMsg().getMsgId()));
        }
        //link message to activity if necessary
        if(messageForm.getSelectedAct()!=null && messageForm.getSelectedAct().length()>0){
            try {
                String act=messageForm.getSelectedAct();
                String activityId=act.substring(act.lastIndexOf("(")+1,act.lastIndexOf("")-1);
                Long actId = new Long(activityId);
                AmpActivityVersion testAct = ActivityUtil.loadActivity(actId);
                if (testAct == null) {
                    if (errors == null) {
                        errors=new ActionErrors();
                    }
                    errors.add("invalidActivity", new ActionMessage("error.message.invalidActivity", TranslatorWorker.translateText("Selected activity does not exist. Please verify the name")));
                }
                message.setRelatedActivityId(actId);
                //now we must create activity URL
                String fullModuleURL=RequestUtils.getFullModuleUrl(request);
                String objUrl = fullModuleURL.substring(0, fullModuleURL.indexOf("message"))
                        + "aim/viewActivityPreview.do~activityId=" + activityId;
                message.setObjectURL(objUrl);
            } catch (NumberFormatException ex) {
                if (errors == null) {
                    errors=new ActionErrors();
                }
                errors.add("invalidActivity", new ActionMessage("error.message.invalidActivity", TranslatorWorker.translateText("Selected activity does not exist. Please verify the name")));
            }
        }
        //should we send a message or not
        if(request.getParameter("toDo")!=null){
            if(request.getParameter("toDo").equals("draft")){
                message.setDraft(true);
            }else {
                message.setDraft(false);
            }
        }


            Calendar cal=Calendar.getInstance();
            message.setCreationDate(cal.getTime());
            
            //save attached files 
            Sdm document=messageForm.getSdmDocument();
            Sdm doc=null;
            if(document!=null){
                document.setName(message.getName());
                document.setId(null);
                doc=DbUtil.saveOrUpdateDocument(document);
                message.setAttachedDocs(doc);
            } 

        //saving message
        AmpMessageUtil.saveOrUpdateMessage(message);

        //now create one state, with senderId=teamMemberId. This AmpMessageState will be used further to see sent messages
        AmpMessageState state=new AmpMessageState();
        state.setMessage(message);
        state.setSender(teamMember.getMemberName()+";"+teamMember.getTeamName());
        state.setSenderId(teamMember.getMemberId());
        
        // check if user's inbox is already full
        Class clazz=null;
        if(message.getClassName().equalsIgnoreCase("u")){
            clazz=UserMessage.class;
        }else if(message.getClassName().equalsIgnoreCase("a")){
            clazz=AmpAlert.class;
        }
        
        if(AmpMessageUtil.isSentOrDraftFull(clazz,teamMember.getMemberId(),message.getDraft())){
            state.setMessageHidden(true);
        }else{
            state.setMessageHidden(false);
        }
        AmpMessageUtil.saveOrUpdateMessageState(state);



        List<AmpMessageState> statesList=AmpMessageUtil.loadMessageStates(messageForm.getMessageId());
//      List<Long> statesMemberIds=new ArrayList<Long>();
        if(statesList==null){
            statesList=new ArrayList<AmpMessageState>();
        }
        /**
         * @TODO
         */
//      if(statesList!=null && statesList.size()>0){
//          //getting members Ids from states list
//          for (AmpMessageState mId : statesList) {
//              statesMemberIds.add(mId.getMemberId());
//          }
//      }
        if (messageReceivers != null && messageReceivers.length > 0 && (errors == null || errors.size() == 0)) {
            Collection<InternetAddress> addrCol=new ArrayList<InternetAddress>();
            for (String receiver : messageReceivers) {
                if (receiver.startsWith("m")) {
                    Long memId = new Long(receiver.substring(2));
                    AmpTeamMember msgReceiver=TeamMemberUtil.getAmpTeamMember(memId);
                    createMessageState(message, msgReceiver, teamMember.getMemberName());
                    message.addMessageReceiver(msgReceiver);
                    if (settings != null && settings.getEmailMsgs() != null && settings.getEmailMsgs().equals(new Long(1))) {
                        //creating internet address where the mail will be sent
                        addrCol.add(new InternetAddress(
                                TeamMemberUtil.getAmpTeamMember(memId).getUser().getEmailUsedForNotification()));
                    }
                } else if (receiver.startsWith("c")) { //contacts or people outside AMP
                    // we should send email to contacts, regardless setting value in Message Manager
                    receiver = receiver.substring(2); 
                    String email = receiver;
                    if (receiver.indexOf("<") != -1) {
                        email = receiver.substring(receiver.indexOf("<") + 1, receiver.indexOf(">"));
                    }   
                    
                    String receivers = message.getExternalReceivers();
                    if (receivers == null) {
                        receivers = "";
                    } else {
                        if (receivers.length() > 0) {
                            receivers += ", ";
                        }
                    }

                    receivers += receiver;
                    message.setExternalReceivers(receivers);                        
                    
                    addrCol.add(new InternetAddress(email));
                }
            }

            if (settings != null && settings.getEmailMsgs() != null && settings.getEmailMsgs().equals(new Long(1))) {
                if (request.getParameter("toDo") != null && !request.getParameter("toDo").equals("draft")) {
                    InternetAddress[] addresses = (InternetAddress[]) addrCol
                            .toArray(new InternetAddress[addrCol.size()]);
                    DgEmailManager.sendMail(addresses, user.getEmailUsedForNotification(), message, doc);
                }
            }
            AmpMessageUtil.saveOrUpdateMessage(message);
        }
        
         //clear session if it contains sdm doc
        if(request.getSession().getAttribute("document")!=null){
            request.getSession().removeAttribute("document");
        }

        //cleaning form values
           setDefaultValues(messageForm);
        if (request.getParameter("toDo") != null && request.getParameter("toDo").equals("draft")) {
            messageForm.setChildTab("draft");
        } else {
            messageForm.setChildTab("inbox");
        }

        String fwd = null;
        
        if (errors != null && errors.size() > 0){
            saveErrors(request, errors);
            fwd = "createMessageError";
        } else {
            fwd = "showAllMessages";
        }
        return mapping.findForward(fwd);
    }


    private void createMessageState(AmpMessage message, AmpTeamMember receiver, String senderName) throws Exception {
        AmpMessageState newMessageState=new AmpMessageState();
        newMessageState.setMessage(message);
        newMessageState.setSender(senderName);      
        newMessageState.setReceiver(receiver);
        newMessageState.setRead(false);
        //check if user's inbox is already full
        Class clazz=null;
        if(message.getClassName().equalsIgnoreCase("u")){
            clazz=UserMessage.class;
        }else if(message.getClassName().equalsIgnoreCase("a")){
            clazz=AmpAlert.class;
        }

        int maxStorage=-1;
        AmpMessageSettings setting=AmpMessageUtil.getMessageSettings();
        if(setting!=null && setting.getMsgStoragePerMsgType()!=null){
            maxStorage=setting.getMsgStoragePerMsgType().intValue();
        }
        if(AmpMessageUtil.isInboxFull(clazz, receiver.getAmpTeamMemId()) || AmpMessageUtil.getInboxMessagesCount(clazz, receiver.getAmpTeamMemId(),false,false, maxStorage)>=maxStorage){
            newMessageState.setMessageHidden(true);
        }else{
            newMessageState.setMessageHidden(false);
        }
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
         form.setPriorityLevel(new Long(2));
         form.setMessageType(new Long(0));
         form.setSenderType(null);
         form.setSenderId(null);
         form.setTeamsMap(null);
         form.setReceiversIds(null);
         form.setClassName(null);
         form.setMsgStateId(null);
         form.setReceivers(null);
         form.setSender(null);
         //form.setTabIndex(1);
         form.setMsgType(0);
         form.setAlertType(0);
         form.setCalendarEventType(0);
         form.setApprovalType(0);
         //form.setChildTab("inbox");
         form.setSetAsAlert(0);
         form.setForwardedMsg(null);
         form.setPage(null);
         form.setAllPages(null);
         form.setPagedMessagesForTm(null);
         form.setLastPage(null);
         form.setDeleteActionWasCalled(false);
         form.setSelectedAct(null);
         form.setInboxFull(false);
         form.setReceivesrsNameMail(null);
         form.setSdmDocument(null);
         form.setAttachmentsSize(0);
         form.setSortBy(null);
     }

     private void fillFormFields (AmpMessage message,AmpMessageForm form,Long id,boolean isStateId) throws Exception{
         if(message!=null){
             form.setMessageId(message.getId());
             form.setMessageName(message.getName());
             form.setDescription(message.getDescription());
             form.setMessageType(message.getMessageType());
             form.setPriorityLevel(message.getPriorityLevel());
             form.setSenderType(MessageConstants.SENDER_TYPE_USER); //this message is created by User
             form.setSenderId(message.getSenderId());
             form.setCreationDate(DateConversion.convertDateToLocalizedString(message.getCreationDate()));
             form.setClassName(message.getClassName());
             form.setObjectURL(message.getObjectURL());

             form.setRepliedMessage(message.getRepliedMessage());
             form.setForwardedMessage(message.getForwardedMessage());

             if(message.getSenderType().equalsIgnoreCase("User")){
                 form.setSender(message.getSenderName());
             } else{
                 form.setSender(message.getSenderType());
             }
             if(isStateId){
                 form.setMsgStateId(id);
                 AmpMessage msg = message.getForwardedMessage();
                 if (msg != null) {
                     form.setForwardedMsg(createHelperMsgFromAmpMessage(msg, id));
                 } else {
                     form.setForwardedMsg(null);
                 }
             }


             //form.setReceivers(getMessageRecipients(message));
             form.setReceiversIds(getMessageRecipients(message));
             form.setSelectedAct(null);
             //getting related activity if exists
             if(message.getRelatedActivityId()!=null){
                 form.setSelectedAct(getRelatedActivity(message.getRelatedActivityId()));
             }
             
             form.setReceivesrsNameMail(new ArrayList<>());

             form.setSdmDocument(message.getAttachedDocs());

             //is alert or not
             if(message.getClassName().equals("a")){
                 form.setSetAsAlert(1);
             }else {
                 form.setSetAsAlert(0);
             }
         }
     }


     private String getRelatedActivity(Long actId){
         String retValue;
         String actName = ActivityUtil.getActivityName(actId);
         retValue = actName + "(" + actId + ")";
         return retValue;
     }

     /**
     * Constructs XML from Messages
     */
    private String messages2XML(List<AmpMessageState> states,AmpMessageForm form) throws AimException {

        String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        result += "<" + ROOT_TAG + ">";
        result+="<" + MESSAGES_TAG +">";
        if (states != null && states.size() > 0) {
            for (AmpMessageState state : states) {
                result += "<" + "message name=\"" + org.digijava.module.aim.util.DbUtil.filter(state.getMessage().getName()) + "\" ";
                result += " id=\"" + state.getId() + "\"";
                result += " msgId=\"" + state.getMessage().getId() + "\"";
                if(state.getMessage().getSenderType()!=null && state.getMessage().getSenderType().equalsIgnoreCase(MessageConstants.SENDER_TYPE_USER)){
                    result += " from=\"" +org.digijava.module.aim.util.DbUtil.filter(state.getMessage().getSenderName()) + "\"";
                }else{
                    result += " from=\"" +MessageConstants.SENDER_TYPE_SYSTEM + "\"";
                }
                result += " to=\"" + org.digijava.module.aim.util.DbUtil.filter(
                        state.getMessage().getAllMessageReceiversAsString()) + "\"";
                result += " received=\"" + DateConversion.convertDateToString(state.getMessage().getCreationDate()) + "\"";
                result += " priority=\"" + state.getMessage().getPriorityLevel() + "\"";
                //attachments start
                if(state.getMessage().getAttachedDocs()!=null){
                    Sdm attachedDocs=state.getMessage().getAttachedDocs();
                    DigiConfig config = DigiConfigManager.getConfig();
                    String partialURL = config.getSiteDomain().getContent() ;
                    String links="";
                    for (SdmItem attachedFile : attachedDocs.getItems()) {              
                        links += "<img src=\""+"/TEMPLATE/ampTemplate/imagesSource/common/attachment.png"+"\" border=\""+"0\" />";
                        links += "<a  href=\"" + partialURL+"sdm/showFile.do~activeParagraphOrder="+attachedFile.getParagraphOrder()+"~documentId="+attachedDocs.getId()+"\" >";
                        links += attachedFile.getContentTitle();
                        links += "</a>";
                        links+=",";                     
                    }
                    links=links.substring(0, links.length()-1);
                    result += " attachments=\"" +org.digijava.module.aim.util.DbUtil.filter(links) + "\"";
                }
                String desc=org.digijava.module.aim.util.DbUtil.filter(state.getMessage().getDescription());
                result += " msgDetails=\"" +desc + "\"";
                result += " read=\"" + state.getRead() + "\"";
                result += " isDraft=\"" + state.getMessage().getDraft() + "\"";
                String objUrl=org.digijava.module.aim.util.DbUtil.filter(state.getMessage().getObjectURL());
                result += " objURL=\"" + objUrl + "\"";
                result += ">";
                AmpMessage forwarded = state.getMessage().getForwardedMessage();
                if (forwarded != null) {
                    result += messages2XML(forwarded,state.getId(),true);
                }
                AmpMessage replied = state.getMessage().getRepliedMessage();
                if(replied!=null && replied.getName() != null){
                    result += messages2XML(replied,state.getId(),false);
                }
                result += "</message>";
            }
        }
        result+="</" + MESSAGES_TAG +">";
        //pagination
        boolean messagesExist=(states==null||states.size()==0)?false:true;
        result+="<" + PAGINATION_TAG +">";
        result+="<"+"pagination messagesExist=\""+messagesExist+"\"";
        result+=" page=\""+form.getPage()+"\"";
        result+=" allPages=\""+form.getAllPages().length+"\"";
        result+=" lastPage=\""+form.getLastPage()+"\"";
        result+=" deleteWasCalled=\""+form.isDeleteActionWasCalled()+"\"";
        result+="/>";
        result+="</" + PAGINATION_TAG +">";
                // general information such as hidden message number and inbox message number
                result+="<" + INFORMATION_TAG + " total=\""+form.getAllmsg()+"\"";
                result+=" totalHidden=\""+form.getHiddenMsgCount()+"\""+"/>";

        result += "</" + ROOT_TAG + ">";
        return result;
    }

    /**
     * used to build forwarded or replied message xml
     */
    private String messages2XML(AmpMessage forwardedOrRepliedMessage, Long parentStateId,boolean isForwarded) throws AimException {

        String result = "";
        result += "<" ;
        if(isForwarded){
            result += "forwarded ";
        }else{
            result += "replied ";
        }
        result += "name=\"" + org.digijava.module.aim.util.DbUtil.filter(forwardedOrRepliedMessage.getName()) + "\" ";
        result += " msgId=\"" + forwardedOrRepliedMessage.getId() + "\"";
        if(forwardedOrRepliedMessage.getSenderType()!=null && forwardedOrRepliedMessage.getSenderType().equalsIgnoreCase(MessageConstants.SENDER_TYPE_USER)){
        result += " from=\"" +org.digijava.module.aim.util.DbUtil.filter(forwardedOrRepliedMessage.getSenderName())+ "\"";
        } else {
            result += " from=\"" + MessageConstants.SENDER_TYPE_SYSTEM + "\"";
        }
        result += " received=\"" + DateConversion.convertDateToString(forwardedOrRepliedMessage.getCreationDate()) + "\"";
        result += " to=\""
                + org.digijava.module.aim.util.DbUtil.filter(forwardedOrRepliedMessage.getAllMessageReceiversAsString())
                + "\"";
        result += " priority=\"" + forwardedOrRepliedMessage.getPriorityLevel() + "\"";
        //attachments start
        if(forwardedOrRepliedMessage.getAttachedDocs()!=null){
            Sdm attachedDocs=forwardedOrRepliedMessage.getAttachedDocs();
            DigiConfig config = DigiConfigManager.getConfig();
            String partialURL = config.getSiteDomain().getContent() ;
            String links="";
            for (SdmItem attachedFile : attachedDocs.getItems()) {              
                links += "<img src=\""+"/repository/message/view/images/attachment.png"+"\" border=\""+"0\" />";
                links += "<a  href=\"" + partialURL+"sdm/showFile.do~activeParagraphOrder="+attachedFile.getParagraphOrder()+"~documentId="+attachedDocs.getId()+"\" >";
                links += attachedFile.getContentTitle();
                links += "</a>";
                links+=",";                     
            }
            links=links.substring(0, links.length()-1);
            result += " attachments=\"" +org.digijava.module.aim.util.DbUtil.filter(links) + "\"";
        }
        result += " objURL=\"" + org.digijava.module.aim.util.DbUtil.filter(forwardedOrRepliedMessage.getObjectURL()) + "\"";
        String desc=org.digijava.module.aim.util.DbUtil.filter(forwardedOrRepliedMessage.getDescription());
        result += " msgDetails=\"" + desc + "\"";
        result+=" read=\""+true+"\"";
        result += " parentStateId=\"" + parentStateId + "\"";
        result += "/>";
        if (forwardedOrRepliedMessage.getForwardedMessage() != null) {
            AmpMessage forwarded = forwardedOrRepliedMessage.getForwardedMessage();
            result += messages2XML(forwarded,parentStateId,true);
        }else if(forwardedOrRepliedMessage.getRepliedMessage()!=null){
            AmpMessage replied = forwardedOrRepliedMessage.getRepliedMessage();
            result += messages2XML(replied,parentStateId,false);
        }
        return result;
    }

     /**
      *create helper Message class from AmpMessage entity
      */
     private MessageHelper createHelperMsgFromAmpMessage(AmpMessage msg,Long stateId) throws Exception{
         MessageHelper msgHelper=new MessageHelper(msg.getId(),msg.getName(),msg.getDescription());
         msgHelper.setFrom(AmpMessageUtil.getMessageState(stateId).getSender());
         msgHelper.setObjectURL(msg.getObjectURL());
         msgHelper.setAttachedDocs(msg.getAttachedDocs());      
         msgHelper.setReceivers(getMessageReceiversNames(msg));
//       if(msgHelper.getReceivers()==null){
//          msgHelper.setReceivers(new ArrayList<String>());
//          List<LabelValueBean> receivers=getMessageRecipients(msg);
//          for (LabelValueBean lvb : receivers) {
//              msgHelper.getReceivers().add(lvb.getLabel());
//          }
//          
//       }
         msgHelper.setCreationDate(DateConversion.convertDateToString(msg.getCreationDate()));
         return msgHelper;
     }
     
     private static String [] getMessageRecipients(AmpMessage message) throws Exception{
         Set<AmpMessageReceiver> msgReceivers = message.getMessageReceivers();
         Collection<String> selAtts = new ArrayList<String> ();
         String [] retVal = null;
         //team members
         if (msgReceivers != null && msgReceivers.size() > 0) {
                Collection<AmpTeam> teamList = new ArrayList<AmpTeam>();
                Collection<AmpTeamMember> memberList = new ArrayList<AmpTeamMember>();
                for (AmpMessageReceiver msgReceiver : msgReceivers) {
                    AmpTeamMember teamMember = msgReceiver.getReceiver();
                    AmpTeam team = teamMember.getAmpTeam();
                    if (!teamList.contains(team)) {
                        teamList.add(team);
                    }
                    memberList.add(teamMember);
                }               
                
                for(AmpTeam team : teamList){
                    for(AmpTeamMember member : memberList){
                        if(team.getAmpTeamId().longValue()==member.getAmpTeam().getAmpTeamId().longValue()){
                            selAtts.add("m:" + member.getAmpTeamMemId().toString());
                        }
                    }
                }               
                retVal = (String[]) selAtts.toArray(new String[selAtts.size()]);    
         }         
         
         return retVal;
     }


     /**
      * used to get message recipients, which will be shown on edit Message Page
      */
     private static List<String> getMessageReceiversNames(AmpMessage message) throws Exception{
        Set<AmpMessageReceiver> messageReceivers = message.getMessageReceivers();
        List<String> allReceivers=null;
        //team members
        if (messageReceivers != null && messageReceivers.size() > 0) {
            allReceivers=new ArrayList<String>();
            Collection<AmpTeam> teamList = new ArrayList<AmpTeam>();
            Collection<AmpTeamMember> memberList = new ArrayList<AmpTeamMember>();
            for (AmpMessageReceiver messageReceiver : messageReceivers) {
                AmpTeamMember teamMember = messageReceiver.getReceiver();
                AmpTeam team = teamMember.getAmpTeam();
                if (!teamList.contains(team)) {
                    teamList.add(team);
                }
                memberList.add(teamMember);
            }               
            
            for(AmpTeam team : teamList){
                allReceivers.add("---"+team.getName()+"---");
                for(AmpTeamMember member : memberList){                     
                    allReceivers.add(member.getUser().getFirstNames() + " " + member.getUser().getLastName());
                }
            }           
        }
        //contacts and external people
        if(message.getExternalReceivers()!=null){            
            String[] externalReceivers=message.getExternalReceivers().split(",");
            for (int i = 0; i < externalReceivers.length; i++) {
                allReceivers.add(externalReceivers[i]);
            }
        }       
        return allReceivers;
     }
}
