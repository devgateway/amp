package org.digijava.kernel.validation;

/**
 * Value context when translations are not applicable.
 *
 * @author Octavian Ciubotaru
 */
public class NotTranslatedValueContext implements TranslatedValueContext {

    NotTranslatedValueContext() {
    }

    @Override
    public String getLang() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getValue(String lang) {
        throw new UnsupportedOperationException();
    }
}
