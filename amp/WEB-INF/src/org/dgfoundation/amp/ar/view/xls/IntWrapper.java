/**
 * IntWrapper.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 31, 2006
 * 
 */
public class IntWrapper {
	public static final int DEFAULT_VALUE = 0;

	protected int value;

	public IntWrapper() {
		super();
		value = DEFAULT_VALUE;
	}

	public IntWrapper inc() {
		value++;
		return this;
	}

	public IntWrapper inc(int amount) {
		value += amount;
		return this;
	}

	public IntWrapper dec() {
		value--;
		return this;
	}

	public IntWrapper dec(int amount) {
		value -= amount;
		return this;
	}

	public short shortValue() {
		return (short) value;
	}

	public int intValue() {
		return value;
	}

	public void reset() {
		value = DEFAULT_VALUE;
	}

	public void set(int amount) {
		value=amount;
	}
	
}
