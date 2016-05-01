package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.ExceptionRunnable;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.NamedTypedEntity;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.newreports.SortingInfo;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.TrailCellsDigest;
import org.dgfoundation.amp.nireports.output.NiReportOutputBuilder;
import org.dgfoundation.amp.nireports.output.NiReportsFormatter;
import org.dgfoundation.amp.testmodels.HardcodedActivities;
import org.dgfoundation.amp.testmodels.NiReportModel;
import org.dgfoundation.amp.testmodels.ReportModelGenerator;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.junit.Test;

/**
 * basic sanity checks common between both the offdb schema and the AmpReportsSchema-using one.
 * These are not supposed to be exhaustive tests; instead they are concerned about "no stupid or weird things happening"
 * @author Dolghier Constantin
 *
 */
public abstract class BasicSanityChecks extends ReportingTestCase {
			
	public BasicSanityChecks(String name) {
		super(name);
	}
	
	final List<String> acts = Arrays.asList(
			"activity 1 with agreement",
			"Activity 2 with multiple agreements",
			"Activity Linked With Pledge",
			"Activity with both MTEFs and Act.Comms",
			"activity with capital spending",
			"activity with components",
			"activity with contracting agency",
			"activity with directed MTEFs",
			"activity_with_disaster_response",
			"activity with funded components",
			"activity with incomplete agreement",
			"activity with many MTEFs",
			"activity with pipeline MTEFs and act. disb",
			"Activity with planned disbursements",
			"activity with primary_program",
			"Activity with primary_tertiary_program",
			"activity with tertiary_program",
			"activity-with-unfunded-components",
			"Activity with Zones",
			"Activity With Zones and Percentages",
			"crazy funding 1",
			"date-filters-activity",
			"Eth Water",
			"execution rate activity",
			"mtef activity 1",
			"mtef activity 2",
			"new activity with contracting",
			"pledged 2",
			"pledged education activity 1",
			"Project with documents",
			"Proposed Project Cost 1 - USD",
			"Proposed Project Cost 2 - EUR",
			"ptc activity 1",
			"ptc activity 2",
			"Pure MTEF Project",
			"SSC Project 1",
			"SSC Project 2",
			"SubNational no percentages",
			"TAC_activity_1",
			"TAC_activity_2",
			"Test MTEF directed",
			"third activity with agreements",
			"Unvalidated activity",
			"with weird currencies"
		);

	final List<String> hierarchiesToTry = Arrays.asList(
			ColumnConstants.STATUS, ColumnConstants.IMPLEMENTATION_LEVEL, 
			ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, 
			ColumnConstants.SECONDARY_SECTOR, ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR,
			ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, ColumnConstants.PRIMARY_PROGRAM_LEVEL_2,
			ColumnConstants.COUNTRY, ColumnConstants.REGION, ColumnConstants.ZONE, ColumnConstants.DISTRICT,
			ColumnConstants.IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, ColumnConstants.IMPLEMENTING_AGENCY_TYPE,
			ColumnConstants.DONOR_AGENCY, ColumnConstants.DONOR_GROUP, ColumnConstants.DONOR_TYPE,
			ColumnConstants.FINANCING_INSTRUMENT, ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.MODE_OF_PAYMENT, ColumnConstants.FUNDING_STATUS);

//	protected ReportSpecificationImpl buildSpecification(String reportName, List<String> columns, List<String> measures, List<String> hierarchies, GroupingCriteria groupingCriteria) {
//		return ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria);
//	}
		
	@Test
	public void testPlainReportTotals() {
		ReportSpecification spec = buildSpecification("plain", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
				null, 
				GroupingCriteria.GROUPING_YEARLY);
		
		assertEquals("{(root)=19408691.186388}", buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		assertEquals("{(root)=8159813.768451}", buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Commitments")).toString());
		
		spec = buildSpecification("plain", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
				
		assertEquals("{(root)=19408691.186388}", buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
	}
	
	@Test
	public void testByRegionReportTotals() { //CHECKED
		ReportSpecification spec = buildSpecification("ByRegion", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.REGION), 
			GroupingCriteria.GROUPING_YEARLY);
		
		assertEquals("{(root) -> Anenii Noi County=1611832, (root) -> Balti County=2144284.31691055, (root) -> Cahul County=7070000, (root) -> Chisinau City=296912, (root) -> Chisinau County=5066960.631302, (root) -> Drochia County=621600, (root) -> Dubasari County=213231, (root) -> Edinet County=567421, (root) -> Falesti County=999888, (root) -> Transnistrian Region=166899.25929045, (root) -> =649662.978885, (root)=19408691.186388}", 
				buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());

		assertEquals("{(root) -> Anenii Noi County=37500, (root) -> Balti County=37500, (root) -> Cahul County=4400000, (root) -> Chisinau City=50000, (root) -> Chisinau County=3365760.631302, (root) -> Drochia County=0, (root) -> Dubasari County=0, (root) -> Edinet County=0, (root) -> Falesti County=0, (root) -> Transnistrian Region=123321, (root) -> =145732.137149, (root)=8159813.768451}", 
				buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Commitments")).toString());

		assertEquals("{(root) -> Anenii Noi County=1100111, (root) -> Balti County=0, (root) -> Cahul County=0, (root) -> Chisinau City=0, (root) -> Chisinau County=35000, (root) -> Drochia County=0, (root) -> Dubasari County=0, (root) -> Edinet County=131845, (root) -> Falesti County=0, (root) -> Transnistrian Region=0, (root) -> =0, (root)=1266956}",
				buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2013 / Actual Disbursements")).toString());
	}
	
	@Test
	public void testByRegionByZoneReportTotals() { //CHECKED
		ReportSpecification spec = buildSpecification("ByRegionByZone", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.ZONE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.REGION, ColumnConstants.ZONE), 
			GroupingCriteria.GROUPING_YEARLY);

		assertEquals(
			"{(root) -> Anenii Noi County -> Bulboaca=285000, (root) -> Anenii Noi County -> Dolboaca=178000, (root) -> Anenii Noi County -> =1148832, (root) -> Anenii Noi County=1611832, (root) -> Balti County -> Apareni=53262.31691055, (root) -> Balti County -> Glodeni=1491289, (root) -> Balti County -> =599733, (root) -> Balti County=2144284.31691055, (root) -> Cahul County -> =7070000, (root) -> Cahul County=7070000, (root) -> Chisinau City -> =296912, (root) -> Chisinau City=296912, (root) -> Chisinau County -> =5066960.631302, (root) -> Chisinau County=5066960.631302, " + 
			"(root) -> Drochia County -> =621600, (root) -> Drochia County=621600, (root) -> Dubasari County -> =213231, (root) -> Dubasari County=213231, (root) -> Edinet County -> =567421, (root) -> Edinet County=567421, (root) -> Falesti County -> =999888, (root) -> Falesti County=999888, (root) -> Transnistrian Region -> Slobozia=43578.25929045, (root) -> Transnistrian Region -> Tiraspol=123321, (root) -> Transnistrian Region=166899.25929045, (root) ->  -> =649662.978885, (root) -> =649662.978885, (root)=19408691.186388}", 
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> Anenii Noi County -> Bulboaca=0, (root) -> Anenii Noi County -> Dolboaca=0, (root) -> Anenii Noi County -> =37500, (root) -> Anenii Noi County=37500, (root) -> Balti County -> Apareni=0, (root) -> Balti County -> Glodeni=37500, (root) -> Balti County -> =0, (root) -> Balti County=37500, (root) -> Cahul County -> =4400000, (root) -> Cahul County=4400000, (root) -> Chisinau City -> =50000, (root) -> Chisinau City=50000, (root) -> Chisinau County -> =3365760.631302, (root) -> Chisinau County=3365760.631302, " +
			"(root) -> Drochia County -> =0, (root) -> Drochia County=0, (root) -> Dubasari County -> =0, (root) -> Dubasari County=0, (root) -> Edinet County -> =0, (root) -> Edinet County=0, (root) -> Falesti County -> =0, (root) -> Falesti County=0, (root) -> Transnistrian Region -> Slobozia=0, (root) -> Transnistrian Region -> Tiraspol=123321, (root) -> Transnistrian Region=123321, (root) ->  -> =145732.137149, (root) -> =145732.137149, (root)=8159813.768451}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Commitments")).toString());
		
		
		assertEquals(
			"{(root) -> Anenii Noi County -> Bulboaca=0, (root) -> Anenii Noi County -> Dolboaca=0, (root) -> Anenii Noi County -> =1100111, (root) -> Anenii Noi County=1100111, (root) -> Balti County -> Apareni=0, (root) -> Balti County -> Glodeni=0, (root) -> Balti County -> =0, (root) -> Balti County=0, (root) -> Cahul County -> =0, (root) -> Cahul County=0, (root) -> Chisinau City -> =0, (root) -> Chisinau City=0, (root) -> Chisinau County -> =35000, (root) -> Chisinau County=35000, " + 
			"(root) -> Drochia County -> =0, (root) -> Drochia County=0, (root) -> Dubasari County -> =0, (root) -> Dubasari County=0, (root) -> Edinet County -> =131845, (root) -> Edinet County=131845, (root) -> Falesti County -> =0, (root) -> Falesti County=0, (root) -> Transnistrian Region -> Slobozia=0, (root) -> Transnistrian Region -> Tiraspol=0, (root) -> Transnistrian Region=0, (root) ->  -> =0, (root) -> =0, (root)=1266956}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2013 / Actual Disbursements")).toString());
	}

