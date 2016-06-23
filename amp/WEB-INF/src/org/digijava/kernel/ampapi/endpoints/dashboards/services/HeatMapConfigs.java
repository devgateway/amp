/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.dashboards.DashboardErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.util.DashboardConstants;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpColorThreshold;
import org.digijava.module.aim.helper.HeatMapConfig;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.visualization.util.DbUtil;

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
    
    private ApiEMGroup errors = new ApiEMGroup();
    
    private Set<String> visibleColumns;
    private List<String> possibleColumns = new ArrayList<String>();
    private Map<String, JsonBean> jsonColumns = new LinkedHashMap<String, JsonBean>();
    
    public HeatMapConfigs(){
    }
    
    public JsonBean getHeatMapConfigs() {
        LOGGER.info("GET HeatMap Configs");
        JsonBean result = new JsonBean();
        this.visibleColumns = ColumnsVisibility.getVisibleColumns();
        List<JsonBean> jsonConfigs = new ArrayList<JsonBean>();
        for (HeatMapConfig hmConfig : CONFIGS) {
            JsonBean hmJson = convert(hmConfig);
            if (hmJson != null)
                jsonConfigs.add(hmJson);
        }
        result.set(DashboardConstants.COLUMNS, jsonColumns.values());
        result.set(DashboardConstants.CHARTS, jsonConfigs);
        result.set(DashboardConstants.AMOUNT_COLORS, getColorThreshold());
        return result;
    }
    
    /**
     * Converts a HeatMap configuration to a Json display
     * @param hmConfig
     * @return null if no column set in configuration is enabled in FM
     */
    private JsonBean convert(HeatMapConfig hmConfig) {
        JsonBean hmJson = new JsonBean();
        
        List<Integer> columnsIndexes = getColumnIndexes(hmConfig.xColumns);
        if (columnsIndexes.size() == 0) return null;
        hmJson.set(DashboardConstants.X_COLUMNS, columnsIndexes);
        columnsIndexes = getColumnIndexes(hmConfig.yColumns);
        if (columnsIndexes.size() == 0) return null;
        hmJson.set(DashboardConstants.Y_COLUMNS, columnsIndexes);
        
        hmJson.set(EPConstants.NAME, TranslatorWorker.translateText(hmConfig.name));
        hmJson.set(EPConstants.TYPE, hmConfig.type.toString());
        
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
                    jsonColumn.set(EPConstants.NAME, TranslatorWorker.translateText(column));
                    jsonColumns.put(column, jsonColumn);
                }
                indexes.add(index);
            }
        }
        return indexes;
    }
    
    private Map<BigDecimal, String> getColorThreshold() {
        Map<BigDecimal, String> colorThresholds = new TreeMap<>();
        List<AmpColorThreshold> thresholds = DbUtil.getColorThresholds();
        if (thresholds != null && !thresholds.isEmpty())
            for (AmpColorThreshold threshold : thresholds)
                colorThresholds.put(threshold.getThresholdStart(), threshold.getColorHash());
        return colorThresholds;
    }
    
    /**
     * @return Heat Map Admin Settings
     */
    public JsonBean getHeatMapAdminSettings() {
        LOGGER.info("GET HeatMap Admin Settings");
        JsonBean result = new JsonBean();
        // for now only color thresholds 
        addColorThresholds(result);
        return result;
    }
    
    private void addColorThresholds(JsonBean result) {
        // get ordered list of colors by threshold
        List<AmpColorThreshold> thresholds = DbUtil.getColorThresholds();
        List<JsonBean> amountColors = new ArrayList<>();
        if (thresholds != null && !thresholds.isEmpty()) {
            for (AmpColorThreshold threshold : thresholds) {
                JsonBean colorThreshold = new JsonBean();
                colorThreshold.set(EPConstants.ID, threshold.getAmpColorThresholdId());
                colorThreshold.set(EPConstants.NAME, TranslatorWorker.translateText(threshold.getColorName()));
                colorThreshold.set(DashboardConstants.COLOR, threshold.getColorHash());
                colorThreshold.set(DashboardConstants.AMOUNT_FROM, threshold.getThresholdStart());
                amountColors.add(colorThreshold);
            }
        }
        result.set(DashboardConstants.AMOUNT_COLORS, amountColors);
    }
    
    /**
     * Updates HeatMapSettings with new configuration
     * @param config new HeatMap configuration as JSON 
     * @throws Exception 
     */
    public JsonBean saveHeatMapAdminSettings(JsonBean config) throws Exception {
        LOGGER.info("Save HeatMap Admin Settings");
        JsonBean result = new JsonBean();
        
        List<AmpColorThreshold> thresholds = readNewColorThresholds(config);
        if (!errors.isEmpty()) {
            EndpointUtils.setResponseStatusMarker(HttpServletResponse.SC_BAD_REQUEST);
            return ApiError.toError(errors.getAllErrors());
        }
        org.digijava.module.aim.util.DbUtil.saveOrUpdate(thresholds);
        
        return result;
    }
    
    private List<AmpColorThreshold> readNewColorThresholds(JsonBean config) {
        List<AmpColorThreshold> colorThresholds = new ArrayList<AmpColorThreshold>();
        List<Map<String, Object>> newThresholds = (List<Map<String, Object>>) config.get(DashboardConstants.AMOUNT_COLORS);
        Map<BigDecimal, AmpColorThreshold> testUnique = new TreeMap<>();
        
        for (Map<String, Object> threshold : newThresholds) {
            AmpColorThreshold ampColorThreshold = findAmpColorThreshold(threshold);
            BigDecimal amountFrom = getNewAmountFrom(threshold); 
            if (ampColorThreshold == null) {
                // so far not saving new, so if not found, report an error
                errors.addApiErrorMessage(DashboardErrors.INVALID_ID, String.valueOf(threshold.get(EPConstants.ID)));
            }
            if (amountFrom == null) {
                errors.addApiErrorMessage(DashboardErrors.INVALID_THRESHOLD, 
                        String.valueOf(threshold.get(DashboardConstants.AMOUNT_FROM)));
            } else if (testUnique.containsKey(amountFrom)) {
                AmpColorThreshold existing = testUnique.get(amountFrom);
                String msg = String.format("%s and %s colors are configured from the same threshold %s",
                        (existing == null ? "(invalid)" : existing.getColorHash()),
                        (ampColorThreshold == null ? "(invalid)" : ampColorThreshold.getColorHash()),
                        amountFrom.toString());
                errors.addApiErrorMessage(DashboardErrors.DUPLICATE_THRESHOLDS, msg);
            } else {
                if (ampColorThreshold != null) {
                    ampColorThreshold.setThresholdStart(amountFrom);
                    colorThresholds.add(ampColorThreshold);
                }
                testUnique.put(amountFrom, ampColorThreshold);
            }
        }
        if (!errors.isEmpty())
            colorThresholds = clear(colorThresholds);
        return colorThresholds;
    }
    
    private AmpColorThreshold findAmpColorThreshold(Map<String, Object> threshold) {
        Object id = threshold.get(EPConstants.ID);
        Long lId = id == null ? null : Long.parseLong(id.toString());
        return id == null ? null : org.digijava.module.aim.util.DbUtil.getObjectOrNull(AmpColorThreshold.class, lId);
    }
    
    private BigDecimal getNewAmountFrom(Map<String, Object> threshold) {
        Object from = threshold.get(DashboardConstants.AMOUNT_FROM);
        return from == null ? null : new BigDecimal(from.toString(), new MathContext(2, RoundingMode.HALF_EVEN));
    }
    
    private List<AmpColorThreshold> clear(List<AmpColorThreshold> colorThresholds) {
        // clear pending changes and do not store them anymore
        if (colorThresholds != null) {
            org.digijava.module.aim.util.DbUtil.clearPendingChanges();
            colorThresholds = null;
        }
        return colorThresholds;
    }
    
}
