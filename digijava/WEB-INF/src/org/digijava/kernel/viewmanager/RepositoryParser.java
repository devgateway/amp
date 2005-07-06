/*
*   RepositoryParser.java
*   @Author Mikheil Kapanadze mikheil@digijava.org
*   Created: Apr 10, 2004
*   CVS-ID: $Id: RepositoryParser.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.digijava.kernel.siteconfig.Instance;
import org.digijava.kernel.siteconfig.Layout;
import org.digijava.kernel.siteconfig.Module;
import org.digijava.kernel.siteconfig.ModuleLayout;
import org.digijava.kernel.siteconfig.Page;
import org.digijava.kernel.siteconfig.Put;
import org.digijava.kernel.siteconfig.PutItem;
import org.digijava.kernel.siteconfig.SiteConfig;
import org.digijava.kernel.siteconfig.SiteLayout;
import org.digijava.kernel.siteconfig.Teaser;
import org.xml.sax.SAXException;
import org.digijava.kernel.siteconfig.RepositoryLayout;
import org.digijava.kernel.siteconfig.SecondaryPage;

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

        digester.addObjectCreate("config/module", Module.class);
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