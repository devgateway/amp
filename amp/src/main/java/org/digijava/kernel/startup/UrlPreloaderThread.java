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
