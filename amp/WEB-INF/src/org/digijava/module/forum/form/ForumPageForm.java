/*
*   ForumPageForm.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created: Mar 15, 2004
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


package org.digijava.module.forum.form;

import java.util.List;

import org.digijava.module.forum.util.ForumManager;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import javax.servlet.http.HttpServletRequest;
public class ForumPageForm
    extends ForumBaseForm{


    private boolean isDigiUser;
    private boolean hasNewPm;
    private int pmCount;
    private int newPmCount;
    private int unreadPmCount;
    private List userPmList;
    private String moveToFolderName;
    private List locationTrailItems;
    private String parsedContent;
    private long quoteId;
    private long pmId;
    private long userId;
    private long jumpToSectionId;
    private long jumpToSubsectionId;
    private List userList;
    private long prevThreadId;
    private long nextThreadId;

    private int filterPostsFrom;
    private int sortOrder;


    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
      ActionErrors errors = new ActionErrors();

      if (mapping.getType() != null) {
        if (mapping.getType().equals(
            "org.digijava.module.forum.action.AddForumPost")) {

        }
        else if (mapping.getType().equals(
            "org.digijava.module.forum.action.SaveUserSettings")) {
          if (getNickName().trim().length() == 0) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.emptyUserNick"));
          }
        }
        else if (mapping.getType().equals(
            "org.digijava.module.forum.action.SaveForumThread")) {
          if (getThreadTitle().trim().length() == 0) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.emptyThreadTitle"));
          }
        }
        else if (mapping.getType().equals(
            "org.digijava.module.forum.action.SendPm")) {
          if (getPostTitle().trim().length() == 0) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.emptyPmTitle"));
          }
        }
      }

      return errors;
    }

    public boolean getIsNewPostInThread(int threadId) {
        return ForumManager.isNewPostInThread(threadId,
                                              getForumUserSettings(),
                                              getVisitedThreadsMap());
    }

    public boolean getIsNewPostInSubsection(int subsId) {
        return ForumManager.isNewPostInSubsection(subsId,
                                                  getForumUserSettings(),
                                                  getVisitedThreadsMap());
    }

    public boolean isLoggedIn() {
    return (getForumUserSettings() == null)?false:true;
}



    public boolean getIsDigiUser() {
        return isDigiUser;
    }
    public void setIsDigiUser(boolean isDigiUser) {
        this.isDigiUser = isDigiUser;
    }

    public boolean getHasNewPm() {
        return hasNewPm;
    }
    public void setHasNewPm(boolean hasNewPm) {
        this.hasNewPm = hasNewPm;
    }
    public int getPmCount() {
        return pmCount;
    }
    public void setPmCount(int pmCount) {
        this.pmCount = pmCount;
    }
  public List getUserPmList() {
    return userPmList;
  }
  public void setUserPmList(List userPmList) {
    this.userPmList = userPmList;
  }
  public int getNewPmCount() {
    return newPmCount;
  }
  public void setNewPmCount(int newPmCount) {
    this.newPmCount = newPmCount;
  }
  public int getUnreadPmCount() {
    return unreadPmCount;
  }
  public void setUnreadPmCount(int unreadPmCount) {
    this.unreadPmCount = unreadPmCount;
  }

    public List getLocationTrailItems() {
        return locationTrailItems;
    }
    public void setLocationTrailItems(List locationTrailItems) {
        this.locationTrailItems = locationTrailItems;
    }
    public String getMoveToFolderName() {
        return moveToFolderName;
    }
    public void setMoveToFolderName(String moveToFolderName) {
        this.moveToFolderName = moveToFolderName;
    }
    public String getParsedContent() {
        return parsedContent;
    }
    public void setParsedContent(String parsedContent) {
        this.parsedContent = parsedContent;
    }
    public long getQuoteId() {
        return quoteId;
    }
    public void setQuoteId(long quoteId) {
        this.quoteId = quoteId;
    }
    public long getPmId() {
        return pmId;
    }
    public void setPmId(long pmId) {
        this.pmId = pmId;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getJumpToSectionId() {
        return jumpToSectionId;
    }
    public void setJumpToSectionId(long jumpToSectionId) {
        this.jumpToSectionId = jumpToSectionId;
    }
    public long getJumpToSubsectionId() {
        return jumpToSubsectionId;
    }
    public void setJumpToSubsectionId(long jumpToSubsectionId) {
        this.jumpToSubsectionId = jumpToSubsectionId;
    }
    public List getUserList() {
        return userList;
    }
    public void setUserList(List userList) {
        this.userList = userList;
    }
    public long getPrevThreadId() {
        return prevThreadId;
    }
    public void setPrevThreadId(long prevThreadId) {
        this.prevThreadId = prevThreadId;
    }
    public long getNextThreadId() {
        return nextThreadId;
    }
    public void setNextThreadId(long nextThreadId) {
        this.nextThreadId = nextThreadId;
    }
    public int getFilterPostsFrom() {
        return filterPostsFrom;
    }
    public void setFilterPostsFrom(int filterPostsFrom) {
        this.filterPostsFrom = filterPostsFrom;
    }
    public int getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

}