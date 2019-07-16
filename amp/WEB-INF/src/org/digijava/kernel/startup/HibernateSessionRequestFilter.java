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

/**
 * @author mihai
 * {@link http://community.jboss.org/wiki/OpenSessionInView}
 */
public class HibernateSessionRequestFilter implements Filter {

    private static Logger logger = Logger.getLogger(HibernateSessionRequestFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            executeInJcrWrapper(request, response, chain);
        } catch (Exception e) {
            if (response.isCommitted()) {
                logger.error("Error occurred while processing the request but response was already committed. "
                        + "Could not send the error to client.", e);
            } else {
                throw e;
            }
        }
    }

    private void executeInJcrWrapper(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            executeInHibernateWrapper(request, response, chain);
        } finally {
            if (request instanceof HttpServletRequest) {
                DocumentManagerUtil.logoutJcrSessions((HttpServletRequest) request);
            }
        }
    }

    private void executeInHibernateWrapper(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Throwable ex) {
            // log exception because this/finally block may throw another exception
            logger.error("Error occurred during request processing.", ex);
            PersistenceManager.rollbackCurrentSessionTx();
            throw ex;
        } finally {
            PersistenceManager.endSessionLifecycle();
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
