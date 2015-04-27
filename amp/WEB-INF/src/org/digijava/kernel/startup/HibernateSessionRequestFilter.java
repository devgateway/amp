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
			log.error("error occured during request processing", ex); 
			PersistenceManager.rollbackCurrentSessionTx(); // rollback
			throw ex;
		}
		finally {
			// Commit and cleanup
			PersistenceManager.endSessionLifecycle();
			if (request instanceof HttpServletRequest)
				DocumentManagerUtil.closeJCRSessions((HttpServletRequest)request);      	
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// nothing
	}
}
