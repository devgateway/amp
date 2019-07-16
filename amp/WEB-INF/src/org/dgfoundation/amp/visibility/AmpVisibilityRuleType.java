/**
 * 
 */
package org.dgfoundation.amp.visibility;

import java.io.Serializable;

/**
 * Visibility rule types
 * @author Nadejda Mandrescu
 */
public enum AmpVisibilityRuleType implements Serializable {
    /** visible if ANY dependent object is enabled, e.g. a dependent FM module is enabled */
    ANY,
    /** visible if ALL dependent objects are enabled, e.g. if all dependent FM features are enabled */
    ALL,
    /** visible if NONE dependent objects are enabled, e.g. if all dependent FM features are disabled */
    NONE;
}
