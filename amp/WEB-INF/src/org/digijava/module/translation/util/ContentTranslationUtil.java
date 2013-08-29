package org.digijava.module.translation.util;

import org.apache.log4j.Logger;
import org.apache.wicket.util.string.Strings;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.aim.dbentity.Versionable;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxyHelper;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @author aartimon@developmentgateway.org
 */
public class ContentTranslationUtil {
    private static Logger logger = Logger.getLogger(ContentTranslationUtil.class);

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
    public static boolean translateObject(Object obj, Serializable id, Object[] state, String[] propertyNames, Type[] types){
    	boolean stateModified = false;
        String currentLocale = TLSUtils.getLangCode();
        if (currentLocale == null)
            return stateModified;

        String objClass = getObjectClass(obj);
        Class clazz = obj.getClass();
        try{
            /*
                Iterate all String fields and replace them with their translation
             */
            for (int i = 0; i < types.length; i++){
            	String fieldName = propertyNames[i];
            	Field field = getFieldByName(clazz, fieldName);
                if (field.getAnnotation(TranslatableField.class) != null){
                    //get translations for current field
                    String trnLocale = loadFieldTranslationInLocale(objClass, (Long)id, fieldName, currentLocale);
                    if (trnLocale != null){
                    	logger.debug("translate field from: " + state[i] + " to: " + trnLocale);
                        //replace the value of the field with the translation in the current locale
                        state[i] = trnLocale;
                        stateModified = true;
                    }
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
     * @param objClass class for your object
     * @param objId object id
     * @param fieldName field name
     * @param currentLocale language in which the field has been modified
     * @param fieldTrnCurrentLocale new value for the field in the currentLocale
     * @return the identifier for the object in the TranslationStore
     */
    private static Long getFieldTrnPack(String objClass, Long objId, String fieldName, String currentLocale, String fieldTrnCurrentLocale){
        //get old translations for current field
        List<AmpContentTranslation> currentTranslations = loadFieldTranslations(objClass, objId, fieldName);
        //create the FieldTranslationPack object
        FieldTranslationPack trnPack = new FieldTranslationPack(objClass, fieldName);
        if (currentTranslations != null){
            for (AmpContentTranslation ampContentTranslation : currentTranslations) {
                trnPack.add(ampContentTranslation);
            }
        }

        //update the translation for the current locale
        trnPack.add(currentLocale, fieldTrnCurrentLocale);
        return TranslationStore.insert(trnPack);
    }

    /**
     * Method that will clone the translations for all the translatable fields
     * in the current object
     *
     * @param obj Object that needs translation cloning
     */
    public static void cloneTranslations(Object obj){
        Hibernate.initialize(obj);
        String objClass = getObjectClass(obj);
        String currentLocale = TLSUtils.getLangCode();

        Class clazz = obj.getClass();
        try{
            Long objId = getObjectId(obj);

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
                    Method methGetField = clazz.getMethod("get" + Strings.capitalize(fieldName));
                    String fieldTrnCurrentLocale = (String) methGetField.invoke(obj);
                    //generate FTP with old translations + insert updated translation
                    Long packId = getFieldTrnPack(objClass, objId, fieldName, currentLocale, fieldTrnCurrentLocale);
                    //replace the value of the field with the identifier for the FTP from the TranslationStore
                    Method methSetField = clazz.getMethod("set" + Strings.capitalize(fieldName), String.class);
                    methSetField.invoke(obj, String.valueOf(packId));
                } else if (Collection.class.isAssignableFrom(field.getType()) || Set.class.isAssignableFrom(field.getType())) {
                    //if we have a collection of objects in the current entity, then we look for translatable
                    //entities and we clone their translations recursively
                    String fieldName = field.getName();
                    Method methGetField = clazz.getMethod("get" + Strings.capitalize(fieldName));
                    Collection collection = (Collection) methGetField.invoke(obj);
                    if (collection != null) {
                        for (Object o : collection) {
                            if (o.getClass().isAnnotationPresent(TranslatableClass.class) &&
                                    !o.getClass().isAssignableFrom(AmpActivityVersion.class) //not supported
                                    )
                                cloneTranslations(o);
                            else {
                                //we don't have mixed collections, no point in iterating forward through the collection
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            logger.error("Can't clone translations", e);
        }
        logger.info("Done cloning");
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
    		String[] propertyNames, Type[] types){
    	boolean stateModified = false;
        boolean isVersionable =  obj instanceof Versionable;
    	//get new object id - hibernate already updated it
        Long objectId = (Long)id;
        Class clazz = Hibernate.getClass(obj);
        String objectClass = clazz.getName();
        String currentLocale = TLSUtils.getLangCode();
        try {
            /*
                Iterate all String fields and replace their contents with the base language
                translation and prepare for save all the FieldTranslationPack's in the database with the
                new object id
             */
            for (int i = 0; i < types.length; i++){
            	String fieldName = propertyNames[i];
            	Field field = getFieldByName(clazz, fieldName);
                if (field.getAnnotation(TranslatableField.class) != null){
                	FieldTranslationPack ftp;
                    Long ftpId;
                	if (isVersionable){ //if versionable we already cloned the translations
                		//FTP already generated, just prepare it for save
                		ftpId = Long.parseLong((String)currentState[i]);
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
                    String baseTranslation;
                    if (isVersionable)
                    	//base translation should be in the FTP
                    	baseTranslation = ftp.getNonNull(getBaseLanguage(), TLSUtils.getLangCode());
                    else
                        //for non-versionable entities just load the base translation from the db
                    	baseTranslation = loadFieldTranslationInLocale(objectClass, objectId, fieldName, getBaseLanguage());
                    
                    if (baseTranslation == null)
                    	logger.debug("Object without base translation");
                    else{
                    	logger.debug("Updated base translation with: " + baseTranslation + " (currentlocale =" + TLSUtils.getLangCode() + ")");
                    	if (previousState != null && previousState[i] != null){
                    		//since we changed the object on load, we need to change the previous state to the db state
                    		Object prevState = loadFieldFromDb(clazz, objectId, fieldName);
                    		previousState[i] = prevState;
                    	}
                        //restore current state to the translation in the base language
                    	currentState[i] = baseTranslation;
                    	stateModified = true;
                    }
                }
            }

        } catch(Exception e) {
            logger.error("Can't restore translations:", e);
        }
        
        return stateModified;
    }

    /**
     * Method to load all the available translations for the field of an object
     * @param objClass class of the current object
     * @param objId object id
     * @param fieldName name of the field
     * @return list of available translations
     */
    public static List<AmpContentTranslation> loadFieldTranslations(String objClass, Long objId, String fieldName){
        try{
        	Session session = PersistenceManager.getRequestDBSession();
        	return loadFieldTranslations(session, objClass, objId, fieldName);
        } catch (Exception e) {
            logger.error("can't load field translations", e);
        }
        return null;
    }

    /**
     * Method to load all the available translations for the field of an object, using a
     * provided session. Useful when you need the translations loaded in a new session.
     *
     * @param session session to use for loading entities
     * @param objClass class of the current object
     * @param objId object id
     * @param fieldName name of the field
     * @return list of available translations
     */
    public static List<AmpContentTranslation> loadFieldTranslations(Session session, String objClass, Long objId, String fieldName){
        Criteria criteria = session.createCriteria(AmpContentTranslation.class);
        criteria.add(Restrictions.eq("objectClass", objClass));
        criteria.add(Restrictions.eq("objectId", objId));
        criteria.add(Restrictions.eq("fieldName", fieldName));
        return criteria.list();
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
    	StatelessSession session = null;
    	try{
        	SessionFactory sf = PersistenceManager.getRequestDBSession().getSessionFactory();
        	session = sf.openStatelessSession(); //this does the trick, doesn't work when entity contains collections
        	StringBuilder query = new StringBuilder();
			query.append("select c." + fieldName + " from ");
			query.append(clazz.getName());
			String objIdField = getObjectIdField(clazz, sf);
			query.append(" c where c."+objIdField+"=:id");
			Query qry = session.createQuery(query.toString());
			qry.setLong("id", id);
            return qry.uniqueResult();
        } catch (Exception e) {
            logger.error("can't load object from database", e);
        } finally {
            session.close();
        }
        return null;
    }

    /**
     * Get's the translation for a field in a specified locale
     * (uses a new session)
     *
     * @param objClass class of object
     * @param objId if of object
     * @param fieldName name of field
     * @param locale current locale
     * @return translation for field
     */
    public static String loadFieldTranslationInLocale(String objClass, Long objId, String fieldName, String locale){
    	Session session = PersistenceManager.openNewSession();
        try{
        	StringBuilder query = new StringBuilder();
			query.append("select t.translation from ");
			query.append(AmpContentTranslation.class.getName());
			query.append(" t where t.objectClass=:objectClass");
			query.append(" and t.objectId=:objectId");
			query.append(" and t.fieldName=:fieldName");
			query.append(" and t.locale=:locale");
			Query qry = session.createQuery(query.toString());
			qry.setString("objectClass", objClass);
			qry.setLong("objectId", objId);
			qry.setString("fieldName", fieldName);
			qry.setString("locale", locale);
            return (String) qry.uniqueResult();
        } catch (Exception e) {
            logger.error("can't load field translations", e);
        } finally {
        	session.close();
        }
        return null;
    }

    /**
     * Save all the translations in a FieldTranslationPack, deleting the old ones
     * if they exist
     * (uses a new session)
     *
     * @param newId the object id for the translations
     * @param ftp pack with field translations
     */
    public static void saveFieldTranslations(Long newId, FieldTranslationPack ftp){
    	Session session = null;
        try{
        	session = PersistenceManager.openNewSession();
            String objClass = ftp.getObjClass();
            String fieldName = ftp.getFieldName();
            
            List<AmpContentTranslation> oldTrns = loadFieldTranslations(session, objClass, newId, fieldName);
            
            
            HashMap<String, String> trns = ftp.getTranslations();
            Set<String> locales = trns.keySet();
            for (String locale : locales) {
                String trn = trns.get(locale);

                //see if we need to update existing translation
                Iterator<AmpContentTranslation> it = oldTrns.iterator();
                while (it.hasNext()) {
                    AmpContentTranslation oldTrn = it
                            .next();
                    if (oldTrn.getLocale().equals(locale)) {
                        session.delete(oldTrn);
                        it.remove();
                        break;
                    }
                }
                AmpContentTranslation act = new AmpContentTranslation(objClass, newId, fieldName, locale, trn);
                session.save(act);
            }
            session.flush();
        } catch (Exception e) {
            logger.error("can't save field translations", e);
        } finally {
        	session.close();
        }
    }

    public static void deleteFieldTranslations(Long objectId, Object entity) {
        Session session = null;
        try {
            session = PersistenceManager.openNewSession();

            String objClass = getObjectClass(entity);
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
            session.close();
        }
    }

    /**
     * @return the base language for the application
     */
    private static String getBaseLanguage(){
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
            Class clazz = Hibernate.getClass(obj);// obj.getClass();
            String objIdField = getObjectIdField(clazz, session.getSessionFactory());
            Method methGetId = clazz.getMethod("get" + Strings.capitalize(objIdField));
            return (Long) methGetId.invoke(obj);
        } catch (Exception e){
            logger.error("Can't get object id:", e);
        }
        return null;
    }

    private static String getObjectClass(Object obj){
        return Hibernate.getClass(obj).getName();
    }

    /**
     * Get all fields of a class  (safe for inheritance)
     * @param fields list of fields to be populated
     * @param type class for which we want to get all the fields
     */
    private static void getAllFields(List<Field> fields, Class<?> type) {
        Collections.addAll(fields, type.getDeclaredFields());
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
    }

    /**
     * Retrieve the Field for a class (safe for inheritance)
     * @param type class for which we want to get all the fields
     * @param name field name
     * @return the Field object representing
     */
    private static Field getFieldByName(Class<?> type, String name){
        for (Field field: type.getDeclaredFields()) {
            if (field.getName().equals(name))
            	return field;
        }

        if (type.getSuperclass() != null) {
            return getFieldByName(type.getSuperclass(), name);
        }
        return null;
    }

}
