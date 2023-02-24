package org.dgfoundation.amp.nireports.schema;

import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DatedCell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiRowId;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnContents;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.runtime.VSplitStrategy;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;

/**
 * a specification of the behaviour of a given {@link NiReportColumn} / {@link NiReportMeasure}
 * @author Dolghier Constantin
 *
 */
public interface Behaviour<V extends NiOutCell> {
    
    /**
     * @return the maximum supported resolution. For any result which is not NONE, the column should contain cells which implement {@link DatedCell}
     */
    public TimeRange getTimeRange();
    
    /**
     * performs a horizontal reducing operation on cells: for multiple {@link Cell}'s in one displayed locum<br />
     * Client code should probably use {@link #horizontalReduce(List)} instead, as that one has guards for null/empty input
     * @param cells
     * @return
     */
    public V doHorizontalReduce(List<NiCell> cells);
    public default Cell filterCell(Map<NiDimensionUsage, IdsAcceptor> acceptors, Cell oldCell, Cell splitCell, boolean isTransactionLevelHierarchy) {
        
        if (cellMeetsCoos(acceptors, oldCell, splitCell, isTransactionLevelHierarchy && isTransactionLevelUndefinedSkipping()))
            return oldCell;
        else
            return null;
    }
    
    /** 
     * <strong>make the common case fast: if this function returns null, it is a shortcut for "measure listenes to all hierarchies"</strong>
     * @return a predicate which governs the hierarchies whose percentages are listened to by an entity 
     */
    public default Predicate<NiDimensionUsage> getHierarchiesListener() {
        return null;
    }
    
    public default ColumnContents horizSplit(ColumnContents oldContents, Map<Long, Cell> splitCells, Set<Long> acceptableMainIds, Map<NiDimensionUsage, IdsAcceptor> acceptors, boolean enqueueAcceptors, boolean isTransactionLevelHierarchy) {
        Map<Long, List<NiCell>> z = new HashMap<>();
        for(Long mainId:acceptableMainIds) {
            List<NiCell> oldCells = oldContents.data.get(mainId);
            Cell splitCell = splitCells.get(mainId);
            if (oldCells == null)
                continue;
            for(NiCell oldCell:oldCells) {
                Cell filteredCell = filterCell(acceptors, oldCell.getCell(), splitCell, isTransactionLevelHierarchy);
                if (filteredCell != null && oldCell.passesFilters(splitCell))
                    z.computeIfAbsent(mainId, id -> new ArrayList<>()).add(oldCell.advanceHierarchy(filteredCell, splitCell, enqueueAcceptors ? acceptors : null));
            }
        }
        return new ColumnContents(z);
    }
    
    public default NiOutCell horizontalReduce(List<NiCell> cells, NiReportsEngine context) {
        return doHorizontalReduce(cells);
    }
    
    public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn);
    
    /**
     * computes a "zero" cell, which is different from an "empty" cell. For numerical cells, this is a cell with zero. For textual cells, this is an "" cell 
     * @return
     */
    public V getZeroCell();
    
    /**
     * returns the cell to be displayed (but not output through the API) in case of missing data 
     * @return
     */
    public NiOutCell getEmptyCell(ReportSpecification spec);
    
    /**
     * merges 1+ splitter cells with the same displayed value into one NiSplitCell
     * @param splitterCells
     * @return
     */
    public default NiSplitCell mergeSplitterCells(List<NiCell> splitterCells) {
        return new NiSplitCell((NiReportColumn<?>) splitterCells.get(0).getEntity(), 
            splitterCells.get(0).getDisplayedValue(), 
            splitterCells.stream().map(z -> z.getCell().entityId).collect(toSet()), 
            splitterCells.stream().anyMatch(z -> z.isUndefinedCell()));
    }
    
    public default String getDebugDigest() {
        return String.format("%s", getTimeRange());
    }
    
    public static boolean cellMeetsCoos(Map<NiDimensionUsage, IdsAcceptor> acceptors, Cell oldCell, Cell splitCell, boolean skipMissingCoordinates) {
        Map<NiDimensionUsage, NiDimension.Coordinate> cellCoos = oldCell.getCoordinates();
        NiUtils.failIf(cellCoos == null, "null cellCoos");
        for(Entry<NiDimensionUsage, NiDimension.Coordinate> splitterElem:splitCell.getCoordinates().entrySet()) {
            NiDimensionUsage dimUsage = splitterElem.getKey();
            boolean splitterIsUndefined = splitterElem.getValue().id == ColumnReportData.UNALLOCATED_ID;
//          NiDimension.Coordinate splitterCoo = splitterElem.getValue();
            NiDimension.Coordinate cellCoo = cellCoos.get(dimUsage);
            if (cellCoo == null) {
                // cell is indifferent to this coordinate
                if (skipMissingCoordinates && !splitterIsUndefined)
                    return false; // being indifferent to transaction-level hier ==> rejected
                continue;  // not a transaction-level hier -> accept all
            }
            if (cellCoo.id == ColumnReportData.UNALLOCATED_ID && splitterIsUndefined)
                continue; // undefineds match
            IdsAcceptor acceptor = acceptors.get(dimUsage);
            if (acceptor == null) {
                continue;
            }
            boolean isAcceptable = acceptor.isAcceptable(cellCoo);
            if (!isAcceptable)
                return false;
        }
        return true;
    }

    /**
     * returns true iff cells of this type, upon running over a transaction-level hierarchy for which they lack a coordinate and the splitter is defined, should be skipped
     * normally all summable columns (e.g. funding cells which sum) should return true while texts should return false
     * @return
     */
    public boolean isTransactionLevelUndefinedSkipping();
    
    public default NiOutCell doVerticalReduce(Collection<V> cells) {
        return getZeroCell();
    }
    
    /**
     * builds the trail cells for GroupReportData
     * @param grd
     * @param cc
     * @param visitedChildren
     * @return
     */
    @SuppressWarnings("unchecked")  
    public default NiOutCell buildGroupTrailCell(GroupReportData grd, CellColumn cc, List<NiReportData> visitedChildren) {
        return doVerticalReduce(AmpCollections.relist(visitedChildren, child -> (V) child.trailCells.get(cc)));
    }
    
    /**
     * builds the trail cells for ColumnReportData 
     */
    @SuppressWarnings("unchecked")
    public default NiOutCell buildColumnTrailCell(ColumnReportData crd, CellColumn cc, Map<CellColumn, Map<NiRowId, NiOutCell>> mappedContents) {
        return doVerticalReduce((Collection<V>) mappedContents.get(cc).values());
    }

    /**
     * whether this CellColumn having a value in a given subreport means keeping the subreport in case {@link ReportSpecification#isDisplayEmptyFundingRows()} is true and we are running a report with hierarchies
     * @return
     */
    public boolean isKeepingSubreports();
    
    public default ImmutablePair<String, ColumnContents> getTotalCells(NiReportsEngine context, NiReportedEntity<?> entity, ColumnContents fetchedContents) {
        return null; // by default no entities go to totals
    }
    
    public default List<VSplitStrategy> getSubMeasureHierarchies(NiReportsEngine context) {
        return null; // by default entities do not split sub-measure
    }
    
    /**
     * whether the reports engine should delete leaves of this column when they are empty report-wise
     * @param column
     * @return
     */
    public default boolean shouldDeleteLeafIfEmpty(CellColumn column) {
        return false;
    }
    
    /**
     * whether cells of this columns have percentages
     * @return
     */
    public boolean hasPercentages();
    
    /**
     * whether the column/measure can be split by currency
     * 
     * @return
     */
    default boolean canBeSplitByCurrency() {
        return false;
    }
}
