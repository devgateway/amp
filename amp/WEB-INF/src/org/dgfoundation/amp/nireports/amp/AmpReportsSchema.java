package org.dgfoundation.amp.nireports.amp;

import static org.dgfoundation.amp.nireports.NiUtils.failIf;
import static org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension.*;
import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.*;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiCurrency;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiReportsGenerator;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.module.aim.util.CurrencyUtil;

import com.google.common.base.Function;

/**
 * the big, glorious, immaculate, AMP Reports schema
 * @author Dolghier Constantin
 *
 */
public class AmpReportsSchema extends AbstractReportsSchema {

	public final OrganisationsDimension orgsDimension = OrganisationsDimension.instance;
	public final LocationsDimension locsDimension = LocationsDimension.instance;
	
	private static AmpReportsSchema instance = new AmpReportsSchema();
	
	/**
	 * the name of the "Donor" instance of the Organisation Dimension
	 */
	public final static String ORG_DIMENSION_DONOR = "DN";
	public final static String ORG_DIMENSION_IA = "IMPL";
	
	public final static String LOC_DIMENSION_LOC = "LOC";
	
	public static AmpReportsSchema getInstance() {
		return instance;
	}
	
	protected AmpReportsSchema() {
		addTextColumn(ColumnConstants.PROJECT_TITLE, "v_titles");
		addTextColumn(ColumnConstants.TEAM, "v_teams");
		addTextColumn(ColumnConstants.OBJECTIVE, "v_objectives");
		addTextColumn(ColumnConstants.ISSUES, "v_issues");
		
		addTextColumn(ColumnConstants.DONOR_AGENCY, "v_ni_donor_orgs", orgsDimension.getLevelColumn(ORG_DIMENSION_DONOR, LEVEL_ORGANISATION));
		addTextColumn(ColumnConstants.DONOR_GROUP, "v_ni_donor_orgsgroups", orgsDimension.getLevelColumn(ORG_DIMENSION_DONOR, LEVEL_ORGANISATION_GROUP));
		addTextColumn(ColumnConstants.DONOR_TYPE, "v_ni_donor_orgstypes", orgsDimension.getLevelColumn(ORG_DIMENSION_DONOR, LEVEL_ORGANISATION_TYPE));
		
		addTextColumnWithPercentages(ColumnConstants.IMPLEMENTING_AGENCY, "v_implementing_agency", orgsDimension.getLevelColumn(ORG_DIMENSION_IA, LEVEL_ORGANISATION));
		addTextColumnWithPercentages(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, "v_implementing_agency_groups", orgsDimension.getLevelColumn(ORG_DIMENSION_IA, LEVEL_ORGANISATION_GROUP));
		addTextColumnWithPercentages(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, "v_implementing_agency_type", orgsDimension.getLevelColumn(ORG_DIMENSION_IA, LEVEL_ORGANISATION_TYPE));
		
		addTextColumnWithPercentages(ColumnConstants.COUNTRY, "v_countries", locsDimension.getLevelColumn(LOC_DIMENSION_LOC, LEVEL_COUNTRY));
		addTextColumnWithPercentages(ColumnConstants.REGION, "v_regions", locsDimension.getLevelColumn(LOC_DIMENSION_LOC, LEVEL_REGION));
		addTextColumnWithPercentages(ColumnConstants.ZONE, "v_zones", locsDimension.getLevelColumn(LOC_DIMENSION_LOC, LEVEL_ZONE));
		addTextColumnWithPercentages(ColumnConstants.DISTRICT, "v_districts", locsDimension.getLevelColumn(LOC_DIMENSION_LOC, LEVEL_DISTRICT));
	}
		
	private AmpReportsSchema addTextColumn(String columnName, String view) {
		return addTextColumn(columnName, view, null);
	}

	private AmpReportsSchema addTextColumn(String columnName, String view, LevelColumn levelColumn) {
		return addColumn(SimpleTextColumn.fromView(columnName, view, levelColumn));
	}

	private AmpReportsSchema addTextColumnWithPercentages(String columnName, String viewName, LevelColumn levelColumn) {
		return addColumn(PercentageTextColumn.fromView(columnName, viewName, levelColumn));
	}
		
//	public final MathContext mathContext = new MathContext(5, RoundingMode.HALF_EVEN); // banker's rounding
	
	protected final CurrencyConvertor currencyConvertor = AmpCurrencyConvertor.getInstance();
	
	@Override
	public NiReportColumn<CategAmountCell> getFundingFetcher() {
		return new AmpFundingColumn();
	}

	@Override
	public Function<ReportFilters, NiFilters> getFiltersConverter() {
		return (ReportFilters rf) -> new AmpNiFilters();
	}

	@Override
	public Function<NiReportsEngine, SchemaSpecificScratchpad> getScratchpadSupplier() {
		return engine -> new AmpReportsScratchpad(engine);
	}
	
	/**
	 * users entrypoint for running reports using NiReports
	 * @return
	 */
	public static ReportExecutor getExecutor() {
		ReportExecutor res = new NiReportsGenerator(AmpReportsSchema.getInstance());
		return res;
	}

	/**
	 * initialized the NiReports subsystem. <br />
	 * Notice that this is not strictly necesary for being able to run NiReports, but it runs many self-checks as part of running a very simple report and cached some frequently-used data
	 * @throws AMPException
	 */
	public static void init() throws AMPException {
		AmpReportsSchema.getExecutor().executeReport(ReportSpecificationImpl.buildFor("self-test report", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
			null,
			GroupingCriteria.GROUPING_YEARLY));
	}
	
	// ========== implementation code below ==========
	@Override public AmpReportsSchema addColumn(NiReportColumn<?> col) {
		return (AmpReportsSchema) super.addColumn(col);
	}
}
