package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * @author Nadejda Mandrescu
 */
public class ActivityImportRules {

    private boolean canDowngradeToDraft;

    private boolean isProcessApprovalFields;

    public ActivityImportRules(boolean canDowngradeToDraft, boolean isProcessApprovalFields) {
        this.canDowngradeToDraft = canDowngradeToDraft;
        this.isProcessApprovalFields = isProcessApprovalFields;
    }

    public boolean isCanDowngradeToDraft() {
        return canDowngradeToDraft;
    }

    public boolean isProcessApprovalFields() {
        return isProcessApprovalFields;
    }

}
