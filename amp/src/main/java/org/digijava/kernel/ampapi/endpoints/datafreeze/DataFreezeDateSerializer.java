package org.digijava.kernel.ampapi.endpoints.datafreeze;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.Property;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Viorel Chihai
 */
public class DataFreezeDateSerializer extends StdSerializer<Date> implements PropertyDescriber {

    public DataFreezeDateSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            String defaultDateFormat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
            DateFormat df = new SimpleDateFormat(defaultDateFormat);
            TimeZone tz = TimeZone.getTimeZone("UTC");
            
            df.setTimeZone(tz);

            gen.writeString(df.format(value));
        }
    }

    @Override
    public Property describe() {
        DateTimeProperty property = new DateTimeProperty();
        property.setDescription("Date format is defined by global settings (Default Date Format).");
        property.setExample("2017-08-30");
        return property;
    }
}
