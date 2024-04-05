package org.digijava.kernel.ampapi.endpoints.common;

import org.apache.log4j.Logger;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.dto.MultilingualContent;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.sync.model.Translation;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.AmpIndicatorLayer;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.FieldTranslationPack;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

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

    public static Map<String, String> getTranslatableFieldValue(String fieldName, String fieldValue,
            Long parentObjectId) {
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
    private static Map<String, String> getTranslationValues(Field field, Class<?> clazz, String fieldValue,
            Long parentObjectId) throws SecurityException, IllegalArgumentException {

        // check if this is translatable field
        boolean isTranslatable = isTranslatable(field);

        if (!isTranslatable) {
            throw new RuntimeException(String.format("Field %s is not translatable.", field));
        }

        // provide map for translatable fields
        return loadTranslationsForField(clazz, field.getName(), fieldValue, parentObjectId);
    }

    public static Map<String, String> loadTranslationsForField(Class<?> clazz, String propertyName, String fieldValue,
            Long id) {
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
        translations.putIfAbsent(defLangCode, fieldValue);

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
        if (MultilingualContent.class.isAssignableFrom(field.getType())) {
            return TranslationSettings.TranslationType.MULTILINGUAL;
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
    public String extractTranslationsOrSimpleValue(String fieldName, Object parentObj, Map<String, String> jsonValue) {
        return extractTranslationsOrSimpleValue(ReflectionUtil.getField(parentObj, fieldName), parentObj, jsonValue);
    }

    public String extractTranslationsOrSimpleValue(Field field, Object parentObj, Map<String, String> jsonValue) {
        return extractContentTranslation(field, parentObj, jsonValue);
    }


    /**
     * Stores all provided translations
     * @param field the field to translate
     * @param parentObj the object the field is part of
     * @param trnJson <lang, value> map of translations for each language
     * @return value to be stored in the base table
     */
    protected String extractContentTranslation(Field field, Object parentObj, Map<String, String> trnJson) {
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
        for (Entry<String, String> trn : trnJson.entrySet()) {
            String langCode = trn.getKey();
            String translation = DgUtil.cleanHtmlTags(trn.getValue());
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
