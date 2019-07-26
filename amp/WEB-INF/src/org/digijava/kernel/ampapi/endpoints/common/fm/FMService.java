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

/**
 * Feature Manager services that can be used by FM, menu and other endpoints 
 * @author Nadejda Mandrescu
 */
public class FMService {
    
    protected static final Logger logger = Logger.getLogger(FMService.class);
    
    /**
     *
     * @param config
     * @return fm settings
     */
    public static FMSettingsResult getFMSettingsResult(FMSettingsConfig config) {
        FMSettingsResult fmSettingsResult = new FMSettingsResult();
        
        if (config.isValid()) {
            if (config.getReportingFields()) {
                fmSettingsResult.setReportingFields(ColumnsVisibility.getConfigurableColumns());
            }
            
            if (config.getEnabledModules()) {
                fmSettingsResult.setEnabledModules(
                        FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES));
            }
            
            provideModulesDetails(fmSettingsResult, config);
        } else {
            fmSettingsResult.setError(String.format("Invalid modules details requested: %s. Allowed are: %s",
                    config.getDetailModules(), config.getAllowedModules()));
        }
        
        return fmSettingsResult;
    }
    
    private static void provideModulesDetails(FMSettingsResult fmSettingsResult, FMSettingsConfig config) {
        List<String> detailModules = config.getDetailModules();
        
        FMSettingsTree settingsTree = new FMSettingsTree();
        FMSettingsFlat settingsFlat = new FMSettingsFlat();
        
        if (detailModules == null || detailModules.isEmpty()) {
            return;
        }
        
        // check if all enabled modules are requested
        if (detailModules.contains(EPConstants.DETAIL_ALL_ENABLED_MODULES)) {
            detailModules = new ArrayList<>(FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES));
        }
        
        for (String module : detailModules) {
            boolean supportsFMTree = FMSettingsMediator.supportsFMTree(module);
            if (config.getDetailsFlat() || !supportsFMTree) {
                Set<String> entries = !supportsFMTree ? FMSettingsMediator.getEnabledSettings(module) :
                    getFmSettingsAsTree(module, config.getRequiredPaths())
                    .toFlattenedTree(config.getFullEnabledPaths());
                settingsFlat.getModules().put(module, entries);
            } else {
                FMTree fmTree = getFmSettingsAsTree(module, config.getRequiredPaths());
                settingsTree.getModules().putAll(fmTree.asFmSettingsTree(config.getFullEnabledPaths()).getModules());
            }
        }
    
        if (config.getDetailsFlat()) {
            fmSettingsResult.setFmSettings(settingsFlat);
        } else {
            fmSettingsResult.setFmSettings(settingsTree);
        }
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
