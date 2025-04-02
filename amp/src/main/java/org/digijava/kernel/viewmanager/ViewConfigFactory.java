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
