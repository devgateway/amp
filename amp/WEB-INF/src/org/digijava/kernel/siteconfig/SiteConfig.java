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

package org.digijava.kernel.siteconfig;

import java.util.Vector;

/**
 * Data class, used by Digester to parse site-config.xml.
 * See org.digijava.kernel.util.ConfigurationManager for more details
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class SiteConfig
    extends ConfigurationItem {
    private SiteLayout siteLayout;
    private ModuleLayout moduleLayout;
    private String template;

    public SiteConfig() {
        this.siteLayout = null;
        this.moduleLayout = null;
        this.template = null;
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {}

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<site-config template=\"" + template).append("\">").append(newLine);

        buf.append(siteLayout).append(newLine);

        buf.append(moduleLayout).append(newLine);
        buf.append("</site-config>");
        return buf.toString();
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public ModuleLayout getModuleLayout() {
        return moduleLayout;
    }

    public void setModuleLayout(ModuleLayout moduleLayout) {
        this.moduleLayout = moduleLayout;
    }

    public SiteLayout getSiteLayout() {
        return siteLayout;
    }

    public void setSiteLayout(SiteLayout siteLayout) {
        this.siteLayout = siteLayout;
    }

    public void merge(ConfigurationItem configurationItem) {
        SiteConfig secondSiteConfig = (SiteConfig) configurationItem;

        if (secondSiteConfig.getTemplate() != null) {
            template = secondSiteConfig.getTemplate();
        }

        if (siteLayout == null) {
            siteLayout = secondSiteConfig.getSiteLayout();
        }
        else {
            if (secondSiteConfig.getSiteLayout() != null) {
                siteLayout.merge(secondSiteConfig.getSiteLayout());
            }
        }

        if (moduleLayout == null) {
            moduleLayout = secondSiteConfig.getModuleLayout();
        }
        else {
            if (secondSiteConfig.getModuleLayout() != null) {
                moduleLayout.merge(secondSiteConfig.getModuleLayout());
            }
        }

    }

    public void validate() throws Exception {};

}
