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

public class UserContactInfo {

    public static final String PHONE = "user.home-info.telecom.telephone.number";
    public static final String FAX = "user.home-info.telecom.fax.number";

    private UserContactInfoPK id;
    private String contactType;
    private String contactData;

    public String getContactData() {
        return contactData;
    }

    public String getContactType() {
        return contactType;
    }

    public UserContactInfoPK getId() {
        return id;
    }

    public void setContactData(String contactData) {
        this.contactData = contactData;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public void setId(UserContactInfoPK id) {
        this.id = id;
    }

}
