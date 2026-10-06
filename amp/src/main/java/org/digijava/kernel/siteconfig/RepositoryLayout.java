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

package org.digijava.kernel.siteconfig;

import java.util.HashMap;
import java.util.Iterator;

public class RepositoryLayout {
    private HashMap secondaryPages;
    private Module module;

    public RepositoryLayout() {
        secondaryPages = new HashMap();
    }

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<config>").append(newLine);

        buf.append("<secondary-pages>").append(newLine);
        Iterator iter = secondaryPages.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }
        buf.append("</secondary-pages>").append(newLine);
        if (module != null) {
            buf.append(module).append(newLine);
        }
        buf.append("</config");

        return buf.toString();
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public HashMap getSecondaryPages() {
        return secondaryPages;
    }

    public void setSecondaryPages(HashMap secondaryPages) {
        this.secondaryPages = secondaryPages;
    }

    public void addSecondaryPage(SecondaryPage secondaryPage) {
        secondaryPages.put(secondaryPage.getName(), secondaryPage);
    }
}
