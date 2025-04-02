package org.digijava.kernel.ampapi.endpoints.activity.preview.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.Property;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.module.common.util.DateTimeUtil;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> implements PropertyDescriber {

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeString(DateTimeUtil.formatISO8601Timestamp(value));
    }

    @Override
    public Property describe() {
        DoubleProperty property = new DoubleProperty();
        property.setDescription("Raw ");
        property.setExample("2020-08-03T03:00:00.000+0000");
        return property;
    }
}
