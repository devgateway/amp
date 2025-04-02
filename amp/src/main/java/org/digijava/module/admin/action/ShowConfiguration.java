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

package org.digijava.module.admin.action;




import java.io.*;

import org.apache.struts.action.*;
import org.digijava.module.admin.form.*;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.DgUtil;
import javax.servlet.ServletException;
import org.apache.struts.tiles.ComponentContext;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.digijava.kernel.util.RequestUtils;

public class ShowConfiguration extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
      ConfigurationForm configurationForm = (ConfigurationForm) form;
      String forward = "configPage";

      Site site = RequestUtils.getSite(request);

          //SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.
          //    CURRENT_SITE);
          if (site == null) {
            throw new ServletException("Unknown site");
          }

           // Get context parameters from configuration XML file and prepare them
          // to insert into tiles context
          ViewConfig viewConfig = ViewConfigFactory.getInstance().getViewConfig(site);
          configurationForm.setXmlString(viewConfig.getXML());



      return mapping.findForward(forward);
    }
}
