package org.digijava.kernel.validation;

import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;

import java.util.Map;

/**
 * Used for fields that are persisted in {@link MultilingualContent}.
 *
 * @author Viorel Chihai
 */
public class MultilingualTranslatedValueContext extends TranslatedValueContext {

    private TranslationContext translationContext;

    private MultilingualContent multilingualContent;

    MultilingualTranslatedValueContext(TranslationContext translationContext, MultilingualContent multilingualContent) {
        super(translationContext);
        this.translationContext = translationContext;
        this.multilingualContent = multilingualContent;
    }

    @Override
    public String getLang() {
        return translationContext.getLanguage();
    }

    @Override
    public String getValue(String lang) {
        if (multilingualContent == null) {
            return null;
        }
        
        Map<String, String> translatedValues = multilingualContent.getOrBuildTranslations();
        return translatedValues != null ? translatedValues.get(lang) : null;
    }

    @Override
    public Map<String, String> getValues() {
        return multilingualContent.getTranslations().getTranslations();
    }
}
