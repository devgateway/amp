package org.digijava.module.aim.dbentity;

public class GPIDefaultFilters {

    public final static String GPI_DEFAULT_FILTER_ORG_GROUP = "GPI_DEFAULT_FILTER_ORG_GROUP";

    private Long gpiDefaultFiltersId;
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getGpiDefaultFiltersId() {
        return gpiDefaultFiltersId;
    }

    public void setGpiDefaultFiltersId(Long gpiDefaultFiltersId) {
        this.gpiDefaultFiltersId = gpiDefaultFiltersId;
    }

}
