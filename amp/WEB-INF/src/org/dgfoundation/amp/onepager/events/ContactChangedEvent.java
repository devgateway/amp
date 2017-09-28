package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class ContactChangedEvent extends AbstractAjaxUpdateEvent {
    
    public ContactChangedEvent(AjaxRequestTarget target) {
        super(target);
    }

}
