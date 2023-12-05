package org.digijava.kernel.ampapi.endpoints.activity;

import com.google.common.collect.ImmutableSet;
import org.digijava.kernel.ampapi.endpoints.activity.field.FieldInfoProvider;

import java.lang.reflect.Field;

import static org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants.TYPE_VARCHAR;

/**
 * @author Octavian Ciubotaru
 */
public class TestFieldInfoProvider implements FieldInfoProvider {

    public static final int MAX_STR_LEN = 10;

    private TranslationSettings translationSettings;

    public TestFieldInfoProvider() {
        this(false);
    }

    public TestFieldInfoProvider(boolean multilingual) {
        this(new TranslationSettings("en", ImmutableSet.of("en", "es", "fr"), multilingual));
    }

    public TestFieldInfoProvider(TranslationSettings translationSettings) {
        this.translationSettings = translationSettings;
    }

    @Override
    public String getType(Field f) {
        return String.class.isAssignableFrom(f.getType()) ? TYPE_VARCHAR : "unknown";
    }

    @Override
    public Integer getMaxLength(Field f) {
        return String.class.isAssignableFrom(f.getType()) && !f.getName().equals("noMaxLen") ? MAX_STR_LEN : null;
    }

    @Override
    public boolean isTranslatable(Field field) {
        return translationSettings.isTranslatable(field);
    }
    
    @Override
    public TranslationSettings.TranslationType getTranslatableType(Field field) {
        return translationSettings.getTranslatableType(field);
    }
}
