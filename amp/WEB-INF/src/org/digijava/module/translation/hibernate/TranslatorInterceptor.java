package org.digijava.module.translation.hibernate;

import org.apache.log4j.Logger;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.FieldTranslationPack;
import org.digijava.module.translation.util.TranslationStore;
import org.hibernate.EmptyInterceptor;
import org.hibernate.mapping.Collection;
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

    /*
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        //new entities
        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
        	logger.debug("Current language in TLS Util:" + TLSUtils.getLangCode());
            logger.debug("onSave:" + entity);
        }
        return false; //nothing modified
    }
    */

    /**
     * @see org.hibernate.Interceptor onFlushDirty
     * Prepare the current object to be saved by Hibernate and it's field translations to be updated
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
    	//existing entities
        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
        	logger.debug("Current language in TLS Util:" + TLSUtils.getLangCode());
            logger.debug("flushDirty versionable: " + entity);
            boolean ret = ContentTranslationUtil.prepareTranslations(entity, id, previousState, currentState, propertyNames, types, entity instanceof Versionable);
            logger.debug("flushDirty returning: " + ret);
            return ret;
        }
        
        return false; //we haven't updated the object
    }

    /**
     * @see org.hibernate.Interceptor onLoad
     * Update the loaded object in order to translate it's fields
     */
    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
        	logger.debug("Current language in TLS Util:" + TLSUtils.getLangCode());
        	logger.debug("onLoad: " + entity);
            //change the state to use the translations
            return ContentTranslationUtil.translateObject(entity, id, state, propertyNames, types);
        }
        return false; //we didn't modify the state
    }

    /**
     * @see org.hibernate.Interceptor postFlush
     * After Hibernate updated the entities, save the associated field translations
     */
    @Override
    public void postFlush(Iterator entities) {
    	// try here to update the translations
    	while (entities.hasNext()) {
			Object entity = entities.next();
			if (entity.getClass().getAnnotation(TranslatableClass.class) != null){
				Long objectId = ContentTranslationUtil.getObjectId(entity);
				List<FieldTranslationPack> list = TranslationStore.saveAndRemove(entity.getClass().getName(), objectId);
				if (list != null){
					//iterate all translations for all the current object
                    for (FieldTranslationPack ftp : list) {
                        //store translations
                        ContentTranslationUtil.saveFieldTranslations(objectId, ftp);
                    }
				}
			}
		}
    }
}

