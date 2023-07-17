package org.digijava.module.aim.dbentity;

import java.io.Serializable;

/**
 * 
 * @deprecated now indicator itself has sector.
 *
 */
import javax.persistence.*;

@Entity
@Table(name = "AMP_INDICATOR_SECTOR")
@Deprecated
public class AmpIndicatorSector implements  Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_INDICATOR_SECTOR_seq")
    @SequenceGenerator(name = "AMP_INDICATOR_SECTOR_seq", sequenceName = "AMP_INDICATOR_SECTOR_seq", allocationSize = 1)
    @Column(name = "amp_indicator_sector_id")
    private Long ampIndicatorSectorId;

    @ManyToOne
    @JoinColumn(name = "amp_theme_ind_id", nullable = false)
    private AmpThemeIndicators themeIndicatorId;

    @ManyToOne
    @JoinColumn(name = "amp_sector_id", nullable = false)
    private AmpSector sectorId;

    public AmpSector getSectorId() {
        return sectorId;
    }

    public void setSectorId(AmpSector sectorId) {
        this.sectorId = sectorId;
    }
    
    public String toString() {
        return sectorId.getName();
    }

    public AmpThemeIndicators getThemeIndicatorId() {
        return themeIndicatorId;
    }

    public void setThemeIndicatorId(AmpThemeIndicators themeIndicatorId) {
        this.themeIndicatorId = themeIndicatorId;
    }

    public Long getAmpIndicatorSectorId() {
        return ampIndicatorSectorId;
    }

    public void setAmpIndicatorSectorId(Long ampIndicatorSectorId) {
        this.ampIndicatorSectorId = ampIndicatorSectorId;
    }

}
