package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.StringGenerator;
import org.dgfoundation.amp.ar.viewfetcher.*;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;

import java.util.*;

public class NameFilteringTeamFilter implements StringGenerator {

    public static java.sql.Connection connection = getConn();
            
    public Set<String> activityNames;   
    
    public NameFilteringTeamFilter(String[] activityNames) {
        this.activityNames = new HashSet<String>(Arrays.asList(activityNames));
    }

    public NameFilteringTeamFilter(List<String> activityNames) {
        this.activityNames = new HashSet<String>(activityNames);
    }

    @Override
    public String getString() {
        String locale = TLSUtils.getEffectiveLangCode();
        //String condition = " name IN (" + Util.toCSString(Arrays.asList(activityNames)) + ")";
        Map<PropertyDescription, ColumnValuesCacher> cachers = new HashMap<PropertyDescription, ColumnValuesCacher>();      
        Set<Long> ids = new HashSet<Long>();
        
        try
        {
            ViewFetcher fetcher = DatabaseViewFetcher.getFetcherForView("amp_activity", " WHERE 1=1 ", locale, cachers, connection, "amp_activity_id", "name");
            try(RsInfo rsi = fetcher.fetch(null)) {
                while (rsi.rs.next())
                {
                    long id = rsi.rs.getInt(1);
                    String actName = rsi.rs.getString(2);
                    if (activityNames.contains(actName))
                        ids.add(id);
                }
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        if (ids.isEmpty())
            ids.add(-999L);
        //return "SELECT amp_activity_id from amp_activity WHERE amp_activity_id IN (" + Util.toCSString(ids) + ")";
        return Util.toCSString(ids);
    }
    
    private static java.sql.Connection getConn()
    {
        try
        {
            return PersistenceManager.getJdbcConnection();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
