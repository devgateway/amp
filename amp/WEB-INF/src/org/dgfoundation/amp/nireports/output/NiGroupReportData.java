package org.dgfoundation.amp.nireports.output;

import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;
import org.dgfoundation.amp.nireports.output.sorting.ReportDataComparator;
import org.dgfoundation.amp.nireports.runtime.CellColumn;
import org.dgfoundation.amp.nireports.runtime.GroupReportData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * a flattened non-leaf node of a report output (see {@link GroupReportData})
 * @author Dolghier Constantin
 *
 */
public class NiGroupReportData extends NiReportData {
    /**
     * a read-only view of {@link #rawSubreports}
     */
    public final List<NiReportData> subreports;
    
    /**
     * the column of the hierarchy used to generate the children of this GRD
     */
    public final String splitterColumn;
    
    /**
     * an in-place modifiable list of children. Should not be accessed outside this class!
     */
    private final List<NiReportData> rawSubreports;

    public NiGroupReportData(List<NiReportData> subreports, Map<CellColumn, NiOutCell> trailCells, NiSplitCell splitter) {
        super(trailCells, subreports.stream().flatMap(z -> z.ids.stream()).collect(Collectors.toSet()), splitter);
        this.rawSubreports = new ArrayList<>(subreports);
        this.subreports = Collections.unmodifiableList(rawSubreports);
        this.splitterColumn = this.rawSubreports.isEmpty() ? null : this.rawSubreports.get(0).getGeneratingColumn();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
    
    @Override
    public int computeRowSpan(boolean summaryReport) {
        int sum = 1;
        for(NiReportData rd:subreports)
            sum += rd.getRowSpan(summaryReport);
        return sum;
    }

    public void reorder(ReportDataComparator rdc, Boolean ascending) {
        Comparator<NiReportData> comp = ascending == null ? 
            rdc : 
            rdc.thenComparing(maybeAscending(Comparator.nullsFirst(Comparator.comparing(NiReportData::getSplitter)), ascending));
        /*Comparator<NiReportData> comparator = Comparator.nullsFirst(Comparator.comparing(NiReportData::getSplitter)); */
        rawSubreports.sort(comp);
    }
    
    public final static<Z> Comparator<Z> maybeAscending(Comparator<Z> in, boolean ascending) {
        return ascending? in : in.reversed();
    }
    
    @Override
    public <K> K accept(NiReportDataVisitor<K> visitor) {
        return visitor.visit(this);
    }
}
