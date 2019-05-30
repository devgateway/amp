package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.map.LRUMap;
import org.dgfoundation.amp.reports.ReportCacher;

/**
 * Project List pagination cacher that reuses the very same approach as in {@link ReportCacher}
 *
 * @author Emanuel Perez
 */
public class ProjectListCacher {
    public final static int MAX_CACHED_PROJECT_LISTS = 10;

    private Map<String, Collection<Map<String, Object>>> lru =
            Collections.synchronizedMap(new LRUMap(MAX_CACHED_PROJECT_LISTS));

    public ProjectListCacher() {
    }

    public void addCachedProjectList(String pid, Collection<Map<String, Object>> projectList) {
        if (projectList != null) {
            lru.put(pid, projectList);
        }
    }

    public Collection<Map<String, Object>> getCachedProjectList(String pid) {
        return lru.get(pid);
    }

    public Collection<Map<String, Object>> deleteCachedProjectList(String pid) {
        return lru.remove(pid);
    }

}
