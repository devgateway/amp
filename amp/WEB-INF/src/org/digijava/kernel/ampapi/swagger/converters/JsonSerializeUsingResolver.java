package org.digijava.kernel.ampapi.swagger.converters;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * <p>Swagger can't describe objects created by Jackson serializers/deserializers.
 * <p>Once a class/method is spotted to use a custom serializer, it will be cast to either {@link PropertyDescriber}
 * or {@link ModelDescriber} and asked to describe the model or property.
 *
 * @author Octavian Ciubotaru
 */
public class JsonSerializeUsingResolver extends AbstractModelConverter {

    public JsonSerializeUsingResolver() {
        super(Json.mapper());
    }

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context, Annotation[] annotations,
            Iterator<ModelConverter> chain) {

        JsonSerialize js = findJsonSerialize(annotations);
        if (js != null && !js.using().equals(JsonSerializer.None.class)) {
            try {
                JsonSerializer jsonSerializer = js.using().newInstance();
                PropertyDescriber propertyDescriber = (PropertyDescriber) jsonSerializer;
                return propertyDescriber.describe();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Cannot instantiate " + js.using().getSimpleName(), e);
            }
        }

        return super.resolveProperty(type, context, annotations, chain);
    }

    private JsonSerialize findJsonSerialize(Annotation[] annotations) {
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(JsonSerialize.class)) {
                    return (JsonSerialize) annotation;
                }
            }
        }
        return null;
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        JavaType javaType = _mapper.constructType(type);

        JsonSerialize js = javaType.getRawClass().getAnnotation(JsonSerialize.class);
        if (js != null && !js.using().equals(JsonSerializer.None.class)) {
            try {
                JsonSerializer jsonSerializer = js.using().newInstance();
                return ((ModelDescriber) jsonSerializer).describe();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Cannot instantiate " + js.using().getSimpleName(), e);
            }
        }

        return super.resolve(type, context, chain);
    }
}
