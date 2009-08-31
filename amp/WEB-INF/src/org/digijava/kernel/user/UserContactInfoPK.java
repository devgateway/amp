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

package org.digijava.kernel.user;

import java.io.Serializable;

public class UserContactInfoPK
    implements Serializable {

    private User user;
    private int contactIndex;

    public UserContactInfoPK() {

    }

    public UserContactInfoPK(User user) {
        this.user = user;
    }

    public boolean equals(Object obj) {
        /**@todo gadaakete :) */
        if (! (obj instanceof UserContactInfoPK)) {
            return false;
        }
        UserContactInfoPK otherUser = (UserContactInfoPK) obj;
        long userId =  this.user != null ?
            this.user.getId().longValue() : 0;
        int index=this.contactIndex;
        long otherUserId = otherUser != null && otherUser.user != null ?
            otherUser.user.getId().longValue() : 0;
        int otherIndex=otherUser.contactIndex;
        return (userId == otherUserId) && (index==otherIndex);
    }

    public int hashCode() {
        if (user != null) {
            return user.getId().hashCode();
        }
        return 0;
    }

    public User getUser() {
        return user;
    }

    public int getContactIndex() {
        return contactIndex;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setContactIndex(int contactIndex) {
        this.contactIndex = contactIndex;
    }

}
