package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.dgfoundation.amp.reports.ReportCacher;
import org.digijava.kernel.ampapi.endpoints.activity.dto.ActivitySummary;

/**
 * Project List pagination cacher that reuses the very same approach as in {@link ReportCacher}
 *
 * @author Emanuel Perez
 */
public class ProjectListCacher {
    public static final int MAX_CACHED_PROJECT_LISTS = 10;

    private Map<String, Collection<ActivitySummary>> lru =
            Collections.synchronizedMap(new LRUMap(MAX_CACHED_PROJECT_LISTS));

    public ProjectListCacher() {
    }

    public void addCachedProjectList(String pid, Collection<ActivitySummary> projectList) {
        if (projectList != null) {
            lru.put(pid, projectList);
        }
    }

    public Collection<ActivitySummary> getCachedProjectList(String pid) {
        return lru.get(pid);
    }

    public Collection<ActivitySummary> deleteCachedProjectList(String pid) {
        return lru.remove(pid);
    }

}
