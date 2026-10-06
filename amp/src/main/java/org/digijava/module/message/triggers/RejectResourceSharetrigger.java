package org.digijava.module.message.triggers;

import org.digijava.module.message.helper.Event;

public class RejectResourceSharetrigger extends AbstractResourceShare {

    public RejectResourceSharetrigger(Object source) {
        super(source);
    }

    @Override
    protected Event getEvent() {
        return new Event(RejectResourceSharetrigger.class);
    }

}
