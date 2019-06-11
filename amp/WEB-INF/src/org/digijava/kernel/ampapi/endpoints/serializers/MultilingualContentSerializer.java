package org.digijava.kernel.ampapi.endpoints.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.ampapi.swagger.converters.ModelDescriber;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;

/**
 * @author Nadejda Mandrescu
 */
public class MultilingualContentSerializer extends JsonSerializer<MultilingualContent> implements ModelDescriber {

    @Override
    public void serialize(MultilingualContent value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {

        if (value.isMultilingual()) {
            gen.writeObject(value.getTranslations());
        } else {
            gen.writeString(value.getText());
        }

    }

    @Override
    public Model describe() {
        ModelImpl model = new ModelImpl();
        model.setName("MultilingualContent");
        model.setDescription("If multilingual content is enabled, then stores a Map of <langCode, langTransaltion> "
                + "pairs. Otherwise is a simple String.");
        model.setExample("String or Map<String, String>");
        // TODO use oneOf property once OpenAPI3
        return model;
    }
}
