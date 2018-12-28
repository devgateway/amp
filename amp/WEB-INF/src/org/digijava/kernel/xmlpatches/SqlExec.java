package org.digijava.kernel.xmlpatches;

import java.sql.Connection;
import java.text.MessageFormat;
import java.util.List;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * @author Octavian Ciubotaru
 */
public final class SqlExec {

    private SqlExec() {
    }

    /**
     * A helper method to execute any sql without having to switch from bsh to sql executor.
     * @param sql sql to execute
     */
    public static void exec(String sql) {
        PersistenceManager.doWorkInTransaction(c -> SQLUtils.executeQuery(c, sql));
    }

    /**
     * Execute an arbitrary sql for each matched table.
     * @param tableNameExpression to be passed to pg's like operator
     * @param sqlTemplate to be formatted via {@link MessageFormat} with table name as parameter
     */
    public static void tableMap(String tableNameExpression, String sqlTemplate) {
        PersistenceManager.doWorkInTransaction(c -> tableMap(c, tableNameExpression, sqlTemplate));
    }

    private static void tableMap(Connection connection, String tableNameExpression, String sqlTemplate) {
        List<String> tableNames = SQLUtils.fetchStrings(connection,
                String.format(
                        "SELECT tablename "
                                + "FROM pg_catalog.pg_tables "
                                + "WHERE schemaname = 'public' "
                                + "AND tablename like '%s'", tableNameExpression));
        for (String tableName : tableNames) {
            String sql = MessageFormat.format(sqlTemplate, tableName);
            SQLUtils.executeQuery(connection, sql);
        }
    }

    /**
     * Execute an arbitrary sql for each matched view.
     * @param viewNameExpression to be passed to pg's like operator
     * @param sqlTemplate to be formatted via {@link MessageFormat} with view name as parameter
     */
    public static void viewMap(String viewNameExpression, String sqlTemplate) {
        PersistenceManager.doWorkInTransaction(c -> viewMap(c, viewNameExpression, sqlTemplate));
    }

    private static void viewMap(Connection connection, String viewNameExpression, String sqlTemplate) {
        List<String> viewNames = SQLUtils.fetchStrings(connection,
                String.format(
                        "SELECT viewname "
                                + "FROM pg_catalog.pg_views "
                                + "WHERE schemaname = 'public' "
                                + "AND viewname like '%s'", viewNameExpression));
        for (String viewName : viewNames) {
            String sql = MessageFormat.format(sqlTemplate, viewName);
            SQLUtils.executeQuery(connection, sql);
        }
    }
}
