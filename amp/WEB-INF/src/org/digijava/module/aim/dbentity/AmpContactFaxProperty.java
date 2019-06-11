package org.digijava.module.aim.dbentity;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.RequiredValidation.ALWAYS;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.validators.common.RegexValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;

/**
 * @author Octavian Ciubotaru
 */
public class AmpContactFaxProperty extends AmpContactProperty {

    @Interchangeable(fieldTitle = "Value", required = ALWAYS, importable = true,
            interValidators = @InterchangeableValidator(
                    value = RegexValidator.class,
                    attributes = "regex=" + ActivityEPConstants.REGEX_PATTERN_PHONE))
    private String value;

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
