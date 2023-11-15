package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.output.nicells.NiOutCell;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * a visitor which digests the values of a given trailCell
 * @author simple
 *
 */
public class GrandTotalsDigest implements NiReportOutputBuilder<Map<String, NiOutCell>> {
    
    final Predicate<String> headerAcceptor;
    
    public GrandTotalsDigest(Predicate<String> headerAcceptor) {
        this.headerAcceptor = headerAcceptor;
    }
    
    @Override
    public Map<String, NiOutCell> buildOutput(ReportSpecification spec, NiReportRunResult reportRun) {
        LinkedHashMap<String, NiOutCell> res = new LinkedHashMap<>();
        reportRun.headers.leafColumns.stream()
            .filter(z -> headerAcceptor.test(z.getHierName()))
            .forEach(z -> res.put(z.getHierName(), reportRun.reportOut.trailCells.get(z))); 
        return res;
    }

    @Override
    public String toString() {
        return "Grand Totals Digester";
    }
    
    /**
     * returns this digester expressed as a single-string-supplying digester
     * @return
     */
    public NiReportOutputBuilder<String> asStringDigester() {
        //return (spec, reportRun) -> this.buildOutput(spec, reportRun).entrySet().stream().map(z -> String.format("%s: %s\n", z.getKey(), z.getValue())).collect(Collectors.toList()).toString();
        return (spec, reportRun) -> this.buildOutput(spec, reportRun).toString();
    }
    
    /**
     * a string digester of all the headers
     */
    public static NiReportOutputBuilder<String> ALL_TOTALS_DIGESTER = new GrandTotalsDigest(a -> true).asStringDigester();
}
