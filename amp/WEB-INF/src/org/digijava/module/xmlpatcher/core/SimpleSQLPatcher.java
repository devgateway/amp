package org.digijava.module.xmlpatcher.core;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.module.aim.helper.Constants;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * quick & dirty SQL patcher made for ugly things like DROPping tables or columns from entities which would otherwise have been locked by Hibernate
 * use the fully-featured patcher for "normal" patches!
 * <strong>You should only call / use the class BEFORE having initialized Hibernate</strong>
 * @author Dolghier Constantin
 *
 */
public class SimpleSQLPatcher {
    
    private static final Logger logger = Logger.getLogger(SimpleSQLPatcher.class) ;
    
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
                    "DROP VIEW IF EXISTS v_act_pp_details",

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
//                  " DELETE FROM amp_fields_templates WHERE field =(SELECT id FROM amp_fields_visibility WHERE name  ='Contract Number')",
//                  " DELETE FROM amp_fields_visibility WHERE name  ='Contract Number'",
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

            //AMP 2.10 patches written after AMP 2.11 has been branched off go to the 010xxxx patchnames
            addPatch(new SimpleSQLPatch("010001", // adds measures which are not present, for some reason, in the amp_measures table
                    "INSERT INTO amp_measures(measureid, measurename, aliasname, type, expression, description) " +  
                    " SELECT nextval('amp_measures_seq'), 'Percentage of Total Commitments', 'Percentage of Total Commitments', 'A', 'percentageOfTotalCommitments', 'Actual commitments for the project / Total actual commitments * 100' " + 
                    " WHERE (select count(*) FROM amp_measures where measurename='Percentage of Total Commitments') = 0",

                    "INSERT INTO amp_measures(measureid, measurename, aliasname, type, expression, description) " +  
                    " SELECT nextval('amp_measures_seq'), 'Percentage of Total Disbursements', 'Percentage of Total Disbursements', 'A', 'percentageOfTotalDisbursements', 'Actual disbursements for the project / Total actual disbursements * 100' " + 
                    " WHERE (select count(*) FROM amp_measures where measurename='Percentage of Total Disbursements') = 0"));


            addPatch(new SimpleSQLPatch("010002", // adds an another batch of measures which are not present, for some reason, in the amp_measures table
                    "INSERT INTO amp_measures(measureid, measurename, aliasname, type, expression, description) " +  
                    " SELECT nextval('amp_measures_seq'), 'Disbursment Ratio', 'Disbursment Ratio', 'A', 'disbursmentRatio', 'Sum of actual disbursment / Total actual disb * 100' " + 
                    " WHERE (select count(*) FROM amp_measures where measurename='Disbursment Ratio') = 0",
                    
                    "INSERT INTO amp_measures(measureid, measurename, aliasname, type, expression, description) " +  
                    " SELECT nextval('amp_measures_seq'), 'Current Month Disbursements', 'Current Month Disbursements', 'A', 'currentMonthDisbursements', 'Sum of Actual Disbursements of the current month' " + 
                    " WHERE (select count(*) FROM amp_measures where measurename='Current Month Disbursements') = 0",

                    "INSERT INTO amp_measures(measureid, measurename, aliasname, type, expression, description) " +  
                    " SELECT nextval('amp_measures_seq'), 'Previous Month Disbursements', 'Previous Month Disbursements', 'A', 'previousMonthDisbursements', 'Sum of Actual Disbursements of the previous month' " + 
                    " WHERE (select count(*) FROM amp_measures where measurename='Previous Month Disbursements') = 0",

                    "INSERT INTO amp_measures(measureid, measurename, aliasname, type, expression, description) " +  
                    " SELECT nextval('amp_measures_seq'), 'Last Year of Planned Disbursements', 'Last Year of Planned Disbursements', 'A', 'lastYearPlannedDisbursements', 'Previous Year Planned Disbursements' " + 
                    " WHERE (select count(*) FROM amp_measures where measurename='Last Year of Planned Disbursements') = 0",
                    
                    "INSERT INTO amp_measures(measureid, measurename, aliasname, type, expression, description) " +  
                    " SELECT nextval('amp_measures_seq'), 'Percentage of Disbursement', 'Percentage of Disbursement', 'A', 'percentageOfDisbursement', '(Total Actual Disbursements for Year,Quarter,Month / Total Actual Disbursements) * 100' " + 
                    " WHERE (select count(*) FROM amp_measures where measurename='Percentage of Disbursement') = 0"
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
            
            addPatch(new SimpleSQLPatch("012",

                    "ALTER TABLE IF EXISTS amp_menu_entry DROP COLUMN IF EXISTS is_public CASCADE", 
                    "ALTER TABLE IF EXISTS amp_menu_entry DROP COLUMN IF EXISTS is_admin CASCADE",
                    "ALTER TABLE IF EXISTS amp_menu_entry DROP COLUMN IF EXISTS is_team CASCADE"
            ));
            // AMP-21756: it is not yet used, thus let's recreate
            addPatch(new SimpleSQLPatch("013",
                    "DROP TABLE IF EXISTS amp_inflation_rates CASCADE"
            ));
            // AMP-22428: fixing amp_activity that was deleted and then restored
            addPatch(new SimpleSQLPatch("014",
                    "UPDATE amp_xml_patch SET state = 0 WHERE patch_id = 'amp_activity.xml'"
            ));
            addPatch(new SimpleSQLPatch("015",
                    "UPDATE dg_locale SET left_to_right = false WHERE name IN "
                            + "('Persian','Urdu','Hebrew','Arabic',"
                            + "'Kurdish')", "UPDATE dg_locale SET left_to_right = true WHERE name NOT IN ('Persian',"
                    + "'Urdu','Hebrew','Arabic','Kurdish')"));
            // AMP-27104 for the patch to work a double restart is needed with a simple_sql_patch we avoid this
            // this was introduced in AMP-26932

    }};
    DataSource dataSource;
    
