package org.digijava.kernel.ampapi.endpoints.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.Property;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;

/**
 * 
 * @author Nadejda Mandrescu
 */
public class ISO8601DateTimeSerializer extends JsonSerializer<Date> implements PropertyDescriber {
    private DateFormat dateFormat = new SimpleDateFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);

    @Override
    public Property describe() {
        DateProperty property = new DateProperty();
        property.setFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);
        return property;
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            synchronized (dateFormat) {
                gen.writeString(dateFormat.format(value));
            }
        }
    }

}
