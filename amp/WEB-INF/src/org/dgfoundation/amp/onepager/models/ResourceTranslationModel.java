package org.dgfoundation.amp.onepager.models;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.helper.ResourceTranslation;
import org.dgfoundation.amp.onepager.helper.ResourceTranslationStore;
import org.dgfoundation.amp.onepager.util.SessionUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import javax.jcr.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResourceTranslationModel extends LocaleAwareProxyModel<String> {
    private static final Logger logger = Logger.getLogger(ResourceTranslationModel.class);
    private IModel<String> keyModel;
    private static final String TEMPORARY_UUID_PREFIX = "newResource";

    public ResourceTranslationModel(IModel<String> model, IModel<String> id) {
        super(model);
        this.keyModel = id;
        if (Session.get().getMetaData(OnePagerConst.RESOURCES_TRANSLATIONS) == null)
            // will contain resource id and the ResourceTranslationStore
            Session.get().setMetaData(OnePagerConst.RESOURCES_TRANSLATIONS, new HashMap<String, ResourceTranslation>());

    }

    @Override
    public String getObject() {
        HashMap<String, ResourceTranslationStore> translationMap = Session.get().getMetaData(
                OnePagerConst.RESOURCES_TRANSLATIONS);
        if (translationMap != null) {
            ResourceTranslationStore store = translationMap.get(keyModel.getObject());
            if (store != null) {
                List<ResourceTranslation> translationsList = store.getResourceFieldTranslations().get(
                        ((PropertyModel<String>) model).getPropertyField().getName());
                if (translationsList != null) {
                    for (ResourceTranslation translation : translationsList) {
                        if (translation.getLocale().equals(localeOfLangModel())) {
                            return translation.getTranslation();
                        }
                    }
                }
            }

        }
        if (!keyModel.getObject().startsWith(TEMPORARY_UUID_PREFIX)) {
            AmpAuthWebSession session = ((AmpAuthWebSession) Session.get());
            // extract from jackrabbit
            // language, field, id
            Node node = DocumentManagerUtil.getWriteNode(keyModel.getObject(), SessionUtil.getCurrentServletRequest());
            if (node != null) {
                String value = "";
                NodeWrapper nw = new NodeWrapper(node);
                final String field = ((PropertyModel) model).getPropertyField().getName();
                switch (field) {
                case "title":
                    value = nw.getTranslatedTitleByLang(localeOfLangModel());
                    break;
                case "description":
                    value = nw.getTranslatedDescriptionByLang(localeOfLangModel());
                    break;
                case "note":
                    value = nw.getTranslatedNoteByLang(localeOfLangModel());
                    break;

                }
                return value;
            }
        }

        return "";

    }

    @Override
    public void setObject(String object) {
        Object target = ((PropertyModel) model).getInnermostModelOrObject();

        if (Session.get().getMetaData(OnePagerConst.RESOURCES_TRANSLATIONS) == null)
            // will contain resource id and the ResourceTranslationStore
            Session.get().setMetaData(OnePagerConst.RESOURCES_TRANSLATIONS, new HashMap<String, ResourceTranslation>());

        HashMap<String, ResourceTranslationStore> translationMap = Session.get().getMetaData(
                OnePagerConst.RESOURCES_TRANSLATIONS);
        ResourceTranslationStore store = translationMap.get(keyModel.getObject());
        if (object == null)
            object = ""; // null would have toggle the getObject to retrieve it
                            // from the db
        if (store == null) {
            store = new ResourceTranslationStore();
            translationMap.put(keyModel.getObject(), store);
        }
        String field = ((PropertyModel) model).getPropertyField().getName();
        List<ResourceTranslation> translatedFields = store.getResourceFieldTranslations().get(field);
        if (translatedFields == null) {
            translatedFields = new ArrayList<ResourceTranslation>();
            store.getResourceFieldTranslations().put(field, translatedFields);
        }
        boolean found = false;
        for (ResourceTranslation aux : translatedFields) {
            if (aux.getLocale().equals(localeOfLangModel())) {
                aux.setTranslation(object);
                found = true;
                break;
            }
        }
        if (!found) {
            ResourceTranslation translation = new ResourceTranslation(keyModel.getObject(), object,
                    localeOfLangModel());
            translatedFields.add(translation);
        }
        // also update the model property
        if (localeOfLangModel().equals(TLSUtils.getLangCode())) {
            model.setObject(object);
            //((TemporaryDocument)target).getTranslatedTitleList().add(e)
        }

    }

    @Override
    public void detach() {
        model.detach();

    }

    public String getKey() {
        return keyModel.getObject();
    }

}
