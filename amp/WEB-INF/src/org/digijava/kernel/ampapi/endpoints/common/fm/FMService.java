/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common.fm;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.FMSettingsMediator;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * Feature Manager services that can be used by FM, menu and other endpoints 
 * @author Nadejda Mandrescu
 */
public class FMService {
    protected static final Logger LOGGER = Logger.getLogger(FMService.class);
    
    /**
     * 
     * @param config
     * @return
     */
    public static JsonBean getFMSettings(JsonBean config) {
        JsonBean result = new JsonBean();
        try {
            String err = validate(config);
            
            if (err != null) {
                result.set(EPConstants.ERROR, err);
            } else {
                if (EndpointUtils.getSingleValue(config, EPConstants.REPORTING_FIELDS, Boolean.FALSE)) {
                    provideReportingFields(result);
                }
                if (EndpointUtils.getSingleValue(config, EPConstants.ENABLED_MODULES, Boolean.FALSE)) {
                    provideEnabledModules(result);
                }
                
                Boolean detailFlat = EndpointUtils.getSingleValue(config, EPConstants.DETAILS_FLAT, Boolean.TRUE);
                Boolean fullEnabledPaths = EndpointUtils.getSingleValue(config, EPConstants.FULL_ENABLED_PATHS, Boolean.TRUE);
                provideModulesDetails(result, EndpointUtils.getSingleValue(config, EPConstants.DETAIL_MODULES, 
                        new ArrayList<String>()), detailFlat, fullEnabledPaths);
            }
        } catch(Exception ex) {
            LOGGER.error("Unexpected error occurred while generating FM settings", ex);
            result.set(EPConstants.ERROR, ex.getMessage());
        }
        
        return result;
    }
    
    /**
     * 
     * @return
     */
    private static String validate(JsonBean config) {
        String err = null;
        List<String> detailModules = EndpointUtils.getSingleValue(config, EPConstants.DETAIL_MODULES, 
                new ArrayList<String>());
        Set<String> visibleModules = FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES);
        if (detailModules != null && !visibleModules.containsAll(detailModules)) {
            err = "Invalid modules details requested: " + detailModules 
                    + ". Allowed are: " + visibleModules;
        }
        return err;
    }
    
    /**
     * Adds reporting fields to the result
     * @param result
     */
    private static void provideReportingFields(JsonBean result) {
        result.set(EPConstants.REPORTING_FIELDS, ColumnsVisibility.getConfigurableColumns());
    }
    
    /**
     * Adds 
     * @param result
     */
    private static void provideEnabledModules(JsonBean result) {
        result.set(EPConstants.ENABLED_MODULES, FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES));
    }
    
    private static void provideModulesDetails(JsonBean result, List<String> detailModules, Boolean detailFlat,
            Boolean fullEnabledPaths) {
        if (detailModules == null || detailModules.size() == 0) return;
        
        // check if all enabled modules are requested
        if (detailModules.contains(EPConstants.DETAIL_ALL_ENABLED_MODULES)) {
            detailModules = new ArrayList<String>(FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES));
        }
        for (String module : detailModules) {
            boolean supportsFMTree = FMSettingsMediator.supportsFMTree(module);
            if (detailFlat || !supportsFMTree) {
                Set<String> entries = !supportsFMTree ? FMSettingsMediator.getEnabledSettings(module) :
                    FMSettingsMediator.getEnabledSettingsAsTree(module).toFlattenedTree(fullEnabledPaths);
                result.set(module, entries);
            } else {
                result.set(module, FMSettingsMediator.getEnabledSettingsAsTree(module).asJson(fullEnabledPaths));
            }
        }
    }
}
