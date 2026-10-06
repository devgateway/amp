package org.digijava.module.message.triggers;

import org.digijava.module.message.helper.Event;

public class PendingResourceShareTrigger extends AbstractResourceShare {        

    public PendingResourceShareTrigger(Object source) {
        super(source);
    }
    
    @Override
    protected Event getEvent() {
        return new Event(PendingResourceShareTrigger.class);
    }

}
