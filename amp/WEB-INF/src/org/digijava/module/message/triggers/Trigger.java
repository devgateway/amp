/**
 * Trigger.java
 * (c) 2007 Development Gateway Foundation
 * A Trigger is an Event factory that produces events based on some source metadata,
 * like an Activity object, Users, Calendar appointments, etc...
 */
package org.digijava.module.message.triggers;

import org.digijava.module.message.helper.Event;
import org.digijava.module.message.helper.AmpMessageWorker;

public abstract class Trigger {

    /**
     * This is the object that encapsulates the information related with the generated Event
     * It can be an AmpActivity, an User, Appointments in calendar, etc..
     */
    protected Object source;

    /**
     * This method is the generator of Event objects. It reads the source object
     * and it extracts meaningful data that has to be described by the event
     * Example: activity title, activity creator, creation date...
     * @return the generated event
     */
    protected abstract Event generateEvent();

    /**
     * A list of parameter names, specific to this trigger,
     * that can be used in a message template to show information.
     * saved in the event.
     * Example of a template that uses the parameterNames as replaceable keys:
     * <<A new activity with the name #name# has been created on #creationDate#.
     * The activity creator is #createdBy#. Check the new activity here: #url#>>
     * @see ActivitySaveTrigger
     * @return
     */
    public abstract String[] getParameterNames();

    /**
     * generates and forwards the event to the messaging processing system. The method
     * {@link AmpMessageWorker#processEvent(Event)}
     * is used here as a stub example. We need to implement it
     */
    protected void forwardEvent() {
        Event e=generateEvent();
        try {
            AmpMessageWorker.processEvent(e);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
