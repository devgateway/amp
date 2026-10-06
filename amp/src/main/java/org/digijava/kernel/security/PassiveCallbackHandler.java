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


import javax.security.auth.callback.*;
import java.io.IOException;

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
    private char[] password;
    private boolean scrambledPassword;

    /**
     * <p>Creates a callback handler with the give username
     * and password.
     */
    public PassiveCallbackHandler(String user, String pass, boolean scrambledPassword) {
        if (user == null || pass == null) {
            throw new IllegalArgumentException("username & password parameters must be non-null");
        }
        this.username = user;
        this.password = pass.toCharArray();
        this.scrambledPassword = scrambledPassword;
    }

    public PassiveCallbackHandler(String user, String pass) {
        this(user, pass, false);
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

    public boolean isScrambledPassword() {
        return scrambledPassword;
    }

}
