/*
 *   BBCodeParser.java
 *   @Author Maka Kharalashvili maka@digijava.org
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

import org.apache.log4j.Logger;
import org.digijava.kernel.taglib.util.TagUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.common.exception.BBCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class BBCodeParser {

    private static Logger logger = Logger.getLogger(BBCodeParser.class);
    private static String tagPrefix = "digi:";

    /**
     * transforms XML to XHTML
     * @param xmlText-XML to transform
     * @return XHTML
     * @throws BBCodeException
     */
    private static String transformXML(String xmlText) throws BBCodeException {

    TransformerFactory tFactory = null;
    Transformer transformer = null;

    Reader sourceXMLStream = new StringReader(xmlText);
    InputStream sourceXSLStream = BBCodeParser.class.getClassLoader().
          getResourceAsStream("org/digijava/module/common/util/BBCode.xsl");

    StreamResult result = new StreamResult();
    result.setWriter(new StringWriter());

    try {
        tFactory = TransformerFactory.newInstance();
        transformer = tFactory.newTransformer(new StreamSource(
          sourceXSLStream));
        transformer.transform(new StreamSource(sourceXMLStream), result);
    }
    catch (Exception ex) {
        logger.debug("Unable to transform XML", ex);
        throw new BBCodeException(
          "Unable to transform XML", ex);
    }

    return result.getWriter().toString();

    }

    /**
     * parses charachters &,<,>,',", new line charachter  and smiles
     * @param text - text to parse
     * @param smiles
     * @param request
     * @return
     * @throws BBCodeException
     */
    private static String parseText(String text, Properties smiles,
                    HttpServletRequest request) throws
      BBCodeException {

    text = text.replaceAll("&", "&amp;");
    text = text.replaceAll("<", "&lt;");
    text = text.replaceAll(">", "&gt;");
    text = text.replaceAll("\'", "&apos;");
    text = text.replaceAll("\"", "&quot;");

    text = text.replaceAll("(\n\r)", "<br />");
    text = text.replaceAll("[\n\r]", "<br />");

    if (smiles != null) { //parse smiles

        String smileTag = new String("");
        String regex =
          "(.*(:-?D).*)|(.*(:-?\\)).*)|(.*(:-?\\().*)|(.*(:oops:).*)|(.*(:-?o).*)|(.*(:shock:).*)|(.*(:\\?:).*)|(.*(:-?\\?).*)|" +
          "(.*(8-?\\)).*)|(.*(:lol:).*)|(.*(:-?x).*)|(.*(:-?P).*)|(.*(:cry:).*)|(.*(:evil:).*)|" +
          "(.*(:twisted:).*)|(.*(:roll:).*)|(.*(:wink:).*)|(.*(:!:).*)|(.*(:idea:).*)|(.*(:arrow:).*)";

//            if (text.matches(regex)) {
        StringTokenizer tokenizer = new StringTokenizer(regex, "|");
        while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        token = token.substring(1, (token.length() - 1));

        if (Pattern.matches(token, text)) {

            smileTag = "<" + tagPrefix + "smile>" +
              TagUtil.calculateURL("module/common/images/smiles/" +
                           smiles.getProperty(token), true,
                           request) + "</" + tagPrefix +
              "smile>";

            token = token.substring(2, (token.length() - 2));
            text = text.replaceAll(token, smileTag);
        }
        }

//            }
    }

    return text;
    }

    /**
     * checks whether tag is safe HTML tag
     * @param tag
     * @return - true is tag is safe, false otherwise
     */
    private static boolean isTagSafe(String tag) {

    String safeHTML = DgUtil.getParamSafehtml();
    StringTokenizer tokenizer = new StringTokenizer(safeHTML, ",");

    boolean isSafe = false;
    while (tokenizer.hasMoreTokens() && !isSafe) {
        String token = tokenizer.nextToken();

        if (tag.regionMatches(0, token, 0, token.length())) {
isSafe = true;
        }
    }
    return isSafe;
    }

    /**
     * checks whether tag is BB tag
     * @param tag
     * @return - true is tag BB tag, false otherwise
     */
    private static boolean isTagBB(String tag) {

    String bbTag = DgUtil.getParamBbTag();
    StringTokenizer tokenizer = new StringTokenizer(bbTag, ",");

    boolean isBbTag = false;
    while (tokenizer.hasMoreTokens() && !isBbTag) {
        String token = tokenizer.nextToken();

        if (tag.equals(token)) {
        isBbTag = true;
        }
    }
    return isBbTag;
    }

    /**
     * Returns parsed attribute like param="property"
     * @param attribute attribute String to parse
     * @return parsed attribute
     */

    private static String parseAttribute(String attribute) {

    String parsed = new String();
    String param = " param";

    StringTokenizer tokenizer = new StringTokenizer(attribute, "=");
    while (tokenizer.hasMoreTokens()) {
        parsed = /*" " +*/ tagPrefix + tokenizer.nextToken() + param + "=";

        if (tokenizer.hasMoreTokens()) {
        String property = tokenizer.nextToken();
        if (property.charAt(0) != '\"') {
            parsed += "\"" + property + "\"";
        }
        else {
            parsed += property;
        }
        }
        else {
        parsed += "\"default\"";
        }
    }

    return parsed;
    }

    /**
     * parses BBCode to XML
     * @param textToParse - BBText to parse
     * @param smiles
     * @param enableHTML
     * @param request
     * @return - XML
     * @throws BBCodeException
     * @throws IOException
     */
    private static String parseBbToXML(String textToParse, Properties smiles,
                       boolean enableHTML,
                       HttpServletRequest request) throws
      BBCodeException, IOException {

    String realTextToParse = textToParse;

    logger.debug("PARSING BBCODE:" + textToParse);

    String xmlText = new String();
    boolean error = false;

    ArrayList bbCode = new ArrayList();

    int indexStart = 0;
    int indexEnd = 0;
    String currentTag = new String("");
    String currentText = new String("");

///----
    while (true) {
        indexStart = textToParse.indexOf('[', indexStart);

        if ( (indexStart < 0) && (indexEnd >= 0)) {
        bbCode.add(parseText(textToParse.substring(indexEnd), smiles,
                     request));
        break;
        }
        if (indexEnd > indexStart) {
        error = true;
        break;
        }

        //extract peace of Text
        currentText = textToParse.substring(indexEnd, indexStart);
        if (currentText.trim().length() != 0) {
        currentText = parseText(currentText, smiles, request);
        bbCode.add(currentText);

        logger.debug("BB_TEXT:" + currentText);
        }

        //extract Tag
        indexEnd = textToParse.indexOf(']', indexEnd) + 1;
        if ( (indexStart >= 0) && (indexEnd <= 0)) {
        error = true;
        break;
        }
        while (indexEnd < indexStart) {
//                bbCode.add(parseText(textToParse.substring(indexEnd,textToParse.indexOf('[', indexEnd)),smiles,request));
        indexEnd = textToParse.indexOf(']', indexEnd) + 1;
        }

        currentTag = textToParse.substring( (indexStart + 1), (indexEnd - 1)).
          trim();
        currentTag = currentTag.toLowerCase();

        logger.debug("BB_TAG:" + currentTag);

        if ( (currentTag.length() != 0)) {
        while (currentTag.indexOf("[") >= 0) {
            bbCode.add(parseText(textToParse.substring(indexStart,
              (indexStart + currentTag.indexOf("[") + 1)), smiles,
                     request));

            indexStart += currentTag.indexOf('[') + 1;
            currentTag = currentTag.substring(currentTag.indexOf('[') +
              1).trim();

            logger.debug("BB_TAG:" + currentTag);
        }

        //
        if (currentTag.indexOf("/") >= 0) { //some close tag reached
            String openTag = currentTag.substring(1);

            if (isTagBB(openTag)) {

            int Index = -1;
            for (int i = (bbCode.size() - 1); i >= 0; i--) {
                String item = (String) bbCode.get(i);

                StringTokenizer tokenizer = new StringTokenizer(
                  item);
                if (tokenizer.hasMoreTokens()) {
                item = tokenizer.nextToken();

                if (item.regionMatches(1, openTag, 0,
                      openTag.length())) {
                    Index = i;
                    break;
                }
                }
            }

            if (Index >= 0) { //open tag found

                String item = (String) bbCode.get(Index);
                item = item.substring(1, (item.length() - 1));
                if (item.indexOf('=') >= 0) {
item = parseAttribute(item);
                bbCode.
                      set(Index, "<" + item + ">");
                }
                else {
                bbCode.
                      set(Index, "<" + tagPrefix + item + ">");
                }

                bbCode.add("</" + tagPrefix + openTag + ">");
            }
            else {
                bbCode.add("[" +
                       parseText(currentTag, smiles, request) +
                       "]");
            }

            }
            else {
            bbCode.add("[" + currentTag.trim() + "]");
            }
        }
        else {

            bbCode.add("[" + currentTag.trim() + "]");
        }
        }
        else {
        bbCode.add("[]");
        }

        ++indexStart;
    }
    //----------
    Iterator iterator = bbCode.iterator();

    boolean listOpen = false;
    int countStar = -1;

    while (iterator.hasNext()) {
        String item = (String) iterator.next();
        if (item.matches("<" + tagPrefix + "list.*>")) {
        listOpen = true;
        }
        if (item.matches("</" + tagPrefix + "list>")) {
        listOpen = false;
        if (countStar >= 0) {
            countStar = -1;
            item = "</" + tagPrefix + "li>" + item;
        }
        }
        if (item.matches("\\[" + "\\*\\]")) {
        if (listOpen) {
            ++countStar;
            if (countStar == 0) {
            item = "<" + tagPrefix + "li>";
            }
            else {
            item = "</" + tagPrefix + "li><" + tagPrefix + "li>";
            }
        }
        }
//            }
        xmlText += item;
    }

    if (error) {
        xmlText = realTextToParse;
    }

    logger.debug("RECIEVED XML:" + xmlText);

    return xmlText;
    }

    public static String parse(String textToParse, boolean enableSmiles,
                   boolean enableHTML, HttpServletRequest request) throws
      BBCodeException, IOException {

    String xmlText = new String();

    Properties smiles = null;
    if (enableSmiles) {
        InputStream smilesIS = BBCodeParser.class.getClassLoader().
          getResourceAsStream(
          "org/digijava/module/common/smile.properties");
        if (smilesIS != null) {
        smiles = new Properties();
        smiles.load(smilesIS);
        }
    }

    if (enableHTML &&
        ( (textToParse.indexOf('<') >= 0) || (textToParse.indexOf('>') >= 0))) {

        logger.debug("PARSING HTML:" + textToParse);

        ArrayList bbCode = new ArrayList();

        int indexStart = 0;
        int indexEnd = 0;
        String currentTag = new String("");
        String currentText = new String("");

///----
        while (true) {

        indexStart = textToParse.indexOf('<', indexStart);

        if ( (indexStart < 0) && (indexEnd >= 0)) {
            if (textToParse.substring(indexEnd).trim().length() != 0) {
            bbCode.add(parseBbToXML(textToParse.substring(indexEnd),
                        smiles, true, request));
            }
            break;
        }
        if (indexEnd > indexStart) {
            break;
        }

        //extract piece of Text
        currentText = textToParse.substring(indexEnd, indexStart);
        if (currentText.trim().length() != 0) {
            currentText = parseBbToXML(currentText, smiles, true,
                           request);
            bbCode.add(currentText);

            logger.debug("HTML_TEXT:" + currentText);
        }

        //extract Tag
        indexEnd = textToParse.indexOf('>', indexEnd) + 1;
        if ( (indexStart >= 0) && (indexEnd < 0)) {
            break;
        }
        while (indexEnd < indexStart) {
            indexEnd = textToParse.indexOf('>', indexEnd) + 1;
        }

        currentTag = textToParse.substring( (indexStart + 1),
              (indexEnd - 1)).trim();
        currentTag = currentTag.toLowerCase();

        logger.debug("HTML_TAG:" + currentTag);

        if ( (currentTag.length() != 0)) {
            while (currentTag.indexOf('<') >= 0) {
            bbCode.add(parseBbToXML(textToParse.substring(
                  indexStart,
                  (indexStart + currentTag.indexOf("<") + 1)),
                        smiles, true,
                        request));

            indexStart += currentTag.indexOf('<') + 1;
            currentTag = currentTag.substring(currentTag.indexOf(
                  '<') + 1).trim();

            logger.debug("HTML_TAG:" + currentTag);
            }

            if (currentTag.indexOf("/") >= 0) { //possibly some closetag reached
            String openTag = currentTag.substring(1);
            if (isTagSafe(openTag)) {

                int Index = -1;
                for (int i = (bbCode.size() - 1); i >= 0; i--) {
                String item = (String) bbCode.get(i);

                StringTokenizer tokenizer = new StringTokenizer(
                      item);
                if (tokenizer.hasMoreTokens()) {
                    item = tokenizer.nextToken();
                }

                if (item.regionMatches(1, openTag, 0,
                      openTag.length())) {
                    Index = i;
                    break;
                }
                }

                if (Index < 0) { //open tag not found
                bbCode.add(parseText("<" + currentTag + ">",
                      smiles, request));
                }
                else {
                bbCode.add("<" + currentTag + ">");
                }
            }
            else {
                int eqInd = currentTag.indexOf('=');
                if (eqInd >= 0 &&
                (isTagSafe(currentTag.substring(0,
                  currentTag.indexOf(" "))) ||
                 isTagSafe(currentTag.substring(0, eqInd)))) {

                bbCode.add("<" + currentTag + ">");
                }
                else {
                bbCode.add(parseBbToXML("<" + currentTag + ">",
                      smiles, true, request));
                }
            }
            }
            else {
            if (isTagSafe(currentTag)) {
                bbCode.add("<" + currentTag + ">");
            }
            else {
                bbCode.add(parseBbToXML("<" + currentTag + ">",
                  smiles, true, request));
            }
            }

        }
        else {
            bbCode.add("&lt;&gt");
        }
        ++indexStart;
        }
        //----------
        ArrayList correctCode = new ArrayList();

        for (int k = 0; k < bbCode.size(); k++) {
        String itemA = (String) bbCode.get(k);
        correctCode.add(itemA);

        if ( (itemA.indexOf('/') < 0) &&
            ( (itemA.indexOf('>') >= 0) || (itemA.indexOf('<') >= 0))) {
            String tag = new String("");
            StringTokenizer tokenizer = new StringTokenizer(
              itemA);
            if (tokenizer.hasMoreTokens()) {
            tag = tokenizer.nextToken();
            }
            if (tag.endsWith(">")) {
            tag = tag.substring(1, tag.length() - 1);
            }
            else {
            tag = tag.substring(1);
            }

            int Index = -1;
            for (int i = (bbCode.size() - 1); i >= k; i--) {
            String itemB = (String) bbCode.get(i);

            if (itemB.trim().equals("</" + tag + ">")) {
                Index = i;
                break;
            }
            }
            if (Index < 0) {
            //close open tag
            correctCode.add("</" + tag + ">");
            }
        }
        }
        //----------
        Iterator iterator = correctCode.iterator();
        while (iterator.hasNext()) {
        String item = (String) iterator.next();
        xmlText += item;
        }
    }
    else {
        xmlText = parseBbToXML(textToParse, smiles, enableHTML, request);
    }

    logger.debug("RECIEVED XML:" + xmlText);

    logger.debug("XML:" + xmlText);
    if (!xmlText.equals(textToParse)) {

        xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<BBText xmlns:digi=\"http://www.digijava.org/XML/BBCode\">" +
          xmlText + "</BBText>";

        return transformXML(xmlText);
    }
    else {
        return xmlText;
    }
    }
}
