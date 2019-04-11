package org.digijava.module.aim.validator;

import org.digijava.module.aim.dbentity.AmpActivityFields;

/**
 * @author Nadejda Mandrescu
 */
public class ActivityValidationContext {

    private static ThreadLocal<ActivityValidationContext> CONTEXT = new ThreadLocal<>();

    private AmpActivityFields oldActivity;

    public AmpActivityFields getOldActivity() {
        return oldActivity;
    }

    public void setOldActivity(AmpActivityFields oldActivity) {
        this.oldActivity = oldActivity;
    }

    public static void set(ActivityValidationContext context) {
        CONTEXT.set(context);
    }

    /**
     * @throws RuntimeException if activity validation context was not configured
     * @return activity validation context
     */
    public static ActivityValidationContext getOrThrow() {
        ActivityValidationContext context = CONTEXT.get();
        if (context == null) {
            throw new RuntimeException("ActivityValidationContext not configured");
        }
        return context;
    }

}
