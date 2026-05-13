package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A class which tracks the hierarchies which have been applied on top of a cell.
 * You can read a (long) description on how it works here https://wiki.dgfoundation.org/display/AMPDOC/3.+NiReports+runtime#id-3.NiReportsruntime-3.4.1Percentagestrackingruntime
 * @author Dolghier Constantin
 *
 */
public class HierarchiesTracker {
    public final static HierarchiesTracker buildEmpty(CacheHitsCounter stats){return new HierarchiesTracker(Collections.emptyMap(), stats, BigDecimal.ONE);}
    
    final Map<NiDimensionUsage, SplitCellPercentage> percentages;
    final BigDecimal percentage;
    
    final CountedCacher<AdvancementInfo, HierarchiesTracker> cache;
    
    private HierarchiesTracker(Map<NiDimensionUsage, SplitCellPercentage> percentages, CacheHitsCounter stats, BigDecimal percentage) {
        //System.out.println("creating instance " + percentages);
        this.percentages = percentages;
        this.percentage = percentage;
        this.cache = new CountedCacher<>(stats);
    }
    
    /**
     * advances a hierarchy according to the main level of a given cell.<br />
     * Please notice that this function will return <strong>this</strong>crash in case the cell does not have a mainLevel: THIS IS INTENTIONAL, as one cannot calculate percentages by columns whose cells have no main dimension <br />
     * @param cell
     * @return
     */
    public HierarchiesTracker advanceHierarchy(Cell cell) {
        if (!cell.mainLevel.isPresent())
            return this; // do not change percentages when advancing over a cell with no main dimension
        return advanceHierarchy(cell.mainLevel.get(), cell.entityId, cell.getPercentage());
    }
    
    public HierarchiesTracker advanceHierarchy(LevelColumn level, long id, BigDecimal cellPercentage) {
        Coordinate coo = level.getCoordinate(id);
        SplitCellPercentage newValue = new SplitCellPercentage(coo, cellPercentage);
        SplitCellPercentage oldValue = percentages.get(level.dimensionUsage);
        
        if (oldValue != null && oldValue.isDeeperOrEqual(newValue))
            return this;

        AdvancementInfo info = new AdvancementInfo(level, id, cellPercentage);
        return cache.retrieveOrCompute(info, percentages.size() + 1, ignored -> {
            Map<NiDimensionUsage, SplitCellPercentage> res = new HashMap<>(percentages); // copy
            res.put(level.dimensionUsage, newValue);
            BigDecimal computedPercentage = BigDecimal.ONE;
            for(SplitCellPercentage scp:res.values())
                computedPercentage = computedPercentage.multiply(scp.getEffectivePercentage());
            
            //System.out.format("%s creating %d instance %s\n", System.identityHashCode(this), ignored.hashCode(), res.toString());
            return new HierarchiesTracker(res, cache.stats, computedPercentage);
        });
    }
    
    @Override
    public String toString() {
        return String.format("%.2f perc (%s)", percentage.doubleValue(), percentages.toString());
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
    
    static class SplitCellPercentage {

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
    
    static class AdvancementInfo {
        LevelColumn level;
        long id;
        BigDecimal cellPercentage;
        
        public AdvancementInfo(LevelColumn level, long id, BigDecimal cellPercentage) {
            this.level = level;
            this.id = id;
            this.cellPercentage = cellPercentage;
        }
        
        @Override
        public int hashCode() {
            return 19 * 17 * level.hashCode() + 17 * Long.hashCode(id) + (cellPercentage == null ? 0 : cellPercentage.hashCode());
        }
        
        @Override
        public boolean equals(Object other) {
            AdvancementInfo oth = (AdvancementInfo) other;
            boolean a = level.equals(oth.level) && (id == oth.id);
            if (!a)
                return false;
            if (cellPercentage == null ^ oth.cellPercentage == null)
                return false;
            // both null or both nonnull
            if (cellPercentage == null && oth.cellPercentage == null)
                return true;
            return cellPercentage.equals(oth.cellPercentage);
        }
        
        @Override
        public String toString() {
            return String.format("level: %s, id: %d, cellPercentage: %s", level, id, cellPercentage);
        }
    }
}
