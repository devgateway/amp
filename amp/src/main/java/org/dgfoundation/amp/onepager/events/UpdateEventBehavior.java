package org.dgfoundation.amp.onepager.events;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;

/**
 * @author aartimon@developmentgateway.org
 * @since 19 September 2013
 */
public class UpdateEventBehavior<T> extends Behavior {
    private Class<T> triggerEvent;
    private Component parent;

    private UpdateEventBehavior(Class<T> triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    @Override
    public void onEvent(Component component, IEvent<?> event) {
        if (event.getPayload().getClass().isAssignableFrom(triggerEvent)){
            AbstractAjaxUpdateEvent ajaxUpdateEvent = (AbstractAjaxUpdateEvent) event.getPayload();
            if (parent.isVisibleInHierarchy()) {
                ajaxUpdateEvent.getTarget().add(parent);
            }
        }
    }

    @Override
    public void bind(Component component) {
        synchronized (this){ //make it thread safe
            if (parent == null){
                parent = component;
            }
            else{
                throw new AssertionError("Don't use the same behavior on different components");
            }
        }

    }

    public static <T> UpdateEventBehavior<T> of(Class<T> triggerEvent){
        return new UpdateEventBehavior<T>(triggerEvent);
    }
}
