package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * the Reports API entry point for NiReports, used for generating & formatting a report and also logging reports runtime (if configured through {@link #logReport})
 * 
 * @author Dolghier Constantin
 *
 */
public class NiReportsGenerator extends NiReportExecutor implements ReportExecutor {
	
	/**
	 * whether to log normal report runs (those not specifically flagged as 'unlogged') to amp_nireports_log
	 */
	public static boolean ENABLE_NIREPORTS_LOGGING = false;
	
	protected static final Logger logger = Logger.getLogger(NiReportsGenerator.class);
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
		super(schema);
		this.logReport = logReport;
	}

	@Override
	protected void consume(NiReportRunResult reportRun){
		if (logReport && ENABLE_NIREPORTS_LOGGING) {
			writeRunNodeToDatabase(reportRun.timings, reportRun.wallclockTime);
		}
	}
	
	public<K> K executeReport(ReportSpecification spec, Function<NiReportRunResult, K> outputBuilder) {
		NiReportsEngine engine = new NiReportsEngine(schema, spec);
		NiReportRunResult reportRun = engine.execute();
		return outputBuilder.apply(reportRun);
	}
	
	@Override
	public GeneratedReport executeReport(ReportSpecification spec) {
		GeneratedReport apiReport = executeReport(spec, AmpNiReportsFormatter.asAmpFormatter());
		return apiReport;
	}
	
	protected void writeRunNodeToDatabase(RunNode node, long wallclockTime) {
		PersistenceManager.getSession().doWork(conn -> {
			List<String> columnNames = Arrays.asList("name", "totaltime", "wallclocktime", "data");
			String json = node.asJsonBean().asJsonString();
			List<Object> values = Arrays.asList(node.getName(), node.getTotalTime(), wallclockTime, json);
			SQLUtils.insert(conn, "amp_nireports_log", "id", "amp_nireports_log_id_seq", columnNames, Arrays.asList(values));
		});
	}
}
