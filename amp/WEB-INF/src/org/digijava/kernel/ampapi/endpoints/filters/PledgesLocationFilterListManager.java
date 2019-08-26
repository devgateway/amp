package org.digijava.kernel.ampapi.endpoints.filters;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.google.common.collect.ImmutableMap;

/**
 * This class generates the filter list (tree) object for pledges locations
 * 
 * @author Viorel Chihai
 *
 */
public final class PledgesLocationFilterListManager extends LocationFilterListManager {
    
    protected static final String PLEDGES_LOCATION_NAME = "Pledges Locations";
    
    private static PledgesLocationFilterListManager pledgesLocationFilterListManager;
    
    public static final Map<String, String> LOCATION_FILTER_ID_TO_PLEDGE_FILTER_ID =
            new ImmutableMap.Builder<String, String>()
                    .put(FiltersConstants.COUNTRY, FiltersConstants.PLEDGES_COUNTRIES)
                    .put(FiltersConstants.DISTRICT, FiltersConstants.PLEDGES_DISTRICTS)
                    .put(FiltersConstants.REGION, FiltersConstants.PLEDGES_REGIONS)
                    .put(FiltersConstants.ZONE, FiltersConstants.PLEDGES_ZONES)
                    .build();
    
    
    private PledgesLocationFilterListManager() {
        super();
    }
    
    public static PledgesLocationFilterListManager getInstance() {
        if (pledgesLocationFilterListManager == null) {
            pledgesLocationFilterListManager = new PledgesLocationFilterListManager();
        }

        return pledgesLocationFilterListManager;
    }

    public List<String> getFilterIds() {
        List<String> filterIds = CategoryManagerUtil
                .getAmpCategoryValueCollectionByKeyExcludeDeleted(CategoryConstants.IMPLEMENTATION_LOCATION_KEY)
                .stream()
                .sorted(Comparator.comparingInt(AmpCategoryValue::getIndex))
                .map(acv -> LOCATION_FILTER_ID_TO_PLEDGE_FILTER_ID.get(acv.getValue().toLowerCase()))
                .collect(Collectors.toList());
        return filterIds;
    }
    
    protected String getFilterDefinitionName() {
        return PLEDGES_LOCATION_NAME;
    }
}
