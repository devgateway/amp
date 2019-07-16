package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * @author Nadejda Mandrescu
 */
public class ActivityImportRules {

    private boolean canDowngradeToDraft;

    private boolean isProcessApprovalFields;

    private boolean isTrackEditors;

    public ActivityImportRules(boolean canDowngradeToDraft, boolean isProcessApprovalFields, boolean isTrackEditors) {
        this.canDowngradeToDraft = canDowngradeToDraft;
        this.isProcessApprovalFields = isProcessApprovalFields;
        this.isTrackEditors = isTrackEditors;
    }

    public boolean isCanDowngradeToDraft() {
        return canDowngradeToDraft;
    }

    public boolean isProcessApprovalFields() {
        return isProcessApprovalFields;
    }

    public boolean isTrackEditors() {
        return isTrackEditors;
    }
}
