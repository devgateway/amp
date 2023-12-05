package org.dgfoundation.amp.onepager.helper;

import java.util.HashMap;
import java.util.List;

/**
 * Contains the list of translations (in different languages) for each field of the Resource
 * 
 * @author user
 *
 */
public class ResourceTranslationStore {

    
    private HashMap <String, List<ResourceTranslation>> resourceFieldTranslations;
    
    
    public ResourceTranslationStore () {
        resourceFieldTranslations = new HashMap<String,List<ResourceTranslation>> ();
    }


    public HashMap<String, List<ResourceTranslation>> getResourceFieldTranslations() {
        return resourceFieldTranslations;
    }
    
    
 }
