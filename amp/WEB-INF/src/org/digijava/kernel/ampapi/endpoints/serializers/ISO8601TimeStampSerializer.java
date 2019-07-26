package org.digijava.kernel.ampapi.endpoints.serializers;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.module.common.util.DateTimeUtil;

import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.Property;

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
            jgen.writeString(DateTimeUtil.formatISO8601Timestamp(value));
        }
    }

    @Override
    public Property describe() {
        DateTimeProperty property = new DateTimeProperty();
        property.setFormat(EPConstants.ISO8601_DATE_AND_TIME_FORMAT);
        return property;
    }
}
