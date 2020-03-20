package org.digijava.module.translation.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.util.string.Strings;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedPropertyDescription;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author aartimon@developmentgateway.org
 */
public class ContentTranslationUtil {
    private static Logger logger = Logger.getLogger(ContentTranslationUtil.class);
    private static final AbstractCache cache = DigiCacheManager.getInstance().getCache(ContentTranslationUtil.class.getName());

    /**
     * Called by the hibernate interceptor onLoad method in order to load the translations
     * for the current entity
     *
     * @param obj current entity
     * @param id object id
     * @param state fields in the current state
     * @param propertyNames field names
     * @param types field types
     * @return true if the entity state was modified
     */
    public static boolean translateObject(Object obj, Serializable id, Object[] state, String[] propertyNames){
        boolean stateModified = false;
        String currentLocale = TLSUtils.getEffectiveLangCode(); // TODO-CONSTANTIN, Arty, please review this change: effectiveLangCode is NEVER null
        if (currentLocale == null)
            return stateModified;

        String objClass = getObjectClassName(obj);
        try{
            /*
                Iterate all String fields and replace them with their translation
             */
            InternationalizedModelDescription entityDescription = InternationalizedModelDescription.getForClass(obj.getClass());
            for(int i = 0; i < propertyNames.length; i++)
            {
                String fieldName = propertyNames[i];
                InternationalizedPropertyDescription prop = entityDescription.properties.get(fieldName);
                if (prop == null)
                    continue; //this field is not internationalized
                
                // get translations for current field
                String trnLocale = loadFieldTranslationInLocale(objClass, (Long)id, fieldName, currentLocale);
                if (trnLocale != null)
                {
                    logger.debug("translate field from: " + state[i] + " to: " + trnLocale);
                    //replace the value of the field with the translation in the current locale
                    state[i] = trnLocale;
                    stateModified = true;
                }
            }
        } catch (Exception e){
            logger.error("Can't translate object", e);
        }
        return stateModified;
    }


    /**
     * Method to generate a FieldTranslationPack with all the translations available for the
     * object specified in the parameters. The FTP will be inserted in the TranslationStore and the
     * identifier will be returned
     *
     *
     * @param objClass class for your object
     * @param objId object id
     * @param fieldName field name
     * @param currentLocale language in which the field has been modified
     * @param fieldTrnCurrentLocale new value for the field in the currentLocale
     * @param formFieldTrns extra translations from the activity form
     *
     * @return the identifier for the object in the TranslationStore
     */
    private static Long getFieldTrnPack(Class clazz, String objClass, Long objId, String fieldName, String currentLocale, String fieldTrnCurrentLocale, List<AmpContentTranslation> formFieldTrns){
        //get old translations for current field
        List<AmpContentTranslation> currentTranslations = loadFieldTranslations(objClass, objId, fieldName);
        //create the FieldTranslationPack object
        FieldTranslationPack trnPack = new FieldTranslationPack(objClass, fieldName);
        if (currentTranslations != null){ //if we have trns from the db, add them to the list
            for (AmpContentTranslation ampContentTranslation : currentTranslations) {
                trnPack.add(ampContentTranslation);
            }
        }

        if (formFieldTrns != null){//override the translations from the db with the ones from the form if available
            for (AmpContentTranslation act: formFieldTrns)
                trnPack.add(act);
        }


        //update the translation for the current locale
        trnPack.add(currentLocale, fieldTrnCurrentLocale);

        if (trnPack.get(getBaseLanguage()) == null){
            //retrieve the current value from the db and set it as the base language translation
            String baseTrn = (String)loadFieldFromDb(clazz, objId, fieldName);
            if (baseTrn != null)
                trnPack.add(getBaseLanguage(), baseTrn);
        }

        return TranslationStore.insert(trnPack);
    }

