package org.digijava.kernel.ampapi.endpoints.indicator.AmpDashboard;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CoreIndicatorValueDTOSerializer implements JsonSerializer<CoreIndicatorValueDTO> {
    @Override
    public JsonElement serialize(CoreIndicatorValueDTO value, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("target-value", value.getTargetValue());
        jsonObject.addProperty("actual-value", value.getActualValue());

        // Serialize coreIndicatorType if present
        CoreIndicatorTypeDTO coreIndicatorType = value.getCoreIndicatorType();
        if (coreIndicatorType != null) {
            JsonObject coreIndicatorTypeJson = new JsonObject();
            coreIndicatorTypeJson.addProperty("name", coreIndicatorType.getName());
            coreIndicatorTypeJson.addProperty("core-type", coreIndicatorType.getCoreType());
            coreIndicatorTypeJson.addProperty("number-divider", coreIndicatorType.getNumberDivider());
            coreIndicatorTypeJson.addProperty("unit", coreIndicatorType.getUnit());
            jsonObject.add("core-indicator-type", coreIndicatorTypeJson);
        }

        return jsonObject;
    }


}
