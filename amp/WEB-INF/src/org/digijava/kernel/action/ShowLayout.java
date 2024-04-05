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

package org.digijava.kernel.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.ServletException;
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
        // dirty fix sorry, Mikheil :(
        if (layout.equals("login")
                && FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.PUBLIC_PORTAL)) {
            layout = "publicPortalLoginLayout";
            String publicPortalUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PUBLIC_PORTAL_URL);
            request.setAttribute("publicPortalUrl", publicPortalUrl);
        }
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
