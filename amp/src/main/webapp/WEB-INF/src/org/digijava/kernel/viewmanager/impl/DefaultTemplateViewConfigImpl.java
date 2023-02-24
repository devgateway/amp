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

import java.util.Map;
import javax.servlet.ServletContext;

import org.digijava.kernel.viewmanager.ViewConfigException;

public class DefaultTemplateViewConfigImpl extends DefaultViewConfigUtil {
    private String template;

    public DefaultTemplateViewConfigImpl(String template, ServletContext servletContext) throws ViewConfigException {
        super(servletContext);
        this.template = template;
        reload();
    }

    public void reload() throws ViewConfigException {
        this.dependencyWalker.clear();
        this.siteConfig = createConfiguration(template, true);
    }

    public String getMainLayoutPath(String layoutName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getMainLayoutPath() not yet implemented.");
    }
    public String getFilePath(String moduleName, String moduleInstance, String pageName, String teaserName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getFilePath() not yet implemented.");
    }
    public Map getMasterContextAttributes(String layoutName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getMasterContextAttributes() not yet implemented.");
    }
    public String getPagePath(String pageName, boolean appendExtension) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getPagePath() not yet implemented.");
    }
    public String getTeaserAction(String moduleName, String moduleInstance, String teaserName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getTeaserAction() not yet implemented.");
    }
    public String getFilePath(String fileName) throws org.digijava.kernel.viewmanager.ViewConfigException {
        /**@todo Implement this org.digijava.kernel.viewmanager.ViewConfig abstract method*/
        throw new java.lang.UnsupportedOperationException("Method getFilePath() not yet implemented.");
    }

    public String getTemplateName() {
        return BLANK_TEMPLATE_NAME;
    }
}
