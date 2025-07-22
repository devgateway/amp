package org.dgfoundation.amp.nireports.runtime;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.nicells.NiSplitCell;

import java.util.*;

import static java.util.stream.Collectors.toList;


/**
 * a disaggregated report containing subreports
 * @author Dolghier Constantin
 *
 */
public class GroupReportData extends ReportData {
    protected final List<ReportData> subreports;
    
    public GroupReportData(NiReportsEngine context, NiSplitCell splitter, List<? extends ReportData> subreports) {
        super(context, splitter);
        this.subreports = Collections.unmodifiableList(new ArrayList<ReportData>(subreports));
    }
    
    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Set<Long> _getIds() {
        Set<Long> res = new HashSet<>();
        for(ReportData rd:subreports)
            res.addAll(rd.getIds());
        return res;
    }

    public void addSubReport(ReportData rd) {
        this.subreports.add(rd);
    }
    
    public List<ReportData> getSubReports() {
        return subreports;
    }
    
    @Override
    public GroupReportData horizSplit(CellColumn column) {
        GroupReportData res = this.clone(subreports.stream().map(z -> z.horizSplit(column)).filter(z -> z != null && !z.subreports.isEmpty()).collect(toList()));
        return res;
    }

    @Override
    public <K> K accept(ReportDataVisitor<K> visitor) {
        K res = visitor.visitGroup(this);
        return res;
    }
}
