package org.digijava.kernel.ampapi.endpoints.performance;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

public class AmpCategoryValueSerializer extends JsonSerializer<AmpCategoryValue> implements PropertyDescriber {

    @Override
    public void serialize(AmpCategoryValue value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("value", value.getValue());
        jgen.writeEndObject();
    }

    @Override
    public Property describe() {
        return new ObjectProperty()
                .name("AmpCategoryValue")
                .property("id", new LongProperty())
                .property("value", new StringProperty());
    }
}
