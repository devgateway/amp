package org.digijava.kernel.ampapi.endpoints.activity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import clover.org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.jdbc.Work;

/**
 * @author Octavian Ciubotaru
 */
public class AmpPossibleValuesDAO implements PossibleValuesDAO {

    @Override
    public List<Object[]> getCategoryValues(String discriminatorOption) {
        @SuppressWarnings("unchecked")
        String queryString = "SELECT acv.id, acv.value, acv.deleted from " + AmpCategoryValue.class.getName() + " acv "
                + "WHERE acv.ampCategoryClass.keyName ='" + discriminatorOption + "' ORDER BY acv.id";
        return (List<Object[]>) InterchangeUtils.getSessionWithPendingChanges().createQuery(queryString).list();
    }

    @Override
    public List<Object[]> getGenericValues(Class<?> clazz, String idFieldName, String valueFieldName) {
        String queryString = "SELECT cls." + idFieldName + ", cls." + valueFieldName
                + " FROM " + clazz.getName() + " cls ORDER BY " + idFieldName;
        return (List<Object[]>) InterchangeUtils.getSessionWithPendingChanges().createQuery(queryString).list();
    }

    /**
     * Method that wraps generic approaches for the programs, sector and org. role entities
     * @param configType
     * @param configTableName
     * @param entityIdColumnName
     * @param conditionColumnName
     * @param idColumnName
     * @param clazz
     * @return
     */
    @Override
    public List<Object[]> getSpecialCaseObjectList(final String configType, final String configTableName,
            String entityIdColumnName, String entityValueColumnName,
            final String conditionColumnName, final String idColumnName, Class<?> clazz) {

        final List<Long> itemIds = new ArrayList<Long>();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String allSectorsQuery = "SELECT " + idColumnName + " FROM " + configTableName + " WHERE "
                        + conditionColumnName + "='" + configType + "'" + " ORDER BY " + idColumnName;
                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
                    ResultSet rs = rsi.rs;
                    while (rs.next()) {
                        itemIds.add(rs.getLong(idColumnName));
                    }
                    rs.close();
                }
            }
        });

        if (itemIds.size() == 0) {
            return new ArrayList<>();
        }

        String ids = StringUtils.join(itemIds, ",");
        String queryString = "select cls." + entityIdColumnName + ", "
                + "cls." + entityValueColumnName + " "
                + " from " + clazz.getName() + " cls where cls." + entityIdColumnName + " in (" + ids + ")";

        return (List<Object[]>) InterchangeUtils.getSessionWithPendingChanges().createQuery(queryString).list();
    }

    @Override
    public List<Object[]> getPossibleLocations() {
        String queryString = "SELECT loc.id, acvl.name, parentLoc.id, parentLoc.name"
                + " ,parentCat.id, parentCat.value"
                + " from " + AmpLocation.class.getName() + " loc "
                + " LEFT JOIN loc.location as acvl"
                + " LEFT JOIN acvl.parentLocation as parentLoc"
                + " LEFT JOIN acvl.parentCategoryValue as parentCat"
                + " ORDER BY loc.id";
        return (List<Object[]>) InterchangeUtils.getSessionWithPendingChanges().createQuery(queryString).list();
    }
}
