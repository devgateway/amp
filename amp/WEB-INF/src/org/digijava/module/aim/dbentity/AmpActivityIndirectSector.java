package org.digijava.module.aim.dbentity;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Diego Rossi
 */
public class AmpActivityIndirectSector implements Serializable, Cloneable {

    public static final int PERCENTAGE_PRECISION = 2;

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    private AmpActivitySector activitySector;

    @Interchangeable(fieldTitle = "Sector", pickIdOnly = true)
    private AmpSector sector;

    @Interchangeable(fieldTitle = "Percentage")
    private BigDecimal percentage;

    public AmpActivityIndirectSector() {
    }

    public AmpActivityIndirectSector(AmpSector sector, BigDecimal percentage) {
        this.sector = sector;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpActivitySector getActivitySector() {
        return activitySector;
    }

    public void setActivitySector(AmpActivitySector activitySector) {
        this.activitySector = activitySector;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public AmpSector getSector() {
        return sector;
    }

    public void setSector(AmpSector sector) {
        this.sector = sector;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
