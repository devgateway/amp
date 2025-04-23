package org.digijava.module.message.triggers;

import org.dgfoundation.amp.onepager.util.ActivityGatekeeper;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.message.helper.Event;
import org.digijava.module.message.helper.MessageConstants;
/**
 * Trigger to notify when an activity is submitted for validation
 * @author jdeanquin
 *
 */
public abstract class ActivityLevelNotificationTrigger extends Trigger {
    public static final String PARAM_NAME="name";
    public static final String PARAM_TRIGGER_SENDER="sender";
    public static final String PARAM_URL="activity url";
    public static final String PARAM_ACTIVITY_ID="activity_id";
    public static final String PARAM_AMP_ID="amp_id";
    private static final String [] parameterNames=new String[]{PARAM_NAME,PARAM_TRIGGER_SENDER,PARAM_URL,PARAM_ACTIVITY_ID,PARAM_AMP_ID};

    public ActivityLevelNotificationTrigger(Object source) {
        if(! (source instanceof AmpActivityVersion)) throw new RuntimeException("Incompatible object. Source must be a ! " + AmpActivity.class);
        this.source=source;
        forwardEvent();
    }
    protected abstract Class getEventClass();
    @Override
    protected Event generateEvent() {
        Event e=new Event(getEventClass());
        AmpActivityVersion activity=(AmpActivityVersion) source;
        e.getParameters().put(PARAM_NAME,activity.getName());
        e.getParameters().put(PARAM_TRIGGER_SENDER,MessageConstants.SENDER_TYPE_SYSTEM);
        
        e.getParameters().put(PARAM_URL, ActivityGatekeeper.buildPreviewUrl(activity.getAmpActivityId().toString()));
        e.getParameters().put(PARAM_ACTIVITY_ID, activity.getAmpActivityId());
        e.getParameters().put(PARAM_AMP_ID, activity.getAmpId());
        
        return e;

    }

    @Override
    public String[] getParameterNames() {

        return parameterNames;
    }

}
