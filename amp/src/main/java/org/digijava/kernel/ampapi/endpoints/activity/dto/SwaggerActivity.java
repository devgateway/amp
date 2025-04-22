package org.digijava.kernel.ampapi.endpoints.activity.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import io.swagger.annotations.ApiModel;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.endpoints.dto.SwaggerMapWrapper;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Used only for documentation in Swagger
 *
 * @author Nadejda Mandrescu
 */
@ApiModel(description = "An activity JSON that follows fields structure according to activity fields definition.\n"
        + "See [/activity/fields](#/activity/getAvailableFields) EP for the full list of fields and rules.\n"
        + "Export an existing activity as an example from "
        + "[/activity/project](#/activity/getProjectByAmpId) EP.")
public class SwaggerActivity extends SwaggerMapWrapper<String, Object> {

    public SwaggerActivity() {
        super();
    }

    public SwaggerActivity(Map<String, Object> map) {
        super(map);
    }

    public static FullActivity getExample() {
        FullActivity a = new FullActivity();
        a.setAmpId("4532580543");
        a.setAmpActivityId(0);
        a.setIatiIdentifier("AA-AAA-123456789");
        a.setCreatedDate(new Date());
        a.setUpdatedDate(new Date());
        a.setName(new MultilingualContent("String or Map<String, String>"));
        a.getProperties().put("is_draft", true);
        a.getProperties().put("...", new Object());
        return a;
    }

    public static class FullActivity extends ActivitySummary {
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
