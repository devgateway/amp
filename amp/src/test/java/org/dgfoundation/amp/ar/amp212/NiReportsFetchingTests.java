package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.junit.Test;


/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * @author Constantin Dolghier
 *
 */
public class NiReportsFetchingTests extends MondrianReportsTestCase {
			
	public NiReportsFetchingTests() {
		super("inclusive runner tests");
	}
		
	@Test
	public void testSpeed() throws AMPException {
		ReportExecutor executor = AmpReportsSchema.getExecutor();
		ReportSpecification spec = buildSpecification("simple report", Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TEAM), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_YEARLY);
		executor.executeReport(spec);
		executor.executeReport(spec);
	}
	
	@Test
	public void testProjectTitle() throws Exception {
		runNiReportsTestcase(Arrays.asList("Unvalidated activity", "execution rate activity"), engine -> {
			List<? extends Cell> cells = engine.schema.getColumns().get(ColumnConstants.PROJECT_TITLE).fetchColumn(engine);
			assertEquals("[(actId: 64, <Unvalidated activity>), (actId: 77, <execution rate activity>)]", cells.toString());
		});
	}
	
	@Test
	public void testFundingSimple() throws Exception {
		runNiReportsTestcase(Arrays.asList("SubNational no percentages", "Unvalidated activity"), engine -> {
			List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetchColumn(engine);
			assertEquals("[" + 
				"(actId: 40, 75000 on 2014-02-05 with meta: {MetaInfoSet: [donor_org: 21700, terms_of_assistance: 2119, financing_instrument: 2120, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, " + 
				"(actId: 64, 45000 on 2015-01-06 with meta: {MetaInfoSet: [donor_org: 21695, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, transaction_type: 0]}]", 
			cells.toString());
		});
	}
	
	@Test
	public void testFundingDoubleFundingProject() throws Exception {
		runNiReportsTestcase(Arrays.asList("Pure MTEF Project"), engine -> {
			List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetchColumn(engine);
			assertEquals("[" + 
				"(actId: 19, 55333 on 2012-01-01 with meta: {MetaInfoSet: [donor_org: 21700, terms_of_assistance: 2119, financing_instrument: 2120, source_role: EA, adjustment_type: Actual, transaction_type: 3]}, " + 
				"(actId: 19, 33888 on 2011-01-01 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2120, source_role: DN, adjustment_type: Actual, transaction_type: 3]}" + 
			"]", 
			cells.toString());
		});
	}
}
