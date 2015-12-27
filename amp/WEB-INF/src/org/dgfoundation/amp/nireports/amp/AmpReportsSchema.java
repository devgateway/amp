package org.dgfoundation.amp.nireports.amp;

import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.LEVEL_COUNTRY;
import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.LEVEL_DISTRICT;
import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.LEVEL_REGION;
import static org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension.LEVEL_ZONE;
import static org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension.LEVEL_ORGANISATION;
import static org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension.LEVEL_ORGANISATION_GROUP;
import static org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension.LEVEL_ORGANISATION_TYPE;

import java.util.Arrays;

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
import org.dgfoundation.amp.nireports.NiFilters;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.NiReportsGenerator;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.amp.dimensions.LocationsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.OrganisationsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.ProgramsDimension;
import org.dgfoundation.amp.nireports.amp.dimensions.SectorsDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;

import com.google.common.base.Function;

/**
 * the big, glorious, immaculate, AMP Reports schema
 * @author Dolghier Constantin
 *
 */
public class AmpReportsSchema extends AbstractReportsSchema {

	public final static OrganisationsDimension orgsDimension = OrganisationsDimension.instance;
	public final static LocationsDimension locsDimension = LocationsDimension.instance;
	public final static SectorsDimension secsDimension = SectorsDimension.instance;
	public final static ProgramsDimension progsDimension = ProgramsDimension.instance;
	
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
		
		addTextColumn(ColumnConstants.ACTIVITY_PLEDGES_TITLE, "v_activity_pledges_title");
		addTextColumn(ColumnConstants.ACTIVITY_UPDATED_BY, "v_activity_modified_by");
		addTextColumn(ColumnConstants.ACTORS, "v_actors");
		addTextColumn(ColumnConstants.AGREEMENT_CODE, "v_agreement_code");
		addTextColumn(ColumnConstants.AGREEMENT_TITLE_CODE, "v_agreement_title_code");
		addTextColumn(ColumnConstants.AMP_ID, "v_amp_id");
//		addTextColumn(ColumnConstants.BENEFICIARY_AGENCY, "v_beneficiary_agency");
//		addTextColumn(ColumnConstants.BENEFICIARY_AGENCY__DEPARTMENT_DIVISION, "v_beneficiary_agency_info");
//		addTextColumn(ColumnConstants.BENEFICIARY_AGENCY_GROUPS, "v_beneficiary_agency_groups");
		addTextColumn(ColumnConstants.BUDGET_ORGANIZATION, "v_budget_organization");
		addTextColumn(ColumnConstants.BUDGET_PROGRAM, "v_budget_program");
		addTextColumn(ColumnConstants.BUDGET_STRUCTURE, "v_budget_structure");
		addTextColumn(ColumnConstants.COMPONENT_DESCRIPTION, "v_component_description");
		addTextColumn(ColumnConstants.COMPONENT_FUNDING_ORGANIZATION, "v_component_funding_organization_name");
		addTextColumn(ColumnConstants.COMPONENT_NAME, "v_components");
		addTextColumn(ColumnConstants.COMPONENT_TYPE, "v_component_type");
//		addTextColumn(ColumnConstants.CONTRACTING_AGENCY, "v_contracting_agency");
//		addTextColumn(ColumnConstants.CONTRACTING_AGENCY_ACRONYM, "v_contracting_agency_acronym");
//		addTextColumn(ColumnConstants.CONTRACTING_AGENCY_DEPARTMENT_DIVISION, "v_contracting_agency_info");
//		addTextColumn(ColumnConstants.CONTRACTING_AGENCY_GROUPS, "v_contracting_agency_groups");
		addTextColumn(ColumnConstants.COSTING_DONOR, "v_costing_donors");
		addTextColumn(ColumnConstants.CREDIT_DONATION, "v_credit_donation");
		addTextColumn(ColumnConstants.DESCRIPTION_OF_COMPONENT_FUNDING, "v_component_funding_description");
		addTextColumn(ColumnConstants.DISASTER_RESPONSE_MARKER, "v_disaster_response_marker");
		addTextColumn(ColumnConstants.DONOR_CONTACT_ORGANIZATION, "v_donor_cont_org");
		addTextColumn(ColumnConstants.ENVIRONMENT, "v_environment");
		addTextColumn(ColumnConstants.EQUAL_OPPORTUNITY, "v_equalopportunity");
//		addTextColumn(ColumnConstants.EXECUTING_AGENCY, "v_executing_agency");
//		addTextColumn(ColumnConstants.EXECUTING_AGENCY_DEPARTMENT_DIVISION, "v_executing_agency_info");
//		addTextColumn(ColumnConstants.EXECUTING_AGENCY_GROUPS, "v_executing_agency_groups");
//		addTextColumn(ColumnConstants.EXECUTING_AGENCY_TYPE, "v_executing_agency_type");
		addTextColumn(ColumnConstants.FINANCIAL_INSTRUMENT, "v_financial_instrument");
		addTextColumn(ColumnConstants.FINANCING_INSTRUMENT, "v_financing_instrument");
		addTextColumn(ColumnConstants.FUNDING_STATUS, "v_funding_status");
		addTextColumn(ColumnConstants.HUMANITARIAN_AID, "v_humanitarian_aid");
		addTextColumn(ColumnConstants.IMPLEMENTATION_LEVEL, "v_implementation_level");
		addTextColumn(ColumnConstants.IMPLEMENTING_AGENCY_DEPARTMENT_DIVISION, "v_implementing_agency_info");
		addTextColumn(ColumnConstants.INDIRECT_ON_BUDGET, "v_indirect_on_budget");
		addTextColumn(ColumnConstants.INSTITUTIONS, "v_institutions");
		addTextColumn(ColumnConstants.MEASURES_TAKEN, "v_measures_taken");
		addTextColumn(ColumnConstants.MINORITIES, "v_minorities");
		addTextColumn(ColumnConstants.MODALITIES, "v_modalities");
		addTextColumn(ColumnConstants.MODE_OF_PAYMENT, "v_mode_of_payment");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES, "v_nationalobjectives_level_1");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_DETAIL, "v_nationalobjectives");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_1, "v_nationalobjectives_level_1");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_2, "v_nationalobjectives_level_2");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_3, "v_nationalobjectives_level_3");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_4, "v_nationalobjectives_level_4");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_5, "v_nationalobjectives_level_5");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_6, "v_nationalobjectives_level_6");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_7, "v_nationalobjectives_level_7");
