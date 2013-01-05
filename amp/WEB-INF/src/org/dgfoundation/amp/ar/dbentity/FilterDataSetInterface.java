package org.dgfoundation.amp.ar.dbentity;

import java.util.Set;

public interface FilterDataSetInterface {
	
	public void setFilterDataSet(Set<? extends AmpFilterData> filterDataSet);
	public Set<? extends AmpFilterData> getFilterDataSet();
	public AmpFilterData newAmpFilterData(FilterDataSetInterface filterRelObj,
			String propertyName, String propertyClassName,
			String elementClassName, String value);
}
