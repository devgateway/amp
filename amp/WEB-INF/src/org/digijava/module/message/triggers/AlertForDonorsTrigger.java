
package org.digijava.module.message.triggers;

import org.digijava.module.message.helper.Event;


public class AlertForDonorsTrigger extends Trigger{
     public AlertForDonorsTrigger(){
          forwardEvent();
    }

    @Override
    protected Event generateEvent() {
        Event e=new Event(AlertForDonorsTrigger.class);
        return e;
    }

    @Override
    public String[] getParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
