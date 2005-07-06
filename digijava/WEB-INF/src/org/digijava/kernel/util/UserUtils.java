/*
 *   UserUtils.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Feb 01, 2004
 * 	 CVS-ID: $Id: UserUtils.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.entity.UserPreferencesPK;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.User;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * This class containts user-related utillity functions. User must be
 * <b>always</b> identified by User object
 */
public class UserUtils {
    private static Logger logger = Logger.getLogger(UserUtils.class);

    /**
     * Returns user preferences for particular site. null, if there are no
     * preferences defined for this user and site
     * @param user User, for which preferences are defined
     * @param site Site, which is the owner of user preferences
     * @throws DgException if error occurs.
     * @return UserPreferences
     */
    public static UserPreferences getUserPreferences(User user, Site site) throws
        DgException {

        logger.debug("Searching user preferences for user#" + user.getId() +
                     " site#" + site.getId());

        UserPreferences result = null;
        net.sf.hibernate.Session session = null;

        UserPreferencesPK key = new UserPreferencesPK(user, site);

        try {
            session = PersistenceManager.getSession();
            result = (UserPreferences) session.load(UserPreferences.class, key);

        }
        catch (ObjectNotFoundException ex) {
            logger.debug("User preferences does not exist");
        }
        catch (Exception ex) {
            logger.warn("Unable to get user preferences", ex);
            throw new DgException(ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed", ex2);
            }
        }

        return result;
    }

    /**
     * Returns user language preferences for particular site. null, if there
     * are no preferences defined for this user and site
     * @param user User, for which preferences are defined
     * @param site Site, which is the owner of user preferences
     * @throws DgException if error occurs.
     * @return UserLangPreferences
     */
    public static UserLangPreferences getUserLangPreferences(User user, Site site) throws
        DgException {

        logger.debug("Searching user language preferences for user#" + user.getId() +
                     " site#" + site.getId());

        UserLangPreferences result = null;
        net.sf.hibernate.Session session = null;

        UserPreferencesPK key = new UserPreferencesPK(user, site);

        try {
            session = PersistenceManager.getSession();
            result = (UserLangPreferences) session.load(UserLangPreferences.class, key);

        }
        catch (ObjectNotFoundException ex) {
            logger.debug("User language preferences do not exist");
        }
        catch (Exception ex) {
            logger.warn("Unable to get user language preferences", ex);
            throw new DgException(ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed", ex2);
            }
        }

        return result;
    }

    /**
     * Store user preferences
     * @param preferences UserPreferences user preferences object. Must have id
     * property initialized
     * @throws DgException
     */
    public static void saveUserPreferences(UserPreferences preferences) throws
        DgException {
        logger.debug("Saving user preferences for user#" +
                     preferences.getId().getUser().getId() +
                     " site#" + preferences.getId().getSite().getId());
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.
                getSession();
            UserPreferences existing = null;
            try {
                existing = (UserPreferences) sess.load(UserPreferences.class,
                    preferences.getId());
                logger.debug("Updating");
            }
            catch (ObjectNotFoundException ex2) {
                logger.debug("Creating new record");
            }
            tx = sess.beginTransaction();

            if (existing == null) {
                sess.save(preferences);
            }
            else {
                // Let's forget about old one
                sess.evict(existing);
                // Save new one
                sess.update(preferences);
            }
            tx.commit();
        }
        catch (Exception ex) {

            logger.error("Unable to update user preferences ", ex);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed ", ex);
                }
            }
            throw new DgException(
                "Unable to update user preferencese", ex);
        }
        finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ", ex1);
                }
            }

        }

    }

    /**
     * Returns user's biography for particular site
     * @param user User
     * @param site Site
     * @throws DgException If error occurs
     * @return user's biography for particular site
     */
    public static String getUserBiography(User user, Site site) throws
        DgException {
        UserPreferences up = getUserPreferences(user, site);
        if (up == null) {
            return null;
        }
        else {
            return up.getBiography();
        }
    }

    /**
     * Returns URL of user's photo to be displayed on the current site. The
     * current site (and its domain) is determined from the request. Returns
     * null if the current user does not have image
     * @param user User
     * @param request HttpServletRequest
     * @return String
     */
    public static String getUserImageUrl(User user, HttpServletRequest request) {
        /**
         * @todo Refactor these metod when RequestUtils class will be ready
         */
        SiteDomain siteDomain = RequestUtils.getSiteDomain(request);
        String siteUrl = DgUtil.getSiteUrl(siteDomain, request);

        if (user.getPhoto() == null) {
            logger.debug("user#" + user.getId() + " does not have image");
            return null;
        }
        else {
            String imageUrl = siteUrl + "/showImage.do?id=" +
                user.getPhoto().getId();

            logger.debug("Image URL for user#" + user.getId() +
                         " for the current site is: " + imageUrl);

            return imageUrl;
        }
    }

    /**
     * Store user's language preferences
     * @param preferences UserLangPreferences user preferences object. Must have id
     * property initialized
     * @throws DgException
     */
    public static void saveUserLangPreferences(UserLangPreferences preferences) throws
        DgException {
        logger.debug("Saving user's language preferences for user#" +
                     preferences.getId().getUser().getId() +
                     " site#" + preferences.getId().getSite().getId());
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.
                getSession();
            UserLangPreferences existing = null;
            try {
                existing = (UserLangPreferences) sess.load(UserLangPreferences.class,
                    preferences.getId());
                logger.debug("Updating");
            }
            catch (ObjectNotFoundException ex2) {
                logger.debug("Creating new record");
            }
            tx = sess.beginTransaction();

            if (existing == null) {
                sess.save(preferences);
            }
            else {
                // Let's forget about old one
                sess.evict(existing);
                // Save new one
                sess.update(preferences);
            }
            tx.commit();
        }
        catch (Exception ex) {

            logger.error("Unable to update user's language preferences ", ex);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed ", ex);
                }
            }
            throw new DgException(
                "Unable to update user's language preferencese", ex);
        }
        finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed ", ex1);
                }
            }

        }

    }
    /**
     * Gets User by scpecified id
     * @param id  id of user to be retrieved
     * @return User by specified id, null of no user by that id was found
     */
    public static User getUser(Long id) {
        User result = null;
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            result = (User) session.load(User.class, id);
            ProxyHelper.initializeObject(result);
        }

        catch (ObjectNotFoundException ex) {
            logger.debug("Unable to get User");
            return null;
        }
        catch (Exception ex) {
            logger.warn("Unable to get User", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed", ex2);
            }
        }

        return result;

    }

}
