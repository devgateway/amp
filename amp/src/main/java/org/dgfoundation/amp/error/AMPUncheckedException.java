package org.dgfoundation.amp.error;

import org.digijava.module.aim.helper.Constants;

import java.util.LinkedList;

public class AMPUncheckedException extends RuntimeException implements AMPTaggedExceptions {
    private static final int DEFAULT_ERROR_LEVEL = Constants.AMP_ERROR_LEVEL_ERROR;
    private LinkedList<String> tags;
    //exception importance
    private int level;
    //was the exception handled and the user action did continue?
    //we use it to signal errors that are handled in the code, but weren'r supposed to happen
    //so maybe the users are doing something wrong
    private boolean continuable;
    
    public AMPUncheckedException() {
        super();
        level = DEFAULT_ERROR_LEVEL;
        continuable = false;
    }
    
    public AMPUncheckedException(String message) {
        super(message);
        level = DEFAULT_ERROR_LEVEL;
        continuable = false;
    }
    
    public AMPUncheckedException(int level, boolean continuable, String message) {
        super(message);
        this.level = level;
        this.continuable = continuable;
    }
    
    public AMPUncheckedException(AMPException ae){ //error get's propagated -- tags get inherited -- tag on this level will be added to the back
        super(ae);
        this.continuable = ae.isContinuable();
        this.level = ae.getLevel();
        this.tags = ae.getTags();
    }
    
    public AMPUncheckedException(AMPUncheckedException ae){ //error get's propagated -- tags get inherited -- tag on this level will be added to the back
        super(ae);
        this.continuable = ae.isContinuable();
        this.level = ae.getLevel();
        this.tags = ae.getTags();
    }

    public AMPUncheckedException(Throwable cause){
        super(cause);
        level = DEFAULT_ERROR_LEVEL;
        continuable = false;
    }

    public AMPUncheckedException(int level, boolean continuable){
        super();
        this.level = level;
        this.continuable = continuable;
    }
    
    public AMPUncheckedException(int level, boolean continuable, AMPException cause){
        super(cause);
        this.level = level;
        this.continuable = continuable;
        this.tags = cause.getTags();
    }
    
    public AMPUncheckedException(int level, boolean continuable, AMPUncheckedException cause){
        super(cause);
        this.level = level;
        this.continuable = continuable;
        this.tags = cause.getTags();
    }

    public AMPUncheckedException(int level, boolean continuable, Throwable cause){
        super(cause);
        this.level = level;
        this.continuable = continuable;
    }


    public LinkedList<String> getTags() {
        return tags;
    }

    public void setTags(LinkedList<String> tags) {
        this.tags = tags;
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
    
    /**
     * Tags are inherited if the exception is propagated and wrapped into a new AMPException
     * if the right constructor is used: new AMPException(AMPException ae)
     * Later on tags are added to the end of the list.
     * The first tag of the list is the most significant one.
     */
    public void addTag(String tag){
        if (tags == null){
            tags = new LinkedList<String>();
        }
        tags.add(tag);
    }
    
    public Throwable getMainCause(){
        Throwable c = this;
        while (c.getCause() != null)
            c = c.getCause();
        return c;
    }
}
