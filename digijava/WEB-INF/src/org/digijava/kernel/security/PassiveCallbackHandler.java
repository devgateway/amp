/*
*   PassiveCallbackHandler.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: PassiveCallbackHandler.java,v 1.1 2005-07-06 10:34:22 rahul Exp $
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

package org.digijava.kernel.security;


import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * <p>
 * PassiveCallbackHandler has constructor that takes
 * a username and password so its handle() method does
 * not have to prompt the user for input.
 * Useful for server-side applications.
 *
 * Built on code example from "All That JAAS" article in JavaWorld
 * http://www.javaworld.com/javaworld/jw-09-2002/jw-0913-jaas.html
 *
 * @author Paul Feuer and John Musser
 * @version 1.0
 */

public class PassiveCallbackHandler
    implements CallbackHandler {

    private String username;
    char[] password;
    private String sessionId;
    private int mode;

    public PassiveCallbackHandler(String sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("sessionId parameter must be non-null");
        }
        this.sessionId = sessionId;
        this.username = null;
        this.password = null;
    }
    /**
     * <p>Creates a callback handler with the give username
     * and password.
     */
    public PassiveCallbackHandler(String user, String pass) {
        if (user == null || pass == null) {
            throw new IllegalArgumentException("username & password parameters must be non-null");
        }
        this.username = user;
        this.password = pass.toCharArray();
        this.sessionId = null;
    }

    /**
     * Handles the specified set of Callbacks. Uses the
     * username and password that were supplied to our
     * constructor to popluate the Callbacks.
     *
     * This class supports NameCallback and PasswordCallback.
     *
     * @param   callbacks the callbacks to handle
     * @throws  IOException if an input or output error occurs.
     * @throws  UnsupportedCallbackException if the callback is not an
     * instance of NameCallback or PasswordCallback
     */
    public void handle(Callback[] callbacks) throws java.io.IOException,
        UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callbacks[i];
                    nameCallback.setName(username);
            }
            else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pwCallback = (PasswordCallback)callbacks[i];
                pwCallback.setPassword(password);
            }
            else if (callbacks[i] instanceof SessionIdCallback) {
                SessionIdCallback idCallback = (SessionIdCallback) callbacks[i];
                idCallback.setSessionId(sessionId);
            }
            else {
                throw new UnsupportedCallbackException(
                    callbacks[i],
                    "Callback class not supported");
            }
        }
    }

    /**
     * Clears out password state.
     */
    public void clearPassword() {
        if (password != null) {
            for (int i = 0; i < password.length; i++) {
                password[i] = ' ';
            }
            password = null;
        }
    }

}
