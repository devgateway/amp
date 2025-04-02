package org.digijava.module.message.dbentity;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;

public class AmpMessageReceiver {

    private Long id;
    private AmpTeamMember receiver;
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