//		addTextColumn(ColumnConstants.NATIONAL_PLANNING_OBJECTIVES_LEVEL_8, "v_nationalobjectives_level_8");
		addTextColumn(ColumnConstants.ON_OFF_TREASURY_BUDGET, "v_on_off_budget");
		addTextColumn(ColumnConstants.ORGANIZATIONS_AND_PROJECT_ID, "v_project_id");
		addTextColumn(ColumnConstants.PAYMENT_CAPITAL___RECURRENT, "v_mode_of_payment_capital_recurrent");
		addTextColumn(ColumnConstants.PHYSICAL_PROGRESS, "v_physical_progress");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM, "v_primaryprogram_level_1");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_DETAIL, "v_primaryprogram");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_1, "v_primaryprogram_level_1");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_2, "v_primaryprogram_level_2");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_3, "v_primaryprogram_level_3");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_4, "v_primaryprogram_level_4");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_5, "v_primaryprogram_level_5");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_6, "v_primaryprogram_level_6");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_7, "v_primaryprogram_level_7");
//		addTextColumn(ColumnConstants.PRIMARY_PROGRAM_LEVEL_8, "v_primaryprogram_level_8");
//		addTextColumn(ColumnConstants.PRIMARY_SECTOR, "v_sectors");
//		addTextColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SECTOR, "v_sub_sectors");
//		addTextColumn(ColumnConstants.PRIMARY_SECTOR_SUB_SUB_SECTOR, "v_sub_sub_sectors");
		addTextColumn(ColumnConstants.PROCUREMENT_SYSTEM, "v_procurement_system");
		addTextColumn(ColumnConstants.PROGRAM_DESCRIPTION, "v_program_description");
		addTextColumn(ColumnConstants.PROJECT_CATEGORY, "v_project_category");
		addTextColumn(ColumnConstants.PROJECT_COMMENTS, "v_project_comments");
		addTextColumn(ColumnConstants.PROJECT_DESCRIPTION, "v_description");
		addTextColumn(ColumnConstants.PROJECT_IMPACT, "v_proj_impact");
		addTextColumn(ColumnConstants.PROJECT_IMPLEMENTING_UNIT, "v_project_impl_unit");
		addTextColumn(ColumnConstants.PURPOSE, "v_purposes");
		addTextColumn(ColumnConstants.REGIONAL_GROUP, "v_regional_group");
		addTextColumn(ColumnConstants.REGIONAL_GROUP_DEPARTMENT_DIVISION, "v_regional_group_info");
		addTextColumn(ColumnConstants.REGIONAL_OBSERVATIONS, "v_regional_observations");
		addTextColumn(ColumnConstants.RELATED_PLEDGES, "v_related_pledges");
		addTextColumn(ColumnConstants.RELATED_PROJECTS, "v_pledges_projects");
