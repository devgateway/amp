package org.digijava.module.translation.util;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.hibernate.Session;

/**
 * class containing all the data regarding a multilingual-input-field
 * @author Dolghier Constantin
 *
 */
public class MultilingualInputFieldValues
{
    private static Logger logger = Logger.getLogger(MultilingualInputFieldValues.class);
    
    /**
     * the TranslatableClass which holds the field
     */
    final Class<?> clazz;
    
    /**
     * the property
     */
    final String propertyName;
    
    /**
     * might be null
     */
    final Long id;
    
    /**
     * locales to use for this input field
     */
    final List<String> locales;
    
    /**
     * translations (preexisting or already-inputted)
     */
    final Map<String, String> translations;
    
    /**
     * used as a prefix of all the stuff which goes into the HTML (div prefix, input's prefix etc)
     */
    final String prefix;
    
    /**
     * constructs an instance of this class and fills it with data
     * @param clazz
     * @param id
     * @param propertyName
     * @param locales might be null or empty, in this case it will be autogenerated (TLSUtils.getRequest() must return a valid value in this case)
     */
    @SuppressWarnings("serial")
    public MultilingualInputFieldValues(Class<?> clazz, Long id, String propertyName, String suffix, List<String> locales)
    {
        this.clazz = clazz;
        this.id = id;
        this.propertyName = propertyName;
        
        if (!ContentTranslationUtil.multilingualIsEnabled()){
            //If content translation is not enabled we need to use the default site language
            this.locales = Collections.unmodifiableList(new ArrayList<String>(){{add(TLSUtils.getSite().getDefaultLanguage().getCode());}});
        }
        else if (locales == null || locales.isEmpty() || !ContentTranslationUtil.multilingualIsEnabled())
            this.locales = Collections.unmodifiableList(TranslatorUtil.getLanguages());
        else
            this.locales = Collections.unmodifiableList(new ArrayList<String>(locales));
        
        this.translations = loadTranslations();
        this.prefix = build_prefix(clazz, propertyName, suffix);
    }
    
