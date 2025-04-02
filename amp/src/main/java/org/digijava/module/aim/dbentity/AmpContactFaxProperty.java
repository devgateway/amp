package org.digijava.module.aim.dbentity;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.validators.common.RegexValidator;
import org.digijava.kernel.validators.common.RequiredValidator;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableValidator;

/**
 * @author Octavian Ciubotaru
 */
public class AmpContactFaxProperty extends AmpContactProperty {

    @Interchangeable(fieldTitle = "Value", importable = true,
            interValidators = {
                    @InterchangeableValidator(RequiredValidator.class),
                    @InterchangeableValidator(
                            value = RegexValidator.class,
                            attributes = "regex=" + ActivityEPConstants.REGEX_PATTERN_PHONE)})
    private String value;

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
