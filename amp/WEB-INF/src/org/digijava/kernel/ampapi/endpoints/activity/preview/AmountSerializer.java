package org.digijava.kernel.ampapi.endpoints.activity.preview;

import java.io.IOException;
import java.math.BigDecimal;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * Amount json serializer used for activity preview endpoint
 * 
 * @author Viorel Chihai
 *
 */
public class AmountSerializer extends JsonSerializer<Double>  {

    @Override
    public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeString(BigDecimal.valueOf(value).setScale(0, BigDecimal.ROUND_HALF_UP).toString());        
    }

}
