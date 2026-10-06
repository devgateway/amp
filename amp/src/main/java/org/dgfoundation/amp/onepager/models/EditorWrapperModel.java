/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.dgfoundation.amp.onepager.models;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.OnePagerConst;
import org.dgfoundation.amp.onepager.helper.EditorStore;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

import java.util.*;

/**
 * @author aartimon@dginternational.org 
 * @since Jun 16, 2011
 */
public class EditorWrapperModel extends LocaleAwareProxyModel<String> {
    private static final Logger logger = Logger.getLogger(EditorWrapperModel.class);

    private static final String KEY_PREFIX = "aim-wckt-";
    
    private IModel<String> keyModel;

    public EditorWrapperModel(IModel<String> m, String id) {
        super(Model.of(""));
        this.keyModel = m;
        
        String valueStoredInActivityVal = "";
        AmpAuthWebSession session = ((AmpAuthWebSession)Session.get());

        if(keyModel.getObject() != null && !keyModel.getObject().startsWith("aim-")){
            //all editor keys start with "aim-" so it should be fine to 
            //assume value was stored inside AmpActivity instead of the key
            valueStoredInActivityVal = keyModel.getObject();
            keyModel.setObject(null); // to generate a proper editor key for it
        }
        
        //if (m.getObject() == null || m.getObject().trim().compareTo("") == 0 || !m.getObject().startsWith(KEY_PREFIX)){
        if (keyModel.getObject() == null || keyModel.getObject().trim().compareTo("") == 0 ){
            //no editor key
            String eKey = generateEditorKey(session, id);
            keyModel.setObject(eKey);
            setObject(valueStoredInActivityVal);
        }
        else{

        }
        
        if (Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS) == null)
            Session.get().setMetaData(OnePagerConst.EDITOR_ITEMS, new EditorStore());

        if (ActivityVersionUtil.isVersioningEnabled()){
            String oldKey = keyModel.getObject();
            String newKey = generateEditorKey(session, id);
            Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS).getOldKey().put(newKey, oldKey);
            keyModel.setObject(newKey);

            //if we cloned the editors we need to put all the old values in the session
            
            List<String> languages ;
            //if multilingual is enable we use all the language active for the current instance
            if (ContentTranslationUtil.multilingualIsEnabled()) {
                languages = TranslatorUtil.getLocaleCache();
            }
            else{
                //if multilingual is not active, we use only ONE language, the default one for the site
                languages =new ArrayList<String>(Arrays.asList(TranslatorUtil.getDefaultLocaleCache()));
            }
            IModel<String> langModel = getLangModel();
            if (langModel == null){
                langModel = Model.of("");
                setLangModel(langModel);
            }
            for (String lang: languages){
                langModel.setObject(lang);
                setObject(getObject());
            }
            //after the loop the langModel's object will always be the last value of languages ArrayList
            //we need to set it to the current language
            if (languages.contains(TLSUtils.getLangCode())) {
                langModel.setObject(TLSUtils.getLangCode());
            }
        }
    }
    
    private String generateEditorKey(AmpAuthWebSession session, String id) {
        String eKey = KEY_PREFIX;
        eKey = eKey + session.getCurrentMember().getMemberId() + "-";
        eKey = eKey + id + "-";
        eKey = eKey + System.currentTimeMillis();
        return eKey;
    }

    @Override
    public void setObject(String object) {
        //model.setObject(object);
        if (Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS) == null)
            Session.get().setMetaData(OnePagerConst.EDITOR_ITEMS, new EditorStore());

        Map<String, Map<String, String>> valuesMap = Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS).getValues();
        Map<String, String> trnSet = valuesMap.get(keyModel.getObject());
        if (trnSet == null) {
            trnSet = new TreeMap<>();
            valuesMap.put(keyModel.getObject(), trnSet);
        }
        if (object == null)
            object = ""; //null would have toggle the getObject to retrieve it from the db
        trnSet.put(localeOfLangModel(), object);
    }
    
    @Override
    public String getObject() {
        //first, try and see if we have the object in the editor store
        EditorStore editorStore = Session.get().getMetaData(OnePagerConst.EDITOR_ITEMS);
        if (editorStore != null) {
            Map<String, Map<String, String>> valuesMap = editorStore.getValues();
            String oldKey = editorStore.getOldKey().get(keyModel.getObject());
            Map<String, String> trnSet = valuesMap.get(keyModel.getObject());
            if (trnSet != null){
                String val = trnSet.get(localeOfLangModel());
                if (val != null)
                    return val;
            }
            AmpAuthWebSession session = ((AmpAuthWebSession)Session.get());
            try {
                Editor editor = DbUtil.getEditor(session.getSite(), oldKey, localeOfLangModel());
                if (editor != null){
                    return editor.getBody();
                }
            } catch (EditorException e) {
                logger.error("Can't get editor:", e);
                return null;
            }
        }


        return null;
    }

    public IModel<String> getKeyModel() {
        return keyModel;
    }

    @Override
    public void detach() {
        model.detach();
    }
}
