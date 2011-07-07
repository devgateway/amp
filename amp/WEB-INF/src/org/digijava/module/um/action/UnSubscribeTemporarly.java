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

import java.util.Date;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.Constants;
import org.digijava.kernel.user.User;
import org.digijava.module.um.form.UserUnSubscribeForm;
import org.digijava.module.um.util.DbUtil;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
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

public class UnSubscribeTemporarly
   extends Action {

   public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                javax.servlet.http.HttpServletRequest request,
                                javax.servlet.http.HttpServletResponse
                                response) throws
       java.lang.Exception {

       UserUnSubscribeForm unsubscribeForm = (UserUnSubscribeForm) form;

       User user = RequestUtils.getUser(request);

       boolean removeHold = (request.getParameter("removeHold")!=null && request.getParameter("removeHold").equalsIgnoreCase("true"));

       if (user!=null) {

           if (!removeHold) {
               GregorianCalendar leaveUntil = new GregorianCalendar();

               leaveUntil.set(java.util.Calendar.MONTH,
                              Integer.parseInt(unsubscribeForm.getSelectedMonth())-1);
               leaveUntil.set(java.util.Calendar.DAY_OF_MONTH,
                              Integer.parseInt(unsubscribeForm.getDay()));
               leaveUntil.set(java.util.Calendar.YEAR,
                              Integer.parseInt(unsubscribeForm.getYear()));

               user.setNoAlertsUntil(leaveUntil.getTime());
           } else {
               user.setNoAlertsUntil(new Date());
           }
           DbUtil.updateUser(user);
       } else {
           ActionMessages errors = new ActionMessages();
           errors.add(ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage("error.logon.invalid"));
       }

       return mapping.findForward("unsubscribedTmp");

       }
}
