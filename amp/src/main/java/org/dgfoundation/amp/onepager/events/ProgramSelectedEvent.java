package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class ProgramSelectedEvent extends AbstractAjaxUpdateEvent {
    public ProgramSelectedEvent(AjaxRequestTarget target) {
        super(target);
    }
}
