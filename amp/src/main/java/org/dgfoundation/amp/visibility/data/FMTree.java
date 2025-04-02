/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.common.fm.FMSettingsTree;

/**
 * A simple representation of an FM section in a tree structure
 * 
 * @author Nadejda Mandrescu
 */
public class FMTree {
    
    private boolean enabled;
    private Map<String, FMTree> entries;
    
    public FMTree(Map<String, FMTree> entries, boolean enabled) {
        this.enabled = enabled;
        this.entries = entries == null ? Collections.emptyMap() : entries;
    }
    
    /**
     * Transforsm the FM tree to a flattened tree like
     * <pre>
     *      /Activity Form/Organiation
     *      /Activity Form/Organiation/Donor Organization
     *      ...
     * </pre>
     * @param fullEnabledPaths if set to true, then it will show only enabled entries that has all ancestors enabled.
     * <pre>
     * E.g. /Activity Form/Organiation/Donor Organization
     * 
     * Otherwise will append for each path section it's status, e.g.:
     *      
     *      /Activity Form[true]/Organiation[false]/Donor Organization[true]
     *      
     * This is to support historical FM usage when some modules check only the feature or fields status, 
     * no matter if the parent feature/module is disabled. 
     * </pre>  
     * @return flattened list
     */
    public Set<String> toFlattenedTree(boolean fullEnabledPaths) {
        Set<String> flattened = new LinkedHashSet<>();
        if (!fullEnabledPaths || this.enabled) {
            for (Entry<String, FMTree> entry : entries.entrySet()) {
                FMTree value = entry.getValue();
                if (!fullEnabledPaths || value.enabled) {
                    String suffix = fullEnabledPaths ? "" : "[" + value.enabled + "]"; 
                    String currentPath = "/" + entry.getKey() + suffix;   
                    Set<String> children = value == null ? null : value.toFlattenedTree(fullEnabledPaths);
                    if (children == null || children.isEmpty()) {
                        // this is a leaf
                        flattened.add(currentPath);
                    } else {
                        for(String childPath : children) {
                            flattened.add(currentPath + childPath);
                        }
                    }
                }
            }
        }
        return flattened;
    }   
    
    /**
     * Transforms FM tree to a FMSettingsTree
     * @param fullEnabledPaths if true, then each level will have at a minimum "__enabled" (true/false) status
     * @return Map<String, Object> tree
     * <pre>
     * "REPORTING": {
     *      "__enabled" : true, // omitted if fullEnabledPaths are requested (same below) 
     *      "Measures": {
     *          "__enabled" : true,
     *          "Actual Disbursements": {
     *              "__enabled" : true
     *          },
     *          ...
     *       },
     *       ...
     *  }
     * </pre>
     */
    public FMSettingsTree asFmSettingsTree(boolean fullEnabledPaths) {
        FMSettingsTree fmSettingsTree = new FMSettingsTree();
        if (!fullEnabledPaths || this.enabled) {
            if (!fullEnabledPaths) {
                fmSettingsTree.setEnabled(this.enabled);
            }
            Map<String, FMSettingsTree> modules = new HashMap<>();
            for (Entry<String, FMTree> entry : entries.entrySet()) {
                FMTree value = entry.getValue();
                if (!fullEnabledPaths || value.enabled) {
                    modules.put(entry.getKey(), value.asFmSettingsTree(fullEnabledPaths));
                }
            }
            fmSettingsTree.setModules(modules);
        }
            
        return fmSettingsTree;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Map<String, FMTree> getEntries() {
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FMTree fmTree = (FMTree) o;
        return enabled == fmTree.enabled && Objects.equals(entries, fmTree.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, entries);
    }
}
