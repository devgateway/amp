package org.digijava.module.calendar.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.calendar.form.CalendarEventForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PreviewCalendarEventDone
    extends DispatchAction {

    public ActionForward save(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {

        CalendarEventForm calendarEventForm = (CalendarEventForm) form;
        try {

        } catch(Exception ex) {
            return mapping.findForward("failure");
        }
        calendarEventForm.setMethod(null);
        return mapping.findForward("save");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
      CalendarEventForm calendarEventForm = (CalendarEventForm) form;
       calendarEventForm.setMethod(null);
        return mapping.findForward("edit");
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws
        Exception {
        return save(mapping, form, request, response);
    }
}
