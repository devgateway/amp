package org.dgfoundation.amp.nireports.amp;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.timing.RunNode;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.GeneratedReport;
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
 */
public class NiReportsGenerator extends NiReportExecutor implements ReportExecutor {

    protected static final Logger logger = Logger.getLogger(NiReportsGenerator.class);
    
    /**
     * whether to log normal report runs (those not specifically flagged as 'unlogged') to amp_nireports_log
     */
    public static boolean ENABLE_NIREPORTS_LOGGING = true;
    
    public final boolean logReport;
    
    public final OutputSettings outputSettings;
    
    public NiReportsGenerator(NiReportsSchema schema) {
        this(schema, true, null);
    }

    /**
     * constructs an instance
     * @param schema the schema to use
     * @param logReport whether to log execution nodes to the DB
     * @param outputSettings the Output Settings to be used
     */
    public NiReportsGenerator(NiReportsSchema schema, boolean logReport, OutputSettings outputSettings) {
        super(schema);
        this.logReport = logReport;
        this.outputSettings = outputSettings;
    }

    @Override
    protected void consume(NiReportRunResult reportRun){
        if (logReport && ENABLE_NIREPORTS_LOGGING) {
            writeRunNodeToDatabase(reportRun.timings, reportRun.wallclockTime);
        }
    }
        
    @Override
    public GeneratedReport executeReport(ReportSpecification spec) {
        GeneratedReport apiReport = executeReport(spec,
                AmpNiReportsFormatter.asOutputBuilder(outputSettings));
        logger.info(String.format("I just ran a report with cols %s, hiers %s, measures %s, filters %s and got back %d acts",
                spec.getColumnNames(), spec.getHierarchyNames(), spec.getMeasureNames(), spec.getFilters() == null ? null : spec.getFilters().getAllFilterRules(), apiReport.reportContents.getNrEntities()));
        return apiReport;
    }
    
    protected void writeRunNodeToDatabase(RunNode node, long wallclockTime) {
        PersistenceManager.getSession().doWork(conn -> {
            List<String> columnNames = Arrays.asList("name", "totaltime", "wallclocktime", "data");
            String details = node.getDetailsAsString();
            List<Object> values = Arrays.asList(node.getName(), node.getTotalTime(), wallclockTime, details);
            SQLUtils.insert(conn, "amp_nireports_log", "id", "amp_nireports_log_id_seq", columnNames, Arrays.asList(values));
        });
    }
}
