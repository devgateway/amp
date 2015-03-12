package org.digijava.module.xmlpatcher.core;

import java.sql.Connection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.module.aim.action.ViewNewAdvancedReport;
import org.digijava.module.aim.helper.Constants;

/**
 * quick & dirty SQL patches made for ugly things like DROPping tables or columns from entities which would otherwise have been locked by Hibernate
 * use the fully-featured patcher for "normal" patches!
 * <strong>You should only call / use the class BEFORE having initialized Hibernate</strong>
 * @author Dolghier Constantin
 *
 */
public class SimpleSQLPatcher {
	
	private static Logger logger = Logger.getLogger(SimpleSQLPatch.class) ;
	
	public SortedSet<SimpleSQLPatch> patches = new TreeSet<SimpleSQLPatch>(){
			void addPatch(SimpleSQLPatch p){
				if (this.contains(p))
					throw new RuntimeException("doubly-defined patch, please fix your commit: " + p.id);
				this.add(p);
			}{
			addPatch(new SimpleSQLPatch("001",
				"DROP TABLE IF EXISTS AMP_TEAM_PAGE_FILTERS",
				"DROP SEQUENCE IF EXISTS AMP_TEAM_PAGE_FILTERS_seq",
      	
				"DROP TABLE IF EXISTS amp_page_filters",
				"DROP SEQUENCE IF EXISTS amp_page_filters_seq",
      
				"DROP TABLE IF EXISTS AMP_FILTERS",
				"DROP SEQUENCE IF EXISTS AMP_FILTERS_seq",

				"ALTER TABLE amp_organisation DROP COLUMN IF EXISTS level_id",
				"ALTER TABLE amp_org_group DROP COLUMN IF EXISTS amp_level_id",
		
				"DROP TABLE IF EXISTS AMP_LEVEL",
				"DROP SEQUENCE IF EXISTS AMP_LEVEL_seq",

				"DROP TABLE IF EXISTS AMP_PAGES",
				"DROP SEQUENCE IF EXISTS AMP_PAGES_seq",

				"DROP TABLE IF EXISTS AMP_STATUS",
				"DROP SEQUENCE IF EXISTS AMP_STATUS_seq",
      	      	
				"DROP TABLE IF EXISTS AMP_TERMS_ASSIST",
				"DROP SEQUENCE IF EXISTS AMP_TERMS_ASSIST_seq",

				"DROP TABLE IF EXISTS amp_member_links",
				"DROP SEQUENCE IF EXISTS amp_member_links_seq",
		
				"DROP TABLE IF EXISTS dg_cms_content_item",
				"DROP SEQUENCE IF EXISTS dg_cms_content_item_seq"));
		
		addPatch(new SimpleSQLPatch("002", 
				"DROP TABLE IF EXISTS dg_gis_map_point",
				"DROP TABLE IF EXISTS dg_gis_map_shape",
				"DROP TABLE IF EXISTS dg_gis_map_segment",
				"DROP TABLE IF EXISTS dg_gis_settings",
				"DROP TABLE IF EXISTS dg_gis_map",

				"DROP TABLE IF EXISTS amp_widget_value",
				"DROP TABLE IF EXISTS amp_widget_column",
				"DROP TABLE IF EXISTS amp_widget_place",
				"DROP TABLE IF EXISTS amp_widget_pi_base_target_val",
				"DROP TABLE IF EXISTS amp_widget_sector_order",
				"DROP TABLE IF EXISTS amp_widget_sector_table_year",
				"DROP TABLE IF EXISTS amp_widget",

				"DROP SEQUENCE IF EXISTS dg_gis_map_seq",
				"DROP SEQUENCE IF EXISTS dg_gis_map_point_seq",
				"DROP SEQUENCE IF EXISTS dg_gis_map_segment_seq",
				"DROP SEQUENCE IF EXISTS dg_gis_map_shape_seq",
				"DROP SEQUENCE IF EXISTS dg_gis_settings_seq",
				"DROP SEQUENCE IF EXISTS amp_widget_column_seq",
				"DROP SEQUENCE IF EXISTS amp_widget_value_seq",
				"DROP SEQUENCE IF EXISTS amp_widget_place_seq",
				"DROP SEQUENCE IF EXISTS amp_widget_pi_base_target_val_seq",
				"DROP SEQUENCE IF EXISTS amp_widget_sector_order_seq",
				"DROP SEQUENCE IF EXISTS amp_widget_sector_table_year_seq",
				"DROP SEQUENCE IF EXISTS amp_widget_seq"));
		
		addPatch(new SimpleSQLPatch("003",
				"DROP VIEW IF EXISTS v_act_pp_regions",
				"UPDATE amp_global_settings SET settingsvalue = 'true' WHERE settingsname='Recreate the views on the next server restart'",
 		
				"ALTER TABLE amp_location DROP COLUMN IF EXISTS country",
				"ALTER TABLE amp_location DROP COLUMN IF EXISTS region",
				"ALTER TABLE amp_location DROP COLUMN IF EXISTS zone",
				"ALTER TABLE amp_location DROP COLUMN IF EXISTS woreda",

				"ALTER TABLE amp_location DROP COLUMN IF EXISTS country_id",
				"ALTER TABLE amp_location DROP COLUMN IF EXISTS region_id",
				"ALTER TABLE amp_location DROP COLUMN IF EXISTS zone_id",
				"ALTER TABLE amp_location DROP COLUMN IF EXISTS woreda_id",

				"DROP TABLE IF EXISTS amp_woreda",
				"DROP TABLE IF EXISTS amp_zone",
				"DROP TABLE IF EXISTS amp_region",

				"DROP SEQUENCE IF EXISTS amp_woreda_seq",
				"DROP SEQUENCE IF EXISTS amp_zone_seq",
				"DROP SEQUENCE IF EXISTS amp_region_seq"
				));
		
		addPatch(new SimpleSQLPatch("004",
		      	"DROP TABLE IF EXISTS amp_report_cache",
		      	"DROP SEQUENCE IF EXISTS amp_report_cache_seq",

		      	"DROP TABLE IF EXISTS amp_report_location",
		      	"DROP SEQUENCE IF EXISTS amp_report_location_seq",

		      	"DROP TABLE IF EXISTS amp_report_sector",
		      	"DROP SEQUENCE IF EXISTS amp_report_sector_seq",

		      	"DROP TABLE IF EXISTS amp_report_sector_project",
		      	"DROP SEQUENCE IF EXISTS amp_report_sector_project_seq",
		      	
				"DROP TABLE IF EXISTS amp_physical_component_report",
		      	"DROP SEQUENCE IF EXISTS amp_physical_component_report_seq",
		      	
				"DROP TABLE IF EXISTS AMP_REPORT_PHYSICAL_PERFORMANC",
		      	"DROP SEQUENCE IF EXISTS AMP_REPORT_PHYSICAL_PERFORMANC_seq",

				"DROP TABLE IF EXISTS amp_report_modality"
				));

			addPatch(new SimpleSQLPatch(
					"006",
					"DROP VIEW IF EXISTS amp_activity CASCADE ",
					"DROP VIEW IF EXISTS v_amp_activity_expanded ",
					"DROP VIEW IF EXISTS v_act_pp_details",
					"DROP VIEW IF EXISTS v_mondrian_programs",
					
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS linked_activities",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS contract_details",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS version",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS cal_type",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS condition_",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS contractors",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS convenio_date_filter",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS classi_code",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS convenio_numcont",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS  customField1",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS  customField2",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS  customField3",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS  customField4",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS  customField5",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS  customField6",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS contracting_arrangements",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS comments",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS cond_seq",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS activity_level_id",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS gbsSbs",
					"ALTER TABLE  amp_theme DROP COLUMN IF EXISTS amp_activity_id",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS amp_theme_id",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS amp_categ_val_modality_id",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS author",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS plan_min_rank",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS chapter_code",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS funding_sources_number CASCADE",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS activity_start_date",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS activity_close_date",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS amp_activity_previous_version_id",
					"ALTER TABLE  amp_activity_version DROP COLUMN IF EXISTS activity_approval_date",
					
					"DROP INDEX IF EXISTS amp_activity_version_comments_idx",
					"DROP INDEX IF EXISTS amp_activity_version_contracting_arrangements_idx",
										
					" UPDATE amp_global_settings SET settingsvalue = 'true' WHERE settingsname='Recreate the views on the next server restart'",
					
					" DELETE FROM amp_report_column where columnid in  ( SELECT columnid FROM  amp_columns WHERE extractorview = 'v_convenio_numcont')",
					" DELETE FROM amp_report_column where columnid in  ( SELECT columnid FROM  amp_columns WHERE extractorview = 'v_contracting_arrangements' )",
					" DELETE FROM amp_report_column where columnid in  ( SELECT columnid FROM  amp_columns WHERE extractorview = 'v_budgeting_year' )",
					" DELETE FROM amp_report_column where columnid in  ( SELECT columnid FROM  amp_columns WHERE extractorview = 'v_code_chapitre' )",
					" DELETE FROM amp_report_column where columnid in  ( SELECT columnid FROM  amp_columns WHERE extractorview = 'v_description_chapitre' )",
					" DELETE FROM amp_report_column where columnid in  ( SELECT columnid FROM  amp_columns WHERE extractorview = 'v_description_imputation' )",
					" DELETE FROM amp_report_column where columnid in  ( SELECT columnid FROM  amp_columns WHERE extractorview = 'v_imputation' )",
					" DELETE FROM  amp_columns WHERE extractorview = 'v_convenio_numcont' ",
					" DELETE FROM  amp_columns WHERE extractorview = 'v_contracting_arrangements'  ",
					" DELETE FROM  amp_columns WHERE extractorview = 'v_budgeting_year' ",
					" DELETE FROM  amp_columns WHERE extractorview = 'v_code_chapitre' ",
					" DELETE FROM  amp_columns WHERE extractorview = 'v_description_chapitre' ",
					" DELETE FROM  amp_columns WHERE extractorview = 'v_description_imputation' ",
					" DELETE FROM  amp_columns WHERE extractorview = 'v_imputation' ",
					
					" DELETE FROM amp_modules_templates WHERE module = (select id from amp_modules_visibility WHERE name LIKE '/Activity Form/Identification/Linked Activities')",
					" DELETE FROM amp_modules_visibility WHERE name LIKE '/Activity Form/Identification/Linked Activities'",
					" DELETE FROM amp_fields_templates WHERE field =(select id  from amp_fields_visibility WHERE name LIKE 'Linked Activities')",
					" DELETE FROM amp_fields_visibility WHERE name LIKE 'Linked Activities'",
					" DELETE FROM amp_fields_templates WHERE field =(select id from amp_fields_visibility WHERE name  ='Contract Number')",
					" DELETE FROM amp_fields_visibility WHERE name  ='Contract Number'",
					" DELETE FROM amp_fields_templates WHERE field =(SELECT id FROM amp_fields_visibility WHERE name  ='Contract Number')",
					" DELETE FROM amp_fields_visibility WHERE name  ='Contract Number'",
					" DELETE FROM amp_features_templates WHERE feature in (SELECT id FROM amp_features_visibility WHERE parent =(SELECT id FROM amp_modules_visibility WHERE name='Custom Fields'))",
					" DELETE FROM amp_fields_templates WHERE field in (SELECT id FROM amp_fields_visibility WHERE name like 'Custom Field%')",
					" DELETE FROM amp_fields_visibility WHERE name like 'Custom Field%'",
					" DELETE FROM amp_features_visibility WHERE parent =(SELECT id FROM amp_modules_visibility WHERE name='Custom Fields')",
					" DELETE FROM amp_modules_templates WHERE MODULE IN(SELECT ID FROM amp_modules_visibility WHERE name='Custom Fields')",
					" DELETE FROM amp_modules_visibility WHERE name='Custom Fields'",
					" DELETE FROM amp_features_visibility WHERE parent =(SELECT id FROM amp_modules_visibility WHERE name='Custom Fields')",
					
					" DELETE FROM  amp_modules_templates WHERE module IN (SELECT id FROM amp_modules_visibility WHERE name ='/Activity Form/Identification/Contracting Arrangements')",
					" DELETE FROM  amp_modules_visibility WHERE name ='/Activity Form/Identification/Contracting Arrangements'",
					" DELETE FROM  amp_fields_templates WHERE field IN (SELECT id FROM  amp_fields_visibility WHERE name = 'Contracting Arrangements')",
					" DELETE FROM  amp_fields_visibility WHERE name = 'Contracting Arrangements'",
					" DELETE FROM  amp_modules_templates WHERE module in(SELECT id FROM amp_modules_visibility WHERE name='/Activity Form/Identification/Conditionality and Sequencing' )",
					" DELETE FROM amp_modules_visibility WHERE name='/Activity Form/Identification/Conditionality and Sequencing' ",
					" DELETE FROM  amp_fields_templates WHERE field in(SELECT id FROM amp_fields_visibility WHERE name='Conditionality and Sequencing' )",
					" DELETE FROM  amp_fields_visibility WHERE name='Conditionality and Sequencing' ",
					" DELETE FROM  amp_modules_templates WHERE module IN (SELECT id FROM amp_modules_visibility WHERE name = '/Activity Form/Planning/Ministry of Planning Rank')",
					" DELETE FROM  amp_modules_visibility WHERE name = '/Activity Form/Planning/Ministry of Planning Rank'",
					" DELETE FROM  amp_fields_templates WHERE field IN (SELECT id FROM amp_fields_visibility WHERE name ='Ministry of Planning Rank')",
					" DELETE FROM  amp_fields_visibility WHERE name = 'Ministry of Planning Rank'",
					" DELETE FROM  amp_fields_templates WHERE field IN (SELECT id FROM amp_fields_visibility WHERE name = 'Budgeting Year')",
					" DELETE FROM  amp_fields_visibility WHERE name = 'Budgeting Year'",
					" DELETE FROM  amp_fields_templates WHERE field IN (SELECT id FROM amp_fields_visibility WHERE name ='Code Chapitre')",
					" DELETE FROM  amp_fields_visibility WHERE name ='Code Chapitre'",
					" DELETE FROM  amp_fields_templates WHERE field IN (SELECT id FROM amp_fields_visibility WHERE name ='Description Chapitre')",
					" DELETE FROM  amp_fields_visibility WHERE name ='Description Chapitre'",
					" DELETE FROM  amp_fields_templates WHERE field IN (SELECT id FROM amp_fields_visibility WHERE name ='Description Imputation')",
					" DELETE FROM  amp_fields_visibility WHERE name ='Description Imputation'",
					"UPDATE amp_global_settings SET settingsvalue = 'true' WHERE settingsname='Recreate the views on the next server restart'"					
					));
			addPatch(new SimpleSQLPatch("007",
					"DROP VIEW IF EXISTS v_m_secondary_sectors",
					"DROP VIEW IF EXISTS v_m_sectors",
					"DROP VIEW IF EXISTS v_donor_funding_cached",
					"DROP VIEW IF EXISTS v_primaryprogram_cached",
					"DROP VIEW IF EXISTS v_regions_cached",
					"DROP VIEW IF EXISTS v_secondaryprogram_cached",
					"DROP VIEW IF EXISTS v_tertiaryprogram_cached",
					"DROP VIEW IF EXISTS v_sectors_cached"
					));
			
			addPatch(new SimpleSQLPatch("008",
					"DROP TABLE IF EXISTS mondrian_raw_donor_transactions",
					
					"DROP TABLE IF EXISTS mondrian_locations",
					"DROP TABLE IF EXISTS mondrian_locations_en",
					"DROP TABLE IF EXISTS mondrian_locations_ro",
					"DROP TABLE IF EXISTS mondrian_locations_fr",
					
					"DROP TABLE IF EXISTS mondrian_sectors",
					"DROP TABLE IF EXISTS mondrian_sectors_en",
					"DROP TABLE IF EXISTS mondrian_sectors_ro",
					"DROP TABLE IF EXISTS mondrian_sectors_fr",
					
					"DROP TABLE IF EXISTS mondrian_programs",
					"DROP TABLE IF EXISTS mondrian_programs_en",
					"DROP TABLE IF EXISTS mondrian_programs_ro",
					"DROP TABLE IF EXISTS mondrian_programs_fr",
					
					"DROP TABLE IF EXISTS mondrian_organizations",
					"DROP TABLE IF EXISTS mondrian_organizations_en",
					"DROP TABLE IF EXISTS mondrian_organizations_ro",
					"DROP TABLE IF EXISTS mondrian_organizations_fr",
					
					"DROP TABLE IF EXISTS mondrian_activity_texts",
					"DROP TABLE IF EXISTS mondrian_activity_texts_en",
					"DROP TABLE IF EXISTS mondrian_activity_texts_ro",
					"DROP TABLE IF EXISTS mondrian_activity_texts_fr",
					
					"DROP TABLE IF EXISTS mondrian_raw_donor_transactions",
					"DROP TABLE IF EXISTS etl_executing_agencies",
					"DROP TABLE IF EXISTS etl_beneficiary_agencies",
					"DROP TABLE IF EXISTS etl_implementing_agencies",
					"DROP TABLE IF EXISTS etl_responsible_agencies",
					"DROP TABLE IF EXISTS etl_locations",
					"DROP TABLE IF EXISTS etl_activity_program_national_plan_objective",
					"DROP TABLE IF EXISTS etl_activity_program_primary_program",
					"DROP TABLE IF EXISTS etl_activity_program_secondary_program",
					"DROP TABLE IF EXISTS etl_activity_program_tertiary_program",
					"DROP TABLE IF EXISTS etl_activity_sector_primary",
					"DROP TABLE IF EXISTS etl_activity_sector_secondary",
					"DROP TABLE IF EXISTS etl_activity_sector_tertiary",
					"DROP TABLE IF EXISTS etl_locations",
					
					"DROP TABLE IF EXISTS mondrian_dates",
					"DROP TABLE IF EXISTS mondrian_fact_table",
					"DROP TABLE IF EXISTS mondrian_exchange_rates"
					));
			//dropping the table so hibernate creates the sequence correctly
			//this is a not yet implemented feature
			
			addPatch(new SimpleSQLPatch("009",
					"DROP TABLE IF EXISTS amp_api_state",
					"DROP SEQUENCE IF EXISTS amp_map_state_seq"
					));

			// update new locations for old patches that were moved to a different place and looks like no longer are maintained, but pollute the output
			addPatch(new SimpleSQLPatch("010",
					XMLPatchesWrongPaths.SQL_PATCH
					));

            addPatch(new SimpleSQLPatch("011",

                    //Has Mondrian reference
                    // "DELETE FROM amp_columns_filters where bean_field_name = 'projectImplementingUnits'",

                    // Has Mondrian reference
                    // "DELETE FROM amp_columns where columnname = 'Project Implementing Unit'",
                    "DELETE FROM amp_columns where columnname = 'Project is on budget'",
                    "DELETE FROM amp_columns where columnname = 'Project is on parliament'",
                    "DELETE FROM amp_columns where columnname = 'Project has been approved by IMAC'",
                    // Has Mondrian reference
                    "DELETE FROM amp_columns where columnname = 'Project uses parallel project implementation unit'",
                    "DELETE FROM amp_columns where columnname = 'Government is member of project steering committee'",
                    "DELETE FROM amp_columns where columnname = 'Project disburses directly into the Government single treasury account'",
                    "DELETE FROM amp_columns where columnname = 'Project uses national financial management systems'",
                    "DELETE FROM amp_columns where columnname = 'Project uses national procurement systems'",
                    "DELETE FROM amp_columns where columnname = 'Project uses national audit systems'",

                    // Has Mondrian reference
                    "DROP VIEW IF EXISTS v_mondrian_activity_fixed_texts CASCADE", 
                    // "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS prj_implementation_unit",
                    "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS imac_approved CASCADE",
                    "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS national_oversight CASCADE",
                    "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS on_budget CASCADE",
                    "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS on_parliament CASCADE",
                    "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS on_treasury CASCADE",
                    "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS national_financial_management CASCADE",
                    "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS national_procurement CASCADE",
                    "ALTER TABLE amp_activity_version DROP COLUMN IF EXISTS national_audit CASCADE"
            ));
	}};
	DataSource dataSource;
	
