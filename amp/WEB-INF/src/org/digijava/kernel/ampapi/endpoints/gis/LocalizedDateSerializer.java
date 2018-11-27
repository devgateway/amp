package org.digijava.kernel.ampapi.endpoints.gis;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.Property;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;
import org.digijava.module.aim.helper.FormatHelper;

/**
 * @author Octavian Ciubotaru
 */
public class LocalizedDateSerializer extends StdSerializer<Date> implements PropertyDescriber {

    public LocalizedDateSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            gen.writeString(FormatHelper.formatDate(value));
        } else {
            gen.writeNull();
        }
    }

    @Override
    public Property describe() {
        DateProperty property = new DateProperty();
        property.setExample("15/10/2016");
        property.setDescription("Localized date using global setting format.");
        return property;
    }
}
