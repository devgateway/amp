package org.digijava.kernel.ampapi.endpoints.gis.services;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class Boundary {

    private AdmLevel id;

    private String label;

    @ApiModelProperty("ISO2 Country Code")
    private String country;

    public AdmLevel getId() {
        return id;
    }

    public void setId(AdmLevel id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
