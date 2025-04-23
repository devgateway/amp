package org.dgfoundation.amp.nireports.testcases;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;
import org.dgfoundation.amp.nireports.testcases.BenchmarksRunner.BenchmarkResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * a benchmarks runner, configured through a schema and a list of specifications
 * @author Dolghier Constantin
 *
 */
public class SchemaBenchmarks {
    
    protected final NiReportsSchema schema;
    
    public SchemaBenchmarks(NiReportsSchema schema) {
        this.schema = schema;
    }
    
    public void run(String runName, List<ImmutablePair<ReportSpecification, BenchmarkResult>> specs) {
        BenchmarksRunner runner = new BenchmarksRunner(schema, GrandTotalsDigest.ALL_TOTALS_DIGESTER);
        Map<String, BenchmarkResult> runRes = runner.runBenchmarks(specs);
        Map<String, BenchmarkResult> corByName = specs.stream().collect(Collectors.toMap(z -> z.k.getReportName(), z -> z.v));
        System.out.format("========================= %s =========================\n", runName);
        System.out.println(runRes);
        System.out.println("reportName,reportEntities,reportTime,outputTime,fundingCells,reportSpeedup,outputSpeedup");
        for(String reportName:runRes.keySet()) {
            BenchmarkResult run = runRes.get(reportName);
            BenchmarkResult cor = corByName.get(reportName);
            
            System.out.format("%s,%d,%d,%d,%d,%.2f,%.2f\n", reportName, run.nrEntities, run.runMillies, run.outputMillies, run.nrFundingCells, speedup(run.runMillies, cor.runMillies), speedup(run.outputMillies, cor.outputMillies));
        }
    }
    
    protected static double speedup(long run, long cor) {
        if (run == 0) return Double.POSITIVE_INFINITY;
        return 1.0 * cor / run;
    }   
}
