/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityTranslationUtils;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.editor.exception.EditorException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;

/**
 * Common base for AMP identifiable entities serialization
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
            return ActivityTranslationUtils.getTranslationValues(field, clazz, fieldValue, value.get().getIdentifier());
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException
                | EditorException | NoSuchFieldException e) {
            throw new IOException(e);
        }
    }

}
