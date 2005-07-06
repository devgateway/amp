/*
 *   SiteConfig.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: SiteConfig.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
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