package org.digijava.kernel.ampapi.endpoints.dashboards.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.digijava.kernel.ampapi.endpoints.dashboards.DashboardErrors;
import org.digijava.kernel.ampapi.endpoints.errors.ApiEMGroup;
import org.digijava.kernel.ampapi.endpoints.exception.AmpWebApplicationException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpColorThreshold;
import org.digijava.module.aim.helper.HeatMapConfig;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;

/**
 * HeatMap Configurations
 * Note: if will be ever managed through Admin, then can be defined in DB, now here for simplicity
 * 
 * @author Nadejda Mandrescu
 */
public class HeatMapConfigService {

    private static final Logger LOGGER = Logger.getLogger(HeatMapConfigService.class);
            
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
               getLocationsForHeatMap(),
               Arrays.asList(ColumnConstants.DONOR_GROUP)
               ));
    }};

    private static final int DEFAULT_SCALE = 6;

    private ApiEMGroup errors = new ApiEMGroup();
    
    private Set<String> visibleColumns;
    private List<String> possibleColumns = new ArrayList<>();
    private Map<String, Column> jsonColumns = new LinkedHashMap<>();
    
    public HeatMapConfigService() {
        visibleColumns = ColumnsVisibility.getVisibleColumns();
    
        for (HeatMapConfig heatMapConfig : CONFIGS) {
            processHeatMapConfigColumns(heatMapConfig.xColumns);
            processHeatMapConfigColumns(heatMapConfig.yColumns);
        }
    }
    
    private void processHeatMapConfigColumns(List<String> columns) {
        for (String column : columns) {
            if (visibleColumns.contains(column)) {
                int index = possibleColumns.indexOf(column);
                if (index == -1) {
                    possibleColumns.add(column);
                    jsonColumns.put(column, new Column(column, TranslatorWorker.translateText(column)));
                }
            }
        }
    }
    
    public HeatMapConfigs getHeatMapConfigs() {
        LOGGER.info("GET HeatMap Configs");
        List<HeatMapConfig> charts = CONFIGS.stream()
                .map(config -> getChartFromConfig(config))
                .filter(config -> config != null)
                .collect(Collectors.toList());
        
        return new HeatMapConfigs(new ArrayList<>(jsonColumns.values()), charts, getColorThreshold());
    }
    
    private HeatMapConfig getChartFromConfig(HeatMapConfig config) {
        List<String> xColumns = new ArrayList<>(possibleColumns);
        xColumns.retainAll(config.xColumns);
        
        List<String> yColumns = new ArrayList<>(possibleColumns);
        yColumns.retainAll(config.yColumns);
        
        if (xColumns.isEmpty() || yColumns.isEmpty()) {
            return null;
        }
        
        return new HeatMapConfig(config.name, config.type, xColumns, yColumns);
    }
    
    private Map<BigDecimal, String> getColorThreshold() {
        Map<BigDecimal, String> colorThresholds = new TreeMap<>();
        List<AmpColorThreshold> thresholds = DbUtil.getColorThresholds();
        if (thresholds != null && !thresholds.isEmpty()) {
            for (AmpColorThreshold threshold : thresholds) {
                colorThresholds.put(threshold.getThresholdStart(), threshold.getColorHash());
            }
        }
        return colorThresholds;
    }
    
    /**
     * @return Heat Map Admin Settings
     */
    public AmpColorThresholdWrapper getHeatMapAdminSettings() {
        LOGGER.info("GET HeatMap Admin Settings");
        AmpColorThresholdWrapper result = new AmpColorThresholdWrapper();
        result.setAmountColors(DbUtil.getColorThresholds());
        return result;
    }

    /**
     * Updates HeatMapSettings with new configuration
     * @param config new thresholds
     */
    public void saveHeatMapAdminSettings(AmpColorThresholdWrapper config) {
        LOGGER.info("Save HeatMap Admin Settings");

        updateColorThresholds(config);
        if (!errors.isEmpty()) {
            throw new AmpWebApplicationException(Response.Status.BAD_REQUEST, errors);
        }
    }
    
    private void updateColorThresholds(AmpColorThresholdWrapper config) {
        List<AmpColorThreshold> newThresholds = config.getAmountColors();
        Map<BigDecimal, AmpColorThreshold> testUnique = new TreeMap<>();
        
        for (AmpColorThreshold threshold : newThresholds) {
            AmpColorThreshold ampColorThreshold = findAmpColorThreshold(threshold);
            BigDecimal amountFrom = getNewAmountFrom(threshold); 
            if (ampColorThreshold == null) {
                // so far not saving new, so if not found, report an error
                errors.addApiErrorMessage(DashboardErrors.INVALID_ID,
                        String.valueOf(threshold.getAmpColorThresholdId()));
            }
            if (amountFrom == null) {
                errors.addApiErrorMessage(DashboardErrors.INVALID_THRESHOLD, 
                        String.valueOf(threshold.getThresholdStart()));
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
                }
                testUnique.put(amountFrom, ampColorThreshold);
            }
        }
        if (!errors.isEmpty()) {
            org.digijava.module.aim.util.DbUtil.clearPendingChanges();
        }
    }
    
    private AmpColorThreshold findAmpColorThreshold(AmpColorThreshold threshold) {
        Long id = threshold.getAmpColorThresholdId();
        return id == null ? null : org.digijava.module.aim.util.DbUtil.getObjectOrNull(AmpColorThreshold.class, id);
    }
    
    private BigDecimal getNewAmountFrom(AmpColorThreshold threshold) {
        BigDecimal from = threshold.getThresholdStart();
        return from == null ? null : from.setScale(DEFAULT_SCALE, RoundingMode.HALF_EVEN);
    }

    private static List<String> getLocationsForHeatMap() {
        List<String> locationColumns = new ArrayList<>(LocationUtil.LOCATIONS_COLUMNS_NAMES);
        locationColumns.remove(ColumnConstants.COUNTRY);
        locationColumns.remove(ColumnConstants.LOCATION);
        return locationColumns;
    }
}
