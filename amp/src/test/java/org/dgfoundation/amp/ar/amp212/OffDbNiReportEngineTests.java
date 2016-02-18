package org.dgfoundation.amp.ar.amp212;

import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.testmodels.HardcodedReportsTestSchema;

/**
 * 
 * sanity checks for NiReports running offdb
 * 
 * @author Alexandru Cartaleanu
 *
 */
public class OffDbNiReportEngineTests extends BasicSanityChecks {
	
	static Logger log = Logger.getLogger(OffDbNiReportEngineTests.class);
	
	HardcodedReportsTestSchema schema = new HardcodedReportsTestSchema();
	public OffDbNiReportEngineTests() {
		super("NiReportsEngine tests");
		nrRunReports = 0;
	}
	
	@Override
	public<K> K buildNiReportDigest(ReportSpecification spec, List<String> activityNames, NiReportOutputBuilder<K> outputBuilder) {
		NiReportExecutor executor = getExecutor(activityNames);
		return executor.executeReport(spec, outputBuilder);
	}	
	
	protected NiReportExecutor getExecutor(List<String> activityNames) {
		NiReportExecutor res = new NiReportExecutor(HardcodedReportsTestSchema.getInstance());
		HardcodedReportsTestSchema.workspaceFilter = new HashSet<>(activityNames);
		return res;
	}
	
	@Override
	public void tearDown() {
		System.err.format("Sanity checks %s have run %d reports\n", this.getName(), nrRunReports);
	}
}
