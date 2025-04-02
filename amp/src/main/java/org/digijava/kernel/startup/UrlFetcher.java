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

import java.net.*;
import java.io.*;
import org.apache.log4j.*;

/**
 * This class is used to fetch an HTML of a page from a given URL.
 */
public class UrlFetcher {

    private static Logger log = Logger.getLogger(UrlFetcher.class);

  /**
   * Method fetching a url and returning HTML output.
   *
   * @param urlString String
   * @throws NullPointerException
   * @return String
   */

  public static String fetch( String urlString ) throws NullPointerException {
    StringBuffer buf = new StringBuffer(); int c;
    long start = System.currentTimeMillis();
    try {

        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
    urlConnection.setRequestProperty("User-Agent","org.digijava.kernel.startup.UrlFetcher");
        urlConnection.connect();
        InputStream is = urlConnection.getInputStream();
        while ( (c = is.read()) != -1) buf.append( (char) c);

        long end = System.currentTimeMillis();
        long prcTime = end-start;
        log.info( "Fetched " + urlString + " in " + prcTime + " milliseconds");

    } catch (MalformedURLException ex1) {
      log.debug("Invalid URL ["+urlString+"] supplied");
    } catch ( Exception ex) {
      log.error( "ERROR: " + urlString, ex);
    }

     return new String ( buf );
 }

}
