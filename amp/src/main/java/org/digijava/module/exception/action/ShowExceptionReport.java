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

package org.digijava.module.exception.action;

import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.error.keeper.ErrorReportingPlugin;
import org.digijava.kernel.exception.ExceptionHelper;
import org.digijava.kernel.exception.ExceptionInfo;
import org.digijava.module.exception.form.DigiExceptionReportForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.user.User;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 *
 */
public final class ShowExceptionReport
    extends Action {

    private static Logger logger = Logger.getLogger(ShowExceptionReport.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        DigiExceptionReportForm formReport = (DigiExceptionReportForm) form;
        
        ExceptionInfo exceptionInfo;
        if (formReport.getReportId() != null) {
            exceptionInfo = ExceptionHelper.getTilesExceptioinInfo(request,
                formReport.getReportId().longValue());
        }
        else {
            exceptionInfo = ExceptionHelper.getExceptioinInfo(request);
        }
        if (exceptionInfo != null) {
            Enumeration j = request.getHeaderNames();
            exceptionInfo.setBackLink(request.getHeader("Referer"));
            formReport.setExceptionInfo(exceptionInfo);
        }

        formReport.setTeaserView(ComponentContext.getContext(request) != null);
        User currentUser = RequestUtils.getUser(request);
        if (currentUser != null) {
            formReport.setName(currentUser.getName());
            formReport.setEmail(currentUser.getEmail());
        }
        
        if (exceptionInfo != null && exceptionInfo.getException() != null)
            ErrorReportingPlugin.handle(exceptionInfo.getException(), null, request);
        
        
        if ( "exception".equalsIgnoreCase(exceptionInfo.getModuleName() ) ) {
            try{
                String errorInfo    = "<html><strong>AMP has encountered an error. Please try to go back to the <a href='/aim'>homepage</a> " +
                        "<br />If the problem persists please contact the system administrator. We're sorry for the inconvenience ! </strong></html>";
                response.getOutputStream().println( errorInfo );
                logger.error("Double exception occured !");
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
       
        //generate a unique sufix
        long rand = System.currentTimeMillis();
        //set the info for the ajax request in the session and store the unique suffix in the form
        formReport.setRand(new Long(rand));
        request.getSession().setAttribute(ExceptionInfo.EXCEPTION_INFO + String.valueOf(rand), exceptionInfo);
        
        return new ActionForward(
            "/showLayout.do?layout=newExceptionLayout");
    }
}
