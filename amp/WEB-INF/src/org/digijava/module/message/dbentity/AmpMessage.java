package org.digijava.module.message.dbentity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.sdm.dbentity.Sdm;

/**
 * General AMP Message.
 * @author Dare Roinishvili
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_MESSAGE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "message_clazz", discriminatorType = DiscriminatorType.STRING)
public abstract class AmpMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_message_seq")
    @SequenceGenerator(name = "amp_message_seq", sequenceName = "AMP_MESSAGE_seq", allocationSize = 1)
    @Column(name = "amp_message_Id")
    private Long id;

    @Column(name = "name", columnDefinition = "text")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "priority_level")
    private Long priorityLevel;

    @Column(name = "message_type")
    private Long messageType;

    @Column(name = "sender_type")
    private String senderType;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "is_emailable")
    private Boolean emailable;

    @Column(name = "is_draft")
    private Boolean draft;

    @ManyToOne
    @JoinColumn(name = "forwarded_message_Id")
    private AmpMessage forwardedMessage;

    @ManyToOne
    @JoinColumn(name = "replied_message_Id")
    private AmpMessage repliedMessage;

    @ManyToOne
    @JoinColumn(name = "attached_doc_id", unique = true)
    private Sdm attachedDocs;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpMessageReceiver> messageReceivers;

    @Column(name = "external_receivers", columnDefinition = "text")
    private String externalReceivers;

    @Column(name = "sender_name", columnDefinition = "text")
    private String senderName;

    @Column(name = "object_URL")
    private String objectURL;

    @Column(name = "related_act_id")
    private Long relatedActivityId;


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