    /**
     * Method that will clone the translations for all the translatable fields
     * in the current object
     *
     * @param obj Object that needs translation cloning
     */
    public static void cloneTranslations(Object obj){
        cloneTranslations(obj, null);
    }

    /**
     * gets a property of an object
     * @param obj
     * @param propertyName
     * @return
     */
    public static Object getProperty(Object obj, String propertyName)
    {
        try{
            Method methGetField = obj.getClass().getMethod("get" + Strings.capitalize(propertyName));
            Object fieldValue = methGetField.invoke(obj);
            return fieldValue;
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     * set a property of an object
     * @param obj
     * @param propertyName
     * @param propertyValue
     * @return
     */
    public static void setProperty(Object obj, String propertyName, Object propertyValue)
    {
        try{
            Method methGetField = obj.getClass().getMethod("set" + Strings.capitalize(propertyName), propertyValue.getClass());
            methGetField.invoke(obj, propertyValue);            
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public static boolean multilingualIsEnabled()
    {
        return "true".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MULTILINGUAL));
    }
    
    /**
     * Method that will clone the translations for all the translatable fields
     * in the current object; see [// item #00000001] for the place where the id's are used
     *
     * @param obj Object that needs translation cloning
     * @param formTranslations the list of translations that were modified using the activity form
     */
    @SuppressWarnings("unchecked")
    public static void cloneTranslations(Object obj, Collection<AmpContentTranslation> formTranslations)
    {
        //check if multilingual is enabled
        if (!multilingualIsEnabled())
            return;
        cloneTranslations(obj, formTranslations, new HashSet<String>());
    }
    
    /**
     * Method that will clone the translations for all the translatable fields
     * in the current object; see [// item #00000001] for the place where the id's are used
     *
     * @param obj Object that needs translation cloning
     * @param formTranslations the list of translations that were modified using the activity form
     * @param processed keeps track of processed object to avoid infinite recursive calls via circular references 
     * and it should be empty at the first function call
     */
    private static void cloneTranslations(Object obj, Collection<AmpContentTranslation> formTranslations, Set<String> processed){
        Hibernate.initialize(obj);
        String objClass = getObjectClassName(obj);
        Long objId = getObjectId(obj);
        
        String processedId = objClass+System.identityHashCode(obj);
        if( processed.contains(processedId) ) return;
        processed.add(processedId);
        
        String currentLocale = TLSUtils.getEffectiveLangCode();

        Class clazz = getObjectClass(obj);
        try{
            /*
                Iterate all String fields and replace their contents with an id pointing
                to an FieldTranslationPack containing the old translations of the field
                plus the updated translation in the currentLocale
             */
            ArrayList<Field> fields = new ArrayList<Field>();
            getAllFields(fields, clazz);
            for (Field field : fields) {
                if (field.getAnnotation(TranslatableField.class) != null) {
                    //retrieve updated translation in the current locale
                    String fieldName = field.getName();
                    String fieldTrnCurrentLocale = (String) getProperty(obj, fieldName);
                    //create a list with translations from the activity form
                    List<AmpContentTranslation> formFieldTrns = null;
                    if (formTranslations != null){//if request is coming from the activity form save
                        Long revObjId = (objId == null ? System.identityHashCode(obj) : objId);
                        for (AmpContentTranslation ft: formTranslations){
                            if (ft.getObjectClass().equals(objClass) && ft.getObjectId().equals(revObjId) &&
                                    ft.getFieldName().equals(fieldName) && ft.getTranslation() != null){
                                //we're not taking into consideration ACT's with the translation null, these haven't been filled
                                if (formFieldTrns == null)
                                    formFieldTrns = new ArrayList<AmpContentTranslation>();
                                formFieldTrns.add(ft);
                            }
                        }
                    }

                    //generate FTP with old translations + insert updated translation
                    Long packId = getFieldTrnPack(clazz, objClass, objId, fieldName, currentLocale, fieldTrnCurrentLocale, formFieldTrns);
                    //replace the value of the field with the identifier for the FTP from the TranslationStore
                    Method methSetField = clazz.getMethod("set" + Strings.capitalize(fieldName), String.class);
                    methSetField.invoke(obj, String.valueOf(packId));
                } else if (Collection.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType())) {
                    //if we have a collection of objects in the current entity, then we look for translatable
                    //entities and we clone their translations recursively
                    String fieldName = field.getName();
                    Method methGetField = null;
                    try
                    {
                        methGetField = clazz.getMethod("get" + Strings.capitalize(fieldName));
                    }
                    catch(NoSuchMethodException e)
                    {
                        // do nothing -> we won't process this field and that's it
                    }
                    if (methGetField != null)
                    {
                        // no method -> no getter -> no need to clone translations
                        Collection collection = (Collection) methGetField.invoke(obj);
                        if (collection != null) {
                            for (Object o : collection) {
                                if (o.getClass().isAnnotationPresent(TranslatableClass.class) &&
                                    !o.getClass().isAssignableFrom(AmpActivityVersion.class) //not supported
                                    )
                                cloneTranslations(o, formTranslations, processed);
                                else {
                                    //we don't have mixed collections, no point in iterating forward through the collection
                                    break;
                                }
                            }
                        }
                    }
                }else if( field.getType().isAnnotationPresent(TranslatableClass.class) && Versionable.class.isAssignableFrom(field.getType())) { //scan deeper levels
                    String fieldName = field.getName();
                    Method methGetField = clazz.getMethod("get" + Strings.capitalize(fieldName));
                    Object o = methGetField.invoke(obj);
                     
                    if(o!=null) {
                        cloneTranslations(o, formTranslations, processed);
                    }
                }
            }
        } catch (Exception e){
            logger.error("Can't clone translations", e);
        }
        //logger.info("Done cloning " + obj.getClass().getName());
    }

    /**
     * Method to prepare the translations for save and restore the object previous state of the object
     * so Hibernate can execute proper updates on it. Also we restore the currentState of the object to
     * use the translations in the base language
     *
     * @param obj current object
     * @param id object id
     * @param previousState array with the previousState of the object, altered at onLoad
     * @param currentState current state of the object
     * @param propertyNames list of field names
     * @param types list of field types
     * @return true if we altered the object state in any way
     */
    public static boolean prepareTranslations(Object obj, Serializable id, Object[] previousState, Object[] currentState,
            String[] propertyNames){
        boolean stateModified = false;
        boolean isVersionable = obj instanceof Versionable;
        //get new object id - hibernate already updated it
        Long objectId = (Long)id;
        Class clazz = Hibernate.getClass(obj);
        String objectClass = clazz.getName();
        String currentLocale = TLSUtils.getEffectiveLangCode();
        try {
            /*
                Iterate all String fields and replace their contents with the base language
                translation and prepare for save all the FieldTranslationPack's in the database with the
                new object id
             */
            InternationalizedModelDescription entityDescription = InternationalizedModelDescription.getForClass(clazz);
            for (int i = 0; i < propertyNames.length; i++){             
                String fieldName = propertyNames[i];
                InternationalizedPropertyDescription propertyDescription = entityDescription.properties.get(fieldName);
                if (propertyDescription == null)
                    continue; // field not translated -> nothing to do
                Field field = propertyDescription.field;
                
                // all translateable fields are of type String, so comparing currentState[i].previousState[i] is ok  (they are both String instances)  
                if ((previousState == null || (StringUtils.isNotBlank((String)currentState[i]) && !currentState[i].equals(previousState[i])))){
                    // "field.getAnnotation(TranslatableField.class) != null" deleted from "if", as InternationalizedModelDescription only contains translateable fields
                    FieldTranslationPack ftp;
                    Long ftpId;
                    if (isVersionable){ //if versionable we already cloned the translations
                        //FTP already generated, just prepare it for save
                        ftpId = Long.parseLong((String)currentState[i]); // item #00000001
                    }
                    else{ //not versionable entity => we don't alter the other translations
                        //create FTP
                        String value = (String)currentState[i];
                        //insert a FieldTranslationPack with just one entry
                        FieldTranslationPack trnPack = new FieldTranslationPack(objectClass, fieldName);
                        trnPack.add(currentLocale, value);
                        ftpId = TranslationStore.insert(trnPack);
                    }
                    //retrieve the FieldTranslationPack from the store
                    ftp = TranslationStore.prepareForSave(ftpId, objectClass, objectId);
                    if (ftp == null)
                        throw new AssertionError("Can't get the field translation pack ... should be in the cache!");
                    //restore the base translation to the current object
                    String baseTranslation = null;
                    if (isVersionable)
                        //base translation should be in the FTP
                        baseTranslation = ftp.getNonNullBaseTrn(getBaseLanguage(), currentLocale);
                    else
                        if (notInBaseLanguage()){
                            //for non-versionable entities just load the base translation from the db
                            baseTranslation = loadFieldTranslationInLocale(objectClass, objectId, fieldName, getBaseLanguage());
                        }

                    if (baseTranslation == null){
                        logger.debug("Object without base translation");
                        //create base translation for object
                        if (notInBaseLanguage()) //if we're in the base language we don't load the field
                            baseTranslation = (String) loadFieldFromDb(clazz, objectId, fieldName);
                        if (baseTranslation == null) //new object, hasn't been saved yet
                            baseTranslation = (String) currentState[i];
                        ftp.add(getBaseLanguage(), baseTranslation);
                    }

                    //restore current state to the translation in the base language
                    currentState[i] = baseTranslation;
                    stateModified = true;

                    logger.debug("Updated base translation with: " + baseTranslation + " (currentlocale =" + TLSUtils.getLangCode() + ")");
                    if (previousState != null && previousState[i] != null){
                        //since we changed the object on load, we need to change the previous state to the db state
                        /**
                         * AMP-17055: the Hibernate javadoc for onFlushDirty() says: It is strongly recommended that the interceptor not modify the previousState.
                         * on the other hand, the commit for AMP-16296 specifically introduces these lines of code. 
                         * The value written to previousState[] is, at some point in time, written by Hibernate to the database and then overwritten with a fresh value
                         * AMP-17055 is happening because, when creating a new translateable entity from scratch inside a transaction:
                         *  - a null value is written by this code into previousState[]
                         *  - Hibernate fails to write the said null value to the database, but it fails because of a NotNull constraint on the column
                         *  - it doesn't matter that Hibernate intends to overwrite the null value with a correct one afterwards - the INSERT fails, 
                         *  so the whole transaction fails
                         *  
                         *  The Hibernate documentation is very fishy regarding why previousState[] shouldn't be modified; also I wasn't able to find any specification regarding the valid cases when it should
                         *  fixing through a half-baked hack: will only write values IF THEY ALREADY EXIST IN THE DATABASE in the understanding/hope that:
                         *   > they will be overwritten anyway
                         *   > they will obey to any kind of constraints the column might have (probably not-null-only, as only strings are translateable and I can't imagine strings being used as FK's)
                         *   > not writing a simple "if null" hack because other constraints might theorethically be used in the future (MAX-LENGTH etc)
                         */
                        List<Object> prevState = loadFieldFromDbList(clazz, objectId, fieldName);
                        if ((prevState != null) && (prevState.size() == 1))
                            previousState[i] = prevState.get(0); // AMP-17055: only overwrite value if it really existed in the database
                    }

                }
            }

        } catch(Exception e) {
            logger.error("Can't restore translations:", e);
        }
        
        return stateModified;
    }

    private static boolean notInBaseLanguage() {
        return !getBaseLanguage().equals(TLSUtils.getLangCode());
    }


    /**
     * @param objClass class of the current object
     * @param objId object id
     * @return cache key built from the two parameters
     */
    private static String getCacheKey(String objClass, Long objId){
        return objClass+objId;
    }

    /**
     * evict the translations associated with the object from the cache
     * @param entity current object
     */
    public static void evictEntityFromCache(Object entity){
        String key = getCacheKey(getObjectClassName(entity), getObjectId(entity));
        cache.evict(key);
    }

    /**
     * Method to load all the available translations for an object, using a
     * provided session. Useful when you need the translations loaded in a new session.
     *
     * @param session session to use for loading entities
     * @param objClass class of the current object
     * @param objId object id
     * @return list of available translations
     */
    private static List<AmpContentTranslation> loadTranslations(Session session, String objClass, Long objId){
        return loadTranslations(session, objClass, objId, null);
    }

    /**
     * Method to load all the available translations for an object, using a
     * provided session. Useful when you need the translations loaded in a new session.
     *
     * @param session session to use for loading entities
     * @param objClass class of the current object
     * @param objId object id
     * @param fieldName name of the field
     * @return list of available translations
     */
    private static List<AmpContentTranslation> loadTranslations(Session session, String objClass, Long objId, String fieldName){
        //method is used to build the cache
        Criteria criteria = session.createCriteria(AmpContentTranslation.class);
        criteria.add(Restrictions.eq("objectClass", objClass));
        criteria.add(Restrictions.eq("objectId", objId));
        criteria.setCacheable(true);
        if (fieldName != null){
            criteria.add(Restrictions.eq("fieldName", fieldName));
        }
        return criteria.list();
    }

    /**
     * Gets all of the translations associated with a field of an object, if the translations are not
     * present in the cache, the method will fetch them from the database
     * @param objClass class of the current object
     * @param objId object id
     * @param fieldName name of the field
     * @return map between locale and the translation object
     */
    @SuppressWarnings("unchecked")
    private static HashMap<String, AmpContentTranslation> loadCachedFieldTranslations(final String objClass, final Long objId, String fieldName){
         HashMap<String, HashMap<String, AmpContentTranslation>> outerfieldMap = (HashMap<String, HashMap<String, AmpContentTranslation>>) cache.get(getCacheKey(objClass, objId));
        if (outerfieldMap== null){
            PersistenceManager.getSession().doWork(
                    new org.hibernate.jdbc.Work() {
                        public void execute(Connection conn) throws SQLException {
                            Session newSession = null;
                            try {
                                HashMap<String, HashMap<String, AmpContentTranslation>> fieldMap;
                                newSession = PersistenceManager.sf().withOptions().connection(conn).openSession();
                                
                                List<AmpContentTranslation> list;
                                synchronized (sessionLock){
                                    //we use the current session to be sure we load the latest values
                                    list = loadTranslations(newSession, objClass, objId);
                                }
                                fieldMap = new HashMap<String, HashMap<String, AmpContentTranslation>>();
                                for (AmpContentTranslation t: list){
                                    HashMap<String, AmpContentTranslation> localeMap = fieldMap.get(t.getFieldName());
                                    if (localeMap == null){
                                        localeMap = new HashMap<String, AmpContentTranslation>();
                                        fieldMap.put(t.getFieldName(), localeMap);
                                    }
                                    localeMap.put(t.getLocale(), t);
                                }
                                cache.put(getCacheKey(objClass, objId), fieldMap);                                
                            }
                            catch(Exception ex){
                                logger.error("Cannot retreive translations",ex);
                            }
                            finally{
                                newSession.close();
                            }
                            
                        }
                    });
            outerfieldMap=(HashMap<String, HashMap<String, AmpContentTranslation>>) cache.get(getCacheKey(objClass, objId));
        }
        if(outerfieldMap==null)
            return null;
        else
            return outerfieldMap.get(fieldName);
    }

    /**
     * Get a translation for a field
     * @param objClass class of the current object
     * @param objId object id
     * @param fieldName name of the field
     * @param locale language in which the translation is requested
     * @return translation object
     */
    public static AmpContentTranslation loadCachedFieldTranslationsInLocale(String objClass, Long objId, String fieldName, String locale){
        HashMap<String, AmpContentTranslation> localeMap = loadCachedFieldTranslations(objClass, objId, fieldName);
        if (localeMap == null)
            return null;
        return localeMap.get(locale);
    }

    /**
     * Method to get all the available translations for the field of an object
     * @param objClass class of the current object
     * @param objId object id
     * @param fieldName name of the field
     * @return list of available translations
     */
    public static List<AmpContentTranslation> loadFieldTranslations(String objClass, Long objId, String fieldName){
        HashMap<String, AmpContentTranslation> map = loadCachedFieldTranslations(objClass, objId, fieldName);
        if (map == null)
            return new ArrayList<AmpContentTranslation>();
        return new ArrayList<AmpContentTranslation>(map.values());
    }

    /**
     * Loads the value of the specified field straight from the database avoiding
     * the Hibernate sessions and caches
     * (uses stateless session)
     *
     * @param clazz object class
     * @param id object id
     * @param fieldName name of the field
     * @return database value for the field
     */
    public static Object loadFieldFromDb(Class clazz, Long id, String fieldName){
        if (clazz == null || id == null || fieldName == null)
            return null;

        List<Object> results = loadFieldFromDbList(clazz, id, fieldName);
        if (results != null && (results.size() == 1))
            return results.get(0);
        
        return null;
    }

    /**
     * Loads the value of the specified field straight from the database avoiding
     * the Hibernate sessions and caches
     * (uses stateless session)
     * returns empty list of entity does not exist in the database
     *
     * @param clazz object class
     * @param id object id
     * @param fieldName name of the field
     * @return database value for the field
     */
    public static List<Object> loadFieldFromDbList(Class clazz, Long id, String fieldName){
        if (clazz == null || id == null || fieldName == null)
            return null;

        StatelessSession session = null;
        try{        
            session =PersistenceManager.openNewStatelessSession(); //this does the trick, doesn't work when entity contains collections
            StringBuilder query = new StringBuilder();
            query.append("select c.");
            query.append(fieldName);
            query.append(" from ");
            query.append(clazz.getName());
            String objIdField = getObjectIdField(clazz, PersistenceManager.sf());
            query.append(" c where c.");
            query.append(objIdField);
            query.append("=:id");
            Query qry = session.createQuery(query.toString());
            qry.setLong("id", id);
            return qry.list();
        } catch (Exception e) {
            logger.error("can't load object from database", e);
        } finally {
            if (session != null)
                session.close();
        }
        return null;
    }
    //this session was used to load values since now the values are loaded within the same transaction
    //we have no need to use a new one
    //private static Session session = PersistenceManager.openNewSession();

    private static Object sessionLock = new Object();
    
    /**
     * Get's the translation for a field in a specified locale
     *
     * @param objClass class of object
     * @param objId if of object
     * @param fieldName name of field
     * @param locale current locale
     * @return translation for field
     */
    public static String loadFieldTranslationInLocale(String objClass, Long objId, String fieldName, String locale){
        AmpContentTranslation ampContentTranslation = loadCachedFieldTranslationsInLocale(objClass, objId, fieldName, locale);
        if (ampContentTranslation == null)
            return null;
        return ampContentTranslation.getTranslation();
    }

    /**
     * Save all the translations in a FieldTranslationPack, deleting the old ones
     * if they exist
     * (uses a new session)
     *
     * @param newId the object id for the translations
     * @param ftp pack with field translations
     */
    public static void saveFieldTranslations(final Long newId,
            final FieldTranslationPack ftp) {
        PersistenceManager.getSession().doWork(
            new org.hibernate.jdbc.Work() {
                public void execute(Connection conn) throws SQLException {
                    Session newSession = null;
                    try {
                        newSession = PersistenceManager.sf().withOptions().connection(conn).openSession();

                        String objClass = ftp.getObjClass();
                        String fieldName = ftp.getFieldName();

                        // load the translations from db, not cache
                        List<AmpContentTranslation> oldTrns = loadTranslations(newSession, objClass, newId, fieldName);

                        // first, delete all translations
                        for (AmpContentTranslation oldTrn : oldTrns) {
                            newSession.delete(oldTrn);
                        }

                        HashMap<String, String> trns = ftp.getTranslations();
                        Set<String> locales = trns.keySet();

                        // then add new ones
                        for (String locale : locales) {
                            String trn = trns.get(locale);
                            AmpContentTranslation act = new AmpContentTranslation(objClass, newId, fieldName, locale, trn);
                            newSession.save(act);
                        }

                        newSession.flush();
                    } 
                    catch (Exception e) {
                        logger.error("can't save field translations", e);
                        if (e.getCause() != null && e.getCause() instanceof SQLException && ((SQLException) e.getCause()).getNextException() != null)
                            logger.error("Next exception: "+ ((SQLException) e.getCause()).getNextException());
                    }
                    finally {
                        newSession.close();
                    }
                }
            });
    }

    public static AmpContentTranslation getByTypeObjidFieldLocale (List<AmpContentTranslation> allTrns,
                                                                   String type, Long objId,
                                                                   String field, String locale) {
        AmpContentTranslation retVal = null;

        for (AmpContentTranslation obj : allTrns) {
            if (obj.getObjectClass().equals(type) && obj.getObjectId().equals(objId) &&
                    obj.getFieldName().equals(field) && obj.getLocale().equals(locale)) {
                retVal = obj;
                break;
            }
        }

        return retVal;
    }

    public static void deleteFieldTranslations(Long objectId, Object entity) {
        Session session = null;
        try {
            session = PersistenceManager.openNewSession();

            String objClass = getObjectClassName(entity);
            StringBuilder query = new StringBuilder();
            query.append("delete from ");
            query.append(AmpContentTranslation.class.getName());
            query.append(" t where t.objectClass=:objectClass");
            query.append(" and t.objectId=:objectId");
            Query qry = session.createQuery(query.toString());
            qry.setString("objectClass", objClass);
            qry.setLong("objectId", objectId);
            qry.executeUpdate();
        } catch (Exception e) {
            logger.error("Can't delete field translations", e);
        } finally {
            PersistenceManager.closeSession(session);
        }
    }

    public static List<AmpContentTranslation> getContentTranslationsByTypes() {
        return getContentTranslationsByTypes(null);
    }

    public static List<AmpContentTranslation> getContentTranslationsByTypes(List<String> types) {
        List<AmpContentTranslation> retVal = null;
        StringBuilder typesWhereClause = null;
        if (types != null && !types.isEmpty()) {
            typesWhereClause = new StringBuilder();
            Iterator<String> it = types.iterator();
            while (it.hasNext()) {
                typesWhereClause.append("'").append(it.next()).append("'");
                if (it.hasNext()) {
                    typesWhereClause.append(",");
                }
            }
        }

        StringBuilder qs = new StringBuilder("from ").append(AmpContentTranslation.class.getName()).append(" ct");

        if (typesWhereClause != null) {
            qs.append(" where ct.objectClass in (").append(typesWhereClause).append(")");
        }
        qs.append(" order by ct.objectId");

        return (List<AmpContentTranslation>) PersistenceManager.getSession().createQuery(qs.toString()).list();
    }

    public static List<String[]> getContentTrnUniqueTypesAndLangs() {
        StringBuilder qs = new StringBuilder("select distinct ct.objectClass, ct.locale from ").
                append(AmpContentTranslation.class.getName()).
                append(" ct");
        return (List<String[]>) PersistenceManager.getSession().createQuery(qs.toString()).list();
    }

    public static Collection<ContentTrnObjectType> getContentTranslationUniques() {
        List uniqs = getContentTrnUniqueTypesAndLangs();
        Map<String, ContentTrnObjectType> trnObjectInfoCreator = new HashMap<String, ContentTrnObjectType>();

        Iterator uniqIt = uniqs.iterator();
        for (Object uniq : uniqs) {
            String objType = (String)((Object[])uniq)[0];
            if (!trnObjectInfoCreator.containsKey(objType)) {
                ContentTrnObjectType newObj = new ContentTrnObjectType(objType);
                trnObjectInfoCreator.put(objType, newObj);
            }

            ContentTrnObjectType exObj = trnObjectInfoCreator.get(objType);
            exObj.addLangKey((String)((Object[])uniq)[1]);
        }

        Collection<ContentTrnObjectType> displayBeans =
                trnObjectInfoCreator.values();

        for (ContentTrnObjectType displayBean : displayBeans) {
            String contentObjType = displayBean.getObjectType();
            Class clazz = null;
            String displayName = null;
            try {
                clazz = Class.forName(contentObjType);
                Annotation a = clazz.getAnnotation(TranslatableClass.class);
                displayName = (String) (a.annotationType().getMethod("displayName").invoke(a));
                displayBean.setObjectDisplayName(displayName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (NoSuchMethodException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        return displayBeans;
    }

    /**
     * @return the base language for the application
     */
    public static String getBaseLanguage(){
        //TODO:
        //TODO: move to category Manager
        //TODO:
        return "en";
    }

    private static String getObjectIdField(Class clazz, SessionFactory sf){
        return sf.getClassMetadata(clazz).getIdentifierPropertyName();
    }

    public static Long getObjectId(Object obj){
        try{
            Session session = PersistenceManager.getRequestDBSession();
            Class<?> clazz = Hibernate.getClass(obj);// obj.getClass();
            String objIdField = getObjectIdField(clazz, session.getSessionFactory());
            return (Long) getProperty(obj, objIdField);
        } catch (Exception e){
            logger.error("Can't get object id:", e);
        }
        return null;
    }

    public static Class getObjectClass(Object obj) {
        return Hibernate.getClass(obj);
        //return obj.getClass();
    }
    
    public static String getObjectClassName(Object obj){
        return getObjectClass(obj).getName();
    }

    /**
     * Get all fields of a class  (safe for inheritance)
     * @param fields list of fields to be populated
     * @param type class for which we want to get all the fields
     */
    public static void getAllFields(List<Field> fields, Class<?> type) {
        Collections.addAll(fields, type.getDeclaredFields());
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
    }

//    /**
//     * Retrieve the Field for a class (safe for inheritance)
//     * @param type class for which we want to get all the fields
//     * @param name field name
//     * @return the Field object representing
//     */
//    private static Field getFieldByName(Class<?> type, String name){
//        for (Field field: type.getDeclaredFields()) {
//            if (field.getName().equals(name))
//              return field;
//        }
//
//        if (type.getSuperclass() != null) {
//            return getFieldByName(type.getSuperclass(), name);
//        }
//        return null;
//    }

    public static void saveOrUpdateContentTrns(Collection <AmpContentTranslation> objectsForDb) {
        try {
            for (AmpContentTranslation trn : objectsForDb) {
                DbUtil.saveOrUpdateObject(trn);
                //update default entry in BaseLanguage - needed when multilingual is disabled 
                if( getBaseLanguage().equalsIgnoreCase(trn.getLocale()) ){
                    DbUtil.updateField(trn.getObjectClass(), trn.getObjectId(),trn.getFieldName(), trn.getTranslation() );
                }
            }
        } catch (Exception e) {
            logger.error("error saving trns!", e);
        }

    }

}
