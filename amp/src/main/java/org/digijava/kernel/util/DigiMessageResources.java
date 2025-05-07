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

package org.digijava.kernel.util;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResourcesFactory;
import org.apache.struts.util.PropertyMessageResources;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;

import java.util.Locale;

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
        if (logger.isDebugEnabled()) logger.debug("Initializing, config='" + config + "'");
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
        if (logger.isDebugEnabled()) logger.debug("Initializing, config='" + config + "', returnNull=" + returnNull);
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
    protected String getMessageFromDatabase(Locale locale, String key) {

        Message message = null;
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
                //SiteCache siteCache = SiteCache.getInstance();
                Site site = SiteCache.lookupByName(args[1]);
                if (site == null) {
                    logger.warn("Site: " + args[1] + " was not found");
                    return super.getMessage(locale, messageKey);
                }
                message = getFromGroup(worker,messageKey, args[0], site);
                if( message == null) {
                    logger.debug("Message not found key: " + messageKey);
                    return super.getMessage(locale, messageKey);
                }

                logger.debug("Message found key: " + messageKey);
                logger.debug("Message: " + message.getMessage());
                return message.getMessage();
            }
            catch (WorkerException ex) {
                logger.error("Unable to process resource. Locale=" + locale + " key=" + key, ex);
            }
        }

        return null;
    }


    public Message getFromGroup(TranslatorWorker worker,String key, String locale, Site site) throws
        WorkerException {

        Long siteId = site.getId();

        Message trnMess = worker.getByKey(key, locale, siteId);
        if (trnMess != null) {
            log.debug("local translation exists");
            return trnMess;
        }

        Long rootSiteId = SiteCache.getInstance().getRootSite(site).getId();
        if (!rootSiteId.equals(siteId)) {
            trnMess = worker.getByKey(key, locale, rootSiteId);
            if (trnMess != null) {
                log.debug("group translation exists");
                return trnMess;
            }
        }

        trnMess = worker.getByKey(key, locale, 0L);
        if (trnMess != null) {
            log.debug("global translation exists");
            return trnMess;
        }

        return null;
    }

}
