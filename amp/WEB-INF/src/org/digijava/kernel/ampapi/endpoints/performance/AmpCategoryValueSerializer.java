package org.digijava.kernel.ampapi.endpoints.performance;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpCategoryValueSerializer extends JsonSerializer<AmpCategoryValue> {

    @Override
    public void serialize(AmpCategoryValue value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("value", value.getValue());
        jgen.writeEndObject();

    }
}
