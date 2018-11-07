package org.digijava.kernel.xmlpatches;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This class is used by XML Patcher to rename old filter names to new ids.
 *
 * @author Octavian Ciubotaru
 */
public class AmpApiStateFiltersUpdater extends AbstractAmpApiStateUpdater {

    public static void main(String[] args) throws IOException {
        System.out.println(new AmpApiStateFiltersUpdater().updateStateBlob(""));
    }

    /**
     * Maps old filter names to new names.
     */
    private static final Map<String, String> FILTERS_TO_RENAME = new HashMap<String, String>() {{
        put("Actual Approval Date", "actual-approval-date");
        put("Actual Completion Date", "actual-completion-date");
        put("Actual Start Date", "actual-start-date");
        put("Approval Status", "approval-status");
        put("Archived", "archived");
        put("Beneficiary Agency", "beneficiary-agency");
        put("Beneficiary Agency Id", "beneficiary-agency");
        put("Computed Year", "computed-year");
        put("Contracting Agency", "contracting-agency");
        put("Contracting Agency Id", "contracting-agency");
        put("Contracting Agency Groups", "contracting-agency-groups");
        put("Country", "country");
        put("Disaster Response Marker", "disaster-response-marker");
        put("District", "district");
        put("Donor Agency", "donor-agency");
        put("Donor", "donor-agency");
        put("Donor Id", "donor-agency");
        put("Donor Group", "donor-group");
        put("Donor Type", "donor-type");
        put("Effective Funding Date", "effective-funding-date");
        put("Executing Agency", "executing-agency");
        put("Executing Agency Id", "executing-agency");
        put("Expenditure Class", "expenditure-class");
        put("Final Date for Contracting", "final-date-for-contracting");
        put("Issue Date", "issue-date");
        put("Financing Instrument", "financing-instrument");
        put("Funding Closing Date", "funding-closing-date");
        put("Funding Status", "funding-status");
        put("Government Approval Procedures", "government-approval-procedures");
        put("Humanitarian Aid", "humanitarian-aid");
        put("Implementing Agency", "implementing-agency");
        put("Implementing Agency Id", "implementing-agency");
        put("Joint Criteria", "joint-criteria");
        put("Location", "location");
        put("Mode of Payment", "mode-of-payment");
        put("National Planning Objectives", "national-planning-objectives");
        put("National Planning Objectives Level 1", "national-planning-objectives-level-1");
        put("National Planning Objectives Level 2", "national-planning-objectives-level-2");
        put("National Planning Objectives Level 3", "national-planning-objectives-level-3");
        put("National Planning Objectives Level 4", "national-planning-objectives-level-4");
        put("National Planning Objectives Level 5", "national-planning-objectives-level-5");
        put("National Planning Objectives Level 6", "national-planning-objectives-level-6");
        put("National Planning Objectives Level 7", "national-planning-objectives-level-7");
        put("National Planning Objectives Level 8", "national-planning-objectives-level-8");
        put("National Plan Objective", "national-planning-objectives");
        put("National Planning Objectives Level 1 Id", "national-planning-objectives-level-1");
        put("National Planning Objectives Level 2 Id", "national-planning-objectives-level-2");
        put("National Planning Objectives Level 3 Id", "national-planning-objectives-level-3");
        put("National Planning Objectives Level 4 Id", "national-planning-objectives-level-4");
        put("National Planning Objectives Level 5 Id", "national-planning-objectives-level-5");
        put("National Planning Objectives Level 6 Id", "national-planning-objectives-level-6");
        put("National Planning Objectives Level 7 Id", "national-planning-objectives-level-7");
        put("National Planning Objectives Level 8 Id", "national-planning-objectives-level-8");
        put("On/Off/Treasury Budget", "on-off-treasury-budget");
        put("Pledges Aid Modality", "pledges-aid-modality");
        put("Pledges Donor Group", "pledges-donor-group");
        put("Pledges National Plan Objectives", "pledges-national-plan-objectives");
        put("Pledges Programs", "pledges-programs");
        put("Pledges Secondary Programs", "pledges-secondary-programs");
        put("Pledges Secondary Sectors", "pledges-secondary-sectors");
        put("Pledges sectors", "pledges-sectors");
        put("Pledges Tertiary Sectors", "pledges-tertiary-sectors");
        put("Pledges Titles", "pledges-titles");
        put("Primary Program", "primary-program");
        put("Primary Program Level 1", "primary-program-level-1");
        put("Primary Program Level 2", "primary-program-level-2");
        put("Primary Program Level 3", "primary-program-level-3");
        put("Primary Program Level 4", "primary-program-level-4");
        put("Primary Program Level 5", "primary-program-level-5");
        put("Primary Program Level 6", "primary-program-level-6");
        put("Primary Program Level 7", "primary-program-level-7");
        put("Primary Program Level 8", "primary-program-level-8");
        put("Primary Sector", "primary-sector");
        put("Primary Sector Id", "primary-sector");
        put("Primary Sector Sub-Sector", "primary-sector-sub-sector");
        put("Primary Sector Sub-Sector Id", "primary-sector-sub-sector");
        put("Primary Sector Sub-Sub-Sector", "primary-sector-sub-sub-sector");
        put("Primary Sector Sub-Sub-Sector Id", "primary-sector-sub-sub-sector");
        put("Procurement System", "procurement-system");
        put("Project Implementing Unit", "project-implementing-unit");
        put("Proposed Approval Date", "proposed-approval-date");
        put("Proposed Completion Date", "proposed-completion-date");
        put("Proposed Start Date", "proposed-start-date");
        put("Region", "region");
        put("Responsible Organization", "responsible-organization");
        put("Secondary Program", "secondary-program");
        put("Secondary Program Level 1", "secondary-program-level-1");
        put("Secondary Program Level 2", "secondary-program-level-2");
        put("Secondary Program Level 3", "secondary-program-level-3");
        put("Secondary Program Level 4", "secondary-program-level-4");
        put("Secondary Program Level 5", "secondary-program-level-5");
        put("Secondary Program Level 6", "secondary-program-level-6");
        put("Secondary Program Level 7", "secondary-program-level-7");
        put("Secondary Program Level 8", "secondary-program-level-8");
        put("Secondary Sector", "secondary-sector");
        put("Secondary Sector Id", "secondary-sector");
        put("Secondary Sector Sub-Sector", "secondary-sector-sub-sector");
        put("Secondary Sector Sub-Sector Id", "secondary-sector-sub-sector");
        put("Secondary Sector Sub-Sub-Sector", "secondary-sector-sub-sub-sector");
        put("Secondary Sector Sub-Sub-Sector Id", "secondary-sector-sub-sub-sector");
        put("Sector Tag", "sector-tag");
        put("Status", "status");
        put("Team", "team");
        put("Tertiary Program", "tertiary-program");
        put("Tertiary Program Level 1", "tertiary-program-level-1");
        put("Tertiary Program Level 2", "tertiary-program-level-2");
        put("Tertiary Program Level 3", "tertiary-program-level-3");
        put("Tertiary Program Level 4", "tertiary-program-level-4");
        put("Tertiary Program Level 5", "tertiary-program-level-5");
        put("Tertiary Program Level 6", "tertiary-program-level-6");
        put("Tertiary Program Level 7", "tertiary-program-level-7");
        put("Tertiary Program Level 8", "tertiary-program-level-8");
        put("Tertiary Sector", "tertiary-sector");
        put("Quaternary Sector", "quaternary-sector");
        put("Quinary Sector", "quinary-sector");

        put("Tertiary Sector Sub-Sector", "tertiary-sector-sub-sector");
        put("Tertiary Sector Sub-Sub-Sector", "tertiary-sector-sub-sub-sector");
        put("Type Of Assistance", "type-of-assistance");
        put("Workspaces", "workspaces");
        put("Zone", "zone");
    }};

