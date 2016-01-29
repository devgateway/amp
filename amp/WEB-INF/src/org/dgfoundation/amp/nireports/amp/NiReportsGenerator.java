package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
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
	
	/**
	 * field to be removed once Mondrian-based reporting is done with
	 */
	public final Class<? extends ReportAreaImpl> reportAreaClazz;
	
	public NiReportsGenerator(NiReportsSchema schema) {
		this(schema, ReportAreaImpl.class);
	}

	public NiReportsGenerator(NiReportsSchema schema, Class<? extends ReportAreaImpl> reportAreaClazz) {
		this(schema, reportAreaClazz, true);
	}

	/**
	 * constructs an instance
	 * @param schema the schema to use
	 * @param reportAreaClazz the ReportArea implementation to be used
	 * @param logReport whether to log execution nodes to the DB
	 */
	public NiReportsGenerator(NiReportsSchema schema, Class<? extends ReportAreaImpl> reportAreaClazz, boolean logReport) {
		super(schema);
		this.logReport = logReport;
		this.reportAreaClazz = reportAreaClazz;
	}

	@Override
	protected void consume(NiReportRunResult reportRun){
		if (logReport && ENABLE_NIREPORTS_LOGGING) {
			writeRunNodeToDatabase(reportRun.timings, reportRun.wallclockTime);
		}
	}
		
	@Override
	public GeneratedReport executeReport(ReportSpecification spec) {
		GeneratedReport apiReport = executeReport(spec, AmpNiReportsFormatter.asAmpFormatter(ReportAreaImpl.buildSupplier(reportAreaClazz)));
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
