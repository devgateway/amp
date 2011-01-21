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

package org.digijava.module.translation.config;

import java.util.ArrayList;
import java.util.List;

import org.digijava.kernel.config.KeyValuePair;

public class GroupCfg {
    private String id;
    private List keys;

    public GroupCfg() {
        keys = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public List getKeys() {
        return keys;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKeys(List keys) {
        this.keys = keys;
    }

    public void addKey(KeyValuePair keyValue) {
        keys.add(keyValue);
    }
}
