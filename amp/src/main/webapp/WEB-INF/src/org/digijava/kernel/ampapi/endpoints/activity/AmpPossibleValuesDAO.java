package org.digijava.kernel.ampapi.endpoints.activity;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.GenericPossibleValuesProvider;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeMapping;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
public class AmpPossibleValuesDAO implements PossibleValuesDAO {

    public static final String CACHE = "org.digijava.kernel.ampapi.endpoints.activity.AmpPossibleValuesDAO";

    private static final int LOC_QUERY_COL_NUM = 8;


    /**
     * If there are workspace prefixes then look for the main category (ie: "modalities") and the related
     * categories (ie: SSC_modalities).
     * @param discriminatorOption
     * @return
     */
    @Override
    public List<Object[]> getCategoryValues(String discriminatorOption) {
        String select = "SELECT acv.id, acv.value, acv.deleted, acv.index ";
        String from = "from " + AmpCategoryValue.class.getName() + " acv ";
        String where;
        List<String> prefixes = ActivityUtil.getWorkspacePrefixesFromRequest();
        if (prefixes == null) {
            prefixes = ActivityUtil.loadWorkspacePrefixesIntoRequest();
        }
        if (prefixes != null && prefixes.size() > 0) {
            where = " WHERE acv.ampCategoryClass.keyName IN (";
            for (String prefix : prefixes) {
                where += "'" + prefix + discriminatorOption + "', ";
            }
            where += "'" + discriminatorOption + "') ORDER BY acv.id";
            select += ", acv.ampCategoryClass.keyName ";
            List<Object[]> result = query(select + from + where);
            result.forEach(row -> {
                String value = row[CategoryValueExtraInfo.EXTRA_INFO_PREFIX_INDEX].toString();
                row[CategoryValueExtraInfo.EXTRA_INFO_PREFIX_INDEX] = value.replace(discriminatorOption, "");
            });
            return result;
        } else {
            where = " WHERE acv.ampCategoryClass.keyName LIKE '" + discriminatorOption + "' ORDER BY acv.id";
            select += ", '' ";
            return query(select + from + where);
        }
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

        String queryString = "SELECT acvl.id, acvl.location_name, acvlParent.id, acvlParent.location_name,"
                + " parentCat.id, parentCat.category_value, acvl.iso, al.amp_location_id"
                + " FROM amp_category_value_location acvl"
                + " LEFT JOIN amp_category_value_location acvlParent on acvl.parent_location=acvlParent.id"
                + " LEFT JOIN amp_category_value parentCat ON acvl.parent_category_value = parentCat.id"
                + " LEFT JOIN amp_location al ON acvl.id = al.location_id"
                + " WHERE NOT acvl.deleted"
                + " OR acvl.deleted IS NULL"
                + " ORDER BY acvl.id";

        List<Object[]> result = new ArrayList<>();

        PersistenceManager.getSession().doWork(conn -> {
            try (RsInfo rsi = SQLUtils.rawRunQuery(conn, queryString, null)) {
                ResultSet rs = rsi.rs;
                while (rs.next()) {
                    Object[] row = new Object[LOC_QUERY_COL_NUM];
                    for (int i = 0; i < LOC_QUERY_COL_NUM; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    result.add(row);
                }
            }
        });

        return result;
    }

    public boolean isLocationValid(Long id) {
        return GenericPossibleValuesProvider.isAllowed(AmpCategoryValueLocations.class, id);
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

    @Override
    public List<AmpThemeMapping> getMappedThemes() {
        return PersistenceManager.getRequestDBSession()
                .createCriteria(AmpThemeMapping.class)
                .setCacheable(true)
                .list();
    }
}
