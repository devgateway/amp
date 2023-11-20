package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.*;

/**
 * @author Diego Rossi
 */
public class AmpSectorMapping {

    @JsonIgnore
    private Long id;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampSectorId",
            resolver = EntityResolver.class, scope = AmpSector.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("src-sector")
    private AmpSector srcSector;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampSectorId",
            resolver = EntityResolver.class, scope = AmpSector.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("dst-sector")
    private AmpSector dstSector;

    public AmpSectorMapping() {
    }

    public AmpSectorMapping(AmpSector srcSector, AmpSector dstSector) {
        this.srcSector = srcSector;
        this.dstSector = dstSector;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpSector getSrcSector() {
        return srcSector;
    }

    public void setSrcSector(AmpSector srcSector) {
        this.srcSector = srcSector;
    }

    public AmpSector getDstSector() {
        return dstSector;
    }

    public void setDstSector(AmpSector dstSector) {
        this.dstSector = dstSector;
    }

}
