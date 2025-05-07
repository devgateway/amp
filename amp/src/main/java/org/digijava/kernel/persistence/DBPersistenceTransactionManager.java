package org.digijava.kernel.persistence;


import javax.persistence.FlushModeType;

public class DBPersistenceTransactionManager implements PersistenceTransactionManager {
    
    @Override
    public void inTransaction(Runnable runnable) {
        PersistenceManager.inTransaction(runnable);
    }
    
    @Override
    public void inTransactionWithPendingChanges(Runnable runnable) {
        PersistenceManager.getSession().setFlushMode(FlushModeType.COMMIT);
        inTransaction(runnable);
    }
    
}
