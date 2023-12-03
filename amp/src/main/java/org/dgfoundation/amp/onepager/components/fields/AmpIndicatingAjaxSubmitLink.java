package org.dgfoundation.amp.onepager.components.fields;

import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.form.Form;

public abstract class AmpIndicatingAjaxSubmitLink extends AjaxSubmitLink implements IAjaxIndicatorAware {

    private static final long serialVersionUID = 1L;
    private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

    public AmpIndicatingAjaxSubmitLink(String id) {
        super(id, null);
        add(indicatorAppender);
    }

    public AmpIndicatingAjaxSubmitLink(String id, Form form) {
        super(id, form);
        add(indicatorAppender);
    }

    /**
     * @see org.apache.wicket.ajax.IAjaxIndicatorAware#getAjaxIndicatorMarkupId()
     */
    public String getAjaxIndicatorMarkupId() {
        return indicatorAppender.getMarkupId();
    }
}
