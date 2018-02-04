package org.digijava.module.aim.startup;

import java.sql.Connection;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jdeanquin
 */
public class AmpUpdateStructureLatitudeLongitude {

    protected Logger logger = LoggerFactory.getLogger(AmpUpdateStructureLatitudeLongitude.class);

    public AmpUpdateStructureLatitudeLongitude() {

    }

    private void updateLatitudeAndLongitude() {
        Session session = PersistenceManager.openNewSession();
        Transaction tx = session.beginTransaction();
        try {
            session.doWork(connection -> {
                updateTable(connection, "amp_structure");
                updateTable(connection, "amp_structure_coordinate");
            });
        } catch (Throwable e) {
            tx.rollback();
            throw e;
        } finally {
            PersistenceManager.closeSession(session);
        }
    }

    private void updateTable(Connection connection, String tableName) throws SQLException {
        
        if (!shouldUpdateStructureTable(connection, tableName)) {
            return;
        }
        
        RsInfo structuresToChange = null;
        try {
            String fetchWrongData = " SELECT true FROM ("
                    + " SELECT 1 FROM %s WHERE latitude !~ '^[-]?[0-9]*.?[0-9]*$' "
                    + " OR longitude !~ '^[-]?[0-9]*.?[0-9]*$' "
                    + " OR latitude::numeric > %s OR latitude::numeric< -%<s "
                    + " OR longitude::numeric> %s OR longitude::numeric< -%<s LIMIT 1 ) AS t";

            String addColumn = "ALTER TABLE %s ADD COLUMN %s_to_fix text";
            String updateColumn = "UPDATE %s SET %s_to_fix = %<s WHERE %<s !~ '^[-]?[0-9]*.?[0-9]*$'"
                    + " OR  %<s::numeric NOT BETWEEN -%s AND %<s";
            String makeNullOriginal = "UPDATE %s SET %s = null WHERE %<s !~ '^[-]?[0-9]*.?[0-9]*$'";

            structuresToChange = SQLUtils.rawRunQuery(connection, 
                    String.format(fetchWrongData, tableName, "90", "180"), null);

            if (structuresToChange.rs.next()) {
                SQLUtils.executeQuery(connection, String.format(addColumn, tableName, "latitude"));
                SQLUtils.executeQuery(connection, String.format(updateColumn, tableName, "latitude", "90"));
                SQLUtils.executeQuery(connection, String.format(addColumn, tableName, "longitude"));
                SQLUtils.executeQuery(connection, String.format(updateColumn, tableName, "longitude", "180"));
                SQLUtils.executeQuery(connection, String.format(makeNullOriginal, tableName, "latitude"));
                SQLUtils.executeQuery(connection, String.format(makeNullOriginal, tableName, "longitude"));
                logger.error("!!!!!!!!!!!!!YOU NEED TO FIX LATITUDE AND LONGITUDE IN " + tableName + " TABLE");
            }
            
            String alterColumn = "ALTER TABLE %s ALTER COLUMN %s type numeric(11,8) USING %<s::numeric(11,8)";
            SQLUtils.executeQuery(connection, String.format(alterColumn, tableName, "latitude"));
            SQLUtils.executeQuery(connection, String.format(alterColumn, tableName, "longitude"));
            
            String addData = "UPDATE %s SET %s = %s";
            SQLUtils.executeQuery(connection, String.format(addData, tableName, "coord_lat", "latitude"));
            SQLUtils.executeQuery(connection, String.format(addData, tableName, "coord_long", "longitude"));
            
            String dropColumn = "ALTER TABLE %s DROP column %s";
            SQLUtils.executeQuery(connection, String.format(dropColumn, tableName, "latitude"));
            SQLUtils.executeQuery(connection, String.format(dropColumn, tableName, "longitude"));
        } catch (Exception e) {
            logger.error("cannot update structures", e);
            throw e;
        } finally {
            structuresToChange.close();
        }
    }

    /**
     * Check if the table contains the longitude and latitude columns.
     * (some DBs don't contain table with such columns, db version < 2.13)
     * 
     * @param tableName
     * @return boolean if the columns exists
     * @throws SQLException 
     */
    private boolean shouldUpdateStructureTable(Connection connection, String tableName) throws SQLException {
        String query = "SELECT column_name FROM information_schema.columns "
                + "WHERE table_name='%s' AND column_name in ('latitude', 'longitude')";
        RsInfo existColumns = null;
        
        try {
            existColumns = SQLUtils.rawRunQuery(connection, String.format(query, tableName), null);
            return existColumns.rs.next();
        } catch (SQLException e) {
            logger.error("Cannot check if the columns longitude and latitude exists in the table " + tableName, e);
            throw e;
        } finally {
            existColumns.close();
        }
    }

    public void work() {
        updateLatitudeAndLongitude();
    }
}