//		addTextColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION, "v_responsible_organisation");
//		addTextColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION_DEPARTMENT_DIVISION, "v_responsible_org_info");
//		addTextColumn(ColumnConstants.RESPONSIBLE_ORGANIZATION_GROUPS, "v_responsible_org_groups");
		addTextColumn(ColumnConstants.RESULTS, "v_results");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM, "v_secondaryprogram_level_1");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_DETAIL, "v_secondaryprogram");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_1, "v_secondaryprogram_level_1");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_2, "v_secondaryprogram_level_2");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_3, "v_secondaryprogram_level_3");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_4, "v_secondaryprogram_level_4");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_5, "v_secondaryprogram_level_5");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_6, "v_secondaryprogram_level_6");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_7, "v_secondaryprogram_level_7");
//		addTextColumn(ColumnConstants.SECONDARY_PROGRAM_LEVEL_8, "v_secondaryprogram_level_8");
//		addTextColumn(ColumnConstants.SECONDARY_SECTOR, "v_secondary_sectors");
//		addTextColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SECTOR, "v_secondary_sub_sectors");
//		addTextColumn(ColumnConstants.SECONDARY_SECTOR_SUB_SUB_SECTOR, "v_secondary_sub_sub_sectors");
//		addTextColumn(ColumnConstants.SECTOR_GROUP, "v_sector_group");
//		addTextColumn(ColumnConstants.SECTOR_GROUP_DEPARTMENT_DIVISION, "v_sector_group_info");
//		addTextColumn(ColumnConstants.SECTOR_LOCATION, "v_sectorloc");
		addTextColumn(ColumnConstants.SECTOR_MINISTRY_CONTACT_ORGANIZATION, "v_sect_min_cont_org");
//		addTextColumn(ColumnConstants.SECTOR_TAG, "v_tag_sectors");
//		addTextColumn(ColumnConstants.SECTOR_TAG_SUB_SECTOR, "v_tag_sub_sectors");
//		addTextColumn(ColumnConstants.SECTOR_TAG_SUB_SUB_SECTOR, "v_tag_sub_sub_sectors");
		addTextColumn(ColumnConstants.SSC_MODALITIES, "v_ssc_modalities");
		addTextColumn(ColumnConstants.STATUS, "v_status");
		addTextColumn(ColumnConstants.STRUCTURES_COLUMN, "v_structures");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM, "v_tertiaryprogram_level_1");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_DETAIL, "v_tertiaryprogram");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_1, "v_tertiaryprogram_level_1");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_2, "v_tertiaryprogram_level_2");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_3, "v_tertiaryprogram_level_3");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_4, "v_tertiaryprogram_level_4");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_5, "v_tertiaryprogram_level_5");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_6, "v_tertiaryprogram_level_6");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_7, "v_tertiaryprogram_level_7");
