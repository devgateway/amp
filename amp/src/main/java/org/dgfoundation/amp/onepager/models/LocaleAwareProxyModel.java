package org.dgfoundation.amp.onepager.models;

import org.apache.wicket.model.IModel;
import org.digijava.kernel.request.TLSUtils;

/**
 * @author aartimon
 * @date 11/15/13
 */
public abstract class LocaleAwareProxyModel<T> implements IModel<T> {
    private IModel<String> langModel;
    protected IModel<String> model;

    public LocaleAwareProxyModel(IModel<String> model) {
        this.model = model;
    }

    public boolean forwardToModel(){
        /**
         *  if {@langModel} is null then it hasn't been set yet, so the language tab wasn't changed
         *  if the object inside the {@langModel} is null, then the tab is the same language as the page
         *
         *  in both cases we can forward the request to the main model
         */
        if (langModel == null || langModel.getObject() == null)
            return true;
        return false;
    }

    protected String localeOfLangModel(){
        if (langModel == null || langModel.getObject() == null){
            //same language as page
            return TLSUtils.getLangCode();
        }
        else
            return langModel.getObject();
    }

    public IModel<String> getLangModel() {
        return langModel;
    }

    public void setLangModel(IModel<String> langModel) {
        this.langModel = langModel;
    }

    public IModel<String> getOriginalModel(){
        return model;
    }
}
