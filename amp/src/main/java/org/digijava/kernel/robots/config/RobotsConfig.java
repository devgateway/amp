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

package org.digijava.kernel.robots.config;

import java.util.ArrayList;
import java.util.List;

public class RobotsConfig {

    private List domains;
    private String content;
    private boolean defaultConf;
    private String path;

    public RobotsConfig() {
        domains = new ArrayList();
        defaultConf = false;
    }

    public String getContent() {
        return content;
    }

    public List getDomains() {
        return domains;
    }

    public boolean isDefaultConf() {
        return defaultConf;
    }

    public String getPath() {
        return path;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDomains(List domains) {
        this.domains = domains;
    }

    public void setDefaultConf(boolean defaultConf) {
        this.defaultConf = defaultConf;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addDomain(String domain) {
        domains.add(domain);
    }
}
