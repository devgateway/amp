package org.dgfoundation.amp.nireports.testcases;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportRunResult;
import org.dgfoundation.amp.nireports.schema.NiReportsSchema;

public class BenchmarksRunner {
	
	final NiReportsSchema schema;
	final NiReportOutputBuilder<String> digester;
	
	public BenchmarksRunner(NiReportsSchema schema, NiReportOutputBuilder<String> digester) {
		this.schema = schema;
		this.digester = digester;
	}
	
	public Map<String, BenchmarkResult> runBenchmarks(Map<String, ReportSpecification> specs, Map<String, String> corDigests) {
		Map<String, BenchmarkResult> res = new LinkedHashMap<>();
		for(String testName:specs.keySet()) {
			res.put(testName, runBenchmark(testName, specs.get(testName), corDigests.get(testName)));
		}
		return res;
	}
	
	protected BenchmarkResult runBenchmark(String runName, ReportSpecification spec, String corDigest) {
		NiReportsEngine engine = new NiReportsEngine(schema, spec);
		long start = System.currentTimeMillis();
		NiReportRunResult reportRes = engine.execute();
		long delta = System.currentTimeMillis() - start;
		String digest = digester.buildOutput(spec, reportRes);
		NiUtils.failIf(corDigest != null && !digest.equals(corDigest), () -> String.format("digest was <%s> instead of <%s>", digest, corDigest));
		return new BenchmarkResult(runName, delta, digest);
	}
	
	public static class BenchmarkResult {
		public final String runName;
		public final long runMillies;
		public final String digest;

		public BenchmarkResult(String runName, long runMillies, String digest) {
			this.runName = runName;
			this.runMillies = runMillies;
			this.digest = digest;
		}
	}
}
