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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.GroupingCriteria;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportExecutor;
import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.nireports.AbstractReportsSchema;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.amp.dimensions.CategoriesDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.ProgramsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.SectorsDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.Constants;

import com.google.common.base.Function;

/**
 * the big, glorious, immaculate, AMP Reports schema
 * @author Dolghier Constantin
 *
 */
public class AmpReportsSchema extends AbstractReportsSchema {

	public static final Logger logger = Logger.getLogger(AmpReportsSchema.class);
	
	public final static OrganisationsDimension orgsDimension = OrganisationsDimension.instance;
	public final static LocationsDimension locsDimension = LocationsDimension.instance;
	public final static SectorsDimension secsDimension = SectorsDimension.instance;
	public final static ProgramsDimension progsDimension = ProgramsDimension.instance;
	public final static CategoriesDimension catsDimension = CategoriesDimension.instance;
	

	@SuppressWarnings("serial")
	public final static Map<String, String> columnDescriptions = new HashMap<String, String>() {{
		put(ColumnConstants.CUMULATIVE_COMMITMENT,  "Sum of all ACTUAL COMMITMENTS independent of filters");
		put(ColumnConstants.CUMULATIVE_DISBURSEMENT,  "Sum of all ACTUAL DISBURSEMENTS independent of filters");
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
		put(ColumnConstants.UNCOMMITTED_CUMULATIVE_BALANCE,  "Proposed project cost - Cummalative Commitments");
		put(ColumnConstants.UNDISBURSED_CUMULATIVE_BALANCE,  "Cumulative Commitment - Cumulative Disbursement");	
		
		
	}};
	@SuppressWarnings("serial")
	public final static Map<String, String> measureDescriptions = new HashMap<String, String>(){{


		put(MeasureConstants.CONSUMPTION_RATE , "(Selected Year Cumulated Disbursements / Selected Year of Planned Disbursements) * 100"); 
		put(MeasureConstants.CUMULATED_DISBURSEMENTS , "Prior Actual Disbursements + Previous Month Disbursements"); 
		put(MeasureConstants.DISBURSMENT_RATIO , "Sum of actual disbursment / Total actual disb * 100"); 
		put(MeasureConstants.PREVIOUS_MONTH_DISBURSEMENTS , "Actual Disbursements Of Previous Month");
		put(MeasureConstants.CURRENT_MONTH_DISBURSEMENTS , "Sum of Actual Disbursements of the current month");
		put(MeasureConstants.PRIOR_ACTUAL_DISBURSEMENTS , "Current Year Actual Disbursements Until Previous Month (not included)"); 
		put(MeasureConstants.SELECTED_YEAR_PLANNED_DISBURSEMENTS , "Selected Year Planned Disbursements");
		put(MeasureConstants.UNCOMMITTED_BALANCE , "Proposed project cost - Cumulative commitments"); 
		put(MeasureConstants.UNDISBURSED_BALANCE , "Cumulative Commitment - Cumulative Disbursement");
		put(MeasureConstants.PERCENTAGE_OF_TOTAL_COMMITMENTS , "Actual commitments for the project / Total actual commitments * 100");
		put(MeasureConstants.LAST_YEAR_OF_PLANNED_DISBURSEMENTS , "Previous Year Planned Disbursements");
		put(MeasureConstants.PERCENTAGE_OF_DISBURSEMENT , "(Total Actual Disbursements for Year,Quarter,Month / Total Actual Disbursements) * 100");
		put(MeasureConstants.PLEDGES_COMMITMENT_GAP , "Total Pledge - Total Actual Commitments");
		put(MeasureConstants.EXECUTION_RATE , "Sum Of Actual Disb (Dependent on Filter) / Sum Of Planned Disb (Dependent on Filter) * 100");
//		put(MeasureConstants.FORECAST_EXECUTION_RATE , "Actual Disbursements / (Most recent of (Pipeline MTEF for the year, Projection MTEF for the year)). "
//					+ "Measure only makes sense in Annual and Totals-only reports");
		
	}};
	
	/**
	 * the name of the "Donor" instance of the Organisation Dimension
	 */
	public final static NiDimensionUsage DONOR_DIM_USG = orgsDimension.getDimensionUsage("DN");
	public final static NiDimensionUsage IA_DIM_USG = orgsDimension.getDimensionUsage("IA");
	public final static NiDimensionUsage BA_DIM_USG = orgsDimension.getDimensionUsage("BA");
	public final static NiDimensionUsage EA_DIM_USG = orgsDimension.getDimensionUsage("EA");
	public final static NiDimensionUsage RO_DIM_USG = orgsDimension.getDimensionUsage("RO");
	public final static NiDimensionUsage CA_DIM_USG = orgsDimension.getDimensionUsage("CA");
	public final static NiDimensionUsage RG_DIM_USG = orgsDimension.getDimensionUsage("RG");
	public final static NiDimensionUsage SG_DIM_USG = orgsDimension.getDimensionUsage("SG");	
	
