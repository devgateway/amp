package org.digijava.kernel.ampapi.endpoints.filters;

import com.google.common.collect.ImmutableMap;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationSkeleton;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.util.*;
import java.util.stream.Collectors;

import static org.digijava.module.categorymanager.util.CategoryConstants.*;

/**
 * This class generates the filter list (tree) object for locations
 *
 * @author Viorel Chihai
 */
public class LocationFilterListManager implements FilterListManager {

    public static final String LOCATION_NAME = "Locations";
    private static final String LOCATIONS_ITEMS_NAME = "locations";

    private static final Map<String, String> FILTER_COLUMN_BY_IMPL_LOC_KEY = new ImmutableMap.Builder<String, String>()
            .put(IMPLEMENTATION_LOCATION_ADM_LEVEL_0.getValueKey(), FiltersConstants.ADMINISTRATIVE_LEVEL_0)
            .put(IMPLEMENTATION_LOCATION_ADM_LEVEL_1.getValueKey(), FiltersConstants.ADMINISTRATIVE_LEVEL_1)
            .put(IMPLEMENTATION_LOCATION_ADM_LEVEL_2.getValueKey(), FiltersConstants.ADMINISTRATIVE_LEVEL_2)
            .put(IMPLEMENTATION_LOCATION_ADM_LEVEL_3.getValueKey(), FiltersConstants.ADMINISTRATIVE_LEVEL_3)
            .put(IMPLEMENTATION_LOCATION_ADM_LEVEL_4.getValueKey(), FiltersConstants.ADMINISTRATIVE_LEVEL_4)
            .build();

    private static LocationFilterListManager locationFilterListManager;

    public static LocationFilterListManager getInstance() {
        if (locationFilterListManager == null) {
            locationFilterListManager = new LocationFilterListManager();
        }

        return locationFilterListManager;
    }

    protected LocationFilterListManager() {
    }

    @Override
    public FilterList getFilterList(boolean showAllCountries, boolean firstLevelOnly) {

        List<FilterListDefinition> locationTreeDefinitions = getLocationListDefinitions(firstLevelOnly);
        Map<String, List<FilterListTreeNode>> locationTreeItems = getLocationListItems(showAllCountries,
                firstLevelOnly);

        return new FilterList(locationTreeDefinitions, locationTreeItems);
    }

    protected List<FilterListDefinition> getLocationListDefinitions(boolean firstLevelOnly) {
        List<FilterListDefinition> listDefinitions = new ArrayList<>();
        FilterListDefinition listDefinition = new FilterListDefinition();

        listDefinition.setId(null);
        listDefinition.setName(getFilterDefinitionName());
        listDefinition.setDisplayName(TranslatorWorker.translateText(getFilterDefinitionName()));
        listDefinition.setFilterIds(getFilterIds(firstLevelOnly));
        listDefinition.setFiltered(true);
        listDefinition.setItems(LOCATIONS_ITEMS_NAME);
        listDefinitions.add(listDefinition);

        return listDefinitions;
    }

    protected List<String> getFilterIds(boolean firstLevelOnly) {
        List<String> filterIds = CategoryManagerUtil
                .getAmpCategoryValueCollectionByKeyExcludeDeleted(IMPLEMENTATION_LOCATION_KEY)
                .stream()
                .sorted(Comparator.comparingInt(AmpCategoryValue::getIndex))
                .filter(cv -> cv.getIndex() == 0 || !firstLevelOnly)
                .map(acv -> FILTER_COLUMN_BY_IMPL_LOC_KEY.get(acv.getValue()))
                .collect(Collectors.toList());
        return filterIds;
    }

    protected Map<String, List<FilterListTreeNode>> getLocationListItems(boolean showAllCountries,
                                                                         boolean firstLevelOnly) {
        Map<String, List<FilterListTreeNode>> items = new HashMap<>();
        List<FilterListTreeNode> locationItems = new ArrayList<>();

        Map<Long, LocationSkeleton> locations = LocationSkeleton.populateSkeletonLocationsList();

        List<Long> countriesWithChildrenIds = getCountriesWithChildrenIds(showAllCountries);

        for (Long countryId : countriesWithChildrenIds) {
            LocationSkeleton countryLocation = locations.get(countryId);
            locationItems.add(getLocations(countryLocation, firstLevelOnly));
        }
        locationItems.add(getUndefinedOption());

        items.put(LOCATIONS_ITEMS_NAME, locationItems);

        return items;
    }

    /**
     * @return Country ids with children or all countries if showAllCountries == true
     */
    protected List<Long> getCountriesWithChildrenIds(boolean pShowAllCountries) {

        Collection<AmpCategoryValueLocations> countryCollection =
                LocationUtil.getCountriesWithChildren(pShowAllCountries);

        AmpApplicationSettings appSettings = EndpointUtils.getAppSettings();
        final boolean showAllCountries = appSettings == null ? false : appSettings.getShowAllCountries();

        String defaultCountryIso = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_COUNTRY);

        List<Long> countryIds = countryCollection
                .stream()
                .filter(country -> showAllCountries || pShowAllCountries
                        || country.getIso().equals(defaultCountryIso) || defaultCountryIso.equals("zz"))
                .map(country -> country.getId())
                .collect(Collectors.toList());

        return countryIds;
    }

    protected FilterListTreeNode getLocations(LocationSkeleton location, boolean firstLevelOnly) {
        FilterListTreeNode node = new FilterListTreeNode();
        node.setId(location.getId());
        node.setCode(location.getCode());
        node.setName(location.getName());
        node.setExtraInfo(this.getExtraInfo(location));
        node.setDescription(location.getDescription());

        List<LocationSkeleton> orderedLocations = location.getChildLocations().stream()
                .sorted(Comparator.comparing(LocationSkeleton::getName))
                .collect(Collectors.toList());
        if (!firstLevelOnly) {
            for (LocationSkeleton locationChild : orderedLocations) {
                node.addChild(getLocations(locationChild, false));
            }
        }

        return node;
    }

    private Object getExtraInfo(LocationSkeleton location) {
        ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<>();

        if (location.getTemplateId() != null) {
            builder.put("template", location.getTemplateId());
        }
        if (location.getGroupId() != null) {
            builder.put("group-id", location.getGroupId());
        }
        if (location.getLat() != null && location.getLon() != null) {
            ImmutableMap.Builder<String, Object> centroid = new ImmutableMap.Builder<>();
            centroid.put("lat", location.getLat());
            centroid.put("lon", location.getLon());
            builder.put("centro-id", centroid.build());

        }
        ImmutableMap extraInfo = builder.build();
        return extraInfo.isEmpty() ? null : extraInfo;
    }

    protected String getFilterDefinitionName() {
        return LOCATION_NAME;
    }

}
