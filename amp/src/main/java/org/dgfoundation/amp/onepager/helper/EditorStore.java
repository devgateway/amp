package org.dgfoundation.amp.onepager.helper;

import java.io.Serializable;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

/**
 * Store for editors to help with propagating the link between old keys and new keys + values for new keys
 * This is useful for versioning, to be able to clone the translations.
 *
 * @author aartimon@developmentgateway.org
 */
public class EditorStore implements Serializable {

    /**
     * map between new editor key and old editor key, in order to be able to copy values in other languages
     */
    private Map<String, String> oldKey;

    /**
     * map between new editor key and editor body from the form
     */
    private Map<String, Map<String, String>> values;
    
    public EditorStore() {
        oldKey = new TreeMap<>();
        values = new TreeMap<>();
    }

    public Map<String, String> getOldKey() {
        return oldKey;
    }

    public Map<String, Map<String, String>> getValues() {
        return values;
    }
    @Override
    public String toString() {
        return new StringJoiner(", ", EditorStore.class.getSimpleName() + "[", "]")
                .add("oldKey=" + oldKey)
                .add("values=" + values)
                .toString();
    }
}
