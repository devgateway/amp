package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.testmodels.HardcodedReportsTestSchema;
import org.dgfoundation.amp.testmodels.NiReportModel;
import org.junit.Test;

/**
 * 
 * sanity checks for NiReports filtering offdb
 * 
 * @author Dolghier Constantin
 *
 */
public class OffDbNiReportFilteringTests extends FilteringSanityChecks {
	
	static Logger log = Logger.getLogger(OffDbNiReportFilteringTests.class);
	
	HardcodedReportsTestSchema schema = new HardcodedReportsTestSchema();
	public OffDbNiReportFilteringTests() {
		super("OffDbNiReportFiltering tests");
		nrRunReports = 0;
	}
	
	@Test
	public void testFilteringByModeOfPaymentCashNegative() {
		NiReportModel cor = null;
		
		ReportSpecificationImpl spec = buildSpecForFiltering("flat mop [NOT cash]", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), null, ColumnConstants.MODE_OF_PAYMENT, Arrays.asList(2093l), false);
		runNiTestCase(spec, "en", Arrays.asList("pledged 2"), cor);
	}
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getOfflineExecutor(activityNames);
	}
	
	@Override
	public void tearDown() {
		//System.err.format("Sanity checks %s have run %d reports\n", this.getName(), nrRunReports);
	}
}
