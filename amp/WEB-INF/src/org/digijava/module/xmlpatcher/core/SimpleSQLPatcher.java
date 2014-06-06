package org.digijava.module.xmlpatcher.core;

import java.sql.Connection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.module.aim.helper.Constants;

/**
 * quick & dirty SQL patches made for ugly things like DROPping tables or columns from entities which would otherwise have been locked by Hibernate
 * use the fully-featured patcher for "normal" patches!
 * <strong>You should only call / use the class BEFORE having initialized Hibernate</strong>
 * @author Dolghier Constantin
 *
 */
public class SimpleSQLPatcher {
	
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
	}};
	DataSource dataSource;
	
	public SimpleSQLPatcher() throws Exception {		
		Context initialContext = new InitialContext();
   		this.dataSource = (javax.sql.DataSource) initialContext.lookup(Constants.UNIFIED_JNDI_ALIAS);
   		if (dataSource == null)
   			throw new RuntimeException("could not find data source!");
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
   			SQLUtils.executeQuery(conn, 
   					"CREATE TABLE IF NOT EXISTS amp_simple_sql_patches(id varchar(255), hash text, date_applied bigint)");
   			for(SimpleSQLPatch patch:patches){
   				java.util.List<String> hashes = SQLUtils.fetchAsList(conn, "SELECT hash FROM amp_simple_sql_patches WHERE id='" + patch.id + "'", 1);
   				if (hashes.size() > 1)
   					throw new RuntimeException("amp_simple_sql_patches is corrupted, please review code / database (patch with id " + patch.id + " is mentioned > 1 times)");   				
   				boolean shouldRunPatch = hashes.isEmpty() || hashes.get(0).equals(patch.hash);
   				if (shouldRunPatch)
   					executePatch(patch, conn, hashes.isEmpty());
   			}
   			conn.setAutoCommit(false);
   			
   			conn.setAutoCommit(autoCommit);
   		}
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
