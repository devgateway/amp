/*
 *   AdminPageForm.java
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

import org.apache.struts.action.ActionForm;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumSection;
import java.util.Map;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import java.util.List;
import org.digijava.module.forum.util.ForumManager;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import org.digijava.module.forum.util.ForumBaseForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Set;
import java.util.HashSet;

public class AdminPageForm
    extends ForumBaseForm {

  private boolean leaveShadow;
  private long moveToSectionId = 0;
  private int moveDirection;
  private List locationTrailItems;

  private static List gmtTimeZones;

  static {
    Calendar calendar = Calendar.getInstance();
    TimeZone zone = calendar.getTimeZone();
    String[] ids = zone.getAvailableIDs();
    gmtTimeZones = new ArrayList ();
          Set uniqueDisplayNameTest = new HashSet();
    for (int zoneIndex = 0; zoneIndex < ids.length; zoneIndex ++ ){
      if (uniqueDisplayNameTest.add(zone.getTimeZone(ids[zoneIndex]).getDisplayName())) {
        gmtTimeZones.add(zone.getTimeZone(ids[zoneIndex]));
      }
    }
  }


  public void reset(ActionMapping mapping,
                    HttpServletRequest request) {

  }

  public ActionErrors validate(ActionMapping mapping,
                               HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();

    if (mapping.getType() != null) {
      if (mapping.getType().equals(
          "org.digijava.module.forum.action.admin.SaveForum")) {
        if (this.getForumName() == null ||
            this.getForumName().trim().length() == 0) {
          errors.add("forumGlobalError",
                     new ActionError("error.forum.emptyForumName"));
        }
      }
      else if (mapping.getType().equals(
          "org.digijava.module.forum.action.admin.SaveForumSection")) {
        if (this.getSectionTitle() == null ||
            this.getSectionTitle().trim().length() == 0) {
          errors.add("forumGlobalError",
                     new ActionError("error.forum.emptySectionName"));
        }

      } else if (mapping.getType().equals(
          "org.digijava.module.forum.action.admin.SaveForumSubsection")) {
          if (this.getSubsectionTitle() == null ||
              this.getSubsectionTitle().trim().length() == 0) {
            errors.add("forumGlobalError",
                       new ActionError("error.forum.emptySubsectionName"));
          }


      } else if (mapping.getType().equals(
          "org.digijava.module.forum.action.admin.SaveForumThreads")) {

      }




    }
    return errors;
  }

  public int getMoveDirection() {
    return moveDirection;
  }

  public void setMoveDirection(int moveDirection) {
    this.moveDirection = moveDirection;
  }

  public boolean getIsNewPostInThread(int threadId) {
    return ForumManager.isNewPostInThread(threadId,
                                          getForumUserSettings(),
                                          getVisitedThreadsMap());
  }

  public boolean getLeaveShadow() {
    return leaveShadow;
  }

  public void setLeaveShadow(boolean leaveShadow) {
    this.leaveShadow = leaveShadow;
  }

  public long getMoveToSectionId() {
    return moveToSectionId;
  }

  public void setMoveToSectionId(long moveToSectionId) {
    this.moveToSectionId = moveToSectionId;
  }

  public List getLocationTrailItems() {
    return locationTrailItems;
  }

  public void setLocationTrailItems(List locationTrailItems) {
    this.locationTrailItems = locationTrailItems;
  }
  public List getGmtTimeZones() {
    return gmtTimeZones;
  }
}