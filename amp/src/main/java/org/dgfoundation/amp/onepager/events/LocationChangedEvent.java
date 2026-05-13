package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class LocationChangedEvent extends AbstractAjaxUpdateEvent {
    public LocationChangedEvent(AjaxRequestTarget target) {
        super(target);
        target.appendJavaScript("indicatorTabs();");
    }
}
