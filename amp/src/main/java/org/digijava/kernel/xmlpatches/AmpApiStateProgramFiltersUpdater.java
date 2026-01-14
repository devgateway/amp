package org.digijava.kernel.xmlpatches;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * This class is used by XML Patcher to rename old program filter names to new id.
 *
 * @author Viorel Chihai
 */
public class AmpApiStateProgramFiltersUpdater extends AbstractAmpApiStateUpdater {

    public static void main(String[] args) throws IOException {
        System.out.println(new AmpApiStateProgramFiltersUpdater().updateStateBlob(""));
    }

    /**
     * Maps old filter names to new names.
     */
    private static final Map<String, String> FILTERS_TO_RENAME = new HashMap<String, String>() {{
        put("national-planning-objectives", "national-planning-objectives-level-1");
        put("primary-program", "primary-program-level-1");
        put("secondary-program", "secondary-program-level-1");
        put("tertiary-program", "tertiary-program-level-1");
    }};

    /**
     * Static method called from xml patcher. Acts as a main method. Check {@link #update()} method.
     */
    public static void run() {
        new AmpApiStateProgramFiltersUpdater().update();
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
