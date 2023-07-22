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

package org.digijava.module.sdm.dbentity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import javax.persistence.*;

@Entity
@Table(name = "DG_SDM_ITEM")
public class SdmItem
    implements Serializable {

    public final static String TYPE_TEXT = "text";
    public final static String TYPE_LINK = "link";
    public final static String TYPE_HTML = "html";
    public final static String TYPE_FILE = "file";
    public final static String TYPE_IMG = "image";
    //
    @EmbeddedId
    private SdmItemKey id;

    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @Column(name = "ALIGNMENT")
    private String alignment;

    @Column(name = "FONT")
    private String font;

    @Column(name = "FONTSIZE")
    private Long fontSize;

    @Column(name = "FONTCOLOR")
    private String fontColor;

    @Column(name = "BOLD")
    private String bold;

    @Column(name = "ITALIC")
    private String italic;

    @Column(name = "UNDERLINE")
    private String underline;

    @Column(name = "CONTENT_TITLE")
    private String contentTitle;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private byte[] content;

    @Column(name = "CONTENT_TXT")
    private String contentText;

    @Column(name = "REAL_TYPE")
    private String realType;
    private Sdm document;
    private Long paragraphOrder;


    public SdmItem() {

    }
    public SdmItem(Sdm document,Long paragraphOrder) {
        this.document=document;
        this.paragraphOrder=paragraphOrder;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getParagraphOrder() {
        return paragraphOrder;
    }

    public void setParagraphOrder(Long paragraphOrder) {
        this.paragraphOrder = paragraphOrder;
    }

    public Sdm getDocument() {
        return document;
    }

    public void setDocument(Sdm document) {
        this.document = document;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getBold() {
        return bold;
    }

    public void setBold(String bold) {
        this.bold = bold;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public Long getFontSize() {
        return fontSize;
    }

    public void setFontSize(Long fontSize) {
        this.fontSize = fontSize;
    }

    public String getItalic() {
        return italic;
    }

    public void setItalic(String italic) {
        this.italic = italic;
    }

    public String getUnderline() {
        return underline;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    public String getRealType() {
        return realType;
    }

    public void setRealType(String realType) {
        this.realType = realType;
    }

    /**
     * This method is required for hibernate, because persistent class has
     * compound primary key
     * @param other Other instance
     * @return true, if objects have <b>equal primary keys</b>
     */
    public boolean equals(Object other) {
        if (! (other instanceof SdmItem))
            return false;
        SdmItem castOther = (SdmItem) other;
        return new EqualsBuilder()
            .append( (this.getDocument() != null ? this.getDocument().getId() : null),
                    castOther.getDocument() != null ?
                    castOther.getDocument().getId() : null)
            .append(this.paragraphOrder, castOther.paragraphOrder)
            .isEquals();
    }

    /**
     * This method is required for hibernate, because persistent class has
     * compound primary key
     * @return Hash Code for the object
     */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getDocument() != null ? this.getDocument().getId() : null)
            .append(this.paragraphOrder)
            .toHashCode();
    }

}
