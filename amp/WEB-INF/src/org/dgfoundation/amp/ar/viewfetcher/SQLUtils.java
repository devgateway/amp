package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.dgfoundation.amp.ar.FilterParam;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;

public class SQLUtils {
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param connection
	 * @param tableName
	 * @return
	 */
	public static LinkedHashSet<String> getTableColumns(Connection connection, String tableName)
	{
		String query = String.format("SELECT c.column_name FROM information_schema.columns As c WHERE table_schema='public' AND table_name = '%s' ORDER BY c.ordinal_position", tableName.toLowerCase());
		return new LinkedHashSet<String>(SQLUtils.<String>fetchAsList(connection, query, 1));		
	}
	
	public static boolean tableExists(Connection connection, String tableName)
	{
		return !getTableColumns(connection, tableName).isEmpty();
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
			ResultSet rs = rawRunQuery(connection, query, null);
			return fetchAsList(rs, n, " with query " + query);
		}
		catch(SQLException e)
		{
			throw new RuntimeException("Error fetching list of values with query " + query, e);
		}
	}
	
	public static <T> List<T> fetchAsList(ResultSet rs, int n, String errMsgAdd)
	{
		try
		{
			ArrayList<T> result = new ArrayList<T>();
			
			while (rs.next())
			{
				T elem = (T) rs.getObject(n);
				result.add(elem);
			}
			return result;
		}
		catch(SQLException e)
		{
			throw new RuntimeException("Error fetching list of values" + errMsgAdd, e);
		}
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
}