	public SimpleSQLPatcher() throws Exception {		
		Context initialContext = new InitialContext();
   		this.dataSource = (javax.sql.DataSource) initialContext.lookup(Constants.UNIFIED_JNDI_ALIAS);
   		if (dataSource == null)
   			throw new RuntimeException("could not find data source!");
	}
	
	protected void createDummyViewIfMissingOrTable(Connection conn, String viewName, String query, boolean force) {
		if (!SQLUtils.isView(conn, viewName)) {
			logger.error(viewName + " is not a view");
			SQLUtils.executeQuery(conn, "DROP TABLE IF EXISTS " + viewName + " CASCADE");
		}
		// past this point: it is either be a view or does not exist
		SQLUtils.executeQuery(conn, "DROP VIEW IF EXISTS " + viewName + " CASCADE");
		SQLUtils.executeQuery(conn, "CREATE OR REPLACE VIEW " + viewName + " AS " + query);

	}
	
	protected void createTrickyViewsIfNeeded(Connection conn) throws Exception {
		boolean recreatingViews = SQLUtils.getLong(conn, "select count(*) from amp_global_settings where settingsvalue = 'true' and settingsname='Recreate the views on the next server restart'") > 0;
		boolean ampActivityIsNotView = !SQLUtils.isView(conn, "amp_activity");
		boolean ampActivityExpandedIsNotView = !SQLUtils.isView(conn, "v_amp_activity_expanded");
		logger.warn(String.format("asked to recreate views: %b, amp_activity is not view: %b, v_amp_activity_expanded is not view: %b", recreatingViews, ampActivityIsNotView, ampActivityExpandedIsNotView));
		if (recreatingViews || ampActivityExpandedIsNotView || ampActivityIsNotView) {
			logger.error("forcing recreating views!");
			SQLUtils.executeQuery(conn, "UPDATE amp_global_settings SET settingsvalue = 'true' WHERE settingsname='Recreate the views on the next server restart'");
			SQLUtils.executeQuery(conn, "INSERT INTO amp_etl_changelog(entity_name, entity_id) VALUES ('full_etl_request', 999)");
			createDummyViewIfMissingOrTable(conn, "amp_activity", "SELECT aav.* from amp_activity_version aav JOIN amp_activity_group aag ON aav.amp_activity_id = aag.amp_activity_last_version_id AND (aav.deleted IS NULL or aav.deleted = false)", recreatingViews);
			createDummyViewIfMissingOrTable(conn, "v_amp_activity_expanded", "SELECT av.*, dg_editor.body AS expanded_description FROM amp_activity av LEFT JOIN dg_editor ON av.description = dg_editor.editor_key", recreatingViews);			
		}
	}
	
