package org.dgfoundation.amp.error;

import org.digijava.module.aim.helper.Constants;

@Deprecated
public abstract class AMPError extends Exception{
    private int level; //the priority that will be used to send the error to the central server
    private boolean continuable;

    public AMPError() {
        level = Constants.AMP_ERROR_LEVEL_WARNING;
        continuable = true;
    }

    public AMPError(int level, boolean continuable) {
        this.level = level;
        this.continuable = continuable;
    }

    public AMPError(int level, boolean continuable, Throwable cause) {
        super(cause);
        this.level = level;
        this.continuable = continuable;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isContinuable() {
        return continuable;
    }

    public void setContinuable(boolean continuable) {
        this.continuable = continuable;
    }
    
    public void autoHandle(){
        
    }
    
    
}