	public final static NiDimensionUsage PS_DIM_USG = secsDimension.getDimensionUsage("Primary");
	public final static NiDimensionUsage SS_DIM_USG = secsDimension.getDimensionUsage("Secondary");	
	public final static NiDimensionUsage TS_DIM_USG = secsDimension.getDimensionUsage("Tertiary");	
	
	public final static NiDimensionUsage PP_DIM_USG = progsDimension.getDimensionUsage("Primary Program");
	public final static NiDimensionUsage SP_DIM_USG = progsDimension.getDimensionUsage("Secondary Program");	
	public final static NiDimensionUsage TP_DIM_USG = progsDimension.getDimensionUsage("Tertiary Program");
	public final static NiDimensionUsage NPO_DIM_USG = progsDimension.getDimensionUsage("National Plan Objective");

	public final static NiDimensionUsage LOC_DIM_USG = locsDimension.getDimensionUsage("LOCS");
	
	private static AmpReportsSchema instance = new AmpReportsSchema();
		
	public final Map<NiDimensionUsage, PercentagesCorrector> PERCENTAGE_CORRECTORS = new HashMap<NiDimensionUsage, PercentagesCorrector>() {{
		putAll(orgsDimension.getAllPercentagesCorrectors());
		putAll(secsDimension.getAllPercentagesCorrectors());
		putAll(progsDimension.getAllPercentagesCorrectors());
		putAll(locsDimension.getAllPercentagesCorrectors());
		putAll(locsDimension.getAllPercentagesCorrectors());
	}};
	
	public static AmpReportsSchema getInstance() {
		return instance;
	}
	
