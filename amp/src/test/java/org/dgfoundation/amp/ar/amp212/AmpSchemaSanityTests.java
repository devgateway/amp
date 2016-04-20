package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
import org.dgfoundation.amp.testmodels.HardcodedActivities;
import org.dgfoundation.amp.testmodels.NiReportModel;
import org.dgfoundation.amp.testutils.ReportTestingUtils;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class AmpSchemaSanityTests extends BasicSanityChecks {

	final List<String> mtefActs = Arrays.asList(
		"mtef activity 1",
		"mtef activity 2",
		"Pure MTEF Project",
		"activity with MTEFs",
		"Activity with both MTEFs and Act.Comms",
		"activity with many MTEFs",
		"Test MTEF directed",
		"activity with pipeline MTEFs and act. disb"
	);
	
	final List<String> ppcActs = Arrays.asList(
			"Proposed Project Cost 1 - USD",
			"Proposed Project Cost 2 - EUR",
			"SubNational no percentages",
			"Activity with primary_tertiary_program",
			"activity with primary_program",
			"activity with tertiary_program",
			"activity 1 with agreement",
			"activity with directed MTEFs"
		);
	
	public AmpSchemaSanityTests() {
		super("AmpReportsSchema sanity tests");
	}
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getDbExecutor(activityNames);
	}
	@Test
	public void testActivityIds() {
		
		NiReportModel cor =new NiReportModel("testcase amp activity ids")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
						"(Activity Id: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
						"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Activity Id", "", "Project Title", "", "Totals-Actual Commitments", "1,011,456", "Totals-Actual Disbursements", "0")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(19), "Activity Id", "19", "Project Title", "Pure MTEF Project"),
				        new ReportAreaForTests(new AreaOwner(70), "Activity Id", "70", "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Actual Commitments", "888,000"),
				        new ReportAreaForTests(new AreaOwner(73), "Activity Id", "73", "Project Title", "activity with directed MTEFs", "Totals-Actual Commitments", "123,456")      ));
		runNiTestCase(
				buildSpecification("testcase amp activity ids", 
						Arrays.asList(ColumnConstants.ACTIVITY_ID, ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en", 
				Arrays.asList("Pure MTEF Project", "activity with directed MTEFs", "Activity with both MTEFs and Act.Comms"),
				cor);
	}	
	@Test
	public void testMtefColumnsPlain() {
		assertEquals("{RAW / Project Title=, RAW / MTEF 2011/2012=1283182.4159, RAW / MTEF 2012/2013=202437, RAW / MTEF 2013/2014=120180.405, RAW / Funding / 2006 / Actual Commitments=80000, RAW / Funding / 2006 / Actual Disbursements=0, RAW / Funding / 2009 / Actual Commitments=78470, RAW / Funding / 2009 / Actual Disbursements=0, RAW / Funding / 2010 / Actual Commitments=0, RAW / Funding / 2010 / Actual Disbursements=613561.3161, RAW / Funding / 2011 / Actual Commitments=896327.2977, RAW / Funding / 2011 / Actual Disbursements=0, RAW / Funding / 2012 / Actual Commitments=19577.5, RAW / Funding / 2012 / Actual Disbursements=9162, RAW / Funding / 2013 / Actual Commitments=5905874.9666, RAW / Funding / 2013 / Actual Disbursements=954144.5636, RAW / Funding / 2014 / Actual Commitments=7409649.482335, RAW / Funding / 2014 / Actual Disbursements=576269.62, RAW / Funding / 2015 / Actual Commitments=1803396.8724, RAW / Funding / 2015 / Actual Disbursements=399024.454, RAW / Totals / Actual Commitments=16193296.119035, RAW / Totals / Actual Disbursements=2552161.9537, RAW / Totals / MTEF=1605799.8209}", 
			buildDigest(spec("AMP-16100-flat-mtefs-eur"), acts, new GrandTotalsDigest(z -> true)).toString());
	}
	
	@Test
	public void testMtefColumnsMixedPlain() {
		assertEquals("{RAW / Project Title=, RAW / MTEF 2011/2012=1718011, RAW / Pipeline MTEF Projections 2011/2012=908888, RAW / Projection MTEF Projections 2011/2012=809123, RAW / MTEF 2012/2013=271000, RAW / Pipeline MTEF Projections 2012/2013=108000, RAW / Projection MTEF Projections 2012/2013=163000, RAW / MTEF 2013/2014=158654, RAW / Pipeline MTEF Projections 2013/2014=158654, RAW / Projection MTEF Projections 2013/2014=0, RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2009 / Actual Commitments=100000, RAW / Funding / 2011 / Actual Commitments=1213119, RAW / Funding / 2012 / Actual Commitments=25000, RAW / Funding / 2013 / Actual Commitments=7842086, RAW / Funding / 2014 / Actual Commitments=8159813.768451, RAW / Funding / 2015 / Actual Commitments=1971831.841736, RAW / Totals / Actual Commitments=19408691.186388, RAW / Totals / MTEF=2147665, RAW / Totals / Pipeline MTEF=1175542, RAW / Totals / Projection MTEF=972123}", 
			buildDigest(spec("AMP-21275-all-plain-mtefs"), acts, new GrandTotalsDigest(z -> true)).toString());
	}

	@Test
	public void testMtefColumnsMixedPlain2() {
		assertEquals("{RAW / Project Title=, RAW / MTEF 2011/2012=1718011, RAW / Pipeline MTEF Projections 2011/2012=908888, RAW / MTEF 2012/2013=271000, RAW / Projection MTEF Projections 2012/2013=163000, RAW / MTEF 2013/2014=158654, RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2009 / Actual Commitments=100000, RAW / Funding / 2011 / Actual Commitments=1213119, RAW / Funding / 2012 / Actual Commitments=25000, RAW / Funding / 2013 / Actual Commitments=7842086, RAW / Funding / 2014 / Actual Commitments=8159813.768451, RAW / Funding / 2015 / Actual Commitments=1971831.841736, RAW / Totals / Actual Commitments=19408691.186388, RAW / Totals / MTEF=2147665, RAW / Totals / Pipeline MTEF=908888, RAW / Totals / Projection MTEF=163000}", 
			buildDigest(spec("AMP-21275-all-plain-mtefs-rare"), acts, new GrandTotalsDigest(z -> true)).toString());
	}
	
	@Test
	public void testMtefColumnsBehaveLikeTrivialMeasuresOnHierarchies() {
		NiReportModel cor = new NiReportModel("AMP-22422-test-mtefs-hiers")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 15))",
					"(Executing Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Contracting Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(MTEF 2010/2011: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(MTEF 2011/2012: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1));(MTEF 2012/2013: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 3))",
					"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Executing Agency", "", "Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "1 718 011", "MTEF 2012/2013", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545 000", "Funding-2015-Actual Commitments", "1 011 456", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "1 011 456", "Totals-Actual Disbursements", "768 777", "Totals-MTEF", "1 989 011")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Executing Agency", "Finland")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "40 740,48", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "40 740,48", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "Finland")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "40 740,48", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "40 740,48", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "40 740,48", "Totals-Actual Commitments", "40 740,48")          )        ),
			        new ReportAreaForTests(new AreaOwner("Executing Agency", "Ministry of Economy")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "33 888", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-MTEF", "33 888", "Executing Agency", "Ministry of Economy")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "33 888", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-MTEF", "33 888", "Contracting Agency", "Contracting Agency: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011/2012", "33 888", "Totals-MTEF", "33 888")          )        ),
			        new ReportAreaForTests(new AreaOwner("Executing Agency", "Norway")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "27 160,32", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "27 160,32", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "Norway")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "27 160,32", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "27 160,32", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "27 160,32", "Totals-Actual Commitments", "27 160,32")          )        ),
			        new ReportAreaForTests(new AreaOwner("Executing Agency", "UNDP")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Executing Agency", "UNDP")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "272 500", "Totals-Actual Disbursements", "272 500")          )        ),
			        new ReportAreaForTests(new AreaOwner("Executing Agency", "USAID")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "55 555,2", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "55 555,2", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "USAID")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "55 555,2", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "55 555,2", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "55 555,2", "Totals-Actual Commitments", "55 555,2")          )        ),
			        new ReportAreaForTests(new AreaOwner("Executing Agency", "Water Foundation")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "150 000", "MTEF 2012/2013", "65 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000", "Executing Agency", "Water Foundation")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "150 000", "MTEF 2012/2013", "65 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000", "Contracting Agency", "Contracting Agency: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011/2012", "150 000", "MTEF 2012/2013", "65 000", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000")          )        ),
			        new ReportAreaForTests(new AreaOwner("Executing Agency", "World Bank")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Executing Agency", "World Bank")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "272 500", "Totals-Actual Disbursements", "272 500")          )        ),
			        new ReportAreaForTests(new AreaOwner("Executing Agency", "Executing Agency: Undefined")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "1 534 123", "MTEF 2012/2013", "206 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "1 740 123", "Executing Agency", "Executing Agency: Undefined")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined"))
			          .withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "1 534 123", "MTEF 2012/2013", "206 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "1 740 123", "Contracting Agency", "Contracting Agency: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011/2012", "789 123", "Totals-MTEF", "789 123"),
			            new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011/2012", "700 000", "MTEF 2012/2013", "150 000", "Funding-2015-Actual Commitments", "888 000", "Totals-Actual Commitments", "888 000", "Totals-MTEF", "850 000"),
			            new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011/2012", "45 000", "MTEF 2012/2013", "56 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "101 000")          )        )      ));

		runNiTestCase(cor, spec("AMP-22422-test-mtefs-hiers"), Arrays.asList("Pure MTEF Project", "activity with directed MTEFs", "Activity with both MTEFs and Act.Comms", "activity with many MTEFs", "mtef activity 1", "Test MTEF directed", "Eth Water"));
	}
	
	@Test
	public void testProjectTitleLanguages() {
		NiReportModel correctReport = new NiReportModel("testcase EN")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
					"(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
					"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Totals-Actual Commitments", "7,181,333", "Totals-Actual Disbursements", "1,550,111")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Totals-Actual Disbursements", "545,000"),
			        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
			        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")      ));
		
		runNiTestCase(
				this.buildSpecification("testcase EN", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),						
						"en", 
						Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
						correctReport); 
				
		NiReportModel correctReportRu = new NiReportModel("testcase RU")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
					"(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
					"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Totals-Actual Commitments", "7,181,333", "Totals-Actual Disbursements", "1,550,111")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Вода Eth", "Totals-Actual Disbursements", "545,000"),
			        new ReportAreaForTests(new AreaOwner(30), "Project Title", "Проект КЮЮ 1", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
			        new ReportAreaForTests(new AreaOwner(48), "Project Title", "обещание 2", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")      ));
		
		runNiTestCase(
				buildSpecification("testcase RU", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"ru", 
				Arrays.asList("Eth Water", "SSC Project 1", "pledged 2"),
				correctReportRu);
	}
	
/*	
	
	@Test
	public void test_AMP_18499_should_fail_for_now() {
		// for running manually: open http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=73 OR http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/73
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Actual Commitments", "666 777")
	    .withChildren(new ReportAreaForTests().withContents("Project Title", "ptc activity 1", "Actual Commitments", "666 777")  );
		
		runMondrianTestCase(
				buildSpecification("AMP-18499", Arrays.asList(ColumnConstants.PROJECT_TITLE), Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en",
				Arrays.asList("Proposed Project Cost 1 - USD", "Project with documents", "ptc activity 1"),
				cor);
	}
	
	@Test
	public void test_AMP_18504_should_fail_for_now() {
		// for running manually: http://localhost:8080/aim/viewNewAdvancedReport.do~view=reset~widget=false~resetSettings=true~ampReportId=24 or http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/24
		
		ReportAreaForTests cor = new ReportAreaForTests()
	    .withContents("Project Title", "Report Totals", "Donor Agency", "", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "0", "2010-Actual Commitments", "0", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "0", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "Total Measures-Actual Commitments", "7 195 000", "Total Measures-Actual Disbursements", "522 000")
	    .withChildren(
	      new ReportAreaForTests()
	          .withContents("Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "2009-Actual Commitments", "100 000", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "60 000", "2012-Actual Commitments", "25 000", "2012-Actual Disbursements", "12 000", "2013-Actual Commitments", "", "2013-Actual Disbursements", "", "2014-Actual Commitments", "", "2014-Actual Disbursements", "", "Total Measures-Actual Commitments", "125 000", "Total Measures-Actual Disbursements", "72 000"),
	      new ReportAreaForTests()
	          .withContents("Project Title", "pledged 2", "Donor Agency", "USAID", "2009-Actual Commitments", "", "2009-Actual Disbursements", "", "2010-Actual Commitments", "", "2010-Actual Disbursements", "", "2012-Actual Commitments", "", "2012-Actual Disbursements", "", "2013-Actual Commitments", "2 670 000", "2013-Actual Disbursements", "", "2014-Actual Commitments", "4 400 000", "2014-Actual Disbursements", "450 000", "Total Measures-Actual Commitments", "7 070 000", "Total Measures-Actual Disbursements", "450 000")  );
		
		runMondrianTestCase(
				buildSpecification("AMP-18504",
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY),
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
						null, GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "pledged 2"),
				cor);
	}
*/
	
	@Test
	public void test_AMP_18509() {
		NiReportModel cor = new NiReportModel("AMP-18509")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 24))",
					"(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(AMP ID: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 3, colSpan: 18));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 21, colSpan: 3))",
					"(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 3));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 3));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 6));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 15, colSpan: 3));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 18, colSpan: 3))",
					"(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 3));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 3));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 3));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 3));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 3));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 3))",
					"(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(Actual Expenditures: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Region", "", "AMP ID", "", "Funding-2009-Q1-Actual Commitments", "100,000", "Funding-2009-Q1-Actual Disbursements", "0", "Funding-2009-Q1-Actual Expenditures", "0", "Funding-2010-Q2-Actual Commitments", "0", "Funding-2010-Q2-Actual Disbursements", "60,000", "Funding-2010-Q2-Actual Expenditures", "0", "Funding-2012-Q3-Actual Commitments", "25,000", "Funding-2012-Q3-Actual Disbursements", "0", "Funding-2012-Q3-Actual Expenditures", "0", "Funding-2012-Q4-Actual Commitments", "0", "Funding-2012-Q4-Actual Disbursements", "12,000", "Funding-2012-Q4-Actual Expenditures", "0", "Funding-2013-Q4-Actual Commitments", "2,670,000", "Funding-2013-Q4-Actual Disbursements", "0", "Funding-2013-Q4-Actual Expenditures", "0", "Funding-2014-Q2-Actual Commitments", "4,400,000", "Funding-2014-Q2-Actual Disbursements", "450,000", "Funding-2014-Q2-Actual Expenditures", "0", "Totals-Actual Commitments", "7,195,000", "Totals-Actual Disbursements", "522,000", "Totals-Actual Expenditures", "0")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "AMP ID", "872113null", "Funding-2009-Q1-Actual Commitments", "100,000", "Funding-2010-Q2-Actual Disbursements", "60,000", "Funding-2012-Q3-Actual Commitments", "25,000", "Funding-2012-Q4-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
			        new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "AMP ID", "87211347", "Funding-2013-Q4-Actual Commitments", "2,670,000", "Funding-2014-Q2-Actual Commitments", "4,400,000", "Funding-2014-Q2-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")      ));
		
		runNiTestCase(
			buildSpecification("AMP-18509", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.AMP_ID),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.ACTUAL_EXPENDITURES),
				null,
				GroupingCriteria.GROUPING_QUARTERLY),
			"en",
			Arrays.asList("date-filters-activity", "pledged 2"),
			cor);
	}
	
	@Test
	public void test_AMP_18577_only_count_donor_transactions() {
		NiReportModel cor = new NiReportModel("AMP_18577_only_count_donor_transaction")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 4))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
				"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
				"(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Region", "", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")
		      .withChildren(
		        new ReportAreaForTests(null, "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")      ));
		
		runNiTestCase(
				buildSpecification("AMP_18577_only_count_donor_transaction",
				Arrays.asList("Project Title", "Region"),
				Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY),
			"en",
			Arrays.asList("Test MTEF directed"),
			cor
		);
	}
	
	@Test
	public void test_AMP_18330_empty_rows() {
		NiReportModel cor = new NiReportModel("test_AMP_18330_empty_rows")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 4))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
				"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
				"(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Region", "", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")
		      .withChildren(
		        new ReportAreaForTests(null, "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
		        new ReportAreaForTests(null, "Project Title", "activity with primary_program", "Region", "")      ));
		
		ReportSpecificationImpl spec = buildSpecification("test_AMP_18330_empty_rows",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
				Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		spec.setDisplayEmptyFundingRows(true);
		
		runNiTestCase(spec, "en",
			Arrays.asList("Test MTEF directed", "activity with primary_program"),
			cor
		);
	}
	
	@Test
	public void test_AMP_18748_no_data() {
		NiReportModel cor = new NiReportModel("test_AMP_18748_no_data")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 3))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
				"",
				"(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Region", "", "Totals-Actual Disbursements", "0")
		      .withChildren(      ));
		
		ReportSpecificationImpl spec = buildSpecification("test_AMP_18748_no_data",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
				Arrays.asList(MeasureConstants.ACTUAL_DISBURSEMENTS),
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		runNiTestCase(spec, "en",
				Arrays.asList("__hopefully____invalid________name____"),
			cor
		);
	}
	
	@Test
	public void test_AMP_22343_Monthly_Fiscal_Calendar() {
		// tests that the headers come out with the months sorted out in the fiscal year order
		NiReportModel cor = new NiReportModel("AMP-22343-fiscal-monthly")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 9))",
					"(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 6));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 7, colSpan: 2))",
					"(Fiscal Year 2013 - 2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 6))",
					"(August: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(December: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(February: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
					"(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Funding-Fiscal Year 2013 - 2014-August-Actual Commitments", "111 333", "Funding-Fiscal Year 2013 - 2014-August-Actual Disbursements", "1 100 111", "Funding-Fiscal Year 2013 - 2014-December-Actual Commitments", "890 000", "Funding-Fiscal Year 2013 - 2014-December-Actual Disbursements", "0", "Funding-Fiscal Year 2013 - 2014-February-Actual Commitments", "75 000", "Funding-Fiscal Year 2013 - 2014-February-Actual Disbursements", "0", "Totals-Actual Commitments", "1 076 333", "Totals-Actual Disbursements", "1 100 111")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-Fiscal Year 2013 - 2014-August-Actual Disbursements", "545 000", "Totals-Actual Disbursements", "545 000"),
			        new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-Fiscal Year 2013 - 2014-August-Actual Commitments", "111 333", "Funding-Fiscal Year 2013 - 2014-August-Actual Disbursements", "555 111", "Totals-Actual Commitments", "111 333", "Totals-Actual Disbursements", "555 111"),
			        new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-Fiscal Year 2013 - 2014-December-Actual Commitments", "890 000", "Totals-Actual Commitments", "890 000"),
			        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-Fiscal Year 2013 - 2014-February-Actual Commitments", "75 000", "Totals-Actual Commitments", "75 000")      ));
		
		runNiTestCase(spec("AMP-22343-fiscal-monthly"), "en",
			Arrays.asList("Eth Water", "SSC Project 1", "Activity With Zones and Percentages", "SubNational no percentages"), 
			cor);
	}
	
	@Test
	public void test_AMP_22322_directed_mtefs_as_plain_mtefs_columns() {
		NiReportModel corPlain = new NiReportModel("AMP-22322-directed-mtefs")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 14))",
					"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(MTEF 2011/2012: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(MTEF 2012/2013: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 11, colSpan: 3))",
					"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1))"))
				.withWarnings(Arrays.asList(
					"-1: [entityId: -1, message: measure Real MTEFs not supported in NiReports]"))
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "MTEF 2011/2012", "1 718 011", "MTEF 2012/2013", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "333 777", "Totals-MTEF", "1 989 011")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011/2012", "150 000", "MTEF 2012/2013", "65 000", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000"),
			        new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011/2012", "33 888", "Totals-MTEF", "33 888"),
			        new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011/2012", "789 123", "Totals-MTEF", "789 123"),
			        new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
			        new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011/2012", "700 000", "MTEF 2012/2013", "150 000", "Funding-2015-Actual Commitments", "888 000", "Totals-Actual Commitments", "888 000", "Totals-MTEF", "850 000"),
			        new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000"),
			        new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011/2012", "45 000", "MTEF 2012/2013", "56 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "101 000")      ));

		NiReportModel corByBenf = new NiReportModel("AMP-22322-directed-mtefs-by-beneficiary")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 15))",
					"(Beneficiary Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(MTEF 2011/2012: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(MTEF 2012/2013: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 12, colSpan: 3))",
					"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(MTEF: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))"))
				.withWarnings(Arrays.asList(
					"-1: [entityId: -1, message: measure Real MTEFs not supported in NiReports]"))
				.withBody(      new ReportAreaForTests(null).withContents("Beneficiary Agency", "", "Project Title", "", "MTEF 2011/2012", "1 718 011", "MTEF 2012/2013", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "333 777", "Totals-MTEF", "1 989 011")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Beneficiary Agency", "Beneficiary Agency: Undefined"))
			        .withContents("Project Title", "", "MTEF 2011/2012", "1 718 011", "MTEF 2012/2013", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "75 000", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "333 777", "Totals-MTEF", "1 989 011", "Beneficiary Agency", "Beneficiary Agency: Undefined")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "MTEF 2011/2012", "150 000", "MTEF 2012/2013", "65 000", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000"),
			          new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "MTEF 2011/2012", "33 888", "Totals-MTEF", "33 888"),
			          new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "MTEF 2011/2012", "789 123", "Totals-MTEF", "789 123"),
			          new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011/2012", "700 000", "MTEF 2012/2013", "150 000", "Funding-2015-Actual Commitments", "888 000", "Totals-Actual Commitments", "888 000", "Totals-MTEF", "850 000"),
			          new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35 000", "Funding-2014-Actual Disbursements", "75 000", "Totals-Actual Disbursements", "110 000"),
			          new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "MTEF 2011/2012", "45 000", "MTEF 2012/2013", "56 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "101 000")        )      ));
		
		runNiTestCase(spec("AMP-22322-directed-mtefs"), "en", mtefActs, corPlain);
		runNiTestCase(spec("AMP-22322-directed-mtefs-by-beneficiary"), "en", mtefActs, corByBenf);
	}
	
	@Test
	public void testProposedProjectCost() {
		NiReportModel corPPCUSD = new NiReportModel("Proposed-cost-USD")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
					"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
					"(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Proposed Project Amount", "4 630 902,72", "Funding-2014-Actual Commitments", "172 000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "580 245", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "752 245", "Totals-Actual Disbursements", "321 765")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1 000 000"),
			        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47"),
			        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Funding-2014-Actual Commitments", "75 000", "Totals-Actual Commitments", "75 000"),
			        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "66 392,25", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000"),
			        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35 000", "Funding-2014-Actual Commitments", "32 000", "Totals-Actual Commitments", "32 000"),
			        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70 000", "Funding-2014-Actual Commitments", "15 000", "Totals-Actual Commitments", "15 000"),
			        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456 789", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "456 789", "Totals-Actual Disbursements", "321 765"),
			        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456")      ));

		runNiTestCase(spec("Proposed-cost-USD"), "en", ppcActs, corPPCUSD);
		
		NiReportModel corPPCEUR = new NiReportModel("Proposed-cost-EUR")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
					"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
					"(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Proposed Project Amount", "3 444 862", "Funding-2014-Actual Commitments", "129 533", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "529 416", "Funding-2015-Actual Disbursements", "293 578", "Totals-Actual Commitments", "658 949", "Totals-Actual Disbursements", "293 578")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "770 600"),
			        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "2 500 000"),
			        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "45 186", "Funding-2014-Actual Commitments", "56 482", "Totals-Actual Commitments", "56 482"),
			        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "50 000", "Funding-2014-Actual Commitments", "37 655", "Totals-Actual Commitments", "37 655"),
			        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "26 358", "Funding-2014-Actual Commitments", "24 099", "Totals-Actual Commitments", "24 099"),
			        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "52 717", "Funding-2014-Actual Commitments", "11 296", "Totals-Actual Commitments", "11 296"),
			        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "416 774", "Funding-2015-Actual Disbursements", "293 578", "Totals-Actual Commitments", "416 774", "Totals-Actual Disbursements", "293 578"),
			        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "112 641", "Totals-Actual Commitments", "112 641")      ));

		runNiTestCase(spec("Proposed-cost-EUR"), "en", ppcActs, corPPCEUR);
	}
	
	@Test
	public void testAnnualProposedProjectCost() {
		NiReportModel corAnnualPPC = new NiReportModel("Annual-Proposed-Project-Cost")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
					"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
					"(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Annual Proposed Project Cost: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Proposed Project Amount", "4 630 902,72", "Funding-2012-Actual Commitments", "0", "Funding-2012-Annual Proposed Project Cost", "350 000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Annual Proposed Project Cost", "726 072,61", "Funding-2014-Actual Commitments", "172 000", "Funding-2014-Annual Proposed Project Cost", "3 382 784,49", "Funding-2015-Actual Commitments", "580 245", "Funding-2015-Annual Proposed Project Cost", "0", "Totals-Actual Commitments", "752 245", "Totals-Annual Proposed Project Cost", "4 458 857,1")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1 000 000", "Funding-2012-Annual Proposed Project Cost", "350 000", "Funding-2013-Annual Proposed Project Cost", "726 072,61", "Funding-2014-Annual Proposed Project Cost", "132 784,49", "Totals-Annual Proposed Project Cost", "1 208 857,1"),
			        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47", "Funding-2014-Annual Proposed Project Cost", "3 250 000", "Totals-Annual Proposed Project Cost", "3 250 000"),
			        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Funding-2014-Actual Commitments", "75 000", "Totals-Actual Commitments", "75 000"),
			        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "66 392,25", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000"),
			        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35 000", "Funding-2014-Actual Commitments", "32 000", "Totals-Actual Commitments", "32 000"),
			        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70 000", "Funding-2014-Actual Commitments", "15 000", "Totals-Actual Commitments", "15 000"),
			        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456 789", "Totals-Actual Commitments", "456 789"),
			        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456")      ));

		runNiTestCase(spec("Annual-Proposed-Project-Cost"), "en", ppcActs, corAnnualPPC);
	}
	
	@Test
	public void testRevisedProjectCost() {
		NiReportModel cor =  new NiReportModel("AMP-22375-revised-project-cost")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 9))",
					"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Proposed Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Revised Project Amount: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 7, colSpan: 2))",
					"(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Proposed Project Amount", "4 630 902,72", "Revised Project Amount", "4 412 539,84", "Funding-2014-Actual Commitments", "172 000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "580 245", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "752 245", "Totals-Actual Disbursements", "321 765")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Proposed Project Amount", "1 000 000", "Revised Project Amount", "1 217 000"),
			        new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Proposed Project Amount", "3 399 510,47", "Revised Project Amount", "3 195 539,84"),
			        new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Proposed Project Amount", "60 000", "Funding-2014-Actual Commitments", "75 000", "Totals-Actual Commitments", "75 000"),
			        new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Proposed Project Amount", "66 392,25", "Funding-2014-Actual Commitments", "50 000", "Totals-Actual Commitments", "50 000"),
			        new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Proposed Project Amount", "35 000", "Funding-2014-Actual Commitments", "32 000", "Totals-Actual Commitments", "32 000"),
			        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Proposed Project Amount", "70 000", "Funding-2014-Actual Commitments", "15 000", "Totals-Actual Commitments", "15 000"),
			        new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456 789", "Funding-2015-Actual Disbursements", "321 765", "Totals-Actual Commitments", "456 789", "Totals-Actual Disbursements", "321 765"),
			        new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123 456", "Totals-Actual Commitments", "123 456")));

		runNiTestCase(spec("AMP-22375-revised-project-cost"), "en", ppcActs, cor);
	}
}