	/**
	 * runs the hardcoded SQL queries
	 * in case any of them fails, AMP startup will stop. THIS IS NORMAL
	 * @throws Exception
	 */
	public void doWork() throws Exception{
   		try(Connection conn = dataSource.getConnection()){
   			boolean autoCommit = conn.getAutoCommit();
   			
   			conn.setAutoCommit(true);
   			SQLUtils.executeQuery(conn, "UPDATE amp_xml_patch SET state = 0 WHERE state != 0 AND state != 4 AND location = 'xmlpatches/general/views/'");
   			createTrickyViewsIfNeeded(conn);
   			SQLUtils.executeQuery(conn, 
   					"CREATE TABLE IF NOT EXISTS amp_simple_sql_patches(id varchar(255), hash text, date_applied bigint)");
   			for(SimpleSQLPatch patch:patches){
   				java.util.List<String> hashes = SQLUtils.fetchAsList(conn, "SELECT hash FROM amp_simple_sql_patches WHERE id='" + patch.id + "'", 1);
   				if (hashes.size() > 1)
   					throw new RuntimeException("amp_simple_sql_patches is corrupted, please review code / database (patch with id " + patch.id + " is mentioned > 1 times)");   				
   				boolean shouldRunPatch = hashes.isEmpty() || (!hashes.get(0).equals(patch.hash));
   				if (shouldRunPatch)
   					executePatch(patch, conn, hashes.isEmpty());
   			}
   			
   			runDrcCleanup(conn);
   			createTrickyViewsIfNeeded(conn);
   			conn.setAutoCommit(false);
   			
   			conn.setAutoCommit(autoCommit);
   		}
	}
	
