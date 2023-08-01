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

package org.digijava.kernel.security.auth;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.ShaCrypt;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class DigiDaoAuthenticationProvider
        extends DaoAuthenticationProvider implements InitializingBean {

    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken
                                                          authentication) throws
            AuthenticationException {

        String encryptPassword = null;
        String compare = null;

        String userPassword = userDetails.getPassword();
        String pass = authentication.getCredentials().toString();

//        Object saltObj;
//        String salt = "";
//
//        SaltSource saltSource = this.getSaltSource();
//        if (saltSource != null) {
//            saltObj = saltSource.getSalt(userDetails);
//            if (saltObj != null) {
//                salt = saltObj.toString();
//            }
//        }

        boolean passwordMatched = false;
        compare = pass.trim();

        // first try new user ( using SHA1 )
        encryptPassword = ShaCrypt.crypt(compare.trim()).trim();


        // check user in database
        if (encryptPassword.equalsIgnoreCase(userPassword.trim())) {
            passwordMatched = true;
        }

        if (!passwordMatched) {
            throw new BadCredentialsException(
                    "Invalid username/password, login denied");
        }
    }


    public boolean supports(Class authentication) {
        return (UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication));
    }

    public Object getSalt(UserDetails userDetails) {
        String email = userDetails.getUsername();
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " + User.class.getName() +
                    " u where lower(u.email) =? ");
            q.setString(0, email.toLowerCase());
            q.setCacheable(true);

            List results = q.list();
            if (results.size() == 0) {
                throw new UsernameNotFoundException(
                        "There is no user with email: " + email);
            }
            else {
                return ( (User) results.get(0)).getSalt();
            }
        }

        catch (Exception ex) {

            throw new DataRetrievalFailureException("Unable to load user: " +
                    email, ex);
        }
    }
}