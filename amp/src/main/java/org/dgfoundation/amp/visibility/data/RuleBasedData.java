/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import org.digijava.module.aim.dbentity.AmpVisibilityRule;

/**
 * @author Nadejda Mandrescu
 */
public interface RuleBasedData {
    
    /** 
     * Data visibility rule that will be checked to see if this data is enabled or not.
     * If no rule defined, that means it is visible.
     */
    AmpVisibilityRule  getRule();
    String  getName();
}
