package org.dgfoundation.amp.nireports.amp;

import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.LEVEL_COUNTRY;
import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.LEVEL_DISTRICT;
import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.LEVEL_REGION;
import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.LEVEL_ZONE;
import static org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension.LEVEL_ORGANISATION;
import static org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension.LEVEL_ORGANISATION_GROUP;
import static org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension.LEVEL_ORGANISATION_TYPE;
import static org.dgfoundation.amp.nireports.amp.dimensions.SectorsDimension.LEVEL_ROOT;
import static org.dgfoundation.amp.nireports.amp.dimensions.SectorsDimension.LEVEL_SUBSECTOR;
import static org.dgfoundation.amp.nireports.amp.dimensions.SectorsDimension.LEVEL_SUBSUBSECTOR;
import static org.dgfoundation.amp.nireports.schema.NiDimension.LEVEL_1;
import static org.dgfoundation.amp.nireports.schema.NiDimension.LEVEL_2;
import static org.dgfoundation.amp.nireports.schema.NiDimension.LEVEL_3;
import static org.dgfoundation.amp.nireports.schema.NiDimension.LEVEL_4;
import static org.dgfoundation.amp.nireports.schema.NiDimension.LEVEL_5;
import static org.dgfoundation.amp.nireports.schema.NiDimension.LEVEL_6;
import static org.dgfoundation.amp.nireports.schema.NiDimension.LEVEL_7;
import static org.dgfoundation.amp.nireports.schema.NiDimension.LEVEL_8;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.dyn.DynamicColumnsUtil;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.mondrian.MondrianETL;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.amp.dimensions.CategoriesDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.ComponentsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.ProgramsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.SectorsDimension;
import org.dgfoundation.amp.nireports.output.NiTextCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.BooleanDimension;
import org.dgfoundation.amp.nireports.schema.GeneratedIntegerBehaviour;
import org.dgfoundation.amp.nireports.schema.NiComputedColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiLinearCombinationTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.NiMultipliedFilterTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiReportedEntity;
import org.dgfoundation.amp.nireports.schema.NiTransactionContextMeasure;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.dgfoundation.amp.nireports.schema.PidTextualTokenBehaviour;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.dgfoundation.amp.visibility.data.MeasuresVisibility;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * the big, glorious, immaculate, AMP Reports schema
 * @author Dolghier Constantin
 *
 */
public class AmpReportsSchema extends AbstractReportsSchema {

	public static final Logger logger = Logger.getLogger(AmpReportsSchema.class);
	
	public boolean ENABLE_CACHING = true;
	
	public final static Set<String> TRANSACTION_LEVEL_HIERARCHIES = Collections.unmodifiableSet(new HashSet<>(
			Arrays.asList(
				ColumnConstants.MODE_OF_PAYMENT, ColumnConstants.FUNDING_STATUS, ColumnConstants.FINANCING_INSTRUMENT, ColumnConstants.TYPE_OF_ASSISTANCE, ColumnConstants.DISASTER_RESPONSE_MARKER, ColumnConstants.RELATED_PROJECTS, 
				ColumnConstants.PLEDGES_AID_MODALITY, ColumnConstants.RELATED_PLEDGES, ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE)));
	
	public final static OrganisationsDimension orgsDimension = OrganisationsDimension.instance;
	public final static LocationsDimension locsDimension = LocationsDimension.instance;
	public final static SectorsDimension secsDimension = SectorsDimension.instance;
	public final static ProgramsDimension progsDimension = ProgramsDimension.instance;
	public final static CategoriesDimension catsDimension = CategoriesDimension.instance;
	public final static ComponentsDimension compsDimension = ComponentsDimension.instance;
	public final static BooleanDimension boolDimension = new BooleanDimension("bool", 1l, 2l); // corroborate with FilterRule.TRUE_VALUE
	public final static NiDimension agreementsDimension = SqlSourcedNiDimension.buildDegenerateDimension("agrs", "amp_agreement", "id");
	public final static NiDimension activitiesDimension = SqlSourcedNiDimension.buildDegenerateDimension("acts", "amp_activity_version", "amp_activity_id");
	public final static NiDimension pledgesDimension = SqlSourcedNiDimension.buildDegenerateDimension("pledges", "amp_funding_pledges", "id");
	    
	/**
	 * the pseudocolumn of the header Splitter for cells which are funding flows
	 */
	public final static String PSEUDOCOLUMN_FLOW = "#amp#FundingFlow";
	public final static String PSEUDOCOLUMN_EXP_CLASS = "#amp#ExpClass";
	
	public final static String UNDEFINED_CATEGORY = "Unassigned";
	
	@SuppressWarnings("serial")
	public final static Map<String, String> columnDescriptions = new HashMap<String, String>() {{
		put(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES,  "Level-1 subprogram of the selected national objective");
		put(ColumnConstants.PROJECT_PERIOD,  "Project Period (months),  Proposed Completion Date - Actual Start date");
		put(ColumnConstants.OVERAGE,  "Overage (months),  Age of project - Project period");
		put(ColumnConstants.OVERAGE_PROJECT,  "Current date - Date of Planned Completion");
		put(ColumnConstants.AGE_OF_PROJECT_MONTHS,  "Current date - Date of Agreement Effective");
		put(ColumnConstants.PREDICTABILITY_OF_FUNDING ,  "((Planned Disbursements - Actual Disbursements) / Planned Disbursements) X 100");
		put(ColumnConstants.AVERAGE_SIZE_OF_PROJECTS,  "Total Commitments / Count Of Activities");
		put(ColumnConstants.VARIANCE_OF_COMMITMENTS,  "Max Commitments - Min Commitments");
		put(ColumnConstants.VARIANCE_OF_DISBURSEMENTS,  "Max Disbursements- Min Disbursements");
		put(ColumnConstants.EXECUTION_RATE,  "(Cumulative Disbursement/ Cumulative Commitment) * 100 ");
		put(ColumnConstants.AVERAGE_SIZE_OF_DISBURSEMENTS,  "Sun Actual Disbursments / Number of Actual disbursments");
		put(ColumnConstants.ACTIVITY_COUNT,  "Count Of Activities under the current hierarchy");
		put(ColumnConstants.AVERAGE_DISBURSEMENT_RATE,  "Sum of Execution Rate / Number of Activities");
		put(ColumnConstants.PROJECT_AGE_RATIO,  "Project Age Ratio,  Age of project / Project Period");
//		put(ColumnConstants.PERCENTAGE_OF_TOTAL_DISBURSMENTS,  "AMP 1.x Disbursement Ratio");
		put(ColumnConstants.PRIMARY_PROGRAM_DETAIL,  "The Primary Program, as entered in the database");
		put(ColumnConstants.SECONDARY_PROGRAM_DETAIL,  "The Secondary Program, as entered in the database");
		put(ColumnConstants.TERTIARY_PROGRAM_DETAIL,  "The Tertiary Program, as entered in the database");
		put(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_DETAIL,  "The National Planning Objectives, as entered in the database");
		put(ColumnConstants.PRIMARY_PROGRAM,  "Level-1 subprogram of the selected primary program");
		put(ColumnConstants.SECONDARY_PROGRAM,  "Level-1 subprogram of the selected secondary program");
		put(ColumnConstants.TERTIARY_PROGRAM,  "Level-1 subprogram of the selected tertiary program");
		put(ColumnConstants.CALCULATED_PROJECT_LIFE,  "Difference in days between Planned Start Date and Actual Completion Date");
		put(ColumnConstants.CUMULATIVE_EXECUTION_RATE,  "(Cumulative Disbursement/ Cumulative Commitment) * 100 ");
		
		
	}};
	@SuppressWarnings("serial")
	public final static Map<String, String> measureDescriptions = new HashMap<String, String>() {{
		put(MeasureConstants.CONSUMPTION_RATE , "(Selected Year Cumulated Disbursements / Selected Year of Planned Disbursements) * 100");
		put(MeasureConstants.CUMULATED_DISBURSEMENTS , "Prior Actual Disbursements + Previous Month Disbursements");
		put(MeasureConstants.CUMULATIVE_COMMITMENT,  "Sum of all ACTUAL COMMITMENTS independent of filters");
		put(MeasureConstants.CUMULATIVE_DISBURSEMENT,  "Sum of all ACTUAL DISBURSEMENTS independent of filters");
		put(MeasureConstants.CURRENT_MONTH_DISBURSEMENTS , "Sum of Actual Disbursements of the current month");
		put(MeasureConstants.DISBURSMENT_RATIO , "Sum of actual disbursment / Total actual disb * 100");
		put(MeasureConstants.EXECUTION_RATE , "Sum Of Actual Disb (Dependent on Filter) / Sum Of Planned Disb (Dependent on Filter) * 100");
		put(MeasureConstants.LAST_YEAR_OF_PLANNED_DISBURSEMENTS , "Previous Year Planned Disbursements");
		put(MeasureConstants.PERCENTAGE_OF_DISBURSEMENT , "(Total Actual Disbursements for Year,Quarter,Month / Total Actual Disbursements) * 100");
		put(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS , "Actual commitments for the project / Total actual commitments * 100");
		put(MeasureConstants.PLEDGES_COMMITMENT_GAP , "Total Pledge - Total Actual Commitments");
		put(MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS , "Actual Disbursements Of Previous Month");
		put(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS , "Current Year Actual Disbursements Until Previous Month (not included)");
		put(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS , "Selected Year Planned Disbursements");
		put(MeasureConstants.UNCOMMITTED_BALANCE , "Proposed Project Cost - Total Actual Commitments");
		put(MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE,  "Proposed project cost - Cummulative Commitments");
		put(MeasureConstants.UNDISBURSED_BALANCE , "Total Actual Commitment - Total Actual Disbursement");
		put(MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE,  "Cumulative Commitment - Cumulative Disbursement");	
//		put(MeasureConstants.FORECAST_EXECUTION_RATE , "Actual Disbursements / (Most recent of (Pipeline MTEF for the year, Projection MTEF for the year)). "
//					+ "Measure only makes sense in Annual and Totals-only reports");
		
	}};
	
