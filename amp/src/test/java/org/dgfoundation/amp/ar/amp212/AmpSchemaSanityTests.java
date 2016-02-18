package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.ExceptionRunnable;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSettings;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.TrailCellsDigest;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.output.NiReportExecutor;
import org.junit.Test;

/**
 * 
 * testcases for the fetching states of AMP + the AMP schema
 * 
 * @author Constantin Dolghier
 *
 */
public class AmpSchemaSanityTests extends MondrianReportsTestCase {

	List<String> acts = Arrays.asList(
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

	public AmpSchemaSanityTests() {
		super("AmpReportsSchema sanity tests");
	}
	
	@Test
	public void testPlainReportTotals() {
		ReportSpecification spec = buildSpecification("plain", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
				null, 
				GroupingCriteria.GROUPING_YEARLY);
		
//		String res = buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString();
//		System.out.println(res);
		
		assertEquals("{(root)=19408691.186388}", buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		assertEquals("{(root)=8159813.768451}", buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Commitments")).toString());
		
		spec = buildSpecification("plain", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
				null, 
				GroupingCriteria.GROUPING_TOTALS_ONLY);
		assertEquals("{(root)=19408691.186388}", buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
	}
	
	@Test
	public void testByRegionReportTotals() {
		ReportSpecification spec = buildSpecification("ByRegion", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.REGION), 
			GroupingCriteria.GROUPING_YEARLY);

		assertEquals("{(root) -> Anenii Noi County=1611832, (root) -> Balti County=2144284.31691055, (root) -> Cahul County=7070000, (root) -> Chisinau City=296912, (root) -> Chisinau County=5066960.631302, (root) -> Drochia County=621600, (root) -> Dubasari County=213231, (root) -> Edinet County=567421, (root) -> Falesti County=999888, (root) -> Transnistrian Region=166899.25929045, (root) -> =649662.978885, (root)=19408691.186388}", 
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals("{(root) -> Anenii Noi County=37500, (root) -> Balti County=37500, (root) -> Cahul County=4400000, (root) -> Chisinau City=50000, (root) -> Chisinau County=3365760.631302, (root) -> Drochia County=0, (root) -> Dubasari County=0, (root) -> Edinet County=0, (root) -> Falesti County=0, (root) -> Transnistrian Region=123321, (root) -> =145732.137149, (root)=8159813.768451}", 
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Commitments")).toString());
		
		assertEquals("{(root) -> Anenii Noi County=1100111, (root) -> Balti County=0, (root) -> Cahul County=0, (root) -> Chisinau City=0, (root) -> Chisinau County=35000, (root) -> Drochia County=0, (root) -> Dubasari County=0, (root) -> Edinet County=131845, (root) -> Falesti County=0, (root) -> Transnistrian Region=0, (root) -> =0, (root)=1266956}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2013 / Actual Disbursements")).toString());
	}
	
	@Test
	public void testByRegionByZoneReportTotals() {
		ReportSpecification spec = buildSpecification("ByRegionByZone", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.ZONE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.REGION, ColumnConstants.ZONE), 
			GroupingCriteria.GROUPING_YEARLY);

		assertEquals(
			"{(root) -> Anenii Noi County -> Bulboaca=285000, (root) -> Anenii Noi County -> Dolboaca=178000, (root) -> Anenii Noi County -> =1148832, (root) -> Anenii Noi County=1611832, (root) -> Balti County -> Apareni=53262.31691055, (root) -> Balti County -> Glodeni=1491289, (root) -> Balti County -> =599733, (root) -> Balti County=2144284.31691055, (root) -> Cahul County -> =7070000, (root) -> Cahul County=7070000, (root) -> Chisinau City -> =296912, (root) -> Chisinau City=296912, (root) -> Chisinau County -> =5066960.631302, (root) -> Chisinau County=5066960.631302, " + 
			"(root) -> Drochia County -> =621600, (root) -> Drochia County=621600, (root) -> Dubasari County -> =213231, (root) -> Dubasari County=213231, (root) -> Edinet County -> =567421, (root) -> Edinet County=567421, (root) -> Falesti County -> =999888, (root) -> Falesti County=999888, (root) -> Transnistrian Region -> Slobozia=43578.25929045, (root) -> Transnistrian Region -> Tiraspol=123321, (root) -> Transnistrian Region=166899.25929045, (root) ->  -> =649662.978885, (root) -> =649662.978885, (root)=19408691.186388}", 
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> Anenii Noi County -> Bulboaca=0, (root) -> Anenii Noi County -> Dolboaca=0, (root) -> Anenii Noi County -> =37500, (root) -> Anenii Noi County=37500, (root) -> Balti County -> Apareni=0, (root) -> Balti County -> Glodeni=37500, (root) -> Balti County -> =0, (root) -> Balti County=37500, (root) -> Cahul County -> =4400000, (root) -> Cahul County=4400000, (root) -> Chisinau City -> =50000, (root) -> Chisinau City=50000, (root) -> Chisinau County -> =3365760.631302, (root) -> Chisinau County=3365760.631302, " +
			"(root) -> Drochia County -> =0, (root) -> Drochia County=0, (root) -> Dubasari County -> =0, (root) -> Dubasari County=0, (root) -> Edinet County -> =0, (root) -> Edinet County=0, (root) -> Falesti County -> =0, (root) -> Falesti County=0, (root) -> Transnistrian Region -> Slobozia=0, (root) -> Transnistrian Region -> Tiraspol=123321, (root) -> Transnistrian Region=123321, (root) ->  -> =145732.137149, (root) -> =145732.137149, (root)=8159813.768451}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> Anenii Noi County -> Bulboaca=0, (root) -> Anenii Noi County -> Dolboaca=0, (root) -> Anenii Noi County -> =1100111, (root) -> Anenii Noi County=1100111, (root) -> Balti County -> Apareni=0, (root) -> Balti County -> Glodeni=0, (root) -> Balti County -> =0, (root) -> Balti County=0, (root) -> Cahul County -> =0, (root) -> Cahul County=0, (root) -> Chisinau City -> =0, (root) -> Chisinau City=0, (root) -> Chisinau County -> =35000, (root) -> Chisinau County=35000, " + 
			"(root) -> Drochia County -> =0, (root) -> Drochia County=0, (root) -> Dubasari County -> =0, (root) -> Dubasari County=0, (root) -> Edinet County -> =131845, (root) -> Edinet County=131845, (root) -> Falesti County -> =0, (root) -> Falesti County=0, (root) -> Transnistrian Region -> Slobozia=0, (root) -> Transnistrian Region -> Tiraspol=0, (root) -> Transnistrian Region=0, (root) ->  -> =0, (root) -> =0, (root)=1266956}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2013 / Actual Disbursements")).toString());
	}

