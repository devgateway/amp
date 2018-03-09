package org.digijava.module.message.form;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.helper.Team;
import org.digijava.module.message.dbentity.AmpMessage;
import org.digijava.module.message.dbentity.AmpMessageState;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.helper.MessageHelper;
import org.digijava.module.message.helper.ReciverName;
import org.digijava.module.sdm.dbentity.Sdm;

public class AmpMessageForm extends ActionForm {
    /**
     * serial version id
     */
    private static final long serialVersionUID = 1L;
    
    private Long messageId;
    private Long msgStateId;
    private String messageName;
    private Long priorityLevel;
    private Long messageType;
    private String senderType; //activity , User e.t.c.
    private Long senderId;
    private String sender;
    private String creationDate;
    private boolean editingMessage=false;
    private String description; 
    private String[] receiversIds; //here come Ids from the receivers dropdown,like t:teamId or m:memberId
    private List<LabelValueBean> receivers;
    private Long receiverId; //I think this should be deleted too
    private int tabIndex=0;     //which tab we are viewing(messages tab, alerts, approvals ...)
    private String childTab; //child tab on tabIndex. used to separate received messages from sent of draft 
    private MessageHelper forwardedMsg;
    private MessageHelper repliedMsg;
    private String objectURL;
    private int allmsg;
    private List<ReciverName> receivesrsNameMail;
    private List<ReciverName> receivesrsTheamName;
    //holds attached files  
    private Sdm sdmDocument;
    private int attachmentsSize;
    
    private int setAsAlert;
    private boolean deleteActionWasCalled;
    
    private String[] relatedActivities;
    private String selectedAct;
    
    private String [] allPages;

  
    private String page;
    private String lastPage;
    private int pagesToShow;
    private int offset;
    private String removeStateIds;
        
    private int hiddenMsgCount;

    private String[] contacts;    
    
    /**
     * used to separate different kinds of messages
     */
    
    private int msgType=0;                              //holds amount of messages
    private int approvalType=0;                         //holds amount of approvals
    private int calendarEventType=0;                    //holds amount of calendar events
    private int alertType=0;                            //holds amount of alerts
    
    private boolean inboxFull=false;
    private boolean addAtTop=false;
    
    
    private Map<String,Team> teamsMap;  
    private List<AmpMessageState> messagesForTm;
    private List<AmpMessageState> pagedMessagesForTm; //used for pagination
    private String sortBy;
    
    
    private String className;
    
    /**
     * Fields for Admin Side(Message Settings) 
     */
    private Long msgRefreshTimeCurr; //current value
    private String msgRefreshTimeNew;  //new value
    
    private Long msgStoragePerMsgTypeCurr;
    private String msgStoragePerMsgTypeNew;
    
    private Long daysForAdvanceAlertsWarningsCurr;
    private String daysForAdvanceAlertsWarningsNew;
    
    private Long emailMsgsCurrent;
    private Long emailMsgsNew;
    
    private Long templateId;
    private List<TemplateAlert> templates;
    private List<LabelValueBean> availableTriggersList;
    private String selectedTrigger;
    
    
    private FormFile fileUploaded;

    AmpMessage repliedMessage;
    AmpMessage forwardedMessage;

    public AmpMessage getForwardedMessage() {
        return forwardedMessage;
    }

    public void setForwardedMessage(AmpMessage forwardedMessage) {
        this.forwardedMessage = forwardedMessage;
    }

    public AmpMessage getRepliedMessage() {
        return repliedMessage;
    }

    public void setRepliedMessage(AmpMessage repliedMessage) {
        this.repliedMessage = repliedMessage;
    }

