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

package org.digijava.kernel.security;

/* Java imports */
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.user.UserLoginInfo;
import org.digijava.kernel.util.I18NHelper;
import org.digijava.kernel.util.ShaCrypt;
import org.digijava.kernel.util.UnixCrypt;
import org.digijava.kernel.util.UserUtils;

public class DgLoginModule
    implements LoginModule {

    private static Logger logger = I18NHelper.getKernelLogger(DgLoginModule.class);

    // initial state
    private CallbackHandler callbackHandler;
    private Subject subject;
    private Map sharedState;
    private Map options;

    private Long userId;
    private boolean increaseCounter;

    // configurable options
    private boolean debug;

    /**
     * <p>Creates a login module
     */
    public DgLoginModule() {
        userId = null;
        debug = false;
        increaseCounter = true;
    }

    /**
     * Initialize this <code>LoginModule</code>.
     *
     * <p>
     *
     * @param subject the <code>Subject</code> to be authenticated. <p>
     *
     * @param callbackHandler a <code>CallbackHandler</code> for communicating
     *          with the end user (prompting for usernames and
     *          passwords, for example). <p>
     *
     * @param sharedState shared <code>LoginModule</code> state. <p>
     *
     * @param options options specified in the login
     *          <code>Configuration</code> for this particular
     *          <code>LoginModule</code>.
     */
    public void initialize(
        Subject subject,
        CallbackHandler callbackHandler,
        Map sharedState,
        Map options) {

        // save the initial state
        this.callbackHandler = callbackHandler;
        this.subject = subject;
        this.sharedState = sharedState;
        this.options = options;
        this.userId = null;
        this.increaseCounter = true;

        // initialize any configured options
        if (options.containsKey("debug")) {
            debug = "true".equalsIgnoreCase( (String) options.get("debug"));

        }

        if (debug) {
            logger.debug("\t\t[RdbmsLoginModule] initialize");
        }
    }

    /**
     * <p> Verify the password against the relevant JDBC datasource.
     *
     * @return true always, since this <code>LoginModule</code>
     *      should not be ignored.
     *
     * @exception FailedLoginException if the authentication fails. <p>
     *
     * @exception LoginException if this <code>LoginModule</code>
     *      is unable to perform the authentication.
     */
    public boolean login() throws LoginException {

        logger.debug("login");
        if (callbackHandler == null) {
            throw new LoginException(
                "Error: no CallbackHandler available "
                + "to garner authentication information from the user");
        }

        try {
            // Setup default callback handlers.
            Callback[] callbacks = new Callback[] {
                new NameCallback("Username: "),
                new PasswordCallback("Password: ", false)};
            callbackHandler.handle(callbacks);

            String username = ( (NameCallback) callbacks[0]).getName();
            String password = new String( ( (PasswordCallback)
                                           callbacks[1]).
                                         getPassword());

            ( (PasswordCallback) callbacks[1]).clearPassword();

            userId = getUserFromDatabase(username, password);
            callbacks[0] = null;
            callbacks[1] = null;

            if (userId == null) {
                throw new LoginException(
                    "Authentication failed: Password does not match");
            }
            else {
                logger.debug("User#" + userId + " has logged in");
            }

            return (true);
        }
        catch (LoginException ex) {
            userId = null;
            throw ex;
        }
        catch (Exception ex) {
            userId = null;
            LoginException exception = new LoginException(
                "Login failed" + ex.getMessage());
            exception.initCause(ex);
            throw exception;
        }
    }

    /**
     * Abstract method to commit the authentication process (phase 2).
     *
     * <p> This method is called if the LoginContext's
     * overall authentication succeeded
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * succeeded).
     *
     * <p> If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> method), then this method associates a
     * <code>RdbmsPrincipal</code>
     * with the <code>Subject</code> located in the
     * <code>LoginModule</code>.  If this LoginModule's own
     * authentication attempted failed, then this method removes
     * any state that was originally saved.
     *
     * <p>
     *
     * @exception LoginException if the commit fails
     *
     * @return true if this LoginModule's own login and commit
     *      attempts succeeded, or false otherwise.
     */
    public boolean commit() throws LoginException {

        logger.debug("commit");

        if (userId != null) {

            if (subject.isReadOnly()) {
                throw new LoginException("Subject is Readonly");
            }

            try {
                fillSubject();
                if (callbackHandler instanceof PassiveCallbackHandler) {
                    ( (PassiveCallbackHandler) callbackHandler).clearPassword();
                }

                return true;
            }
            catch (LoginException ex) {
                throw ex;
            }
            catch (Exception ex) {
                userId = null;
                LoginException exception = new LoginException(
                    "Could not commit the Authentication" + ex.getMessage());
                exception.initCause(ex);
                throw exception;
            }
        }
        return true;
    }

    /**
     * <p> This method is called if the LoginContext's
     * overall authentication failed.
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * did not succeed).
     *
     * <p> If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> and <code>commit</code> methods),
     * then this method cleans up any state that was originally saved.
     *
     * <p>
     *
     * @exception LoginException if the abort fails.
     *
     * @return false if this LoginModule's own login and/or commit attempts
     *     failed, and true otherwise.
     */
    public boolean abort() throws LoginException {

        if (debug) {
            logger.debug("abort");

            // Clean out state
        }
        userId = null;

        if (callbackHandler instanceof PassiveCallbackHandler) {
            ( (PassiveCallbackHandler) callbackHandler).clearPassword();
        }

        logout();

        return (true);
    }

    /**
     * Logout a user.
     *
     * <p> This method removes the Principals
     * that were added by the <code>commit</code> method.
     *
     * <p>
     *
     * @exception LoginException if the logout fails.
     *
     * @return true in all cases since this <code>LoginModule</code>
     *      should not be ignored.
     */
    public boolean logout() throws LoginException {

        logger.debug("logout");

        userId = null;
        subject.getPrincipals().clear();
        subject.getPrivateCredentials().clear();
        subject.getPublicCredentials().clear();

        if (callbackHandler instanceof PassiveCallbackHandler) {
            ( (PassiveCallbackHandler) callbackHandler).clearPassword();
        }

        return (true);
    }

    /**
     * Check user in database
     *
     * @param name
     * @param pass
     * @return
     * @throws Exception
     */
    private Long getUserFromDatabase(String name, String pass) throws
        SQLException, HibernateException {

        String encryptPassword = null;
        //User user = null;
        String compare = null;

        // get user object by email address
        Object[] userInfo = getUserLoginInfo(name);
        if (userInfo == null) {
            logger.debug("Unknown user " + name);
            return null;

        }
        Long dbUserId = (Long) userInfo[0];
        String dbPassword = (String) userInfo[1];
        String salt = (String) userInfo[2];
        logger.debug("User " + name + " has id " + dbUserId);

        for (int i = 0; i < 3; i++) {

            if (pass.length() != 40) {
                switch (i) {
                    case 0:

                        compare = pass.trim() + salt;

                        // first try new user ( using SHA1 )
                        encryptPassword = ShaCrypt.crypt(compare.trim()).trim().
                            toUpperCase();
                        break;

                    case 1:

                        compare = pass.trim();

                        // first try new user ( using SHA1 )
                        encryptPassword = ShaCrypt.crypt(compare.trim()).trim();
                        break;

                    case 2:

                        // second try old user ( using unix crypt )
                        if (!pass.startsWith("8x"))
                            encryptPassword = UnixCrypt.crypt("8x", pass.trim()).
                                trim();
                        else
                            encryptPassword = pass.trim();
                        break;
                }
            }
            else {
                encryptPassword = pass.trim();
            }

            // check user in database
            if (encryptPassword.equalsIgnoreCase(dbPassword.trim())) {
                logger.debug("Password matched");
                // add credentials and principals to this

                return dbUserId;
            }
        }

        return null;
    }

    private static Object[] getUserLoginInfo(String email) throws SQLException,
        HibernateException {
        List list = null;
        Session session = null;

        session = PersistenceManager.getSession();
           String queryString = "select u.id, u.password, u.salt from " + User.class.getName() +
                             " u where lower(u.email) = :email";
            
        Query query = session.createQuery(queryString);
        query.setString("email", email.toLowerCase());
            
        list = query.list();

        if (list == null || list.size() == 0) {
            return null;
        }

        if (list.size() > 1) {
            logger.warn(list.size() + " users have same email, " + email);
        }

        return (Object[]) list.get(0);

    }

    private void fillSubject() throws SQLException, HibernateException,
        LoginException {
        Session session = null;
        User user = null;
        try {
            session = PersistenceManager.getSession();

            if (increaseCounter) {
//beginTransaction();

                UserLoginInfo logInfo = null;
                try {
                    logInfo = (UserLoginInfo) session.get(UserLoginInfo.class,
                        userId);
                }
                catch (ObjectNotFoundException ex) { }
                if (logInfo == null) {
                    logInfo = new UserLoginInfo();
                    logInfo.setSecondToLastVisit(null);
                    logInfo.setNumberOfSessions(1);
                    logInfo.setLastVisit(new Date());
                    logInfo.setId(userId);
                    session.save(logInfo);
                }
                else {
                    logInfo.setSecondToLastVisit(logInfo.getLastVisit());
                    logInfo.setLastVisit(new Date());
                    logInfo.setNumberOfSessions(logInfo.getNumberOfSessions() +
                                                1);
                    session.update(logInfo);
                }

                //tx.commit();
                //session.evict(logInfo);
            }

            //session.getSessionFactory().evict(User.class, userId);
            user = (User) session.get(User.class, userId);

            //ProxyHelper.initializeObject(user);
            if (user.isBanned()) {
                throw new UserBannedException("User #" + userId + " is banned");
            }
            UserUtils.fillUserSubject(subject, user);

        }
        catch (HibernateException ex) {
            throw ex;
        }

    }

}
