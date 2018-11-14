package org.dgfoundation.amp.nireports.testcases.tanzania;

import java.util.Arrays;
import java.util.List;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.amp212.FilteringSanityChecks;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.testcases.SchemaBenchmarks;
import org.dgfoundation.amp.nireports.testcases.BenchmarksRunner.BenchmarkResult;

/**
 * the entry point for NiReports' "Tanzania" offline performance benchmarks (e.g. a curated, hardcoded, dump of altered Tanzania DB)
 * @author Dolghier Constantin
 *
 */
public class TanzaniaBenchmarks {
    
    public static void main(String[] args) {
        List<ImmutablePair<ReportSpecification, BenchmarkResult>> specs = Arrays.asList(
            new ImmutablePair<>(
                    ReportSpecificationImpl.buildFor(
                        "flat", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.REGION),
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                        null,
                        GroupingCriteria.GROUPING_YEARLY), 
                    new BenchmarkResult(null, 115, 48, 2920, 21101,
                        "{RAW / Project Title=, RAW / Primary Sector=, RAW / Region=, RAW / Funding / 201 / Actual Commitments=220000, RAW / Funding / 201 / Actual Disbursements=0, RAW / Funding / 209 / Actual Commitments=0, RAW / Funding / 209 / Actual Disbursements=114953.892819, RAW / Funding / 213 / Actual Commitments=0, RAW / Funding / 213 / Actual Disbursements=38669.501235, RAW / Funding / 1988 / Actual Commitments=2444178.526873, RAW / Funding / 1988 / Actual Disbursements=0, RAW / Funding / 1994 / Actual Commitments=3268377.980404, RAW / Funding / 1994 / Actual Disbursements=0, RAW / Funding / 1996 / Actual Commitments=0, RAW / Funding / 1996 / Actual Disbursements=800462.989965, RAW / Funding / 1997 / Actual Commitments=85278901.597275, RAW / Funding / 1997 / Actual Disbursements=2165603.032627, RAW / Funding / 1998 / Actual Commitments=35522809.130092, RAW / Funding / 1998 / Actual Disbursements=4309542.314909, RAW / Funding / 1999 / Actual Commitments=69985272.261807, RAW / Funding / 1999 / Actual Disbursements=2477350.332132, RAW / Funding / 2000 / Actual Commitments=194955099.235059, RAW / Funding / 2000 / Actual Disbursements=45590790.286893, RAW / Funding / 2001 / Actual Commitments=390160529.97954, RAW / Funding / 2001 / Actual Disbursements=279508741.928501, RAW / Funding / 2002 / Actual Commitments=848028056.115493, RAW / Funding / 2002 / Actual Disbursements=456207789.598045, RAW / Funding / 2003 / Actual Commitments=632269717.956875, RAW / Funding / 2003 / Actual Disbursements=395743519.055453, RAW / Funding / 2004 / Actual Commitments=1746825898.400788, RAW / Funding / 2004 / Actual Disbursements=627854533.144351, RAW / Funding / 2005 / Actual Commitments=1583263695.269142, RAW / Funding / 2005 / Actual Disbursements=833220504.493051, RAW / Funding / 2006 / Actual Commitments=9694468056.957079, RAW / Funding / 2006 / Actual Disbursements=2590848203.418336, RAW / Funding / 2007 / Actual Commitments=19108547770.346273, RAW / Funding / 2007 / Actual Disbursements=2511443394.047281, RAW / Funding / 2008 / Actual Commitments=1859293615.795596, RAW / Funding / 2008 / Actual Disbursements=3410638781.689361, RAW / Funding / 2009 / Actual Commitments=2398610338.175559, RAW / Funding / 2009 / Actual Disbursements=2993646467.74197, RAW / Funding / 2010 / Actual Commitments=2476889932.549029, RAW / Funding / 2010 / Actual Disbursements=2666561405.437508, RAW / Funding / 2011 / Actual Commitments=1297083769.52446, RAW / Funding / 2011 / Actual Disbursements=2797638018.546844, RAW / Funding / 2012 / Actual Commitments=1521435681.439017, RAW / Funding / 2012 / Actual Disbursements=3523951816.924034, RAW / Funding / 2013 / Actual Commitments=2029259219.584825, RAW / Funding / 2013 / Actual Disbursements=3003736590.155452, RAW / Funding / 2014 / Actual Commitments=1846839481.376017, RAW / Funding / 2014 / Actual Disbursements=2224064983.290378, RAW / Funding / 2015 / Actual Commitments=1113045953.111009, RAW / Funding / 2015 / Actual Disbursements=177385818.093526, RAW / Funding / 2016 / Actual Commitments=144664511.860353, RAW / Funding / 2016 / Actual Disbursements=30000, RAW / Funding / 2017 / Actual Commitments=53870311.547194, RAW / Funding / 2017 / Actual Disbursements=0, RAW / Funding / 2018 / Actual Commitments=9270295.325122, RAW / Funding / 2018 / Actual Disbursements=0, RAW / Funding / 2019 / Actual Commitments=0, RAW / Funding / 2019 / Actual Disbursements=271664.018011, RAW / Totals / Actual Commitments=49145501474.044881, RAW / Totals / Actual Disbursements=28548249603.932682}")),
            new ImmutablePair<>(
                    ReportSpecificationImpl.buildFor(
                        "bySector", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.REGION),
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                        Arrays.asList(ColumnConstants.PRIMARY_SECTOR),
                        GroupingCriteria.GROUPING_YEARLY),
                    new BenchmarkResult(null, 101, 37, 2426, 21101, null)
                    ),
            new ImmutablePair<>(
                    ReportSpecificationImpl.buildFor(
                        "bySectorByLocation", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.REGION),
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                        Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.REGION),
                        GroupingCriteria.GROUPING_YEARLY),
                    new BenchmarkResult(null, 158, 44, 2426, 21101, null)
                    ),
            new ImmutablePair<>(
                    ReportSpecificationImpl.buildFor(
                        "bySectorByLocationByProgramQuarterly", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.REGION, ColumnConstants.PRIMARY_PROGRAM_LEVEL_1),
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                        Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.REGION, ColumnConstants.PRIMARY_PROGRAM_LEVEL_1),
                        GroupingCriteria.GROUPING_QUARTERLY),
                    new BenchmarkResult(null, 223, 53, 2426, 21101, null)
                    ),
            new ImmutablePair<>(
                    FilteringSanityChecks.buildSpecForFiltering("bySectorFilteredByLocation", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR), 
                        Arrays.asList(ColumnConstants.PRIMARY_SECTOR), 
                        ColumnConstants.REGION, Arrays.asList(263l), false),
                    new BenchmarkResult(null, 116, 15, 1748, 17230, null))
            );
        new SchemaBenchmarks(TanzaniaReportsTestSchema.getInstance()).run("Hardcoded Tanzania", specs);
    }
}
