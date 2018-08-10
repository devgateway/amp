package org.digijava.kernel.ampapi.endpoints.auditlogger;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AmpActivityInternalIdsDefinition implements AuditLoggerDefinitions{


    List<List<String>> columnDefinitions = new ArrayList<List<String>>(
            Arrays.asList(Arrays.asList("amp_sector","select amp_sector_id, name from amp_sector"),
                    Arrays.asList("amp_classification_config","select id, name from amp_classification_config"), null));
    Map<String,Map<Long,String>> values = new HashMap();
    @Override
    public List<List<String>> getColumnDefinitions() {
        return columnDefinitions;
    }
    @Override
    public void setColumnDefinitions(List<List<String>> coloumnDefinitions) {
        this.columnDefinitions = coloumnDefinitions;
    }
    public String getValue(String rawValue, Integer index){
        if(values.get(columnDefinitions.get(index)) == null) {
            //if value is null means we need to return raw value
            return rawValue;
        }
         else{
                if (values.get(columnDefinitions.get(index).get(0)) == null) {
                    //the first position of the second array we need to fetch and populate the new

                    values.put(columnDefinitions.get(index).get(0), populateListOfValues(columnDefinitions.get(index).get(1)));
                }
                return values.get(columnDefinitions.get(index).get(0)).get(rawValue);
            }
        }
    //we store
    protected Map<Long,String> populateListOfValues(String query) {
        final Map<Long, String> listOfValues = new HashMap<>();
        PersistenceManager.getSession().doWork(connection -> {
            RsInfo rsi = SQLUtils.rawRunQuery(connection, query, null);
            if (rsi.rs.next()) {
                listOfValues.put(rsi.rs.getLong(1), rsi.rs.getString(2));
            }
        });
        return listOfValues;
    }

}
