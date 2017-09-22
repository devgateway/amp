package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class FreezingUpdateEvent extends AbstractAjaxUpdateEvent {

    public FreezingUpdateEvent(AjaxRequestTarget target) {
        super(target);
    }

}
