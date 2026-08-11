package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.SectorDTO;
import org.digijava.module.aim.dbentity.AmpSectorScheme;

@JsonPropertyOrder({"ampSecSchemeId", "secSchemeCode", "secSchemeName", "showInRMFilters", "used", "children"})
public class SectorSchemeDTO {
    @JsonProperty("ampSecSchemeId")
    private final Long ampSecSchemeId;

    @JsonProperty("secSchemeCode")
    private final String secSchemeCode;

    @JsonProperty("secSchemeName")
    private final String secSchemeName;

    @JsonProperty("showInRMFilters")
    private final boolean showInRMFilters;

    @JsonProperty("used")
    private final boolean used;

    @JsonProperty("children")
    private final SectorDTO[] children;

    public SectorSchemeDTO(AmpSectorScheme scheme, SectorDTO[] children) {
        this.ampSecSchemeId = scheme != null ? scheme.getAmpSecSchemeId() : null;
        this.secSchemeCode = scheme != null ? scheme.getSecSchemeCode() : null;
        this.secSchemeName = scheme != null ? scheme.getSecSchemeName() : null;
        this.showInRMFilters = Boolean.TRUE.equals(scheme != null ? scheme.getShowInRMFilters() : null);
        this.used = scheme != null && scheme.isUsed();
        this.children = children != null ? children : new SectorDTO[0];
    }

    public Long getAmpSecSchemeId() {
        return ampSecSchemeId;
    }

    public String getSecSchemeCode() {
        return secSchemeCode;
    }

    public String getSecSchemeName() {
        return secSchemeName;
    }

    public boolean isShowInRMFilters() {
        return showInRMFilters;
    }

    public boolean isUsed() {
        return used;
    }

    public SectorDTO[] getChildren() {
        return children;
    }
}
