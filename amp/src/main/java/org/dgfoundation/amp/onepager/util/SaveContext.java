package org.dgfoundation.amp.onepager.util;

/**
 * @author Octavian Ciubotaru
 */
public final class SaveContext {

    private static final SaveContext JOB_CONTEXT = new SaveContext(ActivitySource.JOB, false, null, true, true);
    private static final SaveContext PATCH_CONTEXT = new SaveContext(ActivitySource.PATCH, false, null, true, true);
    private static final SaveContext ADMIN_CONTEXT = new SaveContext(ActivitySource.ADMIN, false, null, true, true);
    private final ActivitySource source;
    private final boolean updateActivityStatus;
    private final Boolean rejected;
    private final boolean prepareToSave;
    private final boolean closeProjectOnTruBudget;

    public static SaveContext api(boolean updateApprovalStatus) {
        if (updateApprovalStatus) {
            return new SaveContext(ActivitySource.API, true, false, false, true);
        } else {
            return new SaveContext(ActivitySource.API, false, null, false, true);
        }
    }

    public static SaveContext activityForm(boolean rejected) {
        return activityForm(rejected, true);
    }

    public static SaveContext activityForm(boolean rejected, boolean closeProjectOnTruBudget) {
        return new SaveContext(ActivitySource.ACTIVITY_FORM, true, rejected, true, closeProjectOnTruBudget);
    }

    public static SaveContext job() {
        return JOB_CONTEXT;
    }

    public static SaveContext patch() {
        return PATCH_CONTEXT;
    }

    public static SaveContext admin() {
        return ADMIN_CONTEXT;
    }

    private SaveContext(ActivitySource source, boolean updateActivityStatus, Boolean rejected, boolean prepareToSave,
                        boolean closeProjectOnTruBudget) {
        this.source = source;
        this.updateActivityStatus = updateActivityStatus;
        this.rejected = rejected;
        this.prepareToSave = prepareToSave;
        this.closeProjectOnTruBudget = closeProjectOnTruBudget;
    }

    public ActivitySource getSource() {
        return source;
    }

    public boolean isUpdateActivityStatus() {
        return updateActivityStatus;
    }

    public boolean isRejected() {
        return rejected;
    }

    public boolean isPrepareToSave() {
        return prepareToSave;
    }

    public boolean isCloseProjectOnTruBudget() {
        return closeProjectOnTruBudget;
    }
}
