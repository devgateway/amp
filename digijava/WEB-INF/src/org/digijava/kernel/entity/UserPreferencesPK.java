/*
 *   UserPreferencesPK.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created:
     *   CVS-ID: $Id: UserPreferencesPK.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import java.io.*;

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
        return new EqualsBuilder()
            .append( (this.getSite() != null ? this.getSite().getId() : null),
                    castOther.getSite() != null ? castOther.getSite().getId() : null)
            .append( (this.getUser() != null ? this.getUser().getId() : null),
                    castOther.getUser() != null ? castOther.getUser().getId() : null)
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getSite() != null ? this.getSite().getId() : null)
            .append(this.getUser() != null ? this.getUser().getId() : null)
            .toHashCode();
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