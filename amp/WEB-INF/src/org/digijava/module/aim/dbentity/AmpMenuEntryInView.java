/**
 * 
 */
package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.visibility.data.RuleBasedData;

/**
 * @author Nadejda Mandrescu
 */

public abstract class AmpMenuEntryInView extends AmpMenuEntry implements RuleBasedData {
    
    private Long menuViewId;
    private AmpView viewType;
    private AmpVisibilityRule rule;
    
    /**
     * @return the menuViewId
     */
    public Long getMenuViewId() {
        return menuViewId;
    }

    /**
     * @param menuViewId the menuViewId to set
     */
    public void setMenuViewId(Long menuViewId) {
        this.menuViewId = menuViewId;
    }

    /**
     * @return the view
     */
    public AmpView getViewType() {
        return viewType;
    }

    /**
     * @param view the view to set
     */
    public void setViewType(AmpView viewType) {
        this.viewType = viewType;
    }

    /**
     * @return the rule
     */
    public AmpVisibilityRule getRule() {
        return rule;
    }

    /**
     * @param rule the rule to set
     */
    public void setRule(AmpVisibilityRule rule) {
        this.rule = rule;
    }
    /*
    @Override
    public boolean equals(Object o) {
        return o != null && ((AmpMenuEntryInView) o).getId().equals(this.getId()) && ((AmpMenuEntryInView) o).getViewType().equals(viewType);
    }
    
    @Override
    public int hashCode() {
        return (int) (this.getId() * 10 + viewType.ordinal()); 
    }*/
}

