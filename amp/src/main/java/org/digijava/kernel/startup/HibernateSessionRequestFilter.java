package org.digijava.kernel.startup;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
            PersistenceManager.inTransaction(() -> {
                try {
                    chain.doFilter(request, response);
                } catch (IOException | ServletException e) {
                    throw new WrappedEx(e);
                }
            });
        } catch (WrappedEx e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else if (e.getCause() instanceof ServletException) {
                throw (ServletException) e.getCause();
            } else {
                throw e;
            }
        }
    }

    private static class WrappedEx extends RuntimeException {
        WrappedEx(Throwable throwable) {
            super(throwable);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
