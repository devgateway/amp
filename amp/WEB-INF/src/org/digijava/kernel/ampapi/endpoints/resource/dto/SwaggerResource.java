package org.digijava.kernel.ampapi.endpoints.resource.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.endpoints.dto.SwaggerMapWrapper;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceType;

import io.swagger.annotations.ApiModel;

/**
 * Used only for documentation in Swagger
 *
 * @author Nadejda Mandrescu
 */
@ApiModel(description = "A resource JSON that follows fields structure according to resource fields definition.\n"
        + "See [/resource/fields](#/resource/getAvailableFields) EP for the full list of fields and rules.\n"
        + "Export an existing contact as an example from "
        + "[/resource/{uuid}](#/resource/getResource) EP.")
public class SwaggerResource extends SwaggerMapWrapper<String, Object> {

    public static final FullResource getExample() {
        FullResource r = new FullResource();
        r.setTitle(new MultilingualContent("String or Map<String, String>"));
        r.setDescription(new MultilingualContent("String or Map<String, String>"));
        r.setResourceType(ResourceType.LINK);
        r.setUrl("https://www.amp.org");
        r.setPublic(false);
        r.setPrivate(true);
        r.getProperties().put("...", new Object());
        return r;
    }

    public static class FullResource extends AmpResource {
        private Map<String, Object> properties = new LinkedHashMap<>();

        @JsonAnyGetter
        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }
    }


}
