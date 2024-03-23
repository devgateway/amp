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

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.siteconfig.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public final class SiteConfigParser {
    private static Logger log =
        Logger.getLogger(SiteConfigParser.class);

    private static Digester digester = createDigester();

    /**
     * Create new Digester object, which will parse site configuration file
     * @return Digester object
     */
    private static Digester createDigester() {
        Digester digester = new Digester();
        // Turn of validation against DTD
        digester.setValidating(false);
        // Workaround Tomcat's issue with ClassLoader
        digester.setUseContextClassLoader(true);

        // Configure digester
        digester.addObjectCreate("site-config", SiteConfig.class);
        digester.addSetProperties("site-config", "template", "template");

        digester.addObjectCreate("site-config/site-layout", SiteLayout.class);
        digester.addSetNext("site-config/site-layout", "setSiteLayout");

        digester.addObjectCreate("site-config/site-layout/layout", Layout.class);
        digester.addSetNext("site-config/site-layout/layout", "addLayout");
        digester.addSetProperties("site-config/site-layout/layout", "name",
                                  "name");
        digester.addSetProperties("site-config/site-layout/layout", "file",
                                  "file");
        digester.addSetProperties("site-config/site-layout/layout", "extends",
                                  "extendsLayout");

        digester.addObjectCreate("site-config/site-layout/layout/put", Put.class);
        digester.addSetNext("site-config/site-layout/layout/put", "addPut");
        digester.addBeanPropertySetter("site-config/site-layout/layout/put",
                                       "value");
        digester.addSetProperties("site-config/site-layout/layout/put", "name",
                                  "name");

        digester.addObjectCreate("site-config/site-layout/layout/put-item",
                                 PutItem.class);
        digester.addSetNext("site-config/site-layout/layout/put-item",
                            "addPutItem");
        digester.addBeanPropertySetter(
            "site-config/site-layout/layout/put-item", "value");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "name", "name");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "module", "module");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "page", "page");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "instance", "instance");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "file", "file");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "layout", "layout");
        digester.addSetProperties("site-config/site-layout/layout/put-item",
                                  "teaser", "teaser");

        digester.addObjectCreate("site-config/site-layout/page-group", PageGroup.class);
        digester.addSetNext("site-config/site-layout/page-group", "addPageGroup");
        digester.addSetProperties("site-config/site-layout/page-group", "masterLayout", "masterLayout");
        digester.addSetProperties("site-config/site-layout/page-group", "tile", "tileName");

        digester.addObjectCreate("site-config/site-layout/page-group/module", ModulePageGroup.class);
        digester.addSetNext("site-config/site-layout/page-group/module", "addModulePageGroup");
        digester.addSetProperties("site-config/site-layout/page-group/module", "name", "moduleName");

        digester.addObjectCreate("site-config/site-layout/page-group/module/page", GroupedPage.class);
        digester.addSetNext("site-config/site-layout/page-group/module/page", "addPage");
        digester.addBeanPropertySetter("site-config/site-layout/page-group/module/page", "name");

        digester.addObjectCreate("site-config/module-layout", ModuleLayout.class);
        digester.addSetNext("site-config/module-layout", "setModuleLayout");

        digester.addObjectCreate("site-config/module-layout/module", Module.class);
        digester.addSetNext("site-config/module-layout/module", "addModule");
        digester.addSetProperties("site-config/module-layout/module", "name",
                                  "name");
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/teaser-file", "teaserFile");

        digester.addObjectCreate("site-config/module-layout/module/page", Page.class);
        digester.addSetNext("site-config/module-layout/module/page", "addPage");
        digester.addBeanPropertySetter("site-config/module-layout/module/page",
                                       "value");
        digester.addSetProperties("site-config/module-layout/module/page",
                                  "name", "name");
        digester.addObjectCreate(
            "site-config/module-layout/module/teaser", Teaser.class);
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/teaser", "value");
        digester.addSetNext("site-config/module-layout/module/teaser",
                            "addTeaser");
        digester.addSetProperties(
            "site-config/module-layout/module/teaser", "name", "name");
        digester.addSetProperties(
            "site-config/module-layout/module/teaser", "action", "action");
        digester.addSetProperties(
            "site-config/module-layout/module/teaser", "renderJsp", "renderJsp");

        digester.addObjectCreate("site-config/module-layout/module/instance",
                                 Instance.class);
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/instance/teaser-file",
            "teaserFile");
        digester.addSetNext("site-config/module-layout/module/instance",
                            "addInstance");
        digester.addSetProperties("site-config/module-layout/module/instance",
                                  "name", "name");

        digester.addObjectCreate(
            "site-config/module-layout/module/instance/page", Page.class);
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/instance/page", "value");
        digester.addSetNext("site-config/module-layout/module/instance/page",
                            "addPage");
        digester.addSetProperties(
            "site-config/module-layout/module/instance/page", "name", "name");

        digester.addObjectCreate(
            "site-config/module-layout/module/instance/teaser", Teaser.class);
        digester.addBeanPropertySetter(
            "site-config/module-layout/module/instance/teaser", "value");
        digester.addSetNext("site-config/module-layout/module/instance/teaser",
                            "addTeaser");
        digester.addSetProperties(
            "site-config/module-layout/module/instance/teaser", "name", "name");
        digester.addSetProperties(
            "site-config/module-layout/module/instance/teaser", "action",
            "action");
        digester.addSetProperties(
            "site-config/module-layout/module/instance/teaser", "renderJsp",
            "renderJsp");

        return digester;
    }

    /**
     * Parse configuration file and store configuration to easy access via
     * getSiteConfig() method
     * @param file configuration file
     * @return parsed configuration
     * @throws Exception if error occures during parse
     */
    public static SiteConfig parseConfigurationFile(File file) throws ViewConfigException {
        SiteConfig result = null;
        try {
            /**
             * @todo maybe some day we'll implement digester pooling :-)
             */
            synchronized (digester) {
                result = (SiteConfig) digester.parse(file);
            }
        }
        catch (SAXException ex) {
            throw new ViewConfigException(ex);
        }
        catch (IOException ex) {
            throw new ViewConfigException(ex);
        }

        return result;
    }

}
