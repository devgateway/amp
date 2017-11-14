package org.digijava.kernel.ampapi.endpoints.filters;

/**
 * The Filter List Manager returns all list filters (organizations, sectors, programs, locations)
 * 
 * @author Viorel Chihai
 *
 */
public final class FiltersManager {

    private static FiltersManager filtersManager;
    
    /**
     * 
     * @return FiltersManager instance
     */
    public static FiltersManager getInstance() {
        if (filtersManager == null) {
            filtersManager = new FiltersManager();
        }

        return filtersManager;
    }
    
    private FiltersManager() { }
    
    /**
     * 
     * @return the filter list for organizations
     */
    public FilterList getOrganizationFilterList() {
        return OrganizationFilterListManager.getInstance().getFilterList();
    }

    public FilterList getSectorFilterList() {
        return SectorFilterListManager.getInstance().getFilterList();
    }

    public FilterList getLocationFilterList() {
        return LocationFilterListManager.getInstance().getFilterList();
    }

    public FilterList getProgramFilterList() {
        return ProgramFilterListManager.getInstance().getFilterList();
    }

}
