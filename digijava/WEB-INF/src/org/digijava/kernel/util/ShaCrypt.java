/*
 *   ShaCrypt.java
 * 	 @Author Irakli Nadareishvili inadareishvili@worldbank.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: ShaCrypt.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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