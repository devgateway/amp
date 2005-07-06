/*
 *   DigiExceptionReport.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jul 4, 2003
     * 	 CVS-ID: $Id: ShowExceptionReport.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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

package org.digijava.module.exception.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import javax.servlet.ServletException;
import org.digijava.kernel.viewmanager.*;
import javax.mail.internet.InternetAddress;
import org.digijava.kernel.mail.DgEmailManager;
import javax.mail.Address;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.config.ExceptionEmails;
import java.util.*;
import org.digijava.module.exception.form.DigiExceptionReportForm;
import org.digijava.module.exception.util.ModuleErrorStack;

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

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        javax.servlet.http.HttpServletRequest request,
        javax.servlet.http.HttpServletResponse
        response) throws
        java.lang.Exception {

        DigiExceptionReportForm formReport = (DigiExceptionReportForm) form;
        ModuleErrorStack moduleErrorStack = null;
        String moduleInstance = request.getParameter("module");

        if( moduleInstance != null ) {
            moduleErrorStack = formReport.getModuleStack(moduleInstance);
        }

        return new ActionForward("/exception/showLayout.do?layout=exceptionLayout");
    }
}
