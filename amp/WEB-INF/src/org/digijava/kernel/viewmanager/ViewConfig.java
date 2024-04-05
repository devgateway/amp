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

import org.w3c.dom.Document;

import java.util.Map;
import java.util.Set;

/**
 * Describes site's view configuration
 */
public interface ViewConfig {

    /**
     * Returns true if configuration is up to date (no files were changed, etc).
     * false if something was changed and reload is needed
     * @return true if configuration is up to date, false - if not
     */
    public boolean isUpToDate();

    /**
     * Reloads view configuration
     * @throws ViewConfigException if error occurs during reload
     */
    public void reload() throws ViewConfigException;

    /**
     * Returns layout file path for the given name
     * @param layoutName name of the layout
     * @return layout file path for the given name
     */
    public String getLayoutFilePath(String layoutName);

    /**
     * Returns action path for specific teaser
     * @param moduleName name of the module, which owns teaser
     * @param instanceName name of the instance
     * @param teaserName name of the teaser. If null - return action for the
     * default teaser
     * @return action path for specific teaser
     */
    public String getTeaserActionPath(String moduleName, String instanceName, String teaserName);

    public String getTeaserFilePath(String moduleName, String instanceName, String teaserName);

    public String getPageFilePath(String moduleName, String instanceName, String pageName);

    public String getGenericFilePath(String moduleName, String instanceName, String fileName);

    public boolean isAbsoluteFilePath(String filePath);

    /**
     * Searches file in the site's root directory and underlying repository
     * (template, etc)
     * @param fileName name of the file
     * @return file path. null if file was not found
     * @throws ViewConfigException
     * @deprecated this method will be removed after refactoring
     */
    public String getFilePath(String fileName) throws ViewConfigException;

    /**
     * Returns path of the JSP page from site's 'layouts' directory.
     * @param pageName name of the page (header, footer, etc)
     * @param appendExtension true, if .jsp extension should be appended to
     * pageName and false, if not
     * @return Returns path of the JSP page
     * @throws ViewConfigException if error occurs
     * @deprecated this method will be removed after refactoring
     */
    public String getPagePath(String pageName, boolean appendExtension) throws
        ViewConfigException;

    /**
     * Find file's path for given site, module and instance. File can be layout
     * file (header, footer, etc. moduleName==null, moduleInstance==null),
     * default teaser file (pageName == null && teaserName == null), secondary
     * page (pageName != null, teaserName == null), non-default teaser file
     * (pageName == null, teaserName != null)
     * @param moduleName name of the module. If null - assume layout
     * @param moduleInstance name of the module's instance
     * @param pageName name of the page
     * @param teaserName name of the non-default teaser
     * @return file's path
     * @throws ViewConfigException
     * @deprecated this method will be removed after refactoring
     */
    public String getFilePath(String moduleName, String moduleInstance,
                              String pageName, String teaserName)
        throws ViewConfigException;


    /**
     * Returns path for the layout file
     * @param layoutName name of the layout
     * @return path for the layout file
     * @throws ViewConfigException
     * @deprecated this method will be removed after refactoring
     */
    public String getMainLayoutPath(String layoutName)
        throws ViewConfigException;

    /**
     * Returns teaser's action for the given module, instance and teaser name
     * @param moduleName name of the module
     * @param moduleInstance name of the instance
     * @param teaserName name of the teaser
     * @return Returns teaser's action
     * @throws ViewConfigException
     * @deprecated this method will be removed after refactoring
     */
    public String getTeaserAction(String moduleName, String moduleInstance,
                                  String teaserName)
        throws ViewConfigException;

    /**
     * Assemble context attributes for master layout. This method puts correct
     * filenames int tiles context to pass them to master layout
     * @param layoutName name of the layout in site-config.xml file
     * @return context attributes for master layout
     * @throws Exception if error occurs
     */
    public Map getMasterContextAttributes(String layoutName) throws ViewConfigException;

    public boolean isLayoutDefined(String layoutName);

    public String getTemplateName();

    public Set getReferencedModules(boolean includeCommons);

    public Set getReferencedInstances(boolean includeCommons);

    public Document getDocument() throws ViewConfigException;

    public String getXML() throws ViewConfigException;

}
