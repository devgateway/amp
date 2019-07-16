package org.digijava.kernel.ampapi.endpoints.common;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.InterchangeUtils;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.sync.model.Translation;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.annotations.activityversioning.ResourceTextField;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.FieldTranslationPack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

/**
 * Content Translator
 * 
 */
public class TranslationUtil {
    protected static final Logger logger = Logger.getLogger(TranslationUtil.class);
    private List<AmpContentTranslation> translations = null;
    private static final TranslationSettings trnSettings = TranslationSettings.getCurrent();

    public TranslationUtil() {
       this.translations = new ArrayList<AmpContentTranslation>();
    }

    public List<AmpContentTranslation> getTranslations() {
        return translations;
    }

    public static Object getTranslatableFieldValue(String fieldName, String fieldValue, Long parentObjectId) {
        try {
            return getTranslationValues(AmpIndicatorLayer.class.getDeclaredField(fieldName), AmpIndicatorLayer.class, fieldValue, parentObjectId);
        } catch (Exception e) {
            logger.error("Couldn't translate the field value", e);
            throw new RuntimeException(e);
        }
    }

    public static Set<String> getLanguages() {
        Set<String> lang = new HashSet<String>();
        Set<Locale> languages = TLSUtils.getSite().getTranslationLanguages();
        for (Locale l : languages) {
            lang.add(l.getCode());
        }
        return lang;
    }

    /**
     * Get the translation values of the field.
     * @param field
     * @param clazz used for retrieving translation
     * @param fieldValue
     * @param parentObjectId is the parent object id that contains the object in order to retrieve translations
     * @return object with the translated values
     */
    public static Object getTranslationValues(Field field, Class<?> clazz, Object fieldValue, Long parentObjectId) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, EditorException {

        // check if this is translatable field
        boolean isTranslatable = isTranslatable(field);

        // provide map for translatable fields
        if (isTranslatable) {
            return loadTranslationsForField(clazz, field.getName(), (String) fieldValue, parentObjectId);
        }

        return fieldValue;
    }

    public static Object loadTranslationsForField(Class<?> clazz, String propertyName, String fieldValue, Long id) {
        Map<String, String> translations = new LinkedHashMap<String, String>();

        if (ContentTranslationUtil.multilingualIsEnabled()) {
            Set<String> languages = getLanguages();
            for (String l : languages) {
                translations.put(l, null);
            }

            if (id == null)
                return translations;

            List<AmpContentTranslation> trns = ContentTranslationUtil.loadFieldTranslations(clazz.getName(), id, propertyName);
            for(AmpContentTranslation trn:trns){
                if (languages.contains(trn.getLocale())) {
                    translations.put(trn.getLocale(), trn.getTranslation());
                }
            }
        }

        String defLangCode = TranslationSettings.getDefault().getDefaultLangCode();
        if (translations.get(defLangCode) == null) {
            translations.put(defLangCode, fieldValue);
        }

        return translations;
    }

    public static void serialize(Object obj, String propertyName, List<AmpContentTranslation> translations)
    {
        if (obj == null)
            throw new RuntimeException("cannot serialize null!");

        long entityId = ContentTranslationUtil.getObjectId(obj);
        FieldTranslationPack ftp = new FieldTranslationPack(obj.getClass().getName(), propertyName);

        for(AmpContentTranslation trans: translations) {
            ContentTranslationUtil.evictEntityFromCache(obj);
            if (propertyName.equalsIgnoreCase(trans.getFieldName()))
                ftp.add(trans);
        }
        ContentTranslationUtil.saveFieldTranslations(entityId, ftp);
    }

    /**
     * Detects if a field is translatable
     *
     * @param field
     * @return true if this field is translatable
     */
    public static boolean isTranslatable(Field field) {
        return (field.isAnnotationPresent(TranslatableField.class)
                && field.getDeclaringClass().isAnnotationPresent(TranslatableClass.class)
                || field.isAnnotationPresent(VersionableFieldTextEditor.class));
    }

    public static TranslationSettings.TranslationType getTranslatableType(Field field) {
        if (isTranslatable(field)) {
            return TranslationSettings.TranslationType.STRING;
        }
        if (field.isAnnotationPresent(VersionableFieldTextEditor.class)) {
            return TranslationSettings.TranslationType.TEXT;
        }
        if (field.isAnnotationPresent(ResourceTextField.class)) {
            return TranslationSettings.TranslationType.RESOURCE;
        }
        return TranslationSettings.TranslationType.NONE;
    }
    
    /**
     * Retrieves translation or a simple String value 
     * @param fieldName
     * @param parentObj
     * @param jsonValue
     * @return
     */
    public String extractTranslationsOrSimpleValue(String fieldName, Object parentObj, Object jsonValue) {
        return extractTranslationsOrSimpleValue(ReflectionUtil.getField(parentObj, fieldName), parentObj, jsonValue);
    }

    public String extractTranslationsOrSimpleValue(Field field, Object parentObj, Object jsonValue) {
        TranslationSettings.TranslationType trnType = getTranslatableType(field);
        // no translation expected
        if (TranslationSettings.TranslationType.NONE == trnType) {
            return (String) jsonValue;
        }
        // base table value
        String value = null;
        if (TranslationSettings.TranslationType.STRING == trnType) {
            value = extractContentTranslation(field, parentObj, (Map<String, Object>) jsonValue);
        } else {
            Map<String, Object> editorText = null;
            if (trnSettings.isMultilingual()) {
                editorText = (Map<String, Object>) jsonValue;
            } else {
                // simulate the lang-value map, since dg_editor is still stored per language
                editorText = new HashMap<String, Object>();
                editorText.put(trnSettings.getDefaultLangCode(), jsonValue);
            }
            value = extractTextTranslations(field, parentObj, editorText);
        }
        return value;
    }

