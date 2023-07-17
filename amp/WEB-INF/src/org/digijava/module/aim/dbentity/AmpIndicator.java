package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.Validators;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "AMP_INDICATOR")
@TranslatableClass (displayName = "Indicator")
public class AmpIndicator implements Serializable, Identifiable
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_indicator_seq_generator")
    @SequenceGenerator(name = "amp_indicator_seq_generator", sequenceName = "AMP_INDICATOR_seq", allocationSize = 1)
    @Column(name = "indicator_id")
    private Long indicatorId;

    @Column(name = "name")
    @TranslatableField
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    @TranslatableField

    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "default_ind")
    private Boolean defaultInd;

    @Column(name = "unit")
    private String unit;

    @Column(name = "Comments")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "indicators_category")
    @Interchangeable(fieldTitle="Indicator Category")
    private AmpCategoryValue indicatorsCategory;

    @ManyToOne
    @JoinColumn(name = "risk")
    @Interchangeable(fieldTitle="Risk")

    private AmpIndicatorRiskRatings risk;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "AMP_SECTOR_INDICATOR",
            joinColumns = @JoinColumn(name = "indicator_id"),
            inverseJoinColumns = @JoinColumn(name = "amp_sector_id"))
    @Validators (unique="/Activity Form/M&E/uniqueSectorsValidator")

    private Set<AmpSector> sectors;

    @OneToMany(mappedBy = "indicator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IndicatorActivity> valuesActivity;

    @OneToMany(mappedBy = "indicator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IndicatorTheme> valuesTheme;
    
    //IATI-check: to be ignored
    private static final long serialVersionUID = 1L;

    @Transient
    private int category;


    public Long getIndicatorId() {
        return indicatorId;
    }
    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<AmpSector> getSectors() {
        return sectors;
    }
    public void setSectors(Set<AmpSector> sectors) {
        this.sectors = sectors;
    }
    public boolean isDefaultInd() {
        return defaultInd;
    }
    public void setDefaultInd(boolean defaultInd) {
        this.defaultInd = defaultInd;
    }

    public AmpCategoryValue getIndicatorsCategory() {
        return indicatorsCategory;
    }
    public void setIndicatorsCategory(AmpCategoryValue indicatorsCategory) {
        this.indicatorsCategory = indicatorsCategory;
    }
    public AmpIndicatorRiskRatings getRisk() {
        return risk;
    }
    public void setRisk(AmpIndicatorRiskRatings risk) {
        this.risk = risk;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public Set<IndicatorActivity> getValuesActivity() {
        return valuesActivity;
    }
    public void setValuesActivity(Set<IndicatorActivity> valuesActivity) {
        this.valuesActivity = valuesActivity;
    }
    public Set<IndicatorTheme> getValuesTheme() {
        return valuesTheme;
    }

    public String getUnit() {
        return unit;
    }

    public void setValuesTheme(Set<IndicatorTheme> valuesTheme) {
        this.valuesTheme = valuesTheme;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public Object getIdentifier() {
        return indicatorId;
    }
}
