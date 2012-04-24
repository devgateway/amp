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

package org.digijava.kernel.config;

/**
 * Data class, used by Digester to parse digi.xml.
 * See org.digijava.kernel.startup.ConfigLoaderListener,
 * org.digijava.kernel.persistence.HibernateClassLoader
 * for more details
 * @author Lasha Dolidze
 * @version 1.0
 */
public class HibernateClass {

    private String content;
    private String required;
    private String filter;
    private boolean precache;
    private boolean load;
    private String region;
    private boolean forcePrecache;

    public HibernateClass() {
        precache = false;
        load = true;
        filter = null;
        forcePrecache = false;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public boolean isPrecache() {
        return precache;
    }

    public void setPrecache(boolean precache) {
        this.precache = precache;
    }

    public String toString() {
        return "hibernate-class: content=\"" + content + "\" required=\"" +
            required + "\"" + " precache=\"" + precache + "\"" +
            "filter=\"" + filter + "\" region=\"" + region + "\"";
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getRegion() {
        return region;
    }

    public boolean isForcePrecache() {

        return forcePrecache;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setForcePrecache(boolean forcePrecache) {

        this.forcePrecache = forcePrecache;
    }
}
