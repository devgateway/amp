package org.digijava.kernel.xmlpatches;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
abstract class AbstractAmpApiStateUpdater {

    protected final Logger logger = LoggerFactory.getLogger(AbstractAmpApiStateUpdater.class);

    /**
     * Updates amp_api_state table to reflect changes to stateblobs.
     */
    public void update() {
        PersistenceManager.doWorkInTransaction(connection -> {
            Map<Long, String> states = SQLUtils.collectKeyValue(connection, "select id, stateblob from amp_api_state");
            for (Map.Entry<Long, String> state : states.entrySet()) {
                updateApiState(connection, state.getKey(), state.getValue());
            }
        });
    }

    /**
     * Updates one row in amp_api_state table to reflect changes to stateblob.
     * @param connection database connection
     * @param id id of amp_api_state
     * @param blob old stateblob
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
     * Takes as input a json and returns another transformed json.
     * @param json old json
     * @return new json
     * @throws IOException thrown when input json is invalid json or an empty string
     */
    protected abstract String updateStateBlob(String blob) throws IOException;
}
