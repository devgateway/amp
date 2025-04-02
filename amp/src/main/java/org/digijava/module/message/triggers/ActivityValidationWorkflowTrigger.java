package org.digijava.module.message.triggers;

public class ActivityValidationWorkflowTrigger extends ActivityLevelNotificationTrigger {

    public ActivityValidationWorkflowTrigger(Object source) {
        super(source);
    }

    @Override
    protected Class<ActivityValidationWorkflowTrigger> getEventClass() {
        return ActivityValidationWorkflowTrigger.class;
    }

}
