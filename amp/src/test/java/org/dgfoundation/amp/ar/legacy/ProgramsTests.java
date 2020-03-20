package org.dgfoundation.amp.ar.legacy;


import org.dgfoundation.amp.nireports.testcases.ColumnReportDataModel;
import org.dgfoundation.amp.nireports.testcases.GroupColumnModel;
import org.dgfoundation.amp.nireports.testcases.GroupReportModel;
import org.dgfoundation.amp.nireports.testcases.SimpleColumnModel;
import org.dgfoundation.amp.testutils.ReportsTestCase;
import org.digijava.kernel.request.TLSUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

/**
 * Programs-reports tests
 * @author Dolghier Constantin
 *
 */
public class ProgramsTests extends ReportsTestCase {

    @Test
    public void testProgramsByPrimaryWithPercentages()
    {
        GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17190-all-programs-by-primary",
            GroupReportModel.withColumnReports("AMP-17190-all-programs-by-primary",
                ColumnReportDataModel.withColumns("Primary Program: Primary Program Unallocated",
                    SimpleColumnModel.withContents("Project Title", "Activity Linked With Pledge", "Activity Linked With Pledge", "activity with primary_program", "activity with primary_program", "activity with tertiary_program", "activity with tertiary_program"), 
                    SimpleColumnModel.withContents("National Planning Objectives Level 1", MUST_BE_EMPTY), 
                    SimpleColumnModel.withContents("Secondary Program Level 1", MUST_BE_EMPTY), 
                    SimpleColumnModel.withContents("Tertiary Program Level 1", "activity with tertiary_program", "OP1 name"), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2014",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000", "activity with tertiary_program", "15 000"))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        SimpleColumnModel.withContents("Actual Commitments", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000", "activity with tertiary_program", "15 000")))
                .withTrailCells(null, null, null, null, "97 000", "97 000"),
                ColumnReportDataModel.withColumns("Primary Program: Subprogram p1",
                    SimpleColumnModel.withContents("Project Title", "Activity with primary_tertiary_program", "Activity with primary_tertiary_program"), 
                    SimpleColumnModel.withContents("National Planning Objectives Level 1", MUST_BE_EMPTY), 
                    SimpleColumnModel.withContents("Secondary Program Level 1", MUST_BE_EMPTY), 
                    SimpleColumnModel.withContents("Tertiary Program Level 1", "Activity with primary_tertiary_program", "OP1 name"), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2014",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "25 000"))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "25 000")))
                .withTrailCells(null, null, null, null, "25 000", "25 000"),
                ColumnReportDataModel.withColumns("Primary Program: Subprogram p1.b",
                    SimpleColumnModel.withContents("Project Title", "Activity with primary_tertiary_program", "Activity with primary_tertiary_program"), 
                    SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY), 
                    SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY), 
                    SimpleColumnModel.withContents("Tertiary Program", "Activity with primary_tertiary_program", "OP1 name"), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2014",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "25 000"))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "25 000")))
                .withTrailCells(null, null, null, null, "25 000", "25 000"))
            .withTrailCells(null, null, null, null, "147 000", "147 000"))
        .withTrailCells(null, null, null, null, "147 000", "147 000")
        .withPositionDigest(true,
            "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Tertiary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 1))",
            "(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1))",
            "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))");     
        runReportTest("Hier report with all the Program default columns, by Primary Program, with percentages", "AMP-17190-all-programs-by-primary", new String[] {"Activity Linked With Pledge", "Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program"}, fddr_correct);                  
    }

    @Test
    @Ignore
    public void testProgramsDetails()
    {
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17190-all-details",
                ColumnReportDataModel.withColumns("AMP-17190-all-details",
                    SimpleColumnModel.withContents("Project Title", "Activity with primary_tertiary_program", "Activity with primary_tertiary_program", "Activity Linked With Pledge", "Activity Linked With Pledge", "activity with primary_program", "activity with primary_program", "activity with tertiary_program", "activity with tertiary_program"), 
                    SimpleColumnModel.withContents("National Planning Objectives Detail", MUST_BE_EMPTY), 
                    SimpleColumnModel.withContents("Primary Program Detail", "Activity with primary_tertiary_program", "[Subprogram p1, Subprogram p1.b]", "activity with primary_program", "Program #1"), 
                    SimpleColumnModel.withContents("Secondary Program Detail", MUST_BE_EMPTY), 
                    SimpleColumnModel.withContents("Tertiary Program Detail", "Activity with primary_tertiary_program", "OP111 name", "activity with tertiary_program", "OP112 name"), 
                    GroupColumnModel.withSubColumns("Funding",
                        GroupColumnModel.withSubColumns("2014",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "50 000", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000", "activity with tertiary_program", "15 000"))), 
                    GroupColumnModel.withSubColumns("Total Costs",
                        SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "50 000", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000", "activity with tertiary_program", "15 000")))
                .withTrailCells(null, null, null, null, null, "147 000", "147 000"))
            .withTrailCells(null, null, null, null, null, "147 000", "147 000")
            .withPositionDigest(true,
                "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC National Planning Objectives Detail: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Primary Program Detail: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Secondary Program Detail: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Tertiary Program Detail: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 1))",
                "(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1))",
                "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))");
        
        runReportTest("Flat report with all the 'program details' ", "AMP-17190-all-details", new String[] {"Activity Linked With Pledge", "Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program"}, fddr_correct);             
    }

    @Test
    public void testPrograms()
    {
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17190-all-programs-no-hier",
                ColumnReportDataModel.withColumns("AMP-17190-all-programs-no-hier",
                        SimpleColumnModel.withContents("Project Title", "Activity with primary_tertiary_program", "Activity with primary_tertiary_program", "Activity Linked With Pledge", "Activity Linked With Pledge", "activity with primary_program", "activity with primary_program", "activity with tertiary_program", "activity with tertiary_program"), 
                        SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY), 
                        SimpleColumnModel.withContents("Primary Program", "Activity with primary_tertiary_program", "[Subprogram p1, Subprogram p1.b]"), 
                        SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY), 
                        SimpleColumnModel.withContents("Tertiary Program", "Activity with primary_tertiary_program", "OP1 name", "activity with tertiary_program", "OP1 name"), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2014",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "50 000", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000", "activity with tertiary_program", "15 000"))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "50 000", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000", "activity with tertiary_program", "15 000")))
                    .withTrailCells(null, null, null, null, null, "147 000", "147 000"))
                .withTrailCells(null, null, null, null, null, "147 000", "147 000")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Primary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Tertiary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 5, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 6, colSpan: 1))",
                    "(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 5, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 6, colSpan: 1))");
        
        runReportTest("Flat report with all the Program default columns ", "AMP-17190-all-programs-no-hier", new String[] {"Activity Linked With Pledge", "Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program"}, fddr_correct);              
    }

    @Test
    public void testProgramsByTertiary()
    {
        GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17190-all-programs-by-tertiary",
                GroupReportModel.withColumnReports("AMP-17190-all-programs-by-tertiary",
                        ColumnReportDataModel.withColumns("Tertiary Program: OP1 name",
                            SimpleColumnModel.withContents("Project Title", "Activity with primary_tertiary_program", "Activity with primary_tertiary_program", "activity with tertiary_program", "activity with tertiary_program"), 
                            SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY), 
                            SimpleColumnModel.withContents("Primary Program", "Activity with primary_tertiary_program", "[Subprogram p1, Subprogram p1.b]"), 
                            SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY), 
                            GroupColumnModel.withSubColumns("Funding",
                                GroupColumnModel.withSubColumns("2014",
                                    SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "50 000", "activity with tertiary_program", "15 000"))), 
                            GroupColumnModel.withSubColumns("Total Costs",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "50 000", "activity with tertiary_program", "15 000")))
                        .withTrailCells(null, null, null, null, "65 000", "65 000"),
                        ColumnReportDataModel.withColumns("Tertiary Program: Tertiary Program Unallocated",
                            SimpleColumnModel.withContents("Project Title", "Activity Linked With Pledge", "Activity Linked With Pledge", "activity with primary_program", "activity with primary_program"), 
                            SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY), 
                            SimpleColumnModel.withContents("Primary Program", MUST_BE_EMPTY), 
                            SimpleColumnModel.withContents("Secondary Program", MUST_BE_EMPTY), 
                            GroupColumnModel.withSubColumns("Funding",
                                GroupColumnModel.withSubColumns("2014",
                                    SimpleColumnModel.withContents("Actual Commitments", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000"))), 
                            GroupColumnModel.withSubColumns("Total Costs",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000")))
                        .withTrailCells(null, null, null, null, "82 000", "82 000"))
                    .withTrailCells(null, null, null, null, "147 000", "147 000"))
                .withTrailCells(null, null, null, null, "147 000", "147 000")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Primary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Secondary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 1))",
                    "(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))");
        
        runReportTest("Hier report with all the Program default columns, by Tertiary Program", "AMP-17190-all-programs-by-tertiary", new String[] {"Activity Linked With Pledge", "Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program"}, fddr_correct);              
    }

    @Test
    public void testProgramsBySecondary(){
        GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17190-all-programs-by-secondary",
                GroupReportModel.withColumnReports("AMP-17190-all-programs-by-secondary",
                    ColumnReportDataModel.withColumns("Secondary Program: Secondary Program Unallocated",
                        SimpleColumnModel.withContents("Project Title", "Activity with primary_tertiary_program", "Activity with primary_tertiary_program", "Activity Linked With Pledge", "Activity Linked With Pledge", "activity with primary_program", "activity with primary_program", "activity with tertiary_program", "activity with tertiary_program"), 
                        SimpleColumnModel.withContents("National Planning Objectives", MUST_BE_EMPTY), 
                        SimpleColumnModel.withContents("Primary Program", "Activity with primary_tertiary_program", "[Subprogram p1, Subprogram p1.b]"), 
                        SimpleColumnModel.withContents("Tertiary Program", "Activity with primary_tertiary_program", "OP1 name", "activity with tertiary_program", "OP1 name"), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2014",
                                SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "50 000", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000", "activity with tertiary_program", "15 000"))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "Activity with primary_tertiary_program", "50 000", "Activity Linked With Pledge", "50 000", "activity with primary_program", "32 000", "activity with tertiary_program", "15 000")))
                    .withTrailCells(null, null, null, null, "147 000", "147 000"))
                .withTrailCells(null, null, null, null, "147 000", "147 000"))
            .withTrailCells(null, null, null, null, "147 000", "147 000")
            .withPositionDigest(true,
                "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC National Planning Objectives: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Primary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Tertiary Program: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 3, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 4, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 5, colSpan: 1))",
                "(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 4, colSpan: 1))",
                "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 5, colSpan: 1))");
        runReportTest("Hier report with all the Program default columns, by Secondary Program", "AMP-17190-all-programs-by-secondary", new String[] {"Activity Linked With Pledge", "Activity with primary_tertiary_program", "activity with primary_program", "activity with tertiary_program"}, fddr_correct);                
    }
    
    @Before
    public void setUp() {
        TLSUtils.getThreadLocalInstance().setForcedLangCode("en");
    }
}
