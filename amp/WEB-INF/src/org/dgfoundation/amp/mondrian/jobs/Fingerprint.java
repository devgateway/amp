package org.dgfoundation.amp.mondrian.jobs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.ExceptionRunnable;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.monet.OlapDbConnection;

import com.google.common.base.Predicate;

import clover.com.google.common.base.Joiner;

/**
 * a class holding the fingerprint of some kind of AMP OLTP state (usually the hash of a table)
 * the interrogating queries are run in OLTP (Postgres), while the data is stored in OLAP (Monet) table etl_fingerprints
 * 
 * @author Constantin Dolghier
 *
 */
public class Fingerprint {
	
	/**
	 * the Monet table in which to store the fingerprints
	 */
	public final static String FINGERPRINT_TABLE = "etl_fingerprints";
	
	/**
	 * the "fingerprint name"
	 */
	public final String keyName;
	
	/**
	 * the SQL queries to run against PSQL; their outputs should be varchar(s); their outputs will be concatenated, the result being the "fingerprint"
	 */
	public final List<String> fingerprintQueries;
	
	/**
	 * value to return when querying for the stored value of the fingerprint when none exists
	 */
	public final String defaultValue;
	
	protected static Logger logger = Logger.getLogger(Fingerprint.class);
	
	/**
	 * this will be set to true when {@link #runIfFingerprintChangedOr(Connection, MonetConnection, boolean, Predicate, ExceptionRunnable)} is called, 
	 * if the present value of the fingerprint is different to the stored one
	 */
	protected boolean changeDetected = false;
	
	public Fingerprint(String keyName, List<String> fingerprintQueries, String defaultValue) {
		this.keyName = keyName;
		this.fingerprintQueries = Collections.unmodifiableList(new ArrayList<>(fingerprintQueries));
		this.defaultValue = defaultValue;
	}
	
	public Fingerprint(String keyName, List<String> fingerprintQueries) {
		this(keyName, fingerprintQueries, null);
	}
	
	/**
	 * compares fingerprint stored in Monet with computed fingerprint
	 * @return
	 * @throws SQLException
	 */
	public boolean changesDetected(Connection postgresConn, OlapDbConnection monetConn) throws SQLException {
		String currentFingerprint = computeFingerprint(postgresConn);
		String etlFingerprint = readMonetFingerprint(monetConn);
		boolean res = !currentFingerprint.equals(etlFingerprint);
		//logger.error(String.format("fingerprinting %s: stored FP = %s, present FP = %s, needs refresh: %s", keyName, etlFingerprint, currentFingerprint, res));
		return res;
	}
	
	/**
	 * returns a ';'-delimited list of hashes returned by psql queries
	 * @return
	 */
	public String computeFingerprint(Connection postgresConn) {
		List<String> fps = new ArrayList<>();
		for (String query:fingerprintQueries) {
			List<String> fp = SQLUtils.fetchAsList(postgresConn, query, 1);
			fps.add(Joiner.on(',').join(fp));
		}
		return Joiner.on(';').join(fps);
	}
	
	/**
	 * computes the current fingerprint and serializes it
	 * @throws SQLException
	 */
	public void saveFingerprint(Connection postgresConn, OlapDbConnection monetConn) throws SQLException {
		readOrReturnDefaultFingerprint(monetConn);
		serializeFingerprint(monetConn, computeFingerprint(postgresConn));
	}
	
	/**
	 * <strong>assumes</b> that the fingerprints table exists
	 * @param fp
	 */
	public void serializeFingerprint(OlapDbConnection monetConn, String fp) {
		monetConn.executeQuery(String.format("DELETE FROM %s where key='%s'", FINGERPRINT_TABLE, keyName));
		monetConn.executeQuery(String.format("INSERT INTO %s(key,value) VALUES ('%s', '%s')", FINGERPRINT_TABLE, keyName, fp));
		//logger.info(String.format("serializing fingerprint, key=%s, value=%s", keyName, fp));
	}
	
	/**
	 * extracts the currently-stored fingerprint. If none exists, returns a dummy value
	 * @return
	 * @throws SQLException
	 */
	protected String readMonetFingerprint(OlapDbConnection monetConn) throws SQLException {
		return readOrReturnDefaultFingerprint(monetConn);
	}
	
	/**
	 * DROPs and then recreates the etl_fingerprints table
	 * @param monetConn
	 */
	public static void redoFingerprintTable(OlapDbConnection monetConn) {
		monetConn.dropTable(FINGERPRINT_TABLE);
//		if (!monetConn.tableExists(FINGERPRINT_TABLE)) 
		{
			monetConn.executeQuery(String.format("CREATE TABLE %s (key %s, value %s)", FINGERPRINT_TABLE,
					monetConn.mapper.mapSqlTypeToName(java.sql.Types.VARCHAR, 255), monetConn.mapper.mapSqlTypeToName(java.sql.Types.LONGVARCHAR, 999999)));
			monetConn.flush();
		}
	}
	
	/**
	 * reads the Monet-stored fingerprint. If none exists, returns the default value
	 * @param monetConn
	 * @return
	 */
	public String readOrReturnDefaultFingerprint(OlapDbConnection monetConn) {
		//ensureFingerprintTableExists(monetConn);
		
		List<?> hashes = SQLUtils.fetchAsList(monetConn.conn, String.format("SELECT value FROM %s WHERE key='%s'", FINGERPRINT_TABLE, keyName), 1);
		switch(hashes.size()) {
			case 0: {
				String defaultHash = String.format("defaultHash_%s_%d", keyName, System.currentTimeMillis());
				return defaultValue == null ? defaultHash : defaultValue;
			}
				
			case 1: return nullOrString(hashes.get(0));
				
			default: {
				logger.fatal("multiple values found for the hashkey " + keyName + " in the fingerprints storage, deleting & ignoring both");
				monetConn.executeQuery("DELETE FROM " + FINGERPRINT_TABLE + " WHERE key='" + keyName + "'");
				return readOrReturnDefaultFingerprint(monetConn);
			}
				//throw new RuntimeException();
		}
	}
	
	public static String nullOrString(Object obj) {
		if (obj == null)
			return null;
		return obj.toString();		
	}
	
	public void runIfFingerprintChangedOr(Connection postgresConn, OlapDbConnection monetConn, boolean or, Predicate<Fingerprint> onNothingChanged, ExceptionRunnable<SQLException> r) throws SQLException {
		this.changeDetected = changesDetected(postgresConn, monetConn);
		if (or || this.changeDetected) {
			r.run();
			saveFingerprint(postgresConn, monetConn);
			return;
		}
		
		if (onNothingChanged != null)
			onNothingChanged.apply(this);
	}
	
	@Override public String toString() {
		return this.keyName;
	}
	
	public boolean hasChangeBeenDetected() {
		return this.changeDetected;
	}
	
	public static String buildTableHashingQuery(String table, String orderBy) {
		return String.format("select md5(cast((array_agg(tbl)) AS text)) from (select * from %s order by %s) tbl", table, orderBy);
	}
	
	public static String buildTranslationHashingQuery(Class<?> clazz) {
		return String.format(
			"select md5(coalesce(cast((array_agg(t.field_name || t.locale || t.object_id || t.translation)) AS text), 'nothing')) from " + 
					"(select * from amp_content_translation where object_class = '%s' order by object_id, field_name, locale) t",
					clazz.getName()
				);
	}
}
