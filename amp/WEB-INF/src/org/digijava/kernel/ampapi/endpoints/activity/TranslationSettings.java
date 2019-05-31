package org.digijava.kernel.ampapi.endpoints.activity;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.annotations.activityversioning.ResourceTextField;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.translation.util.ContentTranslationUtil;

/**
 * Class for storing translating settings for activity export. <br>
 * The bean is created in AuthRequestFilter and stored in request.
 * 
 * @author Viorel Chihai
 */
public class TranslationSettings {
    /** Translation Types */
    public enum TranslationType {
        /** Associated to @TranslatableField fields if multilingual is enabled*/
        STRING,
        /** Associated to @VersionableFieldTextEditor fields if multilingual is enabled */
        TEXT,
        /** Associated to @ResourceTextField fields if multilingual is enabled */
        RESOURCE,
        /** No Translation associated to the given String field or multilingual is disabled */
        NONE
    };

    /** Base language code (retrieved from 'language' parameter) */
    private String currentLangCode;
    
    /** Default language code for the current site */
    private String defaultLangCode;
    
    /** Allowed language codes */
    private Collection<String> allowedLangCodes = null;
    
    /** 
     * List of translations.
     * Reunion of language codes from <default_language>, 'language' and 'translations' parameter
     */
    private Set<String> trnLocaleCodes = new HashSet<String>();
    
    private boolean multilingual;

    private static TranslationSettings defaultOverride;
    
    /**
     * @return default trn settings for the current request environment
     */
    public static TranslationSettings getDefault() {
        if (defaultOverride != null) {
            return defaultOverride;
        }
        return new TranslationSettings();
    }

    public static void setDefaultOverride(TranslationSettings defaultOverride) {
        TranslationSettings.defaultOverride = defaultOverride;
    }

    /**
     * @return current translation settings or the default one if nothing configured
     */
    public static TranslationSettings getCurrent() {
        TranslationSettings trn = (TranslationSettings) TLSUtils.getRequest().getAttribute(EPConstants.TRANSLATIONS);
        if (trn == null)
            trn = getDefault();
        return trn;
    }
    
    public TranslationSettings() {
        init(ContentTranslationUtil.multilingualIsEnabled());
        this.trnLocaleCodes.add(currentLangCode);
        this.trnLocaleCodes.add(getDefaultLangCode());
    }

    public TranslationSettings(String currentLangCode, Set<String> trnLocaleCodes) {
        this(currentLangCode, trnLocaleCodes, ContentTranslationUtil.multilingualIsEnabled());
    }
    
    public TranslationSettings(String currentLangCode, Set<String> trnLocaleCodes, boolean multilingual) {
        this.currentLangCode = currentLangCode;
        this.trnLocaleCodes = trnLocaleCodes;
        init(multilingual);
    }
    
    private void init(boolean multilingual) {
        if (this.currentLangCode == null) {
            this.currentLangCode = TLSUtils.getEffectiveLangCode();
        }
        this.multilingual = multilingual;
    }
    
    /**
     * @return default language code for the current site
     */
    public String getDefaultLangCode() {
        if (defaultLangCode == null) {
            this.defaultLangCode = TLSUtils.getSite().getDefaultLanguage().getCode();
        }
        return defaultLangCode;
    }
    
    public void setTrnLocaleCodes(Set<String> trnLocaleCodes) {
        this.trnLocaleCodes = trnLocaleCodes;
    }
    
    /**
     * @return user selected language, that can be configured also via 'language' parameter
     */
    public String getCurrentLangCode() {
        return currentLangCode;
    }

    public void setBaseLangCode(String currentLangCode) {
        this.currentLangCode = currentLangCode;
    }

    public Set<String> getTrnLocaleCodes() {
        return trnLocaleCodes;
    }
    
    /**
     * @param langCode code of the language (locale)  
     * @return boolean if the langCode is equal with the default system language code
     * */
    public boolean isDefaultLanguage(String langCode) {
        return getDefaultLangCode().equalsIgnoreCase(langCode);
    }

    /**
     * @return the allowedLangCodes
     */
    public Collection <String> getAllowedLangCodes() {
        if (allowedLangCodes == null) {
            return SiteUtils.getUserLanguagesCodes(TLSUtils.getSite());
        }
        return allowedLangCodes;
    }
    
    /**
     * @return the multilingual
     */
    public boolean isMultilingual() {
        return multilingual;
    }

    /**
     * Detects if a field is translatable
     * 
     * @param field
     * @return true if this field is translatable
     */ 
    public boolean isTranslatable(Field field) {
        return multilingual && (field.isAnnotationPresent(TranslatableField.class) 
                && field.getDeclaringClass().isAnnotationPresent(TranslatableClass.class)
                || field.isAnnotationPresent(VersionableFieldTextEditor.class)
                || field.isAnnotationPresent(ResourceTextField.class));
    }
    
    /**
     * Provides 
     * @param field
     * @return
     */
    public TranslationType getTranslatableType(Field field) {
        if (multilingual && field.isAnnotationPresent(TranslatableField.class)
                && field.getDeclaringClass().isAnnotationPresent(TranslatableClass.class)) {
            return TranslationType.STRING;
        }
        if (field.isAnnotationPresent(VersionableFieldTextEditor.class)) {
            return TranslationType.TEXT;
        }
        if (field.isAnnotationPresent(ResourceTextField.class)) {
            return TranslationType.RESOURCE;
        }
        return TranslationType.NONE;
    }
    
}
