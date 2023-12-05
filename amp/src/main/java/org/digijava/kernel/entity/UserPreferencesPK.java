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

package org.digijava.kernel.entity;

import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UserPreferencesPK
    implements Serializable {

    private User user;
    private Site site;

    public UserPreferencesPK() {}

    public UserPreferencesPK(User user, Site site) {
        this.user = user;
        this.site = site;
    }

    public boolean equals(Object other) {
        if (! (other instanceof UserPreferencesPK))
            return false;
        UserPreferencesPK castOther = (UserPreferencesPK) other;
        long siteId = (this.site != null ? this.site.getId().longValue() : 0);
        long userId = (this.user != null ? this.user.getId().longValue() : 0);

        long otherSiteId = (castOther.site != null ? castOther.site.getId().longValue() : 0);
        long otherUserId = (castOther.user != null ? castOther.user.getId().longValue() : 0);

        return (siteId == otherSiteId) && (userId == otherUserId);
    }

    public int hashCode() {
        long siteId = (this.site != null ? this.site.getId().longValue() : 0);
        long userId = (this.user != null ? this.user.getId().longValue() : 0);

        int hashCode = new Long(siteId ^ userId).hashCode();
        return hashCode;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(site.getId());
        oos.writeObject(user.getId());
        //oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws
        ClassNotFoundException, IOException {
        Long siteId = (Long)ois.readObject();
        Long userId = (Long)ois.readObject();

        site = new Site();
        site.setId(siteId);
        user = new User();
        user.setId(userId);

        //ois.defaultReadObject();
    }

}
