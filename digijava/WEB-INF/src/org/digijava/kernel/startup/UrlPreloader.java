/*
 *   UrlPreloader.java
 *   @Author Irakli Nadareishvili inadareishvili@worldbank.org
 *   Created: Jan 6, 2004
 *   CVS-ID: $Id: UrlPreloader.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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

import org.digijava.commons.asyncdispatcher.AsyncHandler;
import org.apache.log4j.*;

public class UrlPreloader implements AsyncHandler {

  private static Logger log = Logger.getLogger(UrlPreloader.class);

   public UrlPreloader() {

   }

   /**
    * Callback method used in AsyncProcessor
    *
    * Fetches the URL, which basically means it sends an HTTP GET to
    * the page, so that server compiles the jsp, and initates any
    * caches it may be using.
    *
    * @param url Object
    */

   public void handleSingleItem ( Object url ) {
        String urlStrng = (String) url;
        UrlFetcher.fetch( urlStrng );
   }

}
