package org.digijava.kernel.xmlpatches;

import org.apache.commons.beanutils.PropertyUtils;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.editor.dbentity.Editor;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * This class is used by XML Patcher to fixes invalid editor fields.
 *
 * @author Aldo Picca
 */
public class AmpActivityEditorFieldUpdatePatch {

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

    /**
     * Get a list of fields and activities to be update.
     */
    private void updateAllFields() {
        PersistenceManager.getSession().doWork(connection -> {
            StringJoiner finalQuery = new StringJoiner(" union ");
            String query = "select amp_activity_id, '%s' as field from amp_activity_version where "
                    + " %s not like '" + EDITOR_KEY_PREFIX + "%%' and "
                    + " %s not like '" + EDITOR_KEY_IATI_IMPORT_PREFIX + "%%' ";

            for (Map.Entry<String, String> field : EDITOR_FIELDS_MAP.entrySet()) {
                finalQuery.add(String.format(query, field.getKey(),
                        field.getValue(), field.getValue()));
            }
            Map<Long, String> fieldsToFix = SQLUtils.collectKeyValue(connection, finalQuery.toString());
            for (Map.Entry<Long, String> fieldToFix : fieldsToFix.entrySet()) {
                Session session = PersistenceManager.getRequestDBSession();
                AmpActivityVersion currentActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class,
                        fieldToFix.getKey());

                fixEditorFields(currentActivity, fieldToFix.getValue());
            }
        });
    }

    public static void run() {
        new AmpActivityEditorFieldUpdatePatch().updateAllFields();
    }

    /**
     * Updates field creating a new editor entry and updating activity.
     */
    private static void fixEditorFields(AmpActivityVersion activity, String field) {

        boolean saveActivity = false;

        try {
            String currentValue = (String) PropertyUtils.getSimpleProperty(activity, field);

            if (currentValue != null && currentValue.trim().length() > 0
                    && !currentValue.startsWith(EDITOR_KEY_PREFIX) && !currentValue.startsWith(
                    EDITOR_KEY_IATI_IMPORT_PREFIX)) {

                String key = new StringBuilder(EDITOR_KEY_PREFIX).append(field).append("-").append(
                        System.currentTimeMillis()).toString();
                
                Editor editor = new Editor();
                editor.setSite(SiteUtils.getDefaultSite());
                editor.setEditorKey(key);
                editor.setLanguage(TLSUtils.getLangCode());
                editor.setLastModDate(new Date());
                editor.setBody(currentValue);

                PropertyUtils.setSimpleProperty(activity, field, key);
                saveActivity = true;
                org.digijava.module.editor.util.DbUtil.saveEditor(editor);
            }

            if (saveActivity) {
                Session session = PersistenceManager.getRequestDBSession();
                session.update(activity);
            }

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to update dg editor fields for activity");
        }
    }
}
