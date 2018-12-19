package org.dgfoundation.amp.nireports.output;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportEnvBuilder;
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
        NiReportsEngine engine = new NiReportsEngine(schema, spec, ReportEnvBuilder.forSession());
        NiReportRunResult reportRun = engine.execute();
        consume(reportRun);
        long start = System.currentTimeMillis();
        K res = outputBuilder.buildOutput(spec, reportRun);
        long delta = System.currentTimeMillis() - start;
        logger.trace(String.format("converting output to external API took %d millies", delta));
        return res;
    }

    public String renderReport(ReportSpecification spec) {
        return executeReport(spec, NiReportHtmlRenderer.buildNiReportFullPageOutputter());
    }
    
    protected void consume(NiReportRunResult reportRun){};
}
