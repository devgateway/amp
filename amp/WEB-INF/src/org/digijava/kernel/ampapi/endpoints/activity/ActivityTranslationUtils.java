package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.digijava.kernel.ampapi.endpoints.common.AMPTranslatorService;
import org.digijava.kernel.ampapi.endpoints.common.TranslatorService;
import org.digijava.kernel.ampapi.endpoints.resource.AmpResource;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.activityversioning.ResourceTextField;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.editor.exception.EditorException;

/**
 * @author Octavian Ciubotaru
 */
public final class ActivityTranslationUtils {

    private static TranslatorService translatorService = AMPTranslatorService.INSTANCE;

    private ActivityTranslationUtils() {
    }

    public static void setTranslatorService(TranslatorService translatorService) {
        ActivityTranslationUtils.translatorService = translatorService;
    }

    public static boolean isVersionableTextField(Field field) {
        return field.getAnnotation(VersionableFieldTextEditor.class) != null;
    }

    private static boolean isResourceTextField(Field field) {
        return field.getAnnotation(ResourceTextField.class) != null;
    }

    /**
     * Get the translation values of the field.
     * @param field
     * @param clazz class used for retrieving translation
     * @param fieldValue
     * @param parentObject is the parent that contains the object in order to retrieve translations throu parent
     *                     object id
     * @return object with the translated values
     * @throws NoSuchFieldException
     */
    public static Object getTranslationValues(Field field, Class<?> clazz, Object fieldValue, Object parentObject)
            throws IllegalAccessException, EditorException, NoSuchFieldException {

        TranslationSettings translationSettings = TranslationSettings.getCurrent();

        // check if this is translatable field
        boolean isTranslatable = translationSettings.isTranslatable(field);
        boolean isEditor = isVersionableTextField(field);
        boolean isResource = isResourceTextField(field);

        // provide map for translatable fields
        if (isTranslatable) {
            String fieldText = (String) fieldValue;
            if (isEditor) {
                Map<String, Object> fieldTrnValues = new HashMap<String, Object>();
                for (String translation : translationSettings.getTrnLocaleCodes()) {
                    // AMP-20884: no html tags cleanup so far
                    //String translatedText = DgUtil.cleanHtmlTags(DbUtil.getEditorBodyEmptyInclude(
                    // SiteUtils.getGlobalSite(), fieldText, translation));
                    String translatedText = translatorService.getEditorBodyEmptyInclude(TLSUtils.getSite(),
                            fieldText, translation);
                    fieldTrnValues.put(translation, getJsonStringValue(translatedText));
                }
                return fieldTrnValues;
            } else if (isResource) {
                return loadTranslationsForResourceField(field, parentObject, translationSettings);
            } else {
                return loadTranslationsForField(clazz, field.getName(), fieldText, parentObject,
                        translationSettings.getTrnLocaleCodes());
            }
        }

        // for reach text editors
        if (isEditor) {
            // AMP-20884: no html tags cleanup so far
            return translatorService.getEditorBodyEmptyInclude(TLSUtils.getSite(), (String) fieldValue,
                    translationSettings.getDefaultLangCode());
        }

        // other translatable options
        if (fieldValue instanceof String) {
            boolean toTranslate = clazz.equals(AmpCategoryValue.class) && field.getName().equals("value");

            // now we check if is only a CategoryValueLabel field and the field name is value
            String translatedText = toTranslate ? translatorService.translateText((String) fieldValue)
                    : (String) fieldValue;
            return getJsonStringValue(translatedText);
        } else if (fieldValue instanceof Date) {
            return DateTimeUtil.formatISO8601DateTime((Date) fieldValue);
        }

        return fieldValue;
    }

    /**
     *
     * @param value
     * @return value if is not blank or null (in order to have for empty strings null values in result JSON)
     */
    private static String getJsonStringValue(String value) {
        return StringUtils.isBlank(value) ? null : value;
    }

    /**
     * Gets the ID of an enumerable object (used in Possible Values EP)
     * @param obj
     * @return ID if it's identifiable, null otherwise
     */
    private static Long getId(Object obj) {
        //TODO: throw an exception here? it's currently swallowed
        if (obj == null || !Identifiable.class.isAssignableFrom(obj.getClass())) {
            return null;
        }

        Identifiable identifiableObject = (Identifiable) obj;
        Long id = (Long) identifiableObject.getIdentifier();
        return id;
    }

    private static Object loadTranslationsForField(Class<?> clazz, String propertyName, String fieldValue,
            Object parentObject, Set<String> languages) {

        Map<String, String> translations = new LinkedHashMap<>();
        TranslationSettings translationSettings = TranslationSettings.getDefault();
        String defLangCode = translationSettings.getDefaultLangCode();

        for (String l : languages) {
            translations.put(l, null);
        }
        Long id = parentObject instanceof Long ? (Long) parentObject : getId(parentObject);

        if (id == null) {
            return translations;
        }

        List<AmpContentTranslation> trns = translatorService.loadFieldTranslations(clazz.getName(), id, propertyName);
        for (AmpContentTranslation trn : trns) {
            if (languages.contains(trn.getLocale())) {
                translations.put(trn.getLocale(), trn.getTranslation());
            }
        }

        translations.putIfAbsent(defLangCode, fieldValue);

        return translations;
    }

    /**
     * @param field
     * @param parentObject
     * @param translationSettings
     * @return translations
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Map<String, Object> loadTranslationsForResourceField(Field field, Object parentObject,
            TranslationSettings translationSettings) throws NoSuchFieldException, IllegalAccessException {

        Map<String, Object> fieldTrnValues = new HashMap<String, Object>();
        AmpResource resource = (AmpResource) parentObject;
        ResourceTextField resourceAnnotation = field.getAnnotation(ResourceTextField.class);
        Field translationsField = resource.getClass().getDeclaredField(resourceAnnotation.translationsField());
        translationsField.setAccessible(true);

        Map<String, String> resourceTranslations = (Map<String, String>) translationsField.get(resource);
        for (String translation : translationSettings.getTrnLocaleCodes()) {
            fieldTrnValues.put(translation, resourceTranslations.get(translation));
        }

        return fieldTrnValues;
    }
}
