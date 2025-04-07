package org.dgfoundation.amp.testutils;

import org.digijava.kernel.persistence.PersistenceManager;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import javax.persistence.RollbackException;

/**
 * JUnit 5 extension for managing Hibernate transactions during tests.
 * Always rolls back to prevent committing invalid test data.
 */
public class InTransactionRule implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        try {
            PersistenceManager.inTransaction(() -> {
                try {
                    context.getRequiredTestMethod().invoke(context.getRequiredTestInstance());
                } catch (Throwable e) {
                    throw new WrappedException(e);
                }
                throw new RollbackException("gn2389uackm2q10");
            });
        } catch (WrappedException e) {
            throw e;
        } catch (RollbackException e) {
            if (!"gn2389uackm2q10".equals(e.getMessage())) {
                throw e;
            }
        }
    }

    /**
     * Wraps exceptions for proper unwrapping after transaction handling.
     */
    private static final class WrappedException extends RuntimeException {
        WrappedException(Throwable throwable) {
            super(throwable);
        }
    }
}
