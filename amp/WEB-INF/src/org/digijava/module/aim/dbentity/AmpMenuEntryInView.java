/**
 * 
 */
package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.menu.AmpView;
import org.dgfoundation.amp.visibility.data.RuleBasedData;

/**
 * @author Nadejda Mandrescu
 */
public class AmpMenuEntryInView extends AmpMenuEntry implements RuleBasedData {
	
	private AmpView viewType;
	private AmpVisibilityRule rule;
	
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
}

