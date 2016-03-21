package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.testmodels.HardcodedReportsTestSchema;
import org.dgfoundation.amp.testmodels.NiReportModel;
import org.junit.Test;

/**
 * 
 * sanity checks for NiReports running offdb
 * 
 * @author Alexandru Cartaleanu
 *
 */
public class OffDbNiReportSortingTests extends SortingSanityChecks {
	
	static Logger log = Logger.getLogger(OffDbNiReportSortingTests.class);
	
	HardcodedReportsTestSchema schema = new HardcodedReportsTestSchema();
	public OffDbNiReportSortingTests() {
		super("NiReportsSorting tests");
		nrRunReports = 0;
	}
	
//	@Override
//	public<K> K buildNiReportDigest(ReportSpecification spec, List<String> activityNames, NiReportOutputBuilder<K> outputBuilder) {
//		NiReportExecutor executor = getExecutor(activityNames);
//		return executor.executeReport(spec, outputBuilder);
//	}	
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getOfflineExecutor(activityNames);
	}
	
	@Override
	public void tearDown() {
		//System.err.format("Sanity checks %s have run %d reports\n", this.getName(), nrRunReports);
	}
}
