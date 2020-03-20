package org.dgfoundation.amp.nireports.testcases.drc;

import static org.dgfoundation.amp.nireports.testcases.TestModelConstants.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.AmpFiltersConverter;
import org.dgfoundation.amp.nireports.amp.NiReportsGenerator;
import org.dgfoundation.amp.nireports.behaviours.AverageAmountBehaviour;
import org.dgfoundation.amp.nireports.behaviours.DateTokenBehaviour;
import org.dgfoundation.amp.nireports.behaviours.GeneratedIntegerBehaviour;
import org.dgfoundation.amp.nireports.behaviours.PercentageTokenBehaviour;
import org.dgfoundation.amp.nireports.behaviours.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.behaviours.VarianceMeasureBehaviour;
import org.dgfoundation.amp.nireports.formulas.NiFormula;
import org.dgfoundation.amp.nireports.schema.NiComputedColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.schema.TimeRange;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.testcases.ReportsTestScratchpad;
import org.dgfoundation.amp.nireports.testcases.TestFundingFetcher;
import org.dgfoundation.amp.nireports.testcases.TrivialTestMeasure;
import org.dgfoundation.amp.nireports.testcases.drc.columns.*;
import org.dgfoundation.amp.nireports.testcases.drc.dimensions.*;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedCells;
import org.dgfoundation.amp.nireports.testcases.generic.HardcodedColumn;

import org.digijava.module.aim.helper.Constants;

/**
 * the NiReports testcases schema
 * @author acartaleanu
 *
 */
public class DRCReportsTestSchema extends AbstractReportsSchema {

