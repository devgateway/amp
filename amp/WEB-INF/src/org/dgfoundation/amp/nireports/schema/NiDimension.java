package org.dgfoundation.amp.nireports.schema;

import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.nireports.NiReportsEngine;

import static org.dgfoundation.amp.nireports.NiUtils.failIf;

/**
 * 
 * a class describing a set of related Columns, arranged in a stack. There might be multiple Columns on a given level of the stack. This class only references level ids; it is up to the Schema to map columns to levels within the dimension
 * identity comparisons are good here by design
 * @author Dolghier Constantin
 *
 */
public abstract class NiDimension {

	protected static Logger logger = Logger.getLogger(NiDimension.class);

	public final String name;
	public final int depth;
	
	public NiDimension(String name, int depth) {
		failIf(depth < 2, "a NiDimension must have a depth of at least 2!");
		failIf(name == null, "a NiDimension must have a name");
		this.name = name;
		this.depth = depth;
	}
	
	/**
	 * slow! Normally you do not call it directly - it should be used via {@link NiReportsEngine}!
	 * @return
	 */
	public DimensionSnapshot getDimensionData() {
		List<DimensionLevel> data = fetchDimension();
		if (data.size() != depth)
			throw new RuntimeException(String.format("fetchDimension() returned an array with %d elements, instead of: %d", data.size(), depth));
		return new DimensionSnapshot(data);
	}	
	
	/**
	 * 
	 * @param level: 0...(depth-1)
	 */
	public LevelColumn getLevelColumn(String instanceName, int level) {
		if (level >= depth)
			throw new RuntimeException(String.format("cannot build LevelColumn for dimension %s, because level is: %d; allowed range: 0...%d", this.name, level, depth - 1));
		return new LevelColumn(this, instanceName, level);
	}
	
	/**
	 * fetches the dimension data from the underlying storage. Notice that all the elements of the returned list should have a length of exactly {@link #depth}
	 * @return
	 */
	protected abstract List<DimensionLevel> fetchDimension();
		
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
		public final String instanceName;
		public final int level;
		
		private LevelColumn(NiDimension dimension, String instanceName, int level) {
			this.dimension = dimension;
			this.instanceName = instanceName;
			this.level = level;
		}
		
		@Override
		public boolean equals(Object oth) {
			if (!(oth instanceof LevelColumn))
				return false;
			
			LevelColumn other = (LevelColumn) oth;
			if (dimension != other.dimension)
				return false;
			if (!instanceName.equals(other.instanceName))
				return false;
			
			return level == other.level; 
		}
		
		@Override
		public String toString() {
			return String.format("Dimension: %s of %s, level: %d", instanceName, dimension.name, level);
		}
	}
}
