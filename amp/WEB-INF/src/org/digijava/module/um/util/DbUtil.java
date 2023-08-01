/*
 *   DbUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id$
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
package org.digijava.module.um.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.*;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.*;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.um.dbentity.ResetPassword;
import org.digijava.module.um.dbentity.SuspendLogin;
import org.digijava.module.um.exception.UMException;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.*;

public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);


    public static long getuserId(String email) throws UMException {
        long userId = 0;
        Session sess = null;
        try {
            sess = org.digijava.kernel.persistence.PersistenceManager.
                getSession();

            String queryString = "from " + User.class.getName() + " rs where rs.email = :email";
            Query query = sess.createQuery(queryString);
            query.setString("email", email);
            
            Iterator iter = query.iterate();
            
            while(iter.hasNext()) {
                User iterUser = (User) iter.next();
                userId = iterUser.getId().longValue();
                break;
            }

        } catch(Exception ex0) {
            logger.debug("Unable to get user information from database", ex0);
            throw new UMException("Unable to get user information from database", ex0);
        }

        return userId;
    }

    /**
     * check if password is Correct in database
     * @param user
     * @param password
     * @return
     * @throws ServletException
     */
    public static boolean isCorrectPassword(String user, String pass) throws
        UMException {
        Session sess = null;
        String compare = null;
        String encryptPassword = null;
        boolean iscorrect = false;
        try {
            sess = org.digijava.kernel.persistence.PersistenceManager.
                getSession();

            String queryString = "from " + User.class.getName() + " rs where rs.email = :email";
            Query query = sess.createQuery(queryString);
            query.setString("email", user);
            
            Iterator iter = query.iterate();
//////////////////////
            while(iter.hasNext()) {
                User iterUser = (User) iter.next();

                for(int i = 0; i < 3; i++) {

                    switch(i) {
                        case 0:

                            compare = pass.trim() + iterUser.getSalt();

                            // first try new user ( using SHA1 )
                            encryptPassword = ShaCrypt.crypt(compare.trim()).trim().toUpperCase();
                            break;

                        case 1:

                            compare = pass.trim();

                            // first try new user ( using SHA1 )
                            encryptPassword = ShaCrypt.crypt(compare.trim()).trim();
                            break;

                        case 2:

                            // second try old user ( using unix crypt )
                            if(!pass.startsWith("8x"))
                                encryptPassword = UnixCrypt.crypt("8x", pass.trim()).
                                    trim();
                            else
                                encryptPassword = pass.trim();
                            break;
                    }

                    // check user in database
                    if(encryptPassword.equalsIgnoreCase(iterUser.getPassword().trim())) {
                        iscorrect = true;
                        break;
                    }
                }

            }

/////////////////////

        } catch(Exception ex0) {
            logger.debug("isCorrectPassword() failed", ex0);
            throw new UMException(ex0.getMessage(), ex0);
        }

        return iscorrect;
    }


    /**
     * Reset Password in Database
     * @param email
     * @param newPassword
     * @param confirmPassword
     * @throws ServletException
     */
    public static boolean ResetPassword(String email, String code, String newPassword) throws
        UMException {
        
        Transaction tx = null;
        Session session = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.
                getSession();
//beginTransaction();
            String queryString = "from " + User.class.getName() + " rs where rs.email = :email";
            Query query = session.createQuery(queryString);
            query.setString("email", email);
            
            Iterator iter = query.iterate();
            User iterUser = null;
            while(iter.hasNext()) {
                iterUser = (User) iter.next();
                break;
            }
            if(iterUser == null) {
                logger.warn("Attempt to reset password for unknown user: " + email);
                return false;
            }

            ResetPassword resetPassword = null;
            try {
                resetPassword = (ResetPassword) session.load(ResetPassword.class,
                    iterUser.getId());
            } catch(ObjectNotFoundException ex2) {
                logger.warn("User " + email + " have not requested password reset change");
                return false;
            }

            if(!resetPassword.getCode().equals(code)) {
                logger.error("Invalid password request code");
                return false;
            }

            iterUser.setPassword(ShaCrypt.crypt(newPassword.trim()).trim());
            iterUser.setSalt(new Long(newPassword.trim().hashCode()).toString());
            session.update(iterUser);
            session.delete(resetPassword);
            //tx.commit();

        } catch(Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            if(tx != null) {
                try {
                    tx.rollback();
                } catch(HibernateException ex1) {
                    logger.warn("rollback() failed ", ex1);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }
        return true;

    }

    /**
     * Update password in database see table
     *
     * @param user
     * @throws ServletException
     */
    public static void updatePassword(String user, String oldPassword,
                                      String newPassword) throws
        UMException {

        Session session = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.getSession();
            
            String queryString = "from " + User.class.getName() + " rs where trim(lower(rs.email)) = :email";
            Query query = session.createQuery(queryString);
            query.setString("email", user);
            
            Iterator iter = query.iterate();
            User iterUser = null;
            while(iter.hasNext()) {
                iterUser = (User) iter.next();
            }
//beginTransaction();
            iterUser.setPassword(ShaCrypt.crypt(newPassword.trim()).trim());
            iterUser.setSalt(new Long(newPassword.trim().hashCode()).toString());
            iterUser.updateLastModified();
            session.save(iterUser);

            //tx.commit();

        } catch(Exception ex) {
            logger.debug("Unable to update user information into database", ex);
            throw new UMException(
                "Unable to update user information into database", ex);
        }
    }

    /**
     * Updates user biography in Database
     * @param id
     * @param image
     * @param bio
     * @throws ServletException
     */
    public static Image getImage(long id) throws UMException {

        Session sess = null;
        Image iterImage = null;
        try {
            sess = org.digijava.kernel.persistence.PersistenceManager.
                getSession();

            iterImage = (Image) sess.load(Image.class, new Long(id));
            ProxyHelper.initializeObject(iterImage);
        } catch(Exception ex0) {
            logger.debug("Unable to get Image from database ", ex0);
            throw new UMException("Unable to get Image from database ", ex0);
        }

        return iterImage;
    }

    /**
     * Update user bio in database
     *
     * @param user object
     * @throws UMException
     */
    public static void updateUserBio(User user) throws
        UMException {

        Session session = null;
        try {
            session = PersistenceManager.getSession();

            user.updateLastModified();

//beginTransaction();
            session.update(user);
            //tx.commit();
        } catch(Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            throw new UMException(
                "Unable to update user information into database", ex);
        }
        try {
            UserUtils.saveUserPreferences(user.getUserPreference());
            UserUtils.saveUserLangPreferences(user.getUserLangPreferences());
        } catch(DgException ex1) {
            throw new UMException(ex1);
        }
    }

    /**
     * Update user bio in database
     *
     * @param user object
     * @throws UMException
     */
    public static void updateUserMarket(User user) throws
        UMException {

        Session session = null;
        try {
            session = PersistenceManager.getSession();

            user.updateLastModified();

//beginTransaction();
            session.update(user);
            //tx.commit();
        } catch(Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            throw new UMException(
                "Unable to update user information into database", ex);
        } 
        try {
            UserUtils.saveUserPreferences(user.getUserPreference());
            UserUtils.saveUserLangPreferences(user.getUserLangPreferences());
        } catch(DgException ex1) {
            throw new UMException(ex1);
        }
    }

    /**
     * Update user in database
     *
     * @param user object
     * @throws UMException
     */
    public static void updateUser(User user) throws
        UMException {

        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            ArrayList removeArray = new ArrayList();

            if(user.getInterests() != null) {
                Iterator iter = user.getInterests().iterator();
                while(iter.hasNext()) {
                    Interests item = (Interests) iter.next();

                    List list = getGeoupsBySiteId(item.getSite().getId());
                    Iterator iterGroups = list.iterator();
                    while(iterGroups.hasNext()) {
                        Group group = (Group) iterGroups.next();
                        if(group.isMemberGroup()) {
                            if(item.getUser() != null) {
                                org.digijava.module.admin.util.DbUtil.
                                    addUsersToGroup(
                                        group.getId(), new Long[] {user.getId()});
                            } else {
                                org.digijava.module.admin.util.DbUtil.
                                    removeUserFromGroup(
                                        group.getId(), user.getId());
                            }
                        }
                    }

                    if(item.getUser() == null) {
                        session.delete(item);
                        removeArray.add(item);
                    }
                }
            }

            if(removeArray.size() > 0)
                user.getInterests().removeAll(removeArray);

            user.updateLastModified();

            session.update(user);
            
            if(user.getUserPreference()!=null){
                UserUtils.saveUserPreferences(user.getUserPreference());
            }
            if(user.getUserLangPreferences()!=null){
                UserUtils.saveUserLangPreferences(user.getUserLangPreferences());
            }
        } catch(Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            throw new UMException(
                "Unable to update user information into database", ex);
        }
    }
    
    public static void registerUser(User user) throws UMException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
