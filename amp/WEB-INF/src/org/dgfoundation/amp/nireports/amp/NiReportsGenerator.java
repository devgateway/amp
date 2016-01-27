package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.NiReportData;
import org.dgfoundation.amp.nireports.output.NiReportHtmlRenderer;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * the Reports API entrypoint for NiReports, used for anything not directly linked with NiReports or the task of generating a report:
 * 
 * @author Dolghier Constantin
 *
 */
public class NiReportsGenerator implements ReportExecutor {
	
	protected static final Logger logger = Logger.getLogger(NiReportsGenerator.class);
	
	public final NiReportsSchema schema;
	public final boolean logReport;
	
	public NiReportsGenerator(NiReportsSchema schema) {
		this(schema, true);
	}
	
	/**
	 * constructs an instance
	 * @param schema the schema to use
	 * @param logReport whether to log execution nodes to the DB
	 */
	public NiReportsGenerator(NiReportsSchema schema, boolean logReport) {
		this.schema = schema;
		this.logReport = logReport;
	}

	@Override
	public GeneratedReport executeReport(ReportSpecification report/*, NiReportsFormatter outputFormatter*/) {
		NiReportsEngine engine = new NiReportsEngine(schema, report);
		NiReportData reportOutput = engine.execute();
		GeneratedReport apiReport = generateApiOutput(reportOutput, engine)/*outputFormatter.format(reportOutput, engine)*/;
		if (logReport) {
			writeRunNodeToDatabase(apiReport.timings);
		}
		return apiReport;
	}
	
	protected void writeRunNodeToDatabase(RunNode node) {
		PersistenceManager.getSession().doWork(conn -> {
			List<String> columnNames = Arrays.asList("name", "totaltime", "wallclocktime", "data");
			String json = node.asJsonBean().asJsonString();
			List<Object> values = Arrays.asList(node.getName(), node.getTotalTime(), node.getTotalTime(), json);
			SQLUtils.insert(conn, "amp_nireports_log", "id", "amp_nireports_log_id_seq", columnNames, Arrays.asList(values));
		});
	}
	
	/** TODO: refactor once finalized */
	public String renderReport(ReportSpecification report) {
		NiReportsEngine engine = new NiReportsEngine(schema, report);
		NiReportData reportOutput = engine.execute();
		long start = System.currentTimeMillis();
		String renderedReport = new NiReportHtmlRenderer(engine, reportOutput).render();
		long renderTime = System.currentTimeMillis() - start;
		int reportX = engine.headers.leafColumns.size();
		int reportY = reportOutput.getIds().size();
		
		String reportRunTime = String.format("report runtime: %d millies", engine.timer.getCurrentState().getTotalTime());
		String reportRenderTime = String.format("report rendertime: %d millies", renderTime);
		String reportSize = String.format("report size Y*X = %d*%d (%d cells)", reportY, reportX, reportY * reportX);
		
		String pageHeader = String.format("<html><head>%s\n%s</head><body> <div style='position: fixed; left: 0; right: 0; top: 0; bottom: 0; z-index: 9999; background-size: cover; background-image: url(/TEMPLATE/ampTemplate/nireports/nickel2.png)'></div>%s", 
				"<link href='/TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>", 
				"<link href='/TEMPLATE/ampTemplate/nireports/nireports_view.css' rel='stylesheet' type='text/css'>",
				String.format("<div style='padding: 5px; margin: 20px; border: 1px dotted black; border-radius: 7px'>%s\n%s\n%s</div>", 
						String.format("<p style='margin: 10px'>%s</p>", reportRunTime),
						String.format("<p style='margin: 10px'>%s</p>", reportRenderTime),
						String.format("<p style='margin: 10px'>%s</p>", reportSize)));
		
		logger.error(reportRunTime);
		logger.error(reportRenderTime);
		logger.error(reportSize);
		
		return String.format("%s\n%s%s", pageHeader, renderedReport, "</body></html>");
	}
	
	protected GeneratedReport generateApiOutput(NiReportData reportOutput, NiReportsEngine engine) {
		RunNode timings = engine.timer.getCurrentState();
		return new GeneratedReport(engine.spec, (int) timings.getTotalTime(), null, null, null, null, timings);
	}
}
