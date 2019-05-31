package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

    public String getText() {
        return text;
    }

    public boolean isMultilingual() {
        return isMultilingual;
    }

}
