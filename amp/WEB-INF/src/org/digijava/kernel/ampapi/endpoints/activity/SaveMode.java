package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * Used to express which save mode is required to save/validate an activity. It expresses intent much better than a
 * Boolean where null a value should mean neither submit or draft.
 *
 * @author Octavian Ciubotaru
 */
public enum SaveMode {
    SUBMIT,
    DRAFT
}
