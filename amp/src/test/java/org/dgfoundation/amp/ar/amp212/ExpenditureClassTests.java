package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.mondrian.ReportAreaForTests;
import org.dgfoundation.amp.mondrian.ReportingTestCase;
import org.dgfoundation.amp.newreports.AreaOwner;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
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
public class ExpenditureClassTests extends ReportingTestCase {

	final List<String> expClassActs = Arrays.asList("expenditure class", "Eth Water", "Test MTEF directed");
	
	public ExpenditureClassTests() {
		super("ExpenditureClass tests");
	}
	
	@Override
	protected NiReportExecutor getNiExecutor(List<String> activityNames) {
		return getDbExecutor(activityNames);
	}
	
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
			      .withContents("Donor Agency", "", "Primary Sector", "", "Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Actual Commitments", "31", "Totals-Real Disbursements-DN-EXEC", "545,000", "Totals-Real Disbursements-DN-IMPL", "77,222", "Totals-Planned Expenditures", "115", "Totals-Actual Expenditures", "36")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Primary Sector", "", "Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Actual Commitments", "0", "Totals-Real Disbursements-DN-EXEC", "20,000", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Planned Expenditures", "0", "Totals-Actual Expenditures", "0", "Donor Agency", "Finland")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Actual Commitments", "0", "Totals-Real Disbursements-DN-EXEC", "20,000", "Totals-Real Disbursements-DN-IMPL", "0", "Totals-Planned Expenditures", "0", "Totals-Actual Expenditures", "0", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(24), "Activity Id", "24", "Project Title", "Eth Water", "Totals-Real Disbursements-DN-EXEC", "20,000")          )        ),
			        new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700)).withContents("Primary Sector", "", "Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Actual Commitments", "31", "Totals-Real Disbursements-DN-EXEC", "0", "Totals-Real Disbursements-DN-IMPL", "77,222", "Totals-Planned Expenditures", "115", "Totals-Actual Expenditures", "36", "Donor Agency", "Ministry of Economy")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236))
			          .withContents("Activity Id", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Actual Commitments", "31", "Totals-Real Disbursements-DN-EXEC", "0", "Totals-Real Disbursements-DN-IMPL", "77,222", "Totals-Planned Expenditures", "115", "Totals-Actual Expenditures", "36", "Primary Sector", "110 - EDUCATION")
			          .withChildren(
			            new ReportAreaForTests(new AreaOwner(18), "Activity Id", "18", "Project Title", "Test MTEF directed", "Totals-Real Disbursements-DN-IMPL", "77,222"),
			            new ReportAreaForTests(new AreaOwner(86), "Activity Id", "86", "Project Title", "expenditure class", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Actual Commitments", "31", "Totals-Planned Expenditures", "115", "Totals-Actual Expenditures", "36")          )        ),
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
			      .withContents("Donor Agency", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Real Disbursements-DN-EXEC", "545,000", "Totals-Real Disbursements-DN-IMPL", "77,222")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Donor Agency", "Finland", 21698)).withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "0", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Actual Classified Expenditures-Unassigned", "0", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "0", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "0", "Totals-Total Planned Classified Expenditures-Goods and Services", "0", "Totals-Total Planned Classified Expenditures-Others", "0", "Totals-Total Planned Classified Expenditures-Unassigned", "0", "Totals-Real Disbursements-DN-EXEC", "20,000", "Totals-Real Disbursements-DN-IMPL", "0", "Donor Agency", "Finland")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(24), "Project Title", "Eth Water", "Totals-Real Disbursements-DN-EXEC", "20,000")        ),
			        new ReportAreaForTests(new AreaOwner("Donor Agency", "Ministry of Economy", 21700))
			        .withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Real Disbursements-DN-EXEC", "0", "Totals-Real Disbursements-DN-IMPL", "77,222", "Donor Agency", "Ministry of Economy")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(18), "Project Title", "Test MTEF directed", "Totals-Real Disbursements-DN-IMPL", "77,222"),
			          new ReportAreaForTests(new AreaOwner(86), "Project Title", "expenditure class", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25")        ),
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
				.withBody(      new ReportAreaForTests(null).withContents("Primary Sector", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Planned Expenditures", "115")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Primary Sector", "110 - EDUCATION", 6236)).withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Planned Expenditures", "115", "Primary Sector", "110 - EDUCATION")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(86), "Project Title", "expenditure class", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Planned Expenditures", "115")        )      ));
		
		runNiTestCase(
			buildSpecification("testcase expenditure class with primary sector", 
				Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.PROJECT_TITLE), 
				Arrays.asList(MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_EXPENDITURES), 
				Arrays.asList(ColumnConstants.PRIMARY_SECTOR), 
				GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en", expClassActs,	cor);
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
				.withBody(      new ReportAreaForTests(null).withContents("Type Of Assistance", "", "Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Planned Expenditures", "115")
			      .withChildren(
			        new ReportAreaForTests(new AreaOwner("Type Of Assistance", "default type of assistance", 2119)).withContents("Project Title", "", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Planned Expenditures", "115", "Type Of Assistance", "default type of assistance")
			        .withChildren(
			          new ReportAreaForTests(new AreaOwner(86), "Project Title", "expenditure class", "Totals-Total Actual Classified Expenditures-Capital Expenditure", "11", "Totals-Total Actual Classified Expenditures-Compensation / Salaries", "12", "Totals-Total Actual Classified Expenditures-Unassigned", "13", "Totals-Total Planned Classified Expenditures-Capital Expenditure", "21", "Totals-Total Planned Classified Expenditures-Compensation / Salaries", "22", "Totals-Total Planned Classified Expenditures-Goods and Services", "23", "Totals-Total Planned Classified Expenditures-Others", "24", "Totals-Total Planned Classified Expenditures-Unassigned", "25", "Totals-Planned Expenditures", "115")        )      ));

		runNiTestCase(
			buildSpecification("testcase expenditure class with assistance type", 
				Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.TYPE_OF_ASSISTANCE), 
				Arrays.asList(MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES, MeasureConstants.PLANNED_EXPENDITURES), 
				Arrays.asList(ColumnConstants.TYPE_OF_ASSISTANCE), 
				GroupingCriteria.GROUPING_TOTALS_ONLY),
				"en", expClassActs, cor);	
	}
}
