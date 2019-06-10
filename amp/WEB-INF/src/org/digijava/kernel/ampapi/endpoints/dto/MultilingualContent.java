package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.serializers.MultilingualContentSerializer;

/**
 * @author Nadejda Mandrescu
 */
@JsonSerialize(using = MultilingualContentSerializer.class)
public class MultilingualContent {

    private UnwrappedTranslations translations;

    private String text;

    private boolean isMultilingual;

    public MultilingualContent(String text) {
        this(text, false);
    }

    public MultilingualContent(Map<String, String> translations) {
        this(translations, true);
    }

    public MultilingualContent(Object multilingualContent, boolean isMultilingual) {
        this.isMultilingual = isMultilingual;
        try {
            if (isMultilingual) {
                translations = new UnwrappedTranslations((Map<String, String>) multilingualContent);
            } else {
                text = (String) multilingualContent;
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Multilingual content must be a String or a Map", e);
        }
    }

    public UnwrappedTranslations getTranslations() {
        return translations;
    }

    /**
     * @return if multilingual is enabled, then actual translations are provided; otherwise a simulated translations map
     */
    public Map<String, String> getOrBuildTranslations() {
        if (isMultilingual) {
            return this.translations.getTranslations();
        }
        Map<String, String> ts = new HashMap<>();
        ts.put(TranslationSettings.getCurrent().getDefaultLangCode(), text);
        return ts;
    }

    public String getText() {
        return text;
    }

    /**
     * @return if multilingual is disabled, then actual text is provided; otherwise the default language translation
     */
    public String getOrBuildText() {
        if (isMultilingual) {
            return translations.get(TranslationSettings.getCurrent().getDefaultLangCode());
        }
        return text;
    }

    public boolean isMultilingual() {
        return isMultilingual;
    }

    public static MultilingualContent build(boolean isMultilingual, String text, Map<String, String> translations) {
        if (isMultilingual) {
            return new MultilingualContent(translations);
        }
        return new MultilingualContent(text);
    }

}
