/**
 *
 */
package org.digijava.kernel.ampapi.endpoints.common.fm;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.visibility.data.ColumnsVisibility;
import org.dgfoundation.amp.visibility.data.FMSettingsMediator;
import org.dgfoundation.amp.visibility.data.FMTree;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.endpoints.common.FMSettingsConfig;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.util.FeaturesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Feature Manager services that can be used by FM, menu and other endpoints
 * @author Nadejda Mandrescu
 */
public class FMService {

    protected static final Logger logger = Logger.getLogger(FMService.class);

    public static FMSettingsResult getFMSettingsResult(FMSettingsConfig config) {
        //there is no point in using a null template id
            return getFMSettingsResult(config, FeaturesUtil.getCurrentTemplateId());
    }

    /**
     *
     * @param config
     * @return
     */
    public static List<FMMemberSettingsResult> getFMSettingsResultGroupedByWsMember(FMSettingsConfig config) {
        List<FMMemberSettingsResult> fmTrees = new ArrayList<>();

        Map<Long, List<Long>> fmTreesWsMap = getFMTreeWsMap();

        fmTreesWsMap.entrySet().forEach(fmTree -> {
            FMMemberSettingsResult memberSettingsResult = new FMMemberSettingsResult();
            memberSettingsResult.setWsMemberIds(fmTree.getValue());
            memberSettingsResult.setFmTree(getFMSettingsResult(config, fmTree.getKey()));
            fmTrees.add(memberSettingsResult);
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

    /**s
     *
     * @param config
     * @return fm settings
     */
    public static FMSettingsResult getFMSettingsResult(FMSettingsConfig config, Long templateId) {
        FMSettingsResult fmSettingsResult = new FMSettingsResult();

        if (config.isValid(templateId)) {
            if (config.getReportingFields()) {
                fmSettingsResult.setReportingFields(ColumnsVisibility.getConfigurableColumns());
            }

            if (config.getEnabledModules()) {
                fmSettingsResult.setEnabledModules(
                        FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES, templateId));
            }

            provideModulesDetails(fmSettingsResult, config, templateId);
        } else {
            fmSettingsResult.setError(String.format("Invalid modules details requested: %s. Allowed are: %s",
                    config.getDetailModules(), config.getAllowedModules(templateId)));
        }

        return fmSettingsResult;
    }

    private static void provideModulesDetails(FMSettingsResult fmSettingsResult, FMSettingsConfig config,
                                              Long templateId) {
        List<String> detailModules = config.getDetailModules();

        FMSettingsTree settingsTree = new FMSettingsTree();
        FMSettingsFlat settingsFlat = new FMSettingsFlat();

        if (detailModules == null || detailModules.isEmpty()) {
            return;
        }

        // check if all enabled modules are requested
        if (detailModules.contains(EPConstants.DETAIL_ALL_ENABLED_MODULES)) {
            detailModules = new ArrayList<>(
                    FMSettingsMediator.getEnabledSettings(FMSettingsMediator.FMGROUP_MODULES, templateId));
        }

        for (String module : detailModules) {
            boolean supportsFMTree = FMSettingsMediator.supportsFMTree(module, templateId);
            if (config.getDetailsFlat() || !supportsFMTree) {
                Set<String> entries = !supportsFMTree ? FMSettingsMediator.getEnabledSettings(module, templateId)
                        : getFmSettingsAsTree(module, config.getRequiredPaths(), templateId)
                            .toFlattenedTree(config.getFullEnabledPaths());
                settingsFlat.getModules().put(module, entries);
            } else {
                FMTree fmTree = getFmSettingsAsTree(module, config.getRequiredPaths(), templateId);
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
