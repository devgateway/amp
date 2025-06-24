package org.digijava.module.message.dbentity;


public class TemplateAlert extends AmpMessage {
    
    private String relatedTriggerName;
    
    /**
     * This method is used to define whether user should be able to edit message or not.    
     */
    public String getClassName() {
        return "t";
    }

    public String getRelatedTriggerName() {
        return relatedTriggerName;
    }

    public void setRelatedTriggerName(String relatedTriggerName) {
        this.relatedTriggerName = relatedTriggerName;
    }
    
    

}
