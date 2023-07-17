package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.activity.discriminators.AmpIndicatorValueDiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.values.AmpIndicatorPossibleValuesProvider;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.PossibleValues;

/**
 * Connection Indicator.
 * @author Irakli Kobiashvili
 *
 */
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AMP_INDICATOR_CONNECTION")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "sub_clazz")
public class IndicatorConnection implements Serializable, Comparable<IndicatorTheme>{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_INDICATOR_CONNECTION_seq")
    @SequenceGenerator(name = "AMP_INDICATOR_CONNECTION_seq", sequenceName = "AMP_INDICATOR_CONNECTION_seq", allocationSize = 1)
    @Column(name = "id")
    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_id")
    @PossibleValues(AmpIndicatorPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Indicator", importable = true, pickIdOnly = true, uniqueConstraint = true)
    private AmpIndicator indicator;

    @OneToMany(mappedBy = "indicatorConnection", cascade = CascadeType.ALL, orphanRemoval = true)
    @InterchangeableDiscriminator(discriminatorField = "valueType",
            configurer = AmpIndicatorValueDiscriminationConfigurer.class, settings = {
            @Interchangeable(fieldTitle = "Base", discriminatorOption = "" + AmpIndicatorValue.BASE,
                    multipleValues = false, fmPath = "/Activity Form/M&E/ME Item/Base Value", importable = true),
            @Interchangeable(fieldTitle = "Target", discriminatorOption = "" + AmpIndicatorValue.TARGET,
                    multipleValues = false, fmPath = "/Activity Form/M&E/ME Item/Target Value", importable = true),
            @Interchangeable(fieldTitle = "Revised", discriminatorOption = "" + AmpIndicatorValue.REVISED,
                    multipleValues = false, fmPath = "/Activity Form/M&E/ME Item/Revised Value", importable = true),
            @Interchangeable(fieldTitle = "Actual", discriminatorOption = "" + AmpIndicatorValue.ACTUAL,
                    multipleValues = false, fmPath = "/Activity Form/M&E/ME Item/Current Value", importable = true)})
    protected Set<AmpIndicatorValue> values= new HashSet<>();




    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(AmpIndicator indicator) {
        this.indicator = indicator;
    }

    public Set<AmpIndicatorValue> getValues() {
        return values;
    }

    public void setValues(Set<AmpIndicatorValue> values) {
        this.values = values;
    }

    /**
     * Compares by db IDs.
     */
    public int compareTo(IndicatorTheme o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndicatorConnection)) {
            return false;
        }
        IndicatorConnection that = (IndicatorConnection) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
