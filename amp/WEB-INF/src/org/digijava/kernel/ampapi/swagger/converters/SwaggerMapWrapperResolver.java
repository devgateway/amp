package org.digijava.kernel.ampapi.swagger.converters;

import java.lang.reflect.Type;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;

import org.digijava.kernel.ampapi.endpoints.activity.dto.SwaggerActivity;
import org.digijava.kernel.ampapi.endpoints.dto.SwaggerMapWrapper;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.util.Json;

/**
 * @author Nadejda Mandrescu
 */
public class SwaggerMapWrapperResolver extends AbstractModelConverter {

    public SwaggerMapWrapperResolver() {
        super(Json.mapper());
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        JavaType javaType = _mapper.constructType(type);
        Class<?> rawType = javaType.getRawClass();

        if (SwaggerMapWrapper.class.isAssignableFrom(rawType)) {
            Model model = build(type, rawType, context, chain);
            if (model != null) {
                return model;
            }
        }

        return super.resolve(type, context, chain);
    }

    private Model build(Type type, Class<?> rawType, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (SwaggerActivity.class.isAssignableFrom(rawType)) {
            Model model = super.resolve(type, context, chain);
            model.setExample(SwaggerActivity.getInputExample());
            return model;
        }
        return null;
    }
}