	/**
	 * the name of the "Donor" instance of the Organisation Dimension
	 */
	public final static NiDimensionUsage DONOR_DIM_USG = orgsDimension.getDimensionUsage(Constants.FUNDING_AGENCY);
	public final static NiDimensionUsage IA_DIM_USG = orgsDimension.getDimensionUsage(Constants.IMPLEMENTING_AGENCY);
	public final static NiDimensionUsage BA_DIM_USG = orgsDimension.getDimensionUsage(Constants.BENEFICIARY_AGENCY);
	public final static NiDimensionUsage EA_DIM_USG = orgsDimension.getDimensionUsage(Constants.EXECUTING_AGENCY);
	public final static NiDimensionUsage RO_DIM_USG = orgsDimension.getDimensionUsage(Constants.RESPONSIBLE_ORGANISATION);
	public final static NiDimensionUsage CA_DIM_USG = orgsDimension.getDimensionUsage(Constants.CONTRACTING_AGENCY);
	public final static NiDimensionUsage RG_DIM_USG = orgsDimension.getDimensionUsage(Constants.REGIONAL_GROUP);
	public final static NiDimensionUsage SG_DIM_USG = orgsDimension.getDimensionUsage(Constants.SECTOR_GROUP);	
	public final static NiDimensionUsage CF_DIM_USG = orgsDimension.getDimensionUsage("ComponentFunding");
	
	public final static NiDimensionUsage PS_DIM_USG = secsDimension.getDimensionUsage("Primary");
	public final static NiDimensionUsage SS_DIM_USG = secsDimension.getDimensionUsage("Secondary");	
	public final static NiDimensionUsage TS_DIM_USG = secsDimension.getDimensionUsage("Tertiary");	
	public final static NiDimensionUsage TAG_S_DIM_USG = secsDimension.getDimensionUsage("Tag");
	
	public final static NiDimensionUsage PP_DIM_USG = progsDimension.getDimensionUsage("Primary Program");
	public final static NiDimensionUsage SP_DIM_USG = progsDimension.getDimensionUsage("Secondary Program");	
	public final static NiDimensionUsage TP_DIM_USG = progsDimension.getDimensionUsage("Tertiary Program");
	public final static NiDimensionUsage NPO_DIM_USG = progsDimension.getDimensionUsage("National Plan Objective");

	public final static NiDimensionUsage LOC_DIM_USG = locsDimension.getDimensionUsage("LOCS");
	public final static NiDimensionUsage AGR_DIM_USG = agreementsDimension.getDimensionUsage("agr");
	public final static LevelColumn AGR_LEVEL_COLUMN = AGR_DIM_USG.getLevelColumn(0);
	
	public final static NiDimensionUsage ACT_DIM_USG = activitiesDimension.getDimensionUsage("acts");
	public final static LevelColumn ACT_LEVEL_COLUMN = ACT_DIM_USG.getLevelColumn(0);
	
	public final static NiDimensionUsage COMPS_DIM_USG = compsDimension.getDimensionUsage("comps");
	public final static LevelColumn COMPONENT_LEVEL_COLUMN = COMPS_DIM_USG.getLevelColumn(ComponentsDimension.LEVEL_COMPONENT);
	
	public final static NiDimensionUsage PLEDGES_DIM_USG = pledgesDimension.getDimensionUsage("pledges");
	public final static LevelColumn PLEDGES_LEVEL_COLUMN = PLEDGES_DIM_USG.getLevelColumn(0);
	
	/**
	 * the callback called on DG_EDITOR-backed text columns
	 */
	public final static Function<String, String> DG_EDITOR_POSTPROCESSOR = DgUtil::cleanHtmlTags;
	
	private static AmpReportsSchema instance = new AmpReportsSchema();
		
	@SuppressWarnings("serial")
	public final Map<NiDimensionUsage, PercentagesCorrector> PERCENTAGE_CORRECTORS = new HashMap<NiDimensionUsage, PercentagesCorrector>() {{
		putAll(orgsDimension.getAllPercentagesCorrectors(false));
		putAll(secsDimension.getAllPercentagesCorrectors(false));
		putAll(progsDimension.getAllPercentagesCorrectors(false));
		putAll(locsDimension.getAllPercentagesCorrectors(false));
	}};
	
	@SuppressWarnings("serial")
	public final Map<NiDimensionUsage, PercentagesCorrector> PLEDGES_PERCENTAGE_CORRECTORS = new HashMap<NiDimensionUsage, PercentagesCorrector>() {{
		putAll(orgsDimension.getAllPercentagesCorrectors(true));
		putAll(secsDimension.getAllPercentagesCorrectors(true));
		putAll(progsDimension.getAllPercentagesCorrectors(true));
		putAll(locsDimension.getAllPercentagesCorrectors(true));
	}};

	public static AmpReportsSchema getInstance() {
		return instance;
	}
	
	protected final static AmpFundingColumn donorFundingColumn = new AmpFundingColumn(AmpFundingColumn.ENTITY_DONOR_FUNDING, "v_ni_donor_funding");
	protected final static AmpFundingColumn pledgeFundingColumn = new AmpFundingColumn(AmpFundingColumn.ENTITY_PLEDGE_FUNDING, "v_ni_pledges_funding");
	protected final static AmpFundingColumn componentFundingColumn = new AmpFundingColumn(AmpFundingColumn.ENTITY_COMPONENT_FUNDING, "v_ni_component_funding");
	
