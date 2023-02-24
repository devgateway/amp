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

package org.digijava.kernel.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * Implementation of <strong>ExceptionHandler</strong> that handles any
 * Exceptions that come up to the Action layer. This allows us to remove
 * generic try/catch statements from our Action Classes.
 *
 * <p>
 * <a href="ActionExceptionHandler.java.html"><i>View Source</i></a>
 * </p>
 *
 * @version $Revision: 1.1 $ $Date: 2008-07-16 09:19:39 $
 */
public final class ActionExceptionHandler
    extends ExceptionHandler {

    public final static String layout = "exceptionLayout";
    private static Logger logger = Logger.getLogger(ActionExceptionHandler.class);

    /**
     * This method handles any java.lang.Exceptions that are not caught in
     * previous classes. It will loop through and get all the causes (exception
     * chain), create ActionMessages, add them to the request and then forward to
     * the input.
     *
     * @see org.apache.struts.action.ExceptionHandler#execute (
     *      java.lang.Exception, org.apache.struts.config.ExceptionConfig,
     *      org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse )
     */
    public ActionForward execute(
        Exception ex,
        ExceptionConfig ae,
        ActionMapping mapping,
        ActionForm formInstance,
        HttpServletRequest request,
        HttpServletResponse response) throws ServletException {
        // namnamu
        //Special processing for security exceptions
        if (ex instanceof SecurityException) {
            throw new ServletException(ex);
        }

        logger.debug("Error occured in Struts action", ex);
        ExceptionInfo info = ExceptionHelper.populateExceptionInfo(null, ex, request);

        return ExceptionHelper.processExceptionInfo(info, request, response);
    }

    /**
     *
     * @param context
     * @param request
     * @param property
     * @param error
     * @param forward
     */



    /*
    public static ActionForward processException(Exception ex, Integer statusCode, HttpServletRequest request) {
        String property = null;
        String currentModuleInstance = null;
        ModuleInstance moduleInstanceContext = null;
        ComponentContext context = ComponentContext.getContext(request);

       ModuleInstance moduleInstance = RequestUtils.getModuleInstance(request);
       if (moduleInstance != null) {
           currentModuleInstance = moduleInstance.getModuleName() + "." +
                moduleInstance.getInstanceName();
            logger.debug("Exception belongs to instance " + currentModuleInstance);
       } else {
           logger.debug("Can not determine module instance");

           currentModuleInstance = "global.default";

       }

        Site site = RequestUtils.getSite(request);
        String formName = "site" + site.getSiteId() + "default" + "exceptionReportForm";

        DigiExceptionReportForm formReport = (DigiExceptionReportForm)request.getSession(true).getAttribute(formName);

         if( formReport == null ) {
             formReport = new DigiExceptionReportForm();
             request.getSession(true).setAttribute(formName, formReport);
         }

         ModuleErrorStack currentModuleErrorStack = formReport.getModuleStack(
             currentModuleInstance);

          if( currentModuleErrorStack == null ) {
              currentModuleErrorStack = new ModuleErrorStack();
              currentModuleErrorStack.setModuleInstance(currentModuleInstance);
              formReport.setModuleStack(currentModuleInstance, currentModuleErrorStack);
          }


        // -------------- fill form
        User user = RequestUtils.getUser(request);

        if (user != null) {
            currentModuleErrorStack.setName(user.getName());
            currentModuleErrorStack.setEmail(user.getEmail());
        }


        if (ex != null) {
            java.io.CharArrayWriter cw = new java.io.CharArrayWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(cw, true);
            ex.printStackTrace(pw);
            StringBuffer stackTrace = new StringBuffer();
            if( currentModuleErrorStack.getStackTrace() != null ) {
                stackTrace.append(currentModuleErrorStack.getStackTrace());
                stackTrace.append("\n");
            }
            stackTrace.append(cw.toString());
            currentModuleErrorStack.setStackTrace(stackTrace.toString());
            currentModuleErrorStack.setMessage(ex.getMessage());

            // dump error log
            logger.debug("Stack trace to display is:\n" + cw.toString());

        } else {
            logger.warn("No exception to store");
        }

        currentModuleErrorStack.setTimeStamp(new Date());
        currentModuleErrorStack.setStatusCode(statusCode);
        currentModuleErrorStack.setSiteId(site.getSiteId());
        currentModuleErrorStack.setStatusCode(statusCode);
        currentModuleErrorStack.setUrl(RequestUtils.getSourceURL(request).toString());

        formReport.setLayout("http" + statusCode.toString() + "layout");


        if( context != null ) {
            StringBuffer urlTmp = new StringBuffer(1024);

            String url = DgUtil.getCurrRootUrl(request);
            urlTmp.append(url);

            if (!url.endsWith("/")) {
                urlTmp.append("/");
            }

            urlTmp.append("exception/showExceptionReport.do?module=" + currentModuleInstance);
            ActionMessage error = new ActionMessage("errors.detail", urlTmp.toString());
            property = error.getKey();

            storeException(context, request, property, error);
        } else {
            String forwardPath = "/exception/showExceptionReport.do?module=" +
                currentModuleInstance;
            logger.debug("Forwarding exception report to: " + forwardPath);
            return new ActionForward(forwardPath);
        }

        return null;
    }
*/

}
