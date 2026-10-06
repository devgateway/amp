package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * @author Octavian Ciubotaru
 */
public class IndicatorValue {

    private Long id;

    private BigDecimal value;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String geoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    public IndicatorValue(Long id, BigDecimal value, String geoId, String name) {
        this.id = id;
        this.value = value;
        this.geoId = geoId;
        this.name = name;
    }

    public IndicatorValue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
