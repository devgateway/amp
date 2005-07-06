/*
*   RerouteAction.java
*   @Author Philipp Anokhin
*   Created:
*   CVS-ID: $Id: RerouteAction.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.request;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.actions.TilesAction;
import org.digijava.kernel.Constants;

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