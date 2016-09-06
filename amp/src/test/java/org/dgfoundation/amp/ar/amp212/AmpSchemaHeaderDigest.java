package org.dgfoundation.amp.ar.amp212;

import static org.dgfoundation.amp.algo.AmpCollections.relist;

import java.util.List;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.amp.AmpNiReportsFormatter;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;

/**
 * Builds a string digest out of ReportOutputColumns. Used to test if headers were generated correctly.
 *
 * @author Octavian Ciubotaru
 */
class AmpSchemaHeaderDigest implements NiReportOutputBuilder<String> {

    public static final AmpSchemaHeaderDigest instance = new AmpSchemaHeaderDigest();

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
        String digest = "";
        if (column.parentColumn != null) {
            digest += String.format("parent=%s, ", digestOutputHeader(column.parentColumn));
        }
        digest += String.format("name=%s", column.originalColumnName);
        if (column.description != null) {
            digest += String.format(", desc=%s", column.description);
        }
        return String.format("{%s}", digest);
    }
}
