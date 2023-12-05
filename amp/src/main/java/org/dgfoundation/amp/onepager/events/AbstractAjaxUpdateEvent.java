package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author aartimon@developmentgateway.org
 * @since 19 September 2013
 */
public abstract class AbstractAjaxUpdateEvent {
    private AjaxRequestTarget target;

    protected AbstractAjaxUpdateEvent(AjaxRequestTarget target) {
        this.target = target;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }
}
