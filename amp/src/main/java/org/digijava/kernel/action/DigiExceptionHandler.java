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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;

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