//beginTransaction();

            // set encrypted password
            user.setPassword(ShaCrypt.crypt(user.getPassword().trim()).trim());

            // set hashed password
            user.setSalt(new Long(user.getPassword().trim().hashCode()).
                         toString());

            // update user
            session.save(user);

            // update user preference
            if(user.getUserPreference() != null) {
                session.save(user.getUserPreference());
            }

            // update user language preferences
            if(user.getUserLangPreferences() != null) {
                session.save(user.getUserLangPreferences());
            }

            session.getTransaction().commit();

            // Is becoming a member of Member group of corresponding site
            if(user.getInterests() != null) {
                Iterator iter = user.getInterests().iterator();
                while(iter.hasNext()) {
                    Interests item = (Interests) iter.next();

                    List list = getGeoupsBySiteId(item.getSite().getId());
                    Iterator iterGroups = list.iterator();
                    while(iterGroups.hasNext()) {
                        Group group = (Group) iterGroups.next();
                        if(group.isMemberGroup()) {
                            if(item.getUser() != null) {
                                org.digijava.module.admin.util.DbUtil.
                                    addUsersToGroup(
                                        group.getId(), new Long[] {user.getId()});
                            }
                        }
                    }
                }
            }
            // ------------------------------------------------------------

        } catch(Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            if(tx != null) {
                try {
                    tx.rollback();
                } catch(HibernateException ex1) {
                    logger.warn("rollback() failed ", ex1);
                }
            }
            throw new UMException(
                "Unable to update user information into database", ex);
        }

    }
    
    public static boolean registerUser(String id) throws UMException {
        Session session = null;
        boolean verified = false;
        Transaction tx = null;
        BigInteger iduser;
        try {
            session = PersistenceManager.getSession();
            //"from " + User.class.getName() + " rs where sha1(concat(cast(rs.email as byte),rs.id))=:hash and rs.emailVerified=false"; 
            String queryString = "select ID from DG_USER where sha1((cast(EMAIL as bytea) || cast(cast(ID as text)as bytea)))=:hash and EMAIL_VERIFIED=false";
            Query query = session.createNativeQuery(queryString);
            query.setString("hash", id);
            iduser = (BigInteger) query.uniqueResult();
            if (iduser!= null){
                User user = (User) session.load(User.class, iduser.longValue());
                user.setBanned(false);
                user.setEmailVerified(true);
                user.updateLastModified();
                session.update(user);
                verified = true;
            }else{
                verified = false;
            }
        } catch (Exception ex0) {
            logger.debug("isRegisteredEmail() failed", ex0);
            throw new UMException(ex0.getMessage(), ex0);
        }

        return verified ;
    }

    public static boolean isRegisteredEmail(String email) throws
        UMException {
        Session sess = null;
        boolean iscorrect = false;
        try {
            sess = PersistenceManager.getSession();

            String queryString = "from " + User.class.getName() + " rs where trim(lower(rs.email)) = :email";
            Query query = sess.createQuery(queryString);
            query.setString("email", email.toLowerCase().trim());
            
            Iterator iter = query.list().iterator();
            if(iter.hasNext()) {
                iscorrect = true;
            }
        } catch(Exception ex0) {
            logger.debug("isRegisteredEmail() failed", ex0);
            throw new UMException(ex0.getMessage(), ex0);
        }
        return iscorrect;
    }
    
    public static boolean EmailExist(String email, Long id) throws
    UMException {
    Session sess = null;
    boolean iscorrect = false;
    try {
        sess = PersistenceManager.getSession();

        String queryString = "from " + User.class.getName() + " rs where trim(lower(rs.email)) = :email";
        Query query = sess.createQuery(queryString);
        query.setString("email", email.toLowerCase().trim());
        
        Iterator iter = query.list().iterator();
        if(!iter.hasNext()) {
            iscorrect = true;
        }
        for (Iterator iterator = query.list().iterator(); iterator.hasNext();) {
            User user = (User) iterator.next();
            if (user.getId().compareTo(id)==0){
                iscorrect = true;
            }else{
                iscorrect =false;
                break;
            }
        }
        
    } catch(Exception ex0) {
        logger.debug("isRegisteredEmail() failed", ex0);
        throw new UMException(ex0.getMessage(), ex0);
    }

    return iscorrect;
}
    
    public static void saveResetPassword(long userId, String code) throws
        UMException {
        Session session = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.getSession();
//beginTransaction();

            ResetPassword resetPassword;
            boolean create = true;
            try {
                resetPassword = (ResetPassword) session.load(ResetPassword.class, new Long(userId));
                create = false;
            } catch(ObjectNotFoundException ex2) {
                resetPassword = new ResetPassword();
                resetPassword.setUserId(userId);
            }
            resetPassword.setCode(code);
            resetPassword.setResetDate(new Date());

            if(create) {
                session.save(resetPassword);
            } else {
                session.update(resetPassword);
            }

            //tx.commit();
        } catch(Exception ex) {
            logger.debug("Unable to put reset password record into database", ex);
            throw new UMException(
                "Unable to put reset password record into database", ex);
        }
    }

    /**
     * Get Countrie list from DB
     *
     * @return
     * @throws UMException
     */
    public static List getCountries() throws UMException {
        return getList(Country.class.getName(), "countryName");
    }

    /**
     * Get Organization types from DB
     *
     * @return
     * @throws UMException
     */
    public static List getOrganizationTypes() throws UMException {
        return getList(OrganizationType.class.getName(), "type");
    }

    /**
     * Get Content Alerts from DB
     *
     * @return
     * @throws UMException
     */
    public static List getContentAlerts() throws UMException {
        return getList(ContentAlert.class.getName(), null);
    }

    /**
     * Get How Did you hear list from DB
     *
     * @return
     * @throws UMException
     */
    public static List getHowDidYouHear() throws UMException {
        return getList(HowDidYouHear.class.getName(), null);
    }

    public static List getLanguages() throws UMException {
        return getList(Locale.class.getName(), null);
    }

    /**
     *
     *
     * @param className
     * @param order
     * @return
     * @throws UMException
     */
    public static List getList(String className, String order) throws
        UMException {
        Session session = null;
        List list = null;
        String find = null;
        try {
            session = PersistenceManager.getSession();

            if(order != null && order.trim().length() > 0) {
                find = new String("from " + className +
                                  " rs order by rs." + order);
            } else {
                find = new String("from " + className);
            }

            Query query = session.createQuery(find);
            list = query.list();
        } catch(Exception ex) {
            logger.debug("Unable to get data from " + className, ex);
            throw new UMException("Unable to get data from " + className, ex);
        }

        return list;
    }

    /**
     * Get user object by  id
     *
     * @param activeUserId
     * @return
     * @throws UMException
     */
    public static User getSelectedUser(Long activeUserId,
                                       HttpServletRequest request) throws
        UMException {
        User result;
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            result = (User) session.load(User.class, activeUserId);
            ProxyHelper.initializeObject(result);

            Site site = RequestUtils.getSite(request);
            UserPreferences userPreferences = UserUtils.getUserPreferences(result,
                site);
            if(userPreferences == null) {
                userPreferences = new UserPreferences(result, site);
            }
            result.setUserPreference(userPreferences);

        } catch(Exception ex) {
            logger.debug("Unable to get User from database ", ex);
            throw new UMException("Unable to get User from database", ex);
        }

        return result;

    }

    public static List searchUsers(String criteria) throws UMException {

        Session session = null;
        List userList;

        try {
            userList = UserUtils.searchUsers(criteria, new String[] {"firstNames", "lastName", "email"});
        } catch(Exception ex) {
            logger.debug("Unable to get username list from database ", ex);
            throw new UMException(
                "Unable to get username list from database", ex);
        }

        return userList;
    }

    /**
     *
     * @param site Site
     * @param user User
     * @return Interests
     * @throws UMException
     */
    public static Interests getInterestBySite(Site site, User user) throws UMException {
        List interests = null;
        Session session = null;
        try {
            if(site != null) {
                session = PersistenceManager.getSession();
                Query query = session.createQuery("select s from " + Interests.class.getName() +
                                                  " s where (s.site.id = ? and s.user.id = ?)");
                query.setParameter(0, site.getId());
                query.setParameter(1, user.getId());

                interests = query.list();
                if(interests != null) {
                    Iterator iter = interests.iterator();
                    while(iter.hasNext()) {
                        Interests item = (Interests) iter.next();
                        return item;
                    }
                }
            }
        } catch(Exception ex) {
            logger.debug("Unable to get Topics Sites", ex);
            throw new UMException("Unable to get Topics Sites", ex);
        }
        return null;
    }

    /**
     *
     * @param site
     * @return
     * @throws UMException
     */
    public static List getTopicsSites(Site site) throws UMException {
        List sites = null;
        Session session = null;
        try {
            if(site != null) {
                session = PersistenceManager.getSession();
                Query query = session.createQuery("select s from " + Site.class.getName() +
                                                  " s where (s.parentId = ?) order by s.priority");
                query.setParameter(0, site.getId());
                query.setCacheable(true);

                sites = query.list();
            }
        } catch(Exception ex) {
            logger.debug("Unable to get Topics Sites", ex);
            throw new UMException("Unable to get Topics Sites", ex);
        }
        return sites;
    }

    /**
     *
     * @param siteId
     * @return
     * @throws UMException
     */
    public static List getGeoupsBySiteId(Long siteId) throws UMException {
        List groups = null;
        Session session = null;
        try {

            String queryString = "from " + Group.class.getName() +
                                 " g where g.site.id = :siteId";
            Query query = session.createQuery(queryString);
            query.setLong("siteId", siteId);
            
            groups = query.list();
        } catch(Exception ex) {
            logger.debug("Unable to get Group list from database ", ex);
            throw new UMException("Unable to get Group list from database ", ex);
        }
        return groups;
    }

    /**
     * @author akashs
     * retrieves all organisation groups
     * @return col, collection of retrieved organisation groups
     */
    public static Collection getAllOrgGroup() {
        Session session = null;
        Collection col = new ArrayList();
        try {
            session = PersistenceManager.getSession();
            String q = "select grp from " + AmpOrgGroup.class.getName() + " grp";
            col = session.createQuery(q).list();
        } catch(Exception ex) {
            logger.error("Unable to get Org Group" + ex);
        }
        return col;
    }

    /**
     * @author akashs
     * @param Id id of organisation group
     * retrieves all organisations under organisation group with this id
     * @return col collection of retrieved organisations
     */
    public static Collection getOrgByGroup(Long Id) {

        Session sess = null;
        Collection col = new ArrayList();
        Query qry = null;

        try {
            sess = PersistenceManager.getSession();
            String queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (o.orgGrpId=:orgGrpId) and (o.deleted is null or o.deleted = false)  order by o.name asc";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgGrpId", Id, LongType.INSTANCE);
            col = qry.list();
        } catch(Exception e) {
            logger.debug("Exception from getOrgByGroup()");
            logger.debug(e.toString());
        }
        return col;
    }

    /**
     * @author akashs
     * retrieves all organisation types
     * @return col, collection of retrieved organisation types
     */
    public static Collection getAllOrgTypes() {
        Session session = null;
        Collection col = new ArrayList();
        try {
            session = PersistenceManager.getSession();
            String q = "select type from " + AmpOrgType.class.getName() + " type order by type asc";
            col = session.createQuery(q).list();
        } catch(Exception ex) {
            logger.error("Unable to get Org Types" + ex);
        }
        return col;
    }

    /**
     * @author akashs
     * @param Id id of organisation type
     * retrieves all organisation groups of this type with id as their PK
     * @return col collection of retrieved organisation groups
     */
    public static Collection getOrgGroupByType(Long Id) {

        Session sess = null;
        Collection col = new ArrayList();
        Query qry = null;

        try {
            sess = PersistenceManager.getSession();
            String queryString = "select o from " + AmpOrgGroup.class.getName()
                + " o where (o.orgType=:orgTypeId) order by o.orgGrpName asc";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgTypeId", Id, LongType.INSTANCE);
            col = qry.list();
        } catch(Exception e) {
            logger.debug("Exception from getOrgGroupByType()");
            logger.debug(e.toString());
        }
        return col;
    }

    /**
     * @author akashs
     * @param Id id of organisation type
     * retrieves all organisations of this type with id as their PK
     * @return col collection of retrieved organisations
     */
    public static Collection getOrgByType(Long Id) {

        Session sess = null;
        Collection col = new ArrayList();
        Query qry = null;

        try {
            sess = PersistenceManager.getSession();
            String queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (o.orgType=:orgTypeId) and (o.deleted is null or o.deleted = false) ";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgTypeId", Id, LongType.INSTANCE);
            col = qry.list();
        } catch(Exception e) {
            logger.debug("Exception from getOrgByType()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static List<SuspendLogin> getSuspendedLoginObjs() {
        StringBuilder qs = new StringBuilder("From ").append(SuspendLogin.class.getName());
        return PersistenceManager.getSession().createQuery(qs.toString()).list();
    }

    public static void saveSuspendedLoginObj(SuspendLogin sl) {
        if (sl.getActive() == null) sl.setActive(false);
        if (sl.getExpires() == null) sl.setExpires(false);

        Session sess = PersistenceManager.getSession();
        sess.saveOrUpdate(sl);
    }

    public static void deleteSuspendedLoginObj(SuspendLogin sl) {
        PersistenceManager.getSession().delete(sl);
    }

    public static SuspendLogin getSuspendedLoginObjById(Long id) {
        return (SuspendLogin) PersistenceManager.getSession().get(SuspendLogin.class, id);
    }

    public static SuspendLogin getSuspendedLoginObjByName(String name) {
        StringBuilder qs = new StringBuilder("From ").
                append(SuspendLogin.class.getName()).
                append(" sl where sl.name = :NAME");
        return (SuspendLogin) PersistenceManager.getSession().createQuery(qs.toString()).setString("NAME", name).uniqueResult();
    }

    public static List<User> getAllUsers() {
        StringBuilder qs = new StringBuilder("From ").
            append(User.class.getName()).
            append(" u order by u.firstNames");
        return PersistenceManager.getSession().createQuery(qs.toString()).list();
    }

    public static List<SuspendLogin> getUserSuspendReasonsFromDB (User user) {
        StringBuilder qs = new StringBuilder("From ")
            .append(SuspendLogin.class.getName())
            .append(" sl where :USER_ID in elements(sl.users)")
            .append(" and sl.active = true and (sl.expires=false or")
            .append(" (sl.expires=true and sl.suspendTil > current_date()))");
        return PersistenceManager.getSession()
                .createQuery(qs.toString())
                .setLong("USER_ID", user.getId())
                .setCacheable(true)
                .list();
    }
}
