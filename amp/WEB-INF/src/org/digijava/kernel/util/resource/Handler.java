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

package org.digijava.kernel.util.resource;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Handler
    extends java.net.URLStreamHandler {
    private static Log logger = LogFactory.getLog(Handler.class);

    public static final String PREFIX = "resource:";

    protected URLConnection openConnection(URL u) throws IOException {


        String url = u.toString();
        if (url.startsWith(PREFIX)) {
            url = url.substring(PREFIX.length());
            String resource = "/" + url;
            logger.debug("Searching for resource " + resource);

            logger.debug("thread's class loader is: " +
                        Thread.currentThread().getContextClassLoader().toString());

            URL resourceURL = Thread.currentThread().getContextClassLoader().
                getResource(resource);
            if (resourceURL != null) {
                logger.debug("Returning from thread's context class loader");
                return resourceURL.openConnection();
            }
            resourceURL = this.getClass().getResource(resource);
            if (resourceURL != null) {
                logger.debug("Returning from " + Handler.class + "'s class loader");
                return resourceURL.openConnection();
            }
            logger.error("Can not find resource for " + u.toString());
            return null;
        }
        else {
            throw new IOException("Can not process URL:  " + url);
        }
    }
}
