package org.digijava.kernel.services.sync;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.CALENDAR;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.COLLECTION_TYPE_ACTIVITY;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.COLLECTION_TYPE_CONTACT;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.COLLECTION_TYPE_RESOURCE;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.CONTACT;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.EXCHANGE_RATES;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.FEATURE_MANAGER;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.LOCATORS;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.MAP_TILES;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.RESOURCE;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.TRANSLATION;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.WORKSPACES;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.GLOBAL_SETTINGS;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.WORKSPACE_FILTER_DATA;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.WORKSPACE_MEMBER;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.WORKSPACE_ORGANIZATIONS;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.WORKSPACE_SETTINGS;
import static org.digijava.kernel.services.sync.model.SyncConstants.Ops.DELETED;
import static org.digijava.kernel.services.sync.model.SyncConstants.Ops.UPDATED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.jackrabbit.util.ISO8601;
import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.kernel.ampapi.endpoints.activity.field.CachingFieldsEnumerator;
import org.digijava.kernel.ampapi.endpoints.activity.utils.ApiFieldStructuralService;
import org.digijava.kernel.ampapi.endpoints.currency.CurrencyService;
import org.digijava.kernel.ampapi.endpoints.currency.dto.ExchangeRatesForPair;
import org.digijava.kernel.ampapi.endpoints.gis.services.MapTilesService;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceService;
import org.digijava.kernel.ampapi.endpoints.sync.SyncRequest;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpFieldsEnumerator;
import org.digijava.kernel.services.sync.model.ActivityChange;
import org.digijava.kernel.services.sync.model.ExchangeRatesDiff;
import org.digijava.kernel.services.sync.model.ListDiff;
import org.digijava.kernel.services.sync.model.ResourceChange;
import org.digijava.kernel.services.sync.model.SystemDiff;
import org.digijava.kernel.services.sync.model.Translation;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpOfflineChangelog;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.repository.AmpOfflineChangelogRepository;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Octavian Ciubotaru
 */
@Component
public class SyncService implements InitializingBean {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String COLUMNS = "entity_name, entity_id, operation_name, operation_time";

    private static final RowMapper<AmpOfflineChangelog> ROW_MAPPER = new AmpOfflineChangelogMapper();

    private static final RowMapper<Long> ID_MAPPER = new SingleColumnRowMapper<>(Long.class);
    private static final RowMapper<Translation> TRANSLATION_ROW_MAPPER = new BeanPropertyRowMapper<>(Translation.class);

    private static final BeanPropertyRowMapper<ActivityChange> ACTIVITY_CHANGE_ROW_MAPPER =
            new BeanPropertyRowMapper<>(ActivityChange.class);

    private PossibleValuesEnumerator possibleValuesEnumerator = PossibleValuesEnumerator.INSTANCE;
    private CachingFieldsEnumerator fieldsEnumerator = AmpFieldsEnumerator.getEnumerator();
    private CurrencyService currencyService = CurrencyService.INSTANCE;

    private ResourceService resourceService = new ResourceService();

    private AmpOfflineChangelogRepository ampOfflineChangelogRepository = AmpOfflineChangelogRepository.INSTANCE;

    /**
     * Used as approximate time of AMP startup. Initialized only once.
     * Used to detect changes newly implemented fields.
     * Suppose a new field was added to activity. There will be no changelog entries for that field, thus new fields
     * could be ignored. If a client synchronizes with AMP and uses a timestamp before startup time, then we'll report
     * fields as being changed. AMP in production is rarely restarted.
     */
    private static final Date STARTUP_TIME = new Date(System.currentTimeMillis());

    @Autowired
    private SyncDAO syncDAO;

    private static class AmpOfflineChangelogMapper implements RowMapper<AmpOfflineChangelog> {

