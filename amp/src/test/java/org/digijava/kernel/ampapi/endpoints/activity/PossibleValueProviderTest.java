package org.digijava.kernel.ampapi.endpoints.activity;

/**
 * @author Nadejda Mandrescu
 */
public interface PossibleValueProviderTest extends PossibleValuesProvider {

    default boolean isAllowed(Long id) {
        return true;
    }

}
