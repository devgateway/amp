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
        this.ampSecSchemeId = scheme.getAmpSecSchemeId();
        this.secSchemeCode = scheme.getSecSchemeCode();
        this.secSchemeName = scheme.getSecSchemeName();
        this.showInRMFilters = scheme.getShowInRMFilters();
        this.used = scheme.isUsed();
        this.children = children;
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
