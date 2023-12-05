package org.dgfoundation.amp.nireports.testcases.generic;

import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.digijava.module.common.util.DateTimeUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Cell source for hardcoded columns (TestColumn)
 * @author acartaleanu
 *
 * @param <K> - the type of cell used for populating
 */
public abstract class HardcodedCells<K extends Cell> {

    protected final Map<String, Long> activityIds;
    protected final Memoizer<List<K>> cells;
    protected final Map<String, Long> entityIds;
    public final Optional<LevelColumn> levelColumn;
    
    public HardcodedCells(Map<String, Long> activityIds, Map<String, Long> entityIds, LevelColumn levelColumn) {
        this.activityIds = activityIds;
        this.entityIds = entityIds;
        this.levelColumn = Optional.ofNullable(levelColumn);
        this.cells = new Memoizer<>(this::populateCells);
    }

    protected DateCell dateCell(String activityName, String date) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate ld = LocalDate.parse(date, formatter);
            return new DateCell(ld, activityIds.get(activityName), levelColumn.isPresent() ? -1 : DateTimeUtil.toJulianDayNumber(ld), levelColumn);
    }   
    
    public List<K> getCells() {
        return cells.get();
    }
    
    /**
     * must override in child class
     * child class contains createCell statements, depending on what kind of Cells it's made of
     * @return
     */
    protected abstract List<K> populateCells();
    
    /**
     * Create percentage text cell (sectors, programs, locations...)
     * @param activityName the activity title
     * @param text the payload of the cell
     * @param entityId the id of the cell
     * @param percentage percentage in the range of 0..1
     * @return
     */
    protected PercentageTextCell cell(String activityName, String text, long entityId, Double percentage) {
        return new PercentageTextCell(text, activityIds.get(activityName), entityId, levelColumn, BigDecimal.valueOf(percentage));
    }

    /**
     * Create simple text cell
     * @param activityName the activity title
     * @param text
     * @param entityId the id of the cell
     * @return
     */
    protected TextCell cell(String activityName, String text, long entityId) {
        return new TextCell(text, activityIds.get(activityName), entityId, levelColumn);
    }

    /**
     * Create simple text cell
     * @param activityName the activity title
     * @param text
     * @param entityId the id of the cell
     * @param coos coordinates of this cell
     * @return
     */
    protected TextCell cell(String activityName, String text, long entityId, Map<NiDimensionUsage, Coordinate> coos) {
        return new TextCell(text, activityIds.get(activityName), entityId, coos, levelColumn);
    }

    protected Map<NiDimensionUsage, Coordinate> coos(CoordinateEntry ...entries) {
        Map<NiDimensionUsage, Coordinate> coos = new HashMap<>();
        for (CoordinateEntry entry : entries) {
            LevelColumn lc = findSubLevelColumn(entry);
            if (lc != null) {
                coos.put(lc.dimensionUsage, lc.getCoordinate(entry.id));
            }
            if (levelColumn.isPresent()) {
                lc = levelColumn.get();
                if (entry.dimensionName.equals(lc.dimensionUsage.dimension.name)
                        && entry.instanceName.equals(lc.dimensionUsage.instanceName)
                        && entry.level == lc.level) {
                    coos.put(lc.dimensionUsage, lc.getCoordinate(entry.id));
                }
            }
        }
        return coos;
    }

    private LevelColumn findSubLevelColumn(CoordinateEntry entry) {
        return HardcodedReportsTestSchema.SUB_DIMENSION_USAGES.stream()
                .filter(du -> du.dimension.name.equals(entry.dimensionName) && du.instanceName.equals(entry.instanceName))
                .map(du -> du.getLevelColumn(entry.level))
                .findFirst()
                .orElse(null);
    }

    public static CoordinateEntry entry(String dimensionName, String instanceName, int level, long id) {
        CoordinateEntry entry = new CoordinateEntry();
        entry.dimensionName = dimensionName;
        entry.instanceName = instanceName;
        entry.level = level;
        entry.id = id;
        return entry;
    }

    public static class CoordinateEntry {
        private String dimensionName;
        private String instanceName;
        private int level;
        private long id;
    }
    
    protected Map<NiDimensionUsage, Coordinate> coos(long entityId) {
        if (!this.levelColumn.isPresent())
            return Collections.emptyMap();
        
        Map<NiDimensionUsage, Coordinate> res = new HashMap<>();
        res.put(levelColumn.get().dimensionUsage, levelColumn.get().getCoordinate(entityId));
        return res;
    }
    
    public static LevelColumn degenerate(NiDimension dim, String name) {
        return dim.getLevelColumn(name, dim.depth - 1);
    }
}
