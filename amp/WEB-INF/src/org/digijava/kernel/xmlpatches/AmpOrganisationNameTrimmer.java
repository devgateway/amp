package org.digijava.kernel.xmlpatches;

import org.apache.commons.lang.StringEscapeUtils;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.util.OrganisationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is used by XML Patcher to trim organisation name.
 *
 * @author Viorel Chihai
 */
public class AmpOrganisationNameTrimmer {
    
    protected Logger logger = LoggerFactory.getLogger(AmpOrganisationNameTrimmer.class);
    
    private XMLPatchTranslationService translationService = new XMLPatchTranslationService();
    
    private static final int MESSAGE_COLUMN_KEY_INDEX = 3;
    
    private void trimOrganisationNameTranslations() {
        logger.info("Running AmpOrganisationNameTrimmer patch...");
        PersistenceManager.doInTransaction(session -> {
            List<String> orgNamesWithTrailSpaces = OrganisationUtil.getOrganisationNamesWithTrailSpaces(session);
            List<String> orgNamesWithoutTrailSpaces = OrganisationUtil.getOrganisationNamesWithoutTrailSpaces(session);
    
            List<String> orgNames = orgNamesWithTrailSpaces.stream()
                    .filter(name -> !orgNamesWithoutTrailSpaces.contains(name.trim()))
                    .collect(Collectors.toList());
            
            session.doWork(connection -> {
                insertAndDeleteOrgNameTranslations(connection, orgNames);
                updateOrgNames(connection, orgNames);
            });
        });
    }
    
    private void insertAndDeleteOrgNameTranslations(Connection connection, List<String> orgNames) {
        for (String orgName : orgNames) {
            insertAndDeleteTranslation(connection, StringEscapeUtils.escapeSql(orgName));
        }
    }
    
    private void insertAndDeleteTranslation(Connection connection, String orgName) {
        
        String query = String.format("SELECT lang_iso, message_utf8, message_key "
                + "FROM dg_message WHERE orig_message='%s'", orgName);
        
        HashMap<String, String> translationOrgNames = new HashMap<>();
        Set<String> messagesToDelete = new HashSet<>();
        try (RsInfo rsi = SQLUtils.rawRunQuery(connection, query, null)) {
            while (rsi.rs.next()) {
                translationOrgNames.put(rsi.rs.getString(1), StringEscapeUtils.escapeSql(rsi.rs.getString(2)));
                messagesToDelete.add(rsi.rs.getString(MESSAGE_COLUMN_KEY_INDEX));
            }
        } catch (SQLException e) {
            throw AlgoUtils.translateException(e);
        }
        
        String newOrgName = orgName.trim();
        String newKey = TranslatorWorker.generateTrnKey(newOrgName);
        
        for (Map.Entry<String, String> oldOrgName : translationOrgNames.entrySet()) {
            logger.info(String.format("Insert/Update org name translation from '%s' to '%s' for lang = '%s'",
                    oldOrgName.getValue(), oldOrgName.getValue().trim(), oldOrgName.getKey()));
            translationService.insertOrUpdateMessage(connection, newKey, newOrgName, oldOrgName.getKey(),
                    oldOrgName.getValue().trim());
        }
        
        for (String key : messagesToDelete) {
            logger.info(String.format("Deleting org name translation with key = '%s'", key));
            translationService.deleteMessage(connection, key);
        }
    }
    
    private void updateOrgNames(Connection connection, List<String> orgNames) {
        for (String orgName : orgNames) {
            String escapedOrgName = StringEscapeUtils.escapeSql(orgName);
            String query = (String.format("UPDATE amp_organisation SET name = '%s'"
                    + "WHERE name='%s'", escapedOrgName.trim(), escapedOrgName));
            logger.info(String.format("Updating organisation name from: '%s' to '%s'", orgName, orgName.trim()));
            SQLUtils.executeQuery(connection, query);
        }
    }
    
    public static void run() {
        new AmpOrganisationNameTrimmer().trimOrganisationNameTranslations();
    }
}