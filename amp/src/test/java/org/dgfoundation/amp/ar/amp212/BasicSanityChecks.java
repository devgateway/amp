package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.dgfoundation.amp.test.categories.SlowTests;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.ExceptionRunnable;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.ReportingTestCase;
import org.dgfoundation.amp.newreports.AmountsUnits;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportColumn;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportFiltersImpl;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.GrandTotalsDigest;
import org.dgfoundation.amp.nireports.TrailCellsDigest;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.digijava.kernel.ampapi.endpoints.util.DateFilterUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * basic sanity checks common between both the offdb schema and the AmpReportsSchema-using one.
 * These are not supposed to be exhaustive tests; instead they are concerned about "no stupid or weird things happening"
 * @author Dolghier Constantin
 *
 */
public abstract class BasicSanityChecks extends ReportingTestCase {
            
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

    final List<String> actsUnfilteredMeasures = Arrays.asList(
            "TAC_activity_1",
            "TAC_activity_2",
            "date-filters-activity",
            "SSC Project 1",
            "SSC Project 2",
            "pledged 2",
            "activity with capital spending",
            "activity with contracting agency",
            "activity 1 with agreement",
            "Test MTEF directed",
            "Unvalidated activity",
            "Proposed Project Cost 1 - USD"
        );
    
    final List<String> executionRateActs = Arrays.asList(
            "activity with capital spending",
            "Activity with planned disbursements",
            "execution rate activity",
            // with ER ends here
            "crazy funding 1",
            "third activity with agreements",
            "activity with tertiary_program"
        );

    final List<String> indicatorActs = Arrays.asList(
            "activity 1 with agreement",
            "activity 1 with indicators",
            "activity 2 with indicators"
    );

    static final List<String> HIERARCHIES_TO_TRY = new ImmutableList.Builder<String>()
            .add(ColumnConstants.STATUS)
            .add(ColumnConstants.IMPLEMENTATION_LEVEL)
            .add(ColumnConstants.PRIMARY_SECTOR)
            .add(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR)
            .add(ColumnConstants.SECONDARY_SECTOR)
            .add(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR)
            .add(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1)
            .add(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2)
            .add(ColumnConstants.COUNTRY)
            .add(ColumnConstants.REGION)
            .add(ColumnConstants.ZONE)
            .add(ColumnConstants.DISTRICT)
            .build();

