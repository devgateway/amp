package org.digijava.module.calendar.action;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.text.DgDateFormatSymbols;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.form.CalendarEventForm;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.AmpDbUtil;

public class RecurringEvent extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        CalendarEventForm calForm=(CalendarEventForm)form;
    
        DgDateFormatSymbols dgDateFormatSymbols = new DgDateFormatSymbols();
        String[] shortMonth = dgDateFormatSymbols.getShortMonths();
        ArrayList month = new ArrayList();

        for(int i = 1; i<shortMonth.length; i++) {
            month.add(shortMonth[i]);
        }
        calForm.setMonths(month);
      
        
        return mapping.findForward("forward");
    }

}
