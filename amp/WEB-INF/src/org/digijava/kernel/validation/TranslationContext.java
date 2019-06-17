package org.digijava.kernel.validation;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Triple;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.activity.field.APIField;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.editor.dbentity.Editor;

/**
 * This class allows to load values for multilingual fields for validation purposes.
 *
 * @author Octavian Ciubotaru
 */
public class TranslationContext {

    public interface EditorLoader {

        List<Editor> load(String editorKey);
    }

    public interface ContentTranslationLoader {

        List<AmpContentTranslation> load(String objectClass, Long objectId, String fieldName);
    }

    /**
     * Language that is significant for validation purposes. When multilingual is enabled, all required fields will
     * be validated for this language.
     */
    private String language;

    /**
     * Language used for editor values when multilingual is turned off.
     */
    private String defaultLanguage;

    /**
     * Changed editor values.
     */
    private EditorStore editorStore;

    /**
     * Editor values that didn't change.
     */
    private Map<String, Map<String, String>> oldEditors = new HashMap<>();

    /**
     * Used to load editor values when those are not present in {@link #editorStore}.
     */
    private EditorLoader editorLoader;

    /**
     * Changed content translation values.
     */
    private List<AmpContentTranslation> translations;

    /**
     * Content translations that didn't change.
     */
    private Map<Triple<String, Long, String>, List<AmpContentTranslation>> oldTranslations = new HashMap<>();

    /**
     * Used to load content translation values when those are not present in {@link #translations}.
     */
    private ContentTranslationLoader contentTranslationLoader;

    public TranslationContext(String language, String defaultLanguage, EditorStore editorStore,
            List<AmpContentTranslation> translations, EditorLoader editorLoader,
            ContentTranslationLoader contentTranslationLoader) {
        this.language = language;
        this.defaultLanguage = defaultLanguage;
        this.editorStore = editorStore;
        this.editorLoader = editorLoader;
        this.translations = translations;
        this.contentTranslationLoader = contentTranslationLoader;
    }

    public String getLanguage() {
        return language;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public Map<String, String> getEditor(String editorKey) {
        if (!editorStore.getValues().containsKey(editorKey)) {
            return oldEditors.computeIfAbsent(editorKey, this::loadEditorFromDb);
        }
        return editorStore.getValues().get(editorKey);
    }

    private Map<String, String> loadEditorFromDb(String editorKey) {
            List<Editor> editorList = editorLoader.load(editorKey);
            return editorList.stream().collect(toMap(Editor::getLanguage, Editor::getBody));
    }

    public List<AmpContentTranslation> getContentTranslation(String objectClass, Long objectId, String field) {
        List<AmpContentTranslation> translationsForField = translations.stream()
                .filter(ct -> isContentTranslationFor(ct, objectClass, objectId, field))
                .collect(toList());

        if (translationsForField.isEmpty()) {
            Triple<String, Long, String> key = Triple.of(objectClass, objectId, field);
            return oldTranslations.computeIfAbsent(key, this::loadContentTranslationFromDb);
        } else {
            return translationsForField;
        }
    }

    private List<AmpContentTranslation> loadContentTranslationFromDb(Triple<String, Long, String> key) {
        return contentTranslationLoader.load(key.getLeft(), key.getMiddle(), key.getRight());
    }

    private boolean isContentTranslationFor(AmpContentTranslation ct, String objectClass, Long objectId, String field) {
        return ct.getObjectClass().equals(objectClass)
                && ct.getObjectId().equals(objectId)
                && ct.getFieldName().equals(field);
    }

    /**
     * Return the context object for a translated field.
     *
     * @param field field from which was read
     * @param fieldValue field value
     * @param objectValue value of the object containing the translated field
     * @return translated value context
     */
    public TranslatedValueContext getValueTranslationContextForField(APIField field, Object fieldValue,
            Object objectValue) {
        TranslatedValueContext translatedValueContext;

        if (field.getTranslationType() == TranslationSettings.TranslationType.TEXT) {
            boolean multilingual = field.isTranslatable() != null && field.isTranslatable();
            String editorKey = (String) fieldValue;
            translatedValueContext = new EditorTranslatedValueContext(this, editorKey, multilingual);
        } else if (field.isTranslatable() != null && field.isTranslatable()) {
            if (field.getTranslationType() == TranslationSettings.TranslationType.STRING) {
                String objectClass = objectValue.getClass().getName();
                Long objectId = (Long) ((Identifiable) objectValue).getIdentifier();
                if (objectId == null) {
                    objectId = (long) System.identityHashCode(objectValue);
                }
                translatedValueContext = new ContentTranslatedValueContext(this, objectClass, objectId,
                        field.getFieldNameInternal());
            } else {
                throw new RuntimeException("Unsupported translation type " + field.getTranslationType());
            }
        } else {
            translatedValueContext = new NotTranslatedValueContext();
        }
        return translatedValueContext;
    }
}
