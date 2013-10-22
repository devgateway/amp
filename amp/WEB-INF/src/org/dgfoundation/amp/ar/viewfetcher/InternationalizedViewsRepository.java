package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.digijava.module.aim.dbentity.*;

/**
 * repository class holding the configuration for all the internationalized views' i18n columns
 * @author Dolghier Constantin
 *
 */
public class InternationalizedViewsRepository {
	public final static String AGREEMENT_TITLE_AND_CODE = "Agreement Title + Code";
	public final static String CALCULATED_COLUMN = "_Calculated_Column_";
	
	public final static ColumnValueCalculator agreement_title_code_calculator = new ColumnValueCalculator() {
		
		@Override
		public String calculateValue(ResultSet resultSet) throws SQLException{
			return sqlconcat(resultSet.getString("agreement_title"), " - ", resultSet.getString("Agreement_Code"));
		}
	};	
	
	public final static ColumnValueCalculator budget_sector_calculator = new ColumnValueCalculator() {
		
		@Override
		public String calculateValue(ResultSet resultSet) throws SQLException{
			//(org.name::text || ' - '::text) || org.budget_org_code::text
			return sqlconcat(resultSet.getString("orgname"), " - ", resultSet.getString("budget_org_code"));
		}
	};	

	public final static ColumnValueCalculator budget_program_calculator = new ColumnValueCalculator() {
		
		@Override
		public String calculateValue(ResultSet resultSet) throws SQLException{
			// prog.theme_code::text || ' - '::text) || prog.name::text
			return sqlconcat(resultSet.getString("theme_code"), " - ", resultSet.getString("progname"));
		}
	};
	
	public final static ColumnValueCalculator organization_projectid_calculator = new ColumnValueCalculator() {
		
		@Override
		public String calculateValue(ResultSet resultSet) throws SQLException{
			// (org.name::text || ' -- '::text) || aaii.internal_id::text
			return sqlconcat(resultSet.getString("orgname"), " - ", resultSet.getString("internal_id"));
		}
	};		

	public final static ColumnValueCalculator projectid_calculator = new ColumnValueCalculator() {
		
		@Override
		public String calculateValue(ResultSet resultSet) throws SQLException{
			// (org.name::text || ' -- '::text) || aaii.internal_id::text
			return sqlconcat(resultSet.getString("internal_id"), " (", resultSet.getString("orgname"), ")");
		}
	};
	
	/**
	 * concatenates all of the input String's. If <b>any</b> of them is null, returns null
	 * @param inputs
	 * @return
	 */
	public final static String sqlconcat(String... inputs)
	{
		StringBuilder bld = new StringBuilder();
		for(String str:inputs)
		{
			if (str == null)
				return null;
			bld.append(str);
		}
		return bld.toString();
	}

