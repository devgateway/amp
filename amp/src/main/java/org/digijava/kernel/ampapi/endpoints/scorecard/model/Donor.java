package org.digijava.kernel.ampapi.endpoints.scorecard.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class Donor {

    @ApiModelProperty(example = "22")
    private final Long id;

    @ApiModelProperty(example = "Irish Aid")
    private final String name;

    public Donor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
