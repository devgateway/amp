package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * a class which holds the cached i18n values for a (model, field)  
 * @author Dolghier Constantin
 *
 */
public class ColumnValuesCacher {
    
    /**
     * the property whose values are being cached
     */
    public final PropertyDescription cachedProperty;
    
    public final HashMap<Long, String> values = new HashMap<Long, String>();
    
    public ColumnValuesCacher(PropertyDescription cachedProperty)
    {
        this.cachedProperty = cachedProperty;
    }
    
    public void importValues(Map<Long, String> newValues)
    {
        values.putAll(newValues);
    }
    
    @Override
    public String toString()
    {
        return String.format("CVC of %s", cachedProperty.getNiceDescription());
    }
}
