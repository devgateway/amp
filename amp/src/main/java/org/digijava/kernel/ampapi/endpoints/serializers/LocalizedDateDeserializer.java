package org.digijava.kernel.ampapi.endpoints.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.Property;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Important: this deserializer uses JVM default timezone.
 *
 * @author Octavian Ciubotaru
 */
public class LocalizedDateDeserializer extends StdScalarDeserializer<Date> implements PropertyDescriber {

    public LocalizedDateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            String str = p.getText().trim();
            if (str.length() == 0) {
                return (Date) getEmptyValue(ctxt);
            }
            String defaultFormat = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
            try {
                return FormatHelper.parseDate(str).getTime();
            } catch (RuntimeException e) {
                String msg = String.format("expected format \"%s\"", defaultFormat);
                throw new InvalidFormatException(p, msg, str, Date.class);
            }
        }
        return null;
    }

    @Override
    public Property describe() {
        DateProperty property = new DateProperty();
        property.setExample("15/10/2016");
        property.setDescription("Localized date using global setting format.");
        return property;
    }

}
