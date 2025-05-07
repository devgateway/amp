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

package org.digijava.module.admin.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UnclosedSessionsForm
      extends ActionForm {

    public static class UnclosedSessionInfo {
    private Session key;
    private String value;
    private boolean closed;

    public void setKey(Session key) {
        this.key = key;
    }

    public Session getKey() {
        return key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isClosed() {
        return closed;
    }
    }

    private List unclosedSeesions;
    private long totalCount;
    private long totalOpened;
    private long totalClosed;

    private int index;

    private boolean showAll;

    public long getTotalCount() {
    return totalCount;
    }

    public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
    }

    public List getUnclosedSeesions() {
    return unclosedSeesions;
    }

    public void setUnclosedSeesions(List unclosedSeesions) {
    this.unclosedSeesions = unclosedSeesions;
    }

    public int getIndex() {
    return index;
    }

    public void setIndex(int index) {
    this.index = index;
    }

    public boolean isShowAll() {
    return showAll;
    }

    public void setShowAll(boolean showAll) {
    this.showAll = showAll;
    }

    public long getTotalClosed() {
    return totalClosed;
    }

    public void setTotalClosed(long totalClosed) {
    this.totalClosed = totalClosed;
    }

    public long getTotalOpened() {
    return totalOpened;
    }

    public void setTotalOpened(long totalOpened) {
    this.totalOpened = totalOpened;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    index = -1;
    totalCount = 0;
    totalOpened = 0;
    totalClosed = 0;
    }
}
