/**
 * 
 */
package org.dgfoundation.amp.visibility.data;

import java.util.Set;

/**
 * Feature Manager Settings Interface to follow to be included in 
 * {@link FMSettingsMediator} automatic processing   
 * @author Nadejda Mandrescu
 */
public interface FMSettings {
    
    /** @return a set of enabled FM settings */
    Set<String> getEnabledSettings();
    
    default FMTree getEnabledSettingsAsFMTree() {
        throw new RuntimeException("Not implemented");
    }
    
    default boolean supportsFMTree() {
        return false;
    }
}
