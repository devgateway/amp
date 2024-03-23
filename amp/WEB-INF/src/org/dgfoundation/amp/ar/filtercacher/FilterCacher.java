package org.dgfoundation.amp.ar.filtercacher;

import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.persistence.PersistenceManager;

import java.sql.Connection;

/**
 * class which caches AmpARFilter generatedFilterQuery results
 * all the queries 
 * the class is NOT thread safe! only designed for serial usage!
 * 
 */
public abstract class FilterCacher {
    
    private AmpARFilter filter;
    private Connection connection;
    protected final String primaryKeyName;
    
    public FilterCacher(AmpARFilter filter)
    {
        this.filter = filter;
        this.primaryKeyName = filter.isPledgeFilter() ? "pledge_id" : "amp_activity_id";
        try
        {
            this.connection = PersistenceManager.getJdbcConnection();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * get a connection on which to run all the ColumnWorker sql workers
     */
    public Connection getConnection()
    {
        return connection;
    }
    
    protected abstract String customRewriteFilterQuery(String inQuery);
    
    public String rewriteFilterQuery(String inQuery)
    {
        if (!inQuery.equals(filter.getGeneratedFilterQuery()))
            throw new RuntimeException("FilterCacher: one instance can only be used on a single filter");
        return customRewriteFilterQuery(inQuery);
    }
    
    public void closeConnection()
    {
        PersistenceManager.closeQuietly(this.connection);
    }
    
    @Override
    protected void finalize()
    {
        closeConnection();
    }   
    
    public String getPrimaryKeyName(){
        return primaryKeyName;
    }
     
}
