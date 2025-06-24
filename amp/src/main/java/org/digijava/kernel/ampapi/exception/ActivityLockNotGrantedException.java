package org.digijava.kernel.ampapi.exception;

public class ActivityLockNotGrantedException extends RuntimeException {
    private Long userId;
    
    public ActivityLockNotGrantedException(Long userId) {
        this.userId = userId;
    }
    
    public Long getUserId() {
        return userId;
    }
}
