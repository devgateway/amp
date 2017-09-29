package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Event used to notify date change for freezing event validator
 * @author jdeanquin
 *
 */
public class FreezingUpdateEvent extends AbstractAjaxUpdateEvent {

    public FreezingUpdateEvent(AjaxRequestTarget target) {
        super(target);
    }
}
