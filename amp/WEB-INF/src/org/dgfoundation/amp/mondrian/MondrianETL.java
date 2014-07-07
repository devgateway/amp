package org.dgfoundation.amp.mondrian;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.ReportEntityType;

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
			add(new FactTableColumn("transaction_amount", "double NOT NULL", true)); // comment 
			add(new FactTableColumn("currency_id", "integer NOT NULL", true)); // comment 
		
			add(new FactTableColumn("donor_id", "integer", false)); // amp_org_id, might be null for example for pledges (which originate in donor groups)
			add(new FactTableColumn("financing_instrument_id", "integer", false)); // ACV
			add(new FactTableColumn("terms_of_assistance_id", "integer", false));  // ACV
		
			add(new FactTableColumn("primary_sector_id", "integer", false));   // amp_sector_id, subject to Cartesian product
			add(new FactTableColumn("secondary_sector_id", "integer", false)); // amp_sector_id, subject to Cartesian product
			add(new FactTableColumn("tertiary_sector_id", "integer", false));  // amp_sector_id, subject to Cartesian product
		
			add(new FactTableColumn("location_id", "integer", false)); // amp_category_value_location_id, subject to Cartesian product
		
			add(new FactTableColumn("primary_program_id", "integer", false));   // amp_theme_id, subject to Cartesian product
			add(new FactTableColumn("secondary_program_id", "integer", false)); // amp_theme_id, subject to Cartesian product
			add(new FactTableColumn("tertiary_program_id", "integer", false));  // amp_theme_id, subject to Cartesian product
			add(new FactTableColumn("national_objectives_program_id", "integer", false));  // amp_theme_id, subject to Cartesian product
		
			add(new FactTableColumn("ea_org_id", "integer", false)); // EXEC amp_org_id, subject to Cartesian product
			add(new FactTableColumn("ba_org_id", "integer", false)); // BENF amp_org_id, subject to Cartesian product
			add(new FactTableColumn("ia_org_id", "integer", false)); // IMPL amp_org_id, subject to Cartesian product
			add(new FactTableColumn("ro_org_id", "integer", false)); // RESP amp_org_id, subject to Cartesian product
		
			add(new FactTableColumn("src_role_id", "integer", false));  // amp_role.amp_role_id
			add(new FactTableColumn("dest_role_id", "integer", false)); // amp_role_id
			add(new FactTableColumn("dest_org_id", "integer", false));   // amp_org_id		
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
		Set<Long> latestIds = new TreeSet<Long>(SQLUtils.<Long>fetchAsList(conn, "SELECT amp_activity_last_version_id FROM amp_activity_group", 1));
		Set<Long> latestValidatedIds = new TreeSet<Long>(SQLUtils.<Long>fetchAsList(conn,
				"SELECT aag.amp_activity_group_id, max(aav.amp_activity_id) FROM amp_activity_version aav, amp_activity_group aag WHERE aag.amp_activity_group_id = aav.amp_activity_group_id AND (aav.deleted IS NULL OR aav.deleted = false) AND aav.approval_status IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") GROUP BY aag.amp_activity_group_id", 2));
		
		logger.warn("last activity version ids: " + latestIds.toString());
		logger.warn("last validated activity version ids: " + latestIds.toString());
		Set<Long> res = new TreeSet<Long>();
		res.addAll(latestIds);
		res.addAll(latestValidatedIds);
		return res;
	}
	
	protected void execute() throws SQLException{
		checkFactTable();
		deleteStaleFactTableEntries();
		generateActivitiesEntries();
	}
	
	/**
	 * when exiting this function,
	 * 1. a fact table has been created, if needed
	 * 2. the fact table has been sanity checked
	 */
	protected void checkFactTable() {
		if (recreateFactTable) {
			// creates an empty fact table
			SQLUtils.executeQuery(conn, "DROP TABLE IF EXISTS" + FACT_TABLE_NAME);
			StringBuffer query = new StringBuffer(String.format("CREATE TABLE %s (", FACT_TABLE_NAME));
			boolean first = true;
			for (FactTableColumn col:FACT_TABLE_COLUMNS.values()) {
				if (!first) query.append(", ");
				query.append(String.format("%s %s", col.columnName, col.columnDefinition));
				first = false;
			}
			query.append(")");
			SQLUtils.executeQuery(conn, query.toString());
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
		String query = "SELECT af.amp_activity_id, af.amp_funding_id, af.amp_donor_org_id, af.type_of_assistance_category_va, af.financing_instr_category_value, af.source_role_id, afd.amp_fund_detail_id, afd.transaction_type, afd.adjustment_type, afd.transaction_date, afd.transaction_amount, afd.fixed_exchange_rate, afd.amp_currency_id, afd.pledge_id, afd.recipient_org_id, afd.recipient_role_id " + 
				String.format("FROM amp_funding_detail afd, amp_funding af WHERE afd.amp_funding_id = af.amp_funding_id AND af.amp_activity_id IN (%s)", Util.toCSStringForIN(activityIds));
		
		ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
		Map<String, Set<Long>> entitiesWithDistributions = new HashMap<>();
		entitiesWithDistributions.putAll(getAllSectorsForActivities());
	}
	
//	/**
//	 * 
//	 * @param cartesianHolder: Map<field_name, Map<entityId, Map<field_id, percentage>>>, e.g. for example Map<"primary_sector", Map<activityId, Map<sector_id, 33>>>
//	 * @param column
//	 * @param query
//	 */
//	protected void checkAndDistributePercentages(Map<String, Map<Long, >> cartesianHolder, FactTableColumn column, String query) {
//		ResultSet rs = SQLUtils.rawRunQuery(conn, query, null);
//		Map<Lon
//	}
	
	protected Map<String, Set<Long>> getAllSectorsForActivities() throws SQLException {
		String query = "select asl.amp_activity_id, asl.amp_sector_id, asl.sector_percentage from v_mondrian_sectors vms, amp_activity_sector asl where asl.amp_sector_id = vms.amp_sector_id AND vms.typename='Primary' ORDER BY amp_activity_id";
		ResultSet rs = SQLUtils.rawRunQuery(conn, query, null); 
		
		Map<Long, String> sectorIdToSectorScheme = new HashMap<>();
		while (rs.next())
			sectorIdToSectorScheme.put(rs.getLong("amp_sector_id"), rs.getString("name"));
		return null;
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
}

