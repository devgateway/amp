package org.digijava.kernel.ampapi.endpoints.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;

import java.io.IOException;

/**
 * GeoCoded Location json serializer
 *
 * @author Viorel Chihai
 */
public class GeoCodedCategoryValueLocationSerializer extends JsonSerializer<AmpCategoryValueLocations> {

    @Override
    public void serialize(AmpCategoryValueLocations location, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeNumberField("id", location.getId());
        jgen.writeStringField("name", location.getName());
        jgen.writeStringField("administrative_level",
                TranslatorWorker.translateText(location.getParentCategoryValue().getLabel()));
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return true;
    }

}
