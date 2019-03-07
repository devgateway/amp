package org.digijava.module.esrigis.dbentity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.Property;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Octavian Ciubotaru
 */
public class ApiStateDateTimeSerializer extends StdSerializer<Date> implements PropertyDescriber {

    public ApiStateDateTimeSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            String formatString = FeaturesUtil.getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
            DateFormat df = new SimpleDateFormat(formatString + "'T'HH:mm'Z'");
            TimeZone tz = TimeZone.getTimeZone("UTC");

            df.setTimeZone(tz);

            gen.writeString(df.format(value));
        }
    }

    @Override
    public Property describe() {
        DateTimeProperty property = new DateTimeProperty();
        property.setDescription("Date part format is defined by global settings. "
                + "Time part has the 'T'HH:mm'Z' format.");
        property.setExample("27/05/2015T15:25Z");
        return property;
    }
}
