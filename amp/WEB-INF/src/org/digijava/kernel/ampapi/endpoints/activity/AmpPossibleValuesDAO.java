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
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpComponentType;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;

/**
 * @author Octavian Ciubotaru
 */
public class AmpPossibleValuesDAO implements PossibleValuesDAO {

    private static final String CACHE = "org.digijava.kernel.ampapi.endpoints.activity.AmpPossibleValuesDAO";

    @Override
    public List<Object[]> getCategoryValues(String discriminatorOption) {
        String queryString = "SELECT acv.id, acv.value, acv.deleted, acv.index from "
                + AmpCategoryValue.class.getName() + " acv "
                + "WHERE acv.ampCategoryClass.keyName ='" + discriminatorOption + "' ORDER BY acv.id";
        return query(queryString);
    }

    @Override
    public <T> List<T> getGenericValues(Class<T> entity) {
        return InterchangeUtils.getSessionWithPendingChanges()
                .createCriteria(entity)
                .setCacheable(true)
                .setCacheRegion(CACHE)
                .list();
    }

    @Override
    public List<Object[]> getSectors(String configType) {
        return getSpecialCaseObjectList(configType, "all_sectors_with_levels",
                "ampSectorId", "name", "parentSectorId.ampSectorId",
                "sector_config_name", "amp_sector_id", AmpSector.class);
    }

    @Override
    public List<Object[]> getThemes(String configType) {
        return getSpecialCaseObjectList(configType, "all_programs_with_levels",
                "ampThemeId", "name", "parentThemeId.ampThemeId",
                "program_setting_name", "amp_theme_id", AmpTheme.class);
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
    private List<Object[]> getSpecialCaseObjectList(final String configType, final String configTableName,
            String entityIdColumnName, String entityValueColumnName, String entityParentIdColumnName,
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
                + "cls." + entityValueColumnName + ", cls." + entityParentIdColumnName + " "
                + " from " + clazz.getName() + " cls where cls." + entityIdColumnName + " in (" + ids + ")";

        return query(queryString);
    }

    @Override
    public List<Object[]> getPossibleLocations() {
        String queryString = "SELECT loc.id, acvl.id, acvl.name, acvlParent.id, acvlParent.name, "
                + "parentCat.id, parentCat.value, acvl.iso "
                + " FROM " + AmpLocation.class.getName() + " loc "
                + " LEFT JOIN loc.location AS acvl"
                + " LEFT JOIN acvl.parentLocation AS acvlParent"
                + " LEFT JOIN acvl.parentCategoryValue AS parentCat"
                + " ORDER BY loc.id";
        return query(queryString);
    }

    @Override
    public AmpClassificationConfiguration getAmpClassificationConfiguration(String name) {
        return (AmpClassificationConfiguration) InterchangeUtils.getSessionWithPendingChanges()
                .createCriteria(AmpClassificationConfiguration.class)
                .add(Restrictions.eq("name", name))
                .setCacheable(true)
                .setCacheRegion(CACHE)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> query(String queryString) {
        return (List<Object[]>) InterchangeUtils.getSessionWithPendingChanges().createQuery(queryString).list();
    }

    @Override
    public List<AmpComponentType> getComponentTypes() {
        return ComponentsUtil.getAmpComponentTypes(true);
    }
    
    @Override
    public List<AmpContact> getContacts() {
        return InterchangeUtils.getSessionWithPendingChanges()
                .createCriteria(AmpContact.class)
                .setCacheable(true)
                .setCacheRegion(CACHE)
                .list();
    }

}