	@Test
	public void testByZoneReportTotals() { //CHECKED
		ReportSpecification spec = buildSpecification("ByZone", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ZONE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.ZONE), 
			GroupingCriteria.GROUPING_YEARLY);
				
		assertEquals("{(root) -> Apareni=53262.31691055, (root) -> Bulboaca=285000, (root) -> Dolboaca=178000, (root) -> Glodeni=1491289, (root) -> Slobozia=43578.25929045, (root) -> Tiraspol=123321, (root) -> =17234240.610187, (root)=19408691.186388}", 
				buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals("{(root) -> Apareni=27500, (root) -> Bulboaca=0, (root) -> Dolboaca=0, (root) -> Glodeni=0, (root) -> Slobozia=22500, (root) -> Tiraspol=0, (root) -> =660200, (root)=710200}", 
				buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Disbursements")).toString());
	}
	
	@Test
	public void testByZoneByRegionReportTotals() { //CHECKED
		ReportSpecification spec = buildSpecification("ByZoneByRegion", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ZONE, ColumnConstants.REGION), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.ZONE, ColumnConstants.REGION), 
			GroupingCriteria.GROUPING_YEARLY);

		assertEquals(
			"{(root) -> Apareni -> Balti County=53262.31691055, (root) -> Apareni=53262.31691055, (root) -> Bulboaca -> Anenii Noi County=285000, (root) -> Bulboaca=285000, (root) -> Dolboaca -> Anenii Noi County=178000, (root) -> Dolboaca=178000, (root) -> Glodeni -> Balti County=1491289, (root) -> Glodeni=1491289, (root) -> Slobozia -> Transnistrian Region=43578.25929045, (root) -> Slobozia=43578.25929045, " + 
			"(root) -> Tiraspol -> Transnistrian Region=123321, (root) -> Tiraspol=123321, (root) ->  -> Anenii Noi County=1148832, (root) ->  -> Balti County=599733, (root) ->  -> Cahul County=7070000, (root) ->  -> Chisinau City=296912, (root) ->  -> Chisinau County=5066960.631302, (root) ->  -> Drochia County=621600, (root) ->  -> Dubasari County=213231, (root) ->  -> Edinet County=567421, (root) ->  -> Falesti County=999888, (root) ->  -> =649662.978885, (root) -> =17234240.610187, (root)=19408691.186388}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());

		assertEquals(
			"{(root) -> Apareni -> Balti County=27500, (root) -> Apareni=27500, (root) -> Bulboaca -> Anenii Noi County=0, (root) -> Bulboaca=0, (root) -> Dolboaca -> Anenii Noi County=0, (root) -> Dolboaca=0, (root) -> Glodeni -> Balti County=0, (root) -> Glodeni=0, (root) -> Slobozia -> Transnistrian Region=22500, (root) -> Slobozia=22500, " +
			"(root) -> Tiraspol -> Transnistrian Region=0, (root) -> Tiraspol=0, (root) ->  -> Anenii Noi County=0, (root) ->  -> Balti County=0, (root) ->  -> Cahul County=450000, (root) ->  -> Chisinau City=27500, (root) ->  -> Chisinau County=155000, (root) ->  -> Drochia County=0, (root) ->  -> Dubasari County=27500, (root) ->  -> Edinet County=0, (root) ->  -> Falesti County=0, (root) ->  -> =200, (root) -> =660200, (root)=710200}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Disbursements")).toString());

		assertEquals(
			"{(root) -> Apareni -> Balti County=0, (root) -> Apareni=0, (root) -> Bulboaca -> Anenii Noi County=285000, (root) -> Bulboaca=285000, (root) -> Dolboaca -> Anenii Noi County=178000, (root) -> Dolboaca=178000, (root) -> Glodeni -> Balti County=997000, (root) -> Glodeni=997000, (root) -> Slobozia -> Transnistrian Region=0, (root) -> Slobozia=0, " + 
			"(root) -> Tiraspol -> Transnistrian Region=0, (root) -> Tiraspol=0, (root) ->  -> Anenii Noi County=1111332, (root) ->  -> Balti County=333333, (root) ->  -> Cahul County=2670000, (root) ->  -> Chisinau City=0, (root) ->  -> Chisinau County=1700000, (root) ->  -> Drochia County=0, (root) ->  -> Dubasari County=0, (root) ->  -> Edinet County=567421, (root) ->  -> Falesti County=0, (root) ->  -> =0, (root) -> =6382086, (root)=7842086}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2013 / Actual Commitments")).toString());
	}

	@Test
	public void testByTypeOfAssistance() { //CHECKED
		ReportSpecification spec = buildSpecification("ByTypeOfAssistance_", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TYPE_OF_ASSISTANCE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.TYPE_OF_ASSISTANCE), 
			GroupingCriteria.GROUPING_YEARLY);

		assertEquals(
			"{(root) -> default type of assistance=11927387.555086, (root) -> second type of assistance=7481303.631302, (root)=19408691.186388}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> default type of assistance=2676802, (root) -> second type of assistance=530000, (root)=3206802}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Disbursements")).toString());
	}
	
	@Test
	public void testByZoneByPrimarySector() {
		ReportSpecification spec = buildSpecification("ByZoneByPrimarySector", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ZONE, ColumnConstants.PRIMARY_SECTOR), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.ZONE, ColumnConstants.PRIMARY_SECTOR), 
			GroupingCriteria.GROUPING_YEARLY);
		
		assertEquals(
			"{(root) -> Apareni -> 110 - EDUCATION=31957.39014633, (root) -> Apareni -> 112 - BASIC EDUCATION=5326.231691055, (root) -> Apareni -> 120 - HEALTH=15978.695073165, (root) -> Apareni=53262.31691055, (root) -> Bulboaca -> 110 - EDUCATION=285000, (root) -> Bulboaca=285000, (root) -> Dolboaca -> 110 - EDUCATION=53400, (root) -> Dolboaca -> 120 - HEALTH=124600, (root) -> Dolboaca=178000, (root) -> Glodeni -> 110 - EDUCATION=764494.5, (root) -> Glodeni -> 112 - BASIC EDUCATION=228394.5, (root) -> Glodeni -> 120 - HEALTH=498400, (root) -> Glodeni=1491289, (root) -> Slobozia -> 110 - EDUCATION=26146.95557427, (root) -> Slobozia -> 112 - BASIC EDUCATION=4357.825929045, (root) -> Slobozia -> 120 - HEALTH=13073.477787135, (root) -> Slobozia=43578.25929045, " + 
			"(root) -> Tiraspol -> 110 - EDUCATION=123321, (root) -> Tiraspol=123321, (root) ->  -> 110 - EDUCATION=7946744.3122985, (root) ->  -> 112 - BASIC EDUCATION=1145608.2978885, (root) ->  -> 113 - SECONDARY EDUCATION=7130000, (root) ->  -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH=999888, (root) ->  -> =12000, (root) -> =17234240.610187, (root)=19408691.186388}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> Apareni -> 110 - EDUCATION=16500, (root) -> Apareni -> 112 - BASIC EDUCATION=2750, (root) -> Apareni -> 120 - HEALTH=8250, (root) -> Apareni=27500, (root) -> Bulboaca -> 110 - EDUCATION=0, (root) -> Bulboaca=0, (root) -> Dolboaca -> 110 - EDUCATION=0, (root) -> Dolboaca -> 120 - HEALTH=0, (root) -> Dolboaca=0, (root) -> Glodeni -> 110 - EDUCATION=160882.5, (root) -> Glodeni -> 112 - BASIC EDUCATION=160882.5, (root) -> Glodeni -> 120 - HEALTH=0, (root) -> Glodeni=321765, (root) -> Slobozia -> 110 - EDUCATION=13500, (root) -> Slobozia -> 112 - BASIC EDUCATION=2250, (root) -> Slobozia -> 120 - HEALTH=6750, (root) -> Slobozia=22500, " + 
			"(root) -> Tiraspol -> 110 - EDUCATION=0, (root) -> Tiraspol=0, (root) ->  -> 110 - EDUCATION=1675888, (root) ->  -> 112 - BASIC EDUCATION=255936, (root) ->  -> 113 - SECONDARY EDUCATION=450000, (root) ->  -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH=453213, (root) ->  -> =0, (root) -> =2835037, (root)=3206802}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Disbursements")).toString());
	}

	/**
	 * checks that several activities output amounts and dates	
	 */
