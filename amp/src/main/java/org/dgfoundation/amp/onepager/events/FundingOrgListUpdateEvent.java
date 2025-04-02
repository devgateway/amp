package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author aartimon@developmentgateway.org
 * @since 19 September 2013
 */
public class FundingOrgListUpdateEvent extends AbstractAjaxUpdateEvent {
    public FundingOrgListUpdateEvent(AjaxRequestTarget target) {
        super(target);
    }
}
