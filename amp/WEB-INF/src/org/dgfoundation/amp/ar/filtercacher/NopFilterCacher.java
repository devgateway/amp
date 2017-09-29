package org.dgfoundation.amp.ar.filtercacher;

import java.sql.SQLException;

import org.dgfoundation.amp.ar.AmpARFilter;

/**
 * FilterCacher which does nothing
 * @author Dolghier Constantin
 *
 */
public class NopFilterCacher extends FilterCacher {
    public NopFilterCacher(AmpARFilter filter)
    {
        super(filter);
    }
    
    @Override
    protected String customRewriteFilterQuery(String inQuery)
    {
        return inQuery;
    }
}
