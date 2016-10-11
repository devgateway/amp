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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.digijava.kernel.service.ServiceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;

public class StandaloneServiceContext
    implements ServiceContext {

    private File rootDir;

    public StandaloneServiceContext(String rootPath) {
        File root = new File(rootPath);
        if (!root.exists() || !root.isDirectory()) {
            throw new IllegalArgumentException("Path " + rootPath + " does not point to directory");
        }
        this.rootDir = root;
        //System.out.println("DummyServletContext: " + root.getAbsolutePath());
    }
    /**
     * getRealPath
     *
     * @param path String
     * @return String
     * @todo Implement this org.digijava.kernel.service.ServiceContext method
     */
    public String getRealPath(String path) {
        if (path.startsWith("/")) {
            return rootDir.getAbsolutePath() + path;
        }
        else {
            return rootDir.getAbsolutePath() + "/" + path;
        }
    }

    /**
     * getResource
     *
     * @param path String
     * @return URL
     * @throws MalformedURLException
     * @todo Implement this org.digijava.kernel.service.ServiceContext method
     */
    public URL getResource(String path) throws MalformedURLException {
        return null;
    }

    /**
     * getResourceAsStream
     *
     * @param path String
     * @return InputStream
     * @todo Implement this org.digijava.kernel.service.ServiceContext method
     */
    public InputStream getResourceAsStream(String path) {
        File file = new File(getRealPath(path));
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        }
        catch (FileNotFoundException ex) {
            return null;
        }
        return is;
    }

    /**
     * getResourcePaths
     *
     * @param path String
     * @return Set
     * @todo Implement this org.digijava.kernel.service.ServiceContext method
     */
    public Set getResourcePaths(String path) {
        return null;
    }
}
