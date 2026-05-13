package org.digijava.kernel.xmlpatches;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used by XML Patcher to update translations for Maps and Dashboards menu titles.
 *
 * @author Viorel Chihai
 */
public class AmpMenuEntryTranslationsUpdater {
    
    static final String OLD_MAP_KEY = "1843656017";
    static final String NEW_MAP_KEY = "107868";
    static final String OLD_DASHBOARDS_KEY = "936655517";
    static final String NEW_DASHBOARDS_KEY = "1876060255";

    protected Logger logger = LoggerFactory.getLogger(AmpMenuEntryTranslationsUpdater.class);
    
    private void updateAllMenuEntriesAndTranslations() {
        PersistenceManager.doInTransaction(s -> {
            s.doWork(connection -> {
                updateMenuEntries(connection);
                updateMenuTranslations(connection);
                deleteOldMenuTranslations(connection);
            });
        });
    }

    private void updateMenuEntries(Connection connection) {
            updateMenuEntry(connection, "Map", "Map", "New GIS");
            updateMenuEntry(connection, "Dashboards Menu", "Dashboards", "New Dashboards");
    }

    private void updateMenuEntry(Connection connection, String name, String title, String originalName) {
        SQLUtils.executeQuery(connection,
                String.format("UPDATE amp_menu_entry SET name='%s', title='%s' WHERE name='%s'", 
                        name, title, originalName));
    }
    
    private void updateMenuTranslations(Connection connection) {
        updateOrInsertMenuTranslation(connection, OLD_MAP_KEY, NEW_MAP_KEY, "NEW GIS", "Map");
        updateOrInsertMenuTranslation(connection, OLD_DASHBOARDS_KEY, NEW_DASHBOARDS_KEY, "NEW DASHBOARD (BETA)", 
                "Dashboards");
    }
    
    private void updateOrInsertMenuTranslation(Connection connection, String oldKey, String newKey, 
            String oldName, String newName) {
        
        String query = String.format("SELECT lang_iso, message_utf8 FROM dg_message WHERE message_key='%s'", oldKey);
        
        HashMap<String, String> translations = new HashMap<>();
        try (RsInfo rsi = SQLUtils.rawRunQuery(connection, query, null)) {
            while (rsi.rs.next()) {
                translations.put(rsi.rs.getString(1), rsi.rs.getString(2));
            }
        } catch (SQLException e) {
            throw AlgoUtils.translateException(e);
        }
        
        for (Map.Entry<String, String> translation : translations.entrySet()) {
            updateOrInsertMessage(connection, newKey, oldName, newName, translation.getKey(), translation.getValue());
        }
    }
    
    private void updateOrInsertMessage(Connection connection, String newKey, String oldName, String newName, 
            String isoCode, String translatedMessage) {
        
        String insertOrUpdateQuery = "";
        String created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        
        boolean existTranslationInDb = existTranslationInDb(connection, newKey, isoCode);
        
        if (existTranslationInDb) {
            if (!oldName.equalsIgnoreCase(translatedMessage)) {
                insertOrUpdateQuery = String.format("UPDATE dg_message SET message_utf8='%s' WHERE message_key='%s'"
                        + " AND lang_iso='%s'", translatedMessage, newKey, isoCode);
            } else {
                insertOrUpdateQuery = "SELECT 1 = 1";
            }
        } else {
            if (oldName.equalsIgnoreCase(translatedMessage)) {
                translatedMessage = newName;
            }
            
            insertOrUpdateQuery = String.format("INSERT INTO dg_message (message_key, lang_iso, site_id, message_utf8,"
                    + " created, orig_message) VALUES ('%s', '%s', '3', '%s', '%s', '%s')", 
                    newKey, isoCode, translatedMessage, created, translatedMessage);
        }
        
        SQLUtils.executeQuery(connection, insertOrUpdateQuery);
    }

    /**
     * @param connection
     * @param newKey
     * @param isoCode
     * @param keyExists
     * @return
     */
    private boolean existTranslationInDb(Connection connection, String newKey, String isoCode) {
        try (RsInfo rsi = SQLUtils.rawRunQuery(connection, String.format("SELECT * FROM dg_message"
                + " WHERE message_key='%s' AND lang_iso='%s'", newKey, isoCode), null)) {
            return rsi.rs.next();
        } catch (SQLException e) {
            throw AlgoUtils.translateException(e);
        }
    }

    private void deleteOldMenuTranslations(Connection connection) {
        deleteMenuTranslation(connection, OLD_MAP_KEY);
        deleteMenuTranslation(connection, OLD_DASHBOARDS_KEY);
    }
    
    private void deleteMenuTranslation(Connection connection, String messageKey) {
        SQLUtils.executeQuery(connection,
                String.format("DELETE FROM dg_message WHERE message_key='%s'", messageKey));
    }

    public static void run() {
        new AmpMenuEntryTranslationsUpdater().updateAllMenuEntriesAndTranslations();
    }
}
