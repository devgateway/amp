package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author Viorel Chihai
 */
public class DirectProgramMappingUpdateEvent extends AbstractAjaxUpdateEvent {
    public DirectProgramMappingUpdateEvent(AjaxRequestTarget target) {
        super(target);
    }
}
