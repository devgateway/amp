package org.dgfoundation.amp.onepager.util;

/**
 * @author Octavian Ciubotaru
 */
public enum ActivitySource {
    /**
     * Change coming from UI activity form.
     */
    ACTIVITY_FORM,

    /**
     * Change coming from API.
     */
    API,

    /**
     * Change performed by a background job.
     */
    JOB,

    /**
     * Change performed by a background PATCH.
     */
    PATCH
}
