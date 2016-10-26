package org.digijava.kernel.ampapi.endpoints.settings;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used by XML Patcher to rename old setting names like 0,1,2 to human readable ones.
 *
 * @author Octavian Ciubotaru
 */
public class SettingsUpdater {

    private static final Logger logger = LoggerFactory.getLogger(SettingsUpdater.class);

    public static void main(String[] args) throws IOException {
        System.out.println(new SettingsUpdater().updateStateBlob(null));
    }

    /**
     * Maps old setting names to new names.
     */
    private static final Map<String, String> SETTINGS_TO_RENAME = new HashMap<String, String>() {{
        put("0", "funding-type");
        put("1", "currency");
        put("2", "calendar");
    }};

    /**
     * Static method called from xml patcher. Acts as a main method. Check {@link #update()} method.
     */
    public static void run() {
        new SettingsUpdater().update();
    }

    /**
     * Updates amp_api_state table to reflect changes to setting names.
     */
    public void update() {
        PersistenceManager.getSession().doWork(connection -> {
            Map<Long, String> states = SQLUtils.collectKeyValue(connection, "select id, stateblob from amp_api_state");
            for (Map.Entry<Long, String> state : states.entrySet()) {
                updateApiState(connection, state.getKey(), state.getValue());
            }
        });
    }

    /**
     * Updates one row in amp_api_state table to reflect changes to setting names.
     * @param connection database connection
     * @param id id of amp_api_state
     * @param blob old blob containing json with settings
     */
    private void updateApiState(Connection connection, Long id, String blob) {
        try {
            String newBlob = updateStateBlob(blob);
            SQLUtils.executeQuery(connection, String.format("update amp_api_state set stateblob=%s where id=%d",
                    SQLUtils.stringifyObject(newBlob), id));
        } catch (IOException e) {
            logger.error("Failed to update AmpApiState object.", e);
        }
    }

    /**
     * Takes as input a json and renames all settings from old name to new name.
     * @param json json object containing settings
     * @return new json with renamed setting names
     * @throws IOException thrown when input json is invalid json or an empty string
     */
    private String updateStateBlob(String json) throws IOException {
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
