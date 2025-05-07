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

package org.digijava.module.calendar.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.module.calendar.dbentity.CalendarSettings;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

public class CalendarAdminForm
    extends ActionForm {

  public static class Item {

    private String value;

    public Item() {
    }

    public Item(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  public static class ItemView {

    private String code;
    private String name;

    public ItemView() {
    }

    public ItemView(String code, String name) {
      this.code = code;
      this.name = name;
    }

    public String getCode() {
      return this.code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

  }

  /**
   * instance of Calendar settings
   */
  private CalendarSettings setting;

  /**
   * collection containing numbers (up to ModuleInstance.NUMBER_OF_ITEMS_IN_TEASER value) to be selected as a  number of events visible in teaser
   */
  private Collection numberOfTeasers;

  /**
   * selected number of events appearing in teaser
   */
  private Long selectedNumOfItemsInTeaser;

  /**
   * number of charachters in visible title text of event item
   */
  private Long numOfCharsInTitle;

  /**
   * number of visible events per page
   */
  private Long numOfItemsPerPage;

  /**
   * collection with all possible Calendar view(i.e. List View,Month View, Year View)
   */
  private Collection defaultView;

  /**
   * The view selected as a default view for Calendar
   */
  private String selectedDefaultView;

  private String moduleName;
  private String instanceName;
  private String siteName;
  private String approve;
  private String reject;
  private String revoke;
  private String archive;

  public CalendarSettings getSetting() {
    return setting;
  }

  public void setSetting(CalendarSettings setting) {
    this.setting = setting;
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    if (setting != null) {
      setting.setModerated(false);
      setting.setPrivateItem(false);
      setting.setApproveMessage(null);
      setting.setSendApproveMessage(false);
      setting.setRejectMessage(null);
      setting.setSendRejectMessage(false);
      setting.setRevokeMessage(null);
      setting.setSendRevokeMessage(false);
    }

    setSelectedNumOfItemsInTeaser(new Long(ModuleInstance.
                                           NUMBER_OF_ITEMS_IN_TEASER));

    selectedDefaultView = null;
    numOfCharsInTitle = null;
    numOfItemsPerPage = null;

    setCalendarItemView();
  }

  public void setCalendarItemView() {
    defaultView = new ArrayList();

    defaultView.add(new ItemView(CalendarSettings.LIST_VIEW, "List View"));
    defaultView.add(new ItemView(CalendarSettings.MONTH_VIEW, "Month View"));
    defaultView.add(new ItemView(CalendarSettings.YEAR_VIEW, "Year View"));
  }

  public Collection getNumberOfTeasers() {
    return numberOfTeasers;
  }

  public void setNumberOfTeasers(Collection numberOfTeasers) {
    this.numberOfTeasers = numberOfTeasers;
  }

  public Long getSelectedNumOfItemsInTeaser() {
    return this.selectedNumOfItemsInTeaser;
  }

  public void setSelectedNumOfItemsInTeaser(Long selectedNumOfItemsInTeaser) {
    this.selectedNumOfItemsInTeaser = selectedNumOfItemsInTeaser;
  }

  public Collection getDefaultView() {
    return defaultView;
  }

  public void setDefaultView(Collection defaultView) {
    this.defaultView = defaultView;
  }

  public String getSelectedDefaultView() {
    return selectedDefaultView;
  }

  public void setSelectedDefaultView(String selectedDefaultView) {
    this.selectedDefaultView = selectedDefaultView;
  }

  public void setNumOfCharsInTitle(Long numOfCharsInTitle) {
    this.numOfCharsInTitle = numOfCharsInTitle;
  }

  public Long getNumOfItemsPerPage() {
    return numOfItemsPerPage;
  }

  public void setNumOfItemsPerPage(Long numOfItemsPerPage) {
    this.numOfItemsPerPage = numOfItemsPerPage;
  }

  public Long getNumOfCharsInTitle() {
    return numOfCharsInTitle;
  }

  public String getApprove() {
    return approve;
  }

  public void setApprove(String approve) {
    this.approve = approve;
  }

  public String getReject() {
    return reject;
  }

  public void setReject(String reject) {
    this.reject = reject;
  }

  public String getRevoke() {
    return revoke;
  }

  public void setRevoke(String revoke) {
    this.revoke = revoke;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public String getArchive() {
    return archive;
  }
  public void setArchive(String archive) {
    this.archive = archive;
  }
  public String getSiteName() {
    return siteName;
  }
  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }
  public String getInstanceName() {
    return instanceName;
  }
  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }
}
