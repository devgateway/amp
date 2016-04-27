package org.dgfoundation.amp.testmodels;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.AmpFiltersConverter;
import org.dgfoundation.amp.nireports.amp.NiReportsGenerator;
import org.dgfoundation.amp.nireports.schema.DateTokenBehaviour;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.PercentageTokenBehaviour;
import org.dgfoundation.amp.nireports.schema.TextualTokenBehaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.testmodels.dimensions.CategoriesTestDimension;
import org.dgfoundation.amp.testmodels.dimensions.LocationsTestDimension;
import org.dgfoundation.amp.testmodels.dimensions.OrganizationsTestDimension;
import org.dgfoundation.amp.testmodels.dimensions.ProgramsTestDimension;
import org.dgfoundation.amp.testmodels.dimensions.SectorsTestDimension;
import org.dgfoundation.amp.testmodels.nicolumns.ActivityCreatedOnCells;
import org.dgfoundation.amp.testmodels.nicolumns.ActivityUpdatedOnCells;
import org.dgfoundation.amp.testmodels.nicolumns.CountryCells;
import org.dgfoundation.amp.testmodels.nicolumns.DistrictCells;
import org.dgfoundation.amp.testmodels.nicolumns.DonorAgencyCells;
import org.dgfoundation.amp.testmodels.nicolumns.DonorGroupCells;
import org.dgfoundation.amp.testmodels.nicolumns.DonorTypeCells;
import org.dgfoundation.amp.testmodels.nicolumns.FinancingInstrumentCells;
import org.dgfoundation.amp.testmodels.nicolumns.FundingStatusCells;
import org.dgfoundation.amp.testmodels.nicolumns.HardcodedCells;
import org.dgfoundation.amp.testmodels.nicolumns.HardcodedColumn;
import org.dgfoundation.amp.testmodels.nicolumns.ImplementationLevelCells;
import org.dgfoundation.amp.testmodels.nicolumns.ImplementingAgencyCells;
import org.dgfoundation.amp.testmodels.nicolumns.ImplementingAgencyGroupsCells;
import org.dgfoundation.amp.testmodels.nicolumns.ImplementingAgencyTypeCells;
import org.dgfoundation.amp.testmodels.nicolumns.ModeOfPaymentCells;
import org.dgfoundation.amp.testmodels.nicolumns.PrimaryProgramLevel1Cells;
import org.dgfoundation.amp.testmodels.nicolumns.PrimaryProgramLevel2Cells;
import org.dgfoundation.amp.testmodels.nicolumns.PrimarySectorCells;
import org.dgfoundation.amp.testmodels.nicolumns.PrimarySectorSubSectorCells;
import org.dgfoundation.amp.testmodels.nicolumns.ProjectTitleCells;
import org.dgfoundation.amp.testmodels.nicolumns.RegionCells;
import org.dgfoundation.amp.testmodels.nicolumns.SecondarySectorCells;
import org.dgfoundation.amp.testmodels.nicolumns.SecondarySectorSubSectorCells;
import org.dgfoundation.amp.testmodels.nicolumns.StatusCells;
import org.dgfoundation.amp.testmodels.nicolumns.TypeOfAssistanceCells;
import org.dgfoundation.amp.testmodels.nicolumns.ZoneCells;
import org.digijava.module.aim.helper.Constants;

import com.google.common.base.Function;

import static org.dgfoundation.amp.testmodels.TestModelConstants.*;

/**
 * the NiReports testcases schema
 * @author acartaleanu
 *
 */
public class HardcodedReportsTestSchema extends AbstractReportsSchema {

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
			
	private static HardcodedReportsTestSchema instance;
	
	public final static Map<String, Long> activityNames = generateActivityNamesMap();
	
	public static Set<String> workspaceFilter = activityNames.keySet();
	
