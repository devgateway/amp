package org.dgfoundation.amp.testutils;

import javax.persistence.RollbackException;

import org.digijava.kernel.persistence.PersistenceManager;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * The sole purpose of this rule is to create an environment where you can access hibernate session.
 *
 * See {@link org.junit.internal.runners.statements.ExpectException}, it swallows exceptions from nested statements.
 * The only option we have is to always rollback the transaction because committing becomes impossible for tests
 * where current session contains invalid objects. For example objects which cannot pass bean validation or violate
 * database constraints.
 *
 * @author Octavian Ciubotaru
 */
public class InTransactionRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    PersistenceManager.inTransaction(() -> {
                        try {
                            base.evaluate();
                        } catch (Throwable e) {
                            throw new WrappedException(e);
                        }
                        throw new RollbackException("gn2389uackm2q10");
                    });
                } catch (WrappedException e) {
                    throw e.getCause();
                } catch (RollbackException e) {
                    if (!"gn2389uackm2q10".equals(e.getMessage())) {
                        throw e;
                    }
                }
            }
        };
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