    public SimpleSQLPatcher() throws Exception {        
        Context initialContext = new InitialContext();
        this.dataSource = (javax.sql.DataSource) initialContext.lookup(Constants.UNIFIED_JNDI_ALIAS);
        if (dataSource == null)
            throw new RuntimeException("could not find data source!");
    }
    
    protected void createDummyViewIfMissingOrTable(Connection conn, String viewName, String query) {
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
        boolean aaHasOtherColumnsThanAav = !SQLUtils.getTableColumnsWithTypes(conn, "amp_activity", false).toString().equals(SQLUtils.getTableColumnsWithTypes(conn, "amp_activity_version", false).toString());

        logger.warn(String.format("asked to recreate views: %b, amp_activity is not view: %b, aaHasOtherColumnsThanAav: %b", recreatingViews, ampActivityIsNotView, aaHasOtherColumnsThanAav));
        if (recreatingViews || ampActivityIsNotView || aaHasOtherColumnsThanAav) {
            logger.error("forcing recreating views!");
            SQLUtils.executeQuery(conn, "UPDATE amp_global_settings SET settingsvalue = 'true' WHERE settingsname='Recreate the views on the next server restart'");
            createDummyViewIfMissingOrTable(conn, "amp_activity",
                    "SELECT aav.* from amp_activity_version aav "
                            + "JOIN amp_activity_group aag ON aav.amp_activity_id = aag.amp_activity_last_version_id "
                            + "AND (aav.deleted IS NULL or aav.deleted = false)");
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
                if (shouldRunPatch) {
                    logger.info("running patch " + patch.id);
                    executePatch(patch, conn, hashes.isEmpty());
                }
            }
            defineActivityVersionsViews(conn);
            runDrcCleanup(conn);
            createTrickyViewsIfNeeded(conn);
            conn.setAutoCommit(false);
            
            conn.setAutoCommit(autoCommit);
        }
    }
        
    /**
     * (re)define the views which are concerned with activity versioning. Notice that, since some of these views are dependent on AmpARFilter constants, they are being redefined (WITHOUT DROPping) at each startup 
     * @param conn
     */
    void defineActivityVersionsViews(Connection conn) {
        String query = String.format("CREATE OR REPLACE VIEW v_activity_versions AS " + 
            "SELECT aag.amp_activity_group_id, max(aav.amp_activity_id) as amp_activity_latest_validated_id, aag.amp_activity_last_version_id " + 
            "FROM amp_activity_group aag " + 
            "LEFT JOIN amp_activity_version aav ON (aag.amp_activity_group_id = aav.amp_activity_group_id) " + 
            "AND (aav.deleted IS NULL OR aav.deleted = false) AND (aav.draft IS NULL or aav.draft = false) " + 
            "AND (aav.approval_status IN (%s)) " + 
            "GROUP BY aag.amp_activity_group_id", Util.toCSString(AmpARFilter.VALIDATED_ACTIVITY_STATUS));
        SQLUtils.executeQuery(conn, query);
        
        String query2 = "CREATE OR REPLACE VIEW v_activity_latest_and_validated AS " + 
                "SELECT distinct amp_activity_latest_validated_id AS amp_activity_id FROM v_activity_versions WHERE amp_activity_latest_validated_id IS NOT NULL " + 
                "UNION " + 
                "SELECT distinct amp_activity_last_version_id AS amp_activity_id FROM v_activity_versions";
        SQLUtils.executeQuery(conn, query2);
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
