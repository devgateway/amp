/**
 * IntWrapper.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.dgfoundation.amp.algo.ValueWrapper;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org. Modified by Constantin Dolghier
 * @since Aug 31, 2006. Moved / Edited on 21 Aug 2014
 * 
 */
public class IntWrapper extends ValueWrapper<Integer> {
    public static final int DEFAULT_VALUE = 0;

    public IntWrapper() {
        super(DEFAULT_VALUE);
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
        return value.shortValue();
    }

    public int intValue() {
        return value;
    }

    public void reset() {
        value = DEFAULT_VALUE;
    }
}
