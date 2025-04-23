package org.digijava.kernel.ampapi.endpoints.common.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores the original input with associated error codes
 * @author Nadejda Mandrescu
 */
public class BadInput {

    /** the original input */
    @JsonProperty(ActivityEPConstants.INPUT)
    private final Object input;

    /** the set of error codes for the bad input */
    @JsonProperty(ActivityEPConstants.INVALID)
    private final Set<String> invalid = new HashSet<String>();

    public BadInput(Object input) {
        this.input = input;
    }

    public Object getInput() {
        return this.input;
    }

    public Set<String> getInvalid() {
        return this.invalid;
    }

    public void addErrorCode(String errorCode) {
        this.invalid.add(errorCode);
    }
}
