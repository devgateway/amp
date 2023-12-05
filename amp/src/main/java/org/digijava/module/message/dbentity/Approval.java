package org.digijava.module.message.dbentity;

public class Approval extends AmpMessage {
    
    /**
     * This method is used to define whether user should be able to edit message or not.
     *  It Message is of Approval type,that user shouldn't be able to edit it.
     */
    public String getClassName() {
        return "app";
    }
}
