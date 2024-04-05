package org.digijava.kernel.ampapi.endpoints.activity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class ImplementationLocationExtraInfo extends CategoryValueExtraInfo {

    @JsonProperty("implementation-levels")
    private final List<Long> implementationLevels;

    public ImplementationLocationExtraInfo(Integer index, List<Long> implementationLevels) {
        super(index);
        this.implementationLevels = implementationLevels;
    }
}
