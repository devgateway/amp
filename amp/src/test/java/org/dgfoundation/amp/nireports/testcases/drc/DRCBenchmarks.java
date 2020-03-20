package org.dgfoundation.amp.nireports.testcases.drc;

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
 * the entry point for NiReports' "DRC" offline performance benchmarks (e.g. a curated, hardcoded, dump of altered DRC DB)
 * @author Dolghier Constantin
 *
 */
public class DRCBenchmarks {
    
    public static void main(String[] args) {
        List<ImmutablePair<ReportSpecification, BenchmarkResult>> specs = Arrays.asList(
            new ImmutablePair<>(
                    ReportSpecificationImpl.buildFor(
                        "flat", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.LOCATION_ADM_LEVEL_1),
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                        null,
                        GroupingCriteria.GROUPING_YEARLY), 
                    new BenchmarkResult(null, 133, 55, 6917, 34564,
                        "{RAW / Project Title=, RAW / Primary Sector=, RAW / Region=, RAW / Funding / 1976 / Actual Commitments=372500.005327, RAW / Funding / 1976 / Actual Disbursements=372500.005327, RAW / Funding / 1977 / Actual Commitments=372500.005327, RAW / Funding / 1977 / Actual Disbursements=372500.005327, RAW / Funding / 1978 / Actual Commitments=372500.005327, RAW / Funding / 1978 / Actual Disbursements=372500.005327, RAW / Funding / 1979 / Actual Commitments=1425577.918486, RAW / Funding / 1979 / Actual Disbursements=1425577.918486, RAW / Funding / 1980 / Actual Commitments=1053077.913159, RAW / Funding / 1980 / Actual Disbursements=1053077.913159, RAW / Funding / 1981 / Actual Commitments=4617989.846037, RAW / Funding / 1981 / Actual Disbursements=4617989.846037, RAW / Funding / 1982 / Actual Commitments=6115012.818545, RAW / Funding / 1982 / Actual Disbursements=6115012.818545, RAW / Funding / 1983 / Actual Commitments=7282856.955245, RAW / Funding / 1983 / Actual Disbursements=7282856.955245, RAW / Funding / 1984 / Actual Commitments=7282856.955245, RAW / Funding / 1984 / Actual Disbursements=7282856.955245, RAW / Funding / 1985 / Actual Commitments=13154324.934407, RAW / Funding / 1985 / Actual Disbursements=13154324.934407, RAW / Funding / 1986 / Actual Commitments=12101247.021248, RAW / Funding / 1986 / Actual Disbursements=12101247.021248, RAW / Funding / 1987 / Actual Commitments=9617948.990237, RAW / Funding / 1987 / Actual Disbursements=9617948.990237, RAW / Funding / 1988 / Actual Commitments=8450104.853537, RAW / Funding / 1988 / Actual Disbursements=8450104.853537, RAW / Funding / 1989 / Actual Commitments=8450104.853537, RAW / Funding / 1989 / Actual Disbursements=9531718.755404, RAW / Funding / 1990 / Actual Commitments=8450104.853537, RAW / Funding / 1990 / Actual Disbursements=8450104.853537, RAW / Funding / 1991 / Actual Commitments=8016077.69623, RAW / Funding / 1991 / Actual Disbursements=8016077.69623, RAW / Funding / 1999 / Actual Commitments=330031216.709354, RAW / Funding / 1999 / Actual Disbursements=195891617.643982, RAW / Funding / 2000 / Actual Commitments=542824397.798437, RAW / Funding / 2000 / Actual Disbursements=497076321.784925, RAW / Funding / 2001 / Actual Commitments=394173492.042558, RAW / Funding / 2001 / Actual Disbursements=317116001.187688, RAW / Funding / 2002 / Actual Commitments=1212122592.907287, RAW / Funding / 2002 / Actual Disbursements=1196058123.674821, RAW / Funding / 2003 / Actual Commitments=951382045.270229, RAW / Funding / 2003 / Actual Disbursements=915786284.261067, RAW / Funding / 2004 / Actual Commitments=1225118396.345908, RAW / Funding / 2004 / Actual Disbursements=1081800008.272084, RAW / Funding / 2005 / Actual Commitments=1805501018.516628, RAW / Funding / 2005 / Actual Disbursements=1805093294.823869, RAW / Funding / 2006 / Actual Commitments=1674656494.469127, RAW / Funding / 2006 / Actual Disbursements=1668519478.244828, RAW / Funding / 2007 / Actual Commitments=4674153225.635703, RAW / Funding / 2007 / Actual Disbursements=2629643937.447903, RAW / Funding / 2008 / Actual Commitments=3095609817.142104, RAW / Funding / 2008 / Actual Disbursements=1636242783.434274, RAW / Funding / 2009 / Actual Commitments=3163808290.033388, RAW / Funding / 2009 / Actual Disbursements=1748608395.35207, RAW / Funding / 2010 / Actual Commitments=14033758749.62421, RAW / Funding / 2010 / Actual Disbursements=2228340551.429235, RAW / Funding / 2011 / Actual Commitments=5284437427.178273, RAW / Funding / 2011 / Actual Disbursements=1699849257.343659, RAW / Funding / 2012 / Actual Commitments=3199506805.321285, RAW / Funding / 2012 / Actual Disbursements=2105489040.164957, RAW / Funding / 2013 / Actual Commitments=3118846239.436951, RAW / Funding / 2013 / Actual Disbursements=1443415368.621468, RAW / Funding / 2014 / Actual Commitments=1953756970.550959, RAW / Funding / 2014 / Actual Disbursements=544473839.563758, RAW / Totals / Actual Commitments=46756821964.607832, RAW / Totals / Actual Disbursements=21811620702.777886}")),
            new ImmutablePair<>(
                    ReportSpecificationImpl.buildFor(
                        "bySector", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.LOCATION_ADM_LEVEL_1),
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                        Arrays.asList(ColumnConstants.PRIMARY_SECTOR),
                        GroupingCriteria.GROUPING_YEARLY),
                    new BenchmarkResult(null, 173, 50, 6807, 34564, null)
                    ),
            new ImmutablePair<>(
                    ReportSpecificationImpl.buildFor(
                        "bySectorByLocation", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.LOCATION_ADM_LEVEL_1),
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                        Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.LOCATION_ADM_LEVEL_1),
                        GroupingCriteria.GROUPING_YEARLY),
                    new BenchmarkResult(null, 291, 67, 6807, 34564, null)
                    ),
            new ImmutablePair<>(
                    ReportSpecificationImpl.buildFor(
                        "bySectorByLocationByProgramQuarterly", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR, ColumnConstants.LOCATION_ADM_LEVEL_1, ColumnConstants.PRIMARY_PROGRAM_LEVEL_1),
                        Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS),
                        Arrays.asList(ColumnConstants.PRIMARY_SECTOR, ColumnConstants.LOCATION_ADM_LEVEL_1, ColumnConstants.PRIMARY_PROGRAM_LEVEL_1),
                        GroupingCriteria.GROUPING_QUARTERLY),
                    new BenchmarkResult(null, 430, 120, 6807, 34564, null)
                    ),
            new ImmutablePair<>(
                    FilteringSanityChecks.buildSpecForFiltering("bySectorFilteredByLocation", 
                        Arrays.asList(ColumnConstants.PROJECT_TITLE, ColumnConstants.PRIMARY_SECTOR), 
                        Arrays.asList(ColumnConstants.PRIMARY_SECTOR), 
                        ColumnConstants.LOCATION_ADM_LEVEL_1, Arrays.asList(1613l), false),
                    new BenchmarkResult(null, 183, 25, 3868, 18736, null))
            );
        new SchemaBenchmarks(DRCReportsTestSchema.getInstance()).run("Hardcoded DRC", specs);
    }
}
