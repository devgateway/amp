/*
*   ForumAsset.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created:
*   CVS-ID: $Id: ForumAsset.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

package org.digijava.module.forum.dbentity;

import java.util.Date;

public class ForumAsset {
    private long id;
    private String srcFileName;
    private long fileSize;
    private byte[] fileContent;
    private long forumUserId;
    private Date creationDate;
    private String contentType;

    private ForumAsset() {
    }

    public ForumAsset(String srcFileName, byte[] fileContent) {
        this.srcFileName = srcFileName;
        this.fileContent = fileContent;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public byte[] getFileContent() {
        return fileContent;
    }
    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
    public long getFileSize() {
        return fileSize;
    }
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    public long getForumUserId() {
        return forumUserId;
    }
    public void setForumUserId(long forumUserId) {
        this.forumUserId = forumUserId;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getSrcFileName() {
        return srcFileName;
    }
    public void setSrcFileName(String srcFileName) {
        this.srcFileName = srcFileName;
    }
  public String getContentType() {
    return contentType;
  }
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }


}