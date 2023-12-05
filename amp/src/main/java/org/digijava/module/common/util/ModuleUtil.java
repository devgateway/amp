/*
 *   ModuleUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created: Oct 20, 2003
 *   CVS-ID: $Id$
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
package org.digijava.module.common.util;

import java.util.StringTokenizer;

public class ModuleUtil {

    public static final String ESCAPE_CHARACTER = "\\";

    public ModuleUtil() {
    }

    /**
     * Truncates words with more then "maxLength" characters
     *
     * @param words
     * @param maxLength
     * @return
     */
    public static String truncateWords(String words, int maxLength) {

    if (words == null)
        return null;

    String title = new String();

    if (words.length() > maxLength) {
        StringTokenizer tokenizer = new StringTokenizer(words);
        while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        String pTitle = title + token;

        if (pTitle.length() > maxLength) {
            break;
        }
        title += token + " ";
        }
        if (title.trim().length() == 0) {
        title = words.substring(0, maxLength - 1);
        }
        title = title.trim() + "...";
    }
    else {
        title = words;
    }

    return title;
    }

    /**
     * extractShortDescription
     * @deprecated use extractShortDescription(String description, String
     * delimiter) instead
     * @param description String
     * @return String
     */
    public static String extractShortDescription(String description) {
    return description;
    }

    /**
     * parseDescription
     * @deprecated use parseDescription(String description, String delimiter)
     * instead
     * @param description String
     * @return String
     */
    public static String parseDescription(String description) {
    return description;
    }

    /**
     * extracts short version of the description: as a  short version is taken sub part of the whole description text
     * until the first occurance of delimiter character
     * @param description - description from which the short version should be exctracted
     * @param delimiter - delimiter symbol
     * @return extracted - short version
     */
    public static String extractShortDescription(String description,
                         String delimiter) {
    String result = new String("");

    if (delimiter != null && delimiter.trim().length() != 0) {
        delimiter = delimiter.trim();
        StringTokenizer descriptionTokenizer = new StringTokenizer(
          description,
          delimiter, true);

        if (descriptionTokenizer.hasMoreTokens()) {

        boolean which = false;
        while (descriptionTokenizer.hasMoreTokens()) {
            String token = descriptionTokenizer.nextToken();

            if (delimiter.startsWith(token)) {
            int l = 1;
            while (descriptionTokenizer.hasMoreTokens() &&
                   l < delimiter.length()) {
                token += descriptionTokenizer.nextToken();
                if (token.equals(delimiter)) {
                break;
                }
                ++l;
            }
            }

            if (token.equals(delimiter) && !which) {
            break;
            }
            which = false;

            result += token;
            if (token.endsWith(ESCAPE_CHARACTER)) {
            which = true;
            }
        }
        }
        return parseDescription(result, delimiter);
    }
    else {
        return description;
    }
    }

    /**
     * removes delimiter symbol from the description
     * @param description - description from which the delimiter schould be removed
     * @param delimiter - delimiter symbol
     * @return description containing text without delimiters
     */
    public static String removeDelimiters(String description,
                      String delimiter) {
    String result = new String("");

    if (delimiter != null && delimiter.trim().length() != 0) {
        delimiter = delimiter.trim();
        StringTokenizer descriptionTokenizer = new StringTokenizer(
          description,
          delimiter, true);

        if (descriptionTokenizer.hasMoreTokens()) {

        boolean which = false;
        while (descriptionTokenizer.hasMoreTokens()) {
            String token = descriptionTokenizer.nextToken();

            if (delimiter.startsWith(token)) {
            int l = 1;
            while (descriptionTokenizer.hasMoreTokens() &&
                   l < delimiter.length()) {
                token += descriptionTokenizer.nextToken();
                if (token.equals(delimiter)) {
                break;
                }
                ++l;
            }
            }
            if (!token.equals(delimiter) || which) {
            result += token;
            which = false;
            }
            if (token.endsWith(ESCAPE_CHARACTER)) {
            which = true;
            }

        }
        }
        return parseDescription(result, delimiter);
    }
    else {
        return description;
    }
    }

    /**
     * parses description - the escape character to delimiter character is displayed
     * as a delimiter character after parsing
     * @param description - description which should be parsed
     * @param delimiter - delimiter symbol
     * @return - parsed description
     */
    public static String parseDescription(String description, String delimiter) {
    String result = new String("");

    if (delimiter != null && delimiter.trim().length() != 0) {
        delimiter = delimiter.trim();
        StringTokenizer descriptionTokenizer = new StringTokenizer(
          description,
          delimiter, true);
        while (descriptionTokenizer.hasMoreTokens()) {
        String token = descriptionTokenizer.nextToken();

        if (token.endsWith(ESCAPE_CHARACTER)) {
            String tokenDelimiter = new String("");
            boolean isDelimiter = false;

            if (descriptionTokenizer.hasMoreTokens()) {
            while (descriptionTokenizer.hasMoreTokens()) {
                tokenDelimiter += descriptionTokenizer.nextToken();
                if (tokenDelimiter.equals(delimiter)) {
                isDelimiter = true;
                break;
                }
            }
            if (isDelimiter) {
                result += token.substring(0, token.length() - 1) +
                  delimiter;
            }
            else {
                result += token + tokenDelimiter;
            }
            }
            else {
            result += token;
            }
        }
        else {
            result += token;
        }
        }

        return result;
    }
    else {
        return description;
    }

    }

    /**
     * Truncates lines to the maxLength specified. adds a <br> after the length
     *
     * @param words
     * @param maxLength
     * @return
     */

    public static String truncateLines(String words, int maxLength) {

    if (words == null)
        return null;

    StringBuffer title = new StringBuffer();
    boolean first = true;
    if (words.length() > maxLength) {

        for (int i = 0; i < words.length(); i++) {

        if (title.toString().length() <= maxLength) {
            title.append(words.charAt(i));
        }
        else {
            if (first) {

            String rev = title.reverse().toString();
            rev = rev.replaceFirst(" ", ">rb<");
            title = (new StringBuffer(rev)).reverse();
            first = false;
            }
            title.append(words.charAt(i));
        }
        }

    }
    else {
        title = new StringBuffer(words);
    }

    return title.toString();
    }

}
