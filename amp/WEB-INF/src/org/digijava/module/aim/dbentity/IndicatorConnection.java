package org.digijava.module.aim.dbentity;

import org.digijava.kernel.ampapi.endpoints.activity.discriminators.AmpIndicatorValueDiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.values.AmpIndicatorPossibleValuesProvider;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.annotations.interchange.InterchangeableDiscriminator;
import org.digijava.module.aim.annotations.interchange.InterchangeableId;
import org.digijava.module.aim.annotations.interchange.PossibleValues;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Connection Indicator.
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorConnection implements Serializable, Comparable<IndicatorTheme>{

    private static final long serialVersionUID = 1L;

    @InterchangeableId
    @Interchangeable(fieldTitle = "Id")
    private Long id;

    /**
     * Indicator. this field is mandatory. It defines indicator in connection with activity, theme or team.
     */
    @PossibleValues(AmpIndicatorPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Indicator", importable = true, pickIdOnly = true, uniqueConstraint = true)
    private AmpIndicator indicator;

    /**
     * Indicator values.
     */
//    @Interchangeable(fieldTitle = "Indicator Values", importable = true, fmPath = "/Activity Form/M&E/ME Item/Actual Values")
//    @VersionableCollection(fieldTitle = "Indicator Values")
    protected Set<AmpIndicatorValue> values = new HashSet<>();

    private AmpActivityLocation activityLocation;

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

    public AmpActivityLocation getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(AmpActivityLocation activityLocation) {
        this.activityLocation = activityLocation;
    }
}
