/*
 *   UrlPreloaderThread.java
 *   @Author Irakli Nadareishvili inadareishvili@worldbank.org
 *   Created: Nov 22, 2003
 *   CVS-ID: $Id: UrlPreloaderThread.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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

public class UrlPreloaderThread
    extends Thread {

    private static Logger log = Logger.getLogger(UrlPreloaderThread.class);

    public static final int BUFFER_SIZE = 255;
    public static final int POOL_SIZE = 1;
    // How many simultanous threads to use at max.
    public static int maxThreads = 0;
    private static AsyncBuffer[] asyncBuffs;
    private static String[] urls;

    public UrlPreloaderThread(String configFilePath, int _maxThreads) {
        String s;
        StringBuffer szFInput = new StringBuffer();

        maxThreads = _maxThreads;
        asyncBuffs = new AsyncBuffer[maxThreads];

        try {
            BufferedReader in = new BufferedReader(new FileReader(
                configFilePath));
            while ( (s = in.readLine()) != null) {
                szFInput.append(s + "\n");
            }
            in.close();
        }
        catch (java.io.IOException e) {
            log.error("getAndProcessUrls() failed ", e);
        }

        urls = new String(szFInput).split("\n");

    }

    public void run() {
        createBuffs();
        int no = 0;

        for (int i = 0; i < urls.length; i++) {
            no = i % (maxThreads - 1);
            asyncBuffs[no].put(urls[i]);
        }

        destroyBuffs();
    }

    private void createBuffs() {
        for (int i = 0; i < maxThreads; i++) {
            asyncBuffs[i] = new AsyncBuffer(
                new UrlPreloader(),
                BUFFER_SIZE,
                POOL_SIZE
                );
        }
    }

    private void destroyBuffs() {
        for (int i = 0; i < maxThreads; i++) {
            asyncBuffs[i].close();
        }
    }

}