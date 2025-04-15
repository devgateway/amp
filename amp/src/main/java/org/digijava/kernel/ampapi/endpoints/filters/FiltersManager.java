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

    public FilterList getLocationFilterList(boolean showAllCountries, boolean firstLevelOnly) {
        return LocationFilterListManager.getInstance().getFilterList(showAllCountries, firstLevelOnly);
    }

    public FilterList getProgramFilterList() {
        return ProgramFilterListManager.getInstance().getFilterList();
    }

    public FilterList getApprovalStatusFilter() {
        return ApprovalStatusFilterListManager.getInstance().getFilterList();
    }
    
    public FilterList getCategoryValueFilter(String filterId) {
        return CategoryValueFilterListManager.getInstance().getFilterList(filterId);
    }
    
    public FilterList getBooleanFilter(String filterId) {
        return BooleanFilterListManager.getInstance().getFilterList(filterId);
    }
    
    public FilterList getWorkspaceFilter() {
        return WorkspaceFilterListManager.getInstance().getFilterList();
    }
    
    public FilterList getComputedYearFilter() {
        return ComputedYearFilterListManager.getInstance().getFilterList();
    }
    
    public FilterList getDateFilter() {
        return DateFilterListManager.getInstance().getFilterList();
    }
    
    public FilterList getPledgesDonorFilterList() {
        return PledgesDonorFilterListManager.getInstance().getFilterList();
    }

    public FilterList getPledgesLocationFilterList() {
        return PledgesLocationFilterListManager.getInstance().getFilterList();
    }
    
    public FilterList getPledgesSectorFilterList() {
        return PledgesSectorFilterListManager.getInstance().getFilterList();
    }
    
    public FilterList getPledgesProgramFilterList() {
        return PledgesProgramFilterListManager.getInstance().getFilterList();
    }

}
