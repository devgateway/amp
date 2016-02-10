package org.dgfoundation.amp.testmodels;





import java.util.Map;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.amp.NiReportsGenerator;
import org.dgfoundation.amp.nireports.amp.TestNiFilters;
import org.dgfoundation.amp.nireports.amp.dimensions.SectorsDimension;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.PercentageTokenBehaviour;
import org.dgfoundation.amp.nireports.schema.TextualTokenBehaviour;
import org.dgfoundation.amp.testmodels.dimensions.CategoriesTestDimension;
import org.dgfoundation.amp.testmodels.dimensions.HardcodedNiDimension;
import org.dgfoundation.amp.testmodels.dimensions.LocationsTestDimension;
import org.dgfoundation.amp.testmodels.dimensions.OrganizationsTestDimension;
import org.dgfoundation.amp.testmodels.dimensions.ProgramsTestDimension;
import org.dgfoundation.amp.testmodels.dimensions.SectorsTestDimension;
import org.dgfoundation.amp.testmodels.nicolumns.CountryCells;
import org.dgfoundation.amp.testmodels.nicolumns.DistrictCells;
import org.dgfoundation.amp.testmodels.nicolumns.DonorAgencyCells;
import org.dgfoundation.amp.testmodels.nicolumns.FinancingInstrumentCells;
import org.dgfoundation.amp.testmodels.nicolumns.FundingStatusCells;
import org.dgfoundation.amp.testmodels.nicolumns.HardcodedCells;
import org.dgfoundation.amp.testmodels.nicolumns.ImplementationLevelCells;
import org.dgfoundation.amp.testmodels.nicolumns.ModeOfPaymentCells;
import org.dgfoundation.amp.testmodels.nicolumns.PrimarySectorCells;
import org.dgfoundation.amp.testmodels.nicolumns.PrimarySectorSubSectorCells;
import org.dgfoundation.amp.testmodels.nicolumns.ProjectTitleCells;
import org.dgfoundation.amp.testmodels.nicolumns.RegionCells;
import org.dgfoundation.amp.testmodels.nicolumns.SecondarySectorCells;
import org.dgfoundation.amp.testmodels.nicolumns.SecondarySectorSubSectorCells;
import org.dgfoundation.amp.testmodels.nicolumns.StatusCells;
import org.dgfoundation.amp.testmodels.nicolumns.TestColumn;
import org.dgfoundation.amp.testmodels.nicolumns.TypeOfAssistanceCells;
import org.dgfoundation.amp.testmodels.nicolumns.ZoneCells;
import org.digijava.module.aim.helper.Constants;

import com.google.common.base.Function;

import static org.dgfoundation.amp.testmodels.TestModelConstants.*;

public class HardcodedReportsTestSchema extends AbstractReportsSchema {
	
	

	
	
	public final static OrganizationsTestDimension orgsDimension = OrganizationsTestDimension.instance;
	public final static LocationsTestDimension locsDimension = LocationsTestDimension.instance;
	public final static SectorsTestDimension secsDimension = SectorsTestDimension.instance;
	public final static ProgramsTestDimension progsDimension = ProgramsTestDimension.instance;
	public final static CategoriesTestDimension catsDimension = CategoriesTestDimension.instance;
	
	private static HardcodedReportsTestSchema instance;
	
	final Map<String, Long> activityNames;	
	
