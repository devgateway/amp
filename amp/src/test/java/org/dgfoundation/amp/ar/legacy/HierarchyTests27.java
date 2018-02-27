package org.dgfoundation.amp.ar.legacy;

import org.dgfoundation.amp.nireports.testcases.ColumnReportDataModel;
import org.dgfoundation.amp.nireports.testcases.GroupColumnModel;
import org.dgfoundation.amp.nireports.testcases.GroupReportModel;
import org.dgfoundation.amp.nireports.testcases.SimpleColumnModel;
import org.dgfoundation.amp.testutils.*;
import org.junit.Test;

import static org.dgfoundation.amp.testutils.ReportTestingUtils.MUST_BE_EMPTY;

public class HierarchyTests27 extends ReportsTestCase {

    @Test
    public void testRegionEntriesWithoutPercentagesFlat()
    {
        GroupReportModel fddr_correct = GroupReportModel.withColumnReports("AMP-17081-flat",
                ColumnReportDataModel.withColumns("AMP-17081-flat",
                        SimpleColumnModel.withContents("Project Title", "SubNational no percentages", "SubNational no percentages"), 
                        SimpleColumnModel.withContents("Region", "SubNational no percentages", "[Anenii Noi County, Balti County]"), 
                        GroupColumnModel.withSubColumns("Funding",
                            GroupColumnModel.withSubColumns("2014",
                                SimpleColumnModel.withContents("Actual Commitments", "SubNational no percentages", "75 000"))), 
                        GroupColumnModel.withSubColumns("Total Costs",
                            SimpleColumnModel.withContents("Actual Commitments", "SubNational no percentages", "75 000")))
                    .withTrailCells(null, null, "75 000", "75 000"))
                .withTrailCells(null, null, "75 000", "75 000")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Region: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 2, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 1))",
                    "(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 2, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1))");
        
        runReportTest("activity with percentageless location, flat", "AMP-17081-flat", new String[] {"SubNational no percentages"}, fddr_correct);
    }

    @Test
    public void testRegionEntriesWithoutPercentagesByRegion()
    {
        GroupReportModel fddr_correct = GroupReportModel.withGroupReports("AMP-17081-by-region",
                GroupReportModel.withColumnReports("AMP-17081-by-region",
                        ColumnReportDataModel.withColumns("Region: Anenii Noi County",
                            SimpleColumnModel.withContents("Project Title", "SubNational no percentages", "SubNational no percentages"), 
                            GroupColumnModel.withSubColumns("Funding",
                                GroupColumnModel.withSubColumns("2014",
                                    SimpleColumnModel.withContents("Actual Commitments", "SubNational no percentages", "75 000"))), 
                            GroupColumnModel.withSubColumns("Total Costs",
                                SimpleColumnModel.withContents("Actual Commitments", "SubNational no percentages", "75 000")))
                        .withTrailCells(null, "75 000", "75 000"),
                        ColumnReportDataModel.withColumns("Region: Balti County",
                            SimpleColumnModel.withContents("Project Title", "SubNational no percentages", "SubNational no percentages"), 
                            GroupColumnModel.withSubColumns("Funding",
                                GroupColumnModel.withSubColumns("2014",
                                    SimpleColumnModel.withContents("Actual Commitments", "SubNational no percentages", "75 000"))), 
                            GroupColumnModel.withSubColumns("Total Costs",
                                SimpleColumnModel.withContents("Actual Commitments", "SubNational no percentages", "75 000")))
                        .withTrailCells(null, "75 000", "75 000"))
                    .withTrailCells(null, "75 000", "75 000"))
                .withTrailCells(null, "75 000", "75 000")
                .withPositionDigest(true,
                    "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
                    "(line 1:RHLC 2014: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1))",
                    "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))");
        
        runReportTest("activity with percentageless location, by region", "AMP-17081-by-region", new String[] {"SubNational no percentages"}, fddr_correct);        
    }

    @Test
    public void testSectorZonesPercentages()
    {
        GroupReportModel fddr_correct = 
                GroupReportModel.withGroupReports("AMP-16695-sec-zon",
                        GroupReportModel.withGroupReports("AMP-16695-sec-zon",
                            GroupReportModel.withColumnReports("Primary Sector: 110 - EDUCATION",
                                ColumnReportDataModel.withColumns("Zone: Dolboaca",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "53 400"), 
                                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "53 400"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                                .withTrailCells(null, "53 400", "0", "53 400", "0"),
                                ColumnReportDataModel.withColumns("Zone: Glodeni",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "213 600"), 
                                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "213 600"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                                .withTrailCells(null, "213 600", "0", "213 600", "0"))
                            .withTrailCells(null, "267 000", "0", "267 000", "0"),
                            GroupReportModel.withColumnReports("Primary Sector: 120 - HEALTH",
                                ColumnReportDataModel.withColumns("Zone: Dolboaca",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "124 600"), 
                                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "124 600"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                                .withTrailCells(null, "124 600", "0", "124 600", "0"),
                                ColumnReportDataModel.withColumns("Zone: Glodeni",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "498 400"), 
                                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "498 400"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                                .withTrailCells(null, "498 400", "0", "498 400", "0"))
                            .withTrailCells(null, "623 000", "0", "623 000", "0"))
                        .withTrailCells(null, "890 000", "0", "890 000", "0"))
                    .withTrailCells(null, "890 000", "0", "890 000", "0")
                    .withPositionDigest(true,
                        "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 2))",
                        "(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
                        "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))");
        
        runReportTest("report with sectors + zones", "AMP-16695-sec-zon", new String[] {"Activity With Zones and Percentages"}, fddr_correct);      
    }

    @Test
    public void testZonesSectorsPercentages()
    {
        GroupReportModel fddr_correct = 
                GroupReportModel.withGroupReports("AMP-16695-zon-sec",
                        GroupReportModel.withGroupReports("AMP-16695-zon-sec",
                            GroupReportModel.withColumnReports("Zone: Dolboaca",
                                ColumnReportDataModel.withColumns("Primary Sector: 110 - EDUCATION",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "53 400"), 
                                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "53 400"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                                .withTrailCells(null, "53 400", "0", "53 400", "0"),
                                ColumnReportDataModel.withColumns("Primary Sector: 120 - HEALTH",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "124 600"), 
                                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "124 600"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                                .withTrailCells(null, "124 600", "0", "124 600", "0"))
                            .withTrailCells(null, "178 000", "0", "178 000", "0"),
                            GroupReportModel.withColumnReports("Zone: Glodeni",
                                ColumnReportDataModel.withColumns("Primary Sector: 110 - EDUCATION",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "213 600"), 
                                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "213 600"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                                .withTrailCells(null, "213 600", "0", "213 600", "0"),
                                ColumnReportDataModel.withColumns("Primary Sector: 120 - HEALTH",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "498 400"), 
                                            SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "498 400"), 
                                        SimpleColumnModel.withContents("Actual Disbursements", MUST_BE_EMPTY)))
                                .withTrailCells(null, "498 400", "0", "498 400", "0"))
                            .withTrailCells(null, "712 000", "0", "712 000", "0"))
                        .withTrailCells(null, "890 000", "0", "890 000", "0"))
                    .withTrailCells(null, "890 000", "0", "890 000", "0")
                    .withPositionDigest(true,
                        "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 2), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 3, colSpan: 2))",
                        "(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 2))",
                        "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 3, colSpan: 1), RHLC Actual Disbursements: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 4, colSpan: 1))");
        
        runReportTest("report with zones + sectors", "AMP-16695-zon-sec", new String[] {"Activity With Zones and Percentages"}, fddr_correct);      
    }

    @Test
    public void testZonesUnderRegionsBoth()
    {
        GroupReportModel fddr_correct = 
                GroupReportModel.withGroupReports("AMP-16695-1",
                        GroupReportModel.withGroupReports("AMP-16695-1",
                            GroupReportModel.withColumnReports("Region: Anenii Noi County",
                                ColumnReportDataModel.withColumns("Zone: Bulboaca",
                                    SimpleColumnModel.withContents("Project Title", "Activity with Zones", "Activity with Zones").setIsPledge(false), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity with Zones", "570 000").setIsPledge(false))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity with Zones", "570 000").setIsPledge(false)))
                                .withTrailCells(null, "570 000", "570 000"),
                                ColumnReportDataModel.withColumns("Zone: Dolboaca",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages").setIsPledge(false), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "178 000").setIsPledge(false))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "178 000").setIsPledge(false)))
                                .withTrailCells(null, "178 000", "178 000"))
                            .withTrailCells(null, "748 000", "748 000"),
                            GroupReportModel.withColumnReports("Region: Balti County",
                                ColumnReportDataModel.withColumns("Zone: Glodeni",
                                    SimpleColumnModel.withContents("Project Title", "Activity with Zones", "Activity with Zones", "Activity With Zones and Percentages", "Activity With Zones and Percentages").setIsPledge(false), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity with Zones", "570 000", "Activity With Zones and Percentages", "712 000").setIsPledge(false))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity with Zones", "570 000", "Activity With Zones and Percentages", "712 000").setIsPledge(false)))
                                .withTrailCells(null, "1 282 000", "1 282 000"))
                            .withTrailCells(null, "1 282 000", "1 282 000"))
                        .withTrailCells(null, "1 460 000", "1 460 000"))
                    .withTrailCells(null, "1 460 000", "1 460 000")
                    .withPositionDigest(true,
                        "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
                        "(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1))",
                        "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))");

        runReportTest("report with region + zone, 2 acts", "AMP-16695-1", new String[] {"Activity with Zones", "Activity With Zones and Percentages"}, fddr_correct);
    }

    @Test
    public void testZonesUnderRegionsNoPercentages()
    {
        GroupReportModel fddr_correct = 
                GroupReportModel.withGroupReports("AMP-16695-1",
                        GroupReportModel.withGroupReports("AMP-16695-1",
                            GroupReportModel.withColumnReports("Region: Anenii Noi County",
                                ColumnReportDataModel.withColumns("Zone: Bulboaca",
                                    SimpleColumnModel.withContents("Project Title", "Activity with Zones", "Activity with Zones").setIsPledge(false), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity with Zones", "570 000").setIsPledge(false))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity with Zones", "570 000").setIsPledge(false)))
                                .withTrailCells(null, "570 000", "570 000"))
                            .withTrailCells(null, "570 000", "570 000"),
                            GroupReportModel.withColumnReports("Region: Balti County",
                                ColumnReportDataModel.withColumns("Zone: Glodeni",
                                    SimpleColumnModel.withContents("Project Title", "Activity with Zones", "Activity with Zones").setIsPledge(false), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity with Zones", "570 000").setIsPledge(false))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity with Zones", "570 000").setIsPledge(false)))
                                .withTrailCells(null, "570 000", "570 000"))
                            .withTrailCells(null, "570 000", "570 000"))
                        .withTrailCells(null, "570 000", "570 000"))
                    .withTrailCells(null, "570 000", "570 000")
                    .withPositionDigest(true,
                        "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
                        "(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1))",
                        "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))");

        runReportTest("report with region + zone, no percentages", "AMP-16695-1", new String[] {"Activity with Zones"}, fddr_correct);
    }

    @Test
    public void testZonesUnderRegionsWithPercentages()
    {
        GroupReportModel fddr_correct = 
                GroupReportModel.withGroupReports("AMP-16695-1",
                        GroupReportModel.withGroupReports("AMP-16695-1",
                            GroupReportModel.withColumnReports("Region: Anenii Noi County",
                                ColumnReportDataModel.withColumns("Zone: Dolboaca",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "178 000"))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "178 000")))
                                .withTrailCells(null, "178 000", "178 000"))
                            .withTrailCells(null, "178 000", "178 000"),
                            GroupReportModel.withColumnReports("Region: Balti County",
                                ColumnReportDataModel.withColumns("Zone: Glodeni",
                                    SimpleColumnModel.withContents("Project Title", "Activity With Zones and Percentages", "Activity With Zones and Percentages"), 
                                    GroupColumnModel.withSubColumns("Funding",
                                        GroupColumnModel.withSubColumns("2013",
                                            SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "712 000"))), 
                                    GroupColumnModel.withSubColumns("Total Costs",
                                        SimpleColumnModel.withContents("Actual Commitments", "Activity With Zones and Percentages", "712 000")))
                                .withTrailCells(null, "712 000", "712 000"))
                            .withTrailCells(null, "712 000", "712 000"))
                        .withTrailCells(null, "890 000", "890 000"))
                    .withTrailCells(null, "890 000", "890 000")
                    .withPositionDigest(true,
                        "(line 0:RHLC Project Title: (startRow: 0, rowSpan: 3, totalRowSpan: 3, colStart: 0, colSpan: 1), RHLC Funding: (startRow: 0, rowSpan: 1, totalRowSpan: 3, colStart: 1, colSpan: 1), RHLC Total Costs: (startRow: 0, rowSpan: 2, totalRowSpan: 3, colStart: 2, colSpan: 1))",
                        "(line 1:RHLC 2013: (startRow: 1, rowSpan: 1, totalRowSpan: 2, colStart: 1, colSpan: 1))",
                        "(line 2:RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 1, colSpan: 1), RHLC Actual Commitments: (startRow: 2, rowSpan: 1, totalRowSpan: 1, colStart: 2, colSpan: 1))");

        runReportTest("report with region + zone, with percentages", "AMP-16695-1", new String[] {"Activity With Zones and Percentages"}, fddr_correct);
    }   
    
}

