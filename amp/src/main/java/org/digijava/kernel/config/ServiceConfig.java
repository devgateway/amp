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

import java.util.HashMap;
import java.util.Map;

public class ServiceConfig {

    private String serviceClass;
    private String serviceId;
    private String description;
    private Map properties;
    private int level;

    public ServiceConfig() {
        properties = new HashMap();
        level = 1; // init after core initialization is complete
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Map getProperties() {
        return properties;
    }

    public int getLevel() {
        return level;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addProperty(KeyValuePair property) {
        properties.put(property.getKey(), property.getValue());
    }
}
