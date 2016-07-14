package org.digijava.kernel.ampapi.endpoints.indicator;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IndicatorTranslationUtil {
    protected static final Logger logger = Logger.getLogger(IndicatorTranslationUtil.class);
    private static List<AmpContentTranslation> translations = new ArrayList<AmpContentTranslation>();
    private static TranslationSettings trnSettings;

    public IndicatorTranslationUtil() {
        this.trnSettings = TranslationSettings.getCurrent();
    }

    public static Object getTranslatableFieldValue(String fieldName, String fieldValue, Long parentObjectId) {
        try {
            return getTranslationValues(AmpIndicatorLayer.class.getDeclaredField(fieldName), AmpIndicatorLayer.class, fieldValue, parentObjectId);
        } catch (Exception e) {
            logger.error("Couldn't translate the field value", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Get the translation values of the field.
     * @param field
     * @param class used for retrieving translation
     * @param fieldValue
     * @param parentObject is the parent that contains the object in order to retrieve translations throu parent object id
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
            Set<String> languages = TranslationSettings.getDefault().getTrnLocaleCodes();
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
        } else {
            String defLangCode = TranslationSettings.getDefault().getDefaultLangCode();
            if (translations.get(defLangCode) == null){
                translations.put(defLangCode, fieldValue);
            }
        }

        return translations;
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

    public static TranslationSettings getTranslationSettings() {
        HttpServletRequest requestAttributes = TLSUtils.getRequest();

        return (TranslationSettings) requestAttributes.getAttribute(EPConstants.TRANSLATIONS);
    }

    public static String extractTranslationsOrSimpleValue(Field field, Object parentObj, Object jsonValue) {
        TranslationSettings.TranslationType trnType = trnSettings.getTranslatableType(field);
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
                    editor = DbUtil.createEditor( IndicatorUtils.getTeamMember().getUser(), langCode, "", key, null, translation,
                            "Activities API", TLSUtils.getRequest());
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
    protected static String extractContentTranslation(Field field, Object parentObj, Map<String, Object> trnJson) {
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


    protected static Field getField(Object parent, String actualFieldName) {
        if (parent == null) {
            return null;
        }
        Field field = null;
        try {
            Class<?> clazz = parent.getClass();
            while (field == null && !clazz.equals(Object.class)) {
                try {
                    field = clazz.getDeclaredField(actualFieldName);
                    field.setAccessible(true);
                } catch (NoSuchFieldException ex) {
                    clazz = clazz.getSuperclass();
                }
            }
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        return field;
    }

}