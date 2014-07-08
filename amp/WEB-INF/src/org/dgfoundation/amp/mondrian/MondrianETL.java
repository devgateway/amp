package org.dgfoundation.amp.mondrian;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.digijava.module.aim.helper.Constants;


/**
 * the entry point for doing ETL for Mondrian in AMP
 * @author Dolghier Constantin
 *
 */
public class MondrianETL {
	
	public final static String FACT_TABLE_NAME = "mondrian_fact_table";
	
	/**
	 * these two are hardcoded to activities, because pledges lack versioning, ergo a they need a much simpler treatment
	 */
	protected final Set<Long> activityIds;
	protected final Set<Long> activityIdsToRemove;
	
	/**
	 * pledge ids to remove from the fact table and regenerate
	 */
	protected final Set<Long> pledgesToRedo;
	
	protected final boolean recreateFactTable;
	protected java.sql.Connection conn;
		
	protected static Logger logger = Logger.getLogger(MondrianETL.class);
	
	/**
	 * order of iteration is important, thus LinkedHashSet
	 */
	public final static Map<String, FactTableColumn> FACT_TABLE_COLUMNS = new LinkedHashMap<String, FactTableColumn>() {{
			add(new FactTableColumn("entity_type", "char NOT NULL", true)); /* see ReportEntityType.getAsChar() */
			add(new FactTableColumn("entity_id", "integer NOT NULL", true)); // P/A id 
			add(new FactTableColumn("entity_internal_id", "integer NOT NULL", true)); // amp_funding_detail_id, amp_mtef_detail_id, amp_funding_pledges_detail_id
			add(new FactTableColumn("transaction_type", "integer NOT NULL", true)); // ACV
			add(new FactTableColumn("adjustment_type", "integer NOT NULL", true));  // ACV
			add(new FactTableColumn("transaction_date", "date NOT NULL", true));
		
			/**
			 * regarding currencies: if a transaction has a fixed_exchange_rate, BASE_CURRENCY would have been written in currency_id and transaction_amount would be translated
			 */
			add(new FactTableColumn("transaction_amount", "double precision NOT NULL", false)); // comment 
			add(new FactTableColumn("currency_id", "integer NOT NULL", true)); // comment 
		
			add(new FactTableColumn("donor_id", "integer", true)); // amp_org_id, might be null for example for pledges (which originate in donor groups)
			add(new FactTableColumn("financing_instrument_id", "integer", true)); // ACV
			add(new FactTableColumn("terms_of_assistance_id", "integer", true));  // ACV
		
			add(new FactTableColumn("primary_sector_id", "integer", true));   // amp_sector_id, subject to Cartesian product
			add(new FactTableColumn("secondary_sector_id", "integer", true)); // amp_sector_id, subject to Cartesian product
			add(new FactTableColumn("tertiary_sector_id", "integer", true));  // amp_sector_id, subject to Cartesian product
		
			add(new FactTableColumn("location_id", "integer", true)); // amp_category_value_location_id, subject to Cartesian product
		
			add(new FactTableColumn("primary_program_id", "integer", true));   // amp_theme_id, subject to Cartesian product
			add(new FactTableColumn("secondary_program_id", "integer", true)); // amp_theme_id, subject to Cartesian product
			add(new FactTableColumn("tertiary_program_id", "integer", true));  // amp_theme_id, subject to Cartesian product
			add(new FactTableColumn("national_objectives_program_id", "integer", true));  // amp_theme_id, subject to Cartesian product
		
			add(new FactTableColumn("ea_org_id", "integer", true)); // EXEC amp_org_id, subject to Cartesian product
			add(new FactTableColumn("ba_org_id", "integer", true)); // BENF amp_org_id, subject to Cartesian product
			add(new FactTableColumn("ia_org_id", "integer", true)); // IMPL amp_org_id, subject to Cartesian product
			add(new FactTableColumn("ro_org_id", "integer", true)); // RESP amp_org_id, subject to Cartesian product
		
			add(new FactTableColumn("src_role_id", "integer", true));  // amp_role.amp_role_id
			add(new FactTableColumn("dest_role_id", "integer", true)); // amp_role_id
			add(new FactTableColumn("dest_org_id", "integer", true));   // amp_org_id
		}
	
		protected void add(FactTableColumn col) {
			this.put(col.columnName, col);
		}
	};
	
	/**
	 * constructs an instance, does an initial assessment (scans for IDs). The {@link #execute()} method should be run as closely to the constructor as possible (to avoid race conditions)
	 * @param conn
	 * @param activities
	 * @param activitiesToRemove
	 */
	public MondrianETL(java.sql.Connection conn, Collection<Long> activities, Collection<Long> activitiesToRemove, Collection<Long> pledgesToRedo) {
		
		logger.warn("Mondrian ETL started");
		this.conn = conn;
		this.recreateFactTable = activities == null;
		
		this.activityIds = new HashSet<>(activities == null ? this.getAllValidatedAndLatestIds() : activities);
		
		this.activityIdsToRemove = new HashSet<>();
		if (activitiesToRemove != null)
			this.activityIdsToRemove.addAll(activitiesToRemove);
		
		this.pledgesToRedo = new HashSet<>();
		if (pledgesToRedo != null)
			this.pledgesToRedo.addAll(pledgesToRedo);
		
	}
	
