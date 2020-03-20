package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaForTests;
import org.dgfoundation.amp.newreports.AmpReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.testcases.NiReportModel;
import org.junit.Test;

/**
 * 
 * testcases for expenditure class
 * 
 * @author Constantin Dolghier
 *
 */
public class ExpenditureClassTests extends AmpReportingTestCase {

    final List<String> expClassActs = Arrays.asList("expenditure class", "Eth Water", "Test MTEF directed");
    
    final List<String> acts = Arrays.asList(
            "Activity 2 with multiple agreements",
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
            "pledged education activity 1",
            "Project with documents",
            "Proposed Project Cost 1 - USD",
            "ptc activity 1",
            "ptc activity 2",
            "SubNational no percentages",
            "TAC_activity_2",
            "Test MTEF directed",
            "third activity with agreements",
            "Unvalidated activity",
            "with weird currencies",
            "expenditure class"
        );
    
    final static List<String> hierarchiesToTry = Arrays.asList(
            ColumnConstants.STATUS, ColumnConstants.IMPLEMENTATION_LEVEL, 
            ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, 
            ColumnConstants.SECONDARY_SECTOR, ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR,
            ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, ColumnConstants.PRIMARY_PROGRAM_LEVEL_2,
            ColumnConstants.LOCATION_ADM_LEVEL_0, ColumnConstants.LOCATION_ADM_LEVEL_1, ColumnConstants.LOCATION_ADM_LEVEL_2, ColumnConstants.LOCATION_ADM_LEVEL_3,
            ColumnConstants.IMPLEMENTING_AGENCY, ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, ColumnConstants.IMPLEMENTING_AGENCY_TYPE,
            ColumnConstants.DONOR_AGENCY, ColumnConstants.DONOR_GROUP, ColumnConstants.DONOR_TYPE,
            ColumnConstants.FINANCING_INSTRUMENT, ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.MODE_OF_PAYMENT, ColumnConstants.FUNDING_STATUS,
            ColumnConstants.EXPENDITURE_CLASS);

    final static String correctTotals = "{RAW / Funding / 2006 / Actual Expenditures=0, RAW / Funding / 2006 / Actual Commitments=96840.576201, RAW / Funding / 2006 / Actual Disbursements=0, RAW / Funding / 2006 / Planned Expenditures=0, RAW / Funding / 2009 / Actual Expenditures=0, RAW / Funding / 2009 / Actual Commitments=100000, RAW / Funding / 2009 / Actual Disbursements=0, RAW / Funding / 2009 / Planned Expenditures=0, RAW / Funding / 2010 / Actual Expenditures=0, RAW / Funding / 2010 / Actual Commitments=0, RAW / Funding / 2010 / Actual Disbursements=656990, RAW / Funding / 2010 / Planned Expenditures=0, RAW / Funding / 2011 / Actual Expenditures=0, RAW / Funding / 2011 / Actual Commitments=999888, RAW / Funding / 2011 / Actual Disbursements=0, RAW / Funding / 2011 / Planned Expenditures=0, RAW / Funding / 2012 / Actual Expenditures=0, RAW / Funding / 2012 / Actual Commitments=25000, RAW / Funding / 2012 / Actual Disbursements=12000, RAW / Funding / 2012 / Planned Expenditures=0, RAW / Funding / 2013 / Actual Expenditures=0, RAW / Funding / 2013 / Actual Commitments=4493332, RAW / Funding / 2013 / Actual Disbursements=580000, RAW / Funding / 2013 / Planned Expenditures=0, RAW / Funding / 2014 / Actual Expenditures=0, RAW / Funding / 2014 / Actual Commitments=3697813.768451, RAW / Funding / 2014 / Actual Disbursements=260200, RAW / Funding / 2014 / Planned Expenditures=0, RAW / Funding / 2015 / Actual Expenditures=0, RAW / Funding / 2015 / Actual Commitments=1515042.841736, RAW / Funding / 2015 / Actual Disbursements=115570, RAW / Funding / 2015 / Planned Expenditures=0, RAW / Funding / 2016 / Actual Expenditures=72000, RAW / Funding / 2016 / Actual Commitments=62000, RAW / Funding / 2016 / Actual Disbursements=253700, RAW / Funding / 2016 / Planned Expenditures=230000, RAW / Funding / 2016 / Actual Classified Expenditures / Capital Expenditure=22000, RAW / Funding / 2016 / Actual Classified Expenditures / Compensation / Salaries=24000, RAW / Funding / 2016 / Actual Classified Expenditures / Unassigned=26000, RAW / Funding / 2016 / Planned Classified Expenditures / Capital Expenditure=42000, RAW / Funding / 2016 / Planned Classified Expenditures / Compensation / Salaries=44000, RAW / Funding / 2016 / Planned Classified Expenditures / Goods and Services=46000, RAW / Funding / 2016 / Planned Classified Expenditures / Others=48000, RAW / Funding / 2016 / Planned Classified Expenditures / Unassigned=50000, RAW / Totals / Actual Expenditures=72000, RAW / Totals / Actual Commitments=10989917.186388, RAW / Totals / Actual Disbursements=1878460, RAW / Totals / Planned Expenditures=230000, RAW / Totals / Total Actual Classified Expenditures / Capital Expenditure=22000, RAW / Totals / Total Actual Classified Expenditures / Compensation / Salaries=24000, RAW / Totals / Total Actual Classified Expenditures / Unassigned=26000, RAW / Totals / Total Planned Classified Expenditures / Capital Expenditure=42000, RAW / Totals / Total Planned Classified Expenditures / Compensation / Salaries=44000, RAW / Totals / Total Planned Classified Expenditures / Goods and Services=46000, RAW / Totals / Total Planned Classified Expenditures / Others=48000, RAW / Totals / Total Planned Classified Expenditures / Unassigned=50000}";

