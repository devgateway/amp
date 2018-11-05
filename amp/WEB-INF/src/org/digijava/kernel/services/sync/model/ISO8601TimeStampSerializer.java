package org.digijava.kernel.services.sync.model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.Property;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;

/**
 * @author Octavian Ciubotaru
 */
public class ISO8601TimeStampSerializer extends StdSerializer<Date> implements PropertyDescriber {

    public ISO8601TimeStampSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Date value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException {
        if (value == null) {
            jgen.writeNull();
        } else {
            jgen.writeString(new SimpleDateFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT).format(value));
        }
    }

    @Override
    public Property describe() {
        DateTimeProperty property = new DateTimeProperty();
        property.setFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);
        return property;
    }
}
