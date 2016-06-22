/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.HeatMapConfig;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

/**
 * HeatMap Configurations
 * Note: if will be ever managed through Admin, then can be defined in DB, now here for simplicity
 * 
 * @author Nadejda Mandrescu
 */
public class HeatMapConfigs {
    private static final Logger LOGGER = Logger.getLogger(HeatMapConfigs.class);
            
    /** HeatMap Dashboards Configurations */
    public static final List<HeatMapConfig> CONFIGS = new ArrayList<HeatMapConfig>() {{
       add(new HeatMapConfig(
               "HeatMap by Sector and Donor Group",
               HeatMapConfig.Type.SECTOR,
               SectorUtil.getAllSectorColumnNames(),
               Arrays.asList(ColumnConstants.DONOR_GROUP)
               ));
       add(new HeatMapConfig(
               "HeatMap by Program and Donor Group",
               HeatMapConfig.Type.PROGRAM,
               ProgramUtil.getAllProgramColumnNames(),
               Arrays.asList(ColumnConstants.DONOR_GROUP)
               ));
       add(new HeatMapConfig(
               "HeatMap by Location and Donor Group",
               HeatMapConfig.Type.LOCATION,
               LocationUtil.getAllLocationColumnNames(),
               Arrays.asList(ColumnConstants.DONOR_GROUP)
               ));
    }};
    
    private static final Map<String, Integer> SAMPLE_COLORS = new LinkedHashMap<String, Integer>() {{
      put("#C20C0C", 0);
      put("#FF7F7F", 1);
      put("#F47A4C", 5);
      put("#F4A64C", 10);
      put("#8EDD89", 15);
      put("#42873E", 20);
    }};
    
    private Set<String> visibleColumns;
    private List<String> possibleColumns = new ArrayList<String>();
    private Map<String, JsonBean> jsonColumns = new LinkedHashMap<String, JsonBean>();
    
    public HeatMapConfigs(){
    }
    
    public JsonBean getHeatMapConfigs() {
        JsonBean result = new JsonBean();
        this.visibleColumns = ColumnsVisibility.getVisibleColumns();
        List<JsonBean> jsonConfigs = new ArrayList<JsonBean>();
        for (HeatMapConfig hmConfig : CONFIGS) {
            JsonBean hmJson = convert(hmConfig);
            if (hmJson != null)
                jsonConfigs.add(hmJson);
        }
        result.set("columns", jsonColumns.values());
        result.set("charts", jsonConfigs);
        // TODO: update to whever configured in Admin once that part is done, now some dummy based on example
        result.set("colors", SAMPLE_COLORS);
        return result;
    }
    
    /**
     * Converts a HeatMap configuration to a Json display
     * @param hmConfig
     * @return null if no column set in configuration is enabled in FM
     */
    private JsonBean convert(HeatMapConfig hmConfig) {
        JsonBean hmJson = new JsonBean();
        hmJson.set("name", TranslatorWorker.translateText(hmConfig.name));
        hmJson.set("type", hmConfig.type.toString());
        
        List<Integer> columnsIndexes = getColumnIndexes(hmConfig.xColumns);
        if (columnsIndexes.size() == 0) return null;
        hmJson.set("xColumns", columnsIndexes);
        columnsIndexes = getColumnIndexes(hmConfig.yColumns);
        if (columnsIndexes.size() == 0) return null;
        hmJson.set("yColumns", columnsIndexes);
        
        return hmJson;
    }
    
    private List<Integer> getColumnIndexes(List<String> columns) {
        List<Integer> indexes = new ArrayList<Integer>();
        for (String column : columns) {
            if (visibleColumns.contains(column)) {
                int index = possibleColumns.indexOf(column);
                if (index == -1) {
                    index = possibleColumns.size();
                    possibleColumns.add(column);
                    JsonBean jsonColumn = new JsonBean();
                    jsonColumn.set("origName", column);
                    jsonColumn.set("name", TranslatorWorker.translateText(column));
                    jsonColumns.put(column, jsonColumn);
                }
                indexes.add(index);
            }
        }
        return indexes;
    }
    

}
