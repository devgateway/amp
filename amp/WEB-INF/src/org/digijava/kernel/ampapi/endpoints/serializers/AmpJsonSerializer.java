/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.serializers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.editor.exception.EditorException;

/**
 * Common base for AMP indetifiable entities serialization
 * 
 * @author Nadejda Mandrescu
 */
public abstract class AmpJsonSerializer<T extends Identifiable> extends JsonSerializer<T> {
    
    protected ThreadLocal<JsonGenerator> jgen = new ThreadLocal<>();
    protected ThreadLocal<SerializerProvider> provider = new ThreadLocal<>();
    protected ThreadLocal<T> value = new ThreadLocal<>();
    
    protected abstract void serialize(T value) throws IOException;
    protected boolean writeNulls() throws IOException {
        return false;
    }
    
    @Override
    public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        this.jgen.set(jgen);
        this.provider.set(provider);
        this.value.set(value);
        
        jgen.writeStartObject();
        serialize(value);
        jgen.writeEndObject();
    }
    
    protected void writeField(String fieldName, Object value) throws IOException {
        if (value != null || writeNulls()) {
            provider.get().defaultSerializeField(fieldName, value, jgen.get());
        }
    }
    
    /**
     * This is temporary until AMP-23560 is done to reuse Activities Translations
     * @return
     * @throws IOException 
     */
    protected Object getTranslations(String fieldName) throws IOException {
        TranslationSettings translationSettings = TranslationSettings.getCurrent();
        // set translations to all translations, not only requested
        translationSettings.setTrnLocaleCodes(new HashSet<String>(SiteUtils.getUserLanguagesCodes(TLSUtils.getSite())));
        Class<T> clazz = (Class<T>) value.get().getClass();
        
        try {
            Field field = value.get().getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldValue = field.get(value.get());
            return InterchangeUtils.getTranslationValues(field, clazz, fieldValue, (Long) value.get().getIdentifier());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | EditorException | NoSuchFieldException e) {
            throw new IOException(e);
        }
    }

}
