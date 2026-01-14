package org.digijava.module.message.triggers;

import org.digijava.module.message.helper.Event;

public class ApprovedResourceShareTrigger extends AbstractResourceShare {

    public ApprovedResourceShareTrigger(Object source) {
       super(source);
    }

    @Override
    protected Event getEvent() {
        return new Event(ApprovedResourceShareTrigger.class);
    }

}
