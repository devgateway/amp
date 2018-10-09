package org.digijava.kernel.ampapi.endpoints.auditlogger;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.auditlogger.dto.AuditLoggerRecord;
import org.digijava.kernel.persistence.PersistenceManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class AuditLoggerService {
    private AuditLoggerService(){

    }
    public static AuditLoggerService getIinstance(){
        return new AuditLoggerService();
    }
    protected static Logger logger = Logger.getLogger(AuditLoggerService.class);
    private Map<String,AuditLoggerDefinitions> AuditLoggerDefinitionsMap= new HashMap<>();

    public AuditLoggerChanges getAuditLogger() {
        List<AuditLoggerRecord> records = new ArrayList();
        PersistenceManager.getSession().doWork(connection -> {

            RsInfo rsi = SQLUtils.rawRunQuery(connection, getAuditLogerQuery(), null);
            if (rsi.rs.next()) {
                records.add(populateColumns(rsi.rs));
            }
            rsi.close();
        });
        AuditLoggerChanges c = new AuditLoggerChanges();
        c.setAuditLoggerRecords(records);
        return c;
    }

    private String getAuditLogerQuery() {
        String query = "select * from ( " +
                "select Array[a.amp_sector_id, a.classification_config_id, a.sector_percentage]::text[] currentvalue, " +
                "Array[b.amp_sector_id, b.classification_config_id, b.sector_percentage] previousvalue, " +
                "a.amp_activity_id as current_amp_activity_id, " +
                "b.amp_activity_id as  previous_amp_activity_id, " +
                "case when a.amp_activity_sector_id is null then 'D' when b.amp_activity_sector_id is null then 'I' " +
                "else 'U' end as type, " +
                "'amp_activity_sector' as table_affected " +
                "from amp_activity_sector a " +
                "FULL OUTER JOIN amp_activity_sector b " +
                "on a.previous_object_id =b.amp_activity_sector_id " +
                "where a.amp_activity_id = 115729 " +
                "or b.amp_activity_id = 115728  " +
                "union  " +
                "select Array[a.amp_org_id,a.internal_id::bigint]::text[] currentvalue,  " +
                "Array[b.amp_org_id,b.internal_id::bigint] previousvalue, a.amp_activity_id " +
                "current_amp_activity_id, b.amp_activity_id previous_amp_activity_id, " +
                "case when a.id is null then 'D' when b.id is null then 'I' else 'U' end as type, " +
                "'amp_activity_internal_id' as table_affected " +
                "from amp_activity_internal_id a " +
                "FULL OUTER JOIN amp_activity_internal_id b " +
                "on a.previous_object_id =b.id " +
                "where a.amp_activity_id = 115729 " +
                "or b.amp_activity_id = 115728 " +
                " ) t order by table_affected ";
        return query;
    }

    public  AuditLoggerRecord populateColumns(ResultSet record) {
        AuditLoggerRecord r = new AuditLoggerRecord();

        try {
            String type = record.getString("type");
            String tableAffectred = record.getString("table_affected");
            r.setType(type);
            r.setTabletAffected(record.getString("table_affected"));
            r.setCurrentAmpActivityId(record.getLong("current_amp_activity_id"));
            r.setPreviousAmpActivityId(record.getLong("previous_amp_activity_id"));
            r.setCurrentValues(new LinkedHashSet<>());
            r.getCurrentValues().add("hola");

            String[] currentvalue = (String[]) record.getArray("currentvalue").getArray();

            AuditLoggerDefinitions oAuditLoggerDefinitions;
            switch (tableAffectred) { // we will get the isntance by reflection
                case "amp_activity_internal_id":

                    if (AuditLoggerDefinitionsMap.get(tableAffectred) == null) {
                        oAuditLoggerDefinitions = new AmpActivityInternalIdsDefinition();
                    }
                    break;

                case "amp_activity_sector":
                    break;
                default:
                    break;
            }
            AmpActivityInternalIdsDefinition o = new AmpActivityInternalIdsDefinition();
            for (int i = 0 ; i<currentvalue.length ; i++ ){

               // o.getColumnDefinitions().get(0).get(0)
            }
            // Arrays.asList(activityIds.getPath().split("\\s*,\\s*")),
        } catch (SQLException ex) {
            logger.error("cannot extrsact row", ex);
        }
        return r;
    }

}

