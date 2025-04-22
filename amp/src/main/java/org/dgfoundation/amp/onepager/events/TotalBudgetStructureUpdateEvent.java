package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class TotalBudgetStructureUpdateEvent extends AbstractAjaxUpdateEvent {
    public TotalBudgetStructureUpdateEvent(AjaxRequestTarget target) {
        super(target);
    }
}
