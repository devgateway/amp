package org.digijava.kernel.ampapi.endpoints.gis.services;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
public class Boundary {

    private String id;

    private AdmLevel admLevel;

    private String label;

    @ApiModelProperty("ISO2 Country Code")
    private String country;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdmLevel getAdmLevel() {
        return admLevel;
    }

    public void setAdmLevel(AdmLevel admLevel) {
        this.admLevel = admLevel;
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