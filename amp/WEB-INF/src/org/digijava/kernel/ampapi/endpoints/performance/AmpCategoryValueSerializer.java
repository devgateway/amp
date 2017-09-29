package org.digijava.kernel.ampapi.endpoints.performance;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpCategoryValueSerializer extends JsonSerializer<AmpCategoryValue> {

    @Override
    public void serialize(AmpCategoryValue value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("value", value.getValue());
        jgen.writeEndObject();

    }
}