    /**
     * Stores Rich Text Editor entries
     * @param field reference field for the key
     * @param parentObj the object the field is part of
     * @param trnJson <lang, value> map of translations for each language
     * @return dg_editor key reference to be stored in the base table
     */
    protected static String extractTextTranslations(Field field, Object parentObj, Map<String, Object> trnJson) {
        String key = null;
        boolean update= false;
        if (update) { // all editor keys must exist before
            try {
                key = (String) field.get(parentObj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        if (key == null) { // init it in any case
            key = getEditorKey(field.getName());
        }
        for (Entry<String, Object> trn : trnJson.entrySet()) {
            String langCode = trn.getKey();
            // AMP-20884: no cleanup so far DgUtil.cleanHtmlTags((String) trn.getValue());
            String translation = (String) trn.getValue();
            Editor editor;
            try {
                editor = DbUtil.getEditor(key, langCode);
                if (translation == null) {
                    // remove existing translations
                    if (editor != null) {
                        DbUtil.deleteEditor(editor);
                    }
                } else if (editor == null) {
                    // create new
                    editor = DbUtil.createEditor(TeamUtil.getCurrentAmpTeamMember().getUser(), langCode, "", key, null, translation,
                            "Indicator layer API", TLSUtils.getRequest());
                    DbUtil.saveEditor(editor);
                } else if (!editor.getBody().equals(translation)) {
                    // update existing if needed
                    editor.setBody(translation);
                    DbUtil.updateEditor(editor);
                }
            } catch (EditorException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return key;
    }

    private static String getEditorKey(String fieldName) {
        // must start with "aim-" since it is expected by AF like this...
        return "aim-import-" + fieldName + "-" + System.currentTimeMillis();
    }

    /**
     * Stores all provided translations
     * @param field the field to translate
     * @param parentObj the object the field is part of
     * @param trnJson <lang, value> map of translations for each language
     * @return value to be stored in the base table
     */
    protected String extractContentTranslation(Field field, Object parentObj, Map<String, Object> trnJson) {
        String value = null;
        String currentLangValue = null;
        String anyLangValue = null;

        String objectClass = parentObj.getClass().getName();
        Long objId = (Long) ((Identifiable) parentObj).getIdentifier();
        List<AmpContentTranslation> trnList = new ArrayList<AmpContentTranslation>();
        if (objId != null) {
            trnList = ContentTranslationUtil.loadFieldTranslations(objectClass, objId, field.getName());
        } else {
            objId = (long) System.identityHashCode(parentObj);
        }
        // process translations
        for (Entry<String, Object> trn : trnJson.entrySet()) {
            String langCode = trn.getKey();
            String translation = DgUtil.cleanHtmlTags((String) trn.getValue());
            AmpContentTranslation act = null;
            for (AmpContentTranslation existingAct : trnList) {
                if (langCode.equalsIgnoreCase(existingAct.getLocale())) {
                    act = existingAct;
                    break;
                }
            }
            // if translation to be removed
            if (translation == null) {
                trnList.remove(act);
            } else if (act == null) {
                act = new AmpContentTranslation(objectClass, objId, field.getName(), langCode, translation);
                trnList.add(act);
            } else {
                act.setTranslation(translation);
            }
            if (trnSettings.isDefaultLanguage(langCode)) {
                // set default language value as well
                value = translation;
            }
            if (anyLangValue == null) {
                anyLangValue = translation;
            }
            if (trnSettings.getCurrentLangCode().equals(langCode)) {
                currentLangValue = translation;
            }
        }
        // if default language still not set, let's determine it
        if (value == null) {
            value = currentLangValue != null ? currentLangValue : anyLangValue;
        }
        if (trnSettings.isMultilingual())
            translations.addAll(trnList);
        return value;
    }

    /**
     * Translates a list of labels in multiple languages and returns them grouped by label and locale.
     *
     * @param labels labels to be translated
     * @return translated labels grouped by label and locale
     */
    public static Map<String, Map<String, String>> translateLabels(List<String> labels) {
        return labels.stream().collect(toMap(Function.identity(), AMPTranslatorService.INSTANCE::translateLabel));
    }

    /**
     * Group translations by label, then by locale.
     *
     * Note: This output is prepared for AMP Offline thus translations are grouped by label instead of key.
     * There are cases when two different keys have the same label. This leads to a situation when we cannot build
     * this map because of collisions. In order to deal with this reality we are filtering all translations for which
     * key does not coincide with hash of label. {@link Translation#labelMatchesKey()}
     *
     * @param translations a list of translation to be grouped
     * @return translated labels grouped by label and locale
     */
    public static Map<String, Map<String, String>> groupByLabelAndLocale(List<Translation> translations) {
        return translations.stream()
                .filter(Translation::labelMatchesKey)
                .collect(groupingBy(Translation::getLabel,
                        toMap(Translation::getLocale, Translation::getTranslatedLabel)));
    }
}
