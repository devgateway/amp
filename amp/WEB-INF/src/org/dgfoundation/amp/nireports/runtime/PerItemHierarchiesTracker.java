package org.dgfoundation.amp.nireports.runtime;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
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

	public PerItemHierarchiesTracker advanceHierarchy(Cell cell) {
		Map<NiDimensionUsage, SplitCellPercentage> res = new HashMap<>(percentages); // copy
		BigDecimal percentage = BigDecimal.ONE;
		for(Map.Entry<NiDimensionUsage, Coordinate> entry:cell.getCoordinates().entrySet()) {
			SplitCellPercentage newValue = new SplitCellPercentage(entry.getValue(), cell.getPercentage());
			SplitCellPercentage oldValue = percentages.get(entry.getKey());
			if (oldValue == null || newValue.isDeeperOrEqual(oldValue)) {
				res.put(entry.getKey(), newValue);
			}
			percentage = percentage.multiply(res.get(entry.getKey()).getEffectivePercentage());
		}
		return new PerItemHierarchiesTracker(res, percentage);
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