	protected AmpReportsSchema() {
		single_dimension(ColumnConstants.PROJECT_TITLE, "v_titles", ACT_LEVEL_COLUMN);
		no_dimension(ColumnConstants.ACTIVITY_ID, "v_activity_ids");
		no_dimension(ColumnConstants.TEAM, "v_teams");
		no_entity(ColumnConstants.OBJECTIVE, "v_objectives", DG_EDITOR_POSTPROCESSOR);
		no_dimension(ColumnConstants.ISSUES, "v_issues");
		
		no_dimension(ColumnConstants.ACTIVITY_PLEDGES_TITLE, "v_activity_pledges_title");
		no_dimension(ColumnConstants.ACTIVITY_UPDATED_BY, "v_activity_modified_by");
		no_dimension(ColumnConstants.ACTORS, "v_actors");
		
		single_dimension(ColumnConstants.AGREEMENT_CODE, "v_agreement_code", AGR_LEVEL_COLUMN);
		single_dimension(ColumnConstants.AGREEMENT_TITLE_CODE, "v_agreement_title_code", AGR_LEVEL_COLUMN);
		date_column(ColumnConstants.AGREEMENT_CLOSE_DATE, "v_agreement_close_date", AGR_LEVEL_COLUMN);
		date_column(ColumnConstants.AGREEMENT_SIGNATURE_DATE, "v_agreement_signature_date", AGR_LEVEL_COLUMN);
		date_column(ColumnConstants.AGREEMENT_EFFECTIVE_DATE, "v_agreement_effective_date", AGR_LEVEL_COLUMN);
		date_column(ColumnConstants.AGREEMENT_PARLIAMENTARY_APPROVAL_DATE, "v_agreement_parlimentary_date", AGR_LEVEL_COLUMN);
		
		no_dimension(ColumnConstants.AMP_ID, "v_amp_id");
		no_dimension(ColumnConstants.BUDGET_ORGANIZATION, "v_budget_organization");
		no_dimension(ColumnConstants.BUDGET_PROGRAM, "v_budget_program");
		no_dimension(ColumnConstants.BUDGET_STRUCTURE, "v_budget_structure");
		
		single_dimension(ColumnConstants.COMPONENT_DESCRIPTION, "v_component_description", COMPONENT_LEVEL_COLUMN);
		single_dimension(ColumnConstants.COMPONENT_FUNDING_ORGANIZATION, "v_component_funding_organization_name", CF_DIM_USG.getLevelColumn(LEVEL_ORGANISATION));
		single_dimension(ColumnConstants.COMPONENT_NAME, "v_components", COMPONENT_LEVEL_COLUMN);
		single_dimension(ColumnConstants.COMPONENT_TYPE, "v_component_type", COMPS_DIM_USG.getLevelColumn(ComponentsDimension.LEVEL_COMPONENT_TYPE));
		single_dimension(ColumnConstants.DESCRIPTION_OF_COMPONENT_FUNDING, "v_component_funding_description", COMPONENT_LEVEL_COLUMN);
		
		no_dimension(ColumnConstants.COSTING_DONOR, "v_costing_donors");
		no_dimension(ColumnConstants.CREDIT_DONATION, "v_credit_donation");
		
		degenerate_dimension(ColumnConstants.DISASTER_RESPONSE_MARKER, "v_disaster_response_marker", boolDimension);
		no_dimension(ColumnConstants.DONOR_CONTACT_ORGANIZATION, "v_donor_cont_org");
		no_entity(ColumnConstants.ENVIRONMENT, "v_environment", DG_EDITOR_POSTPROCESSOR);
		no_entity(ColumnConstants.EQUAL_OPPORTUNITY, "v_equalopportunity", DG_EDITOR_POSTPROCESSOR);
		degenerate_dimension(ColumnConstants.EXPENDITURE_CLASS, "v_expenditure_class", catsDimension);
		degenerate_dimension(ColumnConstants.FINANCIAL_INSTRUMENT, "v_financial_instrument", catsDimension);
		degenerate_dimension(ColumnConstants.FINANCING_INSTRUMENT, "v_financing_instrument", catsDimension);
		degenerate_dimension(ColumnConstants.FUNDING_STATUS, "v_funding_status", catsDimension);
		degenerate_dimension(ColumnConstants.HUMANITARIAN_AID, "v_humanitarian_aid", boolDimension);
		degenerate_dimension(ColumnConstants.IMPLEMENTATION_LEVEL, "v_implementation_level", catsDimension);
//		addTextColumn(ColumnConstants.IMPLEMENTING_AGENCY_DEPARTMENT_DIVISION, "v_implementing_agency_info");
		no_dimension(ColumnConstants.INDIRECT_ON_BUDGET, "v_indirect_on_budget");
		degenerate_dimension(ColumnConstants.INSTITUTIONS, "v_institutions", catsDimension);
		no_dimension(ColumnConstants.MEASURES_TAKEN, "v_measures_taken");
		no_entity(ColumnConstants.MINORITIES, "v_minorities", DG_EDITOR_POSTPROCESSOR);
		degenerate_dimension(ColumnConstants.MODALITIES, "v_modalities", catsDimension);
		degenerate_dimension(ColumnConstants.MODE_OF_PAYMENT, "v_mode_of_payment", catsDimension);
		degenerate_dimension(ColumnConstants.ON_OFF_TREASURY_BUDGET, "v_on_off_budget", catsDimension);
		no_dimension(ColumnConstants.ORGANIZATIONS_AND_PROJECT_ID, "v_project_id");
		no_dimension(ColumnConstants.PAYMENT_CAPITAL___RECURRENT, "v_mode_of_payment_capital_recurrent");
		no_dimension(ColumnConstants.PHYSICAL_PROGRESS, "v_physical_progress");
		degenerate_dimension(ColumnConstants.PROCUREMENT_SYSTEM, "v_procurement_system", catsDimension);
		degenerate_dimension(ColumnConstants.PROJECT_CATEGORY, "v_project_category", catsDimension);
		no_entity(ColumnConstants.PROJECT_COMMENTS, "v_project_comments", DG_EDITOR_POSTPROCESSOR);
		no_entity(ColumnConstants.PROJECT_DESCRIPTION, "v_description", DG_EDITOR_POSTPROCESSOR);
		no_entity(ColumnConstants.PROJECT_IMPACT, "v_proj_impact", DG_EDITOR_POSTPROCESSOR);
		no_entity(ColumnConstants.RESULTS, "v_results", DG_EDITOR_POSTPROCESSOR);
		no_entity(ColumnConstants.PURPOSE, "v_purposes", DG_EDITOR_POSTPROCESSOR);
		no_entity(ColumnConstants.PROGRAM_DESCRIPTION, "v_program_description", DG_EDITOR_POSTPROCESSOR);
		no_dimension(ColumnConstants.PROJECT_IMPLEMENTING_UNIT, "v_project_impl_unit");
		no_dimension(ColumnConstants.REGIONAL_OBSERVATIONS, "v_regional_observations");
		
		single_dimension(ColumnConstants.RELATED_PLEDGES, "v_related_pledges", PLEDGES_LEVEL_COLUMN);
		
		single_dimension(ColumnConstants.RELATED_PROJECTS, "v_ni_pledges_projects", ACT_LEVEL_COLUMN);
		no_dimension(ColumnConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION, "v_sect_min_cont_org");
		degenerate_dimension(ColumnConstants.SSC_MODALITIES, "v_ssc_modalities", catsDimension);
		degenerate_dimension(ColumnConstants.STATUS, "v_status", catsDimension);
		no_dimension(ColumnConstants.STRUCTURES_COLUMN, "v_structures");
		degenerate_dimension(ColumnConstants.TYPE_OF_ASSISTANCE, "v_terms_assist", catsDimension);
		degenerate_dimension(ColumnConstants.TYPE_OF_COOPERATION, "v_type_of_cooperation", catsDimension);
		degenerate_dimension(ColumnConstants.TYPE_OF_IMPLEMENTATION, "v_type_of_implementation", catsDimension);
		no_dimension(ColumnConstants.APPROVAL_STATUS, "v_approval_status");
		no_dimension(ColumnConstants.FILTERED_APPROVAL_STATUS, "v_filtered_approval_status");
		
		// views with only 2 columns
		no_entity(ColumnConstants.DRAFT, "v_drafts");
		no_entity(ColumnConstants.AC_CHAPTER, "v_ac_chapters");
		no_entity(ColumnConstants.ACCESSION_INSTRUMENT, "v_accession_instruments");
		no_entity(ColumnConstants.ACTIVITY_CREATED_BY, "v_activity_creator");
		no_entity(ColumnConstants.AUDIT_SYSTEM, "v_audit_system"); // catsDimension.getLevelColumn("audit_system", LEVEL_CAT_VALUE));
		no_entity(ColumnConstants.BUDGET_CODE_PROJECT_ID, "v_budget_code_project_id");
		no_entity(ColumnConstants.BUDGET_DEPARTMENT, "v_budget_department");
		no_entity(ColumnConstants.BUDGET_SECTOR, "v_budget_sector");
		no_entity(ColumnConstants.CAPITAL___EXPENDITURE, "v_capital_and_exp");
		no_entity(ColumnConstants.CRIS_NUMBER, "v_cris_number");
		no_entity(ColumnConstants.CURRENT_COMPLETION_DATE_COMMENTS, "v_actual_completion_date_comments");
		no_entity(ColumnConstants.DONOR_CONTACT_EMAIL, "v_donor_cont_email");
		no_entity(ColumnConstants.DONOR_CONTACT_FAX, "v_donor_cont_fax");
		no_entity(ColumnConstants.DONOR_CONTACT_NAME, "v_donor_cont_name");
		no_entity(ColumnConstants.DONOR_CONTACT_PHONE, "v_donor_cont_phone");
		no_entity(ColumnConstants.DONOR_CONTACT_TITLE, "v_donor_cont_title");
		no_entity(ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS_COMMENTS, "v_disbursements_date_comments");
		no_entity(ColumnConstants.FUNDING_ORGANIZATION_ID, "v_funding_org_id");
		no_entity(ColumnConstants.FY, "v_budget_extras_fy");
		no_entity(ColumnConstants.GOVERNMENT_AGREEMENT_NUMBER, "v_gov_agreement_number");
		no_entity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_EMAIL, "v_impl_ex_cont_email");
		no_entity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_FAX, "v_impl_ex_cont_fax");
		no_entity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_NAME, "v_impl_ex_cont_name");
		no_entity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_ORGANIZATION, "v_impl_ex_cont_org");
		no_entity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_PHONE, "v_impl_ex_cont_phone");
		no_entity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_TITLE, "v_impl_ex_cont_title");
		no_entity(ColumnConstants.ISSUES___MEASURES___ACTORS, "v_issues_measure_actors");
		degenerate_dimension(ColumnConstants.JOINT_CRITERIA, "v_yes_no_joint_criteria", boolDimension);
		degenerate_dimension(ColumnConstants.GOVERNMENT_APPROVAL_PROCEDURES, "v_yes_no_government_approval_proc", boolDimension);
		no_entity(ColumnConstants.MINISTRY_CODE, "v_minsitry_code");
		no_entity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_EMAIL, "v_mofed_cont_email");
		no_entity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_FAX, "v_mofed_cont_fax");
		no_entity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_NAME, "v_mofed_cont_name");
		no_entity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION, "v_mofed_cont_org");
		no_entity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_PHONE, "v_mofed_cont_phone");
		no_entity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_TITLE, "v_mofed_cont_title");
		no_entity(ColumnConstants.MULTI_DONOR, "v_multi_donor");
		no_entity(ColumnConstants.PHYSICAL_PROGRESS_DESCRIPTION, "v_physical_description");
		no_entity(ColumnConstants.PHYSICAL_PROGRESS_TITLE, "v_physical_title");
		no_entity(ColumnConstants.PROJECT_CODE, "v_project_code");
		no_entity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_EMAIL, "v_proj_coordr_cont_email");
		no_entity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_FAX, "v_proj_coordr_cont_fax");
		no_entity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_NAME, "v_proj_coordr_cont_name");
		no_entity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_ORGANIZATION, "v_proj_coordr_cont_org");
		no_entity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_PHONE, "v_proj_coordr_cont_phone");
		no_entity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_TITLE, "v_proj_coordr_cont_title");
		no_entity(ColumnConstants.PROPOSED_PROJECT_LIFE, "v_proposed_project_life");
		no_entity(ColumnConstants.REPORTING_SYSTEM, "v_reporting_system"); // , catsDimension.getLevelColumn("mode_of_payment", LEVEL_CAT_VALUE));
		no_entity(ColumnConstants.SECTOR_MINISTRY_CONTACT_EMAIL, "v_sect_min_cont_email");
		no_entity(ColumnConstants.SECTOR_MINISTRY_CONTACT_FAX, "v_sect_min_cont_fax");
		no_entity(ColumnConstants.SECTOR_MINISTRY_CONTACT_NAME, "v_sect_min_cont_name");
		no_entity(ColumnConstants.SECTOR_MINISTRY_CONTACT_PHONE, "v_sect_min_cont_phone");
		no_entity(ColumnConstants.SECTOR_MINISTRY_CONTACT_TITLE, "v_sect_min_cont_title");
		no_entity(ColumnConstants.SUB_VOTE, "v_subvote");
		no_entity(ColumnConstants.VOTE, "v_vote");
		
		single_dimension(ColumnConstants.DONOR_AGENCY, "v_ni_donor_orgs", DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANISATION));
		single_dimension(ColumnConstants.DONOR_GROUP, "v_ni_donor_orgsgroups", DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANISATION_GROUP));
		single_dimension(ColumnConstants.DONOR_TYPE, "v_ni_donor_orgstypes", DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANISATION_TYPE));
		single_dimension(ColumnConstants.DONOR_ACRONYM, "v_ni_donor_orgsacronyms", DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANISATION));
		
		with_percentage(ColumnConstants.IMPLEMENTING_AGENCY, "v_implementing_agency", IA_DIM_USG, LEVEL_ORGANISATION);
		with_percentage(ColumnConstants.IMPLEMENTING_AGENCY_GROUPS, "v_implementing_agency_groups", IA_DIM_USG, LEVEL_ORGANISATION_GROUP);
		with_percentage(ColumnConstants.IMPLEMENTING_AGENCY_TYPE, "v_implementing_agency_type", IA_DIM_USG, LEVEL_ORGANISATION_TYPE);
		
		with_percentage(ColumnConstants.BENEFICIARY_AGENCY, "v_beneficiary_agency", BA_DIM_USG, LEVEL_ORGANISATION);
		with_percentage(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, "v_beneficiary_agency_groups", BA_DIM_USG, LEVEL_ORGANISATION_GROUP);
		//addTextColumnWithPercentages(ColumnConstants.BENEFICIARY_AGENCY__DEPARTMENT_DIVISION, "v_beneficiary_agency_info", orgsDimension.getLevelColumn(ORG_DIMENSION_BA, LEVEL_ORGANISATION_TYPE));
		
		with_percentage(ColumnConstants.EXECUTING_AGENCY, "v_executing_agency", EA_DIM_USG, LEVEL_ORGANISATION);
		with_percentage(ColumnConstants.EXECUTING_AGENCY_GROUPS, "v_executing_agency_groups", EA_DIM_USG, LEVEL_ORGANISATION_GROUP);
		with_percentage(ColumnConstants.EXECUTING_AGENCY_TYPE, "v_executing_agency_type", EA_DIM_USG, LEVEL_ORGANISATION_TYPE);
		//addTextColumnWithPercentages(ColumnConstants.EXECUTING_AGENCY_DEPARTMENT_DIVISION, "v_executing_agency_info", orgsDimension.getLevelColumn(ORG_DIMENSION_EA, LEVEL_ORGANISATION));

		with_percentage(ColumnConstants.RESPONSIBLE_ORGANIZATION, "v_responsible_organisation", RO_DIM_USG, LEVEL_ORGANISATION);
		with_percentage(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, "v_responsible_org_groups", RO_DIM_USG, LEVEL_ORGANISATION_GROUP);
		//addTextColumnWithPercentages(ColumnConstants.RESPONSIBLE_ORGANIZATION_DEPARTMENT_DIVISION, "v_responsible_org_info", orgsDimension.getLevelColumn(ORG_DIMENSION_RO, LEVEL_ORGANISATION_TYPE));

		with_percentage(ColumnConstants.CONTRACTING_AGENCY, "v_contracting_agency", CA_DIM_USG, LEVEL_ORGANISATION);
		single_dimension(ColumnConstants.CONTRACTING_AGENCY_ACRONYM, "v_contracting_agency_acronym", CA_DIM_USG.getLevelColumn(LEVEL_ORGANISATION));
		with_percentage(ColumnConstants.CONTRACTING_AGENCY_GROUPS, "v_contracting_agency_groups", CA_DIM_USG, LEVEL_ORGANISATION_GROUP);
		//addTextColumnWithPercentages(ColumnConstants.CONTRACTING_AGENCY_DEPARTMENT_DIVISION, "v_contracting_agency_info", orgsDimension.getLevelColumn(ORG_DIMENSION_CA, LEVEL_ORGANISATION_GROUP));
		//addTextColumnWithPercentages(ColumnConstants.CONTRACTING_AGENCY_ACRONYM, "v_contracting_agency_acronym", orgsDimension.getLevelColumn(ORG_DIMENSION_CA, LEVEL_ORGANISATION_GROUP));

		with_percentage(ColumnConstants.REGIONAL_GROUP, "v_regional_group", RG_DIM_USG, LEVEL_ORGANISATION_GROUP);
		//addTextColumnWithPercentages(ColumnConstants.REGIONAL_GROUP_DEPARTMENT_DIVISION, "v_regional_group_info", orgsDimension.getLevelColumn(ORG_DIMENSION_RG, LEVEL_ORGANISATION_GROUP));

		with_percentage(ColumnConstants.SECTOR_GROUP, "v_sector_group", SG_DIM_USG, LEVEL_ORGANISATION_GROUP);
		//addTextColumnWithPercentages(ColumnConstants.SECTOR_GROUP_DEPARTMENT_DIVISION, "v_sector_group_info", orgsDimension.getLevelColumn(ORG_DIMENSION_RG, LEVEL_ORGANISATION_GROUP));

		with_percentage(ColumnConstants.PRIMARY_SECTOR, "v_sectors", PS_DIM_USG, LEVEL_ROOT);
		with_percentage(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, "v_sub_sectors", PS_DIM_USG, LEVEL_SUBSECTOR);
		with_percentage(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, "v_sub_sub_sectors", PS_DIM_USG, LEVEL_SUBSUBSECTOR);

		with_percentage(ColumnConstants.SECTOR_TAG, "v_sectors", TAG_S_DIM_USG, LEVEL_ROOT);
		with_percentage(ColumnConstants.SECTOR_TAG_SUB_SECTOR, "v_tag_sub_sectors", TAG_S_DIM_USG, LEVEL_SUBSECTOR);
		with_percentage(ColumnConstants.SECTOR_TAG_SUB_SUB_SECTOR, "v_tag_sub_sub_sectors", TAG_S_DIM_USG, LEVEL_SUBSUBSECTOR);
		
		
		
		with_percentage(ColumnConstants.TERTIARY_SECTOR, "v_tertiary_sectors", TS_DIM_USG, LEVEL_ROOT);
		with_percentage(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, "v_tertiary_sub_sectors", TS_DIM_USG, LEVEL_SUBSECTOR);
		with_percentage(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, "v_tertiary_sub_sub_sectors", TS_DIM_USG, LEVEL_SUBSUBSECTOR);

		with_percentage(ColumnConstants.SECONDARY_SECTOR, "v_secondary_sectors", SS_DIM_USG, LEVEL_ROOT);
		with_percentage(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, "v_secondary_sub_sectors", SS_DIM_USG, LEVEL_SUBSECTOR);
		with_percentage(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, "v_secondary_sub_sub_sectors", SS_DIM_USG, LEVEL_SUBSUBSECTOR);

		with_percentage(ColumnConstants.PRIMARY_PROGRAM, "v_primaryprogram_level_1", PP_DIM_USG, LEVEL_1);
		//addTextColumnWithPercentages(ColumnConstants.PRIMARY_PROGRAM_DETAIL, "v_primaryprogram", progsDimension.getLevelColumn(PROG_DIMENSION_PP, LEVEL_1));
		with_percentage(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, "v_primaryprogram_level_1", PP_DIM_USG, LEVEL_1);
		with_percentage(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2, "v_primaryprogram_level_2", PP_DIM_USG, LEVEL_2);
		with_percentage(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3, "v_primaryprogram_level_3", PP_DIM_USG, LEVEL_3);
		with_percentage(ColumnConstants.PRIMARY_PROGRAM_LEVEL_4, "v_primaryprogram_level_4", PP_DIM_USG, LEVEL_4);
		with_percentage(ColumnConstants.PRIMARY_PROGRAM_LEVEL_5, "v_primaryprogram_level_5", PP_DIM_USG, LEVEL_5);
		with_percentage(ColumnConstants.PRIMARY_PROGRAM_LEVEL_6, "v_primaryprogram_level_6", PP_DIM_USG, LEVEL_6);
		with_percentage(ColumnConstants.PRIMARY_PROGRAM_LEVEL_7, "v_primaryprogram_level_7", PP_DIM_USG, LEVEL_7);
		with_percentage(ColumnConstants.PRIMARY_PROGRAM_LEVEL_8, "v_primaryprogram_level_8", PP_DIM_USG, LEVEL_8);

		with_percentage(ColumnConstants.SECONDARY_PROGRAM, "v_secondaryprogram_level_1", SP_DIM_USG, LEVEL_1);
		//addTextColumnWithPercentages(ColumnConstants.SECONDARY_PROGRAM_DETAIL, "v_secondaryprogram", progsDimension.getLevelColumn(PROG_DIMENSION_SP, LEVEL_0));
		with_percentage(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1, "v_secondaryprogram_level_1", SP_DIM_USG, LEVEL_1);
		with_percentage(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2, "v_secondaryprogram_level_2", SP_DIM_USG, LEVEL_2);
		with_percentage(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3, "v_secondaryprogram_level_3", SP_DIM_USG, LEVEL_3);
		with_percentage(ColumnConstants.SECONDARY_PROGRAM_LEVEL_4, "v_secondaryprogram_level_4", SP_DIM_USG, LEVEL_4);
		with_percentage(ColumnConstants.SECONDARY_PROGRAM_LEVEL_5, "v_secondaryprogram_level_5", SP_DIM_USG, LEVEL_5);
		with_percentage(ColumnConstants.SECONDARY_PROGRAM_LEVEL_6, "v_secondaryprogram_level_6", SP_DIM_USG, LEVEL_6);
		with_percentage(ColumnConstants.SECONDARY_PROGRAM_LEVEL_7, "v_secondaryprogram_level_7", SP_DIM_USG, LEVEL_7);
		with_percentage(ColumnConstants.SECONDARY_PROGRAM_LEVEL_8, "v_secondaryprogram_level_8", SP_DIM_USG, LEVEL_8);

		with_percentage(ColumnConstants.TERTIARY_PROGRAM, "v_tertiaryprogram_level_1", TP_DIM_USG, LEVEL_1);
		//addTextColumnWithPercentages(ColumnConstants.TERTIARY_PROGRAM_DETAIL, "v_tertiaryprogram", progsDimension.getLevelColumn(PROG_DIMENSION_PP, LEVEL_8));
		with_percentage(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1, "v_tertiaryprogram_level_1", TP_DIM_USG, LEVEL_1);
		with_percentage(ColumnConstants.TERTIARY_PROGRAM_LEVEL_2, "v_tertiaryprogram_level_2", TP_DIM_USG, LEVEL_2);
		with_percentage(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3, "v_tertiaryprogram_level_3", TP_DIM_USG, LEVEL_3);
		with_percentage(ColumnConstants.TERTIARY_PROGRAM_LEVEL_4, "v_tertiaryprogram_level_4", TP_DIM_USG, LEVEL_4);
		with_percentage(ColumnConstants.TERTIARY_PROGRAM_LEVEL_5, "v_tertiaryprogram_level_5", TP_DIM_USG, LEVEL_5);
		with_percentage(ColumnConstants.TERTIARY_PROGRAM_LEVEL_6, "v_tertiaryprogram_level_6", TP_DIM_USG, LEVEL_6);
		with_percentage(ColumnConstants.TERTIARY_PROGRAM_LEVEL_7, "v_tertiaryprogram_level_7", TP_DIM_USG, LEVEL_7);
		with_percentage(ColumnConstants.TERTIARY_PROGRAM_LEVEL_8, "v_tertiaryprogram_level_8", TP_DIM_USG, LEVEL_8);

		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, "v_nationalobjectives_level_1", NPO_DIM_USG, LEVEL_1);
		//addTextColumnWithPercentages(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_DETAIL, "v_nationalobjectives", progsDimension.getLevelColumn(PROG_DIMENSION_NO, LEVEL_0));
		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, "v_nationalobjectives_level_1", NPO_DIM_USG, LEVEL_1);
		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2, "v_nationalobjectives_level_2", NPO_DIM_USG, LEVEL_2);
		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3, "v_nationalobjectives_level_3", NPO_DIM_USG, LEVEL_3);
		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_4, "v_nationalobjectives_level_4", NPO_DIM_USG, LEVEL_4);
		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_5, "v_nationalobjectives_level_5", NPO_DIM_USG, LEVEL_5);
		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_6, "v_nationalobjectives_level_6", NPO_DIM_USG, LEVEL_6);
		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_7, "v_nationalobjectives_level_7", NPO_DIM_USG, LEVEL_7);
		with_percentage(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_8, "v_nationalobjectives_level_8", NPO_DIM_USG, LEVEL_8);

		with_percentage(ColumnConstants.COUNTRY, "v_countries", LOC_DIM_USG, LEVEL_COUNTRY);
		with_percentage(ColumnConstants.REGION, "v_regions", LOC_DIM_USG, LEVEL_REGION);
		with_percentage(ColumnConstants.ZONE, "v_zones", LOC_DIM_USG, LEVEL_ZONE);
		with_percentage(ColumnConstants.DISTRICT, "v_districts", LOC_DIM_USG, LEVEL_DISTRICT);
		
		addMtefColumns();
		addPseudoComputedColumns();
		addColumn(new PPCColumn(ColumnConstants.PROPOSED_PROJECT_AMOUNT, "v_proposed_cost"));
		addColumn(new PPCColumn(ColumnConstants.REVISED_PROJECT_AMOUNT, "v_revised_project_cost"));
		
		date_column(ColumnConstants.ACTIVITY_CREATED_ON, "v_creation_date");
		date_column(ColumnConstants.ACTIVITY_UPDATED_ON, "v_updated_date");
		date_column(ColumnConstants.ACTUAL_APPROVAL_DATE, "v_actual_approval_date");
		date_column(ColumnConstants.ACTUAL_COMPLETION_DATE, "v_actual_completion_date");
		date_column(ColumnConstants.ACTUAL_START_DATE, "v_actual_start_date");
		date_column(ColumnConstants.CURRENT_COMPLETION_DATE, "v_actual_completion_date");
		date_column(ColumnConstants.DONOR_COMMITMENT_DATE, "v_donor_commitment_date");
		date_column(ColumnConstants.FINAL_DATE_FOR_CONTRACTING, "v_contracting_date");
		date_column(ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS, "v_disbursements_date");
		date_column(ColumnConstants.FUNDING_CLASSIFICATION_DATE, "v_funding_classification_date");
		date_column(ColumnConstants.FUNDING_END_DATE, "v_funding_end_date");
		date_column(ColumnConstants.FUNDING_START_DATE, "v_funding_start_date");
		date_column(ColumnConstants.ORIGINAL_COMPLETION_DATE, "v_original_completion_date");
		/*pledge columns -- commented for now, should be added once pledge reports are implemented*/
