package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.FilterParam;
import org.apache.log4j.Logger;

/**
 * a {@link ViewFetcher} for fetching data from a real database, either with or without postprocessing
 * @author Dolghier Constantin
 *
 */
public abstract class DatabaseViewFetcher implements ViewFetcher
{
	protected final Connection connection;
	/**
	 * the view to fetch data from
	 */
	protected final String viewName;
	
	/**
	 * the column names to fetch data from.
	 * Hardcoded to the class and not to the interface because it is very important <b>for the columns to get iterated in the same order they would have been returned by a raw SQL query</b>
	 */
	protected LinkedHashSet<String> columnNames;
	
	/**
	 * the raw column names to fetch data from, "raw" = as given by the user. E.g., "*" would equal "get all columns"
	 */
	protected final String[] columnNamesRaw;
	
	/**
	 * the part of the query to send to the database after WHERE, e.g. <br />
	 * "SELECT * FROM view_name WHERE <b>amp_activity_in in (...) and (...) ORDER BY (...)</b>" has the bold part = condition
	 */
	protected final String condition;

	protected static Logger logger = Logger.getLogger(DatabaseViewFetcher.class);
	
	protected DatabaseViewFetcher(String viewName, String condition, Connection connection, String... columnNames)
	{
		this.viewName = viewName;
		this.connection = connection;
		this.columnNamesRaw = defensiveCopy(columnNames);
		this.condition = condition;
	}
	
	@Override
	public ResultSet fetch(ArrayList<FilterParam> params)
	{
		if (columnNames == null)
			columnNames = generateColumnNames();
				
		try
		{
			return fetchRows(params);
		}
		catch(SQLException e)
		{
			throw new RuntimeException("error while fetching rows", e);
		}
	}
	
	/**
	 * runs a query, optimizing for throughput
	 * @param connection
	 * @param query
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet rawRunQuery(Connection connection, String query, ArrayList<FilterParam> params) throws SQLException
	{
		//logger.info("Running raw SQL query: " + query);
		
		PreparedStatement ps = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if (params != null)
		{
			//logger.debug("the parameters are:");
			for (int i = 0; i < params.size(); i++) 
			{
				ps.setObject(i + 1, params.get(i).getValue(), params.get(i).getSqlType());
				//logger.debug(String.format("\tvalue: %s, SQL type: %d", params.get(i).getValue(), params.get(i).getSqlType()));
			}
		}
		
		ResultSet rs = ps.executeQuery();
		rs.setFetchSize(500);
		
		return rs;
	}
	
	public final static String[] defensiveCopy(String[] src)
	{
		if (src == null)
			return null;
		String[] res = new String[src.length];
		for(int i = 0; i < res.length; i++)
			res[i] = src[i];
		return res;
	}
	
	private LinkedHashSet<String> generateColumnNames()
	{
		if (columnNamesRaw != null)
		{
			if (columnNamesRaw.length > 1)
				return new LinkedHashSet<String>(Arrays.asList(columnNamesRaw)); // more than one column specified -> use them rawly
			// got till here -> columnNamesRaw[0] is the sole element
			if (!columnNamesRaw[0].equals("*"))
				return new LinkedHashSet<String>(Arrays.asList(columnNamesRaw)); // not "*" was specified -> use them rawly
			// else fall through: the user specified "*", e.g. he wants all columns
		}
		return getTableColumns(connection, viewName);
	}
	
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param connection
	 * @param tableName
	 * @return
	 */
	public static LinkedHashSet<String> getTableColumns(Connection connection, String tableName)
	{
		String query = String.format("SELECT c.column_name FROM information_schema.columns As c WHERE table_schema='public' AND table_name = '%s' ORDER BY c.ordinal_position", tableName.toLowerCase());
		return new LinkedHashSet<String>(DatabaseViewFetcher.<String>fetchAsList(connection, query, 1));		
	}
	
	public static boolean tableExists(Connection connection, String tableName)
	{
		return !getTableColumns(connection, tableName).isEmpty();
	}
	
	/**
	 * runs a query and returns a list of the nth elements in each of the rows
	 * @param connection
	 * @param query
	 * @param n
	 * @return
	 */
	public static <T> List<T> fetchAsList(Connection connection, String query, int n)
	{
		try
		{
			ArrayList<T> result = new ArrayList<T>();
			ResultSet rs = rawRunQuery(connection, query, null);
			while (rs.next())
			{
				T elem = (T) rs.getObject(n);
				result.add(elem);
			}
			return result;
		}
		catch(SQLException e)
		{
			throw new RuntimeException("Error fetching list of values with query " + query, e);
		}
	}
	
	/**
	 * the implementation-dependent way of fetching rows from a database - either rawly or through translations
	 * @return
	 */
	public abstract ResultSet fetchRows(ArrayList<FilterParam> params) throws SQLException;
	
	@Override
	public String toString()
	{
		return String.format("%s: [%s WHERE %s, columns: %s]", this.getClass().getSimpleName(), this.viewName, this.condition, generateCSV(Arrays.asList(this.columnNamesRaw)));
	}
	
	/**
	 * generates a raw comma-separated-values
	 * @param values
	 * @return
	 */
	public static String generateCSV(java.util.Collection<?> values)
	{
		StringBuilder result = new StringBuilder();
		java.util.Iterator<?> it = values.iterator();
		while (it.hasNext())
		{
			result.append(it.next().toString());
			if (it.hasNext())
				result.append(", ");
		}
		return result.toString();
	}
	
	/**
	 * selects a fetcher for a view, depending on whether the view is configured to use i18n or not
	 * @param viewName - the view to fetch
	 * @param condition - the condition to pass in raw form to SQL
	 * @param locale - the locale. Ignored if it is a non-i18n-view
	 * @param cachers - the cacher for the fetched i18n values. Ignored if it is a non-i18n-view
	 * @param connection - the connection to run the SQL queries on
	 * @param columnNames - the columns to fetch. "*" or "null" or "none" means all
	 * @return
	 */
	public static ViewFetcher getFetcherForView(String viewName, String condition, String locale, java.util.Map<PropertyDescription, ColumnValuesCacher> cachers, Connection connection, String... columnNames)
	{		
		I18nViewDescription viewDesc = InternationalizedViewsRepository.i18Models.get(viewName);
		if (viewDesc == null)
		{
			logger.info("for view " + viewName + ", selected RawFetcher");
			return new RawDatabaseViewFetcher(viewName, condition, connection, columnNames);
		}
		logger.info("for view " + viewDesc.viewName + ", selected i18nFetcher");
		return new I18nDatabaseViewFetcher(viewName, condition, locale, cachers, connection, columnNames);
	}
}

