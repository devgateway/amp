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

package org.digijava.kernel.request;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class RerouteAction
    extends TilesAction {

    public static Logger log = Logger.getLogger(RerouteAction.class);

    public ActionForward execute(ComponentContext context,
                                 ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        IOException, ServletException {

        String siteName = (String) request.getAttribute(Constants.SITE_ID);
        if (siteName == null) {
            siteName = Constants.DEFAULT_SITE_NAME;
        }
        // Check if we need to redirect
        Iterator iter = context.getAttributeNames();
        String name;
        Object value;
        int defaultLength = Constants.DEFAULT_SITE_NAME.length();
        while (!siteName.equals(Constants.DEFAULT_SITE_NAME) && iter.hasNext()) {
            name = (String) iter.next();
            value = context.getAttribute(name);
            if (value.getClass().getName().indexOf("List") != -1) {
                LinkedList myList = new LinkedList();
                Iterator iter1 = ( (List) value).iterator();
                while (iter1.hasNext()) {
                    String myValue = (String) iter1.next();
                    log.debug("list: " + name + "   " + myValue);
                    int start = myValue.indexOf(Constants.DEFAULT_SITE_NAME);
                    if (start != -1) {
                        myList.add( (new StringBuffer(myValue)).replace(start,
                            start + defaultLength, siteName).toString());
                    }
                    else {
                        myList.add(myValue);
                    }
                }
                context.putAttribute(name, myList);
            }
            else {
                int start = ( (String) value).indexOf(Constants.
                    DEFAULT_SITE_NAME);
                if (start != -1) {
                    context.putAttribute(name,
                                         (new StringBuffer( (String) value)).
                                         replace(start, start + defaultLength,
                                                 siteName).toString());
                }
                else {
                    context.putAttribute(name, (String) value);
                }
                log.debug("attribute: " + name + "   " + (String) value);
            }
        }

        iter = context.getAttributeNames();
        log.debug("after:");
        while (iter.hasNext()) {
            name = (String) iter.next();
            if (name.equalsIgnoreCase("left") || name.equalsIgnoreCase("right")) {
                Iterator iter1 = ( (List) context.getAttribute(name)).iterator();
                while (iter1.hasNext()) {
                    log.debug(name + "   " + (String) iter1.next());
                }
            }
            else {
                log.debug(name + "   " + (String) context.getAttribute(name));
            }
        }
        return null;

    }
}
