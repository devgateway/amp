/*
 *   DigiExceptionHandler.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jul 4, 2003
 * 	 CVS-ID: $Id: DigiExceptionHandler.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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

import java.util.Date;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.module.exception.form.DigiExceptionReportForm;
import org.digijava.kernel.Constants;

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

        Integer statusCode = null;
        Exception exception = null;
/*        Enumeration enum = request.getAttributeNames();
        while (enum.hasMoreElements()) {
            String item = (String)enum.nextElement();
            logger.debug("Name: " + item);
        }*/

        statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);

        if( request.getAttribute("javax.servlet.error.exception") != null ) {
            exception = (Exception) request.getAttribute(
                "javax.servlet.error.exception");

            if( statusCode == null ) {
                statusCode = new Integer(javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return ActionExceptionHandler.processException(exception, statusCode, request);
        } else {

            if( statusCode != null ) {
                if (statusCode.intValue() ==
                    javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST) {
                    exception = new Exception("Invalid path " + request.getRequestURL().toString());
                }

                if (statusCode.intValue() ==
                    javax.servlet.http.HttpServletResponse.SC_NOT_FOUND) {
                    exception = new Exception("Resource " + request.getRequestURL().toString() + " not found " );
                }
            }
        }

        return ActionExceptionHandler.processException(exception, statusCode, request);
    }

}