package org.digijava.kernel.job.cachedtables;

import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.job.CachedTableState;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

/**
 * Utilities regarding cached_ variants of reports' extractor tables / views
 * @author Dolghier Constantin
 *
 */
public class PublicViewColumnsUtil 
{
    protected static Logger logger = Logger.getLogger(PublicViewColumnsUtil.class);
    
    /**
     * the views/tables which should be cached for the public view even though they are not extractor columns
     */
    protected static List<String> supplementalCachedViews = Arrays.<String>asList("amp_activity_group", "amp_activity", "amp_activity_version",
                "v_donor_funding", "v_component_funding", "v_regional_funding", "v_pledges_funding_st");
    
    /**
     * only change this to false when you have REALLY good reasons for this and ONLY WHILE TESTING OTHER STUFF <br />
     * NEVER RUN ANY AMP 2.7+ PRODUCTION INSTANCE WITH THIS SET TO false !!! 
     * Злой маньяк Constantin is taking care that this rule is enforced!
     */
    public final static boolean CRASH_ON_INVALID_COLUMNS = true;
    
    public static String getPublicViewTable(String extractorView)
    {
        if (extractorView.equals("v_regions_cached"))
            return "cached_v_m_regions";
        return ArConstants.VIEW_PUBLIC_PREFIX + extractorView;
    }
    
    /**
     * returns Map<String extractorViewName, Boolean does_cached_view_exist>
     * @param session
     * @return
     */
    protected static Map<String, CachedTableState> getExtractorColumns(java.sql.Connection conn)
    {
        try
        {
            Map<String, CachedTableState> result = new TreeMap<String, CachedTableState>();
            List<String> views = SQLUtils.<String>fetchAsList(conn, "SELECT DISTINCT(extractorview) FROM amp_columns WHERE extractorview IS NOT NULL ORDER BY extractorview", 1);
            views.addAll(supplementalCachedViews);
            for(String viewName:views)
            {
                String cachedViewName = getPublicViewTable(viewName);
                CachedTableState publicViewState = compareTableStructures(viewName, cachedViewName);
                result.put(viewName, publicViewState);
            }
            return result;
        }
        catch(Exception e)
        {
            throw new RuntimeException("error getting extractor columns", e);
        }
    }
    
    protected static CachedTableState compareTableStructures(String viewName, String cachedViewName)
    {
        List<String> originalCols = new ArrayList<String>(SQLUtils.getTableColumns(viewName));
        List<String> cacheCols = new ArrayList<String>(SQLUtils.getTableColumns(cachedViewName));
        
        if (originalCols.isEmpty())
            return CachedTableState.ORIGINAL_TABLE_MISSING;
        if (cacheCols.isEmpty())
            return CachedTableState.CACHED_TABLE_MISSING;

        if (originalCols.size() != cacheCols.size())
            return CachedTableState.CACHED_TABLE_DIFFERENT_STRUCTURE;
        for(int i = 0; i < originalCols.size(); i ++)
        {
            String a = originalCols.get(i);
            String b = cacheCols.get(i);
            if (!a.equals(b))
                return CachedTableState.CACHED_TABLE_DIFFERENT_STRUCTURE;
        }
        return CachedTableState.CACHED_TABLE_OK;
    }
    
//  /**
//   * checks whether cached_amp_activity_group has bazillion indices (a bug in AMP 2.6-AMP 2.11) and forces PublicViewCaches cleanup if so
//   * @param conn
//   */
//  public static void checkPublicCaches() {
//      try(java.sql.Connection conn = PersistenceManager.getJdbcConnection()) {
//          long nrIndices = SQLUtils.getLong(conn, "select count(*) from pg_indexes where tablename='cached_amp_activity_group'");
//          if (nrIndices > 4) { // normally 2, but let's leave some slack - will be cleaned up at next full run anyway
//              PublicViewColumnsUtil.maintainPublicViewCaches(conn, true);
//          }
//      }
//      catch(SQLException e) {
//          throw new RuntimeException(e);
//      }
//  }

