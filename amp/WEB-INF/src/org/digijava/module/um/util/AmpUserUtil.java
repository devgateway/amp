package org.digijava.module.um.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferencesPK;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.SiteCache;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.dbentity.AmpUserExtensionPK;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.hibernate.criterion.Restrictions;

/**
 * Methods for working with User related tasks.
 * TODO Let's move some user methods from DbUtil to this class, because DbUtil gets very huge.
 * @author Irakli Kobiashvili
 *
 */
public class AmpUserUtil {
    private static Logger logger = Logger.getLogger(AmpUserUtil.class);

    public static Collection<User> getAllUsers(boolean getBanned) {
        Session session = null;
        Query qry = null;
        Collection<User> users = new ArrayList<User>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select u from " + User.class.getName() + " u"
                    + " where u.banned=:banned and u.globalAdmin=false order by u.email";
            qry = session.createQuery(queryString);
            qry.setBoolean("banned", getBanned);
            users = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get user");
            logger.error("Exception " + e);
            e.printStackTrace();
        }
        return users;
    }
    /**
     * Returns Collection of users who have not activated their accounts
     * or were not activated by admin (i.e. admin has not removed ban status )
     * @return Collection of users
     */

    public static Collection<User> getAllNotVerifiedUsers() {
        Session session = null;
        Query qry = null;
        Collection<User> users = new ArrayList<User>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select u from " + User.class.getName() + " u"
                    + " where u.emailVerified=:emailVerified and u.banned=:banned order by u.email";
            qry = session.createQuery(queryString);
            qry.setBoolean("emailVerified", false);
            qry.setBoolean("banned", true);
            users = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get user");
            logger.error("Exception " + e);
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Delete user who has not activated his/her accounts
     *
     * @param userId the id of the user
     */
    public static void deleteUser(Long userId) {
        Session session = null;
        Transaction tx=null;

        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            User user=(User)session.load(User.class,userId);
            AmpUserExtension userExt=AmpUserUtil.getAmpUserExtension(user);
            if(userExt!=null){
                 /*
                 * During registration phase user
                 * specify organization type, group and organization
                 * So we should delete this information before deleting user
                 */

                session.delete(userExt);
            }
            Site rootSite = SiteCache.lookupByName("amp");
            UserPreferencesPK key = new UserPreferencesPK(user, rootSite);
            UserLangPreferences userLang=(UserLangPreferences)session.load(UserLangPreferences.class,key);
            /*
             * During registration phase user
             * specifies user navigation language
             * So we should delete this information before deleting user
             */

            session.delete(userLang);

            // deleting user
            session.delete(user);
            //tx.commit();
        } catch (Exception e) {
            if(tx!=null){
                try{
                    tx.rollback();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    public static AmpUserExtension getAmpUserExtension(Long userId)
            throws AimException {
        User user = UserUtils.getUser(userId);
        return getAmpUserExtension(user);
    }
    /**
     * Retrieves user extension .
     * @param user 
     * @return db entity
     * @throws AimException
     */

    public static AmpUserExtension getAmpUserExtension(User user)
            throws AimException {
         AmpUserExtension result = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select ext from " + AmpUserExtension.class.getName() + " ext"
                    + " where ext is not null and ext=:ext";
            qry = session.createQuery(queryString);
            qry.setLong("ext", user.getId());
            result =(AmpUserExtension) qry.uniqueResult();

        }
         catch (Exception e) {
            throw new AimException("Cannot retrive AmpUserExtension", e);
        }
        return result;
        
    }

    /**
     * Bulk version of user extensions retrieval.
     */
    public static Map<Long, AmpUserExtension> getAmpUserExtensions(List<User> users) {
        if (users.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> usersIds = users.stream().map(User::getId).collect(Collectors.toList());
        Session session = PersistenceManager.getRequestDBSession();
        List<AmpUserExtension> extensions = session
                .createCriteria(AmpUserExtension.class)
                .add(Restrictions.in("ampUserExtId.user.id", usersIds))
                .list();

        Map<Long, AmpUserExtension> result = new HashMap<>();
        extensions.forEach(e -> result.put(e.getAmpUserExtId().getUser().getId(), e));
        return result;
    }
    
    /**
     * Retrieves user extension .
     * @param org an AmpOrganisation that the user belongs to 
     * @return db entity
     * @throws AimException
     */

    public static Collection<AmpUserExtension> getAmpUserExtensionByOrgId(Long orgId)
            throws AimException {
         Collection<AmpUserExtension> result = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select ext from " + AmpUserExtension.class.getName() + " ext"
                    + " where ext.organization=:orgId";
            qry = session.createQuery(queryString);
            qry.setLong("orgId", orgId);
            result = qry.list();

        }
         catch (Exception e) {
            throw new AimException("Cannot retrive AmpUserExtension", e);
        }
        return result;
        
    }

    /**
     * Retrieves user extension with pk class.
     * @param key primary key for user extension
     * @return db entity
     * @throws AimException
     */
    @Deprecated
    public static AmpUserExtension getAmpUserExtension(AmpUserExtensionPK key)
            throws AimException {
        AmpUserExtension result = null;
        try {
            Session session = PersistenceManager.getRequestDBSession();
            result = (AmpUserExtension) session.load(AmpUserExtension.class,
                    key);
        } catch (ObjectNotFoundException notFounExetion){
            logger.debug("User Extension not found for user"+key.getUser().getEmail());
            return null;
        } catch (Exception e) {
            throw new AimException("Cannot retrive AmpUserExtension", e);
        }
        return result;
    }

    /**
     * Save user extensions.
     * @param userExtension db entity to save, with correct id class.
     * @throws AimException
     */
    public static void saveAmpUserExtension(AmpUserExtension userExtension)
            throws AimException {
        Transaction tx=null;
        try {
            Session session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.save(userExtension);
            //tx.commit();
        } catch (Exception e) {
            if (tx!=null){
                try {
                    tx.rollback();
                } catch (Exception e1) {
                    throw new AimException("Cannot rollback userExtension save operation",e1);
                }
            }
            throw new AimException("Cannot save user extension",e);
        }
    }
    public static Collection<User> getAllUsersNotBelongingToTeam(Long teamId,String keyword) throws Exception{
        Collection<User> retVal=null;
        Session session=null;
        String queryString=null;
        Query qry=null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString="select u from " +User.class.getName() +" u where u.banned=:banned and u.id not in (select tm.user.id from "+AmpTeamMember.class.getName()+
            " tm where (tm.deleted is null or tm.deleted = false) and tm.ampTeam.ampTeamId=:teamId ) ";
                        if(keyword!=null&&keyword.length()>0){
                            queryString+=" and concat(u.firstNames,' ',u.lastName)=:keyword";
                        }
                        queryString+=" order by u.email";
            qry=session.createQuery(queryString);
            qry.setLong("teamId", teamId);
                        if(keyword!=null&&keyword.length()>0){
                            qry.setString("keyword", keyword);
                        }
            qry.setBoolean("banned", false);            
            retVal=qry.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AimException("Cannot get users", e);
        }
        return retVal;
    }

    public static List<String> searchUsesers(String searchStr, Long teamId) throws Exception {
        Session session = null;
        String queryString = null;
        Query query = null;
        List<String> users = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select distinct concat(u.firstNames,' ',u.lastName) from " + User.class.getName()
                    + " u where  lower(concat(u.firstNames,' ',u.lastName)) like lower(:searchStr) and u.id not in "
                    + " (select tm.user.id from " + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and tm.ampTeam.ampTeamId=:teamId) "
                    + " and u.banned=:banned order by concat(u.firstNames,' ',u.lastName)";
            query = session.createQuery(queryString);
            query.setString("searchStr", searchStr + "%");
            query.setLong("teamId", teamId);
            query.setBoolean("banned", false);
            users = query.list();
        } catch (Exception ex) {
            logger.error("couldn't load user " + ex.getMessage(), ex);
        }


        return users;
    }

       public static List<String> getUserReminder(Date lastActivity){
        Session session = null;
        Query qry = null;
        List<String> usersEmailAddr = new ArrayList<String>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = " select al.authorEmail from "+ AmpAuditLogger.class.getName() +" al where al.action='login' and al.loggedDate>:startDate" +
                    " and al.authorEmail not in (Select aal.authorEmail from "+ AmpAuditLogger.class.getName() +" aal " +
                    "where aal.action='login' and aal.loggedDate>:compareDate group by aal.authorEmail) group by al.authorEmail";
            /*String queryString = "select u from " + User.class.getName() + " u"
                    + " where u.email not in(select al.authorEmail from " 
                    + AmpAuditLogger.class.getName() +" al where al.action='login' and al.loggedDate>:date"
                    +" group by al.authorEmail)";*/
            qry = session.createQuery(queryString);
            Date startDate = new Date(112, 2, 1);//March,2013
            qry.setDate("startDate", startDate);
            qry.setDate("compareDate", lastActivity);
            usersEmailAddr = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get list of emails");
            logger.error("Exception " + e);
            e.printStackTrace();
        }
        return usersEmailAddr;
       }

}
