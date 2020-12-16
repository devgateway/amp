package org.digijava.kernel.ampapi.endpoints.activity;

import org.digijava.kernel.persistence.PersistenceTransactionManager;

public class TestPersistenceTransactionManager implements PersistenceTransactionManager {
    
    @Override
    public void inTransaction(Runnable runnable) {
        runnable.run();
    }
    
    @Override
    public void inTransactionWithPendingChanges(Runnable runnable) {
        inTransaction(runnable);
    }
}