	@Test
	public void testByZoneReportTotals() {
		ReportSpecification spec = buildSpecification("ByZone", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ZONE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.ZONE), 
			GroupingCriteria.GROUPING_YEARLY);
		
		assertEquals("{(root) -> Apareni=53262.31691055, (root) -> Bulboaca=285000, (root) -> Dolboaca=178000, (root) -> Glodeni=1491289, (root) -> Slobozia=43578.25929045, (root) -> Tiraspol=123321, (root) -> =17234240.610187, (root)=19408691.186388}", 
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals("{(root) -> Apareni=27500, (root) -> Bulboaca=0, (root) -> Dolboaca=0, (root) -> Glodeni=0, (root) -> Slobozia=22500, (root) -> Tiraspol=0, (root) -> =660200, (root)=710200}", 
				buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Disbursements")).toString());
	}
	
	@Test
	public void testByZoneByRegionReportTotals() {
		ReportSpecification spec = buildSpecification("ByZoneByRegion", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.ZONE, ColumnConstants.REGION), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.ZONE, ColumnConstants.REGION), 
			GroupingCriteria.GROUPING_YEARLY);

		assertEquals(
			"{(root) -> Apareni -> Balti County=53262.31691055, (root) -> Apareni=53262.31691055, (root) -> Bulboaca -> Anenii Noi County=285000, (root) -> Bulboaca=285000, (root) -> Dolboaca -> Anenii Noi County=178000, (root) -> Dolboaca=178000, (root) -> Glodeni -> Balti County=1491289, (root) -> Glodeni=1491289, (root) -> Slobozia -> Transnistrian Region=43578.25929045, (root) -> Slobozia=43578.25929045, " + 
			"(root) -> Tiraspol -> Transnistrian Region=123321, (root) -> Tiraspol=123321, (root) ->  -> Anenii Noi County=1148832, (root) ->  -> Balti County=599733, (root) ->  -> Cahul County=7070000, (root) ->  -> Chisinau City=296912, (root) ->  -> Chisinau County=5066960.631302, (root) ->  -> Drochia County=621600, (root) ->  -> Dubasari County=213231, (root) ->  -> Edinet County=567421, (root) ->  -> Falesti County=999888, (root) ->  -> =649662.978885, (root) -> =17234240.610187, (root)=19408691.186388}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());

		assertEquals(
			"{(root) -> Apareni -> Balti County=27500, (root) -> Apareni=27500, (root) -> Bulboaca -> Anenii Noi County=0, (root) -> Bulboaca=0, (root) -> Dolboaca -> Anenii Noi County=0, (root) -> Dolboaca=0, (root) -> Glodeni -> Balti County=0, (root) -> Glodeni=0, (root) -> Slobozia -> Transnistrian Region=22500, (root) -> Slobozia=22500, " +
			"(root) -> Tiraspol -> Transnistrian Region=0, (root) -> Tiraspol=0, (root) ->  -> Anenii Noi County=0, (root) ->  -> Balti County=0, (root) ->  -> Cahul County=450000, (root) ->  -> Chisinau City=27500, (root) ->  -> Chisinau County=155000, (root) ->  -> Drochia County=0, (root) ->  -> Dubasari County=27500, (root) ->  -> Edinet County=0, (root) ->  -> Falesti County=0, (root) ->  -> =200, (root) -> =660200, (root)=710200}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2014 / Actual Disbursements")).toString());

		assertEquals(
			"{(root) -> Apareni -> Balti County=0, (root) -> Apareni=0, (root) -> Bulboaca -> Anenii Noi County=285000, (root) -> Bulboaca=285000, (root) -> Dolboaca -> Anenii Noi County=178000, (root) -> Dolboaca=178000, (root) -> Glodeni -> Balti County=997000, (root) -> Glodeni=997000, (root) -> Slobozia -> Transnistrian Region=0, (root) -> Slobozia=0, " + 
			"(root) -> Tiraspol -> Transnistrian Region=0, (root) -> Tiraspol=0, (root) ->  -> Anenii Noi County=1111332, (root) ->  -> Balti County=333333, (root) ->  -> Cahul County=2670000, (root) ->  -> Chisinau City=0, (root) ->  -> Chisinau County=1700000, (root) ->  -> Drochia County=0, (root) ->  -> Dubasari County=0, (root) ->  -> Edinet County=567421, (root) ->  -> Falesti County=0, (root) ->  -> =0, (root) -> =6382086, (root)=7842086}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2013 / Actual Commitments")).toString());
	}

	@Test
	public void testByTypeOfAssistance() {
		ReportSpecification spec = buildSpecification("ByTypeOfAssistance_", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TYPE_OF_ASSISTANCE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.TYPE_OF_ASSISTANCE), 
			GroupingCriteria.GROUPING_YEARLY);

		assertEquals(
			"{(root) -> default type of assistance=11927387.555086, (root) -> second type of assistance=7481303.631302, (root)=19408691.186388}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> default type of assistance=2676802, (root) -> second type of assistance=530000, (root)=3206802}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Disbursements")).toString());
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
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> Apareni -> 110 - EDUCATION=16500, (root) -> Apareni -> 112 - BASIC EDUCATION=2750, (root) -> Apareni -> 120 - HEALTH=8250, (root) -> Apareni=27500, (root) -> Bulboaca -> 110 - EDUCATION=0, (root) -> Bulboaca=0, (root) -> Dolboaca -> 110 - EDUCATION=0, (root) -> Dolboaca -> 120 - HEALTH=0, (root) -> Dolboaca=0, (root) -> Glodeni -> 110 - EDUCATION=160882.5, (root) -> Glodeni -> 112 - BASIC EDUCATION=160882.5, (root) -> Glodeni -> 120 - HEALTH=0, (root) -> Glodeni=321765, (root) -> Slobozia -> 110 - EDUCATION=13500, (root) -> Slobozia -> 112 - BASIC EDUCATION=2250, (root) -> Slobozia -> 120 - HEALTH=6750, (root) -> Slobozia=22500, " + 
			"(root) -> Tiraspol -> 110 - EDUCATION=0, (root) -> Tiraspol=0, (root) ->  -> 110 - EDUCATION=1675888, (root) ->  -> 112 - BASIC EDUCATION=255936, (root) ->  -> 113 - SECONDARY EDUCATION=450000, (root) ->  -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH=453213, (root) ->  -> =0, (root) -> =2835037, (root)=3206802}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Disbursements")).toString());
	}

	@Test
	public void testByRegionByPrimarySectorByZone() {
		ReportSpecification spec = buildSpecification("ByRegionByPrimarySectorByZone", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.ZONE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.REGION, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.ZONE), 
			GroupingCriteria.GROUPING_YEARLY);

		assertEquals(
			"{(root) -> Anenii Noi County -> 110 - EDUCATION -> Bulboaca=285000, (root) -> Anenii Noi County -> 110 - EDUCATION -> Dolboaca=53400, (root) -> Anenii Noi County -> 110 - EDUCATION -> =1148832, (root) -> Anenii Noi County -> 110 - EDUCATION=1487232, (root) -> Anenii Noi County -> 120 - HEALTH -> Dolboaca=124600, (root) -> Anenii Noi County -> 120 - HEALTH=124600, (root) -> Anenii Noi County=1611832, (root) -> Balti County -> 110 - EDUCATION -> Apareni=31957.39014633, (root) -> Balti County -> 110 - EDUCATION -> Glodeni=764494.5, (root) -> Balti County -> 110 - EDUCATION -> =493173, (root) -> Balti County -> 110 - EDUCATION=1289624.89014633, (root) -> Balti County -> 112 - BASIC EDUCATION -> Apareni=5326.231691055, (root) -> Balti County -> 112 - BASIC EDUCATION -> Glodeni=228394.5, (root) -> Balti County -> 112 - BASIC EDUCATION -> =106560, (root) -> Balti County -> 112 - BASIC EDUCATION=340280.731691055, (root) -> Balti County -> 120 - HEALTH -> Apareni=15978.695073165, (root) -> Balti County -> 120 - HEALTH -> Glodeni=498400, (root) -> Balti County -> 120 - HEALTH=514378.695073165, (root) -> Balti County=2144284.31691055, (root) -> Cahul County -> 113 - SECONDARY EDUCATION -> =7070000, (root) -> Cahul County -> 113 - SECONDARY EDUCATION=7070000, (root) -> Cahul County=7070000, (root) -> Chisinau City -> 110 - EDUCATION -> =296912, (root) -> Chisinau City -> 110 - EDUCATION=296912, (root) -> Chisinau City=296912, (root) -> Chisinau County -> 110 - EDUCATION -> =5066960.631302, (root) -> Chisinau County -> 110 - EDUCATION=5066960.631302, (root) -> Chisinau County=5066960.631302, (root) -> Drochia County -> 110 - EDUCATION -> =372960, (root) -> Drochia County -> 110 - EDUCATION=372960, (root) -> Drochia County -> 112 - BASIC EDUCATION -> =248640, (root) -> Drochia County -> 112 - BASIC EDUCATION=248640, (root) -> Drochia County=621600, (root) -> Dubasari County -> 110 - EDUCATION -> =0, (root) -> Dubasari County -> 110 - EDUCATION=0, (root) -> Dubasari County -> 112 - BASIC EDUCATION -> =213231, (root) -> Dubasari County -> 112 - BASIC EDUCATION=213231, (root) -> Dubasari County=213231, (root) -> Edinet County -> 112 - BASIC EDUCATION -> =567421, (root) -> Edinet County -> 112 - BASIC EDUCATION=567421, (root) -> Edinet County=567421, (root) -> Falesti County -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH -> =999888, (root) -> Falesti County -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH=999888, (root) -> Falesti County=999888, (root) -> Transnistrian Region -> 110 - EDUCATION -> Slobozia=26146.95557427, (root) -> Transnistrian Region -> 110 - EDUCATION -> Tiraspol=123321, (root) -> Transnistrian Region -> 110 - EDUCATION=149467.95557427, (root) -> Transnistrian Region -> 112 - BASIC EDUCATION -> Slobozia=4357.825929045, (root) -> Transnistrian Region -> 112 - BASIC EDUCATION=4357.825929045, (root) -> Transnistrian Region -> 120 - HEALTH -> Slobozia=13073.477787135, (root) -> Transnistrian Region -> 120 - HEALTH=13073.477787135, (root) -> Transnistrian Region=166899.25929045, (root) ->  -> 110 - EDUCATION -> =567906.6809965, (root) ->  -> 110 - EDUCATION=567906.6809965, (root) ->  -> 112 - BASIC EDUCATION -> =9756.2978885, (root) ->  -> 112 - BASIC EDUCATION=9756.2978885, (root) ->  -> 113 - SECONDARY EDUCATION -> =60000, (root) ->  -> 113 - SECONDARY EDUCATION=60000, (root) ->  ->  -> =12000, (root) ->  -> =12000, (root) -> =649662.978885, (root)=19408691.186388}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString());
		
		assertEquals(
			"{(root) -> Anenii Noi County -> 110 - EDUCATION -> Bulboaca=0, (root) -> Anenii Noi County -> 110 - EDUCATION -> Dolboaca=0, (root) -> Anenii Noi County -> 110 - EDUCATION -> =1243888, (root) -> Anenii Noi County -> 110 - EDUCATION=1243888, (root) -> Anenii Noi County -> 120 - HEALTH -> Dolboaca=0, (root) -> Anenii Noi County -> 120 - HEALTH=0, (root) -> Anenii Noi County=1243888, (root) -> Balti County -> 110 - EDUCATION -> Apareni=16500, (root) -> Balti County -> 110 - EDUCATION -> Glodeni=160882.5, (root) -> Balti County -> 110 - EDUCATION -> =0, (root) -> Balti County -> 110 - EDUCATION=177382.5, (root) -> Balti County -> 112 - BASIC EDUCATION -> Apareni=2750, (root) -> Balti County -> 112 - BASIC EDUCATION -> Glodeni=160882.5, (root) -> Balti County -> 112 - BASIC EDUCATION -> =0, (root) -> Balti County -> 112 - BASIC EDUCATION=163632.5, (root) -> Balti County -> 120 - HEALTH -> Apareni=8250, (root) -> Balti County -> 120 - HEALTH -> Glodeni=0, (root) -> Balti County -> 120 - HEALTH=8250, (root) -> Balti County=349265, (root) -> Cahul County -> 113 - SECONDARY EDUCATION -> =450000, (root) -> Cahul County -> 113 - SECONDARY EDUCATION=450000, (root) -> Cahul County=450000, (root) -> Chisinau City -> 110 - EDUCATION -> =45000, (root) -> Chisinau City -> 110 - EDUCATION=45000, (root) -> Chisinau City=45000, (root) -> Chisinau County -> 110 - EDUCATION -> =190000, (root) -> Chisinau County -> 110 - EDUCATION=190000, (root) -> Chisinau County=190000, (root) -> Drochia County -> 110 - EDUCATION -> =80000, (root) -> Drochia County -> 110 - EDUCATION=80000, (root) -> Drochia County -> 112 - BASIC EDUCATION -> =0, (root) -> Drochia County -> 112 - BASIC EDUCATION=0, (root) -> Drochia County=80000, (root) -> Dubasari County -> 110 - EDUCATION -> =45000, (root) -> Dubasari County -> 110 - EDUCATION=45000, (root) -> Dubasari County -> 112 - BASIC EDUCATION -> =123321, (root) -> Dubasari County -> 112 - BASIC EDUCATION=123321, (root) -> Dubasari County=168321, (root) -> Edinet County -> 112 - BASIC EDUCATION -> =131845, (root) -> Edinet County -> 112 - BASIC EDUCATION=131845, (root) -> Edinet County=131845, (root) -> Falesti County -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH -> =453213, (root) -> Falesti County -> 130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH=453213, (root) -> Falesti County=453213, (root) -> Transnistrian Region -> 110 - EDUCATION -> Slobozia=13500, (root) -> Transnistrian Region -> 110 - EDUCATION -> Tiraspol=0, (root) -> Transnistrian Region -> 110 - EDUCATION=13500, (root) -> Transnistrian Region -> 112 - BASIC EDUCATION -> Slobozia=2250, (root) -> Transnistrian Region -> 112 - BASIC EDUCATION=2250, (root) -> Transnistrian Region -> 120 - HEALTH -> Slobozia=6750, (root) -> Transnistrian Region -> 120 - HEALTH=6750, (root) -> Transnistrian Region=22500, (root) ->  -> 110 - EDUCATION -> =72000, (root) ->  -> 110 - EDUCATION=72000, (root) ->  -> 112 - BASIC EDUCATION -> =770, (root) ->  -> 112 - BASIC EDUCATION=770, (root) ->  -> 113 - SECONDARY EDUCATION -> =0, (root) ->  -> 113 - SECONDARY EDUCATION=0, (root) ->  ->  -> =0, (root) ->  -> =0, (root) -> =72770, (root)=3206802}",
			buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Disbursements")).toString());
	}
	
	@Test
	public void testYearRangeSettings() throws Exception {
		ReportSpecificationImpl spec = buildSpecification("byImplementingAgency", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.IMPLEMENTING_AGENCY), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
			Arrays.asList(ColumnConstants.IMPLEMENTING_AGENCY), 
			GroupingCriteria.GROUPING_YEARLY);
		
		List<ExceptionRunnable<Exception>> actions = Arrays.asList(
				() -> spec.getOrCreateSettings().setYearsRangeFilterRule(2012, 2012),
				() -> spec.getOrCreateSettings().setYearRangeFilter(new FilterRule("2012", true))
				);
		actions.forEach(act -> {
			try{act.run();}
			catch(Exception e){throw AlgoUtils.translateException(e);}
		
			assertEquals(
					"{(root) -> 72 Local Public Administrations from RM=96840.576201, (root) -> Finland=165740.48, (root) -> Ministry of Economy=0, (root) -> Ministry of Finance=0, (root) -> UNDP=82715.52, (root) -> USAID=0, (root) -> =19063394.610187, (root)=19408691.186388}",
					buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Totals / Actual Commitments")).toString()); // totals shouldn't change through YRS
		
			assertEquals(
					"{(root) -> 72 Local Public Administrations from RM=0, (root) -> Finland=25000, (root) -> Ministry of Economy=0, (root) -> Ministry of Finance=0, (root) -> UNDP=0, (root) -> USAID=0, (root) -> =0, (root)=25000}",
					buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2012 / Actual Commitments")).toString());
		
			shouldFail(() -> buildNiReportDigest(spec, acts, new TrailCellsDigest("RAW / Funding / 2013 / Actual Commitments"))); // should not find column
		});
	}
}
