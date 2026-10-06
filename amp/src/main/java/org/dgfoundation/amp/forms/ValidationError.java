package org.dgfoundation.amp.forms;

public class ValidationError {
    public final String errMsg;
    
    public ValidationError(String errMsg)
    {
        this.errMsg = errMsg;
    }
    
    @Override
    public String toString(){
        return this.errMsg;
    }
}
