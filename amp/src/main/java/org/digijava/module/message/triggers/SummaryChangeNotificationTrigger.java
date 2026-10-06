package org.digijava.module.message.triggers;

import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.SummaryChangeData;
import org.digijava.module.message.helper.Event;
import org.digijava.module.message.helper.MessageConstants;

public class SummaryChangeNotificationTrigger extends Trigger {
    public static final String PARAM_TRIGGER_SENDER = "sender";
    public static final String PARAM_SUMMARY_EMAIL = "summaryEmail";
    public static final String PARAM_SUMMARY_DATE = "summaryDate";
    public static final String PARAM_SUMMARY_BODY = "summaryBody";
    public static final String PARAM_SUMMARY_SUBJECT = "summarySubject";
    public static final String PARAM_SUMMARY_BODY_HEADER = "summaryBodyHeader";



    private static final String[] PARAMETER_NAMES = new String[] { PARAM_TRIGGER_SENDER, PARAM_SUMMARY_EMAIL,
            PARAM_SUMMARY_DATE, PARAM_SUMMARY_BODY };

    public SummaryChangeNotificationTrigger(Object source) {
        if (!(source instanceof SummaryChangeData)) {
           throw new RuntimeException("Incompatible object. Source must be a ! " + SummaryChangeData.class);
        }
        
        this.source = source;
        forwardEvent();
    }

    @Override
    protected Event generateEvent() {
        Event e = new Event(SummaryChangeNotificationTrigger.class);
        SummaryChangeData event = (SummaryChangeData) this.source;
        e.getParameters().put(PARAM_TRIGGER_SENDER, MessageConstants.SENDER_TYPE_SYSTEM);
        e.getParameters().put(PARAM_SUMMARY_EMAIL, event.getEmail());
        e.getParameters().put(PARAM_SUMMARY_SUBJECT, event.getSubject());
        e.getParameters().put(PARAM_SUMMARY_BODY, event.getBody());
        e.getParameters().put(PARAM_SUMMARY_DATE, FormatHelper.formatDate(event.getDate()));
        e.getParameters().put(PARAM_SUMMARY_BODY_HEADER, event.getBodyHeader());

        return e;
    }

    @Override
    public String[] getParameterNames() {
        return PARAMETER_NAMES;
    }
}
