package org.digijava.kernel.ampapi.swagger.converters;

import java.lang.reflect.Type;
import java.util.Iterator;

import javax.xml.bind.JAXBElement;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.jackson.AbstractModelConverter;
import io.swagger.models.Model;
import io.swagger.util.Json;

/**
 * This converter handles elements that are wrapped with JAXBElement.
 *
 * A model for JAXBElement&lt;Report&gt; type is generated as if it was just Report.
 *
 * @author Octavian Ciubotaru
 */
public class JAXBElementUnwrapper extends AbstractModelConverter {

    public JAXBElementUnwrapper() {
        super(Json.mapper());
    }

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        JavaType javaType = _mapper.constructType(type);

        if (javaType.getRawClass().equals(JAXBElement.class)) {
            return super.resolve(javaType.getBindings().getBoundType(0), context, chain);
        }

        return super.resolve(type, context, chain);
    }
}
