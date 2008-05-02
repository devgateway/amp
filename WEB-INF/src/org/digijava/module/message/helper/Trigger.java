/**
 * Trigger.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.message.helper;


public abstract class Trigger {
    
    protected Object source;
    
    protected abstract Event generateEvent();
    
    public abstract String[] getParameterNames();
   
    
    protected void forwardEvent() {
	Event e=generateEvent();
	AmpMessageWorker.processEvent(e);
    }
    
    
    
}
