/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;

/**
 * @author mpostelnicu@dgateway.org
 * @since Feb 16, 2011
 */
public class AmpHiddenFieldPanel<T> extends AmpFieldPanel<T> {

    protected HiddenField<T> hiddenContainer;
    
    public HiddenField<T> getHiddenContainer() {
        return hiddenContainer;
    }

    
    /**
     * @param id
     * @param model
     * @param fmName
     */
    public AmpHiddenFieldPanel(String id, IModel<T> model, String fmName) { 
        this(id,fmName);
        hiddenContainer.setModel(model);
    }

    
    public AmpHiddenFieldPanel(String id, String fmName) {  
        super(id, fmName, true, true);
        hiddenContainer = new HiddenField<T>("hiddenContainer");
        hiddenContainer.setOutputMarkupId(true);
        addFormComponent(hiddenContainer);
    }
}
