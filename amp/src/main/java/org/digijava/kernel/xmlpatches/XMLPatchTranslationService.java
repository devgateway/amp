package org.digijava.kernel.xmlpatches;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;

public class XMLPatchTranslationService {
    
    private boolean existTranslationInDb(Connection connection, String newKey, String isoCode) {
        try (RsInfo rsi = SQLUtils.rawRunQuery(connection, String.format("SELECT * FROM dg_message"
                + " WHERE message_key='%s' AND lang_iso='%s'", newKey, isoCode), null)) {
            return rsi.rs.next();
        } catch (SQLException e) {
            throw AlgoUtils.translateException(e);
        }
    }
    
    public void insertOrUpdateMessage(Connection connection, String newKey, String newName, String isoCode,
                                      String translatedMessage) {
        String insertOrUpdateQuery = "";
        String created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        
        boolean existTranslationInDb = existTranslationInDb(connection, newKey, isoCode);
        
        if (existTranslationInDb) {
            insertOrUpdateQuery = String.format("UPDATE dg_message SET message_utf8='%s' WHERE message_key='%s'"
                        + " AND lang_iso='%s'", translatedMessage, newKey, isoCode);
        } else {
            insertOrUpdateQuery = String.format("INSERT INTO dg_message (message_key, lang_iso, site_id, message_utf8,"
                            + " created, orig_message) VALUES ('%s', '%s', '3', '%s', '%s', '%s')",
                    newKey, isoCode, translatedMessage, created, newName);
        }
        
        SQLUtils.executeQuery(connection, insertOrUpdateQuery);
    }
    
    public void deleteMessage(Connection connection, String key) {
        String deleteQuery = String.format("DELETE FROM dg_message WHERE message_key = '%s'", key);
        
        SQLUtils.executeQuery(connection, deleteQuery);
    }
    
}
