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

package org.digijava.module.translation.security;

import org.apache.commons.lang.builder.EqualsBuilder;
import java.io.Serializable;

public class TranslateObject implements Serializable {

    public static final String LOCALE_CODE_ALL = "*";
    public static final String SITE_NAME_GLOBAL = "global";

    private Long siteId;
    private String localeId;

    public TranslateObject() {
    }

    public TranslateObject(Long siteId, String localeId) {
        this.siteId = siteId;
        this.localeId = localeId;
    }

    public String getLocaleId() {
        return localeId;
    }

    public void setLocaleId(String localeId) {
        this.localeId = localeId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (! (other instanceof TranslateObject))
            return false;
        TranslateObject castOther = (TranslateObject) other;
        return new EqualsBuilder()
            .append(this.getSiteId(), castOther.getSiteId())
            .append(this.getLocaleId(), castOther.getLocaleId())
            .isEquals();
    }

    public boolean contains(TranslateObject another) {
        if (siteId == null ||
            (siteId != null && another != null && another.siteId != null &&
             siteId.equals(another.siteId))) {

            return (localeId == null ||
                    (localeId != null && another != null && another.localeId != null &&
                     localeId.equals(another.localeId)));
        }
        else {
            return false;
        }
    }
}
