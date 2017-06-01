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

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.AMPUncheckedException;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.entity.UserPreferencesPK;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.security.principal.GroupPrincipal;
import org.digijava.kernel.security.principal.UserPrincipal;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * This class containts user-related utillity functions. User must be
 * <b>always</b> identified by User object
 */
public class UserUtils {
	private static Logger logger = Logger.getLogger(UserUtils.class);

	/**
	 * Compares users by identifiers
	 */
	public static final Comparator USER_COMPARATOR;


	static {
		USER_COMPARATOR = new Comparator() {
			public int compare(Object o1, Object o2) {
				User user1 = (User)o1;
				User user2 = (User)o2;

				return user1.getId().compareTo(user2.getId());
			}
		};
	}
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
		org.hibernate.Session session = null;

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
	public static UserLangPreferences getUserLangPreferences(User user,Site site) throws DgException {

		logger.debug("Searching user language preferences for user#" +user.getId() +" site#" + site.getId());
		UserLangPreferences result = null;
		org.hibernate.Session session = null;

		UserPreferencesPK key = new UserPreferencesPK(user, site);

		try {
			session = PersistenceManager.getSession();
			result = (UserLangPreferences) session.load(UserLangPreferences.class,
					key);

		}
		catch (ObjectNotFoundException ex) {
			logger.debug("User language preferences do not exist");
		}
		catch (Exception ex) {
			logger.warn("Unable to get user language preferences", ex);
			throw new DgException(ex);
		}

		return result;
	}

	/**
	 * Bulk version of user lang preferences retrieval.
	 */
	public static Map<Long, UserLangPreferences> getUserLangPreferences(List<User> users, Site site) {
		List<UserPreferencesPK> keys = users.stream().map(u -> new UserPreferencesPK(u, site)).collect(toList());

		org.hibernate.Session session = PersistenceManager.getSession();
		List<UserLangPreferences> preferences = session
				.createCriteria(UserLangPreferences.class)
				.add(Restrictions.in("id", keys))
				.list();

		Map<Long, UserLangPreferences> result = new HashMap<>();
		preferences.forEach(p -> result.put(p.getId().getUser().getId(), p));
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
//beginTransaction();

			if (existing == null) {
				sess.save(preferences);
			}
			else {
				// Let's forget about old one
				sess.evict(existing);
				// Save new one
				sess.update(preferences);
			}
			//tx.commit();
		}
		catch (Exception ex) {

			logger.error("Unable to update user preferences ", ex);
			throw new DgException(
					"Unable to update user preferencese", ex);
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
//beginTransaction();

			if (existing == null) {
				sess.save(preferences);
			}
			else {
				// Let's forget about old one
				sess.evict(existing);
				// Save new one
				sess.update(preferences);
			}
			//tx.commit();
		}
		catch (Exception ex) {

			logger.error("Unable to update user's language preferences ", ex);
			throw new DgException(
					"Unable to update user's language preferencese", ex);
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
			session = PersistenceManager.getRequestDBSession();
			result = (User) session.load(User.class, id);
			ProxyHelper.initializeObject(result);
		}

