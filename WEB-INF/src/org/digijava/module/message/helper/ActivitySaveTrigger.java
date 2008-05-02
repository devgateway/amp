/**
 * ActivitySaveTrigger.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.message.helper;

import org.digijava.module.aim.dbentity.AmpActivity;

/**
 * ActivitySaveTrigger.java
 * TODO description here
 * @author mihai
 * @package org.digijava.module.message.helper
 * @since 02.05.2008
 */
public class ActivitySaveTrigger extends Trigger {

    public static final String PARAM_NAME="name";
    public static final String PARAM_CREATED_DATE="createdDate";
    public static final String PARAM_CREATED_BY="createdBy";
    public static final String PARAM_URL="url";
    
    public static final String [] parameterNames=new String[]{PARAM_NAME,PARAM_CREATED_DATE,PARAM_CREATED_BY,PARAM_URL};
    
    public ActivitySaveTrigger(Object source) {
	if(! (source instanceof AmpActivity)) throw new RuntimeException("Incompatible object. Source must be an AmpActivity!"); 
	this.source=source;
	forwardEvent();
    }

    /** @see org.digijava.module.message.helper.Trigger#generateEvent(java.lang.Object)
     */
    @Override
    protected Event generateEvent() {
	Event e=new Event(ActivitySaveTrigger.class);
	AmpActivity act=(AmpActivity) source;
	e.getParameters().put(PARAM_NAME,act.getName());
	e.getParameters().put(PARAM_CREATED_DATE, act.getCreatedDate());
	e.getParameters().put(PARAM_CREATED_BY, act.getCreatedBy());
	e.getParameters().put(PARAM_URL, "http://....?ampActivityId="+act.getAmpActivityId());
	return e;
    }

    /** @see org.digijava.module.message.helper.Trigger#getParameterNames()
     */
    @Override
    public String[] getParameterNames() {
	return parameterNames;
    }

}
