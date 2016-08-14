package org.dgfoundation.amp.nireports.testcases;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.newreports.ReportArea;
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

public class BenchmarksRunner {
	
	final NiReportsSchema schema;
	final NiReportOutputBuilder<String> digester;

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
	
	protected BenchmarkResult doRun(String runName, ReportSpecification spec, BenchmarkResult corOutput, int runNumber) {
		NiReportsEngine engine = new NiReportsEngine(schema, spec);
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
		
		NiUtils.failIf(corDigest != null && !digest.equals(corDigest), () -> String.format("digest was <%s> instead of <%s>", digest, corDigest));
		NiUtils.failIf(corOutput != null && corOutput.nrFundingCells >= 0 && corOutput.nrFundingCells != nrFundingCells, () -> String.format("digest was <%s> instead of <%s>", digest, corDigest));
		
		if (runName.equals("bySector") && runNumber == 0 && false)
			System.out.println(new ReportModelGenerator().buildOutput(spec, reportRes).describeInCode());
		
		return new BenchmarkResult(runName, delta, formatTime, apiOutput.getNrEntities(), nrFundingCells, digest);
	}
	
	protected ReportArea formatOutput(ReportSpecification spec, NiReportRunResult reportRes) {
		DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
		decSymbols.setDecimalSeparator('.');
		decSymbols.setGroupingSeparator(',');
		final CellFormatter cellFormatter = new CellFormatter(spec.getSettings(), new DecimalFormat("###,###,###.##", decSymbols), "dd/MM/yyyy", z -> z, new OutputSettings(null));
		final NiReportsFormatter formatter = new NiReportsFormatter(spec, reportRes, cellFormatter);
		return reportRes.reportOut.accept(formatter);		
	}
	
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
