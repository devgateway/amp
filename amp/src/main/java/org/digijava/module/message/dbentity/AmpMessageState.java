package org.digijava.module.message.dbentity;

import org.digijava.module.aim.dbentity.AmpTeamMember;

import java.io.Serializable;

public class AmpMessageState implements Serializable{
    
    private Long id;
    
    /**
     * which message's state is current state
     */ 
    private AmpMessage message;
    
    
    /**
     * team member Id
     */
    //private Long memberId;
    
    private AmpTeamMember receiver;
    
    /**
     * is message already read
     */
    private Boolean read;
    /**
     * holds the name of sender
     */
    private String sender;
    
    /**
     * this field is used to see sent messages
     */
    private Long senderId;
    /**
     * defines whether a message should appear in inbox or not. if the inbox is full, message should be hidden
     */
    private Boolean messageHidden;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpMessage getMessage() {
        return message;
    }

    public void setMessage(AmpMessage message) {
        this.message = message;
    }

//  public Long getMemberId() {
//      return memberId;
//  }
//
//  public void setMemberId(Long memberId) {
//      this.memberId = memberId;
//  }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getMessageHidden() {
        return messageHidden;
    }

    public void setMessageHidden(Boolean messageHidden) {
        this.messageHidden = messageHidden;
    }

    public AmpTeamMember getReceiver() {
        return receiver;
    }

    public void setReceiver(AmpTeamMember receiver) {
        this.receiver = receiver;
    }
}
