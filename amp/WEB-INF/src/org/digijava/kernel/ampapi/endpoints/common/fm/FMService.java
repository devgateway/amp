/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common.fm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.FMSettingsMediator;
import org.dgfoundation.amp.visibility.data.FMTree;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.FMSettingsConfig;
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
    public static Map<String, Object> getFMSettings(FMSettingsConfig config) {
        Map<String, Object> fmSettingsResult = new HashMap<>();
        
        try {
            if (config.isValid()) {
                if (config.getReportingFields()) {
                    fmSettingsResult.put(EPConstants.REPORTING_FIELDS, ColumnsVisibility.getConfigurableColumns());
                }
                
                if (config.getEnabledModules()) {
                    fmSettingsResult.put(EPConstants.ENABLED_MODULES,
                            FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES));
                }
    
                provideModulesDetails(fmSettingsResult, config);
            } else {
                fmSettingsResult.put(EPConstants.ERROR, "Invalid modules details requested: "
                        + config.getDetailModules() + ". Allowed are: " + config.getAllowedModules());
            }
        } catch(Exception ex) {
            LOGGER.error("Unexpected error occurred while generating FM settings", ex);
            fmSettingsResult.put(EPConstants.ERROR, ex.getMessage());
        }
        
        return fmSettingsResult;
    }
    
    private static void provideModulesDetails(Map<String, Object> fmSettingsResult, FMSettingsConfig config) {
        List<String> detailModules = config.getDetailModules();
        if (detailModules == null || detailModules.size() == 0) {
            return;
        }
        
        // check if all enabled modules are requested
        if (detailModules.contains(EPConstants.DETAIL_ALL_ENABLED_MODULES)) {
            detailModules = new ArrayList<String>(FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES));
        }
        
        for (String module : detailModules) {
            boolean supportsFMTree = FMSettingsMediator.supportsFMTree(module);
            if (config.getDetailsFlat() || !supportsFMTree) {
                Set<String> entries = !supportsFMTree ? FMSettingsMediator.getEnabledSettings(module) :
                    getFmSettingsAsTree(module, config.getRequiredPaths())
                    .toFlattenedTree(config.getFullEnabledPaths());
                fmSettingsResult.put(module, entries);
            } else {
                FMTree fmTree = getFmSettingsAsTree(module, config.getRequiredPaths());
                fmSettingsResult.put(module, getTreeValue(module, fmTree, config.getFullEnabledPaths()));
            }
        }
    }
    
    public static Object getTreeValue(String module, FMTree fmTree, Boolean fullEnabledPaths) {
        JsonBean fmTreeJson = fmTree.asJson(fullEnabledPaths);
        
        Map.Entry<String, Object> entry = fmTreeJson.any().entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().equals(module.toLowerCase()))
                .findAny().get();
        
        return entry.getValue();
    }
    
    /**
     * Get FM entries as a tree structure. If filter is specified and non-empty then FM entries will be filtered.
     * @param module for which to return FM entries
     * @param requiredPaths required FM paths, optional
     * @return FM entries as a tree
     */
    private static FMTree getFmSettingsAsTree(String module, List<String> requiredPaths) {
        FMTree tree = FMSettingsMediator.getEnabledSettingsAsTree(module);
        if (requiredPaths != null && !requiredPaths.isEmpty()) {
            tree = filter(tree, requiredPaths);
        }
        return tree;
    }

    /**
     * Returns a sub tree of the original tree after applying filtering.
     * @param tree to be filtered
     * @param requiredPaths paths that are required to be left in filtered tree
     * @return filtered tree
     */
    static FMTree filter(FMTree tree, List<String> requiredPaths) {
        return filter("", tree, p -> p.equals("") || requiredPaths.contains(p));
    }

    private static FMTree filter(String prefix, FMTree tree, Predicate<String> filter) {
        Map<String, FMTree> entries = new HashMap<>();
        tree.getEntries().forEach((k, v) -> {
            FMTree subTree = filter(prefix + "/" + k, v, filter);
            if (subTree != null) {
                entries.put(k, subTree);
            }
        });
        if (!entries.isEmpty() || filter.test(prefix)) {
            return new FMTree(entries, tree.isEnabled());
        } else {
            return null;
        }
    }
}
