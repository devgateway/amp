package org.digijava.module.aim.validator;

import org.digijava.module.aim.dbentity.AmpActivityFields;

/**
 * @author Nadejda Mandrescu
 */
public class ActivityValidationContext {
    private AmpActivityFields oldActivity;

    public AmpActivityFields getOldActivity() {
        return oldActivity;
    }

    public void setOldActivity(AmpActivityFields oldActivity) {
        this.oldActivity = oldActivity;
    }

}
