package org.digijava.kernel.persistence.interceptors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedPropertyDescription;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.FieldTranslationPack;
import org.digijava.module.translation.util.TranslationStore;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Hibernate interceptor to translate all AMP entities and to touch Auditable entities
 * @author aartimon@developmentgateway.org
 */
public class AmpEntityInterceptor extends EmptyInterceptor {
    private static Logger logger = Logger.getLogger(AmpEntityInterceptor.class);

    
    /**
     * COPY-PASTED WITH {@link #onSave(Object, Serializable, Object[], String[], Type[])}
     * Prepare the current object to be saved by Hibernate and it's field translations to be updated
     * @see org.hibernate.Interceptor onFlushDirty
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (!ContentTranslationUtil.multilingualIsEnabled())
            return false;

        //existing entities
        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
//          printEntityData("onFlushDirty, previousState, before stuff", entity, id, previousState, propertyNames);
//          printEntityData("onFlushDirty, currentState, before stuff", entity, id, currentState, propertyNames);
//          Object[] oldPreviousState = previousState.clone();
            logger.debug("Current language in TLS Util:" + TLSUtils.getEffectiveLangCode());
            logger.debug("flushDirty versionable: " + entity);
            boolean ret = ContentTranslationUtil.prepareTranslations(entity, id, previousState, currentState, propertyNames);
//            if (entity instanceof AmpStructure)
//            {
//              logger.info("\tOVERRIDING EVERYTHING");
//              for(int i = 0; i < oldPreviousState.length; i++)
//                  previousState[i] = oldPreviousState[i];
//            }
            ContentTranslationUtil.evictEntityFromCache(entity);
            logger.debug("flushDirty returning: " + ret);
//          printEntityData("onFlushDirty, previousState, after stuff", entity, id, previousState, propertyNames);
//          printEntityData("onFlushDirty, currentState, after stuff", entity, id, currentState, propertyNames);
            return ret;
        }
        
        return false; //we haven't updated the object
    }

    /**
     * Update the loaded object in order to translate it's fields
     * @see org.hibernate.Interceptor onLoad
     */
    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!ContentTranslationUtil.multilingualIsEnabled())
            return false;

        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
            logger.debug("Current language in TLS Util:" + TLSUtils.getEffectiveLangCode());
            logger.debug("onLoad: " + entity);
            //change the state to use the translations
            return ContentTranslationUtil.translateObject(entity, id, state, propertyNames);
        }
        return false; //we didn't modify the state
    }

    /**
     * After Hibernate updated the entities, save the associated field translations
     * @see org.hibernate.Interceptor postFlush
     */
    @Override
    public void postFlush(Iterator entities) {
        if (!ContentTranslationUtil.multilingualIsEnabled())
            return;

        // try here to update the translations
        while (entities.hasNext()) {
            Object entity = entities.next();
            if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
                Long objectId = ContentTranslationUtil.getObjectId(entity);

                //see if current entity has translations that need saving
                List<FieldTranslationPack> list = TranslationStore.saveAndRemove(entity.getClass().getName(), objectId);
                if (list != null){
                    //iterate all translations for all the current object
                    for (FieldTranslationPack ftp : list) {
                        //store translations
                        ContentTranslationUtil.saveFieldTranslations(objectId, ftp);
                    }
                }
                ContentTranslationUtil.evictEntityFromCache(entity);
            }
        }
    }

    /**
     * Touch AuditableEntity objects.
     * Delete the associated translations for entities that are being deleted from the database.
     * @see org.hibernate.Interceptor onDelete
     */
    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

        if (!ContentTranslationUtil.multilingualIsEnabled())
            return;

        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
            Long objectId = (Long) id;
            //delete translations
            ContentTranslationUtil.evictEntityFromCache(entity);
            ContentTranslationUtil.deleteFieldTranslations(objectId, entity);
        }
    }


    /**
     * Touch AuditableEntity objects.
     * COPY-PASTED WITH {@link #onFlushDirty(Object, Serializable, Object[], Object[], String[], Type[])}
     * We need to insert into the translation store the initial translation for
     * the current entity
     * @param entity
     * @param id
     * @param state
     * @param propertyNames
     * @param types
     * @return
     */
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

        if (!ContentTranslationUtil.multilingualIsEnabled())
            return false;

        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
//          printEntityData("onSave,  before stuff", entity, id, state, propertyNames);
            boolean ret = ContentTranslationUtil.prepareTranslations(entity, id, null, state, propertyNames);
            ContentTranslationUtil.evictEntityFromCache(entity);
//            printEntityData("onSave,  after stuff", entity, id, state, propertyNames);
            return ret;
        }
        return false;
    }
    
    void printEntityData(String prefix, Object entity, Serializable id, Object[] state, String[] propertyNames)
    {
        Class clazz = Hibernate.getClass(entity);
        InternationalizedModelDescription entityDescription = InternationalizedModelDescription.getForClass(clazz);
        StringBuilder bld = new StringBuilder();
        bld.append(prefix + ": type <" + AmpContentTranslation.compressClassName(entity.getClass().getName()) + ">, id = " + id);
        int i = -1;
        for(String propertyName:propertyNames)
        {
            i ++;
            InternationalizedPropertyDescription propertyDescription = entityDescription.properties.get(propertyName);
            if (propertyDescription == null)
                continue; // field not translated -> nothing to do
            String propValue = (String) state[i];
            bld.append("\n\tproperty " + propertyName + " has value " + propValue);
        }
        logger.error(bld.toString());
    }
}

