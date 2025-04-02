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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ShaCrypt {

    /**
     * Encrypt data string using SHA1 and return string digit
     *
     * @param data
     * @return
     */
    public static String crypt(String data) {

        MessageDigest md = null;
        StringBuffer buf = null;

        try {
            md = MessageDigest.getInstance("SHA");

            md.update(data.getBytes());
            byte[] digest = md.digest();

            buf = new StringBuffer();

            for (int i = 0; i < digest.length; i++) {
                buf.append( (Character.forDigit( (digest[i] & 0xF0) >> 4, 16)));
                buf.append( (Character.forDigit( (digest[i] & 0xF), 16)));
            }
        }
        catch (NoSuchAlgorithmException ex) {
            System.err.println("ShaCrypt: " + ex.getMessage());
        }

        return buf.toString();
    }
}
