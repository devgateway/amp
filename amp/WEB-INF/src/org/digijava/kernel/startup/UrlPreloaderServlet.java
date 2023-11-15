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

package org.digijava.kernel.startup;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import java.io.File;

public class UrlPreloaderServlet
    extends HttpServlet {

    private static Logger log = Logger.getLogger(UrlPreloaderServlet.class);

    public void init() {
        boolean successfulConfig = true;

        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("urls-list-file");
        String maxThreads = getInitParameter("max-threads");

        if (file != null) {
            file = file.replace('/', File.separatorChar);
            file = prefix + file;
            log.debug("Urls to preprocess taken from: " + file);
        }
        else {
            log.warn(
                "Url preloader config file not indicated in servlet config");
            successfulConfig = false;
        }

        if (maxThreads == null) {
            successfulConfig = false;
            log.warn("Max Number of threads not indicated in servlet config");
        }
        int iMaxThreads = Integer.parseInt(maxThreads);
        if (iMaxThreads > 0) {}
        else {
            successfulConfig = false;
            log.warn("Max Number of threads not indicated in servlet config");
        }

        if (successfulConfig) {
            UrlPreloaderThread myT = new UrlPreloaderThread(file, iMaxThreads);
            myT.setPriority(Thread.MIN_PRIORITY);
            myT.setName("Url Preloader Main Thread");
            myT.start();
        }

    }

}
