/**
 * 
 */
package org.digijava.kernel.job.cachedtables;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * @author mihai
 *
 */
public class RefreshMondrianCacheJob extends ConnectionCleaningJob implements StatefulJob {
        
    private static Logger logger = Logger.getLogger(RefreshMondrianCacheJob.class);
    
    @Override 
    public void executeInternal(JobExecutionContext context) throws JobExecutionException
    {   
        try(Connection connection = PersistenceManager.getJdbcConnection()) {
            connection.setAutoCommit(false);
            connection.setAutoCommit(true);
            PublicViewColumnsUtil.maintainPublicViewCaches(connection, true); // let Java do all the repetitive work
            connection.setAutoCommit(false); // this will commit any unfinished transaction started by PublicViewColumnsUtil
            connection.commit();
        } catch (SQLException e) {
            logger.error(e, e);
        }
        logger.info("Refresh Mondrian Cache Job Successful!");
    }
}
