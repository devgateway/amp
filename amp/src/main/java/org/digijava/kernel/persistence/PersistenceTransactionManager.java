package org.digijava.kernel.persistence;

public interface PersistenceTransactionManager {
    
    void inTransaction(Runnable runnable);
    
    /**
     *  Execute the runnable with session in FlushMode = Commit so that Hiberante doesn't try to commit intermediate
     *
     * @param runnable
     */
    void inTransactionWithPendingChanges(Runnable runnable);
    
}