//	@Test
//	public void testWithDates() {
//		ReportSpecification spec = buildSpecification("ByActivityUpdateOnByActivityCreatedOn", 
//			Arrays.asList(ColumnConstants.ACTIVITY_UPDATED_ON, ColumnConstants.ACTIVITY_CREATED_ON), 
//			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
//			null, 
//			GroupingCriteria.GROUPING_YEARLY);
//		assertEquals(
//			"{0=1200, 1=123456, 2=97562.978885, 3=[2015-03-22], 4=[2015-03-22], 5=[2015-12-15], 6=1200, 7=123456, 8=93930.841736, 9=[2015-03-22], 10=[2015-03-22], 11=[2015-12-15], 12=3632.137149}",
//			buildDigest(spec, acts, new RawDataDigest(new HashSet<>(Arrays.asList(79l, 67L, 66L)))).toString());
//	}	
	
	@Test
	public void testByRegionByPrimarySectorByZone() {
		ReportSpecification spec = buildSpecification("ByRegionByPrimarySectorByZone", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.ZONE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.ZONE), 
			GroupingCriteria.GROUPING_YEARLY);
		
		assertEquals(
			"{(root) -> Anenii Noi County -> 110 - EDUCATION -> Bulboaca=285000, (root) -> Anenii Noi County -> 110 - EDUCATION -> Dolboaca=53400, (root) -> Anenii Noi County -> 110 - EDUCATION -> =1148832, (root) -> Anenii Noi County -> 110 - EDUCATION=1487232, (root) -> Anenii Noi County -> 120 - HEALTH -> Dolboaca=124600, (root) -> Anenii Noi County -> 120 - HEALTH=124600, (root) -> Anenii Noi County=1611832, (root) -> Balti County -> 110 - EDUCATION -> Apareni=31957.39014633, (root) -> Balti County -> 110 - EDUCATION -> Glodeni=764494.5, (root) -> Balti County -> 110 - EDUCATION -> =493173, (root) -> Balti County -> 110 - EDUCATION=1289624.89014633, (root) -> Balti County -> 112 - BASIC EDUCATION -> Apareni=5326.231691055, (root) -> Balti County -> 112 - BASIC EDUCATION -> Glodeni=228394.5, (root) -> Balti County -> 112 - BASIC EDUCATION -> =106560, (root) -> Balti County -> 112 - BASIC EDUCATION=340280.731691055, (root) -> Balti County -> 120 - HEALTH -> Apareni=15978.695073165, (root) -> Balti County -> 120 - HEALTH -> Glodeni=498400, (root) -> Balti County -> 120 - HEALTH=514378.695073165, (root) -> Balti County=2144284.31691055, (root) -> Cahul County -> 113 - SECONDARY EDUCATION -> =7070000, (root) -> Cahul County -> 113 - SECONDARY EDUCATION=7070000, (root) -> Cahul County=7070000, (root) -> Chisinau City -> 110 - EDUCATION -> =296912, (root) -> Chisinau City -> 110 - EDUCATION=296912, (root) -> Chisinau City=296912, (root) -> Chisinau County -> 110 - EDUCATION -> =5066960.631302, (root) -> Chisinau County -> 110 - EDUCATION=5066960.631302, (root) -> Chisinau County=5066960.631302, (root) -> Drochia County -> 110 - EDUCATION -> =372960, (root) -> Drochia County -> 110 - EDUCATION=372960, (root) -> Drochia County -> 112 - BASIC EDUCATION -> =248640, (root) -> Drochia County -> 112 - BASIC EDUCATION=248640, (root) -> Drochia County=621600, (root) -> Dubasari County -> 110 - EDUCATION -> =0, (root) -> Dubasari County -> 110 - EDUCATION=0, (root) -> Dubasari County -> 112 - BASIC EDUCATION -> =213231, (root) -> Dubasari County -> 112 - BASIC EDUCATION=213231, (root) -> Dubasari County=213231, (root) -> Edinet County -> 112 - BASIC EDUCATION -> =567421, (root) -> Edinet County -> 112 - BASIC EDUCATION=567421, (root) -> Edinet County=567421, (root) -> Falesti County -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH -> =999888, (root) -> Falesti County -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH=999888, (root) -> Falesti County=999888, (root) -> Transnistrian Region -> 110 - EDUCATION -> Slobozia=26146.95557427, (root) -> Transnistrian Region -> 110 - EDUCATION -> Tiraspol=123321, (root) -> Transnistrian Region -> 110 - EDUCATION=149467.95557427, (root) -> Transnistrian Region -> 112 - BASIC EDUCATION -> Slobozia=4357.825929045, (root) -> Transnistrian Region -> 112 - BASIC EDUCATION=4357.825929045, (root) -> Transnistrian Region -> 120 - HEALTH -> Slobozia=13073.477787135, (root) -> Transnistrian Region -> 120 - HEALTH=13073.477787135, (root) -> Transnistrian Region=166899.25929045, (root) ->  -> 110 - EDUCATION -> =567906.6809965, (root) ->  -> 110 - EDUCATION=567906.6809965, (root) ->  -> 112 - BASIC EDUCATION -> =9756.2978885, (root) ->  -> 112 - BASIC EDUCATION=9756.2978885, (root) ->  -> 113 - SECONDARY EDUCATION -> =60000, (root) ->  -> 113 - SECONDARY EDUCATION=60000, (root) ->  ->  -> =12000, (root) ->  -> =12000, (root) -> =649662.978885, (root)=19408691.186388}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> Anenii Noi County -> 110 - EDUCATION -> Bulboaca=0, (root) -> Anenii Noi County -> 110 - EDUCATION -> Dolboaca=0, (root) -> Anenii Noi County -> 110 - EDUCATION -> =1243888, (root) -> Anenii Noi County -> 110 - EDUCATION=1243888, (root) -> Anenii Noi County -> 120 - HEALTH -> Dolboaca=0, (root) -> Anenii Noi County -> 120 - HEALTH=0, (root) -> Anenii Noi County=1243888, (root) -> Balti County -> 110 - EDUCATION -> Apareni=16500, (root) -> Balti County -> 110 - EDUCATION -> Glodeni=160882.5, (root) -> Balti County -> 110 - EDUCATION -> =0, (root) -> Balti County -> 110 - EDUCATION=177382.5, (root) -> Balti County -> 112 - BASIC EDUCATION -> Apareni=2750, (root) -> Balti County -> 112 - BASIC EDUCATION -> Glodeni=160882.5, (root) -> Balti County -> 112 - BASIC EDUCATION -> =0, (root) -> Balti County -> 112 - BASIC EDUCATION=163632.5, (root) -> Balti County -> 120 - HEALTH -> Apareni=8250, (root) -> Balti County -> 120 - HEALTH -> Glodeni=0, (root) -> Balti County -> 120 - HEALTH=8250, (root) -> Balti County=349265, (root) -> Cahul County -> 113 - SECONDARY EDUCATION -> =450000, (root) -> Cahul County -> 113 - SECONDARY EDUCATION=450000, (root) -> Cahul County=450000, (root) -> Chisinau City -> 110 - EDUCATION -> =45000, (root) -> Chisinau City -> 110 - EDUCATION=45000, (root) -> Chisinau City=45000, (root) -> Chisinau County -> 110 - EDUCATION -> =190000, (root) -> Chisinau County -> 110 - EDUCATION=190000, (root) -> Chisinau County=190000, (root) -> Drochia County -> 110 - EDUCATION -> =80000, (root) -> Drochia County -> 110 - EDUCATION=80000, (root) -> Drochia County -> 112 - BASIC EDUCATION -> =0, (root) -> Drochia County -> 112 - BASIC EDUCATION=0, (root) -> Drochia County=80000, (root) -> Dubasari County -> 110 - EDUCATION -> =45000, (root) -> Dubasari County -> 110 - EDUCATION=45000, (root) -> Dubasari County -> 112 - BASIC EDUCATION -> =123321, (root) -> Dubasari County -> 112 - BASIC EDUCATION=123321, (root) -> Dubasari County=168321, (root) -> Edinet County -> 112 - BASIC EDUCATION -> =131845, (root) -> Edinet County -> 112 - BASIC EDUCATION=131845, (root) -> Edinet County=131845, (root) -> Falesti County -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH -> =453213, (root) -> Falesti County -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH=453213, (root) -> Falesti County=453213, (root) -> Transnistrian Region -> 110 - EDUCATION -> Slobozia=13500, (root) -> Transnistrian Region -> 110 - EDUCATION -> Tiraspol=0, (root) -> Transnistrian Region -> 110 - EDUCATION=13500, (root) -> Transnistrian Region -> 112 - BASIC EDUCATION -> Slobozia=2250, (root) -> Transnistrian Region -> 112 - BASIC EDUCATION=2250, (root) -> Transnistrian Region -> 120 - HEALTH -> Slobozia=6750, (root) -> Transnistrian Region -> 120 - HEALTH=6750, (root) -> Transnistrian Region=22500, (root) ->  -> 110 - EDUCATION -> =72000, (root) ->  -> 110 - EDUCATION=72000, (root) ->  -> 112 - BASIC EDUCATION -> =770, (root) ->  -> 112 - BASIC EDUCATION=770, (root) ->  -> 113 - SECONDARY EDUCATION -> =0, (root) ->  -> 113 - SECONDARY EDUCATION=0, (root) ->  ->  -> =0, (root) ->  -> =0, (root) -> =72770, (root)=3206802}",
			buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Disbursements")).toString());
	}
	
	@Test
	public void testYearRangeSettings() throws Exception {
		ReportSpecificationImpl spec = buildSpecification("byImplementingAgency", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.IMPLEMENTING_AGENCY), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.IMPLEMENTING_AGENCY), 
			GroupingCriteria.GROUPING_YEARLY);
		setLocale("en");
		List<ExceptionRunnable<Exception>> actions = Arrays.asList(
				() -> spec.getOrCreateSettings().setYearsRangeFilterRule(2012, 2012),
				() -> spec.getOrCreateSettings().setYearRangeFilter(new FilterRule("2012", true))
				);
		actions.forEach(act -> {
			try{act.run();}
			catch(Exception e){throw AlgoUtils.translateException(e);}
		
			assertEquals(
					"{(root) -> 72 Local Public Administrations from RM=96840.576201, (root) -> Finland=165740.48, (root) -> Ministry of Economy=0, (root) -> Ministry of Finance=0, (root) -> UNDP=82715.52, (root) -> USAID=0, (root) -> =19063394.610187, (root)=19408691.186388}",
					buildDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString()); // totals shouldn't change through YRS
		
			assertEquals(
					"{(root) -> 72 Local Public Administrations from RM=0, (root) -> Finland=25000, (root) -> Ministry of Economy=0, (root) -> Ministry of Finance=0, (root) -> UNDP=0, (root) -> USAID=0, (root) -> =0, (root)=25000}",
					buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2012 / Actual Commitments")).toString());
		
			shouldFail(() -> buildDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2013 / Actual Commitments"))); // should not find column
		});
	}

	final String correctTotals = "{RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2006 / Actual Disbursements=0, RAW / Funding / 2009 / Actual Commitments=100000, RAW / Funding / 2009 / Actual Disbursements=0, RAW / Funding / 2010 / Actual Commitments=0, RAW / Funding / 2010 / Actual Disbursements=780311, RAW / Funding / 2011 / Actual Commitments=1213119, RAW / Funding / 2011 / Actual Disbursements=0, RAW / Funding / 2012 / Actual Commitments=25000, RAW / Funding / 2012 / Actual Disbursements=12000, RAW / Funding / 2013 / Actual Commitments=7842086, RAW / Funding / 2013 / Actual Disbursements=1266956, RAW / Funding / 2014 / Actual Commitments=8159813.768451, RAW / Funding / 2014 / Actual Disbursements=710200, RAW / Funding / 2015 / Actual Commitments=1971831.841736, RAW / Funding / 2015 / Actual Disbursements=437335, RAW / Totals / Actual Commitments=19408691.186388, RAW / Totals / Actual Disbursements=3206802}";
	final GrandTotalsDigest fundingGrandTotalsDigester = new GrandTotalsDigest(z -> z.startsWith("RAW / Funding /") || z.startsWith("RAW / Totals"));

	
	@Test
	public void testActivityFilteringInTests() {
		String hier1Name = ColumnConstants.IMPLEMENTING_AGENCY_GROUPS;
		String hier2Name = ColumnConstants.IMPLEMENTING_AGENCY;
		
		ReportSpecificationImpl spec = buildSpecification(String.format("%s, %s summary: %b", hier1Name, hier2Name, false), 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				Arrays.asList(hier1Name, hier2Name), 
				GroupingCriteria.GROUPING_YEARLY);
		spec.setSummaryReport(true);
		
		String localTotalsTotals = "{RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2006 / Actual Disbursements=0, RAW / Funding / 2014 / Actual Commitments=0, RAW / Funding / 2014 / Actual Disbursements=50000, RAW / Totals / Actual Commitments=96840.576201, RAW / Totals / Actual Disbursements=50000}";
		assertEquals(localTotalsTotals, buildDigest(spec, Arrays.asList("activity with contracting agency"), fundingGrandTotalsDigester).toString());
	}

	@Test
	public void testProgramsBracing() {
		String hier1Name = ColumnConstants.PRIMARY_PROGRAM_LEVEL_1;
		String hier2Name = ColumnConstants.PRIMARY_PROGRAM_LEVEL_2;
		
		ReportSpecificationImpl spec = buildSpecification(String.format("%s, %s summary: %b", hier1Name, hier2Name, false), 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				Arrays.asList(hier1Name, hier2Name), 
				GroupingCriteria.GROUPING_YEARLY);
		
		final String localTotals = "{RAW / Funding / 2013 / Actual Commitments=0, RAW / Funding / 2013 / Actual Disbursements=35000, RAW / Funding / 2014 / Actual Commitments=0, RAW / Funding / 2014 / Actual Disbursements=75000, RAW / Totals / Actual Commitments=0, RAW / Totals / Actual Disbursements=110000}";
		assertEquals(spec.getReportName(), localTotals, buildDigest(spec, Arrays.asList("activity with pipeline MTEFs and act. disb"), fundingGrandTotalsDigester).toString());
	}
	
	/**
	 * generates reports with many hierarchies and checks that, for any of them, the totals do not change
	 * @throws Exception
	 */
	@Test
	public void testSingleHierarchiesDoNotChangeTotals() throws Exception {
		
		ReportSpecificationImpl initSpec = buildSpecification("initSpec", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				null, 
				GroupingCriteria.GROUPING_YEARLY);
		 		
		assertEquals(correctTotals, buildDigest(initSpec, acts, fundingGrandTotalsDigester).toString());
				
		// single-hierarchy reports
		for(boolean isSummary:Arrays.asList(true, false)) {
			for(String hierName:hierarchiesToTry) {
				ReportSpecificationImpl spec = buildSpecification(String.format("%s summary: %b", hierName, isSummary), 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, hierName), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(hierName), 
						GroupingCriteria.GROUPING_YEARLY);
				spec.setSummaryReport(isSummary);
				assertEquals(spec.getReportName(), correctTotals, buildDigest(spec, acts, fundingGrandTotalsDigester).toString());
			}
		}
	}
	
	@Test
	public void testDoubleHierarchiesDoNotChangeTotals() {
		int fails = 0;
		long start = System.currentTimeMillis();
		long reps = 0;
		// double-hierarchy reports
		for(boolean isSummary:Arrays.asList(true, false)) {
			for(String hier1Name:hierarchiesToTry)
				for(String hier2Name:hierarchiesToTry) 
					if (hier1Name != hier2Name) {
						reps ++;
						ReportSpecificationImpl spec = buildSpecification(String.format("%s, %s summary: %b", hier1Name, hier2Name, isSummary), 
								Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name), 
								Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
								Arrays.asList(hier1Name, hier2Name), 
								GroupingCriteria.GROUPING_YEARLY);
						spec.setSummaryReport(isSummary);
//						if (!buildDigest(spec, acts, fundingGrandTotalsDigester).toString().equals(correctTotals)) {
//							fails ++;
//							System.err.println("failed: " + spec.getReportName());
//						}
						assertEquals(spec.getReportName(), correctTotals, buildDigest(spec, acts, fundingGrandTotalsDigester).toString());
			}
		}
		//System.err.println("nr of failures: " + fails);
		long delta = System.currentTimeMillis() - start;
		long speed = reps * 1000 / delta;
		double relativeSpeed = speed / 286.0;
		System.err.format("I ran %d double-hier reports in %d millies (%d per second, relativeSpeed: %.2f)\n", reps, delta, speed, relativeSpeed);
	}
	
	@Test
	public void testTripleHierarchiesDoNotChangeTotals() {
		if (this.getClass().getSimpleName().equals("AmpSchemaSanityTests"))
			return; // these are too slow if backed by DB
		long start = System.currentTimeMillis();
		int fails = 0;
		long reps = 0;
		// triple-hierarchy reports
		for(boolean isSummary:Arrays.asList(true, false)) {
			for(String hier1Name:hierarchiesToTry)
				for(String hier2Name:hierarchiesToTry)
					for(String hier3Name:hierarchiesToTry)
					if (hier1Name != hier2Name && hier2Name != hier3Name && hier1Name != hier3Name) {
						reps ++;
						ReportSpecificationImpl spec = buildSpecification(String.format("%s, %s, %s summary: %b", hier1Name, hier2Name, hier3Name, isSummary), 
								Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name, hier3Name), 
								Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
								Arrays.asList(hier1Name, hier2Name, hier3Name), 
								GroupingCriteria.GROUPING_YEARLY);
						spec.setSummaryReport(isSummary);
//						if (!buildDigest(spec, acts, fundingGrandTotalsDigester).toString().equals(correctTotals)) {
//							fails ++;
//							System.err.println("failed: " + spec.getReportName());
//						}
						assertEquals(spec.getReportName(), correctTotals, buildDigest(spec, acts, fundingGrandTotalsDigester).toString());
			}
		}
		//System.err.println("nr of failures: " + fails);
		long delta = System.currentTimeMillis() - start;
		long speed = reps * 1000 / delta;
		double relativeSpeed = speed / 409.0;
		System.err.format("I ran %d triple-hier reports in %d millies (%d per second, relativeSpeed: %.2f)\n", reps, delta, speed, relativeSpeed);
	}
	
	
	
	@Test
	public void testSummaryReportWithoutHierarchies() {
		ReportSpecificationImpl initSpec = buildSpecification("initSpec", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				null, 
				GroupingCriteria.GROUPING_YEARLY);
		initSpec.setSummaryReport(true);
		assertEquals(correctTotals, buildDigest(initSpec, acts, fundingGrandTotalsDigester).toString());
	}
	
	
	@Test
	public void testByRegionBySector() {
		ReportSpecification spec = buildSpecification("testByRegionBySector", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR),
			GroupingCriteria.GROUPING_YEARLY);
		
		NiReportModel cor = new NiReportModel("testByRegionBySector")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 21))",
					"(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 19, colSpan: 2))",
					"(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Region", "", "Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County"))
			        .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,574,332", "Funding-2013-Actual Disbursements", "1,100,111", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1,611,832", "Totals-Actual Disbursements", "1,243,888", "Region", "Anenii Noi County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,449,732", "Funding-2013-Actual Disbursements", "1,100,111", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "1,487,232", "Totals-Actual Disbursements", "1,243,888", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
			            new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
			            new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
			            new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
			            new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
			            new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000"),
			            new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "53,400", "Totals-Actual Commitments", "53,400"),
			            new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "124,600", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "124,600", "Totals-Actual Disbursements", "0", "Primary Sector", "120 - HEALTH")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "124,600", "Totals-Actual Commitments", "124,600")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Balti County"))
			        .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "53,262,32", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,330,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "723,189", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "2,144,284,32", "Totals-Actual Disbursements", "349,265", "Region", "Balti County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "831,933", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "37,500", "Funding-2014-Actual Disbursements", "16,500", "Funding-2015-Actual Commitments", "388,234,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "1,289,624,89", "Totals-Actual Disbursements", "177,382,5", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
			            new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000"),
			            new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "213,600", "Totals-Actual Commitments", "213,600"),
			            new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Funding-2014-Actual Commitments", "37,500", "Totals-Actual Commitments", "37,500"),
			            new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "31,957,39", "Funding-2014-Actual Disbursements", "16,500", "Totals-Actual Commitments", "31,957,39", "Totals-Actual Disbursements", "16,500"),
			            new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5"),
			            new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "159,840", "Totals-Actual Commitments", "159,840")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "5,326,23", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "2,750", "Funding-2015-Actual Commitments", "334,954,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "340,280,73", "Totals-Actual Disbursements", "163,632,5", "Primary Sector", "112 - BASIC EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "5,326,23", "Funding-2014-Actual Disbursements", "2,750", "Totals-Actual Commitments", "5,326,23", "Totals-Actual Disbursements", "2,750"),
			            new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5"),
			            new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "106,560", "Totals-Actual Commitments", "106,560")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "15,978,7", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "498,400", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "8,250", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "514,378,7", "Totals-Actual Disbursements", "8,250", "Primary Sector", "120 - HEALTH")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Funding-2013-Actual Commitments", "498,400", "Totals-Actual Commitments", "498,400"),
			            new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "15,978,7", "Funding-2014-Actual Disbursements", "8,250", "Totals-Actual Commitments", "15,978,7", "Totals-Actual Disbursements", "8,250")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Cahul County")).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Region", "Cahul County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Primary Sector", "113 - SECONDARY EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Chisinau City")).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "246,912", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "296,912", "Totals-Actual Disbursements", "45,000", "Region", "Chisinau City")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "246,912", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "296,912", "Totals-Actual Disbursements", "45,000", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
			            new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
			            new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
			            new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Disbursements", "45,000")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Chisinau County")).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "3,365,760,63", "Funding-2014-Actual Disbursements", "155,000", "Funding-2015-Actual Commitments", "1,200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,066,960,63", "Totals-Actual Disbursements", "190,000", "Region", "Chisinau County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Commitments", "3,365,760,63", "Funding-2014-Actual Disbursements", "155,000", "Funding-2015-Actual Commitments", "1,200", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "5,066,960,63", "Totals-Actual Disbursements", "190,000", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
			            new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
			            new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
			            new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Drochia County"))
			        .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "621,600", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "621,600", "Totals-Actual Disbursements", "80,000", "Region", "Drochia County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "372,960", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Commitments", "372,960", "Totals-Actual Disbursements", "80,000", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "372,960", "Totals-Actual Commitments", "372,960"),
			            new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "248,640", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "248,640", "Totals-Actual Disbursements", "0", "Primary Sector", "112 - BASIC EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Funding-2015-Actual Commitments", "248,640", "Totals-Actual Commitments", "248,640")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Dubasari County"))
			        .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "168,321", "Region", "Dubasari County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "45,000", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "27,500", "Funding-2015-Actual Disbursements", "17,500", "Totals-Actual Disbursements", "45,000")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Primary Sector", "112 - BASIC EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Edinet County")).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Region", "Edinet County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Primary Sector", "112 - BASIC EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Falesti County")).withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Region", "Falesti County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Transnistrian Region"))
			        .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "43,578,26", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123,321", "Funding-2014-Actual Disbursements", "22,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "166,899,26", "Totals-Actual Disbursements", "22,500", "Region", "Transnistrian Region")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "26,146,96", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "123,321", "Funding-2014-Actual Disbursements", "13,500", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "149,467,96", "Totals-Actual Disbursements", "13,500", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "26,146,96", "Funding-2014-Actual Disbursements", "13,500", "Totals-Actual Commitments", "26,146,96", "Totals-Actual Disbursements", "13,500"),
			            new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "4,357,83", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "2,250", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "4,357,83", "Totals-Actual Disbursements", "2,250", "Primary Sector", "112 - BASIC EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "4,357,83", "Funding-2014-Actual Disbursements", "2,250", "Totals-Actual Commitments", "4,357,83", "Totals-Actual Disbursements", "2,250")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "13,073,48", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "6,750", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "13,073,48", "Totals-Actual Disbursements", "6,750", "Primary Sector", "120 - HEALTH")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "13,073,48", "Funding-2014-Actual Disbursements", "6,750", "Totals-Actual Commitments", "13,073,48", "Totals-Actual Disbursements", "6,750")          )        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined"))
			        .withContents("Primary Sector", "", "Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "145,732,14", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "378,930,84", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "649,662,98", "Totals-Actual Disbursements", "72,770", "Region", "Region: Undefined")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "120,168,92", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "322,737,76", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "567,906,68", "Totals-Actual Disbursements", "72,000", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
			            new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
			            new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
			            new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
			            new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
			            new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
			            new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
			            new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments", "19,800", "Funding-2015-Actual Commitments", "70,200", "Totals-Actual Commitments", "90,000"),
			            new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments", "3,268,92", "Funding-2015-Actual Commitments", "84,537,76", "Totals-Actual Commitments", "87,806,68")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION"))
			          .withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "363,21", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Commitments", "9,393,08", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Commitments", "9,756,3", "Totals-Actual Disbursements", "770", "Primary Sector", "112 - BASIC EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
			            new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Funding-2014-Actual Commitments", "363,21", "Funding-2015-Actual Commitments", "9,393,08", "Totals-Actual Commitments", "9,756,3")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "13,200", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "46,800", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "60,000", "Totals-Actual Disbursements", "0", "Primary Sector", "113 - SECONDARY EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments", "13,200", "Funding-2015-Actual Commitments", "46,800", "Totals-Actual Commitments", "60,000")          ),
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "Primary Sector: Undefined")).withContents("Project Title", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "12,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "12,000", "Totals-Actual Disbursements", "0", "Primary Sector", "Primary Sector: Undefined")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000")          )        )      ));

		runNiTestCase(cor, spec, acts);
	}
	
	@Test
	public void testRegionUndefinedHierarchyOutput() {
		ReportSpecification spec = buildSpecification("regionUndefinedHierarchy", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				Arrays.asList(ColumnConstants.REGION),
				GroupingCriteria.GROUPING_YEARLY);
		
		NiReportModel cor = new NiReportModel("regionUndefinedHierarchy")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
				"(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
				"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Region", "", "Project Title", "", "Primary Sector", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "33,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "117,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "483,333", "Totals-Actual Disbursements", "0")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner("Region", "Balti County")).withContents("Project Title", "", "Primary Sector", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "333,333", "Totals-Actual Disbursements", "0", "Region", "Balti County")
		        .withChildren(
		          new ReportAreaForTests(null, "Project Title", "crazy funding 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")        ),
		        new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined")).withContents("Project Title", "", "Primary Sector", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "33,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "117,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "150,000", "Totals-Actual Disbursements", "0", "Region", "Region: Undefined")
		        .withChildren(
		          new ReportAreaForTests(null, "Project Title", "activity_with_disaster_response", "Primary Sector", "110 - EDUCATION, 113 - SECONDARY EDUCATION", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000")        )      ));
		
		runNiTestCase(cor, spec, Arrays.asList("crazy funding 1", "activity_with_disaster_response"));
	}
	
	@Test
	public void testRegionUndefinedColumn() {
		ReportSpecification spec = buildSpecification("regionUndefinedColumn", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
				null,
				GroupingCriteria.GROUPING_YEARLY);
		
		NiReportModel cor = new NiReportModel("regionUndefinedColumn")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 2))",
				"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Region", "", "Primary Sector", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "33,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "117,000", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "483,333", "Totals-Actual Disbursements", "0")
		      .withChildren(
		        new ReportAreaForTests(null, "Project Title", "crazy funding 1", "Region", "Balti County", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
		        new ReportAreaForTests(null, "Project Title", "activity_with_disaster_response", "Region", "", "Primary Sector", "110 - EDUCATION, 113 - SECONDARY EDUCATION", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000")      ));
		
		runNiTestCase(cor, spec, Arrays.asList("crazy funding 1", "activity_with_disaster_response"));
	}
	
	@Test
	public void testAllowEmptyFundingColumnsDoesNotInfluenceYearlyReports() {
		List<String> acts = Arrays.asList("crazy funding 1", "activity_with_disaster_response");
		ReportSpecificationImpl spec = buildSpecification("allowEmptyFundingColumns",
			Arrays.asList(ColumnConstants.PROJECT_TITLE),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PLANNED_COMMITMENTS),
			null,
			GroupingCriteria.GROUPING_YEARLY);
		
		NiReportModel cor = new NiReportModel("allowEmptyFundingColumns")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
				"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 9));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 3))",
				"(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 3));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 3));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 3))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Planned Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Planned Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Planned Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Planned Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2013-Planned Commitments", "0", "Funding-2014-Actual Commitments", "33,000", "Funding-2014-Actual Disbursements", "0", "Funding-2014-Planned Commitments", "0", "Funding-2015-Actual Commitments", "117,000", "Funding-2015-Actual Disbursements", "0", "Funding-2015-Planned Commitments", "0", "Totals-Actual Commitments", "483,333", "Totals-Actual Disbursements", "0", "Totals-Planned Commitments", "0")
		      .withChildren(
		        new ReportAreaForTests(null, "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
		        new ReportAreaForTests(null, "Project Title", "activity_with_disaster_response", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000")      ));

		runNiTestCase(cor, spec, acts);
		
		spec.setDisplayEmptyFundingColumns(true);
		runNiTestCase(cor, spec, acts);
		
		NiReportModel totalsCor = new NiReportModel("allowEmptyFundingColumns")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
				"(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 3))",
				"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Planned Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Totals-Actual Commitments", "483,333", "Totals-Actual Disbursements", "0", "Totals-Planned Commitments", "0")
		      .withChildren(
		        new ReportAreaForTests(null, "Project Title", "crazy funding 1", "Totals-Actual Commitments", "333,333"),
		        new ReportAreaForTests(null, "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "150,000")));
		
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
		spec.setDisplayEmptyFundingColumns(false);
		runNiTestCase(totalsCor, spec, acts);
		
		spec.setDisplayEmptyFundingColumns(true);
		runNiTestCase(totalsCor, spec, acts);
	}
	
	
	@Test
	public void test_AMP_18497() {
		// for running manually: open http://localhost:8080/TEMPLATE/ampTemplate/saikuui/index.html#report/open/32 on the AMP 2.10 testcases database
		
		NiReportModel cor = new NiReportModel("AMP-18497")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
					"(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Donor Group: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
					"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
				.withWarnings(Arrays.asList())
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Project Title", "", "Donor Group", "", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "1,141,990")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Donor Group", "American", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
			        new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Group", "National", "Totals-Actual Disbursements", "143,777"),
			        new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Donor Group", "American, Default Group, European", "Totals-Actual Disbursements", "545,000")      ));

		runNiTestCase(
				buildSpecification("AMP-18497", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_GROUP), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						null, GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en",
				Arrays.asList("Eth Water", "Test MTEF directed", "TAC_activity_2"),
				cor);
	}
	
	@Test
	public void test_AMP_18530_no_hier() {
		// report with "Region" as a column, an activity without locations + one with locations
		NiReportModel cor = new NiReportModel("AMP-18530-no-hier")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
						"(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "458,333", "Totals-Actual Disbursements", "72,000")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")      ));
		
		runNiTestCase(
				buildSpecification("AMP-18530-no-hier",						
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
						null,
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);

	}
	
	@Test
	public void test_AMP_18530_hier() {
		// report with "Region" as a hier, an activity without locations + one with locations
		NiReportModel cor = new NiReportModel("AMP-18530-hier")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
				"(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
				"(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Region", "", "Project Title", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "458,333", "Totals-Actual Disbursements", "72,000")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner("Region", "Balti County")).withContents("Project Title", "", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "333,333", "Totals-Actual Disbursements", "0", "Region", "Balti County")
		        .withChildren(
		          new ReportAreaForTests(null, "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")        ),
		        new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined")).withContents("Project Title", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000", "Region", "Region: Undefined")
		        .withChildren(
		          new ReportAreaForTests(null, "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000")        )      ));
		
		runNiTestCase(
				buildSpecification("AMP-18530-hier", 
						Arrays.asList(ColumnConstants.REGION, ColumnConstants.PROJECT_TITLE), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(ColumnConstants.REGION), 
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
		
		runNiTestCase(
				buildSpecification("AMP-18541-columns-not-ordered", 
						Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), 
						Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
						Arrays.asList(ColumnConstants.REGION), 
						GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
	}
	
	@Test
	public void test_AMP_18542() {
		// report with "Region" as a column, an activity without locations + one with locations
		NiReportModel cor = new NiReportModel("AMP-18542-ordered-columns")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
				"(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 8));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 10, colSpan: 2))",
				"(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 2))",
				"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Region", "", "Project Title", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "458,333", "Totals-Actual Disbursements", "72,000")
		      .withChildren(
		        new ReportAreaForTests(new AreaOwner("Region", "Balti County")).withContents("Project Title", "", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "333,333", "Totals-Actual Disbursements", "0", "Region", "Balti County")
		        .withChildren(
		          new ReportAreaForTests(null, "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")        ),
		        new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined")).withContents("Project Title", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000", "Region", "Region: Undefined")
		        .withChildren(
		          new ReportAreaForTests(null, "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000")        )      ));
		
		runNiTestCase(
				buildSpecification("AMP-18542-ordered-columns", 
					Arrays.asList(ColumnConstants.REGION, ColumnConstants.PROJECT_TITLE), 
					Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
					Arrays.asList(ColumnConstants.REGION), 
					GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
		
		runNiTestCase(
				buildSpecification("AMP-18542-unordered-columns", 
					Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), 
					Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
					Arrays.asList(ColumnConstants.REGION), 
					GroupingCriteria.GROUPING_YEARLY),
				"en",
				Arrays.asList("date-filters-activity", "crazy funding 1"),
				cor);
	}
	
	@Test
	public void testAllowEmptyColumnsWithQuarterReport() {
		NiReportModel correctResult = new NiReportModel("something 1")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 17))",
						"(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 14));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 15, colSpan: 2))",
						"(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 4));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 7, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 4));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 13, colSpan: 2))",
						"(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2))",
						"(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Funding-2009-Q1-Actual Commitments", "100,000", "Funding-2009-Q1-Actual Disbursements", "0", "Funding-2010-Q1-Actual Commitments", "0", "Funding-2010-Q1-Actual Disbursements", "453,213", "Funding-2010-Q2-Actual Commitments", "0", "Funding-2010-Q2-Actual Disbursements", "60,000", "Funding-2011-Q4-Actual Commitments", "999,888", "Funding-2011-Q4-Actual Disbursements", "0", "Funding-2012-Q3-Actual Commitments", "25,000", "Funding-2012-Q3-Actual Disbursements", "0", "Funding-2012-Q4-Actual Commitments", "0", "Funding-2012-Q4-Actual Disbursements", "12,000", "Funding-2013-Q4-Actual Commitments", "333,333", "Funding-2013-Q4-Actual Disbursements", "0", "Totals-Actual Commitments", "1,458,221", "Totals-Actual Disbursements", "525,213")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Q1-Actual Disbursements", "453,213", "Funding-2011-Q4-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
				        new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Q1-Actual Commitments", "100,000", "Funding-2010-Q2-Actual Disbursements", "60,000", "Funding-2012-Q3-Actual Commitments", "25,000", "Funding-2012-Q4-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Q4-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333")      ));
		
		ReportSpecificationImpl spec = buildSpecification("something 1",
				Arrays.asList(ColumnConstants.PROJECT_TITLE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_QUARTERLY);
		spec.setDisplayEmptyFundingColumns(true);
		runNiTestCase(
				spec,
				"en", 
				Arrays.asList("TAC_activity_2", "date-filters-activity", "crazy funding 1"), 
				correctResult
				);
	}
	
	@Test
	public void testAllowEmptyColumnsWithMonthlyReport() {
		NiReportModel correctResult = new NiReportModel("something 2")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 7))",
				"(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 4));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 5, colSpan: 2))",
				"(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2))",
				"(January: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(November: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
				"(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Funding-2010-January-Actual Commitments", "0", "Funding-2010-January-Actual Disbursements", "453,213", "Funding-2011-November-Actual Commitments", "999,888", "Funding-2011-November-Actual Disbursements", "0", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213")
		      .withChildren(
		        new ReportAreaForTests(null, "Project Title", "TAC_activity_2", "Funding-2010-January-Actual Disbursements", "453,213", "Funding-2011-November-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213")      ));
		
		ReportSpecificationImpl spec = buildSpecification("something 2",
				Arrays.asList(ColumnConstants.PROJECT_TITLE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				null, 
				GroupingCriteria.GROUPING_MONTHLY);
		
		spec.setDisplayEmptyFundingColumns(true);
		runNiTestCase(
				spec,
				"en", 
				Arrays.asList("TAC_activity_2"), 
				correctResult
				);
	}
	
	@Test
	public void testEmptyMeasuresFlat() {
		NiReportModel corYearly = new NiReportModel("emptyMeasuresFlat")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
						"(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
						"(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Pipeline Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Pipeline Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Pipeline Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
					.withWarnings(Arrays.asList(
						"-1: [entityId: -1, message: measure pipeline Estimated Disbursements not supported in NiReports]"))
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Pipeline Commitments", "0", "Funding-2013-Actual Commitments", "1,236,777", "Funding-2013-Pipeline Commitments", "0", "Totals-Actual Commitments", "1,450,008", "Totals-Pipeline Commitments", "0")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231"),
				        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
				        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000")      ));
		
		ReportSpecificationImpl spec = buildSpecification("emptyMeasuresFlat", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.PIPELINE_COMMITMENTS, MeasureConstants.PIPELINE_ESTIMATED_DISBURSEMENTS),
			null,
			GroupingCriteria.GROUPING_YEARLY);
		
		runNiTestCase(spec, "en", Arrays.asList("TAC_activity_1", "ptc activity 1", "Activity with Zones"), corYearly);
		
		NiReportModel corTotalsOnly = new NiReportModel("emptyMeasuresFlat")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
						"(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
						"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Pipeline Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
					.withWarnings(Arrays.asList(
						"-1: [entityId: -1, message: measure pipeline Estimated Disbursements not supported in NiReports]"))
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Project Title", "", "Region", "", "Totals-Actual Commitments", "1,450,008", "Totals-Pipeline Commitments", "0")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Totals-Actual Commitments", "213,231"),
				        new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Totals-Actual Commitments", "666,777"),
				        new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Totals-Actual Commitments", "570,000")      ));
		
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
		
		runNiTestCase(spec, "en", Arrays.asList("TAC_activity_1", "ptc activity 1", "Activity with Zones"), corTotalsOnly);
	}
	
	@Test
	public void testEmptyMeasuresWithHier() {
		NiReportModel corYearly = new NiReportModel("emptyMeasuresWithHier")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 8))",
					"(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 2))",
					"(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2))",
					"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Pipeline Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Pipeline Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Pipeline Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1))"))
				.withWarnings(Arrays.asList(
					"-1: [entityId: -1, message: measure pipeline Estimated Disbursements not supported in NiReports]"))
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Region", "", "Project Title", "", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Pipeline Commitments", "0", "Funding-2013-Actual Commitments", "1,236,777", "Funding-2013-Pipeline Commitments", "0", "Totals-Actual Commitments", "1,450,008", "Totals-Pipeline Commitments", "0")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County"))
			        .withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-Pipeline Commitments", "0", "Funding-2013-Actual Commitments", "951,777", "Funding-2013-Pipeline Commitments", "0", "Totals-Actual Commitments", "951,777", "Totals-Pipeline Commitments", "0", "Region", "Anenii Noi County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
			          new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000")        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Balti County")).withContents("Project Title", "", "Funding-2011-Actual Commitments", "0", "Funding-2011-Pipeline Commitments", "0", "Funding-2013-Actual Commitments", "285,000", "Funding-2013-Pipeline Commitments", "0", "Totals-Actual Commitments", "285,000", "Totals-Pipeline Commitments", "0", "Region", "Balti County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Funding-2013-Actual Commitments", "285,000", "Totals-Actual Commitments", "285,000")        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Dubasari County")).withContents("Project Title", "", "Funding-2011-Actual Commitments", "213,231", "Funding-2011-Pipeline Commitments", "0", "Funding-2013-Actual Commitments", "0", "Funding-2013-Pipeline Commitments", "0", "Totals-Actual Commitments", "213,231", "Totals-Pipeline Commitments", "0", "Region", "Dubasari County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231")        )      ));
		
		ReportSpecificationImpl spec = buildSpecification("emptyMeasuresWithHier", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.PIPELINE_COMMITMENTS, MeasureConstants.PIPELINE_ESTIMATED_DISBURSEMENTS),
			Arrays.asList(ColumnConstants.REGION),
			GroupingCriteria.GROUPING_YEARLY);
		
		runNiTestCase(spec, "en", Arrays.asList("TAC_activity_1", "ptc activity 1", "Activity with Zones"), corYearly);
		
		NiReportModel corTotalsOnly = new NiReportModel("emptyMeasuresWithHier")
			.withHeaders(Arrays.asList(
					"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
					"(Region: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2))",
					"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Pipeline Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
				.withWarnings(Arrays.asList(
					"-1: [entityId: -1, message: measure pipeline Estimated Disbursements not supported in NiReports]"))
				.withBody(      new ReportAreaForTests(null)
			      .withContents("Region", "", "Project Title", "", "Totals-Actual Commitments", "1,450,008", "Totals-Pipeline Commitments", "0")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County"))
			        .withContents("Project Title", "", "Totals-Actual Commitments", "951,777", "Totals-Pipeline Commitments", "0", "Region", "Anenii Noi County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Totals-Actual Commitments", "666,777"),
			          new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Totals-Actual Commitments", "285,000")        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Balti County")).withContents("Project Title", "", "Totals-Actual Commitments", "285,000", "Totals-Pipeline Commitments", "0", "Region", "Balti County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Totals-Actual Commitments", "285,000")        ),
			        new ReportAreaForTests(new AreaOwner("Region", "Dubasari County")).withContents("Project Title", "", "Totals-Actual Commitments", "213,231", "Totals-Pipeline Commitments", "0", "Region", "Dubasari County")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Totals-Actual Commitments", "213,231")        )      ));
		
		spec.setGroupingCriteria(GroupingCriteria.GROUPING_TOTALS_ONLY);
		
		runNiTestCase(spec, "en", Arrays.asList("TAC_activity_1", "ptc activity 1", "Activity with Zones"), corTotalsOnly);
	}
	
	@Test
	public void testDateColumns() {
		NiReportModel cor = new NiReportModel("test_date_columns")
		.withHeaders(Arrays.asList(
				"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 5))",
				"(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Activity Created On: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Activity Updated On: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
				"(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))"))
			.withWarnings(Arrays.asList())
			.withBody(      new ReportAreaForTests(null)
		      .withContents("Project Title", "", "Activity Created On", "", "Activity Updated On", "", "Totals-Actual Commitments", "999,999", "Totals-Actual Disbursements", "0")
		      .withChildren(
		        new ReportAreaForTests(null, "Project Title", "mtef activity 1", "Activity Created On", "05/08/2013", "Activity Updated On", "20/12/2013"),
		        new ReportAreaForTests(null, "Project Title", "mtef activity 2", "Activity Created On", "05/08/2013", "Activity Updated On", "20/12/2013"),
		        new ReportAreaForTests(null, "Project Title", "ptc activity 1", "Activity Created On", "19/08/2013", "Activity Updated On", "20/12/2013", "Totals-Actual Commitments", "666,777"),
		        new ReportAreaForTests(null, "Project Title", "ptc activity 2", "Activity Created On", "19/08/2013", "Activity Updated On", "20/12/2013", "Totals-Actual Commitments", "333,222")      ));
		
		ReportSpecificationImpl spec = buildSpecification("test_date_columns",
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ACTIVITY_CREATED_ON, ColumnConstants.ACTIVITY_UPDATED_ON),
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
			null,
			GroupingCriteria.GROUPING_TOTALS_ONLY);
		
		runNiTestCase(spec, "en", Arrays.asList("ptc activity 1", "mtef activity 1", "mtef activity 2", "ptc activity 2"), cor);
	}	
	
	@Test
	public void testHierByModeOfPayment() {
		NiReportModel cor = new NiReportModel("test_by_mode_of_payment")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 21))",
						"(Mode of Payment: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 19, colSpan: 2))",
						"(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Mode of Payment", "", "Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner("Mode of Payment", "Cash"))
				        .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "111,111", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "50,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "161,111", "Totals-Actual Disbursements", "143,777", "Mode of Payment", "Cash")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
				          new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "111,111", "Totals-Actual Commitments", "111,111"),
				          new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000")        ),
				        new ReportAreaForTests(new AreaOwner("Mode of Payment", "Direct payment"))
				        .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "900,976", "Funding-2013-Actual Disbursements", "721,956", "Funding-2014-Actual Commitments", "111,632,14", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Actual Commitments", "1,222,386,84", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "2,234,994,98", "Totals-Actual Disbursements", "796,956", "Mode of Payment", "Direct payment")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
				          new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
				          new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "222,222", "Totals-Actual Commitments", "222,222"),
				          new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
				          new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
				          new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
				          new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				          new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
				          new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")        ),
				        new ReportAreaForTests(new AreaOwner("Mode of Payment", "No Information")).withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "333,222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "333,222", "Totals-Actual Disbursements", "0", "Mode of Payment", "No Information")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222")        ),
				        new ReportAreaForTests(new AreaOwner("Mode of Payment", "Reimbursable")).withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "666,777", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "666,777", "Totals-Actual Disbursements", "0", "Mode of Payment", "Reimbursable")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777")        ),
				        new ReportAreaForTests(new AreaOwner("Mode of Payment", "Mode of Payment: Undefined"))
				        .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "636,534", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "5,830,000", "Funding-2013-Actual Disbursements", "545,000", "Funding-2014-Actual Commitments", "7,998,181,63", "Funding-2014-Actual Disbursements", "635,200", "Funding-2015-Actual Commitments", "749,445", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "16,012,586,21", "Totals-Actual Disbursements", "2,266,069", "Mode of Payment", "Mode of Payment: Undefined")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
				          new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region", "Falesti County", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
				          new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
				          new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				          new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
				          new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
				          new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				          new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
				          new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
				          new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
				          new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
				          new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
				          new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
				          new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
				          new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Region", "Transnistrian Region", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"),
				          new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
				          new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
				          new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Region", "Balti County", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
				          new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
				          new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				          new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Region", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
				          new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
				          new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
				          new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"))));

		ReportSpecificationImpl spec = buildSpecification("test_by_mode_of_payment",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.MODE_OF_PAYMENT),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				Arrays.asList(ColumnConstants.MODE_OF_PAYMENT),
				GroupingCriteria.GROUPING_YEARLY);

		runNiTestCase(spec, "en", acts, cor);
	}
	
	@Test
	public void testHierByTypeOfAssistance() {
		NiReportModel cor = new NiReportModel("test_by_type_of_assistance")
				.withHeaders(Arrays.asList(
						"(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 21))",
						"(Type Of Assistance: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 19, colSpan: 2))",
						"(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 2))",
						"(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1))"))
					.withWarnings(Arrays.asList())
					.withBody(      new ReportAreaForTests(null)
				      .withContents("Type Of Assistance", "", "Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "19,408,691,19", "Totals-Actual Disbursements", "3,206,802")
				      .withChildren(
				        new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance"))
				        .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "4,949,864", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Actual Commitments", "3,570,732,14", "Funding-2014-Actual Disbursements", "180,200", "Funding-2015-Actual Commitments", "1,971,831,84", "Funding-2015-Actual Disbursements", "437,335", "Totals-Actual Commitments", "11,927,387,56", "Totals-Actual Disbursements", "2,676,802", "Type Of Assistance", "default type of assistance")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321"),
				          new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region", "Falesti County", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213"),
				          new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
				          new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
				          new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000"),
				          new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "666,777", "Totals-Actual Commitments", "666,777"),
				          new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "333,222", "Totals-Actual Commitments", "333,222"),
				          new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111"),
				          new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845"),
				          new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "111,111", "Totals-Actual Commitments", "111,111"),
				          new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "570,000", "Totals-Actual Commitments", "570,000"),
				          new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Funding-2013-Actual Commitments", "890,000", "Totals-Actual Commitments", "890,000"),
				          new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Funding-2014-Actual Commitments", "75,000", "Totals-Actual Commitments", "75,000"),
				          new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				          new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "50,000", "Totals-Actual Commitments", "50,000"),
				          new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Funding-2014-Actual Commitments", "32,000", "Totals-Actual Commitments", "32,000"),
				          new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
				          new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Funding-2013-Actual Commitments", "1,700,000", "Funding-2014-Actual Commitments", "3,300,000", "Totals-Actual Commitments", "5,000,000"),
				          new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000"),
				          new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Funding-2014-Actual Commitments", "12,000", "Totals-Actual Commitments", "12,000"),
				          new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Funding-2014-Actual Commitments", "100", "Totals-Actual Commitments", "100"),
				          new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000"),
				          new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Region", "Balti County", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765"),
				          new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200"),
				          new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				          new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Region", "", "Funding-2015-Actual Commitments", "123,000", "Totals-Actual Commitments", "123,000"),
				          new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", "", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770"),
				          new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Funding-2015-Actual Commitments", "888,000", "Totals-Actual Commitments", "888,000"),
				          new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Funding-2014-Actual Commitments", "33,000", "Funding-2015-Actual Commitments", "117,000", "Totals-Actual Commitments", "150,000"),
				          new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
				          new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
				          new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000"),
				          new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Region", "Drochia County", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
				          new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Funding-2014-Actual Commitments", "3,632,14", "Funding-2015-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "97,562,98")        ),
				        new ReportAreaForTests(new AreaOwner("Type Of Assistance", "second type of assistance"))
				        .withContents("Project Title", "", "Region", "", "Funding-2006-Actual Commitments", "0", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "0", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2011-Actual Commitments", "0", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Actual Commitments", "2,892,222", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "4,589,081,63", "Funding-2014-Actual Disbursements", "530,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "7,481,303,63", "Totals-Actual Disbursements", "530,000", "Type Of Assistance", "second type of assistance")
				        .withChildren(
				          new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Funding-2013-Actual Commitments", "222,222", "Totals-Actual Commitments", "222,222"),
				          new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000"),
				          new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000"),
				          new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Region", "Transnistrian Region", "Funding-2014-Actual Commitments", "123,321", "Totals-Actual Commitments", "123,321"))));
		
		ReportSpecificationImpl spec = buildSpecification("test_by_type_of_assistance",
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.TYPE_OF_ASSISTANCE),
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
				Arrays.asList(ColumnConstants.TYPE_OF_ASSISTANCE),
				GroupingCriteria.GROUPING_YEARLY);

		runNiTestCase(spec, "en", acts, cor);
	}
}
