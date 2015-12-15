package org.dgfoundation.amp.nireports;

import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * the Reports API entrypoint for NiReports, used for anything not directly linked with NiReports or the task of generating a report:
 * 
 * @author Dolghier Constantin
 *
 */
public class NiReportsGenerator implements ReportExecutor {
	
	public final NiReportsSchema schema;
	
	public NiReportsGenerator(NiReportsSchema schema) {
		this.schema = schema;
	}
	
	@Override
	public GeneratedReport executeReport(ReportSpecification report) {
		NiReportsEngine engine = new NiReportsEngine(schema, report);
		GroupReportData reportOutput = engine.execute();
		GeneratedReport apiReport = generateApiOutput(reportOutput, engine);
		return apiReport;
	}
	
	protected GeneratedReport generateApiOutput(GroupReportData reportOutput, NiReportsEngine engine) {
		RunNode timings = engine.timer.getCurrentState();
		return new GeneratedReport(engine.spec, (int) timings.getTotalTime(), null, null, null, null, timings);
	}
}
