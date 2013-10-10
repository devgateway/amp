package org.dgfoundation.amp.ar.filtercacher;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;

public class FastFilterCacher extends FilterCacher {

	private static Logger logger	= Logger.getLogger(FilterCacher.class);
	private String tempTableName;
	
	public FastFilterCacher(AmpARFilter filter)
	{
		super(filter);
	}
	
	@Override
	protected String customRewriteFilterQuery(String inQuery) {
		ensureTemporaryTableExists(inQuery);
		String ret =  String.format("SELECT distinct(%s) AS %s FROM %s", this.primaryKeyName, this.primaryKeyName, this.tempTableName);
		logger.info("Query is: " + ret);
		return ret;
		
	}

	private void ensureTemporaryTableExists(String inQuery)
	{
		if (tempTableName != null)
			return; //already created
		
		tempTableName = String.format("temp_table_%d", Math.abs(System.currentTimeMillis())); // Math.abs: better be paranoid than sorry
		
		try
		{
			Statement statement = getConnection().createStatement();
			String stat = String.format("CREATE TEMPORARY TABLE %s AS %s", tempTableName, inQuery);
			statement.executeUpdate(stat);
			
			stat = String.format("ALTER TABLE %s ADD PRIMARY KEY(%s)", tempTableName, this.primaryKeyName);
			statement.executeUpdate(stat);
			
			stat = String.format("ANALYZE %s", tempTableName);
			statement.executeUpdate(stat);
			
			statement.close();
		}
		catch(SQLException ex)
		{
			throw new RuntimeException(ex);
		}
		
	}
	
	private void deleteTemporaryTable()
	{
		if (tempTableName == null)
			return; //nothing to delete
		try
		{
			Connection conn = getConnection();
			if (conn == null)
				return; //shouldn't happen, but let's guard against it
			
			Statement statement = conn.createStatement();
			String stat = "DROP TABLE IF EXISTS" + tempTableName;
			statement.executeQuery(stat);
			statement.close();
		}
		catch(SQLException e)
		{
			// we don't care, because we are in the teardown sequence anyway
		}
	}
	
	@Override
	public void closeConnection()
	{
		deleteTemporaryTable();
		super.closeConnection(); //close the JDBC connection
	}
}
