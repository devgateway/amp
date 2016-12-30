package org.digijava.kernel.services.sync;

import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.GLOBAL_SETTINGS;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.WORKSPACES;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.WORKSPACE_MEMBER;
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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.digijava.kernel.services.sync.model.AmpOfflineChangelog;
import org.digijava.kernel.services.sync.model.ListDiff;
import org.digijava.kernel.services.sync.model.SystemDiff;
import org.digijava.kernel.services.sync.model.IncrementalListDiff;
import org.digijava.module.aim.helper.Constants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Octavian Ciubotaru
 */
@Component
public class SyncService implements InitializingBean {

    private SimpleJdbcTemplate jdbcTemplate;

    private static final String COLUMNS = "entity_name, entity_id, operation_name, operation_time";

    private static final RowMapper<AmpOfflineChangelog> ROW_MAPPER = new AmpOfflineChangelogMapper();

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
        jdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }

    public SystemDiff diff(List<Long> userIds, Date lastSyncTime) {
        SystemDiff systemDiff = new SystemDiff();

        updateDiffsForWsAndGs(systemDiff, lastSyncTime);
        updateDiffForWorkspaceMembers(systemDiff, userIds, lastSyncTime);
        updateDiffForUsers(systemDiff, userIds, lastSyncTime);

        systemDiff.setActivities(new IncrementalListDiff<>(Collections.emptyList(), Collections.emptyList()));
        systemDiff.setTranslations(new ListDiff<>());

        return systemDiff;
    }

    private void updateDiffForUsers(SystemDiff systemDiff, List<Long> userIds, Date lastSyncTime) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);
        args.put("userIds", userIds);

        List<AmpOfflineChangelog> changelogs = jdbcTemplate.query(
                "select null::varchar, id::varchar, null::varchar, last_modified " +
                "from dg_user " +
                "where last_modified > :lastSyncTime " +
                "and id in (:userIds)", ROW_MAPPER, args);

        List<Long> saved = new ArrayList<>();
        for (AmpOfflineChangelog changelog : changelogs) {
            systemDiff.updateTimestamp(changelog.getOperationTime());
            saved.add(changelog.getEntityIdAsLong());
        }

        systemDiff.setUsers(new IncrementalListDiff<>(Collections.emptyList(), saved));
    }

    private void updateDiffsForWsAndGs(SystemDiff systemDiff, Date lastSyncTime) {
        if (lastSyncTime != null) {

            Map<String, Object> args = new HashMap<>();
            args.put("lastSyncTime", lastSyncTime);
            args.put("entities", Arrays.asList(GLOBAL_SETTINGS, WORKSPACES));

            List<AmpOfflineChangelog> changelogs = jdbcTemplate.query(
                    "select " + COLUMNS + " " +
                    "from amp_offline_changelog " +
                    "where operation_time > :lastSyncTime " +
                    "and entity_name in (:entities)", ROW_MAPPER, args);

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

    private void updateDiffForWorkspaceMembers(SystemDiff systemDiff, List<Long> userIds, Date lastSyncTime) {
        Map<String, Object> args = new HashMap<>();
        args.put("lastSyncTime", lastSyncTime);
        args.put("entity", WORKSPACE_MEMBER);
        args.put("userIds", userIds);
        args.put("deleted", DELETED);

        List<AmpOfflineChangelog> changelogs = jdbcTemplate.query("select " + COLUMNS + " " +
                "from amp_offline_changelog " +
                "where operation_time > :lastSyncTime " +
                "and entity_name = :entity " +
                "and (entity_id in (select amp_team_mem_id::varchar from amp_team_member where user_ in (:userIds)) " +
                "or operation_name = :deleted)", ROW_MAPPER, args);

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

        systemDiff.setWorkspaceMembers(new IncrementalListDiff<>(removed, saved));
    }
}
