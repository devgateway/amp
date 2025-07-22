package org.digijava.module.calendar.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.calendar.form.CalendarEventForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
