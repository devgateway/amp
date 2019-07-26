package org.digijava.kernel.xmlpatches;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This class is used by XML Patcher to rename old setting names like 0,1,2 to human readable ones.
 *
 * @author Octavian Ciubotaru
 */
public class AmpApiStateUpdater extends AbstractAmpApiStateUpdater {

    public static void main(String[] args) throws IOException {
        System.out.println(new AmpApiStateUpdater().updateStateBlob(null));
    }

    /**
     * Maps old setting names to new names.
     */
    private static final Map<String, String> SETTINGS_TO_RENAME = new HashMap<String, String>() {{
        put("0", "funding-type");
        put("1", "currency-code");
        put("2", "calendar-id");
        put("calendarCurrencies", "calendar-currencies");
    }};

    /**
     * Static method called from xml patcher. Acts as a main method. Check {@link #update()} method.
     */
    public static void run() {
        new AmpApiStateUpdater().update();
    }

    /**
     * Takes as input a json and renames all settings from old name to new name.
     * @param json json object containing settings
     * @return new json with renamed setting names
     * @throws IOException thrown when input json is invalid json or an empty string
     */
    protected String updateStateBlob(String json) throws IOException {
        if (json == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        JsonNode settingsNode = rootNode.path("settings");
        if (settingsNode.isObject()) {
            ObjectNode settings = (ObjectNode) settingsNode;
            for (Map.Entry<String, String> entry : SETTINGS_TO_RENAME.entrySet()) {
                if (settings.has(entry.getKey())) {
                    JsonNode node = settings.remove(entry.getKey());
                    settings.put(entry.getValue(), node);
                }
            }
        }
        return mapper.writeValueAsString(rootNode);
    }
}
