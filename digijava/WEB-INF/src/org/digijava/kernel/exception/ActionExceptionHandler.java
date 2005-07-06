/*
 *   ActionExceptionHandler.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jul 4, 2003
 * 	 CVS-ID: $Id: ActionExceptionHandler.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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

package org.digijava.kernel.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import java.util.Enumeration;
import org.digijava.kernel.entity.ModuleInstance;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.module.exception.form.DigiExceptionReportForm;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.DgUtil;
import java.util.Date;
import org.digijava.kernel.request.Site;
import org.digijava.module.exception.util.ModuleErrorStack;

/**
 * Implementation of <strong>ExceptionHandler</strong> that handles any
 * Exceptions that come up to the Action layer. This allows us to remove
 * generic try/catch statements from our Action Classes.
 *
 * <p>
 * <a href="ActionExceptionHandler.java.html"><i>View Source</i></a>
 * </p>
 *
 * @version $Revision: 1.1 $ $Date: 2005-07-06 10:34:30 $
 */
public final class ActionExceptionHandler
    extends ExceptionHandler {

    public final static String layout = "exceptionLayout";
    private static Logger logger = Logger.getLogger(ActionExceptionHandler.class);

    /**
     * This method handles any java.lang.Exceptions that are not caught in
     * previous classes. It will loop through and get all the causes (exception
     * chain), create ActionErrors, add them to the request and then forward to
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

        logger.debug("Error occured in Struts action", ex);

        response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);

        return processException(ex, new Integer(500), request);
    }

    /**
     *
     * @param context
     * @param request
     * @param property
     * @param error
     * @param forward
     */
    public static void storeException(
        ComponentContext context,
        HttpServletRequest request,
        String property,
        ActionError error) {
        logger.debug("Storing exception into context");
        ActionErrors errors =
            (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
        if (errors == null) {
            errors = new ActionErrors();
        }
        errors.add(property, error);
        context.putAttribute(Globals.ERROR_KEY, errors);
    }


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
            ActionError error = new ActionError("errors.detail", urlTmp.toString());
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

}