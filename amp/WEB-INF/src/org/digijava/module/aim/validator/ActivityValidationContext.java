package org.digijava.module.aim.validator;

import org.digijava.module.aim.dbentity.AmpActivityFields;

/**
 * @author Nadejda Mandrescu
 */
public class ActivityValidationContext {

    private static ThreadLocal<ActivityValidationContext> context = new ThreadLocal<>();

    private AmpActivityFields newActivity;
    private AmpActivityFields oldActivity;

    public AmpActivityFields getNewActivity() {
        return newActivity;
    }

    public void setNewActivity(AmpActivityFields newActivity) {
        this.newActivity = newActivity;
    }

    public AmpActivityFields getOldActivity() {
        return oldActivity;
    }

    public void setOldActivity(AmpActivityFields oldActivity) {
        this.oldActivity = oldActivity;
    }

    public static void set(ActivityValidationContext ctx) {
        context.set(ctx);
    }

    /**
     * @throws RuntimeException if activity validation context was not configured
     * @return activity validation context
     */
    public static ActivityValidationContext getOrThrow() {
        ActivityValidationContext ctx = context.get();
        if (ctx == null) {
            throw new RuntimeException("ActivityValidationContext not configured");
        }
        return ctx;
    }

}