    /**
     * Static method called from xml patcher. Acts as a main method. Check {@link #update()} method.
     */
    public static void run() {
        new AmpApiStateFiltersUpdater().update();
    }

    protected String updateStateBlob(String json) throws IOException {
        if (json == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        JsonNode filtersNode = rootNode.path("filters");
        if (filtersNode.isObject()) {
            ObjectNode filters = (ObjectNode) filtersNode;
            copyAndRenameFilters(filters.path("filter").path("otherFilters"), filters);
            copyAndRenameFilters(filters.path("filter").path("columnFilters"), filters);
            copyAndRenameFilters(filters.path("otherFilters"), filters);
            copyAndRenameFilters(filters.path("columnFilters"), filters);
            filters.remove("filter");
            filters.remove("otherFilters");
            filters.remove("columnFilters");
        }
        return mapper.writeValueAsString(rootNode);
    }

    private void copyAndRenameFilters(JsonNode oldFilters, ObjectNode newFilters) {
        Iterator<String> filterNames = oldFilters.fieldNames();
        while (filterNames.hasNext()) {
            String filterName = filterNames.next();
            String newFilterName = FILTERS_TO_RENAME.getOrDefault(filterName, filterName);
            if (newFilterName.equals(filterName) && !filterName.equals("date")) {
                logger.warn("Not mapped: " + filterName);
            }
            newFilters.put(newFilterName, oldFilters.get(filterName));
        }
    }
}
