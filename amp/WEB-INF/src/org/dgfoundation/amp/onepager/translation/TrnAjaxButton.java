/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.translation;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Translatable button
 * 
 * @author aartimon@dginternational.org
 * since Oct 4, 2010
 * @deprecated 
 * @see AmpButtonField
 */
public abstract class TrnAjaxButton extends AjaxButton {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    public TrnAjaxButton(String id, IModel<String> model, Form<?> form, String key) {
        super(id, model, form);
        trnAjaxButton(key);
    }

    public TrnAjaxButton(String id, IModel<String> model, String key) {
        super(id, model);
        trnAjaxButton(key);
    }

    public TrnAjaxButton(String id, Form<?> form, String key) {
        super(id, form);
        trnAjaxButton(key);
    }
    
    /**
     * Add js behaviour that will switch the button to an edit box on hover 
     */
    private void trnAjaxButton(String key){
        if (TranslatorUtil.isTranslatorMode(getSession())){
            super.setOutputMarkupId(true);
            super.add(new AttributeModifier("onmouseover", "spawnEditBox(this.id);"));
            super.add(new AttributeModifier("key", key));
            super.add(new AttributeAppender("style", new Model("text-decoration: underline; color: #6CE66C;"), ""));
        }
    }
}
