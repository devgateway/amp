package org.digijava.kernel.ampapi.endpoints.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dgfoundation.amp.algo.AlgoUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public final class ObjectMapperUtils {

    private ObjectMapperUtils() {
    }

    public static <T> T readValueFromString(String value, Class<T> valueType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(value, valueType);
        } catch (IOException e) {
            throw new RuntimeException("Cannot deserialize " + valueType + " from " + value);
        }
    }
    
    public static Map<String, Object> getMapFromString(String value) {
        return value != null ? ObjectMapperUtils.readValueFromString(value, Map.class) : null;
    }
    
    /**
     * renders this value as a String
     */
    public static String valueToString(Object value) {
        try {
            return new ObjectMapper().writer().writeValueAsString(value);
        } catch (Exception e) {
            throw AlgoUtils.translateException(e);
        }
    }
}