//		addTextColumn(ColumnConstants.TERTIARY_PROGRAM_LEVEL_8, "v_tertiaryprogram_level_8");
//		addTextColumn(ColumnConstants.TERTIARY_SECTOR, "v_tertiary_sectors");
//		addTextColumn(ColumnConstants.TERTIARY_SECTOR_SUB_SECTOR, "v_tertiary_sub_sectors");
//		addTextColumn(ColumnConstants.TERTIARY_SECTOR_SUB_SUB_SECTOR, "v_tertiary_sub_sub_sectors");
		addTextColumn(ColumnConstants.TYPE_OF_ASSISTANCE, "v_terms_assist");
		addTextColumn(ColumnConstants.TYPE_OF_COOPERATION, "v_type_of_cooperation");
		addTextColumn(ColumnConstants.TYPE_OF_IMPLEMENTATION, "v_type_of_implementation");
		
		// views with only 2 columns
		addTextColumnWithoutEntity(ColumnConstants.AC_CHAPTER, "v_ac_chapters");
		addTextColumnWithoutEntity(ColumnConstants.ACCESSION_INSTRUMENT, "v_accession_instruments");
		addTextColumnWithoutEntity(ColumnConstants.ACTIVITY_CREATED_BY, "v_activity_creator");
		addTextColumnWithoutEntity(ColumnConstants.AUDIT_SYSTEM, "v_audit_system");
		addTextColumnWithoutEntity(ColumnConstants.BUDGET_CODE_PROJECT_ID, "v_budget_code_project_id");
		addTextColumnWithoutEntity(ColumnConstants.BUDGET_DEPARTMENT, "v_budget_department");
		addTextColumnWithoutEntity(ColumnConstants.BUDGET_SECTOR, "v_budget_sector");
		addTextColumnWithoutEntity(ColumnConstants.CAPITAL___EXPENDITURE, "v_capital_and_exp");
		addTextColumnWithoutEntity(ColumnConstants.CRIS_NUMBER, "v_cris_number");
		addTextColumnWithoutEntity(ColumnConstants.CURRENT_COMPLETION_DATE_COMMENTS, "v_actual_completion_date_comments");
		addTextColumnWithoutEntity(ColumnConstants.DONOR_CONTACT_EMAIL, "v_donor_cont_email");
		addTextColumnWithoutEntity(ColumnConstants.DONOR_CONTACT_FAX, "v_donor_cont_fax");
		addTextColumnWithoutEntity(ColumnConstants.DONOR_CONTACT_NAME, "v_donor_cont_name");
		addTextColumnWithoutEntity(ColumnConstants.DONOR_CONTACT_PHONE, "v_donor_cont_phone");
		addTextColumnWithoutEntity(ColumnConstants.DONOR_CONTACT_TITLE, "v_donor_cont_title");
		addTextColumnWithoutEntity(ColumnConstants.FINAL_DATE_FOR_DISBURSEMENTS_COMMENTS, "v_disbursements_date_comments");
		addTextColumnWithoutEntity(ColumnConstants.FUNDING_ORGANIZATION_ID, "v_funding_org_id");
		addTextColumnWithoutEntity(ColumnConstants.FY, "v_budget_extras_fy");
		addTextColumnWithoutEntity(ColumnConstants.GOVERNMENT_AGREEMENT_NUMBER, "v_gov_agreement_number");
		addTextColumnWithoutEntity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_EMAIL, "v_impl_ex_cont_email");
		addTextColumnWithoutEntity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_FAX, "v_impl_ex_cont_fax");
		addTextColumnWithoutEntity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_NAME, "v_impl_ex_cont_name");
		addTextColumnWithoutEntity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_ORGANIZATION, "v_impl_ex_cont_org");
		addTextColumnWithoutEntity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_PHONE, "v_impl_ex_cont_phone");
		addTextColumnWithoutEntity(ColumnConstants.IMPLEMENTING_EXECUTING_AGENCY_CONTACT_TITLE, "v_impl_ex_cont_title");
		addTextColumnWithoutEntity(ColumnConstants.JOINT_CRITERIA, "v_yes_no_joint_criteria");
		addTextColumnWithoutEntity(ColumnConstants.MINISTRY_CODE, "v_minsitry_code");
		addTextColumnWithoutEntity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_EMAIL, "v_mofed_cont_email");
		addTextColumnWithoutEntity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_FAX, "v_mofed_cont_fax");
		addTextColumnWithoutEntity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_NAME, "v_mofed_cont_name");
		addTextColumnWithoutEntity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_ORGANIZATION, "v_mofed_cont_org");
		addTextColumnWithoutEntity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_PHONE, "v_mofed_cont_phone");
		addTextColumnWithoutEntity(ColumnConstants.MINISTRY_OF_FINANCE_CONTACT_TITLE, "v_mofed_cont_title");
		addTextColumnWithoutEntity(ColumnConstants.MULTI_DONOR, "v_multi_donor");
		addTextColumnWithoutEntity(ColumnConstants.PHYSICAL_PROGRESS_DESCRIPTION, "v_physical_description");
		addTextColumnWithoutEntity(ColumnConstants.PHYSICAL_PROGRESS_TITLE, "v_physical_title");
		addTextColumnWithoutEntity(ColumnConstants.PROJECT_CODE, "v_project_code");
		addTextColumnWithoutEntity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_EMAIL, "v_proj_coordr_cont_email");
		addTextColumnWithoutEntity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_FAX, "v_proj_coordr_cont_fax");
		addTextColumnWithoutEntity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_NAME, "v_proj_coordr_cont_name");
		addTextColumnWithoutEntity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_ORGANIZATION, "v_proj_coordr_cont_org");
		addTextColumnWithoutEntity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_PHONE, "v_proj_coordr_cont_phone");
		addTextColumnWithoutEntity(ColumnConstants.PROJECT_COORDINATOR_CONTACT_TITLE, "v_proj_coordr_cont_title");
		addTextColumnWithoutEntity(ColumnConstants.PROPOSED_PROJECT_LIFE, "v_proposed_project_life");
		addTextColumnWithoutEntity(ColumnConstants.REPORTING_SYSTEM, "v_reporting_system");
		addTextColumnWithoutEntity(ColumnConstants.SECTOR_MINISTRY_CONTACT_EMAIL, "v_sect_min_cont_email");
		addTextColumnWithoutEntity(ColumnConstants.SECTOR_MINISTRY_CONTACT_FAX, "v_sect_min_cont_fax");
		addTextColumnWithoutEntity(ColumnConstants.SECTOR_MINISTRY_CONTACT_NAME, "v_sect_min_cont_name");
		addTextColumnWithoutEntity(ColumnConstants.SECTOR_MINISTRY_CONTACT_PHONE, "v_sect_min_cont_phone");
		addTextColumnWithoutEntity(ColumnConstants.SECTOR_MINISTRY_CONTACT_TITLE, "v_sect_min_cont_title");
		addTextColumnWithoutEntity(ColumnConstants.SUB_VOTE, "v_subvote");
		addTextColumnWithoutEntity(ColumnConstants.VOTE, "v_vote");
		
		
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
	
	private AmpReportsSchema addTextColumnWithoutEntity(String columnName, String view) {
		return addColumn(SimpleTextColumn.fromViewWithoutEntity(columnName, view));
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
	 * Notice that this is not strictly necessary for being able to run NiReports, but it runs many self-checks as part of running a very simple report and cached some frequently-used data
	 * @throws AMPException
	 */
	public static void init() throws AMPException {
		AmpReportsSchema.getExecutor().executeReport(ReportSpecificationImpl.buildFor("self-test report", 
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
}
