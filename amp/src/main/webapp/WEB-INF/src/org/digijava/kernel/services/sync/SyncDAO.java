package org.digijava.kernel.services.sync;

import static java.util.Collections.singletonMap;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.ACTIVITY_PROGRAM_SETTINGS;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.FEATURE_MANAGER;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.GLOBAL_SETTINGS;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.TRANSLATION;
import static org.digijava.kernel.services.sync.model.SyncConstants.Entities.WORKSPACES;

import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.common.collect.ImmutableList;
import org.digijava.module.aim.helper.Constants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Octavian Ciubotaru
 */
@Component
public class SyncDAO implements InitializingBean {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        Context initialContext = new InitialContext();
        DataSource dataSource = (DataSource) initialContext.lookup(Constants.UNIFIED_JNDI_ALIAS);
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Timestamp getLastModificationDateForFieldDefinitions() {
        return jdbcTemplate.queryForObject(
                "SELECT max(operation_time) "
                        + "FROM amp_offline_changelog "
                        + "WHERE entity_name in (:entities)",
                singletonMap("entities", ImmutableList.of(FEATURE_MANAGER, TRANSLATION, ACTIVITY_PROGRAM_SETTINGS,
                        GLOBAL_SETTINGS, WORKSPACES)),
                Timestamp.class);
    }
}