        @Override
        public AmpOfflineChangelog mapRow(ResultSet resultSet, int i) throws SQLException {
            AmpOfflineChangelog changelog = new AmpOfflineChangelog();
            changelog.setEntityName(resultSet.getString(1));
            changelog.setEntityId(resultSet.getString(2));
            changelog.setOperationName(resultSet.getString(3));
            changelog.setOperationTime(resultSet.getTimestamp(4));
            return changelog;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Context initialContext = new InitialContext();
        DataSource dataSource = (DataSource) initialContext.lookup(Constants.UNIFIED_JNDI_ALIAS);
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public SystemDiff diff(SyncRequest syncRequest) {
        Date lastSyncTime = syncRequest.getLastSyncTime();

        SystemDiff systemDiff = new SystemDiff();

        if (lastSyncTime == null) {
            systemDiff.updateTimestamp(new Date());
        }

        ActivityUtil.loadWorkspacePrefixesIntoRequest();

        systemDiff.setActivityFieldsStructuralChanges(existsActivityStructuralChanges(syncRequest));
        systemDiff.setContactFieldsStructuralChanges(existsContactStructuralChanges(syncRequest));
        systemDiff.setResourceFieldsStructuralChanges(existsResourceStructuralChanges(syncRequest));

        updateDiffsForWsAndGs(systemDiff, lastSyncTime);
        updateDiffForWorkspaceMembers(systemDiff, lastSyncTime);
        updateDiffForUsers(systemDiff, lastSyncTime);
        updateDiffsForActivities(systemDiff, syncRequest);
        updateDiffsForContacts(systemDiff, syncRequest);
        updateDiffsForResources(systemDiff, syncRequest);
        updateDiffsForMapTilesAndLocators(systemDiff, lastSyncTime);
        updateDiffsForCalendars(systemDiff, syncRequest);

        systemDiff.setTranslations(shouldSyncTranslations(systemDiff, lastSyncTime));

        systemDiff.setActivityPossibleValuesFields(findUpdatedActivityPossibleValuesFields(systemDiff, syncRequest));
        systemDiff.setContactPossibleValuesFields(findUpdatedContactPossibleValuesFields(systemDiff, syncRequest));
        systemDiff.setResourcePossibleValuesFields(findUpdatedResourcePossibleValuesFields(systemDiff, syncRequest));
        systemDiff.setCommonPossibleValuesFields(findUpdatedCommonPossibleValuesFields(systemDiff, syncRequest));

        systemDiff.setExchangeRates(shouldSyncExchangeRates(lastSyncTime));

        systemDiff.setFields(shouldSyncFieldsDefinitions(lastSyncTime, systemDiff));

        updateDiffForFeatureManager(systemDiff, syncRequest);

        systemDiff.setFields(true);

        if (systemDiff.getTimestamp() == null) {
            systemDiff.setTimestamp(syncRequest.getLastSyncTime());
        }

        return systemDiff;
    }

    private boolean shouldSyncFieldsDefinitions(Date lastSyncTime, SystemDiff systemDiff) {
        return isFirstSync(lastSyncTime)
                || isFirstSyncSinceAMPStartup(lastSyncTime, systemDiff)
                || fieldDefinitionsChanged(lastSyncTime, systemDiff);
    }

    private boolean isFirstSync(Date lastSyncTime) {
        return lastSyncTime == null;
    }

    private boolean isFirstSyncSinceAMPStartup(Date lastSyncTime, SystemDiff systemDiff) {
        if (STARTUP_TIME.after(lastSyncTime)) {
            systemDiff.updateTimestamp(STARTUP_TIME);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tells if fields definitions changed by looking at changelogs.
     */
    private boolean fieldDefinitionsChanged(Date lastSyncTime, SystemDiff systemDiff) {
        Timestamp dateModified = syncDAO.getLastModificationDateForFieldDefinitions();
        if (dateModified == null || dateModified.after(lastSyncTime)) {
            systemDiff.updateTimestamp(dateModified);
            return true;
        } else {
            return false;
        }
    }

    private void updateDiffForFeatureManager(SystemDiff systemDiff, SyncRequest syncRequest) {
        if (syncRequest.getLastSyncTime() == null) {
            systemDiff.setFeatureManager(true);
        } else {
            List<String> entities = asList(GLOBAL_SETTINGS, WORKSPACES, FEATURE_MANAGER);
            List<AmpOfflineChangelog> changelogs = loadChangeLog(syncRequest.getLastSyncTime(), entities);
            systemDiff.setFeatureManager(!changelogs.isEmpty());
            for (AmpOfflineChangelog changelog : changelogs) {
                systemDiff.updateTimestamp(changelog.getOperationTime());
            }
        }
    }

    private boolean shouldSyncTranslations(SystemDiff systemDiff, Date lastSyncTime) {
        if (lastSyncTime == null) {
            return true;
        }

        Timestamp updatedAt = jdbcTemplate.queryForObject(
                "select max(cl.operation_time) "
                        + "from dg_message m, amp_offline_changelog cl "
                        + "where m.message_key = cl.entity_id "
                        + "and m.amp_offline = true "
                        + "and cl.operation_time > :lastSyncTime",
                singletonMap("lastSyncTime", lastSyncTime),
                Timestamp.class);

        systemDiff.updateTimestamp(updatedAt);

        return updatedAt != null;
    }

    private boolean shouldSyncExchangeRates(Date lastSyncTime) {
        return lastSyncTime == null || ratesChanged(lastSyncTime);
    }

    private boolean ratesChanged(Date lastSyncTime) {
        return !findDaysWithModifiedRates(lastSyncTime).isEmpty();
    }

    private List<String> findUpdatedActivityPossibleValuesFields(SystemDiff systemDiff, SyncRequest syncRequest) {
        Date lastSyncTime = syncRequest.getLastSyncTime();
        Predicate<APIField> fieldFilter = getChangedFields(systemDiff, lastSyncTime);
        Set<String> updatedPossibleValuesFields = new HashSet<>(fieldsEnumerator.findActivityFieldPaths(fieldFilter));

        CachingFieldsEnumerator fieldsEnumeratorSSC = AmpFieldsEnumerator.getEnumerator(2L);
        updatedPossibleValuesFields.addAll(fieldsEnumeratorSSC.findActivityFieldPaths(fieldFilter));

        List<String> newPossibleValuesFields = findNewPossibleValuesFields(
                syncRequest.getActivityPossibleValuesFields(), findAllActivityFieldsWithPossibleValues());

        updatedPossibleValuesFields.addAll(newPossibleValuesFields);

        return new ArrayList<>(updatedPossibleValuesFields);
    }

    private List<String> findUpdatedContactPossibleValuesFields(SystemDiff systemDiff, SyncRequest syncRequest) {
        Date lastSyncTime = syncRequest.getLastSyncTime();
        Predicate<APIField> fieldFilter = getChangedFields(systemDiff, lastSyncTime);
        Set<String> updatedPossibleValuesFields = new HashSet<>(fieldsEnumerator.findContactFieldPaths(fieldFilter));

        List<String> newPossibleValuesFields = findNewPossibleValuesFields(
                syncRequest.getContactPossibleValuesFields(), findAllContactFieldsWithPossibleValues());

        updatedPossibleValuesFields.addAll(newPossibleValuesFields);

        return new ArrayList<>(updatedPossibleValuesFields);
    }

    private List<String> findUpdatedResourcePossibleValuesFields(SystemDiff systemDiff, SyncRequest syncRequest) {
        Date lastSyncTime = syncRequest.getLastSyncTime();
        Predicate<APIField> fieldFilter = getChangedFields(systemDiff, lastSyncTime);
        Set<String> updatedPossibleValuesFields = new HashSet<>(fieldsEnumerator.findResourceFieldPaths(fieldFilter));

        List<String> newPossibleValuesFields = findNewPossibleValuesFields(
                syncRequest.getResourcePossibleValuesFields(), findAllResourceFieldsWithPossibleValues());

        updatedPossibleValuesFields.addAll(newPossibleValuesFields);

        return new ArrayList<>(updatedPossibleValuesFields);
    }

    private List<String> findUpdatedCommonPossibleValuesFields(SystemDiff systemDiff, SyncRequest syncRequest) {
        Date lastSyncTime = syncRequest.getLastSyncTime();
        Predicate<APIField> fieldFilter = getChangedFields(systemDiff, lastSyncTime);
        Set<String> updatedPossibleValuesFields = new HashSet<>(fieldsEnumerator.findCommonFieldPaths(fieldFilter));

        List<String> newPossibleValuesFields = findNewPossibleValuesFields(
                syncRequest.getCommonPossibleValuesFields(), findAllCommonFieldsWithPossibleValues());

        updatedPossibleValuesFields.addAll(newPossibleValuesFields);

        return new ArrayList<>(updatedPossibleValuesFields);
    }

    private Predicate<APIField> getChangedFields(SystemDiff systemDiff, Date lastSyncTime) {
        Predicate<APIField> fieldFilter;
        if (lastSyncTime == null) {
            fieldFilter = possibleValuesEnumerator.fieldsWithPossibleValues();
        } else {
            Set<String> allSyncEntities = possibleValuesEnumerator.getAllSyncEntities();
            List<AmpOfflineChangelog> entityList = loadChangeLog(lastSyncTime, allSyncEntities);

            Set<String> changedEntities = new HashSet<>();
            for (AmpOfflineChangelog changelog : entityList) {
                changedEntities.add(changelog.getEntityName());
                systemDiff.updateTimestamp(changelog.getOperationTime());
            }
            fieldFilter = possibleValuesEnumerator.fieldsDependingOn(changedEntities);
        }
        return fieldFilter;
    }

    private List<String> findNewPossibleValuesFields(List<String> requestFields, List<String> allFields) {
        List<String> offlinePossibleValuesFields = new ArrayList<>();
        if (requestFields != null) {
            offlinePossibleValuesFields.addAll(requestFields);
        }

        allFields.removeAll(offlinePossibleValuesFields);

        return allFields;
    }

    private List<String> findAllActivityFieldsWithPossibleValues() {
        return fieldsEnumerator.findActivityFieldPaths(possibleValuesEnumerator.fieldsWithPossibleValues());
    }

    private List<String> findAllContactFieldsWithPossibleValues() {
        return fieldsEnumerator.findContactFieldPaths(possibleValuesEnumerator.fieldsWithPossibleValues());
    }

    private List<String> findAllResourceFieldsWithPossibleValues() {
        return fieldsEnumerator.findResourceFieldPaths(possibleValuesEnumerator.fieldsWithPossibleValues());
    }

    private List<String> findAllCommonFieldsWithPossibleValues() {
        return fieldsEnumerator.findCommonFieldPaths(possibleValuesEnumerator.fieldsWithPossibleValues());
    }

    private void updateDiffsForActivities(SystemDiff systemDiff, SyncRequest syncRequest) {
        List<Long> userIds = syncRequest.getUserIds();
        Date lastSyncTime = syncRequest.getLastSyncTime();

        if (systemDiff.isActivityFieldsStructuralChanges()) {
            lastSyncTime = null;
        }

        List<ActivityChange> allChanges = getAllActivityChanges(lastSyncTime);
        List<ActivityChange> visibleActivities = getVisibleActivities(userIds);

        Set<String> visibleAmpIds = getAmpIds(visibleActivities);
        Set<String> offlineAmpIds = new HashSet<>();
        if (syncRequest.getAmpIds() != null && !systemDiff.isActivityFieldsStructuralChanges()) {
            offlineAmpIds.addAll(syncRequest.getAmpIds());
        }

        Set<String> deleted = new HashSet<>();
        Set<String> modified = new HashSet<>();

        for (ActivityChange change : allChanges) {
            if (visibleAmpIds.contains(change.getAmpId())) {
                if (change.getDeleted()) {
                    deleted.add(change.getAmpId());
                } else {
                    modified.add(change.getAmpId());
                }
                systemDiff.updateTimestamp(change.getModifiedDate());
            }
        }

        deleted.addAll(subtract(offlineAmpIds, visibleAmpIds));
        modified.addAll(subtract(visibleAmpIds, offlineAmpIds));

        systemDiff.setActivities(new ListDiff<>(new ArrayList<>(deleted), new ArrayList<>(modified)));

        Date maxModifiedDate = maxModifiedDate(visibleActivities);
        if (systemDiff.getTimestamp() != null && maxModifiedDate != null
                && maxModifiedDate.after(systemDiff.getTimestamp())) {
            systemDiff.updateTimestamp(maxModifiedDate);
        }
    }

    private Set<String> getAmpIds(List<ActivityChange> changes) {
        Set<String> ampIds = new HashSet<>();
        for (ActivityChange change : changes) {
            ampIds.add(change.getAmpId());
        }
        return ampIds;
    }

    private Date maxModifiedDate(List<ActivityChange> changes) {
        Date maxModifiedDate = null;
        for (ActivityChange change : changes) {
            Date modifiedDate = change.getModifiedDate();
            if (maxModifiedDate == null || (modifiedDate != null && maxModifiedDate.before(modifiedDate))) {
                maxModifiedDate = modifiedDate;
            }
        }
        return maxModifiedDate;
    }

    /**
     * Returns a new {@link Set} containing <tt><i>a</i> - <i>b</i></tt>.
     */
    private <T> Set<T> subtract(Set<T> a, Collection<T> b) {
        Set<T> result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }

    private List<ActivityChange> getVisibleActivities(List<Long> userIds) {
        List<AmpTeamMember> teamMembers = TeamMemberUtil.getNonManagementTeamMembers(userIds);
        if (teamMembers.isEmpty()) {
            return emptyList();
        }

        String wsFilter = getWorkspaceActivitiesSql(teamMembers);
        String sql = String.format("select amp_id ampId, modified_date modifiedDate, deleted "
                + "from amp_activity "
                + "where amp_activity_id in (%s) and amp_id is not null", wsFilter);
        return jdbcTemplate.query(sql, emptyMap(), ACTIVITY_CHANGE_ROW_MAPPER);
    }

    private List<ActivityChange> getAllActivityChanges(Date lastSyncTime) {
        if (lastSyncTime == null) {
            return emptyList();
        }

        String sql = "select amp_id ampId, modified_date modifiedDate, deleted "
                + "from amp_activity "
                + "where modified_date > :lastSyncTime";

        return jdbcTemplate.query(sql, singletonMap("lastSyncTime", lastSyncTime), ACTIVITY_CHANGE_ROW_MAPPER);
    }

    private String getWorkspaceActivitiesSql(List<AmpTeamMember> teamMembers) {
        StringJoiner sql = new StringJoiner(" UNION ");
        for (AmpTeamMember teamMember : teamMembers) {
            sql.add(WorkspaceFilter.generateWorkspaceFilterQuery(teamMember.toTeamMember()));
        }
        return sql.toString();
    }

    private void updateDiffsForContacts(SystemDiff systemDiff, SyncRequest syncRequest) {
        if (syncRequest.getLastSyncTime() == null || systemDiff.isContactFieldsStructuralChanges()) {
            systemDiff.setContacts(new ListDiff<>(emptyList(), ContactInfoUtil.getContactIds()));
        } else {
            List<AmpOfflineChangelog> changeLogs = loadChangeLog(syncRequest.getLastSyncTime(), asList(CONTACT));
            systemDiff.setContacts(toListDiffWithLongs(changeLogs, systemDiff));
        }
    }

    private void updateDiffsForCalendars(SystemDiff systemDiff, SyncRequest syncRequest) {
        if (syncRequest.getLastSyncTime() == null) {
            systemDiff.setCalendars(new ListDiff<>(emptyList(), findAllCalendarIds()));
        } else {
            List<AmpOfflineChangelog> changeLogs = loadChangeLog(syncRequest.getLastSyncTime(), asList(CALENDAR));
            systemDiff.setCalendars(toListDiffWithLongs(changeLogs, systemDiff));
        }
    }

    private void updateDiffsForResources(SystemDiff systemDiff, SyncRequest syncRequest) {
        if (syncRequest.getLastSyncTime() == null || systemDiff.isResourceFieldsStructuralChanges()) {
            systemDiff.setResources(new ListDiff<>(emptyList(), resourceService.getAllNodeUuids()));
        } else {
            List<String> updated = new ArrayList<>();

            if (resourcesChanged(syncRequest.getLastSyncTime())) {
                List<ResourceChange> allChanges = getAllResourceChanges(syncRequest.getLastSyncTime());
                for (ResourceChange resourceChange : allChanges) {
                    systemDiff.updateTimestamp(resourceChange.getAddingDate());
                    updated.add(resourceChange.getUuid());
                }
            }

            List<String> removed = new ArrayList<>();
            List<AmpOfflineChangelog> removedLogs =
                    ampOfflineChangelogRepository.findRemovedResources(syncRequest.getLastSyncTime());
            for (AmpOfflineChangelog removedLog : removedLogs) {
                removed.add(removedLog.getEntityId());
                systemDiff.updateTimestamp(removedLog.getOperationTime());
            }

            systemDiff.setResources(new ListDiff<>(removed, updated));
        }
    }

    private boolean resourcesChanged(Date lastSyncTime) {
        return !loadChangeLog(lastSyncTime, asList(RESOURCE)).isEmpty();
    }

    private void updateDiffForUsers(SystemDiff systemDiff, Date lastSyncTime) {
        if (lastSyncTime == null) {
            systemDiff.setUsers(new ListDiff<>(emptyList(), findAllUserIds()));
        } else {
            List<AmpOfflineChangelog> changelogs = findChangedUsers(lastSyncTime);

            List<Long> saved = new ArrayList<>();
            for (AmpOfflineChangelog changelog : changelogs) {
                systemDiff.updateTimestamp(changelog.getOperationTime());
                saved.add(changelog.getEntityIdAsLong());
            }

            systemDiff.setUsers(new ListDiff<>(emptyList(), saved));
        }
    }

    private List<Long> findAllUserIds() {
        return jdbcTemplate.query("select id from dg_user", emptyMap(), ID_MAPPER);
    }

    private List<Long> findAllCalendarIds() {
        return jdbcTemplate.query("select amp_fiscal_cal_id from amp_fiscal_calendar", emptyMap(), ID_MAPPER);
    }

    private List<AmpOfflineChangelog> findChangedUsers(Date lastSyncTime) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);

        return jdbcTemplate.query(
                "select null::varchar, id::varchar, null::varchar, last_modified " +
                        "from dg_user " +
                        "where last_modified > :lastSyncTime",
                args, ROW_MAPPER);
    }

    private void updateDiffsForWsAndGs(SystemDiff systemDiff, Date lastSyncTime) {
        if (lastSyncTime != null) {
            List<AmpOfflineChangelog> changelogs = findChangedWsAndGs(lastSyncTime);

            for (AmpOfflineChangelog changelog : changelogs) {
                if (changelog.getEntityName().equals(GLOBAL_SETTINGS)) {
                    systemDiff.setGlobalSettings(true);
                }
                if (changelog.getEntityName().equals(WORKSPACES)) {
                    systemDiff.setWorkspaces(true);
                }
                if (changelog.getEntityName().equals(WORKSPACE_FILTER_DATA)) {
                    systemDiff.setWorkspaces(true);
                }
                if (changelog.getEntityName().equals(WORKSPACE_ORGANIZATIONS)) {
                    systemDiff.setWorkspaces(true);
                }
                if (changelog.getEntityName().equals(WORKSPACE_SETTINGS)) {
                    systemDiff.setWorkspaceSettings(true);
                }
                systemDiff.updateTimestamp(changelog.getOperationTime());
            }
        } else {
            systemDiff.setGlobalSettings(true);
            systemDiff.setWorkspaces(true);
            systemDiff.setWorkspaceSettings(true);
        }
    }

    private void updateDiffsForMapTilesAndLocators(SystemDiff systemDiff, Date lastSyncTime) {
        boolean isMapTilesPublished = MapTilesService.getInstance().getMapTilesNodeWrapper() != null;
        if (lastSyncTime != null) {
            List<AmpOfflineChangelog> changelogs = findChangedMapTilesAndLocators(lastSyncTime);

            for (AmpOfflineChangelog changelog : changelogs) {
                if (changelog.getEntityName().equals(MAP_TILES)) {
                    systemDiff.setMapTiles(isMapTilesPublished);
                }
                if (changelog.getEntityName().equals(LOCATORS)) {
                    systemDiff.setLocators(true);
                }
                systemDiff.updateTimestamp(changelog.getOperationTime());
            }
        } else {
            systemDiff.setMapTiles(isMapTilesPublished);
            systemDiff.setLocators(true);
        }
    }

    private List<AmpOfflineChangelog> findChangedWsAndGs(Date lastSyncTime) {
        return loadChangeLog(lastSyncTime,
                asList(GLOBAL_SETTINGS, WORKSPACES, WORKSPACE_SETTINGS, WORKSPACE_FILTER_DATA,
                        WORKSPACE_ORGANIZATIONS));
    }

    private List<AmpOfflineChangelog> findChangedMapTilesAndLocators(Date lastSyncTime) {
        return loadChangeLog(lastSyncTime, asList(MAP_TILES, LOCATORS));
    }

    private void updateDiffForWorkspaceMembers(SystemDiff systemDiff, Date lastSyncTime) {
        if (lastSyncTime == null) {
            List<Long> workspaceMemberIds = findWorkspaceMembers();

            systemDiff.setWorkspaceMembers(new ListDiff<>(emptyList(), workspaceMemberIds));
        } else {
            List<AmpOfflineChangelog> changeLogs = findChangedWorkspaceMembers(lastSyncTime);
            systemDiff.setWorkspaceMembers(toListDiffWithLongs(changeLogs, systemDiff));
        }
    }

    private ListDiff<Long> toListDiffWithLongs(List<AmpOfflineChangelog> changeLogs, SystemDiff systemDiff) {
        List<Long> removed = new ArrayList<>();
        List<Long> saved = new ArrayList<>();

        for (AmpOfflineChangelog changelog : changeLogs) {
            if (changelog.getOperationName().equals(DELETED)) {
                removed.add(changelog.getEntityIdAsLong());
            }
            if (changelog.getOperationName().equals(UPDATED)) {
                saved.add(changelog.getEntityIdAsLong());
            }
            systemDiff.updateTimestamp(changelog.getOperationTime());
        }

        return new ListDiff<>(removed, saved);
    }

    private List<Long> findWorkspaceMembers() {
        return jdbcTemplate.query("select amp_team_mem_id from amp_team_member", emptyMap(), ID_MAPPER);
    }

    private List<AmpOfflineChangelog> findChangedWorkspaceMembers(Date lastSyncTime) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);
        args.put("entity", WORKSPACE_MEMBER);
        args.put("deleted", DELETED);

