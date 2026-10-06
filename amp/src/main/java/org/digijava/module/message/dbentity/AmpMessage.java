package org.digijava.module.message.dbentity;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.sdm.dbentity.Sdm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * General AMP Message.
 * @author Dare Roinishvili
 *
 */
public abstract class AmpMessage {
    private Long id;
    
    /**
     * name or subject of message.
     */
    private String name;
    private Long priorityLevel; //low, high e.t.c. 
    private Long messageType; //alert,approvals,system message e.t.c.
    private String senderType;  
    private Long senderId;  //if user sends alert, then it's that user's id... vtqvat user daregistrirda,anu User manager agzavnis da romeli useric daregistrirda imis,id iqneba
    private Date creationDate; //date when it was created
    private String objectURL;
    private Long relatedActivityId;    
    
    private String senderName;  //sender name

    /**
     * emails should be sent.
     */
    private Boolean emailable;
    
    private String description;
    
    /**
     * defines whether message is saved(==it's a draft) or sent
     */
    private Boolean draft;
    
    /**
     * this field holds Id of the forwarded message, if it exists 
     */
    private AmpMessage forwardedMessage;
    
    /**
     * holds replied message , if it exists
     */
    private AmpMessage repliedMessage;
        
    private String externalReceivers; //contacts + people outside AMP
        
    private Sdm attachedDocs; //for attaching files
    
    private Set<AmpMessageReceiver> messageReceivers;

    public Set<AmpMessageReceiver> getMessageReceivers() {
        return messageReceivers;
    }

    public void setMessageReceivers(Set<AmpMessageReceiver> messageReceivers) {
        this.messageReceivers = messageReceivers;
    }

    /**
     * This method is used to define whether user should be able to edit message or not.
     * It Message is of SystemMessage type,that user shouldn't be able to edit it.
     * 
     */
    public String getClassName(){
        return "m";
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Boolean getEmailable() {
        return emailable;
    }
    public void setEmailable(Boolean emailable) {
        this.emailable = emailable;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public AmpMessage getForwardedMessage() {
        return forwardedMessage;
    }

    public void setForwardedMessage(AmpMessage forwardedMessage) {
        this.forwardedMessage = forwardedMessage;
    }

    public String getObjectURL() {
        return objectURL;
    }

    public void setObjectURL(String objectURL) {
        this.objectURL = objectURL;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getRelatedActivityId() {
        return relatedActivityId;
    }

    public void setRelatedActivityId(Long relatedActivityId) {
        this.relatedActivityId = relatedActivityId;
    }

    public String getExternalReceivers() {
        return externalReceivers;
    }

    public void setExternalReceivers(String externalReceivers) {
        this.externalReceivers = externalReceivers;
    }

    public Sdm getAttachedDocs() {
        return attachedDocs;
    }

    public void setAttachedDocs(Sdm attachedDocs) {
        this.attachedDocs = attachedDocs;
    }

    public AmpMessage getRepliedMessage() {
        return repliedMessage;
    }

    public void setRepliedMessage(AmpMessage repliedMessage) {
        this.repliedMessage = repliedMessage;
    }

    public void addMessageReceiver(AmpTeamMember receiver) {
        AmpMessageReceiver msgReceiver = new AmpMessageReceiver();
        msgReceiver.setReceiver(receiver);
        msgReceiver.setMessage(this);
        
        if (messageReceivers == null) {
            messageReceivers = new HashSet<>();
        }
        
        messageReceivers.add(msgReceiver);
    }
    
    public void copyMessageReceiversFromTemplate(AmpMessage template) {
        for (AmpMessageReceiver msgReceiver : template.getMessageReceivers()) {
            addMessageReceiver(msgReceiver.getReceiver());
        }
    }
    
    public String getAllMessageReceiversAsString() {
        String msgReceivers = messageReceivers.stream()
                .map(x -> x.toString())
                .collect(Collectors.joining(", "));
        
        return msgReceivers;
    }
}
