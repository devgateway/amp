package org.digijava.kernel.ampapi.endpoints.serializers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.Property;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

/**
 * Important: this deserializer uses JVM default timezone.
 *
 * @author Octavian Ciubotaru
 */
public class ISO8601DateSerializer extends JsonSerializer<Date> implements PropertyDescriber {

    private DateFormat dateFormat = new SimpleDateFormat(EPConstants.ISO8601_DATE_FORMAT);

    @Override
    public void serialize(Date value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException {
        if (value == null) {
            jgen.writeNull();
        } else {
            synchronized (dateFormat) {
                jgen.writeString(dateFormat.format(value));
            }
        }
    }

    @Override
    public Property describe() {
        DateProperty property = new DateProperty();
        property.setFormat(EPConstants.ISO8601_DATE_FORMAT);
        return property;
    }
}
