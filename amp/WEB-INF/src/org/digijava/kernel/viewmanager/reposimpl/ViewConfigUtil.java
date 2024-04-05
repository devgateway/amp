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

package org.digijava.kernel.viewmanager.reposimpl;

import org.apache.log4j.Logger;
import org.digijava.kernel.siteconfig.*;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.viewmanager.AbstractViewConfig;
import org.digijava.kernel.viewmanager.RepositoryParser;
import org.digijava.kernel.viewmanager.ViewConfigException;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.*;

public abstract class ViewConfigUtil
    extends AbstractViewConfig {

    private static Logger logger =
        Logger.getLogger(ViewConfigUtil.class);

    protected HashMap repository;

    protected ViewConfigUtil(ServletContext servletContext) throws
        ViewConfigException {
        super(servletContext);
    }

    public void reload() throws ViewConfigException {
        this.dependencyWalker.clear();
        addComponentsFiles();
    }

    protected SiteConfig createConfiguration(String folderName,
                                             boolean isTemplate) throws
        ViewConfigException {
        SiteConfig siteConfig = null;
        SiteConfig currentConfig = addConfigurationFile(folderName, isTemplate);

        String templateName = null;
        if (currentConfig.getTemplate() != null &&
            currentConfig.getTemplate().trim().length() != 0) {
            templateName = currentConfig.getTemplate().trim();
        }

        if (!isTemplate && templateName != null) {
            SiteConfig parentConfig = createConfiguration(templateName, true);

            siteConfig = parentConfig;
            siteConfig.merge(currentConfig);
            expandLayoutsFromRepository(currentConfig);
            expandLayoutInheritance(siteConfig);
        }
        else {
            expandLayoutsFromRepository(currentConfig);
            siteConfig = currentConfig;
            expandModulesFromRepository(siteConfig);
            expandLayoutInheritance(siteConfig);
        }
        logger.debug("Created configuration file for folder: " + folderName +
                     " template: " + isTemplate);
        logger.debug(siteConfig);

        return siteConfig;
    }

    protected void addComponentsFiles() throws
        ViewConfigException {
        this.repository = new HashMap();
        Iterator iter = DigiConfigManager.getModulesConfig().keySet().iterator();
        while (iter.hasNext()) {
            String moduleName = (String) iter.next();
            addComponentsFile(moduleName);
        }
    }

    protected RepositoryLayout addComponentsFile(String moduleName) throws
        ViewConfigException {
        File configFile;
        configFile = new File(servletContext.getRealPath("/repository/" +
            moduleName + "/components.xml"));

        RepositoryLayout componentsFile = null;
        if (configFile.exists()) {
            dependencyWalker.addFile(configFile);
            componentsFile = RepositoryParser.parseLayoutsFile(configFile);
            repository.put(moduleName, componentsFile);
        }

        logger.debug("adding module " + moduleName + " to repository");
        logger.debug("\n" + componentsFile);
        return componentsFile;
    }

    protected void expandLayoutsFromRepository(SiteConfig siteConfig) {
        if (siteConfig.getSiteLayout() == null) {
            siteConfig.setSiteLayout(new SiteLayout());
        }
        HashMap layouts = siteConfig.getSiteLayout().getLayout();

        HashMap layoutOwners = new HashMap();
        Iterator repositoryIter = repository.entrySet().iterator();
        while (repositoryIter.hasNext()) {
            Map.Entry entry = (Map.Entry) repositoryIter.next();

            RepositoryLayout components = (RepositoryLayout) entry.getValue();
            String moduleName = (String) entry.getKey();

            HashMap secondaryPages = new HashMap();
            Iterator secondaryPageIter = components.getSecondaryPages().values().
                iterator();
            while (secondaryPageIter.hasNext()) {
                SecondaryPage sPage = (SecondaryPage) secondaryPageIter.next();
                if (!layouts.containsKey(sPage.getName())) {
                    secondaryPages.put(sPage.getName(), sPage);
                }
            }
            layoutOwners.put(moduleName, secondaryPages);
        }

        // process in 3 phase
        // 0 - explocitly "layouted" pages by groups
        // 1 - module-wide page groups
        // 2 - system-wide page group (not groups!)
        boolean systemWideDefaultDefined = false;
        for (int i = 0; i < 3; i++) {
            Collection pageGroups = siteConfig.getSiteLayout().getPageGroups();
            Iterator pageGrpIter = pageGroups.iterator();
            while (pageGrpIter.hasNext()) {
                PageGroup pageGroup = (PageGroup) pageGrpIter.next();
                // durind the third step, process system-wide page groups
                if (pageGroup.getModulePageGroups().size() == 0 && i == 2) {
                    if (systemWideDefaultDefined) {
                        logger.error(
                            "two or more system-wide page groups are defined in site-config.xml!");
                    }
                    else {
                        Iterator layoutOwnerIter = layoutOwners.keySet().
                            iterator();
                        while (layoutOwnerIter.hasNext()) {
                            String moduleName = (String) layoutOwnerIter.next();
                            HashMap sPages = (HashMap) layoutOwners.get(
                                moduleName);
                            layouts.putAll(generateLayouts(pageGroup,
                                moduleName, sPages.values()));
                        }
                        layoutOwners.clear();
                    }
                }
                else {
                    Iterator modPageGrpIter = pageGroup.getModulePageGroups().
                        values().iterator();
                    while (modPageGrpIter.hasNext()) {
                        ModulePageGroup modPageGrp = (ModulePageGroup)
                            modPageGrpIter.next();
                        String moduleName = modPageGrp.getModuleName();
                        HashMap sPages = (HashMap) layoutOwners.get(
                            moduleName);
                        if (modPageGrp.getPages().size() == 0 && i == 1) {
                            if (sPages != null) {
                                layouts.putAll(generateLayouts(pageGroup,
                                    moduleName, sPages.values()));
                            }
                            layoutOwners.remove(moduleName);

                        }
                        else {
                            Iterator secPagesIter = modPageGrp.getPages().
                                values().iterator();
                            while (secPagesIter.hasNext()) {
                                GroupedPage gPage = (GroupedPage) secPagesIter.
                                    next();
                                if (sPages != null) {
                                    SecondaryPage sPage = (SecondaryPage)
                                        sPages.get(gPage.getName());

                                    if (sPage != null) {
                                        Layout layout = generateLayout(
                                            pageGroup,
                                            moduleName, sPage);
                                        layouts.put(layout.getName(), layout);
                                    }
                                    sPages.remove(gPage.getName());
                                }
                            }
                        }
                    }
                }
            }

        }

        // generate single-page layouts
        if (!systemWideDefaultDefined) {
            Iterator layoutOwnerIter = layoutOwners.keySet().
                iterator();
            while (layoutOwnerIter.hasNext()) {
                String moduleName = (String) layoutOwnerIter.next();
                HashMap sPages = (HashMap) layoutOwners.get(
                    moduleName);
                layouts.putAll(generateLayouts(null,
                                               moduleName, sPages.values()));
            }

        }
        siteConfig.getSiteLayout().setPageGroups(new ArrayList());
    }

    private Map generateLayouts(PageGroup pageGroup, String moduleName,
                                Collection secondaryPages) {
        HashMap layouts = new HashMap();
        Iterator sPagesIter = secondaryPages.iterator();
        while (sPagesIter.hasNext()) {
            SecondaryPage sPage = (SecondaryPage) sPagesIter.next();
            Layout layout = generateLayout(pageGroup, moduleName, sPage);
            if (layout != null) {
                layouts.put(layout.getName(), layout);
            }
        }
        return layouts;
    }

    private Layout generateLayout(PageGroup pageGroup, String moduleName,
                                  SecondaryPage secondaryPage) {
        Layout layout = new Layout();
        layout.setName(secondaryPage.getName());
        if (secondaryPage.getTitle() != null) {
            Put putTitle = new Put();
            putTitle.setName("title");
            putTitle.setValue(secondaryPage.getTitle());
            layout.addPut(putTitle);
        }

        boolean isTeaser = secondaryPage.getTeaser() != null;
        if (isTeaser) {
            if (pageGroup != null) {
                layout.setExtendsLayout(pageGroup.getMasterLayout());
                layout.setFileBlank(true);
                PutItem putBody = new PutItem();
                putBody.setName(pageGroup.getTileName());
                putBody.setModule(moduleName);
                putBody.setInstance("default");
                putBody.setTeaser(secondaryPage.getTeaser());

                layout.addPutItem(putBody);
            } else {
                return null;
            }
        }
        else {
            String pageName = secondaryPage.getPage() == null ?
                secondaryPage.getName() : secondaryPage.getPage();
            if (pageGroup == null) {
                layout.setFileBlank(false);
                layout.setFile("module/" + moduleName + "/" + pageName + ".jsp");
            }
            else {
                layout.setExtendsLayout(pageGroup.getMasterLayout());
                layout.setFileBlank(true);
                PutItem putBody = new PutItem();
                putBody.setName(pageGroup.getTileName());
                putBody.setModule(moduleName);
                putBody.setPage(pageName);

                layout.addPutItem(putBody);
            }
        }

        return layout;
    }

    protected void expandModulesFromRepository(SiteConfig siteConfig) {
        if (siteConfig.getModuleLayout() == null) {
            siteConfig.setModuleLayout(new ModuleLayout());
        }
        HashMap modules = siteConfig.getModuleLayout().getModule();
        Iterator repositoryIter = repository.entrySet().iterator();
        while (repositoryIter.hasNext()) {
            Map.Entry entry = (Map.Entry) repositoryIter.next();

            RepositoryLayout components = (RepositoryLayout) entry.getValue();
            String moduleName = (String) entry.getKey();
            Module repositoryModule = components.getModule();

            if (repositoryModule != null) {
                Module siteConfModule = (Module) modules.get(moduleName);
                if (siteConfModule == null) {
                    Module newModule = new Module();
                    newModule.setName(moduleName);
                    newModule.setTeaserFile(repositoryModule.getTeaserFile());

                    Iterator reposModIter = repositoryModule.getTeaser().values().
                        iterator();
                    while (reposModIter.hasNext()) {
                        Teaser item = (Teaser) reposModIter.next();
                        newModule.addTeaser(item);
                    }

                    reposModIter = repositoryModule.getPage().values().iterator();
                    while (reposModIter.hasNext()) {
                        Page item = (Page) reposModIter.next();
                        newModule.addPage(item);
                    }

                    siteConfig.getModuleLayout().addModule(newModule);
                }
                else {
                    siteConfModule.merge(repositoryModule);
                }
            }

        }
    }

    protected String findExistingFile(String path, String folderName,
                                      boolean isTemplate,
                                      String parentTemplateName,
                                      String groupType,
                                      String groupName) throws
        ViewConfigException {
        logger.debug("findExistingFile(): " +
                     path + "," + isTemplate + "," + parentTemplateName + "," +
                     groupType + "," + groupName
                     );

        if (path.startsWith("/")) {
            File file = new File(servletContext.getRealPath(path));
            if (file.exists()) {
                return path;
            }
            else {
                throw new ViewConfigException("Unable to open file " + path +
                                              " with absolute path " +
                                              file.getAbsolutePath());
            }
        }
        // Workaround for images
        if ( ( (groupType == null || groupType.trim().length() == 0) ||
              groupType.equals(LAYOUT_DIR)) &&
            path.startsWith("module/")) {
            logger.debug("normalizing call of findExistingFile()");
            String[] parts = DgUtil.fastSplit(path, '/');
            if (parts.length > 2) {
                StringBuffer newPath = new StringBuffer(path.length());
                //String newPath = "";
                for (int i = 2; i < parts.length; i++) {
                    if (i > 2) {
                        newPath.append("/");
                    }
                    newPath.append(parts[i]);
                }

                return findExistingFile(newPath.toString(), folderName,
                                        isTemplate,
                                        parentTemplateName, MODULE_DIR, parts[1]);
            }
        }

        // Assemble path
        String fileName = expandFilePath(path, folderName, isTemplate,
                                         groupType,
                                         groupName);
        File file = new File(servletContext.getRealPath(fileName));

        if (!file.exists()) {
            if (isTemplate || parentTemplateName == null) {
                // Find in the repository
                if (groupType.equals(MODULE_DIR)) {
                    String groupDir = groupName == null ? "" : "/" + groupName;

                    fileName = "/repository" + groupDir + "/view/" +
                        path;
                    file = new File(servletContext.getRealPath(fileName));

                    if (!file.exists()) {

                        throw new ViewConfigException(
                            "Unable to open file in the repository " +
                            fileName +
                            " with absolute path " +
                            file.getAbsolutePath());
                    }
                }
                else {
                    throw new ViewConfigException("Unable to open file " +
                                                  fileName +
                                                  " with absolute path " +
                                                  file.getAbsolutePath());
                }
            }
            else {
                fileName = findExistingFile(path,
                                            parentTemplateName, true, null,
                                            groupType, groupName);

            }
        }
        logger.debug("findExistingFile() returns: " + fileName);

        return fileName;
    }

}
