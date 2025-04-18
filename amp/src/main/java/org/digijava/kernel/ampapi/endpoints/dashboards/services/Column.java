package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
@ApiModel("HeatMapColumn") // needed because of collision with another class also named 'Column', a bug in swagger-core
public class Column {

    @ApiModelProperty(example = "Donor Group")
    private String origName;

    @ApiModelProperty(value = "Translated name", example = "Donor Group")
    private String name;

    public Column(String origName, String name) {
        this.origName = origName;
        this.name = name;
    }

    public String getOrigName() {
        return origName;
    }

    public String getName() {
        return name;
    }
}
