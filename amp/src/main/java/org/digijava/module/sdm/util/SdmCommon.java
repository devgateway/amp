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

package org.digijava.module.sdm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.form.SdmForm;
import java.util.StringTokenizer;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SdmCommon {

    private static Logger logger = Logger.getLogger(SdmCommon.class);

    private static String[] tags = {
//        "p", "font", "b", "i", "u","img", null};
        "p", "font", "b", "i", "u","img", "span", null}; //for span

    /**
     *
     * @param paragraph
     * @return
     */
    public static SdmParagraph createParagraph(SdmItem sdmItem) {

        SdmParagraph parag = null;
        if (sdmItem != null) {

            parag = new SdmParagraph();

            // set alignment
            if( sdmItem.getAlignment() != null ) {
                parag.setAlign(sdmItem.getAlignment().toLowerCase());
            }

            // set font face
            if( sdmItem.getFont() != null ) {
                if( !sdmItem.getFont().equalsIgnoreCase("default") ) {
                    parag.setFontFace(sdmItem.getFont());
                }
            }

            // set font size
            if( sdmItem.getFontSize() != null ) {
                if( sdmItem.getFontSize().longValue() != 0 ) {
                    parag.setFontSize(sdmItem.getFontSize().toString());
                }
            }

            // set font color
            if( sdmItem.getFontColor() != null ) {
                parag.setFontColor(sdmItem.getFontColor());
            }

            // set bold
            if( sdmItem.getBold() != null && sdmItem.getBold().equalsIgnoreCase("<b>")) {
                parag.setBold(true);
            } else {
                parag.setBold(false);
            }

            // set italic
            if( sdmItem.getItalic() != null && sdmItem.getItalic().equalsIgnoreCase("<i>")) {
                parag.setItalic(true);
            } else
                parag.setItalic(false);

            // set underline
            if( sdmItem.getUnderline() != null && sdmItem.getUnderline().equalsIgnoreCase("<u>")) {
                parag.setUnderline(true);
            } else
                parag.setUnderline(false);

            parag.setContent(sdmItem.getContentTitle());
        }

        return parag;
    }

    /**
     *
     * @param paragraph
     * @return
     */
    public static String createParagraph(SdmParagraph paragraph) {

        String html = "";

        if (paragraph != null) {

            // generate p tag
            if (paragraph.getAlign() != null) {
                html += "<p align=\"" + paragraph.getAlign() + "\">";
            }


            // generate span tag
            if (paragraph.getFontColor() != null ||
                paragraph.getFontFace() != null ||
                paragraph.getFontSize() != null) {

                html += "<span style=\"";

                if (paragraph.getFontSize() != null) {
                    int fontSize = new Long( paragraph.getFontSize().trim() ).intValue();
                    fontSize = fontSize + 10;
                    html += "font-size:" + fontSize;
                }
                if (paragraph.getFontFace() != null) {
                    html += ";font-family:" + paragraph.getFontFace().trim();
                }

                if (paragraph.getFontColor() != null) {
                    html += ";color:" + paragraph.getFontColor().trim();

                }
                html += "\">";
            }


            // generate bold tag
            if (paragraph.isBold())
                html += "<b>";

                // generate italic tag
            if (paragraph.isItalic())
                html += "<i>";

                // generate underline tag
            if (paragraph.isUnderline())
                html += "<u>";

            if (paragraph.getContent() != null) {
                html += paragraph.getContent();
            }

            // generate underline tag
            if (paragraph.isUnderline())
                html += "</u>";

                // generate italic tag
            if (paragraph.isItalic())
                html += "</i>";

                // generate bold tag
            if (paragraph.isBold())
                html += "</b>";

            if (paragraph.getFontColor() != null ||
                paragraph.getFontFace() != null ||
                paragraph.getFontSize() != null) {
                html += "</span>";
            }  //for span


            if (paragraph.getAlign() != null) {
                html += "</p>";
            }

            logger.debug("Paragraph: " + html);
        }

        return html;
    }


    /**
     *
     * @param sdmParagraph
     * @param sdmItem
     */
    public static void fillParagraph(SdmParagraph sdmParagraph, SdmItem sdmItem) {

        if( sdmParagraph != null ) {

            if (sdmParagraph.getAlign() != null) {
                sdmItem.setAlignment(sdmParagraph.getAlign());
            }

            if (sdmParagraph.getFontFace() != null ) {
                sdmItem.setFont(sdmParagraph.getFontFace());
            }

            if (sdmParagraph.getFontSize() != null ) {
                sdmItem.setFontSize(new Long(sdmParagraph.getFontSize()));
            }

            if (sdmParagraph.getFontColor() != null ) {
                sdmItem.setFontColor(sdmParagraph.getFontColor());
            }

            if (sdmParagraph.isBold()) {
                sdmItem.setBold("<b>");
            } else{
                sdmItem.setBold(null);
            }

            if (sdmParagraph.isItalic()) {
                sdmItem.setItalic("<i>");
            } else {
                sdmItem.setItalic(null);
            }

            if (sdmParagraph.isUnderline()) {
                sdmItem.setUnderline("<u>");
            } else {
                sdmItem.setUnderline(null);
            }
            if (sdmParagraph.getContent() != null) {
                sdmItem.setContentText(sdmParagraph.getContent());
            }
        }

    }


    /**
     *
     * @param parag
     * @param paragraph
     */
    private static void doParseHtml(SdmParagraph parag, String paragraph) {

        paragraph = paragraph.trim();
        int startIndex = paragraph.indexOf("<");
        if (startIndex != -1) {
            int endIndex = paragraph.indexOf(">", startIndex + 1);
            if (endIndex != -1) {
                String tagData = paragraph.substring(startIndex + 1, endIndex).
                    trim();
                String tagName = null;

                HashMap attributes = getAttributes(tagData);

                logger.debug("tagDATA:"+tagData);
                // p tag
                if (tagData.startsWith(tags[0])) {
                    if (attributes != null) {
                        parag.setAlign( (String) attributes.get("align"));
                        tagName = tags[0];
                    }
                }

                // font tag
                if (tagData.startsWith(tags[1])) {
                    if (attributes != null) {
                        parag.setFontFace( (String) attributes.get("face"));
                        parag.setFontColor( (String) attributes.get("color"));
                        parag.setFontSize( (String) attributes.get("size"));
                        tagName = tags[1];
                    }
                }

               // span tag
                if (tagData.startsWith(tags[5])) {
                    if (attributes != null) {
                        String spanBody = (String) attributes.get("style");
                        logger.debug("span Body is:"+spanBody);
                        StringTokenizer tokenizer = new StringTokenizer(spanBody,";");
                        while(tokenizer.hasMoreTokens()) {
                            String token = tokenizer.nextToken();
                            logger.debug("span token:"+token);
                            if (token.startsWith("font-size:") ) {
                               parag.setFontSize(token.substring( ((String)("font-size:")).length(),((String)("font-size:")).length()+1 ));

                               logger.debug("font-size:"+parag.getFontSize());
                            }
                            if (token.startsWith("font-family:") ) {
                               parag.setFontFace(token.substring( ((String)("font-family:")).length(),token.length() ));

                               logger.debug("font-family:"+parag.getFontFace());
                            }
                            if (token.startsWith("color:") ) {
                               parag.setFontColor(token.substring( ((String)("color:")).length(),token.length() ));

                               logger.debug("color:"+parag.getFontColor());
                            }

                        }
                        tagName = tags[1];
                    }
                }

                // bold tag
                if (tagData.startsWith(tags[2])) {
                    parag.setBold(true);
                    tagName = tags[2];
                }

                // italic tag
                if (tagData.startsWith(tags[3])) {
                    parag.setItalic(true);
                    tagName = tags[3];
                }

                // underline tag
                if (tagData.startsWith(tags[4])) {
                    parag.setUnderline(true);
                    tagName = tags[4];
                }

                startIndex = paragraph.indexOf("</" + tagName, endIndex + 1);
                if (startIndex != -1) {
                    doParseHtml(parag,
                                paragraph.substring(endIndex + 1, startIndex));
                }
            }
        }
        else {
            parag.setContent(paragraph);
        }
    }

    /**
     * parse tag attribute values and put Hasmap
     *
     * @param tagData
     * @return
     */
    private static HashMap getAttributes(String tagData) {

        String str = tagData.trim();
        HashMap map = new HashMap();
        int nStart = 0;
        while( true ) {
            int nIndex = str.indexOf("=", nStart);
            if (nIndex != -1) {
                String key = str.substring(nStart, nIndex);
                nIndex = str.indexOf("\"", nIndex);
                if( nIndex != -1 ) {
                    int end = str.indexOf("\"", nIndex + 1);
                    if( end == -1 ) break;
                    String value = str.substring(nIndex + 1, end);
                    map.put(key.trim(),value.trim());
                    nStart = end + 1;
                } else
                    break;
            } else
                break;
        }

        return map.size() == 0 ? null : map;
    }

    //---
    public static Long getMaxParagraphNumber(Set sdmItems) {

        if (sdmItems != null) {
            Long max = new Long( -1);
            Iterator iterator = sdmItems.iterator();
            while (iterator.hasNext()) {
                SdmItem item = (SdmItem) iterator.next();

                if (max.compareTo(item.getParagraphOrder()) < 0) {
                    max = item.getParagraphOrder();
                }
            }
            return max;
        }
        else {
            return null;
        }
    }

    //---
    public static Long getMinParagraphNumber(Set sdmItems) {

        if (sdmItems != null) {
            Iterator iterator = sdmItems.iterator();
            while (iterator.hasNext()) {
                SdmItem item = (SdmItem) iterator.next();
                return item.getParagraphOrder();
            }
        }

        return null;
    }

    //------
    public static List loadDocumentItemsList(Sdm sdmDocument) {
        //
        Set<SdmItem> dbDocumentItemsList = sdmDocument.getItems();

        if ( (dbDocumentItemsList != null) && (dbDocumentItemsList.size() != 0)) {

            Long max = getMaxParagraphNumber(dbDocumentItemsList);
            Long min = getMinParagraphNumber(dbDocumentItemsList);

            List<SdmForm.SdmItemInfo> documentItemsList = new ArrayList<SdmForm.SdmItemInfo>();

            Iterator<SdmItem> iterator = dbDocumentItemsList.iterator();
            while (iterator.hasNext()) {
                SdmItem item = iterator.next();
                SdmForm.SdmItemInfo sdm_i = new SdmForm.SdmItemInfo();

                SdmParagraph paragraph = SdmCommon.createParagraph(item);
                int sdmItemType = -1;

                sdm_i.setId(sdmDocument.getId());
                //
                if (item.getRealType()!= null) {
                    if (item.getRealType().equals(SdmItem.TYPE_TEXT)) {
                        if (item.getContentText() != null)
                            paragraph.setContent(item.getContentText());
                    }
                } else {
                    if (item.getContentType().equals(SdmItem.TYPE_TEXT)) {
                        if (item.getContentText() != null)
                            paragraph.setContent(item.getContentText());
                    }
                }
                sdm_i.setContentTitle(SdmCommon.createParagraph(paragraph));

                //----
                if (paragraph.getAlign() != null) {
                    sdm_i.setAlignment(paragraph.getAlign());
                }
                else {
                    sdm_i.setAlignment(null);
                }

                //---HTML TEXT--
                if (item.getRealType()!= null) {
                    if (item.getRealType().equals(SdmItem.TYPE_HTML)) {
                        sdmItemType = 0;
                    }
                } else {
                    if (item.getContentType().equals(SdmItem.TYPE_HTML)) {
                        sdmItemType = 0;
                    }
                }
                //---TEXT--
                if (item.getRealType()!= null) {
                    if (item.getRealType().equals(SdmItem.TYPE_TEXT)) {
                        sdmItemType = 1;
                    }
                } else {
                    if (item.getContentType().equals(SdmItem.TYPE_TEXT)) {
                        sdmItemType = 1;
                    }
                }
                //---HYPER LINK--
                if (item.getRealType()!= null) {
                    if (item.getRealType().equals(SdmItem.TYPE_LINK)) {
                        sdmItemType = 2;
                    }
                } else {
                    if (item.getContentType().equals(SdmItem.TYPE_LINK)) {
                        sdmItemType = 2;
                    }
                }
                //---IMAGE--
                if (item.getRealType()!= null) {
                    if (item.getRealType().equals(SdmItem.TYPE_IMG)) {
                        sdmItemType = 3;
                    }
                } else {
                    if ( (item.getContentType().length() > 5) &&
                        (item.getContentType().substring(0, 5).
                         equals("image"))) {

                        sdmItemType = 3;
                    }
                }
                //---FILE--
                if (item.getRealType()!= null) {
                    if (item.getRealType().equals(SdmItem.TYPE_FILE)) {
                        sdmItemType = 4;
                    }
                } else {
                    if (sdmItemType < 0) {
                        sdmItemType = 4;
                    }
                }

                switch (sdmItemType) {
                    case 0 : {//HTML CODE
                        sdm_i.setHtmlCode(true);

                        sdm_i.setFile(false);
                        sdm_i.setText(false);
                        sdm_i.setLink(false);
                        sdm_i.setPicture(false);

                        if (item.getContentText() != null)
                            sdm_i.setContentTitle(item.getContentText());
                        break;
                    }
                    case 1: {//TEXT
                        sdm_i.setText(true);

                        sdm_i.setPicture(false);
                        sdm_i.setLink(false);
                        sdm_i.setFile(false);
                        sdm_i.setHtmlCode(false);
                        break;
                    }
                    case 2: { //HYPER LINK
                        sdm_i.setLink(true);

                        sdm_i.setHtmlCode(false);
                        sdm_i.setFile(false);
                        sdm_i.setText(false);
                        sdm_i.setPicture(false);

                        if (item.getContentText() != null)
                            sdm_i.setContent(item.getContentText());
                        break;
                    }
                    case 3: { //IMAGE
                        sdm_i.setPicture(true);

                        sdm_i.setLink(false);
                        sdm_i.setFile(false);
                        sdm_i.setHtmlCode(false);
                        sdm_i.setText(false);
                        break;
                    }
                    case 4: {//FILE
                        sdm_i.setFile(true);

                        sdm_i.setHtmlCode(false);
                        sdm_i.setText(false);
                        sdm_i.setLink(false);
                        sdm_i.setPicture(false);
                        break;
                    }
                    default: break;
                }

                //
                sdm_i.setParagraphOrder(item.getParagraphOrder());
                if (item.getParagraphOrder().equals(max)) {
                    sdm_i.setMoveUp(true);
                    sdm_i.setMoveDown(false);
                }
                if (item.getParagraphOrder().compareTo(max) < 0) {
                    sdm_i.setMoveUp(true);
                    sdm_i.setMoveDown(true);
                }
                if (item.getParagraphOrder().equals(min)) {
                    sdm_i.setMoveUp(false);
                    sdm_i.setMoveDown(true);
                }
                if (min.equals(max)) {
                    sdm_i.setMoveUp(false);
                    sdm_i.setMoveDown(false);
                }
                //-----


                documentItemsList.add(sdm_i);
            }
            return documentItemsList;
        }
        else {
            return null;
        }
    }

    public static SdmParagraph createParagraph(SdmForm formBean) {

        SdmParagraph paragraph = new SdmParagraph();

        //set alignment
        if (formBean.getAlignment() != null) {
            if (formBean.getAlignment().equals("1")) {
                paragraph.setAlign("left");
            }
            if (formBean.getAlignment().equals("2")) {
                paragraph.setAlign("center");
            }
            if (formBean.getAlignment().equals("3")) {
                paragraph.setAlign("right");
            }
        }
        //set font color
        paragraph.setFontColor(formBean.getSelectedFontColor());
        //set font size
        paragraph.setFontSize(formBean.getSelectedFontSize());
        //set font face
        paragraph.setFontFace(formBean.getSelectedFontFace());

        //set bold,italic,underline

        paragraph.setBold(formBean.isBold());
        paragraph.setItalic(formBean.isItalic());
        paragraph.setUnderline(formBean.isUnderline());

        paragraph.setContent(formBean.getContentTitle());

        return paragraph;
    }

    public static void setFormParagraph(SdmForm formBean,
                                        SdmParagraph paragraph) {

        // set alignment
        if (paragraph.getAlign() != null) {
            if (paragraph.getAlign().equals("left")) {
                formBean.setAlignment("1");
            }
            if (paragraph.getAlign().equals("center")) {
                formBean.setAlignment("2");
            }
            if (paragraph.getAlign().equals("right")) {
                formBean.setAlignment("3");
            }
        }

        //set font color
        if (paragraph.getFontColor() != null) {
            formBean.setSelectedFontColor(paragraph.getFontColor());
        }

        //set fon size
        if (paragraph.getFontSize() != null) {
            formBean.setSelectedFontSize(paragraph.getFontSize());
        }
        //@todo@ set font
        //set font face
        if (paragraph.getFontFace() != null) {
            formBean.setSelectedFontFace(paragraph.getFontFace());
        }

        //set bold,italic,undeline
        formBean.setBold(paragraph.isBold());
        formBean.setItalic(paragraph.isItalic());
        formBean.setUnderline(paragraph.isUnderline());
    }
}
