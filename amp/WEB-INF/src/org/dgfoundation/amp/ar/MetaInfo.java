/**
 * MetaInfo.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * <b> Immutable</b>class describing objects with purpose of wrapping metainformation. a
 * MetaObject is a normal Comparable object with some category text attached.
 * The category, is particularized as a String.
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 15, 2006
 * 
 */
public class MetaInfo<T> implements Serializable, Cloneable  {

    /**
     * somewhat of an ugly hack, but null-valued metadata is only used as boolean anyway (or as a bug, in which case we want a crash)
     */
    public final static Boolean NULL_PLACEHOLDER_VALUE = new Boolean(true);
    
    protected final String category;

    protected final T value;

    /**
     * @return Returns the category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return Returns the value.
     */
    public T getValue() {
        return value;
    }

    public MetaInfo(String category, T value) {
        this.category = category;
        if (value == null)
            value = (T) NULL_PLACEHOLDER_VALUE;
        this.value = value;
    }
    
    public String toString() {
        return category + ": " + value;
    }   
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof MetaInfo)
        {
            MetaInfo<?> theObj = (MetaInfo<?>) obj;
            return this.category.equals(theObj.category) && this.value.equals(theObj.value);            
        }
        return false;
    }

    @Override
    public int hashCode() {     
        return this.category.hashCode() ^ (19 * this.value.hashCode());
    }

}
