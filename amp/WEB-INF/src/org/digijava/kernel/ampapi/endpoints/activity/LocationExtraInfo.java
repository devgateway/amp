package org.digijava.kernel.ampapi.endpoints.activity;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Octavian Ciubotaru
 */
@JsonSerialize(using = LocationExtraInfoJsonSerializer.class)
public class LocationExtraInfo {

    /**
     * Note: this field is kept for backwards compatibility and is only used in flat format of possible values.
     */
    private final Long parentLocationId;

    /**
     * Note: this field is kept for backwards compatibility and is only used in flat format of possible values.
     */
    private final String parentLocationName;

    private final Long categoryValueId;

    private final String categoryValueName;

    private final String iso2;

    public LocationExtraInfo(Long parentLocationId, String parentLocationName, Long categoryValueId,
            String categoryValueName, String iso2) {
        this.parentLocationId = parentLocationId;
        this.parentLocationName = parentLocationName;
        this.categoryValueId = categoryValueId;
        this.categoryValueName = categoryValueName;
        this.iso2 = iso2;
    }

    public Long getParentLocationId() {
        return parentLocationId;
    }

    public String getParentLocationName() {
        return parentLocationName;
    }

    public Long getCategoryValueId() {
        return categoryValueId;
    }

    public String getCategoryValueName() {
        return categoryValueName;
    }

    public String getIso2() {
        return iso2;
    }
}
