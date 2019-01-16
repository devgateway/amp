package org.digijava.kernel.ampapi.swagger.converters;

import java.lang.reflect.Type;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import org.digijava.kernel.services.AmpOfflineVersion;

/**
 * @author Octavian Ciubotaru
 */
public class AmpOfflineVersionResolver extends AbstractModelConverter {

    public AmpOfflineVersionResolver() {
        super(Json.mapper());
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        JavaType javaType = _mapper.constructType(type);

        if (javaType.getRawClass().isAssignableFrom(AmpOfflineVersion.class)) {
            ModelImpl model = new ModelImpl();
            model.name("AmpOfflineVersion");
            model.type(StringProperty.TYPE);
            model.format("major.minor.patch[-suffix]");
            model.example("1.0.0");
            model.setPattern(AmpOfflineVersion.PATTERN.pattern());
            return model;
        }

        return super.resolve(type, context, chain);
    }
}
