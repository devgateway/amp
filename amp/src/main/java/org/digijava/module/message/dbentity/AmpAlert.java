package org.digijava.module.message.dbentity;

/**
 * Alert message.
 * This is subclass of {@link AmpMessage} entity which defines alert type messages in AMP.
 * @author Dare Roinishvili
 *
 */
public class AmpAlert extends AmpMessage{

    private String alertTriggerClass;

    /**
     * This method is used to define whether user should be able to edit message or not.
     *  It Message is of SystemMessage type,that user shouldn't be able to edit it.
     */
    public String getClassName() {
        return "a";
    }

    public String getAlertTriggerClass() {
        return alertTriggerClass;
    }

    public void setAlertTriggerClass(String alertTriggerClass) {
        this.alertTriggerClass = alertTriggerClass;
    }
}
