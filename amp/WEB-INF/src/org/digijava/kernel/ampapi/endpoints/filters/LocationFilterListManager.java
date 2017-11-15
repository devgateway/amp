package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationSkeleton;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

/**
 * This class generates the filter list (tree) object for locations
 * 
 * @author Viorel Chihai
 *
 */
public final class LocationFilterListManager implements FilterListManager {
    
    private static final String LOCATIONS_NAME = "Locations";
    private static final String LOCATIONS_ITEMS_NAME = "locations";
    private static LocationFilterListManager locationFilterListManager;

    public static LocationFilterListManager getInstance() {
        if (locationFilterListManager == null) {
            locationFilterListManager = new LocationFilterListManager();
        }

        return locationFilterListManager;
    }
    
    private LocationFilterListManager() { }

    @Override
    public FilterList getFilterList() {
        List<FilterListDefinition> locationTreeDefinitions = getLocationListDefinitions();
        Map<String, List<FilterListTreeNode>> locationTreeItems = getLocationListItems();
        
        return new FilterList(locationTreeDefinitions, locationTreeItems);
    }
    
    private List<FilterListDefinition> getLocationListDefinitions() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        FilterListDefinition listDefinition = new FilterListDefinition();
        
        List<String> filterIds =  CategoryManagerUtil
                .getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.IMPLEMENTATION_LOCATION_KEY)
                .stream()
                .sorted(Comparator.comparingInt(AmpCategoryValue::getIndex))
                .map(AmpCategoryValue::getValue)
                .collect(Collectors.toList());
        
        listDefinition.setId(null);
        listDefinition.setName(LOCATIONS_NAME);
        listDefinition.setDisplayName(TranslatorWorker.translateText(LOCATIONS_NAME));
        listDefinition.setFilterIds(filterIds);
        listDefinition.setFiltered(true);
        listDefinition.setItems(LOCATIONS_ITEMS_NAME);
        listDefinitions.add(listDefinition);
        
        return listDefinitions;
    }
    
    private Map<String, List<FilterListTreeNode>> getLocationListItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        List<FilterListTreeNode> locationItems = new ArrayList<>();
        
        Map<Long, LocationSkeleton> locations = LocationSkeleton.populateSkeletonLocationsList();
        
        long defaultCountryLocationId = getDefaultCountryLocationId();
        LocationSkeleton defaultCountryLocation = locations.get(defaultCountryLocationId);
        
        locationItems.add(getLocations(defaultCountryLocation));
        items.put(LOCATIONS_ITEMS_NAME, locationItems);

        return items;
    }

    /**
     * @return default AMP country id
     */
    private long getDefaultCountryLocationId() {
        long parentLocationId = PersistenceManager.getLong(PersistenceManager.getSession()
                .createSQLQuery("SELECT acvl.id FROM amp_category_value_location acvl "
                        + "WHERE acvl.parent_location IS NULL AND location_name = (" 
                        + "SELECT country_name FROM dg_countries WHERE iso = '" 
                        + FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY) + "')")
                        .list().get(0));
        
        return parentLocationId;
    }
    
    private FilterListTreeNode getLocations(LocationSkeleton location) {
        FilterListTreeNode node = new FilterListTreeNode();
        node.setId(location.getId());
        node.setCode(location.getCode());
        node.setName(location.getName());
        
        for (LocationSkeleton locationChild : location.getChildLocations()) {
            node.addChild(getLocations(locationChild));
        }
        
        return node;
    }

}
