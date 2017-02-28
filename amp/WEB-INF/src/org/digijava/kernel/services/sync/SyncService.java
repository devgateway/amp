package org.digijava.kernel.services.sync;

import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.*;
import static org.digijava.kernel.services.sync.model.SyncConstants.Ops.DELETED;
import static org.digijava.kernel.services.sync.model.SyncConstants.Ops.UPDATED;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.services.sync.model.AmpOfflineChangelog;
import org.digijava.kernel.services.sync.model.ListDiff;
import org.digijava.kernel.services.sync.model.SystemDiff;
import org.digijava.kernel.services.sync.model.Translation;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.helper.Constants;
import org.springframework.beans.factory.InitializingBean;
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

    public SystemDiff diff(List<Long> userIds, Date lastSyncTime) {
        SystemDiff systemDiff = new SystemDiff();

        updateDiffsForWsAndGs(systemDiff, lastSyncTime);
        updateDiffForWorkspaceMembers(systemDiff, userIds, lastSyncTime);
        updateDiffForUsers(systemDiff, userIds, lastSyncTime);

        systemDiff.setActivities(new ListDiff<>(Collections.emptyList(), Collections.emptyList()));
        systemDiff.setTranslations(shouldSyncTranslations(lastSyncTime));

        return systemDiff;
    }

    private boolean shouldSyncTranslations(Date lastSyncTime) {
        if (lastSyncTime == null) {
            return true;
        }

        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);

        Long count = jdbcTemplate.queryForLong(
                "select count(m.message_key) from dg_message m, amp_offline_changelog cl " +
                "where m.message_key = cl.entity_id " +
                "and m.amp_offline = true " +
                "and operation_time > :lastSyncTime", args);

        return count > 0;
    }

    private void updateDiffForUsers(SystemDiff systemDiff, List<Long> userIds, Date lastSyncTime) {
        if (lastSyncTime == null) {
            systemDiff.setUsers(new ListDiff<>(Collections.emptyList(), userIds));
        } else {
            List<AmpOfflineChangelog> changelogs = findChangedUsers(userIds, lastSyncTime);

            List<Long> saved = new ArrayList<>();
            for (AmpOfflineChangelog changelog : changelogs) {
                systemDiff.updateTimestamp(changelog.getOperationTime());
                saved.add(changelog.getEntityIdAsLong());
            }

            systemDiff.setUsers(new ListDiff<>(Collections.emptyList(), saved));
        }
    }

    private List<AmpOfflineChangelog> findChangedUsers(List<Long> userIds, Date lastSyncTime) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);
        args.put("userIds", userIds);

        return jdbcTemplate.query(
                "select null::varchar, id::varchar, null::varchar, last_modified " +
                        "from dg_user " +
                        "where last_modified > :lastSyncTime " +
                        "and id in (:userIds)", args, ROW_MAPPER);
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
                systemDiff.updateTimestamp(changelog.getOperationTime());
            }
        } else {
            systemDiff.setGlobalSettings(true);
            systemDiff.setWorkspaces(true);
        }
    }

    private List<AmpOfflineChangelog> findChangedWsAndGs(Date lastSyncTime) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);
        args.put("entities", Arrays.asList(GLOBAL_SETTINGS, WORKSPACES));

        return jdbcTemplate.query(
                "select " + COLUMNS + " " +
                "from amp_offline_changelog " +
                "where operation_time > :lastSyncTime " +
                "and entity_name in (:entities)", args, ROW_MAPPER);
    }

    private void updateDiffForWorkspaceMembers(SystemDiff systemDiff, List<Long> userIds, Date lastSyncTime) {
        if (lastSyncTime == null) {
            List<Long> workspaceMemberIds = findWorkspaceMembers(userIds);

            systemDiff.setWorkspaceMembers(new ListDiff<>(Collections.emptyList(), workspaceMemberIds));
        } else {
            List<AmpOfflineChangelog> changelogs = findChangedWorkspaceMembers(userIds, lastSyncTime);

            List<Long> removed = new ArrayList<>();
            List<Long> saved = new ArrayList<>();

            for (AmpOfflineChangelog changelog : changelogs) {
                if (changelog.getOperationName().equals(DELETED)) {
                    removed.add(changelog.getEntityIdAsLong());
                }
                if (changelog.getOperationName().equals(UPDATED)) {
                    saved.add(changelog.getEntityIdAsLong());
                }
                systemDiff.updateTimestamp(changelog.getOperationTime());
            }

            systemDiff.setWorkspaceMembers(new ListDiff<>(removed, saved));
        }
    }

    private List<Long> findWorkspaceMembers(List<Long> userIds) {
        Map<String, Object> args = new HashMap<>();
        args.put("userIds", userIds);

        return jdbcTemplate.query(
                "select amp_team_mem_id from amp_team_member where user_ in (:userIds)",
                args, ID_MAPPER);
    }

    private List<AmpOfflineChangelog> findChangedWorkspaceMembers(List<Long> userIds, Date lastSyncTime) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);
        args.put("entity", WORKSPACE_MEMBER);
        args.put("userIds", userIds);
        args.put("deleted", DELETED);

        return jdbcTemplate.query("select " + COLUMNS + " " +
                "from amp_offline_changelog " +
                "where operation_time > :lastSyncTime " +
                "and entity_name = :entity " +
                "and (entity_id in (select amp_team_mem_id::varchar from amp_team_member where user_ in (:userIds)) " +
                "or operation_name = :deleted)", args, ROW_MAPPER);
    }

    /**
     * Returns translations that should be synchronized with AMP Offline. Parameter lastSyncTime can be null in which
     * case all translations will be returned.
     *
     * @param lastSyncTime time of last sync operation
     * @return list of translations
     */
    public List<Translation> getTranslationsToSync(Date lastSyncTime) {
        Set<String> localeCodes = InterchangeUtils.getTranslationSettings().getTrnLocaleCodes();
        Site site = SiteUtils.getDefaultSite();

        return findChangedAmpOfflineTranslations(lastSyncTime, localeCodes, site);
    }

    /**
     * Returns all translations for AMP Offline. If lastSyncTime is specified then return only translations changed
     * since that time.
     *
     * @param lastSyncTime last sync time
     * @param localeCodes locales to look up
     * @param site site
     * @return list of changed translations
     */
    private List<Translation> findChangedAmpOfflineTranslations(Date lastSyncTime, Set<String> localeCodes, Site site) {
        Map<String, Object> args = new HashMap<>();
        args.put("siteId", site.getId().toString());
        args.put("localeCodes", localeCodes);

        String messageKeyFilter = "";
        if (lastSyncTime != null) {
            messageKeyFilter = "and m_orig.message_key in (" +
                    "select entity_id " +
                    "from amp_offline_changelog " +
                    "where operation_time > :lastSyncTime " +
                    "and entity_name = :entity)";

            args.put("lastSyncTime", lastSyncTime);
            args.put("entity", TRANSLATION);
        }

        return jdbcTemplate.query(
                "select m.message_key \"key\", coalesce(m_orig.orig_message, m_orig.message_utf8) \"label\", " +
                "  m.lang_iso locale, m.message_utf8 translatedLabel " +
                "from dg_message m " +
                "  left join dg_message m_orig on m.message_key = m_orig.message_key " +
                "where m.amp_offline = true " +
                "and m.site_id = :siteId " +
                "and m.lang_iso in (:localeCodes) " +
                "and m_orig.site_id = :siteId " +
                "and m_orig.lang_iso = 'en' "+
                messageKeyFilter,
                args,
                TRANSLATION_ROW_MAPPER);
    }
}
