package org.digijava.module.message.triggers;

import java.util.Date;

import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.message.helper.Event;

public class NotApprovedActivityTrigger extends Trigger {

    public static final String PARAM_NAME="name";
    public static final String PARAM_SAVED_BY="savedBy";
    public static final String PARAM_SAVE_DATE="saveDate";
    public static final String PARAM_URL="url";
    public static final String PARAM_ACTIVIY_CREATOR_TEAM="creator_team";
    public static final String PARAM_ACTIVIY_ID="activity_id";

    public static final String [] parameterNames=new String[]{PARAM_NAME,PARAM_SAVED_BY,PARAM_SAVE_DATE,PARAM_URL};

    public NotApprovedActivityTrigger(Object source) {
        if(!(source instanceof AmpActivityVersion)) throw new RuntimeException("Incompatible object. Source must be an AmpActivity!");
        this.source=source;
        forwardEvent();
    }

    /** @see org.digijava.module.message.helper.Trigger#generateEvent(java.lang.Object)
     */
    @Override
    protected Event generateEvent() {
        Event e=new Event(NotApprovedActivityTrigger.class);
        AmpActivityVersion act=(AmpActivityVersion) source;
        e.getParameters().put(PARAM_NAME,act.getName());
        if(act.getModifiedBy()!=null){
            e.getParameters().put(PARAM_SAVED_BY,act.getModifiedBy());
        }else{
            e.getParameters().put(PARAM_SAVED_BY,act.getActivityCreator());
        }
        e.getParameters().put(PARAM_ACTIVIY_CREATOR_TEAM, act.getTeam().getAmpTeamId());
        e.getParameters().put(PARAM_SAVE_DATE, new Date());
        e.getParameters().put(PARAM_URL, "aim/viewActivityPreview.do~activityId=" + act.getAmpActivityId());
        e.getParameters().put(PARAM_ACTIVIY_ID,act.getAmpActivityId());
    return e;
    }

    /** @see org.digijava.module.message.helper.Trigger#getParameterNames()
     */
    @Override
    public String[] getParameterNames() {
        return parameterNames;
    }
}
