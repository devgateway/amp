package org.digijava.kernel.ampapi.endpoints.gis.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Boundary {

    private AdmLevel id;

    private String label;
    
    @ApiModelProperty("ISO2 Country Code")
    private String country;
    
    @JsonProperty("rectangle-to-center-gis")
    private BoundaryRectangle centerRectangle;
    
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
    
    public BoundaryRectangle getCenterRectangle() {
        return centerRectangle;
    }
    
    public void setCenterRectangle(BoundaryRectangle centerRectangle) {
        this.centerRectangle = centerRectangle;
    }
    
}