        return jdbcTemplate.query("select " + COLUMNS + " "
                + "from amp_offline_changelog "
                + "where operation_time > :lastSyncTime "
                + "and entity_name = :entity "
                + "and (entity_id in (select amp_team_mem_id::varchar from amp_team_member) "
                + "or operation_name = :deleted)", args, ROW_MAPPER);
    }

    /**
     * Returns translations that should be synchronized with AMP Offline. Parameter lastSyncTime can be null in which
     * case all translations will be returned.
     *
     * @param lastSyncTime time of last sync operation
     * @return list of translations
     */
    public List<Translation> getTranslationsToSync(Date lastSyncTime) {
        Set<String> localeCodes = TranslationSettings.getCurrent().getTrnLocaleCodes();
        Site site = SiteUtils.getDefaultSite();

        return findChangedAmpOfflineTranslations(lastSyncTime, localeCodes, site);
    }

    /**
     * Returns all translations for AMP Offline. If lastSyncTime is specified then return only translations changed
     * since that time.
     *
     * @param lastSyncTime last sync time
     * @param localeCodes  locales to look up
     * @param site         site
     * @return list of changed translations
     */
    private List<Translation> findChangedAmpOfflineTranslations(Date lastSyncTime, Set<String> localeCodes, Site site) {
        Map<String, Object> args = new HashMap<>();
        args.put("siteId", site.getId().toString());
        args.put("localeCodes", localeCodes);

        String messageKeyFilter = "";
        if (lastSyncTime != null) {
            messageKeyFilter = "and m_orig.message_key in ("
                    + "select entity_id "
                    + "from amp_offline_changelog "
                    + "where operation_time > :lastSyncTime "
                    + "and entity_name = :entity)";

            args.put("lastSyncTime", lastSyncTime);
            args.put("entity", TRANSLATION);
        }

        return jdbcTemplate.query(
                "select m.message_key \"key\", coalesce(m_orig.orig_message, m_orig.message_utf8) \"label\", "
                        + "  m.lang_iso locale, m.message_utf8 translatedLabel "
                        + "from dg_message m "
                        + "  left join dg_message m_orig on m.message_key = m_orig.message_key "
                        + "where (m.amp_offline = true or m_orig.amp_offline = true) "
                        + "and m.site_id = :siteId "
                        + "and m.lang_iso in (:localeCodes) "
                        + "and m_orig.site_id = :siteId "
                        + "and m_orig.lang_iso = 'en' "
                        + messageKeyFilter,
                args,
                TRANSLATION_ROW_MAPPER);
    }

