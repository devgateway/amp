package org.dgfoundation.amp.mondrian;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.List;
import java.util.TreeSet;

import org.apache.jackrabbit.core.fs.db.DatabaseFileSystem;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.I18nDatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.time.StopWatch;

import com.google.common.collect.HashBiMap;

import static org.dgfoundation.amp.mondrian.MondrianTablesRepository.FACT_TABLE;


/**
 * the entry point for doing ETL for Mondrian in AMP
 * @author Dolghier Constantin
 *
 */
public class MondrianETL {
		
	public final static String MONDRIAN_EXCHANGE_RATES_TABLE = "mondrian_exchange_rates";
	public final static String MONDRIAN_DATE_TABLE = "mondrian_dates";
	
	/**
	 * the dummy id to insert into the Cartesian product calculated by ETL.
	 * <strong>Do not change this unless you want to face Constantin's wrath</strong>
	 */
	public final static Long MONDRIAN_DUMMY_ID_FOR_ETL = 999999999l;
	
	private final static ReadWriteLockHolder MONDRIAN_LOCK = new ReadWriteLockHolder("Mondrian reports/etl lock");
	
	/**
	 * these two are hardcoded to activities, because pledges lack versioning, ergo a they need a much simpler treatment
	 */
	protected final Set<Long> activityIds;
	protected final Set<Long> activityIdsToRemove;
	
	/**
	 * pledge ids to remove from the fact table and regenerate
	 */
	protected final Set<Long> pledgesToRedo;
	
	protected boolean recreateFactTable;
	
	/**
	 * the postgres connection
	 */
	protected java.sql.Connection conn;
	protected MonetConnection monetConn;
		
	protected static Logger logger = Logger.getLogger(MondrianETL.class);
		
