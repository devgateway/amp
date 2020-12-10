package org.dgfoundation.amp.onepager.util;

/**
 * @author Octavian Ciubotaru
 */
public final class SaveContext {

    private static final SaveContext JOB_CONTEXT = new SaveContext(ActivitySource.JOB, false, null, true);
    private static final SaveContext PATCH_CONTEXT = new SaveContext(ActivitySource.PATCH, false, null, true);

    private final ActivitySource source;
    private final boolean updateActivityStatus;
    private final Boolean rejected;
    private final boolean prepareToSave;

    public static SaveContext api(boolean updateApprovalStatus) {
        if (updateApprovalStatus) {
            return new SaveContext(ActivitySource.API, true, false, false);
        } else {
            return new SaveContext(ActivitySource.API, false, null, false);
        }
    }

    public static SaveContext activityForm(boolean rejected) {
        return new SaveContext(ActivitySource.ACTIVITY_FORM, true, rejected, true);
    }

    public static SaveContext job() {
        return JOB_CONTEXT;
    }

    public static SaveContext patch() {
        return PATCH_CONTEXT;
    }

    private SaveContext(ActivitySource source, boolean updateActivityStatus, Boolean rejected, boolean prepareToSave) {
        this.source = source;
        this.updateActivityStatus = updateActivityStatus;
        this.rejected = rejected;
        this.prepareToSave = prepareToSave;
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
}
