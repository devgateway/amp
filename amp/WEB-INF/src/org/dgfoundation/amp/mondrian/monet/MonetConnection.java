package org.dgfoundation.amp.mondrian.monet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.Constants;

/**
 * this class wraps a connection to MonetDB
 * @author simple
 *
 */
public class MonetConnection implements AutoCloseable {
	
	public final Connection conn;
	private static DataSource dataSource = getMonetDataSource();
	
	private MonetConnection() throws SQLException {
		this.conn = dataSource.getConnection();
	}
	
	public static MonetConnection getConnection() {
		try {
			return new MonetConnection();
		}
		catch(SQLException e) {throw new RuntimeException(e);}
	}
	
	public void close(){
		PersistenceManager.closeQuietly(conn);
	}
	
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param tableName - the table / view whose columns to fetch
	 * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
	 * @return
	 * @throws SQLException 
	 */
	public LinkedHashSet<String> getTableColumns(final String tableName, boolean crashOnDuplicates){
		return new LinkedHashSet<String>(getTableColumnsWithTypes(tableName, crashOnDuplicates).keySet());
	}
	
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param tableName - the table / view whose columns to fetch
	 * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
	 * @return Map<ColumnName, data_type>
	 */
	public LinkedHashMap<String, String> getTableColumnsWithTypes(final String tableName, boolean crashOnDuplicates){
		String query = String.format("SELECT c.name, c.type FROM sys.columns c WHERE c.table_id = (SELECT t.id FROM sys.tables t WHERE t.name='mondrian_fact_table') ORDER BY c.number", tableName.toLowerCase());
		return SQLUtils.getTableColumnsWithTypes(this.conn, tableName, query, crashOnDuplicates);
	}
		
	/**
	 * equivalent to calling {@link #getTableColumns(String, false)}
	 * @param tableName
	 * @return
	 */
	public LinkedHashSet<String> getTableColumns(final String tableName) {
		return getTableColumns(tableName, false);
	}
	
	public boolean tableExists(String tableName) {
		return !getTableColumns(tableName).isEmpty();
	}
	
	private static DataSource getMonetDataSource() {
		try {
			Context initialContext = new InitialContext();
			DataSource res = (javax.sql.DataSource) initialContext.lookup(Constants.MONETDB_JNDI_ALIAS);
			if (res == null)
				throw new Error("could not find Monet data source!");
			return res;
		}
		catch(Exception e) {
			throw new Error(e);
		}
	}
}
