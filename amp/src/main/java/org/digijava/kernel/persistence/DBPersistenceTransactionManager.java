package org.digijava.kernel.persistence;


import javax.persistence.FlushModeType;

public class DBPersistenceTransactionManager implements PersistenceTransactionManager {

    @Override
    public void inTransaction(Runnable runnable) {
        PersistenceManager.inTransaction(runnable);
    }

    /**
     * Runs the runnable with flush mode COMMIT. If we are already inside an inTransaction (e.g. import
     * batch), runs the runnable in the same transaction without nesting, so the same session is used
     * throughout and Hibernate's "possible non-threadsafe access to session" assertion is not triggered.
     */
    @Override
    public void inTransactionWithPendingChanges(Runnable runnable) {
        PersistenceManager.getSession().setFlushMode(FlushModeType.COMMIT);
        if (PersistenceManager.getTransactionNestingDepth() > 0) {
            runnable.run();
        } else {
            inTransaction(runnable);
        }
    }
}
