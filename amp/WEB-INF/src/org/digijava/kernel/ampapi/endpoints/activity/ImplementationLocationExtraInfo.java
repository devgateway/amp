package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Octavian Ciubotaru
 */
public class ImplementationLocationExtraInfo {

    @JsonProperty("implementation-levels")
    private final List<Long> implementationLevels;

    public ImplementationLocationExtraInfo(List<Long> implementationLevels) {
        this.implementationLevels = implementationLevels;
    }
}
