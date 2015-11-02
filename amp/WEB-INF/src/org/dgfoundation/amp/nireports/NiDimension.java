package org.dgfoundation.amp.nireports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.digijava.kernel.persistence.PersistenceManager;

/**
 * 
 * a class describing a set of related Columns, where one is hierarchically inferior to another. This class only references level ids; it is up to the Schema to map columns to levels within the dimension
 * identity comparisons are good here by design
 * @author Dolghier Constantin
 *
 */
public abstract class NiDimension {
	public final String name;
	public final int depth;
	
	public NiDimension(String name, int depth) {
		this.name = name;
		this.depth = depth;
	}
	
	protected List<List<Long>> dimensionData;
	
	public List<List<Long>> getDimensionData() {
		boolean shouldRefetch = dimensionData == null || dimensionChanged();
		if (shouldRefetch) {
			dimensionData = freeze(fetchDimension());
		}
		return dimensionData;
	}
	
	/**
	 * 
	 * @param rawDimensionData
	 * @return
	 */
	protected List<List<Long>> freeze(List<Object[]> rawDimensionData) {
		List<List<Long>> res = new ArrayList<>();
		
		for(Object[] rawRow:rawDimensionData) {
			List<Long> row = new ArrayList<>();
			
			for(Object cell:rawRow)
				row.add(PersistenceManager.getLong(cell));
			
			if (row.size() != depth)
				throw new RuntimeException(String.format("row contains %d elements instead of %d: %s", row.size(), depth, row.toString()));
			
			res.add(Collections.unmodifiableList(row));
		}
		return Collections.unmodifiableList(res);
	}
	
	/**
	 * 
	 * @param level: 0...(depth-1)
	 */
	public LevelColumn getLevelColumn(int level) {
		if (level >= depth)
			throw new RuntimeException(String.format("cannot build LevelColumn for dimension %s, because level is: %d; allowed range: 0...%d", this.name, level, depth - 1));
		return new LevelColumn(this, level);
	}
	
	/**
	 * fetches the dimension data from the underlying storage. Notice that all the elements of the returned list should have a length of exactly {@link #depth}
	 * @return
	 */
	public abstract List<Object[]> fetchDimension();
	
	/**
	 * should return true if the dimension in the underlying storage has changed since the last call to {@link #fetchDimension()}.
	 * this function should be as fast as possible
	 * @return
	 */
	public abstract boolean dimensionChanged();
	
	@Override
	public String toString() {
		return String.format("NiDimension %s", this.name);
	}	
	
	/**
	 * a class signaling that an AMP column is a level in a multi-column Dimension (for example: "Donor type" in the "Organizations" dimension or Primary Sector SubSubSector in the "Sectors" dimension)
	 * @author Dolghier Constantin
	 *
	 */
	public static class LevelColumn {
		public final NiDimension dimension;
		public final int level;
		
		private LevelColumn(NiDimension dimension, int level) {
			this.dimension = dimension;
			this.level = level;
		}
	}
}
