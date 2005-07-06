/*
 *   DigiMessageResources.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 * 	 CVS-ID: $Id: DigiMessageResources.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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


package org.digijava.kernel.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

import org.apache.log4j.Logger;
import org.apache.struts.util.PropertyMessageResources;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.*;
import java.util.Locale;
import org.digijava.kernel.request.Site;

/**
 *
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DigiMessageResources extends PropertyMessageResources {

    private static Logger logger = Logger.getLogger(DigiMessageResources.class);

    // ----------------------------------------------------------- Constructors


    /**
     *
     * @param factory
     * @param config
     */
    public DigiMessageResources(MessageResourcesFactory factory,
                                    String config) {

        super(factory, config);
        logger.info("Initializing, config='" + config + "'");

    }


    /**
     *
     * @param factory
     * @param config
     * @param returnNull
     */
    public DigiMessageResources(MessageResourcesFactory factory,
                                    String config, boolean returnNull) {

        super(factory, config, returnNull);
        logger.info("Initializing, config='" + config +
                 "', returnNull=" + returnNull);

    }


    // ------------------------------------------------------------- Properties



    // --------------------------------------------------------- Public Methods


    public String getMessage(Locale locale, String key) {

        String message = null;

        if( key.startsWith("@") )
            message = getMessageFromDatabase(locale, key);
        else
            return super.getMessage(locale, key);

        logger.debug("getMessage(" + locale + "," + key + ")");

       return message;
    }

    /**
     *
     * @param key
     * @return
     */
    private String getMessageFromDatabase(Locale locale, String key) {

        Message message = null;
        String currentLocale = null;
        String currentSiteId = null;
        String messageKey = null;

        String args[] = DgUtil.fastSplit(key.substring(1, key.length() - 1), '.');
        if (args != null && args.length >= 3) {

            logger.debug("Current locale: " + args[0]);
            logger.debug("Current site: " + args[1]);

            // get SiteName
            messageKey = key.substring(args[0].length() + args[1].length() + 3,
                                       key.length());

            logger.debug("Current new key: " + messageKey);

            TranslatorWorker worker = TranslatorWorker.getInstance(messageKey);
            try {
                SiteCache siteCache = SiteCache.getInstance();
                Site site = siteCache.getSite(args[1]);
                message = worker.getFromGroup(messageKey, args[0], site);
                if (message == null) {
                    logger.debug("Message not found key: " + messageKey);
                    return super.getMessage(locale, messageKey);
                } else {
                    logger.debug("Message found key: " + messageKey);
                    logger.debug("Message: " + message.getMessage());
                    return message.getMessage();
                }
            }
            catch (WorkerException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }




}
