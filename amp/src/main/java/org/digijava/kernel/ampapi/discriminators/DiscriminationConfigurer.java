package org.digijava.kernel.ampapi.discriminators;

/**
 * Restore discrimination value during deserialize.
 *
 * @author Octavian Ciubotaru
 */
public interface DiscriminationConfigurer {

    /**
     * Given the object, set the discrimination field to the correct value.
     */
    void configure(Object obj, String fieldName, String discriminationValue);
}
