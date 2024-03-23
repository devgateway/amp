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

package org.digijava.kernel.viewmanager.impl;

import org.digijava.kernel.siteconfig.SiteConfig;
import org.digijava.kernel.viewmanager.AbstractViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigException;

import javax.servlet.ServletContext;

public abstract class DefaultViewConfigUtil
    extends AbstractViewConfig {

    protected DefaultViewConfigUtil(ServletContext servletContext) throws
        ViewConfigException {
        super(servletContext);
    }

    protected SiteConfig createConfiguration(String folderName, boolean isTemplate) throws ViewConfigException {
        SiteConfig siteConfig = null;
        SiteConfig currentConfig = addConfigurationFile(folderName, isTemplate);

        if (isTemplate && folderName.equals(BLANK_TEMPLATE_NAME)) {
            // Blank site must always have site-config.xml
            if (currentConfig == null) {
                throw new ViewConfigException (
                    "Blank site configuration file does not exist");
            }
            siteConfig = currentConfig;
            expandLayoutInheritance(siteConfig);
        }
        else {
            if (currentConfig == null) {
                // If site does not have site-config.xml, use blank site's one
                siteConfig = createConfiguration(BLANK_TEMPLATE_NAME, true);
                expandLayoutInheritance(siteConfig);
            }
            else {
                // Get parent's configuration
                String templateName;
                if (isTemplate) {
                    // Template is always inherited from blank template
                    templateName = BLANK_TEMPLATE_NAME;
                }
                else {
                    if (currentConfig.getTemplate() == null ||
                        currentConfig.getTemplate().trim().length() == 0) {
                        templateName = BLANK_TEMPLATE_NAME;
                    }
                    else {
                        templateName = currentConfig.getTemplate().trim();
                    }
                }

                SiteConfig parentConfig = createConfiguration(templateName, true);

                // Assemble site configuration from parent's configuration +
                // this site configuration

                siteConfig = parentConfig;
                siteConfig.merge(currentConfig);
                expandLayoutInheritance(siteConfig);
            }
        }

        return siteConfig;
    }


}
