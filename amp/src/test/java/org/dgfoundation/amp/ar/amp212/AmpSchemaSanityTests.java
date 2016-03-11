package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
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
	
	public AmpSchemaSanityTests() {
		super("AmpReportsSchema sanity tests");
	}
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getDbExecutor(activityNames);
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
				"(Executing Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Contracting Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(MTEF 2010/2011: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(MTEF 2011/2012: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1));(MTEF 2012/2013: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 5, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 6));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 12, colSpan: 3))",
				"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2));(Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 12, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 13, colSpan: 1));(MTEF: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 14, colSpan: 1))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Executing Agency", "", "Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "1 718 011", "MTEF 2012/2013", "271 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "545 000", "Funding-2015-Actual Commitments", "1 011 456", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "1 011 456", "Totals-Actual Disbursements", "768 777", "Totals-MTEF", "1 989 011")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner("Executing Agency", "Finland")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "40 740,48", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "40 740,48", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "Finland")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "40 740,48", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "40 740,48", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
		          .withChildren(
		            new ReportAreaForTests(null, "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "40 740,48", "Totals-Actual Commitments", "40 740,48")          )        ),
		        new ReportAreaForTests(new AreaOwner("Executing Agency", "Ministry of Economy")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "33 888", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-MTEF", "33 888", "Executing Agency", "Ministry of Economy")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "33 888", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-MTEF", "33 888", "Contracting Agency", "Contracting Agency: Undefined")
		          .withChildren(
		            new ReportAreaForTests(null, "Project Title", "Pure MTEF Project", "MTEF 2011/2012", "33 888", "Totals-MTEF", "33 888")          )        ),
		        new ReportAreaForTests(new AreaOwner("Executing Agency", "Norway")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "27 160,32", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "27 160,32", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "Norway")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "27 160,32", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "27 160,32", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
		          .withChildren(
		            new ReportAreaForTests(null, "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "27 160,32", "Totals-Actual Commitments", "27 160,32")          )        ),
		        new ReportAreaForTests(new AreaOwner("Executing Agency", "UNDP")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Executing Agency", "UNDP")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
		          .withChildren(
		            new ReportAreaForTests(null, "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "272 500", "Totals-Actual Disbursements", "272 500")          )        ),
		        new ReportAreaForTests(new AreaOwner("Executing Agency", "USAID")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "55 555,2", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "55 555,2", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Executing Agency", "USAID")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "55 555,2", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "55 555,2", "Totals-Actual Disbursements", "0", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
		          .withChildren(
		            new ReportAreaForTests(null, "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "55 555,2", "Totals-Actual Commitments", "55 555,2")          )        ),
		        new ReportAreaForTests(new AreaOwner("Executing Agency", "Water Foundation")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "150 000", "MTEF 2012/2013", "65 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000", "Executing Agency", "Water Foundation")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "150 000", "MTEF 2012/2013", "65 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143 777", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000", "Contracting Agency", "Contracting Agency: Undefined")
		          .withChildren(
		            new ReportAreaForTests(null, "Project Title", "Test MTEF directed", "MTEF 2011/2012", "150 000", "MTEF 2012/2013", "65 000", "Funding-2010-Actual Disbursements", "143 777", "Totals-Actual Disbursements", "143 777", "Totals-MTEF", "215 000")          )        ),
		        new ReportAreaForTests(new AreaOwner("Executing Agency", "World Bank")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Executing Agency", "World Bank")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined")).withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "0", "MTEF 2012/2013", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "272 500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "272 500", "Totals-MTEF", "0", "Contracting Agency", "Contracting Agency: Undefined")
		          .withChildren(
		            new ReportAreaForTests(null, "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "272 500", "Totals-Actual Disbursements", "272 500")          )        ),
		        new ReportAreaForTests(new AreaOwner("Executing Agency", "Executing Agency: Undefined")).withContents("Contracting Agency", "", "Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "1 534 123", "MTEF 2012/2013", "206 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "1 740 123", "Executing Agency", "Executing Agency: Undefined")
		        .withChildren(
		          new ReportAreaForTests(new AreaOwner("Contracting Agency", "Contracting Agency: Undefined"))
		          .withContents("Project Title", "", "MTEF 2010/2011", "0", "MTEF 2011/2012", "1 534 123", "MTEF 2012/2013", "206 000", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "888 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Commitments", "888 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "1 740 123", "Contracting Agency", "Contracting Agency: Undefined")
		          .withChildren(
		            new ReportAreaForTests(null, "Project Title", "Activity with both MTEFs and Act.Comms", "MTEF 2011/2012", "700 000", "MTEF 2012/2013", "150 000", "Funding-2015-Actual Commitments", "888 000", "Totals-Actual Commitments", "888 000", "Totals-MTEF", "850 000"),
		            new ReportAreaForTests(null, "Project Title", "mtef activity 1", "MTEF 2011/2012", "789 123", "Totals-MTEF", "789 123"),
		            new ReportAreaForTests(null, "Project Title", "activity with many MTEFs", "MTEF 2011/2012", "45 000", "MTEF 2012/2013", "56 000", "Funding-2015-Actual Disbursements", "80 000", "Totals-Actual Disbursements", "80 000", "Totals-MTEF", "101 000")          )        )      ));

		runNiTestCase(cor, spec("AMP-22422-test-mtefs-hiers"), Arrays.asList("Pure MTEF Project", "activity with directed MTEFs", "Activity with both MTEFs and Act.Comms", "activity with many MTEFs", "mtef activity 1", "Test MTEF directed", "Eth Water"));
	}
}
