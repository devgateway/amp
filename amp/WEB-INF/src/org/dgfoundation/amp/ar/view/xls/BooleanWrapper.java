package org.dgfoundation.amp.ar.view.xls;


public class BooleanWrapper {

	public boolean value = false;

	public BooleanWrapper(boolean value) {
		this.value = value;
	}
	
	public void and(boolean op) {
		this.value &= op;
	}
	
	public void or(boolean op) {
		this.value |= op;
	}
	
	@Override
	public String toString() {
		return Boolean.toString(this.value);
	}
}