	protected Set<Long> getAllValidatedAndLatestIds() {
		Set<Long> latestIds = new TreeSet<Long>(SQLUtils.<Long>fetchAsList(conn, "SELECT amp_activity_id FROM amp_activity", 1));
		Set<Long> latestValidatedIds = new TreeSet<Long>(SQLUtils.<Long>fetchAsList(conn,
				"SELECT aag.amp_activity_group_id, max(aav.amp_activity_id) FROM amp_activity_version aav, amp_activity_group aag WHERE aag.amp_activity_group_id = aav.amp_activity_group_id AND (aav.deleted IS NULL OR aav.deleted = false) AND aav.approval_status IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") GROUP BY aag.amp_activity_group_id", 2));
		
		logger.warn("last activity version ids: " + latestIds.toString());
		logger.warn("last validated activity version ids: " + latestIds.toString());
		Set<Long> res = new TreeSet<Long>();
		res.addAll(latestIds);
		res.addAll(latestValidatedIds);
		return res;
	}
	
	public void execute() throws SQLException{
		checkFactTable();
		deleteStaleFactTableEntries();
		generateActivitiesEntries();
		logger.error("done generating ETL");
	}
	
	/**
	 * when exiting this function,
	 * 1. a fact table has been created, if needed
	 * 2. the fact table has been sanity checked
	 */
	protected void checkFactTable() {
		if (recreateFactTable) {
			// creates an empty fact table
			logger.warn("RECREATING Mondrian Fact table");
			SQLUtils.executeQuery(conn, "DROP TABLE IF EXISTS " + FACT_TABLE_NAME);
			StringBuffer query = new StringBuffer(String.format("CREATE TABLE %s (", FACT_TABLE_NAME));
			boolean first = true;
			for (FactTableColumn col:FACT_TABLE_COLUMNS.values()) {
				if (!first) query.append(", ");
				query.append(String.format("%s %s", col.columnName, col.columnDefinition));
				first = false;
			}
			query.append(")");
			SQLUtils.executeQuery(conn, query.toString());
			
			// create indices
			logger.warn("Creating Mondrian indices");
			for(FactTableColumn col:FACT_TABLE_COLUMNS.values())
				if (col.indexed) {
					String q = String.format("CREATE INDEX %s_%s_idx ON %s(%s)", FACT_TABLE_NAME, col.columnName, FACT_TABLE_NAME, col.columnName);
					SQLUtils.executeQuery(conn, q);
				}
		}
		
		// check that the fact table has a sane structure
		// notice that this is also run if we had just recreated it - to make sure everything is ok (better safe than sorry)
		Set<String> factTableColumnNames = SQLUtils.getTableColumns(FACT_TABLE_NAME);
		for (String colName:factTableColumnNames) {
			if (!FACT_TABLE_COLUMNS.containsKey(colName))
				throw new RuntimeException(String.format("the fact table does not contain the mandatory column %s", colName));
		}		
	}
	
	protected void generateActivitiesEntries() throws SQLException{
		generateSectorsEtlTables();
		generateProgramsEtlTables();
		generateLocationsEtlTables();
		generateOrganisationsEtlTables();
		
		generateFactTable();
	}

	protected void generateFactTable() throws SQLException {
		logger.warn("running the fact-table-generating cartesian...");
		List<String> factTableQueries = generateFactTableQueries();
		for(int i = 0; i < factTableQueries.size(); i++) {
			logger.warn("\texecuting query #" + (i + 1) + "...");
			SQLUtils.executeQuery(conn, factTableQueries.get(i));
			logger.warn("\t...executing query #" + (i + 1) + " done");
		}
		logger.warn("...running the fact-table-generating cartesian done");
	}
	
	protected void runEtlOnTable(String query, String tableName) throws SQLException {
		try (ResultSet rs = SQLUtils.rawRunQuery(conn, query, null)) {
			// Map<activityId, percentages_on_sector_scheme
			Map<Long, PercentagesDistribution> secs = PercentagesDistribution.readInput(ReportEntityType.ENTITY_TYPE_ACTIVITY, rs);
			serializeETLTable(secs, tableName);
		}
	}
	
	protected void generateOrganisationsEtlTables() throws SQLException {
		logger.warn("generating orgs ETL tables...");
		runEtlOnTable("select amp_activity_id, amp_org_id, percentage from v_executing_agency", "etl_executing_agencies");
		runEtlOnTable("select amp_activity_id, amp_org_id, percentage from v_beneficiary_agency", "etl_beneficiary_agencies");
		runEtlOnTable("select amp_activity_id, amp_org_id, percentage from v_implementing_agency", "etl_implementing_agencies");
		runEtlOnTable("select amp_activity_id, amp_org_id, percentage from v_responsible_organisation", "etl_responsible_agencies");
	}
	
	protected void generateSectorsEtlTables() throws SQLException {
		logger.warn("generating Sector ETL tables...");
		generateSectorsEtlTables("Primary");
		generateSectorsEtlTables("Secondary");
		generateSectorsEtlTables("Tertiary");
	}

	protected void generateProgramsEtlTables() throws SQLException {
		logger.warn("generating Program ETL tables...");
		generateProgramsEtlTables("National Plan Objective");
		generateProgramsEtlTables("Primary Program");
		generateProgramsEtlTables("Secondary Program");
		generateProgramsEtlTables("Tertiary Program");
	}
	
	protected void generateLocationsEtlTables() throws SQLException {
		logger.warn("generating location ETL tables...");
		runEtlOnTable("select aal.amp_activity_id, acvl.id, aal.location_percentage from amp_activity_location aal, amp_category_value_location acvl, amp_location al WHERE aal.amp_location_id = al.amp_location_id AND al.location_id = acvl.id", "etl_locations");
	}
	
	protected void generateProgramsEtlTables(String schemeName) throws SQLException {
		String query = String.format("select aap.amp_activity_id, aap.amp_program_id, aap.program_percentage FROM amp_activity_program aap, v_mondrian_programs vmp WHERE aap.amp_program_id = vmp.amp_theme_id and vmp.program_setting_name = '%s'", schemeName);
		runEtlOnTable(query, "etl_activity_program_" + schemeName.replace(' ', '_').toLowerCase());
	}
	
	protected void generateSectorsEtlTables(String schemeName) throws SQLException {
		String query = String.format("select aas.amp_activity_id, aas.amp_sector_id, aas.sector_percentage from amp_activity_sector aas, v_mondrian_sectors vms WHERE aas.amp_Sector_id = vms.amp_sector_id AND vms.typename='%s'", schemeName);
		runEtlOnTable(query, "etl_activity_sector_" + schemeName.toLowerCase());
	}
	
	/**
	 * creates an ETL table with percentages
	 * TODO: generate one query per N activities instead of a huge SQL query
	 * @param percs
	 * @param tableName
	 * @throws SQLException
	 */
	protected void serializeETLTable(Map<Long, PercentagesDistribution> percs, String tableName) throws SQLException {
		SQLUtils.executeQuery(conn, "DROP TABLE IF EXISTS " + tableName);
		SQLUtils.executeQuery(conn, "CREATE TABLE " + tableName + " (act_id integer, ent_id integer, percentage double precision)");
		StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (act_id, ent_id, percentage) VALUES ");
		boolean isFirst = true;
		for (long actId:percs.keySet()) {
			// build an SQL query for each activityId
			PercentagesDistribution pd = percs.get(actId);
			pd.postProcess();
			for (Map.Entry<Long, Double> entry:pd.getPercentages().entrySet()) {
				if (!isFirst)
					query.append(", \n");
				query.append(String.format("(%d, %d, %.3f)", actId, entry.getKey(), entry.getValue() / 100.0));
				isFirst = false;
			}
		}
		if (!isFirst) {
			SQLUtils.executeQuery(conn, query.toString());
		}
		SQLUtils.executeQuery(conn, String.format("CREATE INDEX %s_act_id_idx ON %s(act_id)", tableName, tableName)); // create index on activityId
		SQLUtils.executeQuery(conn, String.format("CREATE INDEX %s_ent_id_idx ON %s(ent_id)", tableName, tableName)); // create index on entityId (entity=sector/program/org)
		SQLUtils.executeQuery(conn, String.format("CREATE INDEX %s_act_ent_id_idx ON %s(act_id, ent_id)", tableName, tableName)); // create index on entityId (entity=sector/program/org)
	}
	
	/**
	 * clears the fact table of the stale entries signaled by {@link #activityIdsToRemove} and {@link #pledgesToRedo}
	 */
	protected void deleteStaleFactTableEntries() {
		String deleteActivitiesQuery = String.format("DELETE FROM mondrian_fact_table WHERE entity_type = '%c' AND entity_id IN (%s)", ReportEntityType.ENTITY_TYPE_ACTIVITY.getAsChar(), Util.toCSStringForIN(activityIdsToRemove));
		String deletePledgesQuery = String.format("DELETE FROM mondrian_fact_table WHERE entity_type = '%c' AND entity_id IN (%s)", ReportEntityType.ENTITY_TYPE_PLEDGE.getAsChar(), Util.toCSStringForIN(pledgesToRedo));
		SQLUtils.executeQuery(conn, deleteActivitiesQuery);
		SQLUtils.executeQuery(conn, deletePledgesQuery);
	}
	
	/**
	 * reads from factTableQuery the list of queries
	 * @return
	 */
	protected List<String> generateFactTableQueries() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("factTableQuery.sql"), "utf-8"));
			StringBuilder builder = new StringBuilder();
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				if (line.trim().startsWith("--"))
					continue;
				builder.append(line);
				builder.append(" ");
			};
			reader.close();
			String str = builder.toString().trim();
			StringTokenizer stok = new StringTokenizer(str, ";");
			List<String> res = new ArrayList<>();
			while (stok.hasMoreTokens())
				res.add(stok.nextToken());
			return res;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}