    public ExchangeRatesDiff getChangedExchangeRates(Date lastSyncTime) {
        Objects.requireNonNull(lastSyncTime);

        List<Date> dates = findDaysWithModifiedRates(lastSyncTime);

        List<ExchangeRatesForPair> exchangeRatesForPairs;
        if (dates.isEmpty()) {
            exchangeRatesForPairs = emptyList();
        } else {
            exchangeRatesForPairs = currencyService.getExchangeRatesForPairs(dates);
        }

        return new ExchangeRatesDiff(dates, exchangeRatesForPairs);
    }

    private List<Date> findDaysWithModifiedRates(Date lastSyncTime) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);
        args.put("entity", EXCHANGE_RATES);

        return jdbcTemplate.query(
                "select entity_id from amp_offline_changelog "
                        + "where entity_name=:entity and operation_time > :lastSyncTime",
                args,
                JulianDayRowMapper.INSTANCE);
    }

    private List<AmpOfflineChangelog> loadChangeLog(Date lastSyncTime, Collection<String> entities) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);
        args.put("entities", entities);

        return jdbcTemplate.query("select " + COLUMNS + " "
                + "from amp_offline_changelog "
                + "where operation_time > :lastSyncTime "
                + "and entity_name in (:entities) ", args, ROW_MAPPER);
    }

    private List<ResourceChange> getAllResourceChanges(Date lastSyncTime) {
        List<ResourceChange> changedResources = new ArrayList<>();
        changedResources.addAll(getLastUpdatedUuids("private", lastSyncTime));
        changedResources.addAll(getLastUpdatedUuids("team", lastSyncTime));

        return changedResources;
    }

    private static List<ResourceChange> getLastUpdatedUuids(String path, Date syncDate) {
        Session session = DocumentManagerUtil.getReadSession(TLSUtils.getRequest());
        Calendar cal = Calendar.getInstance();
        cal.setTime(syncDate);
        String formattedDate = ISO8601.format(cal);

        List<ResourceChange> resources = new ArrayList<>();
        try {
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery(String.format("SELECT * FROM nt:base WHERE %s "
                            + "IS NOT NULL AND jcr:path LIKE '/%s/%%/' AND %s >= TIMESTAMP '%s'",
                    CrConstants.PROPERTY_CREATOR, path, CrConstants.PROPERTY_ADDING_DATE, formattedDate), Query.SQL);
            NodeIterator nodes = query.execute().getNodes();
            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                Property dateProp = DocumentManagerUtil.getPropertyFromNode(node, CrConstants.PROPERTY_ADDING_DATE);
                ResourceChange resourceChange = new ResourceChange();
                resourceChange.setUuid(node.getIdentifier());
                resourceChange.setAddingDate(dateProp.getDate().getTime());
                resources.add(resourceChange);
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }

        return resources;
    }

    private boolean existsActivityStructuralChanges(SyncRequest syncRequest) {
        ApiFieldStructuralService structuralService = ApiFieldStructuralService.getInstance();
        return structuralService.existsStructuralChangesBasedOnWs(syncRequest.getActivityFields(),
                COLLECTION_TYPE_ACTIVITY);
    }

    private boolean existsContactStructuralChanges(SyncRequest syncRequest) {
        ApiFieldStructuralService structuralService = ApiFieldStructuralService.getInstance();
        return structuralService.existsStructuralChangesBasedOnWs(syncRequest.getContactFields(),
                COLLECTION_TYPE_CONTACT);
    }

    private boolean existsResourceStructuralChanges(SyncRequest syncRequest) {
        ApiFieldStructuralService structuralService = ApiFieldStructuralService.getInstance();
        return structuralService.existsStructuralChangesBasedOnWs(syncRequest.getResourceFields(),
                COLLECTION_TYPE_RESOURCE);
    }

}
