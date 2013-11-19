package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.digijava.module.aim.dbentity.AmpContentTranslation;
import org.digijava.module.translation.util.ContentTranslationUtil;

import java.lang.reflect.Field;

/**
 * @author aartimon@developmentgateway.org
 * @since 08 October 2013
 */
public class TranslationDecoratorModel extends LocaleAwareProxyModel<String> {
    private AmpActivityModel am;

    public TranslationDecoratorModel(IModel<String> model) {
        super(model);
    }

    @Override
    public String getObject() {
        if (forwardToModel())
            return model.getObject();
        else {
            if (model instanceof PropertyModel){
                PropertyModel<?> pm = (PropertyModel<?>) model;

                AmpContentTranslation act = retrieveContentTranslation(pm);
                return act.getTranslation();
            }
            else
                if (model instanceof EditorWrapperModel){
                    return model.getObject();
                }
                else{
                    throw new AssertionError("Extend code to use other types of models");
                }
        }
    }


    @Override
    public void setObject(String object) {
        if (forwardToModel())
            model.setObject(object);
        else {
            if (model instanceof PropertyModel){
                PropertyModel<?> pm = (PropertyModel<?>) model;
                AmpContentTranslation act = retrieveContentTranslation(pm);
                act.setTranslation(object);
            }
            else
                if (model instanceof EditorWrapperModel){
                    model.setObject(object);
                }
                else{
                        throw new AssertionError("Extend code to use other types of models");
                }
        }
    }

    private void setToHash(String className, Long objId, String fieldName, String locale, AmpContentTranslation act) {
        am.getTranslationHashMap().put(getHashKey(className, objId, fieldName, locale), act);
    }

    private AmpContentTranslation retrieveContentTranslation(PropertyModel<?> pm) {
        Field field = pm.getPropertyField();
        //get parent object
        Object parentObject = pm.getInnermostModelOrObject();
        String className = parentObject.getClass().getName();
        String fieldName = field.getName();
        Long objId = ContentTranslationUtil.getObjectId(parentObject);
        Long originalObjId = objId;

        if (objId == null){
            //for new objects we'll store the hashCode in the id field, so we can identify them later on
            objId = (long) System.identityHashCode(parentObject);
        }

        AmpContentTranslation act = getFromHash(className, objId, fieldName, localeOfLangModel());
        if (act == null){
            if (originalObjId != null) {
                if (getLangModel().getObject() == null)
                    throw new AssertionError("not null");
                act = ContentTranslationUtil.loadCachedFieldTranslationsInLocale(className, objId, fieldName, getLangModel().getObject());
            }

            if (act == null){
                //either we're dealing with a new object or it hasn't got a translation yet
                act = new AmpContentTranslation(className, objId, fieldName, localeOfLangModel(), "");
            }
            setToHash(className, objId, fieldName, localeOfLangModel(), act);
        }

        return act;
    }

    private AmpContentTranslation getFromHash(String className, Long objId, String fieldName, String locale) {
        return am.getTranslationHashMap().get(getHashKey(className, objId, fieldName, locale));
    }

    private String getHashKey(String className, Long objId, String fieldName, String locale) {
        return className+objId+fieldName+locale;
    }

    @Override
    public void detach() {
        if (forwardToModel())
            model.detach();
    }

    @Override
    public void setLangModel(IModel<String> langModel) {
        super.setLangModel(langModel);
        if (model instanceof EditorWrapperModel) //we need to chain the call
            ((EditorWrapperModel)model).setLangModel(langModel);
    }

    public void setAm(AmpActivityModel am) {
        this.am = am;
    }
}
