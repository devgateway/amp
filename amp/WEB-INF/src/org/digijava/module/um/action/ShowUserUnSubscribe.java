/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.um.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.module.um.form.UserUnSubscribeForm;
import org.digijava.module.um.util.Calendar;
import java.util.GregorianCalendar;
import org.digijava.kernel.util.RequestUtils;


/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShowUserUnSubscribe
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws
        java.lang.Exception {

       UserUnSubscribeForm unsubscribeForm = (UserUnSubscribeForm) form;

       User user = RequestUtils.getUser(request);


       GregorianCalendar       currentDate = new GregorianCalendar();
       Collection months = new ArrayList();
       String     selectedMonthId;
       String     selectedMonth;
       String     day;
       String     year;

       months.add(new Calendar(new Long(1), "January"));
       months.add(new Calendar(new Long(2), "February"));
       months.add(new Calendar(new Long(3), "March"));
       months.add(new Calendar(new Long(4), "April"));
       months.add(new Calendar(new Long(5), "May"));
       months.add(new Calendar(new Long(6), "June"));
       months.add(new Calendar(new Long(7), "July"));
       months.add(new Calendar(new Long(8), "August"));
       months.add(new Calendar(new Long(9), "September"));
       months.add(new Calendar(new Long(10), "October"));
       months.add(new Calendar(new Long(11), "November"));
       months.add(new Calendar(new Long(12), "December"));


       selectedMonth   =  Integer.toString(currentDate.get(java.util.Calendar.MONTH)+1);
       day             =  Integer.toString(currentDate.get(java.util.Calendar.DAY_OF_MONTH));
       year            =  Integer.toString(currentDate.get(java.util.Calendar.YEAR));


       unsubscribeForm.setMonths(months);
       unsubscribeForm.setSelectedMonth(selectedMonth);
       unsubscribeForm.setDay(day);
       unsubscribeForm.setYear(year);

       if (user==null) {

           ActionMessages errors = new ActionMessages();
           errors.add(ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage("error.logon.invalid"));
       }

       Date toDate = user.getNoAlertsUntil();
       if (toDate==null || toDate.after(new Date())) {
           unsubscribeForm.setOnHold(true);
           unsubscribeForm.setFormatedToDate(UserUnSubscribeForm.fmt.format(toDate));
       } else {
           unsubscribeForm.setOnHold(false);
       }

       return mapping.findForward("forward");
     }
}
