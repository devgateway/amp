/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides GIS FM enabled options
 * @author Nadejda Mandrescu
 */
public class GisFMSettings extends DataVisibility implements FMSettings {
	/****************************************
	 * TODO: there is no actual list of options defined so far
	 ****************************************/
	
	@Override
	public Set<String> getEnabledSettings() {
		return getCurrentVisibleData();
	}
	
	protected GisFMSettings() {
	}

	@Override
	protected List<String> getVisibleByDefault() {
		return noDataList;
	}

	@Override
	protected Set<String> getAllData() {
		return noDataSet;
	}

	@Override
	protected Map<String, String> getDataMap(DataMapType dataMapType) {
		return noDataMap;
	}

	@Override
	protected Map<String, Collection<String>> getDependancyMapTypeAny() {
		return noDataCollectionMap;
	}

	@Override
	protected Map<String, Collection<String>> getDependancyMapTypeAll() {
		return noDataCollectionMap;
	}

}
