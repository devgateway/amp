package org.dgfoundation.amp.ar.filtercacher;

import java.sql.Connection;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportContextData;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * class which caches AmpARFilter generatedFilterQuery results
 * all the queries 
 * the class is NOT thread safe! only designed for serial usage!
 * 
 */
public abstract class FilterCacher {
	
	private AmpARFilter filter;
	private Connection connection;
	protected String primaryKeyName;
	
	public FilterCacher(AmpARFilter filter)
	{
		this.filter = filter;
		/*Changed the way how this verify if it's a pledge filter 
		 * at this point the filter in not initialized yes so isPledgeFilter() will return always false 
		*/
		this.primaryKeyName = ReportContextData.getFromRequest().getReportMeta().getType() == ArConstants.PLEDGES_TYPE ? "id" : "amp_activity_id";
		try
		{
			this.connection = PersistenceManager.getJdbcConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * get a connection on which to run all the ColumnWorker sql workers
	 */
	public Connection getConnection()
	{
		return connection;
	}
	
	protected abstract String customRewriteFilterQuery(String inQuery);
	
	public String rewriteFilterQuery(String inQuery)
	{
		if (!inQuery.equals(filter.getGeneratedFilterQuery()))
			throw new RuntimeException("FilterCacher: one instance can only be used on a single filter");
		return customRewriteFilterQuery(inQuery);
	}
	
	public void closeConnection()
	{
		if (connection == null)
			return;
		try
		{
			connection.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			connection = null;
		}
	}
	
	@Override
	protected void finalize()
	{
		closeConnection();
	}	
	 
}