    public final static Set<String> TRANSACTION_LEVEL_HIERARCHIES = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList(ColumnConstants.MODE_OF_PAYMENT, ColumnConstants.FUNDING_STATUS, ColumnConstants.FINANCING_INSTRUMENT, ColumnConstants.TYPE_OF_ASSISTANCE)));

    public final static OrganizationsTestDimension orgsDimension = OrganizationsTestDimension.instance;
    public final static LocationsTestDimension locsDimension = LocationsTestDimension.instance;
    public final static SectorsTestDimension secsDimension = SectorsTestDimension.instance;
    public final static ProgramsTestDimension progsDimension = ProgramsTestDimension.instance;
    public final static CategoriesTestDimension catsDimension = CategoriesTestDimension.instance;

    public final static NiDimensionUsage DONOR_DIM_USG = orgsDimension.getDimensionUsage("DN");
    public final static NiDimensionUsage IA_DIM_USG = orgsDimension.getDimensionUsage("IA");
    public final static NiDimensionUsage PS_DIM_USG = secsDimension.getDimensionUsage("Primary");
    public final static NiDimensionUsage SS_DIM_USG = secsDimension.getDimensionUsage("Secondary");
    
    public final static NiDimensionUsage PP_DIM_USG = progsDimension.getDimensionUsage("Primary Program");
    public final static NiDimensionUsage SP_DIM_USG = progsDimension.getDimensionUsage("Secondary Program");
    
    public final static NiDimensionUsage LOC_DIM_USG = locsDimension.getDimensionUsage("LOCS");
            
    private static DRCReportsTestSchema instance;
    
    public final static Map<String, Long> activityNames = generateActivityNamesMap();
    
    public static Set<String> workspaceFilter = activityNames.keySet();
    
    public DRCReportsTestSchema() { 
        //2x degenerate dimensions
        addTextColumn(ColumnConstants.STATUS, new StatusCells(activityNames, catsDimension.getEntityIds(), catsDimension, "status"));
        addTextColumn(ColumnConstants.IMPLEMENTATION_LEVEL, new ImplementationLevelCells(activityNames, catsDimension.getEntityIds(), catsDimension, "impl_level"));
        //2x hierarchical percentages dimensions (like Primary Sectors > Primary Sector Subsectors)
        addPercentageColumn(ColumnConstants.PRIMARY_SECTOR, new PrimarySectorCells(activityNames, secsDimension.getEntityIds(), PS_DIM_USG.getLevelColumn(LEVEL_SECTOR)));
        addPercentageColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, new PrimarySectorSubSectorCells(activityNames, secsDimension.getEntityIds(), PS_DIM_USG.getLevelColumn(LEVEL_SUBSECTOR)));
        //2x hierarchical percentages dimensions sharing one NiDimension to point (2) each (for example, Primary Sector > Secondary Sector)
        addPercentageColumn(ColumnConstants.SECONDARY_SECTOR, new SecondarySectorCells(activityNames, secsDimension.getEntityIds(), SS_DIM_USG.getLevelColumn(LEVEL_SECTOR)));
        addPercentageColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, new SecondarySectorSubSectorCells(activityNames, secsDimension.getEntityIds(), SS_DIM_USG.getLevelColumn(LEVEL_SUBSECTOR)));
        
        addPercentageColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, new PrimaryProgramLevel1Cells(activityNames, progsDimension.getEntityIds(), PP_DIM_USG.getLevelColumn(LEVEL_PROGRAM_1)));
        addPercentageColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2, new PrimaryProgramLevel2Cells(activityNames, progsDimension.getEntityIds(), PP_DIM_USG.getLevelColumn(LEVEL_PROGRAM_2)));
        
        //one more percentages dimension, independent of those at (2) and (3) (for example, locations)
        addPercentageColumn(ColumnConstants.LOCATION_ADM_LEVEL_0, new CountryCells(activityNames, locsDimension.getEntityIds(), LOC_DIM_USG.getLevelColumn(ADM_LEVEL_0)));
        addPercentageColumn(ColumnConstants.LOCATION_ADM_LEVEL_1, new RegionCells(activityNames, locsDimension.getEntityIds(), LOC_DIM_USG.getLevelColumn(ADM_LEVEL_1)));
        addPercentageColumn(ColumnConstants.LOCATION_ADM_LEVEL_2, new ZoneCells(activityNames, locsDimension.getEntityIds(), LOC_DIM_USG.getLevelColumn(ADM_LEVEL_2)));
        addPercentageColumn(ColumnConstants.LOCATION_ADM_LEVEL_3, new DistrictCells(activityNames, locsDimension.getEntityIds(), LOC_DIM_USG.getLevelColumn(ADM_LEVEL_3)));
        //one non-percentages dimension (like Project Title)
        addTextColumn(ColumnConstants.PROJECT_TITLE, new ProjectTitleCells(activityNames, activityNames, null));
                
        addPercentageColumn(ColumnConstants.IMPLEMENTING_AGENCY, new ImplementingAgencyCells(activityNames, orgsDimension.getEntityIds(), IA_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION)));
        addPercentageColumn(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, new ImplementingAgencyGroupsCells(activityNames, orgsDimension.getEntityIds(), IA_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION_GROUP)));
        addPercentageColumn(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, new ImplementingAgencyTypeCells(activityNames, orgsDimension.getEntityIds(), IA_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION_TYPE)));
        
        addTextColumn(ColumnConstants.DONOR_AGENCY, new DonorAgencyCells(activityNames, orgsDimension.getEntityIds(), DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION)));
        addTextColumn(ColumnConstants.DONOR_GROUP, new DonorGroupCells(activityNames, orgsDimension.getEntityIds(), DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION_GROUP)));
        addTextColumn(ColumnConstants.DONOR_TYPE, new DonorTypeCells(activityNames, orgsDimension.getEntityIds(), DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION_TYPE)));
        
        addTextColumn(ColumnConstants.FINANCING_INSTRUMENT, new FinancingInstrumentCells(activityNames, catsDimension.getEntityIds(), catsDimension, "fin_instr"));
        addTextColumn(ColumnConstants.TYPE_OF_ASSISTANCE, new TypeOfAssistanceCells(activityNames, catsDimension.getEntityIds(), catsDimension, "type_of_assistance"));
        addTextColumn(ColumnConstants.MODE_OF_PAYMENT, new ModeOfPaymentCells(activityNames, catsDimension.getEntityIds(), catsDimension, "mode_of_payment"));
        addTextColumn(ColumnConstants.FUNDING_STATUS, new FundingStatusCells(activityNames, catsDimension.getEntityIds(), catsDimension, "funding_status"));
        
        addDateColumn(ColumnConstants.ACTIVITY_CREATED_ON, new ActivityCreatedOnCells(activityNames, activityNames, null));
        addDateColumn(ColumnConstants.ACTIVITY_UPDATED_ON, new ActivityUpdatedOnCells(activityNames, activityNames, null));
        
        //4x trivial measures
        addMeasure(new TrivialTestMeasure(MeasureConstants.ACTUAL_COMMITMENTS, Constants.COMMITMENT, "Actual", false));
        addMeasure(new TrivialTestMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", false));
        addMeasure(new TrivialTestMeasure(MeasureConstants.PLANNED_COMMITMENTS, Constants.COMMITMENT, "Planned", false));
        addMeasure(new TrivialTestMeasure(MeasureConstants.PLANNED_DISBURSEMENTS, Constants.DISBURSEMENT, "Planned", false));
        
        addMeasure(new TrivialTestMeasure(MeasureConstants.CUMULATIVE_COMMITMENT, Constants.COMMITMENT, "Actual", false, true, TrivialMeasureBehaviour.getTotalsOnlyInstance()));
        addMeasure(new TrivialTestMeasure(MeasureConstants.CUMULATIVE_DISBURSEMENT, Constants.DISBURSEMENT, "Actual", false, true, TrivialMeasureBehaviour.getTotalsOnlyInstance()));
                
        // empty trivial measure
        addMeasure(new TrivialTestMeasure(MeasureConstants.PIPELINE_COMMITMENTS, Constants.COMMITMENT, "Pipeline", false));
        
        addColumn(new NiComputedColumn<>(ColumnConstants.ACTIVITY_COUNT, null, GeneratedIntegerBehaviour.ENTITIES_COUNT_BEHAVIOUR, null));
        
        // computed measures
        // addLinearFilterMeasure(measureName, measureDescriptions.get(measureName), behaviour, false, def);
        addLinearFilterMeasure(MeasureConstants.VARIANCE_OF_COMMITMENTS, null, VarianceMeasureBehaviour.instance, false, false, MeasureConstants.ACTUAL_COMMITMENTS, +1);
        addLinearFilterMeasure(MeasureConstants.VARIANCE_OF_DISBURSEMENTS, null, VarianceMeasureBehaviour.instance, false, false, MeasureConstants.ACTUAL_DISBURSEMENTS, +1);
        addLinearFilterMeasure(MeasureConstants.AVERAGE_SIZE_DISBURSEMENTS, null, AverageAmountBehaviour.instance, false, false, MeasureConstants.ACTUAL_DISBURSEMENTS, +1);
        
        addFormulaComputedMeasure(MeasureConstants.EXECUTION_RATE, null, NiFormula.PERCENTAGE(MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PLANNED_DISBURSEMENTS), false);
        addFormulaComputedMeasure(MeasureConstants.AVERAGE_DISBURSEMENT_RATE, null, NiFormula.PERCENTAGE(MeasureConstants.ACTUAL_DISBURSEMENTS, MeasureConstants.PLANNED_DISBURSEMENTS), true);
        addFormulaComputedMeasure(MeasureConstants.CUMULATIVE_EXECUTION_RATE, null, NiFormula.PERCENTAGE(MeasureConstants.CUMULATIVE_DISBURSEMENT, MeasureConstants.CUMULATIVE_COMMITMENT), false);
    
        addMeasure(new TrivialTestMeasure(MeasureConstants.PERCENTAGE_OF_TOTAL_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", false, false,
                byMeasureDividingBehaviour(TimeRange.NONE, MeasureConstants.ACTUAL_DISBURSEMENTS), singletonMap(MeasureConstants.ACTUAL_DISBURSEMENTS, false)));

        addMeasure(new TrivialTestMeasure(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS, Constants.COMMITMENT, "Actual", false, false,
                byMeasureDividingBehaviour(TimeRange.NONE, MeasureConstants.ACTUAL_COMMITMENTS), singletonMap(MeasureConstants.ACTUAL_COMMITMENTS, false)));
    }

    protected final Memoizer<TestFundingFetcher> fundingFetcher = new Memoizer<>(() -> new TestFundingFetcher(activityNames, new DRCFundingCells(activityNames)));
    
    @Override
    public NiReportColumn<CategAmountCell> getFundingFetcher(NiReportsEngine engine) {
        return fundingFetcher.get();
    }
    
    private void addDateColumn(String name, HardcodedCells<DateCell> cells) {
        addColumn(new HardcodedColumn<DateCell>(name, cells, cells.levelColumn.orElse(null), DateTokenBehaviour.instance));
    }
    
    private static Map<String, Long> generateActivityNamesMap() {
        return new DRCHardcodedActivities().getActIdsMap();
    }
    
    private void addPercentageColumn(String name, HardcodedCells<PercentageTextCell> cells) {
        addColumn(new HardcodedColumn<PercentageTextCell>(name, cells, cells.levelColumn.get(), PercentageTokenBehaviour.instance));
    }

    private void addTextColumn(String name, HardcodedCells<TextCell> cells) {
        addColumn(new HardcodedColumn<TextCell>(name, cells, cells.levelColumn.orElse(null), TextualTokenBehaviour.instance));
    }
    
    public static DRCReportsTestSchema getInstance() {
        if (instance == null){
            instance = new DRCReportsTestSchema();
        }
        return instance;
    }
    
    @Override
    public NiFilters convertFilters(NiReportsEngine engine) {
        return new AmpFiltersConverter(engine).buildNiFilters(z -> getWorkspaceFilterIds());
        //return (ReportFilters rf) -> new TestNiFilters(); 
    }

    protected Set<Long> getWorkspaceFilterIds() {
        Set<String> missing = workspaceFilter.stream().filter(z -> !activityNames.containsKey(z)).collect(Collectors.toSet());
        NiUtils.failIf(!missing.isEmpty(), () -> String.format("the following specified activities are not present in the hardcoded schema: <%s>", missing));
        return workspaceFilter.stream().map(z -> activityNames.get(z)).collect(Collectors.toSet());
    }
    
    public static ReportExecutor getExecutor(boolean logToDb) {
        ReportExecutor res = new NiReportsGenerator(getInstance(), logToDb, null);
        return res;
    }   
    
    @Override
    public SchemaSpecificScratchpad generateScratchpad(NiReportsEngine engine) {
        return new ReportsTestScratchpad(engine);
    }
    
    @Override public DRCReportsTestSchema addColumn(NiReportColumn<?> col) {
        if (TRANSACTION_LEVEL_HIERARCHIES.contains(col.name))
            col = col.setTransactionLevelHierarchy();
        return (DRCReportsTestSchema) super.addColumn(col);
    }
}
