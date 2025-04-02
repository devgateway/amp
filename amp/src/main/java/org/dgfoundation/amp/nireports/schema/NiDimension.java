package org.dgfoundation.amp.nireports.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.nireports.NiReportsEngine;

import static org.dgfoundation.amp.nireports.NiUtils.failIf;

/**
 * 
 * a class describing a set of related Columns, arranged in a stack. There might be multiple Columns on a given level of the stack. This class only references level ids; it is up to the Schema to map columns to levels within the dimension
 * identity comparisons are good here by design.<br />
 * Use by it defining {@link #fetchDimension()}
 * @author Dolghier Constantin
 *
 */
public abstract class NiDimension {

    protected static Logger logger = Logger.getLogger(NiDimension.class);

    public final String name;
    public final int depth;
    public final boolean continuum;
    
    public NiDimension(String name, int depth) {
        //failIf(depth < 2, "a NiDimension must have a depth of at least 2!");
        failIf(name == null, "a NiDimension must have a name");
        this.name = name;
        this.depth = depth;
        this.continuum = false;
    }
    
    public NiDimension(String name) {
        this.name = name;
        this.depth = 1;
        this.continuum = true;
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
        return getDimensionUsage(instanceName).getLevelColumn(level);
    }
    
    
    protected Map<String, NiDimensionUsage> _dimensionUsages = new ConcurrentHashMap<>();
    
    /**
     * generates a cached NiDimensionUsage instance (single object per instanceName)
     * @param instanceName
     * @return
     */
    public NiDimensionUsage getDimensionUsage(String instanceName) {
        return _dimensionUsages.computeIfAbsent(instanceName, z -> new NiDimensionUsage(this, z));
    }
    
    public List<NiDimensionUsage> getDUs() {
        return new ArrayList<>(_dimensionUsages.values());
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
    
    public static class Coordinate {
        public final int level;
        public final long id;
        
        public Coordinate(int level, long id) {
            this.level = level;
            this.id = id;
        }
        
        @Override
        public boolean equals(Object oth) {
            if (!(oth instanceof Coordinate))
                return false;
            return this.equals((Coordinate) oth);
        }
        
        public boolean equals(Coordinate oth) {
            return this.level == oth.level && this.id == oth.id;
        }
        
        @Override
        public int hashCode() {
            return level + 19 * Long.hashCode(id);
        }
        
        @Override
        public String toString() {
            return String.format("(level: %d, id: %d)", level, id);
        }
    }
    
    /**
     * an immutable class for managing an instance of a Dimension (e.g. "Implementing Agency" is an instance of "Organisation")
     * @author Dolghier Constantin
     *
     */
    public static class NiDimensionUsage {
        public final NiDimension dimension;
        public final String instanceName;
        
        private NiDimensionUsage(NiDimension dimension, String instanceName) {
            this.dimension = dimension;
            this.instanceName = instanceName;
        }
        
        private Map<Integer, LevelColumn> _levels = new ConcurrentHashMap<>();
        
        public LevelColumn getLevelColumn(int level) {
            return _levels.computeIfAbsent(level, z -> new LevelColumn(this, z));
        }
        
        @Override
        public final int hashCode() {
            return System.identityHashCode(dimension) + 19 ^ instanceName.hashCode();
        }
        
        @Override
        public final boolean equals(Object oth) {
            if (!(oth instanceof NiDimensionUsage)) return false;
            NiDimensionUsage other = (NiDimensionUsage) oth;
            return this.dimension == other.dimension && this.instanceName.equals(other.instanceName);
        }
        
        @Override
        public String toString() {
            return String.format("%s.%s", dimension.name, instanceName);
        }
    }
    
    /**
     * a class signaling that a column is a level in a multi-column Dimension (for example: 
     * "Donor type" in the "Organizations" dimension or Primary Sector SubSubSector in the "Sectors" dimension)
     * @author Dolghier Constantin
     *
     */
    public static class LevelColumn {
        public final NiDimensionUsage dimensionUsage;
        public final int level;
        
        private LevelColumn(NiDimensionUsage dimensionUsage, int level) {
            if (level >= dimensionUsage.dimension.depth)
                throw new RuntimeException(String.format("cannot build LevelColumn for DimUsage %s, because level is: %d; allowed range: 0...%d", dimensionUsage, level, dimensionUsage.dimension.depth - 1));
            this.dimensionUsage = dimensionUsage;
            this.level = level;
        }
        
        @Override
        public boolean equals(Object oth) {
            if (!(oth instanceof LevelColumn))
                return false;
            
            LevelColumn other = (LevelColumn) oth;
            if (dimensionUsage != other.dimensionUsage)
                return false;
            
            return level == other.level; 
        }
        
        public Coordinate getCoordinate(long id) {
            return new Coordinate(this.level, id);
        }
        
        @Override
        public String toString() {
            return String.format("%s.%s (level: %d)", dimensionUsage.dimension.name, dimensionUsage.instanceName, level);
        }
    }
    
    public final static int LEVEL_ALL_IDS = -999888777;
    public final static int LEVEL_0 = 0;
    public final static int LEVEL_1 = 1;
    public final static int LEVEL_2 = 2;
    public final static int LEVEL_3 = 3;
    public final static int LEVEL_4 = 4;
    public final static int LEVEL_5 = 5;
    public final static int LEVEL_6 = 6;
    public final static int LEVEL_7 = 7;
    public final static int LEVEL_8 = 8;
}
