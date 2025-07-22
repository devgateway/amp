package org.digijava.kernel.ampapi.filters;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.EntityResolver;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter that enables resolution of hibernate entities referenced as ids during jackson deserialization.
 *
 * @author Octavian Ciubotaru
 */
public class HibernateEntityResolverFilter implements Filter {

    private HibernateEntityResolver hibernateEntityResolver;

    @Override
    public void init(FilterConfig filterConfig) {
        PersistenceManager.doInTransaction(session -> {
            hibernateEntityResolver = new HibernateEntityResolver(session);
        });
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        try {
            EntityResolver.doWithResolver(hibernateEntityResolver, () -> {
                try {
                    filterChain.doFilter(servletRequest, servletResponse);
                } catch (IOException | ServletException e) {
                    throw new WrappedException(e);
                }
            });
        } catch (WrappedException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else if (e.getCause() instanceof ServletException) {
                throw (ServletException) e.getCause();
            } else {
                throw new IllegalStateException("Unknown exception to unwrap.", e);
            }
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * This exception is intentionally local to this class as to avoid catching wrong "wrapped exception".
     */
    private static class WrappedException extends RuntimeException {

        WrappedException(Throwable throwable) {
            super(throwable);
        }
    }
}
