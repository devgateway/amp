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
import org.digijava.kernel.entity.Image;
import org.digijava.kernel.user.User;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

/**
 * This class contains utillity methods to initialize proxy classes/lazy
 * collections, etc.
 */
public class ProxyHelper {
    private static Logger logger = Logger.getLogger(ProxyHelper.class);

    /**
     * Fully initialize User object
     * @param user User object
     * @throws HibernateException if error occurs during initialization. For
     * example - when owner session is closed
     */
    public static void initializeObject(User user) throws HibernateException {
        ProxyHelperConfig config = (ProxyHelperConfig) DigiConfigManager.
            getConfigBean("proxyHelperConfig");
        boolean initialize = config == null ? true : config.isInitializeUser();

        if (initialize) {
            logger.debug("Initializing User");
            Hibernate.initialize(user);
            logger.debug("Initializing Interests");
            Hibernate.initialize(user.getInterests());
            logger.debug("Initializing Groups");
            Hibernate.initialize(user.getGroups());
            logger.debug("Initializing image");
            Hibernate.initialize(user.getPhoto());
            logger.debug("Initializing registered through");
            Hibernate.initialize(user.getRegisteredThrough());
        }
        else {
            logger.debug("User object initialization is turned off");
        }
    }

    /**
     * Fully initialize Image object
     * @param image Image object
     * @throws HibernateException if error occurs during initialization. For
     * example - when owner session is closed
     */
    public static void initializeObject(Image image) throws HibernateException {
        logger.debug("Initializing Image");
        Hibernate.initialize(image);
    }
}
