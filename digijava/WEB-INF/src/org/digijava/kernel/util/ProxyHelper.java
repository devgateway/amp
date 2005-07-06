/*
 *   ProxyHelper.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Apr 05, 2004
 * 	 CVS-ID: $Id: ProxyHelper.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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

import org.digijava.kernel.user.User;
import net.sf.hibernate.HibernateException;
import org.apache.log4j.Logger;
import net.sf.hibernate.Hibernate;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.entity.Image;
import java.util.Collection;
import java.util.Iterator;

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