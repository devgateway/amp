package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.digijava.module.aim.dbentity.AmpSector;

@JsonPropertyOrder({"id", "name", "code", "codeOfficial", "deleted"})
public class SectorDTO {

    @JsonProperty("id")
    private final Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("code")
    private final String code;

    @JsonProperty("codeOfficial")
    private final String codeOfficial;

    @JsonProperty("deleted")
    private final boolean deleted;

    public SectorDTO(final AmpSector sector) {
        this.id = sector.getAmpSectorId();
        this.name = sector.getName();
        this.code = sector.getSectorCode();
        this.codeOfficial = sector.getSectorCodeOfficial();
        this.deleted = sector.isSoftDeleted();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getCode() {
        return code;
    }

    public String getCodeOfficial() {
        return codeOfficial;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
