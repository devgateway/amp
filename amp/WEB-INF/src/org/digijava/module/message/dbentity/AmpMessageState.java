package org.digijava.module.message.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import javax.persistence.*;

@Entity
@Table(name = "AMP_MESSAGE_STATE")
public class AmpMessageState implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_message_state_seq")
    @SequenceGenerator(name = "amp_message_state_seq", sequenceName = "AMP_MESSAGE_STATE_seq", allocationSize = 1)
    @Column(name = "message_state_Id")
    private Long id;

    @Column(name = "sender")
    private String sender;

    @Column(name = "sender_Id")
    private Long senderId;

    @Column(name = "is_read")
    private Boolean read;

    @Column(name = "is_message_hidden")
    private Boolean messageHidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private AmpMessage message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "amp_team_mem_id")
    private AmpTeamMember receiver;


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
