package org.digijava.module.translation.hibernate;

import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.FieldTranslationPack;
import org.digijava.module.translation.util.TranslationStore;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Hibernate interceptor to translate all AMP entities
 * @author aartimon@developmentgateway.org
 */
public class TranslatorInterceptor extends EmptyInterceptor{
    private static Logger logger = Logger.getLogger(TranslatorInterceptor.class);


    /**
     * Prepare the current object to be saved by Hibernate and it's field translations to be updated
     * @see org.hibernate.Interceptor onFlushDirty
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if ("false".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MULTILINGUAL)))
            return false;

        //existing entities
        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
        	logger.debug("Current language in TLS Util:" + TLSUtils.getEffectiveLangCode());
            logger.debug("flushDirty versionable: " + entity);
            boolean ret = ContentTranslationUtil.prepareTranslations(entity, id, previousState, currentState, propertyNames);
            ContentTranslationUtil.evictEntityFromCache(entity);
            logger.debug("flushDirty returning: " + ret);
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
        if ("false".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MULTILINGUAL)))
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
        if ("false".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MULTILINGUAL)))
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
     * We need to delete the associated translations for entities that are being
     * deleted from the database
     * @see org.hibernate.Interceptor onDelete
     */
    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if ("false".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MULTILINGUAL)))
            return;

        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
            Long objectId = (Long) id;
            //delete translations
            ContentTranslationUtil.evictEntityFromCache(entity);
            ContentTranslationUtil.deleteFieldTranslations(objectId, entity);
        }
    }


    /**
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
        if ("false".equalsIgnoreCase(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.MULTILINGUAL)))
            return false;

        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
            boolean ret = ContentTranslationUtil.prepareTranslations(entity, id, null, state, propertyNames);
            ContentTranslationUtil.evictEntityFromCache(entity);
            return ret;
        }
        return false;
    }
}

