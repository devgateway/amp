package org.digijava.module.calendar.action;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.calendar.util.AmpDbUtil;
import org.digijava.module.calendar.util.AmpUtil;
import org.digijava.module.common.dbentity.ItemStatus;

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
