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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.security.DgSecurityManager;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.type.StringType;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DigiUserDetailsService
    implements UserDetailsService {

    private boolean populateGroupAuthorities = false;

    /**
     * loadUserByUsername
     *
     * @param string String
     * @return UserDetailsO
     * @throws UsernameNotFoundException
     * @throws DataAccessException
     * @todo Implement this org.acegisecurity.userdetails.UserDetailsService
     *   method
     */
    public UserDetails loadUserByUsername(String email) throws
        UsernameNotFoundException, DataAccessException {
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " + User.class.getName() +
                                          " u where lower(u.email) =:email ");
            q.setParameter("email", email.toLowerCase(), StringType.INSTANCE);
            q.setCacheable(true);

            List results = q.list();
            if (results.size() == 0) {
                throw new UsernameNotFoundException(
                    "There is no user with email: " + email);
            }
            else {
                return getUserDetails( (User) results.get(0));
            }
        }
        catch(UsernameNotFoundException ex){
            throw ex; // rethrow - this will not print it
        }
        catch (Exception ex) {

            throw new DataRetrievalFailureException("Unable to load user: " +
                email, ex);
        }
    }

    protected UserDetails getUserDetails(User user) throws DataAccessException {
        Collection<? extends GrantedAuthority> authorities = getAssignedAuthorities(user);

        UserDetails ud = new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(), true, true, true, true, authorities);

        return ud;
    }

    protected Collection<? extends GrantedAuthority> getAssignedAuthorities(User user) throws
        DataAccessException {
        Set authorities = new HashSet();

        authorities.add(new SimpleGrantedAuthority("ROLE_AUTHENTICATED"));
       
        if (user.isGlobalAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_GLOBAL_ADMIN"));
        }

        if (populateGroupAuthorities) {
            try {
                Hibernate.initialize(user.getGroups());
            }
            catch (HibernateException ex) {
                throw new DataRetrievalFailureException(
                    "Unable to load groups for user: " + user.getId(), ex);
            }

            Iterator groupIter = user.getGroups().iterator();
            while (groupIter.hasNext()) {
                Group group = (Group) groupIter.next();
                authorities.add(new SimpleGrantedAuthority(
                    "GROUP_" +
                    group.getSite().getSiteId() + "_" + group.getName()));
            }
        }
        Collection<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        Iterator iter = authorities.iterator();
        int i = 0;
        while (iter.hasNext()) {
            GrantedAuthority item = (GrantedAuthority) iter.next();
            result.add(item);
        }
        return  result;
    }

    public boolean isPopulateGroupAuthorities() {
        return populateGroupAuthorities;
    }

    public void setPopulateGroupAuthorities(boolean populateGroupAuthorities) {
        this.populateGroupAuthorities = populateGroupAuthorities;
    }

}
