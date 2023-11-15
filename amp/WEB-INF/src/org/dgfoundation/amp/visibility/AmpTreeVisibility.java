/**
 * @author dan
 *
 * 
 */
package org.dgfoundation.amp.visibility;

import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dan
 * 
 */
public class AmpTreeVisibility implements Serializable{


    private ConcurrentHashMap<String,AmpObjectVisibility> itemsCache=null;
    
    public ConcurrentHashMap<String, AmpObjectVisibility> getItemsCache() {
        return itemsCache;
    }

    public void setItemsCache(
            ConcurrentHashMap<String, AmpObjectVisibility> itemsCache) {
        this.itemsCache = itemsCache;
    }

    private AmpObjectVisibility root;

    // key-> name
    // object-> AmpTreeVisibility(template,module,feature,field)

    private ConcurrentHashMap<String, AmpTreeVisibility> items;

    private LinkedHashMap sorteditems;

    public Map<String, AmpTreeVisibility> getItems() {
        /*
         * TreeMap mySet=new TreeMap(FeaturesUtil.ALPHA_AMP_TREE_ORDER);
         * mySet.putAll(this.getItems()); LinkedHashMap sortedItems=new
         * LinkedHashMap(); sortedItems.putAll(mySet); return sortedItems;
         */
        return items;
    
    }

    public void setItems(ConcurrentHashMap<String, AmpTreeVisibility> items) {
        this.items = items;
    }

    public AmpObjectVisibility getRoot() {
        return root;
    }

    public void setRoot(AmpObjectVisibility root) {
        this.root = root;
    }

    public void initializeItemsCache(Collection<AmpObjectVisibility> items) {       
        itemsCache = new ConcurrentHashMap<>(items.size());
        for (AmpObjectVisibility ampObjectVisibility : items) {
            itemsCache.put(ampObjectVisibility.getName(), ampObjectVisibility);
        }
    }
    
    public AmpTreeVisibility() {
        super();
        items = new ConcurrentHashMap<>();
        sorteditems = new LinkedHashMap<>();
        // root=new AmpObjectVisibility();
        // TODO Auto-generated constructor stub
    }

    public boolean buildAmpTreeVisibilityMultiLevel(
            AmpObjectVisibility ampObjVis) {
        this.root = ampObjVis;
        this.setItems(new ConcurrentHashMap<>());
        boolean existSubmodules = false;
        for (Object o : ampObjVis.getAllItems()) {
            boolean notEmpty = false;
            AmpModulesVisibility module = (AmpModulesVisibility) o;
            AmpTreeVisibility moduleNode = new AmpTreeVisibility();
            if (!module.getSubmodules().isEmpty())
                existSubmodules = true;
            if (module.getParent() == null) {
                moduleNode.setRoot(module);
                moduleNode.setItems(new ConcurrentHashMap<>());
                getModuleTree(moduleNode);
                notEmpty = true;
            } else {
                existSubmodules = true;
                ;// ////System.out.println(" si NU are
                // submodule:::"+module.getName());
            }
            if (notEmpty)
                this.getItems().put(module.getName(), moduleNode);
        }
        return existSubmodules;
    }

    public void getModuleTree(AmpTreeVisibility moduleNode) {
        AmpModulesVisibility module = (AmpModulesVisibility) moduleNode
                .getRoot();
        if (module.getSubmodules().isEmpty()) {
            // getItems
            for (AmpObjectVisibility objectVisibility : module.getSortedAlphaItems()) {
                AmpFeaturesVisibility feature = (AmpFeaturesVisibility) objectVisibility;
                AmpTreeVisibility featureNode = new AmpTreeVisibility();
                featureNode.setRoot(feature);
                featureNode.setItems(new ConcurrentHashMap<>());
                // getItems
                for (AmpObjectVisibility ampObjectVisibility : feature.getSortedAlphaItems()) {
                    AmpFieldsVisibility field = (AmpFieldsVisibility) ampObjectVisibility;
                    AmpTreeVisibility fieldNode = new AmpTreeVisibility();
                    fieldNode.setRoot(field);
                    fieldNode.setItems(null);
                    featureNode.getItems().put(field.getName(), fieldNode);
                }
                moduleNode.getItems().put(feature.getName(), featureNode);
            }
            return;
        }

        for (Object o : module.getSortedAlphaSubModules()) {
            AmpModulesVisibility moduleAux = (AmpModulesVisibility) o;
            AmpTreeVisibility moduleNodeAux = new AmpTreeVisibility();
            moduleNodeAux.setRoot(moduleAux);
            {
                getModuleTree(moduleNodeAux);
            }
            moduleNode.getItems().put(moduleAux.getName(), moduleNodeAux);
        }
    }

