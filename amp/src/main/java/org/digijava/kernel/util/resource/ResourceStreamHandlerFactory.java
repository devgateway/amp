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

package org.digijava.kernel.util.resource;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.net.URL;
import java.net.MalformedURLException;

public class ResourceStreamHandlerFactory
    implements URLStreamHandlerFactory {

    private static final String protocolPathProp = "java.protocol.handler.pkgs";

    /**
     * Creates a new <code>URLStreamHandler</code> instance with the
     * specified protocol.
     *
     * @param protocol the protocol ("<code>ftp</code>",
     *   "<code>http</code>", "<code>nntp</code>", etc.).
     * @return a <code>URLStreamHandler</code> for the specific protocol.
     */
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol.equals("resource")) {
            return new Handler();
        }
        else {
            return null;
        }
    }

    public static void installIfNeeded() {
        try {
            URL url = new URL("resource:dummy");
        }
        catch (MalformedURLException e) {
            String packagePrefixList = System.getProperty(protocolPathProp);

            if (packagePrefixList == null) {
                packagePrefixList = "";
            } else {
                if (packagePrefixList != "") {
                    packagePrefixList += "|";
                }
            }
            packagePrefixList += "org.digijava.kernel.util";

            System.setProperty(protocolPathProp, packagePrefixList);
            //URL.setURLStreamHandlerFactory(new ResourceStreamHandlerFactory());
        }
    }
}
