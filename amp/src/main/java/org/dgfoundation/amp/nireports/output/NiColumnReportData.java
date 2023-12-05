package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * a flattened leaf region of a report output (see {@link ColumnReportData})
 * @author Dolghier Constantin
 *
 */
public class NiColumnReportData extends NiReportData {
    public final Map<CellColumn, Map<NiRowId, NiOutCell>> contents;
    
    /** READONLY view of {@link #rawOrderedIds} */
    public final List<NiRowId> orderedIds;
    
    /** MUTABLE the ids in the order they should be output (according to sorting criteria). In case no sorting is applied, ids are sorted numerically */
    private final List<NiRowId> rawOrderedIds;
    
    public NiColumnReportData(Map<CellColumn, Map<NiRowId, NiOutCell>> contents, Map<CellColumn, NiOutCell> trailCells, NiSplitCell splitterCell) {
        super(trailCells, contents.values().stream().flatMap(z -> z.keySet().stream()).collect(Collectors.toSet()), splitterCell);
        this.contents = Collections.unmodifiableMap(contents);
        this.rawOrderedIds = new ArrayList<>(this.ids);
        this.rawOrderedIds.sort(null);
        this.orderedIds = Collections.unmodifiableList(rawOrderedIds);
    }   

    public void reorder(Comparator<NiRowId> sorter) {
        this.rawOrderedIds.sort(sorter);
    }
    
    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public int computeRowSpan(boolean summaryReport) {
        if (summaryReport)
            return 1;
        return ids.size() + 1;
    }

    @Override
    public <K> K accept(NiReportDataVisitor<K> visitor) {
        return visitor.visit(this);
    }
}
