package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.FilterParam;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.TypedValue;

public class SQLUtils {
	
	/**
	 * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
	 * @param tableName - the table / view whose columns to fetch
	 * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
	 * @return
	 */
	public static LinkedHashSet<String> getTableColumns(final String tableName, boolean crashOnDuplicates)
	{
		LinkedHashSet<String> res = new LinkedHashSet<String>();
		String query = String.format("SELECT c.column_name FROM information_schema.columns As c WHERE table_schema='public' AND table_name = '%s' ORDER BY c.ordinal_position", tableName.toLowerCase());	
		Connection connection = null;
		try {
			connection = org.digijava.kernel.persistence.PersistenceManager.getSession().connection();
			for (Object obj : fetchAsList(connection, query, 1)) {
				if (crashOnDuplicates && res.contains((String) obj))
					throw new RuntimeException("not allowed to have duplicate column names in table " + tableName);
				res.add((String) obj);
			}

		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	/**
	 * equivalent to calling {@link #getTableColumns(String, false)}
	 * @param tableName
	 * @return
	 */
	public static LinkedHashSet<String> getTableColumns(final String tableName)
	{
		return getTableColumns(tableName, false);
	}
	
	public static boolean tableExists(String tableName)
	{
		return !getTableColumns(tableName).isEmpty();
	}
	
	public static void executeQuery(Connection conn, String query)
	{
		try
		{
			Statement statement = conn.createStatement();
			statement.execute(query);
			statement.close();
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
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
	public static RsInfo rawRunQuery(Connection connection, String query, ArrayList<FilterParam> params) throws SQLException
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
		
		return new RsInfo(rs, ps);
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
		try(RsInfo rsi = rawRunQuery(connection, query, null)) {
			return fetchAsList(rsi.rs, n, " with query " + query);
		}
		catch(SQLException e) {
			throw new RuntimeException("Error fetching list of values with query " + query, e);
		}
//		finally
//		{
//			PersistenceManager.closeQuietly(rs);
//		}
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
	
	public static Criterion getUnaccentILikeExpression(final String propertyName, final String value, final String locale, final MatchMode matchMode) {
		return new Criterion(){
			private static final long serialVersionUID = -8979378752879206485L;

			@Override
			public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
				Dialect dialect = criteriaQuery.getFactory().getDialect();
				String[] columns = criteriaQuery.findColumns(propertyName, criteria);
				String entityName = criteriaQuery.getEntityName(criteria);
              
				String []ids=criteriaQuery.getIdentifierColumns(criteria);
				if (columns.length!=1)
					throw new HibernateException("ilike may only be used with single-column properties");
				if (ids.length!=1)
					throw new HibernateException("We do not support multiple identifiers just yet!");

				if ( dialect instanceof PostgreSQLDialect ) {
					//AMP-15628 - the replace of "this_." with "" inside the ids and columns was removed
					String ret=" "+ids[0]+" = any(contentmatch('"+entityName+"','"+columns[0]+"','"+locale+"', ?)) OR ";
					ret+=" unaccent(" + columns[0] + ") ilike " +  "unaccent(?)";
					return ret;
				} else {
					throw new HibernateException("We do not handle non-postgresql databases yet, sorry!");
				}
			}
		

			@Override
			public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException 
			{
				return new TypedValue[] { criteriaQuery.getTypedValue( criteria, propertyName, matchMode.toMatchString(value).toLowerCase() ) ,
					criteriaQuery.getTypedValue( criteria, propertyName, matchMode.toMatchString(value).toLowerCase() )};
			}
			};
		}
		
		    
		//ao.* from amp_organisation ao -> "ao.amp_org_id, ao.column2, getOrgName(....), ...."
		/**
		 * 
		 * @param tableName
		 * @param tableAlias
		 * @param renames Map<ColumnName, String to Replace with>
		 * @return
		 */
		public static String rewriteQuery(String tableName, String tableAlias, Map<String, String> renames)
		{
			LinkedHashSet<String> columns = SQLUtils.getTableColumns(tableName);
			ArrayList<String> outputs = new ArrayList<String>();
			for(String column:columns)
			{
				if (renames.containsKey(column))
					outputs.add(renames.get(column) + " AS " + column);
				else
					outputs.add(tableAlias + "." + column);
			}
   		
			return Util.collectionAsString(outputs);
		}
}
