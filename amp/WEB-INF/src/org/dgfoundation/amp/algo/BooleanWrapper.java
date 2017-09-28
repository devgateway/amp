package org.dgfoundation.amp.algo;


public class BooleanWrapper extends ValueWrapper<Boolean> {

    public BooleanWrapper(boolean value) {
        super(value);
    }
    
    public void and(boolean op) {
        this.value &= op;
    }
    
    public void or(boolean op) {
        this.value |= op;
    }   
}
