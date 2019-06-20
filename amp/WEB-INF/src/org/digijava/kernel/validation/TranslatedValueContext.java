package org.digijava.kernel.validation;

import java.util.Map;

import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.module.aim.util.Identifiable;

/**
 * A context object that allows to retrieve the value for a specific language.
 *
 * @author Octavian Ciubotaru
 */
public abstract class TranslatedValueContext {

    private TranslationContext translationContext;

    public TranslatedValueContext(TranslationContext translationContext) {
        this.translationContext = translationContext;
    }

    /**
     * Return the language which is important for validation purposes.
     *
     * @return language code
     */
    public abstract String getLang();

    /**
     * Return the value for a specific language.
     *
     * @param language language code
     * @return value in the requested language or null
     */
    public abstract String getValue(String language);

    public abstract Map<String, String> getValues();

    /**
     * Return the context object for a translated field.
     *
     * @param objectValue value of the object containing the translated field
     * @param field field from which was read
     * @return translated value context
     */
    public TranslatedValueContext forField(Object objectValue, APIField field) {
        TranslatedValueContext translatedValueContext;

        if (field.getTranslationType() == TranslationSettings.TranslationType.TEXT) {
            boolean multilingual = field.isTranslatable() != null && field.isTranslatable();
            Object fieldValue = field.getFieldAccessor().get(objectValue);
            String editorKey = (String) fieldValue;
            translatedValueContext = new EditorTranslatedValueContext(translationContext, editorKey, multilingual);
        } else if (field.isTranslatable() != null && field.isTranslatable()) {
            if (field.getTranslationType() == TranslationSettings.TranslationType.STRING) {
                String objectClass = objectValue.getClass().getName();
                Long objectId = (Long) ((Identifiable) objectValue).getIdentifier();
                if (objectId == null) {
                    objectId = (long) System.identityHashCode(objectValue);
                }
                translatedValueContext = new ContentTranslatedValueContext(translationContext, objectClass, objectId,
                        field.getFieldNameInternal());
            } else {
                throw new RuntimeException("Unsupported translation type " + field.getTranslationType());
            }
        } else {
            translatedValueContext = new NotTranslatedValueContext(translationContext);
        }
        return translatedValueContext;
    }
}
