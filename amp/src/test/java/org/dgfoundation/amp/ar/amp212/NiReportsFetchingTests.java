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
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
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
		ReportSpecification spec = buildSpecification("simple report",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TEAM),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_YEARLY);
		executor.executeReport(spec);
		executor.executeReport(spec);
	}

	@Test
	public void testProjectTitle() throws Exception {
		runNiReportsTestcase(Arrays.asList("Unvalidated activity", "execution rate activity"), engine -> {
			List<? extends Cell> cells = engine.schema.getColumns().get(ColumnConstants.PROJECT_TITLE).fetch(engine);
			assertEquals("[Unvalidated activity (id: 64, eid: 64), execution rate activity (id: 77, eid: 77)]", cells.toString());
		});
	}

	@Test
	public void testFundingSimple() throws Exception {
		runNiReportsTestcase(
				Arrays.asList("SubNational no percentages", "Unvalidated activity"),
				engine -> {
					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
					assertEquals(
							"["
									+ "(actId: 40, 75000 on 2014-02-05 with meta: {MetaInfoSet: [donor_org: 21700, terms_of_assistance: 2119, financing_instrument: 2120, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
									+ "(actId: 64, 45000 on 2015-01-06 with meta: {MetaInfoSet: [donor_org: 21695, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, transaction_type: 0]}]",
							cells.toString());
				});
	}

	@Test
	public void testFundingDoubleFundingProject() throws Exception {
		runNiReportsTestcase(
				Arrays.asList("Pure MTEF Project"),
				engine -> {
					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
					assertEquals(
							"["
									+ "(actId: 19, 55333 on 2012-01-01 with meta: {MetaInfoSet: [donor_org: 21700, terms_of_assistance: 2119, financing_instrument: 2120, source_role: EA, adjustment_type: Actual, transaction_type: 3]}, "
									+ "(actId: 19, 33888 on 2011-01-01 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2120, source_role: DN, adjustment_type: Actual, transaction_type: 3]}"
									+ "]", cells.toString());
				});
	}

	@Test
	public void testExchangeRatesToBase() throws Exception {
		runNiReportsTestcase(
				Arrays.asList("with weird currencies"),
				engine -> {
					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
					assertEquals(
							"["
									+ "(actId: 79, 87680.841736 on 2015-10-06 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
									+ "(actId: 79, 3632.137149 on 2014-12-16 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
									+ "(actId: 79, 6250 on 2015-10-14 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}"
									+ "]", cells.toString());
				});
	}

	@Test
	public void testExchangeRatesToEur() throws Exception {
		runNiReportsTestcase(
				Arrays.asList("with weird currencies"),
				rep -> changeReportCurrency(rep, "EUR"),
				engine -> {
					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
					assertEquals(
							"["
									+ "(actId: 79, 80000 on 2015-10-06 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
									+ "(actId: 79, 3313.961935 on 2014-12-16 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
									+ "(actId: 79, 10000 on 2015-10-14 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}"
									+ "]", cells.toString());
				});
	}

	@Test
	public void testExchangeRatesToMdl() throws Exception {
		runNiReportsTestcase(
			Arrays.asList("with weird currencies"),
			rep -> changeReportCurrency(rep, "MDL"),
			engine -> {
				List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
				assertEquals(
					"["
						+ "(actId: 79, 1728189.390618 on 2015-10-06 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
						+ "(actId: 79, 50000 on 2014-12-16 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}, "
						+ "(actId: 79, 123187.5 on 2015-10-14 with meta: {MetaInfoSet: [donor_org: 21699, terms_of_assistance: 2119, financing_instrument: 2125, source_role: DN, adjustment_type: Actual, mode_of_payment: 2094, transaction_type: 0]}"
					+ "]",
				cells.toString());
		});
	}

	@Test
	public void testActivityUpdatedBy() throws Exception {
		runNiReportsTestcase(
			Arrays.asList("Unvalidated activity", "execution rate activity"),
			engine -> {
				List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.ACTIVITY_UPDATED_BY).fetch(engine));
				assertEquals("[ATL ATL (atl@amp.org) (id: 64, eid: 3), ATL ATL (atl@amp.org) (id: 77, eid: 3)]",
					cells.toString());
			});
	}

	@Test
	public void testImplementationLevel() throws Exception {
		runNiReportsTestcase(Arrays.asList("Unvalidated activity", "execution rate activity"),
			engine -> {
				List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.IMPLEMENTATION_LEVEL).fetch(engine));
				assertEquals("[National (id: 64, eid: 70), Provincial (id: 77, eid: 69)]", cells.toString());
			});
	}

//	@Test
//	public void testRelatedProjects() throws Exception {
//		runNiReportsTestcase(Arrays.asList("Unvalidated activity", "execution rate activity"), engine -> {
//			List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.RELATED_PROJECTS).fetch(engine));
//			assertEquals("[Unvalidated activity (id: -1, eid: 64), execution rate activity (id: -1, eid: 77)]", cells.toString());
//		});
//	}

	@Test
	public void testFy() throws Exception {
		runNiReportsTestcase(Arrays.asList("Unvalidated activity", "execution rate activity"), engine -> {
			List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.FY).fetch(engine));
			assertEquals("[]", cells.toString());
		});
	}

	@Test
	public void testJointCriteria() throws Exception {
		runNiReportsTestcase(Arrays.asList("Unvalidated activity", "execution rate activity", "Activity with Zones"), engine -> {
			List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.JOINT_CRITERIA).fetch(engine));
			assertEquals("[Yes (id: 33)]", cells.toString());
		});
	}
}
