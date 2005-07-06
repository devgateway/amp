/*
*   DefaultViewConfigImpl.java
*   @Author Mikheil Kapanadze mikheil@digijava.org
*   Created: Apr 13, 2004
*   CVS-ID: $Id: DefaultViewConfigUtil.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
package org.digijava.kernel.viewmanager.impl;

import org.digijava.kernel.viewmanager.AbstractViewConfig;
import javax.servlet.ServletContext;
import org.digijava.kernel.viewmanager.ViewConfigException;
import org.digijava.kernel.siteconfig.SiteConfig;

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