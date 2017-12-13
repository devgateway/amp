package org.digijava.module.aim.startup;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Jdeanquin
 */
public class AmpUpdateStructureLatitudeLongitude {

    protected Logger logger = LoggerFactory.getLogger(AmpUpdateStructureLatitudeLongitude.class);

    private DateFormat oldFormat = new SimpleDateFormat(FeaturesUtil.getGlobalSettingValue(
            GlobalSettingsConstants.DEFAULT_DATE_FORMAT));
    private SimpleDateFormat newFormat = new SimpleDateFormat(AmpARFilter.SDF_IN_FORMAT_STRING);

    public AmpUpdateStructureLatitudeLongitude() {

    }

    private void updateLatitudeAndLongitude() {
        PersistenceManager.getSession().doWork(connection -> {
            updateTable(connection, "amp_structure");
            updateTable(connection, "amp_structure_coordinate");
        });
    }

    private void updateTable(Connection connection, String tableName) throws SQLException {
        RsInfo structuresToChange = null;
        try {
            String fetchWrongData = " SELECT true FROM ("
                    + " SELECT 1 FROM %s WHERE latitude !~ '^[-]?[0-9]*.?[0-9]*$' "
                    + " OR longitude !~ '^[-]?[0-9]*.?[0-9]*$' "
                    + " OR latitude::numeric > %s OR latitude::numeric< -%<s "
                    + " OR longitude::numeric> %s OR longitude::numeric< -%<s LIMIT 1 ) AS t";

            String addColumn = "ALTER TABLE %s ADD COLUMN %s_to_fix text";
            String updateColumn = "UPDATE %s SET %s_to_fix = %<s WHERE %<s !~ '^[-]?[0-9]*.?[0-9]*$' OR "
                    + "  %<s::numeric NOT BETWEEN -%s AND %<s";
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
            
            String alterColumn = "ALTER TABLE %s ALTER COLUMN %s type numeric(9,6) USING %<s::numeric(9,6)";
            SQLUtils.executeQuery(connection, String.format(alterColumn, tableName, "latitude"));
            SQLUtils.executeQuery(connection, String.format(alterColumn, tableName, "longitude"));
            
            String addData = "UPDATE %s SET %s = %s";
            SQLUtils.executeQuery(connection, String.format(addData, tableName, "coord_lat", "latitude"));
            SQLUtils.executeQuery(connection, String.format(addData, tableName, "coord_long", "longitude"));
            
            String dropColumn = "ALTER TABLE %s DROP column %s";
            SQLUtils.executeQuery(connection, String.format(dropColumn, tableName, "latitude"));
            SQLUtils.executeQuery(connection, String.format(dropColumn, tableName, "longitude"));
        } catch (Exception e) {
            logger.error("cannot udpdate structures", e);
            throw e;
        } finally {
            structuresToChange.close();
        }
    }

    public void work() {
        updateLatitudeAndLongitude();
    }
}
