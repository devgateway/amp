package org.dgfoundation.amp.algo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * a waver which selects ids using a query like SELECT DISTINCT(BLA) FROM FOO WHERE X IN (in)
 * @author Dolghier Constantin
 *
 */
public class DatabaseWaver implements Waver<Long>{
    
    String query;
    
    /**
     * put $ in lieu of "in" in the SQL query
     * @param query
     */
    public DatabaseWaver(String query)
    {
        this.query = query;
    }

    @Override
    public Set<Long> wave(Set<Long> in)
    {
        final Set<Long> result = new HashSet<Long>();
        if (in == null)
            return result;
        String query = this.query.replace("$", Util.toCSStringForIN(in));
        List<?> ids = PersistenceManager.getSession().createNativeQuery(query).list();
        for(Object longAsObj:ids)
            result.add(PersistenceManager.getLong(longAsObj));
        return result;

    }
}
