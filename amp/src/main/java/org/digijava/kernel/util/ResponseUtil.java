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

import org.springframework.util.FileCopyUtils;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    public static void writeFile(HttpServletRequest request, HttpServletResponse response,
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
        if (contentType != null && contentType.length() > 0) {
            response.setContentType(contentType);
        }
        if (fileName != null && fileName.length() > 0) {
            response.setHeader("Content-Disposition", encodeContentDispositionForDownload(request, fileName));
        }

        output.write(data);
        output.flush();
    }

    public static void writeFile(HttpServletRequest request, HttpServletResponse response,
                                 String contentType, String fileName, InputStream iStream) throws
            IOException {

        if (response == null) {
            throw new IllegalArgumentException(
                    "response parameter must be not-null");
        }

        if (iStream == null) {
            throw new IllegalArgumentException(
                    "data parameter must be not-null");
        }

        ServletOutputStream output = response.getOutputStream();
        if (contentType != null && contentType.length() > 0)
            response.setContentType(contentType);
        if (fileName != null && fileName.length() > 0)
            response.setHeader("Content-Disposition", ResponseUtil.encodeContentDispositionForDownload(request, fileName));
        FileCopyUtils.copy(iStream, output);
    }


    public static String encodeContentDispositionForDownload(HttpServletRequest request,
                                                             String unencodedFileName) {
        return encodeContentDispositionForDownload(request, unencodedFileName, false);
    }

    /**
     * Encodes the content disposition header in case the it contains non-ascii symbols, spaces, commas,
     * special characters etc
     * @param request the Http request
     * @param unencodedFileName the name before encoding
     * @param isInline - flag indicating if the file should be saved as an attachment
     * @return
     */
    public static String encodeContentDispositionForDownload(HttpServletRequest request,
                                                             String unencodedFileName,
                                                             boolean isInline) {
        if (unencodedFileName == null)
            throw new IllegalArgumentException("Value of the \"filename\" parameter cannot be null!");
        String contentDisposition = isInline ? "inline; " : "attachment; ";

        String agent = request.getHeader("USER-AGENT");

        if (agent == null) {
            return isInline ? "inline; " : "attachment; " + "filename=\"" + unencodedFileName + "\"";
        } else {
            agent = agent.toLowerCase();
            try {
                if (!agent.contains("opera") && agent.contains("msie")) {
                    // IE
                    contentDisposition += "filename=" + toHexString(unencodedFileName, true);
                } else if (agent.contains("chrome")) {
                    contentDisposition += "filename=\"" + URLEncoder.encode(unencodedFileName, "UTF-8").replace('+', ' ') + "\"";
                } else if (agent.contains("firefox")) {
                    // Firefox and others
                    contentDisposition += "filename=\"" + MimeUtility.encodeText(unencodedFileName, "utf8", "B") + "\"";
                } else {
                    // this still might be IE. The latest IE versions hide the agent string
                    contentDisposition += "filename=" + toHexString(unencodedFileName, true);
                }
            } catch (UnsupportedEncodingException e) {
                // return filename unencoded
                return isInline ? "inline; " : "attachment; " + "filename=\"" + unencodedFileName + "\"";

            }
        }
        return contentDisposition;
    }


    public static String toHexString(String s, boolean isIE) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (isIE) {
            // if has russian letters in extension
            boolean hasNONAsciiLettersInExtension = false;
            int dotPosition = s.lastIndexOf('.');
            for (int i = dotPosition; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c > 255) {
                    hasNONAsciiLettersInExtension = true;
                }
            }

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (!hasNONAsciiLettersInExtension && i >= dotPosition) {
                    // just do NOT encode extension if it does NOT have NON ASCII symbols
                    sb.append(c);
                } else {
                    sb.append(encodeChar(c));
                }

            }

        } else {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c >= 0 && c <= 255 && !Character.isWhitespace(c)) {
                    sb.append(c);
                } else {
                    sb.append(encodeChar(c));
                }
            }

        }
        return sb.toString();
    }


    private static CharSequence encodeChar(char c) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        byte[] b;
        b = Character.toString(c).getBytes("utf8");
        for (byte aB : b) {
            int k = aB;
            if (k < 0)
                k += 256;
            sb.append("%").append(Integer.toHexString(k).toUpperCase());
        }
        return sb;
    }

}
