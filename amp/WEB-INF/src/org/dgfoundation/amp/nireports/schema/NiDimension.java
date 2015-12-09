package org.dgfoundation.amp.nireports.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.NiReportsEngine;

import static org.dgfoundation.amp.nireports.NiUtils.failIf;

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
		failIf(depth < 2, "a NiDimension must have a depth of at least 2!");
		failIf(name == null, "a NiDimension must have a name");
		this.name = name;
		this.depth = depth;
	}
	
	protected List<List<Long>> dimensionData;
	
	public List<List<Long>> getDimensionData(NiReportsEngine engine) {
		boolean shouldRefetch = dimensionData == null || dimensionChanged(engine);
		if (shouldRefetch) {
			dimensionData = freeze(fetchDimension(engine));
			fetchAuxiliaryData(engine);
		}
		return dimensionData;
	}
	
	/**
	 * callback is called every time after fetchDimension has been called and its data postprocessed and frozen into {@link #dimensionData}
	 */
	protected abstract void fetchAuxiliaryData(NiReportsEngine engine);
	
	protected List<Long> freezeRow(List<Long> row) {
		if (row == null || row.size() != depth)
			throw new RuntimeException(String.format("%s: all returned rows should have length %d", this, depth));
		return Collections.unmodifiableList(new ArrayList<>(row));
	}
	
	/**
	 * 
	 * @param rawDimensionData
	 * @return
	 */
	protected List<List<Long>> freeze(List<List<Long>> rawDimensionData) {
		List<List<Long>> res = rawDimensionData.stream().map(this::freezeRow).collect(Collectors.toList());
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
	public abstract List<List<Long>> fetchDimension(NiReportsEngine engine);
	
	/**
	 * should return true if the dimension in the underlying storage has changed since the last call to {@link #fetchDimension()}.
	 * this function should be as fast as possible
	 * @return
	 */
	public abstract boolean dimensionChanged(NiReportsEngine engine);
		
	@Override
	public String toString() {
		return String.format("NiDimension %s with depth %d", this.name, this.depth);
	}	
	
	/**
	 * a class signaling that a column is a level in a multi-column Dimension (for example: "Donor type" in the "Organizations" dimension or Primary Sector SubSubSector in the "Sectors" dimension)
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
