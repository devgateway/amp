package org.dgfoundation.amp.ar.viewfetcher;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.ExceptionConsumer;
import org.dgfoundation.amp.ar.FilterParam;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

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
        this.condition = condition != null ? condition : "";
    }
    
    @Override
    public RsInfo fetch(ArrayList<FilterParam> params)
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
        
        LinkedHashSet<String> ret = SQLUtils.getTableColumns(viewName, true);
        if ( ret.isEmpty() ) {
            throw new RuntimeException("Table/view is empty:" + viewName);
        }
        return ret;
    }
    
    /**
     * the implementation-dependent way of fetching rows from a database - either rawly or through translations
     * @return
     */
    protected abstract RsInfo fetchRows(ArrayList<FilterParam> params) throws SQLException;
    
    @Override
    public String toString()
    {
        return String.format("%s: [%s WHERE %s, columns: %s]", this.getClass().getSimpleName(), this.viewName, this.condition, SQLUtils.generateCSV(Arrays.asList(this.columnNamesRaw)));
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
            //logger.debug("for view " + viewName + ", selected RawFetcher");
            return new RawDatabaseViewFetcher(viewName, condition, connection, columnNames);
        }
        //logger.debug("for view " + viewDesc.viewName + ", selected i18nFetcher");
        return new I18nDatabaseViewFetcher(viewName, condition, locale, cachers, connection, columnNames);
    }
    
    /**
     * coordinate any changes in this function with changes to {@link #getFetcherForView(String, String, String, java.util.Map, Connection, String...)} and {@link I18nDatabaseViewFetcher.TranslatingResultSet#toString()}
     * @param viewName
     * @param rs
     * @return
     */
    public static boolean isInternationalized(String viewName, ResultSet rs)
    {
        return InternationalizedViewsRepository.i18Models.get(viewName) != null && (rs.toString().contains("i18n"));
    }
    
    /**
     * convenience method for fetching a view using the request's JDBC connection and locale and no inter-view caching and no FilterParams
     * @param viewName
     * @param condition
     * @param locale
     * @param columnNames
     * @return
     */
    public static Map<Long, String> fetchInternationalizedView(final String viewName, final String condition, final String idColumnName, final String payloadColumnName) {
        return PersistenceManager.getSession().doReturningWork(conn -> 
            fetchViewAsKeyValue(conn, TLSUtils.getEffectiveLangCode(), viewName, idColumnName, payloadColumnName));
    }
    
    public static void fetchView(Connection conn, final String locale, final String viewName, final String condition, List<String> columns, Consumer<ResultSet> consumer) {
        ViewFetcher fetcher = getFetcherForView(viewName, condition, locale, new java.util.HashMap<PropertyDescription, ColumnValuesCacher>(), conn, columns.toArray(new String[0]));
        fetcher.forEach(consumer);
    }
    
    public static Map<Long, String> fetchViewAsKeyValue(Connection conn, final String locale, final String viewName, String idColumnName, String payloadColumnName) {
        Map<Long, String> res = new HashMap<>();
        fetchView(conn, locale, viewName, null, Arrays.asList(idColumnName, payloadColumnName), ExceptionConsumer.of(rs -> {
            Long id = rs.getLong(idColumnName);
            String val = rs.getString(payloadColumnName);
            res.put(id, val);
        }));
        return res;
    }
}
