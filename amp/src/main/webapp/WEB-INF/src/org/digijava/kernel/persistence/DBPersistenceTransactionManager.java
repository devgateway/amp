package org.digijava.kernel.persistence;

import org.hibernate.FlushMode;

public class DBPersistenceTransactionManager implements PersistenceTransactionManager {
    
    @Override
    public void inTransaction(Runnable runnable) {
        PersistenceManager.inTransaction(runnable);
    }
    
    @Override
    public void inTransactionWithPendingChanges(Runnable runnable) {
        PersistenceManager.getSession().setFlushMode(FlushMode.COMMIT);
        inTransaction(runnable);
    }
    
}
