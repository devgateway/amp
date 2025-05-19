package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class TopDescription {

    @ApiModelProperty(example = "do")
    private String id;

    @ApiModelProperty(example = "Donor Agency")
    private String name;

    public TopDescription(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