    @Test
    public void testByDonorOrgByPrimarySector() {
        NiReportModel cor = new NiReportModel("testcase expenditure class with everything")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 17))",
                    "(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Activity Id: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 13))",
                    "(Total Actual Classified Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 3));(Total Planned Classified Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 7, colSpan: 5));(Actual Commitments: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 12, colSpan: 1));(Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 13, colSpan: 2));(Planned Expenditures: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 15, colSpan: 1));(Actual Expenditures: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 16, colSpan: 1))",
                    "(Capital Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Compensation / Salaries: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Capital Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Compensation / Salaries: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Goods and Services: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(Others: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1));(DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 13, colSpan: 1));(DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 14, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Donor Agency", "", "Primary Sector", "", "Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Actual Commitments", "62,000", "Totals-Real Disbursements-DN-EXEC", "545,000", "Totals-Real Disbursements-DN-IMPL", "77,222", "Totals-Planned Expenditures", "230,000", "Totals-Actual Expenditures", "72,000")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Primary Sector", "", "Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Actual Commitments", "0", "Totals-Real Disbursements-DN-EXEC", "20,000", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Planned Expenditures", "0", "Totals-Actual Expenditures", "0", "Donor Agency", "Finland")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Actual Commitments", "0", "Totals-Real Disbursements-DN-EXEC", "20,000", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Planned Expenditures", "0", "Totals-Actual Expenditures", "0", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Activity Id", "24", "Project Title", "Eth Water", "Totals-Real Disbursements-DN-EXEC", "20,000")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Primary Sector", "", "Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Actual Commitments", "62,000", "Totals-Real Disbursements-DN-EXEC", "0", "Totals-Real Disbursements-DN-IMPL", "77,222", "Totals-Planned Expenditures", "230,000", "Totals-Actual Expenditures", "72,000", "Donor Agency", "Ministry of Economy")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
                      .withContents("Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Actual Commitments", "62,000", "Totals-Real Disbursements-DN-EXEC", "0", "Totals-Real Disbursements-DN-IMPL", "77,222", "Totals-Planned Expenditures", "230,000", "Totals-Actual Expenditures", "72,000", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(18), "Activity Id", "18", "Project Title", "Test MTEF directed", "Totals-Real Disbursements-DN-IMPL", "77,222"),
                        new ReportAreaForTests(new AreaOwner(87), "Activity Id", "87", "Project Title", "expenditure class", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Actual Commitments", "62,000", "Totals-Planned Expenditures", "230,000", "Totals-Actual Expenditures", "72,000")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694)).withContents("Primary Sector", "", "Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Actual Commitments", "0", "Totals-Real Disbursements-DN-EXEC", "110,000", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Planned Expenditures", "0", "Totals-Actual Expenditures", "0", "Donor Agency", "Norway")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Actual Commitments", "0", "Totals-Real Disbursements-DN-EXEC", "110,000", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Planned Expenditures", "0", "Totals-Actual Expenditures", "0", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Activity Id", "24", "Project Title", "Eth Water", "Totals-Real Disbursements-DN-EXEC", "110,000")          )        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Primary Sector", "", "Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Actual Commitments", "0", "Totals-Real Disbursements-DN-EXEC", "415,000", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Planned Expenditures", "0", "Totals-Actual Expenditures", "0", "Donor Agency", "USAID")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Actual Commitments", "0", "Totals-Real Disbursements-DN-EXEC", "415,000", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Planned Expenditures", "0", "Totals-Actual Expenditures", "0", "Primary Sector", "110 - EDUCATION")
                      .withChildren(
                        new ReportAreaForTests(new AreaOwner(24), "Activity Id", "24", "Project Title", "Eth Water", "Totals-Real Disbursements-DN-EXEC", "415,000")          )        )      ));
        
        runNiTestCase(
            buildSpecification("testcase expenditure class with everything", 
                Arrays.asList(ColumnConstants.ACTIVITY_ID, ColumnConstants.PROJECT_TITLE, ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR), 
                Arrays.asList(MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES, MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.REAL_COMMITMENTS, MeasureConstants.REAL_DISBURSEMENTS, MeasureConstants.PLANNED_EXPENDITURES, MeasureConstants.ACTUAL_EXPENDITURES), 
                Arrays.asList(ColumnConstants.DONOR_AGENCY, ColumnConstants.PRIMARY_SECTOR), 
                GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en", expClassActs, cor);
    }
    
    @Test
    public void testByDonorOrgAndRealMeasures() {
        NiReportModel cor = new NiReportModel("testcase expenditure class with donor org and real measures")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 12))",
                    "(Donor Agency: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 10))",
                    "(Total Actual Classified Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 3));(Total Planned Classified Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 5));(Real Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 10, colSpan: 2))",
                    "(Capital Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Compensation / Salaries: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Capital Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Compensation / Salaries: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Goods and Services: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Others: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1));(DN-EXEC: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 10, colSpan: 1));(DN-IMPL: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null)
                  .withContents("Donor Agency", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Real Disbursements-DN-EXEC", "545,000", "Totals-Real Disbursements-DN-IMPL", "77,222")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Real Disbursements-DN-EXEC", "20,000", "Totals-Real Disbursements-DN-IMPL", "0", "Donor Agency", "Finland")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Totals-Real Disbursements-DN-EXEC", "20,000")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700))
                    .withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Real Disbursements-DN-EXEC", "0", "Totals-Real Disbursements-DN-IMPL", "77,222", "Donor Agency", "Ministry of Economy")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Totals-Real Disbursements-DN-IMPL", "77,222"),
                      new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "Norway", 21694)).withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Real Disbursements-DN-EXEC", "110,000", "Totals-Real Disbursements-DN-IMPL", "0", "Donor Agency", "Norway")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Totals-Real Disbursements-DN-EXEC", "110,000")        ),
                    new ReportAreaForTests(new AreaOwner("Donor Agency", "USAID", 21696)).withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Real Disbursements-DN-EXEC", "415,000", "Totals-Real Disbursements-DN-IMPL", "0", "Donor Agency", "USAID")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Totals-Real Disbursements-DN-EXEC", "415,000")        )      ));
        
        runNiTestCase(
            buildSpecification("testcase expenditure class with donor org and real measures", 
                Arrays.asList(ColumnConstants.DONOR_AGENCY, ColumnConstants.PROJECT_TITLE), 
                Arrays.asList(MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES,  MeasureConstants.REAL_DISBURSEMENTS, MeasureConstants.REAL_COMMITMENTS), 
                Arrays.asList(ColumnConstants.DONOR_AGENCY), 
                GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en", expClassActs, cor);
    }
    
    @Test
    public void testByPrimarySector() {
        NiReportModel cor = new NiReportModel("testcase expenditure class with primary sector")
            .withHeaders(Arrays.asList(
                    "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
                    "(Primary Sector: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 9))",
                    "(Total Actual Classified Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 3));(Total Planned Classified Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 5));(Planned Expenditures: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1))",
                    "(Capital Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Compensation / Salaries: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Capital Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Compensation / Salaries: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Goods and Services: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Others: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
                .withWarnings(Arrays.asList())
                .withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Planned Expenditures", "230,000")
                  .withChildren(
                    new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Planned Expenditures", "230,000", "Primary Sector", "110 - EDUCATION")
                    .withChildren(
                      new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Planned Expenditures", "230,000")        )      ));
        
        runNiTestCase(
            buildSpecification("testcase expenditure class with primary sector", 
                Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PROJECT_TITLE), 
                Arrays.asList(MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_EXPENDITURES), 
                Arrays.asList(ColumnConstants.PRIMARY_SECTOR), 
                GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en", expClassActs, cor);
    }
    
    @Test
    public void testByTypeOfAssistance() {
        NiReportModel cor = new NiReportModel("testcase expenditure class with assistance type")
        .withHeaders(Arrays.asList(
            "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 4, colStart: 0, colSpan: 11))",
            "(Type Of Assistance: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1));(Totals: (startRow: 1, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 9))",
            "(Total Actual Classified Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 3));(Total Planned Classified Expenditures: (startRow: 2, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 5));(Planned Expenditures: (startRow: 2, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1))",
            "(Capital Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1));(Compensation / Salaries: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1));(Capital Expenditure: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1));(Compensation / Salaries: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Goods and Services: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 7, colSpan: 1));(Others: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 8, colSpan: 1));(Unassigned: (startRow: 3, rowSpan: 1, totalRowSpan: 1, colStart: 9, colSpan: 1))"))
        .withWarnings(Arrays.asList())
        .withBody(      new ReportAreaForTests(null).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Planned Expenditures", "230,000")
          .withChildren(
            new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119)).withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Planned Expenditures", "230,000", "Type Of Assistance", "default type of assistance")
            .withChildren(
              new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "22,000", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "24,000", "Totals-Total Actual Classified Expenditures-Unassigned", "26,000", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "42,000", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "44,000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46,000", "Totals-Total Planned Classified Expenditures-Others", "48,000", "Totals-Total Planned Classified Expenditures-Unassigned", "50,000", "Totals-Planned Expenditures", "230,000")        )      ));
        
        runNiTestCase(
            buildSpecification("testcase expenditure class with assistance type", 
                Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TYPE_OF_ASSISTANCE), 
                Arrays.asList(MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_EXPENDITURES), 
                Arrays.asList(ColumnConstants.TYPE_OF_ASSISTANCE), 
                GroupingCriteria.GROUPING_TOTALS_ONLY),
                "en", expClassActs, cor);   
    }
    
    @Test
    public void testHierarchiesDoNotChangeTotals() throws Exception {
        List<String> measures = Arrays.asList(MeasureConstants.ACTUAL_EXPENDITURES, MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PLANNED_EXPENDITURES,
                MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES);
        
        ReportSpecificationImpl initSpec = buildSpecification("initSpec",
            Arrays.asList(ColumnConstants.PROJECT_TITLE),
            measures,
            null,
            GroupingCriteria.GROUPING_YEARLY);
                
        assertEquals(correctTotals, buildDigest(initSpec, acts, BasicSanityChecks.fundingGrandTotalsDigester).toString());
                
        // single-hierarchy reports
        for(boolean isSummary:Arrays.asList(true, false)) {
            for(String hierName:hierarchiesToTry) {
                ReportSpecificationImpl spec = buildSpecification(String.format("%s summary: %b", hierName, isSummary), 
                    Arrays.asList(ColumnConstants.PROJECT_TITLE, hierName), 
                    measures, 
                    Arrays.asList(hierName), 
                    GroupingCriteria.GROUPING_YEARLY);
                spec.setSummaryReport(isSummary);
                assertEquals(spec.getReportName(), correctTotals, buildDigest(spec, acts, BasicSanityChecks.fundingGrandTotalsDigester).toString());
            }
        }
    }
    /*
     * Commented out since Expenditure Class isn't a column anymore
    @Test
    public void testExpenditureClassFiltering() {
        NiReportModel cor = new NiReportModel("expenditure class-filtered-by-exp-class")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 12))",
                "(Expenditure Class: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 5));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 7, colSpan: 5))",
                "(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 5))",
                "(Actual Expenditures: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 4, colSpan: 1));(Planned Expenditures: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1));(Planned Classified Expenditures: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1));(Actual Expenditures: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1));(Planned Expenditures: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1));(Total Planned Classified Expenditures: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1))",
                "(Goods and Services: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Goods and Services: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null).withContents("Expenditure Class", "", "Project Title", "", "Funding-2016-Actual Expenditures", "0", "Funding-2016-Actual Commitments", "0", "Funding-2016-Actual Disbursements", "0", "Funding-2016-Planned Expenditures", "46 000", "Funding-2016-Planned Classified Expenditures-Goods and Services", "46 000", "Totals-Actual Expenditures", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-Planned Expenditures", "46 000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46 000")
              .withChildren(
                new ReportAreaForTests(new AreaOwner("Expenditure Class", "Goods and Services", 2142)).withContents("Project Title", "", "Funding-2016-Actual Expenditures", "0", "Funding-2016-Actual Commitments", "0", "Funding-2016-Actual Disbursements", "0", "Funding-2016-Planned Expenditures", "46 000", "Funding-2016-Planned Classified Expenditures-Goods and Services", "46 000", "Totals-Actual Expenditures", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-Planned Expenditures", "46 000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46 000", "Expenditure Class", "Goods and Services")
                .withChildren(
                  new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Funding-2016-Planned Expenditures", "46 000", "Funding-2016-Planned Classified Expenditures-Goods and Services", "46 000", "Totals-Planned Expenditures", "46 000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46 000")        )      ));

        
        runNiTestCase(spec("expenditure class-filtered-by-exp-class"), "en", acts, cor);
    }
    */
    
    /* Commented out since Expenditure Class isn't a column anymore
     * 
    @Test
    public void testExpenditureClassFilteringFlat() {
        NiReportModel cor = new NiReportModel("expenditure class-filtered-by-exp-class-flat")
        .withHeaders(Arrays.asList(
                "(RAW: (startRow: 0, rowSpan: 1, totalRowSpan: 5, colStart: 0, colSpan: 12))",
                "(Project Title: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 0, colSpan: 1));(Expenditure Class: (startRow: 1, rowSpan: 4, totalRowSpan: 4, colStart: 1, colSpan: 1));(Funding: (startRow: 1, rowSpan: 1, totalRowSpan: 4, colStart: 2, colSpan: 5));(Totals: (startRow: 1, rowSpan: 2, totalRowSpan: 4, colStart: 7, colSpan: 5))",
                "(2016: (startRow: 2, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 5))",
                "(Actual Expenditures: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 2, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 3, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 4, colSpan: 1));(Planned Expenditures: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 5, colSpan: 1));(Planned Classified Expenditures: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 6, colSpan: 1));(Actual Expenditures: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 7, colSpan: 1));(Actual Commitments: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 8, colSpan: 1));(Actual Disbursements: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 9, colSpan: 1));(Planned Expenditures: (startRow: 3, rowSpan: 2, totalRowSpan: 2, colStart: 10, colSpan: 1));(Total Planned Classified Expenditures: (startRow: 3, rowSpan: 1, totalRowSpan: 2, colStart: 11, colSpan: 1))",
                "(Goods and Services: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1));(Goods and Services: (startRow: 4, rowSpan: 1, totalRowSpan: 1, colStart: 11, colSpan: 1))"))
            .withWarnings(Arrays.asList())
            .withBody(      new ReportAreaForTests(null).withContents("Project Title", "", "Expenditure Class", "", "Funding-2016-Actual Expenditures", "0", "Funding-2016-Actual Commitments", "0", "Funding-2016-Actual Disbursements", "0", "Funding-2016-Planned Expenditures", "46 000", "Funding-2016-Planned Classified Expenditures-Goods and Services", "46 000", "Totals-Actual Expenditures", "0", "Totals-Actual Commitments", "0", "Totals-Actual Disbursements", "0", "Totals-Planned Expenditures", "46 000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46 000")
              .withChildren(
                new ReportAreaForTests(new AreaOwner(87), "Project Title", "expenditure class", "Expenditure Class", "Goods and Services", "Funding-2016-Planned Expenditures", "46 000", "Funding-2016-Planned Classified Expenditures-Goods and Services", "46 000", "Totals-Planned Expenditures", "46 000", "Totals-Total Planned Classified Expenditures-Goods and Services", "46 000")      ));
        
        runNiTestCase(spec("expenditure class-filtered-by-exp-class-flat"), "en", acts, cor);
    }
    
    */
}