//		date_column(ColumnConstants.PLEDGES_DETAIL_START_DATE, "v_pledges_funding_start_date"); 
//		date_column(ColumnConstants.PLEDGES_DETAIL_END_DATE, "v_pledges_funding_end_date "); 
		date_column(ColumnConstants.PROPOSED_APPROVAL_DATE, "v_actual_proposed_date"); 
		date_column(ColumnConstants.PROPOSED_COMPLETION_DATE, "v_proposed_completion_date"); 
		date_column(ColumnConstants.PROPOSED_START_DATE, "v_proposed_start_date");
			
		addPledgeColumns();
		
		addTrivialMeasures();
		addUnfilteredTrivialMeasures();
		addFundingFlowMeasures();
		addTaggedMeasures();
		addComputedLinearMeasures();
		addSscMeasures();
		
		addColumn(new NiComputedColumn<>(ColumnConstants.ACTIVITY_COUNT, null, GeneratedIntegerBehaviour.ENTITIES_COUNT_BEHAVIOUR, columnDescriptions.get(ColumnConstants.ACTIVITY_COUNT)));
	}
	
	protected void addPledgeColumns() {
		// pledge columns
		no_entity(ColumnConstants.PLEDGES_TITLES, "v_ni_pledges_titles");
		single_dimension(ColumnConstants.PLEDGES_DONOR_GROUP, "v_pledges_donor_group", DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANISATION_GROUP));
		single_dimension(ColumnConstants.PLEDGES_DONOR_TYPE, "v_pledges_donor_type", DONOR_DIM_USG.getLevelColumn(LEVEL_ORGANISATION_TYPE));
		
		with_percentage(ColumnConstants.PLEDGES_PROGRAMS, "v_pledges_programs", PP_DIM_USG, LEVEL_1);
		with_percentage(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_2, "v_pledges_programs_level_2", PP_DIM_USG, LEVEL_2);
		with_percentage(ColumnConstants.PLEDGES_PROGRAMS_LEVEL_3, "v_pledges_programs_level_3", PP_DIM_USG, LEVEL_3);
		
		with_percentage(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS, "v_pledges_secondary_programs", SP_DIM_USG, LEVEL_1);
		with_percentage(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_2, "v_pledges_secondary_programs_level_2", SP_DIM_USG, LEVEL_2);
		with_percentage(ColumnConstants.PLEDGES_SECONDARY_PROGRAMS_LEVEL_3, "v_pledges_secondary_programs_level_3", SP_DIM_USG, LEVEL_3);
		
		with_percentage(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS, "v_pledges_tertiary_programs", TP_DIM_USG, LEVEL_1);
		with_percentage(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_2, "v_pledges_tertiary_programs_level_2", TP_DIM_USG, LEVEL_2);
		with_percentage(ColumnConstants.PLEDGES_TERTIARY_PROGRAMS_LEVEL_3, "v_pledges_tertiary_programs_level_3", TP_DIM_USG, LEVEL_3);
		
		with_percentage(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES, "v_pledges_npd_objectives", NPO_DIM_USG, LEVEL_1);
		with_percentage(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_2, "v_pledges_npd_objectives_level_2", NPO_DIM_USG, LEVEL_2);
		with_percentage(ColumnConstants.PLEDGES_NATIONAL_PLAN_OBJECTIVES_LEVEL_3, "v_pledges_npd_objectives_level_3", NPO_DIM_USG, LEVEL_3);
		
		with_percentage(ColumnConstants.PLEDGES_SECTORS, "v_pledges_sectors", PS_DIM_USG, LEVEL_ROOT);
		with_percentage(ColumnConstants.PLEDGES_SECTORS_SUBSECTORS, "v_pledges_sectors_subsectors", PS_DIM_USG, LEVEL_SUBSECTOR);
		with_percentage(ColumnConstants.PLEDGES_SECTORS_SUBSUBSECTORS, "v_pledges_sectors_subsubsectors", PS_DIM_USG, LEVEL_SUBSUBSECTOR);
		
		with_percentage(ColumnConstants.PLEDGES_SECONDARY_SECTORS, "v_pledges_secondary_sectors", SS_DIM_USG, LEVEL_ROOT);
		with_percentage(ColumnConstants.PLEDGES_SECONDARY_SUBSECTORS, "v_pledges_secondary_sectors_subsectors", SS_DIM_USG, LEVEL_SUBSECTOR);
		with_percentage(ColumnConstants.PLEDGES_SECONDARY_SUBSUBSECTORS, "v_pledges_secondary_sectors_subsubsectors", SS_DIM_USG, LEVEL_SUBSUBSECTOR);
		
		with_percentage(ColumnConstants.PLEDGES_TERTIARY_SECTORS, "v_pledges_tertiary_sectors", TS_DIM_USG, LEVEL_ROOT);
		with_percentage(ColumnConstants.PLEDGES_TERTIARY_SUBSECTORS, "v_pledges_tertiary_sectors_subsectors", TS_DIM_USG, LEVEL_SUBSECTOR);
		with_percentage(ColumnConstants.PLEDGES_TERTIARY_SUBSUBSECTORS, "v_pledges_tertiary_sectors_subsubsectors", TS_DIM_USG, LEVEL_SUBSUBSECTOR);
		
		with_percentage(ColumnConstants.PLEDGES_COUNTRIES, "v_pledges_countries", LOC_DIM_USG, LEVEL_COUNTRY);
		with_percentage(ColumnConstants.PLEDGES_REGIONS, "v_pledges_regions", LOC_DIM_USG, LEVEL_REGION);
		with_percentage(ColumnConstants.PLEDGES_ZONES, "v_pledges_zones", LOC_DIM_USG, LEVEL_ZONE);
		with_percentage(ColumnConstants.PLEDGES_DISTRICTS, "v_pledges_districts", LOC_DIM_USG, LEVEL_DISTRICT);
		
		degenerate_dimension(ColumnConstants.PLEDGES_AID_MODALITY, "v_pledges_aid_modality", catsDimension);
		degenerate_dimension(ColumnConstants.PLEDGE_STATUS, "v_pledges_status", catsDimension);
		single_dimension(ColumnConstants.PLEDGES_TYPE_OF_ASSISTANCE, "v_pledges_type_of_assistance", catsDimension.getLevelColumn(ColumnConstants.TYPE_OF_ASSISTANCE, 1));
		
		
		@SuppressWarnings("serial")
		Map<String, String> pledgeContacts = new HashMap<String, String>() {{
		    put(ColumnConstants.PLEDGE_CONTACT_1___ADDRESS, "v_pledges_contact1_address");
		    put(ColumnConstants.PLEDGE_CONTACT_1___ALTERNATE_CONTACT, "v_pledges_contact1_alternate");
		    put(ColumnConstants.PLEDGE_CONTACT_1___EMAIL, "v_pledges_contact1_email");
	        put(ColumnConstants.PLEDGE_CONTACT_1___MINISTRY, "v_pledges_contact1_ministry");
		    put(ColumnConstants.PLEDGE_CONTACT_1___NAME, "v_pledges_contact1_name");
		    put(ColumnConstants.PLEDGE_CONTACT_1___TELEPHONE, "v_pledges_contact1_telephone");
		    put(ColumnConstants.PLEDGE_CONTACT_1___TITLE, "v_pledges_contact1_title");
		}};
		for(Entry<String, String> entry: pledgeContacts.entrySet()) {
		    no_entity(entry.getKey(), entry.getValue());
		    no_entity(entry.getKey().replace("1", "2"), entry.getValue().replace("1", "2"));
		}
		no_entity(ColumnConstants.PLEDGES_DETAIL_DATE_RANGE, "v_pledges_funding_range_date");
		no_entity(ColumnConstants.PLEDGES_DETAIL_START_DATE, "v_pledges_funding_start_date");
		no_entity(ColumnConstants.PLEDGES_DETAIL_END_DATE, "v_pledges_funding_end_date");
	}
	/**
	 * Adds pseudocomputed columns -- calculations are done in SQL, but the data isn't extracted from a table directly
	 */
	protected void addPseudoComputedColumns() {
		no_entity(ColumnConstants.AGE_OF_PROJECT_MONTHS, "v_project_age");
		no_entity(ColumnConstants.CALCULATED_PROJECT_LIFE, "v_calculated_project_life");
		no_entity(ColumnConstants.OVERAGE, "v_overage");
		no_entity(ColumnConstants.OVERAGE_PROJECT, "v_project_overage");
		no_entity(ColumnConstants.PROJECT_AGE_RATIO, "v_project_age_ratio");
		no_entity(ColumnConstants.PROJECT_PERIOD, "v_project_period");
		//Project Implementation Delay uses a separate behaviour: '0' instead of '' for empty cells
		no_entity(ColumnConstants.PROJECT_IMPLEMENTATION_DELAY, "v_project_impl_delay", PidTextualTokenBehaviour.instance);
	}
	
	protected void addMtefColumns() {
		for(int mtefYear:DynamicColumnsUtil.getMtefYears()) {
			
			addColumn(new MtefColumn("MTEF " + mtefYear + "/" + (mtefYear + 1), mtefYear, 
					"MTEF", false, Optional.empty()).withGroup("Funding Information"));
			addColumn(new MtefColumn("Pipeline MTEF Projections " + mtefYear + "/" + (mtefYear + 1), mtefYear, 
					"Pipeline MTEF", false, Optional.of(CategoryConstants.MTEF_PROJECTION_PIPELINE)).withGroup("Funding Information"));
			addColumn(new MtefColumn("Projection MTEF Projections " + mtefYear + "/" + (mtefYear + 1), mtefYear, 
					"Projection MTEF", false, Optional.of(CategoryConstants.MTEF_PROJECTION_PROJECTION)).withGroup("Funding Information"));
			addColumn(new MtefColumn("Real MTEF " + mtefYear + "/" + (mtefYear + 1), mtefYear, 
					"Real MTEF", true, Optional.empty()).withGroup("Funding Information"));
		}
	}
	
	protected void addComputedLinearMeasures() {
		addDerivedMeasure(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS,
				AmpReportsScratchpad::getComputedMeasuresBlock, 
				(syb, cell) -> isBetween(cell.amount.getJulianDayCode(), syb.currentYearStartJulian, syb.previousMonthStartJulian - 1),
				TrivialMeasureBehaviour.getTotalsOnlyInstance());
		
		addDerivedMeasure(MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS,
				AmpReportsScratchpad::getComputedMeasuresBlock,
				(syb, cell) -> isBetween(cell.amount.getJulianDayCode(), syb.previousMonthStartJulian, syb.previousMonthEndJulian),
				TrivialMeasureBehaviour.getTotalsOnlyInstance());

		addDerivedMeasure(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS, MeasureConstants.PLANNED_DISBURSEMENTS,
				AmpReportsScratchpad::getComputedMeasuresBlock,
				(syb, cell) -> isBetween(cell.amount.getJulianDayCode(), syb.selectedYearStartJulian, syb.selectedYearEndJulian),
				TrivialMeasureBehaviour.getTotalsOnlyInstance());	
		
		addDerivedMeasure(MeasureConstants.CURRENT_MONTH_DISBURSEMENTS, MeasureConstants.ACTUAL_DISBURSEMENTS,
				AmpReportsScratchpad::getComputedMeasuresBlock,
				(syb, cell) -> isBetween(cell.amount.getJulianDayCode(), syb.selectedMonthStartJulian, syb.selectedMonthEndJulian),
				TrivialMeasureBehaviour.getTotalsOnlyInstance());	
		
		addDerivedMeasure(MeasureConstants.LAST_YEAR_OF_PLANNED_DISBURSEMENTS, MeasureConstants.PLANNED_DISBURSEMENTS,
				AmpReportsScratchpad::getComputedMeasuresBlock,
				(syb, cell) -> isBetween(cell.amount.getJulianDayCode(), syb.previousYearStartJulian, syb.previousYearEndJulian),
				TrivialMeasureBehaviour.getTotalsOnlyInstance());

		addTrivialFilterMeasure(MeasureConstants.UNCOMMITTED_BALANCE,
				TrivialMeasureBehaviour.getTotalsOnlyInstance(),
				MeasureConstants.PROPOSED_PROJECT_AMOUNT_PER_PROJECT, +1,
				MeasureConstants.ACTUAL_COMMITMENTS, -1);

		addTrivialFilterMeasure(MeasureConstants.UNDISBURSED_BALANCE,
				TrivialMeasureBehaviour.getTotalsOnlyInstance(),
				MeasureConstants.ACTUAL_COMMITMENTS, +1,
				MeasureConstants.ACTUAL_DISBURSEMENTS, -1);
		
		addTrivialFilterMeasure(MeasureConstants.PLEDGES_COMMITMENT_GAP,
			TrivialMeasureBehaviour.getInstance(),
			MeasureConstants.ACTUAL_COMMITMENTS, -1,
			MeasureConstants.PLEDGES_ACTUAL_PLEDGE, +1);
		
		addTrivialFilterMeasure(MeasureConstants.UNCOMMITTED_CUMULATIVE_BALANCE, 
				TrivialMeasureBehaviour.getTotalsOnlyInstance(),
				MeasureConstants.PROPOSED_PROJECT_AMOUNT_PER_PROJECT, +1,
				MeasureConstants.CUMULATIVE_COMMITMENT, -1);
		
		addTrivialFilterMeasure(MeasureConstants.UNDISBURSED_CUMULATIVE_BALANCE,
				TrivialMeasureBehaviour.getTotalsOnlyInstance(),
				MeasureConstants.CUMULATIVE_COMMITMENT, +1,
				MeasureConstants.CUMULATIVE_DISBURSEMENT, -1);
				
		addMultipliedFilterTransactionMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS_CAPITAL, MeasureConstants.ACTUAL_DISBURSEMENTS, AmpFundingColumn::getCapitalMultiplier);
		addMultipliedFilterTransactionMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS_RECURRENT, MeasureConstants.ACTUAL_DISBURSEMENTS, AmpFundingColumn::getRecurrentMultiplier);
		addMultipliedFilterTransactionMeasure(MeasureConstants.PLANNED_DISBURSEMENTS_CAPITAL, MeasureConstants.PLANNED_DISBURSEMENTS, AmpFundingColumn::getCapitalMultiplier);
		addMultipliedFilterTransactionMeasure(MeasureConstants.PLANNED_DISBURSEMENTS_EXPENDITURE, MeasureConstants.PLANNED_DISBURSEMENTS, AmpFundingColumn::getRecurrentMultiplier);				
		
	}
	
	protected void addMultipliedFilterTransactionMeasure(String measureName, String baseMeasureName, Function<CategAmountCell, BigDecimal> fMultCalculator) {
		NiTransactionMeasure baseMeasure = (NiTransactionMeasure) getMeasures().get(baseMeasureName);
		NiUtils.failIf(baseMeasure == null, () -> String.format("you are trying to define measure %s based on nonexistant base measure %s", measureName, baseMeasureName));
		addMeasure(new NiMultipliedFilterTransactionMeasure(measureName, cell -> baseMeasure.criterion.test(cell) ? fMultCalculator.apply(cell) : null, measureDescriptions.get(measureName)));
	}
	
	/**
	 * adds a computed-filtering measure which is derived from an another measure by the means of supplementarily filtering by a given function
	 * @param measureName the measure you are creating
	 * @param baseMeasureName the measure based on which you are creating
	 * @param contextBuilder the per-fetch context to build (precalculated info for the supplemental filterer)
	 * @param extraFilter the per-cell filterer
	 * @param behaviour the behaviour of the column
	 */
	protected<K> void addDerivedMeasure(String measureName, String baseMeasureName, 
			Function<NiReportsEngine, K> contextBuilder, 
			BiFunction<K, CategAmountCell, Boolean> extraFilter,
			Behaviour<?> behaviour) {
		
		NiTransactionMeasure baseMeasure = (NiTransactionMeasure) getMeasures().get(baseMeasureName);
		NiUtils.failIf(baseMeasure == null, String.format("you are trying to define measure %s based on nonexistant base measure %s", measureName, baseMeasureName));
		NiUtils.failIf(extraFilter == null, String.format("you are trying to define measure %s based on null extraFilter", measureName));
		
		BiFunction<K, CategAmountCell, Boolean> crit = (context, cell) -> baseMeasure.criterion.test(cell) && extraFilter.apply(context, cell);
		
		addMeasure(new NiTransactionContextMeasure<K>(measureName, contextBuilder, crit, behaviour, measureDescriptions.get(measureName)));
	}
	
	protected void addTrivialFilterMeasure(String measureName, Behaviour<?> behaviour,  Object...def) {
		addLinearFilterMeasure(measureName, measureDescriptions.get(measureName), behaviour, def);
	}
	
	public Set<String> migrateColumns() {
		return PersistenceManager.getSession().doReturningWork(conn -> {
			Map<Long, String> dbColumns = SQLUtils.collectKeyValue(conn, "SELECT columnid, columnname FROM amp_columns");
			Set<String> columnNamestoBeMigrated = dbColumns.values().stream()
					.filter(z -> this.measures.containsKey(z) && !this.columns.containsKey(z)).collect(Collectors.toSet());
			if (columnNamestoBeMigrated.size() > 0) {
				List<String> columnNames = Arrays.asList("amp_report_id", "measureid", "order_id");
				String columnNamesJoined = SQLUtils.generateCSV(columnNamestoBeMigrated.stream().map(z -> String.format("'%s'", z)).collect(Collectors.toList()));
				String query = String.format("SELECT arc.amp_report_id, am.measureid, counts.order_id FROM amp_report_column arc " +
						"JOIN amp_columns ac ON arc.columnid = ac.columnid JOIN amp_measures am ON am.measurename = ac.columnname "
						+ "JOIN (SELECT amp_report_id, max(order_id) + 1 AS order_id FROM amp_report_measures  GROUP BY amp_report_id ) "
						+ "AS counts ON counts.amp_report_id = arc.amp_report_id "
						+ "WHERE ac.columnname IN (%s)"
						+ "AND arc.amp_report_id NOT IN ( " 
						+ "SELECT amp_report_id FROM amp_report_measures arm "
						+ "JOIN amp_measures am2 ON arm.measureid = am2.measureid "
						+ "WHERE measurename IN (%s))", columnNamesJoined, columnNamesJoined);
				List<List<Object>> reportEntriesMigrated = SQLUtils.collect(conn, query, rs -> AmpCollections.relist(columnNames, colName -> SQLUtils.getLong(rs, colName)));
				SQLUtils.insert(conn, "amp_report_measures", null, null, columnNames, reportEntriesMigrated);
				String columnIdsJoined = SQLUtils.generateCSV(SQLUtils.fetchLongs(conn, String.format("SELECT columnid FROM amp_columns WHERE columnname IN (%s)", columnNamesJoined)));
				SQLUtils.executeQuery(conn, String.format("DELETE FROM amp_report_column WHERE columnid IN (%s)", columnIdsJoined));
				SQLUtils.executeQuery(conn, String.format("DELETE FROM amp_report_hierarchy  WHERE columnid IN (%s)", columnIdsJoined));
				SQLUtils.executeQuery(conn, String.format("DELETE FROM amp_columns WHERE columnid IN (%s)", columnIdsJoined));
			}
			return columnNamestoBeMigrated;
		});
	}
	
	
	
	/**
	 * This method is created for the following scenario:
	 * 		- a column was added to AmpReportsSchema
	 * 		- this column doesn't exist in the old reports schema (described by AmpColumns)
	 * 	In this scenario, opening a report with said 
	 *  new column in the old reports engine would probably result in a crash.
	 *  To avoid this, an entry referring to an empty view is added -- so that the column is shown having no data
	 *  (since there's no point in backporting the column entirely). 
	 */
	@SuppressWarnings("deprecation")
	public Set<String> synchronizeAmpColumnsBackport(ServletContext sCtx) {
		return PersistenceManager.getSession().doReturningWork(conn -> {
			Set<String> inDbColumns = new HashSet<>(SQLUtils.fetchAsList(conn, String.format("SELECT %s FROM %s", "columnname", "amp_columns"), 1));
			Set<String> toBeAdded = this.columns.keySet().stream().filter(z -> !inDbColumns.contains(z)).collect(Collectors.toSet());
			for (String newColumnName : toBeAdded) {
				AmpColumns col= new AmpColumns();
				col.setColumnName(newColumnName);
				col.setExtractorView("v_empty_text_column");
				col.setCellType("org.dgfoundation.amp.ar.cell.TextCell");
				String group = null;
				if (this.columns.get(newColumnName) instanceof PsqlSourcedColumn)
					group = ((PsqlSourcedColumn<?>)this.columns.get(newColumnName)).getGroup();
				//group should be non-null only for MTEF columns
				DynamicColumnsUtil.dynamicallyCreateNewColumn(col, group, sCtx);
			}
			return toBeAdded;
		});

//		PersistenceManager.getSession().doWork(conn -> {
//			Set<String> inDbColumns = new HashSet<>(SQLUtils.fetchAsList(conn, String.format("SELECT %s FROM %s", "columnname", "amp_columns"), 1));
//			List<String> toBeAdded = this.columns.keySet().stream().filter(z -> !inDbColumns.contains(z)).collect(Collectors.toList());
//			List<List<Object>> values = toBeAdded.stream().map(z -> Arrays.asList(z, "org.dgfoundation.amp.ar.cell.TextCell", "v_empty_text_column")).collect(Collectors.toList());	
//			if (values.size() > 0)
//				SQLUtils.insert(conn, "amp_columns", "columnid", "amp_columns_seq", Arrays.asList("columnname", "celltype", "extractorview"), values);
//		}); 	
	}
	
	/**
	 * This method is created for the following scenario:
	 * 		- a measure was added to AmpReportsSchema
	 * 		- this measure doesn't exist in the old reports schema (described by AmpMeasures)
	 * 	In this scenario, opening a report with said 
	 *  new measure in the old reports engine would probably result in a crash.
	 *  Therefore, an empty row for said measure is added.
	 */
	public Set<String> synchronizeAmpMeasureBackport() {
		return PersistenceManager.getSession().doReturningWork(conn -> {
			Set<String> inDbMeasures = new HashSet<>(SQLUtils.fetchAsList(conn, String.format("SELECT %s FROM %s", "measurename", "amp_measures"), 1));
			Set<Object> toBeAdded = this.measures.keySet().stream().filter(z -> !inDbMeasures.contains(z)).collect(Collectors.toSet());
			List<List<Object>> values = toBeAdded.stream().map(z -> Arrays.asList(z, z, "A")).collect(Collectors.toList());	
			if (values.size() > 0) {
				SQLUtils.insert(conn, "amp_measures", "measureid", "amp_measures_seq", Arrays.asList("measurename", "aliasname", "type"), values);
				MeasuresVisibility.resetMeasuresList();
			}
			return toBeAdded.stream().map(z -> z.toString()).collect(Collectors.toSet());
		}); 
	}
	
	private AmpReportsSchema addUnfilteredTrivialMeasures() {
		addMeasure(new AmpTrivialMeasure(MeasureConstants.CUMULATIVE_DISBURSEMENT, Constants.DISBURSEMENT, "Actual", false, true, TrivialMeasureBehaviour.getTotalsOnlyInstance()));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.CUMULATIVE_COMMITMENT, Constants.COMMITMENT, "Actual", false, true, TrivialMeasureBehaviour.getTotalsOnlyInstance()));
		return this;
	}
	
	private AmpReportsSchema addTrivialMeasures() {
		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_COMMITMENTS, Constants.COMMITMENT, "Actual", false, cac -> cac.activityId > MondrianETL.PLEDGE_ID_ADDER));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_COMMITMENTS, Constants.COMMITMENT, "Planned", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PIPELINE_COMMITMENTS, Constants.COMMITMENT, "Pipeline", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_DISBURSEMENTS, Constants.DISBURSEMENT, "Planned", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_EXPENDITURES, Constants.EXPENDITURE, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_EXPENDITURES, Constants.EXPENDITURE, "Planned", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_DISBURSEMENT_ORDERS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_DISBURSEMENT_ORDERS, Constants.DISBURSEMENT, "Planned", false));
		
		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_ESTIMATED_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_ESTIMATED_DISBURSEMENTS, Constants.DISBURSEMENT, "Planned", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_RELEASE_OF_FUNDS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_RELEASE_OF_FUNDS, Constants.DISBURSEMENT, "Planned", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_ARREARS, Constants.ARREARS, "Planned", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_ARREARS, Constants.ARREARS, "Actual", false));
		
		
		//addMeasure(new AmpTrivialMeasure(MeasureConstants.OFFICIAL_DEVELOPMENT_AID_COMMITMENTS, Constants.COMMITMENT, "Actual", false));

		//addMeasure(new AmpTrivialMeasure(MeasureConstants.BILATERAL_SSC_COMMITMENTS, Constants.COMMITMENT, "Actual", false));
		//addMeasure(new AmpTrivialMeasure(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS, Constants.COMMITMENT, "Actual", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ANNUAL_PROPOSED_PROJECT_COST, Constants.ANNUAL_PROPOSED_PROJECT_COST));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PROPOSED_PROJECT_AMOUNT_PER_PROJECT, Constants.PROPOSED_PROJECT_AMOUNT_PER_PROJECT));
		//addMeasure(new AmpTrivialMeasure(MeasureConstants.PROJECTION_MTEF_PROJECTIONS, Constants.COMMITMENT, "Actual", false));
		
