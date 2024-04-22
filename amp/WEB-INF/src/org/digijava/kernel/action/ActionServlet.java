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

package org.digijava.kernel.action;

import org.digijava.kernel.exception.DgException;

import java.io.File;

public class ActionServlet extends org.apache.struts.action.ActionServlet {


    private static final String STRUTS_CONFIG_FILE = "struts-config.xml";
    private static final String MODULE_DIRECTORY = "/repository";

    public void init() {

        try {

            String configFiles = getConfigFiles();

            if (!configFiles.trim().isEmpty()) {
                super.config = configFiles;
            }

            super.init();

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private String getConfigFiles() throws DgException {

        File configDir = new File(getServletContext().getRealPath(MODULE_DIRECTORY));
        File tmpFile;
        StringBuilder retVal = new StringBuilder();

        if (!configDir.exists() || !configDir.isDirectory()) {
            throw new DgException("Configuration directory " + MODULE_DIRECTORY + " does not exist or is not directory");
        }

        String[] dirList = configDir.list();

        tmpFile = new File(configDir.getAbsolutePath() +
                File.separator + STRUTS_CONFIG_FILE);

        if (tmpFile.exists() && tmpFile.isFile()) {
            retVal.append(MODULE_DIRECTORY);
            retVal.append("/");
            retVal.append(tmpFile.getName());
        }

        for (String s : dirList) {

            tmpFile = new File(configDir.getAbsolutePath() +
                    File.separator + s +
                    File.separator + STRUTS_CONFIG_FILE);

            if (tmpFile.exists() && tmpFile.isFile()) {

                retVal.append(",");
                retVal.append(MODULE_DIRECTORY);
                retVal.append("/");
                retVal.append(s);
                retVal.append("/");
                retVal.append(tmpFile.getName());

            }

        }

        return retVal.toString();

    }

}
