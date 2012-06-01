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

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CacheForm
    extends ActionForm {

    private List customCachesList;
    private int customCacheCount;
    private long customTotalCount;
    private String customKey;
    private List hibernateCachesList;
    private int hibernateCacheCount;
    private long hibernateTotalCount;
    private String hibernateKey;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        customKey = hibernateKey = null;
    }

    public int getCustomCacheCount() {
        return customCacheCount;
    }

    public List getCustomCachesList() {
        return customCachesList;
    }

    public long getCustomTotalCount() {

        return customTotalCount;
    }

    public int getHibernateCacheCount() {
        return hibernateCacheCount;
    }

    public List getHibernateCachesList() {
        return hibernateCachesList;
    }

    public long getHibernateTotalCount() {

        return hibernateTotalCount;
    }

    public String getCustomKey() {
        return customKey;
    }

    public String getHibernateKey() {
        return hibernateKey;
    }

    public void setCustomCacheCount(int customCacheCount) {
        this.customCacheCount = customCacheCount;
    }

    public void setCustomCachesList(List customCachesList) {
        this.customCachesList = customCachesList;
    }

    public void setCustomTotalCount(long customTotalCount) {

        this.customTotalCount = customTotalCount;
    }

    public void setHibernateCacheCount(int hibernateCacheCount) {
        this.hibernateCacheCount = hibernateCacheCount;
    }

    public void setHibernateCachesList(List hibernateCachesList) {
        this.hibernateCachesList = hibernateCachesList;
    }

    public void setHibernateTotalCount(long hibernateTotalCount) {

        this.hibernateTotalCount = hibernateTotalCount;
    }

    public void setCustomKey(String customKey) {
        this.customKey = customKey;
    }

    public void setHibernateKey(String hibernateKey) {
        this.hibernateKey = hibernateKey;
    }

}