		catch (ObjectNotFoundException ex) {
			logger.debug("Unable to get User");
			return null;
		}
		catch (Exception ex) {
			logger.warn("Unable to get User", ex);
			throw new RuntimeException(ex);
		}
		return result;

	}

	/**
	 * Retrieves all users.
	 * @return list of users
	 */
	public static List<User> getAllUsers() {
		List<User> users = PersistenceManager.getSession()
				.createCriteria(User.class)
				.list();
		return users;
	}
	
	/**
	 * Retrieves users data for the required users ids
	 * @param userIds the users ids
	 * @return list of users
	 */
	public static List<User> getUsers(List<Long> userIds) {
	    if (userIds == null || userIds.isEmpty())
	        return Collections.emptyList();
	    List<User> users = PersistenceManager.getSession().createQuery("from " + User.class.getName() + " o " + 
	            "where o.id in (:ids)").setParameterList("ids", userIds).list();
	    users.forEach(user -> ProxyHelper.initializeObject(user));
	    return users;
	}

	/**
	 * Searchs users with given criteria
	 * @param criteria criteria by which users are searched
	 * @return list of users matching given criteria
	 * @throws DgException
	 */
	public static List searchUsers(String criteria) throws DgException {
		return searchUsers(criteria, null);
	}

	/**
	 * Searchs users with given criteria and sorts users list according to orderBy parameter
	 * @param criteria criteria by which users are searched
	 * @param orderBy list of User object properties by which users list should be ordered
	 * @return list of users matching given criteria
	 * @throws DgException
	 */
	public static List searchUsers(String criteria, String orderBy[]) throws
	DgException {

		Session session = null;
		List userList;

		try {

			session = PersistenceManager.getRequestDBSession();

			String criteriaLow = criteria.trim().toLowerCase();
			String criteriaLowConcat = new String("");

			StringTokenizer st = new StringTokenizer(criteriaLow);
			while (st.hasMoreTokens()) {
				criteriaLowConcat += st.nextToken();
			}
			criteriaLow = "%" + criteriaLow + "%";
			criteriaLowConcat = "%" + criteriaLowConcat + "%";

			String queryString = "from " + User.class.getName() +
			" usr where usr.email like :criteriaLow or lower(usr.firstNames) like :criteriaLow or " +
			" lower(usr.lastName) like :criteriaLow or lower(usr.firstNames || usr.lastName) like :criteriaLowConcat";

			if (orderBy != null && orderBy.length != 0) {
				String orderStr = " order by " + "usr." + orderBy[0];
				for (int i = 1; i < orderBy.length; i++) {
					orderStr += ", " + "usr." + orderBy[i];
				}

				queryString += orderStr;
			}

			Query query = session.createQuery(queryString);
			query.setString("criteriaLow", criteriaLow);
			query.setString("criteriaLowConcat", criteriaLowConcat);

			userList = query.list();

			Iterator userIter = userList.iterator();
			while (userIter.hasNext()) {
				User user = (User) userIter.next();
				ProxyHelper.initializeObject(user);
			}

		}

		catch (Exception ex) {
			logger.debug("Unable to get users list from database ", ex);
			throw new DgException(
					"Unable to get users list from database", ex);
		}

		return userList;
	}

	/**
	 * Creates and returns Subject for the given user
	 * @param user User
	 * @return Subject for the given user
	 */
	public static Subject getUserSubject(User user) {
		return fillUserSubject(null, user);
	}

	/**
	 * Fills given subject with user's information and returns it. If subject
	 * is null, new instance is created
	 * @param subject Subject user's subject
	 * @param user User
	 * @return Subject, filled with user's information
	 */
	public static Subject fillUserSubject(Subject subject, User user) {
		if (subject == null) {
			subject = new Subject();
		}
		subject.getPrincipals().clear();
		UserPrincipal userPrincipal = new UserPrincipal(user);
		subject.getPrincipals().add(userPrincipal);

		Iterator iter = user.getGroups().iterator();
		while (iter.hasNext()) {
			Group group = (Group) iter.next();
			GroupPrincipal groupPrincipal = new GroupPrincipal(group);
			subject.getPrincipals().add(groupPrincipal);
		}
		/*
        if (user.isGlobalAdmin()) {
            GlobalAdminGroup adminGroup = new GlobalAdminGroup();
            adminGroup.addMember(userPrincipal);
            subject.getPrincipals().add(adminGroup);
        }
		 */
		subject.getPublicCredentials().clear();
		subject.getPrivateCredentials().clear();

		return subject;

	}

	/**
	 * Searches user object by email and returns it. If such user does not
	 * exists, returns null
	 * <p>The sole purpose of this function is to handle checked DgException by converting it to
	 * unchecked exception.</p>
	 * @param email String User email
	 * @return User object
	 * @throws AMPUncheckedException if error occurs
	 */
	public static User getUserByEmailRt(String email) {
		try {
			return UserUtils.getUserByEmail(email);
		} catch (DgException e) {
			throw new AMPUncheckedException(e);
		}
	}

	/**
	 * Searches user object by email and returns it. If such user does not
	 * exists, returns null
	 * @param email String User email
	 * @return User object
	 * @throws DgException if error occurs
	 */
	public static User getUserByEmail(String email) throws DgException {
		User user = null;
		Session sess = null;
		try {
			sess = PersistenceManager.getRequestDBSession();
			
			Query query = sess.createQuery("from " + User.class.getName() + " rs where rs.email = :email ");
			query.setString("email", email);
			query.setCacheable(true);

			Iterator iter = query.iterate();
			while (iter.hasNext()) {
				user = (User) iter.next();
				ProxyHelper.initializeObject(user);
				break;
			}

		}
		catch (Exception ex0) {
			logger.debug("Unable to get user from database", ex0);
			throw new DgException(
					"Unable to get user information from database", ex0);
		}

		return user;
	}

	/**
	 * Put new password in user object. This method does not perform any
	 * database modifications. It simply puts new password and salt in object's
	 * fields
	 * @param user User
	 * @param password String new password
	 */
	public static void setPassword(User user, String password) {
		user.setPassword(ShaCrypt.crypt(password.trim()).trim());
		user.setSalt(new Long(password.trim().hashCode()).toString());
		user.setPasswordChangedAt(new Date());
	}
	
	/**
	 * checks whether user is admin or not 
	 * @return
	 */
	public static boolean isAdmin(User user,Site site){
		boolean retVal=false;
		Subject subject = UserUtils.getUserSubject(user);
		retVal = DgSecurityManager.permitted(subject, site,ResourcePermission.INT_ADMIN);
		return retVal;
	}
}
