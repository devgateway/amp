/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.common.fm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.FMSettingsMediator;
import org.dgfoundation.amp.visibility.data.FMTree;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.EndpointUtils;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.persistence.PersistenceManager;

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
    public static JsonBean getCurrentUserFMSettings(JsonBean config) {
        return getFMSettings(config);
    }
    
    /**
     * 
     * @param config
     * @return
     */
    public static List<JsonBean> getFMSettingsGroupedByWsMember(JsonBean config) {
        List<JsonBean> fmTrees = new ArrayList<>();
        
        Map<Long, List<Long>> fmTreesWsMap = getFMTreeWsMap();

        fmTreesWsMap.entrySet().forEach(fmTree -> {
            JsonBean result = new JsonBean();
            result.set(EPConstants.WS_MEMBER_IDS, fmTree.getValue());
            result.set(EPConstants.FM_TREE, getFMSettings(config, fmTree.getKey()));
            fmTrees.add(result);
        });
        
        return fmTrees;
    }
    
    
    public static Map<Long, List<Long>> getFMTreeWsMap() {
        String wsQuery = "SELECT amp_team_id, COALESCE(fm_template, (SELECT settingsvalue FROM amp_global_settings "
                + "WHERE settingsname = 'Visibility Template')::bigint) AS fm_template FROM amp_team";
        
        PersistenceManager.getSession().doReturningWork(connection -> SQLUtils.collectKeyValue(connection, wsQuery));
        Map<Long, Long> valsA = PersistenceManager.getSession()
                .doReturningWork(connection -> SQLUtils.collectKeyLongValue(connection, wsQuery));
        
        Map<Long, List<Long>> vals = valsA.entrySet().stream().collect(Collectors.groupingBy(Entry::getValue, 
                    Collectors.mapping(Entry::getKey, Collectors.toList())));

        return vals;
    }
    
    public static JsonBean getFMSettings(JsonBean config) {
        return getFMSettings(config, null);
    }

    /**
     * 
     * @param config
     * @return
     */
    public static JsonBean getFMSettings(JsonBean config, Long templateId) {
        JsonBean result = new JsonBean();
        try {
            Boolean fullEnabledPaths = EndpointUtils.getSingleValue(config, EPConstants.FULL_ENABLED_PATHS,
                    Boolean.TRUE);

            String err = validate(config, fullEnabledPaths, templateId);
            
            if (err != null) {
                result.set(EPConstants.ERROR, err);
            } else {
                if (EndpointUtils.getSingleValue(config, EPConstants.REPORTING_FIELDS, Boolean.FALSE)) {
                    result.set(EPConstants.REPORTING_FIELDS, ColumnsVisibility.getConfigurableColumns(templateId));
                }
                
                if (EndpointUtils.getSingleValue(config, EPConstants.ENABLED_MODULES, Boolean.FALSE)) {
                    result.set(EPConstants.ENABLED_MODULES, 
                            FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES, templateId));
                }
                
                Boolean detailFlat = EndpointUtils.getSingleValue(config, EPConstants.DETAILS_FLAT, Boolean.TRUE);
                List<String> requiredPaths = (List) config.get(EPConstants.FM_PATHS_FILTER);
                
                provideModulesDetails(result, EndpointUtils.getSingleValue(config, EPConstants.DETAIL_MODULES, 
                        new ArrayList<String>()), detailFlat, fullEnabledPaths, requiredPaths, templateId);
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
    private static String validate(JsonBean config, Boolean fullEnabledPaths, Long templateId) {
        String err = null;
        List<String> requestedModules = EndpointUtils.getSingleValue(config, EPConstants.DETAIL_MODULES,
                new ArrayList<String>());
        Set<String> allowedModules;
        if (fullEnabledPaths) {
            allowedModules = FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES, templateId);
        } else {
            allowedModules = FMSettingsMediator.getSettings(FMSettingsMediator.FMGROUP_MODULES, templateId);
        }
        if (requestedModules != null && !allowedModules.containsAll(requestedModules)) {
            err = "Invalid modules details requested: " + requestedModules
                    + ". Allowed are: " + allowedModules;
        }
        return err;
    }
    
    private static void provideModulesDetails(JsonBean result, List<String> detailModules, Boolean detailFlat,
            Boolean fullEnabledPaths, List<String> requiredPaths, Long templateId) {
        if (detailModules == null || detailModules.size() == 0) return;
        
        // check if all enabled modules are requested
        if (detailModules.contains(EPConstants.DETAIL_ALL_ENABLED_MODULES)) {
            detailModules = new ArrayList<String>(
                    FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES, templateId));
        }
        for (String module : detailModules) {
            boolean supportsFMTree = FMSettingsMediator.supportsFMTree(module, templateId);
            if (detailFlat || !supportsFMTree) {
                Set<String> entries = !supportsFMTree ? FMSettingsMediator.getEnabledSettings(module, templateId) 
                        : getFmSettingsAsTree(module, requiredPaths, templateId).toFlattenedTree(fullEnabledPaths);
                result.set(module, entries);
            } else {
                result.set(module, getFmSettingsAsTree(module, requiredPaths, templateId).asJson(fullEnabledPaths));
            }
        }
    }

    /**
     * Get FM entries as a tree structure. If filter is specified and non-empty then FM entries will be filtered.
     * @param module for which to return FM entries
     * @param requiredPaths required FM paths, optional
     * @return FM entries as a tree
     */
    private static FMTree getFmSettingsAsTree(String module, List<String> requiredPaths, Long templateId) {
        FMTree tree = FMSettingsMediator.getEnabledSettingsAsTree(module, templateId);
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
