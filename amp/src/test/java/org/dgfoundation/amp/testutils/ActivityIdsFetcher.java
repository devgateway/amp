package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.newreports.IdsGeneratorSource;
import org.digijava.module.esrigis.helpers.DbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;


/**
 * a <strong>testcases-only</strong> workspace filter for the AMP schema. Do not ever use it in prod code, as it caches ids based on the assumption that workspaces never change! 
 * @author Constantin Dolghier
 *
 */
public class ActivityIdsFetcher implements IdsGeneratorSource {
    
    public final List<String> activityNames;
    
    /**
     * 
     * @param activityNames if null, then "get all" 
     */
    public ActivityIdsFetcher(List<String> activityNames) {
        this.activityNames = activityNames;
    }
    
    @Override public Set<Long> getIds() {
        List<String> sortedNames = new ArrayList<>(activityNames);
        sortedNames.sort(null);
        return ids.computeIfAbsent(sortedNames.toString(), z -> _getIds());
    }
    
    protected Set<Long> _getIds() {
        try {
            String whereQuery = activityNames == null ? "" : (
                activityNames.isEmpty() ? "WHERE (1 = 2)" : ("WHERE name IN (" + Util.toCSStringForIN(activityNames) + ")"));
            
            String query = "SELECT amp_activity_id from amp_activity " + whereQuery;
            return new TreeSet<Long>(DbHelper.getInActivities(query));
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected final static ConcurrentHashMap<String, Set<Long>> ids = new ConcurrentHashMap<>();
}
