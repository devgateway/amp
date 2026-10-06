package org.digijava.module.aim.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * String to String conveter that translates values via {@link TranslatorWorker#translateText(String)}.
 *
 * @author Octavian Ciubotaru
 */
public class TranslatingConverter implements Converter<String, String> {

    @Override
    public String convert(String value) {
        return TranslatorWorker.translateText(value);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }
}
