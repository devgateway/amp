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

package org.digijava.kernel.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.viewmanager.ViewConfig;
import org.digijava.kernel.viewmanager.ViewConfigFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SiteManager {

    private static Logger logger = Logger.getLogger(SiteManager.class);

    public static void createSite(String name, String siteKey, String dirName,
                                  String[] hosts, String[] pathes,
                                  String siteRootDir, String templateName) throws DgException {

        Site site = prepareSiteObject(name, siteKey, dirName, hosts, pathes, templateName);

        Session sess = null;
        try {
            sess = org.digijava.kernel.persistence.PersistenceManager.
                getSession();
//beginTransaction();
            sess.save(site);
            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to create site ",ex);
            throw new DgException(
                "Unable to create site", ex);
        }
        SiteUtils.fixDefaultGroupPermissions(site);


        createSiteConfig(dirName, siteRootDir, templateName);

    }

    private static void createSiteConfig(String dirName, String siteRootDir,
                                         String templateName) {
        File siteDir = new File(siteRootDir + File.separator + dirName);

        if (!siteDir.exists()) {
            siteDir.mkdir();

            File siteConfig = new File(siteRootDir + File.separator + dirName +
                                       File.separator + "site-config.xml");

            try {
                siteConfig.createNewFile();
                PrintStream ps = new PrintStream(new FileOutputStream(
                    siteConfig));
                ps.println("<?xml version=\"1.0\"?>");
                if (templateName == null) {
                    ps.println("<site-config />");
                }
                else {
                    ps.println("<site-config template=\"" + templateName +
                               "\" />");
                }

            }
            catch (IOException ex3) {
                logger.warn("siteConfig.createNewFile() failed ",ex3);
            }
        }
    }

    private static Site prepareSiteObject(String name, String siteKey,
                                          String dirName, String[] hosts,
                                          String[] pathes, String templateName) throws
        DgException {
        Site site = new Site(name, siteKey);
        site.setFolder(dirName);
        site.setSiteDomains(new HashSet());

        for (int i = 0; i < hosts.length; i++) {
            SiteDomain siteDomain = new SiteDomain();
            siteDomain.setSite(site);
            siteDomain.setSiteDbDomain(hosts[i]);
            siteDomain.setDefaultDomain(i == 0);
            if (pathes != null) {
                try {
                    siteDomain.setSitePath(pathes[i]);
                }
                catch (IndexOutOfBoundsException ex2) {
                    logger.error("siteDomain.setSitePath() failed ",ex2);
                    siteDomain.setSitePath(null);
                }
            }
            site.getSiteDomains().add(siteDomain);
        }

        SiteUtils.addDefaultGroups(site);

        if (templateName != null) {
            ViewConfig templateViewConfig = ViewConfigFactory.getInstance().
                getTemplateViewConfig(templateName);
            HashSet moduleInstances = new HashSet(templateViewConfig.
                getReferencedInstances(false));
            Iterator instancesIter = moduleInstances.iterator();
            while (instancesIter.hasNext()) {
                ModuleInstance moduleInstance = (ModuleInstance)
                    instancesIter.next();
                moduleInstance.setSite(site);
                moduleInstance.setPermitted(true);
                moduleInstance.setRealInstance(null);
            }

            site.setModuleInstances(moduleInstances);
        }
        return site;
    }

    public static boolean createSiteFolder(String folderPath, String template) throws
        IOException {
        boolean retVal = false;
        // Check site directory
        File siteDir = new File(folderPath);

        if (siteDir.exists()) {
            retVal = true;
            logger.warn("directory " + folderPath + "already exists");
            //throw new AdminException("Such directory already exists");
        } else {
            siteDir.mkdir();
        }
        //siteDir.mkdir();

        File siteConfig = new File(siteDir.getAbsolutePath() + File.separator +
                                   "site-config.xml");

        if (!siteConfig.exists()) {
            siteConfig.createNewFile();
            PrintStream ps = new PrintStream(new FileOutputStream(siteConfig));
            ps.println("<?xml version=\"1.0\"?>");

            if (SiteConfigUtils.BLANK_TEMPLATE_NAME.equals(template)) {
                ps.println("<site-config />");
            }
            else {
                ps.println("<site-config template=\"" + template +
                           "\" />");
            }
            ps.close();
        }

        return retVal;
    }

}
