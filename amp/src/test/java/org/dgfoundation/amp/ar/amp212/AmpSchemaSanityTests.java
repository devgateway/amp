package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.dgfoundation.amp.reports.mondrian.converters.AmpReportsToReportSpecification;
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
		ReportSpecification spec = AmpReportsToReportSpecification.convert(ReportTestingUtils.loadReportByName("AMP-16100-flat-mtefs-eur"));
//		assertEquals("", buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		assertEquals("{RAW / Project Title=, RAW / MTEF 2011/2012=1283182.4159, RAW / MTEF 2012/2013=202437, RAW / MTEF 2013/2014=120180.405, RAW / Funding / 2006 / Actual Commitments=80000, RAW / Funding / 2006 / Actual Disbursements=0, RAW / Funding / 2009 / Actual Commitments=78470, RAW / Funding / 2009 / Actual Disbursements=0, RAW / Funding / 2010 / Actual Commitments=0, RAW / Funding / 2010 / Actual Disbursements=613561.3161, RAW / Funding / 2011 / Actual Commitments=896327.2977, RAW / Funding / 2011 / Actual Disbursements=0, RAW / Funding / 2012 / Actual Commitments=19577.5, RAW / Funding / 2012 / Actual Disbursements=9162, RAW / Funding / 2013 / Actual Commitments=5905874.9666, RAW / Funding / 2013 / Actual Disbursements=954144.5636, RAW / Funding / 2014 / Actual Commitments=7409649.482335, RAW / Funding / 2014 / Actual Disbursements=576269.62, RAW / Funding / 2015 / Actual Commitments=1803396.8724, RAW / Funding / 2015 / Actual Disbursements=399024.454, RAW / Totals / Actual Commitments=16193296.119035, RAW / Totals / Actual Disbursements=2552161.9537, RAW / Totals / MTEF=1605799.8209}", 
			buildDigest(spec, acts, new GrandTotalsDigest(z -> true)).toString());
	}
	
	@Test
	public void testMtefColumnsMixedPlain() {
		ReportSpecification spec = AmpReportsToReportSpecification.convert(ReportTestingUtils.loadReportByName("AMP-21275-all-plain-mtefs"));
//		assertEquals("", buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		assertEquals("{RAW / Project Title=, RAW / MTEF 2011/2012=1718011, RAW / Pipeline MTEF Projections 2011/2012=908888, RAW / Projection MTEF Projections 2011/2012=809123, RAW / MTEF 2012/2013=271000, RAW / Pipeline MTEF Projections 2012/2013=108000, RAW / Projection MTEF Projections 2012/2013=163000, RAW / MTEF 2013/2014=158654, RAW / Pipeline MTEF Projections 2013/2014=158654, RAW / Projection MTEF Projections 2013/2014=0, RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2009 / Actual Commitments=100000, RAW / Funding / 2011 / Actual Commitments=1213119, RAW / Funding / 2012 / Actual Commitments=25000, RAW / Funding / 2013 / Actual Commitments=7842086, RAW / Funding / 2014 / Actual Commitments=8159813.768451, RAW / Funding / 2015 / Actual Commitments=1971831.841736, RAW / Totals / Actual Commitments=19408691.186388, RAW / Totals / MTEF=2147665, RAW / Totals / Pipeline MTEF=1175542, RAW / Totals / Projection MTEF=972123}", 
			buildDigest(spec, acts, new GrandTotalsDigest(z -> true)).toString());
	}

	@Test
	public void testMtefColumnsMixedPlain2() {
		ReportSpecification spec = AmpReportsToReportSpecification.convert(ReportTestingUtils.loadReportByName("AMP-21275-all-plain-mtefs-rare"));
//		assertEquals("", buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		assertEquals("{RAW / Project Title=, RAW / MTEF 2011/2012=1718011, RAW / Pipeline MTEF Projections 2011/2012=908888, RAW / MTEF 2012/2013=271000, RAW / Projection MTEF Projections 2012/2013=163000, RAW / MTEF 2013/2014=158654, RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2009 / Actual Commitments=100000, RAW / Funding / 2011 / Actual Commitments=1213119, RAW / Funding / 2012 / Actual Commitments=25000, RAW / Funding / 2013 / Actual Commitments=7842086, RAW / Funding / 2014 / Actual Commitments=8159813.768451, RAW / Funding / 2015 / Actual Commitments=1971831.841736, RAW / Totals / Actual Commitments=19408691.186388, RAW / Totals / MTEF=2147665, RAW / Totals / Pipeline MTEF=908888, RAW / Totals / Projection MTEF=163000}", 
			buildDigest(spec, acts, new GrandTotalsDigest(z -> true)).toString());
	}
}
