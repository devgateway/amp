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

package org.digijava.module.sdm.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SdmForm
    extends ActionForm {

  public static class SdmItemInfo {

    private Long paragraphOrder;
    private Long id;
    private String contentTitle;

    private String contentText;
    private String content;

    private boolean moveUp;
    private boolean moveDown;

    private boolean file;
    private boolean picture;
    private boolean link;
    private boolean text;
    private boolean htmlCode;

    //
    private String alignment;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Long getParagraphOrder() {
      return paragraphOrder;
    }

    public void setParagraphOrder(Long paragraphOrder) {
      this.paragraphOrder = paragraphOrder;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getContentText() {
      return contentText;
    }

    public void setContentText(String contentText) {
      this.contentText = contentText;
    }

    public String getContentTitle() {
      return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
      this.contentTitle = contentTitle;
    }

    public void setMoveUp(boolean moveUp) {
      this.moveUp = moveUp;
    }

    public boolean isMoveUp() {
      return moveUp;
    }

    public void setMoveDown(boolean moveDown) {
      this.moveDown = moveDown;
    }

    public boolean isMoveDown() {
      return moveDown;
    }

    public void setFile(boolean file) {
      this.file = file;
    }

    public boolean isFile() {
      return file;
    }

    public void setPicture(boolean picture) {
      this.picture = picture;
    }

    public boolean isPicture() {
      return picture;
    }

    public void setLink(boolean link) {
      this.link = link;
    }

    public boolean isLink() {
      return link;
    }

    public void setText(boolean text) {
      this.text = text;
    }

    public boolean isText() {
      return text;
    }

    public void setHtmlCode(boolean htmlCode) {
      this.htmlCode = htmlCode;
    }

    public boolean isHtmlCode() {
      return htmlCode;
    }

    public void setAlignment(String alignment) {
      this.alignment = alignment;
    }

    public String getAlignment() {
      return alignment;
    }
  }

  private Long activeDocumentId;
  private Sdm sdmDocument;

  private Long activeParagraphOrder;
  private SdmItem sdmItem;

  private List documentsList;
  private List documentItemsList;

  private String documentTitle;

  private String content;
  private String contentType;
  private String contentTitle;
  private FormFile formFile;

  private String selectedFontFace;
  private String selectedFontColor;
  private String selectedFontSize;

  private String alignment;

  private boolean bold;
  private boolean italic;
  private boolean underline;

  //
  private boolean itemsEmpty;
  //
  private boolean goToEdit;

  public String getAlignment() {
    return alignment;
  }

  public void setAlignment(String alignment) {
    this.alignment = alignment;
  }

  public boolean isBold() {
    return bold;
  }

  public void setBold(boolean bold) {
    this.bold = bold;
  }

  public boolean isItalic() {
    return italic;
  }

  public void setItalic(boolean italic) {
    this.italic = italic;
  }

  public String getSelectedFontColor() {
    return selectedFontColor;
  }

  public void setSelectedFontColor(String selectedFontColor) {
    this.selectedFontColor = selectedFontColor;
  }

  public String getSelectedFontSize() {
    return selectedFontSize;
  }

  public void setSelectedFontSize(String selectedFontSize) {
    this.selectedFontSize = selectedFontSize;
  }

  public boolean isUnderline() {
    return underline;
  }

  public void setUnderline(boolean underline) {
    this.underline = underline;
  }

  public List getDocumentsList() {
    return documentsList;
  }

  public void setDocumentsList(List documentsList) {
    this.documentsList = documentsList;
  }

  public FormFile getFormFile() {
    return formFile;
  }

  public void setFormFile(FormFile formFile) {
    this.formFile = formFile;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
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

  public String getDocumentTitle() {
    return documentTitle;
  }

  public void setDocumentTitle(String documentTitle) {
    this.documentTitle = documentTitle;
  }

  public boolean isItemsEmpty() {
    return itemsEmpty;
  }

  public void setItemsEmpty(boolean itemsEmpty) {
    this.itemsEmpty = itemsEmpty;
  }

  public List getDocumentItemsList() {
    return documentItemsList;
  }

  public void setDocumentItemsList(List documentItemsList) {
    this.documentItemsList = documentItemsList;
  }

  public Long getActiveParagraphOrder() {
    return activeParagraphOrder;
  }

  public void setActiveParagraphOrder(Long activeParagraphOrder) {
    this.activeParagraphOrder = activeParagraphOrder;
  }

  public Sdm getSdmDocument() {
    return sdmDocument;
  }

  public void setSdmDocument(Sdm sdmDocument) {
    this.sdmDocument = sdmDocument;
  }

  public SdmItem getSdmItem() {
    return sdmItem;
  }

  public void setSdmItem(SdmItem sdmItem) {
    this.sdmItem = sdmItem;
  }

  public String getSelectedFontFace() {
    return selectedFontFace;
  }

  public void setSelectedFontFace(String selectedFontFace) {
    this.selectedFontFace = selectedFontFace;
  }

  public boolean isGoToEdit() {
    return goToEdit;
  }

  public void setGoToEdit(boolean goToEdit) {
    this.goToEdit = goToEdit;
  }

  public Long getActiveDocumentId() {
    return activeDocumentId;
  }

  public void setActiveDocumentId(Long activeDocumentId) {
    this.activeDocumentId = activeDocumentId;
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {

    activeDocumentId = null;
    activeParagraphOrder = null;

    documentsList = null;
    documentTitle = null;
    content = null;
    contentTitle = null;
    contentType = null;
    formFile = null;
    alignment = "1";

    selectedFontFace = null;
    selectedFontColor = null;
    selectedFontSize = null;

    bold = false;
    italic = false;
    underline = false;
  }

}