	/**
	 * constructs an instance, does an initial assessment (scans for IDs). The {@link #execute()} method should be run as closely to the constructor as possible (to avoid race conditions)
	 * @param conn
	 * @param activities
	 * @param activitiesToRemove
	 */
	public MondrianETL(java.sql.Connection postgresConn, MonetConnection monetConn, Collection<Long> activities, Collection<Long> activitiesToRemove, Collection<Long> pledgesToRedo) {
		
		logger.warn("Mondrian ETL started");
		
		this.conn = postgresConn;
		this.monetConn = monetConn;
		
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
	
	/**
	 * runs the ETL
	 */
	public void execute(){
		MONDRIAN_LOCK.runUnderWriteLock(new ExceptionRunnable() {
			public void run() throws Exception {
				
				String mondrianEtl = "Mondrian-etl";
			
				StopWatch.reset(mondrianEtl);
				StopWatch.next(mondrianEtl, true, "start");
				checkFactTable();
			
				StopWatch.next(mondrianEtl, true, "checkFactTable");
				deleteStaleFactTableEntries();
			
				StopWatch.next(mondrianEtl, true, "deleteStaleFactTableEntries");
				generateActivitiesEntries();
			
				StopWatch.next(mondrianEtl, true, "generateActivitiesEntries");
				generateExchangeRatesTable();
			
				StopWatch.next(mondrianEtl, true, "generateExchangeRatesTable");
				generateStarTables();
			
				StopWatch.next(mondrianEtl, true, "generateStarTables");
				checkMondrianSanity();
			
				StopWatch.next(mondrianEtl, true, "checkMondrianSanity");
				copyMiscTables();
				
				StopWatch.next(mondrianEtl, true, "copyMiscTables");
				StopWatch.reset(mondrianEtl);
			
				logger.error("done generating ETL");
				SQLUtils.flush(conn);
				SQLUtils.flush(monetConn.conn);
				conn.close();
			};
		});
	}
	
	protected void copyMiscTables() throws SQLException {
		monetConn.copyTableFromPostgres(this.conn, "amp_category_value");
		monetConn.copyTableFromPostgres(this.conn, "amp_category_class");
	}
	
	protected void checkMondrianSanity() {
		String query = "SELECT DISTINCT(mft.date_code) FROM mondrian_fact_table mft WHERE NOT EXISTS (SELECT exchange_rate FROM mondrian_exchange_rates mer WHERE mer.day_code = mft.date_code)";
		List<Long> days = SQLUtils.fetchLongs(monetConn.conn, query);
		if (!days.isEmpty()) {
			logger.error("after having run the ETL, some days do not have a corresponding exchange rate entry: " + days.toString());
			throw new RuntimeException("MONDRIAN ETL BUG: some days have missing exchange rate entries and will not exist in the generated reports: " + days);
		}
		
		query = "SELECT DISTINCT(mft.date_code) FROM mondrian_fact_table mft WHERE NOT EXISTS (SELECT full_date FROM " + MONDRIAN_DATE_TABLE + " mdt WHERE mdt.day_code = mft.date_code)";
		days = SQLUtils.fetchLongs(monetConn.conn, query);
		if (!days.isEmpty()) {
			logger.error("after having run the ETL, some days do not have a corresponding DATE entry: " + days.toString());
			throw new RuntimeException("MONDRIAN ETL BUG: some days have missing date entries and will not exist in the generated reports: " + days);
		};		
	}
	
	/**
	 * generates exchange rate entries for the cartesian product (transaction date, currency)
	 * to be called every time:
	 * 1) an exchange rate changes
	 * 2) a new currency is added
	 * 3) a new transaction date appears in the database
	 */
	protected void generateExchangeRatesTable() throws SQLException {
		logger.warn("generating exchange rates ETL...");
		SQLUtils.executeQuery(conn, "DROP TABLE IF EXISTS " + MONDRIAN_EXCHANGE_RATES_TABLE);
		SQLUtils.executeQuery(conn, "CREATE TABLE " + MONDRIAN_EXCHANGE_RATES_TABLE + "(" + 
										"day_code integer NOT NULL," + 
										"currency_id bigint NOT NULL," + 
										"exchange_rate double precision NOT NULL CHECK (exchange_rate > 0)," + 
										"CONSTRAINT day_pkey PRIMARY KEY (day_code, currency_id))");
		
		SortedSet<Long> allDates = new TreeSet<>(SQLUtils.fetchLongs(conn, "select distinct(date_code) as day_code from mondrian_raw_donor_transactions"));
		List<Long> allCurrencies = SQLUtils.fetchLongs(conn, "SELECT amp_currency_id FROM amp_currency");
		for (Long currency:allCurrencies) {
			generateExchangeRateEntriesForCurrency(CurrencyUtil.getAmpcurrency(currency), allDates);
		}
		monetConn.copyTableFromPostgres(this.conn, MONDRIAN_EXCHANGE_RATES_TABLE);
		monetConn.copyTableFromPostgres(this.conn, "amp_currency");
		logger.warn("... done generating exchange rates ETL...");
	}

	/**
	 * generates the exchange rates for the said currency for all the given dates
	 * @param ampCurrencyId
	 * @param allDates
	 * @throws SQLException
	 */
	protected void generateExchangeRateEntriesForCurrency(AmpCurrency currency, SortedSet<Long> allDates) throws SQLException {
		new CurrencyETL(currency, conn).work(allDates);
	}
	
	
	/**
	 * makes a snapshot of the views which back Mondrian ETL columns
	 * @throws SQLException
	 */
	protected void generateStarTables() throws SQLException {
		logger.warn("generating STAR tables...");
		for (MondrianTableDescription mondrianTable:MondrianTablesRepository.MONDRIAN_TRANSLATED_TABLES)
 			generateStarTable(mondrianTable);
		
		generateStarTableWithQueryInPostgres(MONDRIAN_DATE_TABLE, "day_code",
			"SELECT DISTINCT(mft.date_code) AS day_code, mft.transaction_date AS full_date, date_part('year'::text, mft.transaction_date)::integer AS year_code, date_part('month'::text, mft.transaction_date)::integer AS month_code, to_char(mft.transaction_date, 'TMMonth'::text) AS month_name, date_part('quarter'::text, mft.transaction_date)::integer AS quarter_code, ('Q'::text || date_part('quarter'::text, mft.transaction_date)) AS quarter_name " + 
			"FROM mondrian_fact_table mft " + 
			"UNION ALL " + 
			"SELECT 999999999, '9999-1-1', 9999, 99, 'Undefined', 99, 'Undefined' " + 
			"ORDER BY day_code",
			
			Arrays.asList("day_code", "full_date", "year_code", "month_code", "quarter_code"));
		monetConn.copyTableFromPostgres(this.conn, MONDRIAN_DATE_TABLE);
		logger.warn("...generating STAR tables done");
	}

	/**
	 * processes a MondrianTableDescription using {@link #generateStarTableWithQuery(String, String, String...)} (which creates the table and the indices and the collations
	 * @param mondrianTable
	 * @throws SQLException
	 */
	protected void generateStarTable(MondrianTableDescription mondrianTable) throws SQLException {
		//generateStarTableWithQueryToMonet(mondrianTable.tableName, mondrianTable.primaryKeyColumnName, "SELECT * FROM v_" + mondrianTable.tableName, mondrianTable.indexedColumns);
		generateStarTableWithQueryInPostgres(mondrianTable.tableName, mondrianTable.primaryKeyColumnName, "SELECT * FROM v_" + mondrianTable.tableName, mondrianTable.indexedColumns);
		monetConn.copyTableFromPostgres(conn, mondrianTable.tableName);
		//generateStarTableWithQueryToMonet(mondrianTable.tableName, mondrianTable.primaryKeyColumnName, "SELECT * FROM " + mondrianTable.tableName, mondrianTable.indexedColumns);
		
		// now, the base (non-multilingual) dimension table is ready, now we need to make multilingual clones of it
		LinkedHashSet<String> locales = new LinkedHashSet<>(TranslatorUtil.getLocaleCache(SiteUtils.getDefaultSite()));
		for (String locale:locales)
			cloneMondrianTableForLocale(mondrianTable, locale);
	}
	
	/**
	 * makes a localized version of a Mondrian dimension table, using the i18n fetchers
	 * @param mondrianTable
	 * @param locale
	 * @throws SQLException
	 */
	protected void cloneMondrianTableForLocale(MondrianTableDescription mondrianTable, String locale) throws SQLException {
		logger.warn("cloning table " + mondrianTable.tableName + " into locale " + locale);
		String localizedTableName = mondrianTable.tableName + "_" + locale;
		
		List<List<Object>> vals = mondrianTable.readTranslatedTable(this.conn, locale);
		monetConn.copyTableStructureFromPostgres(this.conn, mondrianTable.tableName, localizedTableName);
		SQLUtils.insert(monetConn.conn, localizedTableName, null, null, monetConn.getTableColumns(localizedTableName), vals);

		
		// checking sanity
		long initTableSz = SQLUtils.countRows(conn, mondrianTable.tableName);
		long createdTableSz = SQLUtils.countRows(monetConn.conn, localizedTableName);
		if (initTableSz != createdTableSz)
			throw new RuntimeException(String.format("HUGE BUG: multilingual-cloned dimension table has a size of %d, while the original dimension table has a size of %d", createdTableSz, initTableSz));
	}
	
	
	/**
	 * makes a snapshot of the view v_<strong>tableName</strong> in the table <strong>tableName</strong> and then creates indices on the relevant columns
	 * @param tableName
	 * @param columnsToIndex columns on which to create indices
	 * @throws SQLException
	 */
	protected void generateStarTableWithQueryInPostgres(String tableName, String primaryKey, String tableCreationQuery, Collection<String> columnsToIndex) throws SQLException {
		SQLUtils.executeQuery(conn, "DROP TABLE IF EXISTS " + tableName);
		SQLUtils.executeQuery(conn, "CREATE TABLE " + tableName + " AS " + tableCreationQuery);
		//postprocessDimensionTable(tableName, primaryKey, columnsToIndex);
	}

	
	/**
	 * makes any needed postprocessing on a Mondrian Dimension table (indices, collations, etc)
	 * @param tableName
	 * @param columnsToIndex
	 */
	protected void postprocessDimensionTable(String tableName, String primaryKey, Collection<String> columnsToIndex) {
		// change text column types' collation to C -> 7x faster GROUP BY / ORDER BY for stupid Mondrian
		Map<String, String> tableColumns = SQLUtils.getTableColumnsWithTypes(tableName, true);
		Set<String> textColumnTypes = new HashSet<>(Arrays.asList("text", "character varying", "varchar"));
		for (String columnName:tableColumns.keySet()) {
			String columnType = tableColumns.get(columnName);
			if (textColumnTypes.contains(columnType)) {
				String q = String.format("ALTER TABLE %s ALTER COLUMN %s SET DATA TYPE text COLLATE \"C\"", tableName, columnName);
				SQLUtils.executeQuery(conn, q);
			}
		}
		
		// create indices
		for (String columnToIndex:columnsToIndex) {
			String indexCreationQuery = String.format("CREATE INDEX %s_%s ON %s(%s)", tableName, columnToIndex, tableName, columnToIndex);
			SQLUtils.executeQuery(conn, indexCreationQuery);
		}
		
		// create primary key
		if (primaryKey != null) {
			SQLUtils.executeQuery(conn, String.format("ALTER TABLE %s ADD CONSTRAINT %s_PK PRIMARY KEY (%s)", tableName, tableName, primaryKey));
		}
	}
	
	/**
	 * when exiting this function,
	 * 1. a fact table has been created, if needed
	 * 2. the fact table has been sanity checked
	 */
	protected void checkFactTable() {
		
		// check that the fact table has a sane structure
		// notice that this is also run if we had just recreated it - to make sure everything is ok (better safe than sorry)
		Set<String> factTableColumnNames = monetConn.getTableColumns(FACT_TABLE.tableName);
		for (String colName:factTableColumnNames) {
			recreateFactTable |= !FACT_TABLE.columns.containsKey(colName);
		}

		if (recreateFactTable) {
			recreateFactTable();
		}
	}
	
	protected void recreateFactTable() {
		try {
			//SQLUtils.flushSchemaChanges(conn);
			// creates an empty fact table
			logger.warn("RECREATING Mondrian Fact table");
			monetConn.dropTable(FACT_TABLE.tableName);
			FACT_TABLE.create(monetConn.conn, false);
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * generates the fact table's Cartesian Product sources (prim/sec/tert sectors + programs + locations + orgs) and then, the fact table
	 * @throws SQLException
	 */
	protected void generateActivitiesEntries() throws SQLException{
		generateSectorsEtlTables();
		generateProgramsEtlTables();
		generateLocationsEtlTables();
		generateOrganisationsEtlTables();
		
		generateRawTransactionTables();
		
		generateFactTable();
	}

	protected void generateRawTransactionTables() throws SQLException {
		logger.warn("materializing the raw transactions tables...");
		for (MondrianTableDescription mondrianTable: MondrianTablesRepository.MONDRIAN_RAW_TRANSACTIONS_TABLES) {
			monetConn.createTableFromQuery(this.conn, "SELECT * FROM v_" + mondrianTable.tableName, mondrianTable.tableName);
		}
	}
	
	protected void generateFactTable() throws SQLException {
		logger.warn("running the fact-table-generating cartesian...");
		List<String> factTableQueries = generateFactTableQueries();
		//monetConn.dropTable(FACT_TABLE.tableName);
		for(int i = 0; i < factTableQueries.size(); i++) {
			logger.warn("\texecuting query #" + (i + 1) + "...");
			monetConn.executeQuery(factTableQueries.get(i));
			logger.warn("\t...executing query #" + (i + 1) + " done");
		}
		logger.warn("...running the fact-table-generating cartesian done");
		long factTableSize = SQLUtils.countRows(monetConn.conn, "mondrian_fact_table");
		long nrTransactions = SQLUtils.countRows(monetConn.conn, "mondrian_raw_donor_transactions");
		double explosionFactor = nrTransactions > 0 ? ((1.0 * factTableSize) / nrTransactions) : 1.0;
		logger.warn(
				String.format("fact table generated %d fact table entries using %d initial transactions (multiplication factor: %.2f)", factTableSize, nrTransactions, explosionFactor));
	}
	
	/**
	 * runs a query on the PostgreSQL database, redistributes percentages, then writes results (one-table-per-locale) to Monet
	 * @param query
	 * @param tableName
	 * @throws SQLException
	 */
	protected void runEtlOnTable(String query, String tableName) throws SQLException {
		try (ResultSet rs = SQLUtils.rawRunQuery(conn, query, null)) {
			// Map<activityId, percentages_on_sector_scheme
			Map<Long, PercentagesDistribution> secs = PercentagesDistribution.readInput(ReportEntityType.ENTITY_TYPE_ACTIVITY, rs);
			serializeETLTable(secs, tableName, false);
		}
	}
	
	protected void generateOrganisationsEtlTables() throws SQLException {
		logger.warn("generating orgs ETL tables...");
		runEtlOnTable("select amp_activity_id, amp_org_id, percentage from v_executing_agency", "etl_executing_agencies");
		runEtlOnTable("select amp_activity_id, amp_org_id, percentage from v_beneficiary_agency", "etl_beneficiary_agencies");
		runEtlOnTable("select amp_activity_id, amp_org_id, percentage from v_implementing_agency", "etl_implementing_agencies");
		runEtlOnTable("select amp_activity_id, amp_org_id, percentage from v_responsible_organisation", "etl_responsible_agencies");
	}
	
	/**
	 * cleans up percentages and NULLs for sectors and generates the tables which are the base for the Cartesian Product
	 * @throws SQLException
	 */
	protected void generateSectorsEtlTables() throws SQLException {
		logger.warn("generating Sector ETL tables...");
		generateSectorsEtlTables("Primary");
		generateSectorsEtlTables("Secondary");
		generateSectorsEtlTables("Tertiary");
	}

	/**
	 * cleans up percentages and NULLs for programs and generates the tables which are the base for the Cartesian Product
	 * @throws SQLException
	 */
	protected void generateProgramsEtlTables() throws SQLException {
		logger.warn("generating Program ETL tables...");
		generateProgramsEtlTables("National Plan Objective");
		generateProgramsEtlTables("Primary Program");
		generateProgramsEtlTables("Secondary Program");
		generateProgramsEtlTables("Tertiary Program");
	}

	/**
	 * cleans up percentages and NULLs for locations and generates the tables which are the base for the Cartesian Product
	 * @throws SQLException
	 */
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
	 * creates an ETL table with cleaned-up percentages
	 * TODO: generate one query per N activities instead of a huge SQL query
	 * @param percs
	 * @param tableName
	 * @throws SQLException
	 */
	protected void serializeETLTable(Map<Long, PercentagesDistribution> percs, String tableName, boolean createIndices) throws SQLException {
		monetConn.dropTable(tableName);
		monetConn.executeQuery("CREATE TABLE " + tableName + " (act_id integer, ent_id integer, percentage double)");
		List<List<Object>> entries = new ArrayList<>();
		for (long actId:percs.keySet()) {
			PercentagesDistribution pd = percs.get(actId);
			pd.postProcess(MONDRIAN_DUMMY_ID_FOR_ETL);
			for (Map.Entry<Long, Double> entry:pd.getPercentages().entrySet())
				entries.add(Arrays.<Object>asList(actId, entry.getKey(), entry.getValue() / 100.0));
		}
		SQLUtils.insert(monetConn.conn, tableName, null, null, Arrays.asList("act_id", "ent_id", "percentage"), entries);

		if (createIndices) {
			SQLUtils.executeQuery(conn, String.format("CREATE INDEX %s_act_id_idx ON %s(act_id)", tableName, tableName)); // create index on activityId
			SQLUtils.executeQuery(conn, String.format("CREATE INDEX %s_ent_id_idx ON %s(ent_id)", tableName, tableName)); // create index on entityId (entity=sector/program/org)
			SQLUtils.executeQuery(conn, String.format("CREATE INDEX %s_act_ent_id_idx ON %s(act_id, ent_id)", tableName, tableName)); // create index on entityId (entity=sector/program/org)
		}
		monetConn.flush();
	}
	
	/**
	 * clears the fact table of the stale entries signaled by {@link #activityIdsToRemove} and {@link #pledgesToRedo}
	 */
	protected void deleteStaleFactTableEntries() {
//		String deleteActivitiesQuery = String.format("DELETE FROM mondrian_fact_table WHERE entity_type = '%c' AND entity_id IN (%s)", ReportEntityType.ENTITY_TYPE_ACTIVITY.getAsChar(), Util.toCSStringForIN(activityIdsToRemove));
		String deleteActivitiesQuery = String.format("DELETE FROM mondrian_fact_table WHERE entity_id IN (%s)", Util.toCSStringForIN(activityIdsToRemove));
		Set<Long> pledgesToRedoChanged = new HashSet<>();
		for(Long pledgeId:pledgesToRedo)
			pledgesToRedoChanged.add(pledgeId + AmpReportGenerator.PLEDGES_IDS_START);
		String deletePledgesQuery = String.format("DELETE FROM mondrian_fact_table WHERE entity_id IN (%s)", Util.toCSStringForIN(pledgesToRedoChanged));
		monetConn.executeQuery(deleteActivitiesQuery);
		monetConn.executeQuery(deletePledgesQuery);
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

