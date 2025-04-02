package org.digijava.module.message.dbentity;

/**
 * Calendar event Type
 * this is subclass of {@link AmpMessage} entity which defines User Message(message which is created by user) type messages in AMP.
 * @author Dare Roinishvili
 *
 */
public class UserMessage extends AmpMessage{

    /**
     * This method is used to define whether user should be able to edit message or not.    
     */
    public String getClassName() {
        return "u";
    }
}
