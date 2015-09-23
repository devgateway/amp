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
	
	Connection connection = null;
	
	
	public PostgresWriter() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isPostgresAvailable() {
		return Constants.getPostgresPassword() != null && Constants.getPostgresPort() != null && Constants.getPostgresUser() != null;
	}
	

	private boolean runStatement(String query) throws SQLException {
		Statement statement = connection.createStatement();
		boolean b = statement.execute(query);
		statement.close();
		return b;
		
	}
	
	private boolean addEntryToAmpMonetCorruptionLog(String cause) throws SQLException {
		if (!tableExists("amp_monet_corruption_log")){
			runStatement("CREATE TABLE amp_monet_corruption_log(id bigint NOT NULL, cause_query text, CONSTRAINT id_pkey PRIMARY KEY (id))");
		}
		runStatement(String.format("INSERT INTO amp_monet_corruption_log(cause_query) VALUES ('%s')", cause));
		return true; //TODO: add meaningful returns
	}
	
	private Long getLastCorruptionId() throws SQLException {
		List<Object[]> values = runRawQuery("SELECT MAX(id) FROM amp_monet_corruption_log");
		if (values.size() > 0)
			return ((Number)values.get(0)[0]).longValue();
		return null;
	}
	
	private void addEntryToAmpEtlChangelog(String cause) throws SQLException {
		if (tableExists("amp_etl_changelog") ) {
			
			addEntryToAmpMonetCorruptionLog(cause);
			runStatement("INSERT INTO amp_etl_changelog(entity_name, entity_id) VALUES ('monetdb_corruption', " + getLastCorruptionId() + ")");
			
		//SQLUtils.executeQuery(conn, "INSERT INTO amp_etl_changelog(entity_name, entity_id) VALUES ('etl', " + currentEtlId + ")");
		
		}
	}
	
	public boolean addErrorToLogs(String cause) {
		if (!isPostgresAvailable())
			return false;
		try {
			this.connection = getConnection();
			addEntryToAmpEtlChangelog(cause);
		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			try {
				closeConnection(this.connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	private boolean tableExists(String tableName) throws SQLException {
		String query = String.format("select table_name FROM information_schema.tables where table_name = '%s'", tableName);
		
		return runRawQuery(query).size() > 0;
		
		
	}
	private void createTable(String tableName) {
		
	}
	
	private List<Object[]> runRawQuery(String query ) throws SQLException {
		//logger.info("Running raw SQL query: " + query);
//		Statement statement = connection.createStatement();
//		statement.execute(query);
//		statement.close();

		PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		List<Object[]> result = new ArrayList<Object[]>();
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsm = rs.getMetaData();
		int columnCount = rsm.getColumnCount();
		while (rs.next()) {
			Object[] row = new Object[columnCount];
			for (int i = 0; i < columnCount; i++) {
				row[i] = rs.getObject(i + 1);
			}
			result.add(row);
			//do nothing, we're just inserting here
		}
		ps.close();
		return result;
	}
	
	private Connection getConnection() throws SQLException {
		String command = String.format("jdbc:postgresql://localhost:%s/%s",Constants.getPostgresPort(),Constants.getDbName() );
		return DriverManager.getConnection(command, Constants.getPostgresUser(), Constants.getPostgresPassword());
//		return DriverManager.getConnection(
//				   "jdbc:postgresql://localhost:port/dbname",Constants.get, "password");
	}
	
	private void closeConnection(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}
	
}
