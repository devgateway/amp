package org.digijava.kernel.ampapi.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple { "code": "translated text" } translations store that is serialized unwrapped
 *
 * @author Nadejda Mandrescu
 */
@ApiModel(description = "Map of { language code: translation } pairs")
public class UnwrappedTranslations {

    private Map<String, String> translations;

    public UnwrappedTranslations() {
        this(new LinkedHashMap<>());
    }

    public UnwrappedTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    @JsonAnyGetter
    public Map<String, String> getTranslations() {
        return translations;
    }

    @JsonAnySetter
    public UnwrappedTranslations set(String code, String translation) {
        this.translations.put(code, translation);
        return this;
    }

    public String get(String code) {
        return translations.get(code);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return translations.isEmpty();
    }

}
