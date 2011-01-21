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

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * General servlet response utility in Digi framework.
 */
public class ResponseUtil {


    /**
     * Write the specified file as the response
     *
     * @param response HttpServletResponse
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
