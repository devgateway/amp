package org.dgfoundation.amp.mondrian;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
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

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.jobs.EtlJob;
import org.dgfoundation.amp.mondrian.jobs.Fingerprint;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.dgfoundation.amp.newreports.ReportEntityType;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.time.StopWatch;
import org.hibernate.SQLQuery;

import com.google.common.base.Predicate;

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
	 * the Postgres timestamp when this ETL process started
	 */
	protected final long currentEtlTime;
	
	/**
	 * the postgres timestamp when the previous ETL was started
	 */
	protected final long previousEtlTime;
	
	/**
	 * activities to add/remove from all the relevant tables
	 */
	protected final Set<Long> activityIds;

	/**
	 * pledge ids to remove from the fact table and regenerate
	 */
	protected Set<Long> pledgeIds;
	
	/**
	 * Julian date codes to add/remove from the relevant tables. null means "all"
	 */
	protected final Set<Long> dateCodes;
	
	protected final boolean etlFromScratch;
	
	//protected boolean recreateFactTable;
	
	/**
	 * the postgres connection
	 */
	protected java.sql.Connection conn;
	protected MonetConnection monetConn;
	
	protected final static Fingerprint ETL_TIME_FINGERPRINT = new Fingerprint("etl_time", new ArrayList<String>(), "-1");
		
	protected static Logger logger = Logger.getLogger(MondrianETL.class);
		
	/**
	 * constructs an instance, does an initial assessment (scans for IDs). The {@link #execute()} method should be run as closely to the constructor as possible (to avoid race conditions)
	 * @param conn
	 * @param activities
	 * @param activitiesToRemove
	 */
	public MondrianETL(java.sql.Connection postgresConn, MonetConnection monetConn) {
		
		logger.warn("Mondrian ETL started");
		
		this.conn = postgresConn;
		this.monetConn = monetConn;
		
		this.etlFromScratch = shouldRecreateFactTable();
		this.currentEtlTime = (Long) SQLUtils.fetchAsList(conn, "SELECT cast (extract(epoch from statement_timestamp()) as bigint)", 1).get(0);
		this.previousEtlTime = Long.valueOf(ETL_TIME_FINGERPRINT.readOrReturnDefaultFingerprint(monetConn));
		this.activityIds = calculateAffectedActivities();
		this.dateCodes = calculateAffectedDateCodes();
		
		
		logger.info("running ETL on the following activities: " + 
				(activityIds.size() > 20 ? String.format("[%d] activities", activityIds.size()) : activityIds.toString()));
		
		logger.info("running ETL on the following dates: " + (dateCodes == null ? "(all)" : dateCodes.toString()));
		//this.pledgesToRedo = new HashSet<>();
		//if (pledgesToRedo != null)
		//	this.pledgesToRedo.addAll(pledgesToRedo);		
	}
	
	private Set<Long> calculateAffectedActivities() {
		if (previousEtlTime > 0 && !etlFromScratch) {
			String affectedRawActivitiesQuery = "SELECT DISTINCT(entity_id) FROM amp_etl_changelog WHERE (event_date > " + previousEtlTime + ") AND entity_name='activity'";
			Set<Long> res = new TreeSet<>(SQLUtils.<Long>fetchAsList(conn, affectedRawActivitiesQuery, 1));
			return res;
		}
		else
			return getAllValidatedAndLatestIds();
	}
	
	private Set<Long> calculateAffectedDateCodes() {
		if (previousEtlTime > 0 && !etlFromScratch) {
			String affectedDatesQuery = "SELECT DISTINCT(entity_id) FROM amp_etl_changelog WHERE (event_date > " + previousEtlTime + ") AND entity_name='exchange_rate'";
			Set<Long> res = new HashSet<>(SQLUtils.<Long>fetchAsList(conn, affectedDatesQuery, 1));
			return res;
		}
		else
			return null;
	}
	
	protected Set<Long> getAllValidatedAndLatestIds() {
		Set<Long> latestIds = new TreeSet<Long>(SQLUtils.<Long>fetchAsList(conn, "SELECT amp_activity_id FROM amp_activity", 1));
		Set<Long> latestValidatedIds = new TreeSet<Long>(SQLUtils.<Long>fetchAsList(conn,
				"SELECT aag.amp_activity_group_id, max(aav.amp_activity_id) FROM amp_activity_version aav, amp_activity_group aag WHERE aag.amp_activity_group_id = aav.amp_activity_group_id AND (aav.deleted IS NULL OR aav.deleted = false) AND aav.approval_status IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") GROUP BY aag.amp_activity_group_id", 2));
		
//		logger.warn("last activity version ids: " + latestIds.toString());
//		logger.warn("last validated activity version ids: " + latestIds.toString());
		Set<Long> res = new TreeSet<Long>();
		res.addAll(latestIds);
		res.addAll(latestValidatedIds);
		return res;
	}
	
	protected final static String MONDRIAN_ETL = "Mondrian-etl";
	
	/**
	 * runs the ETL
	 */
	public void execute() {
		
		MONDRIAN_LOCK.runUnderWriteLock(new ExceptionRunnable<Exception>() {
			@Override public void run() throws Exception {
			
				StopWatch.reset(MONDRIAN_ETL);
				
				StopWatch.next(MONDRIAN_ETL, true, "start");
				
				if (shouldRecreateFactTable())
					recreateFactTable();
				StopWatch.next(MONDRIAN_ETL, true, "checkFactTable");
				
//				deleteStaleFactTableEntries();
//				StopWatch.next(MONDRIAN_ETL, true, "deleteStaleFactTableEntries");
				
				generateActivitiesEntries();			
				StopWatch.next(MONDRIAN_ETL, true, "generateActivitiesEntries");
				
				doExchangeRatesEtl();
				generateStarTables(); // differential
			
				StopWatch.next(MONDRIAN_ETL, true, "generateStarTables");
				checkMondrianSanity();
			
				StopWatch.next(MONDRIAN_ETL, true, "checkMondrianSanity");
				copyMiscTables();
				
				StopWatch.next(MONDRIAN_ETL, true, "copyMiscTables");
				StopWatch.reset(MONDRIAN_ETL);
			
				ETL_TIME_FINGERPRINT.serializeFingerprint(monetConn, Long.toString(currentEtlTime));
				
				logger.error("done generating ETL");
				SQLUtils.flush(conn);
				SQLUtils.flush(monetConn.conn);
				conn.close();
			};
		});
	}
	
	protected void doExchangeRatesEtl() throws SQLException {
		new CalculateExchangeRatesEtlJob(null, conn, monetConn, activityIds, dateCodes).work();
//		Fingerprint fp = CalculateExchangeRatesEtlJob.getFingerprint();
//		fp.runIfFingerprintChanged(conn, monetConn, stepSkipped, new ExceptionRunnable<SQLException>() {
//			@Override public void run() throws SQLException {
//				
//			}});
		StopWatch.next(MONDRIAN_ETL, true, "generateExchangeRatesColumns");
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
	
//	protected String buildMftQuery() throws SQLException {
//		ResultSet dayCodes = SQLUtils.rawRunQuery(monetConn.conn, "SELECT DISTINCT(date_code), transaction_date FROM mondrian_fact_table", null);
//		StringBuilder mftQuery = new StringBuilder("(SELECT mft.date_code, mft.transaction_date FROM (values");
//		boolean isFirst = true;
//		while (dayCodes.next()) {
//			if (!isFirst)
//				mftQuery.append(",");
//			mftQuery.append(String.format("(%d, '%s')", dayCodes.getLong(1), dayCodes.getString(2)));
//			isFirst = false;
//		}
//		mftQuery.append(") as mft(date_code, transaction_date)) mft");
//		return mftQuery.toString();
//	}
	
	/**
	 * callback for "hash equals, not redoing part of ETL"
	 */
	protected Predicate<Fingerprint> stepSkipped = new Predicate<Fingerprint>() {
		@Override public boolean apply(Fingerprint obj) {
			logger.info(String.format("skipping calculating %s, as hash not changed", obj.keyName));
			return true;
		}
	};
	
	/**
	 * makes a snapshot of the views which back Mondrian ETL columns
	 * @throws SQLException
	 */
	protected void generateStarTables() throws SQLException {
		logger.warn("generating STAR tables...");
		for (final MondrianTableDescription mondrianTable:MondrianTablesRepository.MONDRIAN_TRANSLATED_TABLES)
			mondrianTable.fingerprint.runIfFingerprintChanged(conn, monetConn, stepSkipped, new ExceptionRunnable<SQLException>() {
				@Override public void run() throws SQLException {
					generateStarTable(mondrianTable);
				}});
				
		generateMondrianDateTable();
		logger.warn("...generating STAR tables done");
	}

	protected void generateMondrianDateTable() throws SQLException {
		if (etlFromScratch || (!monetConn.tableExists(MONDRIAN_DATE_TABLE))) {
			
			generateStarTableWithQueryInPostgres(MONDRIAN_DATE_TABLE, "date_code", 
				"SELECT to_char(transaction_date, 'J')::integer AS day_code, (CAST (transaction_date as date)) AS full_date, date_part('year'::text, (CAST (transaction_date as date)))::integer AS year_code, date_part('month'::text, (CAST (transaction_date as date)))::integer AS month_code, to_char((CAST (transaction_date as date)), 'TMMonth'::text) AS month_name, date_part('quarter'::text, (CAST (transaction_date as date)))::integer AS quarter_code, ('Q'::text || date_part('quarter'::text, (CAST (transaction_date as date)))) AS quarter_name " + 
				" FROM generate_series('1970-1-1', '2050-1-1', interval '1 day') transaction_date " + 
				" UNION ALL " + 
				" SELECT 999999999, '9999-1-1', 9999, 99, 'Undefined', 99, 'Undefined' " + 
				" ORDER BY day_code",
				new ArrayList<String>());
			monetConn.copyTableFromPostgres(conn, MONDRIAN_DATE_TABLE);

		}
//		Fingerprint fp = new Fingerprint(MONDRIAN_DATE_TABLE, Arrays.asList(
//				Fingerprint.buildTableHashingQuery("v_mondrian_raw_donor_transactions")));
//		
//		fp.runIfFingerprintChanged(conn, monetConn, stepSkipped, new ExceptionRunnable<SQLException>() {
//		
//			@Override public void run() throws SQLException {
//				String mftQuery = buildMftQuery();
//				//select mft.date_code FROM (values (1), (2), (3)) as mft(date_code)
//				generateStarTableWithQueryInPostgres(MONDRIAN_DATE_TABLE, "date_code",
//						"SELECT DISTINCT(mft.date_code) AS day_code, (CAST (mft.transaction_date as date)) AS full_date, date_part('year'::text, (CAST (mft.transaction_date as date)))::integer AS year_code, date_part('month'::text, (CAST (mft.transaction_date as date)))::integer AS month_code, to_char((CAST (mft.transaction_date as date)), 'TMMonth'::text) AS month_name, date_part('quarter'::text, (CAST (mft.transaction_date as date)))::integer AS quarter_code, ('Q'::text || date_part('quarter'::text, (CAST (mft.transaction_date as date)))) AS quarter_name " + 
//								"FROM " + mftQuery +  
//								" UNION ALL " + 
//								" SELECT 999999999, '9999-1-1', 9999, 99, 'Undefined', 99, 'Undefined' " + 
//								" ORDER BY day_code",
//								new ArrayList<String>());
//				monetConn.copyTableFromPostgres(conn, MONDRIAN_DATE_TABLE);
//			}});		
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
		SQLUtils.flush(conn);
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
	 * checks that the fact table should be recreated
	 * @return
	 */
	private boolean shouldRecreateFactTable() {
		
		// check that the fact table has a sane structure
		// notice that this is also run if we had just recreated it - to make sure everything is ok (better safe than sorry)
		boolean recreateFactTable = false;
		Set<String> factTableColumnNames = monetConn.getTableColumns(FACT_TABLE.tableName);
		for (String colName:FACT_TABLE.columns.keySet()) {
			recreateFactTable |= !factTableColumnNames.contains(colName);
		}
		return recreateFactTable;
	}

	/**
	 * drops preexisting fact table and creates an empty one
	 */
	protected void recreateFactTable() {
		try {
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
			String query = "SELECT * FROM v_" + mondrianTable.tableName + " WHERE amp_activity_id IN (" + Util.toCSStringForIN(activityIds) + ")";
			if (etlFromScratch || !monetConn.tableExists(mondrianTable.tableName)) {
				monetConn.createTableFromQuery(this.conn, query, mondrianTable.tableName);
			} else
			{
				monetConn.copyEntries(mondrianTable.tableName, SQLUtils.rawRunQuery(conn, query, null));
			}
		}
	}
	
	protected void generateFactTable() throws SQLException {
		logger.warn("running the fact-table-generating cartesian...");
		List<String> factTableQueries = generateFactTableQueries();
		//monetConn.dropTable(FACT_TABLE.tableName);
		for(int i = 0; i < factTableQueries.size(); i++) {
			logger.warn("\texecuting query #" + (i + 1) + "...");
			String query = factTableQueries.get(i);
			monetConn.executeQuery(query);
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
		if (activityIds.isEmpty())
			return;
		logger.warn("generating orgs ETL tables...");
		String activitiesCondition = this.etlFromScratch ? "1=1" : "amp_activity_id IN (" + Util.toCSStringForIN(this.activityIds) + ")";
		runEtlOnTable(String.format("select amp_activity_id, amp_org_id, percentage from v_executing_agency WHERE (%s)", activitiesCondition), "etl_executing_agencies");
		runEtlOnTable(String.format("select amp_activity_id, amp_org_id, percentage from v_beneficiary_agency WHERE (%s)", activitiesCondition), "etl_beneficiary_agencies");
		runEtlOnTable(String.format("select amp_activity_id, amp_org_id, percentage from v_implementing_agency WHERE (%s)", activitiesCondition), "etl_implementing_agencies");
		runEtlOnTable(String.format("select amp_activity_id, amp_org_id, percentage from v_responsible_organisation WHERE (%s)", activitiesCondition), "etl_responsible_agencies");
	}
	
	/**
	 * cleans up percentages and NULLs for sectors and generates the tables which are the base for the Cartesian Product
	 * @throws SQLException
	 */
	protected void generateSectorsEtlTables() throws SQLException {
		if (activityIds.isEmpty())
			return;
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
		if (activityIds.isEmpty())
			return;
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
		if (activityIds.isEmpty())
			return;
		logger.warn("generating location ETL tables...");
		String activitiesCondition = this.etlFromScratch ? "1=1" : "aal.amp_activity_id IN (" + Util.toCSStringForIN(this.activityIds) + ")";
		String query = String.format("select aal.amp_activity_id, acvl.id, aal.location_percentage from amp_activity_location aal, amp_category_value_location acvl, amp_location al WHERE (%s) AND (aal.amp_location_id = al.amp_location_id) AND (al.location_id = acvl.id)", activitiesCondition);
		runEtlOnTable(query, "etl_locations");
	}
	
	protected void generateProgramsEtlTables(String schemeName) throws SQLException {
		if (activityIds.isEmpty())
			return;
		String activitiesCondition = this.etlFromScratch ? "1=1" : "aap.amp_activity_id IN (" + Util.toCSStringForIN(this.activityIds) + ")";
		String query = String.format("select aap.amp_activity_id, aap.amp_program_id, aap.program_percentage FROM amp_activity_program aap, v_mondrian_programs vmp WHERE (%s) AND (aap.amp_program_id = vmp.amp_theme_id) AND (vmp.program_setting_name = '%s')", activitiesCondition, schemeName);
		runEtlOnTable(query, "etl_activity_program_" + schemeName.replace(' ', '_').toLowerCase());
	}
	
	protected void generateSectorsEtlTables(String schemeName) throws SQLException {
		if (activityIds.isEmpty())
			return;
		String activitiesCondition = this.etlFromScratch ? "1=1" : "aas.amp_activity_id IN (" + Util.toCSStringForIN(this.activityIds) + ")";
		String query = String.format("select aas.amp_activity_id, aas.amp_sector_id, aas.sector_percentage from amp_activity_sector aas, v_mondrian_sectors vms WHERE (%s) AND (aas.amp_sector_id = vms.amp_sector_id) AND (vms.typename='%s')", activitiesCondition, schemeName);
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
		boolean createTable = this.etlFromScratch || !monetConn.tableExists(tableName);
		if (createTable) {
			monetConn.dropTable(tableName);
			monetConn.executeQuery("CREATE TABLE " + tableName + " (act_id integer, ent_id integer, percentage double)");
		} else {
			monetConn.executeQuery("DELETE FROM " + tableName + " WHERE act_id IN (" + Util.toCSStringForIN(percs.keySet()) + ")");
		}
		createIndices &= createTable;
		
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
	
//	/**
//	 * clears the fact table of the stale entries signaled by {@link #activityIdsToRemove} and {@link #pledgesToRedo}
//	 */
//	protected void deleteStaleFactTableEntries() {
////		String deleteActivitiesQuery = String.format("DELETE FROM mondrian_fact_table WHERE entity_type = '%c' AND entity_id IN (%s)", ReportEntityType.ENTITY_TYPE_ACTIVITY.getAsChar(), Util.toCSStringForIN(activityIdsToRemove));
//		String deleteActivitiesQuery = String.format("DELETE FROM mondrian_fact_table WHERE entity_id IN (%s)", Util.toCSStringForIN(activityIdsToRemove));
//		Set<Long> pledgesToRedoChanged = new HashSet<>();
//		for(Long pledgeId:pledgesToRedo)
//			pledgesToRedoChanged.add(pledgeId + AmpReportGenerator.PLEDGES_IDS_START);
//		String deletePledgesQuery = String.format("DELETE FROM mondrian_fact_table WHERE entity_id IN (%s)", Util.toCSStringForIN(pledgesToRedoChanged));
//		monetConn.executeQuery(deleteActivitiesQuery);
//		monetConn.executeQuery(deletePledgesQuery);
//	}
	
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
			while (stok.hasMoreTokens()) {
				String q = stok.nextToken();
				if (q.indexOf("@@activityIdCondition@@") != 0 && activityIds.isEmpty())
					continue; // query references activities, but these are empty -> it is useless
				res.add(q.replace("@@activityIdCondition@@", " IN (" +Util.toCSStringForIN(activityIds) + ")"));
			}
			return res;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static double runETL() throws SQLException {
		long start = System.currentTimeMillis();
		try(Connection conn = PersistenceManager.getJdbcConnection()) {
			try(MonetConnection monetConn = MonetConnection.getConnection()) {
				MondrianETL etl = new MondrianETL(conn, monetConn);
				etl.execute();
			}
		}
		long end = System.currentTimeMillis();
		double secs = (end - start) / 1000.0;
		return secs;
	}
}

