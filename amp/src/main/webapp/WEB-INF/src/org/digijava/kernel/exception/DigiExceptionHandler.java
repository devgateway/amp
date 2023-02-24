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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.helper.Constants;

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
 * are no security constraints in RequestProcessor
 Name: javax.servlet.error.exception
 Name: javax.servlet.error.message
 Name: javax.servlet.error.request_uri
 Name: javax.servlet.error.status_code
 Name: org.digijava.kernel.dg_request_url
 Name: javax.servlet.error.exception_type

 */
public final class DigiExceptionHandler
    extends Action {

    private static Logger logger = Logger.getLogger(DigiExceptionHandler.class);

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        javax.servlet.http.HttpServletRequest request,
        javax.servlet.http.HttpServletResponse
        response) throws
        java.lang.Exception {

        Integer statusCode = (Integer) request.getAttribute(
            "javax.servlet.error.status_code");
        String errorMessage = (String) request.getAttribute(
            "javax.servlet.error.message");
        String requestUri = (String) request.getAttribute(
            "javax.servlet.error.request_uri");
        Throwable cause = (Throwable) request.getAttribute(
            "javax.servlet.error.exception");



        //response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);

        String errorMsg = errorMessage + "[ URI:" + requestUri + ", URL:" +
            request.getRequestURL() + "]";

        ExceptionInfo info = ExceptionHelper.populateExceptionInfo(statusCode,
            cause, request);
        if (info.getErrorMessage() == null) {
            info.setErrorMessage(errorMsg);
        }
        else {
            info.setErrorMessage(errorMsg + ";" + info.getErrorMessage());
        }
        //in case an exception happend check if we need to clean the request for token authentication

        return ExceptionHelper.processExceptionInfo(info, request, response);

    }
}
