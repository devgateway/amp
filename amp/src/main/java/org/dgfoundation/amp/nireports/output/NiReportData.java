package org.dgfoundation.amp.nireports.output;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ReportData;

/**
 * a flattened report output subregion (a flattened counterpart of {@link ReportData})
 * @author Dolghier Constantin
 *
 */
public abstract class NiReportData {
    public final Map<CellColumn, NiOutCell> trailCells;
    public final NiSplitCell splitter;
    public final Set<NiRowId> ids;
    
    protected NiReportData(Map<CellColumn, NiOutCell> trailCells, Set<NiRowId> ids, NiSplitCell splitter) {
        this.trailCells = Collections.unmodifiableMap(trailCells);
        this.splitter = splitter;
        this.ids = Collections.unmodifiableSet(ids);
    }
    
    public NiSplitCell getSplitter() {
        return splitter;
    }
    
    /**
     * returns the column to which the splitter entity of this NRD belongs to. In case there is none, returns null
     * @return
     */
    public String getGeneratingColumn() {
        if (splitter != null && splitter.entity != null)
            return splitter.entity.name;
        return null;
    }
    
    public abstract boolean isLeaf();
    /** function will only be called once per instance and the value of the argument will not change */
    protected abstract int computeRowSpan(boolean summaryReport);
    
    protected int _rowSpan = -1;
    
    /** computes the rowspan  */
    public int getRowSpan(boolean summaryReport) {
        if (_rowSpan < 0) {
            _rowSpan = computeRowSpan(summaryReport);
        }
        return _rowSpan;
    }
    
    public abstract<K> K accept(NiReportDataVisitor<K> visitor);
    
    @Override
    public String toString() {
        return String.format("%s split by %s, trails = %s", this.getClass().getSimpleName(), splitter, trailCells == null ? null : trailCells.entrySet().stream().collect(Collectors.toMap(z -> z.getKey().getHierName(), z -> z.getValue())));
    }
}
