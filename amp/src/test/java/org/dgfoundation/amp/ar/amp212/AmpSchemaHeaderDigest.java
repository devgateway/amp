package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.amp.AmpNiReportsFormatter;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static org.dgfoundation.amp.algo.AmpCollections.relist;

/**
 * Builds a string digest out of ReportOutputColumns. Used to test if headers were generated correctly.
 *
 * @author Octavian Ciubotaru
 */
class AmpSchemaHeaderDigest implements NiReportOutputBuilder<String> {

    private Map<ReportOutputColumn, String> cache = new IdentityHashMap<>();

    @Override
    public String buildOutput(ReportSpecification spec, NiReportRunResult reportRun) {
        GeneratedReport generatedReport = AmpNiReportsFormatter.asOutputBuilder(null).buildOutput(spec, reportRun);
        return String.format("{rootHeaders=%s, leafHeaders=%s}",
                digestOutputHeaders(generatedReport.rootHeaders),
                digestOutputHeaders(generatedReport.leafHeaders));
    }

    private String digestOutputHeaders(List<ReportOutputColumn> columns) {
        return String.join(", ", relist(columns, this::digestOutputHeader));
    }

    private String digestOutputHeader(ReportOutputColumn column) {
        StringBuilder digest = new StringBuilder("{");
        if (column.parentColumn != null) {
            digest.append("parent=")
                    .append(cache.computeIfAbsent(column.parentColumn, this::digestOutputHeader))
                    .append(", ");
        }
        digest.append("name=").append(column.originalColumnName);
        if (column.description != null) {
            digest.append(", desc=").append(column.description);
        }
        digest.append("}");
        return digest.toString();
    }
}
