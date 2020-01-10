package org.digijava.kernel.validation;

import java.util.Map;

/**
 * Value context when translations are not applicable.
 *
 * @author Octavian Ciubotaru
 */
public class NotTranslatedValueContext extends TranslatedValueContext {

    public NotTranslatedValueContext(TranslationContext translationContext) {
        super(translationContext);
    }

    @Override
    public String getLang() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getValue(String lang) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getValues() {
        throw new UnsupportedOperationException();
    }
}
