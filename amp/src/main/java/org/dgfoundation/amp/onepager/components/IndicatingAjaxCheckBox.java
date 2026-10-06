/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components;

import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author mpostelnicu@dgateway.org
 * @since Jun 2, 2011
 */
public abstract class IndicatingAjaxCheckBox<T> extends AjaxCheckBox implements
        IAjaxIndicatorAware {
    
    private static final long serialVersionUID = 1L;
    private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

    /**
     * @param id
     */
    public IndicatingAjaxCheckBox(String id) {
        this(id, new Model<Boolean>());
    }

    /**
     * @param id
     * @param model
     */
    public IndicatingAjaxCheckBox(String id, IModel<Boolean> model) {
        super(id, model);
        add(indicatorAppender);
    }

    /* (non-Javadoc)
     * @see org.apache.wicket.ajax.IAjaxIndicatorAware#getAjaxIndicatorMarkupId()
     */
    @Override
    public String getAjaxIndicatorMarkupId() {
        return indicatorAppender.getMarkupId();
    }


}
