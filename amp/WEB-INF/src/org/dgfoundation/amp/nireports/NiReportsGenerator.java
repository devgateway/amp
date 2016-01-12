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
		ReportData reportOutput = engine.execute();
		GeneratedReport apiReport = generateApiOutput(reportOutput, engine);
		return apiReport;
	}
	
	
	/** TODO: refactor once finalized */
	public String renderReport(ReportSpecification report) {
		NiReportsEngine engine = new NiReportsEngine(schema, report);
		ReportData reportOutput = engine.execute();
		String pageHeader = String.format("<html><head>%s\n%s</head><body>%s", 
				"<link href='/TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>", 
				"<link href='/TEMPLATE/ampTemplate/nireports/nireports_view.css' rel='stylesheet' type='text/css'>",
				String.format("<div style='padding: 20px; margin: 25px; border: 1px dotted black; border-radius: 7px'>report runtime: %d millies</div>", engine.timer.getWallclockTime())
				);
		return String.format("%s\n%s%s", pageHeader, new NiReportRenderer(engine, reportOutput).render(), "</body></html>");
	}
	
	protected GeneratedReport generateApiOutput(ReportData reportOutput, NiReportsEngine engine) {
		RunNode timings = engine.timer.getCurrentState();
		return new GeneratedReport(engine.spec, (int) timings.getTotalTime(), null, null, null, null, timings);
	}
}
