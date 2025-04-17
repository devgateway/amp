package org.dgfoundation.amp.visibility.data;

/**
 * possible values for the GlobalSettings value which dictates the source of visibility for a ReportWizard item (Measures - as of 15/April/2015)
 * @author Constantin Dolghier
 *
 */
public final class VisibilitySourceOptions {
    public final static int ACTIVITY_FORM_VISIBILITY = 1;
    public final static int FEATURE_MANAGER_VISIBILITY = 2;
    public final static int FEATURE_MANAGER_AND_ACTIVITY_FORM_VISIBILITY = 3;
    public final static int FEATURE_MANAGER_OR_ACTIVITY_FORM_VISIBILITY = 4;
}
