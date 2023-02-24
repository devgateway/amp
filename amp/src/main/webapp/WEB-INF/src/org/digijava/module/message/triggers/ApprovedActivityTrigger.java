package org.digijava.module.message.triggers;


import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;

import java.util.Date;
import org.digijava.module.message.helper.Event;

public class ApprovedActivityTrigger extends Trigger {

    public static final String PARAM_NAME="name";
    public static final String PARAM_SAVED_BY="savedBy";    
    public static final String PARAM_SAVE_DATE="saveDate";
    public static final String PARAM_URL="url";
    public static final String PARAM_ACTIVIY_CREATOR_TEAM="creator_team";
    public static final String PARAM_APPROVED_BY="approvedBy";
    
    private AmpTeamMember previouslyUpdatedBy=null;

    public static final String [] parameterNames=new String[]{PARAM_NAME,PARAM_SAVED_BY,PARAM_SAVE_DATE,PARAM_URL,PARAM_APPROVED_BY};

    public ApprovedActivityTrigger(Object source,AmpTeamMember previouslyUpdatedBy) {
        if(!(source instanceof AmpActivityVersion)) throw new RuntimeException("Incompatible object. Source must be an AmpActivity!");
        this.source=source;
        this.previouslyUpdatedBy=previouslyUpdatedBy;
        forwardEvent();
    }

    /** @see org.digijava.module.message.helper.Trigger#generateEvent(java.lang.Object)
     */
    @Override
    protected Event generateEvent() {
        Event e=new Event(ApprovedActivityTrigger.class);
        AmpActivityVersion act=(AmpActivityVersion) source;
        e.getParameters().put(PARAM_NAME,act.getName());
        if(this.previouslyUpdatedBy!=null){
            e.getParameters().put(PARAM_SAVED_BY,this.previouslyUpdatedBy);
           
        }else{
            e.getParameters().put(PARAM_SAVED_BY,act.getActivityCreator());
        } 
        if(act.getModifiedBy()==null){
            e.getParameters().put(PARAM_APPROVED_BY,act.getActivityCreator());
        }
        else{
            e.getParameters().put(PARAM_APPROVED_BY,act.getModifiedBy());
        }
        e.getParameters().put(PARAM_ACTIVIY_CREATOR_TEAM, act.getTeam().getAmpTeamId());
        e.getParameters().put(PARAM_SAVE_DATE, new Date());
        e.getParameters().put(PARAM_URL, "aim/viewActivityPreview.do~activityId=" + act.getAmpActivityId());
        
        return e;
    }

    /** @see org.digijava.module.message.helper.Trigger#getParameterNames()
     */
    @Override
    public String[] getParameterNames() {
        return parameterNames;
    }
}
