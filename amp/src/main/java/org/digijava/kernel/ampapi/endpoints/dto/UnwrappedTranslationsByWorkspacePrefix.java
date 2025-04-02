package org.digijava.kernel.ampapi.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple { "ws-prefix": UnwrappedTranslations } translations store that is serialized unwrapped
 *
 * @author Gabriel Inchauspe
 */
@ApiModel(description = "Map of Maps for each workspace prefix: { language code: translation } pairs")
public class UnwrappedTranslationsByWorkspacePrefix {

    private Map<String, UnwrappedTranslations> prefixes;

    public UnwrappedTranslationsByWorkspacePrefix() {
        this(new LinkedHashMap<>());
    }

    public UnwrappedTranslationsByWorkspacePrefix(Map<String, UnwrappedTranslations> translations) {
        this.prefixes = translations;
    }

    public void setPrefixes(Map<String, UnwrappedTranslations> prefixes) {
        this.prefixes = prefixes;
    }

    @JsonAnyGetter
    public Map<String, UnwrappedTranslations> getPrefixes() {
        return prefixes;
    }

    @JsonAnySetter
    public UnwrappedTranslationsByWorkspacePrefix set(String prefix, String code, String translation) {
        if (this.prefixes.get(prefix) != null) {
            this.prefixes.get(prefix).set(code, translation);
        } else {
            UnwrappedTranslations langs = new UnwrappedTranslations();
            langs.set(code, translation);
            this.prefixes.put(prefix, langs);
        }
        return this;
    }

    @JsonAnySetter
    public UnwrappedTranslationsByWorkspacePrefix set(String prefix, UnwrappedTranslations unwrappedTranslations) {
        this.prefixes.put(prefix, unwrappedTranslations);
        return this;
    }

    public String get(String prefix, String code) {
        return prefixes.get(prefix).get(code);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return prefixes.isEmpty();
    }

}
