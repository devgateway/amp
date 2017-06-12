/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Generic Visibility that simply reflects FM as is, no custom rules
 * 
 * @author Nadejda Mandrescu
 */
public class GenericVisibility extends DataVisibility implements FMSettings {
    protected static final Logger LOGGER = Logger.getLogger(GenericVisibility.class);
            
    private String groupName;
    private volatile FMTree tree;
    private AmpTemplatesVisibility template;
    
    protected GenericVisibility(String groupName) {
        this.groupName = groupName;
        this.template = FeaturesUtil.getCurrentTemplate();
        this.init();
    }
    
    private void init() {
        // initialize as a tree, so that it can be reused if later requested as tree
        // TODO: will it happen to request both as tree and flattened for the same module?
        this.tree = getTree();
    }
    
    private void reInit() {
        // just benefiting from notifications that visibility is changed, but we don't need atomic update
        if (this.atomicVisibilityChanged.get() || tree == null) {
            // force all simultaneous threads to wait until fully reinitialized
            tree = null;
            this.atomicVisibilityChanged.set(false);
            synchronized(groupName) {
                if (tree == null) {
                    init();
                }
            }
        }
    }

    @Override
    public Set<String> getEnabledSettings() {
        reInit();
        return getEnabledSettingsAsFMTree().toFlattenedTree(true);
    }
    
    @Override
    public FMTree getEnabledSettingsAsFMTree() {
        reInit();
        return tree;
    }
    
    private FMTree getTree() {
        FMTree tree = null;
        String moduleRoot = null;
        boolean enabled = false;
        AmpTreeVisibility vTree = FeaturesUtil.getCurrentAmpTreeVisibility();
        if (vTree != null) {
            vTree = vTree.getModuleTreeByNameFromRoot(groupName);
        }
        if (vTree != null) {
            moduleRoot = StringUtils.strip(vTree.getRoot().getName(), "/");
            enabled = ((AmpModulesVisibility) vTree.getRoot()).isVisibleId(template.getId());
            tree = buildTreeByModules((AmpModulesVisibility) vTree.getRoot());
        }
        if (tree == null) {
            LOGGER.warn(String.format("FM group '%s' is either disabled or not present, the FM tree will be empty",
                    groupName));
        }
        Map<String, FMTree> rootItems = new LinkedHashMap<>();
        rootItems.put(moduleRoot, tree);
        return new FMTree(rootItems, enabled);
    }
    
    private FMTree buildTreeByModules(AmpModulesVisibility modulesVisibility) {
        String parentPath = modulesVisibility.getName();
        
        Set<AmpModulesVisibility> submodules = modulesVisibility.getSortedAlphaSubModules();
        Map<String, FMTree> items = new LinkedHashMap<>();
        // build modules tree
        if (submodules != null && !submodules.isEmpty()) {
            for (AmpModulesVisibility mv : submodules) {
                String treePath = mv.getName();
                if (treePath.startsWith(parentPath)) {
                    treePath = treePath.substring(parentPath.length());
                }
                items.put(StringUtils.strip(treePath, "/"), buildTreeByModules(mv));
            }
        }
        // build features tree
        for (AmpObjectVisibility feature : modulesVisibility.getItems()) {
            items.put(feature.getName(), buildFeaturesTree((AmpFeaturesVisibility) feature));
        }
        return new FMTree(items, FeaturesUtil.isVisible(modulesVisibility));
    }

    private FMTree buildFeaturesTree(AmpFeaturesVisibility feature) {
        Map<String, FMTree> items = new LinkedHashMap<>();
        for (AmpObjectVisibility field : feature.getItems()) {
            items.put(field.getName(), new FMTree(null, FeaturesUtil.isVisible(field)));
        }
        return new FMTree(items, FeaturesUtil.isVisible(feature));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean supportsFMTree() {
        return true;
    }
    
    @Override
    protected List<String> getVisibleByDefault() {
        return null;
    }

    @Override
    protected Set<String> getAllData() {
        return null;
    }

    @Override
    protected Map<String, String> getDataMap(DataMapType dataMapType) {
        return null;
    }

    @Override
    protected Map<String, Collection<String>> getDependancyMapTypeAny() {
        return null;
    }

    @Override
    protected Map<String, Collection<String>> getDependancyMapTypeAll() {
        return null;
    }

}
