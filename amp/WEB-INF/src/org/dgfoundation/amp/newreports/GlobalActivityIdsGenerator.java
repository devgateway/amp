package org.dgfoundation.amp.newreports;

import java.util.Set;

import org.dgfoundation.amp.diffcaching.DatabaseChangedDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * Returns all activity ids for filtering in computed workspaces. Activities must match the following criteria:
 * <ul>
 * <li>must be assigned to a workspace</li>
 * </ul>
 *
 * @author Octavian Ciubotaru
 */
public class GlobalActivityIdsGenerator implements IdsGeneratorSource {

    private static final int TIMEOUT = 10 * 60 * 1000;

    private final ExpiringCacher<Boolean, Boolean, Set<Long>> cacher = new ExpiringCacher<>(
            "globalActivityIds",
            (key, context) -> computeIds(),
            new DatabaseChangedDetector(),
            TIMEOUT);

    @Override
    public Set<Long> getIds() {
        return cacher.buildOrGetValue(false, false);
    }

    private Set<Long> computeIds() {
        return ActivityUtil.fetchLongs(
                "SELECT amp_activity_id "
                + "FROM amp_activity "
                + "WHERE amp_team_id IS NOT NULL");
    }
}
