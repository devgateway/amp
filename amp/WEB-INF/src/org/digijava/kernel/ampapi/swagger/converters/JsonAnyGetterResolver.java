package org.digijava.kernel.ampapi.swagger.converters;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.JavaType;

import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.common.JsonApiResponse;
import org.digijava.kernel.ampapi.endpoints.contact.ContactEPConstants;
import org.digijava.kernel.ampapi.endpoints.resource.ResourceEPConstants;

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

    private boolean customAnyGetter(Class<?> rawType, ModelConverterContext context, Map<String, Property> props) {
        if (context.getJsonView() != null) {
            for (Class<?> jsonView : context.getJsonView().value()) {
                if (rawType.isAssignableFrom(JsonApiResponse.class)) {
                    if (org.digijava.kernel.ampapi.endpoints.activity.dto.ActivityView.Import.class
                            .isAssignableFrom(jsonView)) {
                        addObjectProperty(
                                props, ActivityEPConstants.ACTIVITY, "the activity that was provided as an input");
                        return true;
                    }
                    if (org.digijava.kernel.ampapi.endpoints.contact.dto.ContactView.Summary.class
                            .isAssignableFrom(jsonView)) {
                        addObjectProperty(
                                props, ContactEPConstants.CONTACT, "the contact that was provided as an input");
                        return true;
                    }
                    if (org.digijava.kernel.ampapi.endpoints.resource.dto.ResourceView.Common.class
                            .isAssignableFrom(jsonView)) {
                        addObjectProperty(
                                props, ResourceEPConstants.RESOURCE, "the resource that was provided as an input");
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private void addObjectProperty(Map<String, Property> properties, String name, String description) {
        ObjectProperty prop = new ObjectProperty();
        prop.setDescription(description);
        properties.put(name, prop);
    }

    private void addGenericProperties(Map<String, Property> properties) {
        properties.put("additionalProp1", new ObjectProperty());
        properties.put("additionalProp2", new ObjectProperty());
        properties.put("additionalProp3", new ObjectProperty());
    }
}
