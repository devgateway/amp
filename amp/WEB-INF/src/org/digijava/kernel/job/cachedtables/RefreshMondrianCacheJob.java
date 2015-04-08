/**
 * 
 */
package org.digijava.kernel.job.cachedtables;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.message.jobs.ConnectionCleaningJob;
import org.hibernate.Session;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
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
		ServletContext ctx = null;
		Connection connection = null;
		try 
		{
			connection = PersistenceManager.getJdbcConnection();		
			connection.setAutoCommit(false);
			
			// make sure that, in case the following SQL stuff fails, at least the Java side executed correctly and committed its stuff
			connection.setAutoCommit(true);
			PublicViewColumnsUtil.maintainPublicViewCaches(connection, true); // let Java do all the repetitive work
			connection.setAutoCommit(false); // this will commit any unfinished transaction started by PublicViewColumnsUtil
			
//			// handle special stuff in SQL - right now, create cached_v_m_donor_funding
//			ctx = (ServletContext) arg0.getScheduler().getContext().get(Constants.AMP_SERVLET_CONTEXT);
			
//			String patchFile = ctx.getRealPath("/WEB-INF/src/org/digijava/kernel/job/chachedtables/refresh_mondrian_cache.sql");

			StringBuffer sb = new StringBuffer();
			try(LineNumberReader bis = new LineNumberReader(new InputStreamReader(this.getClass().getResourceAsStream("refresh_mondrian_cache.sql")))) {
				String s = bis.readLine();
				while (s != null) {
					sb.append(s);
					s = bis.readLine();
				}		
			}
			StringTokenizer stok = new StringTokenizer(sb.toString(),";");
			
			Statement st = connection.createStatement();
			
			while (stok.hasMoreTokens()) 
			{
				String sqlCommand = stok.nextToken();
				if (sqlCommand.trim().equals(""))
					continue;
				st.addBatch(sqlCommand);
			}
			st.executeBatch();
			connection.commit();		
		}catch (FileNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
			return;
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
			return;
		
		} finally {
			try {
				if (connection != null)
					connection.setAutoCommit(true);
				PersistenceManager.closeQuietly(connection);
			}
			catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
				return;			
			}
		}
		logger.info("Refresh Mondrian Cache Job Successful!");
	}
}
