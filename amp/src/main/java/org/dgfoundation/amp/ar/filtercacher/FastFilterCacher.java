package org.dgfoundation.amp.ar.filtercacher;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * Chuck Norris doesn't need technical documentation. 
 * He just stares down the code until it tells him everything he wants to know.
 * 
 * Please write javadocs!!! Chuck Norris has retired...
 * 
 * Chuck Norris's mentor aka Dolghier Constantin has stared at this code long enough that the comments have written themselves.
 * 
 * An implementation of {@link #FilterCacher} which caches the result of a Filter subquery in a temporary table in PostgreSQL<br />
 * This is normally the only used FilterCacher (the alternative is NopFilterCacher which does nothing - the filter subquery is reexecuted for every single column in the report)
 *  
 *  Modus operandi of the class:
 *  1) the FastFilterCacher constructor is supplied an "INPUT query", which is the (expensive) query to be cached. 
 *  <b>The constructor will open or fixate on a JDBC connection. ALL THE QUERIES WHICH USE THE FILTER CACHER WILL NEED TO USE THIS CONNECTION ONLY</b>  (this is what {@link #getConnection()} is for)
 *  2 FastFilterCacher will construct a query of the form CREATE TEMPORARY TABLE [random_name] AS [INPUT_query]. A PSQL temporary table is only visible from the creating connection, this the why all the customers need to use {@link #getConnection()} 
 *  3) the client code will string-replace all occurrences of INPUT query by the return of {@link #customRewriteFilterQuery(String)} (which will be of the form SELECT DISTINCT(amp_activity_id) FROM [temp_table])
 *  4) PROFIT! the replaced query is much-much faster because it is a fetch-and-lookup-by-index from a table instead of an SQL query
 *
 */
public class FastFilterCacher extends FilterCacher {

    private static Logger logger    = Logger.getLogger(FilterCacher.class);
    private String tempTableName;
    
    public FastFilterCacher(AmpARFilter filter)
    {
        super(filter);
    }
    
    @Override
    protected String customRewriteFilterQuery(String inQuery) {
        ensureTemporaryTableExists(inQuery);
        String ret =  String.format("SELECT distinct(%s) AS %s FROM %s", this.primaryKeyName, this.primaryKeyName, this.tempTableName);
        logger.info("Query is: " + ret);
        return ret;
        
    }

    private void ensureTemporaryTableExists(String inQuery)
    {
        if (tempTableName != null)
            return; //already created
        
        tempTableName = String.format("temp_table_%d", Math.abs(System.currentTimeMillis())); // Math.abs: better be paranoid than sorry
        
        try
        {
            Statement statement = getConnection().createStatement();
            String stat = String.format("CREATE TEMPORARY TABLE %s AS %s", tempTableName, inQuery);
            statement.executeUpdate(stat);
            
            stat = String.format("ALTER TABLE %s ADD PRIMARY KEY(%s)", tempTableName, this.primaryKeyName);
            statement.executeUpdate(stat);
            
            stat = String.format("ANALYZE %s", tempTableName);
            statement.executeUpdate(stat);
            
            statement.close();
        }
        catch(SQLException ex)
        {
            throw new RuntimeException(ex);
        }
        
    }
    
    private void deleteTemporaryTable()
    {
        if (tempTableName == null)
            return; //nothing to delete
        try
        {
            Connection conn = getConnection();
            if (conn == null)
                return; //shouldn't happen, but let's guard against it
            
            Statement statement = conn.createStatement();
            String stat = "DROP TABLE IF EXISTS " + tempTableName;
            statement.executeQuery(stat);
            statement.close();
        }
        catch(SQLException e)
        {
            // we don't care, because we are in the teardown sequence anyway
        }
    }
    
    @Override
    public void closeConnection()
    {
        deleteTemporaryTable();
        super.closeConnection(); //close the JDBC connection
    }
}
