package org.dgfoundation.amp.nireports.testcases.tanzania;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.testcases.BenchmarksRunner;

public class TanzaniaBenchmarks {
	public static void main(String[] args) {
		
		BenchmarksRunner runner = new BenchmarksRunner(TanzaniaReportsTestSchema.getInstance(), GrandTotalsDigest.ALL_TOTALS_DIGESTER);
		Map<String, ReportSpecification> specs = new LinkedHashMap<>();
		specs.put("flat", ReportSpecificationImpl.buildFor(
				"flat", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY));
		
		System.out.println(runner.runBenchmarks(specs, Collections.emptyMap()));
	}
}