    /**
     * unconditionally redoes all the public view caches
     */
    public static void redoCaches() {
        try(java.sql.Connection conn = PersistenceManager.getJdbcConnection()) {
            maintainPublicViewCaches(conn, true);
        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * strategy of this function:
     * <ul>
     *  <li>non-existing tables will always be created and populated</li>
     *  <li>existing_and_correct tables will dropped, created and updated if updateData = true</li>
     *   <li>existing but faulty tables will be dropped and recreated if updateData = true </li>
     * </ul>
     * @param conn
     * @param updateSchemaIfDifferent
     * @param updateData if false, only check & update database scheme; if true - update data unconditionally
     */
    public static void maintainPublicViewCaches(java.sql.Connection conn, boolean updateData)
    {       
        logger.info(String.format("doing maintenance on public view caches, updateData = %b", updateData));
        Map<String, CachedTableState> viewsStates = getExtractorColumns(conn);
        for(String viewName:viewsStates.keySet())
        {
            CachedTableState viewState = viewsStates.get(viewName);
            if (viewState != CachedTableState.CACHED_TABLE_OK)
                logger.info(String.format("the view %s's cache has the schema state %s", viewName, viewState));
            try
            {
                doColumnMaintenance(conn, viewName, viewState, updateData);
            }
            catch(Exception e)
            {
                logger.error("error while doing maintenance on the view!", e);
            }
        }

        TreeSet<String> missingViews = new TreeSet<>();
        for(String viewName:viewsStates.keySet())
            if (viewsStates.get(viewName) == CachedTableState.ORIGINAL_TABLE_MISSING)
                missingViews.add(viewName);
        
        if (!missingViews.isEmpty()) {
            logger.fatal("--------------------------------------------------------------------------");
            logger.fatal("DO NOT IGNORE THIS MESSAGE OR DISABLE THE CHECK."); 
            logger.fatal("This is a list of columns referencing non-existant views:\n" + missingViews.toString() + "\n"); 
            logger.fatal("FIX THE DATABASE.");
            logger.fatal("--------------------------------------------------------------------------");
            
            if (CRASH_ON_INVALID_COLUMNS)
                throw new Error("The following columns reference non-existant views: " + missingViews.toString());
        }
        
        
    }
    
    private static void doColumnMaintenance(java.sql.Connection conn, String viewName, CachedTableState viewState, boolean updateData)
    {
        switch(viewState)
        {
            case ORIGINAL_TABLE_MISSING:
                break;
            case CACHED_TABLE_MISSING:
            {
                String cachedView = getPublicViewTable(viewName);
                createTableCache(conn, viewName, cachedView);                   
            }
            break;
            
            case CACHED_TABLE_DIFFERENT_STRUCTURE:
            {
//              if (!updateData)
//              {
//                  logger.info("\t->not allowed to update table schema, skipping");
//                  return;
//              }
                String cachedView = getPublicViewTable(viewName);
                createTableCache(conn, viewName, cachedView);                   
            }
            break;
            
            case CACHED_TABLE_OK:
            {
                if (!updateData)
                {
                    //logger.info("\t->not allowed to refresh table, skipping");
                    return;
                }
                String cachedView = getPublicViewTable(viewName);
                createTableCache(conn, viewName, cachedView); // recreating table from scratch anyway, because God-knows-what we have there (maybe it is the first script execution and the awful state pre-AMP 2.7.1 is still there
            }           
        }

    }
    
    /**
     * drops a table and replaces it with DDL, data and indices of a view/table
     * @param conn
     * @param viewName
     * @param cacheName
     */
    protected static void createTableCache(java.sql.Connection conn, String viewName, String cacheName)
    {
        logger.info(String.format("\t->creating a cache named %s for view %s...", cacheName, viewName));
        cacheName = cacheName.toLowerCase();
        boolean createIndices = false;
/*      if (cacheName.equals("cached_amp_activity_group") && SQLUtils.tableExists(cacheName)) {
            SQLUtils.executeQuery(conn, String.format("DELETE FROM %s", cacheName));
            SQLUtils.executeQuery(conn, String.format("INSERT INTO %s SELECT * FROM %s WHERE amp_activity_last_version_id IS NOT NULL", cacheName, viewName));
        }
        else */
        {
            SQLUtils.executeQuery(conn, String.format("DROP TABLE IF EXISTS %s", cacheName));
            LinkedHashSet<String> cols = SQLUtils.getTableColumns(viewName);
            
            String condition;
            if ((cols.size() > 0) && cols.iterator().next().toLowerCase().equals("amp_activity_id"))
                condition = "WHERE amp_activity_id IN (SELECT amp_activity_id FROM v_activity_latest_and_validated)";
            else {
                if (viewName.equals("v_pledges_funding_st"))
                    condition = "WHERE (related_project_id IN (SELECT amp_activity_id FROM v_activity_latest_and_validated)) OR (pledge_id > 0)";
                else 
                    condition = "";
            }
            SQLUtils.executeQuery(conn, String.format("CREATE TABLE %s AS SELECT * FROM %s %s;", cacheName, viewName, condition));
            SQLUtils.executeQuery(conn, String.format("GRANT SELECT ON " + cacheName + " TO public")); // AMP-17052: cache tables should be world-visible
            createIndices = true;
        }
        
        if (createIndices) {
            Collection<String> columns = SQLUtils.getTableColumns(viewName);
            for(String columnName:columns)
                if (looksLikeIndexableColumn(viewName, columnName)) {
                    logger.debug(String.format("\t\t...creating an index for column %s of cached table %s", columnName, cacheName));
                    SQLUtils.executeQuery(conn, String.format("CREATE INDEX ON %s(%s)", cacheName, columnName));
                }
        }
        SQLUtils.flush(conn);
    }
    
    /**
     * decides whether a particular column of a particular view should have an index on top of it.<br />
     * current implementation is a quick-and-dirty-and-good-enough heuristics which ignores the viewName. For cases when the heuristics fails, hardcoded cases can be added
     * @param viewName
     * @param columnName
     * @return
     */
    protected static boolean looksLikeIndexableColumn(String viewName, String columnName)
    {
        columnName = columnName.toLowerCase();
        return columnName.endsWith("id") || columnName.startsWith("id") || columnName.endsWith("_type") || columnName.endsWith("_code");
    }
    
}
