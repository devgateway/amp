package org.digijava.module.message.dbentity;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import javax.persistence.*;

@Entity
@Table(name = "AMP_MESSAGE_RECEIVER")
public class AmpMessageReceiver {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_message_receiver_seq")
    @SequenceGenerator(name = "amp_message_receiver_seq", sequenceName = "AMP_MESSAGE_RECEIVER_seq", allocationSize = 1)
    @Column(name = "message_receiver_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private AmpTeamMember receiver;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private AmpMessage message;


    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpTeamMember getReceiver() {
        return receiver;
    }

    public void setReceiver(AmpTeamMember receiver) {
        this.receiver = receiver;
    }

    public AmpMessage getMessage() {
        return message;
    }

    public void setMessage(AmpMessage message) {
        this.message = message;
    }
    
    public String getNotificationUserAndEmail() {
        User u = receiver.getUser();
        
        return String.format("%s %s<%s>", u.getFirstNames(), u.getLastName(), u.getEmailUsedForNotification());
    }
    
    @Override
    public String toString() {
        User u =  receiver.getUser();
        AmpTeam t = receiver.getAmpTeam();
        
        return String.format("%s %s<%s>;%s;", u.getFirstNames(), u.getLastName(), 
                u.getEmailUsedForNotification(), t.getName());
    }

}
