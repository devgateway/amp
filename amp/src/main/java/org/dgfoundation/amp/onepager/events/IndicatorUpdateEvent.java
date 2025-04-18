package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class IndicatorUpdateEvent extends AbstractAjaxUpdateEvent {
    public IndicatorUpdateEvent(AjaxRequestTarget target) {
        super(target);
    }
}