package org.digijava.kernel.ampapi.endpoints.activity;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.digijava.kernel.ampapi.endpoints.activity.utils.AmpMediaType;
import org.digijava.kernel.ampapi.endpoints.activity.utils.ApiCompat;

/**
 * @author Octavian Ciubotaru
 */
public class LocationExtraInfoJsonSerializer extends JsonSerializer<LocationExtraInfo> {

    @Override
    public void serialize(LocationExtraInfo value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        if (!AmpMediaType.POSSIBLE_VALUES_V2_JSON.equals(ApiCompat.getRequestedMediaType())) {
            provider.defaultSerializeField("parent_location_id", value.getParentLocationId(), jgen);
            provider.defaultSerializeField("parent_location_name", value.getParentLocationName(), jgen);
        }
        provider.defaultSerializeField("implementation_level_id", value.getCategoryValueId(), jgen);
        provider.defaultSerializeField("implementation_location_name", value.getCategoryValueName(), jgen);
        jgen.writeEndObject();
    }
}
