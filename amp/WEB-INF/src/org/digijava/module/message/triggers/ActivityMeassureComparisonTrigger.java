package org.digijava.module.message.triggers;

public class ActivityMeassureComparisonTrigger extends ActivityLevelNotificationTrigger{

    public ActivityMeassureComparisonTrigger(Object source) {
        super(source);
    }

    @Override
    protected Class getEventClass() {
        return ActivityMeassureComparisonTrigger.class;
    }

}
