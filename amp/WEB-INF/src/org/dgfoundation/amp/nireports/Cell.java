package org.dgfoundation.amp.nireports;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.dgfoundation.amp.nireports.meta.CategCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * an internal consumption Cell, which roughly corresponds to an API ReportCell.
 * When subclassing, make sure you subclass {@link #buildCopy()}
 * @author Dolghier Constantin
 *
 */
public abstract class Cell implements Comparable, CategCell {
	public final long activityId;
	public final long entityId;
	public final Map<NiDimensionUsage, Coordinate> coordinates;
		
	public Cell(long activityId, long entityId, Map<NiDimensionUsage, Coordinate> coordinates) {
		this.activityId = activityId;
		this.entityId = entityId;
		this.coordinates = Collections.unmodifiableMap(coordinates);
	}
	
	protected static Map<NiDimensionUsage, Coordinate> buildCoordinates(Optional<LevelColumn> levelColumn, long entityId) {
		if (entityId < 0 || !levelColumn.isPresent())
			return Collections.emptyMap();
		HashMap<NiDimensionUsage, Coordinate> res = new HashMap<>();
		res.put(levelColumn.get().dimensionUsage, levelColumn.get().getCoordinate(entityId));
		return res;
	}

	public Cell(long activityId) {
		this(activityId, -1, Collections.emptyMap());
	}
	
	/**
	 * to be overridden by descendants which have percentages
	 */
	public BigDecimal getPercentage() {
		return null;
	}
	
	public abstract String getDisplayedValue();
	
	public abstract <K> K accept(CellVisitor<K> visitor);
	
	public Map<NiDimensionUsage, Coordinate> getCoordinates() {
		return coordinates;
	}
}
