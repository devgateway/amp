package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author aartimon@developmentgateway.org
 * @since 25 September 2013
 */
public class DonorFundingRolesEvent extends AbstractAjaxUpdateEvent {
    public DonorFundingRolesEvent(AjaxRequestTarget target) {
        super(target);
    }
}
