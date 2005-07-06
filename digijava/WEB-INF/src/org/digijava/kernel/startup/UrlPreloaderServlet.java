/*
 *   UrlPreloaderServlet.java
 *   @Author Irakli Nadareishvili inadareishvili@worldbank.org
 *   Created: Nov 22, 2003
 *   CVS-ID: $Id: UrlPreloaderServlet.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
 *
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: Nov 10, 2003
 *   CVS-ID: $Id: UrlPreloaderServlet.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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

package org.digijava.kernel.startup;

import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.digijava.commons.asyncdispatcher.AsyncBuffer;
import org.digijava.commons.asyncdispatcher.*;
import org.apache.log4j.*;

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