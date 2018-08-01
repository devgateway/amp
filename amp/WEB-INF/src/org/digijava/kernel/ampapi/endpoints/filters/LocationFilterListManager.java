package org.digijava.kernel.ampapi.endpoints.filters;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
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
public class LocationFilterListManager implements FilterListManager {
    
    public static final String LOCATION_NAME = "Locations";
    private static final String LOCATIONS_ITEMS_NAME = "locations";
    
    private static LocationFilterListManager locationFilterListManager;

    public static LocationFilterListManager getInstance() {
        if (locationFilterListManager == null) {
            locationFilterListManager = new LocationFilterListManager();
        }

        return locationFilterListManager;
    }
    
    protected LocationFilterListManager() { }

    @Override
    public FilterList getFilterList() {
        List<FilterListDefinition> locationTreeDefinitions = getLocationListDefinitions();
        Map<String, List<FilterListTreeNode>> locationTreeItems = getLocationListItems();
        
        return new FilterList(locationTreeDefinitions, locationTreeItems);
    }
    
    protected List<FilterListDefinition> getLocationListDefinitions() {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        FilterListDefinition listDefinition = new FilterListDefinition();
        
        listDefinition.setId(null);
        listDefinition.setName(getFilterDefinitionName());
        listDefinition.setDisplayName(TranslatorWorker.translateText(getFilterDefinitionName()));
        listDefinition.setFilterIds(getFilterIds());
        listDefinition.setFiltered(true);
        listDefinition.setItems(LOCATIONS_ITEMS_NAME);
        listDefinitions.add(listDefinition);
        
        return listDefinitions;
    }

    protected List<String> getFilterIds() {
        List<String> filterIds = CategoryManagerUtil
                .getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.IMPLEMENTATION_LOCATION_KEY)
                .stream()
                .sorted(Comparator.comparingInt(AmpCategoryValue::getIndex))
                .map(acv -> acv.getValue().toLowerCase())
                .collect(Collectors.toList());
        return filterIds;
    }
    
    protected Map<String, List<FilterListTreeNode>> getLocationListItems() {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        List<FilterListTreeNode> locationItems = new ArrayList<>();
        
        Map<Long, LocationSkeleton> locations = LocationSkeleton.populateSkeletonLocationsList();
        
        List<Long> countriesWithChildrenIds = getCountriesWithChildrenIds();
        
        for (Long countryId : countriesWithChildrenIds) {
            LocationSkeleton countryLocation = locations.get(countryId);
            locationItems.add(getLocations(countryLocation));
            locationItems.add(UNDEFINED_OPTION);
        }
        
        items.put(LOCATIONS_ITEMS_NAME, locationItems);

        return items;
    }

    /**
     * @return Country ids with children
     */
    protected List<Long> getCountriesWithChildrenIds() {
        String query = "SELECT acvl.id FROM amp_category_value_location acvl "
                + "WHERE acvl.parent_location IS NULL "
                + "AND acvl.id IN (SELECT DISTINCT parent_location FROM amp_category_value_location)";
        
        Collection<BigInteger> countryCollection = PersistenceManager.getSession().createSQLQuery(query).list();
        List<Long> countryIds = countryCollection
                .stream()
                .map(b -> Long.valueOf(b.intValue()))
                .collect(Collectors.toList());
        
        return countryIds;
    }
    
    protected FilterListTreeNode getLocations(LocationSkeleton location) {
        FilterListTreeNode node = new FilterListTreeNode();
        node.setId(location.getId());
        node.setCode(location.getCode());
        node.setName(location.getName());
        
        List<LocationSkeleton> orderedLocations = location.getChildLocations().stream()
                .sorted(Comparator.comparing(LocationSkeleton::getName))
                .collect(Collectors.toList());
        
        for (LocationSkeleton locationChild : orderedLocations) {
            node.addChild(getLocations(locationChild));
        }
        
        return node;
    }
    
    protected String getFilterDefinitionName() {
        return LOCATION_NAME;
    }

}
