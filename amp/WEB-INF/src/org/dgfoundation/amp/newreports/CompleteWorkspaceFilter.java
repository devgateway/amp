package org.dgfoundation.amp.newreports;

import java.util.Set;

import org.dgfoundation.amp.ar.WorkspaceFilter;
import org.dgfoundation.amp.diffcaching.DatabaseChangedDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;

/**
 * a cached, etl_log-invalidated, WorkspaceFilter which takes into account both the team and the team filters (if any)
 * @author Dolghier Constantin
 */
public class CompleteWorkspaceFilter implements IdsGeneratorSource {
    
    private final TeamMember tm;
    private final ExpiringCacher<Boolean, Boolean, Set<Long>> cacher = new ExpiringCacher<>(
            "workspaceFilter",
            (b, engine) -> computeIds(),
            new DatabaseChangedDetector(),
            3 * 60 * 1000);
    
    public CompleteWorkspaceFilter(TeamMember tm) {
        this.tm = tm;
    }
    
    @Override
    public Set<Long> getIds() {
        return cacher.buildOrGetValue(true, true);
    }
    
    /** called when the cacher determines that the cache has been invalidated */
    private Set<Long> computeIds() {
        return ActivityUtil.fetchLongs(WorkspaceFilter.generateWorkspaceFilterQuery(tm));
    }
}
