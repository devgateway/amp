/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.translation.TrnLabel;

/**
 * @author mpostelnicu@dgateway.org
 * since Oct 5, 2010
 */
public abstract class AmpAjaxCheckBoxFieldPanel extends AmpFieldPanel<Boolean> {

    private static final long serialVersionUID = -385489587825684759L;
    protected AjaxCheckBox checkbox;
    protected abstract void onUpdate(AjaxRequestTarget target);
    
    /**
     * @param id
     * @param fmName
     * @param hideLabel
     */
    public AmpAjaxCheckBoxFieldPanel(String id, String fmName, IModel<Boolean> model,boolean hideLabel) {
        super(id, fmName, hideLabel);
        newLine.setVisible(false);
        checkbox=new AjaxCheckBox("checkbox", model) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                AmpAjaxCheckBoxFieldPanel.this.onUpdate(target);                
            }
            
        };
        addFormComponent(checkbox);
        add(new TrnLabel("label", fmName));
    }

    /**
     * @param id
     * @param fmName
     */
    public AmpAjaxCheckBoxFieldPanel(String id, String fmName,IModel<Boolean> model) {
        this(id,fmName,model, true);
    }
    

}
