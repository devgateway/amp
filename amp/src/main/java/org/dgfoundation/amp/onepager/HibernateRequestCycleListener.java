package org.dgfoundation.amp.onepager;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Wicket request cycle listener that manages Hibernate session lifecycle
 * for each web request.
 */
public class HibernateRequestCycleListener extends AbstractRequestCycleListener {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HibernateRequestCycleListener.class);

    @Override
    public void onBeginRequest(RequestCycle cycle) {
        if (PersistenceManager.isSessionManaged()) {
            logger.warn("Session already exists when starting new request - possible nesting issue");
            return;
        }

        try {
            Session session = PersistenceManager.getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            logger.debug("Started new session and transaction for request");
        } catch (Exception e) {
            logger.error("Failed to start Hibernate session for request", e);
            throw new RuntimeException("Failed to initialize Hibernate session", e);
        }
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        try {
            Session session = PersistenceManager.getSession();
            if (session == null || !session.isOpen()) {
                logger.debug("No active session to close at end of request");
                return;
            }

            Transaction transaction = session.getTransaction();
            try {
                if (transaction.isActive()) {
                    if (transaction.getStatus().canRollback()) {
                        transaction.commit();
                        logger.debug("Successfully committed transaction");
                    } else {
                        logger.warn("Transaction in unexpected state: " + transaction.getStatus());
                    }
                }
            } catch (Exception e) {
                logger.error("Error during transaction commit - attempting rollback", e);
                try {
                    if (transaction.isActive()) {
                        transaction.rollback();
                        logger.debug("Rolled back transaction after error");
                    }
                } catch (Exception rollbackEx) {
                    logger.error("Failed to rollback transaction", rollbackEx);
                }
                throw new RuntimeException("Failed to commit transaction", e);
            }
        } finally {
            try {
                PersistenceManager.cleanupThread();
                logger.debug("Cleaned up thread resources");
            } catch (Exception e) {
                logger.error("Error during thread cleanup", e);
            }
        }
    }

    @Override
    public void onDetach(RequestCycle cycle) {
        // Ensure cleanup even if request processing fails
        PersistenceManager.cleanupThread();
    }

    @Override
    public IRequestHandler onException(RequestCycle cycle, Exception ex) {
        logger.error("Exception occurred during request processing - cleaning up session", ex);
        PersistenceManager.cleanupThread();
        return null;
    }
}
