package org.digijava.module.message.dbentity;

/**
 * Calendar event Type
 * this is subclass of {@link AmpMessage} entity which defines calendar event type messages in AMP.
 * @author Dare Roinishvili
 *
 */
public class CalendarEvent extends AmpMessage {
    /*
     * This property is used only when user creates new event and saves it.
     */
    String senderEmail;
    /**
     * This method is used to define whether user should be able to edit message or not.
     *  It Message is of SystemMessage type,that user shouldn't be able to edit it.
     */
    public String getClassName() {
        return "c";
    }
    public String getSenderEmail() {
        return senderEmail;
    }
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

}