	/**
	 * the DRC database is miserable as of September 2014 - quick&dirty run-once fixes for it
	 * @param conn
	 */
	void runDrcCleanup(Connection conn) {
		if (SQLUtils.fetchLongs(conn, "SELECT count(*) from amp_columns WHERE columnname in ('Description of Component Funding', 'Component Funding Organization')").get(0) == 0) {
			SQLUtils.executeQuery(conn, "INSERT INTO amp_columns (columnid, columnname, aliasname, celltype, extractorview) VALUES " + 
				"(nextval('amp_columns_seq'), 'Description of Component Funding', 'component_funding_description', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_component_funding_description'), " + 
				"(nextval('amp_columns_seq'), 'Component Funding Organization', 'component_funding_organization_name', 'org.dgfoundation.amp.ar.cell.TextCell', 'v_component_funding_organization_name')");			
		}
		
		boolean reindexACV = false;
		List<Long> aa = SQLUtils.fetchLongs(conn, "select count(*) from amp_category_value acv where (select count(*) from amp_category_value acv2 where acv2.amp_category_class_id = acv.amp_category_class_id AND acv2.index_column = acv.index_column) > 1"); 
		reindexACV |= aa.get(0) > 0;

		aa = SQLUtils.fetchLongs(conn, "select amp_location_id from amp_location where location_id = 1397 order by amp_location_id");
		if (aa.size() == 2 && aa.get(0) == 107 && aa.get(1) == 109) {
			SQLUtils.executeQuery(conn, "delete from amp_location where amp_location_id = 109");
		}
		
		aa = SQLUtils.fetchLongs(conn, "select id from amp_category_value acv WHERE (select count(*) from amp_category_value acv2 where acv.category_value = acv2.category_value and acv.amp_category_class_id = acv2.amp_category_class_id) > 1 order by id");
		
		if (aa.size() == 2 && aa.get(0) == 248 && aa.get(1) == 249) {
			SQLUtils.executeQuery(conn, "delete from amp_category_value where id = 248");
			reindexACV |= true;
		}
		
		aa = SQLUtils.fetchLongs(conn, "select amp_location_id from amp_location where location_id = 9088 order by amp_location_id");
		if (aa.size() == 2 && aa.get(0) == 513 && aa.get(1) == 514) {
			SQLUtils.executeQuery(conn, "update amp_activity_location set amp_location_id = 513 where amp_location_id = 514");
			SQLUtils.executeQuery(conn, "delete from amp_location where amp_location_id = 514");
		}
		
		if (reindexACV)
			SQLUtils.executeQuery(conn, "UPDATE amp_category_value acv SET index_column = (SELECT count(*) FROM amp_category_value acv2 WHERE acv2.amp_category_class_id = acv.amp_category_class_id AND (acv2.index_column < acv.index_column OR (acv2.id < acv.id AND acv2.index_column = acv.index_column)))");
	}
	
	void executePatch(SimpleSQLPatch patch, Connection conn, boolean shouldInsert){
		for(String query:patch.queries){
			SQLUtils.executeQuery(conn, query);
		}
		
		if (shouldInsert){
			SQLUtils.executeQuery(conn, 
					String.format("INSERT INTO amp_simple_sql_patches(id, hash, date_applied) VALUES ('%s', 'dummy', -1)", patch.id));
		}
		
		SQLUtils.executeQuery(conn,
				String.format("UPDATE amp_simple_sql_patches SET hash='%s', date_applied=%d WHERE id='%s'", 
					patch.hash, System.currentTimeMillis(), patch.id));	
	}
}
