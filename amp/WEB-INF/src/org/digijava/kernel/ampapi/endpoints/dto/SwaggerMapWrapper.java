package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * The sole purpose of this class is to define a wrapper around a Map to be able document examples for Swagger.
 * Use it only at EP definition and pass the actual Map further in the workflow.
 *
 * @author Nadejda Mandrescu
 */
public class SwaggerMapWrapper<K, V> {
    @JsonAnySetter
    private Map<K, V> map = new LinkedHashMap<>();

    public SwaggerMapWrapper() {
    }

    public SwaggerMapWrapper(Map<K, V> map) {
        this.map = map;
    }

    public void setMap(Map<K, V> map) {
        this.map = map;
    }

    @JsonAnyGetter
    public Map<K, V> getMap() {
        return this.map;
    }

}
