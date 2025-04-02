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
    
    /** @return a set of enabled FM settings based on templateId*/
    Set<String> getEnabledSettings(Long templateId);

    /**
     * @return return all possible FM settings
     */
    default Set<String> getSettings() {
        throw new RuntimeException("Not implemented");
    }
    
    default FMTree getEnabledSettingsAsFMTree() {
        throw new RuntimeException("Not implemented");
    }
    
    default boolean supportsFMTree() {
        return false;
    }
}
