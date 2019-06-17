package org.digijava.kernel.validation;

import static java.util.Objects.requireNonNull;

import org.digijava.module.aim.dbentity.AmpContentTranslation;

/**
 * Used for translatable fields tha are persisted in {@link AmpContentTranslation}.
 *
 * @author Octavian Ciubotaru
 */
public class ContentTranslatedValueContext implements TranslatedValueContext {

    private TranslationContext translationContext;

    private String objectClass;
    private Long objectId;
    private String fieldName;

    ContentTranslatedValueContext(TranslationContext translationContext, String objectClass, Long objectId,
            String fieldName) {
        this.translationContext = translationContext;
        this.objectClass = requireNonNull(objectClass);
        this.objectId = requireNonNull(objectId);
        this.fieldName = requireNonNull(fieldName);
    }

    @Override
    public String getLang() {
        return translationContext.getLanguage();
    }

    @Override
    public String getValue(String lang) {
        return translationContext.getContentTranslation(objectClass, objectId, fieldName).stream()
                .filter(ct -> ct.getLocale().equals(lang) && ct.getTranslation() != null)
                .map(AmpContentTranslation::getTranslation)
                .findAny()
                .orElse(null);
    }
}