	public HardcodedReportsTestSchema() {
		activityNames = generateActivityNamesMap();
		//2x degenerate dimensions
		addDegenerateTextColumn(ColumnConstants.STATUS, new StatusCells(activityNames, catsDimension.getEntityIds()), LEVEL_CATEGORY, catsDimension);
		addDegenerateTextColumn(ColumnConstants.IMPLEMENTATION_LEVEL, new ImplementationLevelCells(activityNames, catsDimension.getEntityIds()), LEVEL_CATEGORY, catsDimension);
		//2x hierarchical percentages dimensions (like Primary Sectors > Primary Sector Subsectors)
		addPercentageColumn(ColumnConstants.PRIMARY_SECTOR, new PrimarySectorCells(activityNames, secsDimension.getEntityIds()), LEVEL_SECTOR, secsDimension);
		addPercentageColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, new PrimarySectorSubSectorCells(activityNames, secsDimension.getEntityIds()), LEVEL_SUBSECTOR, secsDimension);
		//2x hierarchical percentages dimensions sharing one NiDimension to point (2) each (for example, Primary Sector > Secondary Sector)
		addPercentageColumn(ColumnConstants.SECONDARY_SECTOR, new SecondarySectorCells(activityNames, secsDimension.getEntityIds()), LEVEL_SECTOR, secsDimension);
		addPercentageColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, new SecondarySectorSubSectorCells(activityNames, secsDimension.getEntityIds()), LEVEL_SUBSECTOR, secsDimension);
		//one more percentages dimension, independent of those at (2) and (3) (for example, locations)
		addPercentageColumn(ColumnConstants.COUNTRY, new CountryCells(activityNames, locsDimension.getEntityIds()), LEVEL_COUNTRY, locsDimension);
		addPercentageColumn(ColumnConstants.REGION, new RegionCells(activityNames, locsDimension.getEntityIds()), LEVEL_REGION, locsDimension);
		addPercentageColumn(ColumnConstants.ZONE, new ZoneCells(activityNames, locsDimension.getEntityIds()), LEVEL_ZONE, locsDimension);
		addPercentageColumn(ColumnConstants.DISTRICT, new DistrictCells(activityNames, locsDimension.getEntityIds()), LEVEL_DISTRICT, locsDimension);
		//one non-percentages dimension (like Project Title)
		addNoDimensionColumn(ColumnConstants.PROJECT_TITLE, new ProjectTitleCells(activityNames, activityNames));
		//3x trivial measures
		addMeasure(new TrivialTestMeasure(MeasureConstants.ACTUAL_COMMITMENTS, Constants.COMMITMENT, "Actual", false));
		addMeasure(new TrivialTestMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new TrivialTestMeasure(MeasureConstants.PLANNED_COMMITMENTS, Constants.COMMITMENT, "Planned", false));
		addMeasure(new TrivialTestMeasure(MeasureConstants.PLANNED_DISBURSEMENTS, Constants.DISBURSEMENT, "Planned", false));
		//mandatory for funding
		addSingleDimensionColumn(ColumnConstants.DONOR_AGENCY, new DonorAgencyCells(activityNames, orgsDimension.getEntityIds()), LEVEL_ORGANIZATION, orgsDimension);
		addDegenerateTextColumn(ColumnConstants.FINANCING_INSTRUMENT, new FinancingInstrumentCells(activityNames, catsDimension.getEntityIds()), LEVEL_CATEGORY, catsDimension);
		addDegenerateTextColumn(ColumnConstants.TYPE_OF_ASSISTANCE, new TypeOfAssistanceCells(activityNames, catsDimension.getEntityIds()), LEVEL_CATEGORY, catsDimension);
		addDegenerateTextColumn(ColumnConstants.MODE_OF_PAYMENT, new ModeOfPaymentCells(activityNames, catsDimension.getEntityIds()), LEVEL_CATEGORY, catsDimension);
		addDegenerateTextColumn(ColumnConstants.FUNDING_STATUS, new FundingStatusCells(activityNames, catsDimension.getEntityIds()), LEVEL_CATEGORY, catsDimension);
	}

	private void addSingleDimensionColumn(String name, DonorAgencyCells cells, int level, HardcodedNiDimension dimension) {
		addColumn(new TestColumn<TextCell>(name, cells, dimension.getLevelColumn(name, level), PercentageTokenBehaviour.instance));
	}

	@Override
	public NiReportColumn<CategAmountCell> getFundingFetcher() {
		return new TestFundingFetcher(activityNames);
	}
	
	private Map<String, Long> generateActivityNamesMap() {
		return new HardcodedActivities().getActIdsMap();
	}
	
	private void addPercentageColumn(String name, HardcodedCells<PercentageTextCell> cells, int level, HardcodedNiDimension dimension) {
		addColumn(new TestColumn<PercentageTextCell>(name, cells, dimension.getLevelColumn(name, level), PercentageTokenBehaviour.instance));
	}

	private void addDegenerateTextColumn(String name, HardcodedCells<TextCell> cells, int level, HardcodedNiDimension dimension) {
		addColumn(new TestColumn<TextCell>(name, cells, dimension.getLevelColumn(name, level), TextualTokenBehaviour.instance));
	}
	
	private void addNoDimensionColumn(String name, HardcodedCells<TextCell> cells) {
		addColumn(new TestColumn<TextCell>(name, cells, null, TextualTokenBehaviour.instance));
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
	public Function<ReportFilters, NiFilters> getFiltersConverter() {
		return (ReportFilters rf) -> new TestNiFilters(this.activityNames.values());	
	}

	public static ReportExecutor getExecutor(boolean logToDb) {
		ReportExecutor res = new NiReportsGenerator(getInstance(), ReportAreaImpl.class, logToDb);
		return res;
	}	
	
	@Override
	public Function<NiReportsEngine, SchemaSpecificScratchpad> getScratchpadSupplier() {
		return engine -> new ReportsTestScratchpad(engine);
	}
	
	@Override public HardcodedReportsTestSchema addColumn(NiReportColumn<?> col) {
		return (HardcodedReportsTestSchema) super.addColumn(col);
	}
}
