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

package org.digijava.kernel.services.siteidentity;

import java.util.HashMap;
import java.util.Map;

public class ServiceConfig {
    private String serviceId;
    private String defaultId;
    private Map siteConfigs = new HashMap();

    public String getDefaultId() {
        return defaultId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Map getSiteConfigs() {
        return siteConfigs;
    }

    public void setDefaultId(String defaultId) {
        this.defaultId = defaultId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setSiteConfigs(Map siteConfigs) {
        this.siteConfigs = siteConfigs;
    }

    public void addSite(String realId, String idForService) {
        siteConfigs.put(realId, idForService);
    }
}