	public final static Map<String, I18nViewDescription> i18Models =  // Map<view_name, view_description>
			Collections.<String, I18nViewDescription>unmodifiableMap(
			new HashMap<String, I18nViewDescription>()
			{{
				addViewDef(this, new I18nViewDescription("v_act_pp_details").
						addColumnDef(new I18nViewColumnDescription("name", "amp_activity_id", AmpActivityVersion.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_actors").
						addColumnDef(new I18nViewColumnDescription("name", "amp_actor_id", AmpActor.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_agreement_close_date").
						addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
						addColumnDef(new I18nViewColumnDescription(AGREEMENT_TITLE_AND_CODE, "v_agreement_close_date", agreement_title_code_calculator)).
						addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_agreement_code").
						addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
						addColumnDef(new I18nViewColumnDescription(AGREEMENT_TITLE_AND_CODE, "v_agreement_code", agreement_title_code_calculator)).
						addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_agreement_effective_date").
						addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
						addColumnDef(new I18nViewColumnDescription(AGREEMENT_TITLE_AND_CODE, "v_agreement_effective_date", agreement_title_code_calculator)).
						addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));			

				addViewDef(this, new I18nViewDescription("v_agreement_signature_date").
						addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
						addColumnDef(new I18nViewColumnDescription(AGREEMENT_TITLE_AND_CODE, "v_agreement_signature_date", agreement_title_code_calculator)).
						addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));			

				addViewDef(this, new I18nViewDescription("v_agreement_title_code").
						addColumnDef(new I18nViewColumnDescription("agreement_title", "id", AmpAgreement.class, "title")).
						addColumnDef(new I18nViewColumnDescription(AGREEMENT_TITLE_AND_CODE, "v_agreement_title_code", agreement_title_code_calculator)).
						addColumnDef(new I18nViewColumnDescription("Donor Agency", "amp_org_id", AmpOrganisation.class, "name")));			


				addViewDef(this, new I18nViewDescription("v_budget_organization").
						addColumnDef(new I18nViewColumnDescription("budget_sector", "v_budget_organization", budget_sector_calculator)).
						addColumnDef(new I18nViewColumnDescription("orgname", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_budget_program").
						addColumnDef(new I18nViewColumnDescription("budget_program", "v_budget_program", budget_program_calculator)).
						addColumnDef(new I18nViewColumnDescription("progname", "amp_theme_id", AmpTheme.class, "name")));

				addViewDef(this, new I18nViewDescription("v_organization_projectid").
						addColumnDef(new I18nViewColumnDescription("name", "v_organization_projectid", organization_projectid_calculator)).
						addColumnDef(new I18nViewColumnDescription("orgname", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_project_id").
						addColumnDef(new I18nViewColumnDescription("proj_id", "v_project_id", projectid_calculator)).
						addColumnDef(new I18nViewColumnDescription("orgname", "amp_org_id", AmpOrganisation.class, "name")));

				
				// QUESTION: AmpComponent should not be translateable? If it should - v_component_* should be added to the conf, too
				// backlog: v_organization_projectid does concatenation with orgname, but not used in reports
				// backlog: v_project_id does concat with orgname
				
				addViewDef(this, new I18nViewDescription("v_amp_theme").
						addColumnDef(new I18nViewColumnDescription("value", "id", AmpTheme.class, "name")));

				addViewDef(this, new I18nViewDescription("v_beneficiary_agency").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_beneficiary_agency_groups").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")));

				addViewDef(this, new I18nViewDescription("v_component_funding_organization_name").
						addColumnDef(new I18nViewColumnDescription("title", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_contracting_agency").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_contracting_agency_groups").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")));

				addViewDef(this, new I18nViewDescription("v_contribution_funding").
						addColumnDef(new I18nViewColumnDescription("donor_name", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_costing_donors").
						addColumnDef(new I18nViewColumnDescription("name", "donor_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_countries").
						addColumnDef(new I18nViewColumnDescription("location_name", "location_id", AmpCategoryValueLocations.class, "name")));

				addViewDef(this, new I18nViewDescription("v_districts").
						addColumnDef(new I18nViewColumnDescription("location_name", "location_id", AmpCategoryValueLocations.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_donor_cont_org").
						addColumnDef(new I18nViewColumnDescription("org", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_donor_funding").
						addColumnDef(new I18nViewColumnDescription("donor_name", "org_id", AmpOrganisation.class, "name")).
						addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")).
						addColumnDef(new I18nViewColumnDescription("donor_type_name", "org_type_id", AmpOrgType.class, "orgType"))
						);

				addViewDef(this, new I18nViewDescription("v_donor_groups").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")));
				
				addViewDef(this, new I18nViewDescription("v_donor_type").
						addColumnDef(new I18nViewColumnDescription("org_type", "org_type_id", AmpOrgType.class, "orgType")));
				
				addViewDef(this, new I18nViewDescription("v_donors").
						addColumnDef(new I18nViewColumnDescription("name", "amp_donor_org_id", AmpOrganisation.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_executing_agency").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_id", AmpOrganisation.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_executing_agency_groups").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")));

				addViewDef(this, new I18nViewDescription("v_executing_agency_type").
						addColumnDef(new I18nViewColumnDescription("name", "org_type", AmpOrgType.class, "orgType")));
				
				addViewDef(this, new I18nViewDescription("v_implementing_agency").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_id", AmpOrganisation.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_implementing_agency_groups").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")));

				addViewDef(this, new I18nViewDescription("v_implementing_agency_type").
						addColumnDef(new I18nViewColumnDescription("name", "org_type", AmpOrgType.class, "orgType")));

				addViewDef(this, new I18nViewDescription("v_indicator_description").
						addColumnDef(new I18nViewColumnDescription("name", "amp_me_indicator_id", AmpIndicator.class, "description")));

				addViewDef(this, new I18nViewDescription("v_indicator_name").
						addColumnDef(new I18nViewColumnDescription("name", "amp_me_indicator_id", AmpIndicator.class, "name")));

				addViewDef(this, new I18nViewDescription("v_measures_taken").
						addColumnDef(new I18nViewColumnDescription("name", "amp_measure_id", AmpMeasure.class, "name")));

				addViewDef(this, new I18nViewDescription("v_mtef_funding").
						addColumnDef(new I18nViewColumnDescription("donor_name", "org_id", AmpOrganisation.class, "name")).
						addColumnDef(new I18nViewColumnDescription("org_grp_name", "org_grp_id", AmpOrgGroup.class, "orgGrpName")).
						addColumnDef(new I18nViewColumnDescription("donor_type_name", "org_type_id", AmpOrgType.class, "orgType"))
						);

				addViewDef(this, new I18nViewDescription("v_nationalobjectives_all_level").
						addColumnDef(new I18nViewColumnDescription("n1", "amp_program_id1", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n2", "amp_program_id2", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n3", "amp_program_id3", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n4", "amp_program_id4", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n5", "amp_program_id5", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n6", "amp_program_id6", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n7", "amp_program_id7", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n8", "amp_program_id8", AmpTheme.class, "name"))
						);

				for(int i = 0; i <= 8; i++)
				{
					addViewDef(this, new I18nViewDescription("v_nationalobjectives_level_" + i).
						addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
				}
				
				addViewDef(this, new I18nViewDescription("v_pledges_districts").
						addColumnDef(new I18nViewColumnDescription("location_name", "location_id", AmpCategoryValueLocations.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_pledges_donor").
						addColumnDef(new I18nViewColumnDescription("name", "amp_donor_org_id", AmpOrganisation.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_pledges_donor_group").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")));

				addViewDef(this, new I18nViewDescription("v_pledges_donor_type").
						addColumnDef(new I18nViewColumnDescription("org_type", "org_type_id", AmpOrgType.class, "orgType")));
				
				// !!! V_PLEDGES_FUNDING !!! \\
				// !!! ?? V_PLEDGES_FUNDING_ST ?? !!! \\

				addViewDef(this, new I18nViewDescription("v_pledges_programs").
						addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

				addViewDef(this, new I18nViewDescription("v_pledges_projects").
						addColumnDef(new I18nViewColumnDescription("title", "amp_activity_id", AmpActivityVersion.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_pledges_regions").
						addColumnDef(new I18nViewColumnDescription("location_name", "location_id", AmpCategoryValueLocations.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_pledges_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")).
						addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sector_scheme_id", AmpSectorScheme.class, "secSchemeName"))
						);

				addViewDef(this, new I18nViewDescription("v_pledges_zones").
						addColumnDef(new I18nViewColumnDescription("location_name", "location_id", AmpCategoryValueLocations.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_primaryprogram").
						addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

				addViewDef(this, new I18nViewDescription("v_primaryprogram_all_level").
						addColumnDef(new I18nViewColumnDescription("n1", "amp_program_id1", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n2", "amp_program_id2", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n3", "amp_program_id3", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n4", "amp_program_id4", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n5", "amp_program_id5", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n6", "amp_program_id6", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n7", "amp_program_id7", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n8", "amp_program_id8", AmpTheme.class, "name"))
						);
				
				for(int i = 0; i <= 8; i++)
				{
					addViewDef(this, new I18nViewDescription("v_primaryprogram_level_" + i).
						addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
				}
				
				addViewDef(this, new I18nViewDescription("v_regional_funding").
						addColumnDef(new I18nViewColumnDescription("region_name", "region_id", AmpCategoryValueLocations.class, "name")));

				addViewDef(this, new I18nViewDescription("v_regional_group").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_regional_observations").
						addColumnDef(new I18nViewColumnDescription("name", "amp_regional_observation_id", AmpRegionalObservation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_regions").
						addColumnDef(new I18nViewColumnDescription("region", "region_id", AmpCategoryValueLocations.class, "name")));

				addViewDef(this, new I18nViewDescription("v_responsible_org_groups").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_grp_id", AmpOrgGroup.class, "orgGrpName")));

				addViewDef(this, new I18nViewDescription("v_responsible_organisation").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_secondary_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));

				addViewDef(this, new I18nViewDescription("v_secondary_sub_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")).
						addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName")));

				addViewDef(this, new I18nViewDescription("v_secondary_sub_sub_sectors").
						addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_secondaryprogram").
						addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

				addViewDef(this, new I18nViewDescription("v_secondaryprogram_all_level").
						addColumnDef(new I18nViewColumnDescription("n1", "amp_program_id1", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n2", "amp_program_id2", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n3", "amp_program_id3", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n4", "amp_program_id4", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n5", "amp_program_id5", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n6", "amp_program_id6", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n7", "amp_program_id7", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n8", "amp_program_id8", AmpTheme.class, "name"))
						);
				
				for(int i = 0; i <= 8; i++)
				{
					addViewDef(this, new I18nViewDescription("v_secondaryprogram_level_" + i).
						addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
				}				

				addViewDef(this, new I18nViewDescription("v_sect_min_cont_org").
						addColumnDef(new I18nViewColumnDescription("org", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_sector_group").
						addColumnDef(new I18nViewColumnDescription("name", "amp_org_id", AmpOrganisation.class, "name")));

				addViewDef(this, new I18nViewDescription("v_sectorloc").
						addColumnDef(new I18nViewColumnDescription("location_name", "location_id", AmpCategoryValueLocations.class, "name")));

				addViewDef(this, new I18nViewDescription("v_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")).
						addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sector_scheme_id", AmpSectorScheme.class, "secSchemeName")));

				addViewDef(this, new I18nViewDescription("v_structures").
						addColumnDef(new I18nViewColumnDescription("title", "amp_structure_id", AmpStructure.class, "title")));

				addViewDef(this, new I18nViewDescription("v_sub_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")).
						addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName")));

				addViewDef(this, new I18nViewDescription("v_sub_sub_sectors").
						addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));

				addViewDef(this, new I18nViewDescription("v_tag_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));

				addViewDef(this, new I18nViewDescription("v_tag_sub_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")).
						addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName")));

				addViewDef(this, new I18nViewDescription("v_tag_sub_sub_sectors").
						addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));

				addViewDef(this, new I18nViewDescription("v_teams").
						addColumnDef(new I18nViewColumnDescription("name", "amp_team_id", AmpTeam.class, "name")));

				addViewDef(this, new I18nViewDescription("v_tertiary_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")));

				addViewDef(this, new I18nViewDescription("v_tertiary_sub_sectors").
						addColumnDef(new I18nViewColumnDescription("sectorname", "amp_sector_id", AmpSector.class, "name")).
						addColumnDef(new I18nViewColumnDescription("sec_scheme_name", "amp_sec_scheme_id", AmpSectorScheme.class, "secSchemeName")));

				addViewDef(this, new I18nViewDescription("v_tertiary_sub_sub_sectors").
						addColumnDef(new I18nViewColumnDescription("name", "amp_sector_id", AmpSector.class, "name")));
				
				addViewDef(this, new I18nViewDescription("v_tertiaryprogram").
						addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));

				addViewDef(this, new I18nViewDescription("v_tertiaryprogram_all_level").
						addColumnDef(new I18nViewColumnDescription("n1", "amp_program_id1", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n2", "amp_program_id2", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n3", "amp_program_id3", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n4", "amp_program_id4", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n5", "amp_program_id5", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n6", "amp_program_id6", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n7", "amp_program_id7", AmpTheme.class, "name")).
						addColumnDef(new I18nViewColumnDescription("n8", "amp_program_id8", AmpTheme.class, "name"))
						);
				
				for(int i = 0; i <= 8; i++)
				{
					addViewDef(this, new I18nViewDescription("v_tertiaryprogram_level_" + i).
						addColumnDef(new I18nViewColumnDescription("name", "amp_program_id", AmpTheme.class, "name")));
				}				

				addViewDef(this, new I18nViewDescription("v_titles").
						addColumnDef(new I18nViewColumnDescription("name", "amp_activity_id", AmpActivityVersion.class, "name")));

				addViewDef(this, new I18nViewDescription("v_zones").
						addColumnDef(new I18nViewColumnDescription("location_name", "location_id", AmpCategoryValueLocations.class, "name")));
				
			}});
	
	/**
	 * adds to a Map<view_name, view_description> the description of a view's mapped columns. Does sanity checks while doing so and fails hard in case it doesn't.
	 * @param map
	 * @param viewDesc
	 */
	private static void addViewDef(HashMap<String, I18nViewDescription> map, I18nViewDescription viewDesc)
	{
		// sanity checks...
		if (!RawDatabaseViewFetcher.tableExists(InternationalizedModelDescription.connection, viewDesc.viewName))
			throw new RuntimeException("trying to define an i18n description of a non-existing table/view: " + viewDesc.viewName);
		
		Set<String> columns = DatabaseViewFetcher.getTableColumns(InternationalizedModelDescription.connection, viewDesc.viewName);
		for(I18nViewColumnDescription column:viewDesc.columns.values())
		{
			String columnName = column.columnName; // the translatable colum we are replacing
			String replacingIndexColumnName = column.indexColumnName; // the index column we are using
			if (!columns.contains(columnName))
				throw new RuntimeException("trying to override an nonexisting column: " + columnName + " of table " + viewDesc.viewName);
			if ((!column.isCalculated()) && (!columns.contains(replacingIndexColumnName)))
				throw new RuntimeException("trying to override an i18n by using a non-existant ID column " + replacingIndexColumnName + " of table " + viewDesc.viewName);
		}
		
		// double-definition checks
		if (map.containsKey(viewDesc.viewName))
			throw new RuntimeException("doubly-defined description of view " + viewDesc.viewName + ", please merge descriptions!");
		
		map.put(viewDesc.viewName, viewDesc);
		if (RawDatabaseViewFetcher.tableExists(InternationalizedModelDescription.connection, viewDesc.viewName + "_cached"))
		{
			I18nViewDescription clonedView = viewDesc.cloneView(viewDesc.viewName + "_cached");
			map.put(clonedView.viewName, clonedView);
		}
	}
}
