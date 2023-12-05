package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiReportDataVisitor;
import org.dgfoundation.amp.nireports.output.NiRowId;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class RawDataDigest implements NiReportDataVisitor<Map<Long, NiOutCell>> {

    /**
     * filter on activity id
     * @param idFilter
     */
    public RawDataDigest(Set<NiRowId> idFilter) {
        this.idFilter = idFilter;
    }

    private final Set<NiRowId> idFilter;
    
    @Override
    public Map<Long, NiOutCell> visit(NiColumnReportData crd) {
        Map<Long, NiOutCell> res = new LinkedHashMap<>();
        Set<Map.Entry<CellColumn, Map<NiRowId, NiOutCell>>> contents = crd.contents.entrySet();
        Long resultId = 0L;
        for (Map.Entry<CellColumn, Map<NiRowId, NiOutCell>> row : contents) {
            Map<NiRowId, NiOutCell> cells = row.getValue();
            for (Map.Entry<NiRowId, NiOutCell> cell : cells.entrySet()) {
                if (idFilter.contains(cell.getKey())) {
                    res.put(resultId, cell.getValue());
                    resultId++;
                }
            }
        }
        return res;
    }

    @Override
    public Map<Long, NiOutCell> visit(NiGroupReportData grd) {
        Map<Long, NiOutCell> res = new LinkedHashMap<>();
        return res;
    }

}
