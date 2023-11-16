package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.NumberedCell;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * An immutable {@link Cell} wrapper which has identity and is not shared between reports or columns.
 * The identity is added so that a NiCell adds personally-identifying information to a cell,
 * for example the percentages ({@link #hiersTracker}) or the upstream filters ({@link #passedFilters})
 * @author Dolghier Constantin
 *
 */
public class NiCell implements Comparable<NiCell> {
    /**
     * the entity to which this instance belongs
     */
    protected final NiReportedEntity<?> entity;
    
    /**
     * the enclosed raw cell
     */
    protected final Cell cell;
    
    /**
     * whether this is an undefined cell (e.g. one with a negative entityId)
     */
    protected final boolean undefinedCell;
    
    /**
     * the per-NiDimensionUsage coordinates-and-percentages
     */
    protected final HierarchiesTracker hiersTracker;
    
    /**
     * the upstream-passed filters during the "filters as collapsed hierarchies" phase
     */
    protected final Map<NiDimensionUsage, List<IdsAcceptor>> passedFilters;
    
    public NiCell(Cell cell, NiReportedEntity<?> entity, HierarchiesTracker hiersTracker) {
        this(cell, entity, hiersTracker, Collections.emptyMap());
    }
    
    public NiCell(Cell cell, NiReportedEntity<?> entity, HierarchiesTracker hiersTracker, Map<NiDimensionUsage, List<IdsAcceptor>> passedFilters) {
        NiUtils.failIf(cell == null, "not allowed to have NiCells without contents");
        this.cell = cell;
        this.undefinedCell = cell.entityId <= 0;
        this.entity = entity;
        this.hiersTracker = hiersTracker;
        this.passedFilters = Collections.unmodifiableMap(passedFilters);
    }

    /**
     * creates a new instance, obtained by advancing the hierarchy and passed filters of the given cell
     * 
     * @param newContents the cell to embed in the new instance. Unless you are doing some voodoo, this would equal {@link #cell}
     * @param splitCell the splitter cell used to advance {@link #hiersTracker}
     * @param acceptors might be null, in which case {@link #passedFilters} won't be advanced
     * @return
     */
    public NiCell advanceHierarchy(Cell newContents, Cell splitCell, Map<NiDimensionUsage, IdsAcceptor> acceptors) {
        return new NiCell(newContents, entity, hiersTracker.advanceHierarchy(splitCell), mergeAcceptors(acceptors));
    }
    
    /**
     * computes a map of acceptors which is the result of merging this cell's {@link #passedFilters} with the given acceptors.
     * In case both the cell and the given acceptors contain a mapping for a given {@link NiDimensionUsage}, the given map has priority 
     * @param acceptors
     * @return
     */
    public Map<NiDimensionUsage, List<IdsAcceptor>> mergeAcceptors(Map<NiDimensionUsage, IdsAcceptor> acceptors) {
        if (acceptors == null || acceptors.isEmpty())
            return passedFilters;
        
        Map<NiDimensionUsage, List<IdsAcceptor>> res = new HashMap<>();
        passedFilters.forEach((dimUsg, oldList) -> {
            List<IdsAcceptor> resList = acceptors.containsKey(dimUsg) ? AmpCollections.cat(oldList, acceptors.get(dimUsg)) : oldList;
            res.put(dimUsg, resList);
        });
        for(NiDimensionUsage dimUsg:acceptors.keySet()) {
            if (!res.containsKey(dimUsg)) // else the element has been added in the list above
                res.put(dimUsg, Arrays.asList(acceptors.get(dimUsg)));
        }       
        return res;
    }
    
    public NiReportedEntity<?> getEntity() {
        return entity;
    }

    public Cell getCell() {
        return cell;
    }
    
    public long getMainId() {
        return cell.activityId;
    }
    
    public boolean isUndefinedCell() {
        return this.undefinedCell;
    }

    public Map<NiDimensionUsage, List<IdsAcceptor>> getPassedFilters() {
        return this.passedFilters;
    }
    
    /**
     * returns true IFF this cell passes the filters of a given splitCell by consulting against the stored {@link #passedFilters}
     * @param splitCell
     * @return
     */
    public boolean passesFilters(Cell splitCell) {
//      boolean debug = this.cell instanceof TextCell; 
//      if (debug) {
//          System.err.format("testing passesFilters for id %d, <%s>, passedFilters <%s>...", System.identityHashCode(this), this.cell, this.passedFilters);
//      }
        for(Map.Entry<NiDimensionUsage, Coordinate> splitCellCoo:splitCell.getCoordinates().entrySet()) {
            if (this.passedFilters.containsKey(splitCellCoo.getKey())) {
                for(IdsAcceptor acceptor:this.passedFilters.get(splitCellCoo.getKey())) {
                    if (!acceptor.isAcceptable(splitCellCoo.getValue())) {
                        //if (debug) System.err.format("FAILED\n", 1);
                        return acceptor.isAcceptable(splitCellCoo.getValue());
                    }
                }
            }
        }
        //if (debug) System.err.format("OK\n", 1);
        return true;
    }

    /**
     * calculates the percentage which should be applied to the enclosed {@link #cell}'s amount
     * @return
     */
    public BigDecimal calculatePercentage() {
        return hiersTracker.calculatePercentage(getEntity().getBehaviour().getHierarchiesListener()).setScale(6, RoundingMode.HALF_EVEN); //TODO: maybe use the per-report precision setting
    }
    
    @Override
    public int compareTo(NiCell o) {
        if (undefinedCell && o.undefinedCell)
            return 0;
        if (undefinedCell ^ o.undefinedCell) {
            if (undefinedCell)
                return 1;
            return -1;
        }
        
        // gone till here -> neither of the cells is undefined
        return cell.compareTo(o.cell);
    }
    
    /**
     * computes, taking into account percentages, the amount stored in this cell. In case this NiCell is not holding a NumberedCell, the function will crash OR return a meaningless result
     */
    public BigDecimal getAmount() {
        BigDecimal percentage = calculatePercentage();
        BigDecimal value = ((NumberedCell) cell).getAmount().multiply(percentage);
        return value;
    }
    
    public String getDisplayedValue() {
        return cell.getDisplayedValue();
    }
    
    public HierarchiesTracker getHiersTracker() {
        return this.hiersTracker;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", cell.getDisplayedValue(), hiersTracker == null ? "" : hiersTracker.toString());
    }
}
