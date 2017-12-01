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
            String fetchWrongData = " select true from ("
                    + " select 1 from %s where latitude !~ '^[-]?[0-9]*.?[0-9]*$' "
                    + " or longitude !~ '^[-]?[0-9]*.?[0-9]*$' "
                    + " or latitude::numeric > %s or latitude::numeric< -%<s "
                    + "or longitude::numeric> %s or longitude::numeric< -%<s limit 1 ) as t";

            String addColumn = "ALTER TABLE %s ADD COLUMN %s_to_fix text";
            String updateColumn = "update %s set %s_to_fix = %<s where %<s !~ '^[-]?[0-9]*.?[0-9]*$' or "
                    + "  %<s::numeric not between -%s and %<s";
            String makeNullOriginal = "update %s set %s = null where %<s !~ '^[-]?[0-9]*.?[0-9]*$'";

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
            String alterColumn = "alter table %s alter column %s type numeric(9,6) USING %<s::numeric(9,6)";
            SQLUtils.executeQuery(connection, String.format(alterColumn, tableName, "latitude"));
            SQLUtils.executeQuery(connection, String.format(alterColumn, tableName, "longitude"));
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
