package org.digijava.kernel.ampapi.endpoints.common.values;


/**
 * These source options are used during import field value validation.
 * For now having three options for testing purpose.
 * 
 * @author Nadejda Mandrescu
 */
public enum PossibleValueSource {
    POSSIBLE_VALUES_CACHE,
    POSSIBLE_VALUES_IDS_CACHE,
    DIRECT_SOURCE;

    public static PossibleValueSource get(int id) {
        switch (id) {
        case 0: return POSSIBLE_VALUES_CACHE;
        case 1: return POSSIBLE_VALUES_IDS_CACHE;
        case 2: return DIRECT_SOURCE;
        default: return null;
        }
    }
}
