package org.dgfoundation.amp.nireports.testcases;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportEnvBuilder;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.amp.OutputSettings;
import org.dgfoundation.amp.nireports.output.CellFormatter;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.output.NiReportsFormatter;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

/**
 * a class which, given a schema and a list of benchmarks to run, runs each of them and returns the results in a hashmap having the spec.reportName as a key.
 * Each report is run multiple times and the best result is reported (thus this class is good for CPU-bottlenecked schemas / reports only)
 * @author Dolghier Constantin
 *
 */
public class BenchmarksRunner {
    
    final NiReportsSchema schema;
    final NiReportOutputBuilder<String> digester;

    /**
     * the number of times to run each report
     */
    public static int runs = 5;
    
    public BenchmarksRunner(NiReportsSchema schema, NiReportOutputBuilder<String> digester) {
        this.schema = schema;
        this.digester = digester;
    }
    
    public Map<String, BenchmarkResult> runBenchmarks(List<ImmutablePair<ReportSpecification, BenchmarkResult>> specs) {
        Map<String, BenchmarkResult> res = new LinkedHashMap<>();
        specs.forEach(z -> res.put(z.k.getReportName(), runBenchmark(z.k, z.v))); 
        return res;
    }
    
    /**
     * runs a specific report and returns an estimate of its performance. The estimate is the best result over {@link #runs} runs
     * @param spec
     * @param corOutput - the cor output to take into account. The function will throw an exception in case the non-timing fields are set and disagree with what comes out of the reports engine
     * @return
     */
    protected BenchmarkResult runBenchmark(ReportSpecification spec, BenchmarkResult corOutput) {
        long bestTime = Long.MAX_VALUE;
        BenchmarkResult bestRes = null;
        
        doRun(spec.getReportName(), spec, null, 0); // warm-up / JIT
        
        for(int i = 0; i < runs; i++) {
            BenchmarkResult r = doRun(spec.getReportName(), spec, corOutput, i);
            if (r.runMillies < bestTime)
                bestRes = r;
        }
        return bestRes;
    }
    
    /**
     * runs a given report a single time and returns the metrics (timings, digests, counts) of it. Crashes in case that a cor is supplied and any of its set values disagrees with what came out of the reports engine
     * @param runName
     * @param spec
     * @param corOutput
     * @param runNumber
     * @return
     */
    protected BenchmarkResult doRun(String runName, ReportSpecification spec, BenchmarkResult corOutput, int runNumber) {
        NiReportsEngine engine = new NiReportsEngine(schema, spec, ReportEnvBuilder.dummy());
        long start = System.currentTimeMillis();
        NiReportRunResult reportRes = engine.execute();
        long reportEnd = System.currentTimeMillis();
        long delta = reportEnd - start;
        ReportArea apiOutput = formatOutput(spec, reportRes);
        final long formatEnd = System.currentTimeMillis();
        final long formatTime = formatEnd - reportEnd;
        final String digest = digester.buildOutput(spec, reportRes);
        final int nrFundingCells = engine.funding.size();
        String corDigest = corOutput != null ? corOutput.digest : null;
        
        final int nrEntities = apiOutput.getNrEntities();
        NiUtils.failIf(corDigest != null && !digest.equals(corDigest), () -> String.format("digest was <%s> instead of <%s>", digest, corDigest));
        NiUtils.failIf(corOutput != null && corOutput.nrFundingCells >= 0 && corOutput.nrFundingCells != nrFundingCells, () -> String.format("digest was <%s> instead of <%s>", digest, corDigest));
        NiUtils.failIf(corOutput != null && corOutput.nrEntities >= 0 && nrEntities != corOutput.nrEntities, () -> String.format("nrEntities changed: should be %d, but is %d", corOutput.nrEntities, nrEntities));

        
        if (runName.equals("bySector") && runNumber == 0 && false)
            System.out.println(new ReportModelGenerator().buildOutput(spec, reportRes).describeInCode());
        
        return new BenchmarkResult(runName, delta, formatTime, apiOutput.getNrEntities(), nrFundingCells, digest);
    }

    /**
     * formats the NiReports API output to Reports API output. The output per se is not used, but the process is benchmarked against regressions
     * @param spec
     * @param reportRes
     * @return
     */
    protected ReportArea formatOutput(ReportSpecification spec, NiReportRunResult reportRes) {
        DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
        decSymbols.setDecimalSeparator('.');
        decSymbols.setGroupingSeparator(',');
        final CellFormatter cellFormatter = new CellFormatter(spec.getSettings(), 
                new DecimalFormat("###,###,###.##", decSymbols), "dd/MM/yyyy", z -> z, 
                new OutputSettings(null), reportRes.calendar);
        final NiReportsFormatter formatter = new NiReportsFormatter(spec, reportRes, cellFormatter);
        return reportRes.reportOut.accept(formatter);       
    }
    
    /**
     * the result of running a single test. If the test is being run multiple times, the best one is preserved  (e.g. the one with minimum {@link #runMillies})
     * @author Dolghier Constantin
     *
     */
    public static class BenchmarkResult {
        public final String runName;
        public final long runMillies;
        public final long outputMillies;
        public final String digest;
        public final int nrEntities;
        public final int nrFundingCells;

        public BenchmarkResult(String runName, long runMillies, long outputMillies, int nrEntities, int nrFundingCells, String digest) {
            this.runName = runName;
            this.runMillies = runMillies;
            this.outputMillies = outputMillies;
            this.digest = digest;
            this.nrFundingCells = nrFundingCells;
            this.nrEntities = nrEntities;
        }
        
        @Override
        public String toString() {
            return "BenchmarkResult [runName=" + runName + ", runMillies=" + runMillies + ", outputMillies=" + outputMillies + ", nrEntities=" + nrEntities + ", nrFundingCells=" + nrFundingCells + "]";
        }

        public String toCompleteString() {
            return "BenchmarkResult [runName=" + runName + ", runMillies=" + runMillies + ", outputMillies=" + outputMillies + ", nrEntities=" + nrEntities + ", nrFundingCells=" + nrFundingCells + ", digest=" + digest + "]";
        }

    }
}
