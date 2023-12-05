package org.digijava.module.message.triggers;

import org.digijava.kernel.ampapi.endpoints.datafreeze.DataFreezeUtil;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.message.helper.Event;
import org.digijava.module.message.helper.MessageConstants;
import org.digijava.module.message.jobs.DataFreezeEmailNotificationJob;

public class DataFreezeEmailNotificationTrigger extends Trigger {
    public static final String PARAM_FREEZE_NOTIFICATION_DAYS = "notificationDays";
    public static final String PARAM_TRIGGER_SENDER = "sender";
    public static final String PARAM_DATA_FREEZING_DATE = "freezingDate";

    private static final String[] PARAMETER_NAMES = new String[] { PARAM_TRIGGER_SENDER,
            PARAM_FREEZE_NOTIFICATION_DAYS };

    public DataFreezeEmailNotificationTrigger(Object source) {
        if (!(source instanceof AmpDataFreezeSettings)) {
           throw new RuntimeException("Incompatible object. Source must be a ! " + AmpDataFreezeSettings.class);   
        }
        
        this.source = source;
        forwardEvent();
    }

    @Override
    protected Event generateEvent() {
        Event e = new Event(DataFreezeEmailNotificationTrigger.class);
        AmpDataFreezeSettings event = (AmpDataFreezeSettings) this.source;
        Integer notificationDays = event.getNotificationDays() != null ? event.getNotificationDays()
                : DataFreezeEmailNotificationJob.DATA_FREEZE_NOTIFICATION_DAYS;
        e.getParameters().put(PARAM_TRIGGER_SENDER, MessageConstants.SENDER_TYPE_SYSTEM);
        e.getParameters().put(PARAM_FREEZE_NOTIFICATION_DAYS, notificationDays);
        e.getParameters().put(PARAM_DATA_FREEZING_DATE, DateTimeUtil.formatDate(DataFreezeUtil.getFreezingDate(event)));
        return e;
    }

    @Override
    public String[] getParameterNames() {
        return PARAMETER_NAMES;
    }
}
