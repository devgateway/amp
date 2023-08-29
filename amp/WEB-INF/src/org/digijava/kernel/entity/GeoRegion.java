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

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;

public class GeoRegion implements Group {

    private String id;
    private String name;
    private String type;
    private String active;


    public GeoRegion() {
    }

    public GeoRegion(String name, String id, String type) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.active = "true";
    }

    public String getActive() {
        return active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static String getNameByISO2(String ISO2) {
        /*
         * Uses the DgGeoRegionsCollection map
         */
        return null;
    }


    public String isActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive(String active) {
            return this.active;
    }

    public boolean addMember(Principal member) {
        return true;
    }

    public boolean removeMember(Principal member) {
        return true;
    }

    public boolean isMember(Principal member) {
        return true;
    }

    public Enumeration members() {
        return null;
    }

}