    static final List<String> DONOR_HIERARCHIES_TO_TRY = new ImmutableList.Builder<String>()
            .addAll(HIERARCHIES_TO_TRY)
            .add(ColumnConstants.IMPLEMENTING_AGENCY)
            .add(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS)
            .add(ColumnConstants.IMPLEMENTING_AGENCY_TYPE)
            .add(ColumnConstants.DONOR_AGENCY)
            .add(ColumnConstants.DONOR_GROUP)
            .add(ColumnConstants.DONOR_TYPE)
            .add(ColumnConstants.FINANCING_INSTRUMENT)
            .add(ColumnConstants.TYPE_OF_ASSISTANCE)
            .add(ColumnConstants.MODE_OF_PAYMENT)
            .add(ColumnConstants.FUNDING_STATUS)
            .add(ColumnConstants.FUNDING_ID)
            .build();

//  protected ReportSpecificationImpl buildSpecification(String reportName, List<String> columns, List<String> measures, List<String> hierarchies, GroupingCriteria groupingCriteria) {
//      return ReportSpecificationImpl.buildFor(reportName, columns, measures, hierarchies, groupingCriteria);
//  }
        
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
//  @Test
//  public void testWithDates() {
//      ReportSpecification spec = buildSpecification("ByActivityUpdateOnByActivityCreatedOn", 
//          Arrays.asList(ColumnConstants.ACTIVITY_UPDATED_ON, ColumnConstants.ACTIVITY_CREATED_ON), 
//          Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
//          null, 
//          GroupingCriteria.GROUPING_YEARLY);
//      assertEquals(
//          "{0=1200, 1=123456, 2=97562.978885, 3=[2015-03-22], 4=[2015-03-22], 5=[2015-12-15], 6=1200, 7=123456, 8=93930.841736, 9=[2015-03-22], 10=[2015-03-22], 11=[2015-12-15], 12=3632.137149}",
//          buildDigest(spec, acts, new RawDataDigest(new HashSet<>(Arrays.asList(79l, 67L, 66L)))).toString());
//  }   
    
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
    final static GrandTotalsDigest fundingGrandTotalsDigester = new GrandTotalsDigest(z -> z.startsWith("RAW / Funding /") || z.startsWith("RAW / Totals"));
    
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
            for (String hierName : DONOR_HIERARCHIES_TO_TRY) {
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
            for (String hier1Name : DONOR_HIERARCHIES_TO_TRY)
                for (String hier2Name : DONOR_HIERARCHIES_TO_TRY)
                    if (hier1Name != hier2Name) {
                        reps ++;
                        ReportSpecificationImpl spec = buildSpecification(String.format("%s, %s summary: %b", hier1Name, hier2Name, isSummary), 
                                Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name), 
                                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                                Arrays.asList(hier1Name, hier2Name), 
                                GroupingCriteria.GROUPING_YEARLY);
                        spec.setSummaryReport(isSummary);
//                      if (!buildDigest(spec, acts, fundingGrandTotalsDigester).toString().equals(correctTotals)) {
//                          fails ++;
//                          System.err.println("failed: " + spec.getReportName());
//                      }
                        String digest = buildDigest(spec, acts, fundingGrandTotalsDigester).toString();
                        assertEquals(spec.getReportName(), correctTotals, digest);
            }
        }
        //System.err.println("nr of failures: " + fails);
        long delta = System.currentTimeMillis() - start;
        long speed = reps * 1000 / delta;
        double relativeSpeed = speed / 349.0;
        System.err.format("I ran %d double-hier reports in %d millies (%d per second, relativeSpeed: %.2f)\n", reps, delta, speed, relativeSpeed);
    }
    
    
    @Test
    @Category(SlowTests.class)
    public void testTripleHierarchiesDoNotChangeTotals() {
        if (this.getClass().getSimpleName().equals("AmpSchemaSanityTests"))
            return; // these are too slow if backed by DB
        long start = System.currentTimeMillis();
        int fails = 0;
        long reps = 0;
        // triple-hierarchy reports
        for(boolean isSummary:Arrays.asList(true, false)) {
            for (String hier1Name : DONOR_HIERARCHIES_TO_TRY)
                for (String hier2Name : DONOR_HIERARCHIES_TO_TRY)
                    for (String hier3Name : DONOR_HIERARCHIES_TO_TRY)
                    if (hier1Name != hier2Name && hier2Name != hier3Name && hier1Name != hier3Name) {
                        reps ++;
                        ReportSpecificationImpl spec = buildSpecification(String.format("%s, %s, %s summary: %b", hier1Name, hier2Name, hier3Name, isSummary), 
                                Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name, hier3Name), 
                                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                                Arrays.asList(hier1Name, hier2Name, hier3Name), 
                                GroupingCriteria.GROUPING_YEARLY);
                        spec.setSummaryReport(isSummary);
//                      if (!buildDigest(spec, acts, fundingGrandTotalsDigester).toString().equals(correctTotals)) {
//                          fails ++;
//                          System.err.println("failed: " + spec.getReportName());
//                      }
                        String digest = buildDigest(spec, acts, fundingGrandTotalsDigester).toString();
                        assertEquals(spec.getReportName(), correctTotals, digest);
            }
        }
        //System.err.println("nr of failures: " + fails);
        long delta = System.currentTimeMillis() - start;
        long speed = reps * 1000 / delta;
        double relativeSpeed = speed / 516.0;
        System.err.format("I ran %d triple-hier reports in %d millies (%d per second, relativeSpeed: %.2f)\n", reps, delta, speed, relativeSpeed);
    }
    
    @Test
    public void testSingleHierarchiesWithEmptyRowsDoNotChangeTotals() throws Exception {
        
        ReportSpecificationImpl initSpec = buildSpecification("initSpec", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE), 
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                null, 
                GroupingCriteria.GROUPING_YEARLY);
                
        assertEquals(correctTotals, buildDigest(initSpec, acts, fundingGrandTotalsDigester).toString());
                
        // single-hierarchy reports
        for(boolean isSummary:Arrays.asList(true, false)) {
            for (String hierName : DONOR_HIERARCHIES_TO_TRY) {
                ReportSpecificationImpl spec = buildSpecification(String.format("%s summary: %b", hierName, isSummary), 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, hierName), 
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                        Arrays.asList(hierName), 
                        GroupingCriteria.GROUPING_YEARLY);
                spec.setSummaryReport(isSummary);
                spec.setDisplayEmptyFundingRows(true);
                assertEquals(spec.getReportName(), correctTotals, buildDigest(spec, acts, fundingGrandTotalsDigester).toString());
            }
        }
    }

    @Test
    public void testDoubleHierarchiesWithEmptyRowsDoNotChangeTotals() {
        long start = System.currentTimeMillis();
        long reps = 0;
       
        // double-hierarchy reports
        for(boolean isSummary:Arrays.asList(true, false)) {
            for (String hier1Name : DONOR_HIERARCHIES_TO_TRY)
                for (String hier2Name : DONOR_HIERARCHIES_TO_TRY)
                    if (hier1Name != hier2Name) {
                        reps ++;
                        ReportSpecificationImpl spec = buildSpecification(String.format("%s, %s summary: %b", hier1Name, hier2Name, isSummary), 
                                Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name), 
                                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                                Arrays.asList(hier1Name, hier2Name), 
                                GroupingCriteria.GROUPING_YEARLY);
                        spec.setSummaryReport(isSummary);
                        spec.setDisplayEmptyFundingRows(true);
                        String digest = buildDigest(spec, acts, fundingGrandTotalsDigester).toString();
                        assertEquals(spec.getReportName(), correctTotals, digest);
            }
        }

        long delta = System.currentTimeMillis() - start;
        long speed = reps * 1000 / delta;
        double relativeSpeed = speed / 349.0;
        System.err.format("I ran %d double-hier reports in %d millies (%d per second, relativeSpeed: %.2f)\n", reps, delta, speed, relativeSpeed);
    }
    
    
    @Test
    @Category(SlowTests.class)
    public void testTripleHierarchiesWithEmptyRowsDoNotChangeTotals() {
        if (this.getClass().getSimpleName().equals("AmpSchemaSanityTests"))
            return; // these are too slow if backed by DB
        long start = System.currentTimeMillis();
        long reps = 0;
        
        // triple-hierarchy reports
        for(boolean isSummary:Arrays.asList(true, false)) {
            for (String hier1Name : DONOR_HIERARCHIES_TO_TRY)
                for (String hier2Name : DONOR_HIERARCHIES_TO_TRY)
                    for (String hier3Name : DONOR_HIERARCHIES_TO_TRY)
                    if (hier1Name != hier2Name && hier2Name != hier3Name && hier1Name != hier3Name) {
                        reps ++;
                        ReportSpecificationImpl spec = buildSpecification(String.format("%s, %s, %s summary: %b", hier1Name, hier2Name, hier3Name, isSummary), 
                                Arrays.asList(ColumnConstants.PROJECT_TITLE, hier1Name, hier2Name, hier3Name), 
                                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS), 
                                Arrays.asList(hier1Name, hier2Name, hier3Name), 
                                GroupingCriteria.GROUPING_YEARLY);
                        spec.setSummaryReport(isSummary);
                        spec.setDisplayEmptyFundingRows(true);
                        String digest = buildDigest(spec, acts, fundingGrandTotalsDigester).toString();
                        assertEquals(spec.getReportName(), correctTotals, digest);
            }
        }

        long delta = System.currentTimeMillis() - start;
        long speed = reps * 1000 / delta;
        double relativeSpeed = speed / 516.0;
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
        // for running manually: open http://localhost:8080/TEMPLATE/ampTemplate/saikuui_reports/index.html#report/open/32 on the AMP 2.10 testcases database
        
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
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Group", "American, National", "Totals-Actual Disbursements", "143,777"),
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
        spec.setDisplayTimeRangeSubtotals(false);
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
        spec.setDisplayTimeRangeSubtotals(false);

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
                        "-1: [entityId: -1, message: measure \"pipeline Estimated Disbursements\" not supported in NiReports]"))
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
                        "-1: [entityId: -1, message: measure \"pipeline Estimated Disbursements\" not supported in NiReports]"))
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
                    "-1: [entityId: -1, message: measure \"pipeline Estimated Disbursements\" not supported in NiReports]"))
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
                    "-1: [entityId: -1, message: measure \"pipeline Estimated Disbursements\" not supported in NiReports]"))
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
    
    @Test
    public void testActivityCountSpecFlat() {
        NiReportModel cor = new NiReportModel("testActivityCountFlat")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
                    "(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Region: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Activity Count: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Region", "", "Activity Count", "44", "Totals-Actual Commitments", "19,408,691,19")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Region", "Dubasari County", "Totals-Actual Commitments", "213,231"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Region", "Falesti County", "Totals-Actual Commitments", "999,888"),
                    new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Region", "Drochia County"),
                    new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Region", "Anenii Noi County"),
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Region", "Anenii Noi County"),
                    new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Region", "Cahul County"),
                    new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Region", "Anenii Noi County"),
                    new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Region", "Balti County"),
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Region", "Anenii Noi County"),
                    new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Region", ""),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Region", "", "Totals-Actual Commitments", "125,000"),
                    new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Region", "Anenii Noi County"),
                    new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Region", "Anenii Noi County", "Totals-Actual Commitments", "666,777"),
                    new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Region", "Anenii Noi County", "Totals-Actual Commitments", "333,222"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Region", "Anenii Noi County", "Totals-Actual Commitments", "111,333"),
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Region", "Edinet County", "Totals-Actual Commitments", "567,421"),
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Region", "Balti County", "Totals-Actual Commitments", "333,333"),
                    new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Region", "Anenii Noi County, Balti County", "Totals-Actual Commitments", "570,000"),
                    new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Region", "Anenii Noi County, Balti County", "Totals-Actual Commitments", "890,000"),
                    new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Region", "Anenii Noi County, Balti County", "Totals-Actual Commitments", "75,000"),
                    new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Region", "Chisinau City", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Region", "", "Totals-Actual Commitments", "50,000"),
                    new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Region", "", "Totals-Actual Commitments", "32,000"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Region", "", "Totals-Actual Commitments", "15,000"),
                    new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Region", "Chisinau County", "Totals-Actual Commitments", "5,000,000"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Region", "Cahul County", "Totals-Actual Commitments", "7,070,000"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Region", "Chisinau County", "Totals-Actual Commitments", "65,760,63"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Region", "Balti County, Transnistrian Region", "Totals-Actual Commitments", "96,840,58"),
                    new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Totals-Actual Commitments", "12,000"),
                    new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Region", "Transnistrian Region", "Totals-Actual Commitments", "123,321"),
                    new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Region", "", "Totals-Actual Commitments", "100"),
                    new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Region", "", "Totals-Actual Commitments", "45,000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Region", "Balti County", "Totals-Actual Commitments", "456,789"),
                    new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Region", "Chisinau County", "Totals-Actual Commitments", "1,200"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Region", "Chisinau City", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Region", "", "Totals-Actual Commitments", "123,000"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Region", ""),
                    new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Region", "Balti County, Drochia County", "Totals-Actual Commitments", "888,000"),
                    new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Region", "", "Totals-Actual Commitments", "150,000"),
                    new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Region", "Chisinau City", "Totals-Actual Commitments", "123,456"),
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Region", "Chisinau County"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Region", "Chisinau City, Dubasari County"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Region", "Drochia County"),
                    new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Region", "", "Totals-Actual Commitments", "97,562,98")      ));

        
        ReportSpecificationImpl spec = buildSpecification("testActivityCountFlat",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.ACTIVITY_COUNT),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
            null,
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testActivityCountSpecHier() {
        NiReportModel cor = new NiReportModel("testActivityCountSpecHier")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 4))",
                    "(Region: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Activity Count: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Region", "", "Project Title", "", "Activity Count", "31", "Totals-Actual Commitments", "19,408,691,19")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County", 9085))
                    .withContents("Project Title", "", "Activity Count", "6", "Totals-Actual Commitments", "1,611,832", "Region", "Anenii Noi County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Totals-Actual Commitments", "666,777"),
                      new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Totals-Actual Commitments", "333,222"),
                      new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Totals-Actual Commitments", "111,333"),
                      new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Totals-Actual Commitments", "285,000"),
                      new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Totals-Actual Commitments", "178,000"),
                      new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Totals-Actual Commitments", "37,500")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Balti County", 9086))
                    .withContents("Project Title", "", "Activity Count", "7", "Totals-Actual Commitments", "2,144,284,32", "Region", "Balti County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Totals-Actual Commitments", "333,333"),
                      new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Totals-Actual Commitments", "285,000"),
                      new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Totals-Actual Commitments", "712,000"),
                      new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Totals-Actual Commitments", "37,500"),
                      new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Totals-Actual Commitments", "53,262,32"),
                      new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Totals-Actual Commitments", "456,789"),
                      new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Actual Commitments", "266,400")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Cahul County", 9087)).withContents("Project Title", "", "Activity Count", "1", "Totals-Actual Commitments", "7,070,000", "Region", "Cahul County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Totals-Actual Commitments", "7,070,000")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau City", 9088))
                    .withContents("Project Title", "", "Activity Count", "3", "Totals-Actual Commitments", "296,912", "Region", "Chisinau City")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Totals-Actual Commitments", "50,000"),
                      new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Totals-Actual Commitments", "123,456"),
                      new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Totals-Actual Commitments", "123,456")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau County", 9089))
                    .withContents("Project Title", "", "Activity Count", "3", "Totals-Actual Commitments", "5,066,960,63", "Region", "Chisinau County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Totals-Actual Commitments", "5,000,000"),
                      new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Totals-Actual Commitments", "65,760,63"),
                      new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Totals-Actual Commitments", "1,200")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Drochia County", 9090)).withContents("Project Title", "", "Activity Count", "1", "Totals-Actual Commitments", "621,600", "Region", "Drochia County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Totals-Actual Commitments", "621,600")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Dubasari County", 9091)).withContents("Project Title", "", "Activity Count", "1", "Totals-Actual Commitments", "213,231", "Region", "Dubasari County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Totals-Actual Commitments", "213,231")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Edinet County", 9092)).withContents("Project Title", "", "Activity Count", "1", "Totals-Actual Commitments", "567,421", "Region", "Edinet County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Totals-Actual Commitments", "567,421")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Falesti County", 9093)).withContents("Project Title", "", "Activity Count", "1", "Totals-Actual Commitments", "999,888", "Region", "Falesti County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Totals-Actual Commitments", "999,888")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Transnistrian Region", 9105))
                    .withContents("Project Title", "", "Activity Count", "2", "Totals-Actual Commitments", "166,899,26", "Region", "Transnistrian Region")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Totals-Actual Commitments", "43,578,26"),
                      new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Totals-Actual Commitments", "123,321")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined", -8977))
                    .withContents("Project Title", "", "Activity Count", "10", "Totals-Actual Commitments", "649,662,98", "Region", "Region: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Totals-Actual Commitments", "125,000"),
                      new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Totals-Actual Commitments", "50,000"),
                      new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Totals-Actual Commitments", "32,000"),
                      new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Totals-Actual Commitments", "15,000"),
                      new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Totals-Actual Commitments", "12,000"),
                      new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Totals-Actual Commitments", "100"),
                      new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Totals-Actual Commitments", "45,000"),
                      new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Totals-Actual Commitments", "123,000"),
                      new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Totals-Actual Commitments", "150,000"),
                      new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Totals-Actual Commitments", "97,562,98")        )      ));

        
        ReportSpecificationImpl spec = buildSpecification("testActivityCountSpecHier",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.ACTIVITY_COUNT),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
            Arrays.asList(ColumnConstants.REGION),
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testActivityCountSpecHierSummary() {
        NiReportModel cor = new NiReportModel("testActivityCountSpecHierSummary")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 3))",
                    "(Region: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Activity Count: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                    "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Region", "", "Activity Count", "31", "Totals-Actual Commitments", "19,408,691,19")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Region", "Anenii Noi County", 9085), "Activity Count", "6", "Totals-Actual Commitments", "1,611,832", "Region", "Anenii Noi County"),
                    new ReportAreaForTests(new AreaOwner("Region", "Balti County", 9086), "Activity Count", "7", "Totals-Actual Commitments", "2,144,284,32", "Region", "Balti County"),
                    new ReportAreaForTests(new AreaOwner("Region", "Cahul County", 9087), "Activity Count", "1", "Totals-Actual Commitments", "7,070,000", "Region", "Cahul County"),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau City", 9088), "Activity Count", "3", "Totals-Actual Commitments", "296,912", "Region", "Chisinau City"),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau County", 9089), "Activity Count", "3", "Totals-Actual Commitments", "5,066,960,63", "Region", "Chisinau County"),
                    new ReportAreaForTests(new AreaOwner("Region", "Drochia County", 9090), "Activity Count", "1", "Totals-Actual Commitments", "621,600", "Region", "Drochia County"),
                    new ReportAreaForTests(new AreaOwner("Region", "Dubasari County", 9091), "Activity Count", "1", "Totals-Actual Commitments", "213,231", "Region", "Dubasari County"),
                    new ReportAreaForTests(new AreaOwner("Region", "Edinet County", 9092), "Activity Count", "1", "Totals-Actual Commitments", "567,421", "Region", "Edinet County"),
                    new ReportAreaForTests(new AreaOwner("Region", "Falesti County", 9093), "Activity Count", "1", "Totals-Actual Commitments", "999,888", "Region", "Falesti County"),
                    new ReportAreaForTests(new AreaOwner("Region", "Transnistrian Region", 9105), "Activity Count", "2", "Totals-Actual Commitments", "166,899,26", "Region", "Transnistrian Region"),
                    new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined", -8977), "Activity Count", "10", "Totals-Actual Commitments", "649,662,98", "Region", "Region: Undefined")      ));
        
        ReportSpecificationImpl spec = buildSpecification("testActivityCountSpecHierSummary",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION, ColumnConstants.ACTIVITY_COUNT),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
            Arrays.asList(ColumnConstants.REGION),
            GroupingCriteria.GROUPING_TOTALS_ONLY);
        
        spec.setSummaryReport(true);
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testExecutionRateFlatReport() {
        NiReportModel cor = new NiReportModel("testExecutionRateFlat")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 3))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2))",
                        "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Donor Agency", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Planned Disbursements", "146,300", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Planned Disbursements", "36,500", "Funding-2015-Actual Disbursements", "437,335", "Totals-Planned Disbursements", "182,800", "Totals-Actual Disbursements", "3,206,802", "Totals-Execution Rate", "1,754,27")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Donor Agency", "World Bank", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Actual Disbursements", "123,321", "Totals-Actual Disbursements", "123,321"),
                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Donor Agency", "Water Foundation", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Funding-2010-Actual Disbursements", "453,213", "Totals-Actual Disbursements", "453,213"),
                                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777"),
                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Economy, Ministry of Finance", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Donor Agency", "Finland, Norway, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "545,000", "Totals-Actual Disbursements", "545,000"),
                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Disbursements", "72,000"),
                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Disbursements", "555,111"),
                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Donor Agency", "Water Org", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Disbursements", "131,845"),
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Donor Agency", "Norway", "Primary Sector", "110 - EDUCATION, 120 - HEALTH"),
                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Donor Agency", "World Bank", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Donor Agency", "Finland, USAID", "Primary Sector", "113 - SECONDARY EDUCATION", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Disbursements", "450,000"),
                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "80,000", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "80,000", "Totals-Execution Rate", "88,89"),
                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION, 120 - HEALTH", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "50,000"),
                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Donor Agency", "Finland"),
                                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Donor Agency", "Finland, Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Donor Agency", "UNDP, Water Foundation, World Bank", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Disbursements", "321,765"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Donor Agency", "Ministry of Finance, UNDP, USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Donor Agency", "Norway, USAID", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Planned Disbursements", "500", "Funding-2015-Actual Disbursements", "570", "Totals-Planned Disbursements", "800", "Totals-Actual Disbursements", "770", "Totals-Execution Rate", "96,25"),
                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 113 - SECONDARY EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Donor Agency", "Norway, UNDP", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Donor Agency", "Ministry of Finance, UNDP", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "90,000", "Totals-Execution Rate", "97,83"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Donor Agency", "Finland, Norway, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000"),
                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Donor Agency", "Finland, Ministry of Finance, Norway", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION")      ));

        
        ReportSpecificationImpl spec = buildSpecification("testExecutionRateFlat",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.EXECUTION_RATE),
            null,
            GroupingCriteria.GROUPING_YEARLY
        );
        
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testExecutionRateFlatReportNotAffectedByScaledUnits() {
        NiReportModel cor = new NiReportModel("testExecutionRateFlatReportNotAffectedByScaledUnits")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 3))",
                        "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2))",
                        "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Donor Agency", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "780,31", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "1,266,96", "Funding-2014-Planned Disbursements", "146,3", "Funding-2014-Actual Disbursements", "710,2", "Funding-2015-Planned Disbursements", "36,5", "Funding-2015-Actual Disbursements", "437,34", "Totals-Planned Disbursements", "182,8", "Totals-Actual Disbursements", "3,206,8", "Totals-Execution Rate", "1,754,27")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Donor Agency", "World Bank", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Actual Disbursements", "123,32", "Totals-Actual Disbursements", "123,32"),
                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Donor Agency", "Water Foundation", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Funding-2010-Actual Disbursements", "453,21", "Totals-Actual Disbursements", "453,21"),
                                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Donor Agency", "Ministry of Economy, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "143,78", "Totals-Actual Disbursements", "143,78"),
                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project", "Donor Agency", "Ministry of Economy, Ministry of Finance", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Donor Agency", "Finland, Norway, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "545", "Totals-Actual Disbursements", "545"),
                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Donor Agency", "Ministry of Finance", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "60", "Funding-2012-Actual Disbursements", "12", "Totals-Actual Disbursements", "72"),
                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "555,11", "Totals-Actual Disbursements", "555,11"),
                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Donor Agency", "Water Org", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2013-Actual Disbursements", "131,84", "Totals-Actual Disbursements", "131,84"),
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages", "Donor Agency", "Norway", "Primary Sector", "110 - EDUCATION, 120 - HEALTH"),
                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program", "Donor Agency", "World Bank", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1", "Donor Agency", "USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Donor Agency", "Finland, USAID", "Primary Sector", "113 - SECONDARY EDUCATION", "Funding-2014-Actual Disbursements", "450", "Totals-Actual Disbursements", "450"),
                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "90", "Funding-2014-Actual Disbursements", "80", "Totals-Planned Disbursements", "90", "Totals-Actual Disbursements", "80", "Totals-Execution Rate", "88,89"),
                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION, 120 - HEALTH", "Funding-2014-Actual Disbursements", "50", "Totals-Actual Disbursements", "50"),
                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting", "Donor Agency", "Finland"),
                                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components", "Donor Agency", "Finland, Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components", "Donor Agency", "UNDP, Water Foundation, World Bank", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Donor Agency", "UNDP", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Disbursements", "321,76", "Totals-Actual Disbursements", "321,76"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements", "Donor Agency", "Ministry of Finance, UNDP, USAID", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement", "Donor Agency", "Finland", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Donor Agency", "Norway, USAID", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Planned Disbursements", "0,3", "Funding-2014-Actual Disbursements", "0,2", "Funding-2015-Planned Disbursements", "0,5", "Funding-2015-Actual Disbursements", "0,57", "Totals-Planned Disbursements", "0,8", "Totals-Actual Disbursements", "0,77", "Totals-Execution Rate", "96,25"),
                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response", "Donor Agency", "Finland, Ministry of Finance", "Primary Sector", "110 - EDUCATION, 113 - SECONDARY EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs", "Donor Agency", "Ministry of Economy", "Primary Sector", "110 - EDUCATION"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Donor Agency", "Norway, UNDP", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "35", "Funding-2014-Actual Disbursements", "75", "Totals-Actual Disbursements", "110"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Donor Agency", "Ministry of Finance, UNDP", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "56", "Funding-2014-Actual Disbursements", "55", "Funding-2015-Planned Disbursements", "36", "Funding-2015-Actual Disbursements", "35", "Totals-Planned Disbursements", "92", "Totals-Actual Disbursements", "90", "Totals-Execution Rate", "97,83"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Donor Agency", "Finland, Norway, USAID", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Disbursements", "80", "Totals-Actual Disbursements", "80"),
                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies", "Donor Agency", "Finland, Ministry of Finance, Norway", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION")      ));

        ReportSpecificationImpl spec = buildSpecification("testExecutionRateFlatReportNotAffectedByScaledUnits",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
                Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.EXECUTION_RATE),
                null,
                GroupingCriteria.GROUPING_YEARLY
        );
        ReportSettingsImpl settings = new ReportSettingsImpl();
        settings.setUnitsOption(AmountsUnits.AMOUNTS_OPTION_THOUSANDS);
        spec.setSettings(settings);

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testExecutionRateByDonor() {
        NiReportModel cor = new NiReportModel("testExecutionRateByDonor")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
                "(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 3))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2))",
                "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Donor Agency", "", "Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Planned Disbursements", "146,300", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Planned Disbursements", "36,500", "Funding-2015-Actual Disbursements", "437,335", "Totals-Planned Disbursements", "182,800", "Totals-Actual Disbursements", "3,206,802", "Totals-Execution Rate", "1,754,27")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698))
                .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "575,111", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "130,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "321,765", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "1,026,876", "Totals-Execution Rate", "1,140,97", "Donor Agency", "Finland")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "20,000", "Totals-Actual Disbursements", "20,000"),
                  new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Disbursements", "555,111"),
                  new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "80,000", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "80,000", "Totals-Execution Rate", "88,89"),
                  new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION, 120 - HEALTH", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Disbursements", "50,000"),
                  new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Primary Sector", "110 - EDUCATION, 112 - BASIC EDUCATION", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Disbursements", "321,765")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "143,777", "Donor Agency", "Ministry of Economy")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699))
                .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "162,000", "Totals-Execution Rate", "176,09", "Donor Agency", "Ministry of Finance")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Primary Sector", "110 - EDUCATION", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Disbursements", "72,000"),
                  new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "90,000", "Totals-Execution Rate", "97,83")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694))
                .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "110,000", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "110,700", "Totals-Execution Rate", "15,814,29", "Donor Agency", "Norway")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "110,000", "Totals-Actual Disbursements", "110,000"),
                  new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "700", "Totals-Execution Rate", "100")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "110,000", "Donor Agency", "UNDP")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696))
                .withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "80,070", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "945,070", "Totals-Execution Rate", "945,070", "Donor Agency", "USAID")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                  new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Primary Sector", "113 - SECONDARY EDUCATION", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Disbursements", "450,000"),
                  new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "70", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "70", "Totals-Execution Rate", "70"),
                  new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Foundation", 21702)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "453,213", "Donor Agency", "Water Foundation")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", "Funding-2010-Actual Disbursements", "453,213", "Totals-Actual Disbursements", "453,213")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Org", 21701)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "131,845", "Donor Agency", "Water Org")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Disbursements", "131,845")        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "World Bank", 21697)).withContents("Project Title", "", "Primary Sector", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "123,321", "Donor Agency", "World Bank")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2010-Actual Disbursements", "123,321", "Totals-Actual Disbursements", "123,321")        )      ));
        
        ReportSpecificationImpl spec = buildSpecification("testExecutionRateByDonor",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.EXECUTION_RATE),
            Arrays.asList(ColumnConstants.DONOR_AGENCY),
            GroupingCriteria.GROUPING_YEARLY
        );
        
        runNiTestCase(spec, "en", acts, cor);
    }
    
    @Test
    public void testExecutionRateByDonorByPrimarySector() {
        NiReportModel cor = new NiReportModel("testExecutionRateByDonorByPS")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 16))",
                "(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 10));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 13, colSpan: 3))",
                "(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2))",
                "(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Planned Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Donor Agency", "", "Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "1,266,956", "Funding-2014-Planned Disbursements", "146,300", "Funding-2014-Actual Disbursements", "710,200", "Funding-2015-Planned Disbursements", "36,500", "Funding-2015-Actual Disbursements", "437,335", "Totals-Planned Disbursements", "182,800", "Totals-Actual Disbursements", "3,206,802", "Totals-Execution Rate", "1,754,27")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698))
                .withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "575,111", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "130,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "321,765", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "1,026,876", "Totals-Execution Rate", "1,140,97", "Donor Agency", "Finland")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                  .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "575,111", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "110,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "845,993,5", "Totals-Execution Rate", "939,99", "Primary Sector", "110 - EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "20,000", "Totals-Actual Disbursements", "20,000"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Disbursements", "555,111"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Planned Disbursements", "90,000", "Funding-2014-Actual Disbursements", "80,000", "Totals-Planned Disbursements", "90,000", "Totals-Actual Disbursements", "80,000", "Totals-Execution Rate", "88,89"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2014-Actual Disbursements", "30,000", "Totals-Actual Disbursements", "30,000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Disbursements", "160,882,5")          ),
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242))
                  .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "5,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "165,882,5", "Primary Sector", "112 - BASIC EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2014-Actual Disbursements", "5,000", "Totals-Actual Disbursements", "5,000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Disbursements", "160,882,5")          ),
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "120 - HEALTH", 6252)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "15,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "15,000", "Primary Sector", "120 - HEALTH")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2014-Actual Disbursements", "15,000", "Totals-Actual Disbursements", "15,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "143,777", "Donor Agency", "Ministry of Economy")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "143,777", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "143,777", "Primary Sector", "110 - EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777")          )        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "162,000", "Totals-Execution Rate", "176,09", "Donor Agency", "Ministry of Finance")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                  .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "162,000", "Totals-Execution Rate", "176,09", "Primary Sector", "110 - EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Disbursements", "72,000"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Planned Disbursements", "56,000", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Planned Disbursements", "36,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Planned Disbursements", "92,000", "Totals-Actual Disbursements", "90,000", "Totals-Execution Rate", "97,83")          )        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694))
                .withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "110,000", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "110,700", "Totals-Execution Rate", "15,814,29", "Donor Agency", "Norway")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "110,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "110,000", "Primary Sector", "110 - EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "110,000", "Totals-Actual Disbursements", "110,000")          ),
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "700", "Totals-Execution Rate", "100", "Primary Sector", "112 - BASIC EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2014-Planned Disbursements", "300", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Planned Disbursements", "400", "Funding-2015-Actual Disbursements", "500", "Totals-Planned Disbursements", "700", "Totals-Actual Disbursements", "700", "Totals-Execution Rate", "100")          )        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "110,000", "Donor Agency", "UNDP")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "75,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "110,000", "Primary Sector", "110 - EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb", "Funding-2013-Actual Disbursements", "35,000", "Funding-2014-Actual Disbursements", "75,000", "Totals-Actual Disbursements", "110,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696))
                .withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "80,070", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "945,070", "Totals-Execution Rate", "945,070", "Donor Agency", "USAID")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                  .withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "415,000", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "80,000", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "495,000", "Primary Sector", "110 - EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Funding-2013-Actual Disbursements", "415,000", "Totals-Actual Disbursements", "415,000"),
                    new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs", "Funding-2015-Actual Disbursements", "80,000", "Totals-Actual Disbursements", "80,000")          ),
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "70", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "70", "Totals-Execution Rate", "70", "Primary Sector", "112 - BASIC EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Funding-2015-Planned Disbursements", "100", "Funding-2015-Actual Disbursements", "70", "Totals-Planned Disbursements", "100", "Totals-Actual Disbursements", "70", "Totals-Execution Rate", "70")          ),
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "113 - SECONDARY EDUCATION", 6246)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "450,000", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "450,000", "Primary Sector", "113 - SECONDARY EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Disbursements", "450,000")          )        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Foundation", 21702)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "453,213", "Donor Agency", "Water Foundation")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH", 6267)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "453,213", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "453,213", "Primary Sector", "130 - POPULATION POLICIES/PROGRAMMES AND REPRODUCTIVE HEALTH")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Totals-Actual Disbursements", "453,213")          )        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "Water Org", 21701)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "131,845", "Donor Agency", "Water Org")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "0", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "131,845", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "131,845", "Primary Sector", "112 - BASIC EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Disbursements", "131,845")          )        ),
                new ReportAreaForTests(new AreaOwner("Donor Agency", "World Bank", 21697)).withContents("Primary Sector", "", "Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "123,321", "Donor Agency", "World Bank")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner("Primary Sector", "112 - BASIC EDUCATION", 6242)).withContents("Project Title", "", "Funding-2010-Planned Disbursements", "0", "Funding-2010-Actual Disbursements", "123,321", "Funding-2012-Planned Disbursements", "0", "Funding-2012-Actual Disbursements", "0", "Funding-2013-Planned Disbursements", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Planned Disbursements", "0", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Planned Disbursements", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Planned Disbursements", "0", "Totals-Actual Disbursements", "123,321", "Primary Sector", "112 - BASIC EDUCATION")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Totals-Actual Disbursements", "123,321")          )        )      ));

        
        ReportSpecificationImpl spec = buildSpecification("testExecutionRateByDonorByPS",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.PLANNED_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.EXECUTION_RATE),
            Arrays.asList(ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR),
            GroupingCriteria.GROUPING_YEARLY
        );
        
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testUnfilteredMeasuresInUnfilteredReport() {
        NiReportModel cor = new NiReportModel("testUnfilteredMeasuresInUnfilteredReport")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 22))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 16));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 17, colSpan: 5))",
                "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Cumulative Commitment: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Cumulative Disbursement: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Cumulative Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "780,311", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2011-Actual Disbursements", "0", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "3,348,754", "Funding-2013-Actual Disbursements", "686,956", "Funding-2014-Actual Commitments", "4,465,760,63", "Funding-2014-Actual Disbursements", "580,000", "Funding-2015-Actual Commitments", "501,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "9,751,263,21", "Totals-Actual Disbursements", "2,381,032", "Totals-Cumulative Commitment", "9,751,263,21", "Totals-Cumulative Disbursement", "2,381,032", "Totals-Cumulative Execution Rate", "24,42")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Funding-2010-Actual Disbursements", "123,321", "Funding-2011-Actual Commitments", "213,231", "Totals-Actual Commitments", "213,231", "Totals-Actual Disbursements", "123,321", "Totals-Cumulative Commitment", "213,231", "Totals-Cumulative Disbursement", "123,321", "Totals-Cumulative Execution Rate", "57,83"),
                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Funding-2010-Actual Disbursements", "453,213", "Funding-2011-Actual Commitments", "999,888", "Totals-Actual Commitments", "999,888", "Totals-Actual Disbursements", "453,213", "Totals-Cumulative Commitment", "999,888", "Totals-Cumulative Disbursement", "453,213", "Totals-Cumulative Execution Rate", "45,33"),
                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777", "Totals-Cumulative Disbursement", "143,777"),
                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000", "Totals-Cumulative Commitment", "125,000", "Totals-Cumulative Disbursement", "72,000", "Totals-Cumulative Execution Rate", "57,6"),
                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111", "Totals-Cumulative Commitment", "111,333", "Totals-Cumulative Disbursement", "555,111", "Totals-Cumulative Execution Rate", "498,6"),
                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Totals-Cumulative Commitment", "567,421", "Totals-Cumulative Disbursement", "131,845", "Totals-Cumulative Execution Rate", "23,24"),
                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Totals-Cumulative Commitment", "7,070,000", "Totals-Cumulative Disbursement", "450,000", "Totals-Cumulative Execution Rate", "6,36"),
                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Totals-Cumulative Commitment", "65,760,63", "Totals-Cumulative Disbursement", "80,000", "Totals-Cumulative Execution Rate", "121,65"),
                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2014-Actual Disbursements", "50,000", "Totals-Actual Commitments", "96,840,58", "Totals-Actual Disbursements", "50,000", "Totals-Cumulative Commitment", "96,840,58", "Totals-Cumulative Disbursement", "50,000", "Totals-Cumulative Execution Rate", "51,63"),
                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000", "Totals-Cumulative Commitment", "45,000"),
                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "456,789", "Funding-2015-Actual Disbursements", "321,765", "Totals-Actual Commitments", "456,789", "Totals-Actual Disbursements", "321,765", "Totals-Cumulative Commitment", "456,789", "Totals-Cumulative Disbursement", "321,765", "Totals-Cumulative Execution Rate", "70,44")      ));
        
        ReportSpecificationImpl spec = buildSpecification("testUnfilteredMeasuresInUnfilteredReport",
            Arrays.asList(ColumnConstants.PROJECT_TITLE),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.CUMULATIVE_COMMITMENT, MeasureConstants.CUMULATIVE_DISBURSEMENT, MeasureConstants.CUMULATIVE_EXECUTION_RATE),
            null,
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", actsUnfilteredMeasures, cor);
    }
    
    @Test
    public void testUnfilteredMeasuresInDateFilteredReport() throws Exception {
        NiReportModel cor = new NiReportModel("testUnfilteredMeasuresInDateFilteredReport")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 10))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 4));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 5))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Cumulative Commitment: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Cumulative Disbursement: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Cumulative Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Funding-2013-Actual Commitments", "3,348,754", "Funding-2013-Actual Disbursements", "686,956", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,748,754", "Totals-Actual Disbursements", "1,136,956", "Totals-Cumulative Commitment", "9,751,263,21", "Totals-Cumulative Disbursement", "2,381,032", "Totals-Cumulative Execution Rate", "24,42")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1", "Totals-Cumulative Commitment", "213,231", "Totals-Cumulative Disbursement", "123,321", "Totals-Cumulative Execution Rate", "57,83"),
                    new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2", "Totals-Cumulative Commitment", "999,888", "Totals-Cumulative Disbursement", "453,213", "Totals-Cumulative Execution Rate", "45,33"),
                    new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Totals-Cumulative Disbursement", "143,777"),
                    new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Totals-Cumulative Commitment", "125,000", "Totals-Cumulative Disbursement", "72,000", "Totals-Cumulative Execution Rate", "57,6"),
                    new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111", "Totals-Cumulative Commitment", "111,333", "Totals-Cumulative Disbursement", "555,111", "Totals-Cumulative Execution Rate", "498,6"),
                    new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2", "Funding-2013-Actual Commitments", "567,421", "Funding-2013-Actual Disbursements", "131,845", "Totals-Actual Commitments", "567,421", "Totals-Actual Disbursements", "131,845", "Totals-Cumulative Commitment", "567,421", "Totals-Cumulative Disbursement", "131,845", "Totals-Cumulative Execution Rate", "23,24"),
                    new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2", "Funding-2013-Actual Commitments", "2,670,000", "Funding-2014-Actual Commitments", "4,400,000", "Funding-2014-Actual Disbursements", "450,000", "Totals-Actual Commitments", "7,070,000", "Totals-Actual Disbursements", "450,000", "Totals-Cumulative Commitment", "7,070,000", "Totals-Cumulative Disbursement", "450,000", "Totals-Cumulative Execution Rate", "6,36"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Totals-Cumulative Commitment", "65,760,63", "Totals-Cumulative Disbursement", "80,000", "Totals-Cumulative Execution Rate", "121,65"),
                    new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Totals-Cumulative Commitment", "96,840,58", "Totals-Cumulative Disbursement", "50,000", "Totals-Cumulative Execution Rate", "51,63"),
                    new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Totals-Cumulative Commitment", "45,000"),
                    new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Totals-Cumulative Commitment", "456,789", "Totals-Cumulative Disbursement", "321,765", "Totals-Cumulative Execution Rate", "70,44")      ));
        
        ReportSpecificationImpl spec = buildSpecification("testUnfilteredMeasuresInDateFilteredReport",
            Arrays.asList(ColumnConstants.PROJECT_TITLE),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.CUMULATIVE_COMMITMENT, MeasureConstants.CUMULATIVE_DISBURSEMENT, MeasureConstants.CUMULATIVE_EXECUTION_RATE),
            null,
            GroupingCriteria.GROUPING_YEARLY);
        
        ReportFiltersImpl arf = new ReportFiltersImpl();
        arf.addFilterRule(new ReportElement(ReportElement.ElementType.DATE), DateFilterUtils.getDatesRangeFilterRule(ReportElement.ElementType.DATE, 2456353, 2456897, "2456353", "2456897", true));
        spec.setFilters(arf);
        
        runNiTestCase(spec, "en", actsUnfilteredMeasures, cor);
    }

    @Test
    public void testUnfilteredMeasuresInSectorFilteredReport() throws Exception {
        NiReportModel cor = new NiReportModel("testUnfilteredMeasuresInSectorFilteredReport")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 20))",
                "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 14));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 15, colSpan: 5))",
                "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2010: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2))",
                "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Cumulative Commitment: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Cumulative Disbursement: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Cumulative Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
              .withContents("Project Title", "", "Funding-2006-Actual Commitments", "58,104,35", "Funding-2006-Actual Disbursements", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2009-Actual Disbursements", "0", "Funding-2010-Actual Commitments", "0", "Funding-2010-Actual Disbursements", "203,777", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "110,000", "Funding-2015-Actual Commitments", "273,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "633,592,48", "Totals-Actual Disbursements", "1,041,770,5", "Totals-Cumulative Commitment", "633,592,48", "Totals-Cumulative Disbursement", "1,041,770,5", "Totals-Cumulative Execution Rate", "164,42")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Funding-2010-Actual Disbursements", "143,777", "Totals-Actual Disbursements", "143,777", "Totals-Cumulative Disbursement", "143,777"),
                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity", "Funding-2009-Actual Commitments", "100,000", "Funding-2010-Actual Disbursements", "60,000", "Funding-2012-Actual Commitments", "25,000", "Funding-2012-Actual Disbursements", "12,000", "Totals-Actual Commitments", "125,000", "Totals-Actual Disbursements", "72,000", "Totals-Cumulative Commitment", "125,000", "Totals-Cumulative Disbursement", "72,000", "Totals-Cumulative Execution Rate", "57,6"),
                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1", "Funding-2013-Actual Commitments", "111,333", "Funding-2013-Actual Disbursements", "555,111", "Totals-Actual Commitments", "111,333", "Totals-Actual Disbursements", "555,111", "Totals-Cumulative Commitment", "111,333", "Totals-Cumulative Disbursement", "555,111", "Totals-Cumulative Execution Rate", "498,6"),
                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Totals-Cumulative Commitment", "65,760,63", "Totals-Cumulative Disbursement", "80,000", "Totals-Cumulative Execution Rate", "121,65"),
                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency", "Funding-2006-Actual Commitments", "58,104,35", "Funding-2014-Actual Disbursements", "30,000", "Totals-Actual Commitments", "58,104,35", "Totals-Actual Disbursements", "30,000", "Totals-Cumulative Commitment", "58,104,35", "Totals-Cumulative Disbursement", "30,000", "Totals-Cumulative Execution Rate", "51,63"),
                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity", "Funding-2015-Actual Commitments", "45,000", "Totals-Actual Commitments", "45,000", "Totals-Cumulative Commitment", "45,000"),
                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement", "Funding-2015-Actual Commitments", "228,394,5", "Funding-2015-Actual Disbursements", "160,882,5", "Totals-Actual Commitments", "228,394,5", "Totals-Actual Disbursements", "160,882,5", "Totals-Cumulative Commitment", "228,394,5", "Totals-Cumulative Disbursement", "160,882,5", "Totals-Cumulative Execution Rate", "70,44")      ));
        
        ReportSpecificationImpl spec = buildSpecification("testUnfilteredMeasuresInSectorFilteredReport",
            Arrays.asList(ColumnConstants.PROJECT_TITLE),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.CUMULATIVE_COMMITMENT, MeasureConstants.CUMULATIVE_DISBURSEMENT, MeasureConstants.CUMULATIVE_EXECUTION_RATE),
            null,
            GroupingCriteria.GROUPING_YEARLY);
        
        ReportFiltersImpl arf = new ReportFiltersImpl();
        arf.addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule(Arrays.asList("6236"), true));
        spec.setFilters(arf);
        
        runNiTestCase(spec, "en", actsUnfilteredMeasures, cor);
    }

    @Test
    public void testAverageMeasureFlat() {
        NiReportModel cor = new NiReportModel("testAverageMeasureFlat")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 4))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Average Disbursement Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Primary Sector", "", "Donor Agency", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "80,760,63", "Funding-2014-Actual Disbursements", "135,000", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "537,549,63", "Totals-Actual Disbursements", "170,000", "Totals-Execution Rate", "93,41", "Totals-Average Disbursement Rate", "93,36")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Finland", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Ministry of Economy", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Finland", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Totals-Execution Rate", "88,89", "Totals-Average Disbursement Rate", "88,89"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Finland, Ministry of Finance", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Donor Agency", "Ministry of Finance, UNDP", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000", "Totals-Execution Rate", "97,83", "Totals-Average Disbursement Rate", "97,83")      ));
    
        
        ReportSpecificationImpl spec = buildSpecification("testAverageMeasureFlat",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.DONOR_AGENCY),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.EXECUTION_RATE, MeasureConstants.AVERAGE_DISBURSEMENT_RATE),
            null,
            GroupingCriteria.GROUPING_YEARLY);
        
        ReportFiltersImpl arf = new ReportFiltersImpl();
        arf.addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule(Arrays.asList("6236"), true));
        spec.setFilters(arf);
        
        runNiTestCase(spec, "en", executionRateActs, cor);
    }   
    
    @Test
    public void testAverageMeasureSingleHier() {
        NiReportModel cor = new NiReportModel("testAverageMeasureSingleHier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                        "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 4))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Average Disbursement Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Project Title", "", "Donor Agency", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "80,760,63", "Funding-2014-Actual Disbursements", "135,000", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "537,549,63", "Totals-Actual Disbursements", "170,000", "Totals-Execution Rate", "93,41", "Totals-Average Disbursement Rate", "93,36")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                                        .withContents("Project Title", "", "Donor Agency", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "80,760,63", "Funding-2014-Actual Disbursements", "135,000", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "537,549,63", "Totals-Actual Disbursements", "170,000", "Totals-Execution Rate", "93,41", "Totals-Average Disbursement Rate", "93,36", "Primary Sector", "110 - EDUCATION")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Donor Agency", "Finland", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Donor Agency", "Ministry of Economy", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000"),
                                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Donor Agency", "Finland", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Totals-Execution Rate", "88,89", "Totals-Average Disbursement Rate", "88,89"),
                                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Donor Agency", "Finland, Ministry of Finance", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Donor Agency", "Ministry of Finance, UNDP", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000", "Totals-Execution Rate", "97,83", "Totals-Average Disbursement Rate", "97,83")        )      ));
        
        ReportSpecificationImpl spec = buildSpecification("testAverageMeasureSingleHier",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.DONOR_AGENCY),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.EXECUTION_RATE, MeasureConstants.AVERAGE_DISBURSEMENT_RATE),
            Arrays.asList(ColumnConstants.PRIMARY_SECTOR),
            GroupingCriteria.GROUPING_YEARLY);
        
        ReportFiltersImpl arf = new ReportFiltersImpl();
        arf.addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule(Arrays.asList("6236"), true));
        spec.setFilters(arf);
        
        runNiTestCase(spec, "en", executionRateActs, cor);
    }
    
    @Test
    public void testAverageMeasureDualHier() {
        NiReportModel cor = new NiReportModel("testAverageMeasureDualHier")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 13))",
                    "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 9, colSpan: 4))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Execution Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Average Disbursement Rate: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Donor Agency", "", "Project Title", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "80,760,63", "Funding-2014-Actual Disbursements", "135,000", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "537,549,63", "Totals-Actual Disbursements", "170,000", "Totals-Execution Rate", "93,41", "Totals-Average Disbursement Rate", "93,36")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                    .withContents("Donor Agency", "", "Project Title", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "80,760,63", "Funding-2014-Actual Disbursements", "135,000", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "537,549,63", "Totals-Actual Disbursements", "170,000", "Totals-Execution Rate", "93,41", "Totals-Average Disbursement Rate", "93,36", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698))
                      .withContents("Project Title", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "399,093,63", "Totals-Actual Disbursements", "80,000", "Totals-Execution Rate", "88,89", "Totals-Average Disbursement Rate", "88,89", "Donor Agency", "Finland")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333"),
                        new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Totals-Execution Rate", "88,89", "Totals-Average Disbursement Rate", "88,89")          ),
                      new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Project Title", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "15,000", "Funding-2014-Actual Disbursements", "0", "Funding-2015-Actual Commitments", "0", "Funding-2015-Actual Disbursements", "0", "Totals-Actual Commitments", "15,000", "Totals-Actual Disbursements", "0", "Totals-Average Disbursement Rate", "0", "Donor Agency", "Ministry of Economy")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000")          ),
                      new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Finance", 21699))
                      .withContents("Project Title", "", "Funding-2013-Actual Commitments", "0", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "0", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Commitments", "123,456", "Totals-Actual Disbursements", "90,000", "Totals-Execution Rate", "97,83", "Totals-Average Disbursement Rate", "97,83", "Donor Agency", "Ministry of Finance")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456"),
                        new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000", "Totals-Execution Rate", "97,83", "Totals-Average Disbursement Rate", "97,83")          )        )      ));
        
        ReportSpecificationImpl spec = buildSpecification("testAverageMeasureDualHier",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.DONOR_AGENCY),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.EXECUTION_RATE, MeasureConstants.AVERAGE_DISBURSEMENT_RATE),
            Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.DONOR_AGENCY),
            GroupingCriteria.GROUPING_YEARLY);
        
        ReportFiltersImpl arf = new ReportFiltersImpl();
        arf.addFilterRule(new ReportElement(new ReportColumn(ColumnConstants.PRIMARY_SECTOR)), new FilterRule(Arrays.asList("6236"), true));
        spec.setFilters(arf);
        
        runNiTestCase(spec, "en", executionRateActs, cor);
    }
    
    @Test
    public void testFetchedMeasureTotal() {
        NiReportModel cor = new NiReportModel("testFetchedMeasureTotal")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 4))",
                    "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                    "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Percentage of Total Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Percentage Of Total Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Primary Sector", "", "Funding-2013-Actual Commitments", "333,333", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "80,760,63", "Funding-2014-Actual Disbursements", "135,200", "Funding-2015-Actual Commitments", "123,456", "Funding-2015-Actual Disbursements", "35,570", "Totals-Actual Commitments", "537,549,63", "Totals-Actual Disbursements", "170,770", "Totals-Percentage of Total Commitments", "100", "Totals-Percentage Of Total Disbursements", "100")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,333", "Totals-Actual Commitments", "333,333", "Totals-Percentage of Total Commitments", "62,01"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "15,000", "Totals-Actual Commitments", "15,000", "Totals-Percentage of Total Commitments", "2,79"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "65,760,63", "Funding-2014-Actual Disbursements", "80,000", "Totals-Actual Commitments", "65,760,63", "Totals-Actual Disbursements", "80,000", "Totals-Percentage of Total Commitments", "12,23", "Totals-Percentage Of Total Disbursements", "46,85"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,456", "Totals-Actual Commitments", "123,456", "Totals-Percentage of Total Commitments", "22,97"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Actual Disbursements", "200", "Funding-2015-Actual Disbursements", "570", "Totals-Actual Disbursements", "770", "Totals-Percentage Of Total Disbursements", "0,45"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Disbursements", "55,000", "Funding-2015-Actual Disbursements", "35,000", "Totals-Actual Disbursements", "90,000", "Totals-Percentage Of Total Disbursements", "52,7")      ));

        
        ReportSpecificationImpl spec = buildSpecification("testFetchedMeasureTotal",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS, MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS),
            null,
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", executionRateActs, cor);
    }

    @Test
    public void testFetchedMeasureTotalAndScaledByThousands() {
        NiReportModel cor = new NiReportModel("testFetchedMeasureTotalAndScaledByThousands")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                        "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 6));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 4))",
                        "(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 2));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 2));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 2))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Percentage of Total Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Percentage Of Total Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "", "Primary Sector", "", "Funding-2013-Actual Commitments", "333,33", "Funding-2013-Actual Disbursements", "0", "Funding-2014-Actual Commitments", "80,76", "Funding-2014-Actual Disbursements", "135,2", "Funding-2015-Actual Commitments", "123,46", "Funding-2015-Actual Disbursements", "35,57", "Totals-Actual Commitments", "537,55", "Totals-Actual Disbursements", "170,77", "Totals-Percentage of Total Commitments", "100", "Totals-Percentage Of Total Disbursements", "100")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Primary Sector", "110 - EDUCATION", "Funding-2013-Actual Commitments", "333,33", "Totals-Actual Commitments", "333,33", "Totals-Percentage of Total Commitments", "62,01"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "15", "Totals-Actual Commitments", "15", "Totals-Percentage of Total Commitments", "2,79"),
                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Commitments", "65,76", "Funding-2014-Actual Disbursements", "80", "Totals-Actual Commitments", "65,76", "Totals-Actual Disbursements", "80", "Totals-Percentage of Total Commitments", "12,23", "Totals-Percentage Of Total Disbursements", "46,85"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Primary Sector", "110 - EDUCATION", "Funding-2015-Actual Commitments", "123,46", "Totals-Actual Commitments", "123,46", "Totals-Percentage of Total Commitments", "22,97"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Funding-2014-Actual Disbursements", "0,2", "Funding-2015-Actual Disbursements", "0,57", "Totals-Actual Disbursements", "0,77", "Totals-Percentage Of Total Disbursements", "0,45"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Funding-2014-Actual Disbursements", "55", "Funding-2015-Actual Disbursements", "35", "Totals-Actual Disbursements", "90", "Totals-Percentage Of Total Disbursements", "52,7")      ));

        ReportSpecificationImpl spec = buildSpecification("testFetchedMeasureTotalAndScaledByThousands",
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS, MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS),
                null,
                GroupingCriteria.GROUPING_YEARLY);
        ReportSettingsImpl settings = new ReportSettingsImpl();
        settings.setUnitsOption(AmountsUnits.AMOUNTS_OPTION_THOUSANDS);
        spec.setSettings(settings);

        runNiTestCase(spec, "en", executionRateActs, cor);
    }

    @Test
    public void testFetchedMeasureTotalMissingPrecursor() {
        NiReportModel cor = new NiReportModel("testFetchedMeasureTotalMissingPrecursor")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 4))",
                    "(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 2))",
                    "",
                    "(Percentage of Total Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Percentage Of Total Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Project Title", "", "Primary Sector", "", "Totals-Percentage of Total Commitments", "100", "Totals-Percentage Of Total Disbursements", "100")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Primary Sector", "110 - EDUCATION", "Totals-Percentage of Total Commitments", "62,01"),
                    new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Primary Sector", "110 - EDUCATION", "Totals-Percentage of Total Commitments", "2,79"),
                    new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Primary Sector", "110 - EDUCATION", "Totals-Percentage of Total Commitments", "12,23", "Totals-Percentage Of Total Disbursements", "46,85"),
                    new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Primary Sector", "110 - EDUCATION", "Totals-Percentage of Total Commitments", "22,97"),
                    new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Primary Sector", "112 - BASIC EDUCATION", "Totals-Percentage Of Total Disbursements", "0,45"),
                    new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Primary Sector", "110 - EDUCATION", "Totals-Percentage Of Total Disbursements", "52,7")      ));

        
        ReportSpecificationImpl spec = buildSpecification("testFetchedMeasureTotalMissingPrecursor",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR),
            Arrays.asList(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS, MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS),
            null,
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", executionRateActs, cor);
    }
    
    @Test
    public void testFetchedMeasureTotalMissingPrecursorHier() {
        NiReportModel cor = new NiReportModel("testFetchedMeasureTotalMissingPrecursorHier")
            .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 4))",
                "(Region: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 2))",
                "",
                "(Percentage of Total Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Percentage Of Total Disbursements: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null)
            .withContents("Region", "", "Project Title", "", "Totals-Percentage of Total Commitments", "100", "Totals-Percentage Of Total Disbursements", "100")
            .withChildren(
                    new ReportAreaForTests(new AreaOwner("Region", "Balti County", 9086)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "62,01", "Totals-Percentage Of Total Disbursements", "0", "Region", "Balti County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1", "Totals-Percentage of Total Commitments", "62,01")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau City", 9088))
                    .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "22,97", "Totals-Percentage Of Total Disbursements", "26,35", "Region", "Chisinau City")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements", "Totals-Percentage of Total Commitments", "22,97"),
                      new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Totals-Percentage Of Total Disbursements", "26,35")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Chisinau County", 9089)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "12,23", "Totals-Percentage Of Total Disbursements", "46,85", "Region", "Chisinau County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending", "Totals-Percentage of Total Commitments", "12,23", "Totals-Percentage Of Total Disbursements", "46,85")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Dubasari County", 9091)).withContents("Project Title", "", "Totals-Percentage of Total Commitments", "0", "Totals-Percentage Of Total Disbursements", "26,35", "Region", "Dubasari County")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity", "Totals-Percentage Of Total Disbursements", "26,35")        ),
                    new ReportAreaForTests(new AreaOwner("Region", "Region: Undefined", -8977))
                    .withContents("Project Title", "", "Totals-Percentage of Total Commitments", "2,79", "Totals-Percentage Of Total Disbursements", "0,45", "Region", "Region: Undefined")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program", "Totals-Percentage of Total Commitments", "2,79"),
                      new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements", "Totals-Percentage Of Total Disbursements", "0,45")        )      ));

        
        ReportSpecificationImpl spec = buildSpecification("testFetchedMeasureTotalMissingPrecursorHier",
            Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.REGION),
            Arrays.asList(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS, MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS),
            Arrays.asList(ColumnConstants.REGION),
            GroupingCriteria.GROUPING_YEARLY);
        
        runNiTestCase(spec, "en", executionRateActs, cor);
    }

    @Test
    public void testMeasurelessReportNoHier() {
        NiReportModel cor = new NiReportModel("testMeasurelessReportNoHier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 1))",
                        "(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1"),
                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2"),
                                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed"),
                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project"),
                                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components"),
                                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water"),
                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1"),
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity"),
                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1"),
                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2"),
                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1"),
                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2"),
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages"),
                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2"),
                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending"),
                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency"),
                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting"),
                                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity"),
                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements"),
                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements"),
                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms"),
                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs"),
                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies")      ));

        ReportSpecificationImpl spec = buildSpecification("testMeasurelessReportNoHier",
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                null,
                null,
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testMeasurelessReportWithHier() {
        NiReportModel cor = new NiReportModel("testMeasurelessReportWithHier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2))",
                        "(Country: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Country", "", "Project Title", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Country", "Moldova", 8977))
                                        .withContents("Project Title", "", "Country", "Moldova")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1"),
                                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2"),
                                                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                                                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
                                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed"),
                                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project"),
                                                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components"),
                                                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
                                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water"),
                                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1"),
                                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity"),
                                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1"),
                                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2"),
                                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1"),
                                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2"),
                                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages"),
                                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge"),
                                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program"),
                                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program"),
                                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program"),
                                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2"),
                                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending"),
                                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency"),
                                                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components"),
                                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components"),
                                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity"),
                                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement"),
                                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements"),
                                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements"),
                                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement"),
                                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements"),
                                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms"),
                                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response"),
                                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb"),
                                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity"),
                                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs"),
                                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies")        ),
                                new ReportAreaForTests(new AreaOwner("Country", "Country: Undefined", -999999999)).withContents("Project Title", "", "Country", "Country: Undefined")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testMeasurelessReportWithHier",
                Arrays.asList(ColumnConstants.COUNTRY, ColumnConstants.PROJECT_TITLE),
                null,
                Arrays.asList(ColumnConstants.COUNTRY),
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testMeasurelessReportWithFilteringByRegion() {
        NiReportModel cor = new NiReportModel("testMeasurelessReportWithFilteringByRegion")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2))",
                        "(Region: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Region", "", "Project Title", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(17), "Region", "Anenii Noi County", "Project Title", "Proposed Project Cost 2 - EUR"),
                                new ReportAreaForTests(new AreaOwner(18), "Region", "Anenii Noi County", "Project Title", "Test MTEF directed"),
                                new ReportAreaForTests(new AreaOwner(21), "Region", "Anenii Noi County", "Project Title", "activity with components"),
                                new ReportAreaForTests(new AreaOwner(24), "Region", "Anenii Noi County", "Project Title", "Eth Water"),
                                new ReportAreaForTests(new AreaOwner(27), "Region", "Anenii Noi County", "Project Title", "mtef activity 2"),
                                new ReportAreaForTests(new AreaOwner(28), "Region", "Anenii Noi County", "Project Title", "ptc activity 1"),
                                new ReportAreaForTests(new AreaOwner(29), "Region", "Anenii Noi County", "Project Title", "ptc activity 2"),
                                new ReportAreaForTests(new AreaOwner(30), "Region", "Anenii Noi County", "Project Title", "SSC Project 1"),
                                new ReportAreaForTests(new AreaOwner(33), "Region", "Anenii Noi County", "Project Title", "Activity with Zones"),
                                new ReportAreaForTests(new AreaOwner(36), "Region", "Anenii Noi County", "Project Title", "Activity With Zones and Percentages"),
                                new ReportAreaForTests(new AreaOwner(40), "Region", "Anenii Noi County", "Project Title", "SubNational no percentages")      ));

        ReportSpecificationImpl spec = buildSpecification("testMeasurelessReportWithFilteringByRegion",
                Arrays.asList(ColumnConstants.REGION, ColumnConstants.PROJECT_TITLE),
                null,
                null,
                GroupingCriteria.GROUPING_YEARLY);
        spec.setFilters(buildSimpleFilter(ColumnConstants.REGION, "9085"/*"Anenii Noi County"*/, true));

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testMeasurelessReportWithFilteringByFunding() {
        NiReportModel cor = new NiReportModel("testMeasurelessReportNoHier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 1))",
                        "(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Project Title", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1"),
                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2"),
                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed"),
                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project"),
                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water"),
                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1"),
                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity"),
                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1"),
                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2"),
                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1"),
                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2"),
                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages"),
                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge"),
                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program"),
                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program"),
                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program"),
                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency"),
                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting"),
                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components"),
                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity"),
                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement"),
                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements"),
                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements"),
                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement"),
                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements"),
                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms"),
                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response"),
                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb"),
                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity"),
                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs"),
                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies")      ));

        ReportSpecificationImpl spec = buildSpecification("testMeasurelessReportNoHier",
                Arrays.asList(ColumnConstants.PROJECT_TITLE),
                null,
                null,
                GroupingCriteria.GROUPING_YEARLY);
        spec.setFilters(buildSimpleFilter(ColumnConstants.TYPE_OF_ASSISTANCE, "2119"/*"default type of assistance"*/, true));

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testMeasurelessReportWithFundingLevelFilteringAndHier(){
        NiReportModel cor = new NiReportModel("testMeasurelessReportWithFundingLevelFilteringAndHier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2))",
                        "(Type Of Assistance: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Type Of Assistance", "", "Project Title", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119))
                                        .withContents("Project Title", "", "Type Of Assistance", "default type of assistance")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1"),
                                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2"),
                                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed"),
                                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project"),
                                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water"),
                                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1"),
                                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity"),
                                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1"),
                                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2"),
                                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1"),
                                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2"),
                                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages"),
                                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge"),
                                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program"),
                                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program"),
                                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program"),
                                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency"),
                                                new ReportAreaForTests(new AreaOwner(53), "Project Title", "new activity with contracting"),
                                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components"),
                                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity"),
                                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement"),
                                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements"),
                                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements"),
                                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement"),
                                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements"),
                                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms"),
                                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response"),
                                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb"),
                                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity"),
                                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs"),
                                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testMeasurelessReportWithFundingLevelFilteringAndHier",
                Arrays.asList(ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.PROJECT_TITLE),
                null,
                Arrays.asList(ColumnConstants.TYPE_OF_ASSISTANCE),
                GroupingCriteria.GROUPING_TOTALS_ONLY);
        spec.setFilters(buildSimpleFilter(ColumnConstants.TYPE_OF_ASSISTANCE, "2119"/*"default type of assistance"*/, true));
        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testMeasurelessReportWithFilteringAndHier() {
        NiReportModel cor = new NiReportModel("testMeasurelessReportWithHier")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 2, colStart: 0, colSpan: 2))",
                        "(Country: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Country", "", "Project Title", "")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Country", "Moldova", 8977))
                                        .withContents("Project Title", "", "Country", "Moldova")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(12), "Project Title", "TAC_activity_1"),
                                                new ReportAreaForTests(new AreaOwner(13), "Project Title", "TAC_activity_2"),
                                                new ReportAreaForTests(new AreaOwner(15), "Project Title", "Proposed Project Cost 1 - USD"),
                                                new ReportAreaForTests(new AreaOwner(17), "Project Title", "Proposed Project Cost 2 - EUR"),
                                                new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed"),
                                                new ReportAreaForTests(new AreaOwner(19), "Project Title", "Pure MTEF Project"),
                                                new ReportAreaForTests(new AreaOwner(21), "Project Title", "activity with components"),
                                                new ReportAreaForTests(new AreaOwner(23), "Project Title", "Project with documents"),
                                                new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water"),
                                                new ReportAreaForTests(new AreaOwner(25), "Project Title", "mtef activity 1"),
                                                new ReportAreaForTests(new AreaOwner(26), "Project Title", "date-filters-activity"),
                                                new ReportAreaForTests(new AreaOwner(27), "Project Title", "mtef activity 2"),
                                                new ReportAreaForTests(new AreaOwner(28), "Project Title", "ptc activity 1"),
                                                new ReportAreaForTests(new AreaOwner(29), "Project Title", "ptc activity 2"),
                                                new ReportAreaForTests(new AreaOwner(30), "Project Title", "SSC Project 1"),
                                                new ReportAreaForTests(new AreaOwner(31), "Project Title", "SSC Project 2"),
                                                new ReportAreaForTests(new AreaOwner(32), "Project Title", "crazy funding 1"),
                                                new ReportAreaForTests(new AreaOwner(33), "Project Title", "Activity with Zones"),
                                                new ReportAreaForTests(new AreaOwner(36), "Project Title", "Activity With Zones and Percentages"),
                                                new ReportAreaForTests(new AreaOwner(40), "Project Title", "SubNational no percentages"),
                                                new ReportAreaForTests(new AreaOwner(41), "Project Title", "Activity Linked With Pledge"),
                                                new ReportAreaForTests(new AreaOwner(43), "Project Title", "Activity with primary_tertiary_program"),
                                                new ReportAreaForTests(new AreaOwner(44), "Project Title", "activity with primary_program"),
                                                new ReportAreaForTests(new AreaOwner(45), "Project Title", "activity with tertiary_program"),
                                                new ReportAreaForTests(new AreaOwner(46), "Project Title", "pledged education activity 1"),
                                                new ReportAreaForTests(new AreaOwner(48), "Project Title", "pledged 2"),
                                                new ReportAreaForTests(new AreaOwner(50), "Project Title", "activity with capital spending"),
                                                new ReportAreaForTests(new AreaOwner(52), "Project Title", "activity with contracting agency"),
                                                new ReportAreaForTests(new AreaOwner(61), "Project Title", "activity-with-unfunded-components"),
                                                new ReportAreaForTests(new AreaOwner(63), "Project Title", "activity with funded components"),
                                                new ReportAreaForTests(new AreaOwner(64), "Project Title", "Unvalidated activity"),
                                                new ReportAreaForTests(new AreaOwner(65), "Project Title", "activity 1 with agreement"),
                                                new ReportAreaForTests(new AreaOwner(66), "Project Title", "Activity 2 with multiple agreements"),
                                                new ReportAreaForTests(new AreaOwner(67), "Project Title", "third activity with agreements"),
                                                new ReportAreaForTests(new AreaOwner(68), "Project Title", "activity with incomplete agreement"),
                                                new ReportAreaForTests(new AreaOwner(69), "Project Title", "Activity with planned disbursements"),
                                                new ReportAreaForTests(new AreaOwner(70), "Project Title", "Activity with both MTEFs and Act.Comms"),
                                                new ReportAreaForTests(new AreaOwner(71), "Project Title", "activity_with_disaster_response"),
                                                new ReportAreaForTests(new AreaOwner(73), "Project Title", "activity with directed MTEFs"),
                                                new ReportAreaForTests(new AreaOwner(76), "Project Title", "activity with pipeline MTEFs and act. disb"),
                                                new ReportAreaForTests(new AreaOwner(77), "Project Title", "execution rate activity"),
                                                new ReportAreaForTests(new AreaOwner(78), "Project Title", "activity with many MTEFs"),
                                                new ReportAreaForTests(new AreaOwner(79), "Project Title", "with weird currencies")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testMeasurelessReportWithHier",
                Arrays.asList(ColumnConstants.COUNTRY, ColumnConstants.PROJECT_TITLE),
                null,
                Arrays.asList(ColumnConstants.COUNTRY),
                GroupingCriteria.GROUPING_YEARLY);
        spec.setFilters(buildSimpleFilter(ColumnConstants.COUNTRY, "8977"/*"Moldova"*/, true));

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testYearlyReportNotAffectedByTimeRangeSubTotals() {
        NiReportModel cor = new NiReportModel("testYearlyReportDoesNoHaveSubTotals")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 9))",
                        "(Financing Instrument: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 7));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 8, colSpan: 1))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Financing Instrument", "", "Funding-2006-Actual Commitments", "96,840,58", "Funding-2009-Actual Commitments", "100,000", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2012-Actual Commitments", "25,000", "Funding-2013-Actual Commitments", "7,842,086", "Funding-2014-Actual Commitments", "8,159,813,77", "Funding-2015-Actual Commitments", "1,971,831,84", "Totals-Actual Commitments", "19,408,691,19")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Financing Instrument", "default financing instrument", 2120), "Funding-2006-Actual Commitments", "0", "Funding-2009-Actual Commitments", "100,000", "Funding-2011-Actual Commitments", "1,213,119", "Funding-2012-Actual Commitments", "25,000", "Funding-2013-Actual Commitments", "4,949,864", "Funding-2014-Actual Commitments", "3,534,000", "Funding-2015-Actual Commitments", "580,745", "Totals-Actual Commitments", "10,402,728", "Financing Instrument", "default financing instrument"),
                                new ReportAreaForTests(new AreaOwner("Financing Instrument", "second financing instrument", 2125), "Funding-2006-Actual Commitments", "96,840,58", "Funding-2009-Actual Commitments", "0", "Funding-2011-Actual Commitments", "0", "Funding-2012-Actual Commitments", "0", "Funding-2013-Actual Commitments", "2,892,222", "Funding-2014-Actual Commitments", "4,625,813,77", "Funding-2015-Actual Commitments", "1,391,086,84", "Totals-Actual Commitments", "9,005,963,19", "Financing Instrument", "second financing instrument")      ));

        ReportSpecificationImpl spec = buildSpecification("testYearlyReportDoesNoHaveSubTotals",
                Arrays.asList(ColumnConstants.FINANCING_INSTRUMENT),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.FINANCING_INSTRUMENT),
                GroupingCriteria.GROUPING_YEARLY);
        spec.setSummaryReport(true);
        spec.setDisplayTimeRangeSubtotals(true);

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testTotalsOnlyReportNotAffectedByTimeRangeSubTotals() {
        NiReportModel cor = new NiReportModel("testTotalsOnlyReportNotAffectedByTimeRangeSubTotals")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 0, colSpan: 2))",
                        "(Financing Instrument: (startRow: 1, rowSpan: 2, totalRowSpan: 2, colStart: 0, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1))",
                        "(Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Financing Instrument", "", "Totals-Actual Commitments", "19,408,691,19")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Financing Instrument", "default financing instrument", 2120), "Totals-Actual Commitments", "10,402,728", "Financing Instrument", "default financing instrument"),
                                new ReportAreaForTests(new AreaOwner("Financing Instrument", "second financing instrument", 2125), "Totals-Actual Commitments", "9,005,963,19", "Financing Instrument", "second financing instrument")      ));

        ReportSpecificationImpl spec = buildSpecification("testTotalsOnlyReportNotAffectedByTimeRangeSubTotals",
                Arrays.asList(ColumnConstants.FINANCING_INSTRUMENT),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.FINANCING_INSTRUMENT),
                GroupingCriteria.GROUPING_TOTALS_ONLY);
        spec.setSummaryReport(true);
        spec.setDisplayTimeRangeSubtotals(true);

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testQuarterlyReportWithTimeRangeSubTotals() {
        NiReportModel cor = new NiReportModel("testQuarterlyReportWithTimeRangeSubTotals")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 24))",
                        "(Financing Instrument: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 22));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 23, colSpan: 1))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 2));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 3));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 8, colSpan: 2));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 10, colSpan: 3));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 13, colSpan: 5));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 18, colSpan: 5))",
                        "(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 1));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 1));(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 1));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 1));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 1));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 1));(Q1: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 1));(Q2: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 19, colSpan: 1));(Q3: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 20, colSpan: 1));(Q4: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 21, colSpan: 1));(Total: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 22, colSpan: 1))",
                        "(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 23, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Financing Instrument", "", "Funding-2006-Q2-Actual Commitments", "96,840,58", "Funding-2006-Total-Actual Commitments", "96,840,58", "Funding-2009-Q1-Actual Commitments", "100,000", "Funding-2009-Total-Actual Commitments", "100,000", "Funding-2011-Q3-Actual Commitments", "213,231", "Funding-2011-Q4-Actual Commitments", "999,888", "Funding-2011-Total-Actual Commitments", "1,213,119", "Funding-2012-Q3-Actual Commitments", "25,000", "Funding-2012-Total-Actual Commitments", "25,000", "Funding-2013-Q3-Actual Commitments", "1,678,753", "Funding-2013-Q4-Actual Commitments", "6,163,333", "Funding-2013-Total-Actual Commitments", "7,842,086", "Funding-2014-Q1-Actual Commitments", "222,000", "Funding-2014-Q2-Actual Commitments", "7,700,000", "Funding-2014-Q3-Actual Commitments", "33,000", "Funding-2014-Q4-Actual Commitments", "204,813,77", "Funding-2014-Total-Actual Commitments", "8,159,813,77", "Funding-2015-Q1-Actual Commitments", "749,445", "Funding-2015-Q2-Actual Commitments", "450,000", "Funding-2015-Q3-Actual Commitments", "678,456", "Funding-2015-Q4-Actual Commitments", "93,930,84", "Funding-2015-Total-Actual Commitments", "1,971,831,84", "Totals-Actual Commitments", "19,408,691,19")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Financing Instrument", "default financing instrument", 2120), "Funding-2006-Q2-Actual Commitments", "0", "Funding-2006-Total-Actual Commitments", "0", "Funding-2009-Q1-Actual Commitments", "100,000", "Funding-2009-Total-Actual Commitments", "100,000", "Funding-2011-Q3-Actual Commitments", "213,231", "Funding-2011-Q4-Actual Commitments", "999,888", "Funding-2011-Total-Actual Commitments", "1,213,119", "Funding-2012-Q3-Actual Commitments", "25,000", "Funding-2012-Total-Actual Commitments", "25,000", "Funding-2013-Q3-Actual Commitments", "1,678,753", "Funding-2013-Q4-Actual Commitments", "3,271,111", "Funding-2013-Total-Actual Commitments", "4,949,864", "Funding-2014-Q1-Actual Commitments", "222,000", "Funding-2014-Q2-Actual Commitments", "3,300,000", "Funding-2014-Q3-Actual Commitments", "0", "Funding-2014-Q4-Actual Commitments", "12,000", "Funding-2014-Total-Actual Commitments", "3,534,000", "Funding-2015-Q1-Actual Commitments", "580,745", "Funding-2015-Q2-Actual Commitments", "0", "Funding-2015-Q3-Actual Commitments", "0", "Funding-2015-Q4-Actual Commitments", "0", "Funding-2015-Total-Actual Commitments", "580,745", "Totals-Actual Commitments", "10,402,728", "Financing Instrument", "default financing instrument"),
                                new ReportAreaForTests(new AreaOwner("Financing Instrument", "second financing instrument", 2125), "Funding-2006-Q2-Actual Commitments", "96,840,58", "Funding-2006-Total-Actual Commitments", "96,840,58", "Funding-2009-Q1-Actual Commitments", "0", "Funding-2009-Total-Actual Commitments", "0", "Funding-2011-Q3-Actual Commitments", "0", "Funding-2011-Q4-Actual Commitments", "0", "Funding-2011-Total-Actual Commitments", "0", "Funding-2012-Q3-Actual Commitments", "0", "Funding-2012-Total-Actual Commitments", "0", "Funding-2013-Q3-Actual Commitments", "0", "Funding-2013-Q4-Actual Commitments", "2,892,222", "Funding-2013-Total-Actual Commitments", "2,892,222", "Funding-2014-Q1-Actual Commitments", "0", "Funding-2014-Q2-Actual Commitments", "4,400,000", "Funding-2014-Q3-Actual Commitments", "33,000", "Funding-2014-Q4-Actual Commitments", "192,813,77", "Funding-2014-Total-Actual Commitments", "4,625,813,77", "Funding-2015-Q1-Actual Commitments", "168,700", "Funding-2015-Q2-Actual Commitments", "450,000", "Funding-2015-Q3-Actual Commitments", "678,456", "Funding-2015-Q4-Actual Commitments", "93,930,84", "Funding-2015-Total-Actual Commitments", "1,391,086,84", "Totals-Actual Commitments", "9,005,963,19", "Financing Instrument", "second financing instrument")      ));

        ReportSpecificationImpl spec = buildSpecification("testQuarterlyReportWithTimeRangeSubTotals",
                Arrays.asList(ColumnConstants.FINANCING_INSTRUMENT),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.FINANCING_INSTRUMENT),
                GroupingCriteria.GROUPING_QUARTERLY);
        spec.setSummaryReport(true);
        spec.setDisplayTimeRangeSubtotals(true);

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testMonthlyReportNotAffectedByTimeRangeSubTotals() {
        NiReportModel cor = new NiReportModel("testMonthlyReportNotAffectedByTimeRangeSubTotals")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 23))",
                        "(Financing Instrument: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 1, colSpan: 21));(Totals: (startRow: 1, rowSpan: 3, totalRowSpan: 4, colStart: 22, colSpan: 1))",
                        "(2006: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 1));(2009: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1));(2011: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 3, colSpan: 2));(2012: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 1));(2013: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 6, colSpan: 3));(2014: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 9, colSpan: 6));(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 15, colSpan: 7))",
                        "(April: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1));(February: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1));(August: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 3, colSpan: 1));(November: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1));(September: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1));(August: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1));(November: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 1));(December: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 8, colSpan: 1));(February: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 9, colSpan: 1));(March: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 1));(April: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1));(July: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 12, colSpan: 1));(November: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 1));(December: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 14, colSpan: 1));(January: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 15, colSpan: 1));(March: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 16, colSpan: 1));(April: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 17, colSpan: 1));(June: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 18, colSpan: 1));(August: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 19, colSpan: 1));(September: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 20, colSpan: 1));(October: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 21, colSpan: 1))",
                        "(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 12, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 15, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 16, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 17, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 18, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 19, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 20, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 21, colSpan: 1));(Actual Commitments: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 22, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Financing Instrument", "", "Funding-2006-April-Actual Commitments", "96,840,58", "Funding-2009-February-Actual Commitments", "100,000", "Funding-2011-August-Actual Commitments", "213,231", "Funding-2011-November-Actual Commitments", "999,888", "Funding-2012-September-Actual Commitments", "25,000", "Funding-2013-August-Actual Commitments", "1,678,753", "Funding-2013-November-Actual Commitments", "2,670,000", "Funding-2013-December-Actual Commitments", "3,493,333", "Funding-2014-February-Actual Commitments", "75,000", "Funding-2014-March-Actual Commitments", "147,000", "Funding-2014-April-Actual Commitments", "7,700,000", "Funding-2014-July-Actual Commitments", "33,000", "Funding-2014-November-Actual Commitments", "77,760,63", "Funding-2014-December-Actual Commitments", "127,053,14", "Funding-2015-January-Actual Commitments", "45,000", "Funding-2015-March-Actual Commitments", "704,445", "Funding-2015-April-Actual Commitments", "383,000", "Funding-2015-June-Actual Commitments", "67,000", "Funding-2015-August-Actual Commitments", "555,000", "Funding-2015-September-Actual Commitments", "123,456", "Funding-2015-October-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "19,408,691,19")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Financing Instrument", "default financing instrument", 2120), "Funding-2006-April-Actual Commitments", "0", "Funding-2009-February-Actual Commitments", "100,000", "Funding-2011-August-Actual Commitments", "213,231", "Funding-2011-November-Actual Commitments", "999,888", "Funding-2012-September-Actual Commitments", "25,000", "Funding-2013-August-Actual Commitments", "1,678,753", "Funding-2013-November-Actual Commitments", "0", "Funding-2013-December-Actual Commitments", "3,271,111", "Funding-2014-February-Actual Commitments", "75,000", "Funding-2014-March-Actual Commitments", "147,000", "Funding-2014-April-Actual Commitments", "3,300,000", "Funding-2014-July-Actual Commitments", "0", "Funding-2014-November-Actual Commitments", "12,000", "Funding-2014-December-Actual Commitments", "0", "Funding-2015-January-Actual Commitments", "0", "Funding-2015-March-Actual Commitments", "580,745", "Funding-2015-April-Actual Commitments", "0", "Funding-2015-June-Actual Commitments", "0", "Funding-2015-August-Actual Commitments", "0", "Funding-2015-September-Actual Commitments", "0", "Funding-2015-October-Actual Commitments", "0", "Totals-Actual Commitments", "10,402,728", "Financing Instrument", "default financing instrument"),
                                new ReportAreaForTests(new AreaOwner("Financing Instrument", "second financing instrument", 2125), "Funding-2006-April-Actual Commitments", "96,840,58", "Funding-2009-February-Actual Commitments", "0", "Funding-2011-August-Actual Commitments", "0", "Funding-2011-November-Actual Commitments", "0", "Funding-2012-September-Actual Commitments", "0", "Funding-2013-August-Actual Commitments", "0", "Funding-2013-November-Actual Commitments", "2,670,000", "Funding-2013-December-Actual Commitments", "222,222", "Funding-2014-February-Actual Commitments", "0", "Funding-2014-March-Actual Commitments", "0", "Funding-2014-April-Actual Commitments", "4,400,000", "Funding-2014-July-Actual Commitments", "33,000", "Funding-2014-November-Actual Commitments", "65,760,63", "Funding-2014-December-Actual Commitments", "127,053,14", "Funding-2015-January-Actual Commitments", "45,000", "Funding-2015-March-Actual Commitments", "123,700", "Funding-2015-April-Actual Commitments", "383,000", "Funding-2015-June-Actual Commitments", "67,000", "Funding-2015-August-Actual Commitments", "555,000", "Funding-2015-September-Actual Commitments", "123,456", "Funding-2015-October-Actual Commitments", "93,930,84", "Totals-Actual Commitments", "9,005,963,19", "Financing Instrument", "second financing instrument")      ));

        ReportSpecificationImpl spec = buildSpecification("testMonthlyReportNotAffectedByTimeRangeSubTotals",
                Arrays.asList(ColumnConstants.FINANCING_INSTRUMENT),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.FINANCING_INSTRUMENT),
                GroupingCriteria.GROUPING_MONTHLY);
        spec.setSummaryReport(true);
        spec.setDisplayTimeRangeSubtotals(true);

        runNiTestCase(spec, "en", acts, cor);
    }

    @Test
    public void testSubDimensionColumnFiltering() {
        NiReportModel cor = new NiReportModel("testMonthlyReportNotAffectedByTimeRangeSubTotals")
                .withHeaders(Arrays.asList(
                        "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 4))",
                        "(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Financing Instrument: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
                        "(2015: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                        "(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                        .withContents("Donor Agency", "", "Financing Instrument", "", "Funding-2015-Actual Commitments", "1,200", "Totals-Actual Commitments", "1,200")
                        .withChildren(
                                new ReportAreaForTests(new AreaOwner("Donor Agency", "UNDP", 21695)).withContents("Financing Instrument", "", "Funding-2015-Actual Commitments", "700", "Totals-Actual Commitments", "700", "Donor Agency", "UNDP")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(66), "Financing Instrument", "second financing instrument", "Funding-2015-Actual Commitments", "700", "Totals-Actual Commitments", "700")        ),
                                new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Financing Instrument", "", "Funding-2015-Actual Commitments", "500", "Totals-Actual Commitments", "500", "Donor Agency", "USAID")
                                        .withChildren(
                                                new ReportAreaForTests(new AreaOwner(66), "Financing Instrument", "default financing instrument", "Funding-2015-Actual Commitments", "500", "Totals-Actual Commitments", "500")        )      ));

        ReportSpecificationImpl spec = buildSpecification("testMonthlyReportNotAffectedByTimeRangeSubTotals",
                Arrays.asList(ColumnConstants.DONOR_AGENCY, ColumnConstants.FINANCING_INSTRUMENT),
                Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS),
                Arrays.asList(ColumnConstants.DONOR_AGENCY),
                GroupingCriteria.GROUPING_YEARLY);

        runNiTestCase(spec, "en", Arrays.asList("Activity 2 with multiple agreements"), cor);
    }
}
