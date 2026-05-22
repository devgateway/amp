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
     * Uniqueness is per (indicator + activity_location) via indicator_location_key, not per indicator alone.
     */
    @PossibleValues(AmpIndicatorPossibleValuesProvider.class)
    @Interchangeable(fieldTitle = "Indicator", importable = true, pickIdOnly = true, uniqueConstraint = false)
    private AmpIndicator indicator;

    /**
     * Synthetic key for uniqueness: same indicator can appear multiple times with different locations.
     * Used by UniqueValidator so (indicator A, location 1) and (indicator A, location 2) are both allowed.
     */
    @Interchangeable(fieldTitle = "Indicator Location Key", uniqueConstraint = true, importable = false)
    private transient String indicatorLocationKey;

    /**
     * Indicator values.
     */
//    @Interchangeable(fieldTitle = "Indicator Values", importable = true, fmPath = "/Activity Form/M&E/ME Item/Actual Values")
//    @VersionableCollection(fieldTitle = "Indicator Values")
    protected Set<AmpIndicatorValue> values = new HashSet<>();

    /**
     * Disaggregation values for this indicator connection.
     * Transient — not persisted by Hibernate. Populated externally (e.g. via addExtraFieldsToActivity)
     */
    @Interchangeable(fieldTitle = "Disaggregation Values", importable = false)
    private transient Set<AmpIndicatorDisaggregationValue> disaggregationValues = new HashSet<>();

    /**
     * Activity location for multi-location instances.
     * When an indicator is added per location in a multi-location activity, this links to the specific
     * AmpActivityLocation. The activity API replaces this id with the inner location id at export time
     * (see ActivityInterchangeUtils.resolveIndicatorActivityLocations).
     * Null for single-location activities.
     */
    @Interchangeable(fieldTitle = "Activity Location", importable = true, pickIdOnly = true)
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
        this.indicatorLocationKey = null; // reset so getIndicatorLocationKey() recomputes
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
        this.indicatorLocationKey = null; // reset so getter recomputes
    }

    /** Composite key for uniqueness: indicator id + location id so same indicator with different locations is allowed. */
    public String getIndicatorLocationKey() {
        if (indicatorLocationKey == null) {
            Long indId = indicator != null ? indicator.getIndicatorId() : null;
            Long locId = activityLocation != null && activityLocation.getLocation() != null
                    ? activityLocation.getLocation().getId() : null;
            indicatorLocationKey = (indId != null ? indId : "") + "_" + (locId != null ? locId : "");
        }
        return indicatorLocationKey;
    }

    public Set<AmpIndicatorDisaggregationValue> getDisaggregationValues() {
        return disaggregationValues;
    }

    public void setDisaggregationValues(Set<AmpIndicatorDisaggregationValue> disaggregationValues) {
        this.disaggregationValues = disaggregationValues;
    }
}
