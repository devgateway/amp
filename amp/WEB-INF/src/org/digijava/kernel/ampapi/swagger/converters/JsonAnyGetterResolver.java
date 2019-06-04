package org.digijava.kernel.ampapi.swagger.converters;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.JavaType;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;

/**
 * @author Nadejda Mandrescu
 */
public class JsonAnyGetterResolver extends AbstractModelConverter {

    public JsonAnyGetterResolver() {
        super(Json.mapper());
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        Model model = super.resolve(type, context, chain);

        JavaType javaType = _mapper.constructType(type);
        Class<?> rawType = javaType.getRawClass();

        for (Method method : rawType.getDeclaredMethods()) {
            if (method.getAnnotation(JsonAnyGetter.class) != null) {
                if (model.getProperties() != null) {
                    if (!customAnyGetter(rawType, context, model.getProperties())) {
                        addGenericProperties(model.getProperties());
                    }
                }
            }
        }

        return model;
    }

    private boolean customAnyGetter(Class<?> rawType, ModelConverterContext context, Map<String, Property> properties) {
        if (context.getJsonView() != null) {
            for (Class<?> jsonView : context.getJsonView().value()) {
                if (org.digijava.kernel.ampapi.endpoints.activity.dto.ImportView.class.isAssignableFrom(jsonView)
                        && rawType.isAssignableFrom(JsonApiResponse.class)) {
                    ObjectProperty activityProp = new ObjectProperty();
                    activityProp.setDescription("the activity that was provided as an input");
                    properties.put(ActivityEPConstants.ACTIVITY, activityProp);
                    return true;
                }
            }
        }
        return false;
    }

    private void addGenericProperties(Map<String, Property> properties) {
        properties.put("optionalProp1", new ObjectProperty());
        properties.put("optionalProp2", new ObjectProperty());
        properties.put("optionalPropN", new ObjectProperty());
    }
}
