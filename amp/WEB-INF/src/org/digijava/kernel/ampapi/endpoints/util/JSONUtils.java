package org.digijava.kernel.ampapi.endpoints.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Octavian Ciubotaru
 */
public final class JSONUtils {

    private JSONUtils() {
    }

    public static String serialize(Object value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot serialize");
        }
    }

    public static <T> T readValueFromJson(String json, Class<T> valueType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException("Cannot deserialize " + valueType + " from " + json);
        }
    }
}
