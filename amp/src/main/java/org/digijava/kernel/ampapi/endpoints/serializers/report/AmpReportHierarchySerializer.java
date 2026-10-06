package org.digijava.kernel.ampapi.endpoints.serializers.report;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.UntypedProperty;
import org.digijava.kernel.ampapi.swagger.converters.ModelDescriber;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;

import java.io.IOException;


/**
 * {@link AmpReportHierarchy} class JSON serializer
 *
 * @author Viorel Chihai
 */
public class AmpReportHierarchySerializer extends JsonSerializer<AmpReportHierarchy> implements ModelDescriber {
    @Override
    public void serialize(final AmpReportHierarchy value, final JsonGenerator jgen,
                          final SerializerProvider serializers) throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getColumn().getColumnId());
        jgen.writeStringField("name", value.getColumn().getColumnName());
        jgen.writeNumberField("levelId", value.getLevelId());
        jgen.writeEndObject();
    }

    @Override
    public Model describe() {
        ModelImpl model = new ModelImpl();
        model.name("AmpReportHierarchy");

        model.addProperty("id", new LongProperty());
        model.addProperty("name", new UntypedProperty());
        model.addProperty("orderId", new LongProperty());

        return model;
    }


}
