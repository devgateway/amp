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
import java.util.Iterator;
import java.util.List;

public class RobotsConfigs {

    private List robotsConfigs;

    public RobotsConfigs() {
        robotsConfigs = new ArrayList();
    }

    public List getRobotsConfigs() {
        return robotsConfigs;
    }

    public void setRobotsConfigs(List robotsConfigs) {
        this.robotsConfigs = robotsConfigs;
    }

    public void addRobotsConfig(RobotsConfig config) {
        robotsConfigs.add(config);
    }

    public RobotsConfig getRobotsConfig(String domain) {
        RobotsConfig defaultConf = null;
        Iterator iter = robotsConfigs.iterator();
        while (iter.hasNext()) {
            RobotsConfig item = (RobotsConfig) iter.next();
            if (item.getDomains().contains(domain)) {
                return item;
            }
            if (item.isDefaultConf()) {
                defaultConf = item;
            }
        }
        return defaultConf;
    }
}
