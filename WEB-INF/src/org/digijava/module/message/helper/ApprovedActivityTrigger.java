package org.digijava.module.message.helper;

import org.digijava.module.aim.dbentity.AmpActivity;
import java.util.Date;

public class ApprovedActivityTrigger extends Trigger {

    public static final String PARAM_NAME="name";
    public static final String PARAM_SAVED_BY="savedBy";
    public static final String PARAM_SAVE_DATE="saveDate";
    public static final String PARAM_URL="url";

    public static final String [] parameterNames=new String[]{PARAM_NAME,PARAM_SAVED_BY,PARAM_SAVE_DATE,PARAM_URL};

    public ApprovedActivityTrigger(Object source) {
        if(!(source instanceof AmpActivity)) throw new RuntimeException("Incompatible object. Source must be an AmpActivity!");
        this.source=source;
        forwardEvent();
    }

    /** @see org.digijava.module.message.helper.Trigger#generateEvent(java.lang.Object)
     */
    @Override
    protected Event generateEvent() {
        Event e=new Event(ApprovedActivityTrigger.class);
        AmpActivity act=(AmpActivity) source;
        e.getParameters().put(PARAM_NAME,act.getName());
        if(act.getUpdatedBy()!=null){
            e.getParameters().put(PARAM_SAVED_BY,act.getUpdatedBy());
        }else{
            e.getParameters().put(PARAM_SAVED_BY,act.getActivityCreator());
        }

        e.getParameters().put(PARAM_SAVE_DATE, new Date());
        e.getParameters().put(PARAM_URL,"aim/selectActivityTabs.do~ampActivityId="+act.getAmpActivityId());
    return e;
    }

    /** @see org.digijava.module.message.helper.Trigger#getParameterNames()
     */
    @Override
    public String[] getParameterNames() {
        return parameterNames;
    }
}
