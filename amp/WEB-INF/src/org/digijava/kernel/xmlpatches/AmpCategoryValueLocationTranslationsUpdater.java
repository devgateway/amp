package org.digijava.kernel.xmlpatches;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used by XML Patcher to create translations for Administrative Level CV names.
 *
 * @author Viorel Chihai
 */
public class AmpCategoryValueLocationTranslationsUpdater {
    
    private static final String ADMINISTRATIVE_LEVEL = "Administrative Level";
    
    private static final Map<String, String> PLEDGES_LEVEL_MAP = new HashMap<String, String>() {{
        put("Pledges Countries", "0");
        put("Pledges Regions", "1");
        put("Pledges Zones", "2");
        put("Pledges Districts", "3");
        put("Pledges Communal Section", "4");
    }};
    
    private static final Map<String, String> FORM_LOCATIONS_TRANSLATIONS_MAP = new HashMap<String, String>() {{
        put("Select Region", "1");
        put("Select Zone", "2");
        put("Select Woreda", "3");
    }};
    
    private static final int MESSAGE_COLUMN_KEY_INDEX = 3;

    protected Logger logger = LoggerFactory.getLogger(AmpCategoryValueLocationTranslationsUpdater.class);
    
    private void insertAndDeleteTranslations() {
        PersistenceManager.doInTransaction(s -> {
            s.doWork(connection -> {
                insertAndDeleteCategoryValueLocationTranslations(connection);
                insertAndDeletePledgesLocationTranslations(connection);
                insertAndDeleteFormLocationTranslations(connection);
            });
        });
    }
    
    private void insertAndDeleteCategoryValueLocationTranslations(Connection connection) {
        String query = "SELECT category_value, index_column from amp_category_value "
                + "WHERE amp_category_class_id = "
                + "(SELECT id FROM amp_category_class WHERE category_name = 'Implementation Location') "
                + "AND deleted is false";
    
        HashMap<String, String> catValueLevels = new HashMap<>();
        try (RsInfo rsi = SQLUtils.rawRunQuery(connection, query, null)) {
            while (rsi.rs.next()) {
                catValueLevels.put(rsi.rs.getString(1), rsi.rs.getString(2));
            }
        } catch (SQLException e) {
            throw AlgoUtils.translateException(e);
        }
    
        for (Map.Entry<String, String> catValueLevel : catValueLevels.entrySet()) {
            insertAndDeleteTranslations(connection, "", catValueLevel.getKey(), catValueLevel.getValue());
        }
    }
    
    private void insertAndDeletePledgesLocationTranslations(Connection connection) {
        for (Map.Entry<String, String> pledgeLevel : PLEDGES_LEVEL_MAP.entrySet()) {
            insertAndDeleteTranslations(connection, "Pledges ", pledgeLevel.getKey(), pledgeLevel.getValue());
        }
    }
    
    private void insertAndDeleteFormLocationTranslations(Connection connection) {
        for (Map.Entry<String, String> trn : FORM_LOCATIONS_TRANSLATIONS_MAP.entrySet()) {
            insertAndDeleteTranslations(connection, "Select ", trn.getKey(), trn.getValue());
        }
    }
    
    private void insertAndDeleteTranslations(Connection connection, String prefix, String value, String indexLevel) {
        
        String query = String.format("SELECT lang_iso, message_utf8, message_key "
                + "FROM dg_message WHERE orig_message='%s'", value);
        
        HashMap<String, String> translations = new HashMap<>();
        Set<String> messagesToDelete = new HashSet<>();
        try (RsInfo rsi = SQLUtils.rawRunQuery(connection, query, null)) {
            while (rsi.rs.next()) {
                translations.put(rsi.rs.getString(1), rsi.rs.getString(2));
                messagesToDelete.add(rsi.rs.getString(MESSAGE_COLUMN_KEY_INDEX));
            }
        } catch (SQLException e) {
            throw AlgoUtils.translateException(e);
        }
        
        String newTranslationValue = prefix + ADMINISTRATIVE_LEVEL + " " + indexLevel;
        String newKey = TranslatorWorker.generateTrnKey(newTranslationValue);
        
        for (Map.Entry<String, String> oldTranslation : translations.entrySet()) {
            insertMessage(connection, newKey, newTranslationValue, oldTranslation.getKey(), oldTranslation.getValue());
        }
        
        for (String key : messagesToDelete) {
            deleteMessage(connection, key);
        }
    }
    
    private void insertMessage(Connection connection, String newKey, String newValue, String langIso,
                               String translatedMessage) {
        
        String created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        
        String insertQuery = String.format("INSERT INTO dg_message (message_key, lang_iso, site_id, message_utf8,"
                    + " created, orig_message) VALUES ('%s', '%s', '3', '%s', '%s', '%s')",
                    newKey, langIso, translatedMessage, created, newValue);
        SQLUtils.executeQuery(connection, insertQuery);
    }
    
    private void deleteMessage(Connection connection, String key) {
        String deleteQuery = String.format("DELETE FROM dg_message WHERE message_key = '%s'", key);
        
        SQLUtils.executeQuery(connection, deleteQuery);
    }

    public static void run() {
        new AmpCategoryValueLocationTranslationsUpdater().insertAndDeleteTranslations();
    }
}
