package org.digijava.module.calendar.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.calendar.entity.AmpEventType;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.calendar.util.AmpDbUtil;

public class PreviewCalendarEvent
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {


        CalendarEventForm calendarEventForm = (CalendarEventForm) form;
        try {

        } catch(Exception ex) {
            return mapping.findForward("failure");
        }
        return mapping.findForward("success");
    }
}
