package org.dgfoundation.amp.onepager.util;

/**
 * @author Octavian Ciubotaru
 */
public final class SaveContext {

    private static final SaveContext JOB_CONTEXT = new SaveContext(ActivitySource.JOB, false, null);

    private final ActivitySource source;
    private final boolean updateActivityStatus;
    private final Boolean rejected;

    public static SaveContext api(boolean updateApprovalStatus) {
        if (updateApprovalStatus) {
            return new SaveContext(ActivitySource.API, true, false);
        } else {
            return new SaveContext(ActivitySource.API, false, null);
        }
    }

    public static SaveContext activityForm(boolean rejected) {
        return new SaveContext(ActivitySource.ACTIVITY_FORM, true, rejected);
    }

    public static SaveContext job() {
        return JOB_CONTEXT;
    }

    private SaveContext(ActivitySource source, boolean updateActivityStatus, Boolean rejected) {
        this.source = source;
        this.updateActivityStatus = updateActivityStatus;
        this.rejected = rejected;
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
}
