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
import org.digijava.kernel.entity.trubudget.SubIntents;
import org.digijava.kernel.entity.trubudget.TruBudgetIntent;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.*;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.um.dbentity.ResetPassword;
import org.digijava.module.um.dbentity.SuspendLogin;
import org.digijava.module.um.exception.UMException;
import org.digijava.module.um.model.*;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.net.URISyntaxException;
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
            query.setParameter("email", email, StringType.INSTANCE);
            
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
            query.setParameter("email", user,StringType.INSTANCE);
            
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
            query.setParameter("email", email,StringType.INSTANCE);
            
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
    public static void updatePassword(String user, String newPassword) throws UMException{
        updatePassword(user, null, newPassword);
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
            session = PersistenceManager.getSession();

            User userToUpdate = UserUtils.getUserByEmailAddress(user);
            userToUpdate.setPassword(ShaCrypt.crypt(newPassword.trim()).trim());
            userToUpdate.setSalt(new Long(newPassword.trim().hashCode()).toString());
            userToUpdate.updateLastModified();
            session.saveOrUpdate(userToUpdate);
            session.getTransaction().commit();

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
                for (Object o : user.getInterests()) {
                    Interests item = (Interests) o;

                    List list = getGeoupsBySiteId(item.getSite().getId());
                    for (Object value : list) {
                        Group group = (Group) value;
                        if (group.isMemberGroup()) {
                            if (item.getUser() != null) {
                                org.digijava.module.admin.util.DbUtil.
                                        addUsersToGroup(
                                                group.getId(), new Long[]{user.getId()});
                            } else {
                                org.digijava.module.admin.util.DbUtil.
                                        removeUserFromGroup(
                                                group.getId(), user.getId());
                            }
                        }
                    }

                    if (item.getUser() == null) {
                        session.delete(item);
                        removeArray.add(item);
                    }
                }
            }

            if(removeArray.size() > 0)
                user.getInterests().removeAll(removeArray);

            user.updateLastModified();

            session.update(user);

            session.flush();
            List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");

            if (getSettingValue(settings,"isEnabled").equalsIgnoreCase("true") && Boolean.TRUE.equals(user.getTruBudgetEnabled())) {
                try {
                    TruUserData userData = new TruUserData();
                    TruUserData.Data data = new TruUserData.Data();
                    TruUserData.User user1 = new TruUserData.User();
                    user1.setDisplayName(user.getFirstNames() + " " + user.getLastName());
                    // TODO: 8/28/23 if you are editing a user for the first time with Trubudget integrated. their password will be the email
                    // TODO: 8/28/23 this can be changed later in Trubudget
                    String pass = user.getTruBudgetPassword()!=null?UmUtil.decryptTruBudgetPassword(user.getTruBudgetPassword(), user.getEmail(), user.getTruBudgetKeyGen()):"amptrubudget";
                    user1.setPassword(pass);
                    user1.setId(resolveTruBudgetUserName(user.getTruBudgetUserName(), user.getEmail()));
                    data.setUser(user1);
                    userData.setData(data);
                    if (registerUserOnTrubudget(userData, user)) {
                        user.setTruBudgetEnabled(true);
                        session.update(user);
                        session.flush();
                    }
                } catch (Exception e) {
                    logger.info("Error: " + e.getMessage(), e);
                    tx.rollback();

                }
            }
            
            if(user.getUserPreference()!=null){
                UserUtils.saveUserPreferences(user.getUserPreference());
            }
            if(user.getUserLangPreferences()!=null){
                UserUtils.saveUserLangPreferences(user.getUserLangPreferences());
            }
            session.flush();
        } catch(Exception ex) {
            logger.debug("Unable to update user information into database", ex);

            throw new UMException(
                "Unable to update user information into database", ex);
        }
    }

    // TODO: 9/1/23 rollback if trubudget registration not successful
    public static Mono<TruLoginResponse> loginToTruBudget(TruLoginRequest truLoginRequest, List<AmpGlobalSettings> settings) throws URISyntaxException {
        return GenericWebClient.postForSingleObjResponse(getSettingValue(settings,"baseUrl")+"api/user.authenticate",truLoginRequest, TruLoginRequest.class,TruLoginResponse.class);
    }
    
    public static Mono<TruLoginResponse> refreshTruBudgetToken(String userId, String refreshToken, List<AmpGlobalSettings> settings) throws URISyntaxException {
        TruRefreshTokenRequest refreshRequest = new TruRefreshTokenRequest();
        refreshRequest.setApiVersion(getSettingValue(settings, "apiVersion"));
        TruRefreshTokenRequest.Data data = new TruRefreshTokenRequest.Data();
        TruRefreshTokenRequest.User user = new TruRefreshTokenRequest.User();
        user.setId(userId);
        user.setRefreshToken(refreshToken);
        data.setUser(user);
        refreshRequest.setData(data);
        return GenericWebClient.postForSingleObjResponse(getSettingValue(settings,"baseUrl")+"api/user.refreshToken", refreshRequest, TruRefreshTokenRequest.class, TruLoginResponse.class);
    }
    public static List<AmpGlobalSettings> getGlobalSettingsBySection(String sectionName)
    {
        Session session = PersistenceManager.getRequestDBSession();
        Query<AmpGlobalSettings> query = session.createQuery("FROM "+AmpGlobalSettings.class.getName()+" gs WHERE gs.section= :sectionName", AmpGlobalSettings.class);
        query.setCacheable(true);
        query.setParameter("sectionName", sectionName);

        return query.list();
    }
    public static String getSettingValue(List<AmpGlobalSettings> globalSettings, String settingName)
    {
        String value = globalSettings.stream().filter(x->x.getGlobalSettingsName().equals(settingName)).findFirst().orElseThrow(()->new RuntimeException("Unable to find setting for name: "+settingName)).getGlobalSettingsValue();
        logger.info("Setting value: "+value);
        return value;
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

            session.flush();
            List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");

            if (getSettingValue(settings,"isEnabled").equalsIgnoreCase("true") && Boolean.TRUE.equals(user.getTruBudgetEnabled())) {

                try {
                    TruUserData userData = new TruUserData();
                    TruUserData.Data data = new TruUserData.Data();
                    TruUserData.User user1 = new TruUserData.User();
                    user1.setDisplayName(user.getFirstNames() + " " + user.getLastName());
                    user1.setPassword(user.getTruBudgetPassword()!=null?UmUtil.decrypt(user.getTruBudgetPassword(), user.getTruBudgetKeyGen()):"amptrubudget");
                    user1.setId(resolveTruBudgetUserName(user.getTruBudgetUserName(), user.getEmail()));
                    data.setUser(user1);
                    userData.setData(data);
                    if (registerUserOnTrubudget(userData, user)) {
                        user.setTruBudgetEnabled(true);
                        session.update(user);
                        session.flush();
                    }

                } catch (Exception e) {
                    logger.info("Error: " + e.getMessage(), e);
                    tx.rollback();
                }
            }

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
                for (Object o : user.getInterests()) {
                    Interests item = (Interests) o;

                    List list = getGeoupsBySiteId(item.getSite().getId());
                    for (Object value : list) {
                        Group group = (Group) value;
                        if (group.isMemberGroup()) {
                            if (item.getUser() != null) {
                                org.digijava.module.admin.util.DbUtil.
                                        addUsersToGroup(
                                                group.getId(), new Long[]{user.getId()});
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
            String queryString = "select ID from DG_USER where encode(digest(CAST(EMAIL as text) || CAST(id as text), 'sha1'), 'hex')= :hash and EMAIL_VERIFIED=false";
            Query query = session.createNativeQuery(queryString);
            query.setParameter("hash", id,StringType.INSTANCE);
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
            query.setParameter("email", email.toLowerCase().trim(),StringType.INSTANCE);
            
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

    public static String getDefaultTruBudgetUserName(String email) {
        if (email == null) {
            return null;
        }
        String trimmedEmail = email.trim();
        if (trimmedEmail.isEmpty() || !trimmedEmail.contains("@")) {
            return trimmedEmail.isEmpty() ? null : trimmedEmail;
        }
        return trimmedEmail.substring(0, trimmedEmail.indexOf('@')).trim();
    }

    public static String resolveTruBudgetUserName(String truBudgetUserName, String email) {
        if (truBudgetUserName != null && !truBudgetUserName.trim().isEmpty()) {
            return truBudgetUserName.trim();
        }
        return getDefaultTruBudgetUserName(email);
    }

    public static boolean isTruBudgetUserNameAvailable(String truBudgetUserName, Long currentUserId) throws UMException {
        if (truBudgetUserName == null || truBudgetUserName.trim().isEmpty()) {
            return true;
        }

        Session sess = null;
        try {
            sess = PersistenceManager.getSession();
            String queryString = "from " + User.class.getName() + " rs where trim(lower(rs.truBudgetUserName)) = :userName";
            if (currentUserId != null) {
                queryString += " and rs.id <> :currentUserId";
            }
            Query query = sess.createQuery(queryString);
            query.setParameter("userName", truBudgetUserName.trim().toLowerCase(), StringType.INSTANCE);
            if (currentUserId != null) {
                query.setParameter("currentUserId", currentUserId, LongType.INSTANCE);
            }
            return !query.list().iterator().hasNext();
        } catch (Exception ex0) {
            logger.debug("isTruBudgetUserNameAvailable() failed", ex0);
            throw new UMException(ex0.getMessage(), ex0);
        }
    }

    public static boolean registerUserOnTrubudget(TruUserData userData, User user) throws URISyntaxException {
        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
        if (!getSettingValue(settings,"isEnabled").equals("true")) {
            logger.info("Trubudget is not enabled for this site.");
            return false;
        }
        try {
            userData.setApiVersion(getSettingValue(settings, "apiVersion"));
            userData.getData().getUser().setOrganization(getSettingValue(settings, "organization"));
            logger.info("Registering user on Trubudget");
            logger.info("Settings: " + settings);

            TruLoginRequest truLoginRequest = new TruLoginRequest();
            truLoginRequest.setApiVersion(getSettingValue(settings, "apiVersion"));
            TruLoginRequest.Data data = new TruLoginRequest.Data();
            TruLoginRequest.User rootUser = new TruLoginRequest.User();
            rootUser.setPassword(getSettingValue(settings, "rootPassword"));
            rootUser.setId(getSettingValue(settings, "rootUser"));
            data.setUser(rootUser);
            truLoginRequest.setData(data);

            TruLoginResponse truLoginResponse = loginToTruBudget(truLoginRequest, settings).block();
            if (truLoginResponse == null || truLoginResponse.getData() == null || truLoginResponse.getData().getUser() == null) {
                logger.error("TruBudget login failed: empty response while registering user " + user.getEmail());
                return false;
            }

            String token = truLoginResponse.getData().getUser().getToken();
            String identity = userData.getData().getUser().getId();
            TruUserData registerResponse = null;

            try {
                registerResponse = GenericWebClient.postForSingleObjResponse(
                        getSettingValue(settings, "baseUrl") + "api/global.createUser",
                        userData,
                        TruUserData.class,
                        TruUserData.class,
                        token
                ).block();
                logger.info("Create user response: " + registerResponse);
            } catch (RuntimeException createException) {
                if (isTruBudgetUserAlreadyExists(createException)) {
                    logger.warn("TruBudget user already exists for " + identity + ". Updating password and syncing intents.");
                    updateTruBudgetUserPassword(settings, token, identity, userData.getData().getUser().getPassword());
                } else {
                    throw createException;
                }
            }

            if (registerResponse != null && registerResponse.getData() != null && registerResponse.getData().getUser() != null
                    && registerResponse.getData().getUser().getId() != null) {
                identity = registerResponse.getData().getUser().getId();
            }

            Set<TruBudgetIntent> currentIntents = user.getTruBudgetIntents() != null ? user.getTruBudgetIntents() : Collections.emptySet();
            Set<TruBudgetIntent> initialIntents = user.getInitialTruBudgetIntents() != null ? user.getInitialTruBudgetIntents() : Collections.emptySet();
            List<TruBudgetIntent> toBeRevoked = new ArrayList<>();
            for (TruBudgetIntent truBudgetIntent : initialIntents) {
                if (!currentIntents.contains(truBudgetIntent)) {
                    toBeRevoked.add(truBudgetIntent);
                }
            }

            for (TruBudgetIntent intent : currentIntents) {
                try {
                    grantPermRequest(settings, token, identity, intent.getTruBudgetIntentName());
                } catch (Exception e) {
                    logger.error("Failed to grant TruBudget intent " + intent.getTruBudgetIntentName() + " for user " + identity, e);
                }
            }

            for (TruBudgetIntent intent : toBeRevoked) {
                try {
                    revokePermRequest(settings, token, identity, intent.getTruBudgetIntentName());
                } catch (Exception e) {
                    logger.error("Failed to revoke TruBudget intent " + intent.getTruBudgetIntentName() + " for user " + identity, e);
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("Error during trubudget registration", e);
            return false;
        }
    }

    private static void grantPermRequest(List<AmpGlobalSettings> settings, String token, String identity, String intent) throws URISyntaxException {
        TruGrantPermissionRequest permData = new TruGrantPermissionRequest();
        TruGrantPermissionRequest.Data data = new TruGrantPermissionRequest.Data();
        data.setIdentity(identity);
        data.setIntent(intent);
        permData.setData(data);
        permData.setApiVersion(getSettingValue(settings, "apiVersion"));

        String response = GenericWebClient.postForSingleObjResponse(
                getSettingValue(settings, "baseUrl") + "api/global.grantPermission",
                permData,
                TruGrantPermissionRequest.class,
                String.class,
                token
        ).block();
        logger.info("Grant permission response: " + response);
    }

    private static void revokePermRequest(List<AmpGlobalSettings> settings, String token, String identity, String intent) throws URISyntaxException {
        TruRevokePermissionRequest permData = new TruRevokePermissionRequest();
        TruRevokePermissionRequest.Data data = new TruRevokePermissionRequest.Data();
        data.setIdentity(identity);
        data.setUserId(identity);
        data.setIntent(intent);
        permData.setData(data);
        permData.setApiVersion(getSettingValue(settings, "apiVersion"));

        String response = GenericWebClient.postForSingleObjResponse(
                getSettingValue(settings, "baseUrl") + "api/user.intent.revokePermission",
                permData,
                TruRevokePermissionRequest.class,
                String.class,
                token
        ).block();
        logger.info("Revoke permission response: " + response);
    }

    private static void updateTruBudgetUserPassword(List<AmpGlobalSettings> settings, String token, String userId, String newPassword) {
        String baseUrl = getSettingValue(settings, "baseUrl");
        Map<String, Object> request = new HashMap<>();
        request.put("apiVersion", getSettingValue(settings, "apiVersion"));
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("newPassword", newPassword);
        request.put("data", data);

        String[] passwordEndpoints = {"api/user.resetPassword", "api/user.changePassword"};
        RuntimeException lastException = null;
        for (String endpoint : passwordEndpoints) {
            try {
                logger.info("Trying TruBudget password endpoint " + endpoint + " for user " + userId);
                Object response = GenericWebClient.postForSingleObjResponse(
                        baseUrl + endpoint,
                        request,
                        Map.class,
                        Object.class,
                        token
                ).block();
                logger.info("Updated TruBudget password for user " + userId + " via " + endpoint + ". Response: " + response);
                return;
            } catch (RuntimeException ex) {
                lastException = ex;
                logger.warn("TruBudget password endpoint " + endpoint + " failed for user " + userId + ": " + ex.getMessage());
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (lastException != null) {
            throw new RuntimeException("Could not update TruBudget password for user " + userId, lastException);
        }
    }

    private static boolean isTruBudgetUserAlreadyExists(Throwable error) {
        Throwable current = error;
        while (current != null) {
            String message = current.getMessage();
            if (message != null && message.contains("\"code\":\"409\"") && message.toLowerCase().contains("already exists")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    public static List<SubIntents> getSubIntentsByMother(String motherName)
    {
        Session session = PersistenceManager.openNewSession();
        try {
            return session.createNativeQuery(" SELECT t. * FROM amp_trubudget_sub_intent t WHERE mother_intent_name='"+motherName+"'", SubIntents.class).setCacheable(true).list();

        }catch (Exception e)
        {
            logger.info("Error during intent fetch ",e);
            throw new RuntimeException(e);
        }
    }
    public static  List<TruBudgetIntent> getTruBudgetIntents()
    {
        Session session = PersistenceManager.getRequestDBSession();
        try {
            return session.createNativeQuery(" SELECT t.* FROM amp_trubudget_intent t ORDER BY trubudget_intent_name", TruBudgetIntent.class).list();

        }catch (Exception e)
        {
            logger.info("Error during intent fetch ",e);
            throw new RuntimeException(e);
        }

    }

    public static  List<TruBudgetIntent> getTruBudgetIntentsByName(String [] names)
    {
        if (names.length!=0) {
            Session session = PersistenceManager.getRequestDBSession();
            StringBuilder convertedNames = new StringBuilder("(");
            for (String name : names) {
                convertedNames.append("'").append(name).append("'").append(",");
            }
            String c = convertedNames.toString().replaceAll(",$", "");
            c += ")";
            logger.info("Intent query: " + c);

            return session.createNativeQuery(" SELECT t. * FROM amp_trubudget_intent t WHERE trubudget_intent_name in " + c, TruBudgetIntent.class).list();

        }
        return Collections.emptyList();


    }
    
    public static boolean EmailExist(String email, Long id) throws
    UMException {
    Session sess = null;
    boolean iscorrect = false;
    try {
        sess = PersistenceManager.getSession();

        String queryString = "from " + User.class.getName() + " rs where trim(lower(rs.email)) = :email";
        Query query = sess.createQuery(queryString);
        query.setParameter("email", email.toLowerCase().trim(),StringType.INSTANCE);
        
        Iterator iter = query.list().iterator();
        if(!iter.hasNext()) {
            iscorrect = true;
        }
        for (Object o : query.list()) {
            User user = (User) o;
            if (user.getId().compareTo(id) == 0) {
                iscorrect = true;
            } else {
                iscorrect = false;
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
                resetPassword = session.load(ResetPassword.class, new Long(userId));
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
                    for (Object interest : interests) {
                        return (Interests) interest;
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
        return (SuspendLogin) PersistenceManager.getSession().createQuery(qs.toString()).setParameter("NAME", name,StringType.INSTANCE).uniqueResult();
    }

    public static List<User> getAllUsers() {
        StringBuilder qs = new StringBuilder("From ").
            append(User.class.getName()).
            append(" u order by u.firstNames");
        return PersistenceManager.getSession().createQuery(qs.toString()).list();
    }

    public static List<SuspendLogin> getUserSuspendReasonsFromDB (User user) {
        String qs = "From " +
                SuspendLogin.class.getName() +
                " sl where :USER_ID in elements(sl.users)" +
                " and sl.active = true and (sl.expires=false or" +
                " (sl.expires=true and sl.suspendTil > current_date()))";
        return PersistenceManager.getRequestDBSession()
                .createQuery(qs)
                .setParameter("USER_ID", user.getId(), LongType.INSTANCE)
                .setCacheable(true)
                .list();
    }
}
