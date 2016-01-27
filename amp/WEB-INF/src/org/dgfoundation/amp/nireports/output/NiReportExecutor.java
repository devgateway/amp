package org.dgfoundation.amp.nireports.output;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.NiReportsGenerator;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * a thin layer above NiReportsEngine which converts the output to some external API and also adds support for report run notification
 * @author Dolghier Constantin
 *
 */
public class NiReportExecutor {
	protected static final Logger logger = Logger.getLogger(NiReportsGenerator.class);
	
	public final NiReportsSchema schema;
		
	/**
	 * constructs an instance
	 * @param schema the schema to use
	 */
	public NiReportExecutor(NiReportsSchema schema) {
		this.schema = schema;
	}

	public<K> K executeReport(ReportSpecification spec, NiReportOutputBuilder<K> outputBuilder) {
		NiReportsEngine engine = new NiReportsEngine(schema, spec);
		NiReportRunResult reportRun = engine.execute();
		consume(reportRun);
		return outputBuilder.buildOutput(spec, reportRun);
	}

	public String renderReport(ReportSpecification spec) {
		return executeReport(spec, NiReportHtmlRenderer.buildNiReportFullPageOutputter());
	}
	
	protected void consume(NiReportRunResult reportRun){};
}
