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

public class RepositoryParser {
    private static Logger logger =
        Logger.getLogger(RepositoryParser.class);

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
        digester.addObjectCreate("config", RepositoryLayout.class);
        digester.addObjectCreate("config/secondary-pages/page", SecondaryPage.class);
        digester.addSetNext("config/secondary-pages/page", "addSecondaryPage");
        digester.addSetProperties("config/secondary-pages/page", "name", "name");
        digester.addSetProperties("config/secondary-pages/page", "page", "page");
        digester.addSetProperties("config/secondary-pages/page", "title", "title");
        digester.addSetProperties("config/secondary-pages/page", "teaser", "teaser");

        digester.addObjectCreate("config/module", AmpModule.class);
        digester.addSetNext("config/module", "setModule");
        digester.addSetProperties("config/module", "name",
                                  "name");
        digester.addBeanPropertySetter(
            "config/module/teaser-file", "teaserFile");

        digester.addObjectCreate("config/module/page", Page.class);
        digester.addSetNext("config/module/page", "addPage");
        digester.addBeanPropertySetter("config/module/page",
                                       "value");
        digester.addSetProperties("config/module/page",
                                  "name", "name");
        digester.addObjectCreate(
            "config/module/teaser", Teaser.class);
        digester.addBeanPropertySetter(
            "config/module/teaser", "value");
        digester.addSetNext("config/module/teaser",
                            "addTeaser");
        digester.addSetProperties(
            "config/module/teaser", "name", "name");
        digester.addSetProperties(
            "config/module/teaser", "action", "action");
        digester.addSetProperties(
            "config/module/teaser", "renderJsp", "renderJsp");

        return digester;
    }

    /**
     * Parse configuration file and store configuration to easy access via
     * getSiteConfig() method
     * @param file configuration file
     * @return parsed configuration
     * @throws Exception if error occures during parse
     */
    public static RepositoryLayout parseLayoutsFile(File file) throws ViewConfigException {
        RepositoryLayout result = null;
        try {
            /**
             * @todo maybe some day we'll implement digester pooling :-)
             */
            synchronized (digester) {
                result = (RepositoryLayout) digester.parse(file);
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
