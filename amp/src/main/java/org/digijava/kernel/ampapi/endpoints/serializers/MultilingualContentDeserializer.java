package org.digijava.kernel.ampapi.endpoints.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;

import java.io.IOException;
import java.util.Map;

/**
 * @author Viorel Chihai
 */
public class MultilingualContentDeserializer extends StdScalarDeserializer<MultilingualContent> {

    MultilingualContentDeserializer() {
        super(MultilingualContent.class);
    }

    @Override
    public MultilingualContent deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        switch (jp.getCurrentToken()) {
            case START_OBJECT: {
                JsonDeserializer<Object> deserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Map.class));
                return new MultilingualContent((Map<String, String>) deserializer.deserialize(jp, ctxt));
            }
            case VALUE_STRING: {
                return new MultilingualContent((jp.getText().trim()));
            }
            default:
                ctxt.reportBadDefinition(MultilingualContent.class, "Cannot deserialize " + jp.getCurrentName());
        }

        return null;
    }

}
