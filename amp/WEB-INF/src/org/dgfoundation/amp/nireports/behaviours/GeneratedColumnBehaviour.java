package org.dgfoundation.amp.nireports.behaviours;

import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiRowId;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.output.nicells.NiTextCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;
import org.dgfoundation.amp.nireports.runtime.NiCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;

/**
 * an abstract behaviour of a fully-custom aggregated column/measure. <br />
 * These entities lack disaggregated cells, instead they populate at the late aggregation phase
 * @author Dolghier Constantin
 *
 */
public abstract class GeneratedColumnBehaviour<K extends Cell, V extends NiOutCell> implements Behaviour<V> {
        
    @Override
    public V doHorizontalReduce(List<NiCell> cells) {
        throw new RuntimeException("doHorizontalReduce not supported on generated columns");
    }
        
    @Override
    public NiSplitCell mergeSplitterCells(List<NiCell> splitterCells) {
        throw new RuntimeException("doing hierarchies by numeric values not supported");
    }

    @Override
    public Cell buildUnallocatedCell(long mainId, long entityId, LevelColumn levelColumn) {
        throw new RuntimeException("doing hierarchies by numeric values not supported");
    }       
    
    @Override
    public String getDebugDigest() {
        return this.getClass().getSimpleName();
    }   
    
    public abstract V buildGroupTrailCell(GroupReportData grd, CellColumn cc, List<NiReportData> visitedChildren);
    public abstract V buildColumnTrailCell(ColumnReportData crd, CellColumn cc, Map<CellColumn, Map<NiRowId, NiOutCell>> mappedContents);
    
    @Override
    public V getZeroCell() {
        return null;
    }

    @Override
    public NiOutCell getEmptyCell(ReportSpecification spec) {
        return NiTextCell.EMPTY;
    }

    public boolean isKeepingSubreports() {
        return false;
    }
            
    public boolean hasPercentages() {
        return false;
    }
    
    @Override
    public boolean isTransactionLevelUndefinedSkipping() {
        return false; // technically there shouldn't be cells here; just keeping it safe
    }
}
