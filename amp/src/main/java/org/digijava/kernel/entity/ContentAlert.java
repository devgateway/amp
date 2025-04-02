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

import org.digijava.kernel.user.User;
import org.digijava.kernel.request.Site;
import java.sql.Clob;
import java.util.Set;
import java.io.Serializable;

public class ContentAlert
    implements Serializable {

    private Long value;
    private String name;

    public ContentAlert() {
    }

    public ContentAlert(Long value) {
        this.value = value;
    }

    public ContentAlert(Long value,String name) {
        this.value = value;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Long getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
