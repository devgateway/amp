package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;

/**
 * part of the (NiReportsCore, AmpReportsSchema, Reports API) intersection - formats the output
 * @author Dolghier Constantin
 *
 */
public class AmpNiReportsFormatter {

	public final NiReportRunResult runResult;
	public final ReportSpecification spec;
	
	public AmpNiReportsFormatter(ReportSpecification spec, NiReportRunResult runResult) {
		this.runResult = runResult;
		this.spec = spec;
	}
	
	public GeneratedReport format() {
		return new GeneratedReport(spec, (int) runResult.wallclockTime, null, null, null, null, runResult.timings);
	}

	public static NiReportOutputBuilder<GeneratedReport> asAmpFormatter() {
		return (ReportSpecification spec, NiReportRunResult runResult) -> new AmpNiReportsFormatter(spec, runResult).format();
	}
}
