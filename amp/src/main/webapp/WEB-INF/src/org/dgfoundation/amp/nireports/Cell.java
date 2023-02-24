package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.dgfoundation.amp.nireports.meta.CategCell;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;

/**
 * an immutable piece of data used for the disaggregated phases of the report. Being the abstract root the classes' hierarchy output by {@link NiReportedEntity#fetch(NiReportsEngine)}, this is the basic granularity with which data is fed into NiReports.
 * In this capacity, this class defines a number of important contracts which must be obeyed by all the subclasses. The overarching one is that all the subclasses must be deeply immutable, as a given Cell instance might (and will) be shared between:
 * <ul>
 *  <li>multiple measures</li>
 *  <li>different places, for the same measure, in the same report</li>
 *  <li>multiple threads operating on same instance or partial clones</li>
 *  <li>multiple threads running separate reports concurrently</li>
 *  <li>separate reports being run serially</li>
 * </ul> 
 * When subclassing, make sure you override {@link #buildCopy()} <br />
 * @author Dolghier Constantin
 *
 */
@SuppressWarnings("rawtypes")
public abstract class Cell implements Comparable, CategCell {
    /** the "owner" id. "activity" s the basic entity of the schema. Named like this because of a lack of a better idea. */
    public final long activityId;
    
    /** the id of the owning entity, where "entity" is the {@link LevelColumn} specified by {@link #mainLevel} (if one exists) */
    public final long entityId;
    
    /**
     * the coordinates of this cell in the space of the generating schema. Can never be null, but might very well be empty. <br />
     * In case {@link #mainLevel} is not empty(), this map <strong>should</strong> contain an entry having mainLevel.get().dimensionUsage as a key.
     */
    public final Map<NiDimensionUsage, Coordinate> coordinates;
    
    /**
     * defines the {@link LevelColumn} which will, together with {@link #entityId}, define the driving coordinates when doing a hierarchy having this cell instance as a pivot
     */
    public final Optional<LevelColumn> mainLevel;

    /**
     * constructs an instance of Cell
     * @param activityId see {@link #activityId}
     * @param entityId see {@link #entityId}
     * @param coordinates see {@link #coordinates}. Notice that, for performance reasons, no defensive copy of the input argument is done. <strong>Under no circumstances</strong> should you mutate the maps given as an input here. Make defensive copies if you are unsure 
     * @param mainLevel see {@link #mainLevel}
     */
    protected Cell(long activityId, long entityId, Map<NiDimensionUsage, Coordinate> coordinates, Optional<LevelColumn> mainLevel) {
        this.activityId = activityId;
        this.entityId = entityId;
        this.coordinates = Collections.unmodifiableMap(coordinates);
        this.mainLevel = mainLevel;
    }

    /**
     * constructs a minimum legal Cell instance - one which has all of its values except {@link #activityId} unset
     * @param activityId see {@link #activityId}
     */
    protected Cell(long activityId) {
        this(activityId, -1, Collections.emptyMap(), Optional.empty());
    }

    /**
     * utility method for the common case of a cell having a single coordinate, which coincides with its {@link #mainLevel}: given a {@link LevelColumn} and an entityId, constructs the {@link #coordinates} map of a cell
     * @param levelColumn
     * @param entityId
     * @return
     */
    protected static Map<NiDimensionUsage, Coordinate> buildCoordinates(Optional<LevelColumn> levelColumn, long entityId) {
        if (!levelColumn.isPresent())
            return Collections.emptyMap();
        HashMap<NiDimensionUsage, Coordinate> res = new HashMap<>();
        res.put(levelColumn.get().dimensionUsage, levelColumn.get().getCoordinate(entityId));
        return res;
    }
    
    /**
     * to be overridden by descendants which have percentages
     */
    public BigDecimal getPercentage() {
        return null;
    }
    
    /**
     * in case of cells which are used to drive a hierarchy (e.g. TextCell and PercentageTextCell), this is the value to become the name of the subreport. Otherwise it is useful for debugging in case of cells which are 
     * @return
     */
    public abstract String getDisplayedValue();
    
    /**
     * returns a cell identical to this one, save for the ownerId
     * @param newActivityId
     * @return a new cell. For performance reasons it will most probably share structures with the original instance
     */
    public abstract Cell changeOwnerId(long newActivityId);
    
    public Map<NiDimensionUsage, Coordinate> getCoordinates() {
        return coordinates;
    }
}
