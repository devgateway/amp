package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.nireports.output.NiColumnReportData;
import org.dgfoundation.amp.nireports.output.NiGroupReportData;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportDataVisitor;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;
import org.dgfoundation.amp.nireports.runtime.CellColumn;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * a visitor which digests the values of a given trailCell
 * @author simple
 *
 */
public class TrailCellsDigest implements NiReportDataVisitor<Map<String, NiOutCell>> {

    final String headerPath;
    Stack<NiReportData> stack = new Stack<>();
    
    public TrailCellsDigest(String headerPath) {
        this.headerPath = headerPath;
    }
    
    public CellColumn findLeafHeader(NiReportData nrd) {
        for(CellColumn cc:nrd.trailCells.keySet())
            if (cc.getHierName().equals(headerPath))
                return cc;
        throw new RuntimeException(String.format("header path not found: %s, searched through: %s", headerPath, 
            nrd.trailCells.keySet().stream().map(z -> z.getHierName()).collect(Collectors.toList())));
    }
    
    public String buildPath() {
        return String.join(" -> ", stack.stream().map(z -> z.splitter == null ? "(root)" : z.splitter.text).collect(Collectors.toList()));
    }
    
    @Override
    public Map<String, NiOutCell> visit(NiColumnReportData crd) {
        stack.push(crd);
        Map<String, NiOutCell> res = new LinkedHashMap<>();
        CellColumn leaf = findLeafHeader(crd);
        res.put(buildPath(), crd.trailCells.get(leaf));
        stack.pop();
        return res;
    }

    @Override
    public Map<String, NiOutCell> visit(NiGroupReportData grd) {
        stack.push(grd);
        Map<String, NiOutCell> res = new LinkedHashMap<>();
        grd.subreports.forEach(z -> res.putAll(z.accept(this)));
        CellColumn leaf = findLeafHeader(grd);
        res.put(buildPath(), grd.trailCells.get(leaf));
        stack.pop();
        return res;
    }

}
