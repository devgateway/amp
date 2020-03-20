package org.digijava.kernel.ampapi.endpoints.activity;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.GenericPossibleValuesProvider;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.criterion.Restrictions;

/**
 * @author Octavian Ciubotaru
 */
public class AmpPossibleValuesDAO implements PossibleValuesDAO {

    public static final String CACHE = "org.digijava.kernel.ampapi.endpoints.activity.AmpPossibleValuesDAO";

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

        final List<Long> itemIds = new ArrayList<>();
        PersistenceManager.getSession().doWork(conn -> {
            String allSectorsQuery = "SELECT " + idColumnName + " FROM " + configTableName + " WHERE "
                    + conditionColumnName + "='" + configType + "'" + " ORDER BY " + idColumnName;
            try (RsInfo rsi = SQLUtils.rawRunQuery(conn, allSectorsQuery, null)) {
                ResultSet rs = rsi.rs;
                while (rs.next()) {
                    itemIds.add(rs.getLong(idColumnName));
                }
                rs.close();
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

    public boolean isSectorValid(String configType, Long id) {
        return isValid("all_sectors_with_levels", "sector_config_name", configType, "amp_sector_id", id);
    }

    public boolean isThemeValid(String configType, Long id) {
        return isValid("all_programs_with_levels", "program_setting_name", configType, "amp_theme_id", id);
    }

    private boolean isValid(String tableName, String configColName, String configType, String idColName, Long id) {
        String query = "SELECT count(" + idColName + ") FROM " + tableName + " WHERE " + configColName + "='"
                + configType + "' AND " + idColName + "=" + id;
        BigInteger count = (BigInteger) PersistenceManager.getSession().createSQLQuery(query).uniqueResult();
        return count.intValue() == 1;
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

    public boolean isLocationValid(Long id) {
        return GenericPossibleValuesProvider.isAllowed(AmpLocation.class, id);
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
        return (List<Object[]>) InterchangeUtils.getSessionWithPendingChanges()
                .createQuery(queryString)
                .setCacheable(true)
                .setCacheRegion(CACHE)
                .list();
    }

    @Override
    public List<AmpOrganisation> getOrganisations() {
        return InterchangeUtils.getSessionWithPendingChanges()
                .createCriteria(AmpOrganisation.class)
                .add(Restrictions.or(
                        Restrictions.eq("deleted", false),
                        Restrictions.isNull("deleted")
                    ))
                .setCacheable(true)
                .setCacheRegion(CACHE)
                .list();
    }

    public boolean isOrganizationValid(Long id) {
        AmpOrganisation o = (AmpOrganisation) InterchangeUtils.getSessionWithPendingChanges()
                .get(AmpOrganisation.class, id);
        return o != null && !Boolean.TRUE.equals(o.getDeleted());
    }

    @Override
    public List<AmpIndicatorRiskRatings> getIndicatorRiskRatings() {
        return InterchangeUtils.getSessionWithPendingChanges()
                .createCriteria(AmpIndicatorRiskRatings.class)
                .setCacheable(true)
                .setCacheRegion(CACHE)
                .list();
    }

    @Override
    public boolean isIndicatorRiskRatingValid(Long id) {
        return InterchangeUtils.getSessionWithPendingChanges().get(AmpIndicatorRiskRatings.class, id) != null;
    }

    @Override
    public List<AmpIndicator> getIndicators() {
        return InterchangeUtils.getSessionWithPendingChanges()
                .createCriteria(AmpIndicator.class)
                .setCacheable(true)
                .setCacheRegion(CACHE)
                .list();
    }

    @Override
    public boolean isIndicatorValid(Long id) {
        return InterchangeUtils.getSessionWithPendingChanges().get(AmpIndicator.class, id) != null;
    }
}
