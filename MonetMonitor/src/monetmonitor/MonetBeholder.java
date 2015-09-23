package monetmonitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author acartaleanu
 * Class intended to attempt to connect to MonetDB and perform a specific number of operations
 * 
 */
public abstract class MonetBeholder {

	public MonetBeholder() {
		try {
			Class.forName("nl.cwi.monetdb.jdbc.MonetDriver");
		} catch(Exception exc) {
		}

	}

	/**
	 * runs a select via the connection supplied 
	 * @param conn the connection supplied by the JDBC Monet driver
	 * @param query the SQL query to be ran
	 * @throws SQLException 
	 */
	protected List<Object> runSelect(Connection conn, String query) throws SQLException {
		List<Object> res = new ArrayList<>();
		lastRunQuery = query;
		PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		try(ResultSet rs = ps.executeQuery()) {
			int nrColumns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for(int i = 0; i < nrColumns; i++)
					res.add(rs.getObject(i + 1));
			}
			rs.close();
			ps.close();
			conn.close();
		}
		return res;
	}

	private String lastRunQuery = null;
	/**
	 * 
	 * @return true if server is responding, false if it isn't
	 * @throws ClassNotFoundException 
	 */	
	public BeholderObservationResult check() {
		try {
			runQuery();
			return BeholderObservationResult.SUCCESS;
		} catch (java.sql.SQLException exc) {
			exc.printStackTrace();
			if (exc.getMessage().contains("internal error while starting"))
				return BeholderObservationResult.ERROR_INTERNAL_MONETDB;
			if (exc.getMessage().contains("no such database"))
				return BeholderObservationResult.ERROR_NO_DATABASE;
			if (exc.getMessage().contains("End of stream reached")) {
				new PostgresWriter().addErrorToLogs(lastRunQuery);
				return BeholderObservationResult.ERROR_INTERNAL_MONETDB;
			}
			if (exc.getMessage().contains("Connection refused"))
				return BeholderObservationResult.ERROR_CANNOT_CONNECT;
//			End of stream reached
			if (exc.getMessage().contains("is under maintenance"))
				return BeholderObservationResult.ERROR_DATABASE_MAINTENANCE;
			
		} catch (nl.cwi.monetdb.mcl.MCLException exc) {
			exc.printStackTrace();
			return BeholderObservationResult.ERROR_INTERNAL_MONETDB;
		}
		return BeholderObservationResult.ERROR_UNKNOWN;
	}

	protected abstract void runQuery() throws nl.cwi.monetdb.mcl.MCLException, java.sql.SQLException;
}
