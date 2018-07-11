package org.digijava.kernel.ampapi.swagger.converters;

import java.lang.reflect.Type;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.util.Json;
import org.dgfoundation.amp.newreports.GeneratedReport;

/**
 * Generate dummy object for {@link GeneratedReport} class. This converter is needed because otherwise it will expose
 * lots of classes not meant to be consumed by API client. Also this class triggers infinite recursion in both
 * swagger-ui and redoc.
 *
 * @author Octavian Ciubotaru
 */
public class GeneratedReportResolver extends AbstractModelConverter {

    public GeneratedReportResolver() {
        super(Json.mapper());
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        JavaType javaType = _mapper.constructType(type);

        if (javaType.getRawClass().equals(GeneratedReport.class)) {
            ModelImpl model = new ModelImpl();
            model.name("GeneratedReport");
            context.defineModel("GeneratedReport", model);
            return model;
        }

        return super.resolve(type, context, chain);
    }
}
