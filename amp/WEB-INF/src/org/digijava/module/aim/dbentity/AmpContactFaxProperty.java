package org.digijava.module.aim.dbentity;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.module.aim.annotations.interchange.Interchangeable;

/**
 * @author Octavian Ciubotaru
 */
public class AmpContactFaxProperty extends AmpContactProperty {

    @Interchangeable(fieldTitle = "Value", required = ActivityEPConstants.REQUIRED_ALWAYS, importable = true,
            regexPattern = ActivityEPConstants.REGEX_PATTERN_PHONE)
    private String value;

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
