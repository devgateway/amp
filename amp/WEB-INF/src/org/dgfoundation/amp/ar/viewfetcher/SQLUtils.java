package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.FilterParam;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.spi.TypedValue;

public class SQLUtils {
    
    public final static String SQL_UTILS_NULL = "###NULL###";
    
    /**
     * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
     * @param tableName - the table / view whose columns to fetch
     * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
     * @return
     * @throws SQLException 
     */
    public static LinkedHashSet<String> getTableColumns(final String tableName, boolean crashOnDuplicates){
        return new LinkedHashSet<String>(getTableColumnsWithTypes(tableName, crashOnDuplicates).keySet());
    }
    
    /**
     * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
     * @param tableName - the table / view whose columns to fetch
     * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
     * @return Map<ColumnName, data_type>
     */
    public static LinkedHashMap<String, String> getTableColumnsWithTypes(final String tableName, boolean crashOnDuplicates){
        String query = String.format("SELECT c.column_name, c.data_type FROM information_schema.columns As c WHERE table_schema='public' AND table_name = '%s' ORDER BY c.ordinal_position", tableName.toLowerCase());
        try(Connection jdbcConnection = PersistenceManager.getJdbcConnection()) {
            return getStringToStringMap(jdbcConnection, tableName, query, crashOnDuplicates);
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
     * @param tableName - the table / view whose columns to fetch
     * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
     * @return Map<ColumnName, data_type>
     */
    public static LinkedHashMap<String, String> getTableColumnsWithTypes(Connection jdbcConnection, final String tableName, boolean crashOnDuplicates){
        String query = String.format("SELECT c.column_name, c.data_type FROM information_schema.columns As c WHERE table_schema='public' AND table_name = '%s' ORDER BY c.ordinal_position", tableName.toLowerCase());
        return getStringToStringMap(jdbcConnection, tableName, query, crashOnDuplicates);
    }
    
    /**
     * generically builds a linkedhashmap of (colname, coltype), based on a query
     * @param jdbcConnection
     * @param query - a query which returns (colname, coltype) ORDERED BY appearance in db
     * @param crashOnDuplicates
     * @return
     */
    public static LinkedHashMap<String, String> getStringToStringMap(Connection jdbcConnection, String tableName, String query, boolean crashOnDuplicates) {
        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        try(RsInfo rsi = rawRunQuery(jdbcConnection, query, null)) {
            while (rsi.rs.next()) {
                String columnName = rsi.rs.getString(1);
                String columnType = rsi.rs.getString(2);
                    
                if (crashOnDuplicates && res.containsKey(columnName))
                    throw new RuntimeException("not allowed to have duplicate column names in table " + tableName);
                res.put(columnName, columnType);
            }
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
        return res;
    }
    
    public static Set<String> getTablesWithNameMatching(Connection conn, String query, String begin) {
        Set<String> res = new TreeSet<>();
        List<?> allTableNames = SQLUtils.fetchAsList(conn, query, 1);
        for(Object obj:allTableNames) {
            String tn = obj.toString();
            if (tn.startsWith(begin)) res.add(tn);              
        }
        return res;
    }
    
    public static Set<String> getTablesWithNameMatching(Connection conn, String begin) {
        return getTablesWithNameMatching(conn, "select table_name from information_schema.tables WHERE table_schema='public'", begin);
    }

    public static boolean isView(Connection conn, String viewName) {
        return getLong(conn, "select count(*) from information_schema.tables WHERE table_schema='public' AND lower(table_type)='view' and table_name='" + viewName + "'") > 0;
    }
    
    public static boolean isTable(Connection conn, String viewName) {
        return getLong(conn, "select count(*) from information_schema.tables WHERE table_schema='public' AND lower(table_type)='base table' and table_name='" + viewName + "'") > 0;
    }   
    
    /**
     * returns the rowcount in a table
     * @param conn
     * @param tableName
     * @return
     */
    public static long countRows(Connection conn, String tableName) {
        return fetchLongs(conn, "SELECT COUNT(*) FROM " +tableName).get(0);
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
     * runs a query and calls the consumer for each row of the result
     * @param connection
     * @param query
     * @param consumer
     */
    public static void forEachRow(Connection connection, String query, Consumer<ResultSet> consumer) {
        try(RsInfo rsInfo = rawRunQuery(connection, query, null)) {
            rsInfo.forEach(consumer);
        }
        catch(SQLException ex) {
            throw AlgoUtils.translateException(ex);
        }
    }
    
    /**
     * calls a given function for each row of the result and accumulates the results in a List
     * @param connection
     * @param query
     * @param mapper
     * @return
     */
    public static<K> List<K> collect(Connection connection, String query, Function<ResultSet, K> mapper) {
        List<K> res = new ArrayList<>();
        try(RsInfo rsInfo = rawRunQuery(connection, query, null)) {
            while(rsInfo.rs.next()) {
                res.add(mapper.apply(rsInfo.rs));
            }
        }
        catch(SQLException ex) {
            throw AlgoUtils.translateException(ex);
        }
        return Collections.unmodifiableList(res);
    }
    
    /**
     * runs a query, optimizing for throughput
     * @param connection
     * @param query
     * @param params
     * @return
     * @throws SQLException
     */
    public static RsInfo rawRunQuery(Connection connection, String query, List<FilterParam> params) throws SQLException
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
        if (!connection.getMetaData().getDatabaseProductName().equals("MonetDB"))
            rs.setFetchSize(500);
        
        return new RsInfo(rs, ps);
    }
    
    /**
     * fetches an ArrayList of longs
     * @param connection
     * @param query
     * @return
     */
    public static List<Long> fetchLongs(Connection connection, String query) {
        List<Long> res = new ArrayList<>();
        try(RsInfo rsi = rawRunQuery(connection, query, null)) {
            while (rsi.rs.next()) {
                res.add(rsi.rs.getLong(1));
            }
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
        return res;
    }
    
    /**
     * fetches an ArrayList of Strings
     * @param connection
     * @param query
     * @return
     */
    public static List<String> fetchStrings(Connection connection, String query) {
        List<String> res = new ArrayList<>();
        try (RsInfo rsi = rawRunQuery(connection, query, null)) {
            while (rsi.rs.next()) {
                res.add(rsi.rs.getString(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        
        return res;
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
//      finally
//      {
//          PersistenceManager.closeQuietly(rs);
//      }
    }
    
    /**
     * runs a query and fetches its only result as a Long
     * @param connection
     * @param query
     * @return
     */
    public static Long getLong(Connection connection, String query) {
        List<?> res = SQLUtils.fetchAsList(connection, query, 1);
        if (res.size() != 1)
            throw new RuntimeException("query should have returned exactly one result, but returned instead: " + res.size());
        return PersistenceManager.getLong(res.get(0));
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

        /**
         * inserts, as fastly as possible, a series of rows into a table
         * @param conn - the connection to use
         * @param tableName - the table to insert into
         * @param idColumnName - the name if the id column, might be null IFF seqname is also null
         * @param seqName - the name of the sequence giving values to the id column. should be null if idColumnName is null
         * @param colNames - the names of the columns to insert into
         * @param values - a series of rows. each of them should have exactle colNames.size() rows
         */
        public static void insert(Connection conn, String tableName, String idColumnName, String seqName, Collection<String> colNames, List<List<Object>> values) {
            if ((idColumnName == null) ^ (seqName == null))
                throw new RuntimeException("idColumnName should be both null or both non-null");
            
            int rowsPerInsert = 300;
            int nrSegments = values.size() / rowsPerInsert + 1;         
            for(int i = 0; i < nrSegments; i++) {
                int segmentStart = i * rowsPerInsert; // inclusive
                int segmentEnd = Math.min(values.size(), (i + 1) * rowsPerInsert); // exclusive
                if (segmentStart >= segmentEnd)
                    break; 
                String query = buildMultiRowInsert(tableName, idColumnName, seqName, colNames, values.subList(segmentStart, segmentEnd));
                //System.out.println("executing mondrian dimension table insert " + query);
                SQLUtils.executeQuery(conn, query.toString());
            }
        }
        
        /**
         * builds a statement of the form INSERT INTO $tableName$ (col1, col2, col3) VALUES (val11, val12, val13), (val21, val22, val23);
         * @param tableName
         * @param idColumnName
         * @param seqName
         * @param coordsList
         * @return
         */
        public static String buildMultiRowInsert(String tableName, String idColumnName, String seqName, Collection<String> colNames, List<List<Object>> values) {
            if ((idColumnName == null) ^ (seqName == null))
                throw new RuntimeException("idColumnName and seqName should be either both null or both nonnull");
            
            if (values.isEmpty())
                return null; // nothing to do
            
            List<String> keys = new ArrayList<>(colNames);
            StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
            boolean needComma = false;
            for(String key:keys) {
                if (needComma)
                    query.append(",");
                query.append(key);
                needComma = true;
            }
            
            if (idColumnName != null) {
                if (!keys.isEmpty())
                    query.append(",");
                query.append(idColumnName);
            }
            
            query.append(") VALUES");
            boolean firstRow = true;
            for(List<Object> coords:values) {
                if (coords.size() != keys.size())
                    throw new RuntimeException("row has a wrong number of columns: <" + coords + "> does not match <" + keys + ">");
                query.append(firstRow ? " " : ",");
                query.append(buildCoordsLine(coords, idColumnName, seqName));
                firstRow = false;
            }
            query.append(";");
            
            return query.toString();
        }
        
        /**
         * builds a line of type (colValue, colValue, colValue)
         * @param coords
         * @param keys
         * @param idColumnName
         * @param seqName
         * @return
         */
        public static String buildCoordsLine(List<Object> coords, String idColumnName, String seqName) {
            boolean needComma = false;
            StringBuilder query = new StringBuilder("(");
            for(Object value:coords) {
                if (needComma)
                    query.append(",");
                query.append(stringifyObject(value));
                needComma = true;
            }
            
            if (idColumnName != null) {
                if (!coords.isEmpty())
                    query.append(",");
                query.append(String.format("nextval('%s')", seqName));
            }
            query.append(")");
            return query.toString();
        }
        
        private static ThreadLocal<SimpleDateFormat> dbDateExportFormat = new ThreadLocal<>(); 
        //private static SimpleDateFormat dbDateExportFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        private static String stringifyDate(Date obj) {
            if (dbDateExportFormat.get() == null) 
                dbDateExportFormat.set(new SimpleDateFormat("yyyy-MM-dd"));
            return "'" + dbDateExportFormat.get().format(obj) + "'";
        }
        
        /**
         * returns a ready-to-be-included-into-SQL-query representation of a var
         * @param obj
         * @return
         */
        public static String stringifyObject(Object obj) {
            if (obj instanceof Number)
                return obj.toString();
            else if (obj instanceof String)
            {
                if (obj.toString().equals(SQL_UTILS_NULL))
                    return "NULL";
//              if (obj.toString().indexOf('\'') < 0)
//                  return String.format("'%s'", obj.toString());
                return String.format("'%s'", sqlEscapeStr(obj.toString()));
                
                //$t$blablabla$t$ - dollar-quoting
                //return "'" + obj.toString() + "'";
                
                /*String dollarQuote = "$dAaD41$";
                return dollarQuote + obj.toString() + dollarQuote;*/
            }
            else if (obj == null)
                return "NULL";
            else if (obj instanceof Date) {
                return stringifyDate((Date) obj);
            }
            else
            {
                return "'" + obj.toString() + "'";
            }
        }
        
        public static String sqlEscapeStr(String input) {
            StringBuilder res = new StringBuilder();
            for (char ch:input.toCharArray()) {
                if (ch < ' ')
                    res.append(' ');
                else if (ch != '\'')
                    res.append(ch);
                else
                    res.append("''");
            }
            return res.toString();
        }
        
    /**
     * flush schema changes so that they can used for introspection via {@link #getTableColumnsWithTypes(String, boolean)} and the likes
     * @param conn
     */
    public static void flush(Connection conn){
        try {           
            if (!conn.getAutoCommit())
                conn.commit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * returns an ordered set of column names of a given ResultSet
     * @param rs
     * @return
     * @throws SQLException
     */
    public static LinkedHashSet<String> collectColumnNames(ResultSet rs) throws SQLException {
        LinkedHashSet<String> res = new LinkedHashSet<>();
        for(int i = 0; i < rs.getMetaData().getColumnCount(); i++)
            res.add(rs.getMetaData().getColumnName(i + 1));
        return res;
    }
    
    /**
     * rethrows any exception as a RunTimeException - good for lambdas
     * @param rs
     * @param columnName
     * @return
     */
    public static Long getLong(ResultSet rs, String columnName) {
        try {
            Long res = rs.getLong(columnName);
            if (rs.wasNull())
                return null;
            return res;
        }
        catch(Exception e) {
            throw AlgoUtils.translateException(e);
        }
    }
    
    /**
     * this is written lambda-free so as to maximize performance
     * @param conn
     * @param query
     * @param idColumnName
     * @param payloadColumnName
     * @param map
     * @return
     */
    public static Map<Long, String> collectKeyValue(Connection conn, String query) {
        HashMap<Long, String> map = new HashMap<>();
        try(RsInfo rsi = rawRunQuery(conn, query, null)) {
            while (rsi.rs.next()) {
                map.put(rsi.rs.getLong(1), rsi.rs.getString(2));
            }
        }
        catch(SQLException e) {
            throw AlgoUtils.translateException(e);
        }
        return map;
    }
}