    public List<TemplateAlert> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateAlert> templates) {
        this.templates = templates;
    }

    public Long getMsgRefreshTimeCurr() {
        return msgRefreshTimeCurr;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public List<LabelValueBean> getAvailableTriggersList() {
        return availableTriggersList;
    }

    public void setAvailableTriggersList(List<LabelValueBean> availableTriggersList) {
        this.availableTriggersList = availableTriggersList;
    }

    public String getSelectedTrigger() {
        return selectedTrigger;
    }

    public void setSelectedTrigger(String selectedTrigger) {
        this.selectedTrigger = selectedTrigger;
    }

    public void setMsgRefreshTimeCurr(Long msgRefreshTimeCurr) {
        this.msgRefreshTimeCurr = msgRefreshTimeCurr;
    }

    public String getMsgRefreshTimeNew() {
        return msgRefreshTimeNew;
    }

    public void setMsgRefreshTimeNew(String msgRefreshTimeNew) {
        this.msgRefreshTimeNew = msgRefreshTimeNew;
    }

    public Long getMsgStoragePerMsgTypeCurr() {
        return msgStoragePerMsgTypeCurr;
    }

    public void setMsgStoragePerMsgTypeCurr(Long msgStoragePerMsgTypeCurr) {
        this.msgStoragePerMsgTypeCurr = msgStoragePerMsgTypeCurr;
    }

    public String getMsgStoragePerMsgTypeNew() {
        return msgStoragePerMsgTypeNew;
    }

    public void setMsgStoragePerMsgTypeNew(String msgStoragePerMsgTypeNew) {
        this.msgStoragePerMsgTypeNew = msgStoragePerMsgTypeNew;
    }

    public Long getDaysForAdvanceAlertsWarningsCurr() {
        return daysForAdvanceAlertsWarningsCurr;
    }

    public void setDaysForAdvanceAlertsWarningsCurr(
            Long daysForAdvanceAlertsWarningsCurr) {
        this.daysForAdvanceAlertsWarningsCurr = daysForAdvanceAlertsWarningsCurr;
    }

    public String getDaysForAdvanceAlertsWarningsNew() {
        return daysForAdvanceAlertsWarningsNew;
    }

    public void setDaysForAdvanceAlertsWarningsNew(String daysForAdvanceAlertsWarningsNew) {
        this.daysForAdvanceAlertsWarningsNew = daysForAdvanceAlertsWarningsNew;
    }

    public Long getEmailMsgsCurrent() {
        return emailMsgsCurrent;
    }

    public void setEmailMsgsCurrent(Long emailMsgsCurrent) {
        this.emailMsgsCurrent = emailMsgsCurrent;
    }

    public Long getEmailMsgsNew() {
        return emailMsgsNew;
    }

    public void setEmailMsgsNew(Long emailMsgsNew) {
        this.emailMsgsNew = emailMsgsNew;
    }

    public String getClassName() {
        return className;
    }
    
    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messagetId) {
        this.messageId = messagetId;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }
    
    public Long getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Long priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Long getMessageType() {
        return messageType;
    }

    public void setMessageType(Long messageType) {
        this.messageType = messageType;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isEditingMessage() {
        return editingMessage;
    }

    public void setEditingMessage(boolean editingMessage) {
        this.editingMessage = editingMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

    public String[] getReceiversIds() {
        return receiversIds;
    }

    public void setReceiversIds(String[] receiversIds) {
        this.receiversIds = receiversIds;
    }
    
    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Map<String, Team> getTeamsMap() {
        return teamsMap;
    }

    public void setTeamsMap(Map<String, Team> teamsMap) {
        this.teamsMap = teamsMap;
    }   
    
    public Collection<Team> getTeamMapValues(){
        return (Collection<Team>)teamsMap.values();
    }

    public List<LabelValueBean> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<LabelValueBean> receivers) {
        this.receivers = receivers;
    }

    public List<AmpMessageState> getMessagesForTm() {
        return messagesForTm;
    }

    public void setMessagesForTm(List<AmpMessageState> messagesForTm) {
        this.messagesForTm = messagesForTm;
    }

    public Long getMsgStateId() {
        return msgStateId;
    }

    public void setMsgStateId(Long msgStateId) {
        this.msgStateId = msgStateId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(int approvalType) {
        this.approvalType = approvalType;
    }

    public int getCalendarEventType() {
        return calendarEventType;
    }

    public void setCalendarEventType(int calendarEventType) {
        this.calendarEventType = calendarEventType;
    }

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public String getChildTab() {
        return childTab;
    }

    public void setChildTab(String childTab) {
        this.childTab = childTab;
    }

    public int getSetAsAlert() {
        return setAsAlert;
    }

    public void setSetAsAlert(int setAsAlert) {
        this.setAsAlert = setAsAlert;
    }

    public MessageHelper getForwardedMsg() {
        return forwardedMsg;
    }

    public void setForwardedMsg(MessageHelper forwardedMsg) {
        this.forwardedMsg = forwardedMsg;
    }

    public String[] getAllPages() {
        return allPages;
    }

    public void setAllPages(String[] allPages) {
        this.allPages = allPages;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<AmpMessageState> getPagedMessagesForTm() {
        return pagedMessagesForTm;
    }

    public void setPagedMessagesForTm(List<AmpMessageState> pagedMessagesForTm) {
        this.pagedMessagesForTm = pagedMessagesForTm;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public int getPagesToShow() {
        return pagesToShow;
    }

    public void setPagesToShow(int pagesToShow) {
        this.pagesToShow = pagesToShow;
    }

    public int getOffset() {        
        if (Integer.parseInt(getPage())> (this.getPagesToShow()/2)){
            setOffset((Integer.parseInt(getPage()) - (this.getPagesToShow()/2))-1);     
        }
        else {
            setOffset(0);
        }   
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isDeleteActionWasCalled() {
        return deleteActionWasCalled;
    }

    public void setDeleteActionWasCalled(boolean deleteActionWasCalled) {
        this.deleteActionWasCalled = deleteActionWasCalled;
    }

        public String getObjectURL() {
            return objectURL;
        }

        public void setObjectURL(String objectURL) {
            this.objectURL = objectURL;
        }

        public String[] getRelatedActivities() {
            return relatedActivities;
        }

        public void setRelatedActivities(String[] relatedActivities) {
            this.relatedActivities = relatedActivities;
        }

        public String getSelectedAct() {
            return selectedAct;
        }

        public void setSelectedAct(String selectedAct) {
            this.selectedAct = selectedAct;
        }

        public boolean isInboxFull() {
            return inboxFull;
        }

        public void setInboxFull(boolean inboxFull) {
            this.inboxFull = inboxFull;
        }

        public int getAllmsg() {
            return allmsg;
        }

        public void setAllmsg(int allmsg) {
            this.allmsg = allmsg;
        }

        public boolean isAddAtTop() {
            return addAtTop;
        }

        public void setAddAtTop(boolean addAtTop) {
            this.addAtTop = addAtTop;
        }

        public List<ReciverName> getReceivesrsNameMail() {
            return receivesrsNameMail;
        }

        public void setReceivesrsNameMail(List<ReciverName> receivesrsNameMail) {
            this.receivesrsNameMail = receivesrsNameMail;
        }

        public List getReceivesrsTheamName() {
            return receivesrsTheamName;
        }

        public void setReceivesrsTheamName(List<ReciverName> receivesrsTheamName) {
            this.receivesrsTheamName = receivesrsTheamName;
        }

        public Sdm getSdmDocument() {
            return sdmDocument;
        }

        public void setSdmDocument(Sdm sdmDocument) {
            this.sdmDocument = sdmDocument;
        }

        public int getAttachmentsSize() {
            return attachmentsSize;
        }

        public void setAttachmentsSize(int attachmentsSize) {
            this.attachmentsSize = attachmentsSize;
        }

        public String getRemoveStateIds() {
            return removeStateIds;
        }

        public void setRemoveStateIds(String removeStateIds) {
            this.removeStateIds = removeStateIds;
        }

        public int getHiddenMsgCount() {
            return hiddenMsgCount;
        }

        public void setHiddenMsgCount(int hiddenMsgCount) {
            this.hiddenMsgCount = hiddenMsgCount;
        }

        public String[] getContacts() {
            return contacts;
        }

        public void setContacts(String[] contacts) {
            this.contacts = contacts;
        }

        public FormFile getFileUploaded() {
            return fileUploaded;
        }

        public void setFileUploaded(FormFile fileUploaded) {
            this.fileUploaded = fileUploaded;
        }

        public MessageHelper getRepliedMsg() {
            return repliedMsg;
        }

        public void setRepliedMsg(MessageHelper repliedMsg) {
            this.repliedMsg = repliedMsg;
        }
        
                public String getSortBy() {
                    return sortBy;
                }

                public void setSortBy(String sortBy) {
                    this.sortBy = sortBy;
                }
     
    }
