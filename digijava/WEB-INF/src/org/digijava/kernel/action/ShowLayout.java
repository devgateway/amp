/*
 *   ShowLayout.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 4, 2003
 * 	 CVS-ID: $Id: ShowLayout.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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

import java.util.HashMap;
import javax.servlet.ServletException;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.Constants;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.util.*;
import org.apache.struts.Globals;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import java.util.Map;

/**
 * Struts action, which shows site's layout if "layout" parameter is set, then
 * it shows layout from site-config.xml with the same name. If this parameter
 * does not exist, default layout will be shown.
 * @author Mikheil Kapanadze
 * @version $Version$
 */
public class ShowLayout
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse response) throws
      java.lang.Exception {

      Site site = RequestUtils.getSite(request);

    //SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
    //    CURRENT_SITE);
    if (site == null) {
      throw new ServletException("Unknown site");
    }


    String layout;
    layout = request.getParameter("layout") == null? "default" : request.getParameter("layout");


    // Get tiles context or create new one if it does not exist
    ComponentContext context = ComponentContext.getContext(request);
    if (context == null) {
      context = new ComponentContext();
      ComponentContext.setContext(context, request);
    }

    // Get context parameters from configuration XML file and prepare them
    // to insert into tiles context
    ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);
    Map contextParameters = viewConfig.getMasterContextAttributes(layout);

    // Put attributes into tiles context
    context.addAll(contextParameters);

    // Calculate layout path
    String layoutConfigPath = viewConfig.getMainLayoutPath(layout);

    return new ActionForward(layoutConfigPath);

  }

}