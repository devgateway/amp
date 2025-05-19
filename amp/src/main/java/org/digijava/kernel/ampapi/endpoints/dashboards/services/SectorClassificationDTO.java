package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;

@JsonPropertyOrder({"id", "name", "description", "sectorSchemes", "multisector", "primary"})
public class SectorClassificationDTO {
    @JsonProperty("id")
    private final Long id;

    @JsonProperty("name")
    private final String name;
    @JsonProperty("description")
    private final  String description;

    @JsonProperty("sectorScheme")
    private final SectorSchemeDTO sectorScheme;

    @JsonProperty("multisector")
    private final boolean multisector ;

    @JsonProperty("primary")
    private final boolean primary;

    public SectorClassificationDTO(AmpClassificationConfiguration configuration, SectorSchemeDTO sectorScheme) {
        this.id = configuration.getId();
        this.name = configuration.getName();
        this.description = configuration.getDescription();
        this.sectorScheme = sectorScheme;
        this.multisector = configuration.isMultisector();
        this.primary = configuration.isPrimary();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SectorSchemeDTO getSectorScheme() {
        return sectorScheme;
    }

    public boolean isMultisector() {
        return multisector;
    }

    public boolean isPrimary() {
        return primary;
    }
}
