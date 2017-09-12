package org.digijava.kernel.xmlpatches;

import org.apache.commons.lang3.StringUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Aldo Picca
 */
public class AmpActivityEditorFieldUpdateJob {

    protected Logger logger = LoggerFactory.getLogger(AmpActivityEditorFieldUpdateJob.class);

    private void updateAllFields() {
        PersistenceManager.getSession().doWork(connection -> {
            String finalQuery = "";
            String query = "select amp_activity_id, '%s' as field from amp_activity_version where "
                    + " %s not like '" + Constants.EDITOR_KEY_PREFIX + "%%' and "
                    + " %s not like '" + Constants.EDITOR_KEY_IATI_IMPORT_PREFIX + "%%' ";

            for (Map.Entry<String, String> field : Constants.EDITOR_FIELDS_MAP.entrySet()) {
                finalQuery += (finalQuery.equals("") ? "" : " union ") + String.format(query, field.getKey(),
                        field.getValue(), field.getValue());
            }
            Map<Long, String> fieldsToFix = SQLUtils.collectKeyValue(connection, finalQuery);
            for (Map.Entry<Long, String> fieldToFix : fieldsToFix.entrySet()) {
                Session session = PersistenceManager.getRequestDBSession();
                AmpActivityVersion currentActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class,
                        fieldToFix.getKey());

                ActivityUtil.fixEditorFields(currentActivity, fieldToFix.getValue());
            }
        });
    }

    public static void run() {
        new AmpActivityEditorFieldUpdateJob().updateAllFields();
    }
}
