/*
 *   Exception.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Jul 4, 2003
 * 	 CVS-ID: $Id: DigiExceptionHandler.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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

package org.digijava.kernel.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import java.util.*;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import javax.servlet.ServletException;
import org.digijava.kernel.viewmanager.*;

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
        if (statusCode != null) {
            Site site = RequestUtils.getSite(request);

            // If Error code layout is defined for site configuration
            // redirect there. If not - then skip
            ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);

            String layout = "http" + statusCode.toString() + "layout";

            if (viewConfig.isLayoutDefined(layout)) {
                logger.debug("Forward layout " + layout);
                response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
                return new ActionForward("/showLayout.do?layout=" + layout);
            }
            else {
                logger.debug("No error layout " + layout + " defined for site " +
                             site.getSiteId());
            }
        }

        return null;

    }

}