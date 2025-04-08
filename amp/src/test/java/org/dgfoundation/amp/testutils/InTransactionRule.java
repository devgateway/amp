package org.dgfoundation.amp.testutils;

import org.digijava.kernel.persistence.PersistenceManager;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import javax.persistence.RollbackException;

/**
 * JUnit 5 extension for managing Hibernate transactions during tests.
 * Always rolls back to prevent committing invalid test data.
 */
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class InTransactionRule implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // No setup needed before each test
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        try {
            PersistenceManager.inTransaction(() -> {
                try {
                    // In JUnit 5, the test has already executed by this point
                    // so we don't need to call evaluate() like in JUnit 4
                } catch (Throwable e) {
                    throw new WrappedException(e);
                }
                throw new RollbackException("gn2389uackm2q10");
            });
        } catch (WrappedException e) {
            throw (Exception) e.getCause();
        } catch (RollbackException e) {
            if (!"gn2389uackm2q10".equals(e.getMessage())) {
                throw e;
            }
        }
    }

    /**
     * Using private class to wrap exceptions in order to guarantee correct unwrapping.
     */
    private static final class WrappedException extends RuntimeException {
        WrappedException(Throwable throwable) {
            super(throwable);
        }
    }
}