    public void buildAmpTreeVisibility(AmpObjectVisibility ampObjVis) {
        // buildAmpTreeVisibilityMultiLevel(ampObjVis);
        this.root = ampObjVis;
        
        initializeItemsCache(root.getItems());
        
        this.setItems(new ConcurrentHashMap<>());
        if (ampObjVis.getAllItems() != null)
            if (ampObjVis.getAllItems().iterator() != null)
                for (Object o : ampObjVis.getAllItems()) {
                    AmpModulesVisibility module = (AmpModulesVisibility) o;
                    AmpTreeVisibility moduleNode = new AmpTreeVisibility();
                    moduleNode.setRoot(module);
                    if (module.getItems() != null) {
                        for (AmpObjectVisibility ampObjectVisibility : module.getItems()) {
                            AmpFeaturesVisibility feature = (AmpFeaturesVisibility) ampObjectVisibility;
                            AmpTreeVisibility featureNode = new AmpTreeVisibility();
                            featureNode.setRoot(feature);
                            if (feature.getItems() != null) {
                                for (AmpObjectVisibility objectVisibility : feature.getItems()) {
                                    AmpFieldsVisibility field = (AmpFieldsVisibility) objectVisibility;
                                    AmpTreeVisibility fieldNode = new AmpTreeVisibility();
                                    if (field.getDescription() == null || "".equals(field.getDescription()))
                                        field.setDescription(field.getName());
                                    fieldNode.setRoot(field);
                                    fieldNode.setItems(null);
                                    featureNode.getItems().put(field.getName(),
                                            fieldNode);
                                }
                            }
                            moduleNode.getItems().put(feature.getName(),
                                    featureNode);
                        }
                    }
                    this.getItems().put(module.getName(), moduleNode);
                }
        this.root = ampObjVis;
    }

    public void displayVisibilityTreeForDebug(AmpTreeVisibility atv) {
        for (AmpTreeVisibility obj : atv.getItems().values()) {
            displayRecTreeForDebug(obj);
        }

    }

    public void displayRecTreeForDebug(AmpTreeVisibility atv) {
        if (atv.getItems() == null)
            return;
        if (atv.getItems().isEmpty())
            return;
        for (AmpTreeVisibility auxTree : atv.getItems().values()) {
            displayRecTreeForDebug(auxTree);
        }
    }

    public boolean isVisibleObject(AmpObjectVisibility aov) {
        return this.getRoot().getTemplates().contains(aov.getId());
    }

    public boolean isVisibleId(Long id) {
        for (AmpTemplatesVisibility x : this.getRoot().getTemplates()) {

            if (x.getId().compareTo(id) == 0)
                return true;

        }
        return false;
    }

    public AmpFieldsVisibility getFieldByNameFromRoot(String fieldName) {

        for (AmpTreeVisibility module : this.getItems().values()) {
            for (AmpTreeVisibility feature : module.getItems().values()) {
                if (feature.getItems().containsKey(fieldName))
                    return (AmpFieldsVisibility) ((AmpTreeVisibility) feature
                            .getItems().get(fieldName)).getRoot();
            }
        }
        return null;
    }

    public AmpFeaturesVisibility getFeatureByNameFromRoot(String featureName) {
        for(AmpTreeVisibility obj : this.getItems().values()  ) {
            if (obj.getItems().containsKey(featureName))
                return (AmpFeaturesVisibility) obj
                        .getItems().get(featureName).getRoot();

        }
        return null;
    }

    public AmpFeaturesVisibility getFeatureByNameFromModule(String moduleName, String featureName) {
        AmpTreeVisibility module = this.getModuleTreeByNameFromRoot(moduleName);
        if (module != null && module.getItems().containsKey(featureName)) {
            return (AmpFeaturesVisibility) module
                    .getItems().get(featureName).getRoot();
        }
        return null;
    }

    public AmpTreeVisibility getFeatureTreeByNameFromRoot(String featureName) {
        for (AmpTreeVisibility module : this.getItems().values()) {
            if (module.getItems().containsKey(featureName))
                return module.getItems().get(featureName);

        }
        return null;
    }

    public AmpModulesVisibility getModuleByNameFromRoot(String moduleName) {
        
        AmpTreeVisibility atv   = this.getItems().get( moduleName );
        if ( atv != null ) {
            return (AmpModulesVisibility)atv.getRoot();
        }
        return null;
    }

    public AmpTreeVisibility getModuleTreeByNameFromRoot(String moduleName) {
        if (this.getItems().containsKey(moduleName))
            return this.getItems().get(moduleName);
        return null;
    }

    public LinkedHashMap getSorteditems() {
        TreeMap mySet = new TreeMap<>(items);
        sorteditems.putAll(mySet);
        return sorteditems;
    }
    
    public void setSorteditems(LinkedHashMap sorteditems) {
        this.sorteditems = sorteditems;
    }

}
