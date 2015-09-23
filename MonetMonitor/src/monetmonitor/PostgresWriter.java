package monetmonitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class PostgresWriter {
	
	
	
	public PostgresWriter() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Checks whether all Postgres credentials are available from the settings file
	 * @return
	 */
	private boolean isPostgresAvailable() {
		return Constants.getPostgresPassword() != null && Constants.getPostgresPort() != null && Constants.getPostgresUser() != null;
	}
	

	/**
	 * Runs a statement that doesn't require reading back the answer (delete, drop, create, alter etc)
	 * @param query
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private boolean runStatement(String query, Connection connection) throws SQLException {
		boolean b = false;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			b = statement.execute(query);
		} finally {
			if (statement != null)
			statement.close();
		}
		return b;
		
	}
	
	/**
	 * Adds an entry amp_monet_corruption_log, containing the query that caused the fail (if it was a query)
	 * the id of the freshly-added entry will be used in creating an entry in amp_etl_changelog
	 * @param cause
	 * @param c
	 * @return
	 * @throws SQLException
	 */
	private boolean addEntryToAmpMonetCorruptionLog(String cause, Connection c) throws SQLException {
		boolean b = false;
		if (!tableExists("amp_monet_corruption_log", c)){
			b |= runStatement("CREATE TABLE amp_monet_corruption_log(id bigint NOT NULL, cause_query text, CONSTRAINT id_pkey PRIMARY KEY (id))", c);
		}
		b |= runStatement(String.format("INSERT INTO amp_monet_corruption_log(cause_query) VALUES ('%s')", cause), c);
		return b;
	}
	
	/**
	 * gets the id of the last inserted entry. since it's called right after the entry being inserted,
	 * should return exactly it -- no one else is using the table.
	 *    TODO: CRITICAL: rewrite it, there are better ways to do this
	 * @param c
	 * @return
	 * @throws SQLException
	 */
	private Long getLastCorruptionId(Connection c) throws SQLException {
		List<Object[]> values = runRawQuery("SELECT MAX(id) FROM amp_monet_corruption_log", c);
		if (values.size() > 0)
			return ((Number)values.get(0)[0]).longValue();
		return null;
	}
	
	/**
	 * adds an entry to amp_etl_changelog with a reference to the amp_monet_corruption_log
	 * @param cause
	 * @param c
	 * @throws SQLException
	 */
	private void addEntryToAmpEtlChangelog(String cause, Connection c) throws SQLException {
		if (tableExists("amp_etl_changelog", c) ) {
			addEntryToAmpMonetCorruptionLog(cause, c);
			runStatement("INSERT INTO amp_etl_changelog(entity_name, entity_id) VALUES ('monetdb_corruption', " + getLastCorruptionId(c) + ")", c);
		}
	}
	
	/**
	 * Adds an entry to amp_etl_changelog and amp_monet_corruption_log with the query that detected corruption
	 * @param cause
	 * @return
	 */
	public boolean addErrorToLogs(String cause) {
		if (!isPostgresAvailable())
			return false;
		Connection conn = null;
		try {
			conn = getConnection();
			addEntryToAmpEtlChangelog(cause, conn);
		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return true;
	}
	
	private boolean tableExists(String tableName, Connection c) throws SQLException {
		String query = String.format("select table_name FROM information_schema.tables where table_name = '%s'", tableName);
		return runRawQuery(query, c).size() > 0;
	}
	
	/**
	 * Runs a query and fetches its results as a list (1 list entry = 1 row) of Object arrays (1 array element = 1 column)
	 * @param query
	 * @param c
	 * @return
	 * @throws SQLException
	 */
	private List<Object[]> runRawQuery(String query, Connection c) throws SQLException {
		PreparedStatement ps = null;
		List<Object[]> result = new ArrayList<Object[]>();
		try {
		ps = c.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsm = rs.getMetaData();
		int columnCount = rsm.getColumnCount();
		while (rs.next()) {
			Object[] row = new Object[columnCount];
			for (int i = 0; i < columnCount; i++) {
				//JDBC column indexing starts with 1
				row[i] = rs.getObject(i + 1);
			}
			result.add(row);
		}
		rs.close();
		} finally {
			if (ps != null)
				ps.close();
		}
		return result;
	}
	
	private Connection getConnection() throws SQLException {
		String command = String.format("jdbc:postgresql://localhost:%s/%s",Constants.getPostgresPort(),Constants.getDbName() );
		return DriverManager.getConnection(command, Constants.getPostgresUser(), Constants.getPostgresPassword());
	}
	
	private void closeConnection(Connection conn) {
		try {
		if (conn != null)
			conn.close();
		} catch (SQLException exc) {
			throw new RuntimeException(exc);
		}
	}
	
}
