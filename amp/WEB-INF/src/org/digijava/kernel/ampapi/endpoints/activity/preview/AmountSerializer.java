package org.digijava.kernel.ampapi.endpoints.activity.preview;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.Property;
import org.digijava.kernel.ampapi.swagger.converters.PropertyDescriber;

import java.io.IOException;
import java.math.BigDecimal;


/**
 * Amount json serializer used for activity preview endpoint
 * 
 * @author Viorel Chihai
 *
 */
public class AmountSerializer extends JsonSerializer<Double> implements PropertyDescriber {

    @Override
    public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException{
        jgen.writeString(BigDecimal.valueOf(value).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
    }

    @Override
    public Property describe() {
        DoubleProperty property = new DoubleProperty();
        property.setDescription("Raw ");
        property.setExample("2017-08-30");
        return property;

    }
}