	public HardcodedReportsTestSchema() { 
		//2x degenerate dimensions
		addTextColumn(ColumnConstants.STATUS, new StatusCells(activityNames, catsDimension.getEntityIds(), catsDimension));
		addTextColumn(ColumnConstants.IMPLEMENTATION_LEVEL, new ImplementationLevelCells(activityNames, catsDimension.getEntityIds(), catsDimension));
		//2x hierarchical percentages dimensions (like Primary Sectors > Primary Sector Subsectors)
		addPercentageColumn(ColumnConstants.PRIMARY_SECTOR, new PrimarySectorCells(activityNames, secsDimension.getEntityIds(), PS_DIM_USG.getLevelColumn(LEVEL_SECTOR)));
		addPercentageColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, new PrimarySectorSubSectorCells(activityNames, secsDimension.getEntityIds(), PS_DIM_USG.getLevelColumn(LEVEL_SUBSECTOR)));
		//2x hierarchical percentages dimensions sharing one NiDimension to point (2) each (for example, Primary Sector > Secondary Sector)
		addPercentageColumn(ColumnConstants.SECONDARY_SECTOR, new SecondarySectorCells(activityNames, secsDimension.getEntityIds(), SS_DIM_USG.getLevelColumn(LEVEL_SECTOR)));
		addPercentageColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, new SecondarySectorSubSectorCells(activityNames, secsDimension.getEntityIds(), SS_DIM_USG.getLevelColumn(LEVEL_SUBSECTOR)));
		
		addPercentageColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, new PrimaryProgramLevel1Cells(activityNames, progsDimension.getEntityIds(), PP_DIM_USG.getLevelColumn(LEVEL_PROGRAM_1)));
		addPercentageColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2, new PrimaryProgramLevel2Cells(activityNames, progsDimension.getEntityIds(), PP_DIM_USG.getLevelColumn(LEVEL_PROGRAM_2)));
		
		//one more percentages dimension, independent of those at (2) and (3) (for example, locations)
		addPercentageColumn(ColumnConstants.COUNTRY, new CountryCells(activityNames, locsDimension.getEntityIds(), LOC_DIM_USG.getLevelColumn(LEVEL_COUNTRY)));
		addPercentageColumn(ColumnConstants.REGION, new RegionCells(activityNames, locsDimension.getEntityIds(), LOC_DIM_USG.getLevelColumn(LEVEL_REGION)));
		addPercentageColumn(ColumnConstants.ZONE, new ZoneCells(activityNames, locsDimension.getEntityIds(), LOC_DIM_USG.getLevelColumn(LEVEL_ZONE)));
		addPercentageColumn(ColumnConstants.DISTRICT, new DistrictCells(activityNames, locsDimension.getEntityIds(), LOC_DIM_USG.getLevelColumn(LEVEL_DISTRICT)));
		//one non-percentages dimension (like Project Title)
		addTextColumn(ColumnConstants.PROJECT_TITLE, new ProjectTitleCells(activityNames, activityNames));
				
		addPercentageColumn(ColumnConstants.IMPLEMENTING_AGENCY, new ImplementingAgencyCells(activityNames, orgsDimension.getEntityIds(), IA_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION)));
		addPercentageColumn(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, new ImplementingAgencyGroupsCells(activityNames, orgsDimension.getEntityIds(), IA_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION_GROUP)));
		addPercentageColumn(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, new ImplementingAgencyTypeCells(activityNames, orgsDimension.getEntityIds(), IA_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION_TYPE)));
		
		addTextColumn(ColumnConstants.DONOR_AGENCY, new DonorAgencyCells(activityNames, orgsDimension.getEntityIds(), DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION)));
		addTextColumn(ColumnConstants.DONOR_GROUP, new DonorGroupCells(activityNames, orgsDimension.getEntityIds(), DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION_GROUP)));
		addTextColumn(ColumnConstants.DONOR_TYPE, new DonorTypeCells(activityNames, orgsDimension.getEntityIds(), DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANIZATION_TYPE)));
		
		addTextColumn(ColumnConstants.FINANCING_INSTRUMENT, new FinancingInstrumentCells(activityNames, catsDimension.getEntityIds(), catsDimension));
		addTextColumn(ColumnConstants.TYPE_OF_ASSISTANCE, new TypeOfAssistanceCells(activityNames, catsDimension.getEntityIds(), catsDimension));
		addTextColumn(ColumnConstants.MODE_OF_PAYMENT, new ModeOfPaymentCells(activityNames, catsDimension.getEntityIds(), catsDimension));
		addTextColumn(ColumnConstants.FUNDING_STATUS, new FundingStatusCells(activityNames, catsDimension.getEntityIds(), catsDimension));
		
		addDateColumn(ColumnConstants.ACTIVITY_CREATED_ON, new ActivityCreatedOnCells(activityNames, activityNames, null));
		addDateColumn(ColumnConstants.ACTIVITY_UPDATED_ON, new ActivityUpdatedOnCells(activityNames, activityNames, null));
		
		//4x trivial measures
		addMeasure(new TrivialTestMeasure(MeasureConstants.ACTUAL_COMMITMENTS, Constants.COMMITMENT, "Actual", false));
		addMeasure(new TrivialTestMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new TrivialTestMeasure(MeasureConstants.PLANNED_COMMITMENTS, Constants.COMMITMENT, "Planned", false));
		addMeasure(new TrivialTestMeasure(MeasureConstants.PLANNED_DISBURSEMENTS, Constants.DISBURSEMENT, "Planned", false));
		
		// empty trivial measure
		addMeasure(new TrivialTestMeasure(MeasureConstants.PIPELINE_COMMITMENTS, Constants.COMMITMENT, "Pipeline", false));
	}

	@Override
	public NiReportColumn<CategAmountCell> getFundingFetcher(NiReportsEngine engine) {
		return new TestFundingFetcher(activityNames);
	}
	
    private void addDateColumn(String name, HardcodedCells<DateCell> cells) {
    	addColumn(new HardcodedColumn<DateCell>(name, cells, cells.levelColumn.orElse(null), DateTokenBehaviour.instance));
	}
    
	private static Map<String, Long> generateActivityNamesMap() {
		return new HardcodedActivities().getActIdsMap();
	}
	
	private void addPercentageColumn(String name, HardcodedCells<PercentageTextCell> cells) {
		addColumn(new HardcodedColumn<PercentageTextCell>(name, cells, cells.levelColumn.get(), PercentageTokenBehaviour.instance));
	}

	private void addTextColumn(String name, HardcodedCells<TextCell> cells) {
		addColumn(new HardcodedColumn<TextCell>(name, cells, cells.levelColumn.orElse(null), TextualTokenBehaviour.instance));
	}
	
	public static HardcodedReportsTestSchema getInstance() {
		if (instance == null){
			instance = new HardcodedReportsTestSchema();
		}
		return instance;
	}
	
	public static String getRenderedReport(ReportSpecification spec) {
		NiReportsGenerator niGen = new NiReportsGenerator(getInstance());
		return niGen.renderReport(spec);
	}	
	
	@Override
	public NiFilters convertFilters(NiReportsEngine engine) {
		return new AmpFiltersConverter(engine).buildNiFilters(z -> getWorkspaceFilterIds());
		//return (ReportFilters rf) -> new TestNiFilters();	
	}

	protected Set<Long> getWorkspaceFilterIds() {
		return workspaceFilter.stream().map(z -> activityNames.get(z)).collect(Collectors.toSet());
	}
	
	public static ReportExecutor getExecutor(boolean logToDb) {
		ReportExecutor res = new NiReportsGenerator(getInstance(), logToDb, null);
		return res;
	}	
	
	@Override
	public Function<NiReportsEngine, SchemaSpecificScratchpad> getScratchpadSupplier() {
		return engine -> new ReportsTestScratchpad(engine);
	}
	
	@Override public HardcodedReportsTestSchema addColumn(NiReportColumn<?> col) {
		if (TRANSACTION_LEVEL_HIERARCHIES.contains(col.name))
			col = col.setTransactionLevelHierarchy();
		return (HardcodedReportsTestSchema) super.addColumn(col);
	}
}
