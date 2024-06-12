package org.digijava.kernel.ampapi.endpoints.activity;

import org.apache.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.common.values.providers.GenericPossibleValuesProvider;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;


import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
public class AmpPossibleValuesDAO implements PossibleValuesDAO {
    private static Logger logger = Logger.getLogger(AmpPossibleValuesDAO.class);

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
        StringBuilder where;
        List<String> prefixes = ActivityUtil.getWorkspacePrefixesFromRequest();
        if (prefixes == null) {
            prefixes = ActivityUtil.loadWorkspacePrefixesIntoRequest();
        }
        if (prefixes != null && prefixes.size() > 0) {
            where = new StringBuilder(" WHERE acv.ampCategoryClass.keyName IN (");
            for (String prefix : prefixes) {
                where.append("'").append(prefix).append(discriminatorOption).append("', ");
            }
            where.append("'").append(discriminatorOption).append("') ORDER BY acv.id");
            select += ", acv.ampCategoryClass.keyName ";
            List<Object[]> result = query(select + from + where);
            result.forEach(row -> {
                String value = row[CategoryValueExtraInfo.EXTRA_INFO_PREFIX_INDEX].toString();
                row[CategoryValueExtraInfo.EXTRA_INFO_PREFIX_INDEX] = value.replace(discriminatorOption, "");
            });
            return result;
        } else {
            where = new StringBuilder(" WHERE acv.ampCategoryClass.keyName LIKE '" + discriminatorOption + "' ORDER BY acv.id");
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
        Long longCount = (Long)PersistenceManager.getSession().createNativeQuery(query).uniqueResult();
        BigInteger count = BigInteger.valueOf(longCount);
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
    public List<AmpIndicator> getIndicators() throws DgException {
        boolean filterIndicatorsByProgram= FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.FILTER_INDICATORS_BY_PROGRAM);

        List<AmpIndicator> indicators= InterchangeUtils.getSessionWithPendingChanges()
                .createCriteria(AmpIndicator.class)
                .setCacheable(true)
                .setCacheRegion(CACHE)
                .list();
        List<AmpIndicator> filteredIndicators = new ArrayList<>();
        logger.info("Filter indicators by program: "+filterIndicatorsByProgram);
        if (filterIndicatorsByProgram)
        {
            String globalProgramScheme = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GLOBAL_PROGRAM_SCHEME);
            logger.info("Global program scheme: "+globalProgramScheme);
            if (globalProgramScheme!=null) {
                Long programSettingId = Long.parseLong(globalProgramScheme);
                Session session = PersistenceManager.getRequestDBSession();
                String hql = "FROM " + AmpTheme.class.getName() + " t JOIN FETCH t.siblings JOIN FETCH t.programSettings ps WHERE ps.ampProgramSettingsId= :settingId";

                Query query = session.createQuery(hql);
                query.setParameter("settingId", programSettingId, LongType.INSTANCE);
                Set<AmpTheme> globalSchemePrograms = new HashSet<>(query.list());
                globalSchemePrograms.forEach(ampTheme -> processThemeWithChildren(ampTheme, globalSchemePrograms));
                Set<Long> globalSchemeProgramIds = globalSchemePrograms.stream().map(AmpTheme::getAmpThemeId).collect(Collectors.toSet());
//               globalSchemePrograms.forEach(scheme->{
//                   Set<Long> children = scheme.getSiblings().stream().map(AmpTheme::getAmpThemeId).collect(Collectors.toSet());
//                   logger.info("Children: "+children);
//                   globalSchemeProgramIds.addAll(children);
//               });
                logger.info("Global scheme programs: "+globalSchemeProgramIds);
                for (AmpIndicator indicator : indicators) {
                    List<BigInteger> indicatorPrograms= getProgramIds(indicator.getIndicatorId());
                    logger.info("Indicator programs: "+indicatorPrograms);

                    for (BigInteger progId : indicatorPrograms) {
                        if (progId!=null) {
                            boolean containsProgram = globalSchemeProgramIds.contains(progId.longValue());
                            if (containsProgram) {
                                filteredIndicators.add(indicator);
                                break;
                            }
                        }
                    }
                }
            }

        }


        return !filterIndicatorsByProgram?indicators:filteredIndicators;
    }

    public static void processThemeWithChildren(AmpTheme theme, Set<AmpTheme> allThemes) {
        // Process child themes (siblings) recursively
        allThemes.add(theme);
        for (AmpTheme childTheme : theme.getSiblings()) {
            logger.info("Processing child theme: "+childTheme.getName());
            processThemeWithChildren(childTheme, allThemes);
        }
    }


    private List<BigInteger> getProgramIds(Long indicatorId) {
        Session session = PersistenceManager.getRequestDBSession();
        String sql = "SELECT theme_id FROM AMP_INDICATOR_CONNECTION " +
                "WHERE indicator_id = :indicatorId AND sub_clazz='p'";
        List<BigInteger> themeIds = session.createNativeQuery(sql)
                .setParameter("indicatorId", indicatorId, LongType.INSTANCE)
                .getResultList();
        return themeIds;

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
