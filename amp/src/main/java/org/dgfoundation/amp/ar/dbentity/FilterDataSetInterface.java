package org.dgfoundation.amp.ar.dbentity;

import java.util.Set;

public interface FilterDataSetInterface<K extends AmpFilterData> {
    
    public void setFilterDataSet(Set<K> filterDataSet);
    public Set<K> getFilterDataSet();
    public AmpFilterData newAmpFilterData(FilterDataSetInterface<K> filterRelObj,
            String propertyName, String propertyClassName,
            String elementClassName, String value);
}
