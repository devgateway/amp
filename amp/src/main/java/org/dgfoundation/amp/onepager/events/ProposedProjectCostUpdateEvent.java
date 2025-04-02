package org.dgfoundation.amp.onepager.events;
 
import org.apache.wicket.ajax.AjaxRequestTarget;
 
public class ProposedProjectCostUpdateEvent extends AbstractAjaxUpdateEvent {
        public ProposedProjectCostUpdateEvent(AjaxRequestTarget target) {
                super(target);
        }
}
