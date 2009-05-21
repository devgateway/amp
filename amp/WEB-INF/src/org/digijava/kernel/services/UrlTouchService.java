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

package org.digijava.kernel.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.digijava.commons.asyncdispatcher.AsyncBuffer;
import org.digijava.commons.asyncdispatcher.AsyncHandler;
import org.digijava.kernel.service.AbstractServiceImpl;
import org.digijava.kernel.service.ServiceContext;
import org.digijava.kernel.service.ServiceException;
import org.digijava.kernel.service.ServiceManager;

public class UrlTouchService
    extends AbstractServiceImpl implements AsyncHandler {

    private static Logger logger = Logger.getLogger(UrlTouchService.class);

    private int bufferSize;
    private int poolSize;
    private String userAgent;

    private AsyncBuffer asyncBuffer;

    public UrlTouchService() {
        bufferSize = 64;
        poolSize = 1;
    }

    public void asyncTouchUrl(String url) {
        synchronized (this) {
            if (asyncBuffer != null) {
                logger.debug("Putting URL: " + url);
                asyncBuffer.put(url);
            }
        }
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    protected void processInitEvent(ServiceContext serviceContext) throws
        ServiceException {
        if (poolSize <= 0) {
            throw new ServiceException(
                "Parameter poolSize must be greater than 0");
        }

        if (bufferSize <= poolSize) {
            throw new ServiceException("Parameter bufferSize(" + bufferSize +
                                       ") must be greater than poolSize(" +
                                       poolSize + ")");
        }
    }

    protected void processCreateEvent() throws ServiceException {
        logger.debug("Creating AsyncBuffer");
        synchronized (this) {
            asyncBuffer = new AsyncBuffer(this, bufferSize, poolSize);
        }
    }

    protected void processDestroyEvent() throws ServiceException {
        logger.debug("Destroying AsyncBuffer");
        synchronized (this) {
            asyncBuffer.close();
            asyncBuffer = null;
        }
    }

    public void handleSingleItem(Object object) throws ClassCastException {
        touchUrl( (String) object, userAgent);
    }

    public static void touchUrl(String urlString, String userAgent) {
        int c;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (userAgent != null && userAgent.trim().length() != 0) {
                urlConnection.setRequestProperty("User-Agent", userAgent);
            }
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            while ( (c = is.read()) != -1) {
                ;
            }
            is.close();
            logger.debug("Fetched URL: " + urlString + " as user-agent " +
                         userAgent);
        }
        catch (IOException ex) {
            logger.warn("Could not fetch URL: " + urlString, ex);
        }
    }
}