//		addMeasure(new AmpTrivialMeasure(MeasureConstants.PIPELINE_MTEF_PROJECTIONS, Constants.PIPELINE, "Pipeline", false));
//		addMeasure(new AmpTrivialMeasure(MeasureConstants.PIPELINE_ESTIMATED_DISBURSEMENTS, Constants.PIPELINE, "Pipeline", false));
//		addMeasure(new AmpTrivialMeasure(MeasureConstants.PIPELINE_RELEASE_OF_FUNDS, Constants.PIPELINE, "Pipeline", false));
		
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLEDGES_ACTUAL_PLEDGE, Constants.PLEDGE));
		
		return this;
	}
	
	private AmpReportsSchema addTaggedMeasures() {
		addMeasure(new TaggedMeasure(MeasureConstants.ACTUAL_CLASSIFIED_EXPENDITURES, Constants.EXPENDITURE, "Actual", MetaCategory.EXPENDITURE_CLASS, PSEUDOCOLUMN_EXP_CLASS));
		addMeasure(new TaggedMeasure(MeasureConstants.PLANNED_CLASSIFIED_EXPENDITURES, Constants.EXPENDITURE, "Planned", MetaCategory.EXPENDITURE_CLASS, PSEUDOCOLUMN_EXP_CLASS));
		return this;
	}
	
	private AmpReportsSchema addFundingFlowMeasures() {
		addMeasure(new AmpTrivialMeasure(MeasureConstants.REAL_PLANNED_DISBURSEMENTS, Constants.DISBURSEMENT, "Planned", true));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.REAL_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", true));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.REAL_COMMITMENTS, Constants.COMMITMENT, "Actual", true));
		return this;
	}
	
	protected void addSscMeasures() {
		Collection<AmpCategoryValue> sscAdjTypes = CategoryManagerUtil.getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.SSC_ADJUSTMENT_TYPE_KEY);
		 
		for (Entry<String, Integer> pair : ArConstants.SSC_TRANSACTION_TYPE_NAME_TO_ID.entrySet()) {
			String transactionType = pair.getKey();
			Integer trTypeId = pair.getValue();
			for (AmpCategoryValue adj : sscAdjTypes) {
				String measureName = adj.getValue() + " " + transactionType;
				addMeasure(new AmpTrivialMeasure(measureName, trTypeId, adj.getValue(), false));
			}
		}
		
		if (measures.containsKey(MeasureConstants.BILATERAL_SSC_COMMITMENTS) && measures.containsKey(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS)) {
			addTrivialFilterMeasure(MeasureConstants.CUMULATED_SSC_COMMITMENTS,
					TrivialMeasureBehaviour.getInstance(),
					MeasureConstants.BILATERAL_SSC_COMMITMENTS, +1,
					MeasureConstants.TRIANGULAR_SSC_COMMITMENTS, +1);
		};
	}
	
	private AmpReportsSchema no_dimension(String columnName, String view) {
		return single_dimension(columnName, view, (LevelColumn) null);
	}

	/**
	 * adds a single-dimension column belonging to a degenerate dimension. The dimension must be whitelisted!
	 * @param columnName
	 * @param view
	 * @param dimension
	 * @return
	 */
	private AmpReportsSchema degenerate_dimension(String columnName, String view, NiDimension dimension) {
		NiUtils.failIf(dimension != catsDimension && dimension != agreementsDimension && dimension != boolDimension, String.format(dimension.toString() + " is not whitelisted as a shortcut degenerate dimension"));
		return single_dimension(columnName, view, dimension.getLevelColumn(columnName, dimension.depth - 1)); // taking the leaves
	}
	
	private AmpReportsSchema single_dimension(String columnName, String view, LevelColumn levelColumn) {
		return addColumn(SimpleTextColumn.fromView(columnName, view, levelColumn));
	}

	private AmpReportsSchema single_dimension(String columnName, String view, LevelColumn levelColumn, Function<String, String> postprocessor) {
		return addColumn(SimpleTextColumn.fromView(columnName, view, levelColumn).withPostprocessor(postprocessor));
	}
	
	private AmpReportsSchema no_entity(String columnName, String view, Behaviour<NiTextCell> behaviour) {
		return addColumn(SimpleTextColumn.fromViewWithoutEntity(columnName, view, behaviour));
	}
	
	
	private AmpReportsSchema no_entity(String columnName, String view) {
		return addColumn(SimpleTextColumn.fromViewWithoutEntity(columnName, view));
	}
	
	private AmpReportsSchema no_entity(String columnName, String view, Function<String, String> postprocessor) {
		return addColumn(SimpleTextColumn.fromViewWithoutEntity(columnName, view).withPostprocessor(postprocessor));
	}

	
	private AmpReportsSchema with_percentage(String columnName, String viewName, NiDimensionUsage dimUsg, int level) {
		Map<NiDimensionUsage, PercentagesCorrector> correctors = 
			PsqlSourcedColumn.keyColumnName(viewName, "amp_activity_id").equals("amp_activity_id") ? PERCENTAGE_CORRECTORS : PLEDGES_PERCENTAGE_CORRECTORS;
		return with_percentage(columnName, viewName, dimUsg, level, correctors);
	}

	private AmpReportsSchema with_percentage(String columnName, String viewName, NiDimensionUsage dimUsg, int level, Map<NiDimensionUsage, PercentagesCorrector> correctors) {
		PercentagesCorrector cor = correctors.get(dimUsg);
		NiUtils.failIf(cor == null, String.format("%s: you forgot to configure percentage correctors for %s", columnName, dimUsg));
		return addColumn(new NormalizedPercentagesColumn(columnName, dimUsg.getLevelColumn(level), null, viewName, cor));
	}
			
	/**
	 * adds an entityLess date field
	 * @param columnName
	 * @param viewName
	 * @return
	 */
	private AmpReportsSchema date_column(String columnName, String viewName) {
		return addColumn(new DateColumn(columnName, viewName));
	}
	
	/**
	 * adds a date field ties to a degenerate dimension
	 * @param columnName
	 * @param viewName
	 * @param dim
	 * @return
	 */
	private AmpReportsSchema date_column(String columnName, String viewName, LevelColumn levelColumn) {
		return addColumn(new DateColumn(columnName, levelColumn, viewName));
	}
	
	protected final CurrencyConvertor currencyConvertor = AmpCurrencyConvertor.getInstance();
	
	@Override
	public AmpFundingColumn getFundingFetcher(NiReportsEngine engine) {
		switch(engine.spec.getReportType()) {
			
			case ArConstants.DONOR_TYPE: 
				return donorFundingColumn;
				
			case ArConstants.PLEDGES_TYPE:
				return pledgeFundingColumn;
				
			case ArConstants.COMPONENT_TYPE:
				return componentFundingColumn;
				
			default:
				throw new RuntimeException(String.format("report type %d not implemented in NiReports yet", engine.spec.getReportType()));
		}
	}

	@Override
	public NiFilters convertFilters(NiReportsEngine engine) {
		return new AmpFiltersConverter(engine).buildNiFilters(engine.spec.getReportType() == ArConstants.PLEDGES_TYPE ? this::getWorkspacePledges : this::getWorkspaceActivities);
	}

	public Set<Long> getWorkspacePledges(NiReportsEngine engine) {
		//return new HashSet<>(SQLUtils.fetchLongs(AmpReportsScratchpad.get(engine).connection, "SELECT id FROM amp_funding_pledges"));
		return AmpReportsScratchpad.get(engine).computedPledgeIds.get(); //TODO: remove this comment!!!
		//return new HashSet<>(Arrays.asList(4l));
	}
	
	/**
	 * returns the set of activities selected by the workspace filter. Any filtering specified in the ReportSpec will return a subset of these ids
	 * @param engine
	 * @return
	 */
	public Set<Long> getWorkspaceActivities(NiReportsEngine engine) {
		AmpReportsScratchpad pad = AmpReportsScratchpad.get(engine);
		if (engine.spec.isAlsoShowPledges())
			return AmpCollections.union(_getWorkspaceActivities(engine), getWorkspacePledges(engine).stream().map(z -> z + MondrianETL.PLEDGE_ID_ADDER).collect(Collectors.toSet()));
		else if (engine.spec.getReportType() == ArConstants.COMPONENT_TYPE) {
			Set<Long> wf = new HashSet<>(_getWorkspaceActivities(engine));
			wf.retainAll(SQLUtils.fetchLongs(pad.connection, "SELECT DISTINCT amp_activity_id FROM amp_activity_components"));
			return Collections.unmodifiableSet(wf);
		}
		else
			return Collections.unmodifiableSet(_getWorkspaceActivities(engine));
	}

	/**
	 * uses the WorkspaceFilter to fetch the names of the activities. Overrideable for testcases reasons in subclass
	 * @param engine
	 * @return
	 */
	protected Set<Long> _getWorkspaceActivities(NiReportsEngine engine) {
		return AmpReportsScratchpad.get(engine).environment.workspaceFilter.getIds();
	}
		
	@Override
	public Function<NiReportsEngine, SchemaSpecificScratchpad> getScratchpadSupplier() {
		return engine -> new AmpReportsScratchpad(engine);
	}
	
	public static ReportExecutor getExecutor() {
		return getExecutor(true);
	}
	
	/**
	 * users entrypoint for running reports using NiReports
	 * @return
	 */
	public static ReportExecutor getExecutor(boolean logToDb) {
		ReportExecutor res = new NiReportsGenerator(getInstance(), logToDb, null);
		return res;
	}
	
	public static String getRenderedReport(ReportSpecification spec) {
		NiReportsGenerator niGen = new NiReportsGenerator(getInstance());
		return niGen.renderReport(spec);
	}

	/**
	 * initializes the NiReports subsystem. <br />
	 * Notice that this is not strictly necessary for being able to run NiReports, but it runs many self-checks as part of running a very simple report and cached some frequently-used data
	 * @throws AMPException
	 */
	public static void init() throws AMPException {
		AmpReportsSchema.getExecutor(false).executeReport(ReportSpecificationImpl.buildFor("self-test report", 
			Arrays.asList(ColumnConstants.PROJECT_TITLE), 
			Arrays.asList(MeasureConstants.ACTUAL_COMMITMENTS), 
			null,
			GroupingCriteria.GROUPING_YEARLY));
		
		// test dimensions: make a snapshot of each
		for(NiDimension dimension:Arrays.asList(orgsDimension, locsDimension, secsDimension, progsDimension)) {
			dimension.getDimensionData().toString();
		}
	}
	
	// ========== implementation code below ==========
	@Override public AmpReportsSchema addColumn(NiReportColumn<?> col) {
		if (TRANSACTION_LEVEL_HIERARCHIES.contains(col.name))
			col = col.setTransactionLevelHierarchy();
		return (AmpReportsSchema) super.addColumn(col);
	}
	
	/**
	 * also checks the percentages on the system
	 */
	@Override
	public Map<String, List<ReportRenderWarning>> performColumnChecks(Optional<Set<String>> columns) {
		Map<String, List<ReportRenderWarning>> sp = new HashMap<>(super.performColumnChecks(columns));
		try(java.sql.Connection conn = PersistenceManager.getJdbcConnection()) {
			for(Map.Entry<NiDimensionUsage, PercentagesCorrector> z:PERCENTAGE_CORRECTORS.entrySet()) {
				sp.put(z.getKey().toString(), z.getValue().validateDb(z.getKey().toString(), conn));
			}
			for(Map.Entry<NiDimensionUsage, PercentagesCorrector> z:PLEDGES_PERCENTAGE_CORRECTORS.entrySet()) {
				sp.put("Pledges " + z.getKey().toString(), z.getValue().validateDb(z.getKey().toString(), conn));
			}
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
		return sp;
	};
	
	@SuppressWarnings("unchecked")
	@Override
	public<K extends Cell> List<K> fetchEntity(NiReportsEngine engine, NiReportedEntity<K> entity) throws Exception {
		List<K> origResult = entity.fetch(engine);
		
		String pledgeCompanion = AmpFiltersConverter.DONOR_COLUMNS_TO_PLEDGE_COLUMNS.get(entity.name);
		
		if (engine.spec.getReportType() == ArConstants.DONOR_TYPE && engine.spec.isAlsoShowPledges() && entity instanceof NiReportColumn) {
			if (pledgeCompanion != null) {
				List<K> pledgesResult = new ArrayList<>();
				engine.timer.run("pledgeCompanion: " + pledgeCompanion, () -> {
					pledgesResult.addAll((List<K>) columns.get(pledgeCompanion).fetch(engine));
				});
				List<K> pledgeCellsAsDonorCells = AmpCollections.relist(pledgesResult, this::pledgeCellToDonorCell);
				return AmpCollections.mergeLists(origResult, pledgeCellsAsDonorCells);
			}
			
			if (entity.name.equals(AmpFundingColumn.ENTITY_DONOR_FUNDING)) {
				List<CategAmountCell> pledgeFunding = new ArrayList<>();
				engine.timer.run("pledge funding", () -> {
					pledgeFunding.addAll(pledgeFundingColumn.fetch(engine));
				});
				
				NiLinearCombinationTransactionMeasure commitmentGap = (NiLinearCombinationTransactionMeasure) getMeasures().get(MeasureConstants.PLEDGES_COMMITMENT_GAP);
				List<CategAmountCell> commitmentGapCells = commitmentGap.fetch(pledgeFunding);
				List<K> commitmentGapAsActualCommitments = (List<K>) AmpCollections.relist(commitmentGapCells, this::pledgeCellToDonorCell);
				return AmpCollections.mergeLists(origResult, commitmentGapAsActualCommitments);
			}
		};
		return origResult;
	}
		
	@SuppressWarnings("unchecked")
	protected<K extends Cell> K pledgeCellToDonorCell(K pledgeCell) {
		return (K) pledgeCell.changeOwnerId(pledgeCell.activityId + MondrianETL.PLEDGE_ID_ADDER);
	}
	
	/**
	 * returns true iff a given column is defined and denoting a boolean column
	 * @param columnName
	 * @return
	 */
	public boolean isBooleanColumn(String columnName) {
		NiReportColumn<?> col = getColumns().get(columnName);
		return col != null && col.levelColumn.isPresent() && col.levelColumn.get().dimensionUsage.dimension == boolDimension;
	}

	@Override
	public boolean isTransactionLevelHierarchy(NiReportColumn<?> col, NiReportsEngine engine) {
		//return col.isTransactionLevelHierarchy();
		AmpFundingColumn funding = this.getFundingFetcher(engine);
		return funding.isTransactionLevelHierarchy(col);
		//return super.isTransactionLevelHierarchy(col, engine);
	}
	
	/**
	 * used for filters (to retrieve names for summary sheet in excel report)
	 */
	public enum NamedElemType {SECTOR, LOCATION, PROGRAM, ORGANISATION, ORG_GROUP, ORG_TYPE, ACV, DATE, UNKNOWN}
	
	public NamedElemType getNamedElemType(String colName) {
		NiReportColumn<?> niCol = getColumns().get(colName);
		if (niCol == null)
			return NamedElemType.UNKNOWN;
		
		if (niCol instanceof DateColumn)
			return NamedElemType.DATE;
	
		if (!niCol.levelColumn.isPresent())
			return NamedElemType.UNKNOWN;
		
		LevelColumn lc = niCol.levelColumn.get();
		switch(lc.dimensionUsage.dimension.name) {
			case "sectors":
				return NamedElemType.SECTOR;
				
			case "progs":
				return NamedElemType.PROGRAM;
				
			case "locs":
				return NamedElemType.LOCATION;
				
			case "orgs": {
				switch(lc.level) {
					case 0:
						return NamedElemType.ORG_TYPE;
					case 1:
						return NamedElemType.ORG_GROUP;
					case 2:
						return NamedElemType.ORGANISATION;
					default:
						return NamedElemType.UNKNOWN;
				}
			}
			case "cats":
				return NamedElemType.ACV;
				
			default:
				return NamedElemType.UNKNOWN;
		}
	}
	
	public final static boolean isBetween(long value, long min, long max) {
		return value >= min && value <= max;
	}
}
