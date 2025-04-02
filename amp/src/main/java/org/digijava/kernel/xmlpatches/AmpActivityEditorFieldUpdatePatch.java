package org.digijava.kernel.xmlpatches;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.editor.dbentity.Editor;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * This class is used by XML Patcher to fixes invalid editor fields.
 *
 * @author Aldo Picca
 */
public class AmpActivityEditorFieldUpdatePatch {
    
    private static final int ACT_ID_COL_POS = 1;
    private static final int FIELD_NAME_COL_POS = 2;
    private static final int FIELD_VALUE_COL_POS = 3;

    private static Logger logger = Logger.getLogger(AmpActivityEditorFieldUpdatePatch.class);

    public static final String EDITOR_KEY_PREFIX = "aim-";
    public static final String EDITOR_KEY_IATI_IMPORT_PREFIX = "_iati_import_";
    public static final Map<String, String> EDITOR_FIELDS_MAP = Collections.unmodifiableMap(
            new LinkedHashMap<String, String>() { {
                put("activitySummary", "activity_summary");
                put("conditionality", "conditionality");
                put("description", "description");
                put("environment", "environment");
                put("equalOpportunity", "equalopportunity");
                put("lessonsLearned", "lessons_learned");
                put("minorities", "minorities");
                put("objective", "objectives");
                put("programDescription", "program_description");
                put("projectComments", "projectcomments");
                put("projectImpact", "project_impact");
                put("projectManagement", "project_management");
                put("purpose", "purpose");
                put("results", "results");
                put("statusReason", "status_reason");
            } }
    );
    
    public static void run() {
        new AmpActivityEditorFieldUpdatePatch().updateAllActivitiesWithWrongEditorFields();
    }

    /**
     * Heal wrong activity editor fields
     */
    private void updateAllActivitiesWithWrongEditorFields() {
        
        Map<Long, Map<String, String>> activitiesFieldsMap = new HashMap<>();
        
        Session session = PersistenceManager.openNewSession();
        Transaction tx = session.beginTransaction();
        try {
            activitiesFieldsMap = getActivitiesFieldsMap(session);
            for (Map.Entry<Long, Map<String, String>> activityFieldsMap : activitiesFieldsMap.entrySet()) {
                Long actId = activityFieldsMap.getKey();
                AmpActivityVersion currentActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, actId);
                updateActivityEditorFields(session, currentActivity, activityFieldsMap.getValue());
            }
            logger.info(activitiesFieldsMap.size() + " activities updated");
        } catch (Throwable e) {
            tx.rollback();
            throw e;
        } finally {
            PersistenceManager.closeSession(session);
        }
    }

    /**
     * @param activitiesFieldsMap
     * @param session
     */
    private Map<Long, Map<String, String>> getActivitiesFieldsMap(Session session) {
        Map<Long, Map<String, String>> activitiesFieldsMap = new HashMap<>();
        session.doWork(connection -> {
            try (RsInfo rsi = SQLUtils.rawRunQuery(connection, generateAllEditorSQLQuery(), null)) {
                while (rsi.rs.next()) {
                    Long activityId = rsi.rs.getLong(ACT_ID_COL_POS);
                    String fieldName = rsi.rs.getString(FIELD_NAME_COL_POS);
                    String fieldValue = rsi.rs.getString(FIELD_VALUE_COL_POS);
                    
                    Map<String, String> fieldMap = activitiesFieldsMap.getOrDefault(activityId, new HashMap<>());
                    fieldMap.put(fieldName, fieldValue);
                    activitiesFieldsMap.put(activityId, fieldMap);
                }
            } catch (SQLException e) {
                throw AlgoUtils.translateException(e);
            }
        });
        
        return activitiesFieldsMap;
    }

    /**
     * Updates fields by creating a new editor entry and updating activity.
     */
    private void updateActivityEditorFields(Session session, AmpActivityVersion activity, 
            Map<String, String> fieldsMap) {

        boolean saveActivity = false;
        try {
            for (Entry<String, String> field : fieldsMap.entrySet()) {
                String editorKey = String.format("%s%s-%s", EDITOR_KEY_PREFIX, field.getKey(), 
                        System.currentTimeMillis());
                Editor editor = new Editor();
                editor.setSite(SiteUtils.getDefaultSite());
                editor.setEditorKey(editorKey);
                editor.setLanguage(TLSUtils.getLangCode());
                editor.setLastModDate(new Date());
                editor.setBody(field.getValue());

                PropertyUtils.setSimpleProperty(activity, field.getKey(), editorKey);
                session.save(editor);
                saveActivity = true;
            }

            if (saveActivity) {
                session.update(activity);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to update dg editor fields for activity");
        }
    }
    
    /**
     * @return generated SQL query
     */
    private String generateAllEditorSQLQuery() {
        StringJoiner finalQuery = new StringJoiner(" UNION ");
        String query = "SELECT amp_activity_id, '%s' AS field, %s AS value FROM amp_activity_version WHERE "
                + " %s not like '" + EDITOR_KEY_PREFIX + "%%' and "
                + " %s not like '" + EDITOR_KEY_IATI_IMPORT_PREFIX + "%%' "
                + " AND COALESCE(TRIM(%s), '') <> ''";

        for (Map.Entry<String, String> field : EDITOR_FIELDS_MAP.entrySet()) {
            finalQuery.add(String.format(query, field.getKey(),
                    field.getValue(), field.getValue(), field.getValue(), field.getValue()));
        }
        
        return finalQuery.toString();
    }
}
