package org.dgfoundation.amp.ar.amp212;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.dgfoundation.amp.algo.AmpCollections.sorted;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.digijava.kernel.persistence.PersistenceManager;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class NiReportsFetchingTests extends ReportingTestCase {

	public NiReportsFetchingTests() {
		super("AmpReportsSchema fetching tests");
	}

	@Test
	public void testSpeed() throws AMPException {
		ReportExecutor executor = AmpReportsSchema.getExecutor(false);
		ReportSpecification spec = buildSpecification("simple report",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TEAM),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_YEARLY);
		executor.executeReport(spec);
		executor.executeReport(spec);
	}

	@Test
	public void testProjectTitle() throws Exception {
		runInEngineContext(Arrays.asList("Unvalidated activity", "execution rate activity"), engine -> {
			List<? extends Cell> cells = engine.schema.getColumns().get(ColumnConstants.PROJECT_TITLE).fetch(engine);
			assertEquals("[Unvalidated activity (id: 64, eid: 64), execution rate activity (id: 77, eid: 77)]", cells.toString());
		});
	}

	@Test
	public void testFundingSimple() throws Exception {
		runInEngineContext(
				Arrays.asList("SubNational no percentages", "Unvalidated activity"),
				engine -> {
					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
					assertEquals(
						"[" 
						+ "(actId: 40, amt: 75000 on 2014-02-05, coos: {{cats.Financing Instrument=(level: 1, id: 2120), cats.Mode of Payment=(level: 1, id: 2094), cats.Type Of Assistance=(level: 1, id: 2119), orgs.DN=(level: 2, id: 21700)}}, meta: {MetaInfoSet: [source_org: 21700, source_role: DN, adjustment_type: Actual, transaction_type: 0]}, "
     					+ "(actId: 64, amt: 45000 on 2015-01-06, coos: {{cats.Financing Instrument=(level: 1, id: 2125), cats.Type Of Assistance=(level: 1, id: 2119), orgs.DN=(level: 2, id: 21695)}}, meta: {MetaInfoSet: [source_org: 21695, source_role: DN, adjustment_type: Actual, transaction_type: 0]}]",
						cells.toString());
				});
	}

	@Test
	public void testMtefFunding() throws Exception {
		runInEngineContext(
				Arrays.asList("Pure MTEF Project"),
				engine -> {
					List<CategAmountCell> cells = (List) engine.schema.getColumns().get("MTEF 2011/2012").fetch(engine);
					assertEquals("[(actId: 19, amt: 33888 on 2011-01-01, coos: {{cats.Financing Instrument=(level: 1, id: 2120), cats.Type Of Assistance=(level: 1, id: 2119), orgs.DN=(level: 2, id: 21699)}}, meta: {MetaInfoSet: [source_org: 21699, source_role: DN]}]",
						cells.toString());
				});
		
		runInEngineContext(
				Arrays.asList("Pure MTEF Project"),
				engine -> {
					List<CategAmountCell> cells2 = (List) engine.schema.getColumns().get("MTEF 2012/2013").fetch(engine);
					assertEquals("[]", // directed transaction
						cells2.toString());
				});
	}
	
	@Test
	public void testRealMtefFunding() throws Exception {
		runInEngineContext(
			Arrays.asList("activity with directed MTEFs"),
			engine -> {
				List<CategAmountCell> cells = (List) engine.schema.getColumns().get("Real MTEF 2011/2012").fetch(engine);
				assertEquals("[" + 
					"(actId: 73, amt: 110500 on 2011-01-01, coos: {{cats.Financing Instrument=(level: 1, id: 2125), cats.Mode of Payment=(level: 1, id: 2094), cats.Type Of Assistance=(level: 1, id: 2119), orgs.DN=(level: 2, id: 21698)}}, meta: {MetaInfoSet: [recipient_role: EA, directed_transaction_flow: IMPL-EXEC, source_org: 21698, source_role: IA, recipient_org: 21694]}, " + 
					"(actId: 73, amt: 50000 on 2011-01-01, coos: {{cats.Financing Instrument=(level: 1, id: 2120), cats.Mode of Payment=(level: 1, id: 2096), cats.Type Of Assistance=(level: 1, id: 2124), orgs.DN=(level: 2, id: 21696)}}, meta: {MetaInfoSet: [recipient_role: BA, directed_transaction_flow: EXEC-BENF, source_org: 21696, source_role: EA, recipient_org: 21702]}" 
				+ "]",
				cells.toString());
			});
	}

	@Test
	public void testExchangeRatesToBase() throws Exception {
		runInEngineContext(
				Arrays.asList("with weird currencies"),
				engine -> {
					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
					assertEquals(
						"["
							+ "(actId: 79, 87680.841736 on 2015-10-06, adjustment_type: Actual, transaction_type: 0), "
							+ "(actId: 79, 3632.137149 on 2014-12-16, adjustment_type: Actual, transaction_type: 0), " 
							+ "(actId: 79, 6250 on 2015-10-14, adjustment_type: Actual, transaction_type: 0)"
							+ "]",
						digestCellsList(cells, this::digestTransactionAmounts));
				});
	}
	
	@Test
	public void testExchangeRatesToEur() throws Exception {
		runInEngineContext(
				Arrays.asList("with weird currencies"),
				rep -> changeReportCurrency(rep, "EUR"),
				engine -> {
					List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
					assertEquals(
						"["
							+ "(actId: 79, 80000 on 2015-10-06, adjustment_type: Actual, transaction_type: 0), "
							+ "(actId: 79, 3313.961935 on 2014-12-16, adjustment_type: Actual, transaction_type: 0), "
							+ "(actId: 79, 10000 on 2015-10-14, adjustment_type: Actual, transaction_type: 0)"
							+ "]", 
					digestCellsList(cells, this::digestTransactionAmounts));
				});
	}

	@Test
	public void testExchangeRatesToMdl() throws Exception {
		runInEngineContext(
			Arrays.asList("with weird currencies"),
			rep -> changeReportCurrency(rep, "MDL"),
			engine -> {
				List<CategAmountCell> cells = engine.schema.getFundingFetcher().fetch(engine);
				assertEquals(
					"["
						+ "(actId: 79, 1728189.390618 on 2015-10-06, adjustment_type: Actual, transaction_type: 0), "
						+ "(actId: 79, 50000 on 2014-12-16, adjustment_type: Actual, transaction_type: 0), "
						+ "(actId: 79, 123187.5 on 2015-10-14, adjustment_type: Actual, transaction_type: 0)"
					+ "]",
				digestCellsList(cells, this::digestTransactionAmounts));
		});
	}

	@Test
	public void testActivityUpdatedBy() throws Exception {
		runInEngineContext(
			Arrays.asList("Unvalidated activity", "execution rate activity"),
			engine -> {
				List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.ACTIVITY_UPDATED_BY).fetch(engine));
				assertEquals("[" + 
					"ATL ATL (atl@amp.org) (id: 64, eid: 3), " + 
					"ATL ATL (atl@amp.org) (id: 77, eid: 3)"
					+ "]",
					cells.toString());
			});
	}

	@Test
	public void testImplementationLevel() throws Exception {
		runInEngineContext(Arrays.asList("Unvalidated activity", "execution rate activity"),
			engine -> {
				List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.IMPLEMENTATION_LEVEL).fetch(engine));
					assertEquals("[" + 
						"National (id: 64, eid: 70, coos: {cats.Implementation Level=(level: 1, id: 70)}), " + 
						"Provincial (id: 77, eid: 69, coos: {cats.Implementation Level=(level: 1, id: 69)})"
						+ "]", 
					cells.toString());
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
		runInEngineContext(Arrays.asList("Unvalidated activity", "execution rate activity"), engine -> {
			List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.FY).fetch(engine));
			assertEquals("[]", cells.toString());
		});
	}

	@Test
	public void testJointCriteria() throws Exception {
		runInEngineContext(Arrays.asList("Unvalidated activity", "execution rate activity", "Activity with Zones"), engine -> {
			List<? extends Cell> cells = sorted(engine.schema.getColumns().get(ColumnConstants.JOINT_CRITERIA).fetch(engine));
			assertEquals("[Yes (id: 33, eid: 33)]", cells.toString());
		});
	}
	
	@Test
	public void testPercentagesNormalization() throws Exception {
		// "new activity with contracting" lacks location entries
		List<String> acts = Arrays.asList("new activity with contracting",  "Activity with both MTEFs and Act.Comms", "Activity with Zones");
		runInEngineContext(acts, engine -> {
			List<? extends Cell> cells = nicelySorted(engine.schema.getColumns().get(ColumnConstants.REGION).fetch(engine));
			assertEquals("[Anenii Noi County (id: 33, eid: 9085, coos: {locs.LOCS=(level: 1, id: 9085)}, p: 0.50), Balti County (id: 33, eid: 9086, coos: {locs.LOCS=(level: 1, id: 9086)}, p: 0.50), Balti County (id: 70, eid: 9086, coos: {locs.LOCS=(level: 1, id: 9086)}, p: 0.30), Drochia County (id: 70, eid: 9090, coos: {locs.LOCS=(level: 1, id: 9090)}, p: 0.70)]", 
					cells.toString());

			List<? extends Cell> cellsZone = nicelySorted(engine.schema.getColumns().get(ColumnConstants.ZONE).fetch(engine));
			assertEquals("[Bulboaca (id: 33, eid: 9108, coos: {locs.LOCS=(level: 2, id: 9108)}, p: 0.50), Glodeni (id: 33, eid: 9111, coos: {locs.LOCS=(level: 2, id: 9111)}, p: 0.50),  (id: 70, eid: -9090, coos: {locs.LOCS=(level: 2, id: -9090)}, p: 0.70),  (id: 70, eid: -9086, coos: {locs.LOCS=(level: 2, id: -9086)}, p: 0.30)]", 
					cellsZone.toString());
		});
	}
	
	@Test
	public void testPercentagesCorrectorsFetching() throws Exception {
		AmpReportsSchema schema = AmpReportsSchema.getInstance();
		try(Connection conn = PersistenceManager.getJdbcConnection()) {
			Set<Long> ids = new HashSet<>(SQLUtils.fetchLongs(conn, "SELECT amp_activity_id FROM amp_activity_version"));
			assertEquals("sumOfPercs: {}", schema.PERCENTAGE_CORRECTORS.get(schema.PS_DIM_USG).buildSnapshot(conn, ids).toString());
			assertEquals("sumOfPercs: {33=200.0, 40=200.0}", schema.PERCENTAGE_CORRECTORS.get(schema.LOC_DIM_USG).buildSnapshot(conn, ids).toString());
			assertEquals("sumOfPercs: {}", schema.PERCENTAGE_CORRECTORS.get(schema.PP_DIM_USG).buildSnapshot(conn, ids).toString());
		}
	}
}