	protected AmpReportsSchema() {
		no_dimension(ColumnConstants.PROJECT_TITLE, "v_titles");
		no_dimension(ColumnConstants.TEAM, "v_teams");
		no_dimension(ColumnConstants.OBJECTIVE, "v_objectives");
		no_dimension(ColumnConstants.ISSUES, "v_issues");
		
		no_dimension(ColumnConstants.ACTIVITY_PLEDGES_TITLE, "v_activity_pledges_title");
		no_dimension(ColumnConstants.ACTIVITY_UPDATED_BY, "v_activity_modified_by");
		no_dimension(ColumnConstants.ACTORS, "v_actors");
		no_dimension(ColumnConstants.AGREEMENT_CODE, "v_agreement_code");
		no_dimension(ColumnConstants.AGREEMENT_TITLE_CODE, "v_agreement_title_code");
		no_dimension(ColumnConstants.AMP_ID, "v_amp_id");
		no_dimension(ColumnConstants.BUDGET_ORGANIZATION, "v_budget_organization");
		no_dimension(ColumnConstants.BUDGET_PROGRAM, "v_budget_program");
		no_dimension(ColumnConstants.BUDGET_STRUCTURE, "v_budget_structure");
		no_dimension(ColumnConstants.COMPONENT_DESCRIPTION, "v_component_description");
		no_dimension(ColumnConstants.COMPONENT_FUNDING_ORGANIZATION, "v_component_funding_organization_name");
		no_dimension(ColumnConstants.COMPONENT_NAME, "v_components");
		no_dimension(ColumnConstants.COMPONENT_TYPE, "v_component_type");
		no_dimension(ColumnConstants.COSTING_DONOR, "v_costing_donors");
		no_dimension(ColumnConstants.CREDIT_DONATION, "v_credit_donation");
		no_dimension(ColumnConstants.DESCRIPTION_OF_COMPONENT_FUNDING, "v_component_funding_description");
		no_dimension(ColumnConstants.DISASTER_RESPONSE_MARKER, "v_disaster_response_marker");
		no_dimension(ColumnConstants.DONOR_CONTACT_ORGANIZATION, "v_donor_cont_org");
		no_dimension(ColumnConstants.ENVIRONMENT, "v_environment");
		no_dimension(ColumnConstants.EQUAL_OPPORTUNITY, "v_equalopportunity");
		degenerate_dimension(ColumnConstants.FINANCIAL_INSTRUMENT, "v_financial_instrument", catsDimension);
		degenerate_dimension(ColumnConstants.FINANCING_INSTRUMENT, "v_financing_instrument", catsDimension);
		degenerate_dimension(ColumnConstants.FUNDING_STATUS, "v_funding_status", catsDimension);
		no_dimension(ColumnConstants.HUMANITARIAN_AID, "v_humanitarian_aid");
		degenerate_dimension(ColumnConstants.IMPLEMENTATION_LEVEL, "v_implementation_level", catsDimension);
//		addTextColumn(ColumnConstants.IMPLEMENTING_AGENCY_DEPARTMENT_DIVISION, "v_implementing_agency_info");
		no_dimension(ColumnConstants.INDIRECT_ON_BUDGET, "v_indirect_on_budget");
		degenerate_dimension(ColumnConstants.INSTITUTIONS, "v_institutions", catsDimension);
		no_dimension(ColumnConstants.MEASURES_TAKEN, "v_measures_taken");
		no_dimension(ColumnConstants.MINORITIES, "v_minorities");
		degenerate_dimension(ColumnConstants.MODALITIES, "v_modalities", catsDimension);
		degenerate_dimension(ColumnConstants.MODE_OF_PAYMENT, "v_mode_of_payment", catsDimension);
		degenerate_dimension(ColumnConstants.ON_OFF_TREASURY_BUDGET, "v_on_off_budget", catsDimension);
		no_dimension(ColumnConstants.ORGANIZATIONS_AND_PROJECT_ID, "v_project_id");
		no_dimension(ColumnConstants.PAYMENT_CAPITAL___RECURRENT, "v_mode_of_payment_capital_recurrent");
		no_dimension(ColumnConstants.PHYSICAL_PROGRESS, "v_physical_progress");
		degenerate_dimension(ColumnConstants.PROCUREMENT_SYSTEM, "v_procurement_system", catsDimension);
		no_dimension(ColumnConstants.PROGRAM_DESCRIPTION, "v_program_description");
		degenerate_dimension(ColumnConstants.PROJECT_CATEGORY, "v_project_category", catsDimension);
		no_dimension(ColumnConstants.PROJECT_COMMENTS, "v_project_comments");
		no_dimension(ColumnConstants.PROJECT_DESCRIPTION, "v_description");
		no_dimension(ColumnConstants.PROJECT_IMPACT, "v_proj_impact");
		no_dimension(ColumnConstants.PROJECT_IMPLEMENTING_UNIT, "v_project_impl_unit");
		no_dimension(ColumnConstants.PURPOSE, "v_purposes");
		no_dimension(ColumnConstants.REGIONAL_OBSERVATIONS, "v_regional_observations");
		no_dimension(ColumnConstants.RELATED_PLEDGES, "v_related_pledges");
		no_dimension(ColumnConstants.RELATED_PROJECTS, "v_pledges_projects");
		no_dimension(ColumnConstants.RESULTS, "v_results");
//		addTextColumn(ColumnConstants.SECTOR_LOCATION, "v_sectorloc");
		no_dimension(ColumnConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION, "v_sect_min_cont_org");
		degenerate_dimension(ColumnConstants.SSC_MODALITIES, "v_ssc_modalities", catsDimension);
		degenerate_dimension(ColumnConstants.STATUS, "v_status", catsDimension);
		no_dimension(ColumnConstants.STRUCTURES_COLUMN, "v_structures");
		degenerate_dimension(ColumnConstants.TYPE_OF_ASSISTANCE, "v_terms_assist", catsDimension);
		degenerate_dimension(ColumnConstants.TYPE_OF_COOPERATION, "v_type_of_cooperation", catsDimension);
		degenerate_dimension(ColumnConstants.TYPE_OF_IMPLEMENTATION, "v_type_of_implementation", catsDimension);
		
		// views with only 2 columns
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
		no_entity(ColumnConstants.JOINT_CRITERIA, "v_yes_no_joint_criteria");
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
		
		addTrivialMeasures();
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
	public void synchronizeAmpColumnsBackport() {
		synchronizeAmpReportEntityBackport("columnname", "amp_columns", this.columns, "columnid", "amp_columns_seq");
 	}

	
	/**
	 * This method is created for the following scenario:
	 * 		- a measure was added to AmpReportsSchema
	 * 		- this measure doesn't exist in the old reports schema (described by AmpMeasures)
	 * 	In this scenario, opening a report with said 
	 *  new measure in the old reports engine would probably result in a crash.
	 *  Therefore, an empty
	 */
	public void synchronizeAmpMeasureBackport() {
		synchronizeAmpReportEntityBackport("measurename", "amp_measures", this.measures, "measureid", "amp_measures_seq");
	}	
	
	private void synchronizeAmpReportEntityBackport(String nameColumnEntity, String tableName, Map<String, ?> niEntity, String idColumn,
		String seqname) {
		PersistenceManager.getSession().doWork(conn -> {
			List<String> entityNamesList = SQLUtils.fetchAsList(conn, String.format("SELECT %s FROM %s", nameColumnEntity, tableName), 1);
			Set<String> entityNames = new HashSet<String>(entityNamesList);
			List<String> toBeAdded = new ArrayList<String>();
			for (String niEntityName: niEntity.keySet())
				if (!entityNames.contains(niEntityName)) 
					toBeAdded.add(niEntityName);
			//c is for columns (otherwise, it's measures)
			boolean c = tableName.equals("amp_columns");
			List<String> insertEntityNames = Arrays.asList(nameColumnEntity, "celltype", c ? "extractorview" : "type");
			List<List<Object>> values = new ArrayList<List<Object>>(); 
			for (String added: toBeAdded) 
				values.add(Arrays.asList(added, "org.dgfoundation.amp.ar.cell.TextCell", c? "v_empty_text_column" : "A"));
			SQLUtils.insert(conn, tableName, idColumn, "amp_columns_seq", insertEntityNames, values);
		});
	}
	
	private AmpReportsSchema addTrivialMeasures() {
		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_COMMITMENTS, Constants.COMMITMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", false));
		
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_COMMITMENTS, Constants.COMMITMENT, "Planned", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_DISBURSEMENTS, Constants.DISBURSEMENT, "Planned", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_EXPENDITURES, Constants.EXPENDITURE, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_EXPENDITURES, Constants.EXPENDITURE, "Planned", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_DISBURSEMENT_ORDERS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_DISBURSEMENT_ORDERS, Constants.DISBURSEMENT, "Planned", false));
		
		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_ESTIMATED_DISBURSEMENTS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_ESTIMATED_DISBURSEMENTS, Constants.DISBURSEMENT, "Planned", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ACTUAL_RELEASE_OF_FUNDS, Constants.DISBURSEMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PLANNED_RELEASE_OF_FUNDS, Constants.DISBURSEMENT, "Planned", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.OFFICIAL_DEVELOPMENT_AID_COMMITMENTS, Constants.COMMITMENT, "Actual", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.BILATERAL_SSC_COMMITMENTS, Constants.COMMITMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.TRIANGULAR_SSC_COMMITMENTS, Constants.COMMITMENT, "Actual", false));

		addMeasure(new AmpTrivialMeasure(MeasureConstants.ANNUAL_PROPOSED_PROJECT_COST, Constants.COMMITMENT, "Actual", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PROJECTION_MTEF_PROJECTIONS, Constants.COMMITMENT, "Actual", false));
		
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PIPELINE_MTEF_PROJECTIONS, Constants.PIPELINE, "Pipeline", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PIPELINE_ESTIMATED_DISBURSEMENTS, Constants.PIPELINE, "Pipeline", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PIPELINE_RELEASE_OF_FUNDS, Constants.PIPELINE, "Pipeline", false));
		addMeasure(new AmpTrivialMeasure(MeasureConstants.PIPELINE_COMMITMENTS, Constants.COMMITMENT, "Pipeline", false));
		
		return this;
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
		NiUtils.failIf(dimension != catsDimension, String.format(dimension.toString() + " is not whitelisted as a shortcut degenerate dimension"));
		return single_dimension(columnName, view, dimension.getLevelColumn(columnName, dimension.depth - 1)); // taking the leaves
	}
	
	private AmpReportsSchema single_dimension(String columnName, String view, LevelColumn levelColumn) {
		return addColumn(SimpleTextColumn.fromView(columnName, view, levelColumn));
	}
	
	private AmpReportsSchema no_entity(String columnName, String view) {
		return addColumn(SimpleTextColumn.fromViewWithoutEntity(columnName, view));
	}
	
	private AmpReportsSchema with_percentage(String columnName, String viewName, LevelColumn levelColumn) {
		logger.error(String.format("(column %s) NiDimension %s has no percentages corrector, using raw percentages", columnName, levelColumn.dimensionUsage));
		return addColumn(PercentageTextColumn.fromView(columnName, viewName, levelColumn));
	}
		
	private AmpReportsSchema with_percentage(String columnName, String viewName, NiDimensionUsage dimUsg, int level) {
		PercentagesCorrector cor = PERCENTAGE_CORRECTORS.get(dimUsg);
		NiUtils.failIf(cor == null, String.format("%s: you forgot to configure percentage correctors for %s", columnName, dimUsg));
		return addColumn(new NormalizedPercentagesColumn(columnName, dimUsg.getLevelColumn(level), null, viewName, "amp_activity_id", cor));
	}
			
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
	
	public static ReportExecutor getExecutor() {
		return getExecutor(true);
	}
	
	/**
	 * users entrypoint for running reports using NiReports
	 * @return
	 */
	public static ReportExecutor getExecutor(boolean logToDb) {
		ReportExecutor res = new NiReportsGenerator(getInstance(), ReportAreaImpl.class, logToDb);
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
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
		return sp;
	};
}
