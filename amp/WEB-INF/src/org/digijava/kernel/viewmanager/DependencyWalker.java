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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DependencyWalker {
    Map files;

    public DependencyWalker() {
        files = Collections.synchronizedMap( new HashMap());
    }

    public void clear() {
        files.clear();
    }

    public void addFile(File file) {
        files.put(file.getAbsolutePath(), new Long(file.lastModified()));
    }

    public void addFile(String fileName) {
        File file = new File(fileName);
        addFile(file);
    }

    public Collection getFiles() {
        ArrayList result = new ArrayList();

        Iterator iter = files.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry)iter.next();
            result.add(new File((String)item.getKey()));
        }

        return result;
    }

    public boolean isUpToDate() {
        Iterator iter = files.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry)iter.next();
            File file = new File((String)item.getKey());
            Long timestamp = (Long)item.getValue();

            if (file.lastModified() != timestamp.longValue()) {
                return false;
            }
        }

        return true;
    }
}
