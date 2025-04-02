package org.dgfoundation.amp.forms;

public class LockVerificationResult {
    
    public final static int REFRESH_LOCK_ALLOWED = 1; // nobody cares about this item
    public final static int REFRESH_LOCK_DISALLOWED = 2; // lock actively held by somebody else
    public final static int REFRESH_LOCK_EXPIRED = 3; // lock lost - someone can take it over
    
    public final int resultCode;
    public final Long currentOwner;
    
    public LockVerificationResult(int resultCode, Long currentOwner){
        this.resultCode = resultCode;
        this.currentOwner = currentOwner;
    }
    
    public boolean isActionAllowed(){
        return resultCode != REFRESH_LOCK_DISALLOWED;
    }
}
