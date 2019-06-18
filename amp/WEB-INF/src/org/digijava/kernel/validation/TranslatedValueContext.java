package org.digijava.kernel.validation;

/**
 * A context object that allows to retrieve the value for a specific language.
 *
 * @author Octavian Ciubotaru
 */
public interface TranslatedValueContext {

    /**
     * Return the language which is important for validation purposes.
     *
     * @return language code
     */
    String getLang();

    /**
     * Return the value for a specific language.
     *
     * @param language language code
     * @return value in the requested language or null
     */
    String getValue(String language);
}