    /**
     * prefix for HTML entities (<b>div</b> id, <b>input</b> name)
     * @param clazz
     * @param propertyName
     * @return
     */
    private static String build_prefix(Class<?> clazz, String propertyName, String suffix)
    {
        return clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1) + "_" + propertyName + (suffix == null ? "" : "_" + suffix);
    }
    
    /**
     * loads {@link #translations} with the field's translations in the relevant languages
     * @return
     */
    private Map<String, String> loadTranslations()
    {
        Map<String, String> res = new LinkedHashMap<String, String>(); 
        if (id == null)
            return res; // no preexisting value -> nothing to write
        
        List<AmpContentTranslation> trns = ContentTranslationUtil.loadFieldTranslations(clazz.getName(), id, propertyName);
        for(AmpContentTranslation trn:trns){
            if (locales.contains(trn.getLocale())){  // this row is O(n^2), but since the number of active languages in any installation will never exceed 5, it is ok
                res.put(trn.getLocale(), trn.getTranslation());
            }
        }
        if (res.isEmpty() && (id != null) && (id > 0)){
            // no amp_content_translations entries -> this entry has never been touched by multilingual
            Object entity = PersistenceManager.getSession().get(clazz, id);
            if (entity != null){
                String baseLanTranslation = (String) ContentTranslationUtil.getProperty(entity, propertyName);
                if (baseLanTranslation != null){
                    String localeToSpecify = locales.contains("en") ? "en" : TLSUtils.getEffectiveLangCode();
                    res.put(localeToSpecify, baseLanTranslation);
                }
            }
        }
        return res;
    }
    
    public String getPropertyName()
    {
        return propertyName;
    }
    
    public Class<?> getClazz()
    {
        return this.clazz;
    }
    
    
    public List<String> getLocales()
    {
        return this.locales;
    }
    
    
    public Map<String, String> getTranslations()
    {
        return this.translations;
    }
    
    
    public String getPrefix()
    {
        return this.prefix;
    }
    
    
    @Override
    public String toString()
    {
        return String.format("MIFV: %s for locales: %s, values: %s", this.prefix, this.locales, this.translations);
    
    }
    
    /**
     * import from a request the values of a field, so that the same instance can be used later
     * @param request
     */
    public void populateTranslations(HttpServletRequest request)
    {
        this.translations.clear();
        for(String param:request.getParameterMap().keySet())
        {
            Pair<String, String> localeValuePair = readParameter(param, prefix, request);
            if (localeValuePair == null)
                continue;
            String locale = localeValuePair.getKey(); 
            String translation = localeValuePair.getValue();
            translations.put(locale, translation);
        }
    }
    
    public static Pair<String, String> readParameter(String param, String prefix, HttpServletRequest request)
    {
        if (!param.startsWith(prefix + "_"))
            return null;
        String locale = param.substring(prefix.length() + 1); // skip the prefix and "_"
        String translation = request.getParameter(param); // this is the value submitted by multilingualFieldEntry.jsp
        
        if (translation == null || translation.isEmpty())
            return null;  // user did not input anything -> he might have deleted an already-input translation
        
        Pair<String, String> res = Pair.of(locale, translation);
        return res;
    }
    
    /**
     * returns the default name of an entity stored in the request, taking, in order of precedence:
     * 1. current language
     * 2. base language
     * 3. any other language (as specified by {@link #readTranslationsFromRequest(Class, Long, String, String, HttpServletRequest)}
     * @param clazz - the class of the stored entity
     * @param propertyName - the name of the property whose name we want taken
     * @param suffix - the page-specific suffix (relevant when saving multiple entities of the same type per page)
     * @param request
     * @return
     */
    public static String getDefaultName(Class<?> clazz, String propertyName, String suffix, HttpServletRequest request){
        Map<String, AmpContentTranslation> acts = readTranslationsFromRequest(clazz, 1L, propertyName, suffix, request);
        return acts.get("currentLanguage").getTranslation();
    }
    
    /**
     * reads all the translations of an entity's field values stored in the request and puts them in a Map, indexed by locale
     * the base language gets special treatment, as a value is put there based on a best-effort algorithm. The algorithm is as followes:
     * 1. if the user specified a translation in the base language, use it
     * 2. if he did not, take any other translation
     * 3. if the last step fails, put 'dummy' - THIS SHOULD NOT HAPPEN!!! 
     * 
     * also the special entry "currentLanguage" is populated according to the rules:
     * I) res[TLSUtils.getEffectiveLocale()]
     * II) res[ContentTranslationUtil.getBaseLanguage()]
     * @param clazz
     * @param entityId
     * @param propertyName
     * @param suffix
     * @param request
     * @return
     */
    public static Map<String, AmpContentTranslation> readTranslationsFromRequest(Class<?> clazz, long entityId, String propertyName, String suffix, HttpServletRequest request) {
        List<Pair<String, String>> rawData = new ArrayList<>();
        String prefix = build_prefix(clazz, propertyName, suffix);
        
        for(String param:request.getParameterMap().keySet()) {
            Pair<String, String> localeValuePair = readParameter(param, prefix, request);
            if (localeValuePair != null)
                rawData.add(localeValuePair);
        }
        return populateContentTranslations(rawData, clazz, entityId, propertyName);
    }
    
    /**
     * please see the javadoc for {@link #readTranslationsFromRequest(Class, long, String, String, HttpServletRequest)} (which calls back to this) for a detailed description
     * @param rawData
     * @param clazz
     * @param entityId
     * @param propertyName
     * @return
     */
    public static Map<String, AmpContentTranslation> populateContentTranslations(List<Pair<String,String>> rawData, Class<?> clazz, long entityId, String propertyName) {
        Map<String, AmpContentTranslation> translations = new HashMap<String, AmpContentTranslation>();
        String baseLanguage = ContentTranslationUtil.getBaseLanguage();
        String baseLanguageTranslation = null; // the translation which would be put in place of "base language" if it turns out this does not exist
                
        for(Pair<String, String> localeValuePair:rawData)
        {
            if (localeValuePair == null)
                continue;
            String locale = localeValuePair.getKey(); 
            String translation = localeValuePair.getValue();
            if (baseLanguageTranslation == null && (translation != null) && !translation.trim().isEmpty())
                baseLanguageTranslation = translation;
            AmpContentTranslation trans = new AmpContentTranslation(clazz.getName(), entityId, propertyName, locale, translation);
            translations.put(locale, trans);
        }
        
        if (!translations.containsKey(baseLanguage)){ // user did not enter a value in base language
            if (baseLanguageTranslation == null){
                logger.warn(String.format("while trying to translate an entity of class %s, id = %d, property= %s, no baseLanguage found, putting 'dummy'", clazz.getName(), entityId, propertyName), new RuntimeException());
            }
            translations.put(baseLanguage, new AmpContentTranslation(clazz.getName(), entityId, propertyName, baseLanguage, baseLanguageTranslation));
        }
        String currentLanguage = TLSUtils.getEffectiveLangCode();
        AmpContentTranslation currentLanguageTranslation = translations.containsKey(currentLanguage) ? translations.get(currentLanguage) : translations.get(baseLanguage);
        translations.put("currentLanguage", currentLanguageTranslation);
        return translations;
    }
    
    /**
     * 1. sets Object.(translated-property)
     * 2. builds AmpContentTranslation entries (if multilingual is enabled) and writes these to the database
     * 3. writes the object to the DB 
     * @param obj the object whose property must be serialized in multiple languages
     * @param propertyName the obj's property which must be serialized
     * @param suffix the <b>suffix</b> used when constructing the {@link #MultilingualInputFieldValues(Class, Long, String, String, List)} instance which populated this HTTP request
     * @param session the Hibernate session which should be used to save the object. If null, then #PersistenceManager#getRequestDBSession(boolean) will be used
     * @param request the HTTP request, populated by an instance of this class and the corresponding JSP (multilingualFieldEntry.jsp), where to fetch the translated values from
     */
    public static void serialize(Object obj, String propertyName, String suffix, Session session, HttpServletRequest request)
    {
        if (obj == null)
            throw new RuntimeException("cannot serialize null!");
        
        long entityId = ContentTranslationUtil.getObjectId(obj);
        Map<String, AmpContentTranslation> translations = readTranslationsFromRequest(obj.getClass(), entityId, propertyName, suffix, request);     
        serialize (obj, propertyName, session, translations);
    }
    
    /**
     * 1. sets Object.(translated-property)
     * 2. builds AmpContentTranslation entries (if multilingual is enabled) and writes these to the database
     * 3. writes the object to the DB 
     * @param obj the object whose property must be serialized in multiple languages
     * @param propertyName the obj's property which must be serialized
     * @param session the Hibernate session which should be used to save the object. If null, then #PersistenceManager#getRequestDBSession(boolean) will be used
     * @param translations the translations to set
     */
    public static void serialize(Object obj, String propertyName, Session session, Map<String, AmpContentTranslation> translations)
    {
        if (obj == null)
            throw new RuntimeException("cannot serialize null!");
                    
        if (session == null)
            session = PersistenceManager.getSession();
        
        long entityId = ContentTranslationUtil.getObjectId(obj);
        
        // overwrite property and update entity
        String translationToSet = translations.get("currentLanguage").getTranslation();
        if (translationToSet != null) {
            ContentTranslationUtil.setProperty(obj, propertyName, translationToSet);
            session.saveOrUpdate(obj);
            session.flush();
        
            // prepare for writing trn in the DB
            ContentTranslationUtil.evictEntityFromCache(obj);
            FieldTranslationPack ftp = new FieldTranslationPack(obj.getClass().getName(), propertyName);
            for(AmpContentTranslation trn:translations.values())
                ftp.add(trn);
            ContentTranslationUtil.saveFieldTranslations(entityId, ftp);
        }
        //ContentTranslationUtil.saveOrUpdateContentTrns(translations.values());
    }
    
//  private static String chooseBaseLanguageTranslation(Object obj, String propertyName, String baseLanguageTranslation)
//  {
//      if (baseLanguageTranslation != null)
//          return baseLanguageTranslation;
//      return (String) ContentTranslationUtil.getProperty(obj, propertyName);
//  }
}
