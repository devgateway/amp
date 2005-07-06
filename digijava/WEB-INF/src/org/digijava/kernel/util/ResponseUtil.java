/*
 *   RequestUtils.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: April 21, 2004
 * 	 CVS-ID: $Id: ResponseUtil.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * General servlet response utility in Digi framework.
 */
public class ResponseUtil {


    /**
     * Write the specified file as the response
     *
     * @param response
     * @param contentType file content type
     * @param fileName file name
     * @param data file data
     * @throws IOException if an input/output error
     */
    public static void writeFile(HttpServletResponse response,
                                 String contentType, String fileName, byte data[]) throws
        IOException {

            if (response == null) {
                throw new IllegalArgumentException(
                    "response parameter must be not-null");
            }

            if (data == null) {
                throw new IllegalArgumentException(
                    "data parameter must be not-null");
            }

            ServletOutputStream output = response.getOutputStream();
            if (contentType != null && contentType.length() > 0)
                response.setContentType(contentType);
            if (fileName != null && fileName.length() > 0)
                response.setHeader("Content-Disposition",
                                   "attachment; filename=" + fileName);
            output.write(data);
            output.flush();
    }

}