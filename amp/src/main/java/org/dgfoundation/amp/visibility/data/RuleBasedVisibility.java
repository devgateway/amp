/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpVisibilityRuleType;
import org.digijava.module.aim.dbentity.AmpVisibilityRule;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.VisibilityUtil;

/**
 * A rule based visibility base class
 * @author Nadejda Mandrescu
 */
public abstract class RuleBasedVisibility extends DataVisibility implements FMSettings {
    
    protected abstract Class<? extends RuleBasedData> getClazz();
    
    @Override
    public Set<String> getEnabledSettings(Long templateId) {
        return getVisibleData(templateId);
    }
    
    @Override
    protected Set<String> detectVisibleData(Long templateId) {
        List<? extends RuleBasedData> ruleBasedData = VisibilityUtil.getDataWithVisibilityRule(getClazz());

        // these are visible by default
        Set<String> visibleData = new HashSet<String>(VisibilityUtil.getDataNamesWithoutVisibilityRule(getClazz()));
        // add other in case some custom list is defined
        visibleData.addAll(getVisibleByDefault());
        
        for (RuleBasedData rbd : ruleBasedData) {
            if (isVisibleByRule(rbd.getRule())) {
                visibleData.add(rbd.getName());
            }
        }
        
        return visibleData;
    }
    
    /**
     * Test if the given visibility rule is satisfied 
     * @param rule visibility rule to test
     * @return true if the rule is satisfied, false otherwise
     */
    protected boolean isVisibleByRule(AmpVisibilityRule rule) {
        // consider testing for Any visibility enabled for both ANY and NONE visibility rules
        boolean isAny = !AmpVisibilityRuleType.ALL.equals(rule.getType());
        // if nothing defined, then will be visible by default
        boolean visible = true;
        
        // check children rules if any
        if (rule.getChildren() != null && rule.getChildren().size() > 0) {
            visible = isVisible(rule.getChildren(), isAny);
        } else {
            // check Objects visibility
            List<Set<? extends AmpObjectVisibility>> visibilityObjectsList = Arrays.asList(
                    rule.getModules(), rule.getFeatures(), rule.getFields());
            visible = isVisible(visibilityObjectsList, isAny);
        }
        
        if (AmpVisibilityRuleType.NONE.equals(rule.getType())) {
            /* we were testing ANY visibility for NONE rule type
             * => if any is visible, then this rule is negative and vice-versa
             */
            visible = !visible;
        }
        
        return visible;
    }
    
    /**
     * Tests a list of visibility objects by the given rule: ANY or ALL objects in the list must be visible  
     * @param visibilityObjects
     * @param isAny
     * @return if any/all objects are visible
     */
    protected boolean isVisible(Collection<?> visibilityObjects, boolean isAny) {
        boolean visible = true;
        for (Iterator<?> iter = visibilityObjects.iterator(); iter.hasNext() && (isAny || visible); ) {
            Object o = iter.next();
            if (o instanceof Collection && (o == null || ((Collection<?>) o).size() == 0)) {
                // do not consider empty collection as a positive test for "ANY" rule type 
                continue;
            }
            visible = isVisibleData(o, isAny);
            if (isAny && visible) {
                break;
            }
        }
        return visible;
    }
    
    protected boolean isVisibleData(Object o, boolean isAny) {
        if (o instanceof AmpObjectVisibility) {
            return FeaturesUtil.isVisible((AmpObjectVisibility) o, null);
        }
        if (o instanceof AmpVisibilityRule) {
            return isVisibleByRule((AmpVisibilityRule) o);
        }
        if (o instanceof Collection) {
            return isVisible((Collection<?>) o, isAny);
        }
        return false;
    }
    
    /******************************************************
     * UNUSED 
     *******************************************************/
    
    @Override
    protected List<String> getVisibleByDefault() {
        return noDataList;
    }

    @Override
    protected Map<String, String> getDataMap(DataMapType dataMapType) {
        return noDataMap;
    }

    @Override
    protected Map<String, Collection<String>> getDependancyMapTypeAny() {
        return noDataCollectionMap;
    }

    @Override
    protected Map<String, Collection<String>> getDependancyMapTypeAll() {
        return noDataCollectionMap;
    }

    @Override
    protected Set<String> getAllData() {
        return noDataSet;
    }
    
}
