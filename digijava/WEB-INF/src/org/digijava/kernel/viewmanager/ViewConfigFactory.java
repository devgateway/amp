/*
 *   ViewConfigFactory.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 *   Created: Apr 08, 2004
 *   CVS-ID: $Id: ViewConfigFactory.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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
package org.digijava.kernel.viewmanager;

import org.digijava.kernel.request.Site;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import org.apache.log4j.Logger;
import org.digijava.kernel.viewmanager.impl.DefaultViewConfigImpl;
import org.digijava.kernel.viewmanager.impl.DefaultTemplateViewConfigImpl;
import org.digijava.kernel.util.DigiConfigManager;

public final class ViewConfigFactory {
    private static Logger logger = Logger.getLogger(ViewConfigFactory.class);

    public static final String DEFAULT_IMPL = "default";
    public static final String REPOSITORY_IMPL = "repository";

    private static ViewConfigFactory instance;

    private ServletContext servletContext;
    private Map configs;
    private Map templateConfigs;

    public static synchronized void initialize(ServletContext servletContext) {
        if (instance == null) {
            instance = new ViewConfigFactory(servletContext);
        }
    }

    private ViewConfigFactory(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.configs = Collections.synchronizedMap(new HashMap());
        this.templateConfigs = Collections.synchronizedMap(new HashMap());
    }

    public ViewConfig getViewConfig(Site site) throws ViewConfigException {
        ViewConfig viewConfig = (ViewConfig) configs.get(site.getId());
        if (viewConfig == null) {
            viewConfig = createViewConfig(site);
            configs.put(site.getId(), viewConfig);
        }
        else {
            synchronized (viewConfig) {
                if (!viewConfig.isUpToDate()) {
                    viewConfig.reload();
                }
            }
        }
        return viewConfig;
    }

    public ViewConfig getViewConfig(long siteId) {
        /**
         * @todo complete this method
         */
        throw new UnsupportedOperationException(
            "getViewConfig(long siteId) is not yet implemented");
    }

    public ViewConfig getViewConfig(String siteKey) {
        /**
         * @todo complete this method
         */
        throw new UnsupportedOperationException(
            "getViewConfig(String siteKey) is not yet implemented");
    }

    public ViewConfig getTemplateViewConfig(String templateName) throws ViewConfigException {
        ViewConfig viewConfig = (ViewConfig) templateConfigs.get(templateName);
               if (viewConfig == null) {
                   viewConfig = createTemplateViewConfig(templateName);
                   templateConfigs.put(templateName, viewConfig);
               }
               else {
                   synchronized (viewConfig) {
                       if (!viewConfig.isUpToDate()) {
                           viewConfig.reload();
                       }
                   }
               }
               return viewConfig;
    }

    private ViewConfig createViewConfig(Site site) throws ViewConfigException {
        ViewConfig viewConfig;
        if (DigiConfigManager.getConfig().getSiteConfigImpl().equals(
            REPOSITORY_IMPL)) {
            viewConfig = new org.digijava.kernel.viewmanager.reposimpl.
                ViewConfigImpl(site,
                               servletContext);
        }
        else {
            viewConfig = new DefaultViewConfigImpl(site,
                servletContext);
        }

        logger.debug("createViewConfig() returns implementation of class " + viewConfig.getClass().getName());
        return viewConfig;
    }

    private ViewConfig createTemplateViewConfig(String template) throws ViewConfigException {
        ViewConfig viewConfig;
        if (DigiConfigManager.getConfig().getSiteConfigImpl().equals(
            REPOSITORY_IMPL)) {
            viewConfig = new org.digijava.kernel.viewmanager.reposimpl.
                TemplateViewConfigImpl(template,
                               servletContext);
        }
        else {
            viewConfig = new DefaultTemplateViewConfigImpl(template, servletContext);
        }

        logger.debug("createTemplateViewConfig() returns implementation of class " + viewConfig.getClass().getName());
        return viewConfig;
    }

    public static  ViewConfigFactory getInstance() {
        return instance;
    }
}