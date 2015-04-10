/**
 * 
 */
package org.digijava.kernel.startup;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.hibernate.StaleObjectStateException;

/**
 * @author mihai
 * {@link http://community.jboss.org/wiki/OpenSessionInView}
 */
public class HibernateSessionRequestFilter implements Filter {
	private static Logger log = Logger.getLogger(HibernateSessionRequestFilter.class);
	/**
	 * 
	 */
	public HibernateSessionRequestFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void destroy() {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		// Call the next filter (continue request processing)
		try {
			chain.doFilter(request, response);
		}
		catch(Throwable ex) {
			ex.printStackTrace(); // rollback
			PersistenceManager.rollbackCurrentSessionTx();
			throw ex;
		}
		finally {
			// Commit and cleanup
			PersistenceManager.endSessionLifecycle();
			//PersistenceManager.checkClosedOrLongSessionsFromTraceMap();
			if (request instanceof HttpServletRequest)
				DocumentManagerUtil.closeJCRSessions((HttpServletRequest)request);      	
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// nothing
	}
}
