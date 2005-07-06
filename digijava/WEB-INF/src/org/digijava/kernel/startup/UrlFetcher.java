/*
 *   UrlFetcher.java
 *   @Author Irakli Nadareishvili inadareishvili@worldbank.org
 *   Created: Jan 6, 2004
 *   CVS-ID: $Id: UrlFetcher.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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
