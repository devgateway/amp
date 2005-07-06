/*
 *   ShowUserUnSubscribe.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Sep 1, 2003
 * 	 CVS-ID: $Id: ShowUserUnSubscribe.java,v 1.1 2005-07-06 10:34:07 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/

package org.digijava.module.um.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.module.um.form.UserUnSubscribeForm;
import org.digijava.module.um.util.Calendar;
import java.util.GregorianCalendar;


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

       User user = (User) request.getSession(true).getAttribute(Constants.USER);


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


       unsubscribeForm.setActive(user.isActivate());

       if (user==null) {

           ActionErrors errors = new ActionErrors();
           errors.add(ActionErrors.GLOBAL_ERROR,
                      new ActionError("error.logon.invalid"));
       }

       return mapping.findForward("forward");
     }
}