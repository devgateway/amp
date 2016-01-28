package org.dgfoundation.amp.nireports.runtime;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a class which tracks the hierarchies which have been applied on top of a cell
 * TODO: add testcases
 * @author Dolghier Constantin
 *
 */
public class PerItemHierarchiesTracker {
	public final static PerItemHierarchiesTracker EMPTY = new PerItemHierarchiesTracker(Collections.emptyMap(), BigDecimal.ONE);
	final Map<NiDimensionUsage, SplitCellPercentage> percentages;
	final BigDecimal percentage;
	
	private PerItemHierarchiesTracker(Map<NiDimensionUsage, SplitCellPercentage> percentages, BigDecimal percentage) {
		this.percentages = percentages;
		this.percentage = percentage;
	}
	
	/**
	 * advances a hierarchy according to the main level of a given cell.<br />
	 * Please notice that this function will return <strong>this</strong>crash in case the cell does not have a mainLevel: THIS IS INTENTIONAL, as one cannot calculate percentages by columns whose cells have no main dimension <br />
	 * @param cell
	 * @return
	 */
	public PerItemHierarchiesTracker advanceHierarchy(Cell cell) {
		if (cell.mainLevel.isPresent())
			return advanceHierarchy(cell.mainLevel.get(), cell.entityId, cell.getPercentage());
		return this;
	}
	
	public PerItemHierarchiesTracker advanceHierarchy(LevelColumn level, long id, BigDecimal cellPercentage) {
		Coordinate coo = level.getCoordinate(id);
		SplitCellPercentage newValue = new SplitCellPercentage(coo, cellPercentage);
		SplitCellPercentage oldValue = percentages.get(level.dimensionUsage);
		
		if (oldValue != null && oldValue.isDeeperOrEqual(newValue))
			return this;

		Map<NiDimensionUsage, SplitCellPercentage> res = new HashMap<>(percentages); // copy
		res.put(level.dimensionUsage, newValue);
		BigDecimal computedPercentage = BigDecimal.ONE;
		for(SplitCellPercentage scp:res.values())
			computedPercentage = computedPercentage.multiply(scp.getEffectivePercentage());
		
		return new PerItemHierarchiesTracker(res, computedPercentage);
	}
	
	public BigDecimal calculatePercentage(Predicate<NiDimensionUsage> acceptor) {
		if (acceptor == null)
			return percentage;
		BigDecimal prod = BigDecimal.ONE;
		for(Map.Entry<NiDimensionUsage, SplitCellPercentage> perc:percentages.entrySet()) 
			if (acceptor.test(perc.getKey()))
				prod = prod.multiply(perc.getValue().getEffectivePercentage());
		return prod;
	}
	
	class SplitCellPercentage {

		public final int level;
		public final long id;
		/** might be null */
		public final BigDecimal percentage;

		public SplitCellPercentage(Coordinate coo, BigDecimal percentage) {
			this(coo.level, coo.id, percentage);
		}
		
		public SplitCellPercentage(int level, long id, BigDecimal percentage) {
			this.level = level;
			this.id = id;
			this.percentage = percentage;
		}
		
		public boolean isDeeperOrEqual(SplitCellPercentage oth) {
			return this.level >= oth.level;
		}
		
		public BigDecimal getEffectivePercentage() {
			if (percentage == null)
				return BigDecimal.ONE;
			return percentage;
		}
		
		@Override
		public String toString() {
			return String.format("%.2f perc for (level: %d, id: %d)", percentage == null ? -1 : percentage.doubleValue() * 100, level, id);
		}
	}
}
